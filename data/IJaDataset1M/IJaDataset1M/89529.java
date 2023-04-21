package org.lindenb.mwtools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import org.lindenb.berkeley.db.DuplicateDB;
import org.lindenb.berkeley.db.PrimaryDB;
import org.lindenb.berkeley.db.Walker;
import org.lindenb.io.IOUtils;
import org.lindenb.me.Me;
import org.lindenb.sw.vocabulary.RDF;
import org.lindenb.sw.vocabulary.RDFS;
import org.lindenb.util.Compilation;
import org.lindenb.util.StringUtils;
import org.lindenb.xml.XMLUtilities;
import com.sleepycat.bind.tuple.BooleanBinding;
import com.sleepycat.bind.tuple.IntegerBinding;
import com.sleepycat.bind.tuple.StringBinding;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.je.OperationStatus;
import com.sleepycat.je.Transaction;

/**
 * WPSubCat
 * Author: Pierre Lindenbaum
 * retrieves the articles having a given category in wikipedia
 */
public class WPSubCat extends WPAbstractTool {

    /** dummy transaction (not used) */
    private Transaction txn = null;

    /** berkeleyDB home */
    private File dbHome = new File(System.getProperty("java.io.tmpdir", "/tmp"), "bdb");

    /** BDB env */
    private Environment environment;

    /** the categories we're searching */
    private PrimaryDB<String, Integer> categories;

    /** the categories we're searching */
    private DuplicateDB<String, String> subclass2class = null;

    /** the articles we found */
    private PrimaryDB<String, Boolean> processed;

    /** search depth sub-categories */
    private int max_depth = 3;

    /** namespaces in WP we are looking, default is 14=categories */
    private Set<Integer> cmnamespaces = new HashSet<Integer>();

    /**  output is rdf schema ? */
    private boolean echo_rdf = false;

    /** private/empty cstor */
    private WPSubCat() {
    }

    /**
	 * OPen the BDB environement
	 * @throws DatabaseException
	 */
    private void open() throws DatabaseException, IOException {
        LOG.info("Open " + dbHome);
        if (!this.dbHome.exists()) {
            LOG.info("Creating " + dbHome);
            if (!this.dbHome.mkdir()) {
                throw new FileNotFoundException("File not found " + this.dbHome);
            }
        }
        if (!this.dbHome.isDirectory()) {
            throw new IOException("Not a Directory " + this.dbHome);
        }
        EnvironmentConfig envConfig = new EnvironmentConfig();
        envConfig.setAllowCreate(true);
        envConfig.setReadOnly(false);
        this.environment = new Environment(dbHome, envConfig);
        DatabaseConfig cfg = new DatabaseConfig();
        cfg.setAllowCreate(true);
        cfg.setReadOnly(false);
        cfg.setTemporary(true);
        this.categories = new PrimaryDB<String, Integer>(this.environment, txn, "wp_cats", cfg, new StringBinding(), new IntegerBinding());
        cfg = new DatabaseConfig();
        cfg.setAllowCreate(true);
        cfg.setReadOnly(false);
        cfg.setTemporary(true);
        this.processed = new PrimaryDB<String, Boolean>(this.environment, txn, "wp_proc", cfg, new StringBinding(), new BooleanBinding());
        if (this.echo_rdf) {
            cfg = new DatabaseConfig();
            cfg.setAllowCreate(true);
            cfg.setReadOnly(false);
            cfg.setTemporary(true);
            cfg.setSortedDuplicates(true);
            this.subclass2class = new DuplicateDB<String, String>(this.environment, txn, "subclass2class", cfg, new StringBinding(), new StringBinding());
        }
    }

    /**
	 * Close the BDB env
	 * @throws DatabaseException
	 */
    private void close() throws DatabaseException {
        if (this.subclass2class != null) {
            this.subclass2class.close();
        }
        this.categories.close();
        this.processed.close();
        this.environment.cleanLog();
        this.environment.close();
    }

