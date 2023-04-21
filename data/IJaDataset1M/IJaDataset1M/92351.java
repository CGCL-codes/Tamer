package vmap.modes.mindmapmode;

import vmap.main.Vmap;
import vmap.main.Tools;
import vmap.main.VmapMain;
import vmap.main.XMLElement;
import vmap.modes.MindMapNode;
import vmap.modes.NodeAdapter;
import vmap.modes.MindIcon;
import ims.Identification;
import ims.LIPDescription;
import ims.LIPMedia;
import java.util.*;
import java.io.IOException;
import java.io.Writer;
import java.io.File;

/**
 * This class represents a single Node of a Tree. It contains direct handles 
 * to its parent and children and to its view.
 */
public class MindMapNodeModel extends NodeAdapter {

    public MindMapNodeModel(VmapMain frame) {
        super(frame);
        children = new LinkedList();
        setEdge(new MindMapEdgeModel(this, getFrame()));
    }

    public MindMapNodeModel(String title, VmapMain frame) {
        super(title, frame);
        children = new LinkedList();
        setEdge(new MindMapEdgeModel(this, getFrame()));
    }

    public String getStyle() {
        if (isFolded()) {
            return MindMapNode.STYLE_BUBBLE;
        } else {
            return super.getStyle();
        }
    }

    protected MindMapNode basicCopy() {
        return new MindMapNodeModel("freddy", getFrame());
    }

    public String saveHTML_escapeUnicodeAndSpecialCharacters(String text) {
        int len = text.length();
        StringBuffer result = new StringBuffer(len);
        int intValue;
        char myChar;
        boolean previousSpace = false;
        boolean spaceOccured = false;
        for (int i = 0; i < len; ++i) {
            myChar = text.charAt(i);
            intValue = (int) text.charAt(i);
            if (intValue > 128) {
                result.append("&#").append(intValue).append(';');
            } else {
                spaceOccured = false;
                switch(myChar) {
                    case '&':
                        result.append("&amp;");
                        break;
                    case '<':
                        result.append("&lt;");
                        break;
                    case '>':
                        result.append("&gt;");
                        break;
                    case ' ':
                        spaceOccured = true;
                        if (previousSpace) {
                            result.append("&nbsp;");
                        } else {
                            result.append(" ");
                        }
                        break;
                    case '\n':
                        result.append("\n<br>\n");
                        break;
                    default:
                        result.append(myChar);
                }
                previousSpace = spaceOccured;
            }
        }
        return result.toString();
    }

    ;

