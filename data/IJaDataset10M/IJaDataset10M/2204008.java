package org.appspy.viewer.client.paramchooser.old;

import java.util.Collection;
import org.appspy.viewer.client.ViewerModule;
import org.appspy.viewer.client.action.AbstractAction;
import org.appspy.viewer.client.form.ReportParamForm;
import com.google.gwt.core.client.GWT;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.grid.PropertyGridPanel;
import com.gwtext.client.widgets.layout.FitLayout;

/**
 * @author Olivier HEDIN / olivier@appspy.org
 */
public class ParamWindow extends Window {

    protected PropertyGridPanel mPropertyGridPanel = null;

    protected AbstractAction mAction = null;

    public ParamWindow(ViewerModule viewerModule, final Collection<ReportParamForm> params, final AbstractAction action) {
        super();
        mAction = action;
        setTitle("Parameter values");
        setFrame(true);
        setLayout(new FitLayout());
        setSize(400, 300);
        mPropertyGridPanel = ParamPropertyGridPanelFactory.getPropertyGridPanel(viewerModule, params);
        add(mPropertyGridPanel);
        Button okButton = new Button("OK");
        okButton.addListener(new ButtonListenerAdapter() {

            @Override
            public void onClick(Button button, EventObject e) {
                try {
                    for (ReportParamForm param : params) {
                        ParamPropertyGridPanelFactory.setLastInputValue(param.getName(), param.getDefault());
                    }
                    executeAction();
                    destroy();
                } catch (Exception ex) {
                    GWT.log(ex.getMessage(), ex);
                }
            }
        });
        addButton(okButton);
    }

    public void executeAction() {
        mAction.execute();
    }
}
