package edu.asu.quadriga.conceptpower;

public class ConceptInfo {

    private String description;

    private String uri;

    private String expression;

    private String pos;

    private String conceptList;

    public ConceptInfo() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String url) {
        this.uri = url;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public String getConceptList() {
        return conceptList;
    }

    public void setConceptList(String conceptList) {
        this.conceptList = conceptList;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ConceptInfo) {
            if (((ConceptInfo) obj).getUri().equals(this.getUri())) return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return getUri().hashCode();
    }
}
