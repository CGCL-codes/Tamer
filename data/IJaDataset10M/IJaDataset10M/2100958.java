package org.openxml4j.document.word;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.QName;

/**
 * @author CDubettier
 * manage the TOC of a word doc
 */
public class TableOfContents {

    private static final String STR_TEXT_MESSAGE_FOR_USER_UPDATE_TOC = "Please select and press F9 to generate a new table of contents";

    private static final String PARA_STYLE_FOR_TOC = "TOC1";

    public String paragraphStyleForTOC = PARA_STYLE_FOR_TOC;

    private ArrayList<ParagraphShownInTOC> listOfParagraphFormatInTOC = new ArrayList<ParagraphShownInTOC>();

    private String title;

    private ParagraphBuilder titleParaBuilder = new ParagraphBuilder();

    public TableOfContents(String title) {
        super();
        this.title = title;
    }

    public TableOfContents(String paragraphStyleForTOC, String title, ParagraphBuilder titleParaBuilder) {
        super();
        this.paragraphStyleForTOC = paragraphStyleForTOC;
        this.title = title;
        this.titleParaBuilder = titleParaBuilder;
    }

    public void addParagraphShownInTOC(ParagraphShownInTOC newPara) {
        listOfParagraphFormatInTOC.add(newPara);
    }

    /** add something like
	<w:p >
			<w:r> <w:t>TOC</w:t> </w:r>
		</w:p>
	 */
    private Element buildTitle() {
        if (title != null) {
            Paragraph para = titleParaBuilder.newParagraph();
            para.addTextAsRunWithParagraphSetting(title);
            return para.build();
        } else {
            return null;
        }
    }

    /**
	 * add something like
	 * 		<w:r>
				<w:fldChar w:fldCharType="begin" w:dirty="true"/>
			</w:r>
			<w:r>
				<w:instrText xml:space="preserve"> TOC \h \z \t "Liste 2;1;Untertitel;2" </w:instrText>
			</w:r>
	 *
	 * @param rootTocElement
	 */
    private void addTOC(Element rootParaToc) {
        Element run = rootParaToc.addElement(new QName(WordprocessingML.PARAGRAPH_RUN_TAG_NAME, WordprocessingML.namespaceWord));
        Element fldChar = run.addElement(new QName(WordprocessingML.PARAGRAPH_FLD_CHAR, WordprocessingML.namespaceWord));
        fldChar.addAttribute(new QName(WordprocessingML.ATTRIBUTE_FLD_CHAR_TYPE, WordprocessingML.namespaceWord), "begin");
        fldChar.addAttribute(new QName(WordprocessingML.ATTRIBUTE_DIRTY, WordprocessingML.namespaceWord), "true");
        run = rootParaToc.addElement(new QName(WordprocessingML.PARAGRAPH_RUN_TAG_NAME, WordprocessingML.namespaceWord));
        Element toc = run.addElement(new QName(WordprocessingML.INSTR_TEXT, WordprocessingML.namespaceWord));
        toc.addAttribute("xml:space", "preserve");
        StringBuffer str = new StringBuffer("TOC \\h \\z \\t \"");
        for (Iterator iter = listOfParagraphFormatInTOC.iterator(); iter.hasNext(); ) {
            ParagraphShownInTOC para = (ParagraphShownInTOC) iter.next();
            str.append(para.toString());
        }
        toc.addText(str.toString());
    }

    /**
	 * 		<w:pPr>
				<w:pStyle w:val="TOC1"/>
				///// no more this
				<w:tabs>
					<w:tab w:val="right" w:leader="dot" w:pos="9062"/>
				</w:tabs>
				///end no more
				<w:rPr>
					<w:noProof/>
				</w:rPr>
			</w:pPr>
	 * @param rootTocElement
	 */
    private void addParaPropertiesForTOC(Element rootTocElement) {
        Element tocpPr = rootTocElement.addElement(new QName(WordprocessingML.PARAGRAPH_PROPERTIES_TAG_NAME, WordprocessingML.namespaceWord));
        Element pStyle = tocpPr.addElement(new QName(WordprocessingML.PARAGRAPH_STYLE, WordprocessingML.namespaceWord));
        pStyle.addAttribute(new QName(WordprocessingML.ATTRIBUTE_VAL, WordprocessingML.namespaceWord), paragraphStyleForTOC);
        Element rPr = tocpPr.addElement(new QName(WordprocessingML.PARA_STYLE_NUMBERING_SYMBOL_RUN_PROPERTIES, WordprocessingML.namespaceWord));
        rPr.addElement(new QName(WordprocessingML.NO_PROOF_SYNTAX_GRAMMAR, WordprocessingML.namespaceWord));
    }

