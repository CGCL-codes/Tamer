package com.oat.explorer.domains.cfo.gui.entry;

import com.oat.Domain;
import com.oat.domains.cfo.CFODomain;
import com.oat.explorer.gui.entry.ExplorerApplet;

/**
 * Type: FuncOptMainFrame<br/>
 * Date: 24/11/2006<br/>
 * <br/>
 * Description:
 * <br/>
 * @author Jason Brownlee
 * 
 * <pre>
 * Change History
 * ----------------------------------------------------------------------------
 * 
 * </pre>
 */
public class CFOMainApplet extends ExplorerApplet {

    @Override
    protected Domain[] getDomains() {
        return new Domain[] { new CFODomain() };
    }
}
