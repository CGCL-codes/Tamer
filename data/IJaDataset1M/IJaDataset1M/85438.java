package atv;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*;

class Selections {

    static int countNoSelectedSite, countSelectedName, countSelectedRegion;

    static BitSet selectedName, selectedSite;

    static int select_seq_nb;

    static int select_sit_nb;

    static String saveSpeciesGroupName() {
        return (ToolBoxPanel.selectSpecies.name);
    }

    static String saveSitesSetName() {
        return (ToolBoxPanel.selectSites.name);
    }

    static String saveToString() {
        select_seq_nb = 0;
        select_sit_nb = 0;
        Sequence.listNewSequence.clear();
        for (int i = 0; i <= Sequence.listSequence.size() - 1; i++) {
            Sequence Se = new Sequence();
            Se = (Sequence) Sequence.listSequence.get(i);
            Se.newName = "seq" + Se.numSeq + "";
            if (AlignPanel.canvas.isSelectedName.get(i) == false) Sequence.listNewSequence.addLast(Se);
        }
        Sequence S0 = (Sequence) Sequence.listSequence.get(0);
        for (int j = 0; j <= AlignCanvas.seqLength - 1; j++) if (AlignPanel.canvas.isSelectedSite.get(j) == false) {
            countNoSelectedSite++;
            select_sit_nb++;
        }
        int k = 0;
        for (int i = 0; i <= Sequence.listNewSequence.size() - 1; i++) {
            Sequence Seq = (Sequence) Sequence.listNewSequence.get(i);
            Seq.newSeq = new char[countNoSelectedSite];
            int j = 0;
            k = 0;
            while (j <= AlignCanvas.seqLength - 1) {
                if (AlignPanel.canvas.isSelectedSite.get(j) == false) {
                    Seq.newSeq[k] = Seq.seq.charAt(j);
                    k++;
                }
                j++;
            }
        }
        StringBuffer sb = new StringBuffer();
        sb.append(";;").append('\n');
        sb.append(";").append("sequences number=").append(Sequence.listNewSequence.size()).append('\n');
        select_seq_nb = Sequence.listNewSequence.size();
        for (int i = 0; i <= Sequence.listSequence.size() - 1; i++) {
            if (AlignPanel.canvas.isSelectedName.get(i) == false) {
                Sequence S = (Sequence) Sequence.listSequence.get(i);
                sb.append(";no comment").append('\n');
                sb.append(S.newName).append('\n');
                for (int l = 0; l <= k - 1; l++) sb.append(S.newSeq[l]);
                sb.append('\n');
            }
        }
        return (sb.toString());
    }
}
