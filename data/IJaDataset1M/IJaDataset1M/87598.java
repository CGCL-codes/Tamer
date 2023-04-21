package net.sourceforge.offsprings.examples;

public class Document {

    private String _id;

    private String _content;

    public Document(String id) {
        _id = id;
        _content = "This is a content of the document: " + id;
    }

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id = id;
    }

    public String getContent() {
        return _content;
    }

    public void setContent(String content) {
        this._content = content;
    }

    public String toString() {
        String answer = "id=" + _id;
        return answer;
    }
}
