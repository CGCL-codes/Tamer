package cn.com.believer.songyuanframework.openapi.storage.xdrive.object.functional.contact;

import java.util.ArrayList;
import java.util.List;
import cn.com.believer.songyuanframework.openapi.storage.xdrive.object.functional.BaseOutput;

/**
 * @author Jimmy
 * 
 */
public class ContactGetAolListingOutput extends BaseOutput {

    /**
     * ContactObject list.
     */
    private List contracts;

    /**
     * 
     */
    public ContactGetAolListingOutput() {
        contracts = new ArrayList();
    }

    /**
     * @return the ContactObject list
     */
    public List getContracts() {
        return this.contracts;
    }

    /**
     * @param contracts
     *            the contracts to set
     */
    public void setContracts(List contracts) {
        this.contracts = contracts;
    }
}