    public int saveHTML(Writer fileout, String parentID, int lastChildNumber, boolean isRoot, boolean treatAsParagraph, int depth) throws IOException {
        final String el = System.getProperty("line.separator");
        boolean basedOnHeadings = (getFrame().getProperty("html_export_folding").equals("html_export_based_on_headings"));
        boolean createFolding = isFolded();
        if (getFrame().getProperty("html_export_folding").equals("html_export_fold_all")) {
            createFolding = hasChildren();
        }
        if (getFrame().getProperty("html_export_folding").equals("html_export_no_folding") || basedOnHeadings || isRoot) {
            createFolding = false;
        }
        fileout.write(treatAsParagraph || basedOnHeadings ? "<p>" : "<li>");
        String localParentID = parentID;
        if (createFolding) {
            lastChildNumber++;
            localParentID = parentID + "_" + lastChildNumber;
            fileout.write("<span id=\"show" + localParentID + "\" class=\"foldclosed\" onClick=\"show_folder('" + localParentID + "')\" style=\"POSITION: absolute\">+</span> " + "<span id=\"hide" + localParentID + "\" class=\"foldopened\" onClick=\"hide_folder('" + localParentID + "')\">-</Span>");
            fileout.write("\n");
        }
        if (basedOnHeadings && hasChildren() && depth <= 5) {
            fileout.write("<h" + depth + ">");
        }
        if (getLink() != null) {
            String link = getLink();
            if (link.endsWith(".xml")) {
                link += ".html";
            }
            fileout.write("<a href=\"" + link + "\" target=\"_blank\"><span class=l>~</span>&nbsp;");
        }
        String fontStyle = "";
        if (color != null) {
            fontStyle += "color: " + Tools.colorToXml(getColor()) + ";";
        }
        if (font != null && font.getSize() != 0) {
            int defaultFontSize = Integer.parseInt(getFrame().getProperty("defaultfontsize"));
            int procentSize = (int) (getFont().getSize() * 100 / defaultFontSize);
            if (procentSize != 100) {
                fontStyle += "font-size: " + procentSize + "%;";
            }
        }
        if (font != null) {
            String fontFamily = getFont().getFamily();
            fontStyle += "font-family: " + fontFamily + ", sans-serif; ";
        }
        if (isItalic()) {
            fontStyle += "font-style: italic; ";
        }
        if (isBold()) {
            fontStyle += "font-weight: bold; ";
        }
        if (!fontStyle.equals("")) {
            fileout.write("<span style=\"" + fontStyle + "\">");
        }
        if (getFrame().getProperty("export_icons_in_html").equals("true")) {
            for (int i = 0; i < getIcons().size(); ++i) {
                fileout.write("<img src=\"" + ((MindIcon) getIcons().get(i)).getIconFileName() + "\" alt=\"" + ((MindIcon) getIcons().get(i)).getDescription(getFrame()) + "\">");
            }
        }
        if (this.toString().matches(" *")) {
            fileout.write("&nbsp;");
        } else if (this.toString().startsWith("<html>")) {
            String output = this.toString().substring(6);
            if (output.endsWith("</html>")) {
                output = output.substring(0, output.length() - 7);
            }
            fileout.write(output);
        } else {
            fileout.write(saveHTML_escapeUnicodeAndSpecialCharacters(toString()));
        }
        if (fontStyle != "") {
            fileout.write("</span>");
        }
        fileout.write(el);
        if (getLink() != null) {
            fileout.write("</a>" + el);
        }
        if (basedOnHeadings && hasChildren() && depth <= 5) {
            fileout.write("</h" + depth + ">");
        }
        boolean treatChildrenAsParagraph = false;
        for (ListIterator e = childrenUnfolded(); e.hasNext(); ) {
            if (((MindMapNodeModel) e.next()).toString().length() > 100) {
                treatChildrenAsParagraph = true;
                break;
            }
        }
        if (getFrame().getProperty("html_export_folding").equals("html_export_based_on_headings")) {
            for (ListIterator e = childrenUnfolded(); e.hasNext(); ) {
                MindMapNodeModel child = (MindMapNodeModel) e.next();
                lastChildNumber = child.saveHTML(fileout, parentID, lastChildNumber, false, treatChildrenAsParagraph, depth + 1);
            }
            return lastChildNumber;
        }
        if (hasChildren()) {
            if (getFrame().getProperty("html_export_folding").equals("html_export_based_on_headings")) {
                for (ListIterator e = childrenUnfolded(); e.hasNext(); ) {
                    MindMapNodeModel child = (MindMapNodeModel) e.next();
                    lastChildNumber = child.saveHTML(fileout, parentID, lastChildNumber, false, treatChildrenAsParagraph, depth + 1);
                }
            } else if (createFolding) {
                fileout.write("<ul id=\"fold" + localParentID + "\" style=\"POSITION: relative; VISIBILITY: visible;\">");
                if (treatChildrenAsParagraph) {
                    fileout.write("<li>");
                }
                int localLastChildNumber = 0;
                for (ListIterator e = childrenUnfolded(); e.hasNext(); ) {
                    MindMapNodeModel child = (MindMapNodeModel) e.next();
                    localLastChildNumber = child.saveHTML(fileout, localParentID, localLastChildNumber, false, treatChildrenAsParagraph, depth + 1);
                }
            } else {
                fileout.write("<ul>");
                if (treatChildrenAsParagraph) {
                    fileout.write("<li>");
                }
                for (ListIterator e = childrenUnfolded(); e.hasNext(); ) {
                    MindMapNodeModel child = (MindMapNodeModel) e.next();
                    lastChildNumber = child.saveHTML(fileout, parentID, lastChildNumber, false, treatChildrenAsParagraph, depth + 1);
                }
            }
            if (treatChildrenAsParagraph) {
                fileout.write("</li>");
            }
            fileout.write(el);
            fileout.write("</ul>");
        }
        if (!treatAsParagraph) {
            fileout.write(el + "</li>" + el);
        }
        return lastChildNumber;
    }

