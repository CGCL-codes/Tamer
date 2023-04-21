package de.schwarzrot.themeedit.app;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import org.springframework.context.ApplicationEvent;
import com.jgoodies.binding.PresentationModel;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;
import de.schwarzrot.app.domain.AccessMode;
import de.schwarzrot.app.domain.VideoPageFormat;
import de.schwarzrot.app.event.ConfigurationChangedEvent;
import de.schwarzrot.app.event.PopupHandler;
import de.schwarzrot.app.support.AbstractApplication;
import de.schwarzrot.app.support.ApplicationServiceProvider;
import de.schwarzrot.concurrent.ContextTask;
import de.schwarzrot.concurrent.ProgressPublisher;
import de.schwarzrot.concurrent.Task;
import de.schwarzrot.concurrent.TaskPerThreadExecutor;
import de.schwarzrot.concurrent.VContextTask;
import de.schwarzrot.data.support.EnumUtils;
import de.schwarzrot.dvd.theme.MenueDefinition;
import de.schwarzrot.dvd.theme.Theme;
import de.schwarzrot.dvd.theme.domain.ThemeElement;
import de.schwarzrot.dvd.theme.domain.data.MenueElementType;
import de.schwarzrot.dvd.theme.domain.data.MenuePageType;
import de.schwarzrot.dvd.theme.event.DVDSkinChangedEvent;
import de.schwarzrot.dvd.theme.event.DVDThemeChangedEvent;
import de.schwarzrot.dvd.theme.event.DVDThemePageChangedEvent;
import de.schwarzrot.dvd.theme.support.AbstractItemBase;
import de.schwarzrot.dvd.theme.support.ExchangeHandler;
import de.schwarzrot.themeedit.app.view.ComposerPane;
import de.schwarzrot.themeedit.app.view.ItemSettingsEditor;
import de.schwarzrot.themeedit.domain.TEConfig;
import de.schwarzrot.ui.action.ActionContextEvent;
import de.schwarzrot.ui.action.ActionHandler;
import de.schwarzrot.ui.action.ApplicationActionHandler;
import de.schwarzrot.ui.action.support.AbstractActionCallback;
import de.schwarzrot.ui.action.support.AbstractActionContextCallback;
import de.schwarzrot.ui.action.support.AbstractActionHandler;
import de.schwarzrot.ui.action.support.ApplicationCommandFactory;
import de.schwarzrot.ui.action.support.SubmitHandler;
import de.schwarzrot.ui.control.MenuepageSelector;
import de.schwarzrot.ui.control.ThemeSelectionDialog;
import de.schwarzrot.ui.control.support.AbstractDetailsView;
import de.schwarzrot.ui.control.support.AbstractEditor;
import de.schwarzrot.ui.image.ImageFactory;
import de.schwarzrot.ui.service.CommandFactoryBuilder;
import de.schwarzrot.ui.service.FormComponentFactory;
import de.schwarzrot.ui.support.AbstractStatusBar;

/**
 * controller to manage creation and change of {@code Theme}s. Uses
 * {@code ComposerPane} as view. On empty startup, a create action has to be
 * selected, either via context menue, toolbar or main menue. That will activate
 * the creation mouse handler of {@code ComposerPane}. Using drag operation, an
 * area will be spanned, that on button release will be the space for the new
 * created element.
 * <p>
 * Any element can be moved around using drag and drop and the context menue
 * will change for each element. The size of an element can be change via
 * settings dialog.
 * <p>
 * On every user action, the content of the menue page is checked, so that
 * unique elements can't be added multiple times.
 * <p>
 * Uses {@code MenueDefinition} and {@code ApplicationEventPublisher} to
 * communicate with SkinEditor.
 * 
 * @author <a href="mailto:rmantey@users.sourceforge.net">Reinhard Mantey</a>
 * 
 */
public class EditorManager extends AbstractApplication<TEConfig> implements SubmitHandler<ThemeElement<?>>, PopupHandler {

    private static final long serialVersionUID = 713L;

    private static final String NEW_NAME = "_new_theme";

    private static ImageFactory imgFactory;

    public class MyStatusBar extends AbstractStatusBar {

        private static final long serialVersionUID = 713L;

        @Override
        protected void checkExtends() {
        }

        @Override
        protected void updateExtends() {
        }
    }

    public enum ViewFormatCommand {

        SET_4x3_ASPECT, SET_16x9_ASPECT
    }

    class ConfigEditor extends AbstractEditor<TEConfig> {

        private static final long serialVersionUID = 713L;

        public ConfigEditor(TEConfig cfg) {
            super(cfg, false);
        }

        @Override
        protected JComponent buildPanel() {
            FormLayout layout = new FormLayout("left:max(100dlu;pref), 3dlu, 100dlu:grow");
            DefaultFormBuilder builder = new DefaultFormBuilder(layout);
            String prefix = TEConfig.class.getSimpleName() + ".";
            PresentationModel<TEConfig> pm = getModel();
            JComponent msgBox = new JScrollPane(componentFactory.createTextArea(pm.getBufferedModel(TEConfig.FLD_DEF_DESCRIPTION)));
            msgBox.setPreferredSize(new Dimension(100, 150));
            builder.setDefaultDialogBorder();
            builder.appendSeparator(msgSource.getMessage(prefix + "defaults", null, prefix + "defaults", null));
            builder.append(msgSource.getMessage(prefix + "def.header.label", null, prefix + "def.header.label", null), componentFactory.createTextField(pm.getBufferedModel(TEConfig.FLD_DEF_HEADER)));
            builder.append(msgSource.getMessage(prefix + "def.title.label", null, prefix + "def.title.label", null), componentFactory.createTextField(pm.getBufferedModel(TEConfig.FLD_DEF_TITLE)));
            builder.append(msgSource.getMessage(prefix + "def.item.label", null, prefix + "def.item.label", null), componentFactory.createTextField(pm.getBufferedModel(TEConfig.FLD_DEF_ITEM_TEXT)));
            builder.append(msgSource.getMessage(prefix + "def.descr.label", null, prefix + "def.descr.label", null), msgBox);
            builder.append(msgSource.getMessage(prefix + "def.jobimage.label", null, prefix + "def.jobimage.label", null), new JLabel(msgSource.getMessage(prefix + "def.recimage.label", null, prefix + "def.recimage.label", null)));
            builder.append(componentFactory.createImageChooser(pm.getBufferedModel(TEConfig.FLD_DEF_JOB_IMAGE), this), componentFactory.createImageChooser(pm.getBufferedModel(TEConfig.FLD_DEF_REC_IMAGE), this));
            return builder.getPanel();
        }
    }

