package freetm.client.ui.lists;

import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.KeyboardListenerAdapter;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import gwtm.client.services.tm.virtual.TopicVirtual;
import gwtm.client.services.tm.virtual.VTopicVirtuals;
import net.mygwt.ui.client.Style;
import net.mygwt.ui.client.widget.WidgetContainer;
import net.mygwt.ui.client.widget.layout.BorderLayout;
import net.mygwt.ui.client.widget.layout.BorderLayoutData;

/**
 *
 * @author Yorgos
 */
public class PanListTopics extends WidgetContainer {

    private ListTopics m_list;

    private TextBox m_textFind;

    /** Creates a new instance of PanTopicMapLocatorsFind */
    public PanListTopics(String icon) {
        m_list = new ListTopics(icon);
        init();
    }

    public PanListTopics(String icon, int selectStyle) {
        m_list = new ListTopics(icon, selectStyle);
        init();
    }

    private void init() {
        BorderLayout layout = new BorderLayout();
        setLayout(layout);
        this.add(m_list, new BorderLayoutData(Style.CENTER));
        WidgetContainer panHorizFind = new WidgetContainer();
        BorderLayout layoutDown = new BorderLayout();
        panHorizFind.setLayout(layoutDown);
        this.add(panHorizFind, new BorderLayoutData(Style.SOUTH, 26));
        panHorizFind.add(new Label("Locate"), new BorderLayoutData(Style.WEST, 35));
        m_textFind = new TextBox();
        panHorizFind.add(m_textFind, new BorderLayoutData(Style.CENTER));
        m_textFind.addKeyboardListener(new KeyboardListenerAdapter() {

            public void onKeyUp(Widget sender, char keyCode, int modifiers) {
                String sFilter = m_textFind.getText();
                m_list.filterBasedOn(sFilter);
            }
        });
    }

    public void setLocators(VTopicVirtuals tVirtuals) {
        clearFilter();
        m_list.setTopicVirtuals(tVirtuals);
    }

    public TopicVirtual getSelectedTopic() {
        return m_list.getSelectedTopic();
    }

    public VTopicVirtuals getSelectedTopics() {
        return m_list.getSelectedTopics();
    }

    public void clearFilter() {
        m_textFind.setText("");
        m_list.filterBasedOn(null);
    }

    public ListTopics getListTopics() {
        return m_list;
    }
}
