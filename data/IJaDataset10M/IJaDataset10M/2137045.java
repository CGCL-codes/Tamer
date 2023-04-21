package org.ujac.print.tag.acroform;

import java.awt.Color;
import java.io.IOException;
import org.ujac.print.AttributeDefinitionMap;
import org.ujac.print.DocumentFont;
import org.ujac.print.DocumentHandlerException;
import org.ujac.print.tag.CommonAttributes;
import org.ujac.print.tag.CommonStyleAttributes;
import org.ujac.util.template.TemplateException;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfAcroForm;
import com.lowagie.text.pdf.PdfAction;
import com.lowagie.text.pdf.PdfBorderDictionary;
import com.lowagie.text.pdf.PdfFormField;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.PushbuttonField;

/**
 * Name: CheckboxAcroFieldTag<br>
 * Description: Tag implementation for AcroForm post buttons.
 * 
 * @author lauerc
 */
public class PostButtonAcroFieldTag extends BaseAcroFieldTag {

    /** The item's name. */
    public static final String TAG_NAME = "post-button";

    /** The field value. */
    protected String fieldValue = null;

    /** The URL to call. */
    protected String url = null;

    /**
   * Constructs a PostButtonAcroFieldTag instance with no specific attributes.
   */
    public PostButtonAcroFieldTag() {
        super(TAG_NAME);
    }

    /**
   * Gets a brief description for the item.
   * @return The item's description.
   */
    public String getDescription() {
        return "Adds a post button to the documents AcroForm.";
    }

    /**
   * Gets the list of supported attributes.
   * @return The attribute definitions.
   */
    protected AttributeDefinitionMap buildSupportedAttributes() {
        return super.buildSupportedAttributes().addDefinition(CommonAttributes.CLASS).addDefinition(CommonAttributes.STYLE).addDefinition(FIELD_VALUE).addDefinition(FORM_URL).addDefinition(CommonAttributes.FONT);
    }

    /**
   * Gets the list of supported style attributes.
   * @return The attribute definitions.
   */
    protected AttributeDefinitionMap buildSupportedStyleAttributes() {
        return super.buildSupportedStyleAttributes().addDefinition(CommonStyleAttributes.FONT_NAME);
    }

    /**
   * Opens the item.
   * @exception DocumentHandlerException Thrown in case something went wrong while processing the document item.
   */
    public void openItem() throws DocumentHandlerException {
        super.openItem();
        if (!isValid()) {
            return;
        }
        this.fieldValue = getStringAttribute(FIELD_VALUE, true, null);
        this.url = getStringAttribute(FORM_URL, true, null);
    }

    /**
   * Adds a field to the given AcroForm. 
   * @param form The AcroForm to add the field to.
   * @exception DocumentHandlerException In case something went wrong while adding the form field.
   */
    public PdfFormField addFormField(PdfAcroForm form) throws DocumentHandlerException {
        String caption = null;
        try {
            caption = documentHandler.executeTemplate(getContent());
        } catch (TemplateException ex) {
            throw new DocumentHandlerException(locator(), "Content evaluation failed: " + ex.getMessage(), ex);
        }
        try {
            DocumentFont font = getFont();
            BaseFont bf = font.getFont().getBaseFont();
            PdfWriter pdfWriter = getPdfWriter();
            PushbuttonField pbf = new PushbuttonField(pdfWriter, getFieldDimensions(), fieldName);
            pbf.setFont(bf);
            int rotation = documentHandler.getDocument().getPageSize().getRotation();
            pbf.setRotation(rotation);
            pbf.setFontSize(font.getSize());
            pbf.setText(caption);
            pbf.setBorderStyle(getBorderStyle());
            pbf.setBorderWidth(getBorderWidth());
            pbf.setBorderColor(getBorderColor());
            Color backgroundColor = getBackgroundColor();
            if (backgroundColor == null) {
                backgroundColor = Color.lightGray;
            }
            pbf.setBackgroundColor(backgroundColor);
            pbf.setTextColor(getTextColor());
            pbf.setLayout(PushbuttonField.LAYOUT_LABEL_ONLY);
            PdfFormField ff = pbf.getField();
            applyJavaScript(ff);
            ff.setAction(PdfAction.createSubmitForm(url, null, getPdfAction()));
            return ff;
        } catch (DocumentException ex) {
            throw new DocumentHandlerException(locator(), "Unable to add FormField: " + ex.getMessage(), ex);
        } catch (IOException ex) {
            throw new DocumentHandlerException(locator(), "Unable to add FormField: " + ex.getMessage(), ex);
        }
    }

    /**
   * Gets the PDF action for the defined URL.
   * @return The PDF action that should fit for the given URL.
   */
    protected int getPdfAction() {
        if (url.startsWith("mailto:")) {
            return PdfAction.SUBMIT_XFDF;
        }
        return PdfAction.SUBMIT_HTML_FORMAT | PdfAction.SUBMIT_EXCLUDE;
    }

    /**
   * @see org.ujac.print.tag.acroform.BaseAcroFieldTag#getBorderStyle()
   */
    protected int getBorderStyle() {
        return PdfBorderDictionary.STYLE_BEVELED;
    }
}
