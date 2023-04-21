package br.edu.ufcg.ourgridportal.client.ui;

import br.edu.ufcg.ourgridportal.client.services.JDFDataLoadService;
import br.edu.ufcg.ourgridportal.client.services.JDFDataLoadServiceAsync;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.layout.HorizontalLayout;

public class OperationsJobPanel extends FormPanel {

    private Button cancelJobButton;

    private Button downloadAll;

    private JDFDataLoadServiceAsync JDFService;

    private int idJob;

    public OperationsJobPanel(int idJob) {
        super();
        this.idJob = idJob;
        init();
        layout();
        setListeners();
        packComponents();
    }

    private void init() {
        cancelJobButton = new Button("Cancel Job");
        downloadAll = new Button("Download Results");
        JDFService = JDFDataLoadService.Util.getInstance();
    }

    private void layout() {
        this.setTitle("Job Options");
        this.setFrame(true);
        this.setAutoWidth(true);
        this.setAutoHeight(true);
    }

    private void setListeners() {
        cancelJobButton.addListener(new ButtonListenerAdapter() {

            public void onClick(Button button, EventObject e) {
                JDFService.cancelJob(idJob, new AsyncCallback<Boolean>() {

                    public void onFailure(Throwable caught) {
                        System.out.println("Failed");
                    }

                    public void onSuccess(Boolean result) {
                        MessageBox.alert("Job Cancelled with sucess!!!");
                    }
                });
            }
        });
    }

    private void packComponents() {
        this.addButton(downloadAll);
        this.addButton(cancelJobButton);
        this.doLayout();
    }
}
