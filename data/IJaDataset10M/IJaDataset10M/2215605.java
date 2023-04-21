package org.openremote.modeler.client.widget;

import java.util.List;
import java.util.Set;
import org.openremote.modeler.client.event.DoubleClickEvent;
import org.openremote.modeler.client.event.PropertyEditEvent;
import org.openremote.modeler.client.event.SubmitEvent;
import org.openremote.modeler.client.icon.Icons;
import org.openremote.modeler.client.listener.SubmitListener;
import org.openremote.modeler.client.model.TreeFolderBean;
import org.openremote.modeler.client.proxy.BeanModelDataBase;
import org.openremote.modeler.client.proxy.ConfigCategoryBeanModelProxy;
import org.openremote.modeler.client.proxy.DeviceBeanModelProxy;
import org.openremote.modeler.client.proxy.DeviceCommandBeanModelProxy;
import org.openremote.modeler.client.proxy.DeviceMacroBeanModelProxy;
import org.openremote.modeler.client.proxy.TemplateProxy;
import org.openremote.modeler.client.rpc.AsyncSuccessCallback;
import org.openremote.modeler.client.utils.DeviceBeanModelTable;
import org.openremote.modeler.client.utils.DeviceMacroBeanModelTable;
import org.openremote.modeler.client.utils.PropertyEditableFactory;
import org.openremote.modeler.client.utils.DeviceBeanModelTable.DeviceInsertListener;
import org.openremote.modeler.client.utils.DeviceMacroBeanModelTable.DeviceMacroInsertListener;
import org.openremote.modeler.client.widget.buildingmodeler.ControllerConfigTabItem;
import org.openremote.modeler.client.widget.uidesigner.ScreenPanel;
import org.openremote.modeler.client.widget.uidesigner.ScreenTab;
import org.openremote.modeler.client.widget.uidesigner.TemplatePanel;
import org.openremote.modeler.domain.CommandDelay;
import org.openremote.modeler.domain.ConfigCategory;
import org.openremote.modeler.domain.Device;
import org.openremote.modeler.domain.DeviceCommand;
import org.openremote.modeler.domain.DeviceCommandRef;
import org.openremote.modeler.domain.DeviceMacro;
import org.openremote.modeler.domain.GroupRef;
import org.openremote.modeler.domain.Panel;
import org.openremote.modeler.domain.ScreenPair;
import org.openremote.modeler.domain.ScreenPairRef;
import org.openremote.modeler.domain.Sensor;
import org.openremote.modeler.domain.Slider;
import org.openremote.modeler.domain.Switch;
import org.openremote.modeler.domain.Template;
import org.openremote.modeler.domain.UICommand;
import org.openremote.modeler.domain.component.UIButton;
import org.openremote.modeler.domain.component.UIGrid;
import org.openremote.modeler.domain.component.UIImage;
import org.openremote.modeler.domain.component.UILabel;
import org.openremote.modeler.domain.component.UISlider;
import org.openremote.modeler.domain.component.UISwitch;
import org.openremote.modeler.domain.component.UITabbar;
import org.openremote.modeler.domain.component.UITabbarItem;
import com.extjs.gxt.ui.client.data.BaseTreeLoader;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.LoadEvent;
import com.extjs.gxt.ui.client.data.ModelIconProvider;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.data.TreeLoader;
import com.extjs.gxt.ui.client.event.LoadListener;
import com.extjs.gxt.ui.client.event.TreePanelEvent;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

/**
 * The Class is used for create tree.
 */
public class TreePanelBuilder {

    /**
    * Not be instantiated.
    */
    private TreePanelBuilder() {
    }

    /** The Constant icon. */
    private static final Icons ICON = GWT.create(Icons.class);

    /** The device command treestore. */
    private static TreeStore<BeanModel> deviceTreeStore = null;

    private static TreeStore<BeanModel> deviceAndCmdTreeStore = null;

