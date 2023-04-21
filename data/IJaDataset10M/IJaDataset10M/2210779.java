package org.primordion.xholon.io;

import org.primordion.xholon.base.IXholon;

/**
 * Output an XML file in SMC format.
 * @author <a href="mailto:ken@primordion.com">Ken Webb</a>
 * @see <a href="http://www.primordion.com/Xholon">Xholon Project website</a>
 * @since 0.7 (Created on August 14, 2007)
 */
public interface IXholon2Smc {

    /**
	 * Initialize.
	 * @param smcFileName Name of the output SMC file.
	 * @param modelName Name of the model.
	 * @param root Root of the composite structure hierarchy to write out.
	 * @return Whether or not the initialization succeeded.
	 */
    public abstract boolean initialize(String smcFileName, String modelName, IXholon root);

    /**
	 * Write out all parts of the SMC file.
	 */
    public abstract void writeAll();
}