    /** read starting articles from input stream */
    private void read(BufferedReader in) throws IOException, DatabaseException {
        String line;
        while ((line = in.readLine()) != null) {
            if (line.startsWith("#") || StringUtils.isBlank(line)) continue;
            line = line.trim().replace(' ', '_');
            if (categories.containsKey(txn, line)) {
                LOG.info(line + " already in model");
                continue;
            }
            categories.put(txn, line, 0);
        }
    }

    /**
	 * get each Article in this category
	 * @param entry
	 * @param level
	 * @throws DatabaseException
	 * @throws IOException
	 * @throws XMLStreamException
	 */
    private void process(String entry, int level) throws DatabaseException, IOException, XMLStreamException {
        if (level > this.max_depth) return;
        final int limit = 500;
        final QName AttClcontinue = new QName("cmcontinue");
        final QName AttTitle = new QName("title");
        String cmcontinue = null;
        String cmnamespace = "14";
        int count = 0;
        if (!this.cmnamespaces.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (Integer i : this.cmnamespaces) {
                if (sb.length() > 0) sb.append("|");
                sb.append(String.valueOf(i));
            }
            cmnamespace = sb.toString();
        }
        while (true) {
            String url = this.base_api + "?action=query" + "&list=categorymembers" + "&format=xml" + "&cmnamespace=" + cmnamespace + (cmcontinue != null ? "&cmcontinue=" + escape(cmcontinue) : "") + "&cmtitle=" + escape(entry) + "&cmlimit=" + limit;
            cmcontinue = null;
            LOG.info(url);
            XMLEventReader reader = this.xmlInputFactory.createXMLEventReader(openStream(url));
            while (reader.hasNext()) {
                XMLEvent event = reader.nextEvent();
                if (event.isStartElement()) {
                    StartElement e = event.asStartElement();
                    String name = e.getName().getLocalPart();
                    if (name.equals("cm")) {
                        Attribute cat = e.getAttributeByName(AttTitle);
                        if (cat != null) {
                            String rev = cat.getValue();
                            if (!categories.containsKey(txn, rev)) {
                                LOG.info("adding " + rev + " level=" + level);
                                categories.put(txn, rev, level + 1);
                                if (subclass2class != null) {
                                    subclass2class.put(txn, rev, entry);
                                }
                                ++count;
                            }
                        }
                    } else if (name.equals("categorymembers")) {
                        Attribute clcont = e.getAttributeByName(AttClcontinue);
                        if (clcont != null) {
                            cmcontinue = clcont.getValue();
                        }
                    }
                }
            }
            reader.close();
            if (cmcontinue == null) break;
        }
        LOG.info("count(" + entry + ")=" + count);
    }

    /** main loop
	 * loop over each depth, and call 'process' if a category
	 * was not already processed
	 * @throws DatabaseException
	 * @throws XMLStreamException
	 * @throws IOException
	 */
    private void run() throws DatabaseException, XMLStreamException, IOException {
        LOG.info("run");
        for (int depth = 0; depth <= this.max_depth; ++depth) {
            boolean done = false;
            while (!done) {
                done = true;
                Walker<String, Integer> w = this.categories.openWalker(txn);
                while (w.getNext() == OperationStatus.SUCCESS) {
                    int level = w.getValue();
                    if (level != depth) continue;
                    String cat = w.getKey();
                    if (!this.processed.containsKey(txn, cat)) {
                        LOG.info(cat);
                        done = false;
                        this.processed.put(txn, cat, true);
                        w.close();
                        w = null;
                        process(cat, level);
                        break;
                    }
                }
                if (w != null) w.close();
                LOG.info("loop done:" + done + " depth:" + depth);
            }
        }
        LOG.info("end-run");
    }

    private String getBase() {
        int i = base_api.indexOf("/w/api.php");
        if (i == -1) return this.base_api;
        return base_api.substring(0, i) + "/wiki/";
    }

