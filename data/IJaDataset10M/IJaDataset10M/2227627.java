package fi.vtt.noen.mfw.bundle.server.plugins.webui.derivedmeasurepage;

import fi.vtt.noen.mfw.bundle.common.Logger;
import fi.vtt.noen.mfw.bundle.server.shared.datamodel.DMDefinition;
import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @author Teemu Kanstren
 */
public class DMListDataProvider extends SortableDataProvider<DMDefinition> {

    private static final Logger log = new Logger(DMListDataProvider.class);

    private final List<DMDefinition> dms;

    public DMListDataProvider(List<DMDefinition> dms) {
        this.dms = dms;
        setSort("name", true);
    }

    public Iterator<DMDefinition> iterator(int i, int i1) {
        SortParam sp = getSort();
        String key = sp.getProperty();
        log.debug("sort key:" + key);
        if (sp.isAscending()) {
            Collections.sort(dms, new DMComparator(key, true));
        } else {
            Collections.sort(dms, new DMComparator(key, false));
        }
        return dms.subList(i, i + i1).iterator();
    }

    public int size() {
        return dms.size();
    }

    public IModel<DMDefinition> model(DMDefinition dmDescription) {
        return new DetachableDMModel(dmDescription);
    }
}
