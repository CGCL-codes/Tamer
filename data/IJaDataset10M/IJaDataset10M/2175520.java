package net.sourceforge.symba.webapp.client.gui.main.edit.add;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.rpc.AsyncCallback;
import net.sourceforge.symba.webapp.client.util.fuge.bio.investigation.InvestigationClient;
import net.sourceforge.symba.webapp.client.util.fuge.common.audit.PersonClient;

/**
 * Created by IntelliJ IDEA.
 * User: craig
 * Date: 04-Sep-2009
 * Time: 17:54:10
 * To change this template use File | Settings | File Templates.
 */
public class AddExperiment extends VerticalPanel implements HasValueChangeHandlers<InvestigationClient> {

    private static final String SUBMIT_TEXT = "Add";

    public AddExperiment(final PersonClient creator) {
        super();
        final ExperimentBasicInfo form = new ExperimentBasicInfo();
        final Button submit = new Button(SUBMIT_TEXT);
        submit.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                final String experimentName = form.getExperimentName();
                final String hypothesis = form.getHypothesis();
                final String conclusion = form.getConclusion();
                InvestigationClient.newInstance(experimentName, hypothesis, conclusion, new AsyncCallback<InvestigationClient>() {

                    public void onFailure(final Throwable caught) {
                        caught.printStackTrace();
                    }

                    public void onSuccess(final InvestigationClient result) {
                        ValueChangeEvent.fire(AddExperiment.this, result);
                    }
                });
            }
        });
        add(form);
        add(submit);
    }

    /**
     * Adds a {@link com.google.gwt.event.logical.shared.ValueChangeEvent} handler.
     *
     * @param handler the handler
     * @return the registration for the event
     */
    public HandlerRegistration addValueChangeHandler(final ValueChangeHandler<InvestigationClient> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }
}
