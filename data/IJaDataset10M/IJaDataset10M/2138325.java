package org.jfor.jfor.converter;

import org.xml.sax.Attributes;
import org.jfor.jfor.rtflib.rtfdoc.RtfAttributes;
import org.jfor.jfor.rtflib.rtfdoc.ITableAttributes;
import org.jfor.jfor.rtflib.rtfdoc.BorderAttributesConverter;

public class TableAttributesConverter {

    /**
     * Constructor.
     */
    private TableAttributesConverter() {
    }

    /**
     * Converts cell attributes to rtf attributes.
     *
     * @param attrs Given attributes
     * @param defaultAttributes Default rtf attributes
     *
     * @return All valid rtf attributes together
     *
     * @throws ConverterException On convertion error
     */
    static RtfAttributes convertCellAttributes(Attributes attrs, RtfAttributes defaultAttributes) throws ConverterException {
        RtfAttributes attrib = new RtfAttributes();
        Attributes defAttrs = defaultAttributes == null ? null : defaultAttributes.getXslAttributes();
        attrib.setXslAttributes(defAttrs);
        String attrValue;
        boolean isBorderPresent = false;
        if (AbstractBuilder.attributeIsSet(attrs, "background-color", defAttrs)) {
            attrValue = new String(AbstractBuilder.getValue(attrs, "background-color", defAttrs));
            attrib.set(ITableAttributes.CELL_COLOR_BACKGROUND, FoColorConverter.getInstance().getRtfColorNumber(attrValue, null).intValue());
        }
        if (AbstractBuilder.attributeIsSet(attrs, "border-color", defAttrs)) {
            attrValue = new String(AbstractBuilder.getValue(attrs, "border-color", defAttrs));
            attrib.set(BorderAttributesConverter.BORDER_COLOR, BorderAttributesConverter.convertAttributetoRtf(attrValue));
            isBorderPresent = true;
        }
        if (AbstractBuilder.attributeIsSet(attrs, "border-top-color", defAttrs)) {
            attrValue = new String(AbstractBuilder.getValue(attrs, "border-top-color", defAttrs));
            attrib.set(BorderAttributesConverter.BORDER_COLOR, BorderAttributesConverter.convertAttributetoRtf(attrValue));
            isBorderPresent = true;
        }
        if (AbstractBuilder.attributeIsSet(attrs, "border-bottom-color", defAttrs)) {
            attrValue = new String(AbstractBuilder.getValue(attrs, "border-bottom-color", defAttrs));
            attrib.set(BorderAttributesConverter.BORDER_COLOR, BorderAttributesConverter.convertAttributetoRtf(attrValue));
            isBorderPresent = true;
        }
        if (AbstractBuilder.attributeIsSet(attrs, "border-left-color", defAttrs)) {
            attrValue = new String(AbstractBuilder.getValue(attrs, "border-left-color", defAttrs));
            attrib.set(BorderAttributesConverter.BORDER_COLOR, BorderAttributesConverter.convertAttributetoRtf(attrValue));
            isBorderPresent = true;
        }
        if (AbstractBuilder.attributeIsSet(attrs, "border-right-color", defAttrs)) {
            attrValue = new String(AbstractBuilder.getValue(attrs, "border-right-color", defAttrs));
            attrib.set(BorderAttributesConverter.BORDER_COLOR, BorderAttributesConverter.convertAttributetoRtf(attrValue));
            isBorderPresent = true;
        }
        if (AbstractBuilder.attributeIsSet(attrs, "border-style")) {
            attrValue = new String(AbstractBuilder.getValue(attrs, "border-style"));
            attrib.set(ITableAttributes.CELL_BORDER_LEFT, "\\" + BorderAttributesConverter.convertAttributetoRtf(attrValue));
            attrib.set(ITableAttributes.CELL_BORDER_RIGHT, "\\" + BorderAttributesConverter.convertAttributetoRtf(attrValue));
            attrib.set(ITableAttributes.CELL_BORDER_BOTTOM, "\\" + BorderAttributesConverter.convertAttributetoRtf(attrValue));
            attrib.set(ITableAttributes.CELL_BORDER_TOP, "\\" + BorderAttributesConverter.convertAttributetoRtf(attrValue));
            isBorderPresent = true;
        }
        if (AbstractBuilder.attributeIsSet(attrs, "border-top-style")) {
            attrValue = new String(AbstractBuilder.getValue(attrs, "border-top-style"));
            attrib.set(ITableAttributes.CELL_BORDER_TOP, "\\" + BorderAttributesConverter.convertAttributetoRtf(attrValue));
            isBorderPresent = true;
        }
        if (AbstractBuilder.attributeIsSet(attrs, "border-bottom-style")) {
            attrValue = new String(AbstractBuilder.getValue(attrs, "border-bottom-style"));
            attrib.set(ITableAttributes.CELL_BORDER_BOTTOM, "\\" + BorderAttributesConverter.convertAttributetoRtf(attrValue));
            isBorderPresent = true;
        }
        if (AbstractBuilder.attributeIsSet(attrs, "border-left-style")) {
            attrValue = new String(AbstractBuilder.getValue(attrs, "border-left-style"));
            attrib.set(ITableAttributes.CELL_BORDER_LEFT, "\\" + BorderAttributesConverter.convertAttributetoRtf(attrValue));
            isBorderPresent = true;
        }
        if (AbstractBuilder.attributeIsSet(attrs, "border-right-style")) {
            attrValue = new String(AbstractBuilder.getValue(attrs, "border-right-style"));
            attrib.set(ITableAttributes.CELL_BORDER_RIGHT, "\\" + BorderAttributesConverter.convertAttributetoRtf(attrValue));
            isBorderPresent = true;
        }
        if (AbstractBuilder.attributeIsSet(attrs, "border-width")) {
            attrValue = new String(AbstractBuilder.getValue(attrs, "border-width"));
            attrib.set(BorderAttributesConverter.BORDER_WIDTH, (int) FoUnitsConverter.getInstance().convertToTwips(attrValue));
        } else if (isBorderPresent) {
            attrib.set(BorderAttributesConverter.BORDER_WIDTH, (int) FoUnitsConverter.getInstance().convertToTwips("1pt"));
        }
        if (AbstractBuilder.attributeIsSet(attrs, "number-columns-spanned")) {
            attrValue = new String(AbstractBuilder.getValue(attrs, "number-columns-spanned"));
            boolean set = true;
            for (int i = 0; i < attrValue.length(); i++) {
                if (!Character.isDigit(attrValue.charAt(i))) set = false;
            }
            if (set) {
                attrib.set(ITableAttributes.COLUMN_SPAN, Integer.parseInt(attrValue));
            }
        }
        return convertAttributes(attrs, attrib, true);
    }

