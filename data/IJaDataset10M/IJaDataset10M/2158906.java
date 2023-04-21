package org.openxml4j.document.word;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.QName;

/**
 *
 * @author Julien Chable
 */
public final class Paragraph extends TextObject {

    private static Logger logger = Logger.getLogger("org.openxml4j");

    protected boolean startOnNewPage = false;

    protected int paragraphRefInNumberingXml = ParagraphBuilder.PARAGRAPH_NUMBERING_NONE;

    protected int paragraphNumberShift = 0;

    /**
	 * the style name should be related to the style described in style.xml The
	 * style will be used to generate TOC
	 */
    protected String paragraphStyleName = ParagraphBuilder.DEFAULT_PARAGRAPH_STYLE;

    /**
	 * space above and below paragraph
	 */
    private ParagraphSpacing spacing;

    /**
	 * in a read only document, allow some paragraph to be editable
	 */
    private ParagraphReadOnlyManager readOnlyPermission = null;

    protected ArrayList<Run> runs;

    private ParagraphIndentation indentation = null;

    private CustomTabSet customTabSet = null;

    /**
	 * Pr�vient la construction de paragraphe.
	 */
    protected Paragraph() {
        runs = new ArrayList<Run>();
        spacing = new ParagraphSpacing();
    }

    /**
	 * Ajoute un Run � ce paragraphe.
	 *
	 * @param run
	 *            Le Run � ajouter.
	 */
    public void addRun(Run run) {
        runs.add(run);
    }

    /**
	 * @param text
	 * @return the run created
	 */
    public void addTextAsRunWithParagraphSetting(String text) {
        Run run = new Run(text);
        run.setCharacterFormat(characterFormat);
        runs.add(run);
    }

    private void addParagraphProperties(Element paragraph) {
        Element properties = paragraph.addElement(new QName(WordprocessingML.PARAGRAPH_PROPERTIES_TAG_NAME, WordprocessingML.namespaceWord));
        if (isStartOnNewPage()) {
            properties.addElement(new QName(WordprocessingML.PARA_PAGE_BREAK, WordprocessingML.namespaceWord));
        }
        Element paraAlignement = properties.addElement(new QName(WordprocessingML.PARAGRAPH_ALIGNEMENT_TAG_NAME, WordprocessingML.namespaceWord));
        paraAlignement.addAttribute(new QName(WordprocessingML.ATTRIBUTE_VAL, WordprocessingML.namespaceWord), alignment.toString());
        if (paragraphRefInNumberingXml != ParagraphBuilder.PARAGRAPH_NUMBERING_NONE) {
            Element paraNumbering = properties.addElement(new QName(WordprocessingML.PARAGRAPH_NUMBERING_TAG_NAME, WordprocessingML.namespaceWord));
            Element numberingLevelReference = paraNumbering.addElement(new QName(WordprocessingML.PARAGRAPH_NUMBERING_LEVEL_REFERENCE, WordprocessingML.namespaceWord));
            numberingLevelReference.addAttribute(new QName(WordprocessingML.ATTRIBUTE_VAL, WordprocessingML.namespaceWord), (new Integer(paragraphNumberShift).toString()));
            Element numberingReferenceInNumberingXml = paraNumbering.addElement(new QName(WordprocessingML.PARAGRAPH_NUMBERING_REFERENCE, WordprocessingML.namespaceWord));
            numberingReferenceInNumberingXml.addAttribute(new QName(WordprocessingML.ATTRIBUTE_VAL, WordprocessingML.namespaceWord), (new Integer(paragraphRefInNumberingXml).toString()));
        }
        addParameterStyleXmlCode(properties);
        if (indentation != null) {
            indentation.addParagraphIndentation(properties);
        }
        if (customTabSet != null) {
            customTabSet.addCustomTabSet(properties);
        }
        if (spacing.isSpacingRequired()) {
            spacing.addParagraphSpacingProperties(properties);
        }
    }

    private Element addParameterStyleXmlCode(Element paraProperties) {
        return Paragraph.addParameterStyleXmlCode(paraProperties, paragraphStyleName);
    }

    private static Element addParameterStyleXmlCode(Element paraProperties, String styleName) {
        Element paraStyle = paraProperties.addElement(new QName(WordprocessingML.PARAGRAPH_STYLE, WordprocessingML.namespaceWord));
        paraStyle.addAttribute(new QName(WordprocessingML.ATTRIBUTE_VAL, WordprocessingML.namespaceWord), styleName);
        return paraStyle;
    }

