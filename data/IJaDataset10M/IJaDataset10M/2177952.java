package org.deved.antlride.ui.dialogs;

import java.util.Arrays;
import org.deved.antlride.common.ui.AntlrImages;
import org.deved.antlride.core.AntlrCore;
import org.deved.antlride.core.model.IGrammar;
import org.deved.antlride.core.model.IRule;
import org.deved.antlride.core.model.test.ATestCase;
import org.deved.antlride.core.model.test.ATestSuite;
import org.deved.antlride.internal.ui.views.interpreter.AntlrInterpreterMessages;
import org.deved.antlride.ui.test.AntlrTestSuiteService;
import org.deved.antlride.ui.text.AntlrInputSourceViewer;
import org.deved.antlride.ui.viewers.AntlrModelElementContentProvider;
import org.deved.antlride.ui.viewers.AntlrModelElementLabelProvider;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.texteditor.ITextEditorActionConstants;

public class AntlrTestCaseViewer extends Viewer {

    private Composite fControl;

    private IRule fRule;

    private ComboViewer fRuleViewer;

    private ComboViewer fTestViewer;

    private ISelectionChangedListener fRuleSelectionChangedListener = new RuleSelectionChangedListener();

    private ISelectionChangedListener fTestSelectionChangedListener = new TestCaseSelectionChangedListener();

    private Action fRunAction;

    private AntlrInputSourceViewer fInputViewer;

    private ATestSuite testSuite;

    private Action fRuleActions;

    private Action fTestCaseActions;

    private boolean fEnabled = true;

    private boolean fUpdating;

    private ATestCase fLastCreatedTestCase;

    private class RuleSelectionChangedListener implements ISelectionChangedListener {

        public void selectionChanged(SelectionChangedEvent event) {
            IStructuredSelection selection = (IStructuredSelection) event.getSelection();
            IRule rule = (IRule) selection.getFirstElement();
            if (!fUpdating) {
                setInput(rule);
            }
            fireSelectionChanged(event);
        }
    }

    private class TestCaseContentAndLabelProvider extends LabelProvider implements IStructuredContentProvider {

        @Override
        public String getText(Object element) {
            ATestCase test = (ATestCase) element;
            return test.getName();
        }

        public Object[] getElements(Object input) {
            ATestCase[] tests = (ATestCase[]) input;
            return tests;
        }

