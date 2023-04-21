package com.asoft.common.base.web.view;

/**
 * 记录明细标题4点击标题排序
 *
 */
public class Title4Sort extends Title {

    public static String UP = "asc";

    public static String DOWN = "desc";

    public Title4Sort(String beforeText, String name, String afterText) {
        super(beforeText, name, afterText);
    }

    public Title4Sort(String title, String orderBy) {
        super("");
        this.setName(title);
        this.setText(this.getSortScript(title, orderBy));
    }

    public String getSortScript(String title, String orderBy) {
        StringBuffer sb = new StringBuffer(150);
        sb.append("<div  onclick=\"sort('" + orderBy + "');\" style='cursor:hand;'>" + title);
        sb.append("<image  orderBy='" + orderBy + "' imgType='orderByUp' >");
        sb.append("<image  orderBy='" + orderBy + "' imgType='orderByDown'>");
        sb.append("</div>");
        return sb.toString();
    }
}