    class PreviewActionHandler extends AbstractActionHandler implements ApplicationActionHandler {

        public PreviewActionHandler(String name) {
            super(name);
        }

        @Override
        public JPopupMenu createPopup(Object context) {
            if (context != null) return createPopup(context, new Enum<?>[][] { popCtxCmds });
            return createPopup(null, new Enum<?>[][] { popCmds });
        }

        @Override
        public JMenu createSubMenu(String cmd) {
            JMenu menu = null;
            if (facBuilder == null) facBuilder = ApplicationServiceProvider.getService(CommandFactoryBuilder.class);
            ApplicationCommandFactory acf = facBuilder.createApplicationCommandFactory(new ActionHandler[] { this });
            if (acf == null) return null;
            if (cmd.compareTo(Command.VIEW_SEL_SUB.name()) == 0) {
                menu = acf.createMenu(getName() + "." + cmd, switchCmds);
            } else if (cmd.compareTo(Command.VIEW_FORMAT_SUB.name()) == 0) {
                menu = acf.createMenu(getName() + "." + cmd, ViewFormatCommand.values());
            } else if (cmd.compareTo(Command.NEW_BASE_SUB.name()) == 0) {
                menu = acf.createMenu(getName() + "." + cmd, ncBase);
            } else if (cmd.compareTo(Command.NEW_IMAGE_SUB.name()) == 0) {
                menu = acf.createMenu(getName() + "." + cmd, ncImage);
            } else if (cmd.compareTo(Command.NEW_BUTTON_SUB.name()) == 0) {
                menu = acf.createMenu(getName() + "." + cmd, ncButton);
            } else if (cmd.compareTo(Command.EDIT_ADD_SUB.name()) == 0) {
                menu = acf.createMenu(getName() + "." + cmd, new Enum<?>[][] { ncBase, ncImage, ncButton });
            }
            return menu;
        }

        @Override
        public Enum<?>[] getCommands() {
            return mbCmds;
        }

        @Override
        public Enum<?>[] getExtraToolBarCommands() {
            return tbXtra;
        }

        @Override
        public Enum<?>[] getPopupCommands(Object context) {
            return popCmds;
        }

        @Override
        public Enum<?>[] getToolBarCommands() {
            return tbCmd;
        }