    public static void addDefaultStyleXmlCode(Element paraNode) {
        Element propertiesNode = hasProperties(paraNode);
        if (propertiesNode != null) {
            addParameterStyleXmlCode(propertiesNode, ParagraphBuilder.DEFAULT_PARAGRAPH_STYLE);
        } else {
            propertiesNode = paraNode.addElement(new QName(WordprocessingML.PARAGRAPH_PROPERTIES_TAG_NAME, WordprocessingML.namespaceWord));
            addParameterStyleXmlCode(propertiesNode, ParagraphBuilder.DEFAULT_PARAGRAPH_STYLE);
        }
    }

    public static Element hasProperties(Element nodeToCheck) {
        return hasTag(nodeToCheck, WordprocessingML.PARAGRAPH_PROPERTIES_TAG_NAME);
    }

    public static Element hasStyleName(Element nodeToCheck) {
        Element propertiesNode = Paragraph.hasProperties(nodeToCheck);
        if (propertiesNode == null) {
            return null;
        }
        return Paragraph.hasTag(propertiesNode, WordprocessingML.PARAGRAPH_STYLE);
    }

    private static Element hasTag(Element nodeToCheck, String valueToSearchFor) {
        List listOfElements = nodeToCheck.elements();
        for (Iterator iter = listOfElements.iterator(); iter.hasNext(); ) {
            Element element = (Element) iter.next();
            if (element.getName().equals(valueToSearchFor)) {
                return element;
            }
        }
        return null;
    }

    /**
	 * build a paragraph in open XML
	 *
	 * @return
	 */
    public Element build() {
        DocumentFactory factory = DocumentFactory.getInstance();
        Element paragraph = factory.createElement(new QName(WordprocessingML.PARAGRAPH_BODY_TAG_NAME, WordprocessingML.namespaceWord));
        addParagraphProperties(paragraph);
        if (readOnlyPermission != null) {
            readOnlyPermission.addReadOnlyStartTag(paragraph);
        }
        addRunsInParagraph(paragraph);
        if (readOnlyPermission != null) {
            readOnlyPermission.addReadOnlyEndTag(paragraph);
        }
        return paragraph;
    }

    private void addRunsInParagraph(Element paragraph) {
        for (Run r : runs) {
            Element run = paragraph.addElement(new QName(WordprocessingML.PARAGRAPH_RUN_TAG_NAME, WordprocessingML.namespaceWord));
            Element runProps = run.addElement(new QName(WordprocessingML.RUN_PROPERTIES_TAG_NAME, WordprocessingML.namespaceWord));
            if (r.isBold()) {
                runProps.addElement(new QName(WordprocessingML.RUN_BOLD_PROPERTY_TAG_NAME, WordprocessingML.namespaceWord));
            }
            if (r.isItalic()) {
                runProps.addElement(new QName(WordprocessingML.RUN_ITALIC_PROPERTY_TAG_NAME, WordprocessingML.namespaceWord));
            }
            if (r.getUnderline() != UnderlineStyle.NONE) {
                Element underlineProp = runProps.addElement(new QName(WordprocessingML.RUN_UNDERLINE_PROPERTY_TAG_NAME, WordprocessingML.namespaceWord));
                underlineProp.addAttribute(new QName(WordprocessingML.ATTRIBUTE_VAL, WordprocessingML.namespaceWord), r.getUnderline().toString());
            }
            if (r.getVerticalAlignement() != VerticalAlignment.NONE) {
                Element vertAlignProp = runProps.addElement(new QName(WordprocessingML.RUN_VERTICAL_ALIGNEMENT_PROPERTY_TAG_NAME, WordprocessingML.namespaceWord));
                vertAlignProp.addAttribute(new QName(WordprocessingML.ATTRIBUTE_VAL, WordprocessingML.namespaceWord), r.verticalAlignement.toString());
            }
            Element fontSizeProp = runProps.addElement(new QName(WordprocessingML.RUN_FONT_SIZE_PROPERTY_TAG_NAME, WordprocessingML.namespaceWord));
            fontSizeProp.addAttribute(new QName(WordprocessingML.ATTRIBUTE_VAL, WordprocessingML.namespaceWord), "" + r.fontSize);
            Element text = run.addElement(new QName(WordprocessingML.RUN_TEXT, WordprocessingML.namespaceWord));
            if (r.getText().indexOf(" ") != -1) {
                text.addAttribute("xml:space", "preserve");
            }
            if (logger.isDebugEnabled()) {
                logger.debug("adding (" + r.getText() + ")");
            }
            text.addText(r.getText());
        }
    }

