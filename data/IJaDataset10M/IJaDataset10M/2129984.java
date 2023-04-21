package ast.template;

import ast.DocumentController;
import ast.common.data.QuoteStyle;
import ast.common.data.TypeOfWork;
import ast.common.error.ConfigHandlerError;
import ast.common.error.TemplateError;
import ast.common.util.PathUtils;
import com.sun.star.awt.FontSlant;
import com.sun.star.awt.FontWeight;
import com.sun.star.beans.PropertyValue;
import com.sun.star.beans.XPropertySet;
import com.sun.star.container.XIndexReplace;
import com.sun.star.container.XNameAccess;
import com.sun.star.container.XNameContainer;
import com.sun.star.container.XNamed;
import com.sun.star.frame.XStorable;
import com.sun.star.style.NumberingType;
import com.sun.star.style.PageStyleLayout;
import com.sun.star.style.ParagraphAdjust;
import com.sun.star.style.XStyle;
import com.sun.star.style.XStyleFamiliesSupplier;
import com.sun.star.text.ControlCharacter;
import com.sun.star.text.XChapterNumberingSupplier;
import com.sun.star.text.XDocumentIndex;
import com.sun.star.text.XText;
import com.sun.star.text.XTextColumns;
import com.sun.star.text.XTextContent;
import com.sun.star.text.XTextCursor;
import com.sun.star.text.XTextDocument;
import com.sun.star.text.XTextViewCursor;
import com.sun.star.text.XTextViewCursorSupplier;
import java.io.File;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Wrapper class to manage style properties of current ODT document.
 *
 * @author maermler
 * @author stefbo
 * @author feh
 */
public class TemplateController {

    /**
     * Name of the current template.
     */
    public String currentTemplateName;

    /**
     * Describes if the current template is new.
     */
    public boolean isNewTemplate;

    /**
     * Contains the styles of the default.
     */
    public Template currentTemplate;

    /**
     * Helper variable to describe the styles of the pages.
     */
    private final String[] page = new String[] { "", " left", " right" };

    /**
     * Directory of stored templates.
     */
    private File templateDir;

    /**
     * Reference to the {@link ast.DocumentController}.
     */
    private DocumentController docController;

    /**
     * Describes the styles of the citation charaters.
     */
    private XPropertySet xCharStylePropsCitation;

    /**
     * Describes the styles of the standard paragraph.
     */
    private XPropertySet xParaStylePropsStandard;

    /**
     * Describes the styles of the footer paragraph on a standard page.
     */
    private XPropertySet xParaStylePropsFooter;

    /**
     * Describes the styles of the header paragraph on a standard page.
     */
    private XPropertySet xParaStylePropsHeader;

    /**
     * Describes the styles of the footer paragraph on a left page.
     */
    private XPropertySet xParaStylePropsFooterLeft;

    /**
     * Describes the styles the header paragraph on a left page.
     */
    private XPropertySet xParaStylePropsHeaderLeft;

    /**
     * Describes the styles of the footer paragraph on a right page.
     */
    private XPropertySet xParaStylePropsFooterRight;

    /**
     * Describes the styles of the header paragraph on a right page.
     */
    private XPropertySet xParaStylePropsHeaderRight;

    /**
     * Describes the styles of the heading paragraph.
     */
    private XPropertySet xParaStylePropsHeading;

    /**
     * Describes the styles of the affidavit heading paragraph.
     */
    private XPropertySet xParaStylePropsAffidavitHeading;

    /**
     * Describes the styles of the affidavit paragraph.
     */
    private XPropertySet xParaStylePropsAffidavit;

    /**
     * Describes the styles of the affidavit page.
     */
    private XPropertySet xPageStylePropsAffidavitPage;

    /**
     * Describes the styles of the pages.
     */
    private XPropertySet[] xPageStyleProps;

    /**
     * Default constructor.
     *
     * @param aDocController Reference to the {@link ast.DocumentController}
     */
    public TemplateController(final DocumentController aDocController) {
        this.docController = aDocController;
        this.isNewTemplate = false;
        this.currentTemplateName = null;
        try {
            this.templateDir = new File(this.docController.getPathUtils().getSystemPathFromFileURL(this.docController.getPathUtils().getPath(PathUtils.USER)) + File.separator + "template");
        } catch (final ConfigHandlerError e) {
            e.severe();
        }
        this.docController.getLogger().info("TemplateController initialized!");
    }

    /**
     * Returns directory for templates.
     *
     * @return Template directory
     */
    public File getTemplateDir() {
        return this.templateDir;
    }

    /**
     * Returns current template settings collected as a new
     * {@link ast.template.Template} object.
     *
     * @return Current template settings as new Template object
     * @throws com.sun.star.uno.Exception If getting of template settings failed
     */
    public Template getCurrentTemplate() throws com.sun.star.uno.Exception {
        return new Template(this.isDuplex(), this.isTwoColumned(), this.getHeaderFooter(), this.getMarginTop(), this.getMarginBottom(), this.getMarginIn(), this.getMarginOut(), this.getPageNrPosition(), this.getAlignment(), this.getQuoteStyle(), this.isIndentation(), this.getFont(), this.getSize(), this.getHeadingsStyle(), this.docController.getMetadataController().getTemplateName());
    }

