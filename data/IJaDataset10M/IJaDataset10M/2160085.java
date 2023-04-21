package org.allcolor.ywt.pipe;

import java.util.Map;

/**
 * @author Quentin Anciaux
 * @version 0.1.0
 */
@SuppressWarnings("unchecked")
public interface IPipeTransformer {

    /**
	 * DOCUMENT ME!
	 *
	 * @param pipe DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
    public CPipe transform(final CPipe pipe);

    /**
	 * DOCUMENT ME!
	 *
	 * @param pipe DOCUMENT ME!
	 * @param parameters DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
    public CPipe transform(final CPipe pipe, final Map parameters);
}
