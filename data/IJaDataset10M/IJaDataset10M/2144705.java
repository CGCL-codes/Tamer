package jassEdit;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

public class ReadFile extends Thread {

    File filename;

    FileReader fr;

    BufferedReader br;

    JTextPane editor;

    String readln = new String();

    StyledDocument stDoc;

    CodeColoring check;

    Vector textStyles;

    public ReadFile(File scriptFile, JTextPane editor, CodeColoring synCheck) {
        check = synCheck;
        filename = scriptFile;
        this.editor = editor;
        stDoc = editor.getStyledDocument();
    }

    public void run() {
        try {
            fr = new FileReader(filename);
            br = new BufferedReader(fr);
            while ((readln = br.readLine()) != null) {
                try {
                    char[] spaces = readln.toCharArray();
                    for (int i = 0; i < spaces.length; i++) {
                        if (spaces[i] != ' ' && spaces[i] != '\t') {
                            break;
                        } else if (spaces[i] == ' ') {
                        } else if (spaces[i] == '\t') {
                        }
                    }
                    for (int i = 0; i < textStyles.size(); i = i + 2) {
                        stDoc.insertString(stDoc.getLength(), (String) textStyles.get(i + 1), (AttributeSet) textStyles.get(i));
                        stDoc.insertString(stDoc.getLength(), " ", null);
                    }
                    stDoc.insertString(stDoc.getLength(), "\n", null);
                    editor.repaint();
                } catch (BadLocationException ble) {
                    ble.printStackTrace();
                }
            }
            br.close();
            fr.close();
        } catch (FileNotFoundException fnfe) {
            System.out.println("Error opening file");
        } catch (IOException ie) {
            System.out.println("Error reading file");
        }
    }
}
