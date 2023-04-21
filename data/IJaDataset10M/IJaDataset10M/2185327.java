package org.openxml4j.document.wordprocessing;

import java.util.ArrayList;
import java.util.List;

/**
 * Convenience class to create paragraphs using the same settings (font, numbering ...)
 *
 */
public class ParagraphBuilder {

    private static final int DEFAULT_FONT_SIZE = 22;

    public static final int PARAGRAPH_NUMBERING_NONE = -1;

    public static final String DEFAULT_PARAGRAPH_STYLE = "Normal";

    protected ParagraphAlignment alignment = ParagraphAlignment.LEFT;

    /**
	 * @see ParagraphReadOnlyManager
	 */
    protected boolean allowEditionInReadOnlyDoc = false;

    protected boolean bold = false;

    protected boolean italic = false;

    protected UnderlineStyle underline = UnderlineStyle.NONE;

    protected int fontSize = DEFAULT_FONT_SIZE;

    protected int paragraphRefInNumberingXml = PARAGRAPH_NUMBERING_NONE;

    protected int paragraphNumberShift;

    /**
	 * the style name should be related to the style described in style.xml
	 * The style will be used to generate TOC
	 */
    protected String paragraphStyleName = DEFAULT_PARAGRAPH_STYLE;

    /**
	 * space above/below paragraph
	 */
    protected ParagraphSpacing spacing;

    public ParagraphBuilder() {
        spacing = new ParagraphSpacing();
    }

    public int getParagraphNumberShift() {
        return paragraphNumberShift;
    }

    public void setParagraphNumberShift(int paragraphNumberShift) {
        this.paragraphNumberShift = paragraphNumberShift;
    }

    public int getParagraphRefInNumberingXml() {
        return paragraphRefInNumberingXml;
    }

    public void setParagraphRefInNumberingXml(int paragraphRefInNumberingXml) {
        this.paragraphRefInNumberingXml = paragraphRefInNumberingXml;
    }

    /**
	 * @return   the alignment
	 * @uml.property  name="alignment"
	 */
    public ParagraphAlignment getAlignment() {
        return alignment;
    }

    /**
	 * @param alignment   the alignment to set
	 * @uml.property  name="alignment"
	 */
    public void setAlignment(ParagraphAlignment alignment) {
        this.alignment = alignment;
    }

    /**
	 * @return   the bold
	 * @uml.property  name="bold"
	 */
    public boolean isBold() {
        return bold;
    }

    /**
	 * @param bold   the bold to set
	 * @uml.property  name="bold"
	 */
    public void setBold(boolean bold) {
        this.bold = bold;
    }

    /**
	 * @return   the italic
	 * @uml.property  name="italic"
	 */
    public boolean isItalic() {
        return italic;
    }

    /**
	 * @param italic   the italic to set
	 * @uml.property  name="italic"
	 */
    public void setItalic(boolean italic) {
        this.italic = italic;
    }

    public UnderlineStyle isUnderline() {
        return underline;
    }

    /**
	 * @param underline   the underline to set
	 * @uml.property  name="underline"
	 */
    public void setUnderline(UnderlineStyle underline) {
        this.underline = underline;
    }

    public void setSpacing(ParagraphSpacing spacing) {
        this.spacing = spacing;
    }

    public void setSpacing(ParagraphSpacingPredefined spacing) {
        this.spacing = new ParagraphSpacing(spacing);
    }

    /**
	 * create a paragraph with the predefined settings of this class
	 */
    public Paragraph newParagraph() {
        Paragraph retPar = new Paragraph();
        retPar.setAlignment(alignment);
        retPar.setBold(bold);
        retPar.setItalic(italic);
        retPar.setUnderline(underline);
        retPar.setFontSize(fontSize);
        retPar.setParagraphNumberShift(paragraphNumberShift);
        retPar.setParagraphRefInNumberingXml(paragraphRefInNumberingXml);
        retPar.setSpacing(spacing);
        retPar.setParagraphStyleName(paragraphStyleName);
        if (allowEditionInReadOnlyDoc) {
            retPar.setReadOnlyPermission(new ParagraphReadOnlyManager());
        }
        return retPar;
    }

    public List<Paragraph> newParagraphs(String text) {
        List<Paragraph> listPara = new ArrayList<Paragraph>();
        if (text == null) {
            return listPara;
        }
        String str[] = text.split("\n");
        for (int i = 0; i < str.length; i++) {
            Paragraph para = newParagraph();
            para.addTextAsRunWithParagraphSetting(str[i]);
            listPara.add(para);
        }
        return listPara;
    }

    public boolean isAllowEditionInReadOnlyDoc() {
        return allowEditionInReadOnlyDoc;
    }

    public void setAllowEditionInReadOnlyDoc(boolean allowEditionInReadOnlyDoc) {
        this.allowEditionInReadOnlyDoc = allowEditionInReadOnlyDoc;
    }

    public String getParagraphStyleName() {
        return paragraphStyleName;
    }

    public void setParagraphStyleName(String paragraphStyleName) {
        this.paragraphStyleName = paragraphStyleName;
    }
}
