package edu.belmont.mth.visigraph.gui.dialogs;

import java.io.*;
import java.awt.*;
import java.net.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import java.awt.event.*;
import java.util.regex.*;
import javax.swing.event.*;
import edu.belmont.mth.visigraph.settings.*;
import edu.belmont.mth.visigraph.resources.*;
import edu.belmont.mth.visigraph.utilities.*;
import edu.belmont.mth.visigraph.gui.dialogs.DownloaderDialog.*;

/**
 * @author Cameron Behar
 */
public class OpenFromTheWebDialog extends JDialog implements ActionListener {

    private static JList graphsList;

    private static JButton downloadButton;

    private static List<DownloadData> value;

    private static OpenFromTheWebDialog dialog;

    public static List<DownloadData> showDialog(Component owner) {
        dialog = new OpenFromTheWebDialog(JOptionPane.getFrameForComponent(owner));
        dialog.setVisible(true);
        return value;
    }

    private OpenFromTheWebDialog(Frame owner) {
        super(owner, StringBundle.get("open_from_the_web_dialog_title"), true);
        this.setResizable(false);
        graphsList = new JList(new String[] { StringBundle.get("open_from_the_web_dialog_loading_graphs_label") }) {

            {
                this.setEnabled(false);
                this.setAutoscrolls(true);
                this.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
                this.addListSelectionListener(new ListSelectionListener() {

                    @Override
                    public void valueChanged(ListSelectionEvent event) {
                        downloadButton.setEnabled(true);
                    }
                });
                this.addMouseListener(new MouseAdapter() {

                    @Override
                    public void mousePressed(MouseEvent event) {
                        if (event.getClickCount() > 1) {
                            OpenFromTheWebDialog.this.accept();
                            OpenFromTheWebDialog.dialog.setVisible(false);
                        }
                    }
                });
            }
        };
        final JScrollPane graphsScrollPane = new JScrollPane(graphsList);
        JPanel inputPanel = new JPanel(new BorderLayout()) {

            {
                this.setBorder(BorderFactory.createEmptyBorder(14, 14, 3, 14));
                this.setPreferredSize(new Dimension(350, 150));
                this.add(graphsScrollPane);
            }
        };
        downloadButton = new JButton(StringBundle.get("download_button_text")) {

            {
                this.setPreferredSize(new Dimension(100, 28));
                this.setActionCommand("Download");
                this.addActionListener(OpenFromTheWebDialog.this);
                this.setEnabled(false);
                OpenFromTheWebDialog.this.getRootPane().setDefaultButton(this);
            }
        };
        final JButton cancelButton = new JButton(StringBundle.get("cancel_button_text")) {

            {
                this.setPreferredSize(new Dimension(80, 28));
                this.addActionListener(OpenFromTheWebDialog.this);
            }
        };
        JPanel buttonPanel = new JPanel() {

            {
                this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
                this.setBorder(BorderFactory.createEmptyBorder(-2, 9, 9, 13));
                this.add(Box.createHorizontalGlue());
                this.add(downloadButton);
                this.add(Box.createRigidArea(new Dimension(10, 0)));
                this.add(cancelButton);
            }
        };
        Container contentPanel = this.getContentPane();
        contentPanel.setLayout(new BorderLayout(9, 9));
        contentPanel.add(inputPanel, BorderLayout.CENTER);
        contentPanel.add(buttonPanel, BorderLayout.PAGE_END);
        Dimension size = this.getPreferredSize();
        size.width += 40;
        size.height += 40;
        this.setPreferredSize(size);
        this.pack();
        this.setLocationRelativeTo(owner);
        new Thread("graphsLoader") {

            @Override
            public void run() {
                OpenFromTheWebDialog.this.loadGraphs();
            }
        }.start();
        value = null;
    }

    public void accept() {
        value = new ArrayList<DownloadData>();
        for (Object selectedValue : graphsList.getSelectedValues()) {
            String filename = ((String) selectedValue).substring(1);
            try {
                value.add(new DownloadData(String.format(GlobalSettings.applicationGraphsFileUrl, URLEncoder.encode(filename, "UTF-8").replace("+", "%20")), String.format("graphs/%s.vsg", filename), filename));
            } catch (Throwable t) {
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Download")) try {
            this.accept();
        } catch (Exception ex) {
            DebugUtilities.logException("An exception occurred while downloading files.", ex);
            JOptionPane.showMessageDialog(this, StringBundle.get("an_exception_occurred_while_downloading_files_dialog_message"), GlobalSettings.applicationName, JOptionPane.ERROR_MESSAGE);
        } finally {
            this.setCursor(Cursor.getDefaultCursor());
        }
        OpenFromTheWebDialog.dialog.setVisible(false);
    }

    public void loadGraphs() {
        try {
            URLConnection conn = new URL(GlobalSettings.applicationGraphsDirectoryUrl).openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line = null;
            Vector<String> graphs = new Vector<String>();
            while ((line = in.readLine()) != null) {
                Pattern pattern = Pattern.compile("^.*<li><a\\shref=\"([^\\r\\n]+?)\\.vsg\">([^\\r\\n]+?)\\.vsg</a></li>.*$");
                Matcher matcher = pattern.matcher(line);
                matcher.find();
                if (matcher.matches() && URLDecoder.decode(matcher.group(1), "UTF-8").equals(matcher.group(2))) graphs.add(" " + matcher.group(2));
            }
            OpenFromTheWebDialog.graphsList.setListData(graphs);
            graphsList.setEnabled(true);
            in.close();
        } catch (Throwable ex) {
            DebugUtilities.logException("An exception occurred while downloading the standard list of graphs.", ex);
        }
    }
}
