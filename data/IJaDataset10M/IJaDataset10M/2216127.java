package raptor.swt;

import java.util.HashMap;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import raptor.engine.uci.UCIEngine;
import raptor.engine.uci.UCIOption;
import raptor.engine.uci.options.UCIButton;
import raptor.engine.uci.options.UCICheck;
import raptor.engine.uci.options.UCICombo;
import raptor.engine.uci.options.UCISpinner;
import raptor.engine.uci.options.UCIString;
import raptor.international.L10n;
import raptor.service.UCIEngineService;

public class UCIEnginePropertiesDialog extends Dialog {

    protected Button applyButton;

    protected Button cancelButton;

    protected HashMap<String, Control> optionNameToControl = new HashMap<String, Control>();

    protected Text goAnalysisParams;

    protected UCIEngine engine;

    protected Shell shell;

    protected static L10n local = L10n.getInstance();

    public UCIEnginePropertiesDialog(Shell parent, UCIEngine engine) {
        super(parent);
        setText(local.getString("uciEngPropD1") + engine.getUserName() + local.getString("uciEngPropD2") + engine.getEngineName() + local.getString("uciEngPropD3") + engine.getEngineAuthor());
        this.engine = engine;
    }

    /**
	 * Opens the dialog and returns the input
	 * 
	 * @return String
	 */
    public void open() {
        shell = new Shell(getParent(), SWT.TITLE | SWT.PRIMARY_MODAL | SWT.RESIZE);
        shell.setText(getText());
        createContents();
        shell.pack();
        shell.layout(true, true);
        SWTUtils.center(shell);
        shell.open();
        Rectangle shellBounds = getParent().getBounds();
        Point dialogSize = shell.getSize();
        shell.setLocation(shellBounds.x + (shellBounds.width - dialogSize.x) / 2, shellBounds.y + (shellBounds.height - dialogSize.y) / 2);
        Display display = getParent().getDisplay();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
    }

