package ca.cbc.sportwire.servlet.data;

import java.lang.reflect.Method;
import java.lang.reflect.Constructor;
import org.apache.commons.collections.ExtendedProperties;
import org.apache.velocity.context.Context;
import org.apache.log4j.Category;
import ca.cbc.sportwire.util.PeriodicData;
import ca.cbc.sportwire.util.TTLCacheMap;

/**
 * <p><b>Lineup</b>: An abstract velocity interface to access the news
 * lineups such that lineups are only refreshed periodically; the
 * period of updates is set in the sportpage.conf as
 * <code>lineup.refresh=</code><i>seconds</i>
 *
 * </p>
 *
 * Created: Fri Dec 14 15:40:15 2001
 * <pre>
 * $Log: not supported by cvs2svn $
 * Revision 1.13  2002/01/26 06:46:29  garym
 * authorization bug in lineups
 *
 * Revision 1.12  2002/01/24 19:50:20  garym
 * Major restructuring of beans and pagehandlers
 *
 * Revision 1.11  2002/01/21 20:07:14  garym
 * NewsPage did not include $news; fixed error reporting to be a redirect to 404
 *
 * Revision 1.10  2002/01/16 00:56:26  garym
 * current site on cbc.ca/olympics; checkpoint release
 *
 * Revision 1.9  2002/01/14 21:51:46  garym
 * bug fixes in topics and config files migration to Extended Properties
 *
 * Revision 1.8  2002/01/04 18:37:20  garym
 * PeriodicData moved into sportwire.util package
 *
 * Revision 1.7  2002/01/03 03:36:25  garym
 * Abstracted periodic-refresh data cache objects
 *
 * Revision 1.6  2001/12/23 17:55:23  garym
 * Allow for multiple SportPage apps on one JVM
 *
 * Revision 1.5  2001/12/23 04:54:18  garym
 * Added TemplateLineup support and Velocity logfile setting
 *
 * Revision 1.4  2001/12/17 17:50:13  garym
 * Added date as vcal
 *
 * Revision 1.3  2001/12/17 09:36:26  garym
 * default page bug fixed. debug msg errors in Lineup and vcal
 *
 * Revision 1.2  2001/12/16 22:01:56  garym
 * fixes to default page handling
 *
 * Revision 1.1  2001/12/14 21:10:33  garym
 * Implement error pages and skeletal lineup
 *
 *
 * </pre>
 * @author <a href="mailto:garym@teledyn.com">Gary Lawrence Murphy</a>
 * @version $Id: Lineup.java,v 1.14 2002-01-29 21:08:00 garym Exp $
 */
public abstract class Lineup extends PeriodicData implements LineupBean, GlobalCacheProperties, SportPageProperties {

    static Category cat = Category.getInstance(Lineup.class.getName());

    /**
	 * <code>refresh</code>: 
	 *
	 */
    protected void refresh() {
        setLineup();
    }

    /**
	 * <code>getLineup</code>: this is the method used by the Velocity
	 * pages as <code>$news.lineup</code> to insert a pre-formatted
	 * block of HTML containing the full lineup.
	 *
	 * @return a <code>Object</code> with the lineup representation
	 */
    public abstract Object getLineup();

    /**
	 * <code>setLineup</code>: protected method called by the interval
	 * timer to refresh the internal state of the Lineup object.
	 *
	 */
    protected abstract void setLineup();

    private String topic;

    /**
	 * The Lineup topic is set from the <code>category</code> and is
	 * intended for setting the lineup category.
	 *
	 * @return String value of topic.
	 */
    protected String getTopic() {
        return this.topic;
    }

    /**
	 * Set the value of topic using a protected method; only the
	 * server should set the topic, most often with the category.
	 *
	 * @param v  Value to assign to topic.
	 */
    protected void setTopic(String v) {
        this.topic = v;
    }

    /**
	 * <code>Lineup</code> null constructor does nothing.
	 *
	 */
    protected Lineup() {
    }

