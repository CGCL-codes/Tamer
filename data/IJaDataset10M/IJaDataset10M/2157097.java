package org.wings.externalizer;

import org.wings.session.Session;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision: 6 $
 */
class ExternalizedInfo {

    private static long counter = 0;

    long timestamp;

    boolean stable;

    String extFileName;

    Object extObject;

    ObjectHandler handler;

    Session session;

    public ExternalizedInfo(Object obj, ObjectHandler hdl, Session ses) {
        extObject = obj;
        handler = hdl;
        session = ses;
        stable = handler.isStable(extObject);
        extFileName = generateFileName() + handler.getExtension(extObject);
        touch();
    }

    /**
     * TODO: documentation
     *
     */
    public void touch() {
        if (!stable) timestamp = System.currentTimeMillis();
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public String toString() {
        return extFileName + "[" + timestamp + "," + (stable ? "stable" : "transient") + "]";
    }

    /**
     * TODO: documentation
     */
    protected static final String generateFileName() {
        long maxUniqLifespan = 30 * 24 * 3600;
        long uniqPrefix = (System.currentTimeMillis() / 1000) % maxUniqLifespan;
        return Long.toString(uniqPrefix, Character.MAX_RADIX) + "_" + (counter++ % 100);
    }
}
