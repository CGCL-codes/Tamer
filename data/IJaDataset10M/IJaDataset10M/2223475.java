package org.sss.module.hibernate;

import java.io.Reader;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.io.IOUtils;
import org.hibernate.CacheMode;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.jdbc.Work;
import org.sss.common.impl.AbstractModuleSupport;
import org.sss.common.model.Argument;
import org.sss.common.model.IContext;
import org.sss.common.model.IDatafield;
import org.sss.common.model.IModule;
import org.sss.common.model.IModuleList;
import org.sss.common.model.IResult;
import org.sss.common.model.LockInfo;
import org.sss.exception.ContainerException;
import static org.sss.common.impl.Constants.*;

/**
 * IModelSession Hibernate实现
 * @author Jason.Hoo (latest modification by $Author: hujianxin78728 $)
 * @version $Revision: 621 $ $Date: 2009-12-06 07:10:26 -0500 (Sun, 06 Dec 2009) $
 */
public class HibernateImpl extends AbstractModuleSupport {

    protected final List<List> values = new ArrayList<List>();

    protected final List<String> names = new ArrayList<String>();

    protected IContext ctx;

    protected Transaction transaction;

    protected Session session;

    protected int index = 0;

    protected int count = 0;

    public HibernateImpl(IContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public synchronized void dispose() {
        if (disposed) return;
        for (List list : values) list.clear();
        values.clear();
        names.clear();
        transaction = null;
        DbUtils.closeQuietly(session.close());
        session = null;
        disposed = true;
        super.dispose();
    }

    public void setAutoCreate(boolean flag) {
        HibernateUtils.autoCreate = flag;
    }

    public void setServices(String names) {
        HibernateUtils.names = names.split(",");
    }

    public void setPackageName(String packageName) {
        HibernateUtils.packageName = packageName;
    }

    @Override
    public void init() throws ContainerException {
        session = HibernateUtils.openSession();
        session.setCacheMode(CacheMode.IGNORE);
    }

    protected void reinit() {
        transaction = null;
        DbUtils.closeQuietly(session.close());
        session = null;
        init();
    }

    @Override
    public void begin() throws ContainerException {
        transaction = session.beginTransaction();
    }

    @Override
    public void commit() throws ContainerException {
        try {
            transaction.commit();
            reinit();
        } catch (Exception e) {
            ctx.setError(ERROR_SQL);
            rollback();
        }
    }

    @Override
    public void rollback() throws ContainerException {
        try {
            transaction.rollback();
        } catch (Exception e) {
        } finally {
            reinit();
        }
    }

    @Override
    public void save(IModule module) throws ContainerException {
        try {
            IHibernateModule hm = (IHibernateModule) module;
            hm.newHibernateObject();
            session.save(hm.getHibernateObject());
        } catch (Exception e) {
            log.error("", e);
        }
    }

    @Override
    public void update(IModule module) throws ContainerException {
        try {
            IHibernateModule hm = (IHibernateModule) module;
            if (hm.getHibernateObject() == null) get(module, module.getIdentifier());
            session.update(hm.getHibernateObject());
        } catch (Exception e) {
            log.error("", e);
        }
    }

    @Override
    public void delete(IModule module, Argument... arguments) throws ContainerException {
        try {
            IHibernateModule hm = (IHibernateModule) module;
            if (arguments.length == 0) {
                session.delete(hm.getHibernateObject());
                module.clear();
            } else {
                for (Object object : find(hm.getHibernateClass(), arguments)) session.delete(object);
            }
        } catch (Exception e) {
            log.error("", e);
        }
    }

    private Criterion processCriterion(Argument argument) {
        Argument[] args;
        switch(argument.opType) {
            case ISNULL:
                return Restrictions.isNull(argument.fieldName);
            case ISNOTNULL:
                return Restrictions.isNotNull(argument.fieldName);
            case NE:
                return Restrictions.ne(argument.fieldName, argument.value);
            case LE:
                return Restrictions.le(argument.fieldName, argument.value);
            case GE:
                return Restrictions.ge(argument.fieldName, argument.value);
            case LT:
                return Restrictions.lt(argument.fieldName, argument.value);
            case GT:
                return Restrictions.gt(argument.fieldName, argument.value);
            case LIKE:
                return Restrictions.like(argument.fieldName, argument.value);
            case BETWEEN:
                Object[] values = (Object[]) argument.value;
                return Restrictions.between(argument.fieldName, values[0], values[1]);
            case IN:
                return Restrictions.in(argument.fieldName, (Object[]) argument.value);
            case AND:
                args = (Argument[]) argument.value;
                return Restrictions.and(processCriterion(args[0]), processCriterion(args[1]));
            case OR:
                args = (Argument[]) argument.value;
                return Restrictions.or(processCriterion(args[0]), processCriterion(args[1]));
            case NOT:
                return Restrictions.not(processCriterion((Argument) argument.value));
            default:
                return Restrictions.eq(argument.fieldName, argument.value);
        }
    }

    private List find(Class clazz, Argument... arguments) throws ContainerException {
        if (transaction == null) session.clear();
        Criteria criteria = session.createCriteria(clazz);
        if (HibernateUtils.maxFetchSize > 0) criteria.setMaxResults(HibernateUtils.maxFetchSize);
        for (Argument argument : arguments) {
            if (argument.opType == ASC) criteria.addOrder(Order.asc(argument.fieldName)); else if (argument.opType == DESC) criteria.addOrder(Order.desc(argument.fieldName)); else criteria.add(processCriterion(argument));
        }
        return criteria.list();
    }

    @Override
    public void find(IModuleList list, Argument... arguments) throws ContainerException {
        IHibernateModuleList ml = (IHibernateModuleList) list;
        ml.setHibernateList(find(ml.getHibernateClass(), arguments));
    }

    @Override
    public void get(IModule module, Argument... arguments) throws ContainerException {
        IHibernateModule hm = (IHibernateModule) module;
        List<IModule> list = find(hm.getHibernateClass(), arguments);
        if (list.size() > 0) hm.setHibernateObject(list.get(0), true); else ctx.setError(NO_MORE_ROW);
    }

    @Override
    public void get(IModule module, Serializable id) throws ContainerException {
        IHibernateModule hm = (IHibernateModule) module;
        hm.setHibernateObject(session.get(hm.getHibernateClass(), id), false);
    }

    @Override
    public int execute(final String sql, final Object... args) throws ContainerException {
        index = 0;
        count = 0;
        session.doWork(new Work() {

            public void execute(Connection conn) throws SQLException {
                Statement stmt = null;
                ResultSet rs = null;
                try {
                    log.info("executeSQL: " + sql);
                    if (args.length != 0) {
                        stmt = conn.prepareStatement(sql);
                        for (int i = 0; i < args.length; i++) {
                            if (args[i] instanceof IDatafield) args[i] = ((IDatafield) args[i]).getValue();
                            if (args[i] instanceof BigDecimal) ((PreparedStatement) stmt).setBigDecimal(i + 1, (BigDecimal) args[i]); else if (args[i] instanceof Integer) ((PreparedStatement) stmt).setInt(i + 1, (Integer) args[i]); else if (args[i] instanceof String) ((PreparedStatement) stmt).setString(i + 1, (String) args[i]); else if (args[i] instanceof Timestamp) ((PreparedStatement) stmt).setTimestamp(i + 1, (Timestamp) args[i]); else if (args[i] instanceof java.sql.Date) ((PreparedStatement) stmt).setDate(i + 1, (java.sql.Date) args[i]); else if (args[i] instanceof Date) ((PreparedStatement) stmt).setTimestamp(i + 1, new Timestamp(((Date) args[i]).getTime())); else ((PreparedStatement) stmt).setObject(i + 1, args[i]);
                        }
                    } else stmt = conn.createStatement();
                    if (sql.startsWith("SELECT")) {
                        if (args.length == 0) rs = stmt.executeQuery(sql); else rs = ((PreparedStatement) stmt).executeQuery();
                        names.clear();
                        ResultSetMetaData md = rs.getMetaData();
                        for (int i = 1; i <= md.getColumnCount(); i++) names.add(md.getColumnLabel(i).toLowerCase());
                        values.clear();
                        while (rs.next()) {
                            List<Object> vs = new ArrayList<Object>();
                            for (int i = 1; i <= md.getColumnCount(); i++) vs.add(rs.getObject(i));
                            values.add(vs);
                        }
                        index = 0;
                    } else {
                        log.warn("executeSQL: update/delete not in transaction.");
                        conn.setAutoCommit(true);
                        if (args.length == 0) count = stmt.executeUpdate(sql); else count = ((PreparedStatement) stmt).executeUpdate();
                        log.info("executeSQL: " + count + " record(s) is effected.");
                    }
                } catch (SQLException e) {
                    log.info("", e);
                    ctx.setError(ERROR_SQL);
                    close();
                } finally {
                    DbUtils.closeQuietly(stmt);
                    DbUtils.closeQuietly(rs);
                }
            }
        });
        return count;
    }

    @Override
    public void fetch(IResult... results) throws ContainerException {
        try {
            if (index < values.size()) {
                List<Object> vs = values.get(index);
                for (IResult result : results) {
                    int idx = names.indexOf(result.getName().toLowerCase());
                    if (idx < 0) ctx.setError(ERROR_COLUMN); else {
                        Object object = vs.get(idx);
                        if (object == null) result.setValue(null); else if (object instanceof Clob) {
                            Reader r = ((Clob) object).getCharacterStream();
                            result.setValue(IOUtils.toString(r));
                            r.close();
                        } else result.setValue(object);
                    }
                }
                index++;
                ctx.setError(NO_ERROR);
            } else ctx.setError(NO_MORE_ROW);
        } catch (Exception e) {
            log.info("", e);
            ctx.setError(ERROR_SQL);
            close();
        }
    }

    @Override
    public void close() throws ContainerException {
        for (List list : values) list.clear();
        values.clear();
        names.clear();
    }

    @Override
    public int count(String name) throws ContainerException {
        return HibernateUtils.count(name);
    }

    @Override
    public LockInfo lock(String name, String text) throws ContainerException {
        LockInfo lockInfo = HibernateUtils.lock(name, text);
        if (lockInfo == null) ctx.setError(ERROR_LOCK); else if (!name.equals(lockInfo.name)) ctx.setError(ERROR_LOCKED);
        return lockInfo;
    }

    @Override
    public void unlock(String name) throws ContainerException {
        HibernateUtils.unlock(name);
    }
}
