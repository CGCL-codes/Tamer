package org.openconcerto.erp.core.finance.accounting.ui;

import org.openconcerto.erp.config.ComptaPropsConfiguration;
import org.openconcerto.erp.core.common.ui.TitleLabel;
import org.openconcerto.erp.core.finance.accounting.report.Map2033A;
import org.openconcerto.erp.core.finance.accounting.report.Map2033B;
import org.openconcerto.erp.preferences.TemplateNXProps;
import org.openconcerto.sql.Configuration;
import org.openconcerto.sql.model.SQLRow;
import org.openconcerto.sql.sqlobject.ElementComboBox;
import org.openconcerto.ui.DefaultGridBagConstraints;
import org.openconcerto.ui.JDate;
import org.openconcerto.utils.Tuple2;
import java.awt.Desktop;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

public class CompteResultatBilanPanel extends JPanel {

    private JTextField textFieldResultat = new JTextField();

    private JTextField textFieldBilan = new JTextField();

    private final JDate dateDebutPeriode = new JDate();

    private final JDate dateFinPeriode = new JDate();

    private JButton buttonRemplirBilan = new JButton("Remplir la déclaration");

    private JButton buttonRemplirResultat = new JButton("Remplir la déclaration");

    public CompteResultatBilanPanel() {
        super();
        this.setLayout(new GridBagLayout());
        TitleLabel labelBilan = new TitleLabel("Compte de Bilan");
        TitleLabel labelResultat = new TitleLabel("Compte de Résultat");
        labelBilan.setOpaque(true);
        labelResultat.setOpaque(true);
        final GridBagConstraints c = new DefaultGridBagConstraints();
        this.textFieldResultat.setEditable(false);
        this.textFieldBilan.setEditable(false);
        JPanel panelPeriode = new JPanel();
        panelPeriode.add(new JLabel("Période du "));
        panelPeriode.add(this.dateDebutPeriode);
        panelPeriode.add(new JLabel(" au "));
        panelPeriode.add(this.dateFinPeriode);
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.weightx = 1;
        this.add(panelPeriode, c);
        setUpDatePeriode();
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.weightx = 1;
        c.gridy++;
        this.add(labelBilan, c);
        c.gridy++;
        this.add(new JSeparator(), c);
        c.gridy++;
        c.gridwidth = 1;
        c.weightx = 0;
        this.add(new JLabel("Génération du bilan (Cerfa DGI N°2033A)"), c);
        final JProgressBar progressBarBilan = new JProgressBar();
        c.gridy++;
        c.weightx = 1;
        this.add(progressBarBilan, c);
        c.gridx++;
        c.weightx = 0;
        c.fill = GridBagConstraints.NONE;
        this.add(this.buttonRemplirBilan, c);
        this.buttonRemplirBilan.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Map2033A map2033A = new Map2033A(progressBarBilan, CompteResultatBilanPanel.this.dateDebutPeriode.getDate(), CompteResultatBilanPanel.this.dateFinPeriode.getDate());
                map2033A.generateMap2033A();
            }
        });
        c.gridx = 0;
        c.gridy++;
        c.gridwidth = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        this.add(new JLabel("Le document se trouvera dans le dossier :"), c);
        c.gridy++;
        c.gridx = 0;
        JButton buttonOpenLocationBilan = new JButton(new ImageIcon(ElementComboBox.class.getResource("loupe.png")));
        setIconJButton(buttonOpenLocationBilan);
        buttonOpenLocationBilan.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                openFolder("Location2033APDF");
            }
        });
        {
            JPanel panelLocationBilan = new JPanel();
            panelLocationBilan.setLayout(new GridBagLayout());
            GridBagConstraints cp = new GridBagConstraints();
            cp.fill = GridBagConstraints.HORIZONTAL;
            cp.gridheight = 1;
            cp.gridwidth = 1;
            cp.gridx = 0;
            cp.gridy = 0;
            cp.anchor = GridBagConstraints.WEST;
            cp.weighty = 0;
            cp.insets = new Insets(2, 2, 1, 2);
            cp.weightx = 1;
            panelLocationBilan.add(this.textFieldBilan, cp);
            cp.weightx = 0;
            cp.gridx++;
            cp.fill = GridBagConstraints.NONE;
            panelLocationBilan.add(buttonOpenLocationBilan, cp);
            c.gridwidth = GridBagConstraints.REMAINDER;
            c.weightx = 1;
            c.fill = GridBagConstraints.HORIZONTAL;
            this.add(panelLocationBilan, c);
        }
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.gridy++;
        c.gridx = 0;
        c.gridwidth = GridBagConstraints.REMAINDER;
        this.add(new JSeparator(), c);
        c.gridy++;
        this.add(labelResultat, c);
        c.gridy++;
        this.add(new JSeparator(), c);
        c.gridwidth = 1;
        c.gridy++;
        c.weightx = 0;
        this.add(new JLabel("Génération du résultat (Cerfa DGI N°2033B)"), c);
        final JProgressBar progressBarResultat = new JProgressBar();
        c.gridy++;
        c.weightx = 1;
        this.add(progressBarResultat, c);
        c.gridx++;
        progressBarResultat.setMaximum(100);
        c.weightx = 0;
        c.fill = GridBagConstraints.NONE;
        this.add(this.buttonRemplirResultat, c);
        this.buttonRemplirResultat.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {
                        Map2033B map2033B = new Map2033B(progressBarResultat, CompteResultatBilanPanel.this.dateDebutPeriode.getDate(), CompteResultatBilanPanel.this.dateFinPeriode.getDate());
                        map2033B.generateMap();
                    }
                });
            }
        });
        c.gridx = 0;
        c.gridy++;
        c.gridwidth = GridBagConstraints.REMAINDER;
        this.add(new JLabel("Le document se trouvera dans le dossier :"), c);
        c.gridy++;
        JButton buttonOpenLocationResultat = new JButton(new ImageIcon(ElementComboBox.class.getResource("loupe.png")));
        setIconJButton(buttonOpenLocationResultat);
        buttonOpenLocationResultat.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                openFolder("Location2033BPDF");
            }
        });
        {
            JPanel panelLocationResultat = new JPanel();
            panelLocationResultat.setLayout(new GridBagLayout());
            GridBagConstraints cp = new GridBagConstraints();
            cp.fill = GridBagConstraints.HORIZONTAL;
            cp.gridheight = 1;
            cp.gridwidth = 1;
            cp.gridx = 0;
            cp.gridy = 0;
            cp.anchor = GridBagConstraints.WEST;
            cp.weighty = 0;
            cp.insets = new Insets(2, 2, 1, 2);
            cp.weightx = 1;
            panelLocationResultat.add(this.textFieldResultat, cp);
            cp.weightx = 0;
            cp.gridx++;
            cp.fill = GridBagConstraints.NONE;
            panelLocationResultat.add(buttonOpenLocationResultat, cp);
            c.gridwidth = GridBagConstraints.REMAINDER;
            c.weightx = 1;
            c.fill = GridBagConstraints.HORIZONTAL;
            this.add(panelLocationResultat, c);
        }
        JButton buttonClose = new JButton("Fermer");
        buttonClose.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                ((JFrame) SwingUtilities.getRoot(CompteResultatBilanPanel.this)).dispose();
            }
        });
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.weightx = 1;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.SOUTHEAST;
        c.gridy++;
        c.gridx = 0;
        c.weighty = 1;
        this.add(buttonClose, c);
        setTextLocation();
        this.dateDebutPeriode.addValueListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                isDateValid();
            }
        });
        this.dateFinPeriode.addValueListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                isDateValid();
            }
        });
    }

    private void setIconJButton(JButton res) {
        res.setMargin(new Insets(1, 1, 1, 1));
    }

    private void setTextLocation() {
        File f2033APDF = new File(TemplateNXProps.getInstance().getStringProperty("Location2033APDF") + "\\" + String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
        File f2033BPDF = new File(TemplateNXProps.getInstance().getStringProperty("Location2033BPDF") + "\\" + String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
        try {
            this.textFieldBilan.setText(f2033APDF.getCanonicalPath());
            this.textFieldResultat.setText(f2033BPDF.getCanonicalPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openFolder(String locationProperty) {
        String file = TemplateNXProps.getInstance().getStringProperty(locationProperty) + File.separator + String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        System.err.println(file);
        File f = new File(file);
        f.mkdirs();
        if (Desktop.isDesktopSupported()) {
            Desktop d = Desktop.getDesktop();
            if (d.isSupported(Desktop.Action.BROWSE)) {
                try {
                    d.browse(f.getCanonicalFile().toURI());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(null, "Cette action n'est pas supportée par votre système d'exploitation.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Votre système d'exploitation n'est pas supporté.");
        }
    }

    private void setUpDatePeriode() {
        new SwingWorker<Tuple2<Date, Date>, Object>() {

            @Override
            protected Tuple2<Date, Date> doInBackground() throws Exception {
                SQLRow rowSociete = ((ComptaPropsConfiguration) Configuration.getInstance()).getRowSociete();
                SQLRow rowExercice = Configuration.getInstance().getBase().getTable("EXERCICE_COMMON").getRow(rowSociete.getInt("ID_EXERCICE_COMMON"));
                Date dateFin = (Date) rowExercice.getObject("DATE_FIN");
                Date dateDeb = (Date) rowExercice.getObject("DATE_DEB");
                return new Tuple2<Date, Date>(dateDeb, dateFin);
            }

            @Override
            protected void done() {
                Tuple2<Date, Date> t;
                try {
                    t = get();
                    CompteResultatBilanPanel.this.dateDebutPeriode.setValue(t.get0());
                    CompteResultatBilanPanel.this.dateFinPeriode.setValue(t.get1());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }

    private void isDateValid() {
        System.err.println("Check date");
        Date dDeb = this.dateDebutPeriode.getDate();
        Date dEnd = this.dateFinPeriode.getDate();
        if (dDeb != null && dEnd != null) {
            boolean b = dDeb.before(dEnd);
            this.buttonRemplirBilan.setEnabled(b);
            this.buttonRemplirResultat.setEnabled(b);
        }
    }
}
