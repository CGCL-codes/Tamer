package net.zero.smarttrace.wizards.connectConfiguration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.zero.smarttrace.ConnectorConfiguration;
import net.zero.smarttrace.wizards.IModelWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import com.sun.jdi.Bootstrap;
import com.sun.jdi.connect.Connector;

public class ConnectConnectorType extends WizardPage implements IModelWizardPage, SelectionListener {

    public static final String PAGE_NAME = "ConnectType";

    private List<Connector> connectors = Bootstrap.virtualMachineManager().allConnectors();

    private Map<String, Connector> conMap = new HashMap<String, Connector>();

    private Connector selected;

    public ConnectConnectorType() {
        super(PAGE_NAME);
        setTitle("Connector Type");
        setDescription("Select the connector type to use");
        for (Connector con : connectors) {
            conMap.put(con.description(), con);
        }
    }

    @Override
    public void createControl(Composite parent) {
        Composite container = new Composite(parent, SWT.NULL);
        container.setLayout(new RowLayout(SWT.VERTICAL));
        for (Connector con : connectors) {
            Button button = new Button(container, SWT.RADIO);
            button.setText(con.description());
            button.addSelectionListener(this);
        }
        setControl(container);
    }

    @Override
    public void loadPageBaseOnModel(Object model) {
    }

    @Override
    public void performModelUpdate(Object model) {
        ((ConnectorConfiguration) model).setConnector(selected);
    }

    @Override
    public void widgetDefaultSelected(SelectionEvent e) {
        widgetSelected(e);
    }

    @Override
    public void widgetSelected(SelectionEvent e) {
        selected = conMap.get(((Button) e.getSource()).getText());
    }

    @Override
    public String getModelName() {
        return ConnectionConfigurationWizard.MODEL_CONFIG;
    }
}