    /**
     * Converts table and row attributes to rtf attributes.
     *
     * @param attrs Given attributes
     * @param defaultAttributes Default rtf attributes
     *
     * @return All valid rtf attributes together
     *
     * @throws ConverterException On convertion error
     */
    static RtfAttributes convertRowAttributes(Attributes attrs, RtfAttributes defaultAttributes) throws ConverterException {
        RtfAttributes attrib = defaultAttributes == null ? new RtfAttributes() : (RtfAttributes) defaultAttributes.clone();
        Attributes defAttrs = defaultAttributes == null ? null : defaultAttributes.getXslAttributes();
        attrib.setXslAttributes(defAttrs);
        String attrValue;
        boolean isBorderPresent = false;
        if (AbstractBuilder.attributeIsSet(attrs, "keep-together.within-page", defAttrs)) {
            attrib.set(ITableAttributes.ROW_KEEP_TOGETHER);
        }
        if (AbstractBuilder.attributeIsSet(attrs, "keep-together", defAttrs)) {
            attrib.set(ITableAttributes.ROW_KEEP_TOGETHER);
        }
        if (AbstractBuilder.attributeIsSet(attrs, "keep-with-next", defAttrs)) {
            attrib.set(ITableAttributes.ROW_KEEP_WITH_NEXT);
        }
        if (AbstractBuilder.attributeIsSet(attrs, "keep-with-previous", defAttrs)) {
            attrib.set(ITableAttributes.ROW_KEEP_WITH_PREVIOUS);
        }
        if (AbstractBuilder.attributeIsSet(attrs, "height")) {
            attrValue = new String(AbstractBuilder.getValue(attrs, "height", defAttrs));
            attrib.set(ITableAttributes.ROW_HEIGHT, (int) FoUnitsConverter.getInstance().convertToTwips(attrValue));
        }
        if (AbstractBuilder.attributeIsSet(attrs, "border-style", defAttrs)) {
            attrValue = new String(AbstractBuilder.getValue(attrs, "border-style", defAttrs));
            attrib.set(ITableAttributes.ROW_BORDER_LEFT, "\\" + BorderAttributesConverter.convertAttributetoRtf(attrValue));
            attrib.set(ITableAttributes.ROW_BORDER_RIGHT, "\\" + BorderAttributesConverter.convertAttributetoRtf(attrValue));
            attrib.set(ITableAttributes.ROW_BORDER_HORIZONTAL, "\\" + BorderAttributesConverter.convertAttributetoRtf(attrValue));
            attrib.set(ITableAttributes.ROW_BORDER_VERTICAL, "\\" + BorderAttributesConverter.convertAttributetoRtf(attrValue));
            attrib.set(ITableAttributes.ROW_BORDER_BOTTOM, "\\" + BorderAttributesConverter.convertAttributetoRtf(attrValue));
            attrib.set(ITableAttributes.ROW_BORDER_TOP, "\\" + BorderAttributesConverter.convertAttributetoRtf(attrValue));
            isBorderPresent = true;
        }
        if (AbstractBuilder.attributeIsSet(attrs, "border-top-style", defAttrs)) {
            attrValue = new String(AbstractBuilder.getValue(attrs, "border-top-style", defAttrs));
            attrib.set(ITableAttributes.ROW_BORDER_TOP, "\\" + BorderAttributesConverter.convertAttributetoRtf(attrValue));
            attrib.set(ITableAttributes.ROW_BORDER_HORIZONTAL, "\\" + BorderAttributesConverter.convertAttributetoRtf(attrValue));
            isBorderPresent = true;
        }
        if (AbstractBuilder.attributeIsSet(attrs, "border-bottom-style", defAttrs)) {
            attrValue = new String(AbstractBuilder.getValue(attrs, "border-bottom-style", defAttrs));
            attrib.set(ITableAttributes.ROW_BORDER_BOTTOM, "\\" + BorderAttributesConverter.convertAttributetoRtf(attrValue));
            attrib.set(ITableAttributes.ROW_BORDER_HORIZONTAL, "\\" + BorderAttributesConverter.convertAttributetoRtf(attrValue));
            isBorderPresent = true;
        }
        if (AbstractBuilder.attributeIsSet(attrs, "border-left-style", defAttrs)) {
            attrValue = new String(AbstractBuilder.getValue(attrs, "border-left-style", defAttrs));
            attrib.set(ITableAttributes.ROW_BORDER_LEFT, "\\" + BorderAttributesConverter.convertAttributetoRtf(attrValue));
            attrib.set(ITableAttributes.ROW_BORDER_VERTICAL, "\\" + BorderAttributesConverter.convertAttributetoRtf(attrValue));
            isBorderPresent = true;
        }
        if (AbstractBuilder.attributeIsSet(attrs, "border-right-style", defAttrs)) {
            attrValue = new String(AbstractBuilder.getValue(attrs, "border-right-style", defAttrs));
            attrib.set(ITableAttributes.ROW_BORDER_RIGHT, "\\" + BorderAttributesConverter.convertAttributetoRtf(attrValue));
            attrib.set(ITableAttributes.ROW_BORDER_VERTICAL, "\\" + BorderAttributesConverter.convertAttributetoRtf(attrValue));
            isBorderPresent = true;
        }
        if (AbstractBuilder.attributeIsSet(attrs, "border-horizontal-style", defAttrs)) {
            attrValue = new String(AbstractBuilder.getValue(attrs, "border-horizontal-style", defAttrs));
            attrib.set(ITableAttributes.ROW_BORDER_HORIZONTAL, "\\" + BorderAttributesConverter.convertAttributetoRtf(attrValue));
            attrib.set(ITableAttributes.ROW_BORDER_TOP, "\\" + BorderAttributesConverter.convertAttributetoRtf(attrValue));
            attrib.set(ITableAttributes.ROW_BORDER_BOTTOM, "\\" + BorderAttributesConverter.convertAttributetoRtf(attrValue));
            isBorderPresent = true;
        }
        if (AbstractBuilder.attributeIsSet(attrs, "border-vertical-style", defAttrs)) {
            attrValue = new String(AbstractBuilder.getValue(attrs, "border-vertical-style", defAttrs));
            attrib.set(ITableAttributes.ROW_BORDER_VERTICAL, "\\" + BorderAttributesConverter.convertAttributetoRtf(attrValue));
            attrib.set(ITableAttributes.ROW_BORDER_LEFT, "\\" + BorderAttributesConverter.convertAttributetoRtf(attrValue));
            attrib.set(ITableAttributes.ROW_BORDER_RIGHT, "\\" + BorderAttributesConverter.convertAttributetoRtf(attrValue));
            isBorderPresent = true;
        }
        if (AbstractBuilder.attributeIsSet(attrs, "border-width", defAttrs)) {
            attrValue = new String(AbstractBuilder.getValue(attrs, "border-width", defAttrs));
            attrib.set(BorderAttributesConverter.BORDER_WIDTH, (int) FoUnitsConverter.getInstance().convertToTwips(attrValue));
        } else if (isBorderPresent) {
            attrib.set(BorderAttributesConverter.BORDER_WIDTH, (int) FoUnitsConverter.getInstance().convertToTwips("1pt"));
        }
        final String marginLeft = AbstractBuilder.getAttribute(attrs, null, "margin-left", false, defAttrs);
        if (marginLeft != null) {
            attrib.set(ITableAttributes.ATTR_ROW_LEFT_INDENT, (int) FoUnitsConverter.getInstance().convertToTwips(marginLeft));
        }
        return convertAttributes(attrs, attrib, false);
    }

