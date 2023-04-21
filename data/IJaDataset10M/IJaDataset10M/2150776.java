package eu.pisolutions.ocelot.document.article;

import eu.pisolutions.ocelot.document.information.MetaInformation;
import eu.pisolutions.ocelot.document.io.DocumentNodeFactory;
import eu.pisolutions.ocelot.document.io.DocumentReadingContext;
import eu.pisolutions.ocelot.object.PdfObject;

/**
 * {@link ArticleThread} information.
 *
 * <dl>
 * <dt><b>Specification:</b></dt>
 * <dd>PDF 1.7, 8.3.2</dd>
 * </dl>
 *
 * @author Laurent Pireyn
 */
public final class ArticleThreadInformation extends MetaInformation {

    public enum Factory implements DocumentNodeFactory<ArticleThreadInformation> {

        INSTANCE;

        public ArticleThreadInformation createNode(DocumentReadingContext context, boolean indirect, PdfObject object) {
            return new ArticleThreadInformation(indirect);
        }
    }

    public ArticleThreadInformation() {
        super();
    }

    private ArticleThreadInformation(boolean indirect) {
        super(indirect);
    }
}
