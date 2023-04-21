package org.jeromedl.beans;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.jeromedl.pool.JeromePool;
import org.jeromedl.pool.XppPoolableFactory;
import org.xmlpull.v1.XmlPullParser;

/**
 * Interface class for xpp xml parser objects stored in an object pool 
 * @author     Sebastian Kruk, Adam Westerski
 */
public class Xpp {

    private static Logger logger = Logger.getLogger("org.jeromedl.beans");

    /**
    * returns a parser object from the pool 
    * @return XmlPullParser instance taken from object pool
    */
    public static final XmlPullParser borrowParser() {
        XmlPullParser xpp = null;
        try {
            JeromePool kep = JeromePool.getInstance(XppPoolableFactory.S_POOL_NAME);
            synchronized (kep) {
                xpp = (XmlPullParser) kep.borrowObject();
            }
        } catch (Exception ex) {
            logger.log(Level.FINE, "Pooling problems in XPP", ex);
        }
        return xpp;
    }

    /**
    * returns the parser object to pool
    * @param  xpp  XmlPullParser object that is supposed to be put back into the object pool
    */
    public static final void returnParser(XmlPullParser xpp) {
        try {
            JeromePool kep = JeromePool.getInstance(XppPoolableFactory.S_POOL_NAME);
            synchronized (kep) {
                kep.returnObject(xpp);
            }
        } catch (Exception ex) {
            logger.log(Level.FINE, "Pooling problems in XPP", ex);
        }
    }
}