    /**
     * Converts xsl:fo attributes to RTF table/row/cell attributes.
     *
     * @param attrs Given attributes
     * @param defaultAttributes Default rtf attributes
     * @param cell Flag for is the convertion for a cell or a row/table
     *
     * @return All valid rtf attributes together
     *
     * @throws ConverterException On convertion error
     */
    private static RtfAttributes convertAttributes(Attributes attrs, RtfAttributes defaultAttributes, boolean cell) throws ConverterException {
        RtfAttributes result = null;
        result = defaultAttributes == null ? null : (RtfAttributes) defaultAttributes.clone();
        if (attrs == null) {
            return result;
        }
        Attributes defAttrs = defaultAttributes == null ? null : defaultAttributes.getXslAttributes();
        final String padA = AbstractBuilder.getAttribute(attrs, null, "padding", false, defAttrs);
        if (padA != null) {
            if (result == null) {
                result = new RtfAttributes();
            }
            int twips = (int) FoUnitsConverter.getInstance().convertToTwips(padA);
            addTopPadding(result, twips, cell);
            addLeftPadding(result, twips, cell);
            addBottomPadding(result, twips, cell);
            addRightPadding(result, twips, cell);
        } else {
            final String padT = AbstractBuilder.getAttribute(attrs, null, "padding-top", false, defAttrs);
            if (padT != null) {
                if (result == null) {
                    result = new RtfAttributes();
                }
                addTopPadding(result, (int) FoUnitsConverter.getInstance().convertToTwips(padT), cell);
            }
            final String padL = AbstractBuilder.getAttribute(attrs, null, "padding-left", false, defAttrs);
            if (padL != null) {
                if (result == null) {
                    result = new RtfAttributes();
                }
                addLeftPadding(result, (int) FoUnitsConverter.getInstance().convertToTwips(padL), cell);
            }
            final String padB = AbstractBuilder.getAttribute(attrs, null, "padding-bottom", false, defAttrs);
            if (padB != null) {
                if (result == null) {
                    result = new RtfAttributes();
                }
                addBottomPadding(result, (int) FoUnitsConverter.getInstance().convertToTwips(padB), cell);
            }
            final String padR = AbstractBuilder.getAttribute(attrs, null, "padding-right", false, defAttrs);
            if (padR != null) {
                if (result == null) {
                    result = new RtfAttributes();
                }
                addRightPadding(result, (int) FoUnitsConverter.getInstance().convertToTwips(padR), cell);
            }
        }
        if (result != null) {
            result.setXslAttributes(attrs);
        }
        return result;
    }

