package com.rapidminer.parameter;

import com.rapidminer.operator.ports.InputPort;
import com.rapidminer.operator.ports.Port;

/**
 * This interfaces provides the possibility to retrieve InputPorts during runtime
 * to check for example if the {@link InputPort} is connected or not. 
 * 
 * 
 * @author Nils Woehler
 *
 */
public interface PortProvider {

    /** Returns the desired {@link InputPort}. */
    public Port getPort();
}
