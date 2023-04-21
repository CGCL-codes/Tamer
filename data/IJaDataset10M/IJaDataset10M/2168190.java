package org.columba.mail.gui.composer;

import java.util.Observable;
import java.util.Observer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.columba.core.xml.XmlElement;
import org.columba.mail.config.MailConfig;
import org.columba.mail.gui.composer.util.SubjectDialog;
import org.columba.mail.util.MailResourceLoader;

/**
 * Subject editor component.
 * 
 * @author fdietz
 */
public class SubjectController implements DocumentListener, Observer {

    private SubjectView view;

    private ComposerController controller;

    private XmlElement subject;

    private boolean ask;

    public SubjectController(ComposerController controller) {
        this.controller = controller;
        view = new SubjectView(this);
        XmlElement composerOptions = MailConfig.getInstance().getComposerOptionsConfig().getRoot().getElement("/options");
        subject = composerOptions.getElement("subject");
        if (subject == null) {
            subject = composerOptions.addSubElement("subject");
        }
        subject.addObserver(this);
        String askSubject = subject.getAttribute("ask_if_empty", "true");
        if (askSubject.equals("true")) {
            ask = true;
        } else {
            ask = false;
        }
    }

    public void installListener() {
        view.installListener(this);
    }

    public void updateComponents(boolean b) {
        if (b) {
            view.setText(controller.getModel().getHeaderField("Subject"));
        } else {
            controller.getModel().setHeaderField("Subject", view.getText());
        }
    }

    public boolean checkState() {
        String subject = controller.getModel().getHeaderField("Subject");
        if (ask == true) {
            if (subject.length() == 0) {
                subject = new String(MailResourceLoader.getString("dialog", "composer", "composer_no_subject"));
                SubjectDialog dialog = new SubjectDialog();
                dialog.showDialog(subject);
                if (dialog.success()) {
                    subject = dialog.getSubject();
                }
                controller.getModel().setHeaderField("Subject", subject);
            }
        }
        return true;
    }

    /**************** DocumentListener implementation ***************/
    public void insertUpdate(DocumentEvent e) {
    }

    public void removeUpdate(DocumentEvent e) {
    }

    public void changedUpdate(DocumentEvent e) {
    }

    public void update(Observable arg0, Object arg1) {
        String askSubject = subject.getAttribute("ask_if_empty", "true");
        if (askSubject.equals("true")) {
            ask = true;
        } else {
            ask = false;
        }
    }

    public SubjectView getView() {
        return view;
    }
}