    protected void createContents() {
        shell.setLayout(new GridLayout(1, false));
        ScrolledComposite scrolledComposite = new ScrolledComposite(shell, SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER) {

            @Override
            public Point computeSize(int hint, int hint2, boolean changed) {
                Point result = super.computeSize(hint, hint2, changed);
                if (result.y > 400) {
                    result.y = 400;
                }
                if (result.x > 600) {
                    result.x = 600;
                }
                return result;
            }
        };
        scrolledComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        Composite customControls = new Composite(scrolledComposite, SWT.NONE);
        customControls.setLayout(new GridLayout(2, false));
        scrolledComposite.setContent(customControls);
        scrolledComposite.setMinSize(480, 430);
        scrolledComposite.setExpandVertical(false);
        scrolledComposite.setExpandHorizontal(false);
        Label goParametersLabel = new Label(customControls, SWT.LEFT);
        goParametersLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
        goParametersLabel.setText(local.getString("uciEngPropD4"));
        goAnalysisParams = new Text(customControls, SWT.SINGLE | SWT.BORDER);
        goAnalysisParams.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        goAnalysisParams.setText(engine.getGoAnalysisParameters());
        final Button multiplyBlackScoreByMinus1Button = new Button(customControls, SWT.CHECK);
        multiplyBlackScoreByMinus1Button.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
        multiplyBlackScoreByMinus1Button.setText(local.getString("uciEngPropD5"));
        multiplyBlackScoreByMinus1Button.setSelection(engine.isMultiplyBlackScoreByMinus1());
        String[] optionNames = engine.getOptionNames();
        for (String optionName : optionNames) {
            UCIOption uciOption = engine.getOption(optionName);
            if (!(uciOption instanceof UCIButton)) {
                if (uciOption instanceof UCICheck) {
                    Button button = new Button(customControls, SWT.CHECK);
                    button.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
                    button.setText(uciOption.getName());
                    button.setSelection(StringUtils.equals(uciOption.getValue(), "true"));
                    optionNameToControl.put(uciOption.getName(), button);
                } else {
                    Label label = new Label(customControls, SWT.LEFT);
                    label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
                    label.setText(uciOption.getName());
                    if (uciOption instanceof UCIString) {
                        if (uciOption.getName().equalsIgnoreCase("UCI_EngineAbout")) {
                            CLabel label2 = new CLabel(customControls, SWT.LEFT);
                            label2.setText(WordUtils.wrap(uciOption.getValue(), 50));
                        } else {
                            Text text = new Text(customControls, SWT.BORDER | SWT.SINGLE);
                            text.setText(StringUtils.defaultString(uciOption.getValue()));
                            text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
                            optionNameToControl.put(uciOption.getName(), text);
                        }
                    } else if (uciOption instanceof UCICombo) {
                        Combo combo = new Combo(customControls, SWT.DROP_DOWN | SWT.READ_ONLY);
                        for (String value : ((UCICombo) uciOption).getOptions()) {
                            combo.add(value);
                        }
                        if (StringUtils.isNotBlank(uciOption.getDefaultValue())) {
                            for (int i = 0; i < combo.getItemCount(); i++) {
                                if (StringUtils.equals(combo.getItem(i), uciOption.getValue())) {
                                    combo.select(i);
                                    break;
                                }
                            }
                        }
                        combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
                        optionNameToControl.put(uciOption.getName(), combo);
                    } else if (uciOption instanceof UCISpinner) {
                        Spinner spinner = new Spinner(customControls, SWT.NONE);
                        spinner.setMinimum(((UCISpinner) uciOption).getMinimum());
                        spinner.setMaximum(((UCISpinner) uciOption).getMaximum());
                        spinner.setPageIncrement(1);
                        spinner.setDigits(0);
                        if (StringUtils.isNotBlank(uciOption.getValue())) {
                            spinner.setSelection(Integer.parseInt(uciOption.getValue()));
                        }
                        optionNameToControl.put(uciOption.getName(), spinner);
                    }
                }
            }
        }
        customControls.layout();
        customControls.setSize(customControls.computeSize(SWT.DEFAULT, SWT.DEFAULT, true));
        Composite buttonsComposite = new Composite(shell, SWT.NONE);
        buttonsComposite.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false));
        buttonsComposite.setLayout(new RowLayout());
        Button saveButton = new Button(buttonsComposite, SWT.PUSH);
        saveButton.setText(local.getString("save"));
        saveButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                engine.setGoAnalysisParameters(goAnalysisParams.getText());
                engine.setMultiplyBlackScoreByMinus1(multiplyBlackScoreByMinus1Button.getSelection());
                String[] customNames = engine.getOptionNames();
                for (String optionName : customNames) {
                    saveOptionValue(optionName);
                }
                UCIEngineService.getInstance().saveConfiguration(engine);
                shell.dispose();
            }
        });
        Button cancelButton = new Button(buttonsComposite, SWT.PUSH);
        cancelButton.setText(local.getString("cancel"));
        cancelButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                shell.dispose();
            }
        });
    }

    protected void saveOptionValue(String optionName) {
        UCIOption option = engine.getOption(optionName);
        if (option == null) {
            return;
        }
        if (!(option instanceof UCIButton)) {
            if (option instanceof UCICheck) {
                Button button = (Button) optionNameToControl.get(option.getName());
                if (button != null) {
                    engine.setOverrideOption(option.getName(), button.getSelection() ? "true" : "false");
                    option.setValue(button.getSelection() ? "true" : "false");
                }
            } else if (option instanceof UCIString) {
                Text text = (Text) optionNameToControl.get(option.getName());
                if (text != null) {
                    engine.setOverrideOption(option.getName(), text.getText());
                    option.setValue(text.getText());
                }
            } else if (option instanceof UCISpinner) {
                Spinner spinner = (Spinner) optionNameToControl.get(option.getName());
                if (spinner != null) {
                    engine.setOverrideOption(option.getName(), String.valueOf(spinner.getSelection()));
                    option.setValue(String.valueOf(spinner.getSelection()));
                }
            } else if (option instanceof UCICombo) {
                Combo combo = (Combo) optionNameToControl.get(option.getName());
                if (combo != null) {
                    engine.setOverrideOption(option.getName(), combo.getText());
                    option.setValue(combo.getText());
                }
            }
        }
    }
}
