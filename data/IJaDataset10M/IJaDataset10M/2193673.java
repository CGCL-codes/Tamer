package genomancer.ivy.das.client.modelimpl;

import genomancer.ivy.das.model.Das1FeatureI;
import genomancer.ivy.das.model.Das1GroupI;
import genomancer.ivy.das.model.Das1LinkI;
import genomancer.ivy.das.model.Das1MethodI;
import genomancer.ivy.das.model.Das1SegmentI;
import genomancer.ivy.das.model.Das1TargetI;
import genomancer.ivy.das.model.Das1TypeI;
import genomancer.ivy.das.model.Das1Phase;
import genomancer.trellis.das2.model.Strand;
import java.util.ArrayList;
import java.util.List;

public class Das1Feature implements Das1FeatureI {

    protected String id;

    protected String label;

    protected Das1TypeI type;

    protected Das1SegmentI segment;

    protected int start;

    protected int end;

    protected Strand orientation;

    protected double score;

    protected Das1Phase phase;

    protected Das1MethodI method;

    List<String> notes = null;

    List<Das1LinkI> links = null;

    List<Das1TargetI> targets = null;

    List<Das1GroupI> groups = null;

    public Das1Feature(String id, String label, Das1TypeI type, Das1MethodI method, Das1SegmentI segment, int start, int end, double score, Strand orientation, Das1Phase phase, List<String> notes, List<Das1LinkI> links, List<Das1TargetI> targets, List<Das1GroupI> groups) {
        this.id = id;
        this.label = label;
        this.type = type;
        this.method = method;
        this.segment = segment;
        this.start = start;
        this.end = end;
        this.score = score;
        this.orientation = orientation;
        this.phase = phase;
        this.notes = notes;
        this.links = links;
        this.targets = targets;
        this.groups = groups;
    }

    public void addNote(String note) {
        if (notes == null) {
            notes = new ArrayList<String>();
        }
        notes.add(note);
    }

    public void addLink(Das1LinkI link) {
        if (links == null) {
            links = new ArrayList<Das1LinkI>();
        }
        links.add(link);
    }

    public void addTarget(Das1TargetI target) {
        if (targets == null) {
            targets = new ArrayList<Das1TargetI>();
        }
        targets.add(target);
    }

    public void addGroup(Das1GroupI group) {
        if (groups == null) {
            groups = new ArrayList<Das1GroupI>();
        }
        groups.add(group);
    }

    public String getID() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public Das1TypeI getType() {
        return type;
    }

    public Das1SegmentI getSegment() {
        return segment;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public Strand getOrientation() {
        return orientation;
    }

    public double getScore() {
        return score;
    }

    /**
     *   if feature has no score the score field will be set to Double.MIN_VALUE
     */
    public boolean hasScore() {
        return (score != NO_SCORE);
    }

    public Das1Phase getPhase() {
        return phase;
    }

    public Das1MethodI getMethod() {
        return method;
    }

    public List<Das1GroupI> getGroups() {
        return groups;
    }

    public List<Das1TargetI> getTargets() {
        return targets;
    }

    public List<Das1LinkI> getLinks() {
        return links;
    }

    public List<String> getNotes() {
        return notes;
    }
}
