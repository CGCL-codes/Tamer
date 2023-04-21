package edu.unika.aifb.rules.rules.heuristic.advanced;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;
import com.google.soap.search.GoogleSearch;
import com.google.soap.search.GoogleSearchFault;
import com.google.soap.search.GoogleSearchResult;
import com.google.soap.search.GoogleSearchResultElement;
import edu.unika.aifb.rules.result.ResultTable;
import edu.unika.aifb.rules.rules.heuristic.Heuristic;
import edu.unika.aifb.rules.util.UserInterface;

/**
 * @author meh
 *
 */
public class GoogleSnippets implements Heuristic {

    private static final String GOOGLEKEY = "";

    private static final String STOPWORDFILE = "C:/Documents/Implementation/rules/lib/stopwords.txt";

    private List stopwords;

    private Hashtable hash = new Hashtable();

    public GoogleSnippets() {
        try {
            stopwords = getEntries(STOPWORDFILE);
        } catch (IOException e) {
            UserInterface.print(e.getMessage());
        }
    }

    public double get(Object object1, Object object2) {
        String word1;
        String word2;
        try {
            word1 = (String) object1;
            word2 = (String) object2;
        } catch (Exception e) {
            word1 = object1.toString();
            word2 = object2.toString();
        }
        if ((word1 == null) || (word2 == null)) return 0;
        if ((word1.length() < 3) || (word2.length() < 3)) return 0;
        try {
            Object result1 = hash.get(word1);
            if (result1 == null) {
                result1 = retrieveGoogleSnippets(word1);
                hash.put(word1, result1);
            }
            String string1 = (String) result1;
            Object result2 = hash.get(word2);
            if (result2 == null) {
                result2 = retrieveGoogleSnippets(word2);
                hash.put(word2, result2);
            }
            String string2 = (String) result2;
            double finalresult = getSimilarity(string1, string2);
            return finalresult;
        } catch (Exception e) {
            return 0;
        }
    }

    private String retrieveGoogleSnippets(String word) {
        StringBuffer sbPages = new StringBuffer();
        GoogleSearch gs = new GoogleSearch();
        gs.setKey(GOOGLEKEY);
        gs.setQueryString("\"" + word + "\"");
        GoogleSearchResult gsr = null;
        try {
            gsr = gs.doSearch();
        } catch (GoogleSearchFault e) {
            UserInterface.print(e.getMessage());
            return null;
        }
        GoogleSearchResultElement[] gsre = gsr.getResultElements();
        for (int i = 0; ((i < gsre.length) && (i < 10)); i++) {
            String sPage = gsre[i].getTitle() + " " + gsre[i].getSnippet();
            sPage = getText(sPage);
            sbPages.append(sPage + "\n\n");
        }
        return sbPages.toString();
    }

    private String getText(String sPage) {
        int iStart = Math.max(sPage.indexOf("<body>"), sPage.indexOf("<BODY>"));
        int iEnd = Math.max(sPage.indexOf("</body>"), sPage.indexOf("</BODY>"));
        if (iStart > 0 && iEnd > 0) {
            try {
                sPage = sPage.substring(iStart + 6, iEnd);
            } catch (StringIndexOutOfBoundsException e) {
            }
        }
        sPage = sPage.replaceAll("<[^>]*>", " ");
        sPage = sPage.replaceAll("&[a-z]+;", " ");
        sPage = sPage.replaceAll("<(script|SCRIPT)[^>]*>[^(</)]+</(script|SCRIPT)>", " ");
        sPage = sPage.replaceAll("[A-Za-z]\\(.*\\)[ ]?\\{[^\\}]*\\}", " ");
        sPage = sPage.replaceAll("[A-Za-z]\\(\\)", " ");
        sPage = sPage.replaceAll("<(style|STYLE)[^>]*>[^(</)]+</(style|STYLE)>", " ");
        sPage = sPage.replaceAll("[A-Z]+(\\-[A-Z]+)?:( [0-9]pt)?( [0-9]px)?( solid)?( bolder)?( #[0-9a-z]+)?(;)?", " ");
        sPage = sPage.replaceAll("(Verdana|Arial|Helvetica|sans-serif)(,)?", " ");
        sPage = sPage.replaceAll("<[^ ]*", " ");
        sPage = sPage.replaceAll("[ \t\n\f\r]{2,}", "\n");
        String s = "Google is not affiliated with the authors of this page nor responsible for its content.";
        int iGoogle = sPage.indexOf(s);
        if (iGoogle != -1) {
            sPage = sPage.substring(iGoogle + s.length(), sPage.length());
        }
        sPage = sPage.toString().replaceAll("[\\n]", " ");
        return sPage;
    }

