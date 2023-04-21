package com.alveole.studio.web.managers.dialogs;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.w3c.dom.Element;
import com.alveole.interfaces.GeneralPropertiesEditor;
import com.alveole.studio.web.data.Project;
import com.alveole.studio.web.data.ProjectChangeListener;
import com.alveole.studio.web.managers.struts2editor.Struts2Tools;

/**
 * This editor enables to edit struts 2 specific properties.
 * 
 * @author sylvain
 *
 */
public class Struts2PropertiesEditor extends GeneralPropertiesEditor {

    public static final String ExtId = "com.alveole.studio.properties.Struts2PropertiesEditor";

    private Label label = null;

    private Text extensionField = null;

    private Label label1 = null;

    private Text flatFileTarget = null;

    private Label label4 = null;

    private Text flatLinksFileTarget = null;

    private Composite composite = null;

    private Button okButton = null;

    public Struts2PropertiesEditor(Composite parent) {
        super(parent);
        initialize();
    }

    private void initialize() {
        GridData gridData21 = new GridData();
        gridData21.horizontalAlignment = GridData.CENTER;
        gridData21.grabExcessHorizontalSpace = true;
        gridData21.verticalAlignment = GridData.CENTER;
        GridData gridData11 = new GridData();
        gridData11.horizontalSpan = 2;
        gridData11.horizontalAlignment = GridData.FILL;
        gridData11.verticalAlignment = GridData.CENTER;
        gridData11.grabExcessHorizontalSpace = true;
        GridData gridData2 = new GridData();
        gridData2.grabExcessHorizontalSpace = true;
        gridData2.verticalAlignment = GridData.CENTER;
        gridData2.horizontalAlignment = GridData.FILL;
        GridData gridData3 = new GridData();
        gridData3.grabExcessHorizontalSpace = true;
        gridData3.verticalAlignment = GridData.CENTER;
        gridData3.horizontalAlignment = GridData.FILL;
        GridData gridData4 = new GridData();
        gridData4.grabExcessHorizontalSpace = true;
        gridData4.verticalAlignment = GridData.CENTER;
        gridData4.horizontalAlignment = GridData.FILL;
        GridData gridData1 = new GridData();
        gridData1.grabExcessHorizontalSpace = true;
        gridData1.verticalAlignment = GridData.CENTER;
        gridData1.horizontalAlignment = GridData.FILL;
        GridData gridData = new GridData();
        gridData.horizontalAlignment = GridData.FILL;
        gridData.grabExcessHorizontalSpace = true;
        gridData.verticalAlignment = GridData.CENTER;
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        label = new Label(this, SWT.NONE);
        label.setText("Struts .do extension");
        extensionField = new Text(this, SWT.BORDER);
        extensionField.setLayoutData(gridData);
        label1 = new Label(this, SWT.NONE);
        label1.setText("Flat file");
        flatFileTarget = new Text(this, SWT.BORDER);
        flatFileTarget.setLayoutData(gridData1);
        label4 = new Label(this, SWT.NONE);
        label4.setText("Flat links file");
        flatLinksFileTarget = new Text(this, SWT.BORDER);
        flatLinksFileTarget.setLayoutData(gridData4);
        composite = new Composite(this, SWT.NONE);
        composite.setLayout(new GridLayout());
        composite.setLayoutData(gridData11);
        okButton = new Button(composite, SWT.NONE);
        okButton.setText("Ok");
        okButton.setLayoutData(gridData21);
        this.setLayout(gridLayout);
        setSize(new Point(300, 200));
        okButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                performOk();
            }
        });
    }

    /**
	 * Save extensions.
	 */
    private void performOk() {
        if (!extensionField.getText().matches("\\.[a-zA-Z0-9_.-]+")) {
            MessageDialog.openError(getShell(), "Invalid extension field", "Extension field must match \\.[a-zA-Z0-9_.-]+");
            return;
        }
        Element elt = project.getPluginProperties(ExtId);
        elt.setAttribute("xmlns:s", Struts2Tools.xmlns);
        Project.setAttributeNS(elt, Struts2Tools.xmlns, "s", "extension", extensionField.getText());
        Project.setAttributeNS(elt, Struts2Tools.xmlns, "s", "strutsfile", flatFileTarget.getText());
        Project.setAttributeNS(elt, Struts2Tools.xmlns, "s", "linksfile", flatLinksFileTarget.getText());
        project.fireChangeEvent(ProjectChangeListener.EventType.PluginSettingsChanged, ExtId);
    }

    /**
	 * Initialise panel.
	 */
    public void setProject(Project project) {
        super.setProject(project);
        Element elt = project.getPluginProperties(ExtId);
        String ext = Project.getAttributeNS(elt, Struts2Tools.xmlns, "extension");
        if (ext == null || "".equals(ext)) {
            ext = ".action";
        }
        extensionField.setText(ext);
        ext = Project.getAttributeNS(elt, Struts2Tools.xmlns, "strutsfile");
        if (ext == null || "".equals(ext)) {
            ext = "alveole-struts.xml";
        }
        flatFileTarget.setText(ext);
        ext = Project.getAttributeNS(elt, Struts2Tools.xmlns, "linksfile");
        if (ext == null || "".equals(ext)) {
            ext = "alveole-links.properties";
        }
        flatLinksFileTarget.setText(ext);
    }
}
