package org.plazmaforge.framework.report;

import java.awt.event.ActionEvent;
import org.plazmaforge.framework.action.DefaultAction;
import org.plazmaforge.framework.core.DataTransfer;
import org.plazmaforge.framework.core.IAcceptor;
import org.plazmaforge.framework.core.NullAcceptor;
import org.plazmaforge.framework.core.exception.ApplicationException;

public abstract class AbstractReportAction extends DefaultAction implements IReportAction {

    private IReport report;

    private IAcceptor acceptor;

    private boolean ignoreInvalidParameter;

    private boolean validAcceptor = true;

    public AbstractReportAction() {
        super();
    }

    public AbstractReportAction(IReport report) {
        initReport(report);
    }

    public AbstractReportAction(IAcceptor acceptor) {
        this.acceptor = acceptor;
    }

    public AbstractReportAction(IReport report, IAcceptor acceptor) {
        initReport(report);
        this.acceptor = acceptor;
    }

    protected void initReport(IReport report) {
        this.report = report;
    }

    public void execute(DataTransfer dataTransfer) throws ApplicationException {
        perform(null);
    }

    public void perform(ActionEvent evt) throws ApplicationException {
        try {
            if (!isStartReport() || getReport() == null) {
                return;
            }
            readParameters();
            verifyParameters();
            executeReport();
        } catch (InvalidReportParameterException ex) {
            if (isIgnoreInvalidParameter()) {
                return;
            }
        }
    }

    public IReport getReport() {
        if (report == null) {
            report = createReport();
        }
        return report;
    }

    protected boolean isStartReport() throws ApplicationException {
        return getAcceptor().isStartProcess();
    }

    protected void executeReport() throws ApplicationException {
        IReport report = getReport();
        if (report == null) {
            return;
        }
        ReportProcessor.executeReport(report);
    }

    public final void setAcceptor(IAcceptor acceptor) {
        if (acceptor == null) {
            throw new IllegalArgumentException("Acceptor is NULL");
        }
        this.acceptor = acceptor;
    }

    public final IAcceptor getAcceptor() {
        if (acceptor == null) {
            acceptor = createAcceptor();
        }
        return acceptor;
    }

    protected void readParameters() throws ApplicationException {
        IAcceptor acceptor = getAcceptor();
        if (acceptor != null) {
            getReport().addParameters(acceptor.readParameters());
        }
        getReport().readParameters();
    }

    protected void verifyParameters() throws InvalidReportParameterException {
        getReport().verifyParameters();
    }

    protected abstract IReport createReport();

    protected IAcceptor createAcceptor() {
        return new NullAcceptor();
    }

    public String toString() {
        String name = getName();
        return name == null ? toString() : name;
    }

    public boolean isIgnoreInvalidParameter() {
        return ignoreInvalidParameter;
    }

    public void setIgnoreInvalidParameter(boolean ignoreInvalidParameter) {
        this.ignoreInvalidParameter = ignoreInvalidParameter;
    }

    public boolean isValidAcceptor() {
        return validAcceptor;
    }

    public void setValidAcceptor(boolean validAcceptor) {
        this.validAcceptor = validAcceptor;
    }
}
