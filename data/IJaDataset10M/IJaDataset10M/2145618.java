package gui;

import interfaces.ProfessoreInterface;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import application.Studente;

public class StudList extends JFrame {

    private static final long serialVersionUID = 1L;

    Container container = new JPanel(new BorderLayout());

    JList studenti;

    static DefaultListModel listModel;

    JLabel etichetta = new JLabel("Elenco studenti connessi");

    JButton gestoreControllo = new JButton("Trasferisci il controllo");

    private ProfessoreInterface prof;

    public StudList(final ProfessoreInterface prof) {
        super("Lista Studenti");
        this.prof = prof;
        listModel = new DefaultListModel();
        studenti = new JList(listModel);
        setSize(400, 600);
        studenti.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        setContentPane(container);
        container.add(etichetta, BorderLayout.NORTH);
        container.add(studenti, BorderLayout.CENTER);
        container.add(gestoreControllo, BorderLayout.SOUTH);
        gestoreControllo.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                if (gestoreControllo.getText().equals("Trasferisci il controllo")) {
                    Studente richiedente = (Studente) studenti.getSelectedValue();
                    if (richiedente == null) JOptionPane.showMessageDialog(null, "Nessuno studente selezionato. Selezionare uno studente e riprovare", "ERRORE!", JOptionPane.ERROR_MESSAGE); else {
                        prof.passaControllo(richiedente);
                        MenuBarProf.togliControllo();
                        MainWindowProf.setRichiesta("Nessuna richiesta in attesa");
                        gestoreControllo.setText("Riprendi il controllo");
                    }
                } else {
                    prof.revocaControllo();
                    MenuBarProf.prendiControllo();
                    gestoreControllo.setText("Trasferisci il controllo");
                }
            }
        });
    }

    public static void addStudente(Studente s) {
        listModel.addElement(s);
    }

    public static void removeStudente(String string) {
        for (int i = 0; i < listModel.size(); i++) {
            Studente sh = (Studente) listModel.getElementAt(i);
            if (string.equals(sh.getMatricola())) listModel.removeElementAt(i);
        }
    }
}
