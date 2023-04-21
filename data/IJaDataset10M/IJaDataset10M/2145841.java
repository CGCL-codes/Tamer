package org.outerj.daisy.diff.tag;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.compare.rangedifferencer.IRangeComparator;

public class TagComparator implements IAtomSplitter {

    private List<Atom> atoms = new ArrayList<Atom>(50);

    public TagComparator(String s) {
        generateAtoms(s);
    }

    public TagComparator(StringBuilder s) {
        generateAtoms(s.toString());
    }

    public TagComparator(BufferedReader in) throws IOException {
        StringBuilder sb = new StringBuilder();
        boolean allRead = false;
        while (!allRead) {
            int result = in.read();
            if (result >= 0) {
                sb.append((char) result);
            } else {
                generateAtoms(sb.toString());
                allRead = true;
            }
        }
    }

    public List<Atom> getAtoms() {
        return new ArrayList<Atom>(atoms);
    }

    private void generateAtoms(String s) {
        if (atoms.size() > 0) throw new IllegalStateException("Atoms can only be generated once");
        StringBuilder currentWord = new StringBuilder(100);
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '<' && TagAtom.isValidTag(s.substring(i, s.indexOf('>', i) + 1))) {
                if (currentWord.length() > 0) {
                    atoms.add(new TextAtom(currentWord.toString()));
                    currentWord.setLength(0);
                }
                int end = s.indexOf('>', i);
                atoms.add(new TagAtom(s.substring(i, end + 1)));
                i = end;
            } else if (DelimiterAtom.isValidDelimiter("" + c)) {
                if (currentWord.length() > 0) {
                    atoms.add(new TextAtom(currentWord.toString()));
                    currentWord.setLength(0);
                }
                atoms.add(new DelimiterAtom(c));
            } else {
                currentWord.append(c);
            }
        }
        if (currentWord.length() > 0) {
            atoms.add(new TextAtom(currentWord.toString()));
            currentWord.setLength(0);
        }
    }

    public String substring(int startAtom, int endAtom) {
        if (startAtom == endAtom) return ""; else {
            StringBuilder result = new StringBuilder();
            for (int i = startAtom; i < endAtom; i++) {
                result.append(atoms.get(i).getFullText());
            }
            return result.toString();
        }
    }

    public String substring(int startAtom) {
        return substring(startAtom, atoms.size());
    }

    public Atom getAtom(int i) {
        if (i < 0 || i >= atoms.size()) throw new IndexOutOfBoundsException("There is no Atom with index " + i);
        return atoms.get(i);
    }

    public int getRangeCount() {
        return atoms.size();
    }

    public boolean rangesEqual(int thisIndex, IRangeComparator other, int otherIndex) {
        TagComparator tc2;
        try {
            tc2 = (TagComparator) other;
        } catch (ClassCastException e) {
            return false;
        }
        return tc2.getAtom(otherIndex).equalsIdentifier(getAtom(thisIndex));
    }

    public boolean skipRangeComparison(int length, int maxLength, IRangeComparator other) {
        return false;
    }
}
