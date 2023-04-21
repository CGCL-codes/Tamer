package com.nhn.ssi.action;

import com.nhn.ssi.bo.TestBo;
import com.opensymphony.xwork2.ActionSupport;

public class MainAction extends ActionSupport {

    private static final long serialVersionUID = 1L;

    String keyword;

    TestBo testBo;

    public String execute() throws Exception {
        keyword = "";
        return "input";
    }

    public TestBo getTestBo() {
        return testBo;
    }

    public void setTestBo(TestBo testBo) {
        this.testBo = testBo;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
