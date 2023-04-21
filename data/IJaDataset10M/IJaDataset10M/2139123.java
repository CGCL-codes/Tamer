package view.tabelleMesi;

import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;
import business.internazionalizzazione.I18NManager;
import view.OggettoVistaBase;

public class PerMesiF extends OggettoVistaBase {

    private static final long serialVersionUID = 1L;

    public static void main(final String[] args) {
        final JFrame frame = new JFrame();
        frame.getContentPane().add(new PerMesiF());
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private static TabellaEntrata tabEntrate = new TabellaEntrata();

    private static TabellaUscita tabUscite = new TabellaUscita();

    private static TabellaUscitaGruppi tabUG = new TabellaUscitaGruppi();

    private JTabbedPane tabGenerale;

    public PerMesiF() {
        super();
        initGUI();
    }

    private void initGUI() {
        try {
            this.setPreferredSize(new Dimension(983, 545));
            this.setLayout(null);
            tabGenerale = new JTabbedPane();
            tabGenerale.setBounds(12, 65, 930, 468);
            tabGenerale.addTab(I18NManager.getSingleton().getMessaggio("income"), tabEntrate);
            tabGenerale.addTab(I18NManager.getSingleton().getMessaggio("withdrawal"), tabUscite);
            tabGenerale.addTab(I18NManager.getSingleton().getMessaggio("groupscharge"), tabUG);
            tabUscite.setBounds(26, 10, 400, 400);
            tabEntrate.setBounds(26, 10, 400, 400);
            this.add(tabGenerale);
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    /**
	 * @return the tabEntrate
	 */
    public static TabellaEntrata getTabEntrate() {
        return tabEntrate;
    }

    /**
	 * @param tabEntrate
	 *            the tabEntrate to set
	 */
    public static void setTabEntrate(final TabellaEntrata tabEntrate) {
        PerMesiF.tabEntrate = tabEntrate;
    }

    /**
	 * @return the tabUscite
	 */
    public static TabellaUscita getTabUscite() {
        return tabUscite;
    }

    /**
	 * @param tabUscite
	 *            the tabUscite to set
	 */
    public static void setTabUscite(final TabellaUscita tabUscite) {
        PerMesiF.tabUscite = tabUscite;
    }
}
