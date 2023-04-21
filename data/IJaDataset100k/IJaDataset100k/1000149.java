package spindles.gwt.client;

import java.util.List;
import spindles.gwt.shared.DTO;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.VerticalSplitPanel;
import com.google.gwt.user.client.ui.Widget;

public abstract class RightPanel extends Composite implements Subscriber {

    private VerticalPanel panel = new VerticalPanel();

    private VerticalSplitPanel splitPanel = new VerticalSplitPanel();

    protected Toolbar topWidget;

    protected Widget splitBottomWidget;

    protected Widget splitTopWidget;

    protected Widget mainPanel;

    protected DetailsTable detailsTable;

    public RightPanel() {
    }

    ;

    public RightPanel(Toolbar topWidget, Widget mainPanel) {
        init(topWidget, mainPanel);
    }

    public RightPanel(Toolbar topWidget, Widget splitTopWidget, Widget splitBottomWidget) {
        init(topWidget, splitTopWidget, splitBottomWidget);
    }

    protected void init(Toolbar topWidget, Widget splitTopWidget, Widget splitBottomWidget) {
        this.splitBottomWidget = splitBottomWidget;
        this.splitTopWidget = splitTopWidget;
        splitPanel.setTopWidget(splitTopWidget);
        splitPanel.setBottomWidget(splitBottomWidget);
        splitPanel.setWidth("100%");
        init(topWidget, splitPanel);
    }

    protected void init(Toolbar topWidget, Widget mainPanel) {
        this.topWidget = topWidget;
        this.mainPanel = mainPanel;
        panel.add(topWidget);
        panel.add(mainPanel);
        panel.setWidth("100%");
        panel.setHeight("100%");
        panel.setStyleName("spindles-RightPanel");
        initWidget(panel);
    }

    protected void setDetailsTable(String title, String[] headers, List dtos) {
        final TableData data = new TableData(title, headers);
        final DetailsTable dt = new DetailsTable(data);
        dt.addSubscriber(this);
        for (int i = 0; i < dtos.size(); i++) {
            DTO dto = (DTO) dtos.get(i);
            Registry.addDTO(dto);
            data.addRow(dto, i + 1);
        }
        dt.update();
        detailsTable = dt;
    }

    protected AsyncCallback setDetailsTable(String title, String[] headers) {
        final TableData data = new TableData(title, headers);
        final DetailsTable dt = new DetailsTable(data);
        dt.addSubscriber(this);
        Callback command = new Callback() {

            public void execute(Object result) {
                List dtos = (List) result;
                for (int i = 0; i < dtos.size(); i++) {
                    DTO dto = (DTO) dtos.get(i);
                    Registry.addDTO(dto);
                    data.addRow(dto, i + 1);
                }
                dt.update();
            }
        };
        detailsTable = dt;
        return Spindles.createCallBack(command);
    }

    protected TableData createTableData(List dtos, String title, String[] headers) {
        TableData result = new TableData(title, headers);
        for (int i = 0; i < dtos.size(); i++) {
            DTO dto = (DTO) dtos.get(i);
            Registry.addDTO(dto);
            result.addRow(dto, i + 1);
        }
        return result;
    }

    public void setHeight(int height) {
        setHeight("" + height);
        splitPanel.setHeight("" + (height - topWidget.getOffsetHeight() - 4));
    }

    public void replaceSplitBottomWidget(Widget w) {
        splitPanel.remove(splitBottomWidget);
        splitBottomWidget = w;
        splitPanel.setBottomWidget(splitBottomWidget);
    }

    public void replaceSplitTopWidget(Widget w) {
        splitPanel.remove(splitTopWidget);
        splitTopWidget = w;
        splitPanel.setTopWidget(splitTopWidget);
    }

    public void updateTitle(String newTitle) {
        topWidget.setTitle(newTitle);
    }

    public void replaceTopWidget(Toolbar t) {
        panel.remove(topWidget);
        topWidget = t;
        panel.insert(topWidget, 0);
    }

    public void replaceMainPanel(Widget mp) {
        panel.remove(mainPanel);
        mainPanel = mp;
        panel.insert(mainPanel, 1);
    }
}