    private void buildEndForToc(ArrayList<Element> document) {
        DocumentFactory factory = DocumentFactory.getInstance();
        {
            Element tocSeparate = factory.createElement(new QName(WordprocessingML.PARAGRAPH_BODY_TAG_NAME, WordprocessingML.namespaceWord));
            Element run = tocSeparate.addElement(new QName(WordprocessingML.PARAGRAPH_RUN_TAG_NAME, WordprocessingML.namespaceWord));
            Element fldChar = run.addElement(new QName(WordprocessingML.PARAGRAPH_FLD_CHAR, WordprocessingML.namespaceWord));
            fldChar.addAttribute(new QName(WordprocessingML.ATTRIBUTE_FLD_CHAR_TYPE, WordprocessingML.namespaceWord), "separate");
            document.add(tocSeparate);
        }
        {
            Element tocShowHowToUpdateForUser = factory.createElement(new QName(WordprocessingML.PARAGRAPH_BODY_TAG_NAME, WordprocessingML.namespaceWord));
            Element run = tocShowHowToUpdateForUser.addElement(new QName(WordprocessingML.PARAGRAPH_RUN_TAG_NAME, WordprocessingML.namespaceWord));
            Element text = run.addElement(new QName(WordprocessingML.RUN_TEXT, WordprocessingML.namespaceWord));
            text.addText(STR_TEXT_MESSAGE_FOR_USER_UPDATE_TOC);
            document.add(tocShowHowToUpdateForUser);
        }
        {
            Element tocPara = factory.createElement(new QName(WordprocessingML.PARAGRAPH_BODY_TAG_NAME, WordprocessingML.namespaceWord));
            Element run = tocPara.addElement(new QName(WordprocessingML.PARAGRAPH_RUN_TAG_NAME, WordprocessingML.namespaceWord));
            Element fldChar = run.addElement(new QName(WordprocessingML.PARAGRAPH_FLD_CHAR, WordprocessingML.namespaceWord));
            fldChar.addAttribute(new QName(WordprocessingML.ATTRIBUTE_FLD_CHAR_TYPE, WordprocessingML.namespaceWord), "end");
            document.add(tocPara);
        }
    }

    public List<Element> build(boolean addWriteEnableTags) {
        ArrayList<Element> result = new ArrayList<Element>();
        Element title = buildTitle();
        if (title != null) {
            result.add(title);
        }
        ParagraphReadOnlyManager writeEnablePart = null;
        if (addWriteEnableTags) {
            writeEnablePart = new ParagraphReadOnlyManager();
            result.add(writeEnablePart.buildReadOnlyStartTag());
        }
        DocumentFactory factory = DocumentFactory.getInstance();
        Element tocPara = factory.createElement(new QName(WordprocessingML.PARAGRAPH_BODY_TAG_NAME, WordprocessingML.namespaceWord));
        addParaPropertiesForTOC(tocPara);
        addTOC(tocPara);
        result.add(tocPara);
        buildEndForToc(result);
        if (addWriteEnableTags) {
            result.add(writeEnablePart.buildReadOnlyEndTag());
        }
        return result;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getParagraphStyleForTOC() {
        return paragraphStyleForTOC;
    }

    public void setParagraphStyleForTOC(String paragraphStyleForTOC) {
        this.paragraphStyleForTOC = paragraphStyleForTOC;
    }

    public ParagraphBuilder getTitleParaBuilder() {
        return titleParaBuilder;
    }

    public void setTitleParaBuilder(ParagraphBuilder titleParaBuilder) {
        this.titleParaBuilder = titleParaBuilder;
    }
}
