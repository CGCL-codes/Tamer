package abbot.swt.wtp.tester;

import junit.framework.Assert;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.wst.common.project.facet.core.IPreset;
import org.eclipse.wst.common.project.facet.ui.PresetSelectionPanel;
import abbot.swt.eclipse.tester.WizardTester;
import abbot.swt.finder.MultipleFoundException;
import abbot.swt.finder.NotFoundException;
import abbot.swt.matcher.ClassMatcher;
import abbot.swt.matcher.LabeledTextMatcher;
import abbot.swt.matcher.Matcher;
import abbot.swt.script.Condition;
import abbot.swt.tester.Action;
import abbot.swt.tester.CComboTester;
import abbot.swt.tester.ComboTester;
import abbot.swt.tester.ItemPath;
import abbot.swt.tester.TextTester;
import abbot.swt.tester.TreeItemTester;
import abbot.swt.tester.TreeTester;
import abbot.swt.utilities.Bundles;
import abbot.swt.utilities.Wait;

public class DynamicWebProjectWizardTester extends WizardTester {

    public DynamicWebProjectWizardTester(WizardDialog dialog) {
        super(dialog);
    }

    public DynamicWebProjectWizardTester(Shell shell) {
        super(shell);
    }

    public static final ItemPath WIZARD_PATH = new ItemPath(new String[] { Bundles.getString("org.eclipse.jst.servlet.ui", "web.category_ui_"), Bundles.getString("org.eclipse.jst.servlet.ui", "web.project_ui_") });

    /**
	 * The title of the Dynamic Web Project wizard's main page: "Dynamic Web Project".
	 */
    @SuppressWarnings("restriction")
    public static final String MAIN_PAGE_TITLE = org.eclipse.jst.servlet.ui.internal.plugin.WEBUIMessages.WEB_PROJECT_MAIN_PG_TITLE;

    /**
	 * The Label text of the Dynamic Web Project wizard's project name Text.
	 */
    @SuppressWarnings("restriction")
    public static final String PROJECT_NAME_LABEL_TEXT = org.eclipse.wst.common.frameworks.internal.ui.WTPCommonUIResourceHandler.Name_;

    private static final class FacetsSelectionPageResources extends NLS {

        public static String pageTitle;

        public static String pageDescription;

        @SuppressWarnings("restriction")
        private static final String FACET_SELECTION_PAGE_CLASS_NAME = org.eclipse.wst.common.project.facet.ui.internal.FacetsSelectionPage.class.getName();

        static {
            initializeMessages(FACET_SELECTION_PAGE_CLASS_NAME, FacetsSelectionPageResources.class);
        }
    }

    /**
	 * The title of the facet selection page: "Project Facets".
	 */
    public static final String FACET_PAGE_TITLE = FacetsSelectionPageResources.pageTitle;

    /**
	 * Launches a Dynamic Web Project wizard. Launches the New Project wizard, selects Dynamic Web
	 * Project, clicks Next, and waits for the main page to be shown.
	 * 
	 * @return a {@link DynamicWebProjectWizardTester} for the launched WizardDialog (which will be
	 *         on its main page).
	 */
    @Action
    public static DynamicWebProjectWizardTester launchWizard() {
        DynamicWebProjectWizardTester tester = launchProjectWizard(DynamicWebProjectWizardTester.class, WIZARD_PATH);
        Assert.assertEquals(MAIN_PAGE_TITLE, tester.getCurrentPage().getTitle());
        return tester;
    }

    public Text getProjectNameText() throws NotFoundException, MultipleFoundException {
        return (Text) findCurrent(new LabeledTextMatcher(PROJECT_NAME_LABEL_TEXT));
    }

    @Action
    public void enterProjectName(String projectName) throws NotFoundException, MultipleFoundException {
        Text projectNameText = getProjectNameText();
        TextTester.getTextTester().key(projectNameText, projectName);
    }

    public Combo getPresetsCombo() throws NotFoundException, MultipleFoundException {
        PresetSelectionPanel panel = (PresetSelectionPanel) findCurrent(new ClassMatcher<Widget>(PresetSelectionPanel.class));
        return (Combo) find(panel, new ClassMatcher<Widget>(Combo.class));
    }

    @Action
    public void selectPresetLabel(String presetLabel) throws NotFoundException, MultipleFoundException {
        Combo combo = getPresetsCombo();
        ComboTester.getComboTester().selectItem(combo, presetLabel);
    }

    @Action
    public void selectPresetId(String presetId) throws NotFoundException, MultipleFoundException {
        String presetLabel = getPresetLabel(presetId);
        selectPresetLabel(presetLabel);
    }

    @SuppressWarnings("restriction")
    private String getPresetLabel(String presetId) {
        IPreset preset = org.eclipse.wst.common.project.facet.core.internal.PresetsExtensionPoint.getPreset(presetId);
        return preset.getLabel();
    }

    @Action
    public void selectFacet(String facetName, final String facetVersion) throws NotFoundException, MultipleFoundException {
        final Tree facetTree = (Tree) findCurrent(new ClassMatcher<Widget>(Tree.class));
        TreeTester treeTester = TreeTester.getTreeTester();
        TreeItem facetItem = treeTester.clickItem(facetTree, facetName);
        treeTester.checkItem(facetTree, facetName, true);
        if (facetVersion != null) {
            TreeItemTester itemTester = TreeItemTester.getTreeItemTester();
            Rectangle bounds1 = itemTester.getTextBounds(facetItem, 1);
            Point p = treeTester.toDisplay(facetTree, bounds1.x, bounds1.y);
            int x = p.x + bounds1.width / 2;
            int y = p.y + bounds1.height / 2;
            displayTester().click(x, y, SWT.BUTTON1);
            final Matcher<Widget> matcher = new ClassMatcher<Widget>(CCombo.class);
            final CCombo[] ref = new CCombo[1];
            Wait.wait(new Condition() {

                public boolean test() {
                    try {
                        ref[0] = (CCombo) find(facetTree, matcher);
                        return true;
                    } catch (Exception e) {
                        if (e.getCause() instanceof NotFoundException) return false;
                        if (e instanceof RuntimeException) throw (RuntimeException) e;
                        throw new RuntimeException(e);
                    }
                }
            }, Wait.SHORT_TIMEOUT);
            final CCombo combo = ref[0];
            final CComboTester comboTester = CComboTester.getCComboTester();
            comboTester.selectItem(combo, facetVersion);
            Wait.wait(new Condition() {

                public boolean test() {
                    return facetVersion.equals(comboTester.getSelectedItem(combo));
                }
            }, Wait.DEFAULT_TIMEOUT);
        }
    }
}
