package componente.rss.tag.item;

public class Source {

    private String url;

    private String value;

    @Override
    public String toString() {
        StringBuffer str = new StringBuffer("");
        str.append("Source url: " + (url == null ? "" : url));
        str.append("\n\t\t\t\t: " + value == null ? "" : value);
        return str.toString();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
