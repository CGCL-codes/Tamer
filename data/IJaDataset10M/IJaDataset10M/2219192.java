package org.objectstyle.cayenne.modeler.dialog.pref;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.objectstyle.cayenne.modeler.util.CayenneController;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * @author Andrei Adamchik
 */
public class DataSourcePreferencesView extends JPanel {

    protected JButton addDataSource;

    protected JButton duplicateDataSource;

    protected JButton removeDataSource;

    protected JButton testDataSource;

    protected JComboBox dataSources;

    protected DBConnectionInfoEditor dataSourceEditor;

    public DataSourcePreferencesView(CayenneController controller) {
        this.addDataSource = new JButton("New...");
        this.duplicateDataSource = new JButton("Duplicate...");
        this.removeDataSource = new JButton("Delete");
        this.testDataSource = new JButton("Test...");
        this.dataSources = new JComboBox();
        this.dataSourceEditor = new DBConnectionInfoEditor(controller);
        CellConstraints cc = new CellConstraints();
        PanelBuilder builder = new PanelBuilder(new FormLayout("fill:min(150dlu;pref)", "p, 3dlu, p, 10dlu, p, 3dlu, p, 3dlu, p, 10dlu, p"));
        builder.setDefaultDialogBorder();
        builder.add(new JLabel("Select DataSource"), cc.xy(1, 1));
        builder.add(dataSources, cc.xy(1, 3));
        builder.add(addDataSource, cc.xy(1, 5));
        builder.add(duplicateDataSource, cc.xy(1, 7));
        builder.add(removeDataSource, cc.xy(1, 9));
        builder.add(testDataSource, cc.xy(1, 11));
        setLayout(new BorderLayout());
        add(dataSourceEditor.getView(), BorderLayout.CENTER);
        add(builder.getPanel(), BorderLayout.EAST);
    }

    public DBConnectionInfoEditor getDataSourceEditor() {
        return dataSourceEditor;
    }

    public JComboBox getDataSources() {
        return dataSources;
    }

    public JButton getAddDataSource() {
        return addDataSource;
    }

    public JButton getRemoveDataSource() {
        return removeDataSource;
    }

    public JButton getTestDataSource() {
        return testDataSource;
    }

    public JButton getDuplicateDataSource() {
        return duplicateDataSource;
    }
}
