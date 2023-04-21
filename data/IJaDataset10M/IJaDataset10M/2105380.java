package com.lc.codemate;

import java.util.List;

public class StatFolderResult extends StatResult {

    long subFileNum = 0;

    List subFileList = null;

    List subFolderList = null;

    public long getSubFileNum() {
        return subFileNum;
    }

    public void setSubFileNum(long subFileNum) {
        this.subFileNum = subFileNum;
    }

    public List getSubFileList() {
        return subFileList;
    }

    public void setSubFileList(List subFileList) {
        this.subFileList = subFileList;
    }

    public List getSubFolderList() {
        return subFolderList;
    }

    public void setSubFolderList(List subFolderList) {
        this.subFolderList = subFolderList;
    }
}
