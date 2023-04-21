package projeto.lp2.grupo6.view.utilidades;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * Aluno:       
 *      Guilherme Monteiro 
 *      Italo Guedes 
 *      Tiago Leite
 * 
 * @author guilhermemg
 * @author tiagoln
 * @author italogas
 * 
 * Classe utilizada para limitar um jTextField a permitir somente numeros e um limite definido de digitos.
 */
public class MascaraTextField extends PlainDocument {

    /**
	 * 
	 */
    private static final long serialVersionUID = 103077466139549114L;

    private int limite;

    public MascaraTextField(int limite) {
        super();
        this.limite = limite;
    }

    @Override
    public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
        if (str == null) {
            return;
        }
        if ((getLength() + str.length()) <= limite) {
            for (int i = 0; i < str.length(); i++) {
                if (Character.isDigit(str.charAt(i)) == false) {
                    return;
                }
            }
            super.insertString(offset, str, attr);
        }
    }
}
