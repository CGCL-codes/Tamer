package com.qarks.util.files.diff;

public class FileLine {

    public static final int UNKNOWN = 0;

    public static final int UNCHANGED = 1;

    public static final int MOVED = 2;

    public static final int MODIFIED = 3;

    public static final int INSERTED_ON_OTHER_SIDE = 4;

    public static final int DELETED_ON_OTHER_SIDE = 5;

    public static final int CONFLICT = 6;

    public static final int NO_MATCH = -1;

    public enum EolType {

        IGNORE, NO_EOL, CR, LF, CRLF
    }

    ;

    private EolType eolType;

    private String line;

    private int indexInOtherVersion = -1;

    private boolean matchFound = false;

    private int index;

    private int status = NO_MATCH;

    private int hashcode = -1;

    public FileLine(String line, int index, EolType eolType) {
        this.line = line;
        this.index = index;
        this.eolType = eolType;
    }

    public static FileLine getFormattingLine(int status) {
        FileLine result = new FileLine("", -1, EolType.IGNORE);
        result.status = status;
        return result;
    }

    public EolType getEolType() {
        return eolType;
    }

    public static String statusToString(int status) {
        String result = "?";
        switch(status) {
            case FileLine.CONFLICT:
                result = "conflict";
                break;
            case FileLine.DELETED_ON_OTHER_SIDE:
                result = "deleted on other side";
                break;
            case FileLine.INSERTED_ON_OTHER_SIDE:
                result = "inserted on other side";
                break;
            case FileLine.MODIFIED:
                result = "modified";
                break;
            case FileLine.MOVED:
                result = "moved";
                break;
            case FileLine.NO_MATCH:
                result = "no match";
                break;
            case FileLine.UNCHANGED:
                result = "unchanged";
                break;
        }
        return result;
    }

    public String getContent() {
        return line;
    }

    public int getIndex() {
        return index;
    }

    public boolean isMatchFound() {
        return matchFound;
    }

    public int indexInOtherVersion() {
        return indexInOtherVersion;
    }

    public int getStatus() {
        return status;
    }

    public void setIndexInOtherVersion(int indexInOtherVersion) {
        matchFound = true;
        this.indexInOtherVersion = indexInOtherVersion;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String toString() {
        return line;
    }

    public int getHashCode() {
        if (hashcode == -1) {
            hashcode = line.hashCode();
        }
        return hashcode;
    }

    public boolean matches(FileLine other, boolean ignoreLeadingSpaces) {
        boolean result = false;
        if (ignoreLeadingSpaces) {
            String first = line;
            while (first.startsWith(" ") || first.startsWith("\t")) {
                first = first.substring(1);
            }
            String second = other.getContent();
            while (second.startsWith(" ") || second.startsWith("\t")) {
                second = second.substring(1);
            }
            result = (first.length() == second.length() && first.hashCode() == second.hashCode() && other.eolType == eolType);
        } else if (line.length() == other.getContent().length() && getHashCode() == other.getHashCode() && other.eolType == eolType) {
            result = true;
        }
        return result;
    }
}
