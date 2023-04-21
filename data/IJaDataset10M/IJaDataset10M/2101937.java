package org.rydia.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * <u><b><font color="red">FOR INTERNAL USE ONLY.</font></b></u>
 * Copyright (c)2007, Daniel Kaplan
 *
 * @author Daniel Kaplan
 * @since 7.11.6
 */
public class Rydia implements EntryPoint {

    private SurveyServiceAsync service;

    private NavBar navBar;

    private Survey survey;

    public void onModuleLoad() {
        service = SurveyService.App.getInstance();
        ChangeListener changeListener = new ChangeListener();
        survey = new Survey(changeListener);
        RootPanel.get("rydia_survey").add(survey.getPanel());
        navBar = new NavBar(new ButtonListener());
        RootPanel.get("rydia_navbar").add(navBar.getPanel());
        changeListener.answerChanged(null, null);
    }

    private String surveyProvider() {
        return DOM.getElementProperty(DOM.getElementById("surveyProvider"), "content");
    }

    private class ButtonListener implements ClickListener {

        public void onClick(Widget sender) {
            service.buttonClicked(surveyProvider(), sender.getStyleName(), new AsyncCallback() {

                public void onFailure(Throwable caught) {
                    caught.printStackTrace();
                }

                public void onSuccess(Object result) {
                    survey.newPage();
                    navBar.setState((String[][]) result);
                    survey.setState((String[][]) result);
                }
            });
        }
    }

    public class ChangeListener {

        public void answerChanged(String id, String answer) {
            service.changeAnswer(surveyProvider(), id, answer, new AsyncCallback() {

                public void onFailure(Throwable caught) {
                    caught.printStackTrace();
                }

                public void onSuccess(Object result) {
                    navBar.setState((String[][]) result);
                    survey.setState((String[][]) result);
                }
            });
        }

        public void pageChanged(int toPageNum) {
            service.changePage(surveyProvider(), toPageNum, new AsyncCallback() {

                public void onFailure(Throwable caught) {
                    caught.printStackTrace();
                }

                public void onSuccess(Object result) {
                    navBar.setState((String[][]) result);
                    survey.setState((String[][]) result);
                }
            });
        }
    }
}
