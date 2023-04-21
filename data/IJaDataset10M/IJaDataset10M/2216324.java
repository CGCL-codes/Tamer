package org.proteored.miapeapi.interfaces.ge;

import java.util.Set;
import org.proteored.miapeapi.interfaces.Buffer;

public interface ElectrophoresisProtocol {

    /**
	 * Identifier needed by gelML exporter
	 * 
	 */
    public int getId();

    public String getName();

    public String getElectrophoresisConditions();

    public Set<Buffer> getAdditionalBuffers();

    public Set<Buffer> getRunningBuffers();
}
