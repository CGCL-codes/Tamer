package org.tm4j.topicmap.hibernate.index;

import net.sf.hibernate.type.Type;
import org.tm4j.topicmap.Topic;
import org.tm4j.topicmap.TopicMap;
import org.tm4j.topicmap.hibernate.TopicMapProviderImpl;
import org.tm4j.topicmap.index.basic.TopicTypesIndex;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * This class implements the TopicTypesIndex interface as
 * HQL queries against a Hibernate data store.
 *
 * @author <a href="mailto:kal@techquila.com">Kal Ahmed</a>
 * @since  0.8.0
 */
public class HibernateTopicTypesIndex extends HibernateIndex implements TopicTypesIndex {

    public HibernateTopicTypesIndex(TopicMapProviderImpl provider, TopicMap tm) {
        super(provider, tm);
    }

    /**
     * Returns the topics which are typed by <code>type</code>.
     * @param type The topic which types all of the topics returned.
     * @return An unmodifiable Collection containing those {@link Topic}s for which
     *         <code>type</code> is one of the types.
     */
    public Collection getTopicsOfType(Topic type) {
        return getTopicsOfType(type, true);
    }

    public Collection getTopicsOfType(Topic type, boolean includeMergedTopics) {
        ArrayList params = new ArrayList();
        ArrayList types = new ArrayList();
        getAllTopicDataObjects(type, params, types, includeMergedTopics);
        String typeQueryClause = getTopicQueryClause("type", params.size());
        String query = "select t from t in class org.tm4j.topicmap.hibernate.TopicDataObject, type in t.types.elements where " + typeQueryClause;
        Object[] paramArry = params.toArray(new Object[1]);
        Type[] typeArry = (Type[]) types.toArray(new Type[1]);
        Collection ret = doQuery(query, paramArry, typeArry);
        return Collections.unmodifiableCollection(ret);
    }

    /**
     * Returns the topics which are typed by all of the
     * topics in <code>types</code>.
     * @param types The topics which define the type all of the topics
     *         returned.
     * @return An unmodifiable Collection containing those {@link Topic}s for which
     *         all of the topics contained in <code>types</code> are
     *         one of the types.
     */
    public Collection getTopicsOfTypes(Topic[] types) {
        return getTopicsOfTypes(types, true);
    }

    public Collection getTopicsOfTypes(Topic[] types, boolean includeMergedTopics) {
        ArrayList params = new ArrayList();
        ArrayList paramTypes = new ArrayList();
        StringBuffer selectClause = new StringBuffer();
        selectClause.append("select t from t in class org.tm4j.topicmap.hibernate.TopicDataObject ");
        StringBuffer whereClause = new StringBuffer();
        for (int i = 0; i < types.length; i++) {
            if (i > 0) {
                whereClause.append(" and ");
            }
            Topic type = types[i];
            String param = "type" + String.valueOf(i);
            selectClause.append(", ");
            selectClause.append(param);
            selectClause.append(" in t.types.elements");
            int paramCount = params.size();
            getAllTopicDataObjects(type, params, paramTypes, includeMergedTopics);
            whereClause.append(getTopicQueryClause(param, params.size() - paramCount));
        }
        String query = selectClause.toString() + " where " + whereClause.toString();
        Object[] paramArry = params.toArray(new Object[1]);
        Type[] typeArry = (Type[]) paramTypes.toArray(new Type[1]);
        Collection ret = doQuery(query, paramArry, typeArry);
        return Collections.unmodifiableCollection(ret);
    }

    /**
     * Returns the topics which type other topics.
     * @return An unmodifiable Collection containing those {@link Topic}s which are the
     *         type of at least one topic in the topic map indexed.
     */
    public Collection getTopicTypes() {
        Collection ret = doQuery("select distinct type from t in class org.tm4j.topicmap.hibernate.TopicDataObject, type in t.types.elements where t.topicMap = ?", new Object[] { getTopicMap() });
        return Collections.unmodifiableCollection(ret);
    }
}
