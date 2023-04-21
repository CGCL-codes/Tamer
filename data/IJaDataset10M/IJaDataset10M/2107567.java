package org.dave.bracket.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.text.ParseException;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;
import org.dave.bracket.properties.big.LineScanner;

/**
 * In java, java.util.Properties is not an Interface. Bracket-Properties
 * has one, which allows for a standard and a Sorted Implementation. The
 * standard implementation is backed by a LinkedHashMap, which keeps insertion
 * order intact. This is a key issue for serious use of properties files.
 * 
 * Properties is also the home to the static Factory, which is the supported way to 
 * instantiate instances.
 * 
 * @author Dave
 *
 */
public interface Properties {

    /**
	 * Can be used to get direct access to the Entry data structures
	 * @return
	 */
    public Map<String, ValueModel> getPropertyMap();

    /**
	 * Get the value.
	 * @param key
	 * @return
	 */
    public String get(String key);

    public int intValue(String key);

    public long longValue(String key);

    /**
	 * Date value here is assumed to be a long
	 * @param key
	 * @return
	 */
    public java.util.Date dateValue(String key);

    public java.util.Date dateValue(String key, String format) throws ParseException;

    /**
	 * Get the properties as a tree of nodes. For example,
	 * 
	 * a.b.c=something
	 * a.b.c.d=something else
	 * 
	 * looks like
	 * 
	 * a
	 *   b
	 *     c - something
	 *       d - something else
	 *       
	 *  This method is identical in results to getTree(regex) where the regex
	 *  is "\\.". That is, the separator token in the key is a full stop    
	 * @return
	 */
    public Node getTree();

    /**
	 * Get the properties as a tree of nodes with a selector
	 * 
	 * a.b.c=something
	 * a.b.c.d=something else
	 * a.b.c.e.f=item
	 * a.b.c.e=item2
	 * 
	 * 
	 *
	 */
    public Node getTree(GroupParams params);

    /**
	 * Get the list of comments, return an empty list if none
	 * 
	 * @param key
	 * @return
	 */
    public List<String> getComments(String key);

    /**
	 * The char found in the parse, normally = 
	 * 
	 * @param key
	 * @return
	 */
    public char getSeparator(String key);

    /**
	 * add the key and value or values. Useful with multi-line entries
	 * 
	 * @param key
	 * @param values
	 */
    public void put(String key, String... values);

    /**
	 * Number of entries in the underlying map
	 * 
	 * @return
	 */
    public int size();

    /**
	 * remove all entries from the underlying map
	 */
    public void clear();

    /**
	 * Returns true if the underlying map has this key
	 * @param key
	 * @return
	 */
    public boolean containsKey(String key);

    /**
	 * Returns true if the key exists and has a non-empty value
	 * 
	 * @param key
	 * @return
	 */
    public boolean hasValue(String key);

    /**
	 * Overwrite existing keys with the new ones, keep those existing ones that don't collide
	 * This operation is non-destructive on the input
	 * does not concatenate comments
	 * 
	 * @param props
	 * @return the merged properties
	 */
    public Properties merge(Properties props);

    /**
	 * Overwrite existing keys with the new ones, keep those existing ones that don't collide
	 * This operation is non-destructive on the input
	 * 
	 * @param props
	 * @return the merged properties
	 */
    public Properties merge(Properties props, boolean mergeComments);

    /**
	 * Cause a graph to become the contents of the properties file. Destructive
	 * to current entries, so this is not very useful yet
	 * 
	 * 
	 * @param rootNode
	 */
    public void synchronize(Node rootNode);

    /**
	 * Mode is the available combinations of lexer and parser
	 * 
	 * BasicToken - PropertiesLexer and PropertiesParser. 
	 * Input is a String and a list of tokens is created, then the list
	 * is parsed. Good for small properties files
	 * 
	 * StreamingToken - PropertiesStreamingLexer and PropertiesParser.
	 * Same parser as above but tokens are read from a stream. Tokens are
	 * also read in using java.util.Scanner.
	 * 
	 * Line - LineScanner and PropertiesParser2.
	 * Uses the LineScanner which is essentially a BufferedReader, and there is no
	 * separate token list, parser works directly off lines. Should work best for 
	 * larger properties files.
	 * 
	 *  Usage:
	 * 
	 *  Properties.Factory.Mode = Properties.Mode.StreamingToken;
	 *  Properties props = Properties.Factory.getInstance(reader);
	 * 
	 * @author Dave
	 *
	 */
    public enum Mode {

        BasicToken, StreamingToken, Line
    }

    public static final class Factory {

        public static Mode mode = Mode.BasicToken;

        private static Logger log = Logger.getLogger("org.dave.bracket.properties.Properties.Factory");

        public static Properties getInstance() {
            return new PropertiesImpl();
        }

