package gebruikersinterface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ResourceBundle;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import domein.DomeinController;

/**
* @author Stijn Vannieuwenhuyse
*/
@SuppressWarnings("serial")
public class Menu extends JPanel {

    private ResourceBundle strings;

    private Scherm scherm;

    private JButton wijsToeKnop, registreerExtrasKnop, registreerConsumptiesKnop, nieuweRekeningKnop, nieuweKlantKnop, rekenAfKnop, sluitWerkdagKnop, andereGebruikerKnop;

    private JComboBox kiesTaal;

    public Menu(Scherm scherm) {
        this.scherm = scherm;
        setLayout(null);
        this.refresh();
    }

    @SuppressWarnings("static-access")
    public void wijsToeAction(ActionEvent event) {
        boolean stop = false;
        int aantal;
        while (!stop) {
            try {
                String input = JOptionPane.showInputDialog(this.strings.getString("aantalLabel"), 1);
                if (input == null) throw new Exception();
                aantal = Integer.parseInt(input);
                DomeinController.getInstance().wijsResourceToe(aantal);
                stop = true;
            } catch (NumberFormatException e) {
                stop = false;
            } catch (Exception e) {
                stop = true;
            }
        }
        try {
            Thread.currentThread().sleep(100);
        } catch (InterruptedException e) {
        }
        this.scherm.refresh(true);
    }

    @SuppressWarnings("static-access")
    public void geefVrijAction(ActionEvent event) {
        DomeinController.getInstance().geefResourceVrij();
        try {
            Thread.currentThread().sleep(500);
        } catch (InterruptedException e) {
        }
        this.scherm.refresh(true);
    }

    @SuppressWarnings("static-access")
    public void registreerExtrasAction(ActionEvent event) {
        this.scherm.setScherm(new Extras(this.scherm));
        scherm.refresh();
    }

    public void registreerConsumptiesAction(ActionEvent event) {
        this.scherm.setScherm(new Consumpties(this.scherm));
    }

    public void nieuweRekeningAction(ActionEvent event) {
        @SuppressWarnings("unused") NieuweRekening rekening = new NieuweRekening(scherm);
    }

    public void rekenAfAction(ActionEvent event) {
        if (DomeinController.getInstance().heeftHuidigeRekeningResource()) {
            JOptionPane.showMessageDialog(null, this.strings.getString("nogbezetLabel"), this.strings.getString("nogbezetTitel"), JOptionPane.ERROR_MESSAGE);
        } else {
            @SuppressWarnings("unused") RekenAf rekenaf = new RekenAf(scherm);
        }
    }

