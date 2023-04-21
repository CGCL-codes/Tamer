package org.proteored.miapeapi.interfaces.ge;

import java.util.Set;

public interface Protocol {

    public String getName();

    public String getDescription();

    public Set<Dimension> getDimensions();

    public Set<InterdimensionProcess> getInterdimensionProcesses();
}
