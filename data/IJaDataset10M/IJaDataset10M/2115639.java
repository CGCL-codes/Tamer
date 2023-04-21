package objects.alignments;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import objects.Subset;
import objects.ValidPosition;

public abstract class SingleAlignment implements Subset0Alignment {

    private Subset parent;

    private int start;

    private int count;

    private String cigar;

    private List<ValidPosition> bounds;

    private ValidPosition maxBound;

    public SingleAlignment() {
        initialize(0, 0, null, "");
    }

    public SingleAlignment(int start, int count, Subset parent, String cigar) {
        initialize(start, count, parent, cigar);
        createBounds();
    }

    private void initialize(int start, int count, Subset parent, String cigar) {
        this.parent = parent;
        this.start = start;
        this.count = count;
        this.cigar = cigar;
        this.bounds = new ArrayList<ValidPosition>();
        this.maxBound = null;
    }

    protected void createBounds() {
        String cigarTemp = cigar;
        for (int offset = 0; !cigarTemp.isEmpty(); ) {
            int m = cigarTemp.indexOf("M");
            int n = cigarTemp.indexOf("N");
            if (m < 0 && n < 0) return;
            int places = 0;
            if (m > 0 && (m < n || n < 0)) {
                places = Integer.parseInt(cigarTemp.substring(0, m));
                ValidPosition temp = new ValidPosition(start + offset, start + offset + places);
                bounds.add(temp);
                updateMaxBound(temp);
                cigarTemp = cigarTemp.substring(m + 1);
            } else {
                places = Integer.parseInt(cigarTemp.substring(0, n));
                cigarTemp = cigarTemp.substring(n + 1);
            }
            offset += places;
        }
    }

    private void updateMaxBound(ValidPosition bound) {
        if (maxBound != null) {
            maxBound.start = (maxBound.start > bound.start) ? bound.start : maxBound.start;
            maxBound.end = (maxBound.end > bound.end) ? maxBound.end : bound.end;
        } else maxBound = new ValidPosition(bound.start, bound.end);
    }

    @Override
    public List<ValidPosition> getValidPositions() {
        return bounds;
    }

    @Override
    public int getAlignmentSize() {
        return bounds.size();
    }

    @Override
    public ValidPosition getMaxBound() {
        return maxBound;
    }

    @Override
    public void setCigarString(String cigar) {
        this.cigar = cigar;
    }

    @Override
    public String getCigarString() {
        return cigar;
    }

    @Override
    public void setParent(Subset parent) {
        this.parent = parent;
    }

    @Override
    public Subset getParent() {
        return parent;
    }

    @Override
    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public void setStart(int start) {
        this.start = start;
    }

    @Override
    public int getStart() {
        return start;
    }

    @Override
    public abstract void draw(Graphics graphic, int x, int y);

    public static CollapsedAlignment collapse(Subset0Alignment one, Subset0Alignment two) {
        if (one instanceof IntronAlignment && two instanceof IntronAlignment) return new IntronCollapsedAlignment((IntronAlignment) one, (IntronAlignment) two);
        if (one instanceof SpliceAlignment && two instanceof SpliceAlignment) return new SpliceCollapsedAlignment((SpliceAlignment) one, (SpliceAlignment) two);
        return null;
    }
}
