package kursach2;

import java.awt.Shape;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import kursach2.barriers.AArchiBot;
import kursach2.barriers.ABarier;
import kursach2.barriers.IArchitecture;
import kursach2.barriers.IBarier;
import kursach2.barriers.IPurposeful;

/**
 * @author Vsevolod
 *
 */
public class TFlow implements IPurposeful {

    private List<AArchiBot> humans = new LinkedList<AArchiBot>();

    private Map<Integer, AArchiBot> maphum = new HashMap<Integer, AArchiBot>();

    private byte target;

    private TPassage TargetPass;

    private TPremises MyPrem;

    private TExit Goal;

    public TFlow(TPremises M, byte t) {
        MyPrem = M;
        setTarget(t);
    }

    public List<AArchiBot> getHumans() {
        return humans;
    }

    public int takeStep(double dt) {
        int r = 0;
        for (IBarier n : humans) {
            r += n.takeStep(dt);
        }
        return r;
    }

    public void setTarget(int t) {
        target = (byte) t;
        TargetPass = MyPrem.getPassage(t);
        for (IArchitecture n : humans) {
            n.setTarget(t);
        }
    }

    public byte getNumberTarget() {
        return target;
    }

    @Override
    public TExit getGoal() {
        return Goal;
    }

    @Override
    public TPremises getMyPrem() {
        return MyPrem;
    }

    @Override
    public TPassage getTargetPass() {
        return TargetPass;
    }

    @Override
    public void setGoal(TExit goal) {
        Goal = goal;
        for (IArchitecture n : humans) {
            n.setGoal(goal);
        }
    }

    @Override
    public void setMyPrem(TPremises pr) {
        System.out.println("Flow.setMyPrem():Call of this method is incorrect for this class");
    }

    @Override
    public ABarier getTarget() {
        return TargetPass;
    }

    public int count() {
        return maphum.size();
    }

    public AArchiBot remove(int Number) {
        AArchiBot i = maphum.remove(Number);
        humans.remove(i);
        return i;
    }

    public AArchiBot remove(THuman h) {
        return remove(h.getNumber());
    }

    public AArchiBot show(int Number) {
        return maphum.get(Number);
    }

    public void add(AArchiBot h) {
        humans.add(h);
        maphum.put(h.getNumber(), h);
    }

    public List<AArchiBot> merge(TFlow plus) {
        if ((plus.getMyPrem() != this.getMyPrem()) && (plus.getTarget() != this.getTarget())) {
            return null;
        }
        List<AArchiBot> l = new LinkedList<AArchiBot>();
        for (AArchiBot h : plus.humans) {
            if (!maphum.containsKey(h.getNumber())) {
                humans.add(h);
                maphum.put(h.getNumber(), h);
                l.add(h);
            }
        }
        return l;
    }
}
