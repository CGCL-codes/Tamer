package library.swing.guicomponents;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import library.LibraryBaseClientReader;
import library.enums.Library;
import library.swing.guicomponents.panel.ReaderMatchesList;
import library.utils.DocumentDescriptionShort;

public class ReaderMatchesListDialog extends JDialog {

    public ReaderMatchesListDialog(Frame owner, Map<Library, DocumentDescriptionShort[]> matches, LibraryBaseClientReader baseClient) {
        super(owner);
        this.baseClient = baseClient;
        Set<DocumentDescriptionShort> m = new HashSet<DocumentDescriptionShort>();
        for (Library lib : matches.keySet()) {
            for (DocumentDescriptionShort dds : matches.get(lib)) {
                m.add(dds);
            }
        }
        this.matches = m.toArray(new DocumentDescriptionShort[0]);
        buildLayout();
    }

    protected void buildLayout() {
        setTitle("Reader's matches list dialog");
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setModalityType(JDialog.DEFAULT_MODALITY_TYPE.APPLICATION_MODAL);
        panel = new ReaderMatchesList() {

            {
                jButton1.addActionListener(new DocumentDetailsListener());
                jButton2.addActionListener(new BookSelectedListener());
                jButton4.addActionListener(new ExportToXMLListener());
                jButton3.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        ReaderMatchesListDialog.this.dispose();
                    }
                });
            }

            @Override
            protected DocumentDescriptionShort[] getMatches() {
                return ReaderMatchesListDialog.this.matches;
            }

            class DocumentDetailsListener implements ActionListener {

                @Override
                public void actionPerformed(ActionEvent e) {
                    int row = jTable1.getSelectedRow();
                    if (row > -1) {
                        int documentID = matches[row].getDocumentID();
                        new ReaderDocumentDetailsDialog(ReaderMatchesListDialog.this, documentID, baseClient).setVisible(true);
                    }
                }
            }

            class BookSelectedListener implements ActionListener {

                @Override
                public void actionPerformed(ActionEvent e) {
                    int[] rows = jTable1.getSelectedRows();
                    boolean success = true;
                    for (int row : rows) {
                        int documentID = matches[row].getDocumentID();
                        success = success && baseClient.book(documentID);
                    }
                    if (!success) {
                        JOptionPane.showMessageDialog(ReaderMatchesListDialog.this, "Booking failed for some documents", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(ReaderMatchesListDialog.this, "Book completed.");
                    }
                }
            }

            class ExportToXMLListener implements ActionListener {

                protected File chooseFile(final String title, final java.awt.Component parent) {
                    File result = null;
                    JFileChooser choose = new JFileChooser();
                    choose.setDialogTitle(title);
                    int choosed = choose.showOpenDialog(parent);
                    if (choosed == JFileChooser.APPROVE_OPTION) {
                        result = choose.getSelectedFile();
                    }
                    return result;
                }

                @Override
                public void actionPerformed(ActionEvent e) {
                    int[] rows = jTable1.getSelectedRows();
                    int length = rows.length;
                    Integer[] documentIDs = new Integer[length];
                    for (int i = 0; i < length; i++) {
                        documentIDs[i] = matches[rows[i]].getDocumentID();
                    }
                    String xmlDocument = baseClient.exportToXML(documentIDs);
                    try {
                        File xmlFile = chooseFile("Select XML output file", ReaderMatchesListDialog.this);
                        Writer w = new BufferedWriter(new FileWriter(xmlFile));
                        w.write(xmlDocument);
                        w.close();
                    } catch (Exception excp) {
                        JOptionPane.showMessageDialog(ReaderMatchesListDialog.this, "Saving of XML export failed.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        };
        add(panel);
        pack();
    }

    protected LibraryBaseClientReader baseClient;

    protected DocumentDescriptionShort[] matches;

    protected JPanel panel;
}
