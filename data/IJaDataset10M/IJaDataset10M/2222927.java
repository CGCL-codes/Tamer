package org.primordion.xholon.io;

import org.primordion.xholon.base.IXholon;

/**
 * Output Java file(s) in Quantum Event Processor (QEP) format.
 * @author <a href="mailto:ken@primordion.com">Ken Webb</a>
 * @see <a href="http://www.primordion.com/Xholon">Xholon Project website</a>
 * @since 0.7 (Created on August 14, 2007)
 * @see http://www.quantum-leaps.com for information about QEP.
 */
public interface IXholon2Qep {

    /**
	 * Initialize.
	 * @param qepFileName Name of the output QEP file.
	 * @param modelName Name of the model.
	 * @param root Root of the composite structure hierarchy to write out.
	 * @return Whether or not the initialization succeeded.
	 */
    public abstract boolean initialize(String qepFileName, String modelName, IXholon root);

    /**
	 * Write out all parts of the QEP file.
	 */
    public abstract void writeAll();
}
