package ch.intertec.storybook.main.toolbar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JComboBox;
import javax.swing.JToolBar;
import net.miginfocom.swing.MigLayout;
import ch.intertec.storybook.action.ActionManager.SbAction;
import ch.intertec.storybook.action.ActionRegistry;
import ch.intertec.storybook.model.PCSDispatcher;
import ch.intertec.storybook.toolkit.ProjectTools;
import ch.intertec.storybook.toolkit.swing.SwingTools;
import ch.intertec.storybook.view.IRefreshable;
import ch.intertec.storybook.view.IconButton;

@SuppressWarnings("serial")
public class MainToolBar extends JToolBar implements ActionListener, PropertyChangeListener, IRefreshable {

    public static final String COMP_NAME = "toolbar";

    public static final String COMP_NAME_BT_NEW_CHARACTER = "bt:new_character";

    public static final String COMP_NAME_BT_NEW_TAG = "bt:new_tag";

    public static final String COMP_NAME_BT_NEW_ITEM = "bt:new_item";

    public static final String COMP_NAME_BT_NEW_LOCATION = "bt:new_location";

    public static final String COMP_NAME_BT_NEXT_PART = "bt:next_part";

    public static final String COMP_NAME_BT_PREV_PART = "bt:prev_part";

    public static final String COMP_NAME_COB_VIEW = "cob:view";

    private JComboBox cobView;

    private IconButton btShowSidebar;

    public MainToolBar() {
        initGUI();
        PCSDispatcher pcs = PCSDispatcher.getInstance();
        pcs.addPropertyChangeListener(PCSDispatcher.Property.PROJECT, this);
    }

    private void initGUI() {
        MigLayout layout = new MigLayout("insets 0 2 0 2", "", "[center]");
        setLayout(layout);
        setFloatable(false);
        setName(COMP_NAME);
        boolean active = ProjectTools.isProjectOpen();
        ActionRegistry ar = ActionRegistry.getInstance();
        IconButton btNewFile = new IconButton("icon.small.file.new", "msg.file.new", ar.getAction(SbAction.FILE_NEW));
        IconButton btOpenFile = new IconButton("icon.small.open", "msg.common.open", ar.getAction(SbAction.FILE_OPEN));
        IconButton btSaveFile = new IconButton("icon.small.save", "msg.file.save", ar.getAction(SbAction.FILE_SAVE));
        btSaveFile.setEnabled(active);
        IconButton btExportPrint = new IconButton("icon.small.print", "msg.menu.file.export_print", ar.getAction(SbAction.EXPORT_PRINT));
        btExportPrint.setEnabled(active);
        IconButton btNewScene = new IconButton("icon.small.new", "msg.common.scene.add", ar.getAction(SbAction.SCENE_NEW));
        btNewScene.setEnabled(active);
        IconButton btNewChapter = new IconButton("icon.small.chapter.new", "msg.common.chapter.add", ar.getAction(SbAction.CHAPTER_NEW));
        btNewChapter.setEnabled(active);
        IconButton btManageChapters = new IconButton("icon.medium.manage.chapters", "msg.menu.chapters.manage", ar.getAction(SbAction.CHAPTER_MANAGE));
        btManageChapters.setEnabled(active);
        IconButton btNewLocation = new IconButton("icon.medium.new.location", "msg.common.location.new", ar.getAction(SbAction.LOCATION_NEW));
        btNewLocation.setEnabled(active);
        btNewLocation.setName(COMP_NAME_BT_NEW_LOCATION);
        IconButton btNewCharacter = new IconButton("icon.medium.new.person", "msg.common.person.new", ar.getAction(SbAction.CHARACTER_NEW));
        btNewCharacter.setEnabled(active);
        btNewCharacter.setName(COMP_NAME_BT_NEW_CHARACTER);
        IconButton btManageLocations = new IconButton("icon.medium.manage.locations", "msg.dlg.mng.loc.title", ar.getAction(SbAction.LOCATION_MANAGE));
        btManageLocations.setEnabled(active);
        IconButton btManageCharacters = new IconButton("icon.medium.manage.persons", "msg.dlg.mng.persons.title", ar.getAction(SbAction.CHARACTER_MANAGE));
        btManageCharacters.setEnabled(active);
        IconButton btNewTag = new IconButton("icon.medium.new.tag", "msg.tag.new", ar.getAction(SbAction.TAG_NEW));
        btNewTag.setEnabled(active);
        btNewTag.setName(COMP_NAME_BT_NEW_TAG);
        IconButton btManageTags = new IconButton("icon.medium.manage.tags", "msg.tags.manage", ar.getAction(SbAction.TAG_MANAGE));
        btManageTags.setEnabled(active);
        IconButton btTagAssignments = new IconButton("icon.medium.manage.tag_links", "msg.tag.assignments", ar.getAction(SbAction.TAG_ASSIGNMENTS));
        btTagAssignments.setEnabled(active);
        IconButton btNewItem = new IconButton("icon.medium.new.item", "msg.item.new", ar.getAction(SbAction.ITEM_NEW));
        btNewItem.setEnabled(active);
        btNewItem.setName(COMP_NAME_BT_NEW_ITEM);
        IconButton btManageItems = new IconButton("icon.medium.manage.items", "msg.items.manage", ar.getAction(SbAction.ITEM_MANAGE));
        btManageItems.setEnabled(active);
        IconButton btItemAssignments = new IconButton("icon.medium.manage.item_links", "msg.item.assignments", ar.getAction(SbAction.ITEM_ASSIGNMENTS));
        btItemAssignments.setEnabled(active);
        IconButton btIdeas = new IconButton("icon.small.bulb", "msg.foi.title", ar.getAction(SbAction.IDEAS_FOI));
        btIdeas.setEnabled(active);
        IconButton btMemoria = new IconButton("icon.small.memoria", "msg.menu.view.pov", ar.getAction(SbAction.MEMORIA));
        btMemoria.setEnabled(active);
        IconButton btTaskList = new IconButton("icon.small.tasklist", "msg.tasklist.title", ar.getAction(SbAction.TASK_LIST));
        btTaskList.setEnabled(active);
        IconButton btRefresh = new IconButton("icon.small.refresh", "msg.common.refresh", ar.getAction(SbAction.REFRESH));
        btRefresh.setEnabled(active);
        btShowSidebar = new IconButton("icon.small.show.infos", "msg.menu.view.show.infos", ar.getAction(SbAction.SIDEBAR_TOGGLE));
        btShowSidebar.setEnabled(active);
        add(btNewFile);
        add(btOpenFile);
        add(btSaveFile);
        add(btExportPrint);
        add(SwingTools.createMenuBarSpacer());
        add(btNewScene);
        add(btNewChapter);
        add(btManageChapters);
        add(btNewCharacter);
        add(btManageCharacters);
        add(btNewLocation);
        add(btManageLocations);
        add(btNewTag);
        add(btManageTags);
        add(btTagAssignments);
        add(btNewItem);
        add(btManageItems);
        add(btItemAssignments);
        add(SwingTools.createMenuBarSpacer());
        add(btMemoria);
        add(btIdeas);
        add(btTaskList);
        add(btRefresh, "pushx,right");
        add(btShowSidebar);
    }

    @Override
    public void refresh() {
        removeAll();
        initGUI();
        revalidate();
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource() == cobView) {
            ViewItem item = (ViewItem) cobView.getSelectedItem();
            item.getViewAction().actionPerformed(evt);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        refresh();
    }
}
