package panels;

import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

/**
 *
 * @author MDL
 */
public class QueryBuilder {

    private JButton jbSubmit;

    private JComboBox jcbTables;

    private String[] tabelNamen;

    private client.NetworkManager networkManager;

    private useQuery p;

    private ButtonHandler buttonHandler = new ButtonHandler();

    private String queryFull = "";

    /**
     * Panel die de queries toont en doorgeeft aan de sender
     * @param networkManager
     * @param panel
     */
    public QueryBuilder(client.NetworkManager networkManager, useQuery panel) {
        this.networkManager = networkManager;
        p = panel;
        ArrayList<ArrayList<String>> tabel = null;
        try {
            tabel = networkManager.executeQuery("SELECT [name] FROM [Queries]", false);
        } catch (Exception e) {
        }
        tabelNamen = new String[tabel.size()];
        for (int i = 0; i < tabel.size(); i++) {
            tabelNamen[i] = tabel.get(i).get(0);
        }
        jcbTables = new JComboBox(tabelNamen);
        jbSubmit = new JButton("Execute");
        jbSubmit.addActionListener(buttonHandler);
        p.add(jcbTables);
        p.add(jbSubmit);
    }

    /**
     * Een gemakkelijke functie voor het verwijderen van een component van een panel, static en beschikbaar.
     * @param pane
     * @param object
     */
    public static void removeFromPane(JPanel pane, Object object) {
        for (int i = 0; i < pane.getComponents().length; i++) {
            if (pane.getComponents()[i].equals(object)) {
                pane.remove(i);
            }
        }
    }

    /**
     * ActionListener voor de JButtons.
     */
    public class ButtonHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == jbSubmit) {
                try {
                    queryFull = networkManager.executeQuery("SELECT [query] FROM [Queries] WHERE [name] = '" + jcbTables.getSelectedItem() + "'", false).get(0).get(0);
                    p.getQuery(queryFull);
                    removeFromPane(p, jbSubmit);
                    removeFromPane(p, jcbTables);
                } catch (Exception ex) {
                }
            }
            p.validate();
            p.repaint();
        }
    }
}
