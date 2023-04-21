package fr.lip6.sma.simulacion.simcafe;

import java.util.Iterator;
import java.util.Map;
import fr.lip6.sma.simulacion.app.map.MapTest;

/**
 * Classe pour le test de la carte de SimCaf�.
 *
 * @author Paul Guyot <paulguyot@acm.org>
 * @version $Revision: 1.10 $
 *
 * @see "aucun test d�fini."
 */
public class SimCafeMapTest extends MapTest {

    /**
	 * Constructeur par d�faut.
	 */
    public SimCafeMapTest() {
        super("etc/simcafe.xml");
    }

    /**
	 * Fonction appel�e pour initialiser les �l�ments de cette fen�tre.
	 * Appelle la fonction de MapTest pour initialiser la plupart des �l�ments
	 * et cr�e la carte.
	 */
    protected void initComponents() {
        super.initComponents();
        final SimCafeMapPanel theMapPanel = new SimCafeMapPanel(this, getConfiguration(), getLocalAgentName());
        final Map theAgents = getAgents();
        final Iterator theAgentNamesIter = theAgents.keySet().iterator();
        while (theAgentNamesIter.hasNext()) {
            final String theAgentName = (String) theAgentNamesIter.next();
            theMapPanel.addAgent(theAgentName);
        }
        addMapPanel(theMapPanel);
        pack();
        centerWindow();
    }

    /**
	 * Point d'entr�e du test.
	 *
	 * @param inArgs	arguments sur la ligne de commande
	 *					(ignor�s)
	 */
    public static void main(String[] inArgs) {
        final SimCafeMapTest theTestFrame = new SimCafeMapTest();
        theTestFrame.initComponents();
        theTestFrame.setVisible(true);
    }
}
