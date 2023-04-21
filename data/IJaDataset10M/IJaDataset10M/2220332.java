package gov.nih.niaid.bcbb.nexplorer3.client.widgets;

import gov.nih.niaid.bcbb.nexplorer3.client.Mediator;
import gov.nih.niaid.bcbb.nexplorer3.client.TreeData;
import gov.nih.niaid.bcbb.nexplorer3.client.UIParameterConfig;
import gov.nih.niaid.bcbb.nexplorer3.client.ViewDataClient;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class DisplayControls extends HorizontalPanel implements ClickHandler {

    private VerticalPanel column1;

    private CheckBox bootstrapCheckBox;

    private CheckBox inodeCheckBox;

    private CheckBox otuLabelsCheckBox;

    public DisplayControls(Mediator med) {
        med.registerDisplayControls(this);
        column1 = new VerticalPanel();
        bootstrapCheckBox = getCheckBox("bootstrap", "Show support values", UIParameterConfig.getInstance().isShowBootStrap());
        inodeCheckBox = getCheckBox("inode", "Show internal node labels", UIParameterConfig.getInstance().isShowInternalNodeLabel());
        otuLabelsCheckBox = getCheckBox("otuLabel", "Show OTU labels", UIParameterConfig.getInstance().isShowOTULabels());
        column1.add(bootstrapCheckBox);
        column1.add(inodeCheckBox);
        column1.add(otuLabelsCheckBox);
        column1.setWidth("180px");
        add(column1);
    }

    public CheckBox getCheckBox(String name, String label, boolean isChecked) {
        CheckBox cb = new CheckBox();
        cb.setText(label);
        cb.setName(name);
        cb.setValue(isChecked);
        cb.setStyleName("nexplorer-ParameterLabel");
        cb.addClickHandler(this);
        return cb;
    }

    public HorizontalPanel getListBox(String name, String label, Vector<HashMap<String, String>> v, String selectedValue) {
        HorizontalPanel hp = new HorizontalPanel();
        ListBox listb = new ListBox(false);
        Label la = new Label(label);
        Label sep = new Label(" : ");
        int count = 0;
        int selected = 0;
        for (Iterator<HashMap<String, String>> iter = v.iterator(); iter.hasNext(); ) {
            HashMap<String, String> tmpHm = iter.next();
            Map.Entry<String, String> e = tmpHm.entrySet().iterator().next();
            listb.addItem(e.getKey(), e.getValue());
            if (selectedValue.equalsIgnoreCase((String) e.getValue())) selected = count;
            count++;
        }
        listb.setItemSelected(selected, true);
        hp.add(la);
        hp.add(sep);
        la.setStyleName("nexplorer-ParameterLabel");
        hp.add(listb);
        hp.setStyleName("nexplorer-ParameterTable");
        return hp;
    }

    public boolean isBootstrapSelected() {
        return bootstrapCheckBox.getValue();
    }

    public boolean isINodeSelected() {
        return inodeCheckBox.getValue();
    }

    public boolean isShowOtuLabelsSelect() {
        return otuLabelsCheckBox.getValue();
    }

    public void onClick(ClickEvent clickEvent) {
        CheckBox cb = (CheckBox) clickEvent.getSource();
        if (cb.getName().equalsIgnoreCase("bootstrap")) {
            UIParameterConfig.getInstance().setShowBootStrap(cb.getValue());
        } else if (cb.getName().equalsIgnoreCase("inode")) {
            UIParameterConfig.getInstance().setShowInternalNodeLabel(cb.getValue());
        } else if (cb.getName().equalsIgnoreCase("otuLabel")) {
            UIParameterConfig.getInstance().setShowOTULabels(cb.getValue());
        }
        Mediator.getInstance().loadImages();
    }

    public void setWidgetsEnabled(boolean b) {
        bootstrapCheckBox.setEnabled(b);
        inodeCheckBox.setEnabled(b);
        otuLabelsCheckBox.setEnabled(b);
    }

    public void update(ViewDataClient vd) {
        TreeData tvd = vd.getTreeData();
        otuLabelsCheckBox.setValue(tvd.isShowOTULabels());
        inodeCheckBox.setValue(tvd.isShowInternalNodeLabel());
        bootstrapCheckBox.setValue(tvd.isShowBootStrap());
    }
}
