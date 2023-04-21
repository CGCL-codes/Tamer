package com.doculibre.intelligid.reports.impl.helpers;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ListeDossiersEtDocumentsData implements Serializable {

    private String image;

    private String text;

    private Integer indentation;

    public ListeDossiersEtDocumentsData(String image, String text, Integer indentation) {
        super();
        this.image = image;
        this.text = text;
        this.indentation = indentation;
    }

    public String getImage() {
        return image;
    }

    public String getText() {
        return text;
    }

    public Integer getIndentation() {
        return indentation;
    }
}