        @Override
        public void init() {
            setupAction(getName(), Command.FILE_NEW, new AbstractActionCallback() {

                @Override
                public void actionPerformed(ActionEvent ae) {
                    doNew();
                }
            }, KeyEvent.VK_N, AccessMode.APP_MODIFY);
            setupAction(getName(), Command.FILE_OPEN, new AbstractActionCallback() {

                @Override
                public void actionPerformed(ActionEvent ae) {
                    doOpen();
                }
            }, KeyEvent.VK_N, AccessMode.APP_READ);
            setupAction(getName(), Command.FILE_SAVE, new AbstractActionCallback() {

                @Override
                public void actionPerformed(ActionEvent ae) {
                    doSave();
                }
            }, KeyEvent.VK_N, AccessMode.APP_MODIFY);
            setupAction(getName(), Command.FILE_SAVE_AS, new AbstractActionCallback() {

                @Override
                public void actionPerformed(ActionEvent ae) {
                    doSaveAs();
                }
            }, KeyEvent.VK_N, AccessMode.APP_MODIFY);
            setupAction(getName(), Command.FILE_EXPORT, new AbstractActionCallback() {

                @Override
                public void actionPerformed(ActionEvent ae) {
                    doExport();
                }
            }, KeyEvent.VK_N, AccessMode.APP_MODIFY);
            setupAction(getName(), Command.FILE_IMPORT, new AbstractActionCallback() {

                @Override
                public void actionPerformed(ActionEvent ae) {
                    doImport();
                }
            }, KeyEvent.VK_N, AccessMode.APP_MODIFY);
            setupAction(getName(), Command.FILE_IMG_EXPORT, new AbstractActionCallback() {

                @Override
                public void actionPerformed(ActionEvent ae) {
                    doSaveImage(ae);
                }
            }, KeyEvent.VK_N, AccessMode.APP_MODIFY);
            setupAction(getName(), Command.VIEW_REFRESH, new AbstractActionCallback() {

                @Override
                public void actionPerformed(ActionEvent ae) {
                    System.out.println("size: " + content.getSize());
                    activePane.repaint();
                }
            }, KeyEvent.VK_N, AccessMode.APP_ANY);
            setupAction(getName(), Command.VIEW_CHANGE_GRID, new AbstractActionCallback() {

                @Override
                public void actionPerformed(ActionEvent ae) {
                    doGridConfig(ae);
                }
            }, KeyEvent.VK_N, AccessMode.APP_ANY);
            setupAction(getName(), Command.VIEW_GRID_TOGGLE, new AbstractActionCallback() {

                @Override
                public void actionPerformed(ActionEvent ae) {
                    doGridToggle(ae);
                }
            }, KeyEvent.VK_N, AccessMode.APP_ANY);
            setupAction(getName(), ViewFormatCommand.SET_4x3_ASPECT, new AbstractActionContextCallback(VideoPageFormat.STANDARD) {

                @Override
                public void actionPerformed(ActionEvent ae) {
                    doChangeAspect(new ActionContextEvent(ae, getContext()));
                }
            }, KeyEvent.VK_N, AccessMode.APP_ANY);
            setupAction(getName(), ViewFormatCommand.SET_16x9_ASPECT, new AbstractActionContextCallback(VideoPageFormat.WIDESCREEN) {

                @Override
                public void actionPerformed(ActionEvent ae) {
                    doChangeAspect(new ActionContextEvent(ae, getContext()));
                }
            }, KeyEvent.VK_N, AccessMode.APP_ANY);
            setupAction(getName(), Command.EDIT_CLEAR, new AbstractActionCallback() {

                @Override
                public void actionPerformed(ActionEvent ae) {
                    doClear(ae);
                }
            }, KeyEvent.VK_N, AccessMode.APP_MODIFY);
            setupAction(getName(), Command.EDIT_SHOW_ORDER, new AbstractActionCallback() {

                @Override
                public void actionPerformed(ActionEvent ae) {
                    doShowOrder(ae);
                }
            }, KeyEvent.VK_N, AccessMode.APP_ANY);
            setupAction(getName(), Command.EDIT_SKIN_PROPAGATE, new AbstractActionContextCallback(Command.EDIT_SKIN_PROPAGATE) {

                @Override
                public void actionPerformed(final ActionEvent ae) {
                    new TaskPerThreadExecutor().execute(new Task() {

                        @Override
                        public void run() {
                            doPropagate(new ActionContextEvent(ae, getContext()));
                        }
                    });
                }
            }, KeyEvent.VK_N, AccessMode.APP_MODIFY);
            setupAction(getName(), Command.EDIT_THEME_PROPAGATE, new AbstractActionContextCallback(Command.EDIT_THEME_PROPAGATE) {

                @Override
                public void actionPerformed(final ActionEvent ae) {
                    new TaskPerThreadExecutor().execute(new Task() {

                        @Override
                        public void run() {
                            doPropagate(new ActionContextEvent(ae, getContext()));
                        }
                    });
                }
            }, KeyEvent.VK_N, AccessMode.APP_MODIFY);
            int i = 0;
            switchCmds = new String[MenuePageType.values().length];
            for (MenuePageType cur : MenuePageType.values()) {
                String actionKey = "SWITCH_" + cur.name();
                switchCmds[i++] = actionKey;
                setupAction(getName(), actionKey, new AbstractActionContextCallback(cur) {

                    @Override
                    public void actionPerformed(ActionEvent ae) {
                        menueDef.setCurrentPage((MenuePageType) getContext());
                    }
                }, KeyEvent.VK_N, AccessMode.APP_MODIFY);
            }
            setupAction(getName(), Theme.NewCommands.NEW_TITLE_ITEM, new AbstractActionContextCallback(MenueElementType.TITLE_ITEM) {

                @Override
                public void actionPerformed(ActionEvent ae) {
                    activePane.doAddElement(new ActionContextEvent(ae, getContext()));
                }
            }, 0, AccessMode.APP_MODIFY);
            setupAction(getName(), Theme.NewCommands.NEW_HEADER_ITEM, new AbstractActionContextCallback(MenueElementType.HEADER_ITEM) {

                @Override
                public void actionPerformed(ActionEvent ae) {
                    activePane.doAddElement(new ActionContextEvent(ae, getContext()));
                }
            }, 0, AccessMode.APP_MODIFY);
            setupAction(getName(), Theme.NewCommands.NEW_SKIN_IMAGE, new AbstractActionContextCallback(MenueElementType.SKIN_IMAGE) {

                @Override
                public void actionPerformed(ActionEvent ae) {
                    activePane.doAddElement(new ActionContextEvent(ae, getContext()));
                }
            }, 0, AccessMode.APP_MODIFY);
            setupAction(getName(), Theme.NewCommands.NEW_THEME_IMAGE, new AbstractActionContextCallback(MenueElementType.THEME_IMAGE) {

                @Override
                public void actionPerformed(ActionEvent ae) {
                    activePane.doAddElement(new ActionContextEvent(ae, getContext()));
                }
            }, 0, AccessMode.APP_MODIFY);
            setupAction(getName(), Theme.NewCommands.NEW_JOB_IMAGE, new AbstractActionContextCallback(MenueElementType.JOB_IMAGE) {

                @Override
                public void actionPerformed(ActionEvent ae) {
                    activePane.doAddElement(new ActionContextEvent(ae, getContext()));
                }
            }, 0, AccessMode.APP_MODIFY);
            setupAction(getName(), Theme.NewCommands.NEW_REC_IMAGE, new AbstractActionContextCallback(MenueElementType.REC_IMAGE) {

                @Override
                public void actionPerformed(ActionEvent ae) {
                    activePane.doAddElement(new ActionContextEvent(ae, getContext()));
                }
            }, 0, AccessMode.APP_MODIFY);
            setupAction(getName(), Theme.NewCommands.NEW_ITEM, new AbstractActionContextCallback(MenueElementType.ITEM) {

                @Override
                public void actionPerformed(ActionEvent ae) {
                    activePane.doAddElement(new ActionContextEvent(ae, getContext()));
                }
            }, 0, AccessMode.APP_MODIFY);
            setupAction(getName(), Theme.NewCommands.NEW_ITEM_AREA, new AbstractActionContextCallback(MenueElementType.ITEM_AREA) {

                @Override
                public void actionPerformed(ActionEvent ae) {
                    activePane.doAddElement(new ActionContextEvent(ae, getContext()));
                }
            }, 0, AccessMode.APP_MODIFY);
            setupAction(getName(), Theme.NewCommands.NEW_DESCRIPTION, new AbstractActionContextCallback(MenueElementType.DESCRIPTION) {

                @Override
                public void actionPerformed(ActionEvent ae) {
                    activePane.doAddElement(new ActionContextEvent(ae, getContext()));
                }
            }, 0, AccessMode.APP_MODIFY);
            setupAction(getName(), Theme.NewCommands.NEW_MAINMENUE_BUTTON, new AbstractActionContextCallback(MenueElementType.MAINMENUE_BUTTON) {

                @Override
                public void actionPerformed(ActionEvent ae) {
                    activePane.doAddElement(new ActionContextEvent(ae, getContext()));
                }
            }, 0, AccessMode.APP_MODIFY);
            setupAction(getName(), Theme.NewCommands.NEW_NEXT_BUTTON, new AbstractActionContextCallback(MenueElementType.NEXT_BUTTON) {

                @Override
                public void actionPerformed(ActionEvent ae) {
                    activePane.doAddElement(new ActionContextEvent(ae, getContext()));
                }
            }, 0, AccessMode.APP_MODIFY);
            setupAction(getName(), Theme.NewCommands.NEW_PREVIOUS_BUTTON, new AbstractActionContextCallback(MenueElementType.PREVIOUS_BUTTON) {

                @Override
                public void actionPerformed(ActionEvent ae) {
                    activePane.doAddElement(new ActionContextEvent(ae, getContext()));
                }
            }, 0, AccessMode.APP_MODIFY);
            setupAction(getName(), Theme.NewCommands.NEW_PLAY_BUTTON, new AbstractActionContextCallback(MenueElementType.PLAY_BUTTON) {

                @Override
                public void actionPerformed(ActionEvent ae) {
                    activePane.doAddElement(new ActionContextEvent(ae, getContext()));
                }
            }, 0, AccessMode.APP_MODIFY);
            setupAction(getName(), Theme.NewCommands.NEW_PLAY_ALL_BUTTON, new AbstractActionContextCallback(MenueElementType.PLAY_ALL_BUTTON) {

                @Override
                public void actionPerformed(ActionEvent ae) {
                    activePane.doAddElement(new ActionContextEvent(ae, getContext()));
                }
            }, 0, AccessMode.APP_MODIFY);
            setupAction(getName(), Theme.NewCommands.NEW_BONUSMENUE_BUTTON, new AbstractActionContextCallback(MenueElementType.BONUSMENUE_BUTTON) {

                @Override
                public void actionPerformed(ActionEvent ae) {
                    activePane.doAddElement(new ActionContextEvent(ae, getContext()));
                }
            }, 0, AccessMode.APP_MODIFY);
            setupAction(getName(), Theme.NewCommands.NEW_LANGUAGE_BUTTON, new AbstractActionContextCallback(MenueElementType.LANGUAGE_BUTTON) {

                @Override
                public void actionPerformed(ActionEvent ae) {
                    activePane.doAddElement(new ActionContextEvent(ae, getContext()));
                }
            }, 0, AccessMode.APP_MODIFY);
            setupAction(getName(), Theme.NewCommands.NEW_SUBTITLE_BUTTON, new AbstractActionContextCallback(MenueElementType.SUBTITLE_BUTTON) {

                @Override
                public void actionPerformed(ActionEvent ae) {
                    activePane.doAddElement(new ActionContextEvent(ae, getContext()));
                }
            }, 0, AccessMode.APP_MODIFY);
            setupAction(getName(), Theme.NewCommands.NEW_CHAPTER_BUTTON, new AbstractActionContextCallback(MenueElementType.CHAPTER_BUTTON) {

                @Override
                public void actionPerformed(ActionEvent ae) {
                    activePane.doAddElement(new ActionContextEvent(ae, getContext()));
                }
            }, 0, AccessMode.APP_MODIFY);
        }