    /**
     * Adds the top padding attribute to the rtf attributes.
     *
     * @param result Given rtfAttributes to add into
     * @param twips Padding width
     * @param cell Flag for is the convertion for a cell or a row/table
     */
    private static void addTopPadding(RtfAttributes result, int twips, boolean cell) {
        if (cell) {
            result.set(ITableAttributes.ATTR_CELL_PADDING_TOP, twips);
            result.set(ITableAttributes.ATTR_CELL_U_PADDING_TOP, 3);
        } else {
            result.set(ITableAttributes.ATTR_ROW_PADDING_TOP, twips);
            result.set(ITableAttributes.ATTR_ROW_U_PADDING_TOP, 3);
        }
    }

    /**
     * Adds the left padding attribute to the rtf attributes.
     *
     * @param result Given rtfAttributes to add into
     * @param twips Padding width
     * @param cell Flag for is the convertion for a cell or a row/table
     */
    private static void addLeftPadding(RtfAttributes result, int twips, boolean cell) {
        if (cell) {
            result.set(ITableAttributes.ATTR_CELL_PADDING_LEFT, twips);
            result.set(ITableAttributes.ATTR_CELL_U_PADDING_LEFT, 3);
        } else {
            result.set(ITableAttributes.ATTR_ROW_PADDING_LEFT, twips);
            result.set(ITableAttributes.ATTR_ROW_U_PADDING_LEFT, 3);
        }
    }

