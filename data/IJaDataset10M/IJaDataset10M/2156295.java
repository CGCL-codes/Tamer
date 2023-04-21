package writer2latex.latex.style;

import java.util.Hashtable;
import writer2latex.util.*;
import writer2latex.office.*;
import writer2latex.latex.LaTeXDocumentPortion;
import writer2latex.latex.HeadingMap;
import writer2latex.latex.ConverterPalette;
import writer2latex.latex.Context;

public class ParStyleConverter extends StyleConverter {

    private String[] sHeadingStyles = new String[11];

    /**
     * <p>
     * Constructs a new <code>ParStyleConverter</code>.
     * </p>
     */
    public ParStyleConverter(OfficeReader ofr, Config config, ConverterPalette palette) {
        super(ofr, config, palette);
    }

    public void appendDeclarations(LaTeXDocumentPortion pack, LaTeXDocumentPortion decl) {
        if (config.formatting() >= Config.CONVERT_MOST) {
            decl.append("\\raggedbottom").nl();
        }
        appendHeadingStyles(decl);
        if (config.formatting() >= Config.CONVERT_MOST) {
            decl.append("% Paragraph styles").nl();
            palette.getCharSc().applyDefaultFont(ofr.getDefaultParStyle(), decl);
            super.appendDeclarations(pack, decl);
        }
    }

    /**
     * <p>
     * Use a paragraph style in LaTeX.
     * </p>
     * 
     * @param <code>sName</code> the name of the text style
     * @param <code>ba</code> a <code>BeforeAfter</code> to put code into
     * @param <code>context</code> the current context. This method will use and
     *            update the formatting context
     * @param <code>bNoTextPar</code> true if this paragraph has no text content
     *            (hence character formatting is not needed)
     */
    public void applyParStyle(String sName, BeforeAfter ba, Context context, boolean bNoTextPar) {
        applyParStyle(sName, ba, context, bNoTextPar, true);
    }

    private void applyParStyle(String sName, BeforeAfter ba, Context context, boolean bNoTextPar, boolean bBreakInside) {
        if (sName == null) {
            return;
        }
        if (context.isInSimpleTable()) {
            if (config.formatting() != Config.IGNORE_ALL) {
                ParStyle style = ofr.getParStyle(sName);
                if (style != null) {
                    palette.getI18n().applyLanguage(style, true, true, ba);
                    palette.getCharSc().applyFont(style, true, true, ba, context);
                    if (ba.getBefore().length() > 0) {
                        ba.add(" ", "");
                    }
                }
            }
        } else if (bNoTextPar && (config.formatting() == Config.CONVERT_BASIC || config.formatting() == Config.IGNORE_MOST)) {
            ba.add("", "\n");
            ParStyle style = ofr.getParStyle(sName);
            if (style != null) {
                applyPageBreak(style, false, ba);
                String sTextAlign = style.getProperty(XMLString.FO_TEXT_ALIGN, true);
                if ("center".equals(sTextAlign)) {
                    ba.add("{\\centering ", "\\par}");
                } else if ("end".equals(sTextAlign)) {
                    ba.add("{\\raggedleft ", "\\par}");
                }
            }
        } else {
            ba.add("", "\n");
            if (!styleMap.contains(sName)) {
                createParStyle(sName);
            }
            String sBefore = styleMap.getBefore(sName);
            String sAfter = styleMap.getAfter(sName);
            ba.add(sBefore, sAfter);
            if (bBreakInside && styleMap.getLineBreak(sName)) {
                if (sBefore.length() > 0) {
                    ba.add("\n", "");
                }
                if (sAfter.length() > 0 && !"}".equals(sAfter)) {
                    ba.add("", "\n");
                }
            }
        }
        ParStyle style = ofr.getParStyle(sName);
        if (style == null) {
            return;
        }
        context.updateFormattingFromStyle(style);
        context.setVerbatim(styleMap.getVerbatim(sName));
    }

    /**
     * <p>
     * Convert a paragraph style to LaTeX.
     * </p>
     * <p>
     * A soft style is declared in <code>styleDeclarations</code> as
     * <code>\newenvironment...</code>
     * </p>
     * <p>
     * A hard style is used by applying LaTeX code directly
     * </p>
     * 
     * @param <code>sName</code> the OOo name of the style
     */
    private void createParStyle(String sName) {
        Context context = (Context) palette.getMainContext().clone();
        StyleMap sm = config.getParStyleMap();
        if (sm.contains(sName)) {
            styleMap.put(sName, sm.getBefore(sName), sm.getAfter(sName), sm.getLineBreak(sName), sm.getVerbatim(sName));
            return;
        }
        ParStyle style = ofr.getParStyle(sName);
        if (style == null) {
            styleMap.put(sName, "", "");
            return;
        }
        switch(config.formatting()) {
            case Config.CONVERT_MOST:
                if (style.isAutomatic()) {
                    createAutomaticParStyle(style, context);
                    return;
                }
            case Config.CONVERT_ALL:
                createSoftParStyle(style, context);
                return;
            case Config.CONVERT_BASIC:
            case Config.IGNORE_MOST:
                createSimpleParStyle(style, context);
                return;
            case Config.IGNORE_ALL:
            default:
                styleMap.put(sName, "", "");
        }
    }

