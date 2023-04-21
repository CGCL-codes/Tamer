package fitService.fitnesse.components;

import fitService.fitnesse.wiki.*;
import fitService.fitnesse.wikitext.widgets.*;
import fitService.fitnesse.wikitext.*;

public abstract class ReferenceRenamer implements FitNesseTraversalListener {

    protected WikiPage root;

    public ReferenceRenamer(WikiPage root) {
        this.root = root;
    }

    protected void renameReferences() throws Exception {
        root.getPageCrawler().traverse(root, this);
    }

    public void processPage(WikiPage currentPage) throws Exception {
        PageData data = currentPage.getData();
        String content = data.getContent();
        WidgetRoot widgetRoot = new WidgetRoot(content, currentPage, referenceModifyingWidgetBuilder);
        widgetRoot.acceptVisitor(getVisitor());
        String newContent = widgetRoot.asWikiText();
        boolean pageHasChanged = !newContent.equals(content);
        if (pageHasChanged) {
            data.setContent(newContent);
            currentPage.commit(data);
        }
    }

    protected abstract WidgetVisitor getVisitor();

    public static WidgetBuilder referenceModifyingWidgetBuilder = new WidgetBuilder(new Class[] { WikiWordWidget.class, LiteralWidget.class, CommentWidget.class, PreformattedWidget.class, LinkWidget.class, ImageWidget.class, AliasLinkWidget.class, ClasspathWidget.class, FixtureWidget.class });
}
