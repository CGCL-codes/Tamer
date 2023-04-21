package jalview;

import java.util.*;
import java.awt.*;

public class ClustalxColourScheme implements ColourScheme {

    Hashtable[] cons;

    int[][] cons2;

    ConsensusColour[] colours;

    ConsensusColour[] ResidueColour;

    int size;

    Hashtable colhash = new Hashtable();

    Consensus[] conses = new Consensus[32];

    Vector colourTable = new Vector();

    public ClustalxColourScheme() {
        this.cons2 = null;
        this.size = 0;
        makeColours();
    }

    public ClustalxColourScheme(int[][] cons2, int size) {
        this.cons2 = cons2;
        this.size = size;
        makeColours();
    }

    public void makeColours() {
        colhash.put("RED", new Color((float) 0.9, (float) 0.2, (float) 0.1));
        colhash.put("BLUE", new Color((float) 0.5, (float) 0.7, (float) 0.9));
        colhash.put("GREEN", new Color((float) 0.1, (float) 0.8, (float) 0.1));
        colhash.put("ORANGE", new Color((float) 0.9, (float) 0.6, (float) 0.3));
        colhash.put("CYAN", new Color((float) 0.1, (float) 0.7, (float) 0.7));
        colhash.put("PINK", new Color((float) 0.9, (float) 0.5, (float) 0.5));
        colhash.put("MAGENTA", new Color((float) 0.8, (float) 0.3, (float) 0.8));
        colhash.put("YELLOW", new Color((float) 0.8, (float) 0.8, (float) 0.0));
        conses[0] = new Consensus("WLVIMAFCYHP", 60);
        conses[1] = new Consensus("WLVIMAFCYHP", 80);
        conses[2] = new Consensus("ED", 50);
        conses[3] = new Consensus("KR", 60);
        conses[4] = new Consensus("G", 50);
        conses[5] = new Consensus("N", 50);
        conses[6] = new Consensus("QE", 50);
        conses[7] = new Consensus("P", 50);
        conses[8] = new Consensus("TS", 50);
        conses[26] = new Consensus("A", 85);
        conses[27] = new Consensus("C", 85);
        conses[10] = new Consensus("E", 85);
        conses[11] = new Consensus("F", 85);
        conses[12] = new Consensus("G", 85);
        conses[13] = new Consensus("H", 85);
        conses[14] = new Consensus("I", 85);
        conses[15] = new Consensus("L", 85);
        conses[16] = new Consensus("M", 85);
        conses[17] = new Consensus("N", 85);
        conses[18] = new Consensus("P", 85);
        conses[19] = new Consensus("Q", 85);
        conses[20] = new Consensus("R", 85);
        conses[21] = new Consensus("S", 85);
        conses[22] = new Consensus("T", 85);
        conses[23] = new Consensus("V", 85);
        conses[24] = new Consensus("W", 85);
        conses[25] = new Consensus("Y", 85);
        conses[28] = new Consensus("K", 85);
        conses[29] = new Consensus("D", 85);
        conses[30] = new Consensus("G", 0);
        conses[31] = new Consensus("P", 0);
        colours = new ConsensusColour[11];
        Consensus[] tmp8 = new Consensus[1];
        tmp8[0] = conses[30];
        colours[7] = new ConsensusColour((Color) colhash.get("ORANGE"), tmp8);
        Consensus[] tmp9 = new Consensus[1];
        tmp9[0] = conses[31];
        colours[8] = new ConsensusColour((Color) colhash.get("YELLOW"), tmp9);
        Consensus[] tmp10 = new Consensus[1];
        tmp10[0] = conses[27];
        colours[9] = new ConsensusColour((Color) colhash.get("PINK"), tmp8);
        Consensus[] tmp1 = new Consensus[14];
        tmp1[0] = conses[0];
        tmp1[1] = conses[1];
        tmp1[2] = conses[26];
        tmp1[3] = conses[27];
        tmp1[4] = conses[11];
        tmp1[5] = conses[13];
        tmp1[6] = conses[14];
        tmp1[7] = conses[15];
        tmp1[8] = conses[16];
        tmp1[9] = conses[23];
        tmp1[10] = conses[24];
        tmp1[11] = conses[25];
        tmp1[12] = conses[18];
        tmp1[13] = conses[19];
        colours[0] = new ConsensusColour((Color) colhash.get("BLUE"), tmp1);
        colours[10] = new ConsensusColour((Color) colhash.get("CYAN"), tmp1);
        Consensus[] tmp2 = new Consensus[5];
        tmp2[0] = conses[8];
        tmp2[1] = conses[21];
        tmp2[2] = conses[22];
        tmp2[3] = conses[0];
        tmp2[4] = conses[1];
        colours[1] = new ConsensusColour((Color) colhash.get("GREEN"), tmp2);
        Consensus[] tmp3 = new Consensus[3];
        tmp3[0] = conses[17];
        tmp3[1] = conses[29];
        tmp3[2] = conses[5];
        colours[2] = new ConsensusColour((Color) colhash.get("GREEN"), tmp3);
        Consensus[] tmp4 = new Consensus[6];
        tmp4[0] = conses[6];
        tmp4[1] = conses[19];
        tmp4[2] = conses[22];
        tmp4[3] = conses[3];
        tmp4[4] = conses[28];
        tmp4[5] = conses[20];
        colours[3] = new ConsensusColour((Color) colhash.get("GREEN"), tmp4);
        Consensus[] tmp5 = new Consensus[4];
        tmp5[0] = conses[3];
        tmp5[1] = conses[28];
        tmp5[2] = conses[20];
        tmp5[3] = conses[19];
        colours[4] = new ConsensusColour((Color) colhash.get("RED"), tmp5);
        Consensus[] tmp6 = new Consensus[5];
        tmp6[0] = conses[3];
        tmp6[1] = conses[29];
        tmp6[2] = conses[10];
        tmp6[3] = conses[6];
        tmp6[4] = conses[19];
        colours[5] = new ConsensusColour((Color) colhash.get("MAGENTA"), tmp6);
        Consensus[] tmp7 = new Consensus[5];
        tmp7[0] = conses[3];
        tmp7[1] = conses[29];
        tmp7[2] = conses[10];
        tmp7[3] = conses[17];
        tmp7[4] = conses[2];
        colours[6] = new ConsensusColour((Color) colhash.get("MAGENTA"), tmp7);
        ResidueColour = new ConsensusColour[20];
        ResidueColour[0] = colours[0];
        ResidueColour[1] = colours[4];
        ResidueColour[2] = colours[2];
        ResidueColour[3] = colours[6];
        ResidueColour[4] = colours[0];
        ResidueColour[5] = colours[3];
        ResidueColour[6] = colours[5];
        ResidueColour[7] = colours[7];
        ResidueColour[8] = colours[10];
        ResidueColour[9] = colours[0];
        ResidueColour[10] = colours[0];
        ResidueColour[11] = colours[4];
        ResidueColour[12] = colours[0];
        ResidueColour[13] = colours[0];
        ResidueColour[14] = colours[8];
        ResidueColour[15] = colours[1];
        ResidueColour[16] = colours[1];
        ResidueColour[17] = colours[0];
        ResidueColour[18] = colours[10];
        ResidueColour[19] = colours[0];
    }