    private void createAutomaticParStyle(ParStyle style, Context context) {
        BeforeAfter ba = new BeforeAfter();
        BeforeAfter baPar = new BeforeAfter();
        BeforeAfter baText = new BeforeAfter();
        String sParentName = style.getParentName();
        if (styleMap.getVerbatim(sParentName)) {
            styleMap.put(style.getName(), styleMap.getBefore(sParentName), styleMap.getAfter(sParentName), styleMap.getLineBreak(sParentName), styleMap.getVerbatim(sParentName));
            return;
        }
        applyParStyle(sParentName, baPar, context, false, false);
        applyPageBreak(style, false, ba);
        palette.getI18n().applyLanguage(style, true, false, baText);
        palette.getCharSc().applyFont(style, true, false, baText, context);
        if (baPar.isEmpty() && !baText.isEmpty()) {
            ba.add("{", "}");
        } else {
            ba.add(baPar.getBefore(), baPar.getAfter());
        }
        ba.add(baText.getBefore(), baText.getAfter());
        boolean bLineBreak = styleMap.getLineBreak(sParentName);
        if (!bLineBreak && !baText.isEmpty()) {
            ba.add(" ", "");
        }
        styleMap.put(style.getName(), ba.getBefore(), ba.getAfter(), bLineBreak, false);
    }

    private void createSimpleParStyle(ParStyle style, Context context) {
        if (style.isAutomatic() && config.getParStyleMap().contains(style.getParentName())) {
            createAutomaticParStyle(style, context);
            return;
        }
        BeforeAfter ba = new BeforeAfter();
        BeforeAfter baText = new BeforeAfter();
        applyPageBreak(style, false, ba);
        String sTextAlign = style.getProperty(XMLString.FO_TEXT_ALIGN, true);
        if ("center".equals(sTextAlign)) {
            baText.add("\\centering", "\\par");
        } else if ("end".equals(sTextAlign)) {
            baText.add("\\raggedleft", "\\par");
        }
        palette.getI18n().applyLanguage(style, true, true, baText);
        palette.getCharSc().applyFont(style, true, true, baText, context);
        if (!baText.isEmpty()) {
            ba.add("{", "}");
        }
        ba.add(baText.getBefore(), baText.getAfter());
        styleMap.put(style.getName(), ba.getBefore(), ba.getAfter());
    }

    private void createSoftParStyle(ParStyle style, Context context) {
        if (style.isAutomatic() && config.getParStyleMap().contains(style.getParentName())) {
            createAutomaticParStyle(style, context);
        }
        BeforeAfter ba = new BeforeAfter();
        applyParProperties(style, ba);
        ba.add("\\writerlistparindent\\writerlistleftskip", "");
        palette.getI18n().applyLanguage(style, true, true, ba);
        ba.add("\\leavevmode", "");
        palette.getCharSc().applyNormalFont(ba);
        palette.getCharSc().applyFont(style, true, true, ba, context);
        ba.add("\\writerlistlabel", "");
        ba.add("\\ignorespaces", "");
        String sTeXName = "style" + styleNames.getExportName(style.getName());
        styleMap.put(style.getName(), "\\begin{" + sTeXName + "}", "\\end{" + sTeXName + "}");
        declarations.append("\\newenvironment{").append(sTeXName).append("}{").append(ba.getBefore()).append("}{").append(ba.getAfter()).append("}").nl();
    }

