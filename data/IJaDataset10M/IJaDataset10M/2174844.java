package writer2latex.xhtml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import writer2latex.office.*;
import writer2latex.util.*;

/**
 * <p>This class converts OpenDocument styles to CSS2 styles.</p>
 * <p>Note that some elements in OpenDocument has attributes that also maps
 * to CSS2 properties. Example: the width of a text box.</p>
 * <p>Also note, that some OpenDocument style properties cannot be mapped to
 * CSS2 without creating an additional inline element.</p>
 * <p>The class uses one helper class per OpenDocument style family
 * (paragraph, frame etc.)</p>
 */
class StyleConverter extends ConverterHelper {

    private TextStyleConverter textSc;

    private ParStyleConverter parSc;

    private HeadingStyleConverter headingSc;

    private ListStyleConverter listSc;

    private SectionStyleConverter sectionSc;

    private TableStyleConverter tableSc;

    private RowStyleConverter rowSc;

    private CellStyleConverter cellSc;

    private FrameStyleConverter frameSc;

    private PresentationStyleConverter presentationSc;

    private PageStyleConverter pageSc;

    /** <p>Create a new <code>StyleConverter</code></p>
     */
    public StyleConverter(OfficeReader ofr, XhtmlConfig config, Converter converter, int nType) {
        super(ofr, config, converter);
        textSc = new TextStyleConverter(ofr, config, converter, nType);
        parSc = new ParStyleConverter(ofr, config, converter, nType);
        headingSc = new HeadingStyleConverter(ofr, config, converter, nType);
        listSc = new ListStyleConverter(ofr, config, converter, nType);
        sectionSc = new SectionStyleConverter(ofr, config, converter, nType);
        tableSc = new TableStyleConverter(ofr, config, converter, nType);
        rowSc = new RowStyleConverter(ofr, config, converter, nType);
        cellSc = new CellStyleConverter(ofr, config, converter, nType);
        frameSc = new FrameStyleConverter(ofr, config, converter, nType);
        presentationSc = new PresentationStyleConverter(ofr, config, converter, nType);
        pageSc = new PageStyleConverter(ofr, config, converter, nType);
    }

    protected TextStyleConverter getTextSc() {
        return textSc;
    }

    protected ParStyleConverter getParSc() {
        return parSc;
    }

    protected HeadingStyleConverter getHeadingSc() {
        return headingSc;
    }

    protected ListStyleConverter getListSc() {
        return listSc;
    }

    protected SectionStyleConverter getSectionSc() {
        return sectionSc;
    }

    protected TableStyleConverter getTableSc() {
        return tableSc;
    }

    protected RowStyleConverter getRowSc() {
        return rowSc;
    }

    protected CellStyleConverter getCellSc() {
        return cellSc;
    }

    protected FrameStyleConverter getFrameSc() {
        return frameSc;
    }

    protected PresentationStyleConverter getPresentationSc() {
        return presentationSc;
    }

    protected PageStyleConverter getPageSc() {
        return pageSc;
    }

    private StyleWithProperties getDefaultStyle() {
        if (ofr.isSpreadsheet()) return ofr.getDefaultCellStyle(); else if (ofr.isPresentation()) return ofr.getDefaultFrameStyle(); else return ofr.getDefaultParStyle();
    }

    public void applyDefaultLanguage(Element node) {
        StyleWithProperties style = getDefaultStyle();
        if (style != null) {
            StyleInfo info = new StyleInfo();
            StyleConverterHelper.applyLang(style, info);
            applyStyle(info, node);
        }
    }

    public String exportStyles(boolean bIndent) {
        String sIndent = bIndent ? "      " : "";
        StringBuffer buf = new StringBuffer();
        if (config.xhtmlCustomStylesheet().length() == 0 && (config.xhtmlFormatting() == XhtmlConfig.CONVERT_ALL || config.xhtmlFormatting() == XhtmlConfig.IGNORE_HARD)) {
            StyleWithProperties defaultStyle = getDefaultStyle();
            if (defaultStyle != null) {
                CSVList props = new CSVList(";");
                getTextSc().cssTextCommon(defaultStyle, props, true);
                if (config.useDefaultFont() && config.defaultFontName().length() > 0) {
                    props.addValue("font-family", "'" + config.defaultFontName() + "'");
                }
                buf.append(sIndent).append("body {").append(props.toString()).append("}").append(config.prettyPrint() ? "\n" : " ");
            }
        }
        buf.append(getTextSc().getStyleDeclarations(sIndent));
        buf.append(getParSc().getStyleDeclarations(sIndent));
        buf.append(getHeadingSc().getStyleDeclarations(sIndent));
        buf.append(getListSc().getStyleDeclarations(sIndent));
        buf.append(getSectionSc().getStyleDeclarations(sIndent));
        buf.append(getCellSc().getStyleDeclarations(sIndent));
        buf.append(getTableSc().getStyleDeclarations(sIndent));
        buf.append(getRowSc().getStyleDeclarations(sIndent));
        buf.append(getFrameSc().getStyleDeclarations(sIndent));
        buf.append(getPresentationSc().getStyleDeclarations(sIndent));
        buf.append(getPageSc().getStyleDeclarations(sIndent));
        return buf.toString();
    }

    public Node exportStyles(Document htmlDOM) {
        String sStyles = exportStyles(config.prettyPrint());
        if (sStyles.length() > 0) {
            Element htmlStyle = htmlDOM.createElement("style");
            htmlStyle.setAttribute("media", "all");
            htmlStyle.setAttribute("type", "text/css");
            htmlStyle.appendChild(htmlDOM.createTextNode(config.prettyPrint() ? "\n" : " "));
            htmlStyle.appendChild(htmlDOM.createTextNode(sStyles));
            if (config.prettyPrint()) {
                htmlStyle.appendChild(htmlDOM.createTextNode("    "));
            }
            return htmlStyle;
        } else {
            return null;
        }
    }
}
