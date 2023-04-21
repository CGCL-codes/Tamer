package org.jampa.gui.wizard.podcastadder;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jampa.gui.translations.Messages;
import org.jampa.model.validators.PodcastNameValidator;

public class PodcastAdderPage extends WizardPage {

    private Composite container;

    private Text tePodcastName;

    private Text tePodcastUrl;

    private PodcastNameValidator nameValidator;

    protected PodcastAdderPage() {
        super("PodcastAdderWizard");
        setTitle(Messages.getString("PodcastAdderWizard.Title"));
        setDescription(Messages.getString("PodcastAdderWizard.IntroText"));
        nameValidator = new PodcastNameValidator();
    }

    @Override
    public void createControl(Composite parent) {
        container = new Composite(parent, SWT.NULL);
        GridLayout gl = new GridLayout(2, false);
        container.setLayout(gl);
        Label laPodcastName = new Label(container, SWT.NONE);
        laPodcastName.setText(Messages.getString("PodcastAdderWizard.PodcastName"));
        tePodcastName = new Text(container, SWT.BORDER);
        tePodcastName.setText(Messages.getString("PodcastAdderWizard.DefaultName"));
        GridData gd = new GridData(SWT.FILL, SWT.NONE, true, false);
        tePodcastName.setLayoutData(gd);
        tePodcastName.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                validatePage();
            }
        });
        Label laPodcastUrl = new Label(container, SWT.NONE);
        laPodcastUrl.setText(Messages.getString("PodcastAdderWizard.Url"));
        tePodcastUrl = new Text(container, SWT.BORDER);
        tePodcastUrl.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        tePodcastUrl.addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent e) {
            }

            public void keyReleased(KeyEvent e) {
                validatePage();
            }
        });
        tePodcastName.setSelection(0, tePodcastName.getText().length());
        setControl(container);
        validatePage();
    }

    private boolean checkPodcastName() {
        boolean returnResult;
        String result = nameValidator.isValid(tePodcastName.getText());
        if (result != null) {
            setErrorMessage(result);
            returnResult = false;
        } else {
            setErrorMessage(null);
            returnResult = true;
        }
        return returnResult;
    }

    private void validatePage() {
        setPageComplete(checkPodcastName() && !tePodcastName.getText().isEmpty() && !tePodcastUrl.getText().isEmpty());
    }

    public String getPodcastName() {
        return tePodcastName.getText();
    }

    public String getPodcastUrl() {
        return tePodcastUrl.getText();
    }

    public Control getControl() {
        return container;
    }
}