        @Override
        public void init(Object contextObj) {
            setupAction(getName(), Command.CTX_CONFIGURE, new AbstractActionContextCallback(contextObj) {

                @Override
                public void actionPerformed(ActionEvent ae) {
                    doConfigure(new ActionContextEvent(ae, getContext()));
                }
            }, KeyEvent.VK_N, AccessMode.APP_MODIFY);
            setupAction(getName(), Command.CTX_CLONE, new AbstractActionContextCallback(contextObj) {

                @Override
                public void actionPerformed(ActionEvent ae) {
                    doClone(new ActionContextEvent(ae, getContext()));
                }
            }, KeyEvent.VK_N, AccessMode.APP_MODIFY);
            setupAction(getName(), Command.CTX_SELECT, new AbstractActionContextCallback(contextObj) {

                @Override
                public void actionPerformed(ActionEvent ae) {
                    doSelect(new ActionContextEvent(ae, getContext()));
                }
            }, KeyEvent.VK_N, AccessMode.APP_MODIFY);
            setupAction(getName(), Command.CTX_ACTIVATE, new AbstractActionContextCallback(contextObj) {

                @Override
                public void actionPerformed(ActionEvent ae) {
                    doActivate(new ActionContextEvent(ae, getContext()));
                }
            }, KeyEvent.VK_N, AccessMode.APP_MODIFY);
            setupAction(getName(), Command.CTX_ZO_FIRST, new AbstractActionContextCallback(contextObj) {

                @Override
                public void actionPerformed(ActionEvent ae) {
                    doFirstZOrder(new ActionContextEvent(ae, getContext()));
                }
            }, KeyEvent.VK_N, AccessMode.APP_MODIFY);
            setupAction(getName(), Command.CTX_ZO_DOWN, new AbstractActionContextCallback(contextObj) {

                @Override
                public void actionPerformed(ActionEvent ae) {
                    doDecZOrder(new ActionContextEvent(ae, getContext()));
                }
            }, KeyEvent.VK_N, AccessMode.APP_MODIFY);
            setupAction(getName(), Command.CTX_ZO_UP, new AbstractActionContextCallback(contextObj) {

                @Override
                public void actionPerformed(ActionEvent ae) {
                    doIncZOrder(new ActionContextEvent(ae, getContext()));
                }
            }, KeyEvent.VK_N, AccessMode.APP_MODIFY);
            setupAction(getName(), Command.CTX_ZO_LAST, new AbstractActionContextCallback(contextObj) {

                @Override
                public void actionPerformed(ActionEvent ae) {
                    doLastZOrder(new ActionContextEvent(ae, getContext()));
                }
            }, KeyEvent.VK_N, AccessMode.APP_MODIFY);
            setupAction(getName(), Command.CTX_DELETE, new AbstractActionContextCallback(contextObj) {

                @Override
                public void actionPerformed(ActionEvent ae) {
                    doDelete(new ActionContextEvent(ae, getContext()));
                }
            }, KeyEvent.VK_N, AccessMode.APP_MODIFY);
            setupAction(getName(), Theme.NewCommands.NEW_ITEM_RECTANGLE, new AbstractActionContextCallback(new Object[] { MenueElementType.ITEM_RECTANGLE, contextObj }) {

                @Override
                public void actionPerformed(ActionEvent ae) {
                    activePane.doAddElement(new ActionContextEvent(ae, getContext()));
                }
            }, 0, AccessMode.APP_MODIFY);
            setupAction(getName(), Theme.NewCommands.NEW_ITEM_ELLIPSE, new AbstractActionContextCallback(new Object[] { MenueElementType.ITEM_ELLIPSE, contextObj }) {

                @Override
                public void actionPerformed(ActionEvent ae) {
                    activePane.doAddElement(new ActionContextEvent(ae, getContext()));
                }
            }, 0, AccessMode.APP_MODIFY);
            setupAction(getName(), Theme.NewCommands.NEW_ITEM_COMPOUND, new AbstractActionContextCallback(new Object[] { MenueElementType.ITEM_COMPOUND, contextObj }) {

                @Override
                public void actionPerformed(ActionEvent ae) {
                    activePane.doAddElement(new ActionContextEvent(ae, getContext()));
                }
            }, 0, AccessMode.APP_MODIFY);
            setupAction(getName(), Theme.NewCommands.NEW_BUTTON_RECTANGLE, new AbstractActionContextCallback(new Object[] { MenueElementType.BUTTON_RECTANGLE, contextObj }) {

                @Override
                public void actionPerformed(ActionEvent ae) {
                    activePane.doAddElement(new ActionContextEvent(ae, getContext()));
                }
            }, 0, AccessMode.APP_MODIFY);
            setupAction(getName(), Theme.NewCommands.NEW_BUTTON_ELLIPSE, new AbstractActionContextCallback(new Object[] { MenueElementType.BUTTON_ELLIPSE, contextObj }) {

                @Override
                public void actionPerformed(ActionEvent ae) {
                    activePane.doAddElement(new ActionContextEvent(ae, getContext()));
                }
            }, 0, AccessMode.APP_MODIFY);
            setupAction(getName(), Theme.NewCommands.NEW_BUTTON_COMPOUND, new AbstractActionContextCallback(new Object[] { MenueElementType.BUTTON_COMPOUND, contextObj }) {

                @Override
                public void actionPerformed(ActionEvent ae) {
                    activePane.doAddElement(new ActionContextEvent(ae, getContext()));
                }
            }, 0, AccessMode.APP_MODIFY);
        }

