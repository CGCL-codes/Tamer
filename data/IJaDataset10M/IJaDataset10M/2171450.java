package samples.dicom4j.toolkit.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import org.apache.log4j.BasicConfigurator;
import org.dicom4j.data.DataElements;
import org.dicom4j.data.DataSet;
import org.dicom4j.toolkit.ui.component.DicomComponent;
import org.dicom4j.toolkit.ui.component.DicomDateComponent;
import org.dicom4j.toolkit.ui.component.DicomModalityComponent;
import org.dicom4j.toolkit.ui.component.DicomSexComponent;
import org.dicom4j.toolkit.ui.component.QueryRetrieveLevelComboxBox;
import org.dolmen.swing.GUIUtils;
import org.dolmen.swing.frames.BaseFrame;

public class DicomComponentSample {

    private DicomComponent patientSexComp;

    private DicomComponent scheduledAdmissionDate;

    private DicomModalityComponent modality;

    private QueryRetrieveLevelComboxBox queryRetrieveLevelComboxBox;

    private JTextArea textarea = new JTextArea();

    public DicomComponentSample() throws Exception {
        super();
        BaseFrame frame = new BaseFrame();
        frame.setBounds(0, 0, 800, 800);
        frame.setLayout(new BorderLayout());
        JPanel top = new JPanel(new FlowLayout());
        JPanel center = new JPanel(new BorderLayout());
        center.add(new JScrollPane(textarea), BorderLayout.CENTER);
        patientSexComp = new DicomSexComponent(DataElements.newPatientSex());
        scheduledAdmissionDate = new DicomDateComponent(DataElements.newScheduledAdmissionDate());
        modality = new DicomModalityComponent(DataElements.newModality());
        queryRetrieveLevelComboxBox = new QueryRetrieveLevelComboxBox();
        top.add(patientSexComp.getComponent());
        top.add(modality.getComponent());
        top.add(scheduledAdmissionDate.getComponent());
        top.add(queryRetrieveLevelComboxBox.getComponent());
        JButton showDataSetButton = new JButton("Show Dataset");
        showDataSetButton.addActionListener(new ShowDataSetButtonActionListener());
        top.add(showDataSetButton);
        frame.add(top, BorderLayout.NORTH);
        frame.add(center, BorderLayout.CENTER);
        frame.showCentered();
    }

    private class ShowDataSetButtonActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            DataSet data = new DataSet();
            data.addElement(patientSexComp.getElement());
            data.addElement(scheduledAdmissionDate.getElement());
            data.addElement(modality.getElement());
            data.addElement(queryRetrieveLevelComboxBox.getElement());
            textarea.setText("DataSet: \n" + data.toString());
        }
    }

    public static void main(String[] args) {
        try {
            BasicConfigurator.configure();
            new DicomComponentSample();
        } catch (Exception ex) {
            GUIUtils.showException(ex);
        }
    }
}