        public void dispose() {
        }

        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        }
    }

    private class TestCaseSelectionChangedListener implements ISelectionChangedListener {

        public void selectionChanged(SelectionChangedEvent event) {
            IStructuredSelection selection = (IStructuredSelection) event.getSelection();
            ATestCase test = (ATestCase) selection.getFirstElement();
            setInputText(test.getInput());
            fireSelectionChanged(event);
        }
    }

    private class DropDownMenu extends Action implements IMenuCreator {

        private Menu fMenu;

        private Action[] fDelegates;

        public DropDownMenu(Action... delegates) {
            super(delegates[0].getText(), delegates.length > 1 ? IAction.AS_DROP_DOWN_MENU : IAction.AS_PUSH_BUTTON);
            setImageDescriptor(delegates[0].getImageDescriptor());
            setToolTipText(delegates[0].getToolTipText());
            setMenuCreator(this);
            fDelegates = delegates;
        }

        public void dispose() {
            if (fMenu != null) fMenu.dispose();
            fDelegates = null;
        }

        public Menu getMenu(Control parent) {
            fMenu = new Menu(parent);
            for (int i = 1; i < fDelegates.length; i++) {
                ActionContributionItem item = new ActionContributionItem(fDelegates[i]);
                item.fill(fMenu, -1);
            }
            return fMenu;
        }

        public Menu getMenu(Menu parent) {
            return null;
        }

        @Override
        public void run() {
            fDelegates[0].run();
        }

        @Override
        public void setEnabled(boolean enabled) {
            for (int i = 0; i < fDelegates.length; i++) {
                fDelegates[i].setEnabled(enabled);
            }
            super.setEnabled(enabled);
        }
    }

    private class SearchRuleAction extends Action {

        public SearchRuleAction() {
            setText(AntlrInterpreterMessages.GrammarInterpreter_Search_Rule);
            setToolTipText(AntlrInterpreterMessages.GrammarInterpreter_Search_Rule);
            setImageDescriptor(AntlrImages.getDescriptor(AntlrImages.SEARCH_HISTORY));
        }

        public void run() {
            if (fEnabled) {
                IGrammar grammar = fRule.getAdapter(IGrammar.class);
                FilteredElementsSelectionDialog<IRule> dlgFindRule = new AntlrSearchRuleDialog();
                dlgFindRule.setElements(Arrays.asList(grammar.getRules()));
                int status = dlgFindRule.open();
                if (Dialog.OK == status) {
                    Object rule = dlgFindRule.getFirstResult();
                    fRuleViewer.setSelection(new StructuredSelection(rule));
                }
            }
        }
    }

    private class SaveTestSuite implements IWorkspaceRunnable {

        public void run(IProgressMonitor monitor) throws CoreException {
            AntlrTestSuiteService.getInstance().saveTestSuite(testSuite);
        }
    }

    private class SearchTestCaseAction extends Action {

        public SearchTestCaseAction() {
            super(AntlrInterpreterMessages.GrammarInterpreter_Search_Testcase);
            setToolTipText(AntlrInterpreterMessages.GrammarInterpreter_Search_Testcase);
            setImageDescriptor(AntlrImages.getDescriptor(AntlrImages.SEARCH_HISTORY));
        }

        public void run() {
            if (fEnabled) {
                FilteredElementsSelectionDialog<ATestCase> dlgFindRule = new AntlrSearchTestCaseDialog(getControl().getShell());
                dlgFindRule.setElements(Arrays.asList(testSuite.getTests(fRule.getElementName())));
                int status = dlgFindRule.open();
                if (Dialog.OK == status) {
                    Object selection = dlgFindRule.getFirstResult();
                    fInputViewer.setSelection(new StructuredSelection(selection));
                }
            }
        }
    }

    private class SaveTestCaseAction extends Action {

        private boolean saveAs;

        public SaveTestCaseAction(boolean saveAs) {
            this.saveAs = saveAs;
            setText(saveAs ? AntlrInterpreterMessages.GrammarInterpreter_SaveAs_Testcase : AntlrInterpreterMessages.GrammarInterpreter_Save_Testcase);
            setToolTipText(saveAs ? AntlrInterpreterMessages.GrammarInterpreter_SaveAs_Testcase : AntlrInterpreterMessages.GrammarInterpreter_Save_Testcase);
            setImageDescriptor(saveAs ? AntlrImages.getDescriptor(AntlrImages.SAVE_AS_TEST_CASE) : AntlrImages.getDescriptor(AntlrImages.SAVE_TEST_CASE));
        }

        public void run() {
            StructuredSelection selection = (StructuredSelection) fTestViewer.getSelection();
            fLastCreatedTestCase = null;
            ATestCase testCase = (ATestCase) selection.getFirstElement();
            if (testCase == null || saveAs) {
                InputDialog dlg = new InputDialog(Display.getCurrent().getActiveShell(), "Save Test", "Test name", null, new IInputValidator() {

                    public String isValid(String newText) {
                        if (newText == null || newText.length() == 0) return "Test name is required";
                        return null;
                    }
                });
                if (dlg.open() == Window.OK) {
                    String testName = dlg.getValue();
                    String ruleName = fRule.getElementName();
                    testCase = testSuite.findTest(ruleName, testName);
                    if (testCase == null) {
                        testCase = testSuite.createTest(ruleName, testName, getInputText());
                        testSuite.addTest(testCase);
                        fLastCreatedTestCase = testCase;
                    } else {
                        testCase.setInput(getInputText());
                    }
                    fTestViewer.setInput(testSuite.getTests(ruleName));
                    fTestViewer.setSelection(new StructuredSelection(testCase));
                    saveTestSuite();
                }
            } else {
                testCase.setInput(getInputText());
                saveTestSuite();
            }
        }
    }

    private class LoadTestCaseAction extends Action {

        public LoadTestCaseAction() {
            setText(AntlrInterpreterMessages.GrammarInterpreter_Load_Testcase);
            setToolTipText(AntlrInterpreterMessages.GrammarInterpreter_Load_Testcase);
        }

        public void run() {
            if (fEnabled) {
                Shell shell = new Shell();
                AntlrPromptFileDialog promptFileDialog = new AntlrPromptFileDialog(getText(), shell);
                if (promptFileDialog.open() == Dialog.OK) {
                    String filename = promptFileDialog.getFile();
                    if (filename != null) {
                        setInputText(promptFileDialog.getFileContent());
                    }
                }
                shell.dispose();
            }
        }
    }

    public AntlrTestCaseViewer(Composite parent, Action ruleAction) {
        fControl = new Composite(parent, SWT.NONE);
        fControl.setLayout(new GridLayout(3, false));
        fRunAction = ruleAction;
        createContent(fControl);
    }

    protected void createContent(Composite parent) {
        createRulePart(parent);
        createTestCasePart(parent);
        fInputViewer = new AntlrInputSourceViewer(parent);
        GridData gd = new GridData(GridData.FILL_BOTH);
        gd.horizontalSpan = 3;
        fInputViewer.getControl().setLayoutData(gd);
    }

    protected void createRulePart(Composite parent) {
        Label label = new Label(parent, SWT.NONE);
        label.setText("Rule: ");
        fRuleViewer = new ComboViewer(parent);
        fRuleViewer.setLabelProvider(new AntlrModelElementLabelProvider());
        fRuleViewer.setContentProvider(new AntlrModelElementContentProvider());
        GridData gridData = new GridData(GridData.GRAB_HORIZONTAL);
        gridData.horizontalAlignment = GridData.FILL;
        gridData.horizontalIndent = 5;
        fRuleViewer.getCombo().setLayoutData(gridData);
        fRuleViewer.addSelectionChangedListener(fRuleSelectionChangedListener);
        ToolBar toolBar = new ToolBar(parent, SWT.RIGHT | SWT.FLAT);
        ToolBarManager manager = new ToolBarManager(toolBar);
        Action[] actions = new Action[fRunAction == null ? 1 : 2];
        int index = 0;
        if (fRunAction != null) {
            actions[index++] = fRunAction;
        }
        actions[index] = new SearchRuleAction();
        fRuleActions = new DropDownMenu(actions);
        manager.add(fRuleActions);
        manager.update(true);
    }

    public void menuAboutToShow(IMenuManager menu) {
        menu.add(fInputViewer.getAction(ITextEditorActionConstants.CUT));
        menu.add(fInputViewer.getAction(ITextEditorActionConstants.COPY));
        menu.add(fInputViewer.getAction(ITextEditorActionConstants.PASTE));
        menu.add(new Separator());
        menu.add(fInputViewer.getAction(ITextEditorActionConstants.SELECT_ALL));
    }

    protected void createTestCasePart(Composite parent) {
        Label label = new Label(parent, SWT.NONE);
        label.setText("Test: ");
        fTestViewer = new ComboViewer(parent);
        TestCaseContentAndLabelProvider provider = new TestCaseContentAndLabelProvider();
        fTestViewer.setLabelProvider(provider);
        fTestViewer.setContentProvider(provider);
        GridData gridData = new GridData(GridData.GRAB_HORIZONTAL);
        gridData.horizontalAlignment = GridData.FILL;
        gridData.horizontalIndent = 5;
        fTestViewer.getCombo().setLayoutData(gridData);
        fTestViewer.addSelectionChangedListener(fTestSelectionChangedListener);
        ToolBar toolBar = new ToolBar(parent, SWT.RIGHT | SWT.FLAT);
        ToolBarManager manager = new ToolBarManager(toolBar);
        fTestCaseActions = new DropDownMenu(new SaveTestCaseAction(false), new SaveTestCaseAction(true), new LoadTestCaseAction(), new SearchTestCaseAction());
        manager.add(fTestCaseActions);
        manager.update(true);
    }

    protected void setInputText(String input) {
        getTextWidget().setText(input);
    }

    @Override
    public Control getControl() {
        return fControl;
    }

    @Override
    public Object getInput() {
        return fRule;
    }

    @Override
    public ISelection getSelection() {
        return fRuleViewer.getSelection();
    }

    public ATestCase getSelectedTestCase() {
        IStructuredSelection selection = (IStructuredSelection) fTestViewer.getSelection();
        ATestCase result = selection.isEmpty() ? fLastCreatedTestCase : (ATestCase) selection.getFirstElement();
        return result;
    }

    @Override
    public void refresh() {
        fRuleActions.setEnabled(fEnabled);
        fTestCaseActions.setEnabled(fEnabled);
        fInputViewer.setEditable(fEnabled);
        fRuleViewer.setInput(null);
        fTestViewer.setInput(null);
        setInputText("");
    }

    @Override
    public void setInput(Object input) {
        fRule = (IRule) input;
        fEnabled = fRule != null;
        refresh();
        if (fEnabled) {
            IGrammar grammar = fRule.getAdapter(IGrammar.class);
            fRuleViewer.setInput(grammar);
            fUpdating = true;
            fRuleViewer.setSelection(new StructuredSelection(fRule));
            fUpdating = false;
            setDefaultTestCase();
        }
    }

    private void setDefaultTestCase() {
        IGrammar grammar = fRule.getAdapter(IGrammar.class);
        testSuite = AntlrTestSuiteService.getInstance().loadTestSuite(grammar);
        ATestCase[] tests = testSuite.getTests(fRule.getElementName());
        ATestCase defaultTestCase = testSuite.getDefaultTestCase(fRule.getElementName());
        fTestViewer.setInput(tests);
        String input = "";
        if (defaultTestCase != null) {
            fTestViewer.setSelection(new StructuredSelection(defaultTestCase));
            input = defaultTestCase.getInput();
        }
        fInputViewer.setGrammar(grammar);
        setInputText(input);
    }

    @Override
    public void setSelection(ISelection selection, boolean reveal) {
        fRuleViewer.setSelection(selection, reveal);
    }

    public StyledText getTextWidget() {
        return fInputViewer.getTextWidget();
    }

    private String getInputText() {
        return getTextWidget().getText();
    }

    public void saveTestSuite() {
        try {
            ResourcesPlugin.getWorkspace().run(new SaveTestSuite(), new NullProgressMonitor());
        } catch (CoreException e) {
            AntlrCore.error(e);
        }
    }

    public void dispose() {
        if (fRuleViewer != null) {
            fRuleViewer.removeSelectionChangedListener(fRuleSelectionChangedListener);
            fRuleViewer = null;
        }
        if (fTestViewer != null) {
            fTestViewer.removeSelectionChangedListener(fTestSelectionChangedListener);
            fTestViewer = null;
        }
        if (fInputViewer != null) {
            fInputViewer.dispose();
            fInputViewer = null;
        }
    }

    public boolean isEnabled() {
        return fEnabled;
    }

    public void setEnabled(boolean enabled) {
        fEnabled = enabled;
    }
}
