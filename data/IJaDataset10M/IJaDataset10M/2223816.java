package uk.ac.ebi.intact.application.dataConversion.psiUpload.model;

import uk.ac.ebi.intact.application.dataConversion.PsiConstants;

/**
 * That class . <attributeList> <attribute name="comment">CAV1 was expressed as GST fusion in E. coli.</attribute>
 * <attribute name="comment">CAV1 was expressed in MDCK cells.</attribute> <attribute name="remark">aa 61-101 of CAV1
 * are sufficient</attribute> </attributeList>
 *
 * @author Samuel Kerrien (skerrien@ebi.ac.uk)
 * @version $Id: AnnotationTag.java 3917 2005-04-28 08:27:39Z skerrien $
 */
public final class AnnotationTag {

    private final String type;

    private final String text;

    public AnnotationTag(final String type, final String text) {
        if (type == null || "".equals(type)) {
            throw new IllegalArgumentException("You must give a non null/empty type for an annotation");
        }
        this.type = type;
        this.text = (text == null ? text : text.trim());
    }

    public boolean hasText() {
        return !(text == null || "".equals(text));
    }

    public String getText() {
        return text;
    }

    public String getType() {
        return type;
    }

    public boolean isDissociationConstant() {
        return (PsiConstants.KD_ATTRIBUTE_NAME.equalsIgnoreCase(type) || PsiConstants.DISSOCIATION_CONSTANT_ATTRIBUTE_NAME.equalsIgnoreCase(type));
    }

    public boolean isExpressedIn() {
        return PsiConstants.EXPRESSED_IN_ATTRIBUTE_NAME.equalsIgnoreCase(type);
    }

    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AnnotationTag)) {
            return false;
        }
        final AnnotationTag annotationTag = (AnnotationTag) o;
        if (!text.equals(annotationTag.text)) {
            return false;
        }
        if (!type.equals(annotationTag.type)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int result;
        result = type.hashCode();
        result = 29 * result + text.hashCode();
        return result;
    }

    public String toString() {
        final StringBuffer buf = new StringBuffer();
        buf.append("AnnotationTag");
        buf.append("{text=").append(text);
        buf.append(",type=").append(type);
        buf.append('}');
        return buf.toString();
    }
}
