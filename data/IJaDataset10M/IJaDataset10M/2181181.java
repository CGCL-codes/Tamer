package org.primordion.memcomp.base;

import org.primordion.xholon.base.IXholon;

/**
 * Membrane Computing.
 * @author <a href="mailto:ken@primordion.com">Ken Webb</a>
 * @see <a href="http://www.primordion.com/Xholon">Xholon Project website</a>
 * @since 0.3 (Created on Jan 2, 2006)
 */
public interface IMembraneComputing extends IXholon {

    public static final int P_HERE = 0;

    public static final int P_OUT = 1;

    public static final int P_IN1 = 2;

    public static final int P_IN2 = 3;

    public static final int P_IN3 = 4;

    public static final int P_IN4 = 5;

    public static final int P_OUTPUT = 0;

    public static final int SIZE_MYAPP_PORTS = 6;

    public static final int SIG_APPEND = 100;

    public static final int SIG_DISSOLVE = 200;

    public static final int PRIORITY_DEFAULT = 100;

    public static final int PRIORITY_LOWEST = 0;

    public static final int SD_IN = 1;

    public static final int SD_OUT = 2;
}
