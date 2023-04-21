package br.com.ita.rentacar.view;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Point;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 * Caixa de di�logo para exibi��o de informa��es sobre o programa e seus autores
 */
public class AboutDialog extends JDialog {

    /**
	 * UniqueID utilizado para serializa��o
	 */
    private static final long serialVersionUID = -4402562462140768660L;

    /**
	 * Painel para exibi��o de informa��es e imagens sobre o programa
	 */
    private JPanel programInfoPanel;

    /**
	 * Painel para exibi��o de informa��es e imagens sobre o autor jorge
	 */
    private JPanel jorgeInfoPanel;

    /**
	 * Painel para exibi��o de informa��es e imagens sobre o autor ricardo
	 */
    private JPanel ricardoInfoPanel;

    /**
	 * TextArea para exibir o texto sobre o programa
	 */
    private JTextArea programTextArea;

    /**
	 * TextArea para exibir o texto sobre o autor Jorge
	 */
    private JTextArea jorgeTextArea;

    /**
	 * TextArea para exibir o texto sobre o autor Ricardo
	 */
    private JTextArea ricardoTextArea;

    /**
	 * Construtor
	 * 
	 * @param frame
	 *            Frame principal da aplica��o
	 */
    public AboutDialog(Frame frame) {
        super(frame);
        programTextArea = new JTextArea("Rent a Car - Vers�o 1.0.3\n" + "Aplica��o para Loca��o de Ve�culos");
        programTextArea.setEditable(false);
        programTextArea.setFont(new Font("Arial", Font.BOLD, 11));
        programTextArea.setBackground(this.getBackground());
        programInfoPanel = new JPanel();
        programInfoPanel.setLayout(new FlowLayout());
        programInfoPanel.add(programTextArea);
        jorgeTextArea = new JTextArea("Jorge Augusto da Silva (jorgeaos@gmail.com)\n\n" + "Bacharel em ci�ncia da computa��o pela Universidade Anhembi-Morumbi\n" + "e atualmente cursando p�s-gradua��o em engenharia de software pelo\n" + "ITA/Stefanini. Possui as certifica��es SCJP e SCWCD, e tem experi�ncia\n" + "nas plataformas J2SE, J2ME e J2EE. Atualmente � analista-programador\n" + "java atuando com a plataforma J2EE pela Telef�nica.");
        jorgeTextArea.setEditable(false);
        jorgeTextArea.setFont(new Font("Arial", Font.BOLD, 11));
        jorgeTextArea.setBackground(this.getBackground());
        jorgeInfoPanel = new JPanel();
        jorgeInfoPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        jorgeInfoPanel.add(jorgeTextArea);
        ricardoTextArea = new JTextArea("Ricardo Okura (ricardokura@gmail.com)\n\n" + "Bacharel em ci�ncia da computa��o pela FASP e atualmente cursando p�s-\n" + "gradua��o em engenharia de software pelo ITA/Stefanini. Possui as\n" + "certifica��es SCJP e SCWCD, e tem experi�ncia nas plataformas J2SE e\n" + "J2EE. Atualmente � analista-programador java atuando com a plataforma\n" + "J2EE pela YMF Financial Architecture.");
        ricardoTextArea.setEditable(false);
        ricardoTextArea.setFont(new Font("Arial", Font.BOLD, 11));
        ricardoTextArea.setBackground(this.getBackground());
        ricardoInfoPanel = new JPanel();
        ricardoInfoPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        ricardoInfoPanel.add(ricardoTextArea);
        setLayout(new FlowLayout());
        add(programInfoPanel);
        add(jorgeInfoPanel);
        add(ricardoInfoPanel);
        setTitle("Rent a Car - Sobre");
        setLocation(new Point(400, 210));
        setSize(600, 400);
        setModal(true);
        setVisible(true);
    }
}
