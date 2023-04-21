package gwtm.client.ui.props;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import gwtm.client.MainEntryPoint;
import gwtm.client.services.schema.SchemaServiceInvoker;
import gwtm.client.services.tm.Callback;
import gwtm.client.services.tm.virtual.NameVirtual;
import gwtm.client.services.tm.virtual.TopicVirtual;
import gwtm.client.services.tm.virtual.VTopicVirtuals;
import gwtm.client.ui.lists.DlgListTopics;
import net.mygwt.ui.client.Style;
import net.mygwt.ui.client.event.BaseEvent;
import net.mygwt.ui.client.event.SelectionListener;
import net.mygwt.ui.client.event.ShellListenerAdapter;
import net.mygwt.ui.client.widget.Button;
import net.mygwt.ui.client.widget.Dialog;
import net.mygwt.ui.client.widget.WidgetContainer;
import net.mygwt.ui.client.widget.layout.BorderLayout;
import net.mygwt.ui.client.widget.layout.BorderLayoutData;
import net.mygwt.ui.client.widget.layout.FillLayout;
import net.mygwt.ui.client.widget.layout.RowData;
import net.mygwt.ui.client.widget.layout.RowLayout;

public class DlgTopicName extends Dialog {

    private TopicVirtual m_tv, m_nameType;

    private NameVirtual m_nv;

    private Button m_btnType;

    private TextBox m_textType, m_textValue;

    /** Creates a new instance of DlgTopicName */
    public DlgTopicName(TopicVirtual tv, NameVirtual nv) {
        super(Style.CLOSE | Style.MODAL | Style.RESIZE);
        this.setText("Topic Name");
        setSize(300, 145);
        m_tv = tv;
        m_nv = nv;
        createPan();
        addButton("OK", new SelectionListener() {

            public void widgetSelected(BaseEvent be) {
                DlgTopicName.this.close();
            }
        });
        addButton("Cancel", new SelectionListener() {

            public void widgetSelected(BaseEvent be) {
                DlgTopicName.this.close();
            }
        });
    }

    private void createPan() {
        WidgetContainer c = getContent();
        getContent().setLayout(new FillLayout(10));
        WidgetContainer pan = new WidgetContainer();
        getContent().add(pan);
        BorderLayout layout = new BorderLayout();
        layout.setSpacing(10);
        pan.setLayout(layout);
        WidgetContainer type = getContainerType();
        pan.add(type, new BorderLayoutData(Style.NORTH, 25));
        WidgetContainer cent = new WidgetContainer();
        RowLayout layout2 = new RowLayout(Style.VERTICAL);
        cent.setLayout(layout2);
        pan.add(cent, new BorderLayoutData(Style.CENTER));
        WidgetContainer locator = getContainerValueLocator();
        cent.add(locator, new RowData(RowData.FILL_HORIZONTAL));
        displayName();
    }

    private WidgetContainer getContainerType() {
        WidgetContainer c = new WidgetContainer();
        RowLayout layout = new RowLayout(Style.HORIZONTAL);
        c.setLayout(layout);
        c.add(new Label("Type:"), new RowData(RowData.FILL_VERTICAL));
        m_textType = new TextBox();
        m_textType.setEnabled(false);
        c.add(m_textType, new RowData(RowData.FILL_VERTICAL));
        m_btnType = new Button("...", new SelectionListener() {

            public void widgetSelected(BaseEvent be) {
                selectType();
            }
        });
        c.add(m_btnType);
        return c;
    }

    private WidgetContainer getContainerValueLocator() {
        WidgetContainer c = new WidgetContainer();
        RowLayout layout = new RowLayout(Style.HORIZONTAL);
        c.setLayout(layout);
        c.add(new Label("Value:"), new RowData(RowData.FILL_VERTICAL));
        m_textValue = new TextBox();
        c.add(m_textValue, new RowData(RowData.FILL_BOTH));
        return c;
    }

    private void displayName() {
        if (m_nv == null) return;
        m_nameType = m_nv._getType();
        if (m_nameType != null) m_textType.setText(m_nameType.getDisplayName());
        if (m_nv._getValue() != null) m_textValue.setText(m_nv._getValue());
    }

    public void selectType() {
        final DlgListTopics d = new DlgListTopics("Topic Name Types (from schema)", "icon-nameType");
        ShellListenerAdapter listener = new ShellListenerAdapter() {

            public void shellClosed(BaseEvent be) {
                DlgListTopics _d = (DlgListTopics) be.widget;
                TopicVirtual t = _d.getSelTopic();
                if (t == null) return;
                m_nameType = t;
                m_textType.setText(m_nameType.getDisplayName());
            }
        };
        d.addShellListener(listener);
        d.open();
        Callback.Ts ret = new Callback.Ts() {

            public void retTopics(VTopicVirtuals ocTypes) {
                d.getListTopics().setTopicVirtuals(ocTypes);
                d.getListTopics().selectTopicServer(m_nameType);
            }
        };
        SchemaServiceInvoker.getNameTypes(ret);
    }

    public void createGetName(final Callback.N ret) {
        if (m_nv == null) {
            m_tv.createTopicName(m_textValue.getText(), ret);
        } else {
            m_nv.setValue(m_textValue.getText(), new Callback.OK() {

                public void retOK() {
                    ret.retTopicName(m_nv);
                    MainEntryPoint.getInstance().setDerty(true);
                }
            });
        }
    }
}
