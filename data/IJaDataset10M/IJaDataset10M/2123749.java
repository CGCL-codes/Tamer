package sequime.io.read.ena;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponent;
import org.knime.core.node.defaultnodesettings.DialogComponentStringListSelection;
import org.knime.core.node.defaultnodesettings.DialogComponentStringSelection;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.defaultnodesettings.SettingsModelStringArray;

/**
 * <code>NodeDialog</code> for the "ENABrowser" Node.
 * Retrieve nucleotide sequences from the european nucleotide archive (ENA)
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Nikolas Fechner
 */
public class ENABrowserNodeDialog extends DefaultNodeSettingsPane {

    private SettingsModelString m_domain = new SettingsModelString(ENABrowserNodeModel.CFG_DOMAIN, "Eukaryota");

    private SettingsModelStringArray m_taxa = new SettingsModelStringArray(ENABrowserNodeModel.CFG_TAXA, new String[] { "" });

    private DialogComponentStringSelection d_class;

    private DialogComponentStringListSelection d_taxa;

    /**
     * New pane for configuring the ENABrowser node.
     */
    protected ENABrowserNodeDialog() {
        d_class = new DialogComponentStringSelection(m_domain, "Choose domain", "Eukaryota");
        addDialogComponent(d_class);
        d_taxa = new DialogComponentStringListSelection(m_taxa, "Select sequences to retrieve", getTaxaList(), true, 20);
        d_class.getModel().addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                d_taxa.replaceListItems(getTaxaList(), "");
            }
        });
        addDialogComponent(d_taxa);
    }

    private List<String> getTaxaList() {
        List<String> taxa = new Vector<String>();
        String domain = m_domain.getStringValue();
        String id = "";
        if (domain.equalsIgnoreCase("Eukaryota")) id = "eukaryota";
        try {
            URL url = new URL("http://www.ebi.ac.uk/genomes/" + id + ".details.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String link = "";
            String key = "";
            String name = "";
            int counter = 0;
            String line = "";
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] st = line.split("\t");
                ena_details ena = new ena_details(st[0], st[1], st[2], st[3], st[4]);
                ENADataHolder.instance().put(ena.desc, ena);
                taxa.add(ena.desc);
            }
            reader.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return taxa;
    }

    public static class ena_details {

        String key;

        String entry;

        String date;

        String taxid;

        String desc;

        public ena_details(String _key, String _entry, String _date, String _taxid, String _desc) {
            key = _key;
            entry = _entry;
            date = _date;
            taxid = _taxid;
            desc = _desc;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof ena_details) {
                ena_details new_name = (ena_details) obj;
                return key.equalsIgnoreCase(new_name.key);
            }
            return false;
        }

        @Override
        public int hashCode() {
            return key.hashCode();
        }

        @Override
        public String toString() {
            return key + "\t" + entry + "\t" + date + "\t" + taxid + "\t" + desc;
        }
    }
}
