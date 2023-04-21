package uk.org.sgj.YAT.Tests;

import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import uk.org.sgj.YAT.LearnVocabPaneWorker;
import uk.org.sgj.YAT.VocabPair;
import uk.org.sgj.YAT.YATFontSet;

/**
 *
 * @author  Steffen
 */
public class VocabTestPane extends javax.swing.JPanel {

    private YATFontSet testFontSet;

    private String foreign, translation, foreignCxt, transCxt, notes;

    private SimpleAttributeSet foreignStyle, translationStyle, foreignCxtStyle, transCxtStyle, notesStyle;

    private DefaultStyledDocument doc;

    StyleContext styles;

    /** Creates new form VocabTestPane */
    public VocabTestPane(YATFontSet testFontSet) {
        this.testFontSet = testFontSet;
        initComponents();
        styles = new StyleContext();
        doc = new DefaultStyledDocument(styles);
        vocabPane.setDocument(doc);
        initialiseStyles();
    }

    private void initComponents() {
        vocabScrollPane1 = new javax.swing.JScrollPane();
        vocabPane = new javax.swing.JTextPane();
        setLayout(new java.awt.BorderLayout());
        vocabPane.setEditable(false);
        vocabScrollPane1.setViewportView(vocabPane);
        add(vocabScrollPane1, java.awt.BorderLayout.CENTER);
    }

    private javax.swing.JTextPane vocabPane;

    private javax.swing.JScrollPane vocabScrollPane1;

    protected void setForeign(String foreign) {
        this.foreign = foreign;
        showOneWordInPane();
    }

    protected void setTranslation(String translation) {
        this.translation = translation;
        showOneWordInPane();
    }

    protected void setForeignCxt(String foreignCxt) {
        this.foreignCxt = foreignCxt;
        showOneWordInPane();
    }

    protected void setTransCxt(String transCxt) {
        this.transCxt = transCxt;
        showOneWordInPane();
    }

    protected void setNotes(String notes) {
        this.notes = notes;
        showOneWordInPane();
    }

    public void showWords(Vector<VocabPair> wordsVector, LearnVocabPaneWorker ourWorker) {
        try {
            doc.remove(0, doc.getLength());
            ourWorker.setNumRecords(wordsVector.size());
            int wordsShown = 0;
            Iterator<VocabPair> wordsToShow = wordsVector.iterator();
            while (wordsToShow.hasNext()) {
                VocabPair vp = wordsToShow.next();
                this.foreign = vp.getForeign();
                this.translation = vp.getTranslation();
                this.foreignCxt = vp.getForeignCxt();
                this.transCxt = vp.getTranslationCxt();
                this.notes = vp.getFullInfo();
                this.showCurrentWord();
                styleString(doc, "---------", notesStyle);
                wordsShown++;
                ourWorker.report(wordsShown);
            }
            vocabPane.setCaretPosition(0);
        } catch (BadLocationException ex) {
            Logger.getLogger(VocabTestPane.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void initialiseStyles() {
        foreignStyle = new SimpleAttributeSet();
        uk.org.sgj.SGJNifty.Style.setStyleFromFontAndColor(foreignStyle, testFontSet.getF());
        foreignCxtStyle = new SimpleAttributeSet();
        uk.org.sgj.SGJNifty.Style.setStyleFromFontAndColor(foreignCxtStyle, testFontSet.getFC());
        translationStyle = new SimpleAttributeSet();
        uk.org.sgj.SGJNifty.Style.setStyleFromFontAndColor(translationStyle, testFontSet.getT());
        transCxtStyle = new SimpleAttributeSet();
        uk.org.sgj.SGJNifty.Style.setStyleFromFontAndColor(transCxtStyle, testFontSet.getTC());
        notesStyle = new SimpleAttributeSet();
        uk.org.sgj.SGJNifty.Style.setStyleFromFontAndColor(notesStyle, testFontSet.getI());
        Style def = styles.getStyle(StyleContext.DEFAULT_STYLE);
        Style centeredVocab = styles.addStyle("centeredVocab", def);
        StyleConstants.setAlignment(centeredVocab, StyleConstants.ALIGN_CENTER);
        StyleConstants.setSpaceBelow(centeredVocab, 10);
    }

    private void showCurrentWord() throws BadLocationException {
        if ((foreign != null) && !foreign.isEmpty()) {
            styleString(doc, foreign, foreignStyle);
        }
        if ((translation != null) && !translation.isEmpty()) {
            styleString(doc, translation, translationStyle);
        }
        if ((foreignCxt != null) && !foreignCxt.isEmpty()) {
            styleString(doc, foreignCxt, foreignCxtStyle);
        }
        if ((transCxt != null) && !transCxt.isEmpty()) {
            styleString(doc, transCxt, transCxtStyle);
        }
        if ((notes != null) && !notes.isEmpty()) {
            styleString(doc, notes, notesStyle);
        }
    }

    private void showOneWordInPane() {
        try {
            doc.remove(0, doc.getLength());
            showCurrentWord();
            vocabPane.setCaretPosition(0);
        } catch (BadLocationException ex) {
            Logger.getLogger(VocabTestPane.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected YATFontSet getTestFontSet() {
        return testFontSet;
    }

    protected void setTestFontSet(YATFontSet testFontSet) {
        this.testFontSet = testFontSet;
        initialiseStyles();
    }

    private void styleString(DefaultStyledDocument doc, String thisString, SimpleAttributeSet thisStyle) throws BadLocationException {
        int startLen = doc.getLength();
        String instertedString = thisString.concat("\n");
        doc.insertString(startLen, instertedString, thisStyle);
        int lineIdx = instertedString.indexOf('\n');
        while (lineIdx != -1) {
            doc.setLogicalStyle(startLen + lineIdx, doc.getStyle("centeredVocab"));
            lineIdx++;
            if (instertedString.length() > lineIdx) {
                lineIdx = instertedString.indexOf('\n', lineIdx);
            } else {
                break;
            }
        }
    }
}
