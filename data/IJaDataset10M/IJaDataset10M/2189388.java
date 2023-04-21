package net.sf.elbe.core.model.ldif;

public final class LdifInvalidPart implements LdifPart {

    private static final long serialVersionUID = 3107136058896890735L;

    private int offset;

    private String unknown;

    protected LdifInvalidPart() {
    }

    public LdifInvalidPart(int offset, String unknown) {
        this.offset = offset;
        this.unknown = unknown;
    }

    public final int getOffset() {
        return this.offset;
    }

    public final int getLength() {
        return this.toRawString().length();
    }

    public final String toRawString() {
        return this.unknown;
    }

    public final String toFormattedString() {
        return this.unknown;
    }

    public final String toString() {
        String text = toRawString();
        text = text.replaceAll("\n", "\\\\n");
        text = text.replaceAll("\r", "\\\\r");
        return getClass().getName() + " (" + getOffset() + "," + getLength() + "): '" + text + "'";
    }

    public final boolean isValid() {
        return false;
    }

    public String getInvalidString() {
        return "Unexpected Token";
    }

    public final void adjustOffset(int adjust) {
        this.offset += adjust;
    }
}
