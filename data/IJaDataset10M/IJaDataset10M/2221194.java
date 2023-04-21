package com.google.doclava;

public final class ErrorCode {

    private final int code;

    private int level;

    public ErrorCode(int code, int level) {
        this.code = code;
        this.level = level;
    }

    public String toString() {
        return "Error #" + this.getCode();
    }

    public int getCode() {
        return code;
    }

    protected void setLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }
}
