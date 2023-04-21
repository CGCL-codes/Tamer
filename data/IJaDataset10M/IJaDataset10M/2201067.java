package com.adactus.mpeg21.didl.model;

import java.util.List;

/**
 * 
 * The DIDL is the root element and has either an item or a container. 
 * 
 * @author Thomas Rørvik Skjølberg
 *
 */
public interface DIDL {

    /**
	 * 
	 * Get the {@link Item} document root instance.
	 * 
	 * @return the document root item, null if not set
	 */
    Item getItem();

    /**
	 * 
	 * Get the {@link Container} document root instance.
	 * 
	 * @return the document root container, null if not set
	 */
    Container getContainer();

    /**
	 * 
	 * Get the {@link DIDLInfo} associated with this instance.
	 * 
	 * @return the document info, null if not set
	 */
    DIDLInfo getDIDLInfo();

    /**
	 * 
	 * Get the {@link Declarations}s associated with this instance.
	 * 
	 * @return list of declarations
	 */
    List<Declarations> getDeclarations();
}
