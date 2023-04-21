package org.uguess.birt.report.engine.emitter.xls;

import java.util.Locale;
import org.eclipse.birt.report.engine.api.script.IReportContext;

/**
 * A special context object simulates the IReportContext interface but
 * eliminates all potential unsecured operations.
 */
public class ManagedReportContext {

    private IReportContext cxt;

    ManagedReportContext(IReportContext cxt) {
        this.cxt = cxt;
    }

    public Locale getLocale() {
        return cxt == null ? null : cxt.getLocale();
    }

    public Object getGlobalVariable(String name) {
        return cxt == null ? null : cxt.getGlobalVariable(name);
    }

    public Object getPageVariable(String name) {
        return cxt == null ? null : cxt.getPageVariable(name);
    }

    public Object getPersistentGlobalVariable(String name) {
        return cxt == null ? null : cxt.getPersistentGlobalVariable(name);
    }

    public Object getParameterDisplayText(String name) {
        return cxt == null ? null : cxt.getParameterDisplayText(name);
    }

    public Object getParameterValue(String name) {
        return cxt == null ? null : cxt.getParameterValue(name);
    }
}
