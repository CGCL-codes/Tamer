package mr3.ui;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import mr3.jgraph.*;
import com.jgraph.event.*;

/**
 *
 * @auther takeshi morita
 */
public class SelectRDFSDialog extends SelectClassDialog {

    private Set orgRegionSet;

    private Set newRegionSet;

    private JList regionList;

    private JScrollPane regionListScroll;

    public SelectRDFSDialog(String title, GraphManager manager) {
        super(title, manager);
        newRegionSet = new HashSet();
    }

    public void setRegionSet(Set set) {
        orgRegionSet = set;
        regionList.setListData(orgRegionSet.toArray());
    }

    protected void initEachDialogAttr() {
        regionList = new JList();
        regionListScroll = new JScrollPane(regionList);
        regionListScroll.setBorder(BorderFactory.createTitledBorder("Selected List"));
        regionListScroll.setPreferredSize(new Dimension(450, 80));
        regionListScroll.setMinimumSize(new Dimension(450, 80));
    }

    protected void setEachDialogAttrLayout() {
        gridbag.setConstraints(regionListScroll, c);
        inlinePanel.add(regionListScroll);
    }

    public void valueChanged(GraphSelectionEvent e) {
        newRegionSet.removeAll(newRegionSet);
        Object[] cells = graph.getSelectionCells();
        for (int i = 0; i < cells.length; i++) {
            if (graph.isRDFSCell(cells[i])) {
                newRegionSet.add(cells[i]);
            }
        }
        if (orgRegionSet != null) {
            newRegionSet.addAll(orgRegionSet);
        }
        regionList.setListData(newRegionSet.toArray());
    }

    public Object getValue() {
        if (isOk) {
            isOk = false;
            return newRegionSet;
        } else {
            return null;
        }
    }
}
