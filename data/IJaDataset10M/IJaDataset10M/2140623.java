package org.eclipse.examples.helloworld;

import java.util.Iterator;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ProgressBar;
import de.fraunhofer.fokus.metrics.Group;
import de.fraunhofer.fokus.metrics.MetricException;
import de.fraunhofer.fokus.metrics.OCLMetric;
import de.fraunhofer.fokus.metrics.Rule;
import de.fraunhofer.fokus.metrics.utils.RunMetrics;

public class GenerateMetricsThread extends Thread {

    private double increment;

    private double step;

    protected Display display;

    private ProgressBar progressBar;

    private Rule rule;

    public GenerateMetricsThread(Display display, ProgressBar progressBar, Rule rule, double step) {
        this.rule = rule;
        this.display = display;
        this.progressBar = progressBar;
        this.step = step;
        this.increment = 0;
    }

    public void run() {
        runRule(rule, false);
    }

    public void runRule(Rule rule, boolean run) {
        for (Iterator iter = rule.getGroups().iterator(); iter.hasNext(); ) {
            Group vGroup = (Group) iter.next();
            if (vGroup.isRunnable()) evaluteGroup(vGroup, run);
        }
    }

    public void evaluteGroup(Group vGroup, boolean run) {
        if (!vGroup.getAllMetrics().isEmpty()) for (Iterator iter2 = vGroup.getAllMetrics().iterator(); iter2.hasNext(); ) {
            OCLMetric metric = (OCLMetric) iter2.next();
            if (!run) {
                try {
                    RunMetrics.evaluate(metric.toStringWithoutComment());
                } catch (MetricException me) {
                    System.out.println(me);
                }
            } else if (metric.isRunnable() && run) {
                try {
                    RunMetrics.evaluate("context " + metric.getContext() + " inv: self." + metric.getName());
                } catch (MetricException me) {
                    System.out.println(me);
                }
            }
            display.asyncExec(new Runnable() {

                public void run() {
                    synchronized (progressBar) {
                        if (progressBar.isDisposed()) return;
                        progressBar.setSelection(progressBar.getSelection() + (int) (Math.floor(step + increment)));
                        increment = step + increment - Math.floor(step + increment);
                    }
                }
            });
        }
        if (!vGroup.getGroups().isEmpty()) {
            for (Iterator it1 = vGroup.getGroups().iterator(); it1.hasNext(); ) {
                Group group = (Group) it1.next();
                if (group.isRunnable()) evaluteGroup(group, run);
            }
        }
    }

    public Rule getRule() {
        return rule;
    }

    public void setRule(Rule rule) {
        this.rule = rule;
    }
}