        private String[] switchCmds;

        private Enum<?>[] mbCmds = new Enum<?>[] { Command.FILE_NEW, Command.SEP_FILE_0, Command.FILE_OPEN, Command.FILE_SAVE, Command.FILE_SAVE_AS, Command.SEP_FILE_1, Command.FILE_IMPORT, Command.FILE_EXPORT, Command.FILE_IMG_EXPORT, Command.SEP_FILE_2, Command.FILE_REMOVE, Command.EDIT_CLEAR, Command.EDIT_ADD_SUB, Command.EDIT_THEME_PROPAGATE, Command.EDIT_SKIN_PROPAGATE, Command.EDIT_SHOW_ORDER, Command.VIEW_REFRESH, Command.VIEW_GRID_TOGGLE, Command.VIEW_CHANGE_GRID, Command.SEP_VIEW_0, Command.VIEW_FORMAT_SUB, Command.VIEW_SEL_SUB };

        private Enum<?>[] popCmds = new Enum<?>[] { Command.VIEW_SEL_SUB, Command.EDIT_SHOW_ORDER, Command.SEP_VIEW_0, Command.EDIT_THEME_PROPAGATE, Command.EDIT_SKIN_PROPAGATE, Command.VIEW_CHANGE_GRID, Command.EDIT_CLEAR, Command.SEP_CTX, Command.NEW_BASE_SUB, Command.NEW_IMAGE_SUB, Command.NEW_BUTTON_SUB };

        private Enum<?>[] popCtxCmds = new Enum<?>[] { Command.CTX_CONFIGURE, Command.SEP_VIEW_1, Command.CTX_SELECT, Command.CTX_ACTIVATE, Command.CTX_CLONE, Command.CTX_ZO_FIRST, Command.CTX_ZO_UP, Command.CTX_ZO_DOWN, Command.CTX_ZO_LAST, Command.CTX_DELETE, Command.SEP_VIEW_0, Command.VIEW_SEL_SUB, Command.EDIT_SHOW_ORDER, Command.EDIT_THEME_PROPAGATE, Command.EDIT_SKIN_PROPAGATE, Command.EDIT_CLEAR, Command.VIEW_CHANGE_GRID, Command.SEP_CTX, Command.NEW_BASE_SUB, Command.NEW_IMAGE_SUB, Command.NEW_BUTTON_SUB };

        private Enum<?>[] ncBase = new Enum<?>[] { Theme.NewCommands.NEW_TITLE_ITEM, Theme.NewCommands.NEW_HEADER_ITEM, Theme.NewCommands.NEW_DESCRIPTION, Theme.NewCommands.NEW_ITEM, Theme.NewCommands.NEW_ITEM_AREA, Theme.NewCommands.NEW_ITEM_RECTANGLE, Theme.NewCommands.NEW_ITEM_ELLIPSE, Theme.NewCommands.NEW_ITEM_COMPOUND, Theme.NewCommands.NEW_BUTTON_RECTANGLE, Theme.NewCommands.NEW_BUTTON_ELLIPSE, Theme.NewCommands.NEW_BUTTON_COMPOUND };

        private Enum<?>[] ncImage = new Enum<?>[] { Theme.NewCommands.NEW_SKIN_IMAGE, Theme.NewCommands.NEW_THEME_IMAGE, Theme.NewCommands.NEW_JOB_IMAGE, Theme.NewCommands.NEW_REC_IMAGE };

        private Enum<?>[] ncButton = new Enum<?>[] { Theme.NewCommands.NEW_MAINMENUE_BUTTON, Theme.NewCommands.NEW_NEXT_BUTTON, Theme.NewCommands.NEW_PREVIOUS_BUTTON, Theme.NewCommands.NEW_PLAY_BUTTON, Theme.NewCommands.NEW_PLAY_ALL_BUTTON, Theme.NewCommands.NEW_BONUSMENUE_BUTTON, Theme.NewCommands.NEW_LANGUAGE_BUTTON, Theme.NewCommands.NEW_SUBTITLE_BUTTON, Theme.NewCommands.NEW_CHAPTER_BUTTON };

        private Enum<?>[] tbXtra = new Enum<?>[] { Theme.NewCommands.NEW_TITLE_ITEM, Theme.NewCommands.NEW_HEADER_ITEM, Theme.NewCommands.NEW_DESCRIPTION, Theme.NewCommands.NEW_ITEM, Theme.NewCommands.NEW_ITEM_AREA, Theme.NewCommands.NEW_SKIN_IMAGE, Theme.NewCommands.NEW_THEME_IMAGE, Theme.NewCommands.NEW_JOB_IMAGE, Theme.NewCommands.NEW_REC_IMAGE, Command.SEP_FILE_0, Theme.NewCommands.NEW_MAINMENUE_BUTTON, Theme.NewCommands.NEW_NEXT_BUTTON, Theme.NewCommands.NEW_PREVIOUS_BUTTON, Theme.NewCommands.NEW_PLAY_BUTTON, Theme.NewCommands.NEW_PLAY_ALL_BUTTON, Theme.NewCommands.NEW_BONUSMENUE_BUTTON, Theme.NewCommands.NEW_LANGUAGE_BUTTON, Theme.NewCommands.NEW_SUBTITLE_BUTTON, Theme.NewCommands.NEW_CHAPTER_BUTTON };

        private Enum<?>[] tbCmd = new Enum<?>[] { Command.FILE_NEW, Command.FILE_OPEN, Command.FILE_SAVE, Command.FILE_SAVE_AS, Command.SEP_FILE_0, Command.FILE_EXPORT, Command.FILE_IMPORT, Command.SEP_FILE_1, Command.FILE_IMG_EXPORT, Command.EDIT_CLEAR, Command.EDIT_THEME_PROPAGATE, Command.EDIT_SKIN_PROPAGATE, Command.EDIT_SHOW_ORDER, Command.VIEW_CHANGE_GRID, Command.VIEW_REFRESH, Command.VIEW_GRID_TOGGLE, Command.SEP_CTX };
    }

    private enum Command {

