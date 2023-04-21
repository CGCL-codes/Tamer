package ch.intertec.storybook.view.modify.scene;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import net.miginfocom.swing.MigLayout;
import ch.intertec.storybook.action.ActionManager.SbAction;
import ch.intertec.storybook.action.ActionRegistry;
import ch.intertec.storybook.model.Scene;
import ch.intertec.storybook.toolkit.I18N;
import ch.intertec.storybook.view.IRefreshable;
import ch.intertec.storybook.view.IconButton;
import ch.intertec.storybook.view.modify.CharacterLinksFactory;
import ch.intertec.storybook.view.modify.LocationLinksFactory;

@SuppressWarnings("serial")
public class CharactersLocationsPanel extends JPanel implements MouseListener, IRefreshable {

    public static final String COMP_NAME = "characters_locations_panel";

    public static final String COMP_NAME_CHARACTER_LIST = "list:characters";

    public static final String COMP_NAME_LOCATION_LIST = "list:locations";

    private Scene scene;

    private JList characters;

    private JList chosenCharacters;

    private JList locations;

    private JList chosenLocations;

    private List<Object> characterLinksList;

    private List<Object> locationLinksList;

    private CharacterLinksFactory characterFactory;

    private LocationLinksFactory locationFactory;

    public CharactersLocationsPanel(Scene scene) {
        this.scene = scene;
        characterLinksList = new ArrayList<Object>();
        locationLinksList = new ArrayList<Object>();
        characterFactory = new CharacterLinksFactory(characterLinksList, scene);
        locationFactory = new LocationLinksFactory(locationLinksList, scene);
        initGUI();
    }

    private void initGUI() {
        MigLayout layout = new MigLayout("wrap 2,fill", "[]20[]", "[][grow][][grow]");
        setLayout(layout);
        JLabel lbCharacterLink = new JLabel(I18N.getMsgColon("msg.dlg.scene.person.links"));
        chosenCharacters = characterFactory.createList(scene, scene == null ? true : false);
        chosenCharacters.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        chosenCharacters.addMouseListener(this);
        JScrollPane spChosenCharacters = new JScrollPane(chosenCharacters);
        IconButton btAddCharacter = new IconButton("icon.small.arrow.up", "msg.common.add", getAddCharacterAction());
        IconButton btRemoveCharacter = new IconButton("icon.small.arrow.down", "msg.common.delete", getRemoveCharacterAction());
        IconButton btNewCharacter = new IconButton("icon.medium.new.person", "msg.common.person.new", getNewCharacterAction());
        characters = characterFactory.createList();
        characters.setName(COMP_NAME_CHARACTER_LIST);
        characters.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        characters.addMouseListener(this);
        JScrollPane spCharacters = new JScrollPane(characters);
        JLabel lbLocation = new JLabel(I18N.getMsgColon("msg.dlg.scene.location.links"));
        chosenLocations = locationFactory.createList(scene, scene == null ? true : false);
        chosenLocations.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        chosenLocations.addMouseListener(this);
        JScrollPane spChosenLocations = new JScrollPane(chosenLocations);
        IconButton btAddLoction = new IconButton("icon.small.arrow.up", "msg.common.add", getAddLocationAction());
        IconButton btRemoveLoction = new IconButton("icon.small.arrow.down", "msg.common.delete", getRemoveLocationAction());
        IconButton btNewLocation = new IconButton("icon.medium.new.location", "msg.common.location.new", getNewLocationAction());
        locations = locationFactory.createList();
        locations.setName(COMP_NAME_LOCATION_LIST);
        locations.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        locations.addMouseListener(this);
        JScrollPane spLocations = new JScrollPane(locations);
        add(lbCharacterLink);
        add(lbLocation);
        add(spChosenCharacters, "h 30%,w 50%");
        add(spChosenLocations, "h 30%,w 50%");
        add(btAddCharacter, "split 3");
        add(btRemoveCharacter, "");
        add(btNewCharacter, "gap push");
        add(btAddLoction, "split 3");
        add(btRemoveLoction, "");
        add(btNewLocation, "gap push");
        add(spCharacters, "h 70%,w 50%");
        add(spLocations, "h 70%,w 50%");
    }

    @Override
    public void refresh() {
        removeAll();
        initGUI();
    }

    private AbstractAction getAddCharacterAction() {
        return new AbstractAction(I18N.getMsg("msg.common.add")) {

            @Override
            public void actionPerformed(ActionEvent e) {
                addCharacter();
            }
        };
    }

    private AbstractAction getRemoveCharacterAction() {
        return new AbstractAction(I18N.getMsg("msg.common.delete")) {

            @Override
            public void actionPerformed(ActionEvent e) {
                removeCharacter();
            }
        };
    }

    private AbstractAction getAddLocationAction() {
        return new AbstractAction(I18N.getMsg("msg.common.add")) {

            @Override
            public void actionPerformed(ActionEvent e) {
                addLocation();
            }
        };
    }

    private AbstractAction getRemoveLocationAction() {
        return new AbstractAction(I18N.getMsg("msg.common.delete")) {

            @Override
            public void actionPerformed(ActionEvent e) {
                removeLocation();
            }
        };
    }

    private AbstractAction getNewCharacterAction() {
        return new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ActionRegistry.getInstance().getAction(SbAction.CHARACTER_NEW).actionPerformed(null);
                refresh();
            }
        };
    }

    private AbstractAction getNewLocationAction() {
        return new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ActionRegistry.getInstance().getAction(SbAction.LOCATION_NEW).actionPerformed(null);
                refresh();
            }
        };
    }

    private void addCharacter() {
        Object obj = characters.getSelectedValue();
        if (obj == null) {
            return;
        }
        DefaultListModel model = (DefaultListModel) chosenCharacters.getModel();
        if (model != null && !model.contains(obj)) {
            model.addElement(obj);
        }
    }

    private void removeCharacter() {
        Object obj = chosenCharacters.getSelectedValue();
        if (obj == null) {
            return;
        }
        DefaultListModel model = (DefaultListModel) chosenCharacters.getModel();
        model.removeElement(obj);
    }

    private void addLocation() {
        Object obj = locations.getSelectedValue();
        if (obj == null) {
            return;
        }
        DefaultListModel model = (DefaultListModel) chosenLocations.getModel();
        if (model != null && !model.contains(obj)) {
            model.addElement(obj);
        }
    }

    private void removeLocation() {
        Object obj = chosenLocations.getSelectedValue();
        if (obj == null) {
            return;
        }
        DefaultListModel model = (DefaultListModel) chosenLocations.getModel();
        model.removeElement(obj);
    }

    public JList getChoosenCharacterList() {
        return chosenCharacters;
    }

    public JList getChoosenLocationList() {
        return chosenLocations;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getClickCount() == 2) {
            if (e.getSource() == characters) {
                addCharacter();
            } else if (e.getSource() == chosenCharacters) {
                removeCharacter();
            } else if (e.getSource() == locations) {
                addLocation();
            } else if (e.getSource() == chosenLocations) {
                removeLocation();
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }
}
