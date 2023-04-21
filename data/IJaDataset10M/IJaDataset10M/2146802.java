package edu.upc.lsi.kemlg.aws.gui.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class SendPerceptCallback implements AsyncCallback {

    private View view;

    public SendPerceptCallback(View view) {
        this.view = view;
    }

    public void onFailure(Throwable caught) {
        view.render(caught);
    }

    public void onSuccess(Object result) {
        view.finishSendPercept();
    }
}
