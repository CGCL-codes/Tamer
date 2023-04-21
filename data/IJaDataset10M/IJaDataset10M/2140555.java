package pl.edu.pw.polygen.ui.file.upload;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import pl.edu.pw.polygen.util.ApplicationHelper;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ProgressIndicator;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.FailedEvent;
import com.vaadin.ui.Upload.FinishedEvent;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.StartedEvent;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.VerticalLayout;

public class FileUploadPanel extends VerticalLayout {

    private Label status = new Label("Please select a file to upload");

    private ProgressIndicator pi = new ProgressIndicator();

    private MyReceiver receiver = new MyReceiver();

    private HorizontalLayout progressLayout = new HorizontalLayout();

    private Upload upload = new Upload(null, receiver);

    private static String tempPath = "c:\\polygen\\tmp\\";

    public FileUploadPanel() {
        setSpacing(true);
        receiver.setSlow(true);
        addComponent(status);
        addComponent(upload);
        addComponent(progressLayout);
        upload.setImmediate(true);
        upload.setButtonCaption("Select file");
        progressLayout.setSpacing(true);
        progressLayout.setVisible(false);
        progressLayout.addComponent(pi);
        progressLayout.setComponentAlignment(pi, Alignment.MIDDLE_LEFT);
        final Button cancelProcessing = new Button("Cancel");
        cancelProcessing.addListener(new Button.ClickListener() {

            public void buttonClick(ClickEvent event) {
                upload.interruptUpload();
            }
        });
        cancelProcessing.setStyleName("small");
        progressLayout.addComponent(cancelProcessing);
        upload.addListener(new Upload.StartedListener() {

            public void uploadStarted(StartedEvent event) {
                upload.setVisible(false);
                progressLayout.setVisible(true);
                pi.setValue(0f);
                pi.setPollingInterval(500);
                status.setValue("Uploading file \"" + event.getFilename() + "\"");
            }
        });
        upload.addListener(new Upload.ProgressListener() {

            public void updateProgress(long readBytes, long contentLength) {
                pi.setValue(new Float(readBytes / (float) contentLength));
            }
        });
        upload.addListener(new Upload.SucceededListener() {

            public void uploadSucceeded(SucceededEvent event) {
                status.setValue("Uploading file \"" + event.getFilename() + "\" succeeded");
            }
        });
        upload.addListener(new Upload.FailedListener() {

            public void uploadFailed(FailedEvent event) {
                status.setValue("Uploading interrupted");
            }
        });
        upload.addListener(new Upload.FinishedListener() {

            public void uploadFinished(FinishedEvent event) {
                progressLayout.setVisible(false);
                upload.setVisible(false);
            }
        });
    }

    public static class MyReceiver implements Receiver {

        private String fileName;

        private String mtype;

        private boolean sleep;

        private int total = 0;

        private File file;

        public OutputStream receiveUpload(String filename, String mimetype) {
            String userName = ApplicationHelper.getLoggedUser().getUsername();
            this.fileName = userName + "_" + filename;
            mtype = mimetype;
            FileOutputStream fos = null;
            file = new File(tempPath + fileName);
            try {
                fos = new FileOutputStream(file);
            } catch (final java.io.FileNotFoundException e) {
                e.printStackTrace();
                return null;
            }
            return fos;
        }

        public String getFileName() {
            return fileName;
        }

        public String getMimeType() {
            return mtype;
        }

        public void setSlow(boolean value) {
            sleep = value;
        }
    }

    public File getFile() {
        File file = new File(tempPath + receiver.fileName);
        if (file.exists() == true) {
            return file;
        }
        return null;
    }
}
