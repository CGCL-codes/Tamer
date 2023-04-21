package com.pcmsolutions.device.EMU.E4.gui.preset.presetcontext;

import com.pcmsolutions.device.EMU.DeviceException;
import com.pcmsolutions.device.EMU.E4.DeviceContext;
import com.pcmsolutions.device.EMU.E4.PresetClassManager;
import com.pcmsolutions.device.EMU.E4.SampleContextMacros;
import com.pcmsolutions.device.EMU.E4.ViewMessaging;
import com.pcmsolutions.device.EMU.E4.gui.AbstractContextTableModel;
import com.pcmsolutions.device.EMU.E4.gui.device.DefaultDeviceEnclosurePanel;
import com.pcmsolutions.device.EMU.E4.gui.sample.samplecontext.SampleContextEnclosurePanel;
import com.pcmsolutions.device.EMU.E4.preset.PresetContext;
import com.pcmsolutions.device.EMU.E4.preset.PresetContextMacros;
import com.pcmsolutions.device.EMU.E4.preset.PresetException;
import com.pcmsolutions.device.EMU.E4.preset.ReadablePreset;
import com.pcmsolutions.device.EMU.E4.remote.Remotable;
import com.pcmsolutions.device.EMU.E4.sample.ReadableSample;
import com.pcmsolutions.device.EMU.database.EmptyException;
import com.pcmsolutions.device.EMU.database.NoSuchContextException;
import com.pcmsolutions.gui.FrameMenuBarProvider;
import com.pcmsolutions.gui.UserMessaging;
import com.pcmsolutions.gui.ZCommandFactory;
import com.pcmsolutions.gui.ZoeosFrame;
import com.pcmsolutions.gui.desktop.ViewMessageReceiver;
import com.pcmsolutions.system.IntPool;
import com.pcmsolutions.system.Linkable;
import com.pcmsolutions.system.ZDisposable;
import com.pcmsolutions.system.tasking.ResourceUnavailableException;
import com.pcmsolutions.system.tasking.TicketRunnable;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PresetContextEnclosurePanel extends DefaultDeviceEnclosurePanel implements ZDisposable, FrameMenuBarProvider, ListSelectionListener, Linkable, ViewMessageReceiver {

    protected DeviceContext device;

    private PresetContextEditorPanel pcep;

    private JMenuBar menuBar;

    private JMenu processMenu;

    private JMenu locateMenu;

    private JMenu filterMenu;

    private JLabel statusLabel = new JLabel(" ");

    private static final String statusPrefix = " .. ";

    private static final String filterPostfix = " [ filter on ]";

    private static final String statusInfix = " / ";

    private PresetContext presetContext;

    private SampleContextEnclosurePanel sampleContextEnclosurePanel;

    private Timer freeMemTimer;

    private static int freeMemInterval = 20000;

    public void init(DeviceContext device) throws Exception {
        this.device = device;
        pcep = new PresetContextEditorPanel(presetContext = device.getDefaultPresetContext());
        super.init(device, pcep);
        setupMenu();
        pcep.getPresetContextTable().getSelectionModel().addListSelectionListener(this);
        updateStatus();
    }

    protected void buildRunningPanel() {
        super.buildRunningPanel();
        runningPanel.add(statusLabel, BorderLayout.NORTH);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    }

    public PresetContextTable getPresetContextTable() {
        return pcep.getPresetContextTable();
    }

    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) handleSelectionChange();
    }

    private void finalizeProcessMenu() {
        if (processMenu.getMenuComponentCount() == 0) processMenu.setEnabled(false); else processMenu.setEnabled(true);
    }

    private void handleSelectionChange() {
        updateStatus();
        processMenu.removeAll();
        JMenu m = ZCommandFactory.getMenu(pcep.getPresetContextTable().getSelObjects(), "Process");
        Component[] comps = m.getMenuComponents();
        for (int i = 0, j = comps.length; i < j; i++) processMenu.add(comps[i]);
        finalizeProcessMenu();
    }

    public void setStatus(String status) {
        if (status == null) status = " ";
        if (((PresetContextTableModel) pcep.getPresetContextTable().getModel()).getContextFilter() != PresetContextTableModel.allPassFilter) this.statusLabel.setText(statusPrefix + status + filterPostfix); else this.statusLabel.setText(statusPrefix + status);
    }

    public String getStatus() {
        return statusLabel.getText();
    }

    public boolean scrollToPreset(Integer preset, boolean select) {
        PresetContextTableModel pctm = ((PresetContextTableModel) pcep.getPresetContextTable().getModel());
        int row = pctm.getRowForIndex(preset);
        if (row != -1) {
            Rectangle cellRect = pcep.getPresetContextTable().getCellRect(row, 0, true);
            pcep.scrollRectToVisible(cellRect);
            if (select) {
                pcep.getPresetContextTable().setRowSelectionInterval(row, row);
                pcep.getPresetContextTable().setColumnSelectionInterval(0, 0);
            }
            return true;
        }
        return false;
    }

    private void setupMenu() {
        processMenu = new JMenu("Process");
        processMenu.setMnemonic(KeyEvent.VK_P);
        finalizeProcessMenu();
        locateMenu = new JMenu("Select");
        locateMenu.setMnemonic(KeyEvent.VK_S);
        JMenuItem jmi;
        jmi = new JMenuItem(new AbstractAction("Index (goto)") {

            public void actionPerformed(ActionEvent e) {
                String input = (String) JOptionPane.showInputDialog(ZoeosFrame.getInstance(), "Preset index?", "Goto preset index", JOptionPane.QUESTION_MESSAGE, null, null, null);
                if (input != null) try {
                    int pi = Integer.parseInt(input);
                    scrollToPreset(IntPool.get(pi), true);
                } catch (NumberFormatException e1) {
                    UserMessaging.showInfo("Not found");
                    return;
                }
            }
        });
        jmi.setMnemonic(KeyEvent.VK_I);
        locateMenu.add(jmi);
        final String firstEmpty = "First empty";
        jmi = new JMenuItem(new AbstractAction(firstEmpty) {

            public void actionPerformed(ActionEvent e) {
                try {
                    Integer empty = pcep.getPresetContext().firstEmpty();
                    if (!pcep.getPresetContextTable().showingAllIndexes(new Integer[] { empty })) ((PresetContextTableModel) pcep.getPresetContextTable().getModel()).addIndexesToCurrentContextFilter(new Integer[] { empty }, firstEmpty);
                    scrollToPreset(empty, true);
                } catch (NoSuchContextException e1) {
                    e1.printStackTrace();
                } catch (DeviceException e1) {
                    e1.printStackTrace();
                }
            }
        });
        jmi.setMnemonic(KeyEvent.VK_E);
        locateMenu.add(jmi);
        jmi = new JMenuItem(new AbstractAction("Using Regex on name") {

            public void actionPerformed(ActionEvent e) {
                String regex = (String) JOptionPane.showInputDialog(ZoeosFrame.getInstance(), "Regular Expression?", "Select by Regular Expression on Preset Name", JOptionPane.QUESTION_MESSAGE, null, null, null);
                if (regex == null) return;
                Integer first = pcep.getPresetContextTable().selectPresetsByRegex(regex, false, false, true);
                if (first != null) scrollToPreset(first, false); else {
                    UserMessaging.showInfo("Nothing to select");
                    return;
                }
            }
        });
        jmi.setMnemonic(KeyEvent.VK_R);
        locateMenu.add(jmi);
        jmi = new JMenuItem(new AbstractAction("Using Regex on indexed name") {

            public void actionPerformed(ActionEvent e) {
                String regex = (String) JOptionPane.showInputDialog(ZoeosFrame.getInstance(), "Regular Expression?", "Select by Regular Expression on Indexed Preset Name", JOptionPane.QUESTION_MESSAGE, null, null, null);
                if (regex == null) return;
                Integer first = pcep.getPresetContextTable().selectPresetsByRegex(regex, false, true, true);
                if (first != null) scrollToPreset(first, false); else {
                    UserMessaging.showInfo("Nothing to select");
                    return;
                }
            }
        });
        jmi.setMnemonic(KeyEvent.VK_X);
        locateMenu.add(jmi);
        jmi = new JMenuItem(new AbstractAction("Referencing current sample selection") {

            public void actionPerformed(ActionEvent e) {
                final ReadableSample[] currSamples = SampleContextMacros.extractReadableSamples(sampleContextEnclosurePanel.getSampleContextTable().getSelObjects());
                try {
                    List cp = pcep.getPresetContextTable().getContext().getContextPresets();
                    final ReadablePreset[] currPresets = (ReadablePreset[]) cp.toArray(new ReadablePreset[cp.size()]);
                    determineReferencing(currPresets, currSamples);
                } catch (Exception e1) {
                    UserMessaging.showOperationFailed("Internal error");
                }
            }
        });
        jmi.setMnemonic(KeyEvent.VK_S);
        locateMenu.add(jmi);
        jmi = new JMenuItem(new AbstractAction("In current selection referencing current sample selection") {

            public void actionPerformed(ActionEvent e) {
                final ReadableSample[] currSamples = SampleContextMacros.extractReadableSamples(sampleContextEnclosurePanel.getSampleContextTable().getSelObjects());
                final ReadablePreset[] currPresets = PresetContextMacros.extractReadablePresets(pcep.getPresetContextTable().getSelObjects());
                determineReferencing(currPresets, currSamples);
            }
        });
        jmi.setMnemonic(KeyEvent.VK_L);
        locateMenu.add(jmi);
        final String multimodePresets = "Multimode presets";
        jmi = new JMenuItem(new AbstractAction(multimodePresets) {

            public void actionPerformed(ActionEvent e) {
                try {
                    final Integer[] presets = pcep.getPresetContext().getDeviceContext().getMultiModeContext().getDistinctMultimodePresetIndexes();
                    if (presets.length > 0) {
                        if (!pcep.getPresetContextTable().showingAllIndexes(presets)) ((PresetContextTableModel) pcep.getPresetContextTable().getModel()).addIndexesToCurrentContextFilter(presets, multimodePresets);
                        pcep.getPresetContextTable().clearSelection();
                        SwingUtilities.invokeLater(new Runnable() {

                            public void run() {
                                pcep.getPresetContextTable().addIndexesToSelection(presets);
                            }
                        });
                    } else {
                        UserMessaging.showInfo("Nothing to select");
                        return;
                    }
                } catch (DeviceException e1) {
                    UserMessaging.showWarning(e1.getMessage());
                }
            }
        });
        jmi.setMnemonic(KeyEvent.VK_M);
        locateMenu.add(jmi);
        jmi = new JMenuItem(new AbstractAction("Open in workspace") {

            public void actionPerformed(ActionEvent e) {
                try {
                    pcep.getPresetContext().getDeviceContext().getViewManager().selectOpenPresetsInPresetContext().post();
                } catch (ResourceUnavailableException e1) {
                    e1.printStackTrace();
                }
            }
        });
        jmi.setMnemonic(KeyEvent.VK_O);
        locateMenu.add(jmi);
        jmi = new JMenuItem(new AbstractAction("All") {

            public void actionPerformed(ActionEvent e) {
                pcep.getPresetContextTable().selectAll();
            }
        });
        jmi.setMnemonic(KeyEvent.VK_A);
        locateMenu.add(jmi);
        jmi = new JMenuItem(new AbstractAction("Clear") {

            public void actionPerformed(ActionEvent e) {
                pcep.getPresetContextTable().clearSelection();
            }
        });
        jmi.setMnemonic(KeyEvent.VK_C);
        locateMenu.add(jmi);
        jmi = new JMenuItem(new AbstractAction("Invert") {

            public void actionPerformed(ActionEvent e) {
                pcep.getPresetContextTable().invertSelection();
            }
        });
        jmi.setMnemonic(KeyEvent.VK_V);
        locateMenu.add(jmi);
        final String deep = "Deep";
        jmi = new JMenuItem(new AbstractAction(deep) {

            public void actionPerformed(ActionEvent e) {
                try {
                    device.getQueues().generalQ().getPostableTicket(new TicketRunnable() {

                        public void run() throws Exception {
                            final Object[] sobjs = pcep.getPresetContextTable().getSelObjects();
                            final Set<Integer> deepSet = new HashSet<Integer>();
                            for (int i = 0; i < sobjs.length; i++) if (sobjs[i] instanceof ReadablePreset) try {
                                deepSet.addAll(((ReadablePreset) sobjs[i]).getPresetSet());
                            } catch (EmptyException e1) {
                            } catch (PresetException e1) {
                            }
                            final Integer[] deepArray = deepSet.toArray(new Integer[deepSet.size()]);
                            if (!pcep.getPresetContextTable().showingAllIndexes(deepArray)) ((PresetContextTableModel) pcep.getPresetContextTable().getModel()).addIndexesToCurrentContextFilter(deepArray, deep);
                            pcep.getPresetContextTable().clearSelection();
                            SwingUtilities.invokeLater(new Runnable() {

                                public void run() {
                                    pcep.getPresetContextTable().addIndexesToSelection(deepArray);
                                }
                            });
                        }
                    }, "deepSelect").post();
                } catch (ResourceUnavailableException e1) {
                    e1.printStackTrace();
                }
            }
        });
        jmi.setMnemonic(KeyEvent.VK_D);
        locateMenu.add(jmi);
        filterMenu = new JMenu("Filter");
        filterMenu.setMnemonic(KeyEvent.VK_F);
        jmi = new JMenuItem(new AbstractAction("All (reset)") {

            public void actionPerformed(ActionEvent e) {
                ((PresetContextTableModel) pcep.getPresetContextTable().getModel()).setContextFilter(null);
                updateStatus();
            }
        });
        jmi.setMnemonic(KeyEvent.VK_A);
        jmi.setSelected(true);
        filterMenu.add(jmi);
        jmi = new JMenuItem(new AbstractAction("Non-Empty") {

            public void actionPerformed(ActionEvent e) {
                ((PresetContextTableModel) pcep.getPresetContextTable().getModel()).setContextFilter(new AbstractContextTableModel.ContextFilter() {

                    public boolean filter(Integer index, String name, boolean wasFilteredPreviously) {
                        if (!wasFilteredPreviously || name != null && name.trim().equals(DeviceContext.EMPTY_PRESET)) return false;
                        return true;
                    }

                    public String getFilterName() {
                        return "Non-Empty Presets";
                    }
                });
                updateStatus();
            }
        });
        jmi.setMnemonic(KeyEvent.VK_N);
        filterMenu.add(jmi);
        jmi = new JMenuItem(new AbstractAction("Empty") {

            public void actionPerformed(ActionEvent e) {
                ((PresetContextTableModel) pcep.getPresetContextTable().getModel()).setContextFilter(new AbstractContextTableModel.ContextFilter() {

                    public boolean filter(Integer index, String name, boolean wasFilteredPreviously) {
                        if (wasFilteredPreviously && name != null && name.trim().equals(DeviceContext.EMPTY_PRESET)) return true;
                        return false;
                    }

                    public String getFilterName() {
                        return "Empty Presets";
                    }
                });
                updateStatus();
            }
        });
        jmi.setMnemonic(KeyEvent.VK_E);
        filterMenu.add(jmi);
        jmi = new JMenuItem(new AbstractAction("Cached") {

            public void actionPerformed(ActionEvent e) {
                ((PresetContextTableModel) pcep.getPresetContextTable().getModel()).setContextFilter(new AbstractContextTableModel.ContextFilter() {

                    public boolean filter(Integer index, String name, boolean wasFilteredPreviously) {
                        try {
                            if (wasFilteredPreviously && (presetContext.isInitialized(index) && !presetContext.isEmpty(index))) return true;
                        } catch (DeviceException e1) {
                            e1.printStackTrace();
                        }
                        return false;
                    }

                    public String getFilterName() {
                        return "Initialized Presets";
                    }
                });
                updateStatus();
            }
        });
        jmi.setMnemonic(KeyEvent.VK_C);
        filterMenu.add(jmi);
        jmi = new JMenuItem(new AbstractAction("User") {

            public void actionPerformed(ActionEvent e) {
                ((PresetContextTableModel) pcep.getPresetContextTable().getModel()).setContextFilter(new AbstractContextTableModel.ContextFilter() {

                    public boolean filter(Integer index, String name, boolean wasFilteredPreviously) {
                        if (wasFilteredPreviously && index.intValue() <= DeviceContext.MAX_USER_PRESET) return true;
                        return false;
                    }

                    public String getFilterName() {
                        return "User Presets";
                    }
                });
                updateStatus();
            }
        });
        jmi.setMnemonic(KeyEvent.VK_U);
        filterMenu.add(jmi);
        jmi = new JMenuItem(new AbstractAction("Flash") {

            public void actionPerformed(ActionEvent e) {
                ((PresetContextTableModel) pcep.getPresetContextTable().getModel()).setContextFilter(new AbstractContextTableModel.ContextFilter() {

                    public boolean filter(Integer index, String name, boolean wasFilteredPreviously) {
                        if (wasFilteredPreviously && index.intValue() > DeviceContext.MAX_USER_PRESET) return true;
                        return false;
                    }

                    public String getFilterName() {
                        return "Flash Presets";
                    }
                });
                updateStatus();
            }
        });
        jmi.setMnemonic(KeyEvent.VK_F);
        filterMenu.add(jmi);
        jmi = new JMenuItem(new AbstractAction("Invert") {

            public void actionPerformed(ActionEvent e) {
                ((PresetContextTableModel) pcep.getPresetContextTable().getModel()).setContextFilter(new AbstractContextTableModel.ContextFilter() {

                    public boolean filter(Integer index, String name, boolean wasFilteredPreviously) {
                        if (wasFilteredPreviously) return false;
                        return true;
                    }

                    public String getFilterName() {
                        return "Invert current filter";
                    }
                });
                updateStatus();
            }
        });
        jmi.setMnemonic(KeyEvent.VK_I);
        filterMenu.add(jmi);
        jmi = new JMenuItem(new AbstractAction("Selected") {

            public void actionPerformed(ActionEvent e) {
                Object[] sobjs = pcep.getPresetContextTable().getSelObjects();
                if (sobjs.length < 1) {
                    UserMessaging.showInfo("No presets selected");
                    pcep.getPresetContextTable().requestFocus();
                    return;
                }
                final HashMap map_sobjs = new HashMap();
                for (int i = 0, j = sobjs.length; i < j; i++) map_sobjs.put(((ReadablePreset) sobjs[i]).getIndex(), null);
                ((PresetContextTableModel) pcep.getPresetContextTable().getModel()).setContextFilter(new AbstractContextTableModel.ContextFilter() {

                    public boolean filter(Integer index, String name, boolean wasFilteredPreviously) {
                        if (wasFilteredPreviously && map_sobjs.containsKey(index)) return true;
                        return false;
                    }

                    public String getFilterName() {
                        return "Selected Presets";
                    }
                });
                updateStatus();
            }
        });
        jmi.setMnemonic(KeyEvent.VK_S);
        filterMenu.add(jmi);
        PresetClassManager.PresetClassProfile[] profs = PresetClassManager.getAllProfilesWithNonNullPrefix();
        for (int i = 0, j = profs.length; i < j; i++) {
            final String name = profs[i].getName();
            final String prefix = profs[i].getPrefix();
            jmi = new JCheckBoxMenuItem(new AbstractAction(profs[i].getName()) {

                public void actionPerformed(ActionEvent e) {
                    ((PresetContextTableModel) pcep.getPresetContextTable().getModel()).setContextFilter(new AbstractContextTableModel.ContextFilter() {

                        public boolean filter(Integer index, String name, boolean wasFilteredPreviously) {
                            if (name.substring(0, prefix.length()).equals(prefix)) return true;
                            return false;
                        }

                        public String getFilterName() {
                            return name;
                        }
                    });
                    updateStatus();
                }
            });
            jmi.setMnemonic(prefix.charAt(0));
            filterMenu.add(jmi);
        }
        final JMenu freeMem = new JMenu("");
        freeMem.setEnabled(false);
        freeMemTimer = new Timer(freeMemInterval, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    presetContext.getDeviceContext().getQueues().generalQ().getPostableTicket(new TicketRunnable() {

                        public void run() throws Exception {
                            try {
                                final Remotable.PresetMemory pm = presetContext.getDeviceContext().getPresetMemory();
                                SwingUtilities.invokeLater(new Runnable() {

                                    public void run() {
                                        freeMem.setText((pm.getPresetFreeMemory().intValue() + " Kb"));
                                    }
                                });
                            } catch (Exception e1) {
                            }
                        }
                    }, "Preset memory monitor").post();
                } catch (ResourceUnavailableException e1) {
                    e1.printStackTrace();
                }
            }
        });
        freeMemTimer.setCoalesce(true);
        freeMemTimer.start();
        menuBar = new JMenuBar();
        menuBar.add(processMenu);
        menuBar.add(locateMenu);
        menuBar.add(filterMenu);
        menuBar.add(freeMem);
    }

    public void zDispose() {
        pcep.getPresetContextTable().getSelectionModel().removeListSelectionListener(this);
        super.zDispose();
        freeMemTimer.stop();
        device = null;
        presetContext = null;
        pcep = null;
        sampleContextEnclosurePanel = null;
    }

    public boolean isFrameMenuBarAvailable() {
        return true;
    }

    public JMenuBar getFrameMenuBar() {
        return menuBar;
    }

    public void linkTo(Object o) throws Linkable.InvalidLinkException {
        if (o instanceof SampleContextEnclosurePanel) sampleContextEnclosurePanel = (SampleContextEnclosurePanel) o; else throw new Linkable.InvalidLinkException("PresetContextEnclosurePanel cannot link to " + o.getClass());
    }

    void determineReferencing(final ReadablePreset[] presets, final ReadableSample[] samples) {
        try {
            if (!PresetContextMacros.confirmInitializationOfPresets(presets)) return;
            device.getQueues().generalQ().getPostableTicket(new TicketRunnable() {

                public void run() throws Exception {
                    final ReadablePreset[] filtPresets = PresetContextMacros.filterPresetsReferencingSamples(presets, samples);
                    if (filtPresets.length == 0) {
                        UserMessaging.showInfo("Nothing to select");
                        return;
                    }
                    SwingUtilities.invokeLater(new Runnable() {

                        public void run() {
                            if (!UserMessaging.askYesNo("Referencing presets determined. Select now?")) return;
                            final Integer[] fpi = PresetContextMacros.extractPresetIndexes(filtPresets);
                            if (!pcep.getPresetContextTable().showingAllIndexes(fpi)) ((PresetContextTableModel) pcep.getPresetContextTable().getModel()).addIndexesToCurrentContextFilter(fpi, "preset referencing");
                            pcep.getPresetContextTable().clearSelection();
                            SwingUtilities.invokeLater(new Runnable() {

                                public void run() {
                                    pcep.getPresetContextTable().addIndexesToSelection(fpi);
                                }
                            });
                        }
                    });
                }
            }, "presetReferencing").post();
        } catch (PresetException e) {
            e.printStackTrace();
        } catch (ResourceUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void receiveMessage(String msg) {
        if (msg.startsWith(ViewMessaging.MSG_ADD_PRESETS_TO_PRESET_CONTEXT_FILTER)) {
            Integer[] presets = ViewMessaging.extractIntegersFromMessage(msg);
            PresetContextTableModel pctm = (PresetContextTableModel) pcep.getPresetContextTable().getModel();
            pctm.addIndexesToCurrentContextFilter(presets, ViewMessaging.MSG_ADD_PRESETS_TO_PRESET_CONTEXT_FILTER);
        } else if (msg.startsWith(ViewMessaging.MSG_SELECT_OPEN_PRESETS_IN_PRESET_CONTEXT)) {
            final Integer[] presets = ViewMessaging.extractIntegersFromMessage(msg);
            PresetContextTableModel pctm = (PresetContextTableModel) pcep.getPresetContextTable().getModel();
            pctm.addIndexesToCurrentContextFilter(presets, ViewMessaging.MSG_SELECT_OPEN_PRESETS_IN_PRESET_CONTEXT);
            pcep.getPresetContextTable().clearSelection();
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    pcep.getPresetContextTable().addIndexesToSelection(presets);
                }
            });
        }
    }

    public boolean testCondition(String condition) {
        return false;
    }

    void updateStatus() {
        setStatus(pcep.getPresetContextTable().getSelectedRowCount() + " of " + pcep.getPresetContextTable().getRowCount() + " presets selected");
    }
}
