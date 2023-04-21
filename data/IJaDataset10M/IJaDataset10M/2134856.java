package locadora.view;

import javax.swing.JOptionPane;
import locadora.model.Jogo;

/**
 * Classe de UI - User Inferface - interface com o usuário do Item
 * @author Leonardo Rocha Dias
 * @author Marccelo Augusto Selvaggio
 * @version 9.11-09
 * @since 2009
 */
public class UIJogo {

    private static final String tituloUI = "Jogo - Teste";

    public static void main(String args[]) {
        String titulo = String.valueOf(JOptionPane.showInputDialog(null, "Informe o título:", tituloUI, JOptionPane.QUESTION_MESSAGE, null, null, "Matrix"));
        String plataforma = String.valueOf(JOptionPane.showInputDialog(null, "Informe a plataforma:", tituloUI, JOptionPane.QUESTION_MESSAGE, null, null, "NintendoWii"));
        Jogo jogo = new Jogo(titulo);
        jogo.setPlataforma(plataforma);
        JOptionPane.showMessageDialog(null, jogo.toString(), tituloUI, JOptionPane.INFORMATION_MESSAGE);
        JOptionPane.showMessageDialog(null, jogo.hashCode(), tituloUI + " - Hash", JOptionPane.INFORMATION_MESSAGE);
    }
}
