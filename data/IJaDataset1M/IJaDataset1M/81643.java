package net.ikvm.ant;

public class Wildcard implements Filter {

    private String wildcard;

    public void addText(String wildcard) {
        this.wildcard = wildcard;
    }

    public boolean suppress(String output) {
        return WildcardMatcher.match(output, wildcard);
    }
}
