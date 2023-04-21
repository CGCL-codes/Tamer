package pedro.mda.config;

public class HelpDocument {

    private String label;

    private String link;

    public HelpDocument(String label, String link) {
        this.label = label;
        this.link = link;
    }

    public String getLabel() {
        return label;
    }

    public String getLink() {
        return link;
    }
}
