package org.oclc.da.ndiipp.domain;

import org.oclc.da.common.pvt.RefTypes;
import org.oclc.da.ndiipp.common.guid.GUIDRef;

/** This class faciliates identification of EntryPoints 
 *
 * @author MJT
 * 
 * Created 12/29/2004
 */
public class EntryPointRef extends GUIDRef {

    /**
     * Public constructor.
     * 
     */
    public EntryPointRef() {
        super();
    }

    /**
     * Public constructor.
     * @param guid supplied guid
     * 
     */
    public EntryPointRef(String guid) {
        super(guid);
    }

    /**
     * @see org.oclc.da.common.Ref#getRefType()
     */
    public String getRefType() {
        return RefTypes.ENTRY_POINT;
    }
}
