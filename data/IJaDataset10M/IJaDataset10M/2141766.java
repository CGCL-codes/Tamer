package fr.lip6.sma.simulacion.simcafe;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import fr.lip6.sma.simulacion.app.Bundle;
import fr.lip6.sma.simulacion.app.Configuration;
import fr.lip6.sma.simulacion.app.map.MapPanelListener;
import fr.lip6.sma.simulacion.app.map.MapRegionPanel;

/**
 * Classe pour le paneau qui g�re la carte dans SimCaf�.
 *
 * @author Paul Guyot <paulguyot@acm.org>
 * @version $Revision: 1.13 $
 *
 * @see fr.lip6.sma.simulacion.app.map.MapTest
 */
public class SimCafeMapPanel extends MapRegionPanel {

    /**
	 * Taille du beneficio du joueur, sur une �chelle de 0 � 1.
	 */
    private final double mLocalAgentBeneficioSize;

    /**
	 * Tableau avec les tailles des beneficios des joueurs. Les cl�s sont les
	 * noms, les valeurs sont les tailles sur une �chelle de 0 � 1 (Double)
	 */
    private final Map mAgentsBeneficioSizes;

    /**
	 * Taille maximale du beneficio.
	 */
    private final double mBeneficioMaxSize;

    /**
	 * Constructeur � partir de la configuration et d'un gestionnaire
	 * (si le joueur local n'est pas sur la carte).
	 *
	 * @param inMapListener				r�f�rence sur le gestionnaire
	 * @param inConfiguration			configuration globale
	 */
    public SimCafeMapPanel(MapPanelListener inMapListener, Configuration inConfiguration) {
        this(inMapListener, inConfiguration, null);
    }

    /**
	 * Constructeur � partir d'un gestionnaire, de la configuration et du
	 * nom du joueur local.
	 *
	 * @param inMapListener				r�f�rence sur le gestionnaire.
	 * @param inConfiguration			configuration globale.
	 * @param inLocalAgentName			nom du joueur local.
	 */
    public SimCafeMapPanel(MapPanelListener inMapListener, Configuration inConfiguration, String inLocalAgentName) {
        super(inMapListener, inConfiguration, inLocalAgentName);
        mAgentsBeneficioSizes = new Hashtable();
        mBeneficioMaxSize = Double.parseDouble(inConfiguration.getProperty("beneficioMaxSize"));
        if (inLocalAgentName == null) {
            mLocalAgentBeneficioSize = 0;
        } else {
            mLocalAgentBeneficioSize = Double.parseDouble(getConfiguration().getAgentProperty(inLocalAgentName, "beneficio")) / mBeneficioMaxSize;
        }
        setSize(468, 295);
    }

