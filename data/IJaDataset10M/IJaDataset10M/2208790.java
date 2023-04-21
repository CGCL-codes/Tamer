package pl.xperios.rdk.client.blog;

import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.FlowData;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.user.client.ui.Widget;

/**
 *
 * @author Praca
 */
public class BlogView extends ContentPanel {

    private Button button1 = new Button("page1");

    private Button button2 = new Button("page2");

    private Button button3 = new Button("page3");

    private Button button4 = new Button("page4");

    private ContentPanel menu = new ContentPanel();

    private ContentPanel main = new ContentPanel();

    public BlogView() {
        setHeading("Blog");
        setLayout(new RowLayout());
        button1.setStyleAttribute("float", "left");
        menu.add(button1, new FlowData(5));
        button2.setStyleAttribute("float", "left");
        menu.add(button2, new FlowData(5));
        button3.setStyleAttribute("float", "left");
        menu.add(button3, new FlowData(5));
        button4.setStyleAttribute("float", "left");
        menu.add(button4, new FlowData(5));
        menu.setHeading("Menu");
        add(menu, new RowData(1, -1, new Margins(5)));
        main.setHeading("Main");
        add(main, new RowData(1, -1, new Margins(5)));
    }

    void setCenterSection(Widget widget) {
        main.removeAll();
        main.add(widget, new FlowData(5));
        main.layout();
    }

    public Button getButton1() {
        return button1;
    }

    public Button getButton2() {
        return button2;
    }

    public Button getButton3() {
        return button3;
    }

    public Button getButton4() {
        return button4;
    }

    void clear() {
        main.removeAll();
        main.layout();
    }
}
