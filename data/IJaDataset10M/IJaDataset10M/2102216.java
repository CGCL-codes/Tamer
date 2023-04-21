package com.eric.simms.model.vo;

public class PromptMsgVO {

    public static final int INFO = 3;

    public static final int WARNING = 2;

    public static final int EXCEPTION = 1;

    public static final int ERROR = 0;

    public Object shell;

    public int level;

    public String title;

    public String msg;

    public PromptMsgVO(String title, String msg) {
        this(EXCEPTION, title, msg, null);
    }

    public PromptMsgVO(int level, String title, String msg) {
        this(level, title, msg, null);
    }

    public PromptMsgVO(int level, String title, String msg, Object shell) {
        this.level = level;
        this.title = title;
        this.msg = msg;
        this.shell = shell;
    }
}
