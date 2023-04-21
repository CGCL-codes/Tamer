package com.asoft.common.base.web.view;

/**
 * 功能按钮
 * 如: 新增
 */
public class FuncButton {

    /** 按钮文本 */
    private String text;

    /** 图片文件名 */
    private String imageFileName;

    /** js函数 */
    private String onClickJSFunc;

    public FuncButton(String text, String imageFileName, String onClickJSFunc) {
        this.text = text;
        this.imageFileName = imageFileName;
        this.onClickJSFunc = onClickJSFunc;
    }

    public String getText() {
        return this.text;
    }

    public String getImageFileName() {
        return this.imageFileName;
    }

    public String getOnClickJSFunc() {
        return this.onClickJSFunc;
    }
}
