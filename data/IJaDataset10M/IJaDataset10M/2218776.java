package com.ewansilver.raindrop;

/**
 * Calculates the adjustment required to the QueueLength of a Stage based on the
 * observed throughput.
 * 
 * See Welsh page 148.
 * 
 * @author ewan.silver AT gmail.com
 */
public class OverloadQueueLengthAdjustmentStrategy {

    private long target;

    private double errorDecrease, errorIncrease;

    private double adjIncrease, adjDecrease;

    private double constant;

    private QueueLengthAdmissionsController controller;

    /**
	 * 
	 */
    public OverloadQueueLengthAdjustmentStrategy(long aTarget, QueueLengthAdmissionsController aController) {
        target = aTarget;
        controller = aController;
        errorDecrease = 0.0;
        errorIncrease = -0.005;
        adjIncrease = 2.0;
        adjDecrease = 1.2;
        constant = -0.1;
    }

    public void updateThroughputRate(long aRate) {
        System.out.println("rate: " + aRate);
        System.out.println("target: " + target);
        long top = aRate - target;
        System.out.println("top: " + top);
        double error = (double) (aRate - target) / target;
        System.out.println("error: " + error);
        if (error > errorDecrease) handleDecrease(); else if (error < errorIncrease) handleIncrease(error);
    }

    /**
	 * Should handleIncrease and handleDecrease by Strategies???
	 * 
	 */
    private void handleIncrease(double anError) {
        int maxLength = controller.getMaximumLength();
        int adjustment = (int) (adjIncrease * -(anError - constant));
        controller.setMaximumLength(adjustment + maxLength);
    }

    /**
	 * Decrease the maximumLength by the adjDecrease multiplicativeFactor;
	 * 
	 */
    private void handleDecrease() {
        int maxLength = controller.getMaximumLength();
        controller.setMaximumLength((int) (maxLength / adjDecrease));
    }
}
