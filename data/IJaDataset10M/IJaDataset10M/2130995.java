package rm.gwt.appspace.client.informer;

import static rm.gwt.appspace.client.HasApplication.*;
import static com.google.gwt.user.client.ui.HasVerticalAlignment.*;
import java.util.EnumSet;
import rm.gwt.appspace.client.util.MinMaxButton;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public abstract class InfoFace extends Composite implements ClickListener {

    private MinMaxButton mmBtn = null;

    private HTML preview = null;

    private ScrollPanel scrollPanel = null;

    private Grid detailGrid = null;

    public InfoFace() {
        super();
        mmBtn = new MinMaxButton();
        mmBtn.addClickListener(this);
        preview = new HTML();
        HorizontalPanel previewPanel = new HorizontalPanel();
        previewPanel.add(mmBtn);
        previewPanel.add(preview);
        previewPanel.setCellWidth(preview, MAX_WIDTH);
        previewPanel.setCellVerticalAlignment(preview, ALIGN_MIDDLE);
        previewPanel.addStyleName("info-preview");
        detailGrid = new Grid(1, 2);
        detailGrid.getCellFormatter().setWidth(0, 1, MAX_WIDTH);
        scrollPanel = new ScrollPanel();
        scrollPanel.setStyleName("info-detail");
        scrollPanel.setHeight("4cm");
        scrollPanel.setWidget(detailGrid);
        VerticalPanel mainPanel = new VerticalPanel();
        mainPanel.setWidth(MAX_WIDTH);
        mainPanel.add(previewPanel);
        mainPanel.add(scrollPanel);
        initWidget(mainPanel);
        scrollPanel.setVisible(false);
    }

    public void onClick(Widget sender) {
        if (getMessageCount() > 0) {
            scrollPanel.setVisible(!mmBtn.isMinimised());
            render();
        }
    }

    public void render() {
        int totalMsgs = getMessageCount();
        if (totalMsgs == 1 || totalMsgs != detailGrid.getRowCount()) {
            preview.setHTML(getMessageContent(0));
            formatMessageWidget(preview, 0);
            if (!mmBtn.isMinimised()) {
                detailGrid.resizeRows(totalMsgs);
                HTML htmlTime = null, htmlMessage = null;
                for (int row = 0; row < totalMsgs; row++) {
                    htmlTime = getMessageWidget(row, 0);
                    htmlTime.setHTML(getMessageTime(row));
                    formatMessageWidget(htmlTime);
                    htmlTime.addStyleDependentName(InfoType.INFO_MSG.getStyleDependentName());
                    htmlMessage = getMessageWidget(row, 1);
                    htmlMessage.setHTML(getMessageContent(row));
                    formatMessageWidget(htmlMessage, row);
                }
            }
        }
    }

    protected abstract int getMessageCount();

    protected abstract String getMessageTime(int row);

    protected abstract String getMessageContent(int row);

    protected abstract InfoType getMessageType(int row);

    private HTML getMessageWidget(int row, int column) {
        HTML html = (HTML) detailGrid.getWidget(row, column);
        if (html == null) {
            html = new HTML();
            detailGrid.setWidget(row, column, html);
        }
        return html;
    }

    private void formatMessageWidget(HTML messageWidget, int row) {
        formatMessageWidget(messageWidget);
        String styleSuffix = getMessageType(row).getStyleDependentName();
        messageWidget.addStyleDependentName(styleSuffix);
    }

    private void formatMessageWidget(HTML messageWidget) {
        for (InfoType infoType : EnumSet.allOf(InfoType.class)) {
            messageWidget.removeStyleDependentName(infoType.getStyleDependentName());
        }
    }
}