    /**
	 * @return the bold
	 * @uml.property name="bold"
	 */
    public boolean isBold() {
        return characterFormat.isBold();
    }

    /**
	 * @param bold
	 *            the bold to set
	 * @uml.property name="bold"
	 */
    public void setBold(boolean bold) {
        characterFormat.setBold(bold);
    }

    /**
	 * @return the fontSize
	 * @uml.property name="fontSize"
	 */
    public int getFontSize() {
        return characterFormat.getFontSize();
    }

    /**
	 * @param fontSize
	 *            the fontSize to set
	 * @uml.property name="fontSize"
	 */
    public void setFontSize(int fontSize) {
        characterFormat.setFontSize(fontSize);
    }

    /**
	 * @return the italic
	 * @uml.property name="italic"
	 */
    public boolean isItalic() {
        return characterFormat.isItalic();
    }

    /**
	 * @param italic
	 *            the italic to set
	 * @uml.property name="italic"
	 */
    public void setItalic(boolean italic) {
        characterFormat.setItalic(italic);
    }

    /**
	 * @return the underline
	 * @uml.property name="underline"
	 */
    public UnderlineStyle getUnderline() {
        return characterFormat.getUnderline();
    }

    /**
	 * @param underline
	 *            the underline to set
	 * @uml.property name="underline"
	 */
    public void setUnderline(UnderlineStyle underline) {
        characterFormat.setUnderline(underline);
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

    public ParagraphSpacing getSpacing() {
        return spacing;
    }

    public void setSpacing(ParagraphSpacing spacing) {
        this.spacing = spacing;
    }

    public void setSpacing(ParagraphSpacingPredefined spacing) {
        this.spacing = new ParagraphSpacing(spacing);
    }

    public ParagraphReadOnlyManager getReadOnlyPermission() {
        return readOnlyPermission;
    }

    public void setReadOnlyPermission(ParagraphReadOnlyManager readOnlyPermission) {
        this.readOnlyPermission = readOnlyPermission;
    }

    public String getParagraphStyleName() {
        return paragraphStyleName;
    }

    public void setParagraphStyleName(String paragraphStyleName) {
        this.paragraphStyleName = paragraphStyleName;
    }

    public CustomTabSet getCustomTabSet() {
        return customTabSet;
    }

    public void setCustomTabSet(CustomTabSet customTabSet) {
        this.customTabSet = customTabSet;
    }

    public ParagraphIndentation getIndentation() {
        return indentation;
    }

    public void setIndentation(ParagraphIndentation indentation) {
        this.indentation = indentation;
    }

    public void addRuns(List listOfRuns) {
        for (Iterator iter = listOfRuns.iterator(); iter.hasNext(); ) {
            Run element = (Run) iter.next();
            addRun(element);
        }
    }

    public void setCharacterFormat(CharacterFormat p_characterFormat) {
        characterFormat = new CharacterFormat(p_characterFormat);
    }

    public CharacterFormat getCharacterFormat() {
        return characterFormat;
    }

    public boolean isStartOnNewPage() {
        return startOnNewPage;
    }

    public void setStartOnNewPage(boolean startOnNewPage) {
        this.startOnNewPage = startOnNewPage;
    }

    public void changeToBold(boolean isBold) {
        for (Iterator iter = runs.iterator(); iter.hasNext(); ) {
            Run run = (Run) iter.next();
            run.setBold(isBold);
        }
    }

    public void changeToItalic(boolean isItalic) {
        for (Iterator iter = runs.iterator(); iter.hasNext(); ) {
            Run run = (Run) iter.next();
            run.setItalic(isItalic);
        }
    }

    public void changeToUnderline(UnderlineStyle isUnderline) {
        for (Iterator iter = runs.iterator(); iter.hasNext(); ) {
            Run run = (Run) iter.next();
            run.setUnderline(isUnderline);
        }
    }
}
