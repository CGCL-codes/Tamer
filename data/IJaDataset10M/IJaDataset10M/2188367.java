package org.mvz.gwttest.client;

import java.util.Map;
import org.mvz.gwt.client.Sidebar;
import org.mvz.gwt.client.DynamicForm;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class TestSidebar extends Composite implements Sidebar.Listener, DynamicForm.Handler {

    private Sidebar sidebar = new Sidebar();

    private TextArea console = new TextArea();

    private String messages = "";

    private static final String loremIpsum = "Lorem ipsum dolor sit amet, " + "consectetuer adipiscing elit. Donec elementum, neque ac adipiscing " + "tincidunt, libero augue imperdiet diam, in ullamcorper diam eros sed " + "massa. Nullam accumsan leo sit amet metus. Quisque ut ligula. Sed mattis " + "nonummy neque. Ut faucibus sem id nisi. Integer sed dui. Pellentesque " + "adipiscing erat sit amet elit. Mauris diam. Phasellus dictum, velit sit " + "amet mattis imperdiet, nunc nisi molestie tellus, in laoreet libero est " + "in urna. Curabitur orci lorem, dignissim quis, mattis fermentum, adipiscing " + "sit amet, enim. Pellentesque dictum elit ac metus. Integer ac dui. Aenean " + "tempor quam a diam. Nulla facilisi. In tortor purus, tempus.";

    private DynamicForm form = new DynamicForm();

    private boolean formExpanded = true;

    private PushButton toggleFormButton = new PushButton("Toggle form", new ClickListener() {

        public void onClick(Widget w) {
            formExpanded = !formExpanded;
            sidebar.setExpanded(form, formExpanded);
        }
    });

    private PushButton exclusiveButton = new PushButton("Toggle exclusive", new ClickListener() {

        private boolean exclusive = false;

        public void onClick(Widget w) {
            exclusive = !exclusive;
        }
    });

    private void buildSidebar() {
        form.setHandler(this);
        form.addTextField("name", "Name", false, DynamicForm.ALWAYS_VALID);
        form.addTextField("date", "Dame", false, DynamicForm.ALWAYS_VALID);
        form.addTextField("locality", "Locality", false, DynamicForm.ALWAYS_VALID);
        sidebar.add(form, "Verbatim Locality", true);
        TextArea foo = new TextArea();
        foo.setSize("100%", "20em");
        foo.setText(loremIpsum);
        sidebar.add(foo, "Lorem Ipsum", true);
        PushButton b1 = new PushButton("Click me!");
        PushButton b2 = new PushButton("No, click ME!");
        PushButton b3 = new PushButton("Shut up!");
        VerticalPanel panel = new VerticalPanel();
        panel.add(b1);
        panel.add(b2);
        panel.add(b3);
        sidebar.add(panel, "Stupid Buttons", true);
        sidebar.addListener(this);
    }

    public TestSidebar() {
        buildSidebar();
        console.setSize("20em", "30em");
        HorizontalPanel mainPanel = new HorizontalPanel();
        mainPanel.add(console);
        mainPanel.add(toggleFormButton);
        mainPanel.add(exclusiveButton);
        mainPanel.add(sidebar);
        mainPanel.setCellHorizontalAlignment(sidebar, HasHorizontalAlignment.ALIGN_RIGHT);
        mainPanel.setWidth("100%");
        initWidget(mainPanel);
    }

    public boolean onWidgetClosing(Widget w) {
        messages += "onWidgetClosing\n";
        console.setText(messages);
        if (w == form) return false;
        return true;
    }

    public boolean onWidgetOpening(Widget w) {
        messages += "onWidgetOpening\n";
        console.setText(messages);
        return true;
    }

    public void onCancel(DynamicForm sender) {
    }

    public void onSubmit(DynamicForm sender, Map formValues) {
    }
}
