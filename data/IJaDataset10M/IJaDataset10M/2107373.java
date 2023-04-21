package com.companyname.common.base.web.view;

/**
 * 记录明细标题4优先级
 *
 */
public class Title4SortPri extends Title4Sort {

    public Title4SortPri() {
        super("优先级", "pri");
        this.setPrinted(false);
    }

    public Title4SortPri(String priField) {
        super("优先级", priField);
        this.setPrinted(false);
    }
}
