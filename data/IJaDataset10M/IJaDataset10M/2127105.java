package org.biblestudio.model;

public class Paragraph extends ModelEntity {

    private Integer idBible;

    private Integer idBook;

    private String divKey;

    private Integer chapter;

    private boolean isHeader;

    private Integer verse;

    private Integer page;

    private String xmlText;

    private String plainText;

    public Paragraph() {
    }

    public void setIdBible(Integer idBible) {
        this.idBible = idBible;
    }

    public Integer getIdBible() {
        return idBible;
    }

    public void setIdBook(Integer idBook) {
        this.idBook = idBook;
    }

    public Integer getIdBook() {
        return idBook;
    }

    public void setDivKey(String divKey) {
        this.divKey = divKey;
    }

    public String getDivKey() {
        return divKey;
    }

    public void setChapter(Integer chapter) {
        this.chapter = chapter;
    }

    public Integer getChapter() {
        return chapter;
    }

    public void setHeader(boolean isHeader) {
        this.isHeader = isHeader;
    }

    public boolean isHeader() {
        return isHeader;
    }

    public void setVerse(Integer verse) {
        this.verse = verse;
    }

    public Integer getVerse() {
        return verse;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPage() {
        return page;
    }

    public void setXmlText(String text) {
        this.xmlText = text;
    }

    public String getXmlText() {
        return xmlText;
    }

    public void setPlainText(String plainText) {
        this.plainText = plainText;
    }

    public String getPlainText() {
        return plainText;
    }

    @Override
    public String toString() {
        return this.plainText;
    }
}
