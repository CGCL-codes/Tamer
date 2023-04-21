package es.gva.cit.gazetteer.ui.showresults;

import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.gvsig.i18n.Messages;
import es.gva.cit.gazetteer.querys.Feature;

/**
 * 
 * 
 * 
 * @author Jorge Piera Llodra (piera_jor@gva.es)
 */
public class ShowResultsPanel extends JPanel {

    /**
 * <p></p>
 * 
 * 
 * 
 * @poseidon-type es.gva.cit.gazetteer.querys.Feature
 */
    public java.util.Collection features = new java.util.ArrayList();

    /**
 * 
 * 
 */
    private JPanel descriptionPanel = null;

    /**
 * 
 * 
 */
    private JPanel linksPanel = null;

    /**
 * 
 * 
 */
    private JButton mapButton = null;

    /**
 * 
 * 
 */
    private JScrollPane descriptionScroll = null;

    /**
 * 
 * 
 */
    private JLabel jLabel1 = null;

    /**
 * 
 * 
 */
    private JPanel nextLastPanel = null;

    /**
 * 
 * 
 */
    private JButton lastButton = null;

    /**
 * 
 * 
 */
    private JLabel textLabel = null;

    /**
 * 
 * 
 */
    private JButton nextButton = null;

    /**
 * 
 * 
 */
    private int numPages;

    /**
 * 
 * 
 */
    private JButton closeButton = null;

    /**
 * 
 * 
 */
    private DefaultListModel listModel = null;

    /**
 * 
 * 
 */
    private JList descriptionList = null;

    /**
 * 
 * 
 * 
 * @param features 
 * @param numPages 
 * @param recordsByPage 
 * @param translator 
 */
    public ShowResultsPanel(Feature[] features, int numPages, int recordsByPage) {
        super();
        setFeatures(features);
        this.numPages = numPages;
        initialize(recordsByPage);
    }

    /**
 * This method initializes this
 * 
 * 
 * @param recordsByPage 
 */
    private void initialize(int recordsByPage) {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setPreferredSize(new java.awt.Dimension(410, 230));
        this.add(getNextLastPanel(), null);
        this.add(getDescriptionPanel(), null);
        this.add(getLinksPanel(), null);
        actualizaLabel(1, recordsByPage);
    }

    /**
 * It loads the feature list
 * 
 * 
 * @param first 
 * @param last 
 */
    private void loadList(int first, int last) {
        if (features.size() < last) last = features.size() - 1;
        listModel.removeAllElements();
        int index = 0;
        for (int i = first; i <= last; i++) if (i < features.size()) {
            listModel.add(index, getAllFeatures()[i]);
            index++;
        }
    }

    /**
 * Actualiza el valor de la cadena de taexto que muestra los resultados
 * 
 * 
 * @param number Registro actual
 * @param recordsByPage 
 */
    public void actualizaLabel(int number, int recordsByPage) {
        textLabel.setText(Messages.getText("results") + ": " + String.valueOf(features.size()) + ". " + Messages.getText("page") + " " + String.valueOf(number) + " " + Messages.getText("of") + " " + " " + String.valueOf(this.numPages));
        loadList((number * recordsByPage) - recordsByPage, (number * recordsByPage) - 1);
    }

    /**
 * 
 * 
 * 
 * @param mapButton The mapButton to set.
 */
    public void setMapButton(JButton mapButton) {
        this.mapButton = mapButton;
    }

    /**
 * This method initializes jPanel
 * 
 * 
 * @return javax.swing.JPanel
 */
    private JPanel getDescriptionPanel() {
        if (descriptionPanel == null) {
            jLabel1 = new JLabel();
            descriptionPanel = new JPanel();
            descriptionPanel.setLayout(new FlowLayout());
            descriptionPanel.setPreferredSize(new java.awt.Dimension(400, 155));
            jLabel1.setText("");
            jLabel1.setPreferredSize(new java.awt.Dimension(40, 0));
            descriptionPanel.add(getDescriptionScroll(), null);
        }
        return descriptionPanel;
    }

