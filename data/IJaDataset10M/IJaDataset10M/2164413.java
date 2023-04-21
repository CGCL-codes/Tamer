package nglv2_2LuceneSearch;

import java.io.StringReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;
import newgenlib.marccomponent.conversion.NewGenLibImplementation;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopFieldDocs;
import nglv2_2luceneindex.*;
import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.RangeQuery;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocCollector;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

/**
 *
 * @author root
 */
public class NGLLuceneSearch {

    int mode = 0;

    BooleanQuery bqsearchlimits = new BooleanQuery();

    public NGLLuceneSearch() {
    }

    private String returndata(String arr[]) {
        String bib = "", lang = "", subloc = "", mattype = "", data = "";
        if (arr != null) {
            for (int i = 0; i < arr.length; i++) {
                data = data + " " + arr[i];
            }
        } else {
            data = "";
        }
        return data;
    }

    private BooleanQuery getQueryforSearchLimits(Element searchlimits) {
        BooleanQuery b1 = new BooleanQuery();
        try {
            List lssearchlimits = searchlimits.getChildren();
            String arraysubloc[] = null, arraylang[] = null, arraymattype[] = null, arraybibid[] = null;
            String language = "", range = "", date = "", publication = "", sublocation = "", bibliographicid = "", materialtype = "", library = "", openarchive = "", operator = "";
            for (int i = 0; i < lssearchlimits.size(); i++) {
                Element el = (Element) lssearchlimits.get(i);
                if (el.getName().equalsIgnoreCase("SubLocations")) {
                    List lssubloc = el.getChildren("Id");
                    String fieldarr[] = new String[1];
                    MultiFieldQueryParser mfqp1 = new MultiFieldQueryParser(fieldarr, new WhitespaceAnalyzer());
                    mfqp1.setDefaultOperator(QueryParser.OR_OPERATOR);
                    arraysubloc = new String[lssubloc.size()];
                    for (int j = 0; j < lssubloc.size(); j++) {
                        arraysubloc[j] = el.getChildText("Id");
                        sublocation = sublocation + " " + arraysubloc[j];
                    }
                } else if (el.getName().equalsIgnoreCase("Operator")) {
                    operator = el.getText();
                } else if (el.getName().equalsIgnoreCase("LibraryId")) {
                    library = library + " " + el.getText();
                } else if (el.getName().equalsIgnoreCase("Language")) {
                    List lslang = searchlimits.getChildren("Language");
                    arraylang = new String[lslang.size()];
                    for (int j = 0; j < lslang.size(); j++) {
                        Element lang = (Element) lslang.get(j);
                        arraylang[j] = String.valueOf(lang.getText());
                        language = language + " " + arraylang[j];
                    }
                } else if (el.getName().equalsIgnoreCase("MaterialTypeId")) {
                    List lsmattypeid = searchlimits.getChildren("MaterialTypeId");
                    arraymattype = new String[lsmattypeid.size()];
                    for (int j = 0; j < lsmattypeid.size(); j++) {
                        Element mattype = (Element) lsmattypeid.get(j);
                        arraymattype[j] = String.valueOf(mattype.getText());
                        materialtype = materialtype + " " + arraymattype[j];
                    }
                } else if (el.getName().equalsIgnoreCase("BibliographicLevelId")) {
                    List lsbibid = searchlimits.getChildren("BibliographicLevelId");
                    arraybibid = new String[lsbibid.size()];
                    for (int j = 0; j < lsbibid.size(); j++) {
                        Element bibid = (Element) lsbibid.get(j);
                        arraybibid[j] = String.valueOf(bibid.getText());
                        bibliographicid = bibliographicid + " " + arraybibid[j];
                    }
                } else if (el.getName().equalsIgnoreCase("OpenArchive")) {
                    openarchive = openarchive + " " + el.getText();
                } else if (el.getName().equalsIgnoreCase("PublicationYear")) {
                    publication = el.getText();
                    System.out.println("PUBLICATION YEAR " + publication);
                } else if (el.getName().equalsIgnoreCase("Range")) {
                    range = el.getText();
                    System.out.println("range YEAR " + range);
                }
            }
            if (publication.length() > 0) {
                System.out.println("OPERATOR IS " + operator);
                Term t1 = new Term("260_c", publication);
                Term t2 = new Term("260_c", range);
                Query query = new RangeQuery(t1, t2, true);
                Query query1 = new RangeQuery(null, t2, true);
                Query query2 = new RangeQuery(t1, null, true);
                System.out.println("QUERY " + query + " QUERY 1 " + query1 + " QUERY 2 " + query2);
            }
            Query queryDate = null;
            if (range.length() > 0) {
                System.out.println("OPERATOR IS " + operator);
                Term t1 = new Term("260_c", publication);
                Term t2 = new Term("260_c", range);
                Query query0 = new RangeQuery(t1, t2, true);
                Query query1 = new RangeQuery(null, t2, true);
                Query query2 = new RangeQuery(t1, null, true);
                System.out.println("QUERY " + query0 + " QUERY 1 " + query1 + " QUERY 2 " + query2);
            }
            if (operator.equalsIgnoreCase("=") && publication.length() > 0) {
                Term t1 = new Term("260_c", publication);
                QueryParser qp = new QueryParser("260_c", new WhitespaceAnalyzer());
                queryDate = qp.parse(publication);
                System.out.println("Q IN = " + queryDate);
            }
            if (operator.equalsIgnoreCase("<") && publication.length() > 0) {
                Term t1 = new Term("260_c", "0");
                Term t2 = new Term("260_c", publication);
                queryDate = new RangeQuery(t1, t2, true);
                System.out.println("Q in < " + queryDate);
            }
            if (operator.equalsIgnoreCase(">") && publication.length() > 0) {
                Term t1 = new Term("260_c", publication);
                Term t2 = new Term("260_c", "");
                queryDate = new RangeQuery(t1, null, true);
                System.out.println("Q in > " + queryDate);
            }
            if (operator.equalsIgnoreCase("Range") && publication.length() > 0 && range.length() > 0) {
                Term t1 = new Term("260_c", publication);
                Term t2 = new Term("260_c", range);
                queryDate = new RangeQuery(t1, t2, true);
                System.out.println("Q in range " + queryDate);
            }
            String bib = "", sublocid = "", lang = "", mat = "";
            bib = getTestedString(returndata(arraybibid).trim());
            sublocid = getTestedString(returndata(arraysubloc).trim());
            lang = getTestedString(returndata(arraylang).trim());
            mat = getTestedString(returndata(arraymattype).trim());
            System.out.println("data at" + bib + " SUBLOC " + sublocid + " lang " + lang + " mat " + mat);
            Query qbib = null, qlang = null, qmat = null, qsubloc = null, qlib = null, qoper = null, qopenarc = null, qlibr = null;
            String bibarr[] = new String[1];
            bibarr[0] = "BibliographicLevel";
            String langarr[] = new String[1];
            langarr[0] = "Language";
            String mattype[] = new String[1];
            mattype[0] = "MaterialType";
            String subloc[] = new String[1];
            subloc[0] = "Sublocation";
            String lib[] = new String[1];
            lib[0] = "OwnerLibraryId";
            String oper[] = new String[1];
            oper[0] = "260_c";
            String openarch[] = new String[1];
            openarch[0] = "OpenArchive";
            MultiFieldQueryParser mfqp = new MultiFieldQueryParser(bibarr, new WhitespaceAnalyzer());
            MultiFieldQueryParser mfqp1 = new MultiFieldQueryParser(langarr, new WhitespaceAnalyzer());
            MultiFieldQueryParser mfqp2 = new MultiFieldQueryParser(mattype, new WhitespaceAnalyzer());
            MultiFieldQueryParser mfqp3 = new MultiFieldQueryParser(subloc, new WhitespaceAnalyzer());
            MultiFieldQueryParser mfqp4 = new MultiFieldQueryParser(lib, new WhitespaceAnalyzer());
            MultiFieldQueryParser mfqp6 = new MultiFieldQueryParser(openarch, new WhitespaceAnalyzer());
            if (bib.length() > 0) {
                qbib = mfqp.parse(bib);
                b1.add(qbib, Occur.MUST);
            }
            if (sublocid.length() > 0) {
                qsubloc = mfqp3.parse(sublocid);
                b1.add(qsubloc, Occur.MUST);
            }
            if (lang.length() > 0) {
                qlang = mfqp1.parse(lang);
                b1.add(qlang, Occur.MUST);
            }
            if (mat.length() > 0) {
                qmat = mfqp2.parse(mat);
                b1.add(qmat, Occur.MUST);
            }
            if (operator.length() > 0 && (publication.length() > 0 || range.length() > 0)) {
                b1.add(queryDate, Occur.MUST);
            }
            if (openarchive.length() > 0) {
                if (openarchive.trim().equalsIgnoreCase("YES")) {
                    qopenarc = mfqp6.parse("A");
                    b1.add(qopenarc, Occur.MUST);
                } else {
                    qopenarc = mfqp6.parse("B");
                    b1.add(qopenarc, Occur.MUST);
                }
            }
            if (library.length() > 0) {
                qlibr = mfqp4.parse(library.trim());
                b1.add(qlibr, Occur.MUST);
            }
            System.out.println("BOOLEAN QUERY IS " + b1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return b1;
    }

    public String getTestedString(String string) {
        if (string == null || string.trim().length() == 0 || string.trim().equals("null") || string.trim().equals("NULL") || string.trim() == "NULL" || string.trim() == "null") {
            return "";
        } else {
            return string.trim();
        }
    }

    public String getTestedString(Object object) {
        if (object == null || object.equals("null") || object.equals("NULL")) {
            object = "";
        }
        return object.toString();
    }

    public Hashtable search(String xml) throws JDOMException, ParseException {
        System.out.println("XML IN SEARCH is " + xml);
        try {
            SAXBuilder sax = new SAXBuilder();
            org.jdom.Document doc = sax.build(new StringReader(xml));
            Element ele = (Element) doc.getRootElement();
            Element t1 = ele.getChild("Term1");
            Element t2 = ele.getChild("Term2");
            System.out.println("ELEMENT " + t1 + " ELEMENT " + t2);
            String boolop = ele.getChildText("BooleanString");
            Element elarr[] = null;
            if (t1 != null && t2 != null) {
                elarr = new Element[2];
                elarr[0] = t1;
                elarr[1] = t2;
            }
            if (t1 != null && t2 == null) {
                elarr = new Element[1];
                elarr[0] = t1;
            }
            String queries[] = getQuery(elarr);
            System.out.println("QUERIES.size 12312312 " + queries.length);
            BooleanQuery bq = new BooleanQuery();
            String finalquery = "";
            for (int i = 0; i < queries.length; i++) {
                System.out.println("IN SEARCH METHOD 12312312" + queries[i]);
                QueryParser q = new QueryParser(queries[i], new WhitespaceAnalyzer());
                Query que = q.parse(queries[i]);
                System.out.println("QUERIES " + que);
                if (boolop.equalsIgnoreCase("AND")) {
                    bq.add(que, Occur.MUST);
                } else if (boolop.equalsIgnoreCase("OR")) {
                    bq.add(que, Occur.SHOULD);
                } else if (boolop.equalsIgnoreCase("NOT")) {
                    if (finalquery.length() > 0) {
                        finalquery = queries[i];
                        bq.add(que, Occur.MUST_NOT);
                    } else {
                        finalquery = queries[i];
                        que = q.parse(finalquery);
                        bq.add(que, Occur.MUST);
                    }
                }
                if (bqsearchlimits != null) {
                    bq.add(bqsearchlimits, Occur.MUST);
                }
            }
            System.out.println("BQ IN SEARCH  IS " + bq);
            TopDocCollector collector = new TopDocCollector(10);
            IndexSearcher is = new IndexSearcher("/usr/Index");
            is.search(bq, collector);
            ScoreDoc[] hits = collector.topDocs().scoreDocs;
            System.out.println("Hits length " + hits.length);
            Hashtable htpkeys = new Hashtable();
            Hashtable htvalues = new Hashtable();
            Vector ownerlibids = new Vector();
            Vector catalogrecids = new Vector();
            for (int i = 0; i < hits.length; i++) {
                int docId = hits[i].doc;
                org.apache.lucene.document.Document d = is.doc(docId);
                ownerlibids.add(d.get("OwnerLibraryId"));
                catalogrecids.add(d.get("Catalogrecordid"));
            }
            htvalues.put("OwnerLibId", ownerlibids);
            htvalues.put("Catalogrecid", catalogrecids);
            for (int i = 0; i < ownerlibids.size() && i < catalogrecids.size(); i++) {
            }
            htpkeys.put("Values", htvalues);
            return htpkeys;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String[] getQuery(Element[] elearr) throws ParseException {
        MultiFieldQueryParser mfqp = null;
        String data = "";
        String qarr[] = new String[elearr.length];
        for (int i = 0; i < elearr.length; i++) {
            if (elearr[i] != null) {
                Element element = elearr[i];
                data = element.getChildText("Data");
                Element indele = element.getChild("Index");
                List ls = indele.getChildren();
                String fieldarr[] = new String[ls.size()];
                for (int j = 0; j < ls.size(); j++) {
                    Element field = (Element) ls.get(j);
                    fieldarr[j] = field.getText();
                }
                StringTokenizer tokens = new StringTokenizer(data, " ");
                String tokensarr[] = new String[tokens.countTokens()];
                System.out.println("tokens size " + tokens.countTokens());
                int k = 0;
                while (tokens.hasMoreTokens()) {
                    tokensarr[k] = tokens.nextToken();
                    k++;
                }
                if (element.getAttributeValue("kind").toString().equalsIgnoreCase("AS")) {
                    System.out.println("AS A PHRASE");
                    data = "\"" + data + "\"";
                    String datar[] = new String[1];
                    datar[0] = data;
                    qarr[i] = getTokenQuery(fieldarr, datar, "");
                } else if (element.getAttributeValue("kind").toString().equalsIgnoreCase("AND")) {
                    System.out.println("AND");
                    qarr[i] = getTokenQuery(fieldarr, tokensarr, "&&");
                } else if (element.getAttributeValue("kind").toString().equalsIgnoreCase("ANY")) {
                    System.out.println("ANY");
                    qarr[i] = getTokenQuery(fieldarr, tokensarr, "||");
                }
            }
        }
        return qarr;
    }

    private String getTokenQuery(String[] fieldarr, String[] tokensarr, String string) {
        String query = "";
        for (int i = 0; i < fieldarr.length; i++) {
            String queryField = "";
            for (int j = 0; j < tokensarr.length; j++) {
                if (queryField.length() > 0) {
                    queryField += " " + string + " " + fieldarr[i] + ":" + tokensarr[j];
                } else {
                    queryField += fieldarr[i] + ":" + tokensarr[j];
                }
            }
            if (query.length() > 0) {
                query += "|| (" + queryField + ")";
            } else {
                query += "(" + queryField + ")";
            }
        }
        System.out.println("QUERY in getTokenQuery " + query);
        return query;
    }

    public Hashtable luceneSearch(String xml) {
        Searcher search = new Searcher();
        try {
            org.jdom.input.SAXBuilder saxb = new org.jdom.input.SAXBuilder();
            org.jdom.Document doc = null;
            try {
                doc = saxb.build(new java.io.StringReader(xml));
            } catch (Exception ex) {
                ex.printStackTrace(System.out);
            }
            Element root = doc.getRootElement();
            if (root.getChild("SearchLimits") != null) {
                Element searchlimit = root.getChild("SearchLimits");
                bqsearchlimits = getQueryforSearchLimits(searchlimit);
            } else {
                bqsearchlimits = null;
            }
            search(xml);
            String termdata1 = "";
            String termatttype1 = "";
            String termattkind1 = "";
            String termdata2 = "";
            String termatttype2 = "";
            String termattkind2 = "";
            String booleanvar = "";
            String fieldarray[] = null;
            String fieldarray2[] = null;
            java.util.Hashtable htParams = new java.util.Hashtable();
            if (doc.getRootElement().getAttributeValue("searchlimits").equalsIgnoreCase("SET")) {
                List ls = doc.getRootElement().getChildren("SearchLimits");
            }
            if (doc.getRootElement().getChild("Term1") != null) {
                if (isnullorEmpty(doc.getRootElement().getChild("Term1").getChild("Data").getText())) {
                    termdata1 = doc.getRootElement().getChild("Term1").getChild("Data").getText();
                }
                if (isnullorEmpty(doc.getRootElement().getChild("Term1").getAttributeValue("type"))) {
                    termatttype1 = doc.getRootElement().getChild("Term1").getAttributeValue("type");
                    htParams.put("term1type", termatttype1);
                }
                if (isnullorEmpty(doc.getRootElement().getChild("Term1").getAttributeValue("kind"))) {
                    termattkind1 = doc.getRootElement().getChild("Term1").getAttributeValue("kind");
                    htParams.put("term1kind", termattkind1);
                }
            }
            if (doc.getRootElement().getChild("Term2") != null) {
                if (isnullorEmpty(doc.getRootElement().getChild("Term2").getChild("Data").getText())) {
                    termdata2 = doc.getRootElement().getChild("Term2").getChild("Data").getText();
                }
                if (isnullorEmpty(doc.getRootElement().getChild("Term2").getAttributeValue("type"))) {
                    termatttype2 = doc.getRootElement().getChild("Term2").getAttributeValue("type");
                    htParams.put("term2type", termatttype2);
                }
                if (isnullorEmpty(doc.getRootElement().getChild("Term2").getAttributeValue("kind"))) {
                    termattkind2 = doc.getRootElement().getChild("Term2").getAttributeValue("kind");
                    htParams.put("term2kind", termattkind2);
                }
            }
            if (isnullorEmpty(doc.getRootElement().getChild("BooleanString").getText())) {
                booleanvar = doc.getRootElement().getChild("BooleanString").getText();
                htParams.put("booleanop", booleanvar);
            }
            List termfield2lst, termfield1lst;
            if (doc.getRootElement().getChild("Term1") != null && doc.getRootElement().getChild("Term2") != null) {
                termfield1lst = doc.getRootElement().getChild("Term1").getChild("Index").getChildren("Field");
                termfield2lst = doc.getRootElement().getChild("Term2").getChild("Index").getChildren("Field");
                String fielddata1[] = new String[termfield1lst.size()];
                String fielddata2[] = new String[termfield2lst.size()];
                Element field1 = null, field2 = null;
                if (isnullorEmpty(termdata1) && isnullorEmpty(termattkind1) && isnullorEmpty(termatttype1) && termfield1lst.size() > 0) {
                    for (int l1 = 0; l1 < termfield1lst.size(); l1++) {
                        fieldarray = new String[termfield1lst.size()];
                        field1 = (Element) termfield1lst.get(l1);
                        fielddata1[l1] = field1.getText();
                    }
                    fieldarray = fielddata1;
                    if (isnullorEmpty(termdata2) && isnullorEmpty(termattkind2) && isnullorEmpty(termatttype2) && termfield2lst.size() > 0) {
                        for (int l1 = 0; l1 < termfield2lst.size(); l1++) {
                            fieldarray2 = new String[termfield1lst.size()];
                            field2 = (Element) termfield2lst.get(l1);
                            fielddata2[l1] = field2.getText();
                        }
                        fieldarray2 = fielddata2;
                    }
                }
            } else if (doc.getRootElement().getChild("Term1") != null && doc.getRootElement().getChild("Term2") == null) {
                termfield1lst = doc.getRootElement().getChild("Term1").getChild("Index").getChildren("Field");
                String fielddata1[] = new String[termfield1lst.size()];
                Element field1 = null, field2 = null;
                if (isnullorEmpty(termdata1) && isnullorEmpty(termattkind1) && isnullorEmpty(termatttype1) && termfield1lst.size() > 0) {
                    for (int l1 = 0; l1 < termfield1lst.size(); l1++) {
                        field1 = (Element) termfield1lst.get(l1);
                        fielddata1[l1] = field1.getText();
                    }
                    fieldarray = fielddata1;
                }
            }
            Hashtable htvalues = search(xml);
            return htvalues;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean isnullorEmpty(String value) {
        if (value != null && !value.equalsIgnoreCase("")) {
            return true;
        } else {
            return false;
        }
    }
}
