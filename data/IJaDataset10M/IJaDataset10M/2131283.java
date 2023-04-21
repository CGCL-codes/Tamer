package org.oclc.da.ndiipp.rundef;

import org.oclc.da.common.pvt.RefTypes;
import org.oclc.da.ndiipp.common.guid.GUIDRef;

/**
 * This class faciliates identification of Analysis.
 * 
 * @author MJT
 * 
 * Created 6/30/2005
 */
public class RunDefRef extends GUIDRef {

    /**
     * Public constructor.
     */
    public RunDefRef() {
        super();
    }

    /**
     * Public constructor.
     * 
     * @param guid the guid
     */
    public RunDefRef(String guid) {
        super(guid);
    }

    /**
     * @see org.oclc.da.common.Ref#getRefType()
     */
    public String getRefType() {
        return RefTypes.RUN_DEF;
    }
}
