package net.sf.jpkgmk.pkgmap;

import net.sf.jpkgmk.LineProvider;

/**
 * @author gommma (gommma AT users.sourceforge.net)
 * @author Last changed by: $Author: gommma $
 * @version $Revision: 2 $ $Date: 2008-08-20 15:14:19 -0400 (Wed, 20 Aug 2008) $
 * @since 1.0
 */
public interface PkgMapEntry extends LineProvider {

    /**
	 * @return Returns the type of this entry
	 */
    public PkgMapEntryType getType();

    /**
	 * @param obj
	 * @param ignoreLastModified whether or not the "lastModified" field should be ignored when checking for equality
	 * @return
	 */
    public boolean equals(Object obj, boolean ignoreLastModified);
}