    /**
     * Refreshes internal style properties and link them to current document.
     */
    public void loadCurrentDocStyles() {
        final XStyleFamiliesSupplier xSupplier = (XStyleFamiliesSupplier) this.docController.getXInterface(XStyleFamiliesSupplier.class, this.docController.getXMSF());
        final XNameAccess xFamilies = (XNameAccess) this.docController.getXInterface(XNameAccess.class, xSupplier.getStyleFamilies());
        try {
            final XNameContainer xFamilyChar = (XNameContainer) this.docController.getXInterface(XNameContainer.class, xFamilies.getByName("CharacterStyles"));
            this.xCharStylePropsCitation = (XPropertySet) this.docController.getXInterface(XPropertySet.class, xFamilyChar.getByName("Citation"));
            final XNameContainer xFamilyPara = (XNameContainer) this.docController.getXInterface(XNameContainer.class, xFamilies.getByName("ParagraphStyles"));
            this.xParaStylePropsStandard = (XPropertySet) this.docController.getXInterface(XPropertySet.class, xFamilyPara.getByName("Standard"));
            this.xParaStylePropsFooter = (XPropertySet) this.docController.getXInterface(XPropertySet.class, xFamilyPara.getByName("Footer"));
            this.xParaStylePropsHeader = (XPropertySet) this.docController.getXInterface(XPropertySet.class, xFamilyPara.getByName("Header"));
            this.xParaStylePropsFooterLeft = (XPropertySet) this.docController.getXInterface(XPropertySet.class, xFamilyPara.getByName("Footer left"));
            this.xParaStylePropsHeaderLeft = (XPropertySet) this.docController.getXInterface(XPropertySet.class, xFamilyPara.getByName("Header left"));
            this.xParaStylePropsFooterRight = (XPropertySet) this.docController.getXInterface(XPropertySet.class, xFamilyPara.getByName("Footer right"));
            this.xParaStylePropsHeaderRight = (XPropertySet) this.docController.getXInterface(XPropertySet.class, xFamilyPara.getByName("Header right"));
            this.xParaStylePropsHeading = (XPropertySet) this.docController.getXInterface(XPropertySet.class, xFamilyPara.getByName("Heading"));
            final XNameContainer xFamilyPage = (XNameContainer) this.docController.getXInterface(XNameContainer.class, xFamilies.getByName("PageStyles"));
            this.xPageStyleProps = new XPropertySet[] { (XPropertySet) this.docController.getXInterface(XPropertySet.class, xFamilyPage.getByName("Standard")), (XPropertySet) this.docController.getXInterface(XPropertySet.class, xFamilyPage.getByName("Left Page")), (XPropertySet) this.docController.getXInterface(XPropertySet.class, xFamilyPage.getByName("Right Page")) };
            if (!xFamilyPara.hasByName(this.docController.getLanguageController().__("Affidavit"))) {
                final XStyle xNewStyleAffidavit = (XStyle) this.docController.getXInterface(XStyle.class, this.docController.getXMSF().createInstance("com.sun.star.style.ParagraphStyle"));
                this.xParaStylePropsAffidavit = (XPropertySet) this.docController.getXInterface(XPropertySet.class, xNewStyleAffidavit);
                xNewStyleAffidavit.setParentStyle("Standard");
                xFamilyPara.insertByName(this.docController.getLanguageController().__("Affidavit"), xNewStyleAffidavit);
            } else this.xParaStylePropsAffidavit = (XPropertySet) this.docController.getXInterface(XPropertySet.class, xFamilyPara.getByName(this.docController.getLanguageController().__("Affidavit")));
            if (!xFamilyPara.hasByName(this.docController.getLanguageController().__("Affidavit Heading"))) {
                final XStyle xNewStyleAffidavitHeading = (XStyle) this.docController.getXInterface(XStyle.class, this.docController.getXMSF().createInstance("com.sun.star.style.ParagraphStyle"));
                this.xParaStylePropsAffidavitHeading = (XPropertySet) this.docController.getXInterface(XPropertySet.class, xNewStyleAffidavitHeading);
                xNewStyleAffidavitHeading.setParentStyle("Heading");
                xFamilyPara.insertByName(this.docController.getLanguageController().__("Affidavit Heading"), xNewStyleAffidavitHeading);
            } else this.xParaStylePropsAffidavitHeading = (XPropertySet) this.docController.getXInterface(XPropertySet.class, xFamilyPara.getByName(this.docController.getLanguageController().__("Affidavit Heading")));
            if (!xFamilyPage.hasByName(this.docController.getLanguageController().__("Affidavit Page"))) {
                final XStyle xNewStyleAffidavitPage = (XStyle) this.docController.getXInterface(XStyle.class, this.docController.getXMSF().createInstance("com.sun.star.style.PageStyle"));
                xNewStyleAffidavitPage.setParentStyle("Standard");
                this.xPageStylePropsAffidavitPage = (XPropertySet) this.docController.getXInterface(XPropertySet.class, xNewStyleAffidavitPage);
                xFamilyPage.insertByName(this.docController.getLanguageController().__("Affidavit Page"), xNewStyleAffidavitPage);
            } else this.xPageStylePropsAffidavitPage = (XPropertySet) this.docController.getXInterface(XPropertySet.class, xFamilyPage.getByName(this.docController.getLanguageController().__("Affidavit Page")));
            this.xParaStylePropsAffidavitHeading.setPropertyValue("ParaAdjust", ParagraphAdjust.CENTER);
            this.xParaStylePropsAffidavit.setPropertyValue("ParaAdjust", ParagraphAdjust.BLOCK);
            this.xPageStylePropsAffidavitPage.setPropertyValue("RightMargin", 6500);
            this.xPageStylePropsAffidavitPage.setPropertyValue("LeftMargin", 6500);
            this.xPageStylePropsAffidavitPage.setPropertyValue("BottomMargin", 5000);
            this.xPageStylePropsAffidavitPage.setPropertyValue("TopMargin", 5000);
            this.currentTemplate = this.getCurrentTemplate();
        } catch (final com.sun.star.uno.Exception e) {
            new TemplateError(e).severe();
        }
    }

