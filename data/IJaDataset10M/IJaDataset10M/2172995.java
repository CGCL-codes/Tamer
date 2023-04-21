package fr.soleil.mambo.models;

import java.sql.Date;
import javax.swing.table.DefaultTableModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import fr.esrf.Tango.DevFailed;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.DbData;
import fr.soleil.mambo.bean.view.ViewConfigurationBean;
import fr.soleil.mambo.data.view.ViewConfiguration;
import fr.soleil.mambo.datasources.db.extracting.ExtractingManagerFactory;
import fr.soleil.mambo.datasources.db.extracting.IExtractingManager;
import fr.soleil.mambo.tools.Messages;

public class ViewStringStateBooleanScalarTableModel extends DefaultTableModel {

    static final Logger logger = LoggerFactory.getLogger(ViewStringStateBooleanScalarTableModel.class);

    private static final long serialVersionUID = 1047005533201322779L;

    private DbData readReference;

    private DbData writeReference;

    private final String name;

    private final java.text.SimpleDateFormat genFormatUS;

    private final ViewConfigurationBean viewConfigurationBean;

    public ViewStringStateBooleanScalarTableModel(final String attributeName, final ViewConfigurationBean viewConfigurationBean) {
        super();
        genFormatUS = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        name = attributeName;
        readReference = null;
        writeReference = null;
        this.viewConfigurationBean = viewConfigurationBean;
    }

    @Override
    public int getRowCount() {
        int length = 0;
        if (readReference != null && readReference.getData_timed() != null) {
            length = readReference.getData_timed().length;
        }
        if (writeReference != null && writeReference.getData_timed() != null && writeReference.getData_timed().length > length) {
            length = writeReference.getData_timed().length;
        }
        return length;
    }

    @Override
    public int getColumnCount() {
        int col = 2;
        if (readReference != null && readReference.getData_timed() != null && writeReference != null && writeReference.getData_timed() != null) {
            if (readReference.getData_timed().length > 0 && writeReference.getData_timed().length > 0) {
                if (readReference.getData_timed()[0].value != null && readReference.getData_timed()[0].value.length > 0 && writeReference.getData_timed()[0].value != null && writeReference.getData_timed()[0].value.length > 0) {
                    col = 3;
                }
            }
        }
        return col;
    }

    @Override
    public Object getValueAt(final int rowIndex, final int columnIndex) {
        switch(columnIndex) {
            case 0:
                if (readReference != null && readReference.getData_timed() != null && readReference.getData_timed().length > 0) {
                    if (readReference.getData_timed()[0].value != null && readReference.getData_timed()[0].value.length > 0) {
                        return genFormatUS.format(new Date(readReference.getData_timed()[rowIndex].time.longValue()));
                    }
                }
                if (writeReference != null && writeReference.getData_timed() != null && writeReference.getData_timed().length > 0) {
                    if (writeReference.getData_timed()[0].value != null && writeReference.getData_timed()[0].value.length > 0) {
                        return genFormatUS.format(new Date(writeReference.getData_timed()[rowIndex].time.longValue()));
                    }
                }
                return null;
            case 1:
                if (readReference != null && readReference.getData_timed() != null && readReference.getData_timed().length > 0) {
                    if (readReference.getData_timed()[0].value != null && readReference.getData_timed()[0].value.length > 0) {
                        return readReference.getData_timed()[rowIndex].value[0];
                    }
                }
                if (writeReference != null && writeReference.getData_timed() != null && writeReference.getData_timed().length > 0) {
                    if (writeReference.getData_timed()[0].value != null && writeReference.getData_timed()[0].value.length > 0) {
                        return writeReference.getData_timed()[rowIndex].value[0];
                    }
                }
                return null;
            case 2:
                if (writeReference != null && writeReference.getData_timed() != null && writeReference.getData_timed().length > 0) {
                    if (writeReference.getData_timed()[0].value != null && writeReference.getData_timed()[0].value.length > 0) {
                        return writeReference.getData_timed()[rowIndex].value[0];
                    }
                }
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(final int column) {
        switch(column) {
            case 0:
                return Messages.getMessage("DIALOGS_VIEW_TAB_SCALAR_STRING_AND_STATE_DATE");
            case 1:
                if (readReference != null && readReference.getData_timed() != null && readReference.getData_timed().length > 0) {
                    if (readReference.getData_timed()[0].value != null && readReference.getData_timed()[0].value.length > 0) {
                        return Messages.getMessage("DIALOGS_VIEW_TAB_SCALAR_STRING_AND_STATE_READ");
                    }
                } else if (writeReference != null && writeReference.getData_timed() != null && writeReference.getData_timed().length > 0) {
                    if (writeReference.getData_timed()[0].value != null && writeReference.getData_timed()[0].value.length > 0) {
                        return Messages.getMessage("DIALOGS_VIEW_TAB_SCALAR_STRING_AND_STATE_WRITE");
                    }
                } else {
                    return Messages.getMessage("DIALOGS_VIEW_TAB_SCALAR_STRING_AND_STATE_VALUE");
                }
            case 2:
                if (writeReference != null && writeReference.getData_timed() != null && writeReference.getData_timed().length > 0) {
                    if (writeReference.getData_timed()[0].value != null && writeReference.getData_timed()[0].value.length > 0) {
                        return Messages.getMessage("DIALOGS_VIEW_TAB_SCALAR_STRING_AND_STATE_WRITE");
                    }
                }
            default:
                return "";
        }
    }

    @Override
    public boolean isCellEditable(final int rowIndex, final int columnIndex) {
        return false;
    }

    public DbData[] loadData() {
        final IExtractingManager extractingManager = ExtractingManagerFactory.getCurrentImpl();
        DbData[] splitedData = null;
        final ViewConfiguration conf = viewConfigurationBean.getViewConfiguration();
        if (conf != null) {
            final String[] param = new String[3];
            param[0] = name;
            String startDate = "";
            String endDate = "";
            try {
                startDate = extractingManager.timeToDateSGBD(conf.getData().isHistoric(), conf.getData().getStartDate().getTime());
                endDate = extractingManager.timeToDateSGBD(conf.getData().isHistoric(), conf.getData().getEndDate().getTime());
            } catch (final Exception e) {
                e.printStackTrace();
                return null;
            }
            param[1] = startDate;
            param[2] = endDate;
            try {
                final DbData retrievedData = extractingManager.retrieveData(param, conf.getData().isHistoric(), conf.getData().getSamplingType());
                if (retrievedData != null) {
                    splitedData = retrievedData.splitDbData();
                    if (splitedData != null) {
                        if (splitedData[0] != null && extractingManager.isShowRead()) {
                            readReference = splitedData[0];
                        }
                        if (splitedData[1] != null && extractingManager.isShowWrite()) {
                            writeReference = splitedData[1];
                        }
                    }
                }
            } catch (final DevFailed e) {
                logger.error("", e);
            } catch (final Exception e) {
                e.printStackTrace();
                logger.error("", e);
            }
        }
        fireTableStructureChanged();
        return splitedData;
    }

    public void clearData() {
        readReference = null;
        writeReference = null;
        fireTableStructureChanged();
    }
}
