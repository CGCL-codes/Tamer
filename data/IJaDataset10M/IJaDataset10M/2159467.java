package org.danann.cernunnos.sql;

import java.sql.Connection;
import javax.sql.DataSource;
import org.danann.cernunnos.AbstractContainerTask;
import org.danann.cernunnos.AttributePhrase;
import org.danann.cernunnos.EntityConfig;
import org.danann.cernunnos.Formula;
import org.danann.cernunnos.LiteralPhrase;
import org.danann.cernunnos.Phrase;
import org.danann.cernunnos.Reagent;
import org.danann.cernunnos.ReagentType;
import org.danann.cernunnos.SimpleFormula;
import org.danann.cernunnos.SimpleReagent;
import org.danann.cernunnos.TaskRequest;
import org.danann.cernunnos.TaskResponse;
import org.springframework.jdbc.datasource.DataSourceUtils;

/**
 * <code>Task</code> implementation that opens a JDBC connection and makes it
 * available to subtasks.  The new connection will automatically be closed when
 * execution of this task concludes.
 */
@Deprecated
public final class OpenConnectionTask extends AbstractContainerTask {

    private Phrase attribute_name;

    private Phrase data_source;

    private Phrase driverPhrase;

    private Phrase urlPhrase;

    private Phrase usernamePhrase;

    private Phrase passwordPhrase;

    private BasicDataSourceTemplate basicDataSourceTemplate;

    public static final Reagent ATTRIBUTE_NAME = new SimpleReagent("ATTRIBUTE_NAME", "@attribute-name", ReagentType.PHRASE, String.class, "Optional name under which the new connection will be registered as a request attribute.  If omitted, " + "the name 'SqlAttributes.CONNECTION' will be used.", new LiteralPhrase(SqlAttributes.CONNECTION));

    public static final Reagent DATA_SOURCE = new SimpleReagent("DATA_SOURCE", "@data-source", ReagentType.PHRASE, DataSource.class, "Optional DataSource object from which the connection may be opened.  This phrase will be evaluated only if " + "DRIVER, URL, USERNAME, and PASSWORD are not provided.  The default is a request attribute under the name " + "'SqlAttributes.DATA_SOURCE'.", new AttributePhrase(SqlAttributes.DATA_SOURCE));

    public static final Reagent DATA_SOURCE_ATTRIBUTE_NAME = new SimpleReagent("DATA_SOURCE_ATTRIBUTE_NAME", "@data-source-attribute-name", ReagentType.PHRASE, String.class, "Optional name under which the new DataSource will be registered as a request attribute.  If omitted, " + "the name 'SqlAttributes.DATA_SOURCE' will be used.", new LiteralPhrase(SqlAttributes.DATA_SOURCE));

    public static final Reagent DRIVER = new SimpleReagent("DRIVER", "@driver", ReagentType.PHRASE, String.class, "Optional JDBC driver class name to use when opening the connection.  You must " + "provide either DATA_SOURCE or DRIVER, URL, USERNAME, and PASSWORD.", null);

    public static final Reagent URL = new SimpleReagent("URL", "@url", ReagentType.PHRASE, String.class, "Optional JDBC connection URL to use when opening the connection.  You " + "must provide either DATA_SOURCE or DRIVER, URL, USERNAME, and PASSWORD.", null);

    public static final Reagent USERNAME = new SimpleReagent("USERNAME", "@username", ReagentType.PHRASE, String.class, "Optional username to use when opening the connection.  You must provide either " + "DATA_SOURCE or DRIVER, URL, USERNAME, and PASSWORD.", null);

    public static final Reagent PASSWORD = new SimpleReagent("PASSWORD", "@password", ReagentType.PHRASE, String.class, "Optional password to use when opening the connection.  You must provide either " + "DATA_SOURCE or DRIVER, URL, USERNAME, and PASSWORD.", null);

    public Formula getFormula() {
        Reagent[] reagents = new Reagent[] { ATTRIBUTE_NAME, DATA_SOURCE, DRIVER, URL, USERNAME, PASSWORD, DATA_SOURCE_ATTRIBUTE_NAME, AbstractContainerTask.SUBTASKS };
        return new SimpleFormula(getClass(), reagents);
    }

    @Override
    public void init(EntityConfig config) {
        super.init(config);
        this.attribute_name = (Phrase) config.getValue(ATTRIBUTE_NAME);
        this.data_source = (Phrase) config.getValue(DATA_SOURCE);
        final Phrase dataSourceAttributeName = (Phrase) config.getValue(DATA_SOURCE_ATTRIBUTE_NAME);
        this.driverPhrase = (Phrase) config.getValue(DRIVER);
        this.urlPhrase = (Phrase) config.getValue(URL);
        this.usernamePhrase = (Phrase) config.getValue(USERNAME);
        this.passwordPhrase = (Phrase) config.getValue(PASSWORD);
        this.basicDataSourceTemplate = new BasicDataSourceTemplateImpl(dataSourceAttributeName, driverPhrase, urlPhrase, usernamePhrase, passwordPhrase);
    }

    public void perform(TaskRequest req, TaskResponse res) {
        final DataSource dataSource;
        if (this.driverPhrase != null && this.urlPhrase != null && this.usernamePhrase != null && this.passwordPhrase != null) {
            this.basicDataSourceTemplate.perform(req, res);
        } else {
            dataSource = (DataSource) data_source.evaluate(req, res);
            this.performCreateConnection(req, res, dataSource);
        }
    }

    protected void performCreateConnection(TaskRequest req, TaskResponse res, DataSource dataSource) {
        final Connection connection = DataSourceUtils.getConnection(dataSource);
        try {
            final String connectionAttrName = (String) attribute_name.evaluate(req, res);
            res.setAttribute(connectionAttrName, connection);
            super.performSubtasks(req, res);
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }
    }

    /**
     * Local DataSource template that executes {@link OpenConnectionTask#performCreateConnection(TaskRequest, TaskResponse, DataSource)}
     * in the call-back.
     */
    private final class BasicDataSourceTemplateImpl extends BasicDataSourceTemplate {

        private BasicDataSourceTemplateImpl(Phrase attributeNamePhrase, Phrase driverPhrase, Phrase urlPhrase, Phrase usernamePhrase, Phrase passwordPhrase) {
            super(attributeNamePhrase, driverPhrase, urlPhrase, usernamePhrase, passwordPhrase);
        }

        @Override
        protected void performWithDataSource(TaskRequest req, TaskResponse res, DataSource dataSource) {
            OpenConnectionTask.this.performCreateConnection(req, res, dataSource);
        }
    }
}
