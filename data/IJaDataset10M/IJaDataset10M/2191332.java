package eu.sarunas.projects.atf.xml;

public class StringXmlWriter extends IXmlWriter {

    public StringXmlWriter(StringBuilder sb) {
        this.sb = sb;
    }

    ;

    public void openTag(String name) {
        sb.append("\n<" + name + " ");
    }

    ;

    public void closeTag(String name) {
        sb.append("</" + name + ">");
    }

    ;

    public void writeAttribute(String name, String value) {
        sb.append(name + "=\"" + value + "\"");
    }

    ;

    private StringBuilder sb = null;
}

;
