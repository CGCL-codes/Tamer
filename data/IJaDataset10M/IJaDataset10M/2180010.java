package fr.lip6.sma.simulacion.simbar3;

import java.awt.Graphics;
import java.beans.PropertyChangeEvent;
import fr.lip6.sma.simulacion.app.Bundle;
import fr.lip6.sma.simulacion.app.map.MapCell;
import fr.lip6.sma.simulacion.app.map.MapMultiCellPanel;
import fr.lip6.sma.simulacion.app.map.MapPanelListener;
import fr.lip6.sma.simulacion.simbar3.EnvironmentModel;

/**
 * Classe pour la carte de l'application SimBar3.
 * 
 * @author Paul Guyot <paulguyot@acm.org>
 * @version $Revision: 1.13 $
 *
 * @see fr.lip6.sma.simulacion.simbar3.test.SimBarMapTest
 */
public class SimBarMapPanel extends MapMultiCellPanel {

    /**
     * Game controller.
     */
    private final SimBarController mGameController;

    /**
	 * Mod�le de l'environnement.
	 */
    private final EnvironmentModel mEnvironmentModel;

    /**
	 * Constructeur � partir de l'application et d'un gestionnaire.
	 *
	 * @param inApplication			r�f�rence sur l'application.
	 * @param inController			contr�leur du jeu.
	 * @param inMapListener			client de la carte.
	 * @param inLocalAgentName		nom de l'agent local.
	 */
    public SimBarMapPanel(SimBarApplication inApplication, SimBarController inController, MapPanelListener inMapListener, String inLocalAgentName) {
        super(((SimBarModel) inApplication.getGameModel()).getEnvironmentModel(), inController, inMapListener, inLocalAgentName, ((SimBarModel) inApplication.getGameModel()).getShapesModel());
        final SimBarModel theModel = (SimBarModel) inApplication.getGameModel();
        mGameController = inController;
        mEnvironmentModel = theModel.getEnvironmentModel();
        initCells();
    }

    /**
     * Dessine le composant. Dessine la carte et les maisons des diff�rents
     * agents.
     * 
     * @param inGraphics
     *            interface pour dessiner la carte.
     */
    public void paintComponent(Graphics inGraphics) {
        inGraphics.drawImage(Bundle.getImage("artwork/map/simbar3/map-large.png"), 0, 0, this);
    }

    /**
     * Cr�ation d'une cellule pour la carte.
     *
     * @param inXCoord	coordonn�e X de la cellule.
     * @param inYCoord	coordonn�e Y de la cellule.
     * @return une nouvelle cellule pour la carte.
     */
    protected MapCell createMapCell(int inXCoord, int inYCoord) {
        return new SimBarMapCell(mGameController, mEnvironmentModel, inXCoord, inYCoord);
    }

    /**
     * Method called whenever a property change. We catch team change events
     * here.
     * 
     * @param inEvent	The event to process.
     */
    public void propertyChange(PropertyChangeEvent inEvent) {
        super.propertyChange(inEvent);
        final String theEventName = inEvent.getPropertyName();
        if (theEventName.startsWith(EnvironmentModel.PROPERTY_AGENT_TEAM_PREFIX)) {
            if (!hasFocus()) {
                if (!isRequestFocusEnabled()) {
                    setRequestFocusEnabled(true);
                }
                requestFocus();
            }
        }
    }
}
