package sfeir.gwt.ergosoom.client;

import sfeir.gwt.ergosoom.client.model.Person;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;

public class ImportDialog extends DialogBox {

    Messages message = GWT.create(Messages.class);

    private Person person = null;

    private class ImportFormPanel extends FormPanel {

        public ImportFormPanel() {
            super();
            setAction("/gwtergosoom/import");
            setEncoding(ENCODING_MULTIPART);
            setMethod(METHOD_POST);
            Grid panel = new Grid(5, 2);
            setWidget(panel);
            panel.setWidget(0, 1, new Label(message.caution_erase_profile()));
            FileUpload upload = new FileUpload();
            upload.setName("uploadVCard");
            panel.setWidget(1, 0, new Label(message.vcard()));
            panel.setWidget(1, 1, upload);
            panel.setWidget(2, 0, new Label(message.dot_tel()));
            TextBox telTB = new TextBox();
            telTB.setName("telDomain");
            panel.setWidget(2, 1, telTB);
            panel.setWidget(3, 0, new Label(message.website()));
            TextBox webTB = new TextBox();
            webTB.setName("webSite");
            panel.setWidget(3, 1, webTB);
            panel.setWidget(4, 0, new Button(message.cancel(), new ClickHandler() {

                public void onClick(ClickEvent event) {
                    event.preventDefault();
                    ImportDialog.this.hide();
                }
            }));
            panel.setWidget(4, 1, new Button(message.submit(), new ClickHandler() {

                public void onClick(ClickEvent event) {
                    submit();
                }
            }));
            addSubmitHandler(new FormPanel.SubmitHandler() {

                public void onSubmit(SubmitEvent event) {
                }
            });
            addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {

                public void onSubmitComplete(SubmitCompleteEvent event) {
                    String result = event.getResults();
                    if (result.indexOf("@ERGOSOOM@PERSON!#@") >= 0) {
                        String s = result.substring(result.indexOf("@ERGOSOOM@PERSON!#@") + 19, result.lastIndexOf("@ERGOSOOM@PERSON!#@"));
                        person = Person.unblob(s);
                        ImportDialog.this.hide();
                    } else {
                        Window.alert(result);
                    }
                }
            });
        }
    }

    public ImportDialog() {
        super(false, true);
        setText(message.import_profile());
        setWidget(new ImportFormPanel());
    }

    public Person getPerson() {
        return person;
    }
}
