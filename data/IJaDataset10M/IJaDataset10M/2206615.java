package net.maizegenetics.gwas.NAMgwas;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class FilterOutput {

    public static void main(String[] args) {
        FilterOutput fout = new FilterOutput();
        System.exit(0);
    }

    public FilterOutput() {
        FilterDialog fd = new FilterDialog();
        fd.pack();
        fd.setVisible(true);
    }

    class FilterDialog extends JDialog implements ActionListener {

        JTextField txtInputFile;

        JTextField txtOutputFile;

        public FilterDialog() {
            init();
            pack();
        }

        private void init() {
            this.setModal(true);
            this.setSize(new Dimension(400, 400));
            this.setLocation(200, 200);
            txtInputFile = new JTextField(30);
            JLabel lblInput = new JLabel("Input File");
            txtOutputFile = new JTextField(30);
            JLabel lblOutput = new JLabel("OutputFile");
            JTextField txtMinPosition = new JTextField(30);
            JLabel lblMinPosition = new JLabel("Min Position");
            JTextField txtMaxPosition = new JTextField(30);
            JLabel lblMaxPosition = new JLabel("Max Position");
            JTextField txtMinLogp = new JTextField(30);
            JLabel lblMinLogp = new JLabel("Min LogP");
            JButton btnFilter = new JButton("Filter");
            JButton btnCancel = new JButton("Cancel");
            btnFilter.setActionCommand("filter");
            btnFilter.addActionListener(this);
            btnCancel.setActionCommand("cancel");
            btnCancel.addActionListener(this);
            JButton btnBrowseInput = new JButton("Browse");
            JButton btnBrowseOutput = new JButton("Browse");
            btnBrowseInput.setActionCommand("input");
            btnBrowseInput.addActionListener(this);
            btnBrowseOutput.setActionCommand("output");
            btnBrowseOutput.addActionListener(this);
            Container content = this.getContentPane();
            content.setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridwidth = 1;
            gbc.gridheight = 1;
            gbc.gridx = 0;
            gbc.gridy = 0;
            content.add(lblInput, gbc);
            gbc.gridx++;
            content.add(txtInputFile, gbc);
            gbc.gridx++;
            content.add(btnBrowseInput, gbc);
            gbc.gridx = 0;
            gbc.gridy++;
            content.add(lblOutput, gbc);
            gbc.gridx++;
            content.add(txtOutputFile, gbc);
            gbc.gridx++;
            content.add(btnBrowseOutput, gbc);
            gbc.gridx = 0;
            gbc.gridy++;
            content.add(lblMinPosition, gbc);
            gbc.gridx++;
            content.add(txtMinPosition, gbc);
            gbc.gridx = 0;
            gbc.gridy++;
            content.add(lblMaxPosition, gbc);
            gbc.gridx++;
            content.add(txtMaxPosition, gbc);
            gbc.gridx = 0;
            gbc.gridy++;
            content.add(lblMinLogp, gbc);
            gbc.gridx++;
            content.add(txtMinLogp, gbc);
            gbc.gridx = 0;
            gbc.gridy++;
            gbc.insets = new Insets(20, 20, 20, 20);
            content.add(btnFilter, gbc);
            gbc.gridx++;
            content.add(btnCancel, gbc);
        }

        @Override
        public void actionPerformed(ActionEvent ev) {
            if (ev.getActionCommand().equals("cancel")) {
                setVisible(false);
                return;
            } else if (ev.getActionCommand().equals("input")) {
                JFileChooser jfc = new JFileChooser();
                int result = jfc.showOpenDialog(this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File inputFile = jfc.getSelectedFile();
                    txtInputFile.setText(inputFile.getPath());
                    Rectangle r = txtInputFile.getBounds();
                    repaint(r.x, r.y, r.width, r.height);
                }
            } else if (ev.getActionCommand().equals("output")) {
                JFileChooser jfc = new JFileChooser();
                int result = jfc.showSaveDialog(this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File inputFile = jfc.getSelectedFile();
                    txtOutputFile.setText(inputFile.getPath());
                    Rectangle r = txtOutputFile.getBounds();
                    repaint(r.x, r.y, r.width, r.height);
                }
            } else if (ev.getActionCommand().equals("filter")) {
            }
        }
    }
}
