package documentProcessor;

import java.io.File;
import java.util.Vector;

public class WordWorkingSets {

    private int uniquesize = 0;

    @SuppressWarnings("unused")
    private int start, stop;

    private double WholeDocumentSize;

    private String wholedoc;

    private Vector<String> uniqueworkingSet = new Vector<String>();

    private Vector<String> nounworkingSet = new Vector<String>();

    public Vector<String> getNounWorkinSet() {
        return this.nounworkingSet;
    }

    public int getUniqueSize() {
        return this.uniquesize;
    }

    public void setThreadStartStop(int start, int stop) {
        this.start = start;
        this.stop = stop;
    }

    public WordWorkingSets(String wholedoc) {
        this.wholedoc = wholedoc;
        this.uniqueworkingSet.addElement("the");
        this.BuildUniqueWordWorkingSet();
        this.BuildNounWorkingSet();
        this.WholeDocumentSize = this.wholedoc.split(" ").length;
    }

    public double getDocumentSize() {
        return this.WholeDocumentSize;
    }

    public void BuildNounWorkingSet() {
        WordManagement NounList = new WordManagement(".." + File.separator + "Concordance" + File.separator + "Datafiles" + File.separator + "noun.pnz");
        for (int i = 0; i < this.uniqueworkingSet.size(); i++) {
            if (NounList.isInList(this.uniqueworkingSet.get(i))) {
                this.nounworkingSet.addElement(this.uniqueworkingSet.get(i));
            }
        }
    }

    public void BuildNounWorkingSet(int start, int end) {
        WordManagement NounList = new WordManagement(".." + File.separator + "Concordance" + File.separator + "Datafiles" + File.separator + "noun.pnz");
        for (int i = start; i < end; i++) {
            if (NounList.isInList(this.uniqueworkingSet.get(i))) {
                this.nounworkingSet.addElement(this.uniqueworkingSet.get(i));
            }
        }
    }

    private void BuildUniqueWordWorkingSet() {
        String[] activeSet = this.wholedoc.split(" ");
        for (int i = 0; i < activeSet.length; i++) {
            if (!this.uniqueworkingSet.contains(activeSet[i])) {
                this.uniqueworkingSet.addElement(activeSet[i]);
            }
        }
        this.uniquesize = this.uniqueworkingSet.size();
    }

    public String PrintNounWorkingSet() {
        return "Out of the unique " + this.uniqueworkingSet.size() + " words only " + this.nounworkingSet.size() + " are standard nouns";
    }

    public String PrintUniqueWordStats() {
        return "Out of the original " + this.WholeDocumentSize + " words only " + this.uniqueworkingSet.size() + " are unique words";
    }
}
