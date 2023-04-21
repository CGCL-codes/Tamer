package fr.soleil.mambo.containers.archiving;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import fr.soleil.archiving.gui.tools.GUIUtilities;
import fr.soleil.mambo.components.archiving.ACAttributeDetailTdbATable;
import fr.soleil.mambo.components.archiving.ACAttributeDetailTdbCTable;
import fr.soleil.mambo.components.archiving.ACAttributeDetailTdbDTable;
import fr.soleil.mambo.components.archiving.ACAttributeDetailTdbETable;
import fr.soleil.mambo.components.archiving.ACAttributeDetailTdbPTable;
import fr.soleil.mambo.components.archiving.ACAttributeDetailTdbRTable;
import fr.soleil.mambo.components.archiving.ACAttributeDetailTdbTTable;
import fr.soleil.mambo.components.archiving.ACAttributeDetailTdbTitleTable;
import fr.soleil.mambo.data.archiving.ArchivingConfigurationAttribute;
import fr.soleil.mambo.models.ACAttributeDetailTdbATableModel;
import fr.soleil.mambo.models.ACAttributeDetailTdbCTableModel;
import fr.soleil.mambo.models.ACAttributeDetailTdbDTableModel;
import fr.soleil.mambo.models.ACAttributeDetailTdbETableModel;
import fr.soleil.mambo.models.ACAttributeDetailTdbPTableModel;
import fr.soleil.mambo.models.ACAttributeDetailTdbRTableModel;
import fr.soleil.mambo.models.ACAttributeDetailTdbTTableModel;
import fr.soleil.mambo.models.ACAttributeDetailTdbTitleTableModel;

public class ACAttributeDetailTdbPanel extends JPanel {

    private static ACAttributeDetailTdbPanel instance;

    private ACAttributeDetailTdbPanel() {
        super();
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        removeComponents();
        placeComponents();
        repaint();
    }

    public static ACAttributeDetailTdbPanel getInstance() {
        if (instance == null) {
            instance = new ACAttributeDetailTdbPanel();
            GUIUtilities.setObjectBackground(instance, GUIUtilities.ARCHIVING_COLOR);
        }
        return instance;
    }

    public void load(ArchivingConfigurationAttribute _attribute) {
        ACAttributeDetailTdbTitleTableModel.getInstance().load(_attribute);
        ACAttributeDetailTdbPTableModel.getInstance().load(_attribute);
        ACAttributeDetailTdbATableModel.getInstance().load(_attribute);
        ACAttributeDetailTdbRTableModel.getInstance().load(_attribute);
        ACAttributeDetailTdbTTableModel.getInstance().load(_attribute);
        ACAttributeDetailTdbDTableModel.getInstance().load(_attribute);
        ACAttributeDetailTdbCTableModel.getInstance().load(_attribute);
        ACAttributeDetailTdbETableModel.getInstance().load(_attribute);
        removeComponents();
        placeComponents();
        repaint();
    }

    private void removeComponents() {
        remove(ACAttributeDetailTdbTitleTable.getInstance());
        remove(ACAttributeDetailTdbPTable.getInstance());
        remove(ACAttributeDetailTdbATable.getInstance());
        remove(ACAttributeDetailTdbRTable.getInstance());
        remove(ACAttributeDetailTdbTTable.getInstance());
        remove(ACAttributeDetailTdbDTable.getInstance());
        remove(ACAttributeDetailTdbCTable.getInstance());
        remove(ACAttributeDetailTdbETable.getInstance());
    }

    private void placeComponents() {
        if (ACAttributeDetailTdbTitleTableModel.getInstance().toPaint()) {
            add(ACAttributeDetailTdbTitleTable.getInstance());
        }
        if (ACAttributeDetailTdbPTableModel.getInstance().toPaint()) {
            add(ACAttributeDetailTdbPTable.getInstance());
        }
        if (ACAttributeDetailTdbATableModel.getInstance().toPaint()) {
            add(ACAttributeDetailTdbATable.getInstance());
        }
        if (ACAttributeDetailTdbRTableModel.getInstance().toPaint()) {
            add(ACAttributeDetailTdbRTable.getInstance());
        }
        if (ACAttributeDetailTdbTTableModel.getInstance().toPaint()) {
            add(ACAttributeDetailTdbTTable.getInstance());
        }
        if (ACAttributeDetailTdbDTableModel.getInstance().toPaint()) {
            add(ACAttributeDetailTdbDTable.getInstance());
        }
        if (ACAttributeDetailTdbCTableModel.getInstance().toPaint()) {
            add(ACAttributeDetailTdbCTable.getInstance());
        }
        if (ACAttributeDetailTdbETableModel.getInstance().toPaint()) {
            add(ACAttributeDetailTdbETable.getInstance());
        }
    }
}
