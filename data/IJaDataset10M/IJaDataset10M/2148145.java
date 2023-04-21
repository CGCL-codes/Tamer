package ca.ucalgary.cpsc.ebe.fitClipse.render.wikitext;

import ca.ucalgary.cpsc.ebe.fitClipse.render.wiki.WikiPage;
import ca.ucalgary.cpsc.ebe.fitClipse.render.wikitext.JSPWikiWidgets.ParentWidget;

public abstract class WikiWidget {

    protected ParentWidget parent = null;

    protected WikiWidget(ParentWidget parent) {
        this.parent = parent;
        addToParent();
    }

    protected void addToParent() {
        if (this.parent != null) this.parent.addChild(this);
    }

    public abstract String render() throws Exception;

    public WikiPage getWikiPage() {
        return parent.getWikiPage();
    }

    public String asWikiText() throws Exception {
        return getClass().toString() + ".asWikiText()";
    }
}
