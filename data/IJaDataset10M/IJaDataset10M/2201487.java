package ppine.ui;

import cytoscape.CyNetwork;
import java.util.Collection;
import ppine.viewmodel.structs.CytoProtein;
import ppine.logicmodel.structs.SpeciesTreeNode;

public abstract class UIController {

    private static UIController controller = new DefaultUIController();

    public static UIController getInstance() {
        return controller;
    }

    public abstract Collection<SpeciesTreeNode> getSelectedNetworks();

    public abstract Collection<CytoProtein> getSelectedProteins(CyNetwork cyNetwork);

    public abstract void selectConnectedNodes(CyNetwork cyNetwork);

    public abstract void selectUnconnectedNodes(CyNetwork cyNetwork);

    public abstract void unselectUnConnectedNodes(CyNetwork cyNetwork);

    public abstract void showSelectedNetworks();

    public abstract void showLoadedInteractions();

    public abstract void newData();

    public abstract void updateData();

    public abstract void deleteData();

    public abstract void setPPINEActiveTab();

    public abstract void initDataView();
}
