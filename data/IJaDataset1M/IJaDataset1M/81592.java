package com.global360.sketchpadbpmn.documents;

public abstract class VersionBase {

    public static final char NULL_MODIFIER = '\000';

    protected int majorVersion = 0;

    protected int minorVersion = 0;

    protected char modifier = NULL_MODIFIER;

    public VersionBase() {
    }

    public VersionBase(String versionString) {
        this.set(versionString);
    }

    public VersionBase(int majorVersion, int minorVersion) {
        this.set(majorVersion, minorVersion);
    }

    public String toString() {
        return this.majorVersion + "." + this.minorVersion;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof VersionBase) {
            return equals((VersionBase) other);
        }
        return false;
    }

    public boolean equals(VersionBase other) {
        return (this.majorVersion == other.majorVersion) && (this.minorVersion == other.minorVersion);
    }

    public boolean greaterThanOrEquals(VersionBase other) {
        return (this.majorVersion >= other.majorVersion) && (this.minorVersion >= other.minorVersion);
    }

    public boolean is_1() {
        return this.majorVersion == 1;
    }

    public boolean is_2() {
        return this.majorVersion == 2;
    }

    public boolean is_2_0() {
        return this.majorVersion == 2 && this.minorVersion == 0;
    }

    public boolean is_2_1() {
        return this.majorVersion == 2 && this.minorVersion == 1;
    }

    public boolean is_2_2() {
        return this.majorVersion == 2 && this.minorVersion == 2;
    }

    public boolean is_2_0_or_greater() {
        return this.majorVersion >= 2;
    }

    public boolean is_2_1_or_greater() {
        return this.majorVersion == 2 && this.minorVersion >= 1;
    }

    public boolean is_2_2_or_greater() {
        return this.majorVersion == 2 && this.minorVersion >= 2;
    }

    public void set(int major, int minor) {
        this.majorVersion = major;
        this.minorVersion = minor;
    }

    public boolean set(String versionString) {
        boolean result = false;
        if (versionString != null) {
            try {
                versionString = versionString.trim();
                int dotIndex = versionString.indexOf(".");
                if (dotIndex < 0) {
                    this.majorVersion = Integer.parseInt(versionString);
                    this.minorVersion = 0;
                } else {
                    String substring = versionString.substring(0, dotIndex);
                    this.majorVersion = Integer.parseInt(substring);
                    char lastChar = versionString.charAt(versionString.length() - 1);
                    if (Character.isDigit(lastChar)) {
                        substring = versionString.substring(dotIndex + 1, versionString.length());
                        this.minorVersion = Integer.parseInt(substring);
                        this.modifier = NULL_MODIFIER;
                    } else {
                        substring = versionString.substring(dotIndex + 1, versionString.length() - 1);
                        this.minorVersion = Integer.parseInt(substring);
                        this.modifier = lastChar;
                    }
                }
                result = true;
            } catch (NumberFormatException e) {
                ;
                ;
            }
        }
        return result;
    }

    public int getMajor() {
        return this.majorVersion;
    }

    public int getMinor() {
        return this.minorVersion;
    }

    public char getModifier() {
        return this.modifier;
    }
}
