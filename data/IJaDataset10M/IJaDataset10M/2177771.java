package org.sf.pkb.db;

import java.io.StringReader;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CachingTokenFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermDocs;
import org.apache.lucene.index.TermEnum;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MultiTermQuery;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocCollector;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.SpanScorer;
import org.sf.pkb.util.ProcessKeywordsUtil;

public class SearchIndexHelper {

    public static String[] getTerms() {
        long start = System.currentTimeMillis();
        TreeSet<String> treeSet = new TreeSet<String>(new AlphabeticalOrder());
        IndexReader reader = null;
        TermEnum enumerator = null;
        TermDocs termDocs = null;
        try {
            reader = IndexReader.open(DBHelper.getIndexDirectory(), true);
            termDocs = reader.termDocs();
            Term[] terms = new Term[3];
            terms[0] = new Term("content");
            terms[1] = new Term("subject");
            terms[2] = new Term("keywords");
            for (int i = 0; i < terms.length; i++) {
                enumerator = reader.terms(terms[i]);
                try {
                    while (enumerator.next()) {
                        Term term = enumerator.term();
                        if (term != null) {
                            termDocs.seek(term);
                            if (termDocs.next()) {
                                String txt = term.text();
                                if (!isFiltered(txt)) {
                                    treeSet.add(term.text());
                                }
                            }
                        }
                    }
                } catch (Exception ex1) {
                    ex1.printStackTrace();
                } finally {
                    enumerator.close();
                    termDocs.close();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                    reader = null;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        System.out.println("Getting all terms take " + (System.currentTimeMillis() - start) + " ms");
        return (String[]) treeSet.toArray(new String[] {});
    }

    public static String[] getFollowingTerms(String previousTerms) {
        long start = System.currentTimeMillis();
        IndexReader reader = null;
        try {
            reader = IndexReader.open(DBHelper.getIndexDirectory(), true);
            Searcher searcher = new IndexSearcher(reader);
            Analyzer analyzer = new StandardAnalyzer();
            String[] fields = new String[] { "keywords", "subject" };
            QueryParser parser = new MultiFieldQueryParser(fields, analyzer);
            Query query = parser.parse("\"" + previousTerms + "\"");
            TopDocCollector collector = new TopDocCollector(10);
            searcher.search(query, collector);
            ScoreDoc[] hits = collector.topDocs().scoreDocs;
            TreeSet<String> set = new TreeSet<String>();
            for (int i = 0; i < hits.length; i++) {
                Document doc = searcher.doc(hits[i].doc);
                for (int j = 0; j < fields.length; j++) {
                    String fieldName = fields[j];
                    String text = doc.get(fieldName);
                    CachingTokenFilter tokenStream = new CachingTokenFilter(analyzer.tokenStream(fieldName, new StringReader(text)));
                    Highlighter highlighter = new Highlighter(new SpanScorer(query, fieldName, tokenStream));
                    tokenStream.reset();
                    String result = highlighter.getBestFragments(tokenStream, text, 2, "...");
                    int bpos = result.lastIndexOf("</B>");
                    if (bpos >= 0) {
                        result = result.substring(result.lastIndexOf("</B>") + "</B>".length()).trim();
                        result = ProcessKeywordsUtil.extractKeywords(result);
                        if (result.indexOf(' ') > 0) {
                            result = result.substring(0, result.indexOf(' '));
                            set.add((previousTerms + result).toLowerCase());
                        } else if (result.trim().length() > 0) {
                            set.add((previousTerms + result).toLowerCase());
                        }
                    }
                }
            }
            String[] result = new String[set.size()];
            Iterator<String> itr = set.iterator();
            int index = 0;
            while (itr.hasNext()) {
                result[index++] = (String) itr.next();
            }
            System.out.println("Getting following terms take " + (System.currentTimeMillis() - start) + " ms");
            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                    reader = null;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            ;
        }
        System.out.println("Get following terms take " + (System.currentTimeMillis() - start) + " ms");
        return null;
    }

    public static boolean isFiltered(String str) {
        if (str.length() < 3) {
            return true;
        }
        if (!Character.isLetterOrDigit(str.charAt(0))) {
            return true;
        }
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isLetterOrDigit(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    public static String[] extractQueryKeywords(String key) {
        if (key == null || key.trim().length() == 0) {
            return null;
        }
        Analyzer analyzer = new StandardAnalyzer();
        QueryParser parser = new QueryParser("subject", analyzer);
        try {
            Query query = parser.parse(key);
            HashSet<String> set = new HashSet<String>();
            extractQueryKeywords(query, set);
            String[] results = new String[set.size()];
            Iterator<String> itr = set.iterator();
            int pos = 0;
            while (itr.hasNext()) {
                results[pos++] = (String) itr.next();
            }
            return results;
        } catch (Exception ex) {
            if (ex.getMessage() != null) {
                System.out.println("Error extract keywords from query : " + ex.getMessage());
            }
        }
        return new String[] { key };
    }

    private static void extractQueryKeywords(Query query, Set<String> set) {
        if (query instanceof BooleanQuery) {
            BooleanQuery bq = (BooleanQuery) query;
            BooleanClause[] bqs = bq.getClauses();
            if (bqs != null && bqs.length > 0) {
                for (int i = 0; i < bqs.length; i++) {
                    extractQueryKeywords(bqs[i].getQuery(), set);
                }
            }
        } else if (query instanceof TermQuery) {
            TermQuery termQuery = (TermQuery) query;
            HashSet<Term> termSet = new HashSet<Term>();
            termQuery.extractTerms(termSet);
            Iterator<Term> itr = termSet.iterator();
            while (itr.hasNext()) {
                Term term = (Term) itr.next();
                set.add(term.text());
            }
        } else if (query instanceof PhraseQuery) {
            PhraseQuery phraseQuery = (PhraseQuery) query;
            Term[] terms = phraseQuery.getTerms();
            if (terms != null) {
                for (int i = 0; i < terms.length; i++) {
                    set.add(terms[i].text());
                }
            }
        } else if (query instanceof PrefixQuery) {
            PrefixQuery prefixQuery = (PrefixQuery) query;
            HashSet<Term> termSet = new HashSet<Term>();
            prefixQuery.extractTerms(termSet);
            Iterator<Term> itr = termSet.iterator();
            while (itr.hasNext()) {
                Term term = (Term) itr.next();
                set.add(term.text());
            }
        } else if (query instanceof WildcardQuery) {
            MultiTermQuery multiTermQuery = (MultiTermQuery) query;
            HashSet<Term> termSet = new HashSet<Term>();
            multiTermQuery.extractTerms(termSet);
            Iterator<Term> itr = termSet.iterator();
            while (itr.hasNext()) {
                Term term = (Term) itr.next();
                String text = term.text();
                set.add(text);
            }
        } else if (query instanceof FuzzyQuery) {
            FuzzyQuery fuzzyQuery = (FuzzyQuery) query;
            Term term = fuzzyQuery.getTerm();
            set.add(term.text());
        }
    }
}