    public void sluitWerkdagAction(ActionEvent event) {
        if (DomeinController.getInstance().zijnResourcesVrij()) {
            @SuppressWarnings("unused") SluitWerkdag slw = new SluitWerkdag();
        } else {
            JOptionPane.showMessageDialog(null, this.strings.getString("nietAfsluitenLabel"), this.strings.getString("nietAfsluitenTitel"), JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void andereGebruikerAction(ActionEvent event) {
        if (JOptionPane.showConfirmDialog(null, this.strings.getString("afmeldenLabel"), this.strings.getString("afmeldenTitel"), JOptionPane.OK_OPTION) == 0) this.scherm.sluit();
    }

    public void kiesTaal() {
        Taal.setCurrentLocale(this.kiesTaal.getSelectedIndex() + 1);
        this.scherm.refresh();
    }

    public void refresh() {
        this.removeAll();
        this.wijsToeKnop = new JButton();
        this.wijsToeKnop.setBounds(20, 12, 150, 25);
        this.add(this.wijsToeKnop);
        this.registreerExtrasKnop = new JButton();
        this.registreerExtrasKnop.setBounds(190, 12, 150, 25);
        this.registreerExtrasKnop.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                registreerExtrasAction(event);
            }
        });
        this.add(this.registreerExtrasKnop);
        this.registreerConsumptiesKnop = new JButton();
        this.registreerConsumptiesKnop.setBounds(360, 12, 150, 25);
        this.registreerConsumptiesKnop.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                registreerConsumptiesAction(event);
            }
        });
        this.add(this.registreerConsumptiesKnop);
        this.nieuweRekeningKnop = new JButton();
        this.nieuweRekeningKnop.setBounds(530, 12, 150, 25);
        this.nieuweRekeningKnop.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                nieuweRekeningAction(event);
            }
        });
        this.add(this.nieuweRekeningKnop);
        this.nieuweKlantKnop = new JButton();
        this.nieuweKlantKnop.setBounds(20, 49, 150, 25);
        this.nieuweKlantKnop.setEnabled(false);
        this.add(this.nieuweKlantKnop);
        this.rekenAfKnop = new JButton();
        this.rekenAfKnop.setBounds(190, 49, 150, 25);
        this.rekenAfKnop.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                rekenAfAction(event);
            }
        });
        this.add(this.rekenAfKnop);
        this.sluitWerkdagKnop = new JButton();
        this.sluitWerkdagKnop.setBounds(360, 49, 150, 25);
        this.sluitWerkdagKnop.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                sluitWerkdagAction(event);
            }
        });
        this.add(this.sluitWerkdagKnop);
        if (DomeinController.getInstance().getGebruikerLevel() == 1) {
            this.sluitWerkdagKnop.setEnabled(true);
        } else {
            this.sluitWerkdagKnop.setEnabled(false);
        }
        this.andereGebruikerKnop = new JButton();
        this.andereGebruikerKnop.setBounds(530, 49, 150, 25);
        this.andereGebruikerKnop.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                andereGebruikerAction(event);
            }
        });
        this.add(this.andereGebruikerKnop);
        this.kiesTaal = new JComboBox(Taal.getTalen());
        this.kiesTaal.setSelectedIndex(Taal.getIndex());
        this.kiesTaal.setBounds(700, 12, 100, 25);
        this.kiesTaal.setMaximumRowCount(3);
        this.kiesTaal.addItemListener(new ItemListener() {

            @SuppressWarnings("static-access")
            public void itemStateChanged(ItemEvent event) {
                if (event.getStateChange() == ItemEvent.SELECTED) kiesTaal();
            }
        });
        this.add(this.kiesTaal);
        this.vulLabels();
        if (DomeinController.getInstance().isHuidigeResourceSet() && DomeinController.getInstance().isHuidigeRekeningSet()) {
            if (DomeinController.getInstance().isHuidigeResourceBezet()) {
                this.wijsToeKnop.setText(this.strings.getString("geefVrijLabel"));
                this.wijsToeKnop.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent event) {
                        geefVrijAction(event);
                    }
                });
                this.wijsToeKnop.setEnabled(true);
            } else if (!DomeinController.getInstance().isHuidigeResourceBezet() && !DomeinController.getInstance().heeftHuidigeRekeningResource()) {
                this.wijsToeKnop.setText(this.strings.getString("wijsToeLabel"));
                this.wijsToeKnop.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent event) {
                        wijsToeAction(event);
                        scherm.refresh();
                    }
                });
                this.wijsToeKnop.setEnabled(true);
            } else {
                this.wijsToeKnop.setText("");
                this.wijsToeKnop.setEnabled(false);
            }
        } else {
            this.wijsToeKnop.setText("");
            this.wijsToeKnop.setEnabled(false);
        }
        if (DomeinController.getInstance().isHuidigeRekeningSet()) {
            this.rekenAfKnop.setEnabled(true);
            this.registreerExtrasKnop.setEnabled(true);
            this.registreerConsumptiesKnop.setEnabled(true);
        } else {
            this.rekenAfKnop.setEnabled(false);
            this.registreerExtrasKnop.setEnabled(false);
            this.registreerConsumptiesKnop.setEnabled(false);
        }
    }

    public void vulLabels() {
        this.strings = Taal.getBundle("menuBundle");
        this.wijsToeKnop.setText(this.strings.getString("wijsToeLabel"));
        this.registreerExtrasKnop.setText(this.strings.getString("registreerExtrasLabel"));
        this.registreerConsumptiesKnop.setText(this.strings.getString("registreerConsumptieLabel"));
        this.nieuweRekeningKnop.setText(this.strings.getString("nieuweRekeningLabel"));
        this.nieuweKlantKnop.setText(this.strings.getString("nieuweKlantLabel"));
        this.rekenAfKnop.setText(this.strings.getString("rekenAfLabel"));
        this.sluitWerkdagKnop.setText(this.strings.getString("sluitWerkdagLabel"));
        this.andereGebruikerKnop.setText(this.strings.getString("andereGebruikerLabel"));
    }
}
