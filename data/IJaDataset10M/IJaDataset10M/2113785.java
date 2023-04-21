package org.openconcerto.modules.badge;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import org.openconcerto.erp.core.common.element.ComptaSQLConfElement;
import org.openconcerto.sql.element.BaseSQLComponent;
import org.openconcerto.sql.element.SQLComponent;
import org.openconcerto.ui.DefaultGridBagConstraints;
import org.openconcerto.ui.JTime;
import org.openconcerto.utils.CollectionMap;

public class PlageHoraireSQLElement extends ComptaSQLConfElement {

    public PlageHoraireSQLElement() {
        super("PLAGE_HORAIRE", "une plage horaire", "plages horaires");
    }

    @Override
    protected List<String> getListFields() {
        final List<String> l = new ArrayList<String>();
        l.add("NOM");
        return l;
    }

    @Override
    protected List<String> getComboFields() {
        final List<String> l = new ArrayList<String>();
        l.add("NOM");
        return l;
    }

    @Override
    public CollectionMap<String, String> getShowAs() {
        return CollectionMap.singleton(null, getComboFields());
    }

    @Override
    public SQLComponent createComponent() {
        return new BaseSQLComponent(this) {

            @Override
            protected void addViews() {
                this.setLayout(new GridBagLayout());
                JTabbedPane tabDay = new JTabbedPane();
                tabDay.add("Lundi", createTabPanel("LUNDI"));
                tabDay.add("Mardi", createTabPanel("MARDI"));
                tabDay.add("Mercredi", createTabPanel("MERCREDI"));
                tabDay.add("Jeudi", createTabPanel("JEUDI"));
                tabDay.add("Vendredi", createTabPanel("VENDREDI"));
                tabDay.add("Samedi", createTabPanel("SAMEDI"));
                tabDay.add("Dimanche", createTabPanel("DIMANCHE"));
                GridBagConstraints c = new DefaultGridBagConstraints();
                JTextField fieldNom = new JTextField();
                this.add(new JLabel(getLabelFor("NOM")), c);
                c.gridx++;
                c.weightx = 1;
                this.add(fieldNom, c);
                this.addView(fieldNom, "NOM", REQ);
                c.gridy++;
                c.gridx = 0;
                c.weightx = 1;
                c.weighty = 1;
                c.fill = GridBagConstraints.BOTH;
                c.gridwidth = GridBagConstraints.REMAINDER;
                this.add(tabDay, c);
            }

            private JPanel createTabPanel(String day) {
                JPanel panel = new JPanel(new GridBagLayout());
                GridBagConstraints c = new DefaultGridBagConstraints();
                {
                    c.weightx = 0;
                    panel.add(new JLabel("Début 1"), c);
                    c.gridx++;
                    c.weightx = 1;
                    final JTime textDeb1 = new JTime();
                    panel.add(textDeb1, c);
                    c.gridx++;
                    c.weightx = 0;
                    panel.add(new JLabel("Fin 1"), c);
                    c.gridx++;
                    c.weightx = 1;
                    final JTime textFin1 = new JTime();
                    panel.add(textFin1, c);
                    this.addView(textDeb1, "DEBUT_1_" + day);
                    this.addView(textFin1, "FIN_1_" + day);
                }
                {
                    c.gridy++;
                    c.gridx = 0;
                    c.weightx = 0;
                    panel.add(new JLabel("Début 2"), c);
                    c.gridx++;
                    c.weightx = 1;
                    final JTime textDeb2 = new JTime();
                    panel.add(textDeb2, c);
                    c.gridx++;
                    c.weightx = 0;
                    panel.add(new JLabel("Fin 2"), c);
                    c.gridx++;
                    c.weightx = 1;
                    final JTime textFin2 = new JTime();
                    panel.add(textFin2, c);
                    this.addView(textDeb2, "DEBUT_2_" + day);
                    this.addView(textFin2, "FIN_2_" + day);
                }
                {
                    c.gridy++;
                    c.gridx = 0;
                    c.weightx = 0;
                    panel.add(new JLabel("Début 3"), c);
                    c.gridx++;
                    c.weightx = 1;
                    final JTime textDeb3 = new JTime();
                    panel.add(textDeb3, c);
                    c.gridx++;
                    c.weightx = 0;
                    panel.add(new JLabel("Fin 3"), c);
                    c.gridx++;
                    c.weightx = 1;
                    final JTime textFin3 = new JTime();
                    panel.add(textFin3, c);
                    this.addView(textDeb3, "DEBUT_3_" + day);
                    this.addView(textFin3, "FIN_3_" + day);
                }
                panel.setOpaque(false);
                return panel;
            }
        };
    }
}
