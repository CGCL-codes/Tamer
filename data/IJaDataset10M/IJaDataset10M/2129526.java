package com.ail.insurance.policy;

import com.ail.core.Type;

/**
 * An insured thing, or a thing about which information is collected and upon which risk (and other factors) are assessed.
 * Generally an Asset represents a thing which is insured, for example a building in a household policy, or a driver
 * in a motor policy.
 * @version $Revision: 1.1 $
 * @state $State: Exp $
 * @date $Date: 2005/08/19 20:20:58 $
 * @source $Source: /home/bob/CVSRepository/projects/insurance/insurance.ear/insurance.jar/com/ail/insurance/policy/Asset.java,v $
 * @stereotype type
 */
public class Asset extends Type {

    static final long serialVersionUID = 7326823306523810654L;

    private String id;

    private String assetTypeId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAssetTypeId() {
        return assetTypeId;
    }

    public void setAssetTypeId(String assetTypeId) {
        this.assetTypeId = assetTypeId;
    }
}
