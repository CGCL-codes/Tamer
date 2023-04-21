package org.ujac.print.tag.acroform;

import java.io.IOException;
import org.ujac.print.AttributeDefinitionMap;
import org.ujac.print.DocumentFont;
import org.ujac.print.DocumentHandlerException;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseField;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfAcroForm;
import com.lowagie.text.pdf.PdfFormField;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.TextField;

/**
 * Name: HiddenFieldAcroFieldTag<br>
 * Description: Tag implementation for AcroForm hidden fields.
 * 
 * @author lauerc
 */
public class HiddenFieldAcroFieldTag extends BaseAcroFieldTag {

    /** The item's name. */
    public static final String TAG_NAME = "hidden-field";

    /** The field value. */
    protected String fieldValue = null;

    /** The field status. */
    protected boolean status = false;

    /**
   * Constructs a HiddenFieldAcroFieldTag instance with no specific attributes.
   */
    public HiddenFieldAcroFieldTag() {
        super(TAG_NAME);
    }

    /**
   * Gets a brief description for the item.
   * @return The item's description.
   */
    public String getDescription() {
        return "Adds a hidden field to the documents AcroForm.";
    }

    /**
   * Gets the list of supported attributes.
   * @return The attribute definitions.
   */
    protected AttributeDefinitionMap buildSupportedAttributes() {
        return super.buildSupportedAttributes().addDefinition(FIELD_NAME).addDefinition(FIELD_VALUE);
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
    }

    /**
   * Adds a field to the given AcroForm. 
   * @param form The AcroForm to add the field to.
   * @exception DocumentHandlerException In case something went wrong while adding the form field.
   */
    public PdfFormField addFormField(PdfAcroForm form) throws DocumentHandlerException {
        try {
            DocumentFont font = getFont();
            BaseFont bf = font.getFont().getBaseFont();
            PdfWriter pdfWriter = getPdfWriter();
            TextField tf = new TextField(pdfWriter, new Rectangle(0, 0, 0, 0), fieldName);
            tf.setFont(bf);
            int rotation = documentHandler.getDocument().getPageSize().getRotation();
            tf.setRotation(rotation);
            tf.setFontSize(font.getSize());
            tf.setText(fieldValue);
            tf.setOptions(BaseField.HIDDEN);
            PdfFormField ff = tf.getTextField();
            pdfWriter.addAnnotation(ff);
            return null;
        } catch (DocumentException ex) {
            throw new DocumentHandlerException(locator(), "Unable to add FormField: " + ex.getMessage(), ex);
        } catch (IOException ex) {
            throw new DocumentHandlerException(locator(), "Unable to add FormField: " + ex.getMessage(), ex);
        }
    }

    /**
   * Tells whether or not this element is visible to the user.
   * @return true if to render the element.
   */
    public boolean isVisibleElement() {
        return false;
    }
}