    /**
 * This method initializes jPanel
 * 
 * 
 * @return javax.swing.JPanel
 */
    private JPanel getLinksPanel() {
        if (linksPanel == null) {
            linksPanel = new JPanel();
            FlowLayout flowLayout = new FlowLayout();
            flowLayout.setAlignment(FlowLayout.RIGHT);
            linksPanel = new JPanel(flowLayout);
            linksPanel.add(getMapButton(), null);
            linksPanel.add(getCloseButton(), null);
        }
        return linksPanel;
    }

    /**
 * This method initializes jButton 1
 * 
 * 
 * @return javax.swing.JButton
 */
    public JButton getMapButton() {
        if (mapButton == null) {
            mapButton = new JButton();
            mapButton.setText(Messages.getText("localize"));
            mapButton.setActionCommand("localize");
            mapButton.setPreferredSize(new Dimension(80, 23));
        }
        return mapButton;
    }

    /**
 * This method initializes jScrollPane
 * 
 * 
 * @return javax.swing.JScrollPane
 */
    public JScrollPane getDescriptionScroll() {
        if (descriptionScroll == null) {
            descriptionScroll = new JScrollPane();
            descriptionScroll.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            descriptionScroll.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            descriptionScroll.setPreferredSize(new java.awt.Dimension(400, 150));
            descriptionScroll.setViewportView(getDescriptionList());
        }
        return descriptionScroll;
    }

    /**
 * This method initializes jPanel2
 * 
 * 
 * @return javax.swing.JPanel
 */
    private JPanel getNextLastPanel() {
        if (nextLastPanel == null) {
            textLabel = new JLabel();
            nextLastPanel = new JPanel();
            textLabel.setText("JLabel");
            nextLastPanel.add(getLastButton(), null);
            nextLastPanel.add(textLabel, null);
            nextLastPanel.add(getNextButton(), null);
        }
        return nextLastPanel;
    }

    /**
 * This method initializes jButton
 * 
 * 
 * @return javax.swing.JButton
 */
    public JButton getLastButton() {
        if (lastButton == null) {
            lastButton = new JButton();
            lastButton.setText(Messages.getText("last"));
            lastButton.setActionCommand("last");
            lastButton.setEnabled(false);
            lastButton.setPreferredSize(new Dimension(80, 23));
        }
        return lastButton;
    }

    /**
 * This method initializes jButton
 * 
 * 
 * @return javax.swing.JButton
 */
    public JButton getNextButton() {
        if (nextButton == null) {
            nextButton = new JButton();
            nextButton.setText(Messages.getText("next"));
            nextButton.setActionCommand("next");
            nextButton.setPreferredSize(new Dimension(80, 23));
            if (this.numPages < 2) {
                nextButton.setEnabled(false);
            }
        }
        return nextButton;
    }

    /**
 * This method initializes jButton
 * 
 * 
 * @return javax.swing.JButton
 */
    public JButton getCloseButton() {
        if (closeButton == null) {
            closeButton = new JButton();
            closeButton.setText(Messages.getText("close"));
            closeButton.setActionCommand("close");
            closeButton.setPreferredSize(new Dimension(80, 23));
        }
        return closeButton;
    }

    /**
 * This method initializes jList
 * 
 * 
 * @return javax.swing.JList
 */
    private JList getDescriptionList() {
        if (descriptionList == null) {
            listModel = new DefaultListModel();
            descriptionList = new JList(listModel);
        }
        return descriptionList;
    }

    /**
 * 
 * 
 * 
 * @return 
 */
    public Feature getFeature() {
        if (descriptionList.getSelectedIndex() == -1) return null;
        return (Feature) listModel.getElementAt(descriptionList.getSelectedIndex());
    }

    /**
 * 
 * 
 * 
 * @return 
 */
    public Feature[] getAllFeatures() {
        Feature[] aux = new Feature[features.size()];
        for (int i = 0; i < aux.length; i++) {
            aux[i] = (Feature) features.toArray()[i];
        }
        return aux;
    }

    /**
 * 
 * 
 * 
 * @param features 
 */
    public void setFeatures(Feature[] features) {
        this.features = new java.util.ArrayList();
        for (int i = 0; i < features.length; i++) {
            this.features.add(features[i]);
        }
    }
}
