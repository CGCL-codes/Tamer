package com.ggvaidya.TaxonDNA.GenBankExplorer;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.prefs.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import com.ggvaidya.TaxonDNA.Common.*;
import com.ggvaidya.TaxonDNA.DNA.*;
import com.ggvaidya.TaxonDNA.DNA.formats.*;
import com.ggvaidya.TaxonDNA.UI.*;

public class LociDisplayMode extends DisplayMode {

    public LociDisplayMode(ViewManager man) {
        super(man);
    }

    public void setGenBankFile(GenBankFile genBankFile) {
        super.setGenBankFile(genBankFile);
        if (genBankFile != null) {
            viewManager.setFileText("Current file: " + genBankFile.getFile().getAbsolutePath() + "\nNumber of loci in file: " + genBankFile.getLocusCount());
        } else {
            viewManager.setFileText("No file loaded.");
        }
    }

    public Object getRoot() {
        if (genBankFile == null) return "No file loaded";
        return "Current file (" + genBankFile.getFile().getAbsolutePath() + ")";
    }

    protected java.util.List getSubnodes(Object node) {
        if (genBankFile == null) return null;
        if (node.equals(getRoot())) {
            return genBankFile.getLoci();
        }
        int index = getIndexOfChild(getRoot(), node);
        if (index == -1) ; else {
            GenBankFile.Locus l = genBankFile.getLocus(index);
            if (l != null) {
                return l.getSections();
            }
        }
        return null;
    }

    public void pathSelected(TreePath p) {
        Object obj = p.getLastPathComponent();
        Class cls = obj.getClass();
        if (cls.equals(String.class)) {
            viewManager.setSelectionText("");
        } else if (cls.equals(GenBankFile.Locus.class)) {
            GenBankFile.Locus l = (GenBankFile.Locus) obj;
            StringBuffer buff = new StringBuffer();
            Iterator i_sec = l.getSections().iterator();
            while (i_sec.hasNext()) {
                GenBankFile.Section sec = (GenBankFile.Section) i_sec.next();
                buff.append(sec.getName() + ": " + sec.entry() + "\n");
            }
            viewManager.setSelectionText("Currently selected: locus " + l.toString() + "\n" + buff);
        } else if (GenBankFile.Section.class.isAssignableFrom(cls)) {
            GenBankFile.Section sec = (GenBankFile.Section) obj;
            String additionalText = "";
            if (GenBankFile.OriginSection.class.isAssignableFrom(cls)) {
                GenBankFile.OriginSection ori = (GenBankFile.OriginSection) obj;
                Sequence seq = null;
                try {
                    seq = ori.getSequence();
                    additionalText = "Sequence:\n" + seq.getSequenceWrapped(70);
                } catch (SequenceException e) {
                    additionalText = "Sequence: Could not be extracted.\nThe following error occured while parsing sequence: " + e.getMessage();
                }
            }
            viewManager.setSelectionText("Currently selected: section " + sec.getName() + " of locus " + sec.getLocus() + "\nValue: " + sec.entry() + "\n" + additionalText);
        }
    }
}
