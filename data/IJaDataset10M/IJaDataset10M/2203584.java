package rcscene.validation.gui;

import rcscene.ActionWeights;

public class AWeightsMenu extends WeightsMenu {

    private static final long serialVersionUID = 1L;

    /** Constructor for objects of class SetUpTest */
    public AWeightsMenu(RoboTester test) {
        this(test, null);
    }

    public AWeightsMenu(RoboTester test, String[] data) {
        super("AWeights (Action Weights)", data);
        this.test = test;
    }

    protected void makeLabelList() {
        labelArrayList.add("None");
        labelArrayList.add("Dash");
        labelArrayList.add("Kick");
        labelArrayList.add("Turn Neck");
        labelArrayList.add("Turn");
        labelArrayList.add("Catch");
        labelArrayList.add("Move");
    }

    protected void setWeights() {
        float[] weights = new float[var.size()];
        for (int ii = 0; ii < var.size(); ii++) {
            weights[ii] = var.get(ii).floatValue();
        }
        test.setAWeights(new ActionWeights(weights));
        var.clear();
        super.exit();
    }
}
