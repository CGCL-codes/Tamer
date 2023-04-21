package view;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.FlowLayout;
import javax.swing.SwingConstants;
import java.awt.event.KeyEvent;

/**
 * JPanel contenant le bouton afin de dessiner et le bouton permettant de param�trer le crayon.
 * @author Antoine Blanchet
 *
 */
public class Pencil extends JPanel {

    private static final long serialVersionUID = 1L;

    private JButton jButtonPencil = null;

    private JButton jButtonParam = null;

    /**
	 * This is the default constructor
	 */
    public Pencil() {
        super();
        initialize();
    }

    /**
	 * This method initializes this
	 * 
	 * @return void
	 */
    private void initialize() {
        this.setLayout(new FlowLayout());
        this.setPreferredSize(new Dimension(70, 42));
        this.setBounds(new Rectangle(0, 0, 70, 42));
        this.add(getJButtonPencil(), null);
        this.add(getJButtonParam(), null);
    }

    /**
	 * This method initializes jButtonPencil	
	 * 	
	 * @return javax.swing.JButton	
	 */
    public JButton getJButtonPencil() {
        if (jButtonPencil == null) {
            jButtonPencil = new JButton();
            jButtonPencil.setIcon(new ImageIcon(getClass().getResource("/Icon/pencil-gimp.png")));
            jButtonPencil.setToolTipText("Outils crayon");
            jButtonPencil.setPreferredSize(new Dimension(32, 32));
        }
        return jButtonPencil;
    }

    /**
	 * This method initializes jButtonParam	
	 * 	
	 * @return javax.swing.JButton	
	 */
    public JButton getJButtonParam() {
        if (jButtonParam == null) {
            jButtonParam = new JButton();
            jButtonParam.setIcon(new ImageIcon(getClass().getResource("/Icon/bullet_go.png")));
            jButtonParam.setMnemonic(KeyEvent.VK_UNDEFINED);
            jButtonParam.setPreferredSize(new Dimension(25, 25));
            jButtonParam.setToolTipText("Parametre du crayon");
            jButtonParam.setVerticalAlignment(SwingConstants.BOTTOM);
        }
        return jButtonParam;
    }
}
