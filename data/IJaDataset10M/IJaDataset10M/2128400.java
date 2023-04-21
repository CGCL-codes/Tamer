package com.gorillalogic.faces.listeners;

import java.util.Iterator;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.faces.event.ValueChangeEvent;
import javax.faces.event.ValueChangeListener;
import org.apache.log4j.Logger;
import com.gorillalogic.dal.AccessException;
import com.gorillalogic.faces.FacesRuntimeException;
import com.gorillalogic.faces.beans.GlRequest;
import com.gorillalogic.faces.beans.GlSession;
import com.gorillalogic.faces.components.Controls;
import com.gorillalogic.faces.components.GorillaComponent;
import com.gorillalogic.faces.components.glui.GLUIComponentBase;
import com.gorillalogic.faces.components.glui.GLUIInput;
import com.gorillalogic.faces.components.glui.GLUIPanel;
import com.gorillalogic.faces.components.glui.GLUISelectOne;
import com.gorillalogic.faces.util.FacesUtils;

/**
 * @author Stu
 * 
 */
public class SearchListener implements ActionListener {

    static Logger logger = Logger.getLogger(SearchListener.class);

    public static SearchListener listener = new SearchListener();

    public static SearchValueListener valueListener = new SearchValueListener();

    public void processAction(ActionEvent ev) throws AbortProcessingException {
        List list = GlRequest.getCurrent().getSearchArgs();
        if (list == null) {
            GlSession.getCurrentInstance().addMessage(null, "No search terms requested");
            return;
        }
        Iterator i = list.iterator();
        StringBuffer s = new StringBuffer();
        while (i.hasNext()) {
            String[] args = (String[]) i.next();
            s.append("((" + args[0] + ") " + args[1] + " (\"" + args[2] + "\"))");
            if (i.hasNext()) {
                s.append(" and ");
            }
        }
        try {
            GlSession.getCurrentInstance().getGosh().run("cd [" + s.toString() + "]");
        } catch (AccessException e) {
            GlSession.getCurrentInstance().addMessage("Search expression invalid: " + s.toString());
            FacesUtils.dispatchView(GlSession.SEARCH);
            return;
        }
        FacesUtils.dispatchView(GlSession.LIST);
    }

    public static class SearchValueListener implements ValueChangeListener {

        public void processValueChange(ValueChangeEvent event) throws AbortProcessingException {
            GLUISelectOne select = (GLUISelectOne) ((GLUIComponentBase.GLUIComponentExtension) event.getComponent()).getWrapper();
            String op = (String) select.getValue();
            GLUIPanel grid = (GLUIPanel) FacesUtils.findParent(select, GLUIPanel.class);
            GLUIInput input = (GLUIInput) grid.getChildren().get(1);
            String value = (String) input.getSubmittedValue();
            String context = (String) select.getAttributes().get(GorillaComponent.CONTEXT);
            if (!value.equals("") && op.equals("")) {
                op = "=";
            }
            if (!op.equals("")) {
                GlRequest.getCurrent().addSearchArg(context, op, value);
            }
        }
    }
}
