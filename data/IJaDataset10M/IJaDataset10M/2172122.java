package net.sf.wug.dev;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import net.sf.wug.Writable;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class TemplateEngine {

    static final String WUG_NS = "http://wug.sf.net/2007a";

    Processor processor;

    TemplateEngine() {
        Processor[] processors = new Processor[] { new ElementProcessor(), new LabelProcessor(), new ChildrenProcessor() };
        for (int i = 0; i < processors.length - 1; i++) {
            processors[i].setNext(processors[i + 1]);
        }
        processor = processors[0];
    }

    public Writable process(Document document, Class<?> modelType, String viewName) {
        ViewCode view = new ViewCode(modelType, viewName);
        processChildren(document, view);
        view.flush();
        return view;
    }

    void processChildren(Node node, ViewCode view) {
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node child = childNodes.item(i);
            switch(child.getNodeType()) {
                case Node.ELEMENT_NODE:
                    processor.process((Element) child, view);
                    break;
                case Node.PROCESSING_INSTRUCTION_NODE:
                    break;
                case Node.TEXT_NODE:
                    String text = child.getNodeValue();
                    text = text.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
                    view.addPlain("%s", text);
                    break;
                default:
                    throw new IllegalStateException();
            }
        }
    }

    abstract static class Processor {

        Processor next;

        void setNext(Processor next) {
            this.next = next;
        }

        protected abstract boolean skip(Element element, ViewCode view);

        void process(Element element, ViewCode view) {
            Processor current = next;
            while (current.skip(element, view)) {
                current = current.next;
            }
            current.process(element, view);
        }
    }

    class ChildrenProcessor extends Processor {

        protected boolean skip(Element element, ViewCode view) {
            return false;
        }

        void process(Element element, ViewCode view) {
            processChildren(element, view);
        }
    }

    static class ElementProcessor extends Processor {

        protected boolean skip(Element element, ViewCode view) {
            return false;
        }

        void process(Element element, ViewCode view) {
            view.addPlain("<%s", element.getNodeName());
            Object key = view.putGuard(">");
            super.process(element, view);
            if (view.cancelGuard(key)) {
                view.addPlain("/>");
            } else {
                view.addPlain("</%s>", element.getNodeName());
            }
        }
    }

    static class LabelProcessor extends Processor {

        protected boolean skip(Element element, ViewCode view) {
            return !element.hasAttributeNS(WUG_NS, "label");
        }

        void process(Element element, ViewCode view) {
            String expr = element.getAttributeNS(WUG_NS, "label");
            view.addWrite(expr);
        }
    }

    public static void main(String[] arguments) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(arguments[1]);
        Element documentElement = document.getDocumentElement();
        String root = documentElement.getAttributeNS(WUG_NS, "root");
        Class<?> modelType = Class.forName(root);
        int n = root.lastIndexOf('.');
        String modelName;
        String modelPackage;
        if (n != -1) {
            modelName = root.substring(n + 1);
            modelPackage = root.substring(0, n);
        } else {
            modelName = root;
            modelPackage = "";
        }
        String viewPackage;
        if (modelPackage.equals("")) {
            viewPackage = "net.sf.wug.tmp";
        } else {
            viewPackage = "net.sf.wug.tmp" + "." + modelPackage;
        }
        String viewName = modelName + "View";
        TemplateEngine templateEngine = new TemplateEngine();
        Writable viewCode = templateEngine.process(document, modelType, viewName);
        File dir = new File(arguments[0]);
        write(dir, viewPackage, viewName, viewCode);
    }

    static File getDirectoryForPackage(File dir, String packageName) {
        int n = packageName.indexOf('.');
        if (n == -1) {
            return new File(dir, packageName);
        }
        File child = new File(dir, packageName.substring(0, n));
        return getDirectoryForPackage(child, packageName.substring(n + 1));
    }

    static void write(File base, String packageName, String className, Writable writable) throws IOException {
        File dir = getDirectoryForPackage(base, packageName);
        dir.mkdirs();
        File file = new File(dir, className + ".java");
        Writer writer = new OutputStreamWriter(new FileOutputStream(file));
        try {
            writer.write("package ");
            writer.write(packageName);
            writer.write(";");
            writable.write(writer);
            writer.flush();
        } finally {
            writer.close();
        }
    }
}
