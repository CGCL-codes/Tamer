package uk.org.ogsadai.activity.pipeline;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import uk.org.ogsadai.activity.Activity;
import uk.org.ogsadai.activity.ActivityInstanceName;
import uk.org.ogsadai.activity.pipeline.ActivityIOVisitor;
import uk.org.ogsadai.activity.pipeline.ActivityUtilities;
import uk.org.ogsadai.util.UniqueName;

/**
 * A simple activity pipleine implementation.
 * 
 * @author The OGSA-DAI Project Team
 */
public class SimpleActivityPipeline implements ActivityPipeline {

    /** Copyright statement. */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh 2007.";

    /** The activities contained in the pipeline. */
    private final Set mActivities = new HashSet();

    /** Parent activity (if there is one) */
    private Activity mParentActivity;

    /**
     * Creates a new and initially empty activity pipeline.
     */
    public SimpleActivityPipeline() {
    }

    /**
     * Creates a new activity pipeline containing the specified activity
     * descriptors.
     * 
     * @param activities
     *            a set of activity descriptors
     */
    public SimpleActivityPipeline(final Set activities) {
        if (activities == null) {
            throw new IllegalArgumentException("The activities argument must not be null.");
        }
        mActivities.addAll(activities);
    }

    public Set getActivities() {
        return new HashSet(mActivities);
    }

    public void connect(final ActivityDescriptor sourceActivity, final String outputName, final ActivityDescriptor targetActivity, final String inputName) {
        if (sourceActivity == null) {
            throw new IllegalArgumentException("The sourceActivity argument must not be null.");
        }
        if (outputName == null) {
            throw new IllegalArgumentException("The outputName argument must not be null.");
        }
        if (targetActivity == null) {
            throw new IllegalArgumentException("The targetActivity argument must not be null.");
        }
        if (inputName == null) {
            throw new IllegalArgumentException("The inputName argument must not be null.");
        }
        addActivity(sourceActivity);
        addActivity(targetActivity);
        final String pipe = UniqueName.newName();
        sourceActivity.addOutput(new ActivityOutputStream(outputName, pipe));
        targetActivity.addInput(new ActivityInputStream(inputName, pipe));
    }

    public void addActivity(final ActivityDescriptor descriptor) {
        if (!mActivities.contains(descriptor)) {
            mActivities.add(descriptor);
        }
    }

    /**
     * Gets a string describing all the activities in the activity graph.
     * 
     * @return descriptive string
     */
    public String toString() {
        final StringBuffer s = new StringBuffer();
        for (Iterator i = mActivities.iterator(); i.hasNext(); ) {
            ActivityDescriptor activity = (ActivityDescriptor) i.next();
            s.append("Activity:\n");
            s.append("    activity name   = ").append(activity.getActivityName()).append("\n");
            s.append("    instance name   = ").append(activity.getInstanceName()).append("\n");
            if (activity.getTargetResource() != null) {
                s.append("    target resource = ").append(activity.getTargetResource()).append("\n");
            }
            s.append("    inputs:\n");
            for (Iterator j = activity.getInputs().iterator(); j.hasNext(); ) {
                ActivityInput input = (ActivityInput) j.next();
                s.append("        ").append(input.toString());
                s.append("\n");
            }
            s.append("    outputs:\n");
            for (Iterator j = activity.getOutputs().iterator(); j.hasNext(); ) {
                ActivityOutput output = (ActivityOutput) j.next();
                s.append("        ").append(output.toString());
                s.append("\n");
            }
            s.append("\n");
        }
        return s.toString();
    }

    public boolean containsActivityInstance(ActivityInstanceName instanceName) {
        boolean found = false;
        for (Iterator i = mActivities.iterator(); i.hasNext(); ) {
            ActivityDescriptor activity = (ActivityDescriptor) i.next();
            if (activity.getInstanceName().equals(instanceName)) {
                found = true;
                break;
            }
        }
        return found;
    }

    public ActivityDescriptor getActivityInstance(ActivityInstanceName instanceName) {
        ActivityDescriptor activity = null;
        for (Iterator i = mActivities.iterator(); i.hasNext(); ) {
            activity = (ActivityDescriptor) i.next();
            if (activity.getInstanceName().equals(instanceName)) {
                break;
            }
        }
        return activity;
    }

    public boolean containsPipe(final String pipeName) {
        final Collection found = new ArrayList();
        ActivityUtilities.visitAllInputsAndOutputs(this, new ActivityIOVisitor() {

            public void visitInputLiteral(ActivityInputLiteral input) {
            }

            public void visitInputStream(ActivityInputStream stream) {
                if (stream.getPipeName().equals(pipeName)) {
                    found.add(stream.getInputName());
                }
            }

            public void visitOutputStream(ActivityOutputStream stream) {
                if (stream.getPipeName().equals(pipeName)) {
                    found.add(stream.getOutputName());
                }
            }
        });
        return found.size() == 2;
    }

    public boolean containsActivity(ActivityDescriptor activity) {
        boolean found = false;
        for (Iterator i = mActivities.iterator(); i.hasNext(); ) {
            ActivityDescriptor a = (ActivityDescriptor) i.next();
            if (a.equals(activity)) {
                found = true;
                break;
            }
        }
        return found;
    }

    public void setParent(Activity parentActivity) {
        mParentActivity = parentActivity;
    }

    public Activity getParent() {
        return mParentActivity;
    }

    public boolean hasParent() {
        return (mParentActivity != null);
    }
}
