package com.swabunga.spell.swing.autospell;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;

/**
 * This editorkit just forwards all method calls to the original EditorKit
 * for all method but getAction where it also adds a "MarkAsMisspelled" action 
 * and getViewFactory where we return our own ViewFactory (Based on the original).
 * 
 * @author Robert Gustavsson (robert@lindesign.se)
 *
 */
public class AutoSpellEditorKit extends StyledEditorKit implements AutoSpellConstants {

    private StyledEditorKit editorKit = null;

    private JEditorPane pane = null;

    public AutoSpellEditorKit(StyledEditorKit editorKit) {
        this.editorKit = editorKit;
    }

    public StyledEditorKit getStyledEditorKit() {
        return editorKit;
    }

    public Object clone() {
        return new AutoSpellEditorKit(editorKit);
    }

    public void deinstall(JEditorPane c) {
        editorKit.deinstall(c);
        pane = null;
    }

    public Element getCharacterAttributeRun() {
        return editorKit.getCharacterAttributeRun();
    }

    public MutableAttributeSet getInputAttributes() {
        return editorKit.getInputAttributes();
    }

    public void install(JEditorPane c) {
        editorKit.install(c);
        pane = c;
    }

    public String getContentType() {
        return editorKit.getContentType();
    }

    public Action[] getActions() {
        Action[] actions = new Action[editorKit.getActions().length + 1];
        for (int i = 0; i < editorKit.getActions().length; i++) {
            actions[i] = editorKit.getActions()[i];
        }
        actions[actions.length - 1] = new SpellCheckAction();
        return actions;
    }

    public Caret createCaret() {
        return editorKit.createCaret();
    }

    public Document createDefaultDocument() {
        return editorKit.createDefaultDocument();
    }

    public ViewFactory getViewFactory() {
        return new AutoSpellViewFactory(editorKit.getViewFactory());
    }

    public void read(InputStream in, Document doc, int pos) throws IOException, BadLocationException {
        editorKit.read(in, doc, pos);
    }

    public void write(OutputStream out, Document doc, int pos, int len) throws IOException, BadLocationException {
        editorKit.write(out, doc, pos, len);
    }

    public void read(Reader in, Document doc, int pos) throws IOException, BadLocationException {
        editorKit.read(in, doc, pos);
    }

    public void write(Writer out, Document doc, int pos, int len) throws IOException, BadLocationException {
        editorKit.write(out, doc, pos, len);
    }

    private class SpellCheckAction extends AbstractAction {

        public SpellCheckAction() {
            super("Mark as misspelled");
        }

        public void actionPerformed(ActionEvent evt) {
            SimpleAttributeSet attr;
            int pos = pane.getCaretPosition();
            if (pos < 0) return;
            attr = new SimpleAttributeSet(((StyledDocument) pane.getDocument()).getCharacterElement(pos).getAttributes());
            attr.addAttribute(wordMisspelled, wordMisspelledTrue);
            ((JTextPane) pane).setCharacterAttributes(attr, false);
        }
    }
}