        public static synchronized Properties getInstance(URL url) {
            switch(mode) {
                case BasicToken:
                    return new PropertiesImpl(url);
                case StreamingToken:
                    {
                        try {
                            PropertiesStreamingLexer lexer = new PropertiesStreamingLexer(url.openStream());
                            lexer.lex();
                            List<PropertiesToken> list = lexer.getList();
                            PropertiesImpl props = new PropertiesImpl();
                            new PropertiesParser(list, props).parse();
                            return props;
                        } catch (IOException x) {
                            x.printStackTrace();
                        }
                    }
                case Line:
                    {
                        try {
                            LineScanner lexer = new LineScanner(new InputStreamReader(url.openStream()));
                            PropertiesImpl props = new PropertiesImpl();
                            new PropertiesParser2(lexer, props).parse();
                            return props;
                        } catch (IOException x) {
                            x.printStackTrace();
                        }
                    }
            }
            return new PropertiesImpl(url);
        }

        public static synchronized Properties getInstance(Reader reader) {
            switch(mode) {
                case BasicToken:
                    return new PropertiesImpl(reader);
                case StreamingToken:
                    {
                        PropertiesStreamingLexer lexer = new PropertiesStreamingLexer(reader);
                        lexer.lex();
                        List<PropertiesToken> list = lexer.getList();
                        PropertiesImpl props = new PropertiesImpl();
                        new PropertiesParser(list, props).parse();
                        return props;
                    }
                case Line:
                    {
                        LineScanner lexer = new LineScanner(reader);
                        PropertiesImpl props = new PropertiesImpl();
                        new PropertiesParser2(lexer, props).parse();
                        return props;
                    }
            }
            return new PropertiesImpl(reader);
        }

        public static synchronized Properties getInstance(InputStream in) {
            switch(mode) {
                case BasicToken:
                    return new PropertiesImpl(in);
                case StreamingToken:
                    {
                        PropertiesStreamingLexer lexer = new PropertiesStreamingLexer(in);
                        lexer.lex();
                        List<PropertiesToken> list = lexer.getList();
                        PropertiesImpl props = new PropertiesImpl();
                        new PropertiesParser(list, props).parse();
                        return props;
                    }
                case Line:
                    {
                        LineScanner lexer = new LineScanner(new InputStreamReader(in));
                        PropertiesImpl props = new PropertiesImpl();
                        new PropertiesParser2(lexer, props).parse();
                        return props;
                    }
            }
            return new PropertiesImpl(in);
        }

        public static Properties getInstance(java.util.Properties legacy) {
            return new PropertiesImpl(legacy);
        }

        /**
		 * baseName is something like a.b.c.MyProperty which with Locale.AU will be 
		 * a search path like /a.b.c.MyProperty_en_AU.properties
		 * 
		 * @param baseName
		 * @param locale
		 * @return
		 */
        public static Properties getInstance(String baseName, Locale locale) {
            LocaleStringBuilder builder = new LocaleStringBuilder(baseName, locale);
            List<String> list = builder.getSearchStrings();
            PropertiesImpl base = new PropertiesImpl();
            for (String s : list) {
                InputStream in = null;
                try {
                    in = Thread.currentThread().getClass().getResourceAsStream(s);
                    if (in != null) {
                        base.merge(Properties.Factory.getInstance(in));
                    }
                } finally {
                    if (in != null) try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return base;
        }

        /**
		 * @param parent is a path representing the location of the working copy/export
		 * @param path is the relative requested file, like opt/app/eppstaff/portal-ext.properties
		 * @param locale details such as environment, cluster and instance
		 *
		 * @return
		 */
        public static Properties getInstance(File parent, String path, ServerLocale locale) throws IOException {
            ServerLocaleStringBuilder builder = new ServerLocaleStringBuilder(path, locale);
            List<String> list = builder.getSearchStrings();
            PropertiesImpl base = new PropertiesImpl();
            for (String s : list) {
                InputStream in = null;
                try {
                    File file = new File(parent, s);
                    if (!file.exists()) {
                        log.warn("File " + path + " does not exist in workspace " + parent.getCanonicalPath());
                    } else {
                        log.debug("File Exists: " + file.getCanonicalPath());
                        in = new FileInputStream(file);
                        if (in != null) {
                            base.merge(Properties.Factory.getInstance(in), false);
                        }
                    }
                } finally {
                    if (in != null) try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return base;
        }

        /**
		 * Not yet implemented
		 * 
		 * @param file
		 * @return
		 */
        public static Properties getInstanceFromXML(File file) {
            return null;
        }

        public static synchronized Properties sortedInstance(Properties props) {
            SortedPropertiesImpl impl = new SortedPropertiesImpl();
            return impl.merge(props);
        }

        public static synchronized Properties sortedInstance(Properties props, Comparator<String> comp) {
            SortedPropertiesImpl impl = new SortedPropertiesImpl(comp);
            return impl.merge(props);
        }
    }
}