    /**
	 * <code>defaultPeriod</code>: 
	 *
	 * @param sc an <code>ExtendedProperties</code> value
	 * @return an <code>int</code> value
	 */
    protected static int defaultPeriod(ExtendedProperties sc) {
        int period = DEFAULT_REFRESH;
        if (sc.containsKey(LINEUP_REFRESH_PROPERTY)) period = sc.getInteger(LINEUP_REFRESH_PROPERTY);
        return period;
    }

    /**
	 * <code>Lineup</code> category constructor sets the topic and
	 * starts the refresh thread.
	 *
	 * @param sc the global <code>ExtendedProperties</code> value
	 * @param category a <code>String</code> news category key
	 */
    public Lineup(ExtendedProperties sc, String category) {
        this(category, defaultPeriod(sc));
    }

    /**
	 * <code>Lineup</code> constructor
	 *
	 * @param category a <code>String</code> value
	 * @param period an <code>int</code> value
	 */
    protected Lineup(String category, int period) {
        super(period);
        setTopic(category);
    }

    /**
	 * <code>getCategory</code>: 
	 *
	 * @param context a <code>Context</code> value
	 * @return a <code>String</code> value
	 */
    protected static String getCategory(Context context) {
        String category = (String) context.get(Lineup.LINEUP_CATEGORY_VAR);
        if (category == null) {
            category = Lineup.DEFAULT_CATEGORY;
        }
        cat.debug("Lineup Category: " + category);
        return category;
    }

    /**
	 * <code>getCached</code>: 
	 *
	 * @param context a <code>Context</code> value
	 * @param lineupclass a <code>Class</code> value
	 * @return a <code>Lineup</code> value
	 */
    protected static Lineup getCached(Context context, Class lineupclass) {
        return getCached(getCategory(context), context, lineupclass);
    }

    protected static Lineup getCached(String category, Context context, Class lineupclass) {
        Lineup result = null;
        String key = lineupclass.getName() + ":" + category;
        ExtendedProperties sc = (ExtendedProperties) context.get(GLOBAL_VAR);
        if (sc.containsKey(MRU_PROPERTY)) {
            TTLCacheMap m = (TTLCacheMap) sc.getProperty(MRU_PROPERTY);
            result = (Lineup) m.get(key);
            if (result == null) {
                cat.debug("Creating new lineup: " + key);
                try {
                    Constructor c = lineupclass.getConstructor(new Class[] { ExtendedProperties.class, String.class });
                    result = (Lineup) c.newInstance(new Object[] { sc, category });
                    m.put(key, result);
                } catch (Exception e) {
                    cat.error("Cannot load " + lineupclass + ": " + e.getClass().getName() + ": " + e.getMessage());
                    e.printStackTrace();
                }
            } else cat.debug("using cached Lineup: " + key);
        } else cat.fatal("MISSING MRU");
        return result;
    }

    /**
	 * <code>getInstance</code>: 
	 *
	 * @param context a <code>Context</code> value
	 * @return a <code>Lineup</code> value
	 */
    public static Lineup getInstance(Context context) {
        Lineup lineup = null;
        ExtendedProperties sc = (ExtendedProperties) context.get(GLOBAL_VAR);
        String lineupclass = sc.getString(Lineup.LINEUP_CLASS_PROPERTY);
        String category = (String) context.get(Lineup.LINEUP_CATEGORY_VAR);
        if (category == null) {
            category = sc.getString(Lineup.LINEUP_CATEGORY_VAR);
        }
        if (category == null) {
            category = Lineup.DEFAULT_CATEGORY;
        }
        if (lineupclass != null) {
            try {
                cat.debug("Loading " + lineupclass + "(" + category + ")");
                Method m = Class.forName(lineupclass).getMethod("getInstance", new Class[] { Context.class });
                lineup = (Lineup) m.invoke(null, new Object[] { context });
            } catch (Exception e) {
                cat.error("Cannot load " + lineupclass + ": " + e.getClass().getName() + ": " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            cat.error("Bad or missing " + Lineup.LINEUP_CLASS_PROPERTY + " in config");
        }
        return lineup;
    }
}
