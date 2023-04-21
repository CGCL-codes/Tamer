package wsl.mdn.common;

import wsl.fw.security.Feature;
import wsl.fw.security.FwFeatures;
import java.util.Vector;
import wsl.fw.resource.ResId;
import wsl.fw.resource.ResourceManager;

/**
 * Class to hold constants defining the Features used by security for
 * wsl.mdn and sub-packages.
 */
public class MdnFeatures extends FwFeatures {

    private static final String _ident = "$Date: 2002/06/11 23:35:35 $  $Revision: 1.1.1.1 $ " + "$Archive: /Mobile Data Now/Source/wsl/mdn/common/MdnFeatures.java $ ";

    /**
     * Default constructor.
     */
    public MdnFeatures() {
    }

    /**
     * Override to get the set of features for use by saveFeatureSet.
     * @return a vector of all the features that are to be saved.
     */
    protected Vector getFeatureSet() {
        Vector v = super.getFeatureSet();
        return v;
    }
}