    /** The macro tree store. */
    private static TreeStore<BeanModel> macroTreeStore = null;

    private static TreeStore<BeanModel> widgetTreeStore = null;

    private static TreeStore<BeanModel> panelTreeStore = null;

    private static TreeStore<BeanModel> controllerConfigCategoryTreeStore = null;

    private static TreeStore<BeanModel> templateTreeStore = null;

    /**
    * Builds a device command tree.
    * 
    * @return the a new device command tree
    */
    public static TreePanel<BeanModel> buildDeviceCommandTree() {
        RpcProxy<List<BeanModel>> loadDeviceRPCProxy = new RpcProxy<List<BeanModel>>() {

            @Override
            protected void load(Object o, final AsyncCallback<List<BeanModel>> listAsyncCallback) {
                DeviceBeanModelProxy.loadDeviceAndCommand((BeanModel) o, new AsyncSuccessCallback<List<BeanModel>>() {

                    public void onSuccess(List<BeanModel> result) {
                        listAsyncCallback.onSuccess(result);
                    }
                });
            }
        };
        TreeLoader<BeanModel> loadDeviceTreeLoader = new BaseTreeLoader<BeanModel>(loadDeviceRPCProxy) {

            @Override
            public boolean hasChildren(BeanModel beanModel) {
                if (beanModel.getBean() instanceof Device) {
                    return true;
                }
                return false;
            }
        };
        deviceAndCmdTreeStore = new TreeStore<BeanModel>(loadDeviceTreeLoader);
        final TreePanel<BeanModel> tree = new TreePanel<BeanModel>(deviceAndCmdTreeStore) {

            @Override
            protected void afterRender() {
                super.afterRender();
                mask("Loading...");
                removeStyleName("x-masked");
            }
        };
        tree.setBorders(false);
        tree.setStateful(true);
        tree.setDisplayProperty("displayName");
        tree.setStyleAttribute("overflow", "auto");
        tree.setHeight("100%");
        tree.setIconProvider(new ModelIconProvider<BeanModel>() {

            public AbstractImagePrototype getIcon(BeanModel thisModel) {
                if (thisModel.getBean() instanceof DeviceCommand) {
                    return ICON.deviceCmd();
                } else if (thisModel.getBean() instanceof Device) {
                    return ICON.device();
                } else {
                    return ICON.folder();
                }
            }
        });
        return tree;
    }