    /**
     * <p>
     * Use a paragraph style on a heading. If hard paragraph formatting is
     * applied to a heading, page break and font is converted - other hard
     * formatting is ignored.
     * <p>
     * This method also collects name of heading style and master page
     * 
     * @param <code>nLevel</code> The level of this heading
     * @param <code>sStyleName</code> the name of the paragraph style to use
     * @param <code>baPage</code> a <code>BeforeAfter</code> to put page break
     *            code into
     * @param <code>baText</code> a <code>BeforeAfter</code> to put character
     *            formatting code into
     * @param <code>context</code> the current context. This method will use and
     *            update the formatting context
     */
    public void applyHardHeadingStyle(int nLevel, String sStyleName, BeforeAfter baPage, BeforeAfter baText, Context context) {
        ParStyle style = ofr.getParStyle(sStyleName);
        if (style == null) {
            return;
        }
        if (sHeadingStyles[nLevel] == null) {
            sHeadingStyles[nLevel] = style.isAutomatic() ? style.getParentName() : sStyleName;
        }
        if (style.isAutomatic()) {
            applyPageBreak(style, false, baPage);
            palette.getCharSc().applyHardCharFormatting(style, baText);
        }
        context.updateFormattingFromStyle(style);
    }

    public String getFontName(String sStyleName) {
        StyleWithProperties style = ofr.getParStyle(sStyleName);
        if (style == null) {
            return null;
        }
        String sName = style.getProperty(XMLString.STYLE_FONT_NAME);
        if (sName == null) {
            return null;
        }
        FontDeclaration fd = ofr.getFontDeclaration(sName);
        if (fd == null) {
            return null;
        }
        return fd.getProperty(XMLString.FO_FONT_FAMILY);
    }

    /**
     * <p>
     * Apply page break properties from a style.
     * </p>
     * 
     * @param <code>style</code> the paragraph style to use
     * @param <code>bInherit</code> true if inheritance from parent style should
     *            be used
     * @param <code>ba</code> a <code>BeforeAfter</code> to put code into
     */
    public void applyPageBreak(ParStyle style, boolean bInherit, BeforeAfter ba) {
        if (style == null) {
            return;
        }
        if (style.isAutomatic() && config.ignoreHardPageBreaks()) {
            return;
        }
        String s = style.getProperty(XMLString.FO_BREAK_BEFORE, bInherit);
        if ("page".equals(s)) {
            ba.add("\\clearpage", "");
        }
        s = style.getProperty(XMLString.FO_BREAK_AFTER, bInherit);
        if ("page".equals(s)) {
            ba.add("", "\\clearpage");
        }
        String sMasterPage = style.getMasterPageName();
        if (sMasterPage == null || sMasterPage.length() == 0) {
            return;
        }
        ba.add("\\clearpage", "");
        palette.getPageSc().applyMasterPage(sMasterPage, ba);
    }

    /**
     * <p>
     * Apply line spacing from a style.
     * </p>
     * 
     * @param <code>style</code> the paragraph style to use
     * @param <code>ba</code> a <code>BeforeAfter</code> to put code into
     */
    private void applyLineSpacing(ParStyle style, BeforeAfter ba) {
        if (style == null) {
            return;
        }
        String sLineHeight = style.getProperty(XMLString.FO_LINE_HEIGHT);
        if (sLineHeight == null || !sLineHeight.endsWith("%")) {
            return;
        }
        float fPercent = Misc.getFloat(sLineHeight.substring(0, sLineHeight.length() - 1), 1);
        ba.add("\\renewcommand\\baselinestretch{" + fPercent / 120 + "}", "");
    }

    /**
     * <p>
     * Helper: Get a length property that defaults to 0cm.
     */
    private String getLength(ParStyle style, String sProperty) {
        String s = style.getAbsoluteProperty(sProperty);
        if (s == null) {
            return "0cm";
        } else {
            return s;
        }
    }

    /**
     * <p>
     * Helper: Create a horizontal border.
     * </p>
     */
    private String createBorder(String sLeft, String sRight, String sTop, String sHeight, String sColor) {
        BeforeAfter baColor = new BeforeAfter();
        palette.getCharSc().applyThisColor(sColor, false, baColor);
        return "{\\setlength\\parindent{0pt}\\setlength\\leftskip{" + sLeft + "}" + "\\setlength\\baselineskip{0pt}\\setlength\\parskip{" + sHeight + "}" + baColor.getBefore() + "\\rule{\\textwidth-" + sLeft + "-" + sRight + "}{" + sHeight + "}" + baColor.getAfter() + "\\par}";
    }

