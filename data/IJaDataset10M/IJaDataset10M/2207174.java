package bg.unisofia.fmi.kanban.client.model.impl;

import bg.unisofia.fmi.kanban.client.model.IPhase;

/**
 *
 * An implementation of project phase used in the GUI.
 *
 * @author nikolay.grozev
 */
public class PhaseClientWrapper implements IPhase {

    private Integer id;

    private String phaseName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String getPhaseName() {
        return phaseName;
    }

    /**
     * Sets the name of the phase.
     * @param phaseName - the name of the phase.
     */
    public void setPhaseName(String phaseName) {
        this.phaseName = phaseName;
    }
}
