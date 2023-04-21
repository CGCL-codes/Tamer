package org.sf.net.sopf.cache;

/**
 * @author admin
 *
 */
public interface ISopCache {

    public Object getObj(long objId);

    public Deltable getDeltable(long objId);

    public IData getData(long objId);

    public long addData(IData data);

    public void remove(IData data);
}
