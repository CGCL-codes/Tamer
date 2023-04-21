package net.sf.filePiper.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import net.sf.filePiper.model.ToolModel;
import net.sf.sfac.editor.EditorConfig;
import net.sf.sfac.gui.ExceptionDialog;
import net.sf.sfac.gui.cmp.PartialLineBorder;
import net.sf.sfac.gui.editor.EditorOptionsDialog;
import net.sf.sfac.gui.framework.ActionRepository;
import net.sf.sfac.gui.framework.BarChangeListener;
import net.sf.sfac.gui.framework.SharedResources;
import net.sf.sfac.setting.SubSettingsList;
import net.sf.sfac.setting.SubSettingsProxy;
import net.sf.sfac.utils.Comparison;
import org.apache.log4j.Logger;

public class ProfilesController implements BarChangeListener {

    private static Logger log = Logger.getLogger(ProfilesController.class);

    private ToolModel mainModel;

    private PiperMainPanel mainPanel;

    private JComboBox pipelineCombo;

    private Action restoreProfile;

    private Action renameProfile;

    private Action removeProfile;

    public ProfilesController(ToolModel model, PiperMainPanel pane, ActionRepository repo) {
        mainModel = model;
        mainPanel = pane;
        createActions(repo);
        repo.addBarChangeListener(this);
    }

    private void createActions(ActionRepository repo) {
        repo.addAction("storeProfile", "Store profile", "Store current profile", "Profile", "d/a1", "z1", SharedResources.getIcon("borderAdd.gif"), new AbstractAction() {

            public void actionPerformed(ActionEvent ae) {
                storeCurrentProfile();
            }
        });
        restoreProfile = repo.addAction("restoreProfile", "Restore profile", "Restore profile values", "Profile", "d/a2", "z1", SharedResources.getIcon("refresh.gif"), new AbstractAction() {

            public void actionPerformed(ActionEvent ae) {
                refreshProfile();
            }
        });
        renameProfile = repo.addAction("renameProfile", "Rename profile", "Rename current profile", "Profile", "d/a3", "z2", SharedResources.getIcon("rename.gif"), new AbstractAction() {

            public void actionPerformed(ActionEvent ae) {
                renameProfile();
            }
        });
        removeProfile = repo.addAction("removeProfile", "Remove profile", "Remove current profile", "Profile", "d/a4", "z3", SharedResources.getIcon("borderRemove.gif"), new AbstractAction() {

            public void actionPerformed(ActionEvent ae) {
                removeProfile();
            }
        });
        checkActionStates();
    }

    void checkActionStates() {
        boolean oneProfileSelected = (mainModel.getStoredSettings().getSelectedItem() != null);
        restoreProfile.setEnabled(oneProfileSelected);
        renameProfile.setEnabled(oneProfileSelected);
        removeProfile.setEnabled(oneProfileSelected);
    }

    private String getCurrentProfileName() {
        SubSettingsList subs = mainModel.getStoredSettings();
        SubSettingsProxy current = (SubSettingsProxy) subs.getSelectedItem();
        return (current == null) ? null : current.getName();
    }

    void storeCurrentProfile() {
        String name = null;
        try {
            EditorOptionsDialog dial = new EditorOptionsDialog(mainPanel);
            dial.setTitle("Store Profile");
            StoreProfileParams params = new StoreProfileParams(getCurrentProfileName());
            Object result = dial.showAndEditObject(params);
            if ("OK".equals(result)) {
                name = params.getProfileName();
            }
            if (Comparison.isDefined(name)) {
                log.info("Store current profile with name='" + name + "'");
                mainPanel.updateModel();
                mainModel.storeCurrentProfile(name);
                checkActionStates();
            } else {
                log.info("Store action cancelled by user");
            }
        } catch (Exception e) {
            log.error("Cannot store profile: " + name, e);
            ExceptionDialog.showExceptionDialog(mainPanel, "Profile error", "Unable to add profile", e);
        }
    }

