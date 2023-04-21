package fr.loria.ecoo.wootEngine.op;

import fr.loria.ecoo.wootEngine.core.WootId;
import fr.loria.ecoo.wootEngine.core.WootPage;
import java.io.Serializable;

/**
 * DOCUMENT ME!
 *
 * @author molli
 */
public abstract class WootOp implements Serializable {

    private WootId opid;

    private String pageName;

    /**
     * DOCUMENT ME!
     *
     * @param page DOCUMENT ME!
     */
    public abstract void execute(WootPage page);

    /**
     * DOCUMENT ME!
     *
     * @param page DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract boolean precond(WootPage page);

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public WootId getOpid() {
        return this.opid;
    }

    /**
     * DOCUMENT ME!
     *
     * @param opid DOCUMENT ME!
     */
    public void setOpid(WootId opid) {
        this.opid = opid;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        String s = " siteId: " + this.opid.getSiteid() + " opid: " + this.opid;
        return s;
    }

    /**
     * DOCUMENT ME!
     *
     * @param o DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object o) {
        if (!(o instanceof WootOp)) {
            return false;
        }
        return this.getOpid().equals(((WootOp) o).getOpid());
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getPageName() {
        return this.pageName;
    }

    /**
     * DOCUMENT ME!
     *
     * @param pageName DOCUMENT ME!
     */
    public void setPageName(String pageName) {
        this.pageName = pageName;
    }
}
