package org.chordcast.io.export;

import org.chordcast.*;
import java.io.*;
import org.chordcast.io.*;
import org.chordcast.data.*;

/**
 *
 * @author  johou36
 */
public class TablatureExport implements ExportInterface {

    public static final int CHORD_BY_LINE = 20;

    public static final int SPACE_BETWEEN_CHORDS = 2;

    public ChordSheetFileFilter getFileFilter() {
        return new ChordSheetFileFilter("tab", ChordCast.getString("chordcast.export.description.tab"));
    }

    public void export(org.chordcast.data.ChordSheet sheet, String filename) throws Exception {
        StringBuffer buffer = new StringBuffer();
        String lTitle = sheet.getTitle();
        if (lTitle != null && lTitle.length() > 0) {
            buffer.append(lTitle + "\n");
        }
        String lAuthor = sheet.getAuthor();
        if (lAuthor != null && lAuthor.length() > 0) {
            buffer.append("By " + lAuthor + "\n");
        }
        String lComments = sheet.getComments();
        if (lComments != null && lComments.length() > 0) {
            buffer.append("Comments: " + lComments + "\n");
        }
        buffer.append("\n\n");
        int stringNumber = sheet.getStringNB();
        int fretNumber = sheet.getFretNB();
        int chordIndex = 0;
        int chordInLine = 0;
        int stringOfRow = stringNumber;
        while (chordIndex < sheet.getChordCount()) {
            Chord chord = sheet.getChordAt(chordIndex).getChord();
            for (int i = fretNumber; i >= 0; i--) {
                byte element = chord.getElement(stringOfRow, i);
                if (element == Chord.BARRED || element == Chord.DOT) {
                    buffer.append("-" + (chord.getStartFret() + i) + "-");
                    break;
                } else if (element == Chord.ZERO) {
                    buffer.append("-" + chord.getStartFret() + "-");
                    break;
                } else if (element == Chord.SQUARE) {
                    if (chord.hasCapo()) {
                        buffer.append("-" + chord.getStartFret() + "-");
                    } else {
                        buffer.append("-0-");
                    }
                    break;
                } else if (element == Chord.IXE) {
                    buffer.append("---");
                    break;
                } else if (i == 0) {
                    buffer.append("---");
                }
            }
            buffer.append("---");
            chordIndex++;
            if (chordIndex == sheet.getChordCount() || (chordIndex > 0 && chordIndex % CHORD_BY_LINE == 0)) {
                if (stringOfRow > 1) {
                    buffer.append("\n");
                    stringOfRow--;
                    chordIndex = chordInLine;
                } else {
                    stringOfRow = stringNumber;
                    chordInLine = chordIndex;
                    buffer.append("\n\n");
                }
            } else {
            }
        }
        FileWriter writer = new FileWriter(filename);
        writer.write(buffer.toString());
        writer.close();
    }

    public static final void main(String[] args) {
    }
}
