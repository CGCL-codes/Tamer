package werkzeugkasten.twowaysql.dao.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import werkzeugkasten.twowaysql.Markers;
import werkzeugkasten.twowaysql.dao.BinderProducer;
import werkzeugkasten.twowaysql.dao.QueryLoader;
import werkzeugkasten.twowaysql.dao.QueryWrapper;
import werkzeugkasten.twowaysql.dao.SqlContext;
import werkzeugkasten.twowaysql.dao.SqlEnviroment;
import werkzeugkasten.twowaysql.dao.SqlExecutor;
import werkzeugkasten.twowaysql.dao.el.ExpressionParser;
import werkzeugkasten.twowaysql.nls.Messages;
import werkzeugkasten.twowaysql.tree.visitor.QueryTreeVisitor;

public class DefaultSqlEnviroment implements SqlEnviroment {

    static final Logger LOG = LoggerFactory.getLogger(DefaultSqlEnviroment.class);

    protected QueryLoader<?> queryLoader;

    protected ExpressionParser elparser;

    protected BinderProducer binderProducer;

    protected SqlExecutor executor;

    @Override
    @SuppressWarnings("unchecked")
    public <LC> QueryLoader<LC> getLoader(LC context) {
        return (QueryLoader<LC>) this.queryLoader;
    }

    public void setLoader(QueryLoader<?> loader) {
        if (LOG.isDebugEnabled()) {
            LOG.debug(Markers.DETAIL, Messages.SET_DEPENDENCY, loader, "loader");
        }
        this.queryLoader = loader;
    }

    @Override
    public <EC> SqlContext<EC> createContext(EC expressionContext, QueryWrapper twoWayQuery) {
        return new DefaultSqlContext<EC>(expressionContext, twoWayQuery);
    }

    protected ExpressionParser getELParser() {
        return this.elparser;
    }

    public void setELParser(ExpressionParser elparser) {
        if (LOG.isDebugEnabled()) {
            LOG.debug(Markers.DETAIL, Messages.SET_DEPENDENCY, elparser, "elparser");
        }
        this.elparser = elparser;
    }

    @Override
    public BinderProducer getBinderProducer() {
        return this.binderProducer;
    }

    public void setBinderProducer(BinderProducer producer) {
        if (LOG.isDebugEnabled()) {
            LOG.debug(Markers.DETAIL, Messages.SET_DEPENDENCY, producer, "binderProducer");
        }
        this.binderProducer = producer;
    }

    @Override
    public <EC> QueryTreeVisitor<SqlContext<EC>> createVisitor() {
        return new SqlTreeVisitor<EC>(getELParser(), getBinderProducer());
    }

    @Override
    public SqlExecutor getExecutor() {
        return this.executor;
    }

    public void setExecutor(SqlExecutor executor) {
        if (LOG.isDebugEnabled()) {
            LOG.debug(Markers.DETAIL, Messages.SET_DEPENDENCY, executor, "executor");
        }
        this.executor = executor;
    }
}
