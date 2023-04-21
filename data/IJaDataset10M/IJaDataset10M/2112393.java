package es.eucm.eadventure.editor.control.writer.domwriters.ims;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import es.eucm.eadventure.editor.data.meta.LangString;
import es.eucm.eadventure.editor.data.meta.Vocabulary;

public class IMSSimpleDataWriter {

    public static Node buildVocabularyNode(Document doc, String name, Vocabulary vocabulary) {
        Element vocElement = null;
        vocElement = doc.createElement(name);
        Node source = doc.createElement("source");
        source.appendChild(buildStringNode(doc, vocabulary.getSource()));
        vocElement.appendChild(source);
        Node value = doc.createElement("value");
        value.appendChild(buildStringNode(doc, vocabulary.getValue()));
        vocElement.appendChild(value);
        return vocElement;
    }

    public static Node buildLangStringNode(Document doc, LangString lang) {
        Element langElement = null;
        String language = new String("x-none");
        String value = new String("");
        langElement = doc.createElement("langstring");
        if (lang != null) {
            if (lang.getLanguage(0) != null) {
                language = lang.getLanguage(0);
            }
            if (lang.getValue(0) != null) {
                value = lang.getValue(0);
            }
        }
        langElement.setAttribute("xml:lang", language);
        langElement.setTextContent(value);
        return langElement;
    }

    public static Node buildLangWithoutAttrStringNode(Document doc, LangString lang) {
        Element langElement = null;
        langElement = doc.createElement("langstring");
        langElement.setTextContent(lang.getValue(0));
        return langElement;
    }

    public static Node buildStringNode(Document doc, String lang) {
        return buildLangStringNode(doc, new LangString("x-none", lang));
    }

    public static boolean isStringSet(String string) {
        return string != null && !string.equals("");
    }

    public static boolean isStringSet(LangString string) {
        return string != null && string.getLanguage(0) != null && !string.getLanguage(0).equals("") && string.getValue(0) != null && !string.getValue(0).equals("");
    }
}
