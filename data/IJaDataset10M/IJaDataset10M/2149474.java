package com.jplt.cobosoda;

public class JointAdditionMutation extends NullMutation {

    public static final String VERSION = "$Revision: 1.4 $";

    public static final String ID = "$Id: JointAdditionMutation.java,v 1.4 2008/05/24 21:02:50 potatono Exp $";

    private double maxOffset = 1;

    public JointAdditionMutation() {
        super();
    }

    public JointAdditionMutation(double maxOffset) {
        super();
        this.maxOffset = maxOffset;
    }

    public double getMaxOffset() {
        return maxOffset;
    }

    public void setMaxOffset(double maxOffset) {
        this.maxOffset = maxOffset;
    }

    public Model mutate(Model m) {
        int from = (int) Math.floor(Math.random() * m.getNumberOfJoints());
        Joint j = m.getJoint(from);
        m.spawnJoint(randomlyShift(j.getX(), maxOffset), randomlyShift(j.getY(), maxOffset), false);
        int to = m.getNumberOfJoints() - 1;
        try {
            m.spawnSpring(from, to, m.getMechanics().getSpringK());
        } catch (Exception e) {
        }
        if (Math.random() < 0.5) {
            int from2 = (int) Math.floor(Math.random() * m.getNumberOfJoints());
            if (from2 != from && from2 != to) {
                try {
                    m.spawnSpring(from2, to, m.getMechanics().getSpringK());
                } catch (Exception e) {
                }
            }
        }
        return m;
    }

    private double randomlyShift(double n, double m) {
        return n + (Math.random() * m) * (Math.random() > 0.5 ? -1 : 1);
    }
}
