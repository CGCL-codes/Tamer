package gwtm.client.ui.topicmaps;

import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.KeyboardListenerAdapter;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import net.mygwt.ui.client.Style;
import net.mygwt.ui.client.widget.WidgetContainer;
import net.mygwt.ui.client.widget.layout.BorderLayout;
import net.mygwt.ui.client.widget.layout.BorderLayoutData;

/**
 *
 * @author Yorgos
 */
public class PanTopicMapLocatorsFind extends WidgetContainer {

    private ListTopicMapLocators m_list;

    private TextBox m_textFind;

    /** Creates a new instance of PanTopicMapLocatorsFind */
    public PanTopicMapLocatorsFind() {
        this.setLayout(new BorderLayout());
        m_list = new ListTopicMapLocators();
        this.add(m_list, new BorderLayoutData(Style.CENTER));
        WidgetContainer panHorizFind = new WidgetContainer();
        this.add(panHorizFind, new BorderLayoutData(Style.SOUTH, 26));
        panHorizFind.setLayout(new BorderLayout());
        panHorizFind.add(new Label("Locate"), new BorderLayoutData(Style.WEST, 30));
        m_textFind = new TextBox();
        panHorizFind.add(m_textFind, new BorderLayoutData(Style.CENTER));
        m_textFind.addKeyboardListener(new KeyboardListenerAdapter() {

            public void onKeyUp(Widget sender, char keyCode, int modifiers) {
                String sFilter = m_textFind.getText();
                m_list.filterBasedOn(sFilter);
            }
        });
    }

    public void setLocators(String[] names) {
        clearFilter();
        m_list.setLocators(names);
    }

    public String getSelectedText() {
        return m_list.getSelectedText();
    }

    public void clearFilter() {
        m_textFind.setText("");
        m_list.filterBasedOn(null);
    }

    public ListTopicMapLocators getList() {
        return m_list;
    }
}
