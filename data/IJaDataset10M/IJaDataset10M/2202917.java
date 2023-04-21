package com.gerodp.view.newWidgets;

import com.gerodp.controller.CorrectTestListener;
import com.gerodp.controller.WordTestContentProvider;
import com.gerodp.controller.WordTestLabelProvider;
import com.gerodp.view.AppMessagesSingleton;
import com.gerodp.view.ScreenTest;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.jface.viewers.ContentViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;

/**
 * @name TestViewer
 * @description UI element that shows a test
 * 
 * @author Ger�nimo Di Pierro
 * @creationDate 05/01/2008
 */
public class TestViewer extends ContentViewer {

    private FormToolkit formToolkit;

    private ScrolledForm testForm;

    private List<Integer> fields;

    private Label resultLabel;

    public static final int LABEL = 0;

    public static final int TEXT = 1;

    public static final int HIDE_LABEL = 2;

    public TestViewer(Composite parent, int style) {
        createContents(parent, style);
        fields = new ArrayList<Integer>();
    }

    private void createContents(Composite parent, int style) {
        GridLayout layout = new GridLayout();
        layout.numColumns = 1;
        parent.setLayout(layout);
        formToolkit = new FormToolkit(parent.getDisplay());
        testForm = formToolkit.createScrolledForm(parent);
        testForm.setText(AppMessagesSingleton.getMessage("WordTest"));
        testForm.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL));
        layout = new GridLayout();
        layout.numColumns = 3;
        testForm.getBody().setLayout(layout);
    }

    /**
     * 
     * Adds a field to the fields.
     *  
     * @param fieldType   
     */
    public void addField(int fieldType) {
        fields.add(new Integer(fieldType));
    }

    /**
     * 
     * Adds UI elements to the form conforming the test content 
     *     
     */
    public void createTestUI() {
        while (testForm.getBody().getChildren().length > 0) testForm.getBody().getChildren()[0].dispose();
        Label label = formToolkit.createLabel(testForm.getBody(), AppMessagesSingleton.getMessage("WordTestExplanation"), SWT.WRAP);
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        data.horizontalSpan = fields.size();
        label.setLayoutData(data);
        WordTestContentProvider content = (WordTestContentProvider) this.getContentProvider();
        WordTestLabelProvider labelContent = (WordTestLabelProvider) this.getLabelProvider();
        Object[] elements = content.getElements(getInput());
        for (int i = 0; i < elements.length; i++) {
            for (int j = 0; j < fields.size(); j++) {
                switch(fields.get(j).intValue()) {
                    case LABEL:
                        Label l = formToolkit.createLabel(testForm.getBody(), labelContent.getColumnText(elements[i], j), SWT.WRAP);
                        l.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
                        break;
                    case TEXT:
                        Text t = formToolkit.createText(testForm.getBody(), labelContent.getColumnText(elements[i], j), SWT.SINGLE | SWT.BORDER);
                        t.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
                        break;
                    case HIDE_LABEL:
                        Label lh = formToolkit.createLabel(testForm.getBody(), labelContent.getColumnText(elements[i], j), SWT.WRAP);
                        lh.setVisible(false);
                        lh.setForeground(new Color(testForm.getBody().getDisplay(), 220, 0, 0));
                        lh.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
                        break;
                }
            }
        }
        Button correctButton = formToolkit.createButton(testForm.getBody(), AppMessagesSingleton.getMessage("Correct"), SWT.NONE);
        data = new GridData(GridData.HORIZONTAL_ALIGN_CENTER);
        data.horizontalSpan = fields.size();
        correctButton.setLayoutData(data);
        correctButton.addSelectionListener(new CorrectTestListener((ScreenTest) testForm.getParent()));
        resultLabel = formToolkit.createLabel(testForm.getBody(), AppMessagesSingleton.getMessage("ResultObtained"), SWT.WRAP);
        FontData fdata = new FontData();
        fdata.setHeight(15);
        resultLabel.setFont(new Font(null, fdata));
        resultLabel.setForeground(new Color(testForm.getBody().getDisplay(), 0, 0, 240));
        resultLabel.setVisible(false);
        data = new GridData(GridData.HORIZONTAL_ALIGN_CENTER);
        data.horizontalSpan = fields.size();
        resultLabel.setLayoutData(data);
        testForm.reflow(true);
    }

    @Override
    public Control getControl() {
        return null;
    }

    @Override
    public ISelection getSelection() {
        return null;
    }

    @Override
    public void refresh() {
    }

    @Override
    public void setSelection(ISelection arg0, boolean arg1) {
    }

    public Composite getFormBody() {
        return testForm.getBody();
    }

    public void printResult(int result) {
        resultLabel.setText(AppMessagesSingleton.getMessage("ResultObtained") + String.valueOf(result));
        for (int i = 0; i < testForm.getBody().getChildren().length; i++) testForm.getBody().getChildren()[i].setVisible(true);
        testForm.reflow(true);
    }
}