    /**
     * Adds the bottom padding attribute to the rtf attributes.
     *
     * @param result Given rtfAttributes to add into
     * @param twips Padding width
     * @param cell Flag for is the convertion for a cell or a row/table
     */
    private static void addBottomPadding(RtfAttributes result, int twips, boolean cell) {
        if (cell) {
            result.set(ITableAttributes.ATTR_CELL_PADDING_BOTTOM, twips);
            result.set(ITableAttributes.ATTR_CELL_U_PADDING_BOTTOM, 3);
        } else {
            result.set(ITableAttributes.ATTR_ROW_PADDING_BOTTOM, twips);
            result.set(ITableAttributes.ATTR_ROW_U_PADDING_BOTTOM, 3);
        }
    }

    /**
     * Adds the right padding attribute to the rtf attributes.
     *
     * @param result Given rtfAttributes to add into
     * @param twips Padding width
     * @param cell Flag for is the convertion for a cell or a row/table
     */
    private static void addRightPadding(RtfAttributes result, int twips, boolean cell) {
        if (cell) {
            result.set(ITableAttributes.ATTR_CELL_PADDING_RIGHT, twips);
            result.set(ITableAttributes.ATTR_CELL_U_PADDING_RIGHT, 3);
        } else {
            result.set(ITableAttributes.ATTR_ROW_PADDING_RIGHT, twips);
            result.set(ITableAttributes.ATTR_ROW_U_PADDING_RIGHT, 3);
        }
    }
}
