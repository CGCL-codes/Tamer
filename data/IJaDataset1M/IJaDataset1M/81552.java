package lug.serenity.npc.gui;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.ListModel;
import lug.EquipmentList;
import lug.MutableList;
import lug.serenity.npc.gui.equipment.EquipmentSelector;
import lug.serenity.npc.gui.equipment.EquipmentTab;
import lug.serenity.npc.gui.equipment.ExplosivesTab;
import lug.serenity.npc.model.equipment.Equipment;
import lug.serenity.npc.model.equipment.Explosive;
import abbot.finder.ComponentNotFoundException;
import abbot.finder.MultipleComponentsFoundException;
import abbot.finder.matchers.NameMatcher;
import abbot.tester.JTabbedPaneLocation;

/**
 * 
 */
public class ExplosivesTabTest extends AbstractGeneratorTest {

    private ExplosivesTab equipmentPanel;

    private EquipmentSelector<Equipment> selector;

    private JButton addButton;

    private JButton removeButton;

    private JList selectedItemsList;

    private JList catalogItemsList;

    private JScrollPane selectedScroll;

    private JScrollPane catalogScroll;

    /** Number of items to add then remove during the tests.*/
    private static final int NUMBER_OF_ITEMS = 5;

    @SuppressWarnings("unchecked")
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        JTabbedPane tabPane = super.findTabbedPane(CharacterPanel.TAB_PANE);
        assertNotNull(tabPane);
        tabPaneTester.actionSelectTab(tabPane, new JTabbedPaneLocation("Explosives"));
        equipmentPanel = (ExplosivesTab) findPanel(CharacterPanel.EXPLOSIVES_TAB);
        assertShowing(equipmentPanel);
        selector = (EquipmentSelector<Equipment>) finder.find(equipmentPanel, new NameMatcher(EquipmentTab.EQUIPMENT_SELECTOR));
        assertShowing(selector);
        findCommonComponents();
    }

    /**
	 * Find the components used by the selector
	 * @throws ComponentNotFoundException
	 * @throws MultipleComponentsFoundException
	 */
    private void findCommonComponents() throws ComponentNotFoundException, MultipleComponentsFoundException {
        addButton = findButtonIn(selector, EquipmentSelector.ADD_BUTTON);
        removeButton = findButtonIn(selector, EquipmentSelector.REMOVE_BUTTON);
        selectedItemsList = findListIn(selector, EquipmentSelector.SELECTED_LIST);
        catalogItemsList = findListIn(selector, EquipmentSelector.CATALOG_LIST);
        selectedScroll = findScrollPaneIn(selector, EquipmentSelector.SELECTED_SCROLL);
        catalogScroll = findScrollPaneIn(selector, EquipmentSelector.CATALOG_SCROLL);
        assertShowing(addButton);
        assertShowing(removeButton);
        assertShowing(selectedItemsList);
        assertShowing(catalogItemsList);
        assertShowing(selectedScroll);
        assertShowing(catalogScroll);
    }

    @Override
    protected String getTestName() {
        return "Explosives test";
    }

    /**
	 * Test adding and removing with the add/remove buttons
	 * @throws MultipleComponentsFoundException 
	 * @throws ComponentNotFoundException 
	 */
    public void testAddRemoveWithButtons() throws ComponentNotFoundException, MultipleComponentsFoundException {
        MutableList<Explosive> equipmentList = getPerson().getExplosives();
        ListModel catalogModel = catalogItemsList.getModel();
        assertEquals(0, equipmentList.size());
        assertEquals(0, selectedItemsList.getModel().getSize());
        assertTrue(catalogModel.getSize() > 0);
        double weightAdded = 0;
        double costAdded = 0;
        for (int i = 0; i < NUMBER_OF_ITEMS; i++) {
            int idx = random.nextInt(catalogModel.getSize());
            Equipment selected = (Equipment) catalogModel.getElementAt(idx);
            listTester.actionSelectIndex(catalogItemsList, idx);
            weightAdded = weightAdded + (selected.getWeight());
            costAdded = costAdded + (selected.getCost());
            assertEnabled(addButton);
            buttonTester.actionClick(addButton);
            assertEquals(i + 1, equipmentList.size());
            assertEquals(weightAdded, getPerson().getCarriedWeight());
            assertEquals(costAdded, getPerson().getEquipmentCost());
        }
        for (int i = 0; i < NUMBER_OF_ITEMS; i++) {
            int idx = random.nextInt(equipmentList.getSize());
            Equipment selected = equipmentList.get(idx);
            listTester.actionSelectIndex(selectedItemsList, idx);
            weightAdded = weightAdded - (selected.getWeight());
            costAdded = costAdded - (selected.getCost());
            assertEnabled(removeButton);
            buttonTester.actionClick(removeButton);
            assertEquals(NUMBER_OF_ITEMS - i - 1, equipmentList.size());
            assertEquals(Math.round(weightAdded), Math.round(getPerson().getCarriedWeight()));
            assertEquals(Math.round(costAdded), Math.round(getPerson().getEquipmentCost()));
        }
    }

    /**
	 * Test adding and removing with the double clicks instead of the buttons.
	 * (Buttons are still checked for enabling though).
	 * @throws MultipleComponentsFoundException 
	 * @throws ComponentNotFoundException 
	 */
    public void testAddRemoveWithDoubleClick() throws ComponentNotFoundException, MultipleComponentsFoundException {
        EquipmentList<Explosive> equipmentList = getPerson().getExplosives();
        ListModel catalogModel = catalogItemsList.getModel();
        assertEquals(0, equipmentList.size());
        assertEquals(0, selectedItemsList.getModel().getSize());
        assertTrue(catalogModel.getSize() > 0);
        double weightAdded = 0;
        double costAdded = 0;
        int listSize = equipmentList.size();
        for (int i = 0; i < NUMBER_OF_ITEMS; i++) {
            int idx = random.nextInt(catalogModel.getSize());
            Equipment selected = (Equipment) catalogModel.getElementAt(idx);
            catalogItemsList.setSelectedIndex(idx);
            tester.delay(75);
            selector.doQuickAdd(idx);
            int added = equipmentList.size() - listSize;
            listSize = equipmentList.size();
            weightAdded = weightAdded + (selected.getWeight() * added);
            costAdded = costAdded + (selected.getCost() * added);
            assertEquals(Math.round(weightAdded), Math.round(getPerson().getCarriedWeight()));
            assertEquals(Math.round(costAdded), Math.round(getPerson().getEquipmentCost()));
        }
        while (equipmentList.size() > 0) {
            int idx = random.nextInt(equipmentList.getSize());
            Equipment selected = equipmentList.get(idx);
            selectedItemsList.setSelectedIndex(idx);
            tester.delay(75);
            selector.doQuickRemove(idx);
            weightAdded = weightAdded - selected.getWeight();
            costAdded = costAdded - selected.getCost();
            assertEquals(equipmentList.getWeightTotal(), getPerson().getCarriedWeight());
            assertEquals(equipmentList.getCostTotal(), getPerson().getEquipmentCost());
        }
    }
}
