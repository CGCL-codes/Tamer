package tei.cr.teiScheme;

public final class TeiElement {

    private String teiName;

    private String localName;

    protected TeiElement(String teiName, String localName) {
        this.teiName = teiName;
        this.localName = localName;
    }

    public String getLocalName() {
        return localName;
    }

    public String getTeiName() {
        return teiName;
    }

    protected void setLocalName(String localName) {
        this.localName = localName;
    }
}
