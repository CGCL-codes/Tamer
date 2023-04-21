package sntool.plugin;

import java.util.Properties;
import sntool.plugin.metadata.*;

/**
 * Base plugin.
 *
 * <p>Created in 20/12/2002 by Ant�nio S. R. Gomes
 *
 * @author <a href="mailto:asrgomes@dca.fee.unicamp.br">Antonio S.R. Gomes<a/>
 * @version $Id: AbstractPlugin.java,v 1.1 2003/10/22 03:06:49 asrgomes Exp $
 */
public class AbstractPlugin implements Plugin {

    protected PluginMetadata metadata;

    /**
	 * @see sntool.plugin.Plugin#init(Properties)
	 */
    public void init(Properties props) throws TransitionFailedException {
    }

    /**
	 * @see sntool.plugin.Plugin#destroy()
	 */
    public void destroy() throws TransitionFailedException {
    }

    /**
	 * @see sntool.plugin.Plugin#setMetadata(PluginMetadata)
	 */
    public void setMetadata(PluginMetadata metadata) {
        this.metadata = metadata;
    }

    /**
	 * @see sntool.plugin.Plugin#getMetadata()
	 */
    public PluginMetadata getMetadata() {
        return metadata;
    }
}
