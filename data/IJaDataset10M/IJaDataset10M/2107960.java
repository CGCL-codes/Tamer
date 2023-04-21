package net.sf.compositor.util.doclet;

class HtmlUl extends HtmlElement {

    HtmlUl() {
        super("ul");
    }

    HtmlUl(final String[] attributeDetails) {
        super("ul", attributeDetails);
    }

    HtmlUl(final HtmlElement... content) {
        super("ul", content);
    }
}
