package org.makagiga.settings;

import static org.makagiga.commons.UI._;
import java.awt.Window;
import javax.swing.Action;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.makagiga.commons.*;
import org.makagiga.internetsearch.InternetSearchPlugin;
import org.makagiga.plugins.PluginInfo;
import org.makagiga.plugins.PluginInstaller;
import org.makagiga.plugins.PluginList;
import org.makagiga.plugins.PluginManager;
import org.makagiga.plugins.PluginOptions;
import org.makagiga.plugins.PluginType;

/** The "Plugins" dialog. */
public final class PluginSettings extends AbstractSettings<PluginSettings.PluginSettingsPage> {

    private ActionGroup actionGroup;

    /**
	 * Constructs a new "Plugins" dialog.
	 * @param parent The parent window
	 *
	 * @since 2.0
	 */
    public PluginSettings(final Window parent) {
        super(parent, _("Plugins"), "ui/misc", SIMPLE_DIALOG | URL_BUTTON | USER_BUTTON, "plugins");
        changeButton(getUserButton(), _("Install Plugin..."), "ui/newfile");
        changeButton(getURLButton(), _("Get More Plugins..."));
        createActions();
        InternetSearchPlugin.registerBuiltIn();
        for (PluginType i : PluginType.values()) addPage(new PluginSettingsPage(i));
        setToolBar(actionGroup.createToolBar());
        MTip tip = new MTip(getClass());
        tip.showNextTip(MTip.Visible.SELDOM);
        addSouth(tip);
        getTabs().addChangeListener(new ChangeListener() {

            public void stateChanged(final ChangeEvent e) {
                PluginList list = getSelectedList();
                if (list != null) {
                    list.clearSelection();
                    updateActions(null);
                    getTabs().requestFocusInWindow();
                }
            }
        });
        updateActions(null);
        setSize(800, 600);
    }

    @Override
    protected boolean onReject() {
        apply();
        return true;
    }

    @Override
    protected void onURLClick() {
        PluginDownloader.getMorePlugins(this, null);
    }

    @Override
    protected void onUserClick() {
        if (PluginInstaller.showInstallDialog(this)) setNeedRestart(true);
    }

    private void about() {
        PluginInfo info = getSelectedInfo();
        if (info == null) return;
        MAbout about = new MAbout(this, info.getIcon(), info.name.get(), info.shortDescription.get());
        about.setBugs(info.bugs.get());
        about.setCopyright(info.copyright.get());
        about.setHomePage(info.homePageURL.get());
        about.setLicense(info.licenseName.get(), info.licenseURL.get());
        about.setVersion(info.version.toString());
        about.pack();
        about.exec();
    }

    private void createActions() {
        actionGroup = new ActionGroup();
        if (Kiosk.actionSettings.get()) {
            actionGroup.add("configure", new MApplication.ConfigureAction() {

                @Override
                public void onAction() {
                    AppSettings.showPluginConfigDialog(PluginSettings.this, PluginSettings.this.getSelectedInfo());
                }
            });
            actionGroup.addSeparator();
        }
        MAction deleteAction = new MAction(_("Uninstall"), "ui/delete") {

            @Override
            public void onAction() {
                removePlugin();
            }
        };
        deleteAction.setHTMLHelp(_("Removes plugin from the disk.<br>Only external plugin can be removed."));
        actionGroup.add("delete", deleteAction);
        actionGroup.addSeparator();
        actionGroup.add("enableDisable", new MAction() {

            @Override
            public void onAction() {
                togglePlugin();
            }
        });
        actionGroup.addStretch();
        actionGroup.add("moveDown", new MAction(_("Move Down"), "ui/down") {

            @Override
            public void onAction() {
                PluginSettings.this.movePlugin(false);
            }
        });
        actionGroup.add("moveUp", new MAction(_("Move Up"), "ui/up") {

            @Override
            public void onAction() {
                PluginSettings.this.movePlugin(true);
            }
        });
        actionGroup.addSeparator();
        actionGroup.add("about", new MAction(_("About"), "ui/info") {

            @Override
            public void onAction() {
                about();
            }
        });
        ActionGroup.Item item = actionGroup.add("postPluginInfo", new MAction("Post plugin info to makagiga.sf.net (requires administrator password)") {

            @Override
            public void onAction() {
                PluginSettings.this.postPluginInfo();
            }
        });
        item.setVisibleInMenu(MLogger.developer.get());
        item.setVisibleInToolBar(false);
    }

    private PluginInfo getSelectedInfo() {
        return getSelectedList().getSelectedItem();
    }

    private PluginList getSelectedList() {
        return getTabs().getSelectedTab().list;
    }

