package org.mitre.rt.client.ui.recommendations;

import java.awt.Component;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.TableCellRenderer;
import org.apache.log4j.Logger;
import org.mitre.rt.client.ui.tables.RecommendationTableItem;
import org.mitre.rt.client.util.GlobalUITools;
import org.mitre.rt.client.util.MixedContent;
import org.mitre.rt.client.util.StringUtils;
import org.mitre.rt.common.xml.RTHashKey;
import org.mitre.rt.rtclient.HtmlTextType;

/**
 *
 * @author BWORRELL
 */
public class RecommendationLineWrapTextRenderer extends JTextArea implements TableCellRenderer {

    private static final Logger logger = Logger.getLogger(RecommendationLineWrapTextRenderer.class.getPackage().getName());

    public RecommendationLineWrapTextRenderer() {
        this.setFont(GlobalUITools.FONT);
        this.setLineWrap(true);
        this.setWrapStyleWord(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        RecommendationTableItem data = (RecommendationTableItem) value;
        super.setText(data.title);
        super.setToolTipText(data.recToolTip);
        super.setSize(table.getColumnModel().getColumn(column).getWidth(), getPreferredSize().height);
        GlobalUITools.setupTableRendererUI(this, table, row, column, isSelected, true);
        return this;
    }
}
