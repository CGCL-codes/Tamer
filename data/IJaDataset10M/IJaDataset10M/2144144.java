package org.remus.infomngmnt.welcome;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ImageHyperlink;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

public class WelcomePage extends FormPage {

    /**
	 * Create the form page
	 * 
	 * @param id
	 * @param title
	 */
    public WelcomePage(final String id, final String title) {
        super(id, title);
    }

    /**
	 * Create the form page
	 * 
	 * @param editor
	 * @param id
	 * @param title
	 */
    public WelcomePage(final FormEditor editor, final String id, final String title) {
        super(editor, id, title);
    }

    /**
	 * Create contents of the form
	 * 
	 * @param managedForm
	 */
    @Override
    protected void createFormContent(final IManagedForm managedForm) {
        FormToolkit toolkit = managedForm.getToolkit();
        ScrolledForm form = managedForm.getForm();
        Composite body = form.getBody();
        body.setLayout(new GridLayout());
        toolkit.paintBordersFor(body);
        final Section section = toolkit.createSection(body, Section.DESCRIPTION);
        section.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        section.setDescription("Welcome to the Remus Information Management. This application allows you to store all you incoming information in an easy and comfortable way. Check out the following links to see what RIM can do for you.");
        final Composite composite_1 = toolkit.createComposite(section, SWT.NONE);
        composite_1.setLayout(new TableWrapLayout());
        toolkit.paintBordersFor(composite_1);
        section.setClient(composite_1);
        final ImageHyperlink learnRimInImageHyperlink = toolkit.createImageHyperlink(body, SWT.NONE);
        learnRimInImageHyperlink.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false));
        learnRimInImageHyperlink.setText("Learn RIM in 5 Minutes (Video)");
        final Section linksSection = toolkit.createSection(body, SWT.NONE);
        linksSection.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false));
        linksSection.setText("Links");
        final Composite composite_2 = toolkit.createComposite(linksSection, SWT.NONE);
        final TableWrapLayout tableWrapLayout = new TableWrapLayout();
        tableWrapLayout.makeColumnsEqualWidth = true;
        tableWrapLayout.numColumns = 2;
        composite_2.setLayout(tableWrapLayout);
        toolkit.paintBordersFor(composite_2);
        linksSection.setClient(composite_2);
        final ImageHyperlink imageHyperlink_1 = toolkit.createImageHyperlink(composite_2, SWT.NONE);
        imageHyperlink_1.setLayoutData(new TableWrapData(TableWrapData.FILL, TableWrapData.TOP));
        imageHyperlink_1.setText("New ImageHyperlink");
        final ImageHyperlink imageHyperlink = toolkit.createImageHyperlink(composite_2, SWT.NONE);
        imageHyperlink.setLayoutData(new TableWrapData(TableWrapData.FILL, TableWrapData.TOP));
        imageHyperlink.setText("New ImageHyperlink");
        final ImageHyperlink imageHyperlink_2 = toolkit.createImageHyperlink(composite_2, SWT.NONE);
        imageHyperlink_2.setText("New ImageHyperlink");
        final Section latestNewsSection = toolkit.createSection(body, SWT.NONE);
        latestNewsSection.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        latestNewsSection.setText("Latest News");
        final Composite composite = toolkit.createComposite(latestNewsSection, SWT.NONE);
        composite.setLayout(new GridLayout());
        toolkit.paintBordersFor(composite);
        latestNewsSection.setClient(composite);
        final Browser browser = new Browser(composite, SWT.NONE);
        browser.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        toolkit.adapt(browser, true, true);
        browser.setUrl("http://www.eclipse.org");
        final Button button = toolkit.createButton(body, "Show this page on startup", SWT.CHECK);
        button.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false));
    }
}
