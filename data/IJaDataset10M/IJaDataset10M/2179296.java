package de.sonivis.tool.ontology.view.views;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.part.ViewPart;
import de.sonivis.tool.core.ExtensionPointManager;
import de.sonivis.tool.core.datamodel.extension.proxy.IGroupingElement;
import de.sonivis.tool.core.datamodel.networkloader.NetworkFilter;
import de.sonivis.tool.core.datamodel.networkloader.NetworkType;
import de.sonivis.tool.core.eventhandling.EEventType;
import de.sonivis.tool.core.eventhandling.EventManager;

public class NetworkLoaderView extends ViewPart {

    public static final String ID_VIEW = "de.sonivis.tool.ontology.view.views.NetworkLoaderView";

    /**
	 * the filter combos
	 */
    private Combo networkTypeSelectionCombo;

    @Override
    public void createPartControl(Composite parent) {
        Composite loadComposite = new Composite(parent, SWT.NONE);
        loadComposite.setLayout(new GridLayout(3, false));
        final Label networkTypeLabel = new Label(loadComposite, SWT.NONE);
        networkTypeLabel.setText("Network type:");
        networkTypeSelectionCombo = new Combo(loadComposite, SWT.DROP_DOWN);
        networkTypeSelectionCombo.setToolTipText("Choose type of network");
        networkTypeSelectionCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        Button goButton = new Button(loadComposite, SWT.FLAT);
        goButton.setText("Load Network");
        goButton.setToolTipText("Click here to load the network and calculate its metrics.");
        goButton.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                networkFilterChanged();
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });
        updateNetworkTypeCombo();
    }

    @Override
    public void setFocus() {
    }

    private void updateNetworkTypeCombo() {
        Map<String, NetworkType> networkTypes = Collections.synchronizedMap(new HashMap<String, NetworkType>());
        networkTypes = new ExtensionPointManager<NetworkType>().getAllExtensionPointImplementorsByName("de.sonivis.tool.core.networktype");
        final String[] networkTypesArray = networkTypes.keySet().toArray(new String[0]);
        networkTypeSelectionCombo.setItems(networkTypesArray);
        if (networkTypeSelectionCombo.getItemCount() != 0) {
            networkTypeSelectionCombo.select(0);
        }
    }

    private void networkFilterChanged() {
        Map<String, NetworkType> networkTypes = Collections.synchronizedMap(new HashMap<String, NetworkType>());
        networkTypes = new ExtensionPointManager<NetworkType>().getAllExtensionPointImplementorsByName("de.sonivis.tool.core.networktype");
        final String networkName = networkTypeSelectionCombo.getText();
        final NetworkType networkType = networkTypes.get(networkName);
        final NetworkFilter filter = new NetworkFilter(networkType, new ArrayList<IGroupingElement>(), new ArrayList<IGroupingElement>(), new Date(), new Date(), 0, 0);
        EventManager.getInstance().fireEvent(EEventType.NETWORK_FILTER_CHANGE, filter);
    }
}
