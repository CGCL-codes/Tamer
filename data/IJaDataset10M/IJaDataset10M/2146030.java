package fr.soleil.mambo.models;

import javax.swing.table.DefaultTableModel;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.Mode;
import fr.soleil.mambo.data.archiving.ArchivingConfigurationAttribute;
import fr.soleil.mambo.data.archiving.ArchivingConfigurationAttributeProperties;
import fr.soleil.mambo.data.archiving.ArchivingConfigurationAttributeTDBProperties;
import fr.soleil.mambo.data.archiving.ArchivingConfigurationMode;
import fr.soleil.mambo.datasources.db.archiving.ArchivingManagerFactory;
import fr.soleil.mambo.datasources.db.archiving.IArchivingManager;
import fr.soleil.mambo.tools.Messages;

public class ACAttributeDetailTdbDTableModel extends DefaultTableModel {

    private static ACAttributeDetailTdbDTableModel instance;

    private ArchivingConfigurationAttribute attribute;

    private ArchivingConfigurationAttributeTDBProperties TDBProperties;

    public static ACAttributeDetailTdbDTableModel getInstance() {
        if (instance == null) {
            instance = new ACAttributeDetailTdbDTableModel();
        }
        return instance;
    }

    private ACAttributeDetailTdbDTableModel() {
        super();
    }

    public void load(ArchivingConfigurationAttribute _attribute) {
        attribute = _attribute;
        ArchivingConfigurationAttributeProperties attrProperties = attribute.getProperties();
        TDBProperties = attrProperties.getTDBProperties();
        fireTableDataChanged();
    }

    public int getColumnCount() {
        return 4;
    }

    public int getRowCount() {
        return 1;
    }

    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        switch(columnIndex) {
            case 0:
                return getFirstColumnValue(rowIndex);
            case 1:
                return getSecondColumnValue(rowIndex);
            case 2:
                return getACColumnValue(rowIndex);
            case 3:
                return getDBColumnValue(rowIndex);
            default:
                return null;
        }
    }

    private Object getDBColumnValue(int rowIndex) {
        String ret = "";
        IArchivingManager manager = ArchivingManagerFactory.getCurrentImpl();
        try {
            Mode mode = manager.getArchivingMode(attribute.getCompleteName(), false);
            if (mode != null && mode.getModeD() != null) {
                switch(rowIndex) {
                    case 0:
                        ret = mode.getModeD().getPeriod() + "ms";
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    private Object getACColumnValue(int rowIndex) {
        String ret = "";
        if (TDBProperties != null) {
            ArchivingConfigurationMode ACDMode = TDBProperties.getMode(ArchivingConfigurationMode.TYPE_D);
            if (ACDMode != null) {
                switch(rowIndex) {
                    case 0:
                        ret = ACDMode.getMode().getModeD().getPeriod() + "ms";
                        break;
                }
            }
        }
        return ret;
    }

    public boolean toPaint() {
        if (TDBProperties != null) {
            if (TDBProperties.getMode(ArchivingConfigurationMode.TYPE_D) != null) {
                return true;
            } else if (attribute != null && attribute.getCompleteName() != null) {
                try {
                    Mode mode = ArchivingManagerFactory.getCurrentImpl().getArchivingMode(attribute.getCompleteName(), false);
                    if (mode.getModeD() != null) {
                        return true;
                    }
                } catch (Exception e) {
                    return false;
                }
            }
        }
        return false;
    }

    private Object getSecondColumnValue(int rowIndex) {
        String ret = "";
        switch(rowIndex) {
            case 0:
                ret = Messages.getMessage("ARCHIVING_ATTRIBUTES_DETAIL_MODED_PERIOD");
                break;
        }
        return ret;
    }

    private Object getFirstColumnValue(int rowIndex) {
        String ret = "";
        if (rowIndex == 0) {
            ret = Messages.getMessage("ARCHIVING_ATTRIBUTES_DETAIL_MODED_NAME");
        }
        return ret;
    }
}