    /**
     * Generates the initial document structure with front page, affidavit,
     * table of contents and new page to start writing. All is based on current
     * template settings and if the scientific paper is set to "Paper" no
     * affidavit or table of contents is being generated.
     *
     * @throws TemplateError If generating document structure failed
     */
    public void fitDoc() throws TemplateError {
        final XTextDocument xTextDocument = (XTextDocument) this.docController.getXInterface(XTextDocument.class, this.docController.getXFrame().getController().getModel());
        try {
            this.xPageStyleProps[2].setPropertyValue("FollowStyle", "Left Page");
            this.xPageStyleProps[1].setPropertyValue("FollowStyle", "Right Page");
            final XText xText = xTextDocument.getText();
            final XTextViewCursorSupplier xTextViewCursorSupplier = (XTextViewCursorSupplier) this.docController.getXInterface(XTextViewCursorSupplier.class, this.docController.getXFrame().getController());
            final XTextViewCursor xTextViewCursor = xTextViewCursorSupplier.getViewCursor();
            final XPropertySet xTextViewCursorPropSet = (XPropertySet) this.docController.getXInterface(XPropertySet.class, xTextViewCursor);
            xTextViewCursorPropSet.setPropertyValue("ParaStyleName", "Standard");
            xTextViewCursorPropSet.setPropertyValue("PageDescName", this.isDuplex() ? "Right Page" : "Standard");
            if (!this.docController.getPropertyHandler().isASTDocument()) {
                xTextViewCursorPropSet.setPropertyValue("PageDescName", "First Page");
                xTextViewCursorPropSet.setPropertyValue("ParaAdjust", ParagraphAdjust.CENTER);
                xTextViewCursorPropSet.setPropertyValue("CharHeight", 13f);
                xText.insertString(xTextViewCursor, this.docController.getMetadataController().getAuthors().get(0).getInstitution(), false);
                xTextViewCursorPropSet.setPropertyValue("CharHeight", 24f);
                xText.insertControlCharacter(xTextViewCursor, ControlCharacter.LINE_BREAK, false);
                xText.insertControlCharacter(xTextViewCursor, ControlCharacter.LINE_BREAK, false);
                xText.insertControlCharacter(xTextViewCursor, ControlCharacter.LINE_BREAK, false);
                xText.insertControlCharacter(xTextViewCursor, ControlCharacter.LINE_BREAK, false);
                xText.insertControlCharacter(xTextViewCursor, ControlCharacter.LINE_BREAK, false);
                xText.insertControlCharacter(xTextViewCursor, ControlCharacter.LINE_BREAK, false);
                xText.insertControlCharacter(xTextViewCursor, ControlCharacter.LINE_BREAK, false);
                xText.insertControlCharacter(xTextViewCursor, ControlCharacter.LINE_BREAK, false);
                xTextViewCursorPropSet.setPropertyValue("CharHeight", 14f);
                xTextViewCursorPropSet.setPropertyValue("CharWeight", FontWeight.BOLD);
                xTextViewCursorPropSet.setPropertyValue("ParaSplit", true);
                xText.insertString(xTextViewCursor, this.getLocalizedTypeOfWork(this.docController.getMetadataController().getTypeOfWork(), false), false);
                xText.insertControlCharacter(xTextViewCursor, ControlCharacter.LINE_BREAK, false);
                xText.insertControlCharacter(xTextViewCursor, ControlCharacter.LINE_BREAK, false);
                xText.insertControlCharacter(xTextViewCursor, ControlCharacter.LINE_BREAK, false);
                xTextViewCursorPropSet.setPropertyValue("CharHeight", 15f);
                xTextViewCursorPropSet.setPropertyValue("CharWeight", FontWeight.BOLD);
                xTextViewCursorPropSet.setPropertyValue("ParaSplit", true);
                xText.insertString(xTextViewCursor, this.docController.getMetadataController().getTitle(), false);
                xTextViewCursorPropSet.setPropertyValue("CharHeight", 8f);
                xText.insertControlCharacter(xTextViewCursor, ControlCharacter.LINE_BREAK, false);
                xText.insertControlCharacter(xTextViewCursor, ControlCharacter.LINE_BREAK, false);
                xTextViewCursorPropSet.setPropertyValue("CharHeight", 15f);
                xTextViewCursorPropSet.setPropertyValue("CharWeight", FontWeight.NORMAL);
                xTextViewCursorPropSet.setPropertyValue("ParaSplit", true);
                xText.insertString(xTextViewCursor, this.docController.getMetadataController().getSubtitle(), false);
                xText.insertControlCharacter(xTextViewCursor, ControlCharacter.LINE_BREAK, false);
                xText.insertControlCharacter(xTextViewCursor, ControlCharacter.LINE_BREAK, false);
                xText.insertControlCharacter(xTextViewCursor, ControlCharacter.LINE_BREAK, false);
                xText.insertControlCharacter(xTextViewCursor, ControlCharacter.LINE_BREAK, false);
                xText.insertControlCharacter(xTextViewCursor, ControlCharacter.LINE_BREAK, false);
                xText.insertControlCharacter(xTextViewCursor, ControlCharacter.LINE_BREAK, false);
                xText.insertControlCharacter(xTextViewCursor, ControlCharacter.LINE_BREAK, false);
                xText.insertControlCharacter(xTextViewCursor, ControlCharacter.LINE_BREAK, false);
                xText.insertControlCharacter(xTextViewCursor, ControlCharacter.LINE_BREAK, false);
                xText.insertControlCharacter(xTextViewCursor, ControlCharacter.LINE_BREAK, false);
                xText.insertControlCharacter(xTextViewCursor, ControlCharacter.LINE_BREAK, false);
                xTextViewCursorPropSet.setPropertyValue("CharHeight", 13f);
                if (this.docController.getStatController().getTimelineEndDate() != null) {
                    xText.insertString(xTextViewCursor, this.docController.getLanguageController().__("Submission date") + ": " + DateFormat.getDateInstance(DateFormat.MEDIUM).format(this.docController.getStatController().getTimelineEndDate().getTime()), false);
                } else {
                    xText.insertString(xTextViewCursor, this.docController.getLanguageController().__("Submission date") + ": " + DateFormat.getDateInstance(DateFormat.MEDIUM).format(Calendar.getInstance().getTime()), false);
                }
                xTextViewCursorPropSet.setPropertyValue("CharHeight", 24f);
                xText.insertControlCharacter(xTextViewCursor, ControlCharacter.LINE_BREAK, false);
                xText.insertControlCharacter(xTextViewCursor, ControlCharacter.LINE_BREAK, false);
                xText.insertControlCharacter(xTextViewCursor, ControlCharacter.LINE_BREAK, false);
                xText.insertControlCharacter(xTextViewCursor, ControlCharacter.LINE_BREAK, false);
                xText.insertControlCharacter(xTextViewCursor, ControlCharacter.LINE_BREAK, false);
                xText.insertControlCharacter(xTextViewCursor, ControlCharacter.LINE_BREAK, false);
                xText.insertControlCharacter(xTextViewCursor, ControlCharacter.PARAGRAPH_BREAK, false);
                xTextViewCursorPropSet.setPropertyValue("ParaAdjust", ParagraphAdjust.LEFT);
                xTextViewCursorPropSet.setPropertyValue("CharHeight", 12f);
                xText.insertString(xTextViewCursor, this.docController.getLanguageController().__("submitted by:"), false);
                xText.insertControlCharacter(xTextViewCursor, ControlCharacter.LINE_BREAK, false);
                for (final AuthorData curAuthorData : this.docController.getMetadataController().getAuthors()) xText.insertString(xTextViewCursor, this.docController.getUserController().getUser(curAuthorData.getID()).getSurname() + " " + this.docController.getUserController().getUser(curAuthorData.getID()).getName(), false);
                xText.insertControlCharacter(xTextViewCursor, ControlCharacter.LINE_BREAK, false);
                xTextViewCursorPropSet.setPropertyValue("BreakType", new Integer(com.sun.star.style.BreakType.PAGE_AFTER_value));
                xText.insertControlCharacter(xTextViewCursor, ControlCharacter.PARAGRAPH_BREAK, false);
                xTextViewCursorPropSet.setPropertyValue("BreakType", new Integer(com.sun.star.style.BreakType.NONE_value));
                xTextViewCursorPropSet.setPropertyValue("PageDescName", this.isDuplex() ? "Left Page" : "Standard");
                xTextViewCursorPropSet.setPropertyValue("PageDescName", this.docController.getLanguageController().__("Affidavit Page"));
                switch(this.docController.getMetadataController().getTypeOfWork()) {
                    case PAPER:
                        break;
                    default:
                        xTextViewCursorPropSet.setPropertyValue("ParaStyleName", this.docController.getLanguageController().__("Affidavit Heading"));
                        xText.insertString(xTextViewCursor, this.docController.getLanguageController().__("Affidavit"), false);
                        xText.insertControlCharacter(xTextViewCursor, ControlCharacter.PARAGRAPH_BREAK, false);
                        xTextViewCursorPropSet.setPropertyValue("ParaStyleName", this.docController.getLanguageController().__("Affidavit"));
                        xText.insertString(xTextViewCursor, this.docController.getLanguageController().__("I hereby declare that the following {0} {1}{2}{3} has been written only by the undersigned and without any assistance from third parties.\nFurthermore, I confirm that no sources have been used in the preparation of this thesis other than those indicated in the thesis itself.", new String[] { this.getLocalizedTypeOfWork(this.docController.getMetadataController().getTypeOfWork(), true), this.docController.getLanguageController().__("quoteLeft"), this.docController.getMetadataController().getTitle(), this.docController.getLanguageController().__("quoteRight") }), false);
                        xTextViewCursorPropSet.setPropertyValue("BreakType", new Integer(com.sun.star.style.BreakType.PAGE_AFTER_value));
                        xText.insertControlCharacter(xTextViewCursor, ControlCharacter.PARAGRAPH_BREAK, false);
                        xTextViewCursorPropSet.setPropertyValue("BreakType", new Integer(com.sun.star.style.BreakType.NONE_value));
                        break;
                }
                xTextViewCursorPropSet.setPropertyValue("PageDescName", "Standard");
                switch(this.docController.getMetadataController().getTypeOfWork()) {
                    case PAPER:
                        break;
                    default:
                        final Object oContentIndex = this.docController.getXMSF().createInstance("com.sun.star.text.ContentIndex");
                        final XNamed xNamedIndex = (XNamed) this.docController.getXInterface(XNamed.class, oContentIndex);
                        xNamedIndex.setName("ASTContent Index 1");
                        final XPropertySet xContentIndexProps = (XPropertySet) this.docController.getXInterface(XPropertySet.class, oContentIndex);
                        xContentIndexProps.setPropertyValue("IsProtected", Boolean.TRUE);
                        xContentIndexProps.setPropertyValue("CreateFromOutline", Boolean.TRUE);
                        XIndexReplace xIndexReplace = (XIndexReplace) this.docController.getXInterface(XIndexReplace.class, xContentIndexProps.getPropertyValue("LevelFormat"));
                        PropertyValue[][] propertyvalues = new PropertyValue[5][];
                        PropertyValue[] propertyvaluesentry = new PropertyValue[3];
                        propertyvaluesentry = new PropertyValue[2];
                        propertyvalues[0] = propertyvaluesentry;
                        PropertyValue pv = new PropertyValue();
                        propertyvaluesentry[0] = pv;
                        pv.Name = "TokenType";
                        pv.Value = "TokenEntryNumber";
                        pv = new PropertyValue();
                        propertyvaluesentry[1] = pv;
                        pv.Name = "CharacterStyleName";
                        pv.Value = "";
                        propertyvaluesentry = new PropertyValue[3];
                        propertyvalues[1] = propertyvaluesentry;
                        pv = new PropertyValue();
                        propertyvaluesentry[0] = pv;
                        pv.Name = "TokenType";
                        pv.Value = "TokenText";
                        pv = new PropertyValue();
                        propertyvaluesentry[1] = pv;
                        pv.Name = "CharacterStyleName";
                        pv.Value = "";
                        pv = new PropertyValue();
                        propertyvaluesentry[2] = pv;
                        pv.Name = "Text";
                        pv.Value = " ";
                        propertyvaluesentry = new PropertyValue[2];
                        propertyvalues[2] = propertyvaluesentry;
                        pv = new PropertyValue();
                        propertyvaluesentry[0] = pv;
                        pv.Name = "TokenType";
                        pv.Value = "TokenEntryText";
                        pv = new PropertyValue();
                        propertyvaluesentry[1] = pv;
                        pv.Name = "CharacterStyleName";
                        pv.Value = "";
                        propertyvaluesentry = new PropertyValue[5];
                        propertyvalues[3] = propertyvaluesentry;
                        pv = new PropertyValue();
                        propertyvaluesentry[0] = pv;
                        pv.Name = "TokenType";
                        pv.Value = "TokenTabStop";
                        pv = new PropertyValue();
                        propertyvaluesentry[1] = pv;
                        pv.Name = "TabStopPosition";
                        pv.Value = 0;
                        pv = new PropertyValue();
                        propertyvaluesentry[2] = pv;
                        pv.Name = "TabStopRightAligned";
                        pv.Value = true;
                        pv = new PropertyValue();
                        propertyvaluesentry[3] = pv;
                        pv.Name = "TabStopFillCharacter";
                        pv.Value = new String(".");
                        pv = new PropertyValue();
                        propertyvaluesentry[4] = pv;
                        pv.Name = "CharacterStyleName";
                        pv.Value = "";
                        propertyvaluesentry = new PropertyValue[2];
                        propertyvalues[4] = propertyvaluesentry;
                        pv = new PropertyValue();
                        propertyvaluesentry[0] = pv;
                        pv.Name = "TokenType";
                        pv.Value = "TokenPageNumber";
                        pv = new PropertyValue();
                        propertyvaluesentry[1] = pv;
                        pv.Name = "CharacterStyleName";
                        pv.Value = "";
                        for (short i = 1; i <= 10; i++) xIndexReplace.replaceByIndex(i, propertyvalues);
                        final XDocumentIndex xDocumentIndex = (XDocumentIndex) this.docController.getXInterface(XDocumentIndex.class, oContentIndex);
                        xText.insertTextContent(xTextViewCursor, xDocumentIndex, false);
                        xDocumentIndex.update();
                        xTextViewCursorPropSet.setPropertyValue("BreakType", new Integer(com.sun.star.style.BreakType.PAGE_AFTER_value));
                        xText.insertControlCharacter(xTextViewCursor, ControlCharacter.PARAGRAPH_BREAK, false);
                        xTextViewCursorPropSet.setPropertyValue("BreakType", new Integer(com.sun.star.style.BreakType.NONE_value));
                        break;
                }
            }
            xTextViewCursorPropSet.setPropertyValue("ParaStyleName", "Standard");
            xTextViewCursorPropSet.setPropertyValue("PageDescName", this.isDuplex() ? "Left Page" : "Standard");
        } catch (final com.sun.star.uno.Exception e) {
            throw new TemplateError("Can't generate document structure!", e);
        }
    }

