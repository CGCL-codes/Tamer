package org.opennms.web.acegisecurity;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.sql.DataSource;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import org.acegisecurity.ConfigAttributeDefinition;
import org.acegisecurity.intercept.method.AbstractMethodDefinitionSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author min zhang
 *
 */
public class RdbmsMethodInvocationDefinitionSource extends AbstractMethodDefinitionSource implements InitializingBean {

    protected static final Log logger = LogFactory.getLog(RdbmsMethodInvocationDefinitionSource.class);

    private DataSource dataSource;

    private RdbmsSecuredMethodDefinition rdbmsInvocationDefinition;

    private Ehcache methodResdbCache;

    private List getRdbmsEntryHolderList() {
        List list = null;
        Element element = this.methodResdbCache.get("methodRes");
        if (element != null) {
            list = (List) element.getValue();
        } else {
            list = this.rdbmsInvocationDefinition.execute();
            Element elem = new Element("methodRes", list);
            this.methodResdbCache.put(elem);
        }
        return list;
    }

    @Override
    protected ConfigAttributeDefinition lookupAttributes(Method arg0) {
        List list = this.getRdbmsEntryHolderList();
        if (list == null || list.size() == 0) return null;
        Iterator iter = list.iterator();
        while (iter.hasNext()) {
            RdbmsEntryHolder entryHolder = (RdbmsEntryHolder) iter.next();
            if (logger.isDebugEnabled()) {
                logger.debug("matched class： '" + arg0.getName() + "'；to" + entryHolder.getRes());
            }
            return entryHolder.getCad();
        }
        return null;
    }

    public Iterator getConfigAttributeDefinitions() {
        Set set = new HashSet();
        Iterator iter = this.getRdbmsEntryHolderList().iterator();
        while (iter.hasNext()) {
            RdbmsEntryHolder entryHolder = (RdbmsEntryHolder) iter.next();
            set.add(entryHolder.getCad());
        }
        return set.iterator();
    }

    public void afterPropertiesSet() throws Exception {
        this.rdbmsInvocationDefinition = new RdbmsSecuredMethodDefinition(this.getDataSource());
        if (this.methodResdbCache == null) throw new IllegalArgumentException("Must config a methodResdbCache for RdbmsFilterInvocationDefinitionSource");
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Ehcache getMethodResdbCache() {
        return methodResdbCache;
    }

    public void setMethodResdbCache(Ehcache methodResdbCache) {
        this.methodResdbCache = methodResdbCache;
    }
}
