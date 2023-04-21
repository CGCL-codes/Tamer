package com.google.inject.tools.ideplugin.eclipse;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.FormDialog;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.events.IHyperlinkListener;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import com.google.inject.tools.ideplugin.ProjectManager;
import com.google.inject.tools.ideplugin.eclipse.PreferencesPage.PreferenceDialogArea;
import com.google.inject.tools.suite.Messenger;
import com.google.inject.tools.suite.module.ApplicationModuleContextRepresentation;
import com.google.inject.tools.suite.module.ClassNameUtility;
import com.google.inject.tools.suite.module.CustomModuleContextRepresentation;
import com.google.inject.tools.suite.module.ModuleContextRepresentation;
import com.google.inject.tools.suite.module.ModuleManager;
import com.google.inject.tools.suite.module.ModuleContextRepresentation.ModuleInstanceRepresentation;

/**
 * Eclipse dialog allowing selection of module contexts.
 * 
 * @author Darren Creutz (dcreutz@gmail.com)
 */
class EclipseModuleDialog extends FormDialog {

    private final Messenger messenger;

    private final ModuleManager moduleManager;

    private final ProjectManager projectManager;

    private Set<ModuleContextRepresentation> moduleContexts;

    private Set<ModuleContextRepresentation> activeModuleContexts;

    private FormToolkit toolkit;

    private final Shell shell;

    private final PreferenceDialogArea preferencesPage;

    private final Map<String, Button> usercheckboxes;

    private final Map<String, Button> autocheckboxes;

    public EclipseModuleDialog(Shell parent, Messenger messenger, ProjectManager projectManager, ModuleManager moduleManager) {
        super(parent);
        this.shell = parent;
        this.messenger = messenger;
        this.moduleManager = moduleManager;
        this.projectManager = projectManager;
        moduleContexts = moduleManager.getModuleContexts();
        activeModuleContexts = moduleManager.getActiveModuleContexts();
        preferencesPage = new PreferenceDialogArea();
        usercheckboxes = new HashMap<String, Button>();
        autocheckboxes = new HashMap<String, Button>();
    }

    public static boolean display(Shell parent, Messenger messenger, ProjectManager projectManager, ModuleManager moduleManager) {
        EclipseModuleDialog dialog = new EclipseModuleDialog(parent, messenger, projectManager, moduleManager);
        dialog.create();
        dialog.getShell().setBounds(200, 200, 500, 400);
        dialog.getShell().setText(PluginTextValues.GUICE_CONTEXT_CONFIGURATION);
        dialog.setBlockOnOpen(true);
        dialog.open();
        return dialog.getReturnCode() == Window.OK;
    }

    @Override
    protected void createFormContent(IManagedForm form) {
        ScrolledForm scrolledForm = form.getForm();
        toolkit = form.getToolkit();
        Composite parent = scrolledForm.getBody();
        parent.setLayout(new GridLayout());
        if (moduleContexts == null) {
            toolkit.createLabel(parent, PluginTextValues.NO_PROJECT);
        } else {
            createScanContent(scrolledForm.getBody());
            createUserContexts(scrolledForm.getBody());
            createPremadeContexts(scrolledForm.getBody());
            createOptionsContent(scrolledForm.getBody());
        }
        scrolledForm.pack();
        scrolledForm.reflow(true);
    }

    static class ScanForNewContexts implements IHyperlinkListener {

        private final ProjectManager projectManager;

        private final ModuleManager moduleManager;

        private final Shell shell;

        private final Messenger messenger;

        private final EclipseModuleDialog dialog;

        public ScanForNewContexts(ProjectManager projectManager, ModuleManager moduleManager, Shell shell, Messenger messenger, EclipseModuleDialog dialog) {
            this.projectManager = projectManager;
            this.moduleManager = moduleManager;
            this.shell = shell;
            this.messenger = messenger;
            this.dialog = dialog;
        }

        public void linkActivated(HyperlinkEvent e) {
            dialog.cancelPressed();
            projectManager.findNewContexts(projectManager.getJavaManager(moduleManager), new ModuleManager.PostUpdater() {

                public void execute(boolean success) {
                    Display.getDefault().asyncExec(new Runnable() {

                        public void run() {
                            EclipseModuleDialog.display(shell, messenger, projectManager, moduleManager);
                        }
                    });
                }
            }, false);
        }

        public void linkEntered(HyperlinkEvent e) {
        }

        public void linkExited(HyperlinkEvent e) {
        }
    }

