package jacg.config;

import jacg.CBGCompilerFactory;
import jacg.CompiledJavaTemplate;
import jacg.Configuration;
import jacg.EmbeddedJavaGenerator;
import jacg.FileTemplate;
import jacg.FileUtil;
import jacg.Generator;
import jacg.StringTemplate;
import jacg.Template;
import org.w3c.dom.DOMException;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class GeneratorCP extends AbstractConfigurationProcessor {

    private String directory = "";

    ;

    public GeneratorCP() {
        NAME = "generator";
    }

    void processInternal(Node node) {
        Generator g = readGenerator(node);
        String stereotype = node.getAttributes().getNamedItem("stereotype").getNodeValue();
        Configuration.addGenerator(stereotype, g);
    }

    public Generator readGenerator(Node node) {
        Generator g = new Generator();
        NamedNodeMap attributes = node.getAttributes();
        String outputpath = attributes.getNamedItem("outputPath").getNodeValue();
        String roottemplate = attributes.getNamedItem("rootTemplate").getNodeValue();
        Node typeNode = attributes.getNamedItem("type");
        if (typeNode != null) {
            String type = typeNode.getNodeValue();
            g.setType(type);
        }
        g.setOutputPath(outputpath);
        g.setRootTemplateName(roottemplate);
        NodeList childNodes = node.getChildNodes();
        if (childNodes.getLength() > 0) {
            for (int i = 0; i < childNodes.getLength(); i++) {
                Node item = childNodes.item(i);
                if ("templates".equalsIgnoreCase(item.getLocalName())) {
                    processTemplates(item, g);
                } else if ("codeBlockGenerator".equalsIgnoreCase(item.getLocalName())) {
                    processCodeBlockGenerator(item, g);
                } else {
                    if (item.getNodeType() == Node.ELEMENT_NODE) hasErrors = true;
                }
            }
        }
        return g;
    }

    private void processCodeBlockGenerator(Node item, Generator g) {
        NamedNodeMap attributes = item.getAttributes();
        Node nameNode = attributes.getNamedItem("name");
        Node classNode = attributes.getNamedItem("class");
        if (nameNode != null && classNode != null) {
            try {
                g.addCodeBlockGenerator(nameNode.getNodeValue(), classNode.getNodeValue());
            } catch (DOMException e) {
                e.printStackTrace();
                hasErrors = true;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                hasErrors = true;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                hasErrors = true;
            } catch (InstantiationException e) {
                e.printStackTrace();
                hasErrors = true;
            }
        }
    }

    private void processTemplates(Node node, Generator g) {
        Node directoryNode = node.getAttributes().getNamedItem("directory");
        if (directoryNode != null) {
            directory = directoryNode.getNodeValue() + FileUtil.FILE_SEPARATOR;
        } else {
            hasErrors = true;
        }
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node item = childNodes.item(i);
            if ("template".equalsIgnoreCase(item.getLocalName())) {
                try {
                    processOneTemplate(item, g);
                } catch (Exception e) {
                    System.err.println("Fehler: Konnte Template nicht verarbeiten.");
                    e.printStackTrace();
                }
            } else if ("default".equalsIgnoreCase(item.getLocalName())) {
                processTemplateDefault(item, g);
            }
        }
    }

    private void processOneTemplate(Node item, Generator g) throws Exception {
        Node fileNode = item.getAttributes().getNamedItem("file");
        Node contentNode = item.getAttributes().getNamedItem("content");
        Node nameNode = item.getAttributes().getNamedItem("name");
        String name = null;
        if (nameNode != null) name = nameNode.getNodeValue();
        if (contentNode != null && nameNode != null) {
            StringTemplate t = new StringTemplate(contentNode.getNodeValue());
            g.addCodeBlockGenerator(nameNode.getNodeValue(), t);
        } else if (fileNode != null) {
            Template template = null;
            String file = fileNode.getNodeValue();
            if (file.endsWith(".ejt") || file.endsWith(".EJT")) {
                file = directory + file;
                EmbeddedJavaGenerator ejg = (EmbeddedJavaGenerator) EmbeddedJavaGenerator.compileEJT(file);
                template = ejg;
                if (name == null || "".equals(name)) {
                    name = ejg.__getHookName();
                }
                g.addCodeBlockGenerator(name, ejg);
            } else if (file.endsWith(".java")) {
                CompiledJavaTemplate cjg;
                if (Configuration.isFromClasspath()) {
                    String className = CBGCompilerFactory.getCBGCompilerInstance().convertToClassName(file);
                    cjg = (CompiledJavaTemplate) Class.forName(className).newInstance();
                } else {
                    file = CompiledJavaTemplate.prepareForCompile(directory, file);
                    cjg = compileJava(Configuration.getCompilerConfig().getOutputPath(), file);
                }
                if (name == null || "".equals(name)) {
                    cjg.setHookName(cjg.toHookName(file));
                } else {
                    cjg.setHookName(name);
                }
                g.addCodeBlockGenerator(name, cjg);
                name = cjg.getHookName();
                template = cjg;
            } else {
                template = new FileTemplate(directory, file);
            }
            if (name != null) {
                g.addTemplate(name, template);
            } else {
                hasErrors = true;
            }
        } else {
            hasErrors = true;
        }
    }

    private CompiledJavaTemplate compileJava(String dir, String fileName) throws Exception {
        return (CompiledJavaTemplate) CBGCompilerFactory.getCBGCompilerInstance().compileJavaFile(dir, fileName);
    }

    private void processTemplateDefault(Node item, Generator g) {
        Node fileNode = item.getAttributes().getNamedItem("file");
        if (fileNode != null) {
            g.setRootTemplate(new FileTemplate(directory + fileNode.getNodeValue()));
        }
    }
}
