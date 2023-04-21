package net.kodveus.kumanifest.gui;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import net.kodveus.gui.arabirim.AramaSonucInterface;
import net.kodveus.gui.araclar.AramaSonuc;
import net.kodveus.gui.jcombobox.JSteppedComboBox;
import net.kodveus.kumanifest.interfaces.DugmeInterface;
import net.kodveus.kumanifest.jdo.Location;
import net.kodveus.kumanifest.jdo.Office;
import net.kodveus.kumanifest.jdo.Vessel;
import net.kodveus.kumanifest.jdo.Voyage;
import net.kodveus.kumanifest.operation.LocationOperation;
import net.kodveus.kumanifest.operation.OfficeOperation;
import net.kodveus.kumanifest.operation.VesselOperation;
import net.kodveus.kumanifest.operation.VoyageOperation;
import net.kodveus.kumanifest.utility.GUIHelper;
import org.jdesktop.swingx.JXDatePicker;

public class VoyagePanel extends JPanel implements AramaSonucInterface, DugmeInterface {

    private static final long serialVersionUID = 1L;

    private JLabel jLabel = null;

    private JLabel jLabel1 = null;

    private JTextField txtVoyage = null;

    private AramaSonuc aramaSonuc = null;

    private Long id;

    private JLabel jLabel2 = null;

    private JLabel jLabel3 = null;

    private JLabel jLabel4 = null;

    private DugmePanel dugmePanel = null;

    private JSteppedComboBox cmbVessel = null;

    private JSteppedComboBox cmbOffice = null;

    private JLabel jLabel5 = null;

    private JLabel jLabel6 = null;

    private JLabel jLabel7 = null;

    private JLabel jLabel8 = null;

    private JSteppedComboBox cmbExport = null;

    private JSteppedComboBox cmbFirstLeavedPort = null;

    private JSteppedComboBox cmbLastLeavedPort = null;

    private JTextField txtNameOfCaptain = null;

    private JXDatePicker jdpArrivalDate = null;

    private JXDatePicker jdpDepartureDate = null;

    public VoyagePanel() {
        super();
        initialize();
    }

    private void initialize() {
        jLabel8 = new JLabel();
        jLabel8.setBounds(new java.awt.Rectangle(10, 170, 111, 21));
        jLabel8.setText("Name Of Captain:");
        jLabel7 = new JLabel();
        jLabel7.setBounds(new java.awt.Rectangle(10, 150, 111, 21));
        jLabel7.setText("Departure Date:");
        jLabel6 = new JLabel();
        jLabel6.setBounds(new java.awt.Rectangle(10, 130, 111, 21));
        jLabel6.setText("Arrival Date:");
        jLabel5 = new JLabel();
        jLabel5.setBounds(new java.awt.Rectangle(10, 90, 111, 21));
        jLabel5.setText("First Leaved Port:");
        jLabel4 = new JLabel();
        jLabel4.setBounds(new java.awt.Rectangle(10, 110, 111, 21));
        jLabel4.setText("Last Leaved Port:");
        jLabel3 = new JLabel();
        jLabel3.setBounds(new java.awt.Rectangle(10, 70, 111, 21));
        jLabel3.setText("Office:");
        jLabel2 = new JLabel();
        jLabel2.setBounds(new java.awt.Rectangle(10, 50, 111, 21));
        jLabel2.setText("Vessel:");
        jLabel1 = new JLabel();
        jLabel1.setBounds(new java.awt.Rectangle(10, 30, 111, 21));
        jLabel1.setText("Export:");
        jLabel = new JLabel();
        jLabel.setBounds(new java.awt.Rectangle(10, 10, 111, 21));
        jLabel.setText("Voyage:");
        this.setLayout(null);
        this.setSize(new java.awt.Dimension(401, 448));
        this.add(jLabel, null);
        this.add(jLabel1, null);
        this.add(getTxtVoyage(), null);
        this.add(getAramaSonuc(), null);
        this.add(jLabel2, null);
        this.add(jLabel3, null);
        this.add(jLabel4, null);
        this.add(getDugmePanel(), null);
        this.add(getCmbVessel(), null);
        this.add(getCmbOffice(), null);
        this.add(jLabel5, null);
        this.add(jLabel6, null);
        this.add(jLabel7, null);
        this.add(jLabel8, null);
        this.add(getCmbExport(), null);
        this.add(getCmbFirstLeavedPort(), null);
        this.add(getCmbLastLeavedPort(), null);
        this.add(getTxtNameOfCaptain(), null);
        this.add(getJdpArrivalDate(), null);
        this.add(getJdpDepartureDate(), null);
        updateRecords();
    }

    private void updateRecords() {
        aramaSonuc.listeGuncelle(VoyageOperation.getInstance().ara(new Voyage()));
    }

    private JTextField getTxtVoyage() {
        if (txtVoyage == null) {
            txtVoyage = new JTextField();
            txtVoyage.setBounds(new java.awt.Rectangle(120, 10, 271, 21));
        }
        return txtVoyage;
    }

    private AramaSonuc getAramaSonuc() {
        if (aramaSonuc == null) {
            aramaSonuc = GUIHelper.getInstance().createAramaSonuc((new Voyage()).getAliasMap(), new java.awt.Rectangle(10, 190, 381, 221), this);
        }
        return aramaSonuc;
    }