    public static TreePanel<BeanModel> buildDeviceTree() {
        if (deviceTreeStore == null) {
            RpcProxy<List<BeanModel>> loadDeviceRPCProxy = new RpcProxy<List<BeanModel>>() {

                @Override
                protected void load(Object o, final AsyncCallback<List<BeanModel>> listAsyncCallback) {
                    DeviceBeanModelProxy.loadDevice((BeanModel) o, new AsyncSuccessCallback<List<BeanModel>>() {

                        public void onSuccess(List<BeanModel> result) {
                            listAsyncCallback.onSuccess(result);
                        }
                    });
                }
            };
            final TreeLoader<BeanModel> loadDeviceTreeLoader = new BaseTreeLoader<BeanModel>(loadDeviceRPCProxy) {

                @Override
                public boolean hasChildren(BeanModel beanModel) {
                    if (beanModel.getBean() instanceof DeviceCommand || beanModel.getBean() instanceof UICommand) {
                        return false;
                    }
                    return true;
                }
            };
            deviceTreeStore = new TreeStore<BeanModel>(loadDeviceTreeLoader);
        }
        final TreePanel<BeanModel> tree = new TreePanel<BeanModel>(deviceTreeStore) {

            @SuppressWarnings("unchecked")
            @Override
            protected void onDoubleClick(TreePanelEvent tpe) {
                super.onDoubleClick(tpe);
                this.fireEvent(DoubleClickEvent.DOUBLECLICK, new DoubleClickEvent());
            }

            @Override
            protected void afterRender() {
                super.afterRender();
                mask("Loading devices...");
                removeStyleName("x-masked");
            }
        };
        ((DeviceBeanModelTable) BeanModelDataBase.deviceTable).addDeviceInsertListener(new DeviceInsertListener<BeanModel>() {

            @Override
            public void handleInsert(BeanModel beanModel) {
                if (beanModel != null && beanModel.getBean() instanceof Device) {
                    if (deviceTreeStore.contains(beanModel)) {
                        tree.getStore().removeAll(beanModel);
                        tree.getStore().remove(beanModel);
                    }
                    deviceTreeStore.add(beanModel, false);
                    tree.getSelectionModel().select(beanModel, true);
                    tree.getStore().getLoader().load();
                }
            }
        });
        tree.setBorders(false);
        tree.setStateful(true);
        tree.setDisplayProperty("displayName");
        tree.setStyleAttribute("overflow", "auto");
        tree.setHeight("100%");
        tree.setIconProvider(new ModelIconProvider<BeanModel>() {

            public AbstractImagePrototype getIcon(BeanModel thisModel) {
                if (thisModel.getBean() instanceof DeviceCommand) {
                    return ICON.deviceCmd();
                } else if (thisModel.getBean() instanceof Device) {
                    return ICON.device();
                } else if (thisModel.getBean() instanceof Sensor) {
                    return ICON.sensorIcon();
                } else if (thisModel.getBean() instanceof Switch) {
                    return ICON.switchIcon();
                } else if (thisModel.getBean() instanceof Slider) {
                    return ICON.sliderIcon();
                } else if (thisModel.getBean() instanceof UICommand) {
                    return ICON.deviceCmd();
                } else {
                    return ICON.folder();
                }
            }
        });
        return tree;
    }

    public static TreePanel<BeanModel> buildCommandTree(final Device device, final BeanModel selectedCommandModel) {
        RpcProxy<List<BeanModel>> loadDeviceRPCProxy = new RpcProxy<List<BeanModel>>() {

            @Override
            protected void load(Object o, final AsyncCallback<List<BeanModel>> listAsyncCallback) {
                DeviceCommandBeanModelProxy.loadDeviceCmdFromDevice(device, new AsyncSuccessCallback<List<DeviceCommand>>() {

                    @Override
                    public void onSuccess(List<DeviceCommand> result) {
                        listAsyncCallback.onSuccess(DeviceCommand.createModels(result));
                    }
                });
            }
        };
        TreeLoader<BeanModel> loadDeviceTreeLoader = new BaseTreeLoader<BeanModel>(loadDeviceRPCProxy) {

            @Override
            public boolean hasChildren(BeanModel beanModel) {
                if (beanModel.getBean() instanceof Device) {
                    return true;
                }
                return false;
            }
        };
        TreeStore<BeanModel> commandTree = new TreeStore<BeanModel>(loadDeviceTreeLoader);
        final TreePanel<BeanModel> tree = new TreePanel<BeanModel>(commandTree);
        loadDeviceTreeLoader.addLoadListener(new LoadListener() {

            public void loaderLoad(LoadEvent le) {
                super.loaderLoad(le);
                if (selectedCommandModel != null) {
                    tree.getSelectionModel().select(selectedCommandModel, false);
                }
            }
        });
        tree.setBorders(false);
        tree.setStateful(true);
        tree.setDisplayProperty("displayName");
        tree.setStyleAttribute("overflow", "auto");
        tree.setHeight("100%");
        tree.setIconProvider(new ModelIconProvider<BeanModel>() {

            public AbstractImagePrototype getIcon(BeanModel thisModel) {
                if (thisModel.getBean() instanceof DeviceCommand) {
                    return ICON.deviceCmd();
                } else if (thisModel.getBean() instanceof Device) {
                    return ICON.device();
                } else {
                    return ICON.folder();
                }
            }
        });
        return tree;
    }