    /**
     * Saves the current styles in a template file.
     *
     * @throws TemplateError If storing template failed
     */
    public void saveAsTemplate() throws TemplateError {
        final XTextDocument xTextDocument = (XTextDocument) this.docController.getXInterface(XTextDocument.class, this.docController.getXFrame().getController().getModel());
        final XStorable xStore = (XStorable) this.docController.getXInterface(XStorable.class, xTextDocument);
        final PropertyValue[] lProperties = new PropertyValue[2];
        lProperties[0] = new PropertyValue();
        lProperties[0].Name = "AsTemplate";
        lProperties[0].Value = new Boolean(true);
        lProperties[1] = new PropertyValue();
        lProperties[1].Name = "Overwrite";
        lProperties[1].Value = new Boolean(true);
        try {
            final String url = this.getTemplateDir().getAbsolutePath() + File.separator + this.docController.getMetadataController().getTemplateName() + ".ott";
            xStore.storeToURL(this.docController.getPathUtils().getFileURLFromSystemPath(url, url), lProperties);
        } catch (final com.sun.star.uno.Exception e) {
            throw new TemplateError("Can't store template!", e);
        } catch (final ConfigHandlerError e) {
            throw new TemplateError(e.getLogMessage(), e);
        }
    }

    /**
     * Returns the left (one-sided) or inside margin (duplex) in cm.
     *
     * @return the marginIn
     * @throws com.sun.star.uno.Exception
     */
    public Double getMarginIn() throws com.sun.star.uno.Exception {
        return Double.parseDouble(this.xPageStyleProps[0].getPropertyValue("LeftMargin").toString()) / 1000.00;
    }

    /**
     * Sets the left (one-sided) or the inside margin (duplex) in cm.
     *
     * @param aMarginIn the left or inside margin
     * @throws com.sun.star.uno.Exception
     */
    public void setMarginIn(final Double aMarginIn) throws com.sun.star.uno.Exception {
        for (final XPropertySet curentProbs : this.xPageStyleProps) {
            curentProbs.setPropertyValue("LeftMargin", ((Double) (aMarginIn * 1000)).intValue());
        }
    }

