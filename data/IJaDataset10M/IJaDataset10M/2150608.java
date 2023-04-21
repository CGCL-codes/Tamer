package com.pcmsolutions.device.EMU.E4.gui.multimode;

import com.pcmsolutions.device.EMU.DeviceException;
import com.pcmsolutions.device.EMU.E4.gui.preset.presetcontext.PresetContextTransferHandler;
import com.pcmsolutions.device.EMU.E4.multimode.IllegalMultimodeChannelException;
import com.pcmsolutions.device.EMU.E4.preset.ReadablePreset;
import com.pcmsolutions.device.EMU.E4.selections.ContextPresetSelection;
import com.pcmsolutions.device.EMU.E4.selections.DataFlavorGrid;
import com.pcmsolutions.device.EMU.E4.selections.MultiModeSelection;
import com.pcmsolutions.system.IntPool;
import com.pcmsolutions.system.tasking.ResourceUnavailableException;
import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.InputEvent;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: pmeehan
 * Date: 19-May-2003
 * Time: 19:23:57
 * To change this template use Options | File Templates.
 */
public class MultiModeTransferHandler extends TransferHandler implements Transferable {

    public static final DataFlavorGrid multiModeFlavor = new DataFlavorGrid(MultiModeSelection.class, "MultiModeSelection");

    private MultiModeSelection mms;

    public MultiModeTransferHandler() {
    }

    public void exportAsDrag(JComponent comp, InputEvent e, int action) {
        super.exportAsDrag(comp, e, action);
    }

    public boolean importData(final JComponent comp, Transferable t) {
        if (comp instanceof MultiModeTable) {
            if (t.isDataFlavorSupported(multiModeFlavor)) {
                try {
                    ((MultiModeTable) comp).setSelection((MultiModeSelection) t.getTransferData(multiModeFlavor));
                    return true;
                } catch (UnsupportedFlavorException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (t.isDataFlavorSupported(PresetContextTransferHandler.presetContextFlavor)) {
                final int sr = ((MultiModeTable) comp).getSelectedRow();
                final int rc = ((MultiModeTable) comp).getRowCount();
                try {
                    final ReadablePreset[] readablePresets = ((ContextPresetSelection) t.getTransferData(PresetContextTransferHandler.presetContextFlavor)).getReadablePresets();
                    for (int i = 0, j = readablePresets.length; i < j; i++) {
                        if (sr + i >= rc) break;
                        try {
                            ((MultiModeTable) comp).getDevice().getMultiModeContext().setPreset(IntPool.get(sr + i + 1), readablePresets[i].getIndex()).post();
                        } catch (IllegalMultimodeChannelException e) {
                            e.printStackTrace();
                        } catch (DeviceException e) {
                            e.printStackTrace();
                        } catch (ResourceUnavailableException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (UnsupportedFlavorException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    public boolean canImport(JComponent comp, DataFlavor[] transferFlavors) {
        if (!(comp instanceof MultiModeTable)) return false;
        DataFlavor chosenFlavor = null;
        for (int i = 0, n = transferFlavors.length; i < n; i++) if (transferFlavors[i].equals(multiModeFlavor)) {
            chosenFlavor = multiModeFlavor;
            break;
        } else if (transferFlavors[i].equals(PresetContextTransferHandler.presetContextFlavor)) {
            chosenFlavor = PresetContextTransferHandler.presetContextFlavor;
            break;
        }
        if (chosenFlavor != null) {
            ((MultiModeTable) comp).setDropFeedbackActive(true);
            ((MultiModeTable) comp).setChosenDropFlavor(chosenFlavor);
            return true;
        }
        return false;
    }

    public int getSourceActions(JComponent c) {
        if (c instanceof MultiModeTable) return TransferHandler.COPY;
        return 0;
    }

    protected Transferable createTransferable(JComponent c) {
        if (c instanceof MultiModeTable) {
            try {
                mms = ((MultiModeTable) c).getSelection();
                MultiModeSelection.MultiModeChannelSelection[] mmcs = mms.getChannelData();
                int[] selCols = mms.getSelCols();
                multiModeFlavor.clearGrid();
                multiModeFlavor.setDefCols(selCols);
                for (int i = 0, j = mmcs.length; i < j; i++) multiModeFlavor.addRow(mmcs[i].getChannel().intValue());
                return this;
            } catch (DeviceException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public DataFlavor[] getTransferDataFlavors() {
        return new DataFlavor[] { multiModeFlavor };
    }

    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return flavor.equals(multiModeFlavor);
    }

    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        if (isDataFlavorSupported(flavor)) return mms;
        return null;
    }
}
