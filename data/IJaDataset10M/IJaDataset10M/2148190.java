package genomemap.netbeans.jobs1;

import cef.command.Command;
import cef.command.listener.CommandListener;
import cef.pui.EventConstructionException;
import cef.pui.EventImpl;
import genomemap.cef.command.ProcessComplDataCommand;
import genomemap.cef.command.listener.ProcessComplDataCommandPrintWriter;
import genomemap.cef.pui.ProcessComplDataEventImpl;
import genomemap.cef.pui.pob.OrganismDataBuilder;
import java.util.Arrays;
import java.util.List;
import jobs.Job;

/**
 * @since Jun 17, 2011
 * @author Susanta Tewari
 */
public class ProcessComplDataJob extends Job<ProcessComplDataEventImpl> {

    public ProcessComplDataJob() {
        this.name = "Process Raw Compl Data";
        setDescription(ProcessComplDataEventImpl.JOB_DESC);
        setComplDataURL();
        setOrganismDataURL();
        setOrganismDataLinkageGroupsProp();
    }

    private String complDataURL = "../genomemap_data/data/all_compl_data.txt";

    public String getComplDataURL() {
        return complDataURL;
    }

    public void setComplDataURL(String complDataURL) {
        this.complDataURL = complDataURL;
        setComplDataURL();
    }

    private void setComplDataURL() {
        props.put(getPropertySuffix() + EventImpl.PROP_DELIMETER + ProcessComplDataEventImpl.COMPL_DATA_URL_PROP, complDataURL);
    }

    private String organismDataURL = "impl1://../genomemap_data/organism_data_impl1";

    public String getOrganismDataURL() {
        return organismDataURL;
    }

    public void setOrganismDataURL(String organismDataURL) {
        this.organismDataURL = organismDataURL;
        setOrganismDataURL();
    }

    private void setOrganismDataURL() {
        props.put(OrganismDataBuilder.URL_PROP, organismDataURL);
    }

    private String organismDataLinkageGroups = "1,2,3,4,5,6,7";

    public String getOrganismDataLinkageGroups() {
        return organismDataLinkageGroups;
    }

    public void setOrganismDataLinkageGroups(String organismDataLinkageGroups) {
        this.organismDataLinkageGroups = organismDataLinkageGroups;
        setOrganismDataLinkageGroupsProp();
    }

    private void setOrganismDataLinkageGroupsProp() {
        props.put(OrganismDataBuilder.LINKAGE_GROUPS_PROP, organismDataLinkageGroups);
    }

    @Override
    protected ProcessComplDataEventImpl createEvent() throws EventConstructionException {
        return new ProcessComplDataEventImpl(props);
    }

    @Override
    protected Command createCommand(ProcessComplDataEventImpl event) {
        return new ProcessComplDataCommand(event);
    }

    @Override
    protected List<CommandListener> addCommandListeners() {
        return Arrays.asList(new CommandListener[] { new ProcessComplDataCommandPrintWriter() });
    }

    @Override
    protected String getPropertySuffix() {
        return ProcessComplDataEventImpl.getPropertySuffix();
    }
}
