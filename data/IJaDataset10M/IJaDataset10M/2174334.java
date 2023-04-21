package help.pages.tutorials.java;

import be.lassi.lanbox.tools.CueListBuilder;

public class Hello extends CueListBuilder {

    public static void main(final String[] args) {
        new Hello().test();
    }

    public void test() {
        createCueList("HELLO");
        writeCueLists();
    }
}
