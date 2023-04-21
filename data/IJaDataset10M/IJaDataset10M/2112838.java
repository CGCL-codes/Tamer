package pogvue.datamodel;

import java.util.Hashtable;

public final class Exon extends SequenceFeature {

    private String phase;

    private int coding_start = -1;

    private int coding_end = -1;

    private final Hashtable hphase;

    public Exon(Sequence sequence, String type, int start, int end, String description, String phase) {
        super(sequence, type, start, end, description);
        this.phase = phase;
        hphase = new Hashtable();
    }

    public String toGFFString() {
        String gff = id + "\t" + type + "\tfeature\t" + start + "\t" + end + "\t" + score + "\t" + strand + "\t" + phase + "\t";
        if (hit != null) {
            gff = gff + "\t" + hit.getId();
            if (coding_start != -1) {
                gff = gff + "\tcoding_start=" + coding_start;
            }
            if (coding_end != -1) {
                gff = gff + "\tcoding_end=" + coding_end;
            }
        }
        return gff;
    }

    public void setCodingStart(int coding_start) {
        this.coding_start = coding_start;
    }

    public void setCodingEnd(int coding_end) {
        this.coding_end = coding_end;
    }

    public int getCodingStart() {
        return coding_start;
    }

    public int getCodingEnd() {
        return coding_end;
    }

    public String getPhase() {
        return phase;
    }

    public void setPhase(String phase) {
        this.phase = phase;
    }

    public int getHPhase(String org) {
        if (hphase.containsKey(org)) {
            return (Integer) hphase.get(org);
        } else {
            return -1;
        }
    }

    public void setHPhase(String org, int phase) {
        hphase.put(org, phase);
    }
}
