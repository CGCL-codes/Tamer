package lug.serenity.npc.gui;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import lug.serenity.npc.gui.controls.ValuePanel;
import lug.serenity.npc.model.skills.GeneralSkill;
import lug.serenity.npc.model.skills.SkillData;
import lug.serenity.npc.model.skills.SkillSheet;
import lug.util.RandomFactory;
import abbot.finder.ComponentNotFoundException;
import abbot.finder.MultipleComponentsFoundException;
import abbot.finder.matchers.NameMatcher;
import abbot.tester.JTabbedPaneLocation;

/**
 * 
 */
public class SkillSheetPanelTest extends AbstractGeneratorTest {

    private static final int RANDOM_SKILL_COUNT = 6;

    private SkillSheetPanel skillSheetPanel;

    private JPanel generalSkillsPanel;

    private JScrollPane leftScroll;

    private JScrollPane rightScroll;

    private ChildSkillsPanel childSkillsPanel;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        tester.setAutoDelay(10);
        JTabbedPane tabPane = super.findTabbedPane(CharacterPanel.TAB_PANE);
        assertNotNull(tabPane);
        tabPaneTester.actionSelectTab(tabPane, new JTabbedPaneLocation("Skills"));
        skillSheetPanel = (SkillSheetPanel) findPanel(CharacterPanel.SKILL_PANEL);
        assertNotNull(skillSheetPanel);
        assertTrue(skillSheetPanel.isShowing());
    }

    @Override
    protected String getTestName() {
        return "Skills Panel Test";
    }

    private void findGeneralListPanel() throws ComponentNotFoundException, MultipleComponentsFoundException {
        if (generalSkillsPanel == null) {
            generalSkillsPanel = findPanel(SkillSheetPanel.GENERAL_SKILLS_PANEL);
            assertNotNull(generalSkillsPanel);
        }
    }

    private void findGeneralListPanelScroller() throws ComponentNotFoundException, MultipleComponentsFoundException {
        if (leftScroll == null) {
            leftScroll = findScrollPane(SkillSheetPanel.LEFT_SCROLLER);
            assertNotNull(leftScroll);
        }
    }

    private void findChildListPanel() throws ComponentNotFoundException, MultipleComponentsFoundException {
        if (childSkillsPanel == null) {
            Component c = findPanel(SkillSheetPanel.CHILD_SKILLS_PANEL);
            assertNotNull(generalSkillsPanel);
            assertTrue(c instanceof ChildSkillsPanel);
            childSkillsPanel = (ChildSkillsPanel) c;
        }
    }

    private void findChildListPanelScroller() throws ComponentNotFoundException, MultipleComponentsFoundException {
        if (rightScroll == null) {
            rightScroll = findScrollPane(SkillSheetPanel.RIGHT_SCROLLER);
            assertNotNull(rightScroll);
        }
    }

    private GeneralSkillPanel findSkillPanelFor(String name) throws ComponentNotFoundException, MultipleComponentsFoundException {
        if (generalSkillsPanel == null) {
            findGeneralListPanel();
        }
        Component c = find(SkillSheetPanel.PANEL_NAME + "." + name);
        assertNotNull(c);
        assertTrue(c instanceof GeneralSkillPanel);
        return (GeneralSkillPanel) c;
    }

    /**
	 * Test that the general skills can be selected and that the child skills
	 * panel updates to show the correct skills.
	 * @throws ComponentNotFoundException
	 * @throws MultipleComponentsFoundException
	 */
    public void XtestSelectGeneralSkills() throws ComponentNotFoundException, MultipleComponentsFoundException {
        findGeneralListPanelScroller();
        SkillSheet sheet = getPerson().getSkillSheet();
        String[] names = sheet.getSkillDataMap().keySet().toArray(new String[0]);
        Arrays.sort(names);
        JScrollBar scrollBar = leftScroll.getVerticalScrollBar();
        for (String name : names) {
            GeneralSkillPanel gsp = findSkillPanelFor(name);
            scrollBarTester.actionScrollTo(scrollBar, gsp.getBounds().y);
            tester.actionClick(gsp, centerOf(gsp));
            assertChildPanel(sheet.getNamed(name));
        }
    }

    /**
	 * Assert that the child skills panel is both showing
	 * and showing for the named general skill.
	 * @param namedSkill
	 * @throws MultipleComponentsFoundException 
	 * @throws ComponentNotFoundException 
	 */
    private void assertChildPanel(SkillData namedSkill) throws ComponentNotFoundException, MultipleComponentsFoundException {
        assertNotNull(namedSkill);
        findChildListPanel();
        assertTrue(childSkillsPanel.isShowing());
        assertEquals(namedSkill.getName(), childSkillsPanel.getSkillData().getName());
        for (String childSkillName : namedSkill.getChildren()) {
            ValuePanel vpanel = getChildSkillValuePanel(childSkillName);
            assertNotNull(vpanel);
        }
    }

    private List<GeneralSkill> randomSkillsList(int count) {
        List<GeneralSkill> ret = new ArrayList<GeneralSkill>(count);
        Random random = RandomFactory.getRandom();
        for (int i = 0; i < count; i++) {
            ret.add(GeneralSkill.values()[random.nextInt(GeneralSkill.length())]);
        }
        return ret;
    }

    /**
	 * Test that a general skill can be incremented to 6 but no further.
	 * @throws ComponentNotFoundException
	 * @throws MultipleComponentsFoundException
	 */
    public void testChangeGeneralSkills() throws ComponentNotFoundException, MultipleComponentsFoundException {
        findGeneralListPanelScroller();
        JScrollBar scrollBar = leftScroll.getVerticalScrollBar();
        getPerson().getSkillSheet().init();
        skillSheetPanel.dataChanged(getPerson(), getPerson());
        int originalTotal = getPerson().getSkillSheet().getTotalPoints();
        for (GeneralSkill generalSkill : randomSkillsList(RANDOM_SKILL_COUNT)) {
            SkillData skillData = findSkill(generalSkill);
            assertNotNull(skillData);
            assertEquals(0, skillData.getPoints());
            assertEquals(0, skillData.countChildrenWithPoints());
            assertEquals(0, skillData.getTotalPoints());
            GeneralSkillPanel gsp = findSkillPanelFor(skillData.getName());
            assertNotNull(gsp);
            assertEquals(skillData, gsp.getSkillData());
            scrollBarTester.actionScrollTo(scrollBar, gsp.getBounds().y);
            tester.actionClick(gsp, centerOf(gsp));
            JButton plusButton = (JButton) finder.find(gsp, new NameMatcher(GeneralSkillPanel.PLUS_BUTTON));
            JButton minusButton = (JButton) finder.find(gsp, new NameMatcher(GeneralSkillPanel.MINUS_BUTTON));
            assertEnabled(plusButton);
            assertDisabled(minusButton);
            int total = getPerson().getSkillSheet().getTotalPoints();
            buttonTester.actionClick(plusButton);
            assertEquals(2, skillData.getPoints());
            assertEnabled(plusButton);
            assertEnabled(minusButton);
            assertEquals(total + 2, getPerson().getSkillSheet().getTotalPoints());
            total = getPerson().getSkillSheet().getTotalPoints();
            buttonTester.actionClick(plusButton);
            assertEquals(4, skillData.getPoints());
            assertEnabled(plusButton);
            assertEnabled(minusButton);
            assertEquals(total + 2, getPerson().getSkillSheet().getTotalPoints());
            total = getPerson().getSkillSheet().getTotalPoints();
            buttonTester.actionClick(plusButton);
            assertEquals(6, skillData.getPoints());
            assertDisabled(plusButton);
            assertEnabled(minusButton);
            assertEquals(total + 2, getPerson().getSkillSheet().getTotalPoints());
            total = getPerson().getSkillSheet().getTotalPoints();
            assertChildPanelOperable(skillData, gsp);
            assertChildPanelAddNewSkill(skillData, gsp);
            buttonTester.actionClick(minusButton);
            assertEquals(4, skillData.getPoints());
            assertEnabled(plusButton);
            assertEnabled(minusButton);
            assertEquals(total - 2, getPerson().getSkillSheet().getTotalPoints());
            total = getPerson().getSkillSheet().getTotalPoints();
            buttonTester.actionClick(minusButton);
            assertEquals(2, skillData.getPoints());
            assertEnabled(plusButton);
            assertEnabled(minusButton);
            assertEquals(total - 2, getPerson().getSkillSheet().getTotalPoints());
            total = getPerson().getSkillSheet().getTotalPoints();
            buttonTester.actionClick(minusButton);
            assertEquals(0, skillData.getPoints());
            assertEnabled(plusButton);
            assertDisabled(minusButton);
            assertEquals(originalTotal, getPerson().getSkillSheet().getTotalPoints());
            total = getPerson().getSkillSheet().getTotalPoints();
        }
    }

    /**
	 * Assert that the child panel allows the addition of new skills
	 * Also checks that the given skill is at D6.
	 * @throws MultipleComponentsFoundException 
	 * @throws ComponentNotFoundException 
	 */
    private void assertChildPanelAddNewSkill(SkillData skillData, GeneralSkillPanel gsp) throws ComponentNotFoundException, MultipleComponentsFoundException {
        assert (skillData.getPoints() == 6);
        JButton plusButton = (JButton) finder.find(gsp, new NameMatcher(GeneralSkillPanel.PLUS_BUTTON));
        JButton minusButton = (JButton) finder.find(gsp, new NameMatcher(GeneralSkillPanel.MINUS_BUTTON));
        JButton addSkill = (JButton) finder.find(childSkillsPanel, new NameMatcher(ChildSkillsPanel.ADD_NEW_SKILL_BUTTON));
        assertNotNull(addSkill);
        int cnt = 0;
        while (!addSkill.isShowing() && cnt++ < 10) {
            tester.delay(125);
            addSkill = (JButton) finder.find(childSkillsPanel, new NameMatcher(ChildSkillsPanel.ADD_NEW_SKILL_BUTTON));
        }
        if (!addSkill.isShowing()) {
            return;
        }
        String childName = "NewSkill" + (random.nextInt(200) + 1);
        assertFalse(skillData.hasChild(childName));
        int old = skillData.getChildren().size();
        buttonTester.actionClick(addSkill);
        tester.actionKeyString(childName);
        tester.actionKeyStroke(KeyEvent.VK_ENTER);
        tester.actionKeyStroke(KeyEvent.VK_ENTER);
        assertEquals(old + 1, skillData.getChildren().size());
        assertTrue(skillData.hasChild(childName));
        JButton childPlusButton;
        JButton childMinusButton;
        ValuePanel vpanel = getChildSkillValuePanel(childName);
        assertNotNull(vpanel);
        assertTrue(vpanel.isShowing());
        childPlusButton = (JButton) finder.find(vpanel, new NameMatcher(ValuePanel.PLUS_BUTTON));
        childMinusButton = (JButton) finder.find(vpanel, new NameMatcher(ValuePanel.MINUS_BUTTON));
        assertEnabled(childPlusButton);
        assertDisabled(childMinusButton);
        int points = skillData.getChildPoints(childName);
        int skillMax = getPerson().getSkillMax() - 6;
        while (skillData.getChildPoints(childName) < skillMax) {
            buttonTester.actionClick(childPlusButton);
            assertEquals(points + 2, skillData.getChildPoints(childName));
            points = points + 2;
            assertButton(childPlusButton, points < skillMax);
            assertEnabled(childMinusButton);
            assertDisabled(plusButton);
            assertDisabled(minusButton);
        }
        while (skillData.getChildPoints(childName) > 0) {
            buttonTester.actionClick(childMinusButton);
            assertEquals(points - 2, skillData.getChildPoints(childName));
            points = points - 2;
            assertButton(childMinusButton, points > 0);
            assertButton(plusButton, false);
            assertButton(minusButton, points == 0);
        }
    }

    /**
	 * Assert that the child panel is operable for the given skill.
	 * Also checks that the given skill is at D6.
	 * @throws MultipleComponentsFoundException 
	 * @throws ComponentNotFoundException 
	 */
    private void assertChildPanelOperable(SkillData skillData, GeneralSkillPanel gsp) throws ComponentNotFoundException, MultipleComponentsFoundException {
        assert (skillData.getPoints() == 6);
        JButton plusButton = (JButton) finder.find(gsp, new NameMatcher(GeneralSkillPanel.PLUS_BUTTON));
        JButton minusButton = (JButton) finder.find(gsp, new NameMatcher(GeneralSkillPanel.MINUS_BUTTON));
        JButton addSkill = (JButton) finder.find(childSkillsPanel, new NameMatcher(ChildSkillsPanel.ADD_NEW_SKILL_BUTTON));
        assertNotNull(addSkill);
        int cnt = 0;
        while (!addSkill.isShowing() && cnt++ < 10) {
            tester.delay(125);
            addSkill = (JButton) finder.find(childSkillsPanel, new NameMatcher(ChildSkillsPanel.ADD_NEW_SKILL_BUTTON));
        }
        if (!addSkill.isShowing()) {
            return;
        }
        JButton childPlusButton;
        JButton childMinusButton;
        ArrayList<String> childNames = new ArrayList<String>();
        childNames.addAll(skillData.getChildren());
        int idx = 0;
        int max = (childNames.size()) / 2;
        while (childNames.size() > max) {
            idx = random.nextInt(childNames.size());
            String childName = childNames.get(idx);
            childNames.remove(idx);
            ValuePanel vpanel = getChildSkillValuePanel(childName);
            assertNotNull(vpanel);
            assertTrue(vpanel.isShowing());
            childPlusButton = (JButton) finder.find(vpanel, new NameMatcher(ValuePanel.PLUS_BUTTON));
            childMinusButton = (JButton) finder.find(vpanel, new NameMatcher(ValuePanel.MINUS_BUTTON));
            assertEnabled(childPlusButton);
            assertDisabled(childMinusButton);
            int points = skillData.getChildPoints(childName);
            int skillMax = getPerson().getSkillMax() - 6;
            while (skillData.getChildPoints(childName) < skillMax) {
                buttonTester.actionClick(childPlusButton);
                assertEquals(points + 2, skillData.getChildPoints(childName));
                points = points + 2;
                assertButton(childPlusButton, points < skillMax);
                assertEnabled(childMinusButton);
                assertDisabled(plusButton);
                assertDisabled(minusButton);
            }
            while (skillData.getChildPoints(childName) > 0) {
                buttonTester.actionClick(childMinusButton);
                if (points - 2 != skillData.getChildPoints(childName)) {
                    System.out.println(gsp.getName() + "." + childName + " expected " + (points - 2) + " found " + (skillData.getChildPoints(childName)) + " points");
                    tester.delay(1000 * 180);
                    fail("Points total wrong.");
                }
                points = points - 2;
                assertButton(childMinusButton, points > 0);
                assertButton(plusButton, false);
                assertButton(minusButton, points == 0);
            }
        }
    }

    /**
	 * Find a named skill
	 * @param name
	 * @return
	 */
    private SkillData findSkill(GeneralSkill skill) {
        return getPerson().getSkillSheet().getFor(skill);
    }

    /**
	 * Scroll the child skills pane to the given panel.
	 * @param vpanel
	 * @throws MultipleComponentsFoundException 
	 * @throws ComponentNotFoundException 
	 */
    protected void scrollToChild(ValuePanel vpanel) throws ComponentNotFoundException, MultipleComponentsFoundException {
        findChildListPanelScroller();
        if (rightScroll.getVerticalScrollBar().isVisible()) {
            scrollBarTester.actionScrollTo(rightScroll.getVerticalScrollBar(), vpanel.getBounds().y);
        }
    }

    /**
	 * @param childSkillName
	 * @throws ComponentNotFoundException
	 * @throws MultipleComponentsFoundException
	 */
    private ValuePanel getChildSkillValuePanel(String childSkillName) throws ComponentNotFoundException, MultipleComponentsFoundException {
        Component c = findIn(childSkillsPanel, ChildSkillsPanel.PANEL_NAME + "." + childSkillName);
        assertNotNull(c);
        assertTrue(c instanceof ValuePanel);
        return (ValuePanel) c;
    }
}
