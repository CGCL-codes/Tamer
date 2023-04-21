package org.in4ama.editor.project.pdfdocument;

import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.util.LinkedHashMap;
import java.util.Map;
import org.in4ama.documentautomator.documents.acroform.AcroFieldBinding;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.AcroFields;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PRIndirectReference;
import com.lowagie.text.pdf.PdfArray;
import com.lowagie.text.pdf.PdfDictionary;
import com.lowagie.text.pdf.PdfFormField;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfNumber;
import com.lowagie.text.pdf.PdfObject;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PdfString;
import com.lowagie.text.pdf.TextField;

public class AcroFieldInfoText extends AcroFieldInfo {

    private static final Map<String, Integer> ALIGNMENTS = getAvailableAlignments();

    private static final String[] ALIGNMENT_NAMES = ALIGNMENTS.keySet().toArray(new String[0]);

    public static final String TEXT_FONT_SIZE = "font";

    public static final String TEXT_ALIGNMENT = "text alignment";

    public static final String BACKGROUND_COLOR = "background color";

    public static final String TEXT_COLOR = "text color";

    public static final String MULTI_LINE = "multiline";

    public static final String ROTATION = "rotation";

    private static Map<String, Integer> getAvailableAlignments() {
        Map<String, Integer> alignmentTypes = new LinkedHashMap<String, Integer>();
        alignmentTypes.put("left", Element.ALIGN_LEFT);
        alignmentTypes.put("right", Element.ALIGN_RIGHT);
        alignmentTypes.put("center", Element.ALIGN_CENTER);
        return alignmentTypes;
    }

    @Override
    protected void initProperties() {
        super.initProperties();
        properties.put(TEXT_ALIGNMENT, "left");
        properties.put(TEXT_COLOR, Color.BLACK);
        properties.put(BACKGROUND_COLOR, Color.WHITE);
        properties.put(TEXT_FONT_SIZE, new FontAndSize());
        properties.put(MULTI_LINE, Boolean.FALSE);
        properties.put(ROTATION, "0 deg");
    }

    @Override
    public int getPropertyType(String propertyName) {
        if (TEXT_COLOR.equals(propertyName)) return PROPERTY_COLOR; else if (BACKGROUND_COLOR.equals(propertyName)) return PROPERTY_COLOR; else if (TEXT_ALIGNMENT.equals(propertyName)) return PROPERTY_MULTI; else if (TEXT_FONT_SIZE.equals(propertyName)) return PROPERTY_FONT; else if (MULTI_LINE.equals(propertyName)) return PROPERTY_BOOLEAN; else if (ROTATION.equals(propertyName)) return PROPERTY_MULTI; else return super.getPropertyType(propertyName);
    }

    @Override
    public Object[] getPropertyAllowedValues(String propertyName) {
        if (TEXT_ALIGNMENT.equals(propertyName)) return ALIGNMENT_NAMES; else if (ROTATION.equals(propertyName)) return ROTATION_NAMES; else return super.getPropertyAllowedValues(propertyName);
    }

    @Override
    public void setProperty(String propertyName, Object propertyValue) {
        if (ROTATION.equals(propertyName)) {
            PropertyChangeEvent evt = createPropertyChangeEvent(propertyName, propertyValue);
            setDirty(true);
            setRotation((String) propertyValue);
            updatePropertyChangeListeners(evt);
        } else {
            super.setProperty(propertyName, propertyValue);
        }
    }

    protected void setRotation(String rotationS) {
        int rotation1 = ROTATIONS.get((String) getProperty(ROTATION));
        properties.put(ROTATION, rotationS);
        int rotation2 = ROTATIONS.get((String) getProperty(ROTATION));
        int d = Math.abs(rotation1 - rotation2);
        if ((d == 90) || (d == 270)) {
            double width = getWidth();
            double height = getHeight();
            setWidth(height);
            setHeight(width);
        }
    }

    @Override
    public String getBindingType() {
        return AcroFieldBinding.TEXT;
    }

    public String getIconName() {
        return "textcomp_icon_16.png";
    }

    @Override
    protected void storeNewAcroField(BasePdfPageInfo pageInfo, PdfStamper stamper) throws Exception {
        TextField textField = createTextAcroField(pageInfo, stamper);
        textField.setBackgroundColor((Color) getProperty(BACKGROUND_COLOR));
        textField.setBorderColor((Color) getProperty(PROP_BORDER_COLOR));
        textField.setTextColor((Color) getProperty(TEXT_COLOR));
        textField.setAlignment(ALIGNMENTS.get((String) getProperty(TEXT_ALIGNMENT)));
        FontAndSize fontAndSize = (FontAndSize) getProperty(TEXT_FONT_SIZE);
        BaseFont baseFont = fontAndSize.getBaseFont();
        if (baseFont != null) textField.setFont(baseFont);
        textField.setFontSize(fontAndSize.getSize());
        Boolean multiLine = (Boolean) getProperty(MULTI_LINE);
        if (multiLine) textField.setOptions(TextField.MULTILINE);
        textField.setRotation(ROTATIONS.get((String) getProperty(ROTATION)));
        stamper.addAnnotation(textField.getTextField(), pageInfo.getNr());
    }