    public void saveTXT(Writer fileout, int depth) throws IOException {
        for (int i = 0; i < depth; ++i) {
            fileout.write("    ");
        }
        if (this.toString().matches(" *")) {
            fileout.write("o");
        } else {
            if (getLink() != null) {
                String link = getLink();
                if (!link.equals(this.toString())) {
                    fileout.write(this.toString() + " ");
                }
                fileout.write("<" + link + ">");
            } else {
                fileout.write(this.toString());
            }
        }
        fileout.write("\n");
        for (ListIterator e = childrenUnfolded(); e.hasNext(); ) {
            ((MindMapNodeModel) e.next()).saveTXT(fileout, depth + 1);
        }
    }

    public void collectColors(HashSet colors) {
        if (color != null) {
            colors.add(getColor());
        }
        for (ListIterator e = childrenUnfolded(); e.hasNext(); ) {
            ((MindMapNodeModel) e.next()).collectColors(colors);
        }
    }

    private String saveRFT_escapeUnicodeAndSpecialCharacters(String text) {
        int len = text.length();
        StringBuffer result = new StringBuffer(len);
        int intValue;
        char myChar;
        for (int i = 0; i < len; ++i) {
            myChar = text.charAt(i);
            intValue = (int) text.charAt(i);
            if (intValue > 128) {
                result.append("\\u").append(intValue).append("?");
            } else {
                switch(myChar) {
                    case '\\':
                        result.append("\\\\");
                        break;
                    case '{':
                        result.append("\\{");
                        break;
                    case '}':
                        result.append("\\}");
                        break;
                    case '\n':
                        result.append(" \\line ");
                        break;
                    default:
                        result.append(myChar);
                }
            }
        }
        return result.toString();
    }

    public void saveRTF(Writer fileout, int depth, HashMap colorTable) throws IOException {
        String pre = "{" + "\\li" + depth * 350;
        String level;
        if (depth <= 8) {
            level = "\\outlinelevel" + depth;
        } else {
            level = "";
        }
        String fontsize = "";
        if (color != null) {
            pre += "\\cf" + ((Integer) colorTable.get(getColor())).intValue();
        }
        if (isItalic()) {
            pre += "\\i ";
        }
        if (isBold()) {
            pre += "\\b ";
        }
        if (font != null && font.getSize() != 0) {
            fontsize = "\\fs" + Math.round(1.5 * getFont().getSize());
            pre += fontsize;
        }
        pre += "{}";
        fileout.write("\\li" + depth * 350 + level + "{}");
        if (this.toString().matches(" *")) {
            fileout.write("o");
        } else {
            String text = saveRFT_escapeUnicodeAndSpecialCharacters(this.toString());
            if (getLink() != null) {
                String link = saveRFT_escapeUnicodeAndSpecialCharacters(getLink());
                if (link.equals(this.toString())) {
                    fileout.write(pre + "<{\\ul\\cf1 " + link + "}>" + "}");
                } else {
                    fileout.write("{" + fontsize + pre + text + "} ");
                    fileout.write("<{\\ul\\cf1 " + link + "}}>");
                }
            } else {
                fileout.write(pre + text + "}");
            }
        }
        fileout.write("\\par");
        fileout.write("\n");
        for (ListIterator e = childrenUnfolded(); e.hasNext(); ) {
            ((MindMapNodeModel) e.next()).saveRTF(fileout, depth + 1, colorTable);
        }
    }

