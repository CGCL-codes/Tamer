package course.wicket.quiz.document;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.markup.html.link.PageLink;
import org.modelibra.wicket.container.DmPage;
import course.quiz.document.Documents;
import course.quiz.test.Test;

/**
 * Document display table page.
 * 
 * @author Dzenan Ridjanovic
 * @version 2007-12-27
 */
public class DocumentDisplayTablePage extends DmPage {

    private static final long serialVersionUID = 282020L;

    public DocumentDisplayTablePage(Test test) {
        add(homePageLink("homePage"));
        Documents documents = test.getDocuments();
        add(new DocumentDisplayTablePanel("documentDisplayTablePanel", documents));
    }

    public static PageLink link(String id, final Test test) {
        PageLink link = new PageLink(id, new IPageLink() {

            static final long serialVersionUID = 282021L;

            public Page getPage() {
                return new DocumentDisplayTablePage(test);
            }

            public Class<? extends Page> getPageIdentity() {
                return DocumentDisplayTablePage.class;
            }
        });
        return link;
    }
}