    void refreshProfile() {
        String currentProfileName = getCurrentProfileName();
        try {
            if (currentProfileName != null) {
                mainModel.useProfile(currentProfileName);
            }
        } catch (Exception e) {
            log.error("Cannot add refresh pipeline with profile " + currentProfileName, e);
            ExceptionDialog.showExceptionDialog(mainPanel, "Profile error", "Unable to use current profile: " + currentProfileName, e);
        }
    }

    void renameProfile() {
        String oldName = getCurrentProfileName();
        String newName = null;
        try {
            newName = JOptionPane.showInputDialog(mainPanel, "New name for current profile");
            if (newName != null) {
                mainModel.changeProfileName(oldName, newName);
            } else {
                log.info("Rename action cancelled by user");
            }
        } catch (Exception e) {
            log.error("The current profile (" + oldName + ") cannot be renamed to: " + newName, e);
            ExceptionDialog.showExceptionDialog(mainPanel, "Profile error", "Unable to rename profile: " + oldName + " --> " + newName, e);
        }
    }

    void removeProfile() {
        String profileName = getCurrentProfileName();
        try {
            mainModel.removeProfile(profileName);
            checkActionStates();
        } catch (Exception e) {
            log.error("The current profile (" + profileName + ") cannot be removed", e);
            ExceptionDialog.showExceptionDialog(mainPanel, "Profile error", "Unable to remove profile: " + profileName, e);
        }
    }

    public ToolModel getToolModel() {
        return mainModel;
    }

    public static enum StoreAction {

        OVERRIDE("Override existing profile"), ADD_NEW("Add new profile");

        private String text;

        StoreAction(String txt) {
            text = txt;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    @EditorConfig(label = "")
    public class StoreProfileParams {

        private StoreAction action;

        private String existingProfileName;

        private String newProfileName;

        public StoreProfileParams(String currentProfileName) {
            if (currentProfileName != null) {
                action = StoreAction.OVERRIDE;
                existingProfileName = currentProfileName;
            } else {
                action = StoreAction.ADD_NEW;
            }
        }

        public ToolModel getToolModel() {
            return ProfilesController.this.getToolModel();
        }

        public String getProfileName() {
            return (action == StoreAction.OVERRIDE) ? existingProfileName : newProfileName;
        }

        @EditorConfig(label = "Action", index = 0, editor = "net.sf.filePiper.gui.ProfileNameVisibilityEditor")
        public StoreAction getAction() {
            return action;
        }

        public void setAction(StoreAction act) {
            action = act;
        }

        @EditorConfig(label = "Profile Name", editor = "net.sf.filePiper.gui.ProfilesComboEditor")
        public String getExistingProfileName() {
            ProfilesComboEditor.class.getName();
            return existingProfileName;
        }

        public void setExistingProfileName(String profileName) {
            existingProfileName = profileName;
        }

        @EditorConfig(label = "New Profile Name")
        public String getNewProfileName() {
            return newProfileName;
        }

        public void setNewProfileName(String profileName) {
            newProfileName = profileName;
        }
    }

    public void beforeMnemonicAssignment(ActionRepository repo) {
    }

    public void menubarCreated(ActionRepository repo, JMenuBar menubar) {
    }

    public void toolbarCreated(ActionRepository repo, JToolBar toolbar) {
        int insertIndex = toolbar.getComponentCount() - 4;
        toolbar.setBorder(new PartialLineBorder(new Color(153, 153, 153), PartialLineBorder.BOTTOM, 2));
        toolbar.add(new JLabel("Current Profile:"), insertIndex);
        toolbar.add(Box.createHorizontalStrut(4), insertIndex + 1);
        pipelineCombo = new JComboBox(mainModel.getStoredSettings());
        pipelineCombo.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    checkActionStates();
                    refreshProfile();
                }
            }
        });
        toolbar.add(pipelineCombo, insertIndex + 2);
        toolbar.add(Box.createHorizontalGlue());
    }
}
