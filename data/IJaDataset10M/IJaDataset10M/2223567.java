package com.manydesigns.portofino.base;

import com.manydesigns.portofino.base.cache.CachedResult;
import com.manydesigns.portofino.base.cache.CachedResultSet;
import com.manydesigns.portofino.base.cache.ClassBasedCache;
import com.manydesigns.portofino.base.cache.Query;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Paolo Predonzani - paolo.predonzani@manydesigns.com
 * @author Angelo Lupo      - angelo.lupo@manydesigns.com
 */
public class QueryCache extends ClassBasedCache {

    public static final String copyright = "Copyright (c) 2005-2009, ManyDesigns srl";

    private final MDConfig config;

    private final Pattern pattern;

    /**
     * Creates a new instance of QueryCache
     */
    public QueryCache(MDConfig config, MDThreadLocals threadLocals) {
        super(threadLocals);
        this.config = config;
        pattern = Pattern.compile("\"" + config.getSchema1() + "\"\\.\"([^\"]+?)(_view|_up|_down)?\"");
    }

    public Matcher matchClassNames(CharSequence s) {
        return pattern.matcher(s);
    }

    public synchronized int getEstimatedByteSize() {
        int acc = 0;
        for (Object o : cacheMap.values()) {
            CachedResult current = (CachedResult) o;
            acc = acc + current.getEstimatedByteSize();
        }
        return acc;
    }

    public synchronized ResultSet executeQuery(Query query) throws Exception {
        Transaction tx = threadLocals.getCurrentTransaction();
        tx.sync();
        CachedResult result = (CachedResult) cacheMap.get(query);
        if (result == null) {
            Query queryCopy = new Query(query);
            PreparedStatement st = null;
            ResultSet rs = null;
            try {
                st = queryCopy.prepareStatement(tx);
                rs = st.executeQuery();
                result = new CachedResult(rs);
            } finally {
                if (rs != null) try {
                    rs.close();
                } catch (Exception ignore) {
                }
                if (st != null) try {
                    st.close();
                } catch (Exception ignore) {
                }
            }
            registerQueryWithClasses(queryCopy);
            cacheMap.put(queryCopy, result);
        }
        return new CachedResultSet(result);
    }

    protected synchronized void registerQueryWithClasses(Query q) {
        Matcher matcher = matchClassNames(q.getSql());
        while (matcher.find()) {
            try {
                String name = matcher.group(1);
                String suffix = matcher.group(2);
                MDClass cls = null;
                if ("_up".equals(suffix) || "_down".equals(suffix)) {
                    for (MDAttribute current : config.getAllAttributes()) {
                        if (current instanceof MDRelAttribute && current.getName().equals(name)) {
                            cls = current.getOwnerClass();
                            break;
                        }
                    }
                } else {
                    cls = config.getMDClassByName(name);
                }
                registerKeyWithClass(q, cls);
            } catch (Exception ignore) {
            }
        }
    }
}