    public void setSecili(Object secili) {
        Voyage voyage = (Voyage) secili;
        id = voyage.getVoyageId();
        jdpArrivalDate.setDate(voyage.getArrivalDate());
        jdpDepartureDate.setDate(voyage.getDepartureDate());
        cmbExport.setSelectedIndex(voyage.getExport().intValue());
        cmbFirstLeavedPort.setSelectedItem(voyage.getFirstLeavedPort());
        cmbLastLeavedPort.setSelectedItem(voyage.getLastLeavedPort());
        txtNameOfCaptain.setText(voyage.getNameOfCaptain());
        cmbOffice.setSelectedItem(voyage.getOffice());
        cmbVessel.setSelectedItem(voyage.getVessel());
        txtVoyage.setText(voyage.getVoyage());
        this.updateUI();
    }

    public void save() {
        id = VoyageOperation.getInstance().create(getVoyageFromPanel());
        updateRecords();
    }

    public void delete() {
        if (id != null) {
            Voyage voyage = new Voyage();
            voyage.setVoyageId(id);
            VoyageOperation.getInstance().delete(voyage);
            updateRecords();
        }
    }

    public void update() {
        Voyage voyage = getVoyageFromPanel();
        voyage.setVoyageId(id);
        VoyageOperation.getInstance().update(voyage);
        updateRecords();
    }

    private Voyage getVoyageFromPanel() {
        Voyage voyage = new Voyage();
        voyage.setArrivalDate(jdpArrivalDate.getDate());
        voyage.setDepartureDate(jdpDepartureDate.getDate());
        voyage.setExport((long) cmbExport.getSelectedIndex());
        voyage.setFirstLeavedPort((Location) cmbFirstLeavedPort.getSelectedItem());
        voyage.setLastLeavedPort((Location) cmbLastLeavedPort.getSelectedItem());
        voyage.setNameOfCaptain(txtNameOfCaptain.getText());
        voyage.setOffice((Office) cmbOffice.getSelectedItem());
        voyage.setVessel((Vessel) cmbVessel.getSelectedItem());
        voyage.setVoyage(txtVoyage.getText());
        return voyage;
    }

    private DugmePanel getDugmePanel() {
        if (dugmePanel == null) {
            dugmePanel = new DugmePanel();
            dugmePanel.setBounds(new java.awt.Rectangle(50, 410, 311, 31));
            dugmePanel.setDugmeListener(this);
        }
        return dugmePanel;
    }

    private JSteppedComboBox getCmbVessel() {
        if (cmbVessel == null) {
            cmbVessel = new JSteppedComboBox(VesselOperation.getInstance().ara(new Vessel()).toArray());
            cmbVessel.setBounds(new java.awt.Rectangle(120, 50, 271, 21));
        }
        return cmbVessel;
    }

    private JSteppedComboBox getCmbOffice() {
        if (cmbOffice == null) {
            cmbOffice = new JSteppedComboBox(OfficeOperation.getInstance().ara(new Office()).toArray());
            cmbOffice.setBounds(new java.awt.Rectangle(120, 70, 271, 21));
        }
        return cmbOffice;
    }

    private JSteppedComboBox getCmbExport() {
        if (cmbExport == null) {
            cmbExport = new JSteppedComboBox(new Object[] { 0, 1 });
            cmbExport.setBounds(new java.awt.Rectangle(120, 30, 271, 21));
        }
        return cmbExport;
    }

    private JSteppedComboBox getCmbFirstLeavedPort() {
        if (cmbFirstLeavedPort == null) {
            cmbFirstLeavedPort = new JSteppedComboBox(LocationOperation.getInstance().getPorts().toArray());
            cmbFirstLeavedPort.setBounds(new java.awt.Rectangle(120, 90, 271, 21));
        }
        return cmbFirstLeavedPort;
    }

    private JSteppedComboBox getCmbLastLeavedPort() {
        if (cmbLastLeavedPort == null) {
            cmbLastLeavedPort = new JSteppedComboBox(LocationOperation.getInstance().getPorts().toArray());
            cmbLastLeavedPort.setBounds(new java.awt.Rectangle(120, 110, 271, 21));
        }
        return cmbLastLeavedPort;
    }

    private JTextField getTxtNameOfCaptain() {
        if (txtNameOfCaptain == null) {
            txtNameOfCaptain = new JTextField();
            txtNameOfCaptain.setBounds(new java.awt.Rectangle(120, 170, 271, 21));
        }
        return txtNameOfCaptain;
    }

    private JXDatePicker getJdpArrivalDate() {
        if (jdpArrivalDate == null) {
            jdpArrivalDate = new JXDatePicker();
            jdpArrivalDate.setBounds(new java.awt.Rectangle(120, 130, 271, 21));
        }
        return jdpArrivalDate;
    }

    private JXDatePicker getJdpDepartureDate() {
        if (jdpDepartureDate == null) {
            jdpDepartureDate = new JXDatePicker();
            jdpDepartureDate.setBounds(new java.awt.Rectangle(120, 150, 271, 21));
        }
        return jdpDepartureDate;
    }
}
