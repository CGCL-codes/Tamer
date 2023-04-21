package html;

public class Head extends Tag {

    public Head(String title) {
        super("head", new Tag("title", title));
    }
}
