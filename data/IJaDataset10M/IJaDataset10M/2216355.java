package bioinfo.comaWebServer.pages.show;

import java.util.List;
import org.acegisecurity.annotation.Secured;
import org.apache.tapestry5.annotations.IncludeStylesheet;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.ioc.annotations.Inject;
import bioinfo.comaWebServer.dataServices.IDataSource;
import bioinfo.comaWebServer.entities.DatabaseItem;
import bioinfo.comaWebServer.pages.edit.EditDatabase;

@IncludeStylesheet("context:assets/styles.css")
@Secured("ROLE_ADMIN")
public class ShowDatabases {

    private List<DatabaseItem> databases;

    private DatabaseItem database;

    public void onPrepare() throws Exception {
        databases = dataSource.getDatabases(DatabaseItem.SEQUENCE_DB);
        databases.addAll(dataSource.getDatabases(DatabaseItem.PROFILE_DB));
    }

    @Inject
    private IDataSource dataSource;

    @InjectPage
    private EditDatabase editDatabase;

    @Secured("ROLE_ADMIN")
    Object onActionFromEdit(int id) throws Exception {
        editDatabase.setUp(id);
        return editDatabase;
    }

    @Secured("ROLE_ADMIN")
    void onActionFromDelete(int id) throws Exception {
        dataSource.deletePSIBlastDatabase(id);
    }

    public DatabaseItem getDatabase() {
        return database;
    }

    public void setDatabase(DatabaseItem database) {
        this.database = database;
    }

    public List<DatabaseItem> getDatabases() {
        return databases;
    }

    public void setDatabases(List<DatabaseItem> databases) {
        this.databases = databases;
    }

    @InjectPage
    private ShowInfo infoPage;

    Object onException(Throwable cause) {
        infoPage.setUp("", "We are sorry but managing databases failed!");
        return infoPage;
    }
}
