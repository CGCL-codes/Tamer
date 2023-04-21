package outils.saisie;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class DocumentLimitedIntegerCase extends PlainDocument {

    private static final long serialVersionUID = 1L;

    private int nbInt;

    /**Constructeur*/
    public DocumentLimitedIntegerCase(int nb) {
        super();
        nbInt = nb;
    }

    /**Méthode appelé lorsqu'on veut insérer un élément dans un JTextField*/
    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
        if (str == null) {
            return;
        }
        char[] upper = str.toCharArray();
        String tempo = "";
        for (int i = 0; i < upper.length; i++) {
            switch(upper[i]) {
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    if (super.getLength() < nbInt) {
                        tempo = tempo + upper[i];
                    }
                    break;
                default:
                    tempo = tempo + "";
            }
        }
        super.insertString(offs, tempo, a);
    }
}
