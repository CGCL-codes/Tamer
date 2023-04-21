package conga.io.format;

import conga.CongaRuntimeException;
import conga.io.Source;
import conga.param.Parameter;
import conga.param.ParameterImpl;
import conga.param.tree.ParamList;
import conga.param.tree.ParamListImpl;
import conga.param.tree.Tree;
import conga.param.tree.TreeIterationHandler;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import javax.xml.namespace.QName;
import java.util.List;

/** @author Justin Caballero */
public class DefaultXmlFormat extends AbstractXmlFormat {

    public static final String CONGA_NS = "http://congaconfig.org";

    public static final QName ROOT_ELEM = new QName(CONGA_NS, "configuration");

    static final org.dom4j.QName ROOT_ELEM_JDOM = org.dom4j.QName.get(ROOT_ELEM.getLocalPart(), "conga", ROOT_ELEM.getNamespaceURI());

    static final org.dom4j.QName COMMENT_QNAME_JDOM = org.dom4j.QName.get("comment", "conga", CONGA_NS);

    protected void doRead(Document doc, Tree tree, Source source) {
        Element root = doc.getRootElement();
        if (!root.getQName().equals(ROOT_ELEM_JDOM)) {
            throw new CongaRuntimeException("Root element must be " + ROOT_ELEM_JDOM + ", not " + root.getQName());
        }
        ParamListImpl list = new ParamListImpl();
        tree.add(list);
        readRecurse(new ReadContext(list, root, "", source));
    }

    private static void readRecurse(ReadContext ctx) {
        for (Element kid : (List<Element>) ctx.elem.elements()) {
            if (kid.getNamespaceURI().equals(CONGA_NS)) {
                if (kid.getQName().equals(COMMENT_QNAME_JDOM)) {
                    ctx.nextElemComment = kid.getTextTrim();
                }
            } else {
                ReadContext deeper = readSingleElem(kid, ctx);
                ctx.nextElemComment = null;
                readRecurse(deeper);
                ctx.count = deeper.count;
            }
        }
        ctx.nextElemComment = null;
    }

    /** Reads the text content then attributes of a single element */
    private static ReadContext readSingleElem(Element kid, ReadContext ctx) {
        if (!StringUtils.isBlank(kid.getText())) {
            ctx.list.getBackingList().add(new ParameterImpl(ctx.prefix + kid.getName(), kid.getTextTrim(), ctx.source, ctx.nextElemComment, ctx.count++));
        }
        String newPrefix = ctx.prefix + kid.getName() + Parameter.PART_SEPARATOR;
        for (Attribute att : (List<Attribute>) kid.attributes()) {
            if (!StringUtils.isBlank(att.getText())) {
                ctx.list.getBackingList().add(new ParameterImpl(newPrefix + att.getName(), att.getValue().trim(), ctx.source, null, ctx.count++));
            }
        }
        return new ReadContext(ctx, kid, newPrefix);
    }

    /** Parameter Object (the design pattern that is) used while recursing */
    private static class ReadContext {

        ParamListImpl list;

        Element elem;

        String prefix;

        Source source;

        String nextElemComment;

        int count;

        public ReadContext(ReadContext copy, Element elem, String prefix) {
            this.elem = elem;
            this.prefix = prefix;
            list = copy.list;
            source = copy.source;
            count = copy.count;
        }

        @SuppressWarnings({ "SameParameterValue" })
        public ReadContext(ParamListImpl list, Element elem, String prefix, Source source) {
            this.list = list;
            this.elem = elem;
            this.prefix = prefix;
            this.source = source;
        }
    }

    public Document writeToDom4j(Tree tree) {
        Document doc = DocumentHelper.createDocument();
        final Element rootElem = doc.addElement(ROOT_ELEM_JDOM);
        tree.traverse(new TreeIterationHandler.NoOp() {

            public void process(ParamList list) {
                for (Parameter p : list.getParams()) {
                    Element elem = rootElem;
                    String[] elemNames = p.getName().split("\\" + Parameter.PART_SEPARATOR);
                    for (int i = 0; i < elemNames.length - 1; i++) {
                        String name = elemNames[i];
                        Element child = elem.element(name);
                        if (child == null) {
                            child = elem.addElement(name);
                        }
                        elem = child;
                    }
                    if (p.hasComment()) {
                        elem.addElement(COMMENT_QNAME_JDOM).setText(p.getComment());
                    }
                    elem.addElement(elemNames[elemNames.length - 1]).setText(p.getValue());
                }
            }
        });
        return doc;
    }
}
