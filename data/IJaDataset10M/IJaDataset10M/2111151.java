package classes;

public class Learning extends Media {

    private String language;

    public Learning(String name, String language) {
        super(name);
        this.language = language;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
