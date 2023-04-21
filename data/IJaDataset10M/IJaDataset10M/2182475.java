package org.htmltransfer.ds;

/**
 *
 * @author jude
 * @version 0.1 2011-4-8
 */
public class MyNode {

    boolean isTitle;

    boolean hasLink;

    String content;

    String linkUrl;

    public MyNode() {
        this.content = "";
        this.hasLink = false;
        this.isTitle = false;
        this.linkUrl = "";
    }

    public void setisTitle(boolean a) {
        this.isTitle = a;
    }

    public void sethasLink(boolean a) {
        this.hasLink = a;
    }

    public void setContent(String a) {
        this.content = a;
    }

    public void setLinkUrl(String a) {
        this.linkUrl = a;
    }

    public boolean getIsTitle() {
        return this.isTitle;
    }

    public boolean getHasLink() {
        return this.hasLink;
    }

    public String getContent() {
        return this.content;
    }

    public String getLinkUrl() {
        return this.linkUrl;
    }

    public void printnode() {
        String info = "{";
        info += "IsTitle: ";
        info += this.getIsTitle();
        info += "; HasLink: ";
        info += this.hasLink;
        info += "; Content: ";
        info += this.getContent();
        if (this.hasLink) {
            info += "; LinkUrl:";
            info += this.getLinkUrl();
        }
        info += "}";
        System.out.println(info);
    }

    @Override
    public String toString() {
        String info = "{";
        info += "IsTitle: ";
        info += this.getIsTitle();
        info += "; HasLink: ";
        info += this.hasLink;
        info += "; Content: ";
        info += this.getContent();
        if (this.hasLink) {
            info += "; LinkUrl:";
            info += this.getLinkUrl();
        }
        info += "}";
        return info;
    }
}
