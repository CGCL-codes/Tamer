package org.mitre.rt.client.ui.groups.subgroup;

import java.awt.Component;
import javax.swing.JTable;
import org.apache.log4j.Logger;
import org.mitre.rt.client.ui.AbsColorTableTextRenderer;
import org.mitre.rt.client.util.MixedContent;
import org.mitre.rt.client.util.StringUtils;
import org.mitre.rt.client.xml.GroupHelper;
import org.mitre.rt.rtclient.ApplicationType;
import org.mitre.rt.rtclient.GroupType;
import org.mitre.rt.rtclient.GroupReferenceType;

/**
 *
 * @author JWINSTON
 */
public class SubGroupTextRenderer extends AbsColorTableTextRenderer {

    private static final Logger logger = Logger.getLogger(SubGroupTextRenderer.class.getPackage().getName());

    private ApplicationType application = null;

    private final GroupHelper groupHelper = new GroupHelper();

    private final MixedContent mixedContent = new MixedContent();

    public SubGroupTextRenderer(JTable table, ApplicationType app) {
        super(table);
        application = app;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        GroupReferenceType groupRef = (GroupReferenceType) value;
        if (groupRef != null) {
            GroupType group = groupHelper.getItem(application.getGroups().getGroupList(), groupRef.getStringValue());
            if (group != null && group.getDescription() != null) {
                setToolTipText(mixedContent.getMixedText(group.getDescription()));
            }
        }
        return this;
    }

    @Override
    public void setData(Object value, int row, int column) {
        GroupReferenceType groupRef = (GroupReferenceType) value;
        String text = "";
        column = table.convertColumnIndexToModel(column);
        if (groupRef != null) {
            if (column == SubGroupTableModel.TITLE) {
                GroupType group = groupHelper.getItem(application.getGroups().getGroupList(), groupRef.getStringValue());
                if (group != null) text = group.getTitle();
            } else if (column == SubGroupTableModel.ORDER) {
                text = groupRef.getOrderNum().toString();
            }
        }
        super.setText(text);
    }
}
