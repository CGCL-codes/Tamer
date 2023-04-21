package marubinotto.piggydb.presentation.page;

import javax.sql.DataSource;
import org.apache.velocity.app.FieldMethodizer;
import marubinotto.piggydb.external.jdbc.DatabaseSchema;
import marubinotto.piggydb.model.FileRepository;
import marubinotto.piggydb.model.FilterRepository;
import marubinotto.piggydb.model.FragmentRepository;
import marubinotto.piggydb.model.Tag;
import marubinotto.piggydb.model.TagRepository;
import marubinotto.piggydb.presentation.page.model.SelectedFragments;
import marubinotto.piggydb.presentation.wiki.WikiParser;
import marubinotto.util.procedure.Transaction;

public class DependencyAccessors extends PageBase {

    public static final String MK_CONSTANTS_TAG = "tagConstants";

    public static final String MK_WIKI_PARSER = "wikiParser";

    public static final String SK_SELECTED_FRAGMENTS = "selectedFragments";

    private static final FieldMethodizer CONSTANTS_TAG = new FieldMethodizer(Tag.class.getName());

    protected static final int ALMOST_UNLIMITED_PAGE_SIZE = 1000000;

    public DependencyAccessors() {
    }

    @Override
    public void onRender() {
        super.onRender();
        addModel(MK_CONSTANTS_TAG, CONSTANTS_TAG);
        addModel(MK_WIKI_PARSER, getWikiParser());
    }

    protected WikiParser getWikiParser() {
        return (WikiParser) getBean("wikiParser");
    }

    protected DataSource getDataSource() {
        return (DataSource) getBean("dataSource");
    }

    protected Transaction getTransaction() {
        return (Transaction) getBean("transaction");
    }

    protected DatabaseSchema getDatabaseSchema() {
        return (DatabaseSchema) getBean("databaseSchema");
    }

    protected TagRepository getTagRepository() {
        return (TagRepository) getBean("tagRepository");
    }

    protected FragmentRepository getFragmentRepository() {
        return (FragmentRepository) getBean("fragmentRepository");
    }

    protected FilterRepository getFilterRepository() {
        return (FilterRepository) getBean("filterRepository");
    }

    protected FileRepository getFileRepository() {
        return (FileRepository) getBean("fileRepository");
    }

    protected SelectedFragments getSelectedFragments() {
        return createOrGetObjectInSession(SK_SELECTED_FRAGMENTS, new Factory<SelectedFragments>() {

            public SelectedFragments create() {
                return new SelectedFragments();
            }
        });
    }
}