        CTX_CONFIGURE, FILE_NEW, SEP_FILE_0, FILE_OPEN, FILE_SAVE, FILE_SAVE_AS, SEP_FILE_1, FILE_IMG_EXPORT, SEP_FILE_2, FILE_EXPORT, FILE_IMPORT, SEP_FILE_3, FILE_REMOVE, EDIT_CLEAR, EDIT_ADD_SUB, EDIT_THEME_PROPAGATE, EDIT_SKIN_PROPAGATE, EDIT_SHOW_ORDER, VIEW_REFRESH, VIEW_GRID_TOGGLE, VIEW_CHANGE_GRID, SEP_VIEW_0, VIEW_FORMAT_SUB, VIEW_SEL_SUB, SEP_VIEW_1, CTX_SELECT, CTX_ACTIVATE, CTX_CLONE, CTX_ZO_FIRST, CTX_ZO_UP, CTX_ZO_DOWN, CTX_ZO_LAST, CTX_DELETE, SEP_CTX, NEW_BASE_SUB, NEW_IMAGE_SUB, NEW_BUTTON_SUB
    }

    public EditorManager() {
        super("ThemeEditor");
        setActionHandler(new PreviewActionHandler(getName()));
    }

    public void change(ThemeElement<?> te) {
        AbstractDetailsView<ThemeElement<?>> adv = new ItemSettingsEditor("Positions-Einstellungen", te);
        adv.setSubmitHandler(this);
        if (adv.showDialog(content) == ItemSettingsEditor.APPROVE_OPTION) refresh();
    }

    @Override
    public JComponent createPane() {
        return createPane(null);
    }

    public JComponent createPane(String command) {
        if (content == null) content = buildPanel();
        return content;
    }

    @Override
    public AbstractStatusBar createStatusBar() {
        return new MyStatusBar();
    }

    @Override
    public void doBasePopup(int x, int y, ActionListener themeExecutor) {
        getLogger().info("open base popup on " + x + "/" + y);
        JPopupMenu popup = getActionHandler().createPopup(null);
        popup.show(activePane, x, y);
    }

    @Override
    public void doContextPopup(int x, int y, int actual, ActionListener executor) {
        JPopupMenu popup = null;
        ThemeElement<?> te = activeTheme.getThemeElement(menueDef.getCurrentPage(), actual);
        popup = getActionHandler().createPopup(te);
        getLogger().info("open context popup on " + x + "/" + y + " => " + actual);
        popup.show(activePane, x, y);
    }

    public void doSaveImage(ActionEvent e) {
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int rv = fc.showSaveDialog(content);
        if (rv == JFileChooser.APPROVE_OPTION) {
            File newFile = fc.getSelectedFile();
            activePane.doSaveImage(newFile);
        }
    }

    @Override
    public JComponent getConfigPage() {
        if (configEditor == null) configEditor = new ConfigEditor(getAppConfig());
        return configEditor;
    }

    @Override
    public void onApplicationEvent(ApplicationEvent ae) {
        if (activePane == null) return;
        getLogger().error("==> onApplicationEvent: " + ae);
        if (ae instanceof DVDThemePageChangedEvent) {
            activePane.setPageType(menueDef.getCurrentPage());
            setTitle(EnumUtils.getLabelFor(menueDef.getCurrentPage()));
            validateActions();
            activePane.repaint();
        } else if (ae instanceof DVDThemeChangedEvent) {
            activeTheme = menueDef.getTheme();
            activePane.setTheme(activeTheme);
            activePane.repaint();
            validateActions();
        } else if (ae instanceof DVDSkinChangedEvent) {
            if (((DVDSkinChangedEvent) ae).getCategory() != null) {
                activeTheme.refresh(((DVDSkinChangedEvent) ae).getPageType(), ((DVDSkinChangedEvent) ae).getCategory());
                activeTheme.refresh();
                activePane.repaint();
            }
        } else if (ae instanceof ConfigurationChangedEvent) {
            Object source = ae.getSource();
            if (source != null && source instanceof TEConfig) {
                menueDef.setupTheme((TEConfig) source);
                activePane.repaint();
            }
        }
    }

    @Override
    public void onSubmit(ThemeElement<?> any) {
        getLogger().fatal("unimplemented submit-handler for Entity " + any.getClass().getName());
    }

    @Override
    public void setTitle(String title) {
        if (title == null) title = EnumUtils.getLabelFor(menueDef.getCurrentPage());
        super.setTitle(title);
    }

    @Override
    public void setup() {
        FormComponentFactory componentFactory = ApplicationServiceProvider.getService(FormComponentFactory.class);
        if (componentFactory != null && componentFactory.getDefaultImageDirectory() == null) {
            componentFactory.setDefaultImageDirectory(new File(getAppConfig().getImageBaseDir()));
        }
    }

    @Override
    public void start() {
        super.start();
        getTopWindow().pack();
    }

    @Override
    public void stop() {
        super.stop();
        content = null;
        activeTheme = null;
        activePane = null;
        seen.clear();
    }

    @Override
    public void validateActions() {
        seen.put(Theme.NewCommands.NEW_TITLE_ITEM, false);
        seen.put(Theme.NewCommands.NEW_HEADER_ITEM, false);
        seen.put(Theme.NewCommands.NEW_DESCRIPTION, false);
        seen.put(Theme.NewCommands.NEW_MAINMENUE_BUTTON, false);
        seen.put(Theme.NewCommands.NEW_NEXT_BUTTON, false);
        seen.put(Theme.NewCommands.NEW_PREVIOUS_BUTTON, false);
        seen.put(Theme.NewCommands.NEW_PLAY_BUTTON, false);
        seen.put(Theme.NewCommands.NEW_PLAY_ALL_BUTTON, false);
        seen.put(Theme.NewCommands.NEW_BONUSMENUE_BUTTON, false);
        seen.put(Theme.NewCommands.NEW_LANGUAGE_BUTTON, false);
        seen.put(Theme.NewCommands.NEW_SUBTITLE_BUTTON, false);
        seen.put(Theme.NewCommands.NEW_CHAPTER_BUTTON, false);
        seen.put(Theme.NewCommands.NEW_JOB_IMAGE, false);
        seen.put(Theme.NewCommands.NEW_REC_IMAGE, false);
        seen.put(Theme.NewCommands.NEW_SKIN_IMAGE, false);
        seen.put(Theme.NewCommands.NEW_THEME_IMAGE, false);
        for (ThemeElement<?> te : activeTheme.getThemeElements(menueDef.getCurrentPage())) {
            try {
                Theme.NewCommands nc = Enum.valueOf(Theme.NewCommands.class, "NEW_" + te.getType().name());
                switch(nc) {
                    case NEW_TITLE_ITEM:
                    case NEW_HEADER_ITEM:
                    case NEW_MAINMENUE_BUTTON:
                    case NEW_NEXT_BUTTON:
                    case NEW_PREVIOUS_BUTTON:
                    case NEW_PLAY_BUTTON:
                    case NEW_PLAY_ALL_BUTTON:
                    case NEW_BONUSMENUE_BUTTON:
                    case NEW_LANGUAGE_BUTTON:
                    case NEW_SUBTITLE_BUTTON:
                    case NEW_CHAPTER_BUTTON:
                    case NEW_DESCRIPTION:
                    case NEW_JOB_IMAGE:
                    case NEW_REC_IMAGE:
                    case NEW_SKIN_IMAGE:
                    case NEW_THEME_IMAGE:
                        seen.put(nc, true);
                        break;
                }
            } catch (Exception e) {
                getLogger().error("Oups ", e);
            }
        }
        EnumSet<Theme.NewCommands> allowed = activeTheme.getAllowedElementCommands(menueDef.getCurrentPage());
        ActionHandler h = getActionHandler();
        for (Theme.NewCommands nc : Theme.NewCommands.values()) {
            Boolean check = allowed.contains(nc);
            if (seen.containsKey(nc) && seen.get(nc)) check = false;
            h.enableAction(getName() + "." + nc.name(), check);
        }
    }

