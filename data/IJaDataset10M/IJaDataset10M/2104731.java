package br.eteg.curso.java.util;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import sun.reflect.ReflectionFactory.GetReflectionFactoryAction;

public class SwingUtil {

    public static final Font fonteGrande = new Font("Arial", Font.BOLD, 18);

    /**
	 * metodo que cria um novo painel com o layout definido.
	 */
    public static JPanel obterPainelComLayout(String layout) {
        JPanel painel1 = new JPanel();
        if (layout.equals("BoxLayout")) {
            painel1.setLayout(new BoxLayout(painel1, BoxLayout.Y_AXIS));
        } else if (layout.equals("GridLayout")) {
            painel1.setLayout(new GridLayout(6, 1));
        }
        return painel1;
    }

    public static JPanel criarPainelParaEntradaDados(String s) {
        JPanel painel = new JPanel();
        JLabel label = new JLabel(s);
        JTextField textField = new JTextField();
        textField.setFont(fonteGrande);
        painel.setLayout(new FlowLayout());
        painel.add(label);
        painel.add(textField);
        return painel;
    }
}
