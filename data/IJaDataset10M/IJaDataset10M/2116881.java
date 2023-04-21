package vue;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

public class Interface extends JFrame {

    private static final long serialVersionUID = 2299418078282622060L;

    private Controleur m_controleur = null;

    public Interface(Controleur controleur) {
        super();
        this.m_controleur = controleur;
        build();
    }

    public void build() {
        this.setTitle("Outil d'Aide � la R�daction en Langue Etrang�re");
        this.setSize(600, 400);
        this.setLocationRelativeTo(null);
        this.setResizable(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setJMenuBar(this.m_controleur.getCMenu().getView());
        JSplitPane jsp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(this.m_controleur.getCMDL().getView()), this.m_controleur.getCA().getView());
        this.setContentPane(jsp);
    }
}