    public Color findColour(DrawableSequence seq, String s, int j) {
        int i = seq.getNum(j);
        Color c = Color.white;
        for (int k = 0; k < ResidueColour[i].conses.length; k++) {
            if (ResidueColour[i].conses[k].isConserved(cons2, j, seq.getNum(j), size)) {
                c = ResidueColour[i].c;
            }
        }
        if (i == 4) {
            if (conses[27].isConserved(cons2, j, seq.getNum(j), size)) {
                c = (Color) colhash.get("PINK");
            }
        }
        return c;
    }

    public void setColours(DrawableSequence seq, int j) {
        Color c = Color.white;
        String s = seq.getSequence().substring(j, j + 1);
        try {
            c = findColour(seq, s, j);
            seq.setResidueBoxColour(j, c);
        } catch (Exception e) {
            seq.setResidueBoxColour(j, Color.white);
        }
    }

    public void setColours(DrawableSequence s) {
        for (int j = 0; j < s.getLength(); j++) {
            setColours(s, j);
        }
    }

    public void setColours(SequenceGroup sg) {
        for (int j = 0; j < sg.sequences.size(); j++) {
            DrawableSequence s = (DrawableSequence) sg.sequences.elementAt(j);
            for (int i = 0; i < s.getSequence().length(); i++) {
                setColours(s, i);
            }
        }
    }
}
