package com.zmicer.utils.model.junit;

import org.apache.log4j.Logger;

/**
 * $Author:: zmicer $<br/>
 * $Rev:: 82 $<br/>
 * $Date:: 2007-10-11 23:38:04 #$<br/>
 */
public class JustInterfaceImpl1 implements IJustInterface {

    /**
	 * Logger instance.
	 */
    public static final Logger LOG = Logger.getLogger(JustInterfaceImpl1.class);

    /**
	 * @see IJustInterface#doNothing()
	 */
    @Override
    public void doNothing() {
    }
}