    protected JPanel buildPanel() {
        JPanel rv = new JPanel();
        if (imgFactory == null) imgFactory = ApplicationServiceProvider.getService(ImageFactory.class);
        if (menueDef == null) menueDef = ApplicationServiceProvider.getService(MenueDefinition.class);
        doOpen();
        if (activeTheme == null) activeTheme = menueDef.getTheme();
        if (activeTheme == null) throw new UnsupportedOperationException("could not initialize MenueDefinition");
        rv.setLayout(new BorderLayout());
        activePane = new ComposerPane(activeTheme, menueDef.getCurrentPage());
        activePane.setAppConfig(getAppConfig());
        activePane.setPopupHandler(this);
        mpSelector = new MenuepageSelector(menueDef, MenuepageSelector.HORIZONTAL);
        rv.add(activePane, BorderLayout.CENTER);
        rv.add(mpSelector, BorderLayout.SOUTH);
        validateActions();
        changeDefaultSize(activeTheme.getAspect());
        return rv;
    }

    protected void changeDefaultSize(VideoPageFormat aspect) {
        if (content != null) {
            content.setPreferredSize(getSizeForAspect(aspect));
            getTopWindow().pack();
        }
    }

    protected void doActivate(ActionEvent e) {
        getLogger().debug("Event: " + e);
        if (e instanceof ActionContextEvent) {
            ThemeElement<?> te = (ThemeElement<?>) ((ActionContextEvent) e).getContext();
            if (te instanceof AbstractItemBase<?>) {
                ((AbstractItemBase<?>) te).setActive(!((AbstractItemBase<?>) te).isActive());
                activePane.repaint();
            }
        }
    }

    protected void doChangeAspect(ActionEvent e) {
        if (e instanceof ActionContextEvent) {
            VideoPageFormat aspect = (VideoPageFormat) ((ActionContextEvent) e).getContext();
            activePane.setAspect(aspect);
            changeDefaultSize(aspect);
            getTopWindow().pack();
        }
    }

    protected void doClear(ActionEvent e) {
        getLogger().debug("Event: " + e);
        activeTheme.getThemeElements(menueDef.getCurrentPage()).clear();
        activePane.repaint();
        validateActions();
    }

    protected void doClone(ActionEvent e) {
        getLogger().debug("Event: " + e);
        if (e instanceof ActionContextEvent) {
            ThemeElement<?> te = (ThemeElement<?>) ((ActionContextEvent) e).getContext();
            switch(te.getType()) {
                case ITEM:
                case ITEM_AREA:
                case DESCRIPTION:
                case SKIN_IMAGE:
                case THEME_IMAGE:
                case ITEM_RECTANGLE:
                case ITEM_ELLIPSE:
                case ITEM_COMPOUND:
                case BUTTON_RECTANGLE:
                case BUTTON_ELLIPSE:
                case BUTTON_COMPOUND:
                    ThemeElement<?> nte = activeTheme.createThemeElement(te.getPageType(), te.getType(), te.getBounds());
                    if (nte != null) {
                        nte.setPosY(te.getPosY() + te.getHeight() + 10);
                        activeTheme.addThemeElement(menueDef.getCurrentPage(), nte);
                        refresh();
                    }
                    break;
            }
        }
    }

    protected void doConfigure(ActionEvent e) {
        getLogger().info("Event: " + e);
        if (e instanceof ActionContextEvent) {
            ThemeElement<?> te = (ThemeElement<?>) ((ActionContextEvent) e).getContext();
            ItemSettingsEditor ise = new ItemSettingsEditor(getName() + ".configure.item", te);
            ise.setSubmitHandler(this);
            if (ise.showDialog(activePane) == ItemSettingsEditor.APPROVE_OPTION) activePane.repaint();
        }
    }

    protected void doDecZOrder(ActionEvent e) {
        getLogger().debug("Event: " + e);
        if (e instanceof ActionContextEvent) {
            ThemeElement<?> te = (ThemeElement<?>) ((ActionContextEvent) e).getContext();
            int itemIndex = te.getWeight();
            activeTheme.changeZOrder(menueDef.getCurrentPage(), itemIndex, Theme.ChangeZOrder.PREVIOUS);
            activePane.repaint();
        }
    }

    protected void doDelete(ActionEvent e) {
        getLogger().debug("Event: " + e);
        if (e instanceof ActionContextEvent) {
            ThemeElement<?> te = (ThemeElement<?>) ((ActionContextEvent) e).getContext();
            activeTheme.removeThemeElement(menueDef.getCurrentPage(), te);
            activePane.repaint();
            validateActions();
        }
    }

    protected void doExport() {
        JFileChooser fc = new JFileChooser("keine Ahnung");
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int rv = fc.showOpenDialog(getMainWindow());
        if (rv == JFileChooser.APPROVE_OPTION) {
            File newFile = fc.getSelectedFile();
            if (newFile != null) {
                new TaskPerThreadExecutor().execute(new ContextTask(newFile) {

                    @Override
                    public void run() {
                        doExport(this.getProgressPublisher(), getContext());
                    }
                });
            }
        }
    }