    /**
    * Builds a new macro tree.
    * 
    * @return a new macro tree
    */
    public static TreePanel<BeanModel> buildMacroTree() {
        if (macroTreeStore == null) {
            RpcProxy<List<BeanModel>> loadDeviceMacroRPCProxy = new RpcProxy<List<BeanModel>>() {

                protected void load(Object o, final AsyncCallback<List<BeanModel>> listAsyncCallback) {
                    DeviceMacroBeanModelProxy.loadDeviceMaro((BeanModel) o, new AsyncSuccessCallback<List<BeanModel>>() {

                        public void onSuccess(List<BeanModel> result) {
                            listAsyncCallback.onSuccess(result);
                        }
                    });
                }
            };
            BaseTreeLoader<BeanModel> loadDeviceMacroTreeLoader = new BaseTreeLoader<BeanModel>(loadDeviceMacroRPCProxy) {

                @Override
                public boolean hasChildren(BeanModel beanModel) {
                    if (beanModel.getBean() instanceof DeviceMacro) {
                        return true;
                    }
                    return false;
                }
            };
            macroTreeStore = new TreeStore<BeanModel>(loadDeviceMacroTreeLoader);
        }
        final TreePanel<BeanModel> tree = new TreePanel<BeanModel>(macroTreeStore) {

            @SuppressWarnings("unchecked")
            @Override
            protected void onDoubleClick(TreePanelEvent tpe) {
                super.onDoubleClick(tpe);
                this.fireEvent(DoubleClickEvent.DOUBLECLICK, new DoubleClickEvent());
            }
        };
        ((DeviceMacroBeanModelTable) BeanModelDataBase.deviceMacroTable).addDeviceMacroInsertListener(new DeviceMacroInsertListener<BeanModel>() {

            @Override
            public void handleInsert(BeanModel beanModel) {
                if (beanModel != null && beanModel.getBean() instanceof DeviceMacro) {
                    if (macroTreeStore.contains(beanModel)) {
                        tree.getStore().removeAll(beanModel);
                        tree.getStore().remove(beanModel);
                    }
                    macroTreeStore.add(beanModel, false);
                    tree.getSelectionModel().select(beanModel, true);
                    tree.getStore().getLoader().load();
                }
            }
        });
        tree.setStateful(true);
        tree.setBorders(false);
        tree.setHeight("100%");
        tree.setDisplayProperty("displayName");
        tree.setStyleAttribute("overflow", "auto");
        tree.setIconProvider(new ModelIconProvider<BeanModel>() {

            public AbstractImagePrototype getIcon(BeanModel thisModel) {
                if (thisModel.getBean() instanceof DeviceMacro) {
                    return ICON.macroIcon();
                } else if (thisModel.getBean() instanceof DeviceCommandRef) {
                    return ICON.deviceCmd();
                } else if (thisModel.getBean() instanceof CommandDelay) {
                    return ICON.delayIcon();
                } else {
                    return ICON.macroIcon();
                }
            }
        });
        return tree;
    }

