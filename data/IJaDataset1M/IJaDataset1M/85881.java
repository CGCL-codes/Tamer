package gov.sns.xal.smf;

import java.util.*;

public final class AcceleratorSector extends AcceleratorSeq {

    /** List of legal predecessors (AcceleratorSectors) to this AcceleratorSector */
    private List<AcceleratorSector> m_lstPred;

    /** Charge of beam in this sequence (+-1) */
    protected double m_dblBeamCharge;

    /** particle species charge to mass ratio */
    protected double m_dblQ2M;

    /** particle species rest energy */
    protected double m_dblEr;

    /** Return the signum of design particle species charge */
    public double getChargeSignum() {
        return m_dblBeamCharge;
    }

    ;

    /** Return the charge to mass ratio of the design particle species */
    public double getCharge2Mass() {
        return m_dblQ2M;
    }

    ;

    /** Return the rest energy of the design particle species */
    public double getRestEnergy() {
        return m_dblEr;
    }

    ;

    /** Set the charge to mass ratio of the design particle */
    public void setCharge2Mass(double dblQ2M) {
        m_dblQ2M = dblQ2M;
    }

    ;

    /** Set the charge sign of the design particle @param dblSgn {-1,+1} */
    public void setChargeSignum(double dblSgn) {
        m_dblBeamCharge = dblSgn;
    }

    ;

    /** Set the rest energy of the design particle species */
    public void setRestEnergy(double dblEr) {
        m_dblEr = dblEr;
    }

    ;

    /** Creates a new instance of AcceleratorSector */
    public AcceleratorSector(String strId) {
        super(strId);
    }

    /** Creates a new instance of AcceleratorSector */
    public AcceleratorSector(String strId, int intReserve) {
        super(strId, intReserve);
    }

    /** Adds node to the Sector at the tail.  Sector become the owner of the node.
     *                  CKA 08.02
     *  @param  node    node to be appended to Sector
     *  @return         true if successfully add, false if node already is owned by Sector
     *
     */
    @Override
    public boolean addNode(AcceleratorNode node) {
        if (!super.addNode(node)) return false;
        return true;
    }

    public AcceleratorSeq concatenate(AcceleratorSector sec) {
        boolean bolTest = false;
        Iterator<AcceleratorSector> iterPred = m_lstPred.iterator();
        while (iterPred.hasNext()) {
            AcceleratorSector secValid = iterPred.next();
            if (secValid.equals(sec)) bolTest = true;
        }
        String strId;
        AcceleratorSeq seqNew;
        strId = this.getId() + ":" + sec.getId();
        seqNew = new AcceleratorSeq(strId);
        return seqNew;
    }
}
