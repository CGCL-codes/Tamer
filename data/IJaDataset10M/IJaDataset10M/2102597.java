package com.uspto.pati.common;

import java.util.List;

public class PartList {

    private List<String> partId;

    private List<String> partDesc;

    public void setPartId(List<String> partId) {
        this.partId = partId;
    }

    public List<String> getPartId() {
        return partId;
    }

    public void setPartDesc(List<String> partDesc) {
        this.partDesc = partDesc;
    }

    public List<String> getPartDesc() {
        return partDesc;
    }
}
