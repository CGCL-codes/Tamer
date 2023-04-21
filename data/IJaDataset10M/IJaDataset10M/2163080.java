package ca.ucalgary.cpsc.ase.productLineDesigner.refactoring.OptionPoint.newOptionPoint.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import org.eclipse.jdt.core.JavaConventions;
import org.eclipse.ltk.ui.refactoring.UserInputWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import ca.ucalgary.cpsc.ase.productLineDesigner.refactoring.RefactoringUtils;
import ca.ucalgary.cpsc.ase.productLineDesigner.refactoring.OptionPoint.newOptionPoint.NewOptionPointInfo;

@SuppressWarnings("unused")
public class NewOPInputPage extends UserInputWizardPage {

    NewOptionPointInfo info;

    private Text textArea = null;

    private Combo methods = null;

    private Text textArea1 = null;

    private Text opName = null;

    private List options = null;

    private Button remButton = null;

    private Button clearButton = null;

    private Button addButton = null;

    private Text option = null;

    public NewOPInputPage(String name, NewOptionPointInfo info) {
        super(name);
        this.info = info;
    }

    @Override
    public void createControl(Composite parent) {
        Composite result = new Composite(parent, SWT.NONE);
        setControl(result);
        initialize(result);
        setPageComplete(false);
    }

    private void initialize(Composite parent) {
        GridData gridData6 = new GridData();
        gridData6.widthHint = 140;
        GridData gridData5 = new GridData();
        gridData5.heightHint = 23;
        gridData5.widthHint = 50;
        GridData gridData4 = new GridData();
        gridData4.heightHint = 23;
        gridData4.widthHint = 50;
        GridData gridData3 = new GridData();
        gridData3.heightHint = 23;
        gridData3.widthHint = 50;
        GridData gridData21 = new GridData();
        gridData21.horizontalAlignment = GridData.BEGINNING;
        gridData21.grabExcessHorizontalSpace = false;
        gridData21.grabExcessVerticalSpace = false;
        gridData21.verticalSpan = 3;
        gridData21.heightHint = 80;
        gridData21.widthHint = 165;
        gridData21.verticalAlignment = GridData.CENTER;
        GridData gridData11 = new GridData();
        gridData11.grabExcessHorizontalSpace = true;
        gridData11.verticalAlignment = GridData.CENTER;
        gridData11.horizontalSpan = 3;
        gridData11.widthHint = 140;
        gridData11.horizontalAlignment = GridData.BEGINNING;
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 3;
        parent.setLayout(gridLayout);
        createCombo(parent);
        Label fill = new Label(parent, SWT.NONE);
        parent.setSize(new Point(300, 208));
        final Label title = new Label(parent, SWT.LEFT);
        title.setText("enter option point name");
        title.setLayoutData(gridData11);
        opName = new Text(parent, SWT.BORDER);
        opName.setLayoutData(gridData11);
        Label fill1 = new Label(parent, SWT.NONE);
        Label fill2 = new Label(parent, SWT.NONE);
        Label fill3 = new Label(parent, SWT.NONE);
        final Label title1 = new Label(parent, SWT.NONE);
        title1.setText("enter option names");
        Label fill5 = new Label(parent, SWT.NONE);
        Label fill6 = new Label(parent, SWT.NONE);
        options = new List(parent, SWT.NONE);
        options.setLayoutData(gridData21);
        remButton = new Button(parent, SWT.NONE);
        remButton.setText("remove");
        remButton.setLayoutData(gridData4);
        remButton.setEnabled(false);
        Label filler = new Label(parent, SWT.NONE);
        clearButton = new Button(parent, SWT.NONE);
        clearButton.setText("clear");
        clearButton.setLayoutData(gridData3);
        Label filler1 = new Label(parent, SWT.NONE);
        addButton = new Button(parent, SWT.NONE);
        addButton.setText("add");
        addButton.setLayoutData(gridData5);
        addButton.setEnabled(false);
        option = new Text(parent, SWT.BORDER);
        option.setLayoutData(gridData6);
        gridData11.widthHint = 157;
        opName.setLayoutData(gridData11);
        methods.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                ;
            }

