package com.ggvaidya.TaxonDNA.SpeciesIdentifier;

import java.util.*;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.*;
import java.io.*;
import com.ggvaidya.TaxonDNA.Common.*;
import com.ggvaidya.TaxonDNA.DNA.*;
import com.ggvaidya.TaxonDNA.UI.*;

public class ExportBySpeciesName extends Panel implements UIExtension, ActionListener {

    private SpeciesIdentifier seqId = null;

    private TextArea text_main = new TextArea();

    private Button btn_Calculate = new Button("Export sequences with the following species names");

    private Button btn_Copy;

    public ExportBySpeciesName(SpeciesIdentifier seqId) {
        this.seqId = seqId;
        setLayout(new BorderLayout());
        Panel top = new Panel();
        RightLayout rl = new RightLayout(top);
        top.setLayout(rl);
        btn_Calculate.addActionListener(this);
        rl.add(btn_Calculate, RightLayout.NEXTLINE | RightLayout.FILL_4);
        add(top, BorderLayout.NORTH);
        add(text_main);
        text_main.setText("Enter species list here");
        Panel buttons = new Panel();
        buttons.setLayout(new FlowLayout(FlowLayout.RIGHT));
        btn_Copy = new Button("Copy to Clipboard");
        btn_Copy.addActionListener(this);
        buttons.add(btn_Copy);
        add(buttons, BorderLayout.SOUTH);
    }

    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (cmd.equals("Copy to Clipboard") || cmd.equals("Oops, try again?")) {
            try {
                Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
                StringSelection selection = new StringSelection(text_main.getText());
                clip.setContents(selection, selection);
            } catch (IllegalStateException ex) {
                btn_Copy.setLabel("Oops, try again?");
            }
            btn_Copy.setLabel("Copy to Clipboard");
        }
        if (e.getSource().equals(btn_Calculate)) {
            exportSequences();
        }
    }

    public void exportSequences() {
        SequenceList list = seqId.lockSequenceList();
        try {
            String line;
            Vector vec_species_names = new Vector();
            BufferedReader reader = new BufferedReader(new StringReader(text_main.getText()));
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.length() == 0) {
                } else {
                    vec_species_names.add(line);
                }
            }
            int[] sequence_counts = new int[vec_species_names.size()];
            SequenceList to_export = new SequenceList();
            Iterator i = list.iterator();
            while (i.hasNext()) {
                Sequence seq = (Sequence) i.next();
                Iterator i_names = vec_species_names.iterator();
                int x = 0;
                while (i_names.hasNext()) {
                    String name = (String) i_names.next();
                    if (seq.getSpeciesName().equalsIgnoreCase(name)) {
                        to_export.add(seq);
                        sequence_counts[x]++;
                        break;
                    }
                    x++;
                }
            }
            FileDialog fd = new FileDialog(seqId.getFrame(), "Where would you like me to extract " + to_export.count() + " sequences as FASTA?", FileDialog.SAVE);
            fd.setVisible(true);
            File f_output;
            if (fd.getFile() == null) {
                return;
            }
            if (fd.getDirectory() != null) f_output = new File(fd.getDirectory(), fd.getFile()); else f_output = new File(fd.getFile());
            com.ggvaidya.TaxonDNA.DNA.formats.FastaFile ff = new com.ggvaidya.TaxonDNA.DNA.formats.FastaFile();
            ff.writeFile(f_output, to_export, null);
            StringBuilder results = new StringBuilder();
            results.append("Export successful!\n\n");
            i = vec_species_names.iterator();
            int x = 0;
            while (i.hasNext()) {
                String name = (String) i.next();
                results.append("\t" + name + "\t" + sequence_counts[x] + "\tsequences exported.\n");
                x++;
            }
            text_main.setText(results.toString());
        } catch (Exception e) {
            new MessageBox(seqId.getFrame(), "Error: could not export sequences!", "There was a problem exporting sequences. The technical description is: " + e.getMessage());
        } finally {
            seqId.unlockSequenceList();
        }
    }

    public void dataChanged() {
        return;
    }

    public String getShortName() {
        return "Export by Species Name";
    }

    public String getDescription() {
        return "Allows you to export species by species name";
    }

    public boolean addCommandsToMenu(Menu commandMenu) {
        return false;
    }

    public Panel getPanel() {
        return this;
    }
}