    /**
     * Returns the right (one-sided) or the outside margin (duplex) in cm.
     *
     * @return the marginOut
     * @throws com.sun.star.uno.Exception
     */
    public Double getMarginOut() throws com.sun.star.uno.Exception {
        return Double.parseDouble(this.xPageStyleProps[0].getPropertyValue("RightMargin").toString()) / 1000.00;
    }

    /**
     * Set the right (one-sided) or the outside margin (duplex) in cm.
     *
     * @param aMarginOut the right or outside margin
     * @throws com.sun.star.uno.Exception
     */
    public void setMarginOut(final Double aMarginOut) throws com.sun.star.uno.Exception {
        for (final XPropertySet curentProbs : this.xPageStyleProps) {
            curentProbs.setPropertyValue("RightMargin", ((Double) (aMarginOut * 1000)).intValue());
        }
    }

    /**
     * Returns the top margin in cm.
     *
     * @return the marginTop
     * @throws com.sun.star.uno.Exception
     */
    public Double getMarginTop() throws com.sun.star.uno.Exception {
        return Double.parseDouble(this.xPageStyleProps[0].getPropertyValue("TopMargin").toString()) / 1000.00;
    }

    /**
     * Sets the top margin in cm.
     *
     * @param aMarginTop the top margin
     * @throws com.sun.star.uno.Exception
     */
    public void setMarginTop(final Double aMarginTop) throws com.sun.star.uno.Exception {
        for (final XPropertySet curentProbs : this.xPageStyleProps) {
            curentProbs.setPropertyValue("TopMargin", ((Double) (aMarginTop * 1000)).intValue());
        }
    }

    /**
     * Returns the bottom margin in cm.
     *
     * @return the marginBottom
     * @throws com.sun.star.uno.Exception
     */
    public Double getMarginBottom() throws com.sun.star.uno.Exception {
        return Double.parseDouble(this.xPageStyleProps[0].getPropertyValue("BottomMargin").toString()) / 1000.00;
    }

    /**
     * Sets the bottom margin in cm.
     *
     * @param aMarginBottom the bottom margin
     * @throws com.sun.star.uno.Exception
     */
    public void setMarginBottom(final Double aMarginBottom) throws com.sun.star.uno.Exception {
        for (final XPropertySet curentProbs : this.xPageStyleProps) {
            curentProbs.setPropertyValue("BottomMargin", ((Double) (aMarginBottom * 1000)).intValue());
        }
    }

    /**
     * Returns the alignment (left-aligned, right-aligned, centered or grouped style).
     *
     * @return the alignment
     * @throws com.sun.star.uno.Exception
     */
    public ParagraphAdjust getAlignment() throws com.sun.star.uno.Exception {
        return ParagraphAdjust.fromInt(Integer.parseInt(this.xParaStylePropsStandard.getPropertyValue("ParaAdjust").toString()));
    }

    /**
     * Sets the alignment (left-aligned, right-aligned, centerd or grouped style).
     *
     * @param aAlignment the alignment
     * @throws com.sun.star.uno.Exception
     */
    public void setAlignment(final ParagraphAdjust aAlignment) throws com.sun.star.uno.Exception {
        this.xParaStylePropsStandard.setPropertyValue("ParaAdjust", aAlignment);
    }

    /**
     * Returns the position of the page number (left, center or right).
     *
     * @return the pageNrPosition
     * @throws com.sun.star.uno.Exception
     */
    public ParagraphAdjust getPageNrPosition() throws com.sun.star.uno.Exception {
        return ParagraphAdjust.fromInt(Integer.parseInt(this.xParaStylePropsFooter.getPropertyValue("ParaAdjust").toString()));
    }

    /**
     * Sets the position of the page number (left, center or right).
     *
     * @param nPageNrPosition position of the page number
     * @throws com.sun.star.uno.Exception
     */
    public void setPageNrPosition(final ParagraphAdjust nPageNrPosition) throws com.sun.star.uno.Exception {
        this.xParaStylePropsFooter.setPropertyValue("ParaAdjust", nPageNrPosition);
        this.xParaStylePropsHeader.setPropertyValue("ParaAdjust", nPageNrPosition);
        this.xParaStylePropsFooterRight.setPropertyValue("ParaAdjust", nPageNrPosition);
        this.xParaStylePropsHeaderRight.setPropertyValue("ParaAdjust", nPageNrPosition);
        switch(nPageNrPosition.getValue()) {
            case ParagraphAdjust.LEFT_value:
                this.xParaStylePropsFooterLeft.setPropertyValue("ParaAdjust", ParagraphAdjust.RIGHT);
                this.xParaStylePropsHeaderLeft.setPropertyValue("ParaAdjust", ParagraphAdjust.RIGHT);
                break;
            case ParagraphAdjust.CENTER_value:
                this.xParaStylePropsFooterLeft.setPropertyValue("ParaAdjust", ParagraphAdjust.CENTER);
                this.xParaStylePropsHeaderLeft.setPropertyValue("ParaAdjust", ParagraphAdjust.CENTER);
                break;
            case ParagraphAdjust.RIGHT_value:
                this.xParaStylePropsFooterLeft.setPropertyValue("ParaAdjust", ParagraphAdjust.LEFT);
                this.xParaStylePropsHeaderLeft.setPropertyValue("ParaAdjust", ParagraphAdjust.LEFT);
                break;
        }
    }

    /**
     * Returns the current font.
     *
     * @return current font
     * @throws com.sun.star.uno.Exception
     */
    public String getFont() throws com.sun.star.uno.Exception {
        return (String) this.xParaStylePropsStandard.getPropertyValue("CharFontName");
    }

    /**
     * Sets the current font.
     *
     * @param sFont current font
     * @throws com.sun.star.uno.Exception
     */
    public void setFont(final String sFont) throws com.sun.star.uno.Exception {
        this.xParaStylePropsStandard.setPropertyValue("CharFontName", sFont);
    }

    /**
     * Returns the current font size.
     *
     * @return Current font size
     * @throws com.sun.star.uno.Exception
     */
    public Float getSize() throws com.sun.star.uno.Exception {
        return (Float) this.xParaStylePropsStandard.getPropertyValue("CharHeight");
    }

    /**
     * Sets the current font size.
     *
     * @param nSize Current font size
     * @throws com.sun.star.uno.Exception
     */
    public void setSize(final float nSize) throws com.sun.star.uno.Exception {
        this.xParaStylePropsStandard.setPropertyValue("CharHeight", nSize);
    }

    /**
     * Retunrns if the document contains a header, footer or none.
     *
     * @return the header
     * @throws com.sun.star.uno.Exception
     */
    public HeaderFooter getHeaderFooter() throws com.sun.star.uno.Exception {
        if (this.xPageStyleProps[0].getPropertyValue("HeaderIsOn").equals(true)) {
            return HeaderFooter.HEADER;
        } else if (this.xPageStyleProps[0].getPropertyValue("FooterIsOn").equals(true)) {
            return HeaderFooter.FOOTER;
        } else return HeaderFooter.NONE;
    }

