package uk.gov.dti.og.fox;

import uk.gov.dti.og.fox.dom.DOM;
import uk.gov.dti.og.fox.dom.DOMList;
import uk.gov.dti.og.fox.ex.ExDoSyntax;
import uk.gov.dti.og.fox.ex.ExInternal;
import uk.gov.dti.og.fox.ex.ExModule;
import uk.gov.dti.og.fox.track.Track;

/**
 * 
 * A collection of UElems that contains no duplicate elements.
 * Map Set elements have the following main features:
 * 
 * Display Items - Usually textual values such as displayed in a html LOV pulldown
 * 
 * Key Items - The key values corresponding to the Display Items. Note a display item meya have a composite key of several key items.
 * 
 * map sets are cached in the mapSet cache dom various options are available as to the level of cache
 * 
 */
class MapSetDfn extends Track implements FoxModuleComponent {

    public final String mName;

    public final String mFullName;

    private StoreLocation mStoreLocation;

    private XDo mDo;

    public final long mRefreshTimeoutMillis;

    private boolean mBackgroundRefresh;

    /**
   * gets a map set store location definition 
   * 
   * @return map set store location
   */
    StoreLocation getStoreLocation() throws ExModule {
        if (mStoreLocation == null) {
            throw new ExModule("no store location defined for map set " + mName);
        }
        return mStoreLocation;
    }

    /**
   * gets the mandatory XDo definition 
   * 
   * @return XDo construct
   */
    XDo getXDo() {
        if (mDo == null) {
            throw new ExInternal("no Xdo block defined for map set " + mName);
        }
        return mDo;
    }

    /**
   * Returns true if the map set should be uploaded on application startup
   * note: this is true if the tag mBackgroundRefresh is True and 
   *       mRefreshTimeout = 0. (See Jason for the reasons)
   *
   *      This method just makes the calling code simpler and reduces confusion
   * 
   */
    boolean loadOnStartup() {
        if (mBackgroundRefresh && mRefreshTimeoutMillis == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
   * Parses and map set definition based upon a xml definition
   */
    MapSetDfn(DOM pMapSetDfn, Mod pMod) throws ExInternal, ExModule, ExDoSyntax {
        boolean backgroundRefreshTagSupplied = false;
        DOMList mapSetTags = pMapSetDfn.getChildElements();
        mName = pMapSetDfn.getAttr("name");
        mFullName = pMod.getName() + '/' + mName;
        DOM msElem;
        try {
            if (XFUtil.isNull(mName)) {
                throw new ExModule("map-set name required ", pMapSetDfn);
            }
            long lRefreshTimeoutMillis = -1;
            for (int i = 0; i < mapSetTags.getLength(); i++) {
                msElem = mapSetTags.item(i);
                String tagName = msElem.getLocalName();
                if (tagName.compareToIgnoreCase("storage-location") == 0) {
                    mStoreLocation = pMod.getStoreLocation(msElem.value());
                } else if (tagName.compareToIgnoreCase("do") == 0) {
                    mDo = new XDo(pMod, msElem);
                } else if (tagName.compareToIgnoreCase("refresh-timeout-mins") == 0) {
                    lRefreshTimeoutMillis = Long.parseLong(msElem.value()) * 60000;
                } else if (tagName.compareToIgnoreCase("refresh-in-background") == 0) {
                    mBackgroundRefresh = Boolean.valueOf(msElem.value()).booleanValue();
                    backgroundRefreshTagSupplied = true;
                }
            }
            mRefreshTimeoutMillis = lRefreshTimeoutMillis;
            if (!backgroundRefreshTagSupplied) {
                throw new ExModule("Mandatory refresh-in-background tag must be defined for map-set " + mName, pMapSetDfn);
            }
            if (mRefreshTimeoutMillis == -1) {
                throw new ExModule("Mandatory refresh-timeout-mins tag must be defined for map-set " + mName, pMapSetDfn);
            }
            if (mStoreLocation == null) {
                throw new ExModule("Mandatory storage-location tag must be defined for map-set " + mName, pMapSetDfn);
            }
            if (mDo == null) {
                throw new ExModule("Mandatory do tag must be defined for map-set " + mName, pMapSetDfn);
            }
        } catch (ExModule ex) {
            throw new ExModule("Can not parse map set " + mName, pMapSetDfn, ex);
        }
    }

    /**
   * Validates that the map-set, and its sub-components, are valid.
   *
   * @param module the module where the component resides
   * @throws ExInternal if the component syntax is invalid.
   */
    public void validate(Mod module) throws ExInternal {
        if (mDo != null) mDo.validate(module);
    }
}
