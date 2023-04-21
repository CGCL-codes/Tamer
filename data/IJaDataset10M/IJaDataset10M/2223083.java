package blue.patterns;

import java.util.ArrayList;
import blue.patterns.pattern.Pattern;
import blue.soundObject.NoteList;

public class PatternSet extends ArrayList {

    ArrayList patternArray;

    public PatternSet() {
        patternArray = new ArrayList();
    }

    public Pattern getPatterns(int i) {
        return (Pattern) this.get(i);
    }

    public void addPattern(Pattern pattern) {
        this.add(pattern);
        boolean[] tempPatternArray = new boolean[999];
        patternArray.add(tempPatternArray);
    }

    public void addPattern(int index, Pattern pattern) {
        this.add(index, pattern);
        boolean[] tempPatternArray = new boolean[999];
        patternArray.add(index, tempPatternArray);
    }

    public void addPattern(int index, Pattern pattern, boolean[] tempPatternArray) {
        this.add(index, pattern);
        patternArray.add(index, tempPatternArray);
    }

    public Pattern removePattern(int i) {
        patternArray.remove(i);
        return (Pattern) this.remove(i);
    }

    public Pattern removePattern(Pattern pattern) {
        int index = this.indexOf(pattern);
        if (index >= 0) {
            return removePattern(index);
        }
        return null;
    }

    public ArrayList getPatternArray() {
        return patternArray;
    }

    public static void main(String[] args) {
    }

    public NoteList generateNotes() {
        return generateNotes(1);
    }

    public NoteList generateNotes(int start) {
        NoteList tempNoteList = new NoteList();
        boolean soloFound = false;
        for (int i = 0; i < this.size(); i++) {
            if (this.getPatterns(i).isSolo()) {
                soloFound = true;
                if (!this.getPatterns(i).isMuted()) {
                    boolean[] tempPatternArray = (boolean[]) this.patternArray.get(i);
                    for (int j = start - 1; j < tempPatternArray.length; j++) {
                        int measure = j - (start - 1);
                        if (tempPatternArray[j]) {
                            NoteList tempPattern = this.getPatterns(i).generateNotes();
                            blue.utility.ScoreUtilities.setScoreStart(tempPattern, measure);
                            tempNoteList.merge(tempPattern);
                        }
                    }
                }
            }
        }
        if (soloFound) {
            return tempNoteList;
        }
        for (int i = 0; i < this.patternArray.size(); i++) {
            if (!this.getPatterns(i).isMuted()) {
                boolean[] tempPatternArray = (boolean[]) this.patternArray.get(i);
                for (int j = start - 1; j < tempPatternArray.length; j++) {
                    int measure = j - (start - 1);
                    if (tempPatternArray[j]) {
                        NoteList tempPattern = this.getPatterns(i).generateNotes();
                        blue.utility.ScoreUtilities.setScoreStart(tempPattern, measure);
                        tempNoteList.merge(tempPattern);
                    }
                }
            }
        }
        return tempNoteList;
    }
}
