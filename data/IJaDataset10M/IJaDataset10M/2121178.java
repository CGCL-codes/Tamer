package com.pcmsolutions.device.EMU.E4.gui.multimode;

import com.pcmsolutions.device.EMU.E4.DeviceContext;
import com.pcmsolutions.device.EMU.E4.AuditionManager;
import com.pcmsolutions.device.EMU.E4.remote.Remotable;
import com.pcmsolutions.device.EMU.E4.events.multimode.MultiModeChannelChangedEvent;
import com.pcmsolutions.device.EMU.E4.events.multimode.MultiModeRefreshedEvent;
import com.pcmsolutions.device.EMU.E4.events.multimode.MultiModeChannelChangedEvent;
import com.pcmsolutions.device.EMU.E4.gui.colors.UIColors;
import com.pcmsolutions.device.EMU.E4.gui.parameter2.ParameterModelTableCellEditor;
import com.pcmsolutions.device.EMU.E4.gui.table.AbstractRowHeaderedAndSectionedTableModel;
import com.pcmsolutions.device.EMU.E4.gui.table.ColumnData;
import com.pcmsolutions.device.EMU.E4.gui.table.SectionData;
import com.pcmsolutions.device.EMU.E4.multimode.IllegalMultimodeChannelException;
import com.pcmsolutions.device.EMU.E4.multimode.MultiModeContext;
import com.pcmsolutions.device.EMU.E4.multimode.MultiModeListener;
import com.pcmsolutions.device.EMU.E4.parameter.*;
import com.pcmsolutions.device.EMU.E4.preset.*;
import com.pcmsolutions.device.EMU.database.NoSuchContextException;
import com.pcmsolutions.device.EMU.DeviceException;
import com.pcmsolutions.device.EMU.database.EmptyException;
import com.pcmsolutions.device.EMU.database.ContentUnavailableException;
import com.pcmsolutions.device.EMU.database.ContextLocation;
import com.pcmsolutions.device.EMU.DeviceException;
import com.pcmsolutions.system.*;
import com.pcmsolutions.system.tasking.TicketRunnable;
import com.pcmsolutions.system.tasking.ResourceUnavailableException;
import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: pmeehan
 * Date: 05-May-2003
 * Time: 08:49:26
 * To change this template use Options | File Templates.
 */
public class MultiModeTableModel extends AbstractRowHeaderedAndSectionedTableModel implements MultiModeListener, ZDisposable {

    private MultiModeContext multimodeContext;

    private DeviceContext device;

    private int chnls;

    private List readablePresets;

    private ArrayList presetParameterModels = new ArrayList();

    private ArrayList volumeParameterModels = new ArrayList();

    private ArrayList panParameterModels = new ArrayList();

    private ArrayList submixParameterModels = new ArrayList();

    private ParameterModelTableCellEditor pmtce;

    private MultiModePresetTableCellEditor mmptce;

    private Timer refreshTimer;

    private static int refreshInterval = 20000;