    /**
     * Sets if the document contains a header, footer or none.
     *
     * @param aHeaderFooter header, footer or none
     * @throws com.sun.star.uno.Exception
     */
    public void setHeaderFooter(final HeaderFooter aHeaderFooter) throws com.sun.star.uno.Exception {
        for (int i = 0; i <= 2; i++) {
            if (this.xPageStyleProps[i].getPropertyValue("HeaderIsOn").equals(true)) ((XText) this.docController.getXInterface(XText.class, this.xPageStyleProps[i].getPropertyValue("HeaderText"))).setString("");
            if (this.xPageStyleProps[i].getPropertyValue("FooterIsOn").equals(true)) ((XText) this.docController.getXInterface(XText.class, this.xPageStyleProps[i].getPropertyValue("FooterText"))).setString("");
        }
        switch(aHeaderFooter) {
            case FOOTER:
                for (XPropertySet curentProbs : this.xPageStyleProps) {
                    curentProbs.setPropertyValue("FooterIsOn", true);
                    curentProbs.setPropertyValue("HeaderIsOn", false);
                }
                break;
            case HEADER:
                for (XPropertySet curentProbs : this.xPageStyleProps) {
                    curentProbs.setPropertyValue("FooterIsOn", false);
                    curentProbs.setPropertyValue("HeaderIsOn", true);
                }
                break;
            case NONE:
                for (XPropertySet curentProbs : this.xPageStyleProps) {
                    curentProbs.setPropertyValue("FooterIsOn", false);
                    curentProbs.setPropertyValue("HeaderIsOn", false);
                }
                break;
        }
        for (final String currentPage : this.page) {
            if (this.getHeaderFooter() != HeaderFooter.NONE) {
                XText footerHeaderText = null;
                final Object pageNumber = this.docController.getXMSF().createInstance("com.sun.star.text.TextField.PageNumber");
                final XTextContent pageCountTC = (XTextContent) this.docController.getXInterface(XTextContent.class, pageNumber);
                final XPropertySet pageCountPS = (XPropertySet) this.docController.getXInterface(XPropertySet.class, pageNumber);
                pageCountPS.setPropertyValue("NumberingType", new Short(com.sun.star.style.NumberingType.ARABIC));
                if (currentPage.equals("")) {
                    footerHeaderText = (XText) this.docController.getXInterface(XText.class, this.xPageStyleProps[0].getPropertyValue(this.getHeaderFooter().firstToUpperCase() + "Text"));
                } else if (currentPage.equals(" left")) {
                    footerHeaderText = (XText) this.docController.getXInterface(XText.class, this.xPageStyleProps[1].getPropertyValue(this.getHeaderFooter().firstToUpperCase() + "Text"));
                } else if (currentPage.equals(" right")) {
                    footerHeaderText = (XText) this.docController.getXInterface(XText.class, this.xPageStyleProps[2].getPropertyValue(this.getHeaderFooter().firstToUpperCase() + "Text"));
                }
                final XTextCursor footerHeaderCursor = footerHeaderText.createTextCursor();
                final XPropertySet xFooterHeaderCursorProps = (XPropertySet) this.docController.getXInterface(XPropertySet.class, footerHeaderCursor);
                xFooterHeaderCursorProps.setPropertyValue("ParaStyleName", this.getHeaderFooter().firstToUpperCase() + currentPage);
                footerHeaderText.insertTextContent(footerHeaderText.createTextCursor(), pageCountTC, true);
            }
        }
    }

    /**
     * Returns the quotation style.
     *
     * @return the quotation style
     * @throws com.sun.star.uno.Exception
     */
    public QuoteStyle getQuoteStyle() throws com.sun.star.uno.Exception {
        if (this.xCharStylePropsCitation.getPropertyValue("CharWeight").equals(FontWeight.BOLD)) {
            return QuoteStyle.BOLD;
        } else if (this.xCharStylePropsCitation.getPropertyValue("CharPosture").equals(FontSlant.ITALIC)) {
            return QuoteStyle.ITALIC;
        } else return QuoteStyle.NONE;
    }

    /**
     * Sets the quotation style.
     *
     * @param aQuoteStyle the Quotestyle
     * @throws com.sun.star.uno.Exception
     */
    public void setQuoteStyle(final QuoteStyle aQuoteStyle) throws com.sun.star.uno.Exception {
        switch(aQuoteStyle) {
            case BOLD:
                this.xCharStylePropsCitation.setPropertyValue("CharWeight", FontWeight.BOLD);
                this.xCharStylePropsCitation.setPropertyValue("CharPosture", FontSlant.NONE);
                break;
            case ITALIC:
                this.xCharStylePropsCitation.setPropertyValue("CharWeight", FontWeight.NORMAL);
                this.xCharStylePropsCitation.setPropertyValue("CharPosture", FontSlant.ITALIC);
                break;
            case NONE:
                this.xCharStylePropsCitation.setPropertyValue("CharWeight", FontWeight.NORMAL);
                this.xCharStylePropsCitation.setPropertyValue("CharPosture", FontSlant.NONE);
                break;
        }
    }

    /**
     * Returns the layout for duplex printing.
     *
     * @return the duplex
     * @throws com.sun.star.uno.Exception
     */
    public boolean isDuplex() throws com.sun.star.uno.Exception {
        final XStyleFamiliesSupplier xSupplier = (XStyleFamiliesSupplier) this.docController.getXInterface(XStyleFamiliesSupplier.class, this.docController.getXMSF());
        final XNameAccess xFamilies = (XNameAccess) this.docController.getXInterface(XNameAccess.class, xSupplier.getStyleFamilies());
        final XNameContainer xFamilyPage = (XNameContainer) this.docController.getXInterface(XNameContainer.class, xFamilies.getByName("PageStyles"));
        if (xFamilyPage.hasByName("IsDuplex")) {
            final XPropertySet xPageStyle = (XPropertySet) this.docController.getXInterface(XPropertySet.class, xFamilyPage.getByName("IsDuplex"));
            return Boolean.parseBoolean(xPageStyle.getPropertyValue("FooterIsOn").toString());
        } else {
            return false;
        }
    }

