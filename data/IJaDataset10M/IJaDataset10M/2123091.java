package com.simonstl.moe;

import java.util.List;

/**
* <p>The ComponentListI interface defines the bare minimum of functionality needed for the ordered content contained in MOE objects.</p>
*
* <p>version 0.01 is the initial release.</p>
*
* @version 0.01 21 August 2001
* @author Simon St.Laurent
**/
public interface ComponentListI extends ComponentCollectionI, List, Cloneable {

    /**
* Returns the textual content of child nodes, minus any markup.
**/
    public String getTextContent();

    public Object clone();
}