    private void createScanContent(Composite parent) {
        Section section = toolkit.createSection(parent, Section.EXPANDED | Section.TITLE_BAR);
        section.setText(PluginTextValues.FIND_NEW_CONTEXTS);
        ScrolledForm insideScrolledForm = toolkit.createScrolledForm(section);
        insideScrolledForm.setExpandHorizontal(true);
        insideScrolledForm.setExpandVertical(true);
        Composite body = insideScrolledForm.getBody();
        GridLayout layout = new GridLayout();
        layout.numColumns = 1;
        body.setLayout(layout);
        makeHyperlink(body, PluginTextValues.SCAN_FOR_CONTEXTS, new ScanForNewContexts(projectManager, moduleManager, shell, messenger, this));
        body.pack();
        insideScrolledForm.pack();
        insideScrolledForm.reflow(true);
        section.setClient(insideScrolledForm);
        section.pack();
    }

    private void createOptionsContent(Composite parent) {
        Section section = toolkit.createSection(parent, Section.EXPANDED | Section.TITLE_BAR);
        section.setText(PluginTextValues.GLOBAL_OPTIONS);
        ScrolledForm insideScrolledForm = toolkit.createScrolledForm(section);
        insideScrolledForm.setExpandHorizontal(true);
        insideScrolledForm.setExpandVertical(true);
        Composite body = insideScrolledForm.getBody();
        GridLayout layout = new GridLayout();
        layout.numColumns = 1;
        body.setLayout(layout);
        preferencesPage.buildComposite(body);
        body.pack();
        insideScrolledForm.pack();
        insideScrolledForm.reflow(true);
        section.setClient(insideScrolledForm);
        section.pack();
    }

    private static class NewContextDialog extends FormDialog {

        private final ModuleManager moduleManager;

        private Text classNameText = null;

        private Text methodNameText = null;

        private Text titleText = null;

        private String classNameTextValue;

        private String methodNameTextValue;

        private String titleTextValue;

        public NewContextDialog(Shell parent, ModuleManager moduleManager) {
            super(parent);
            this.moduleManager = moduleManager;
        }

        @Override
        public void createFormContent(IManagedForm form) {
            FormToolkit toolkit = form.getToolkit();
            ScrolledForm scrolledForm = form.getForm();
            Composite body = scrolledForm.getBody();
            GridLayout layout = new GridLayout();
            layout.numColumns = 2;
            body.setLayout(layout);
            toolkit.createLabel(body, PluginTextValues.CONTEXT_NAME);
            titleText = toolkit.createText(body, "", SWT.BORDER);
            titleText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
            titleText.addModifyListener(new ModifyListener() {

                public void modifyText(ModifyEvent e) {
                    titleTextValue = titleText.getText();
                }
            });
            toolkit.createLabel(body, PluginTextValues.FULLY_QUALIFIED_CLASS_NAME);
            classNameText = toolkit.createText(body, "", SWT.BORDER);
            classNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
            classNameText.addModifyListener(new ModifyListener() {

                public void modifyText(ModifyEvent e) {
                    classNameTextValue = classNameText.getText();
                }
            });
            toolkit.createLabel(body, PluginTextValues.METHOD_NAME);
            methodNameText = toolkit.createText(body, "", SWT.BORDER);
            methodNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
            methodNameText.addModifyListener(new ModifyListener() {

                public void modifyText(ModifyEvent e) {
                    methodNameTextValue = methodNameText.getText();
                }
            });
        }

        public void saveSettings(Messenger messenger) {
            String title = titleTextValue;
            String classToUse = classNameTextValue;
            String methodToUse = methodNameTextValue;
            if (title == null || classToUse == null || methodToUse == null) {
                messenger.display("Please fill in all fields of a custom context.");
            } else {
                moduleManager.addCustomContext(title, classToUse, methodToUse);
            }
        }

        public static void display(Shell parent, Messenger messenger, ProjectManager projectManager, ModuleManager moduleManager) {
            NewContextDialog dialog = new NewContextDialog(parent, moduleManager);
            dialog.create();
            dialog.getShell().setBounds(200, 200, 500, 200);
            dialog.getShell().setText(PluginTextValues.CREATE_CONTEXT);
            dialog.setBlockOnOpen(true);
            dialog.open();
            if (dialog.getReturnCode() == Window.OK) {
                dialog.saveSettings(messenger);
            }
            EclipseModuleDialog.display(parent, messenger, projectManager, moduleManager);
        }
    }

    private void createUserContexts(Composite parent) {
        Section section = toolkit.createSection(parent, Section.EXPANDED | Section.TITLE_BAR);
        section.setText(PluginTextValues.YOUR_CONTEXTS);
        ScrolledForm insideScrolledForm = toolkit.createScrolledForm(section);
        insideScrolledForm.setExpandHorizontal(true);
        insideScrolledForm.setExpandVertical(true);
        Composite body = insideScrolledForm.getBody();
        GridLayout layout = new GridLayout();
        layout.numColumns = 1;
        body.setLayout(layout);
        makeHyperlink(body, PluginTextValues.CREATE_NEW_CONTEXT, new IHyperlinkListener() {

            public void linkActivated(HyperlinkEvent e) {
                EclipseModuleDialog.this.close();
                NewContextDialog.display(shell, messenger, projectManager, moduleManager);
            }

            public void linkEntered(HyperlinkEvent e) {
            }

            public void linkExited(HyperlinkEvent e) {
            }
        });
        for (ModuleContextRepresentation moduleContext : moduleContexts) {
            if (moduleContext instanceof CustomModuleContextRepresentation || moduleContext instanceof ApplicationModuleContextRepresentation) {
                String tooltip = moduleContext.getLongName();
                Button checkbox = makeCheckbox(body, activeModuleContexts.contains(moduleContext), moduleContext.getShortName(), tooltip);
                usercheckboxes.put(moduleContext.getName(), checkbox);
            }
        }
        body.pack();
        insideScrolledForm.pack();
        insideScrolledForm.reflow(true);
        section.setClient(insideScrolledForm);
        section.pack();
    }