    @Override
    protected void storeUpdatedAcroField(BasePdfPageInfo pageInfo, PdfStamper stamper) throws Exception {
        storeBackgroundColor(stamper);
        storeTextColor(stamper);
        storeFontAndSize(stamper);
        storeAlignment(stamper);
        storeMultiline(stamper);
        storeRotation(stamper);
    }

    protected void storeBackgroundColor(PdfStamper stamper) {
        String name = getName();
        stamper.getAcroFields().setFieldProperty(name, "bgcolor", (Color) getProperty(BACKGROUND_COLOR), null);
    }

    protected void storeTextColor(PdfStamper stamper) {
        String name = getName();
        stamper.getAcroFields().setFieldProperty(name, "textcolor", (Color) getProperty(TEXT_COLOR), null);
    }

    protected void storeFontAndSize(PdfStamper stamper) {
        String name = getName();
        AcroFields acroFields = stamper.getAcroFields();
        FontAndSize fontAndSize = (FontAndSize) getProperty(TEXT_FONT_SIZE);
        BaseFont baseFont = fontAndSize.getBaseFont();
        if (baseFont != null) acroFields.setFieldProperty(name, "textfont", baseFont, null);
        Float textSize = Float.valueOf(String.valueOf(fontAndSize.getSize()));
        acroFields.setFieldProperty(name, "textsize", textSize, null);
    }

    protected void storeAlignment(PdfStamper stamper) {
        AcroFields acroFields = stamper.getAcroFields();
        AcroFields.Item fieldItem = acroFields.getFieldItem(getName());
        int alignType1 = ALIGNMENTS.get((String) getProperty(TEXT_ALIGNMENT));
        int alignType2 = PdfFormField.Q_LEFT;
        if (alignType1 == Element.ALIGN_RIGHT) alignType2 = PdfFormField.Q_RIGHT; else if (alignType1 == Element.ALIGN_CENTER) alignType2 = PdfFormField.Q_CENTER;
        int n = fieldItem.widgets.size();
        for (int i = 0; i < n; i++) {
            PdfDictionary wDict = (PdfDictionary) fieldItem.widgets.get(i);
            PdfDictionary mDict = (PdfDictionary) fieldItem.merged.get(i);
            PdfNumber align = new PdfNumber(alignType2);
            wDict.put(PdfName.Q, align);
            mDict.put(PdfName.Q, align);
        }
    }

    protected void storeMultiline(PdfStamper stamper) {
        AcroFields acroFields = stamper.getAcroFields();
        AcroFields.Item fieldItem = acroFields.getFieldItem(getName());
        int n = fieldItem.widgets.size();
        for (int i = 0; i < n; i++) {
            PdfDictionary wDict = (PdfDictionary) fieldItem.widgets.get(i);
            PdfDictionary mDict = (PdfDictionary) fieldItem.merged.get(i);
            int flags = 0;
            PdfNumber nfl = (PdfNumber) PdfReader.getPdfObject(mDict.get(PdfName.FF));
            if (nfl != null) flags = nfl.intValue();
            Boolean multiLine = (Boolean) getProperty(MULTI_LINE);
            if (multiLine) flags |= PdfFormField.FF_MULTILINE; else flags &= ~PdfFormField.FF_MULTILINE;
            nfl = new PdfNumber(flags);
            wDict.put(PdfName.FF, nfl);
            mDict.put(PdfName.FF, nfl);
        }
    }

    @Override
    public void restoreProperties(PdfStamper stamper, BasePdfPageInfo pageInfo) {
        super.restoreProperties(stamper, pageInfo);
        restoreAlignment(stamper);
        restoreRotation(stamper);
        restoreMultiLine(stamper);
        restoreBackgroundColor(stamper);
        restoreBorderColor(stamper);
        restoreText(stamper);
    }

    private void restoreMultiLine(PdfStamper stamper) {
        AcroFields.Item fieldItem = stamper.getAcroFields().getFieldItem(getName());
        PdfDictionary mDict = (PdfDictionary) fieldItem.merged.get(0);
        PdfNumber nfl = (PdfNumber) PdfReader.getPdfObject(mDict.get(PdfName.FF));
        int flags = 0;
        if (nfl != null) flags = nfl.intValue();
        Boolean multiLine = !((flags & PdfFormField.FF_MULTILINE) == 0);
        initProperty(MULTI_LINE, multiLine);
    }

    private void restoreAlignment(PdfStamper stamper) {
        AcroFields.Item fieldItem = stamper.getAcroFields().getFieldItem(getName());
        PdfDictionary mDict = (PdfDictionary) fieldItem.merged.get(0);
        PdfNumber alignTypeP = (PdfNumber) PdfReader.getPdfObject(mDict.get(PdfName.Q));
        String alignment = "left";
        if (alignTypeP != null) {
            if (alignTypeP.intValue() == PdfFormField.Q_CENTER) alignment = "center"; else if (alignTypeP.intValue() == PdfFormField.Q_RIGHT) alignment = "right";
        }
        initProperty(TEXT_ALIGNMENT, alignment);
    }

