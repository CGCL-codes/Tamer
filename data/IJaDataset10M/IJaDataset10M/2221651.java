package com.knowgate.dataxslt;

import java.util.HashMap;
import java.lang.ref.SoftReference;

public class MicrositeFactory {

    public static boolean bCache = true;

    public static HashMap oMicrosites = new HashMap();

    public MicrositeFactory() {
    }

    /**
   * @return Caching status on/off
   */
    public static boolean cache() {
        return bCache;
    }

    /**
   * Turns Microsite caching on/off
   * @param bCacheOnOf <b>true</b> if Microsite caching is to be activated,
   * <b>false</b> if Microsite caching is to be deactivated.
   */
    public static void cache(boolean bCacheOnOf) {
        bCache = bCacheOnOf;
        if (false == bCacheOnOf) oMicrosites.clear();
    }

    /**
   * Get a Microsite from an XML file
   * If Microsite is cached then cached instance is returned.
   * @param sURI XML file URI starting with file://
   * (for example file:///opt/knowgate/storage/xslt/templates/Comtemporary.xml)
   * @param bValidateXML <b>true</b> if XML validation with W3C schemas is to be done,
   * <b>false</b> is no validation is to be done.
   * @return Microsite instance
   * @throws ClassNotFoundException
   * @throws IllegalAccessException
   */
    public static synchronized Microsite getInstance(String sURI, boolean bValidateXML) throws ClassNotFoundException, Exception, IllegalAccessException {
        Microsite oRetObj;
        Object oRefObj;
        if (bCache) {
            oRefObj = oMicrosites.get(sURI);
            if (null == oRefObj) {
                oRetObj = new Microsite(sURI, bValidateXML);
                oMicrosites.put(sURI, new SoftReference(oRetObj));
            } else {
                oRetObj = (Microsite) ((SoftReference) oRefObj).get();
                if (null == oRetObj) oRetObj = new Microsite(sURI, bValidateXML);
            }
            return oRetObj;
        } else oRetObj = new Microsite(sURI, bValidateXML);
        return oRetObj;
    }

    /**
   * Get a Microsite from an XML file
   * If Microsite is cached then cached instance is returned.
   * XML validation is disabled.
   * @param sURI XML file path
   * @return Microsite instance
   * @throws ClassNotFoundException
   * @throws IllegalAccessException
   */
    public static Microsite getInstance(String sURI) throws ClassNotFoundException, Exception, IllegalAccessException {
        return getInstance(sURI, false);
    }
}