    /**
    * Builds the widget tree, it contain all kind's of component.
    * 
    * @return the tree panel< bean model>
    */
    public static TreePanel<BeanModel> buildWidgetTree() {
        if (widgetTreeStore == null) {
            widgetTreeStore = new TreeStore<BeanModel>();
        }
        TreePanel<BeanModel> widgetTree = new TreePanel<BeanModel>(widgetTreeStore);
        widgetTree.setStateful(true);
        widgetTree.setBorders(false);
        widgetTree.setHeight("100%");
        widgetTree.setDisplayProperty("name");
        widgetTree.setStyleAttribute("overflow", "auto");
        widgetTreeStore.add(new UIGrid().getBeanModel(), true);
        widgetTreeStore.add(new UILabel().getBeanModel(), true);
        widgetTreeStore.add(new UIImage().getBeanModel(), true);
        widgetTreeStore.add(new UIButton().getBeanModel(), true);
        widgetTreeStore.add(new UISwitch().getBeanModel(), true);
        widgetTreeStore.add(new UISlider().getBeanModel(), true);
        widgetTreeStore.add(new UITabbar().getBeanModel(), true);
        widgetTreeStore.add(new UITabbarItem().getBeanModel(), true);
        widgetTree.setIconProvider(new ModelIconProvider<BeanModel>() {

            public AbstractImagePrototype getIcon(BeanModel thisModel) {
                if (thisModel.getBean() instanceof UIButton) {
                    return ICON.buttonIcon();
                } else if (thisModel.getBean() instanceof UISwitch) {
                    return ICON.switchIcon();
                } else if (thisModel.getBean() instanceof UILabel) {
                    return ICON.labelIcon();
                } else if (thisModel.getBean() instanceof UIImage) {
                    return ICON.imageIcon();
                } else if (thisModel.getBean() instanceof UISlider) {
                    return ICON.sliderIcon();
                } else if (thisModel.getBean() instanceof UIGrid) {
                    return ICON.gridIcon();
                } else if (thisModel.getBean() instanceof UITabbar) {
                    return ICON.tabbarConfigIcon();
                } else if (thisModel.getBean() instanceof UITabbarItem) {
                    return ICON.tabbarItemIcon();
                } else {
                    return ICON.buttonIcon();
                }
            }
        });
        return widgetTree;
    }

