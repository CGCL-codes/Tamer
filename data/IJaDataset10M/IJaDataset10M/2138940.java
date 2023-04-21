package pcgen.gui.tabs;

import pcgen.gui.tools.CharacterStateEditable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import pcgen.gui.PCGenUIManager;
import pcgen.gui.facade.AbilityCatagoryFacade;
import pcgen.gui.facade.CharacterFacade;
import pcgen.gui.util.DefaultGenericListModel;
import pcgen.gui.util.GenericListModel;
import pcgen.gui.util.event.AbstractGenericListDataWrapper;
import pcgen.util.CollectionMaps;
import pcgen.util.ListMap;

/**
 *
 * @author Connor Petty <cpmeister@users.sourceforge.net>
 */
public class AbilitiesInfoTab extends JTabbedPane implements CharacterStateEditable {

    private final AbilityChooserTab abilityTab;

    private Map<String, Hashtable<Object, Object>> tabStates = null;

    private String selectedTitle;

    public AbilitiesInfoTab() {
        this.abilityTab = new AbilityChooserTab();
        initComponents();
    }

    private void initComponents() {
        addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                abilityTab.storeState(tabStates.get(selectedTitle));
                if (getSelectedIndex() != -1) {
                    selectedTitle = getTitleAt(getSelectedIndex());
                    abilityTab.restoreState(tabStates.get(selectedTitle));
                }
            }
        });
    }

    public Hashtable<Object, Object> createState(final CharacterFacade character) {
        HashMap<String, Hashtable<Object, Object>> tabs = new HashMap<String, Hashtable<Object, Object>>();
        List<String> titles = new ArrayList<String>();
        @SuppressWarnings("unchecked") final ListMap<String, AbilityCatagoryFacade, DefaultGenericListModel<AbilityCatagoryFacade>> catagoryListMap = CollectionMaps.createListMap(HashMap.class, DefaultGenericListModel.class);
        AbstractGenericListDataWrapper<AbilityCatagoryFacade> listener = new AbstractGenericListDataWrapper<AbilityCatagoryFacade>() {

            @Override
            protected void addData(Collection<? extends AbilityCatagoryFacade> data) {
                @SuppressWarnings("unchecked") ListMap<String, AbilityCatagoryFacade, ArrayList<AbilityCatagoryFacade>> catagorySubListMap = CollectionMaps.createListMap(HashMap.class, ArrayList.class);
                for (AbilityCatagoryFacade catagory : data) {
                    catagorySubListMap.add(catagory.getType(), catagory);
                }
                for (String type : catagorySubListMap.keySet()) {
                    catagoryListMap.addAll(type, catagorySubListMap.get(type));
                }
            }

            @Override
            protected void removeData(Collection<? extends AbilityCatagoryFacade> data) {
                @SuppressWarnings("unchecked") ListMap<String, AbilityCatagoryFacade, ArrayList<AbilityCatagoryFacade>> catagorySubListMap = CollectionMaps.createListMap(HashMap.class, ArrayList.class);
                for (AbilityCatagoryFacade catagory : data) {
                    catagorySubListMap.add(catagory.getType(), catagory);
                }
                for (String type : catagorySubListMap.keySet()) {
                    catagoryListMap.removeAll(type, catagorySubListMap.get(type));
                }
            }
        };
        listener.setModel(character.getDataSet().getAbilityCatagories());
        for (String type : catagoryListMap.keySet()) {
            titles.add(type);
            tabs.put(type, abilityTab.createState(character, catagoryListMap.get(type)));
        }
        Hashtable<Object, Object> state = new Hashtable<Object, Object>();
        state.put("Titles", titles);
        state.put("Tabs", tabs);
        state.put("SelectedTitle", titles.get(0));
        return state;
    }

    public void storeState(Hashtable<Object, Object> state) {
        state.put("SelectedTitle", selectedTitle);
    }

    @SuppressWarnings("unchecked")
    public void restoreState(Hashtable<?, ?> state) {
        removeAll();
        List<String> titles = (List<String>) state.get("Titles");
        for (String title : titles) {
            addTab(title, abilityTab);
        }
        tabStates = (Map<String, Hashtable<Object, Object>>) state.get("Tabs");
        selectedTitle = (String) state.get("SelectedTitle");
        setSelectedIndex(indexOfTab(selectedTitle));
    }
}
