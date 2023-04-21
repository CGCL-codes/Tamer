package net.sf.amemailchecker.gui.messageviewer.messagedetails.messagebodyview;

import net.sf.amemailchecker.mail.model.Letter;
import net.sf.amemailchecker.mail.impl.letter.LetterImpl;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class MessageBodyPaneModel {

    private LetterImpl message;

    private Map<String, DocumentsWrapper> documents = new HashMap<String, DocumentsWrapper>();

    public void replaceInDocument(String old, String replacement) {
        Document document = getSelectedDocument();
        try {
            String text = document.getText(0, document.getLength());
            int pos = text.indexOf(old);
            if (pos != -1) {
                document.remove(pos, old.length());
                document.insertString(pos, replacement, null);
            } else {
                document.insertString(document.getLength(), replacement, null);
            }
        } catch (BadLocationException ex) {
        }
    }

    public void insertInDocument(int offset, String input) {
        Document document = getSelectedDocument();
        offset = (offset == -1) ? document.getLength() : offset;
        try {
            document.insertString(offset, input, null);
        } catch (BadLocationException e) {
        }
    }

    public Document getSelectedDocument() {
        return getDocument(getMessageDocuments().getSelectedContentType());
    }

    public void setSelectedContentType(String contentType) {
        getMessageDocuments().setSelectedContentType(contentType);
    }

    public String getSelectedContentType() {
        return getMessageDocuments().getSelectedContentType();
    }

    public void putDocuments(DocumentsWrapper wrapper) {
        documents.put(message.getUid(), wrapper);
    }

    public void putDocument(Document document, String contentType) {
        DocumentsWrapper messageDocuments = getMessageDocuments();
        messageDocuments.putDocument(contentType, document);
    }

    public Document getDocument(String contentType) {
        return getMessageDocuments().getDocument(contentType);
    }

    public DocumentsWrapper getMessageDocuments(Letter message) {
        DocumentsWrapper messageDocuments = documents.get(message.getUid());
        if (messageDocuments == null) {
            messageDocuments = new DocumentsWrapper();
            documents.put(message.getUid(), messageDocuments);
        }
        return messageDocuments;
    }

    public DocumentsWrapper getMessageDocuments() {
        return getMessageDocuments(message);
    }

    public void clearDocuments() throws BadLocationException {
        Map<String, Document> messageDocuments = getMessageDocuments().getDocuments();
        for (String key : messageDocuments.keySet()) {
            Document document = messageDocuments.get(key);
            if (document != null) document.remove(0, document.getLength());
        }
    }

    public void removeDocuments(Letter... messages) {
        for (Letter message : messages) documents.remove(message.getUid());
    }

    public void acceptUpdates() throws BadLocationException {
        Map<String, Document> messageDocuments = getMessageDocuments().getDocuments();
        for (String key : messageDocuments.keySet()) {
            Document document = messageDocuments.get(key);
            if (document != null) message.setText(key, document.getText(0, document.getLength()));
        }
    }

    public LetterImpl getMessage() {
        return message;
    }

    public void setMessage(LetterImpl message) {
        this.message = message;
    }

    public static class DocumentsWrapper {

        private Map<String, Document> documents = new HashMap<String, Document>();

        private String selectedContentType;

        private Map<String, Image> images = new HashMap<String, Image>();

        public Document getDocument(String contentType) {
            return documents.get(contentType);
        }

        public void putDocument(String contentType, Document document) {
            documents.put(contentType, document);
        }

        public Map<String, Document> getDocuments() {
            return documents;
        }

        public String getSelectedContentType() {
            return selectedContentType;
        }

        public void setSelectedContentType(String selectedContentType) {
            this.selectedContentType = selectedContentType;
        }

        public Map<String, Image> getImages() {
            return images;
        }

        public void addImage(String name, Image image) {
            images.put(name, image);
        }

        public Image getImage(String name) {
            return images.get(name);
        }
    }
}
