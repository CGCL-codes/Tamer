package com.joe.lucene.facet;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermEnum;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.HitCollector;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.QueryWrapperFilter;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import com.joe.lucene.analyzer.MarcAnalyzer;
import com.joe.utils.io.GetString;

public class FacetedSearchTest {

    private static String index_dir = "F:\\opacindex\\solr\\data\\index";

    private static Directory fsDir = null;

    private static IndexSearcher indexSearcher = null;

    static {
        try {
            fsDir = FSDirectory.getDirectory(index_dir);
            indexSearcher = new IndexSearcher(fsDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void search(String q) throws ParseException, IOException {
        Query query = new QueryParser("marc", new MarcAnalyzer()).parse(q);
        long start = new Date().getTime();
        Hits hits = indexSearcher.search(query);
        long end = new Date().getTime();
        for (int i = 0; i < hits.length(); i++) {
            if (i > 20) {
                break;
            }
            Document doc = hits.doc(i);
            System.out.println(i + "->title:" + doc.get("title") + ",author:" + doc.get("author") + ",curlocal:" + doc.get("curlocal"));
        }
        System.out.println(q + "<==>" + query);
        System.err.println("Found " + hits.length() + " document(s) (in " + (end - start) + " milliseconds) that matched query '" + q + "'");
    }

    private static HashSet hashSet = new HashSet();

    public static void facetedSearch(String q) throws ParseException, IOException {
        Query query = new QueryParser("marc", new MarcAnalyzer()).parse(q);
        long start = new Date().getTime();
        Hits hits = indexSearcher.search(query);
        IndexReader indexReader = indexSearcher.getIndexReader();
        String facetFiled = "f_subject";
        TermEnum termEnum = indexReader.terms(new Term(facetFiled, ""));
        int count = 0;
        int notThisTerm = 0;
        while (termEnum.next()) {
            count++;
            Term term = termEnum.term();
            if (null == term || !term.field().equals(facetFiled)) {
                notThisTerm++;
                if (notThisTerm < 10) {
                    System.out.println("not this term = " + term);
                } else {
                    break;
                }
            }
            if (!hashSet.contains(term.text())) {
                hashSet.add(term.text());
            }
            if (count < 10) {
                System.out.println("term = " + term);
            }
        }
        if (count == 0) {
            System.err.println("û�н��뵽whileѭ���ڲ�.");
        }
        Iterator iter = hashSet.iterator();
        int limit = 0;
        while (iter.hasNext()) {
            String termStr = (String) iter.next();
            TermQuery vendorQuery = new TermQuery(new Term(facetFiled, termStr));
            MyHitCollector hitCollector = new MyHitCollector();
            indexSearcher.search(query, new QueryWrapperFilter(vendorQuery), hitCollector);
            int termCount = hitCollector.count();
            if (termCount > 0) {
                System.out.println(termStr + " -> " + termCount);
            }
        }
        System.out.println("count = " + count);
        System.out.println("hashSet.size() = " + hashSet.size());
    }

    public static void facetedSearchByHits(String q) throws ParseException, IOException {
        String facetFiled = "f_pubdate";
        Query query = new QueryParser("marc", new MarcAnalyzer()).parse(q);
        long start = new Date().getTime();
        Hits hits = indexSearcher.search(query);
        HashMap facetMap = new HashMap();
        System.out.println("hits.length() = " + hits.length());
        for (int i = 0; i < hits.length(); i++) {
            Document doc = hits.doc(i);
            Field[] fields = doc.getFields(facetFiled);
            if (fields == null) {
                System.err.println("this one is null <==> " + i + " ,continue next one.");
                continue;
            }
            for (int j = 0; j < fields.length; j++) {
                Field field = fields[j];
                String subjectStr = field.stringValue();
                if (facetMap.containsKey(subjectStr)) {
                    facetMap.put(subjectStr, Integer.parseInt(facetMap.get(subjectStr).toString()) + 1);
                } else {
                    facetMap.put(subjectStr, "1");
                }
            }
        }
        Set facetMapKeySet = facetMap.keySet();
        Iterator facetMapIter = facetMapKeySet.iterator();
        while (facetMapIter.hasNext()) {
            String subject = (String) facetMapIter.next();
            Object count = facetMap.get(subject);
            System.out.println(subject + " -> " + count);
        }
        if (true) {
            return;
        }
        IndexReader indexReader = indexSearcher.getIndexReader();
        TermEnum termEnum = indexReader.terms(new Term(facetFiled, ""));
        int count = 0;
        int notThisTerm = 0;
        while (termEnum.next()) {
            count++;
            Term term = termEnum.term();
            if (null == term || !term.field().equals(facetFiled)) {
                notThisTerm++;
                if (notThisTerm < 10) {
                    System.out.println("not this term = " + term);
                } else {
                    break;
                }
            }
            if (!hashSet.contains(term.text())) {
                hashSet.add(term.text());
            }
            if (count < 10) {
                System.out.println("term = " + term);
            }
        }
        if (count == 0) {
            System.err.println("û�н��뵽whileѭ���ڲ�.");
        }
        Iterator iter = hashSet.iterator();
        int limit = 0;
        while (iter.hasNext()) {
            String termStr = (String) iter.next();
            TermQuery vendorQuery = new TermQuery(new Term(facetFiled, termStr));
            MyHitCollector hitCollector = new MyHitCollector();
            indexSearcher.search(query, new QueryWrapperFilter(vendorQuery), hitCollector);
            int termCount = hitCollector.count();
            if (termCount > 0) {
                System.out.println(termStr + " -> " + termCount);
            }
        }
        System.out.println("count = " + count);
        System.out.println("hashSet.size() = " + hashSet.size());
    }

    public static void facetSearchByHitsFilter(String q) throws ParseException, IOException {
        String facetFiled = "f_pubdate";
        Query query = new QueryParser("marc", new MarcAnalyzer()).parse(q);
        long start = new Date().getTime();
        Hits hits = indexSearcher.search(query);
        Query userQuery = new TermQuery(new Term("marc", q));
        QueryParser queryParser = new QueryParser("marc", new MarcAnalyzer());
    }

    public static void run() throws ParseException, IOException {
        String str = null;
        while (!(str = GetString.getString()).equals("end")) {
            facetedSearchByHits(str);
        }
    }

    private static class MyHitCollector extends HitCollector {

        private int count = 0;

        @Override
        public void collect(int doc, float score) {
            try {
                count++;
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }

        public int count() {
            return count;
        }
    }

    /**
	 * @param args
	 * @throws IOException 
	 * @throws ParseException 
	 */
    public static void main(String[] args) throws ParseException, IOException {
        run();
    }
}