    /**
     * Sets the layout for duplex printing.
     *
     * @param bDuplex Layout for duplex printing
     * @throws com.sun.star.uno.Exception
     */
    public void setDuplex(final boolean bDuplex) throws com.sun.star.uno.Exception {
        final XStyleFamiliesSupplier xSupplier = (XStyleFamiliesSupplier) this.docController.getXInterface(XStyleFamiliesSupplier.class, this.docController.getXMSF());
        final XNameAccess xFamilies = (XNameAccess) this.docController.getXInterface(XNameAccess.class, xSupplier.getStyleFamilies());
        if (bDuplex) {
            this.xPageStyleProps[1].setPropertyValue("PageStyleLayout", PageStyleLayout.MIRRORED);
            this.xPageStyleProps[2].setPropertyValue("PageStyleLayout", PageStyleLayout.MIRRORED);
        } else {
            this.xPageStyleProps[1].setPropertyValue("PageStyleLayout", PageStyleLayout.ALL);
            this.xPageStyleProps[2].setPropertyValue("PageStyleLayout", PageStyleLayout.ALL);
        }
        final XNameContainer xFamilyPage = (XNameContainer) this.docController.getXInterface(XNameContainer.class, xFamilies.getByName("PageStyles"));
        if (xFamilyPage.hasByName("IsDuplex")) {
            XPropertySet xPageStyle = (XPropertySet) this.docController.getXInterface(XPropertySet.class, xFamilyPage.getByName("IsDuplex"));
            xPageStyle.setPropertyValue("FooterIsOn", bDuplex);
        } else {
            final XStyle xNewStyle = (XStyle) this.docController.getXInterface(XStyle.class, this.docController.getXMSF().createInstance("com.sun.star.style.PageStyle"));
            final XPropertySet xPageStyle = (XPropertySet) this.docController.getXInterface(XPropertySet.class, xNewStyle);
            xFamilyPage.insertByName("IsDuplex", xNewStyle);
            xPageStyle.setPropertyValue("FooterIsOn", bDuplex);
        }
    }

    /**
     * Returns the layout quote idention.
     *
     * @return true if quote is with idention elde false
     * @throws com.sun.star.uno.Exception
     */
    public boolean isIndentation() throws com.sun.star.uno.Exception {
        final XStyleFamiliesSupplier xSupplier = (XStyleFamiliesSupplier) this.docController.getXInterface(XStyleFamiliesSupplier.class, this.docController.getXMSF());
        final XNameAccess xFamilies = (XNameAccess) this.docController.getXInterface(XNameAccess.class, xSupplier.getStyleFamilies());
        final XNameContainer xFamilyPage = (XNameContainer) this.docController.getXInterface(XNameContainer.class, xFamilies.getByName("PageStyles"));
        if (xFamilyPage.hasByName("IsDuplex")) {
            final XPropertySet xPageStyle = (XPropertySet) this.docController.getXInterface(XPropertySet.class, xFamilyPage.getByName("IsDuplex"));
            return Boolean.parseBoolean(xPageStyle.getPropertyValue("HeaderIsOn").toString());
        } else {
            return false;
        }
    }

    /**
     * Sets the idention for quotes.
     *
     * @param bIdention Idention for qoutes
     * @throws com.sun.star.uno.Exception
     */
    public void setIndentation(final boolean bIdention) throws com.sun.star.uno.Exception {
        final XStyleFamiliesSupplier xSupplier = (XStyleFamiliesSupplier) this.docController.getXInterface(XStyleFamiliesSupplier.class, this.docController.getXMSF());
        final XNameAccess xFamilies = (XNameAccess) this.docController.getXInterface(XNameAccess.class, xSupplier.getStyleFamilies());
        final XNameContainer xFamilyPage = (XNameContainer) this.docController.getXInterface(XNameContainer.class, xFamilies.getByName("PageStyles"));
        if (xFamilyPage.hasByName("IsDuplex")) {
            XPropertySet xPageStyle = (XPropertySet) this.docController.getXInterface(XPropertySet.class, xFamilyPage.getByName("IsDuplex"));
            xPageStyle.setPropertyValue("HeaderIsOn", bIdention);
        } else {
            final XStyle xNewStyle = (XStyle) this.docController.getXInterface(XStyle.class, this.docController.getXMSF().createInstance("com.sun.star.style.PageStyle"));
            final XPropertySet xPageStyle = (XPropertySet) this.docController.getXInterface(XPropertySet.class, xNewStyle);
            xFamilyPage.insertByName("IsDuplex", xNewStyle);
            xPageStyle.setPropertyValue("HeaderIsOn", bIdention);
        }
    }

    /**
     * Returns the layout the headings.
     *
     * @return the titlestyle
     * @throws com.sun.star.uno.Exception
     */
    public HeadingsStyle getHeadingsStyle() throws com.sun.star.uno.Exception {
        final XStyleFamiliesSupplier xSupplier = (XStyleFamiliesSupplier) this.docController.getXInterface(XStyleFamiliesSupplier.class, this.docController.getXMSF());
        final XNameAccess xFamilies = (XNameAccess) this.docController.getXInterface(XNameAccess.class, xSupplier.getStyleFamilies());
        final XNameContainer xFamilyPage = (XNameContainer) this.docController.getXInterface(XNameContainer.class, xFamilies.getByName("PageStyles"));
        if (xFamilyPage.hasByName("IsDuplex")) {
            final XPropertySet xPageStyle = (XPropertySet) this.docController.getXInterface(XPropertySet.class, xFamilyPage.getByName("IsDuplex"));
            try {
                return HeadingsStyle.values()[(Short) xPageStyle.getPropertyValue("NumberingType")];
            } catch (final ArrayIndexOutOfBoundsException e) {
                return HeadingsStyle.values()[0];
            }
        } else {
            return HeadingsStyle.values()[0];
        }
    }

