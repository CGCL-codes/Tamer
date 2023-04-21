package br.usp.ime.dojo.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

public class CodeArea {

    private TextArea codeArea;

    private TextArea resultArea;

    private TextArea testArea;

    private Timer timer;

    private DojoRoomServiceAsync dojoRoomService;

    public CodeArea() {
        codeArea = new TextArea();
        resultArea = new TextArea();
        testArea = new TextArea();
        resultArea.setReadOnly(true);
        codeArea.setReadOnly(true);
        testArea.setReadOnly(true);
        codeArea.setVisibleLines(10);
        resultArea.setVisibleLines(10);
        testArea.setVisibleLines(10);
        timer = new Timer() {

            public void run() {
                updateCodeArea(codeArea.isReadOnly());
                updateTestArea(testArea.isReadOnly());
                updateResultArea();
            }
        };
    }

    private void updateCodeArea(boolean readOnly) {
        if (dojoRoomService == null) dojoRoomService = GWT.create(DojoRoomService.class);
        if (readOnly) {
            dojoRoomService.updateCodeInClient(new AsyncCallback<String>() {

                public void onFailure(Throwable caught) {
                }

                public void onSuccess(String result) {
                    codeArea.setText(result);
                }
            });
        } else {
            dojoRoomService.updateCodeInServer(codeArea.getText(), new AsyncCallback<Void>() {

                public void onFailure(Throwable caught) {
                }

                public void onSuccess(Void result) {
                }
            });
        }
    }

    private void updateTestArea(boolean readOnly) {
        if (dojoRoomService == null) dojoRoomService = GWT.create(DojoRoomService.class);
        if (readOnly) {
            dojoRoomService.updateTestCodeInClient(new AsyncCallback<String>() {

                public void onFailure(Throwable caught) {
                }

                public void onSuccess(String result) {
                    testArea.setText(result);
                }
            });
        } else {
            dojoRoomService.updateTestCodeInServer(testArea.getText(), new AsyncCallback<Void>() {

                public void onFailure(Throwable caught) {
                }

                public void onSuccess(Void result) {
                }
            });
        }
    }

    private void updateResultArea() {
        if (dojoRoomService == null) dojoRoomService = GWT.create(DojoRoomService.class);
        dojoRoomService.updateResultCodeInClient(new AsyncCallback<String>() {

            public void onFailure(Throwable caught) {
            }

            public void onSuccess(String result) {
                resultArea.setText(result);
            }
        });
    }

    public Widget getCodeArea() {
        return codeArea;
    }

    public String getCodeAreaText() {
        return codeArea.getText();
    }

    public void setCodeAreaText(String string) {
        codeArea.setText(string);
    }

    public void setCodeAreaReadOnly(boolean readOnly) {
        codeArea.setReadOnly(readOnly);
    }

    public Widget getResultArea() {
        return resultArea;
    }

    public String getResultAreaText() {
        return resultArea.getText();
    }

    public void setResultAreaText(String string) {
        resultArea.setText(string);
    }

    public Widget getTestArea() {
        return testArea;
    }

    public String getTestAreaText() {
        return testArea.getText();
    }

    public void setTestAreaText(String string) {
        testArea.setText(string);
    }

    public void setTestAreaReadOnly(boolean readOnly) {
        testArea.setReadOnly(readOnly);
    }

    public void scheduleRepeating(int refreshRate) {
        timer.scheduleRepeating(refreshRate);
    }
}