    /**
     * <p>
     * Apply margin+alignment properties from a style.
     * </p>
     * 
     * @param <code>style</code> the paragraph style to use
     * @param <code>ba</code> a <code>BeforeAfter</code> to put code into
     */
    private void applyMargins(ParStyle style, BeforeAfter ba) {
        String sPaddingTop = getLength(style, XMLString.FO_PADDING_TOP);
        String sPaddingBottom = getLength(style, XMLString.FO_PADDING_BOTTOM);
        String sPaddingLeft = getLength(style, XMLString.FO_PADDING_LEFT);
        String sPaddingRight = getLength(style, XMLString.FO_PADDING_RIGHT);
        String sMarginTop = getLength(style, XMLString.FO_MARGIN_TOP);
        String sMarginBottom = getLength(style, XMLString.FO_MARGIN_BOTTOM);
        String sMarginLeft = getLength(style, XMLString.FO_MARGIN_LEFT);
        String sMarginRight = getLength(style, XMLString.FO_MARGIN_RIGHT);
        String sTextIndent;
        if ("true".equals(style.getProperty(XMLString.STYLE_AUTO_TEXT_INDENT))) {
            sTextIndent = "2em";
        } else {
            sTextIndent = getLength(style, XMLString.FO_TEXT_INDENT);
        }
        boolean bRaggedLeft = false;
        boolean bRaggedRight = false;
        boolean bParFill = false;
        String sTextAlign = style.getProperty(XMLString.FO_TEXT_ALIGN);
        if ("center".equals(sTextAlign)) {
            bRaggedLeft = true;
            bRaggedRight = true;
        } else if ("start".equals(sTextAlign)) {
            bRaggedRight = true;
            bParFill = true;
        } else if ("end".equals(sTextAlign)) {
            bRaggedLeft = true;
        } else if (!"justify".equals(style.getProperty(XMLString.FO_TEXT_ALIGN_LAST))) {
            bParFill = true;
        }
        ba.add("\\setlength\\leftskip{" + sMarginLeft + (bRaggedLeft ? " plus 1fil" : "") + "}", "");
        ba.add("\\setlength\\rightskip{" + sMarginRight + (bRaggedRight ? " plus 1fil" : "") + "}", "");
        ba.add("\\setlength\\parindent{" + sTextIndent + "}", "");
        ba.add("\\setlength\\parfillskip{" + (bParFill ? "0pt plus 1fil" : "0pt") + "}", "");
        ba.add("\\setlength\\parskip{" + sMarginTop + "}", "\\unskip\\vspace{" + sMarginBottom + "}");
    }

    private void applyAlignment(ParStyle style, boolean bIsSimple, boolean bInherit, BeforeAfter ba) {
        if (bIsSimple || style == null) {
            return;
        }
        String sTextAlign = style.getProperty(XMLString.FO_TEXT_ALIGN, bInherit);
        if ("center".equals(sTextAlign)) {
            ba.add("\\centering", "");
        } else if ("start".equals(sTextAlign)) {
            ba.add("\\raggedright", "");
        } else if ("end".equals(sTextAlign)) {
            ba.add("\\raggedleft", "");
        }
    }

    /**
     * <p>
     * Apply all paragraph properties.
     * </p>
     * 
     * @param <code>style</code> the paragraph style to use
     * @param <code>ba</code> a <code>BeforeAfter</code> to put code into
     */
    private void applyParProperties(ParStyle style, BeforeAfter ba) {
        applyPageBreak(style, true, ba);
        ba.add("", "\\par");
        applyLineSpacing(style, ba);
        applyMargins(style, ba);
    }

