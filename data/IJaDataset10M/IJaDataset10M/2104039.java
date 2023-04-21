package uk.ac.ebi.intact.util.simplegraph;

import java.util.Map;

/**
 * A simple graph package for temporary processing, for example to prepare output for graph analysis packages.
 * Extends Map to allow easy implementation of key-value functionality.
 */
@Deprecated
public interface BasicGraphI extends Map {

    public String getId();

    public void setId(String anId);

    public String getLabel();

    public void setLabel(String aLabel);

    public String getAc();
}