    protected void doExport(ProgressPublisher pp, Object ctx) {
        getLogger().error("export menue template to: " + ctx);
        if (exHandler == null) {
            exHandler = ApplicationServiceProvider.getService(ExchangeHandler.class);
        }
        exHandler.setWorkingDirectory((File) ctx);
        exHandler.exportTemplateArchive();
    }

    protected void doFirstZOrder(ActionEvent e) {
        getLogger().debug("Event: " + e);
        if (e instanceof ActionContextEvent) {
            ThemeElement<?> te = (ThemeElement<?>) ((ActionContextEvent) e).getContext();
            int itemIndex = te.getWeight();
            activeTheme.changeZOrder(menueDef.getCurrentPage(), itemIndex, Theme.ChangeZOrder.FIRST);
            activePane.repaint();
        }
    }

    protected void doGridConfig(ActionEvent e) {
        activePane.doGridConfig(e);
    }

    protected void doGridToggle(ActionEvent e) {
        activePane.toggleGrid();
    }

    protected void doImport() {
        if (lastImport == null) lastImport = new File(".");
        JFileChooser fc = new JFileChooser(lastImport);
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int rv = fc.showOpenDialog(getMainWindow());
        if (rv == JFileChooser.APPROVE_OPTION) {
            lastImport = fc.getSelectedFile();
            String sudoPassword = JOptionPane.showInputDialog(msgSource.getMessage(getName() + ".sudo.password", null, getName() + ".sudo.password", null));
            if (lastImport != null) {
                VContextTask task = new VContextTask() {

                    @Override
                    public void run() {
                        doImport(this.getProgressPublisher(), getContext("importFile"), getContext("password"));
                    }
                };
                task.addContext("importFile", lastImport);
                task.addContext("password", sudoPassword);
                new TaskPerThreadExecutor().execute(task);
            }
        }
    }

    protected void doImport(ProgressPublisher pp, Object importFile, Object password) {
        getLogger().error("import theme from: " + importFile);
        if (exHandler == null) exHandler = ApplicationServiceProvider.getService(ExchangeHandler.class);
        if (exHandler != null && importFile instanceof File) {
            File file2Import = (File) importFile;
            exHandler.importTemplateArchive(file2Import, (String) password);
        }
    }

    protected void doIncZOrder(ActionEvent e) {
        getLogger().debug("Event: " + e);
        if (e instanceof ActionContextEvent) {
            ThemeElement<?> te = (ThemeElement<?>) ((ActionContextEvent) e).getContext();
            int itemIndex = te.getWeight();
            activeTheme.changeZOrder(menueDef.getCurrentPage(), itemIndex, Theme.ChangeZOrder.NEXT);
            activePane.repaint();
        }
    }

    protected void doLastZOrder(ActionEvent e) {
        getLogger().debug("Event: " + e);
        if (e instanceof ActionContextEvent) {
            ThemeElement<?> te = (ThemeElement<?>) ((ActionContextEvent) e).getContext();
            int itemIndex = te.getWeight();
            activeTheme.changeZOrder(menueDef.getCurrentPage(), itemIndex, Theme.ChangeZOrder.LAST);
            activePane.repaint();
        }
    }

    protected void doNew() {
        menueDef.selectTheme(NEW_NAME);
        menueDef.setupTheme(getAppConfig());
    }

    protected void doOpen() {
        VideoPageFormat oa = menueDef.getAspect();
        ThemeSelectionDialog dlg = new ThemeSelectionDialog(ThemeSelectionDialog.MainInterest.THEME, menueDef, ThemeSelectionDialog.Direction.LOAD);
        if (dlg.showDialog(getMainWindow()) == ThemeSelectionDialog.APPROVE_OPTION) {
            if (dlg.getSkinName() != null) menueDef.selectSkin(dlg.getSkinName());
            if (dlg.getThemeName() != null) menueDef.selectTheme(dlg.getThemeName(), dlg.getAspect());
        } else {
            if (menueDef.getTheme() == null) {
                menueDef.selectTheme("standard");
                menueDef.selectSkin("standard");
            } else menueDef.selectTheme("standard");
        }
        if (menueDef.getAspect() != oa) changeDefaultSize(menueDef.getAspect());
        menueDef.setupTheme(getAppConfig());
    }

    protected void doPropagate(ActionEvent e) {
        getLogger().debug("Event: " + e);
        if (e instanceof ActionContextEvent) {
            Enum<?> cmd = (Enum<?>) ((ActionContextEvent) e).getContext();
            if (cmd.name().compareTo(Command.EDIT_THEME_PROPAGATE.name()) == 0) menueDef.propagateThemePage(); else if (cmd.name().compareTo(Command.EDIT_SKIN_PROPAGATE.name()) == 0) menueDef.propagateSkinPage();
        }
        activePane.repaint();
    }

    protected void doSave() {
        if (menueDef.getThemeName().equals(NEW_NAME)) doSaveAs();
        menueDef.saveTheme();
    }

    protected void doSaveAs() {
        ThemeSelectionDialog dlg = new ThemeSelectionDialog(ThemeSelectionDialog.MainInterest.THEME, menueDef, ThemeSelectionDialog.Direction.SAVE);
        if (dlg.showDialog(getMainWindow()) == ThemeSelectionDialog.APPROVE_OPTION) {
            if (dlg.getThemeName() != null) menueDef.saveThemeAs(dlg.getThemeName());
        }
    }

    protected void doSelect(ActionEvent e) {
        getLogger().debug("Event: " + e);
        if (e instanceof ActionContextEvent) {
            ThemeElement<?> te = (ThemeElement<?>) ((ActionContextEvent) e).getContext();
            activeTheme.selectItem(te);
            activePane.repaint();
        }
    }

    protected void doShowOrder(ActionEvent e) {
        getLogger().debug("Event: " + e);
        activeTheme.toggleOrderVisibility();
        activePane.repaint();
    }

    protected Dimension getSizeForAspect(VideoPageFormat aspect) {
        Dimension rv = null;
        if (aspect == VideoPageFormat.WIDESCREEN) rv = new Dimension(1034, 730); else rv = new Dimension(730, 730);
        return rv;
    }

    protected ComposerPane activePane;

    protected MenueDefinition menueDef;

    protected JPanel content;

    private Map<Theme.NewCommands, Boolean> seen = new HashMap<Theme.NewCommands, Boolean>();

    private ExchangeHandler exHandler;

    private MenuepageSelector mpSelector;

    private ConfigEditor configEditor;

    private Theme activeTheme;

    private File lastImport;
}
