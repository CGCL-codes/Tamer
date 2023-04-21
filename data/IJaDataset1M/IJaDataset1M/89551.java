package com.bbn.vessel.core.runtime.report;

import com.bbn.vessel.core.runtime.condition.IsWhile;
import com.bbn.vessel.core.runtime.trigger.Trigger;

/**
 * Report if the "param" trigger occurs while this assessor was enabled.
 * <p>
 * There will be a report for every time the assessor is enabled.
 */
public final class TriggerAssessor extends IsWhile {

    private String in_tr_param;

    private String out_rpt;

    private boolean fired = false;

    @Override
    protected void configure() {
        super.configure();
        in_tr_param = args.assertGetString("param.in_tr");
        out_rpt = args.assertGetString("report.out_rpt");
    }

    @Override
    protected void onStart() {
        fired = false;
    }

    @Override
    protected void onOther(Object o, boolean isDuring) {
        if (!isDuring) {
            return;
        }
        if (!(o instanceof Trigger)) {
            return;
        }
        Trigger t = (Trigger) o;
        if (!in_tr_param.equals(t.getName())) {
            return;
        }
        fired = true;
    }

    @Override
    protected void onStop(boolean isAbort) {
        Report r = new Report(out_rpt, (name + " " + (isAbort ? "abort" : "end") + " " + (fired ? "saw" : "no") + " " + in_tr_param));
        if (logger.isInfoEnabled()) {
            logger.info("publishing report: " + r);
        }
        publish(r);
    }
}
