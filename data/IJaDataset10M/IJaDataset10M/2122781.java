package au.com.trgtd.tr.reports.weekly;

import au.com.thinkingrock.tr.resource.Icons;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import tr.extract.reports.ReportAction;
import tr.model.Data;
import tr.model.DataLookup;

/**
 * Report action implementation.
 *
 * @author Jeremy Moore (jimoore@netspace.net.au)
 */
public final class ReportActionImpl extends ReportAction {

    private static final String HELP_CTX = "tr.reports.weekly";

    /** Constructs a new instance. */
    public ReportActionImpl() {
        super();
        setIcon(Icons.PDF);
    }

    /** Save the current datastore as another file. */
    @Override
    public void performAction() {
        Data data = (Data) DataLookup.instance().lookup(Data.class);
        if (data == null) {
            return;
        }
        try {
            new ReportImpl().process(data);
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
        }
    }

    /** Get the report action name. */
    @Override
    public String getName() {
        return NbBundle.getMessage(this.getClass(), "CTL_ReportAction");
    }

    /** Get the help context. */
    @Override
    public HelpCtx getHelpCtx() {
        return new HelpCtx(HELP_CTX);
    }
}
