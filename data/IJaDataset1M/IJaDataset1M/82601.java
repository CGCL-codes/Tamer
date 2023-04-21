package ontool.app;

import java.net.URL;
import java.util.Locale;
import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.help.HelpSetException;
import org.apache.log4j.Category;

/**
 * HelpProvider loads a HelpSet and creates a HelpBroker 
 * 
 * @author <a href="mailto:rcmds@dca.fee.unicamp.br">Ricardo Capitanio M. da Silva <a/>
 * @version $Id: HelpProvider.java,v 1.1 2003/10/22 03:06:44 asrgomes Exp $
 */
public class HelpProvider {

    protected final Category cat = Category.getInstance(HelpProvider.class);

    private ClassLoader loader = null;

    private HelpBroker hb = null;

    protected HelpSet hs = null;

    public HelpProvider(String hsName) {
        loader = this.getClass().getClassLoader();
        hs = initHS(hsName, loader);
        hb = hs.createHelpBroker();
        App.getInstance().setHelpProvider(this);
    }

    /**
	 *  Loads a helpSet
	 *
	 *@param  name    The helpset name (extension ".hs" must be used in the 
	 *                HelpSet file)
	 *@param  loader  The ClassLoader to use. If loader is null the 
	 *                default is used.
	 *@return         new Helpset 
	 */
    private HelpSet initHS(String name, ClassLoader loader) {
        cat.info("loading HelpSet");
        cat.debug("HelpSet name = " + name);
        HelpSet hsT = null;
        URL url = HelpSet.findHelpSet(loader, name, ".hs", Locale.getDefault());
        cat.debug("HelpSet url = " + url);
        try {
            hsT = new HelpSet(loader, url);
        } catch (HelpSetException e) {
            cat.error("could not load help set", e);
        }
        return hsT;
    }

    public HelpSet getHelpSet() {
        return hs;
    }

    public HelpBroker getHelpBroker() {
        return hb;
    }
}
