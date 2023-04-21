package net.sourceforge.pharos.web;

import net.sourceforge.pharos.utils.ResonseType;

/**
 * @author kaushikr
 *
 */
public interface Response {

    /**
	 * @return
	 */
    Boolean isForCompression();

    /**
	 * @return
	 */
    ResonseType getResponseType();
}
