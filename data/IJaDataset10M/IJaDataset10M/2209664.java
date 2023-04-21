package org.pojodm.party;

import org.pojodm.entity.Entity;

/**
 * @author Les Hazlewood
 */
public class PhoneNumber extends Entity {

    private static final int PHONE_NUMBER_DIGIT_MIN_LENGTH = 10;

    private String text;

    private String extension;

    private String label;

    public PhoneNumber() {
        super();
    }

    public PhoneNumber(String text) {
        super();
        setText(text);
    }

    public PhoneNumber(String text, String extension) {
        this(text);
        setExtension(extension);
    }

    public PhoneNumber(String text, String extension, String label) {
        this(text, extension);
        setLabel(label);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public static String justDigits(String in) {
        String out = null;
        if (in != null) {
            StringBuffer sb = new StringBuffer();
            char[] chars = in.toCharArray();
            for (char aChar : chars) {
                if (Character.isDigit(aChar)) {
                    sb.append(aChar);
                }
            }
            out = sb.toString();
            if (out.equals("")) {
                out = null;
            }
        }
        return out;
    }

    public static boolean isValidText(String text) {
        String trimmed = justDigits(text);
        return trimmed != null && trimmed.length() >= PHONE_NUMBER_DIGIT_MIN_LENGTH;
    }

    public boolean isValid() {
        return isValidText(getText());
    }

    public boolean onEquals(Entity o) {
        if (o instanceof PhoneNumber) {
            PhoneNumber pn = (PhoneNumber) o;
            return (text != null ? text.equals(pn.getText()) : pn.getText() == null) && (label != null ? label.equals(pn.getLabel()) : pn.getLabel() == null) && (extension != null ? extension.equals(pn.getExtension()) : pn.getExtension() == null);
        }
        return false;
    }

    public int hashCode() {
        int result;
        result = text != null ? text.hashCode() : 0;
        result = 31 * result + (extension != null ? extension.hashCode() : 0);
        return result;
    }

    public StringBuffer toStringBuffer() {
        StringBuffer sb = new StringBuffer(getText());
        if (getExtension() != null) {
            sb.append(",extension=").append(getExtension());
        }
        if (getLabel() != null) {
            sb.append(",label=").append(getLabel());
        }
        return sb;
    }

    @Override
    @SuppressWarnings({ "CloneDoesntDeclareCloneNotSupportedException" })
    public Object clone() {
        PhoneNumber clone = (PhoneNumber) super.clone();
        clone.setText(getText());
        clone.setExtension(getExtension());
        clone.setLabel(getLabel());
        return clone;
    }
}
