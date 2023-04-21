package com.loribel.commons.abstraction;

import java.io.File;

/**
 * File Owner.
 *
 * @author Gregory Borelli
 */
public interface GB_FileOwnerSet extends GB_FileOwner {

    void setFile(File a_file);
}
