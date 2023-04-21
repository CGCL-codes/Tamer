package com.chromamorph.score;

import java.io.File;
import java.util.ArrayList;
import java.util.TreeSet;
import com.chromamorph.Note;
import com.chromamorph.Score;
import com.chromamorph.Staff;

public class SCORE {

    private ArrayList<PGE> pges = null;

    private String name = null;

    public SCORE() {
    }

    public ArrayList<PGE> getPges() {
        return pges;
    }

    public void setPges(String pathToPGEDirectory) {
        if (!pathToPGEDirectory.endsWith("/")) pathToPGEDirectory += "/";
        setName(pathToPGEDirectory);
        try {
            pges = new ArrayList<PGE>();
            File directory = new File(pathToPGEDirectory);
            String[] dirList = directory.list();
            TreeSet<String> dirSet = new TreeSet<String>();
            for (String name : dirList) if (name.toUpperCase().endsWith(".PGE")) dirSet.add(name);
            for (String name : dirSet) {
                PGE pge = new PGE(pathToPGEDirectory + name);
                pges.add(pge);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public Score getChromamorphScore() {
        ArrayList<Code1Item> scoreNotes = new ArrayList<Code1Item>();
        TreeSet<SCOREStaff> scoreStaves = new TreeSet<SCOREStaff>();
        for (PGE pge : getPges()) {
            for (Item item : pge.getItems()) {
                if (item instanceof Code1Item) {
                    scoreNotes.add((Code1Item) item);
                } else if (item instanceof Code8Item) {
                    SCOREStaff scoreStaff = new SCOREStaff();
                    Code8Item code8Item = (Code8Item) item;
                    scoreStaff.setInstrumentNumber(code8Item.getP09InstrumentNumber());
                    SCOREStaff foundSCOREStaff = scoreStaves.floor(scoreStaff);
                    if (foundSCOREStaff != null) scoreStaff = foundSCOREStaff; else scoreStaves.add(scoreStaff);
                    String pgeFileName = pge.getFileName();
                    int staffNumber = (int) code8Item.getP02StaffNumber();
                    PGEStaffNumberPair pgeStaffNumberPair = new PGEStaffNumberPair(pgeFileName, staffNumber);
                    scoreStaff.addPGEStaffNumberPair(pgeStaffNumberPair);
                }
            }
        }
        TreeSet<Note> notes = new TreeSet<Note>();
        TreeSet<Staff> staves = new TreeSet<Staff>();
        String name = this.name;
        Score score = new Score(notes, staves, name);
        return score;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(getName());
        for (PGE pge : getPges()) sb.append("\n " + pge.getFileName());
        return sb.toString();
    }

    public static void main(String[] args) {
        SCORE score = new SCORE();
        String pathToPGEDirectory = "data/SCORE/sym3_1/";
        score.setPges(pathToPGEDirectory);
        System.out.println(score);
    }
}
