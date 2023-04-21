package gov.nih.niaid.bcbb.nexplorer3.client.widgets;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormHandler;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormSubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormSubmitEvent;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

@SuppressWarnings("deprecation")
public class UploadFileDialogBox {

    public static DialogBox inputDialogBox;

    static String name;

    static String content;

    static Button ok;

    private static class MyDialog extends DialogBox {

        public MyDialog() {
            setText("Upload file...");
            final FormPanel form = new FormPanel();
            form.setEncoding(FormPanel.ENCODING_MULTIPART);
            form.setMethod(FormPanel.METHOD_POST);
            form.setAction("/gpipe/cgi-bin/results.cgi");
            FileUpload upload = new FileUpload();
            upload.setName("upload_file");
            HorizontalPanel formPanel = new HorizontalPanel();
            formPanel.add(upload);
            form.setWidget(formPanel);
            VerticalPanel mainPanel = new VerticalPanel();
            HorizontalPanel buttonPanel = new HorizontalPanel();
            Button close = new Button("Close");
            close.addClickListener(new ClickListener() {

                public void onClick(Widget sender) {
                    hide();
                }
            });
            Button submit = new Button("Upload");
            submit.addClickListener(new ClickListener() {

                public void onClick(Widget sender) {
                    form.submit();
                }
            });
            buttonPanel.add(submit);
            buttonPanel.add(close);
            form.addFormHandler(new FormHandler() {

                public void onSubmit(FormSubmitEvent event) {
                }

                public void onSubmitComplete(FormSubmitCompleteEvent event) {
                    hide();
                }
            });
            SimplePanel emptyRow = new SimplePanel();
            emptyRow.add(new Label(" "));
            mainPanel.add(emptyRow);
            mainPanel.add(form);
            mainPanel.add(buttonPanel);
            setWidget(mainPanel);
        }
    }

    public UploadFileDialogBox() {
    }

    public void showDialog() {
        inputDialogBox = new MyDialog();
        int nLeft = 80;
        int nTop = 100;
        inputDialogBox.setPopupPosition(nLeft, nTop);
        inputDialogBox.show();
    }
}
