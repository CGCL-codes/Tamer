package com.gwtaf.ext.core.client.widget;

import java.util.List;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.gwtaf.core.client.action.ActionList;
import com.gwtaf.core.client.action.IAction;
import com.gwtaf.core.client.action.IActionSource;
import com.gwtaf.core.client.actionadapter.ActionFocusWidgetAdapter;
import com.gwtaf.core.client.widget.BorderlessButton;
import com.gwtaf.core.shared.i18n.GWTAFConstants;
import com.gwtaf.ext.core.client.GWTAFEXTImageBundle;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.layout.FitLayout;

public class ActionPanel extends Panel implements RequiresResize {

    private IActionSource actionSource;

    private VerticalPanel body;

    private Panel frame;

    public ActionPanel(IActionSource actionSource) {
        setTitle(GWTAFConstants.c.actions());
        setFrame(true);
        frame = new Panel();
        frame.setPaddings(4);
        frame.setLayout(new FitLayout());
        this.body = new VerticalPanel();
        this.body.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
        this.body.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
        this.body.setSize("100%", "100%");
        setAutoHeight(true);
        frame.add(body);
        add(frame);
        this.actionSource = actionSource;
    }

    @Override
    public void onResize() {
        frame.setSize(getInnerWidth(), getInnerHeight());
    }

    public void updateActions() {
        ActionList list = new ActionList();
        actionSource.addActions(list);
        List<IAction> actions = list.getActions();
        boolean changed = false;
        int idx = -1;
        for (final IAction action : actions) {
            ++idx;
            if (body.getWidgetCount() > idx) {
                BorderlessButton button = (BorderlessButton) body.getWidget(idx);
                if (button.getAction().equals(action)) {
                    ActionFocusWidgetAdapter adapter = new ActionFocusWidgetAdapter(action, button);
                    action.getWidgetHandler().addAdapter(adapter);
                    adapter.update();
                    continue;
                }
            }
            BorderlessButton button = new BorderlessButton(action, BorderlessButton.ICONANDTEXT, GWTAFEXTImageBundle.ACTION_ICON);
            ActionFocusWidgetAdapter adapter = new ActionFocusWidgetAdapter(action, button);
            action.getWidgetHandler().addAdapter(adapter);
            adapter.update();
            button.setWidth("100%");
            if (body.getWidgetCount() > idx) body.insert(button, idx); else body.add(button);
            changed = true;
        }
        int size = actions.size();
        if (size < body.getWidgetCount()) {
            changed = true;
            while (size < body.getWidgetCount()) body.remove(size);
        }
        if (changed) doLayout();
    }
}
