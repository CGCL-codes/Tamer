package edu.udo.scaffoldhunter.model.dataexport.csv;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.openscience.cdk.Molecule;
import au.com.bytecode.opencsv.CSVWriter;
import edu.udo.scaffoldhunter.model.dataexport.ExportInterface;

/**
 * @author Bernhard Dick
 * 
 */
public class CSVExport implements ExportInterface {

    private CSVConfigurationPanel configurationPanel;

    /**
     * 
     */
    public CSVExport() {
        configurationPanel = new CSVConfigurationPanel();
    }

    @Override
    public void writeData(Iterable<Molecule> molecules, String[] propertyNames, String filename) {
        String[] props = new String[propertyNames.length + 1];
        String[] propNames = new String[propertyNames.length + 1];
        for (int i = 0; i < propertyNames.length; i++) {
            propNames[i] = propertyNames[i];
        }
        propNames[propertyNames.length] = "Title";
        try {
            CSVWriter myWriter = new CSVWriter(new FileWriter(filename), configurationPanel.getSeparator(), configurationPanel.getQuoteChar());
            myWriter.writeNext(propNames);
            for (Molecule cur : molecules) {
                for (int i = 0; i < propertyNames.length; i++) {
                    if (cur.getProperty(propertyNames[i]) != null) {
                        props[i] = cur.getProperty(propertyNames[i]).toString();
                    }
                }
                props[propertyNames.length] = cur.getID();
                myWriter.writeNext(props);
            }
            myWriter.close();
            JOptionPane.showMessageDialog(configurationPanel.getParent(), "Export successful", "Export", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public JPanel getConfigurationPanel() {
        return configurationPanel;
    }

    private static class CSVConfigurationPanel extends JPanel {

        private JLabel separatorLabel;

        private JLabel quotecharLabel;

        private JComboBox separatorComboBox;

        private JComboBox quotecharComboBox;

        /**
         */
        public CSVConfigurationPanel() {
            super();
            separatorLabel = new JLabel("Cell separator:");
            separatorComboBox = new JComboBox();
            separatorComboBox.addItem(",");
            separatorComboBox.addItem(";");
            separatorComboBox.addItem(":");
            separatorComboBox.addItem("{Tab}");
            separatorComboBox.addItem("{Space}");
            separatorComboBox.setEditable(true);
            separatorComboBox.setSelectedItem(";");
            quotecharLabel = new JLabel("Quotation character:");
            quotecharComboBox = new JComboBox();
            quotecharComboBox.addItem('"');
            quotecharComboBox.addItem('\'');
            quotecharComboBox.setEditable(true);
            quotecharComboBox.setSelectedItem('"');
            this.setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 0;
            gbc.gridx = 0;
            gbc.gridy = 0;
            this.add(separatorLabel, gbc);
            gbc.gridy = 1;
            this.add(quotecharLabel, gbc);
            gbc.weightx = 1;
            gbc.gridx = 1;
            gbc.gridy = 0;
            this.add(separatorComboBox, gbc);
            gbc.gridy = 1;
            this.add(quotecharComboBox, gbc);
        }

        public char getQuoteChar() {
            return quotecharComboBox.getSelectedItem().toString().charAt(0);
        }

        public char getSeparator() {
            String tmp = separatorComboBox.getSelectedItem().toString();
            if (tmp.equals("{Tab}")) {
                return '\t';
            } else if (tmp.equals("{Space}")) {
                return ' ';
            } else {
                return tmp.charAt(0);
            }
        }
    }
}