            @Override
            public void widgetSelected(SelectionEvent e) {
                validate();
            }
        });
        final Listener vpNameListener = new Listener() {

            @Override
            public void handleEvent(Event event) {
                switch(event.type) {
                    case SWT.FocusOut:
                        validate();
                        break;
                    case SWT.Modify:
                        validate();
                        break;
                }
            }
        };
        opName.addListener(SWT.FocusOut, vpNameListener);
        opName.addListener(SWT.Modify, vpNameListener);
        addButton.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                ;
            }

            @Override
            public void widgetSelected(SelectionEvent e) {
                options.add(option.getText());
                option.setText("");
                addButton.setEnabled(false);
                validate();
            }
        });
        clearButton.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                ;
            }

            @Override
            public void widgetSelected(SelectionEvent e) {
                options.removeAll();
                validate();
            }
        });
        remButton.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                ;
            }

            @Override
            public void widgetSelected(SelectionEvent e) {
                options.remove(options.getSelectionIndex());
                remButton.setEnabled(false);
                validate();
            }
        });
        final Listener optionlistener = new Listener() {

            @SuppressWarnings("deprecation")
            @Override
            public void handleEvent(Event event) {
                switch(event.type) {
                    case SWT.FocusOut:
                        if (JavaConventions.validateFieldName(RefactoringUtils.createOptionConstantName(option.getText())).isOK() && !Arrays.asList(options.getItems()).contains(option.getText())) {
                            addButton.setEnabled(true);
                            validate();
                        } else {
                            addButton.setEnabled(false);
                            validate();
                        }
                        break;
                    case SWT.Modify:
                        if (JavaConventions.validateFieldName(RefactoringUtils.createOptionConstantName(option.getText())).isOK() && !Arrays.asList(options.getItems()).contains(option.getText())) {
                            addButton.setEnabled(true);
                            validate();
                        } else {
                            addButton.setEnabled(false);
                            validate();
                        }
                        break;
                }
            }
        };
        option.addListener(SWT.FocusOut, optionlistener);
        option.addListener(SWT.Modify, optionlistener);
        options.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                ;
            }

            @Override
            public void widgetSelected(SelectionEvent e) {
                if (options.getSelectionIndex() != -1) {
                    remButton.setEnabled(true);
                } else {
                    remButton.setEnabled(false);
                }
            }
        });
    }

    private void createCombo(Composite parent) {
        GridData gridData1 = new GridData();
        gridData1.grabExcessHorizontalSpace = true;
        gridData1.widthHint = 140;
        gridData1.horizontalSpan = 3;
        final Label title = new Label(parent, SWT.LEFT);
        title.setText("select method");
        title.setLayoutData(gridData1);
        gridData1.horizontalAlignment = GridData.BEGINNING;
        gridData1.verticalAlignment = GridData.BEGINNING;
        methods = new Combo(parent, SWT.NONE);
        methods.setLayoutData(gridData1);
        Set<String> keys = info.getMethods().keySet();
        for (String qualifiedName : keys) {
            methods.add(qualifiedName);
        }
        if (keys.size() > 18) {
            methods.setVisibleItemCount(18);
        } else {
            methods.setVisibleItemCount(keys.size());
        }
    }

    @SuppressWarnings("deprecation")
    private void validate() {
        boolean complete = true;
        if (methods.getSelectionIndex() == -1) {
            complete = false;
        } else {
            info.setOPMethod(info.getMethods().get(methods.getText()));
        }
        String name = RefactoringUtils.createEnumName(opName.getText());
        if (name.equals("") || !JavaConventions.validateTypeVariableName(name).isOK()) {
            info.setOptionPoint("");
            complete = false;
        } else {
            info.setOptionPoint(opName.getText());
        }
        if (options.getItems().length == 0) {
            complete = false;
        }
        if (complete) {
            java.util.List<String> optionList = new ArrayList<String>();
            for (String option : options.getItems()) {
                optionList.add(option);
            }
            info.setOptions(optionList);
        }
        setPageComplete(complete);
    }
}