    private void movePlugin(final boolean up) {
        PluginList list = getSelectedList();
        int oldIndex = list.getSelectedIndex();
        PluginInfo info = list.getSelectedItem();
        int newIndex;
        if (up) newIndex = oldIndex - 1; else newIndex = oldIndex + 1;
        PluginInfo oldInfo = list.getItemAt(newIndex);
        list.getDefaultModel().setElementAt(info, newIndex);
        list.getDefaultModel().setElementAt(oldInfo, oldIndex);
        list.setSelectedIndex(newIndex, true);
        if (up) info.priority.set(oldInfo.priority.get() + 1); else info.priority.set(oldInfo.priority.get() - 1);
        Config config = Config.getDefault();
        config.write("Plugin.priority." + info.getID(), info.priority.get());
        config.save();
    }

    private void postPluginInfo() {
        Config config = Config.getDefault();
        PluginInfo info = getSelectedInfo();
        String postHost = config.read("Plugin.host", AppInfo.www.get());
        MMainWindow.openURL(postHost + "/updateplugininfo.php?action=upload&" + "uuid={0}&" + "type={1}&" + "name={2}&" + "description={3}&" + "copyright={4}&" + "homepage={5}&" + "license={6}&" + "version={7}&" + "requires={8}", info.getID().replaceAll("\\{|\\}", ""), info.type.get(), info.name.get(), info.shortDescription.get(), info.copyright.get(), info.homePageURL.get(), info.licenseName.get(), info.version.get().toString(), info.requires.get().toString());
    }

    private void removePlugin() {
        PluginInfo info = getSelectedInfo();
        if (info == null) return;
        if (!info.canRemove()) return;
        if (!MMessage.customConfirm(PluginSettings.this, MIcon.stock("ui/delete"), MIcon.stock("ui/delete"), _("Uninstall"), null, null, _("Uninstall selected plugin?"), new Object[] { info })) return;
        Config config = Config.getDefault();
        config.write("Plugin.remove." + info.getID(), true);
        config.save();
        getSelectedList().removeItem(info);
        PluginManager.getInstance().unregister(info);
        setNeedRestart(true);
        updateActions(null);
    }

    private void togglePlugin() {
        PluginInfo info = getSelectedInfo();
        if (info == null) return;
        info.enabled.toggle();
        getSelectedList().repaint();
        final PluginManager pm = PluginManager.getInstance();
        if (info.enabled.get()) pm.register(info); else pm.unregister(info);
        Config.getDefault().write("Plugin.enabled." + info.getID(), info.enabled);
        setNeedRestart(true);
        updateActions(info);
    }

    private void updateActions(final PluginInfo info) {
        if (info == null) {
            actionGroup.setEnabled(false);
            MAction enableDisableAction = (MAction) actionGroup.getAction("enableDisable");
            enableDisableAction.setIcon("ui/minus");
            enableDisableAction.setName(_("Disable"));
        } else {
            actionGroup.setEnabled("about", true);
            if (Kiosk.actionSettings.get()) actionGroup.setEnabled("configure", info.getPluggable() instanceof PluginOptions);
            actionGroup.setEnabled("delete", info.canRemove());
            PluginList list = getSelectedList();
            int index = list.getSelectedIndex();
            actionGroup.setEnabled("moveDown", (index != -1) && (index < list.getItemCount() - 1));
            actionGroup.setEnabled("moveUp", (index != -1) && (index > 0));
            actionGroup.setEnabled("postPluginInfo", !info.isInternal());
            MAction enableDisableAction = (MAction) actionGroup.getAction("enableDisable");
            enableDisableAction.setEnabled(info.isCompatible());
            if (info.enabled.get()) {
                enableDisableAction.setIcon("ui/minus");
                enableDisableAction.setName(_("Disable"));
            } else {
                enableDisableAction.setIcon("ui/plus");
                enableDisableAction.setName(_("Enable"));
            }
        }
    }

    public final class PluginSettingsPage extends AbstractSettingsPage {

        private PluginList list;

        private PluginType type;

        /**
		 * @since 1.2
		 */
        public PluginSettingsPage(final PluginType type) {
            super(type.getText(), type.getIconName());
            this.type = type;
        }

        protected void onInit() {
            MLabel webStartWarning = PluginManager.createWebStartWarning();
            if (webStartWarning != null) add(webStartWarning);
            list = new PluginList(type) {

                @Override
                protected void onAction() {
                    switch(getActionType()) {
                        case ITEM_POPUP_MENU:
                            actionGroup.createMenu().showPopup(getActionEvent());
                            break;
                        case TRIGGER:
                            if (Kiosk.actionSettings.get()) {
                                Action configureAction = actionGroup.getAction("configure");
                                MAction.fire(configureAction, this);
                            }
                            break;
                    }
                }

                @Override
                protected void onSelect() {
                    updateActions(list.getSelectedItem());
                }
            };
            add(new MScrollPane(list));
        }

        protected void onOK() {
        }
    }
}
