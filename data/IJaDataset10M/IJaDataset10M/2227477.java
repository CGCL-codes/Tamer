package tr.extract.exports.actions;

import au.com.thinkingrock.tr.resource.Icons;
import org.openide.util.NbBundle;
import tr.extract.exports.ExportAction;
import tr.model.Data;
import tr.model.DataLookup;

/**
 * Export to an XML file action.
 *
 * @author Jeremy Moore (jimoore@netspace.net.au)
 */
public final class ExportXMLAction extends ExportAction {

    /** Constructs a new instance. */
    public ExportXMLAction() {
        super();
        setIcon(Icons.XML);
    }

    /** Save the current datastore as another file. */
    @Override
    public void performAction() {
        Data data = (Data) DataLookup.instance().lookup(Data.class);
        if (data == null) {
            return;
        }
        try {
            new ExportXML().process(data);
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
        }
    }

    /** Get the report action name. */
    @Override
    public String getName() {
        return NbBundle.getMessage(getClass(), "CTL_ExportXMLAction");
    }
}