    protected void restoreBackgroundColor(PdfStamper stamper) {
        AcroFields.Item fieldItem = stamper.getAcroFields().getFieldItem(getName());
        PdfDictionary mDict = (PdfDictionary) fieldItem.merged.get(0);
        PdfDictionary mk = (PdfDictionary) PdfReader.getPdfObject(mDict.get(PdfName.MK));
        if (mk != null) {
            PdfArray ar = (PdfArray) PdfReader.getPdfObject(mk.get(PdfName.BG));
            Color backgroundColor = AcroFieldUtil.getMKColor(ar);
            if (backgroundColor != null) initProperty(BACKGROUND_COLOR, backgroundColor);
        }
    }

    protected void restoreText(PdfStamper stamper) {
        AcroFields acroFields = stamper.getAcroFields();
        AcroFields.Item fieldItem = acroFields.getFieldItem(getName());
        PdfDictionary mDict = (PdfDictionary) fieldItem.merged.get(0);
        Color textColor = null;
        FontAndSize fontAndSize = new FontAndSize();
        PdfString da = (PdfString) PdfReader.getPdfObject(mDict.get(PdfName.DA));
        if (da != null) {
            Object dab[] = AcroFieldUtil.splitDAelements(da.toUnicodeString());
            if (dab[AcroFieldUtil.DA_SIZE] != null) fontAndSize.setSize(((Float) dab[AcroFieldUtil.DA_SIZE]).intValue());
            if (dab[AcroFieldUtil.DA_COLOR] != null) textColor = (Color) dab[AcroFieldUtil.DA_COLOR];
            if (dab[AcroFieldUtil.DA_FONT] != null) {
                PdfDictionary font = (PdfDictionary) PdfReader.getPdfObject(mDict.get(PdfName.DR));
                if (font != null) {
                    font = (PdfDictionary) PdfReader.getPdfObject(font.get(PdfName.FONT));
                    if (font != null) {
                        PdfObject po = font.get(new PdfName((String) dab[AcroFieldUtil.DA_FONT]));
                        if (po != null && po.type() == PdfObject.INDIRECT) fontAndSize.setBaseFont(BaseFont.createFont((PRIndirectReference) po)); else {
                            String fn[] = (String[]) AcroFieldUtil.stdFieldFontNames.get(dab[AcroFieldUtil.DA_FONT]);
                            if (fn != null) {
                                try {
                                    String enc = "winansi";
                                    if (fn.length > 1) enc = fn[1];
                                    BaseFont bf = BaseFont.createFont(fn[0], enc, false);
                                    fontAndSize.setBaseFont(bf);
                                } catch (Exception e) {
                                }
                            }
                        }
                    }
                }
            }
        }
        if (textColor != null) initProperty(TEXT_COLOR, textColor);
        initProperty(TEXT_FONT_SIZE, fontAndSize);
    }

    protected void storeRotation(PdfStamper stamper) {
        int rotation = ROTATIONS.get((String) getProperty(ROTATION));
        PdfNumber nfl = new PdfNumber(rotation);
        AcroFields acroFields = stamper.getAcroFields();
        AcroFields.Item fieldItem = acroFields.getFieldItem(getName());
        int n = fieldItem.widgets.size();
        for (int i = 0; i < n; i++) {
            PdfDictionary mDict = (PdfDictionary) fieldItem.merged.get(i);
            PdfDictionary wDict = (PdfDictionary) fieldItem.widgets.get(i);
            PdfDictionary mk = (PdfDictionary) PdfReader.getPdfObject(mDict.get(PdfName.MK));
            PdfDictionary wk = (PdfDictionary) PdfReader.getPdfObject(wDict.get(PdfName.MK));
            mk.put(PdfName.R, nfl);
            wk.put(PdfName.R, nfl);
        }
    }

    protected void restoreRotation(PdfStamper stamper) {
        AcroFields acroFields = stamper.getAcroFields();
        AcroFields.Item fieldItem = acroFields.getFieldItem(getName());
        PdfDictionary mDict = (PdfDictionary) fieldItem.merged.get(0);
        PdfDictionary mk = (PdfDictionary) PdfReader.getPdfObject(mDict.get(PdfName.MK));
        if (mk != null) {
            PdfNumber rotationN = (PdfNumber) PdfReader.getPdfObject(mk.get(PdfName.R));
            if (rotationN != null) {
                int rotation = rotationN.intValue();
                String rotS = "O deg";
                if (rotation == 0) {
                    rotS = "0 deg";
                } else if (rotation == 90) {
                    rotS = "90 deg";
                } else if (rotation == 180) {
                    rotS = "180 deg";
                } else if (rotation == 270) {
                    rotS = "270 deg";
                }
                initProperty(ROTATION, rotS);
            }
        }
    }

    public boolean isCustomProperty(String propertyName) {
        if (propertyName.equals(PROP_DATA)) {
            return true;
        }
        return false;
    }
}
