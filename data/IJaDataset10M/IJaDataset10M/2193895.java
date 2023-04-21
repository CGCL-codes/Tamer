package ch.apmlViewer.plotting;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.JLabel;
import ch.apmlViewer.mainAPMLViewer.APMLmanager;
import ch.l2j.proteomics_general.MS1_feature;
import ch.mastermapframework.basicGUIs.BasicGUIFrame;
import ch.mastermapframework.basicGUIs.JavaListGUIPanel;
import ch.mastermapframework.basicGUIs.GUIhelperClasses.ListGUIItem;
import ch.mastermapframework.swingElements.MMButton;

public class FeatureListGUI implements ActionListener {

    private JavaListGUIPanel listPanel;

    private BasicGUIFrame myGUI;

    private MMButton plotFeatures;

    public static float listElementFontSize = 10.0f;

    private static String featureViewText = "Open Feature Viewer";

    public FeatureListGUI() {
    }

    private void createFeatureScrollListPanel() {
        this.listPanel = new JavaListGUIPanel(this);
        this.plotFeatures = new MMButton("Plot Features", 10, this);
        this.listPanel.setEditPanelView();
        Vector<String> tmp = new Vector<String>();
        tmp.add(FeatureListGUI.featureViewText);
        this.listPanel.setEditActionText(tmp);
        this.listPanel.addColumnCategory("   m/z  ");
        this.listPanel.addColumnCategory(" z ");
        this.listPanel.addColumnCategory("  Tr  ");
        this.listPanel.addColumnCategory(" Protein ");
        this.listPanel.addColumnCategory("    Sequence  ");
        this.listPanel.addColumnCategory(" Prob. ");
        this.listPanel.addColumnCategory("#aligned");
        Iterator I = APMLmanager.MasterMap.get_feature_iterator();
        while (I.hasNext()) {
            MS1_feature fe = (MS1_feature) I.next();
            this.listPanel.insertListElementEditable(fe.get_feature_ID(), this.getFeatureInfoVector(fe));
        }
        this.listPanel.createListPanel();
        this.listPanel.addBorderTitle("All features from the map:");
    }

    private void assembleViewPanels() {
        this.myGUI = new BasicGUIFrame();
        myGUI.add(this.listPanel.getPanel(), 1, 1);
    }

    public void createFeatureListFrame() {
        this.createFeatureScrollListPanel();
        this.assembleViewPanels();
        myGUI.showFrame();
    }

    private Vector<Object> getFeatureInfoVector(MS1_feature in) {
        Vector<Object> labels = new Vector<Object>();
        JLabel label = new JLabel(String.format("%.2f", in.get_MZ()));
        Font f = label.getFont();
        label.setFont(f.deriveFont(listElementFontSize));
        labels.add(in.get_MZ());
        label = new JLabel(String.format("%d", in.get_charge_state()));
        f = label.getFont();
        label.setFont(f.deriveFont(listElementFontSize));
        labels.add(in.get_charge_state());
        label = new JLabel(String.format("%.2f", in.get_retention_time()));
        f = label.getFont();
        label.setFont(f.deriveFont(listElementFontSize));
        labels.add(in.get_retention_time());
        double thres = -3.0;
        if (in.get_MS2_info(thres)) {
            labels.add(in.get_AC(thres));
            labels.add(in.get_MOD_SQ(thres));
            labels.add(in.get_pep_prob(thres));
        } else {
            labels.add("na");
            labels.add("na");
            labels.add("na");
        }
        label = new JLabel(String.format("%d", in.get_replicate_match_nb()));
        f = label.getFont();
        label.setFont(f.deriveFont(listElementFontSize));
        labels.add(in.get_replicate_match_nb());
        return labels;
    }

    public void actionPerformed(ActionEvent evt) {
        String comm = evt.getActionCommand();
        if (comm.contains(FeatureListGUI.featureViewText)) {
            ListGUIItem itm = (ListGUIItem) evt.getSource();
            int id = itm.getItemID();
            MS1_feature fe = APMLmanager.MasterMap.getFeatureByID(id);
            if (fe != null) {
                FeatureExplorer exp = new FeatureExplorer();
                exp.createFeatureFrame(fe);
            }
        }
        if (this.plotFeatures.checkSource(evt.getSource())) {
            Vector<MS1_feature> feV = new Vector<MS1_feature>();
            Vector<ListGUIItem> out = this.listPanel.getSelectedRowsInTable();
            Iterator I = out.iterator();
            while (I.hasNext()) {
                ListGUIItem itm = (ListGUIItem) I.next();
                int id = itm.getItemID();
                MS1_feature fe = APMLmanager.MasterMap.getFeatureByID(id);
                feV.add(fe);
            }
            if (!feV.isEmpty()) {
                MultipleFeatureProfileViewer vew = new MultipleFeatureProfileViewer(feV);
                vew.createViewer();
            }
        }
    }
}