    private void appendHeadingStyles(LaTeXDocumentPortion ldp) {
        if (config.formatting() <= Config.IGNORE_MOST) {
            return;
        }
        HeadingMap hm = config.getHeadingMap();
        int nMaxLevel = 0;
        for (int i = 1; i <= 5; i++) {
            if (sHeadingStyles[i] != null) {
                nMaxLevel = i;
            }
        }
        if (nMaxLevel == 0) {
            return;
        }
        if (nMaxLevel > hm.getMaxLevel()) {
            nMaxLevel = hm.getMaxLevel();
        }
        boolean bOnlyNum = config.formatting() == Config.CONVERT_BASIC;
        if (bOnlyNum) {
            ldp.append("% Outline numbering").nl();
        } else {
            ldp.append("% Headings and outline numbering").nl().append("\\makeatletter").nl();
        }
        if (!bOnlyNum) {
            for (int i = 1; i <= nMaxLevel; i++) {
                if (sHeadingStyles[i] != null) {
                    ParStyle style = ofr.getParStyle(sHeadingStyles[i]);
                    if (style != null) {
                        BeforeAfter decl = new BeforeAfter();
                        BeforeAfter comm = new BeforeAfter();
                        applyPageBreak(style, true, decl);
                        palette.getCharSc().applyNormalFont(decl);
                        palette.getCharSc().applyFont(style, true, true, decl, new Context());
                        applyAlignment(style, false, true, decl);
                        palette.getI18n().applyLanguage(style, false, true, comm);
                        palette.getCharSc().applyFontEffects(style, true, comm);
                        String sMarginTop = getLength(style, XMLString.FO_MARGIN_TOP);
                        String sMarginBottom = getLength(style, XMLString.FO_MARGIN_BOTTOM);
                        String sMarginLeft = getLength(style, XMLString.FO_MARGIN_LEFT);
                        String sSecName = hm.getName(i);
                        if (!comm.isEmpty()) {
                            ldp.append("\\newcommand\\cs").append(sSecName).append("[1]{").append(comm.getBefore()).append("#1").append(comm.getAfter()).append("}").nl();
                        }
                        ldp.append("\\renewcommand\\").append(sSecName).append("{\\@startsection{").append(sSecName).append("}{" + hm.getLevel(i)).append("}{" + sMarginLeft + "}{");
                        ldp.append(sMarginTop).append("}{").append(sMarginBottom).append("}{");
                        ldp.append(decl.getBefore());
                        if (!comm.isEmpty()) {
                            ldp.append("\\cs").append(sSecName);
                        }
                        ldp.append("}}").nl();
                    }
                }
            }
        }
        if (!bOnlyNum) {
            ldp.append("\\renewcommand\\@seccntformat[1]{").append("\\csname @textstyle#1\\endcsname{\\csname the#1\\endcsname}").append("\\csname @distance#1\\endcsname}").nl();
        }
        int nSecnumdepth = nMaxLevel;
        ListStyle outline = ofr.getOutlineStyle();
        String[] sNumFormat = new String[6];
        for (int i = nMaxLevel; i >= 1; i--) {
            sNumFormat[i] = ListStyleConverter.numFormat(outline.getLevelProperty(i, XMLString.STYLE_NUM_FORMAT));
            if (sNumFormat[i] == null || "".equals(sNumFormat[i])) {
                nSecnumdepth = i - 1;
            }
        }
        ldp.append("\\setcounter{secnumdepth}{" + nSecnumdepth + "}").nl();
        for (int i = 1; i <= nMaxLevel; i++) {
            if (sNumFormat[i] == null || "".equals(sNumFormat[i])) {
                if (!bOnlyNum) {
                    ldp.append("\\newcommand\\@distance").append(hm.getName(i)).append("{}").nl().append("\\newcommand\\@textstyle").append(hm.getName(i)).append("[1]{#1}").nl();
                }
            } else {
                if (!bOnlyNum) {
                    String sDistance = outline.getLevelProperty(i, XMLString.TEXT_MIN_LABEL_DISTANCE);
                    ldp.append("\\newcommand\\@distance").append(hm.getName(i)).append("{");
                    if (sDistance != null) {
                        ldp.append("\\hspace{").append(sDistance).append("{");
                    }
                    ldp.append("}").nl();
                    String sStyleName = outline.getLevelProperty(i, XMLString.TEXT_STYLE_NAME);
                    BeforeAfter baText = new BeforeAfter();
                    if (!bOnlyNum) {
                        palette.getCharSc().applyTextStyle(sStyleName, baText, new Context());
                    }
                    ldp.append("\\newcommand\\@textstyle").append(hm.getName(i)).append("[1]{").append(baText.getBefore()).append("#1").append(baText.getAfter()).append("}").nl();
                }
                String sPrefix = outline.getLevelProperty(i, XMLString.STYLE_NUM_PREFIX);
                String sSuffix = outline.getLevelProperty(i, XMLString.STYLE_NUM_SUFFIX);
                int nLevels = Misc.getPosInteger(outline.getLevelProperty(i, XMLString.TEXT_DISPLAY_LEVELS), 1);
                ldp.append("\\renewcommand\\the").append(hm.getName(i)).append("{");
                StringBuffer labelbuf = new StringBuffer();
                if (sPrefix != null) {
                    labelbuf.append(sPrefix);
                }
                for (int j = i - nLevels + 1; j < i; j++) {
                    labelbuf.append(sNumFormat[j]).append("{").append(sectionName(j)).append("}").append(".");
                }
                labelbuf.append(sNumFormat[i]).append("{").append(hm.getName(i)).append("}");
                if (sSuffix != null) {
                    labelbuf.append(sSuffix);
                }
                if (bOnlyNum) {
                    ldp.append(labelbuf.toString().trim());
                } else {
                    ldp.append(labelbuf.toString());
                }
                ldp.append("}").nl();
            }
        }
        if (!bOnlyNum) {
            ldp.append("\\makeatother").nl();
        }
    }

    static final String sectionName(int nLevel) {
        switch(nLevel) {
            case 1:
                return "section";
            case 2:
                return "subsection";
            case 3:
                return "subsubsection";
            case 4:
                return "paragraph";
            case 5:
                return "subparagraph";
            default:
                return null;
        }
    }
}
