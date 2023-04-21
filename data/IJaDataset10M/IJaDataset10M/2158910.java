package view.report;

import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import view.Alert;
import view.font.ButtonF;
import view.font.CheckBoxF;
import view.font.LabelListaGruppi;
import business.Controllore;
import business.DBUtil;
import business.ascoltatori.AscoltatoreAggiornatoreNiente;
import business.internazionalizzazione.I18NManager;

public class ReportView extends AbstractReportView {

    private static final long serialVersionUID = 1L;

    private void settaValoriReportDati(final JCheckBox chckbxSpeseVariabili_1, final JCheckBox chckbxEntrateMensCategorie, final JCheckBox chckbxSpeseMensCat, final JCheckBox chckbxEntratePerCategorie, final JCheckBox chckbxSpesePerCategorie, final JCheckBox chckbxUsciteMensili, final JCheckBox chckbxEntrateMensili, final JCheckBox chckbxUsciteAnnuali, final JCheckBox chckbxEntrateAnnuali, final JCheckBox chckbxSpeseFutili_1, final JCheckBox chckbxAvanzo, final JCheckBox chckbxMedie) {
        inserisciUsciteVariabili(chckbxSpeseVariabili_1.isSelected());
        inserisciEntrateCatMensili(chckbxEntrateMensCategorie.isSelected());
        inserisciUsciteCatMensili(chckbxSpeseMensCat.isSelected());
        inserisciEntrateCatAnnuali(chckbxEntratePerCategorie.isSelected());
        inserisciUsciteCatAnnuali(chckbxSpesePerCategorie.isSelected());
        inserisciUsciteMensili(chckbxUsciteMensili.isSelected());
        inserisciEntrateMensili(chckbxEntrateMensili.isSelected());
        inserisciUsciteAnnuali(chckbxUsciteAnnuali.isSelected());
        inserisciEntrateAnnuali(chckbxEntrateAnnuali.isSelected());
        inserisciUsciteFutili(chckbxSpeseFutili_1.isSelected());
        inserisciAvanzo(chckbxAvanzo.isSelected());
        inserisciMediaEntrate(chckbxMedie.isSelected());
        inserisciMediaUscite(chckbxMedie.isSelected());
    }

    /**
	 * Create the panel
	 * 
	 * @throws FileNotFoundException
	 */
    public ReportView() throws FileNotFoundException {
        setReportData(new ReportData());
        getContentPane().setLayout(null);
        this.setTitle("Report");
        this.setSize(250, 425);
        final JLabel Istruzioni = new LabelListaGruppi(I18NManager.getSingleton().getMessaggio("selectreport"));
        Istruzioni.setText(I18NManager.getSingleton().getMessaggio("select") + ":");
        Istruzioni.setBounds(12, 12, 207, 20);
        getContentPane().add(Istruzioni);
        final JCheckBox chckbxEntrateAnnuali = new CheckBoxF(I18NManager.getSingleton().getMessaggio("yearincome"));
        chckbxEntrateAnnuali.setBounds(22, 40, 197, 23);
        getContentPane().add(chckbxEntrateAnnuali);
        final JCheckBox chckbxUsciteAnnuali = new CheckBoxF(I18NManager.getSingleton().getMessaggio("yearoutcome"));
        chckbxUsciteAnnuali.setBounds(22, 67, 197, 23);
        getContentPane().add(chckbxUsciteAnnuali);
        final JCheckBox chckbxEntrateMensili = new CheckBoxF(I18NManager.getSingleton().getMessaggio("monthlyincome"));
        chckbxEntrateMensili.setBounds(22, 94, 197, 23);
        getContentPane().add(chckbxEntrateMensili);
        final JCheckBox chckbxUsciteMensili = new CheckBoxF(I18NManager.getSingleton().getMessaggio("monthlyoutcome"));
        chckbxUsciteMensili.setBounds(22, 121, 197, 23);
        getContentPane().add(chckbxUsciteMensili);
        final JCheckBox chckbxSpesePerCategorie = new CheckBoxF(I18NManager.getSingleton().getMessaggio("catspeseyear"));
        chckbxSpesePerCategorie.setBounds(22, 148, 197, 23);
        getContentPane().add(chckbxSpesePerCategorie);
        final JCheckBox chckbxEntratePerCategorie = new CheckBoxF(I18NManager.getSingleton().getMessaggio("catentrateyear"));
        chckbxEntratePerCategorie.setBounds(22, 175, 197, 23);
        getContentPane().add(chckbxEntratePerCategorie);
        final JCheckBox chckbxSpeseMensCat = new CheckBoxF(I18NManager.getSingleton().getMessaggio("catspesemonth"));
        chckbxSpeseMensCat.setBounds(22, 229, 197, 23);
        getContentPane().add(chckbxSpeseMensCat);
        final JCheckBox chckbxEntrateMensCategorie = new CheckBoxF(I18NManager.getSingleton().getMessaggio("catentratemonth"));
        chckbxEntrateMensCategorie.setBounds(22, 202, 197, 23);
        getContentPane().add(chckbxEntrateMensCategorie);
        final JCheckBox chckbxSpeseVariabili_1 = new CheckBoxF("% " + I18NManager.getSingleton().getMessaggio("spesevar"));
        chckbxSpeseVariabili_1.setBounds(22, 255, 197, 23);
        getContentPane().add(chckbxSpeseVariabili_1);
        final JCheckBox chckbxSpeseFutili_1 = new CheckBoxF("% " + I18NManager.getSingleton().getMessaggio("spesefut"));
        chckbxSpeseFutili_1.setBounds(22, 282, 197, 23);
        getContentPane().add(chckbxSpeseFutili_1);
        final JCheckBox chckbxMedie = new CheckBoxF(I18NManager.getSingleton().getMessaggio("annualaverages"));
        chckbxMedie.setBounds(22, 336, 197, 23);
        getContentPane().add(chckbxMedie);
        final JCheckBox chckbxAvanzo = new CheckBoxF(I18NManager.getSingleton().getMessaggio("avanzo"));
        chckbxAvanzo.setBounds(22, 309, 197, 23);
        getContentPane().add(chckbxAvanzo);
        final JButton btnGeneraReport = new ButtonF(I18NManager.getSingleton().getMessaggio("reports"));
        btnGeneraReport.setBounds(22, 366, 197, 25);
        getContentPane().add(btnGeneraReport);
        btnGeneraReport.addActionListener(new AscoltatoreAggiornatoreNiente() {

            @Override
            protected void actionPerformedOverride(ActionEvent e) {
                super.actionPerformedOverride(e);
                settaValoriReportDati(chckbxSpeseVariabili_1, chckbxEntrateMensCategorie, chckbxSpeseMensCat, chckbxEntratePerCategorie, chckbxSpesePerCategorie, chckbxUsciteMensili, chckbxEntrateMensili, chckbxUsciteAnnuali, chckbxEntrateAnnuali, chckbxSpeseFutili_1, chckbxAvanzo, chckbxMedie);
                try {
                    IScrittoreReport scrittoreReport = new ScrittoreReportTxt(reportData);
                    scrittoreReport.generaReport();
                } catch (Exception e11) {
                    e11.printStackTrace();
                }
                Alert.operazioniSegnalazioneInfo("Aggiornato Report: " + DBUtil.dataToString(new Date(), "dd/MM/yyyy HH:mm"));
            }
        });
        Controllore.getLog().info("Registrato Report: " + DBUtil.dataToString(new Date(), "dd/MM/yyyy HH:mm"));
    }
}