    public MultiModeTableModel(DeviceContext d, boolean just16) throws DeviceException {
        this.multimodeContext = d.getMultiModeContext();
        this.device = d;
        pmtce = new ParameterModelTableCellEditor(Color.white, Color.black);
        mmptce = new MultiModePresetTableCellEditor(device);
        try {
            readablePresets = d.getDefaultPresetContext().getDatabasePresets();
        } catch (DeviceException e) {
            readablePresets = new ArrayList();
            e.printStackTrace();
        }
        if (multimodeContext.has32Channels()) {
            if (!just16) chnls = 32; else chnls = 16;
        } else chnls = 16;
        super.init();
        buildParameterModels();
        multimodeContext.addMultiModeListener(this);
        refreshTimer = new Timer(refreshInterval, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    multimodeContext.refresh().post();
                } catch (ResourceUnavailableException e1) {
                    e1.printStackTrace();
                }
            }
        });
        refreshTimer.setCoalesce(true);
        refreshTimer.start();
    }

    protected static class MultiModePresetEditableParameterModel implements ParameterModelWrapper, EditableParameterModel {

        protected EditableParameterModel pm;

        protected PresetContext defPC;

        public MultiModePresetEditableParameterModel(EditableParameterModel pm, PresetContext pc) {
            this.pm = pm;
            defPC = pc;
            pm.setTipShowingOwner(true);
        }

        public synchronized Integer getValue() throws ParameterException {
            return pm.getValue();
        }

        public void setTipShowingOwner(boolean tipShowsOwner) {
            pm.setTipShowingOwner(tipShowsOwner);
        }

        public boolean isTipShowingOwner() {
            return pm.isTipShowingOwner();
        }

        public String getValueString() throws ParameterException {
            return pm.getValueString();
        }

        public String getValueUnitlessString() throws ParameterException {
            return pm.getValueUnitlessString();
        }

        public GeneralParameterDescriptor getParameterDescriptor() {
            return pm.getParameterDescriptor();
        }

        public void addChangeListener(ChangeListener cl) {
            pm.addChangeListener(cl);
        }

        public void removeChangeListener(ChangeListener cl) {
            pm.removeChangeListener(cl);
        }

        public void setShowUnits(boolean showUnits) {
            pm.setShowUnits(showUnits);
        }

        public void zDispose() {
            pm.zDispose();
        }

        public ZCommand[] getZCommands(Class markerClass) {
            return pm.getZCommands(markerClass);
        }

        public Class[] getZCommandMarkers() {
            return pm.getZCommandMarkers();
        }

        public String toString() {
            Integer pn = IntPool.get(0);
            try {
                pn = pm.getValue();
                return new ContextLocation(pn, defPC.getString(pm.getValue())).toString();
            } catch (DeviceException e) {
            } catch (ParameterException e) {
            }
            return pn.toString();
        }

        public Icon getIcon() {
            try {
                return defPC.getReadablePreset(pm.getValue()).getIcon();
            } catch (DeviceException e) {
            } catch (ParameterException e) {
                e.printStackTrace();
            }
            return null;
        }

        public String getToolTipText() {
            try {
                return defPC.getReadablePreset(pm.getValue()).getToolTipText();
            } catch (DeviceException e) {
            } catch (ParameterException e) {
                e.printStackTrace();
            }
            return "";
        }

        public void setValue(Integer value) throws ParameterException {
            ((EditableParameterModel) pm).setValue(value);
        }

        public void offsetValue(Integer offset) throws ParameterException {
            ((EditableParameterModel) pm).offsetValue(offset);
        }

        public void offsetValue(Double offsetAsFOR) throws ParameterException {
            ((EditableParameterModel) pm).offsetValue(offsetAsFOR);
        }

        public void setValueString(String value) throws ParameterException {
            ((EditableParameterModel) pm).setValueString(value);
        }

        public void setValueUnitlessString(String value) throws ParameterException {
            ((EditableParameterModel) pm).setValueUnitlessString(value);
        }

        public void defaultValue() throws ParameterException {
            ((EditableParameterModel) pm).defaultValue();
        }

        public boolean getShowUnits() {
            return pm.getShowUnits();
        }

        public Object[] getWrappedObjects() {
            return new Object[] { pm };
        }
    }

    public MultiModeContext getMultimodeContext() {
        return multimodeContext;
    }

    private void buildParameterModels() {
        presetParameterModels.clear();
        for (int n = 0; n < chnls; n++) {
            try {
                presetParameterModels.add(new ParameterModelEncloser(multimodeContext.getMultiModeChannel(IntPool.get(n + 1)).getPresetEditableParameterModel()));
            } catch (Exception e) {
                e.printStackTrace();
                presetParameterModels.clear();
                break;
            }
        }
        volumeParameterModels.clear();
        for (int n = 0; n < chnls; n++) {
            try {
                volumeParameterModels.add(new ParameterModelEncloser(multimodeContext.getMultiModeChannel(IntPool.get(n + 1)).getVolumeEditableParameterModel()));
            } catch (Exception e) {
                e.printStackTrace();
                volumeParameterModels.clear();
                break;
            }
        }
        panParameterModels.clear();
        for (int n = 0; n < chnls; n++) {
            try {
                panParameterModels.add(new ParameterModelEncloser(multimodeContext.getMultiModeChannel(IntPool.get(n + 1)).getPanEditableParameterModel()));
            } catch (Exception e) {
                e.printStackTrace();
                panParameterModels.clear();
                break;
            }
        }
        submixParameterModels.clear();
        for (int n = 0; n < chnls; n++) {
            try {
                submixParameterModels.add(new ParameterModelEncloser(multimodeContext.getMultiModeChannel(IntPool.get(n + 1)).getSubmixEditableParameterModel()));
            } catch (Exception e) {
                e.printStackTrace();
                submixParameterModels.clear();
                break;
            }
        }
    }

    protected void buildColumnAndSectionData() {
        int hw = 45;
        int pw = 180;
        int vw = 50;
        int pnw = 45;
        int sw = 55;
        rowHeaderColumnData = new ColumnData("", hw, JLabel.LEFT, 0, Object.class);
        columnData = new ColumnData[4];
        sectionData = new SectionData[2];
        columnData[0] = new ColumnData("Preset", pw, JLabel.LEFT, 0, ReadablePreset.class, null, mmptce);
        columnData[1] = new ColumnData("Volume", vw, JLabel.LEFT, 1, ReadableParameterModel.class, null, pmtce);
        columnData[2] = new ColumnData("Pan", pnw, JLabel.LEFT, 1, ReadableParameterModel.class, null, pmtce);
        columnData[3] = new ColumnData("Submix", sw, JLabel.LEFT, 1, ReadableParameterModel.class, null, pmtce);
        String sStr = " ";
        sectionData[0] = new SectionData(UIColors.getTableFirstSectionBG(), UIColors.getTableFirstSectionHeaderBG(), UIColors.getTableFirstSectionFG(), pw, sStr);
        sectionData[1] = new SectionData(UIColors.getTableSecondSectionBG(), UIColors.getTableSecondSectionHeaderBG(), UIColors.getTableSecondSectionFG(), vw + pnw + sw, sStr);
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if (columnIndex > 0) return true;
        return false;
    }

    protected void doRefresh() {
        for (int i = 0; i < chnls; i++) {
            final int row = i;
            tableRowObjects.add(new ColumnValueProvider() {

                public Object getValueAt(int col) {
                    switch(col) {
                        case 0:
                            return "Ch " + (row + 1);
                        case 1:
                            try {
                                if (presetParameterModels.size() >= row + 1) {
                                    EditableParameterModel p = (EditableParameterModel) presetParameterModels.get(row);
                                    if (p == null) return multimodeContext.getPreset(IntPool.get(row + 1)).toString(); else return p;
                                } else return "error";
                            } catch (IllegalMultimodeChannelException e) {
                                e.printStackTrace();
                            } catch (DeviceException e) {
                                e.printStackTrace();
                            }
                            break;
                        case 2:
                            try {
                                if (volumeParameterModels.size() >= row + 1) {
                                    EditableParameterModel p = (EditableParameterModel) volumeParameterModels.get(row);
                                    if (p == null) return multimodeContext.getVolume(IntPool.get(row + 1)).toString(); else return p;
                                } else return "error";
                            } catch (IllegalMultimodeChannelException e) {
                                e.printStackTrace();
                            } catch (DeviceException e) {
                                e.printStackTrace();
                            }
                            break;
                        case 3:
                            try {
                                if (panParameterModels.size() >= row + 1) {
                                    EditableParameterModel p = (EditableParameterModel) panParameterModels.get(row);
                                    if (p == null) return multimodeContext.getPan(IntPool.get(row + 1)).toString(); else return p;
                                } else return "error";
                            } catch (IllegalMultimodeChannelException e) {
                                e.printStackTrace();
                            } catch (DeviceException e) {
                                e.printStackTrace();
                            }
                            break;
                        case 4:
                            try {
                                if (submixParameterModels.size() >= row + 1) {
                                    EditableParameterModel p = (EditableParameterModel) submixParameterModels.get(row);
                                    if (p == null) return multimodeContext.getSubmix(IntPool.get(row + 1)).toString(); else return p;
                                } else return "error";
                            } catch (IllegalMultimodeChannelException e) {
                                e.printStackTrace();
                            } catch (DeviceException e) {
                                e.printStackTrace();
                            }
                            break;
                    }
                    return "";
                }

                public void zDispose() {
                }
            });
        }
    }

    protected void doPreRefresh() {
    }

    protected void doPostRefresh() {
    }

    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        fireTableCellUpdated(rowIndex, columnIndex);
    }

    public Object getValueAt(int row, int col) {
        switch(col) {
            case 0:
                if (row + 1 == device.getDevicePreferences().ZPREF_auditionChnl.getValue()) return "Ch " + (row + 1) + " A";
                return "Ch " + (row + 1);
            case 1:
                try {
                    if (presetParameterModels.size() >= row + 1) {
                        EditableParameterModel p = (EditableParameterModel) presetParameterModels.get(row);
                        if (p == null) return multimodeContext.getVolume(IntPool.get(row + 1)).toString(); else return p;
                    } else return "error";
                } catch (IllegalMultimodeChannelException e) {
                    e.printStackTrace();
                } catch (DeviceException e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                try {
                    if (volumeParameterModels.size() >= row + 1) {
                        EditableParameterModel p = (EditableParameterModel) volumeParameterModels.get(row);
                        if (p == null) return multimodeContext.getVolume(IntPool.get(row + 1)).toString(); else return p;
                    } else return "error";
                } catch (IllegalMultimodeChannelException e) {
                    e.printStackTrace();
                } catch (DeviceException e) {
                    e.printStackTrace();
                }
                break;
            case 3:
                try {
                    if (panParameterModels.size() >= row + 1) {
                        EditableParameterModel p = (EditableParameterModel) panParameterModels.get(row);
                        if (p == null) return multimodeContext.getPan(IntPool.get(row + 1)).toString(); else return p;
                    } else return "error";
                } catch (IllegalMultimodeChannelException e) {
                    e.printStackTrace();
                } catch (DeviceException e) {
                    e.printStackTrace();
                }
                break;
            case 4:
                try {
                    if (submixParameterModels.size() >= row + 1) {
                        EditableParameterModel p = (EditableParameterModel) submixParameterModels.get(row);
                        if (p == null) return multimodeContext.getSubmix(IntPool.get(row + 1)).toString(); else return p;
                    } else return "error";
                } catch (IllegalMultimodeChannelException e) {
                    e.printStackTrace();
                } catch (DeviceException e) {
                    e.printStackTrace();
                }
                break;
        }
        return "";
    }

    public void mmChannelChanged(final MultiModeChannelChangedEvent ev) {
        MultiModeTableModel.this.fireTableRowsUpdated(ev.getChannel().intValue() - 1, ev.getChannel().intValue() - 1);
    }

    public void mmRefreshed(final MultiModeRefreshedEvent ev) {
        MultiModeTableModel.this.fireTableRowsUpdated(0, chnls - 1);
    }

    public void zDispose() {
        super.zDispose();
        multimodeContext.removeMultiModeListener(this);
        multimodeContext = null;
        readablePresets.clear();
        refreshTimer.stop();
        int size;
        size = volumeParameterModels.size();
        for (int n = 0; n < size; n++) ((EditableParameterModel) volumeParameterModels.get(n)).zDispose();
        size = panParameterModels.size();
        for (int n = 0; n < size; n++) ((EditableParameterModel) panParameterModels.get(n)).zDispose();
        size = submixParameterModels.size();
        for (int n = 0; n < size; n++) ((EditableParameterModel) submixParameterModels.get(n)).zDispose();
        size = presetParameterModels.size();
        for (int n = 0; n < size; n++) ((EditableParameterModel) presetParameterModels.get(n)).zDispose();
        volumeParameterModels.clear();
        volumeParameterModels = null;
        panParameterModels.clear();
        panParameterModels = null;
        submixParameterModels.clear();
        submixParameterModels = null;
        presetParameterModels.clear();
        presetParameterModels = null;
        device = null;
        readablePresets = null;
        pmtce = null;
        mmptce = null;
    }
}