    /**
     * Sets the layout for the headings.
     *
     * @param aTitleStyle Layout for duplex printing
     * @throws com.sun.star.uno.Exception
     */
    public void setHeadingsStyle(final HeadingsStyle aTitleStyle) throws com.sun.star.uno.Exception {
        final XTextDocument xTextDocument = (XTextDocument) this.docController.getXInterface(XTextDocument.class, this.docController.getXFrame().getController().getModel());
        final XStyleFamiliesSupplier xSupplier = (XStyleFamiliesSupplier) this.docController.getXInterface(XStyleFamiliesSupplier.class, this.docController.getXMSF());
        final XNameAccess xFamilies = (XNameAccess) this.docController.getXInterface(XNameAccess.class, xSupplier.getStyleFamilies());
        final XNameContainer xFamilyPage = (XNameContainer) this.docController.getXInterface(XNameContainer.class, xFamilies.getByName("PageStyles"));
        if (xFamilyPage.hasByName("IsDuplex")) {
            XPropertySet xPageStyle = (XPropertySet) this.docController.getXInterface(XPropertySet.class, xFamilyPage.getByName("IsDuplex"));
            xPageStyle.setPropertyValue("NumberingType", aTitleStyle.ordinal());
            final XNameContainer xFamilyPara = (XNameContainer) this.docController.getXInterface(XNameContainer.class, xFamilies.getByName("ParagraphStyles"));
            for (int i = 0; i < this.getHeadingsStyle().getHeadingDescriptors().length; i++) {
                this.xParaStylePropsHeading = (XPropertySet) this.docController.getXInterface(XPropertySet.class, xFamilyPara.getByName("Heading " + String.valueOf(i + 1)));
                this.xParaStylePropsHeading.setPropertyValue("CharFontName", this.getHeadingsStyle().getHeadingDescriptors()[i].Name);
                this.xParaStylePropsHeading.setPropertyValue("CharPosture", this.getHeadingsStyle().getHeadingDescriptors()[i].Slant);
                this.xParaStylePropsHeading.setPropertyValue("CharWeight", this.getHeadingsStyle().getHeadingDescriptors()[i].Weight);
                this.xParaStylePropsHeading.setPropertyValue("CharHeight", this.getHeadingsStyle().getHeadingDescriptors()[i].Height);
                final XIndexReplace xChapterNumberingRules = ((XChapterNumberingSupplier) this.docController.getXInterface(XChapterNumberingSupplier.class, xTextDocument)).getChapterNumberingRules();
                final ArrayList<PropertyValue> aChapterNumberingList = new ArrayList<PropertyValue>();
                final PropertyValue aHeadingStyleName = new PropertyValue(), aIndentAt = new PropertyValue(), aListtabStopPosition = new PropertyValue(), aFirstLineIndent = new PropertyValue(), aNumberingType = new PropertyValue(), aSuffix = new PropertyValue(), aPrefix = new PropertyValue(), aParentNumbering = new PropertyValue();
                aHeadingStyleName.Name = "HeadingStyleName";
                aHeadingStyleName.Value = "Heading " + String.valueOf(i + 1);
                aChapterNumberingList.add(aHeadingStyleName);
                aIndentAt.Name = "IndentAt";
                aIndentAt.Value = this.getHeadingsStyle().getHeadingDescriptors()[i].indentAt;
                aChapterNumberingList.add(aIndentAt);
                aListtabStopPosition.Name = "ListtabStopPosition";
                aListtabStopPosition.Value = this.getHeadingsStyle().getHeadingDescriptors()[i].listtabStopPosition;
                aChapterNumberingList.add(aListtabStopPosition);
                aFirstLineIndent.Name = "FirstLineIndent";
                aFirstLineIndent.Value = this.getHeadingsStyle().getHeadingDescriptors()[i].firstLineIndent;
                aChapterNumberingList.add(aFirstLineIndent);
                switch(this.getHeadingsStyle().getHeadingDescriptors()[i].numberingType) {
                    case NumberingType.ARABIC:
                        aParentNumbering.Name = "ParentNumbering";
                        aParentNumbering.Value = new Short((short) (i + 1));
                        aChapterNumberingList.add(aParentNumbering);
                        break;
                    case NumberingType.CHARS_LOWER_LETTER:
                    case NumberingType.CHARS_UPPER_LETTER:
                    case NumberingType.ROMAN_UPPER:
                    case NumberingType.NUMBER_NONE:
                        aParentNumbering.Name = "ParentNumbering";
                        aParentNumbering.Value = new Short((short) 0);
                        aChapterNumberingList.add(aParentNumbering);
                        break;
                }
                aNumberingType.Name = "NumberingType";
                aNumberingType.Value = this.getHeadingsStyle().getHeadingDescriptors()[i].numberingType;
                aChapterNumberingList.add(aNumberingType);
                aSuffix.Name = "Suffix";
                aSuffix.Value = this.getHeadingsStyle().getHeadingDescriptors()[i].suffix;
                aChapterNumberingList.add(aSuffix);
                xChapterNumberingRules.replaceByIndex(i, aChapterNumberingList.toArray(new PropertyValue[aChapterNumberingList.size()]));
                aChapterNumberingList.clear();
            }
            this.xParaStylePropsHeading = (XPropertySet) this.docController.getXInterface(XPropertySet.class, xFamilyPara.getByName("Heading"));
            this.xParaStylePropsHeading.setPropertyValue("CharFontName", this.getHeadingsStyle().getHeadingDescriptors()[1].Name);
            this.xParaStylePropsHeading.setPropertyValue("CharPosture", this.getHeadingsStyle().getHeadingDescriptors()[1].Slant);
            this.xParaStylePropsHeading.setPropertyValue("CharWeight", this.getHeadingsStyle().getHeadingDescriptors()[1].Weight);
            this.xParaStylePropsHeading.setPropertyValue("CharHeight", this.getHeadingsStyle().getHeadingDescriptors()[1].Height);
        } else {
            final XStyle xNewStyle = (XStyle) this.docController.getXInterface(XStyle.class, this.docController.getXMSF().createInstance("com.sun.star.style.PageStyle"));
            final XPropertySet xPageStyle = (XPropertySet) this.docController.getXInterface(XPropertySet.class, xNewStyle);
            xFamilyPage.insertByName("IsDuplex", xNewStyle);
            xPageStyle.setPropertyValue("NumberingType", aTitleStyle.ordinal());
        }
    }

    /**
     * Returns the page layout (one or two columns).
     *
     * @return the twoColumned
     * @throws com.sun.star.uno.Exception
     */
    public boolean isTwoColumned() throws com.sun.star.uno.Exception {
        final XTextColumns xTextColumns = (XTextColumns) this.docController.getXInterface(XTextColumns.class, this.xPageStyleProps[0].getPropertyValue("TextColumns"));
        return xTextColumns.getColumnCount() == (short) 2.0;
    }

    /**
     * Sets the page layout (one or two columns).
     *
     * @param bTwoColumned page layout (one or two columns)
     * @throws com.sun.star.uno.Exception
     */
    public void setTwoColumned(final boolean bTwoColumned) throws com.sun.star.uno.Exception {
        if (bTwoColumned) {
            for (final XPropertySet curentProbs : this.xPageStyleProps) {
                final XTextColumns xTextColumns = (XTextColumns) this.docController.getXInterface(XTextColumns.class, curentProbs.getPropertyValue("TextColumns"));
                xTextColumns.setColumnCount((short) 2.0);
                curentProbs.setPropertyValue("TextColumns", xTextColumns);
            }
        } else {
            for (final XPropertySet curentProbs : this.xPageStyleProps) {
                XTextColumns xTextColumns = (XTextColumns) this.docController.getXInterface(XTextColumns.class, curentProbs.getPropertyValue("TextColumns"));
                xTextColumns.setColumnCount((short) 1.0);
                curentProbs.setPropertyValue("TextColumns", xTextColumns);
            }
        }
    }

    /**
     * Returns a localized meaning of a {@link ast.common.data.TypeOfWork} ENUM.
     *
     * @param aTypeOfWork ENUM to translate
     * @param bInSentence Special focus on capitalized letters for use in a sentence
     * @return Localized ENUM
     */
    public String getLocalizedTypeOfWork(final TypeOfWork aTypeOfWork, final boolean bInSentence) {
        switch(aTypeOfWork) {
            case DIPLOM:
                return bInSentence ? this.docController.getLanguageController().__("diploma thesis") : this.docController.getLanguageController().__("Diploma Thesis");
            case BACHELOR:
                return bInSentence ? this.docController.getLanguageController().__("bachelor report") : this.docController.getLanguageController().__("Bachelor Report");
            case MASTER:
                return bInSentence ? this.docController.getLanguageController().__("master thesis") : this.docController.getLanguageController().__("Master Thesis");
            case PAPER:
                return bInSentence ? this.docController.getLanguageController().__("paper") : this.docController.getLanguageController().__("Paper");
            case DISSERT:
                return bInSentence ? this.docController.getLanguageController().__("dissertation") : this.docController.getLanguageController().__("Dissertation");
            case HABIL:
                return bInSentence ? this.docController.getLanguageController().__("habilitation") : this.docController.getLanguageController().__("Habilitation");
            default:
                return "";
        }
    }
}
