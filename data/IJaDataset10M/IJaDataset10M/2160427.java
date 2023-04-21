package cw.customermanagementmodul.gui;

import cw.boardingschoolmanagement.gui.component.CWComponentFactory;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import cw.boardingschoolmanagement.gui.component.CWLabel;
import cw.boardingschoolmanagement.gui.component.CWView;

/**
 *
 * @author ManuelG
 */
public class CustomerHomeExtentionView extends CWView {

    private CustomerHomeExtentionPresentationModel model;

    private CWComponentFactory.CWComponentContainer componentContainer;

    private CWLabel lSizeCustomers;

    public CustomerHomeExtentionView(CustomerHomeExtentionPresentationModel model) {
        this.model = model;
        initComponents();
        buildView();
        initEventHandling();
    }

    private void initComponents() {
        lSizeCustomers = CWComponentFactory.createLabel(model.getSizeCustomersValueModel());
        componentContainer = CWComponentFactory.createComponentContainer().addComponent(lSizeCustomers);
    }

    private void initEventHandling() {
    }

    private void buildView() {
        this.setHeaderInfo(new CWHeaderInfo("Kundeninformationen"));
        FormLayout layout = new FormLayout("pref", "pref");
        PanelBuilder builder = new PanelBuilder(layout, this.getContentPanel());
        CellConstraints cc = new CellConstraints();
        builder.add(lSizeCustomers, cc.xy(1, 1));
    }

    @Override
    public void dispose() {
        componentContainer.dispose();
        model.dispose();
    }
}
