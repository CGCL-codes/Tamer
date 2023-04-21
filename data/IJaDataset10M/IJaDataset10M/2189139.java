package jsi.search;

public class Article {

    public int id;

    public String author;

    public String title;

    public String text;

    public String keywords;

    public int authorWeight;

    public int titleWeight;

    public int textWeight;

    public Article() {
        super();
    }

    public Article(int id, String author, String title, String text, String keywords, int authorWeight, int titleWeight, int textWeight) {
        super();
        this.id = id;
        this.author = author;
        this.title = title;
        this.text = text;
        this.keywords = keywords;
        this.authorWeight = authorWeight;
        this.titleWeight = titleWeight;
        this.textWeight = textWeight;
    }

    public int getAuthorWeight() {
        return authorWeight;
    }

    public void setAuthorWeight(int authorWeight) {
        this.authorWeight = authorWeight;
    }

    public int getTitleWeight() {
        return titleWeight;
    }

    public void setTitleWeight(int titleWeight) {
        this.titleWeight = titleWeight;
    }

    public int getTextWeight() {
        return textWeight;
    }

    public void setTextWeight(int textWeight) {
        this.textWeight = textWeight;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }
}
