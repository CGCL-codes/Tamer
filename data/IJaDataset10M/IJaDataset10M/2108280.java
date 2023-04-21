package com.luzan.app.map.tool;

import com.luzan.app.map.utils.Configuration;
import com.luzan.common.util.Utils;
import com.luzan.common.lucene.search.UniqueFilter;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.*;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.LinkedList;
import java.util.List;

/**
 * SearchClient
 *
 * @author Alexander Bondar
 */
public class SearchClient {

    private static final Logger logger = Logger.getLogger(SearchClient.class);

    protected String cfg;

    private String query;

    private String type;

    private String lang;

    private String UID;

    private Double south;

    private Double west;

    private Double north;

    private Double east;

    public void setType(final String type) {
        this.type = type;
    }

    public void setLang(final String lang) {
        this.lang = lang;
    }

    public void setUID(final String UID) {
        this.UID = UID;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public void setCfg(String cfg) {
        this.cfg = cfg;
    }

    public void setSouth(String south) {
        this.south = Double.parseDouble(south);
    }

    public void setWest(String west) {
        this.west = Double.parseDouble(west);
    }

    public void setNorth(String north) {
        this.north = Double.parseDouble(north);
    }

    public void setEast(String east) {
        this.east = Double.parseDouble(east);
    }

    private void doIt() throws Throwable {
        Searcher searcher = new IndexSearcher(Configuration.getInstance().getString("index.dir", "/storage/lmaps/lucene/"));
        List<String> terms = new LinkedList<String>();
        List<String> queries = new LinkedList<String>();
        List<BooleanClause.Occur> occures = new LinkedList<BooleanClause.Occur>();
        type = QueryParser.escape(StringUtils.defaultIfEmpty(type, ""));
        lang = QueryParser.escape(StringUtils.defaultIfEmpty(lang, ""));
        UID = QueryParser.escape(StringUtils.defaultIfEmpty(UID, ""));
        Query geoQuery = MultiFieldQueryParser.parse(new String[] { "[" + Utils.double2SearchableString(south) + " TO " + Utils.double2SearchableString(north) + "]", "[" + Utils.double2SearchableString(west) + " TO " + Utils.double2SearchableString(east) + "]" }, new String[] { "SOUTH", "WEST" }, new BooleanClause.Occur[] { BooleanClause.Occur.MUST, BooleanClause.Occur.MUST }, new StandardAnalyzer());
        if (!StringUtils.isEmpty(query)) {
            terms.add("NAME");
            terms.add("DESCRIPTION");
            terms.add("TAG");
            queries.add(query);
            queries.add(query);
            queries.add(query);
            occures.add(BooleanClause.Occur.SHOULD);
            occures.add(BooleanClause.Occur.SHOULD);
            occures.add(BooleanClause.Occur.SHOULD);
        }
        final Query lQuery = MultiFieldQueryParser.parse(queries.toArray(new String[] {}), terms.toArray(new String[] {}), occures.toArray(new BooleanClause.Occur[] {}), new StandardAnalyzer());
        BooleanQuery q = new BooleanQuery();
        q.add(geoQuery, BooleanClause.Occur.MUST);
        q.add(lQuery, BooleanClause.Occur.MUST);
        if (type != null && !StringUtils.isEmpty(type)) {
            QueryParser parser = new QueryParser("TYPE", new StandardAnalyzer());
            q.add(parser.parse(type), BooleanClause.Occur.MUST);
        }
        if (lang != null && !StringUtils.isEmpty(lang)) {
            QueryParser parser = new QueryParser("LANG", new StandardAnalyzer());
            q.add(parser.parse(lang), BooleanClause.Occur.MUST);
        }
        if (UID != null && !StringUtils.isEmpty(UID)) {
            QueryParser parser = new QueryParser("PUID", new StandardAnalyzer());
            q.add(parser.parse(UID), BooleanClause.Occur.MUST);
        }
        final Hits hits = searcher.search(new MatchAllDocsQuery(), new CachingWrapperFilter(new UniqueFilter(new QueryWrapperFilter(q), "PUID")));
        System.out.println(hits.length() + " total matching documents");
        final int HITS_PER_PAGE = 10;
        for (int start = 0; start < hits.length(); start += HITS_PER_PAGE) {
            int end = Math.min(hits.length(), start + HITS_PER_PAGE);
            for (int i = start; i < end; i++) {
                Document doc = hits.doc(i);
                String title = doc.get("NAME");
                if (title != null) {
                    System.out.println(doc.get("PNAME") + " -- " + doc.get("TYPE") + ":" + title + " (" + doc.get("SOUTH") + ", " + doc.get("WEST") + ")");
                } else {
                    System.out.println((i + 1) + ". " + "No title for this document");
                }
            }
        }
        searcher.close();
    }

    public static void main(String args[]) {
        SearchClient proc = new SearchClient();
        String allArgs = StringUtils.join(args, ' ');
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(SearchClient.class, Object.class);
            for (PropertyDescriptor pd : beanInfo.getPropertyDescriptors()) {
                Pattern p = Pattern.compile("-" + pd.getName() + "\\s*([\\S]*)", Pattern.CASE_INSENSITIVE);
                final Matcher m = p.matcher(allArgs);
                if (m.find()) {
                    pd.getWriteMethod().invoke(proc, m.group(1));
                }
            }
            Configuration.getInstance().load(proc.cfg);
            proc.doIt();
        } catch (Throwable e) {
            logger.error("error", e);
            System.out.println(e.getMessage());
            try {
                BeanInfo beanInfo = Introspector.getBeanInfo(SearchClient.class);
                System.out.println("Options:");
                for (PropertyDescriptor pd : beanInfo.getPropertyDescriptors()) {
                    System.out.println("-" + pd.getName());
                }
            } catch (Throwable t) {
                System.out.print("Internal error");
            }
        }
    }
}
