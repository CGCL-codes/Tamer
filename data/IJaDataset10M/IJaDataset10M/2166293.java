package edu.zao.fire.editors.list;

import java.util.Arrays;
import java.util.List;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import edu.zao.fire.ListRule;
import edu.zao.fire.ListRule.ListStyle;
import edu.zao.fire.RenamerRule;
import edu.zao.fire.editors.RenamerRuleEditor;
import edu.zao.fire.rcp.Activator;

public class ListRuleEditor extends RenamerRuleEditor {

    public static final String ID = "file-utils.editors.list";

    private ListRule inputRule;

    private Button radioAscending;

    private Button radioDescending;

    private Button radioAlphabetical;

    private Button radioNumerical;

    private Button radioRoman;

    private Button radioAddToStart;

    private Button radioAddToEnd;

    private Text startFrom;

    private Text seperatorToken;

    private Spinner numDigitsSpinner;

    @Override
    public RenamerRule getRule() {
        return inputRule;
    }

    @Override
    public void init(IEditorSite site, IEditorInput input) throws PartInitException {
        setSite(site);
        setInput(input);
        inputRule = ((ListRuleEditorInput) input).getRule();
        setPartName(input.getName());
    }

    @Override
    public void createPartControl(Composite parent) {
        GridLayout parentLayout = new GridLayout(1, false);
        parentLayout.marginRight = 5;
        parent.setLayout(parentLayout);
        Composite topArea = new Composite(parent, SWT.NONE);
        topArea.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
        topArea.setLayout(new FillLayout());
        radioAscending = new Button(topArea, SWT.RADIO);
        radioAscending.setText("Ascending");
        radioDescending = new Button(topArea, SWT.RADIO);
        radioDescending.setText("Descending");
        Label topSeparator = new Label(parent, SWT.HORIZONTAL | SWT.SEPARATOR);
        topSeparator.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
        Composite midArea = new Composite(parent, SWT.NONE);
        midArea.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
        GridLayout midAreaLayout = new GridLayout(3, false);
        midAreaLayout.marginHeight = 0;
        midAreaLayout.marginWidth = 0;
        midArea.setLayout(midAreaLayout);
        Composite typeRadioArea = new Composite(midArea, SWT.NONE);
        GridLayout typeRadioAreaLayout = new GridLayout(1, false);
        typeRadioAreaLayout.marginWidth = 0;
        typeRadioAreaLayout.marginHeight = 0;
        typeRadioArea.setLayout(typeRadioAreaLayout);
        GridData typeRadioAreaData = new GridData(SWT.LEFT, SWT.TOP, false, false);
        typeRadioAreaData.verticalSpan = 2;
        typeRadioArea.setLayoutData(typeRadioAreaData);
        radioAlphabetical = new Button(typeRadioArea, SWT.RADIO);
        radioAlphabetical.setText("a, b, c...");
        radioNumerical = new Button(typeRadioArea, SWT.RADIO);
        radioNumerical.setText("1, 2, 3...");
        radioRoman = new Button(typeRadioArea, SWT.RADIO);
        radioRoman.setText("i, ii, iii...");
        Label startFromLabel = new Label(midArea, SWT.SINGLE);
        startFromLabel.setText("Start from: ");
        startFromLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false));
        startFrom = new Text(midArea, SWT.SINGLE | SWT.BORDER);
        GridData startFromTextData = new GridData(SWT.FILL, SWT.CENTER, true, true);
        startFrom.setLayoutData(startFromTextData);
        Label numDigitsLabel = new Label(midArea, SWT.SINGLE);
        numDigitsLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false));
        numDigitsLabel.setText("Digits Displayed: ");
        numDigitsSpinner = new Spinner(midArea, SWT.WRAP | SWT.BORDER);
        numDigitsSpinner.setValues(0, 0, 6, 0, 1, 1);
        numDigitsSpinner.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));
        Label midSeparator = new Label(parent, SWT.HORIZONTAL | SWT.SEPARATOR);
        midSeparator.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
        Composite bottomArea = new Composite(parent, SWT.NONE);
        FillLayout bottomAreaLayout = new FillLayout();
        bottomAreaLayout.marginHeight = 0;
        bottomAreaLayout.marginWidth = 0;
        bottomArea.setLayout(bottomAreaLayout);
        bottomArea.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
        Label addToLabel = new Label(bottomArea, SWT.SINGLE);
        addToLabel.setText("Add to:");
        radioAddToStart = new Button(bottomArea, SWT.RADIO);
        radioAddToStart.setText("Start");
        radioAddToEnd = new Button(bottomArea, SWT.RADIO);
        radioAddToEnd.setText("End");
        Composite bottomArea2 = new Composite(parent, SWT.NONE);
        bottomArea2.setLayout(bottomAreaLayout);
        bottomArea2.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
        Label sepWithLabel = new Label(bottomArea2, SWT.SINGLE);
        sepWithLabel.setText("Separate with: ");
        seperatorToken = new Text(bottomArea2, SWT.SINGLE | SWT.BORDER);
        selectRuleConfigurationInUI();
        addRuleModificationListeners();
    }

    private void addRuleModificationListeners() {
        final ControlDecoration badSeperatorNotification = new ControlDecoration(seperatorToken, SWT.RIGHT | SWT.TOP);
        badSeperatorNotification.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_DEC_FIELD_ERROR));
        badSeperatorNotification.hide();
        final ControlDecoration badStartFromNotification = new ControlDecoration(startFrom, SWT.RIGHT | SWT.TOP);
        badStartFromNotification.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_DEC_FIELD_ERROR));
        badStartFromNotification.hide();
        badStartFromNotification.setDescriptionText("Start from must be numeric");
        ModifyListener numDigitsListener = new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                inputRule.setDigitsDisplayed(numDigitsSpinner.getSelection());
                fireRuleChanged(inputRule);
            }
        };
        numDigitsSpinner.addModifyListener(numDigitsListener);
        ModifyListener startFromModifiedListener = new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                boolean isGood = true;
                try {
                    Integer.parseInt(startFrom.getText());
                } catch (NumberFormatException f) {
                    isGood = false;
                }
                if (isGood) {
                    badStartFromNotification.hide();
                    inputRule.setStartFrom(Integer.parseInt(startFrom.getText()));
                    fireRuleChanged(inputRule);
                } else {
                    badStartFromNotification.show();
                }
            }
        };
        startFrom.addModifyListener(startFromModifiedListener);
        VerifyListener seperatorTokenVerifyListener = new VerifyListener() {

            @Override
            public void verifyText(VerifyEvent e) {
                List<Character> badChars = Arrays.asList('<', '>', ':', '\"', '/', '\\', '|', '?', '*');
                if (badChars.contains(e.character)) {
                    badSeperatorNotification.setDescriptionText("The character \"" + e.character + "\" is not allowed");
                    badSeperatorNotification.show();
                    e.doit = false;
                } else {
                    badSeperatorNotification.hide();
                }
            }
        };
        seperatorToken.addVerifyListener(seperatorTokenVerifyListener);
        ModifyListener seperatorTokenModifiedListener = new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                inputRule.setSeperatorToken(seperatorToken.getText());
                fireRuleChanged(inputRule);
            }
        };
        seperatorToken.addModifyListener(seperatorTokenModifiedListener);
        SelectionAdapter ascendingListener = new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                inputRule.setAscending(true);
                fireRuleChanged(inputRule);
            }
        };
        radioAscending.addSelectionListener(ascendingListener);
        SelectionAdapter descendingListener = new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                inputRule.setAscending(false);
                fireRuleChanged(inputRule);
            }
        };
        radioDescending.addSelectionListener(descendingListener);
        SelectionAdapter alphabeticalListener = new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                inputRule.setListStyleState(ListStyle.ALPHABETICAL);
                numDigitsSpinner.setEnabled(true);
                fireRuleChanged(inputRule);
            }
        };
        radioAlphabetical.addSelectionListener(alphabeticalListener);
        SelectionAdapter numericalListener = new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                numDigitsSpinner.setEnabled(true);
                inputRule.setListStyleState(ListStyle.NUMERIC);
                fireRuleChanged(inputRule);
            }
        };
        radioNumerical.addSelectionListener(numericalListener);
        SelectionAdapter romanListener = new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                numDigitsSpinner.setEnabled(false);
                inputRule.setListStyleState(ListStyle.ROMAN_NUMERALS);
                fireRuleChanged(inputRule);
            }
        };
        radioRoman.addSelectionListener(romanListener);
        SelectionAdapter addToStartListener = new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                inputRule.setAddToEnd(false);
                fireRuleChanged(inputRule);
            }
        };
        radioAddToStart.addSelectionListener(addToStartListener);
        SelectionAdapter addToEndListener = new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                inputRule.setAddToEnd(true);
                fireRuleChanged(inputRule);
            }
        };
        radioAddToEnd.addSelectionListener(addToEndListener);
    }

    private void selectRuleConfigurationInUI() {
        radioAscending.setSelection(inputRule.isAscending());
        radioDescending.setSelection(!inputRule.isAscending());
        radioAlphabetical.setSelection(inputRule.getListStyleState() == ListStyle.ALPHABETICAL);
        radioNumerical.setSelection(inputRule.getListStyleState() == ListStyle.NUMERIC);
        radioRoman.setSelection(inputRule.getListStyleState() == ListStyle.ROMAN_NUMERALS);
        radioAddToStart.setSelection(!inputRule.isAddToEnd());
        radioAddToEnd.setSelection(inputRule.isAddToEnd());
        startFrom.setText(Integer.toString(inputRule.getStartFrom()));
        seperatorToken.setText(inputRule.getSeperatorToken());
    }

    @Override
    public void setFocus() {
        Activator.getDefault().getEditorManager().setActiveEditor(this);
    }
}
