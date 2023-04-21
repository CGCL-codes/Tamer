package be.lassi.ui.main;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import be.lassi.context.ShowContext;
import be.lassi.ui.patch.FadeTimeMenu;
import be.lassi.ui.patch.LayerMenu;
import be.lassi.ui.patch.PatchActions;
import be.lassi.ui.patch.PatchPresentationModel;
import be.lassi.ui.sheet.actions.SheetActions;
import be.lassi.ui.util.MacMenuBuilder;
import be.lassi.util.NLS;

/**
 * Builds the menu that is used when running on Mac OS X.  This menu
 * is added to all frames in the application (each frame needs to have
 * its own unique copy !!), so that the menu is always shown, independant
 * of which frame currently has focus.
 */
public class MacMenu {

    private final ShowContext context;

    private final MainPresentationModel mainPresentationModel;

    private final SheetActions sheetActions;

    private final PatchPresentationModel patchPresentationModel;

    /**
     * Constructs a new instance.
     *
     * @param context the show context
     * @param frames the application frames
     */
    public MacMenu(final ShowContext context, final MainFrames frames) {
        this.context = context;
        mainPresentationModel = frames.getMainFrame().getModel();
        sheetActions = frames.getSheet().getModel().getActions();
        patchPresentationModel = frames.getPatch().getModel();
    }

    /**
     * Builds the application menu bar for use under Mac OS X.
     *
     * @return the menu bar
     */
    public JMenuBar build() {
        JMenuBar menu = new JMenuBar();
        menu.add(buildMenuFile());
        menu.add(buildMenuCues());
        menu.add(buildMenuPatch());
        menu.add(new WindowsMenu(mainPresentationModel).build());
        menu.add(buildMenuHelp());
        return menu;
    }

    private JMenu buildMenuFile() {
        MainActions actions = mainPresentationModel.getActions();
        MacMenuBuilder b = new MacMenuBuilder(NLS.get("main.menu.file"));
        b.add(actions.getActionNew());
        b.add(actions.getActionOpen());
        b.add(actions.getActionSave());
        b.addSeparator();
        b.add(actions.getActionSetup());
        return b.getMenu();
    }

    private JMenu buildMenuCues() {
        MacMenuBuilder b = new MacMenuBuilder(NLS.get("sheet.menu"));
        b.add(sheetActions.getActionCurrent());
        b.addSeparator();
        b.add(sheetActions.getActionFade());
        b.add(sheetActions.getActionGo());
        b.addSeparator();
        b.add(sheetActions.getActionRenumber());
        b.addSeparator();
        b.add(sheetActions.getActionBlackout());
        b.add(sheetActions.getActionAllUp());
        b.add(sheetActions.getActionRecord());
        b.addSeparator();
        b.add(sheetActions.getActionCut());
        b.add(sheetActions.getActionCopy());
        b.add(sheetActions.getActionPaste());
        b.addSeparator();
        b.add(sheetActions.getActionInsert());
        b.add(sheetActions.getActionInsertMany());
        b.add(sheetActions.getActionAdd());
        b.add(sheetActions.getActionAddMany());
        b.addSeparator();
        b.add(sheetActions.getActionSaveToLanbox());
        return b.getMenu();
    }

    private JMenu buildMenuPatch() {
        PatchActions actions = patchPresentationModel.getActions();
        MacMenuBuilder b = new MacMenuBuilder(NLS.get("patch.menu"));
        b.add(actions.getActionSavePatch());
        b.add(actions.getActionLoadPatch());
        b.addSeparator();
        b.add(actions.getActionClearPatch());
        b.add(actions.getActionDefaultPatch());
        b.addSeparator();
        b.add(actions.getActionCopyChannelNamesToDimmers());
        b.add(actions.getActionCopyDimmerNamesToChannels());
        b.add(actions.getActionCopyToClipboard());
        b.addSeparator();
        JMenu layerMenu = new LayerMenu(context, actions.getActionSetLayer());
        JMenu fadeTimeMenu = new FadeTimeMenu(context, actions.getActionSetFadeTime());
        b.add(layerMenu);
        b.add(fadeTimeMenu);
        b.add(actions.getActionStageFollowsSelection(), patchPresentationModel.getStageFollowsSelection());
        b.add(actions.getActionUpdateImmediately(), patchPresentationModel.getUpdateImmediately());
        return b.getMenu();
    }

    private JMenu buildMenuHelp() {
        MacMenuBuilder b = new MacMenuBuilder(NLS.get("main.menu.help"));
        b.add(mainPresentationModel.getActions().getActionHelp());
        return b.getMenu();
    }
}