    /** dump the result to stdout */
    private void dump() throws DatabaseException, IOException {
        LOG.info("dump");
        if (echo_rdf) {
            String prev = null;
            Walker<String, String> w = this.subclass2class.openWalker(txn);
            System.out.println("<rdf:RDF xmlns:rdf=\"" + RDF.NS + " xmlns:rdfs=\"" + RDFS.NS + "\">");
            while (w.getNext() == OperationStatus.SUCCESS) {
                String entry = w.getKey();
                if (prev == null || !prev.equals(entry)) {
                    prev = entry;
                    if (prev != null) {
                        System.out.println(" </rdfs:Class>");
                    }
                    System.out.println(" <rdfs:Class rdf:about=\"" + getBase() + "/wiki/" + URLEncoder.encode(entry.replace(' ', '_'), "UTF-8") + "\">");
                    System.out.println("  <rdfs:label>" + XMLUtilities.escape(entry.replace('_', ' ')) + "</rdfs:label>");
                }
                System.out.println("  <rdfs:subClassOf rdf:resource=\"" + getBase() + "/wiki/" + URLEncoder.encode(w.getValue().replace(' ', '_'), "UTF-8") + "\"/>");
            }
            if (prev != null) {
                System.out.println(" </rdfs:Class>");
            }
            System.out.println("</rdf:RDF>");
            w.close();
        } else {
            Walker<String, Integer> w = this.categories.openWalker(txn);
            while (w.getNext() == OperationStatus.SUCCESS) {
                System.out.println(w.getKey());
            }
            w.close();
        }
        LOG.info("end-dump");
    }

    public static void main(String[] args) {
        LOG.setLevel(Level.OFF);
        WPSubCat app = new WPSubCat();
        try {
            Set<String> added = new HashSet<String>();
            int optind = 0;
            while (optind < args.length) {
                if (args[optind].equals("-h")) {
                    System.err.println(Compilation.getLabel());
                    System.err.println("Download the articles having a defined category in wikipedia.");
                    System.err.println(Me.FIRST_NAME + " " + Me.LAST_NAME + " " + Me.MAIL + " " + Me.WWW);
                    System.err.println(" -debug-level <java.util.logging.Level> default:" + LOG.getLevel());
                    System.err.println(" -api <url> default:" + app.base_api);
                    System.err.println(" -rdf echo RDF");
                    System.err.println(" -ns <int> restrict to given namespace default:14");
                    System.err.println(" -db-home BDB default directory:" + app.dbHome);
                    System.err.println(" -d <integer> max recursion depth default:" + app.max_depth);
                    System.err.println(" -add <category> add a starting article");
                    System.err.println(" OR");
                    System.err.println(" (stdin|files) containing articles' titles");
                    return;
                } else if (args[optind].equals("-debug-level") || args[optind].equals("-log-level")) {
                    LOG.setLevel(Level.parse(args[++optind]));
                } else if (args[optind].equals("-rdf")) {
                    app.echo_rdf = true;
                } else if (args[optind].equals("-ns")) {
                    app.cmnamespaces.add(Integer.parseInt(args[++optind]));
                } else if (args[optind].equals("-api")) {
                    app.base_api = args[++optind];
                } else if (args[optind].equals("-d")) {
                    app.max_depth = Integer.parseInt(args[++optind]);
                } else if (args[optind].equals("-add")) {
                    added.add(args[++optind].replace(' ', '_'));
                } else if (args[optind].equals("-db_home")) {
                    app.dbHome = new File(args[++optind]);
                } else if (args[optind].equals("--")) {
                    optind++;
                    break;
                } else if (args[optind].startsWith("-")) {
                    System.err.println("Unknown option " + args[optind]);
                } else {
                    break;
                }
                ++optind;
            }
            app.open();
            for (String s : added) {
                LOG.info("adding " + s);
                app.categories.put(app.txn, s, 0);
            }
            if (optind == args.length && !added.isEmpty()) {
            } else if (optind == args.length) {
                LOG.info("read from stdin");
                java.io.BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
                app.read(r);
                r.close();
            } else {
                while (optind < args.length) {
                    String fname = args[optind++];
                    LOG.info("opening " + fname);
                    java.io.BufferedReader r = IOUtils.openReader(fname);
                    app.read(r);
                    r.close();
                }
            }
            app.run();
            app.dump();
            app.close();
        } catch (Throwable err) {
            err.printStackTrace();
        }
    }
}