    private double getSimilarity(String sText1, String sText2) {
        Vector v1 = new Vector();
        Vector v2 = new Vector();
        Hashtable htToken2Freq1 = getToken2Freq(sText1);
        Hashtable htToken2Freq2 = getToken2Freq(sText2);
        Enumeration e1 = htToken2Freq1.keys();
        while (e1.hasMoreElements()) {
            String s1 = (String) e1.nextElement();
            Integer d1 = (Integer) htToken2Freq1.get(s1);
            Integer d2 = (Integer) htToken2Freq2.get(s1);
            if (d2 == null) {
                d2 = new Integer(0);
            }
            v1.add(d1);
            v2.add(d2);
        }
        Enumeration e2 = htToken2Freq2.keys();
        while (e2.hasMoreElements()) {
            String s2 = (String) e2.nextElement();
            Integer d2 = (Integer) htToken2Freq2.get(s2);
            Integer d1 = (Integer) htToken2Freq1.get(s2);
            if (d1 == null) {
                v1.add(new Integer(0));
                v2.add(d2);
            }
        }
        return getSimilarityCosinus(v1, v2);
    }

    private Hashtable getToken2Freq(String sText) {
        Hashtable htToken2Freq = new Hashtable();
        StringTokenizer st = new StringTokenizer(sText, " \n\t.,:;()-\"\'?!");
        int iTokens = 0;
        while (st.hasMoreTokens()) {
            String sToken = st.nextToken().toLowerCase();
            if (stopwords.contains(sToken) == false) {
                Integer dFreq = (Integer) htToken2Freq.get(sToken);
                if (dFreq == null) {
                    dFreq = new Integer(0);
                }
                Integer newFreq = new Integer(dFreq.intValue() + 1);
                htToken2Freq.put(sToken, newFreq);
            }
        }
        return htToken2Freq;
    }

    private double getSimilarityCosinus(Vector v1, Vector v2) {
        double dSum1 = 0.0;
        double dSum2a = 0.0;
        double dSum2b = 0.0;
        for (int i = 0; i < v1.size(); i++) {
            int d1 = ((Integer) v1.get(i)).intValue();
            int d2 = ((Integer) v2.get(i)).intValue();
            dSum1 += d1 * d2;
            dSum2a += d1 * d1;
            dSum2b += d2 * d2;
        }
        dSum2a = Math.sqrt(dSum2a);
        dSum2b = Math.sqrt(dSum2b);
        double dSim = 0.0;
        if (dSum2a > 0.0 && dSum2b > 0.0) {
            dSim = dSum1 / (dSum2a * dSum2b);
        }
        return dSim;
    }

    private List getEntries(String sFile) throws IOException {
        ArrayList al = new ArrayList();
        File file = new File(sFile);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String sLine = null;
            while ((sLine = reader.readLine()) != null) {
                sLine = sLine.trim();
                if (sLine.length() > 0) {
                    al.add(sLine);
                }
            }
        } finally {
            if (reader != null) reader.close();
        }
        return al;
    }

    public static void main(String[] args) {
        GoogleSnippets google = new GoogleSnippets();
        double res = google.get("mickey", "minnie");
        System.out.println(res);
    }

    public void setPreviousResult(ResultTable resultTable) {
    }
}