    public void save(Writer writer, MindMapMapModel model, boolean pres) throws IOException {
        XMLElement node = new XMLElement();
        XMLElement extObj = new XMLElement();
        XMLElement vmapMeta = new XMLElement();
        XMLElement xdesc = null;
        LIPDescription desc = new LIPDescription((String) getTitle(), (String) toString());
        node.setName(getNodeTypeName());
        if (isRoot()) {
            node.setAttribute("xmlns", "http://www.imsglobal.org/xsd/ims_ePortfoliov1p0");
            node.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
            node.setAttribute("xsi:schemaLocation", "http://www.imsglobal.org/xsd/ims_ePortfoliov1p0 ims_eportfoliov1p0.xsd");
        } else {
            extObj.setName(getNodeExtName());
            vmapMeta.setName("vmapmeta");
            extObj.addChild(vmapMeta);
            if (!(this.getAttach().equals(""))) {
                File attach = new File(this.getAttach());
                File xmlFile = getFrame().getController().getModel().getFile();
                if ((attach.getParent() != null) && (attach.getParent().equals(xmlFile.getParent()))) {
                    this.setAttach(attach.getName());
                    desc.addMediaURI(attach.getName());
                } else {
                    desc.addMediaURI(getAttach());
                }
            }
            xdesc = desc.getXML();
        }
        XMLElement edge = ((MindMapEdgeModel) getEdge()).save();
        if (edge != null) {
            vmapMeta.addChild(edge);
        }
        Vector linkVector = model.getLinkRegistry().getAllLinksFromMe(this);
        for (int i = 0; i < linkVector.size(); ++i) {
            if (linkVector.get(i) instanceof MindMapArrowLinkModel) {
                XMLElement arrowLinkElement = ((MindMapArrowLinkModel) linkVector.get(i)).save();
                vmapMeta.addChild(arrowLinkElement);
            }
        }
        if (isFolded()) {
            vmapMeta.setAttribute("folded", "true");
        }
        if ((isLeft() != null) && !(isRoot()) && (getParentNode().isRoot())) {
            vmapMeta.setAttribute("position", (isLeft().getValue()) ? "left" : "right");
        }
        String label = model.getLinkRegistry().getLabel(this);
        if (label != null) {
            vmapMeta.setAttribute("id", label);
        }
        if (color != null) {
            vmapMeta.setAttribute("color", Tools.colorToXml(getColor()));
        }
        if (style != null) {
            vmapMeta.setAttribute("style", super.getStyle());
        }
        if (getLink() != null) {
            vmapMeta.setAttribute("link", getLink());
        }
        if (font != null) {
            XMLElement fontElement = new XMLElement();
            fontElement.setName("font");
            if (font != null) {
                fontElement.setAttribute("name", font.getFamily());
            }
            if (font.getSize() != 0) {
                fontElement.setAttribute("size", Integer.toString(font.getSize()));
            }
            if (isBold()) {
                fontElement.setAttribute("bold", "true");
            }
            if (isItalic()) {
                fontElement.setAttribute("italic", "true");
            }
            if (isUnderlined()) {
                fontElement.setAttribute("underline", "true");
            }
            vmapMeta.addChild(fontElement);
        }
        if (getCloud() != null) {
            Identification id = ((MindMapNode) getParent()).getId();
            vmapMeta.setAttribute("cloudcolor", Tools.colorToXml(getCloud().getColor()));
            String tf = null;
            if (getVisible()) {
                tf = new String("true");
            } else {
                tf = new String("false");
            }
            vmapMeta.setAttribute("visible", tf);
            if (id != null) {
                node.addChild(id.getXML());
            }
        }
        if (xdesc != null) {
            node.addChild(xdesc);
        }
        node.addChild(extObj);
        if (getTypeName() != null) {
            XMLElement tn = new XMLElement();
            XMLElement tns = new XMLElement();
            XMLElement tnv = new XMLElement();
            tn.setName("typename");
            tns.setName("tysource");
            tns.setAttribute("sourcetype", "list");
            tns.setContent(getTypeName().getSourceListXML());
            tnv.setName("tyvalue");
            tnv.setContent(getTypeName().getValue());
            tn.addChild(tns);
            tn.addChild(tnv);
            node.addChild(tn);
        }
        if (createdate != null) {
            node.addChild(createdate.getXML());
        }
        if (updatedate != null) {
            node.addChild(updatedate.getXML());
        }
        if (childrenUnfolded().hasNext()) {
            node.writeWithoutClosingTag(writer);
            for (ListIterator e = childrenUnfolded(); e.hasNext(); ) {
                MindMapNodeModel child = (MindMapNodeModel) e.next();
                if ((!pres) || (child.getCloud() == null) || (child.getVisible())) {
                    child.save(writer, model, pres);
                }
            }
            node.writeClosingTag(writer);
        } else {
            node.write(writer);
        }
    }

    private String getNodeTypeName() {
        String[] res = null;
        if (isRoot()) {
            return (new String("portfolio"));
        } else if (getCloud() != null) {
            return (new String("portfolioPart"));
        } else if (getIcons().size() >= 1) {
            res = Tools.nodeTypeToXML(((MindIcon) getIcons().get(0)).getName());
            return (res[0]);
        } else {
            return (new String("activity"));
        }
    }

    private String getNodeExtName() {
        String[] res = null;
        if (isRoot()) {
            return (null);
        } else if (getIcons().size() >= 1) {
            res = Tools.nodeTypeToXML(((MindIcon) getIcons().get(0)).getName());
            return (res[1]);
        } else {
            return (new String("ext_portfolio"));
        }
    }
}
