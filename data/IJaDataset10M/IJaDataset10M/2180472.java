package com.shine.framework.HtmlManager.model;

/**
 * ImgModel 图片model
 * 
 * @author viruscodecn@gmail.com
 * @project JavaFramework 1.0 2010-12-02
 */
public final class ImgModel extends HtmlBaseModel {

    /**
	 * 获取图片描述
	 * 
	 * @return
	 */
    public String getAlt() {
        return this.getString("alt");
    }

    /**
	 * 获取图片源路径
	 * 
	 * @return
	 */
    public String getSrc() {
        return this.getString("src");
    }
}