    private void createPremadeContexts(Composite parent) {
        Section section = toolkit.createSection(parent, Section.EXPANDED | Section.TITLE_BAR);
        section.setText(PluginTextValues.AUTOGENERATED_CONTEXTS);
        ScrolledForm insideScrolledForm = toolkit.createScrolledForm(section);
        insideScrolledForm.setExpandHorizontal(true);
        insideScrolledForm.setExpandVertical(true);
        Composite body = insideScrolledForm.getBody();
        GridLayout layout = new GridLayout();
        layout.numColumns = 1;
        body.setLayout(layout);
        boolean hasAutoModuleContexts = false;
        for (ModuleContextRepresentation moduleContext : moduleContexts) {
            if (!(moduleContext instanceof CustomModuleContextRepresentation || moduleContext instanceof ApplicationModuleContextRepresentation)) {
                hasAutoModuleContexts = true;
                break;
            }
        }
        if (!hasAutoModuleContexts) {
            makeText(body, SWT.NONE, PluginTextValues.NO_CONTEXTS_AVAILABLE);
        } else {
            makeHyperlink(body, PluginTextValues.ACTIVATE_ALL, new IHyperlinkListener() {

                public void linkActivated(HyperlinkEvent e) {
                    for (Button checkbox : autocheckboxes.values()) {
                        checkbox.setSelection(true);
                    }
                }

                public void linkEntered(HyperlinkEvent e) {
                }

                public void linkExited(HyperlinkEvent e) {
                }
            });
            makeHyperlink(body, PluginTextValues.DEACTIVATE_ALL, new IHyperlinkListener() {

                public void linkActivated(HyperlinkEvent e) {
                    for (Button checkbox : autocheckboxes.values()) {
                        checkbox.setSelection(false);
                    }
                }

                public void linkEntered(HyperlinkEvent e) {
                }

                public void linkExited(HyperlinkEvent e) {
                }
            });
            for (ModuleContextRepresentation moduleContext : moduleContexts) {
                if (!(moduleContext instanceof CustomModuleContextRepresentation || moduleContext instanceof ApplicationModuleContextRepresentation)) {
                    StringBuilder text = new StringBuilder();
                    text.append("Guice.createInjector(");
                    int count = 0;
                    for (ModuleInstanceRepresentation module : moduleContext.getModules()) {
                        text.append(module.getCreationString());
                        count++;
                        if (count < moduleContext.getModules().size()) {
                            text.append(", ");
                        }
                    }
                    text.append(");");
                    Button checkbox = makeCheckbox(body, activeModuleContexts.contains(moduleContext), ClassNameUtility.shorten(moduleContext.getName()), text.toString());
                    autocheckboxes.put(moduleContext.getName(), checkbox);
                }
            }
        }
        section.setClient(insideScrolledForm);
        section.pack();
    }

    private Label makeText(Composite parent, int style, String text) {
        return toolkit.createLabel(parent, text, style);
    }

    private Button makeCheckbox(Composite parent, boolean selected, String text, String tooltip) {
        Button button = toolkit.createButton(parent, text, SWT.CHECK);
        button.setSelection(selected);
        button.setToolTipText(tooltip);
        return button;
    }

    private Hyperlink makeHyperlink(Composite parent, String text, IHyperlinkListener listener) {
        Hyperlink link = toolkit.createHyperlink(parent, text, SWT.NONE);
        link.addHyperlinkListener(listener);
        return link;
    }

    @Override
    protected void okPressed() {
        try {
            preferencesPage.saveSettings();
            Map<String, Button> checkboxes = new HashMap<String, Button>();
            checkboxes.putAll(autocheckboxes);
            checkboxes.putAll(usercheckboxes);
            if (checkboxes.size() > 0) {
                for (String context : checkboxes.keySet()) {
                    if (checkboxes.get(context).getSelection()) {
                        moduleManager.activateModuleContext(context);
                    } else {
                        moduleManager.deactivateModuleContext(context);
                    }
                }
            }
        } catch (Throwable t) {
            messenger.logException("Module Dialog Exception", t);
        }
        super.okPressed();
    }
}