    public static TreePanel<BeanModel> buildPanelTree(final ScreenPanel screenPanel) {
        if (panelTreeStore == null) {
            panelTreeStore = new TreeStore<BeanModel>();
        }
        TreePanel<BeanModel> panelTree = new TreePanel<BeanModel>(panelTreeStore) {

            @SuppressWarnings("unchecked")
            @Override
            protected void onClick(TreePanelEvent tpe) {
                super.onClick(tpe);
                BeanModel beanModel = this.getSelectionModel().getSelectedItem();
                if (beanModel != null && beanModel.getBean() instanceof ScreenPairRef) {
                    ScreenPair screen = ((ScreenPairRef) beanModel.getBean()).getScreen();
                    screen.setTouchPanelDefinition(((ScreenPairRef) beanModel.getBean()).getTouchPanelDefinition());
                    screen.setParentGroup(((ScreenPairRef) beanModel.getBean()).getGroup());
                    ScreenTab screenTabItem = screenPanel.getScreenItem();
                    if (screenTabItem != null) {
                        if (screen == screenTabItem.getScreenPair()) {
                            screenTabItem.updateTouchPanel();
                            screenTabItem.updateTabbarForScreenCanvas((ScreenPairRef) beanModel.getBean());
                        } else {
                            screenTabItem = new ScreenTab(screen);
                            screenTabItem.updateTabbarForScreenCanvas((ScreenPairRef) beanModel.getBean());
                            screenPanel.setScreenItem(screenTabItem);
                        }
                    } else {
                        screenTabItem = new ScreenTab(screen);
                        screenTabItem.updateTabbarForScreenCanvas((ScreenPairRef) beanModel.getBean());
                        screenPanel.setScreenItem(screenTabItem);
                    }
                    screenTabItem.updateScreenIndicator();
                }
                if (beanModel != null) {
                    this.fireEvent(PropertyEditEvent.PropertyEditEvent, new PropertyEditEvent(PropertyEditableFactory.getPropertyEditable(beanModel, this)));
                }
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void onDoubleClick(TreePanelEvent tpe) {
                super.onDoubleClick(tpe);
                this.fireEvent(DoubleClickEvent.DOUBLECLICK, new DoubleClickEvent());
            }

            @Override
            protected void afterRender() {
                super.afterRender();
                mask("Loading panels...");
                removeStyleName("x-masked");
            }
        };
        panelTree.setStateful(true);
        panelTree.setBorders(false);
        panelTree.setHeight("100%");
        panelTree.setDisplayProperty("displayName");
        panelTree.setStyleAttribute("overflow", "auto");
        panelTree.setIconProvider(new ModelIconProvider<BeanModel>() {

            public AbstractImagePrototype getIcon(BeanModel thisModel) {
                if (thisModel.getBean() instanceof Panel) {
                    return ICON.panelIcon();
                } else if (thisModel.getBean() instanceof GroupRef) {
                    return ICON.groupIcon();
                } else if (thisModel.getBean() instanceof ScreenPairRef) {
                    if (((ScreenPairRef) thisModel.getBean()).getScreen().getRefCount() > 1) {
                        return ICON.screenLinkIcon();
                    }
                    return ICON.screenIcon();
                } else {
                    return ICON.panelIcon();
                }
            }
        });
        return panelTree;
    }

    public static TreePanel<BeanModel> buildPanelTree(TreeStore<BeanModel> store) {
        TreePanel<BeanModel> panelTree = new TreePanel<BeanModel>(store);
        panelTree.setStateful(true);
        panelTree.setBorders(false);
        panelTree.setHeight("100%");
        panelTree.setDisplayProperty("displayName");
        panelTree.setStyleAttribute("overflow", "auto");
        panelTree.setIconProvider(new ModelIconProvider<BeanModel>() {

            public AbstractImagePrototype getIcon(BeanModel thisModel) {
                if (thisModel.getBean() instanceof Panel) {
                    return ICON.panelIcon();
                } else if (thisModel.getBean() instanceof GroupRef) {
                    return ICON.groupIcon();
                } else if (thisModel.getBean() instanceof ScreenPairRef) {
                    return ICON.screenIcon();
                } else {
                    return ICON.panelIcon();
                }
            }
        });
        return panelTree;
    }

    public static TreePanel<BeanModel> buildControllerConfigCategoryPanelTree(final TabPanel configTabPanel) {
        if (controllerConfigCategoryTreeStore == null) {
            controllerConfigCategoryTreeStore = new TreeStore<BeanModel>();
            ConfigCategoryBeanModelProxy.getAllCategory(new AsyncSuccessCallback<Set<ConfigCategory>>() {

                @Override
                public void onSuccess(Set<ConfigCategory> result) {
                    for (ConfigCategory category : result) {
                        controllerConfigCategoryTreeStore.add(category.getBeanModel(), false);
                    }
                }
            });
        }
        TreePanel<BeanModel> tree = new TreePanel<BeanModel>(controllerConfigCategoryTreeStore) {

            @Override
            public void onBrowserEvent(Event event) {
                if (event.getTypeInt() == Event.ONCLICK) {
                    BeanModel beanModel = this.getSelectionModel().getSelectedItem();
                    final ConfigCategory category = beanModel.getBean();
                    configTabPanel.removeAll();
                    ControllerConfigTabItem configTabItem = new ControllerConfigTabItem(category);
                    configTabPanel.add(configTabItem);
                    configTabItem.addListener(SubmitEvent.SUBMIT, new SubmitListener() {

                        @Override
                        public void afterSubmit(SubmitEvent be) {
                            configTabPanel.removeAll();
                            ControllerConfigTabItem configTabItem = new ControllerConfigTabItem(category);
                            configTabPanel.add(configTabItem);
                        }
                    });
                }
                super.onBrowserEvent(event);
            }
        };
        tree.setIconProvider(new ModelIconProvider<BeanModel>() {

            public AbstractImagePrototype getIcon(BeanModel thisModel) {
                return ICON.configIcon();
            }
        });
        tree.setStateful(true);
        tree.setBorders(false);
        tree.setHeight("100%");
        tree.setDisplayProperty("name");
        return tree;
    }

    public static TreePanel<BeanModel> buildTemplateTree(final TemplatePanel templatePanel) {
        TreeFolderBean privateTemplatesBean = new TreeFolderBean();
        privateTemplatesBean.setDisplayName("My private templates");
        TreeFolderBean publicTemplatesBean = new TreeFolderBean();
        publicTemplatesBean.setDisplayName("My public templates");
        RpcProxy<List<BeanModel>> loadTemplateRPCProxy = new RpcProxy<List<BeanModel>>() {

            @Override
            protected void load(Object loadConfig, final AsyncCallback<List<BeanModel>> callback) {
                if (loadConfig != null && loadConfig instanceof BeanModel) {
                    BeanModel model = (BeanModel) loadConfig;
                    if (model.getBean() instanceof TreeFolderBean) {
                        TreeFolderBean folderBean = model.getBean();
                        if (folderBean.getDisplayName().contains("Private")) {
                            TemplateProxy.getTemplates(true, new AsyncSuccessCallback<List<Template>>() {

                                @Override
                                public void onSuccess(List<Template> result) {
                                    callback.onSuccess(Template.createModels(result));
                                }
                            });
                        } else {
                            TemplateProxy.getTemplates(false, new AsyncSuccessCallback<List<Template>>() {

                                @Override
                                public void onSuccess(List<Template> result) {
                                    callback.onSuccess(Template.createModels(result));
                                }
                            });
                        }
                    }
                }
            }
        };
        TreeLoader<BeanModel> templateLoader = new BaseTreeLoader<BeanModel>(loadTemplateRPCProxy) {

            @Override
            public boolean hasChildren(BeanModel beanModel) {
                if (beanModel.getBean() instanceof TreeFolderBean) {
                    return true;
                }
                return false;
            }
        };
        if (templateTreeStore == null) {
            templateTreeStore = new TreeStore<BeanModel>(templateLoader);
        }
        templateTreeStore.add(privateTemplatesBean.getBeanModel(), false);
        templateTreeStore.add(publicTemplatesBean.getBeanModel(), false);
        TreePanel<BeanModel> tree = new TreePanel<BeanModel>(templateTreeStore) {

            @Override
            public void onBrowserEvent(Event event) {
                super.onBrowserEvent(event);
                if (event.getTypeInt() == Event.ONCLICK) {
                    BeanModel beanModel = this.getSelectionModel().getSelectedItem();
                    if (beanModel != null && beanModel.getBean() instanceof Template) {
                        Template template = beanModel.getBean();
                        templatePanel.setTemplateInEditing(template);
                    }
                }
            }
        };
        tree.setIconProvider(new ModelIconProvider<BeanModel>() {

            public AbstractImagePrototype getIcon(BeanModel thisModel) {
                if (thisModel.getBean() instanceof TreeFolderBean) {
                    return ICON.folder();
                }
                return ICON.templateIcon();
            }
        });
        tree.setStateful(true);
        tree.setBorders(false);
        tree.setHeight("100%");
        tree.setDisplayProperty("displayName");
        return tree;
    }

    public static TreePanel<BeanModel> buildDeviceContentTree(TreeStore<BeanModel> store) {
        TreePanel<BeanModel> deviceContentTree = new TreePanel<BeanModel>(store);
        deviceContentTree.setStateful(true);
        deviceContentTree.setBorders(false);
        deviceContentTree.setHeight("100%");
        deviceContentTree.setDisplayProperty("displayName");
        deviceContentTree.setStyleAttribute("overflow", "auto");
        deviceContentTree.setIconProvider(new ModelIconProvider<BeanModel>() {

            public AbstractImagePrototype getIcon(BeanModel thisModel) {
                if (thisModel.getBean() instanceof DeviceCommand) {
                    return ICON.deviceCmd();
                } else if (thisModel.getBean() instanceof Sensor) {
                    return ICON.sensorIcon();
                } else if (thisModel.getBean() instanceof Switch) {
                    return ICON.switchIcon();
                } else if (thisModel.getBean() instanceof Slider) {
                    return ICON.sliderIcon();
                } else {
                    return ICON.folder();
                }
            }
        });
        return deviceContentTree;
    }
}