    /**
	 * Dessine le composant.
	 * Dessine la carte et les maisons des diff�rents agents.
	 *
	 * @param inGraphics	interface pour dessiner la carte.
	 */
    public void paintComponent(Graphics inGraphics) {
        inGraphics.drawImage(Bundle.getImage("artwork/map/simcafe/map.png"), 0, 0, this);
        Image theThermometerImage;
        int theXCoord;
        int theYCoord;
        final Map theAgentsLocations = getAgentsLocations();
        synchronized (theAgentsLocations) {
            final Iterator theAgentNamesIter = theAgentsLocations.keySet().iterator();
            while (theAgentNamesIter.hasNext()) {
                final String theAgentName = (String) theAgentNamesIter.next();
                final Point theAgentLocation = (Point) theAgentsLocations.get(theAgentName);
                final double theAgentBeneficio = ((Double) mAgentsBeneficioSizes.get(theAgentName)).doubleValue();
                theXCoord = (int) theAgentLocation.getX();
                theYCoord = (int) theAgentLocation.getY();
                final Set theSelectedAgents = getSelectedAgents();
                String theImagePath;
                if (theSelectedAgents.contains(theAgentName)) {
                    theImagePath = getConfiguration().getAgentProperty(theAgentName, "map-picture-selected");
                } else {
                    theImagePath = getConfiguration().getAgentProperty(theAgentName, "map-picture");
                }
                final Image theImage = Bundle.getImage(theImagePath);
                final int halfWidth = theImage.getWidth(this) / 2;
                final int halfHeight = theImage.getHeight(this) / 2;
                inGraphics.drawImage(theImage, theXCoord - halfWidth, theYCoord - halfHeight, this);
                theThermometerImage = getThermometerImage(theAgentBeneficio);
                inGraphics.drawImage(theThermometerImage, theXCoord - halfWidth, theYCoord + halfHeight, this);
                final Map theAgentRects = getAgentsRects();
                if (!theAgentRects.containsKey(theAgentName)) {
                    final Rectangle theAgentRectangle = new Rectangle(theXCoord - halfWidth, theYCoord - halfHeight, Math.max(theThermometerImage.getWidth(this), theImage.getWidth(this)), theImage.getHeight(this) + theThermometerImage.getHeight(this));
                    theAgentRects.put(theAgentName, theAgentRectangle);
                }
            }
        }
        final Point theLocalAgentLocation = getLocalAgentLocation();
        if (theLocalAgentLocation != null) {
            final Image theImage = Bundle.getImage("artwork/map/simcafe/computer.png");
            final int halfWidth = theImage.getWidth(this) / 2;
            final int halfHeight = theImage.getHeight(this) / 2;
            theXCoord = (int) theLocalAgentLocation.getX();
            theYCoord = (int) theLocalAgentLocation.getY();
            inGraphics.drawImage(theImage, theXCoord - halfWidth, theYCoord - halfHeight, this);
            theThermometerImage = getThermometerImage(mLocalAgentBeneficioSize);
            inGraphics.drawImage(theThermometerImage, theXCoord - halfWidth, theYCoord + halfHeight, this);
            Rectangle theLocalAgentRect = getLocalAgentRect();
            if (theLocalAgentRect == null) {
                theLocalAgentRect = new Rectangle(theXCoord - halfWidth, theYCoord - halfHeight, Math.max(theThermometerImage.getWidth(this), theImage.getWidth(this)), theImage.getHeight(this) + theThermometerImage.getHeight(this));
                setLocalAgentRect(theLocalAgentRect);
            }
        }
    }

    /**
	 * M�thode appel�e pour indiquer qu'un nouveau
	 * joueur est arriv�.
	 *
	 * @param inName			nom du joueur � ajouter.
	 */
    public final void addAgent(String inName) {
        synchronized (mAgentsBeneficioSizes) {
            final double theBeneficioSize = Double.parseDouble(getConfiguration().getAgentProperty(inName, "beneficio")) / mBeneficioMaxSize;
            mAgentsBeneficioSizes.put(inName, new Double(theBeneficioSize));
            super.addAgent(inName);
        }
    }

    /**
	 * M�thode appel�e pour supprimer un joueur.
	 *
	 * @param inName		nom du joueur � supprimer
	 */
    public final void removeAgent(String inName) {
        synchronized (mAgentsBeneficioSizes) {
            mAgentsBeneficioSizes.remove(inName);
            super.removeAgent(inName);
        }
    }

    /**
	 * M�thode pour r�cup�rer le thermom�tre en fonction de la valeur entre 0
	 * et 1.
	 *
	 * @param inValue	valeur entre 0 et 1
	 * @return l'image de thermom�tre correspondante
	 */
    private Image getThermometerImage(double inValue) {
        String theImageName;
        final int theRoundedValue = (int) Math.floor((inValue * 10) + 0.5);
        if (theRoundedValue <= 0) {
            theImageName = "slider.png";
        } else if (theRoundedValue > 10) {
            theImageName = "slider_f10.png";
        } else {
            theImageName = "slider_f" + theRoundedValue + ".png";
        }
        return Bundle.getImage("artwork/widgets/simcafe/" + theImageName);
    }
}
