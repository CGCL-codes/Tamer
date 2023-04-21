package net.community.chest.eclipse.wst;

import java.io.IOException;
import java.io.StreamCorruptedException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.xml.transform.TransformerException;
import net.community.chest.dom.DOMUtils;
import net.community.chest.eclipse.AbstractEclipseFileTransformer;
import net.community.chest.io.FileUtil;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * <P>Copyright GPLv2</P>
 *
 * @author Lyor G.
 * @since Apr 26, 2009 2:24:30 PM
 */
public class WstComponentsFileTransformer extends AbstractEclipseFileTransformer {

    public WstComponentsFileTransformer() {
        super();
    }

    public Writer writeRootElement(Element root, final Writer w) throws TransformerException, IOException {
        if ((null == root) || (null == w)) throw new TransformerException("transformRootElement() no " + Element.class.getSimpleName() + "/" + Writer.class.getSimpleName() + " instance(s)");
        if (isValidatingElementName()) {
            final String tagName = root.getTagName();
            if (!WstUtils.PROJ_MODULES_ELEM_NAME.equals(tagName)) throw new TransformerException("transformRootElement(" + tagName + ") bad " + Element.class.getSimpleName() + " name - expected " + WstUtils.PROJ_MODULES_ELEM_NAME);
        }
        return writeElementData(root, w, false, false);
    }

    @Override
    public Writer writeElementData(Element elem, Writer org, boolean followChildren, boolean closeIt, CharSequence indent) throws IOException {
        final String tagName = elem.getTagName();
        if (WstUtils.DEPMODULE_ELEM_NAME.equalsIgnoreCase(tagName)) {
            Writer w = super.writeElementData(elem, org, false, false, indent);
            final Collection<? extends Element> chList = DOMUtils.extractAllNodes(Element.class, elem, Node.ELEMENT_NODE);
            if ((chList != null) && (chList.size() > 0)) {
                for (final Element se : chList) w = writeElementData(se, w, true, true, indent);
            }
            if (closeIt) return FileUtil.writeln(w, indent + "</" + tagName + ">"); else return w;
        }
        if (WstUtils.DEPTYPE_ELEM_NAME.equalsIgnoreCase(tagName)) {
            final String val = DOMUtils.getElementStringValue(elem);
            if ((null == val) || (val.length() <= 0)) throw new StreamCorruptedException("writeElementData(" + DOMUtils.toString(elem) + ") missing value");
            return FileUtil.writeln(org, indent + "\t<" + tagName + ">" + val + "</" + tagName + ">");
        }
        return super.writeElementData(elem, org, followChildren, closeIt, indent);
    }

    public Writer transformModuleEntry(Element elem, final Writer w) throws TransformerException, IOException {
        if ((null == elem) || (null == w)) throw new TransformerException("transformModuleEntry() no " + Element.class.getSimpleName() + "/" + Writer.class.getSimpleName() + " instance(s)");
        return writeElementData(elem, w, true, true, "\t\t");
    }

    public Writer transformDependencyModules(final List<Element> el, final Writer org) throws TransformerException, IOException {
        final int numElems = (null == el) ? 0 : el.size();
        if (numElems <= 0) return org;
        if (numElems > 1) Collections.sort(el, WstDepModuleEntryComparator.ASCENDING);
        Writer w = org;
        for (final Element elem : el) w = transformModuleEntry(elem, w);
        return w;
    }

    public Writer transformDependencyModules(NodeList nodes, Writer org) throws TransformerException, IOException {
        final int numNodes = (null == nodes) ? 0 : nodes.getLength();
        if (numNodes <= 0) return org;
        Writer w = org;
        List<Element> ml = null;
        for (int nIndex = 0; nIndex < numNodes; nIndex++) {
            final Node n = nodes.item(nIndex);
            if ((null == n) || (n.getNodeType() != Node.ELEMENT_NODE)) continue;
            final Element elem = (Element) n;
            final String tagName = elem.getTagName();
            if (WstUtils.DEPMODULE_ELEM_NAME.equalsIgnoreCase(tagName)) {
                if (null == ml) ml = new ArrayList<Element>(numNodes);
                ml.add(elem);
                continue;
            }
            w = transformModuleEntry(elem, w);
        }
        return transformDependencyModules(ml, w);
    }

    public Writer transformDependencyModules(Element root, Writer org) throws TransformerException, IOException {
        Writer w = writeRootElement(root, org);
        final Collection<? extends Element> el = DOMUtils.extractAllNodes(Element.class, root, Node.ELEMENT_NODE);
        if ((el != null) && (el.size() > 0)) {
            for (final Element elem : el) {
                final String tagName = (null == elem) ? null : elem.getTagName();
                if (WstUtils.WEB_MODULE_ELEM_NAME.equalsIgnoreCase(tagName)) {
                    w = writeElementData(elem, w, false, false, "\t");
                    w = transformDependencyModules(elem.getChildNodes(), w);
                    w = FileUtil.writeln(w, "\t</" + tagName + ">");
                }
            }
        }
        final String tagName = root.getTagName();
        w = FileUtil.writeln(w, "</" + tagName + ">");
        return w;
    }

    @Override
    public Writer transformRootElement(Element root, Writer org) throws TransformerException, IOException {
        if (isSortedOutputEntries()) return transformDependencyModules(root, org); else return writeElementData(root, org, true, true, "\t");
    }

    public static final WstComponentsFileTransformer DEFAULT = new WstComponentsFileTransformer();
}
