package net.sourceforge.ondex.parser.aracyc.parse;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.sourceforge.ondex.parser.aracyc.objects.DBName;
import net.sourceforge.ondex.parser.aracyc.objects.Dblink;
import net.sourceforge.ondex.parser.aracyc.objects.Publication;
import net.sourceforge.ondex.parser.aracyc.objects.SinkFactory;

/**
 * basic parser function for the concret(e) parsers.
 * @author peschr
 */
public abstract class AbstractParser implements IParser {

    private static Pattern dbName = Pattern.compile("\\(([A-Z\\-]+)\\s+(\\\"\\s*|\\|)*([\\w-]*).*");

    private static Pattern citations = Pattern.compile("\\w+");

    public abstract void distribute(String key, String value) throws Exception;

    public final void distributeCore(String key, String value) throws Exception {
        if (key.equals("UNIQUE-ID")) {
            this.addUniqueId(value);
        } else if (key.equals("NAME") || key.equals("COMMON-NAME")) {
            this.addCommonName(value);
        } else if (key.equals("COMMENT")) {
            this.getNode().setComment(value);
        } else if (key.equals("SYNONYMS")) {
            this.getNode().addSynonym(value);
        } else if (key.equals("DBLINKS")) {
            this.addDblink(value);
        } else if (key.equals("CITATIONS")) this.addCitation(value); else this.distribute(key, value);
    }

    /**
	 * adds a publication reference to a node. It tries first to find the publication in the SinkFactory
	 * @param value
	 * @throws Exception
	 */
    public void addCitation(String value) throws Exception {
        Matcher m = citations.matcher(value);
        if (m.matches()) {
            Publication pub = null;
            String citations = m.group(0).toUpperCase();
            try {
                pub = (Publication) SinkFactory.getInstance().findByUniqueId("PUB-" + citations);
            } catch (Exception e) {
                pub = (Publication) SinkFactory.getInstance().create(Publication.class, citations);
            }
            this.getNode().addPublication(pub);
        }
    }

    /**
	 * 
	 * @param value
	 * @throws Exception
	 */
    public final void addCommonName(String value) throws Exception {
        this.getNode().setCommonName(value.trim());
    }

    /**
	 * 
	 * @param value
	 * @throws Exception
	 */
    public final void addUniqueId(String value) throws Exception {
        this.start(value);
        this.getNode().setUniqueId(value);
    }

    /**
	 * adds a database reference
	 * @param link
	 */
    public final void addDblink(String link) {
        Matcher m = dbName.matcher(link);
        if (m.matches()) {
            String tmp = m.group(1).replace("-", "");
            DBName dbName = null;
            try {
                dbName = DBName.valueOf(tmp);
                String accession = m.group(3);
                Dblink dblink = new Dblink();
                dblink.setDbName(dbName);
                dblink.setAccession(accession);
                this.getNode().setDbLink(dblink);
            } catch (Exception e) {
            }
        } else {
        }
    }
}
