package pulsarhunter.datatypes;

import java.util.ArrayList;

/**
 *
 * @author mkeith
 */
public class PulsarPolyco {

    private String psrName;

    private double dm;

    private PolyCoBlock targetBlock = null;

    public ArrayList<PolyCoBlock> blocks = new ArrayList<PolyCoBlock>();

    /** Creates a new instance of PulsarPolyco */
    public PulsarPolyco(String name, double dm) {
        this.setPsrName(name);
        this.setDm(dm);
    }

    public double getPhaseAt(double targetMjd) {
        PolyCoBlock pcb = getTarget(targetMjd);
        if (pcb != null) return pcb.getPhaseAt(targetMjd); else return -1;
    }

    public double getPeriodAt(double targetMjd) {
        PolyCoBlock pcb = getTarget(targetMjd);
        if (pcb != null) return pcb.getPeriodAt(targetMjd); else return -1;
    }

    public PolyCoBlock getTarget(double targetMjd) {
        double dtmin = (targetMjd - targetBlock.getMjdmid()) * 1440.0;
        if (Math.abs(dtmin) <= targetBlock.getBlockLength() / 2.) {
            return targetBlock;
        } else {
            for (PolyCoBlock block : blocks) {
                dtmin = (targetMjd - block.getMjdmid()) * 1440.0;
                if (Math.abs(dtmin) <= block.getBlockLength() / 2.) {
                    targetBlock = block;
                    return block;
                }
            }
        }
        return null;
    }

    public String getPsrName() {
        return psrName;
    }

    public void setPsrName(String psrName) {
        this.psrName = psrName;
    }

    public double getDm() {
        return dm;
    }

    public void setDm(double dm) {
        this.dm = dm;
    }

    public void addBlock(PolyCoBlock pcb) {
        this.blocks.add(pcb);
        this.targetBlock = pcb;
    }
}
