package com.skruk.elvis.search.analysis;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import java.io.File;
import java.io.Reader;
import java.util.Hashtable;

/**
 * Analyzer for Polish language. Supports an external list of stopwords (words that
 * will not be indexed at all) and an external list of exclusions (word that will
 * not be stemmed, but indexed).
 * A default set of stopwords is used unless an alternative list is specified, the
 * exclusion list is empty by default.
 */
public class PolishAnalyzer extends Analyzer {

    /**
   * The only instance of the class - pattern Singelton
   */
    private static PolishAnalyzer instance = null;

    /**
	 * Słowa które występują na tyle często i nie niosą informacji .
	 */
    private Hashtable stoptable = null;

    /**
	 * Wyjątki z których nie można wydobyć formantu tworzącego
	 */
    private Hashtable excltable = null;

    /**
   * Application context
   */
    private javax.servlet.ServletContext context = null;

    /**
	 * List of typical Polish stopwords - in case the file with stopwords could not be loaded
	 */
    private String[] Polish_STOP_WORDS = { "ja", "ty", "on", "ona", "ono", "my", "wy", "oni", "one", "mnie", "tobie", "jemu", "jej", "nam", "wam", "im", "moje", "twoje", "jego", "nasze", "wasze", "ich", "i", "lub", "albo", "z", "w", "o", "przy", "obok", "przez", "tak", "nie" };

    /**
	 * Builds an analyzer.
	 */
    private PolishAnalyzer() {
        this.stoptable = StopFilter.makeStopTable(Polish_STOP_WORDS);
        this.excltable = new Hashtable();
    }

    /**
	 * Builds an analyzer with the given stop words.
	 * @param stopwords Table with stopwords
	 */
    private PolishAnalyzer(Hashtable stopwords) {
        this.stoptable = stopwords;
        this.excltable = new Hashtable();
    }

    /**
	 * Builds an analyzer with the given stop words.
	 * @param stopwords Table with stopwords
	 * @param exclusionlist Table with exclusions
	 */
    private PolishAnalyzer(Hashtable stopwords, Hashtable exclusionlist) {
        this.stoptable = stopwords;
        this.excltable = exclusionlist;
    }

    /**
   * Gets instance of class - pattern Singelton
	 * @param context Application context
	 * @param stopwordsPath Path to stopwords file
	 */
    public static PolishAnalyzer getInstance(javax.servlet.ServletContext context, String stopwordsPath) {
        if (instance == null) {
            Hashtable stopwords = null;
            try {
                stopwords = WordlistLoader.getInstance().getWordtable(new File(stopwordsPath));
            } catch (org.jdom.JDOMException ex) {
                ex.printStackTrace();
            }
            instance = new PolishAnalyzer(stopwords);
            instance.context = context;
        }
        ;
        return instance;
    }

    /**
   * Gets instance of class - pattern Singelton
	 * @param context Application context
	 * @param stopwordsPath Path to stopwords file
   * @param exclusionlistPath Path to exclusions file
	 */
    public static PolishAnalyzer getInstance(javax.servlet.ServletContext context, String stopwordsPath, String exclusionlistPath) {
        if (instance == null) {
            Hashtable stopwords = null;
            Hashtable excltable = null;
            try {
                stopwords = WordlistLoader.getInstance().getWordtable(new File(stopwordsPath));
                excltable = WordlistLoader.getInstance().getWordtable(new File(exclusionlistPath));
            } catch (org.jdom.JDOMException ex) {
                ex.printStackTrace();
            }
            instance = new PolishAnalyzer(stopwords, excltable);
            instance.context = context;
        }
        ;
        return instance;
    }

    /**
   * Gets instance of class - pattern Singelton 
	 * Assumes default paths for stopwords and exlusions files
	 * @param context Application context
	 */
    public static PolishAnalyzer getInstance(javax.servlet.ServletContext context) {
        if (instance == null) {
            StringBuffer stopwordsPath = new StringBuffer(context.getInitParameter("installDir"));
            StringBuffer exclusionlistPath = new StringBuffer(context.getInitParameter("installDir"));
            stopwordsPath.append("xml/stopwords.xml");
            exclusionlistPath.append("xml/exclusions.xml");
            Hashtable stopwords = null;
            Hashtable excltable = null;
            try {
                stopwords = WordlistLoader.getInstance().getWordtable(new File(stopwordsPath.toString()));
                excltable = WordlistLoader.getInstance().getWordtable(new File(exclusionlistPath.toString()));
            } catch (org.jdom.JDOMException ex) {
                ex.printStackTrace();
            }
            instance = new PolishAnalyzer(stopwords, excltable);
            instance.context = context;
        }
        ;
        return instance;
    }

    /**
	 * Creates a TokenStream which tokenizes all the text in the provided Reader.
	 *
	 * @return  A TokenStream build from a StandardTokenizer filtered with
	 * 			StandardFilter, StopFilter, PolishStemFilter and LowerCaseFilter
	 */
    public TokenStream tokenStream(String fieldName, Reader reader) {
        TokenStream result = new StandardTokenizer(reader);
        result = new StandardFilter(result);
        result = new LowerCaseFilter(result);
        result = new StopFilter(result, stoptable);
        result = new PolishStemFilter(context, result, excltable);
        return result;
    }
}
