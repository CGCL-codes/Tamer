package org.remus.infomngmnt.file.ui;

import java.lang.reflect.InvocationTargetException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.remus.InfomngmntPackage;
import org.eclipse.remus.core.commands.CommandFactory;
import org.eclipse.remus.core.model.InformationStructureRead;
import org.eclipse.remus.ui.databinding.BindingWidgetFactory;
import org.eclipse.remus.ui.editors.editpage.AbstractInformationFormPage;
import org.eclipse.remus.ui.operation.LoadFileToTmpFromPathRunnable;
import org.eclipse.remus.util.InformationUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.remus.infomngmnt.file.Activator;
import org.remus.infomngmnt.file.Messages;

/**
 * @author Tom Seidel <tom.seidel@remus-software.org>
 */
public class FileEditPage extends AbstractInformationFormPage {

    protected IFile newTmpFile;

    private Text text;

    @Override
    protected void renderPage(IManagedForm managedForm) {
        FormToolkit toolkit = managedForm.getToolkit();
        ScrolledForm form = managedForm.getForm();
        Composite body = form.getBody();
        body.setLayout(new GridLayout());
        toolkit.paintBordersFor(body);
        final Section generalSection = toolkit.createSection(body, ExpandableComposite.TITLE_BAR | ExpandableComposite.TWISTIE | ExpandableComposite.EXPANDED);
        final GridData gd_generalSection = new GridData(SWT.FILL, SWT.CENTER, true, false);
        generalSection.setLayoutData(gd_generalSection);
        generalSection.setText(Messages.FileEditPage_General);
        final Composite composite = toolkit.createComposite(generalSection, SWT.NONE);
        final GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 5;
        composite.setLayout(gridLayout);
        toolkit.paintBordersFor(composite);
        generalSection.setClient(composite);
        toolkit.createLabel(composite, Messages.FileEditPage_Filename, SWT.NONE);
        text = toolkit.createText(composite, null, SWT.BORDER);
        text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));
        new Label(composite, SWT.NONE);
        final Hyperlink openImageWithExternalApp = toolkit.createHyperlink(composite, Messages.FileEditPage_OpenWithExternal, SWT.NONE);
        openImageWithExternalApp.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1));
        openImageWithExternalApp.addHyperlinkListener(new HyperlinkAdapter() {

            @Override
            public void linkActivated(final HyperlinkEvent e) {
                IFile firstBinaryReferenceFile = InformationUtil.getBinaryReferenceFile(getModelObject());
                if (firstBinaryReferenceFile != null) {
                    composite.getDisplay().asyncExec(new Runnable() {

                        public void run() {
                            new OpenFileDialog(composite.getShell(), getModelObject()).open();
                        }
                    });
                    Program.launch(firstBinaryReferenceFile.getLocation().toOSString());
                }
            }
        });
        new Label(composite, SWT.NONE);
        final Hyperlink changeImageHyperlink = toolkit.createHyperlink(composite, Messages.FileEditPage_ChangeFile, SWT.NONE);
        changeImageHyperlink.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 4, 1));
        changeImageHyperlink.addHyperlinkListener(new HyperlinkAdapter() {

            @Override
            public void linkActivated(final HyperlinkEvent e) {
                FileDialog fd = new FileDialog(getSite().getShell());
                fd.setFilterExtensions(new String[] { "*.*" });
                String open = fd.open();
                if (open != null) {
                    LoadFileToTmpFromPathRunnable runnable = new LoadFileToTmpFromPathRunnable();
                    runnable.setFilePath(open);
                    ProgressMonitorDialog pmd = new ProgressMonitorDialog(getSite().getShell());
                    try {
                        InformationStructureRead read = InformationStructureRead.newSession(getModelObject());
                        pmd.run(true, false, runnable);
                        newTmpFile = runnable.getTmpFile();
                        CompoundCommand cc = new CompoundCommand();
                        cc.append(CommandFactory.addFileToInfoUnit(newTmpFile, getModelObject(), getEditingDomain()));
                        cc.append(SetCommand.create(getEditingDomain(), read.getChildByNodeId(Activator.FILENAME), read.getFeatureByNodeId(Activator.FILENAME), new Path(open).lastSegment()));
                        cc.append(SetCommand.create(getEditingDomain(), read.getChildByNodeId(Activator.ORIGIN), read.getFeatureByNodeId(Activator.ORIGIN), open));
                        cc.append(SetCommand.create(getEditingDomain(), getModelObject(), InfomngmntPackage.Literals.ABSTRACT_INFORMATION_UNIT__LABEL, new Path(open).lastSegment()));
                        cc.setLabel(Messages.FileEditPage_ChangeFile);
                        getEditingDomain().getCommandStack().execute(cc);
                    } catch (InvocationTargetException e1) {
                        e1.printStackTrace();
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
        doCreateSemanticSection(body, toolkit);
    }

    @Override
    public void bindValuesToUi() {
        super.bindValuesToUi();
        bind(BindingWidgetFactory.createTextBinding(text, this), Activator.FILENAME);
    }
}
