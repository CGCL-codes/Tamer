package es.eucm.eadventure.common.data.chapter.effects;

import es.eucm.eadventure.common.data.HasTargetId;

/**
 * An effect that activates a flag
 */
public class ActivateEffect extends AbstractEffect implements HasTargetId {

    /**
     * Name of the flag to be activated
     */
    private String idFlag;

    /**
     * Creates a new Activate effect.
     * 
     * @param idFlag
     *            the id of the flag to be activated
     */
    public ActivateEffect(String idFlag) {
        super();
        this.idFlag = idFlag;
    }

    @Override
    public int getType() {
        return ACTIVATE;
    }

    /**
     * Returns the idFlag
     * 
     * @return String containing the idFlag
     */
    public String getTargetId() {
        return idFlag;
    }

    /**
     * Sets the new idFlag
     * 
     * @param idFlag
     *            New idFlag
     */
    public void setTargetId(String idFlag) {
        this.idFlag = idFlag;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        ActivateEffect ae = (ActivateEffect) super.clone();
        ae.idFlag = (idFlag != null ? new String(idFlag) : null);
        return ae;
    }
}
