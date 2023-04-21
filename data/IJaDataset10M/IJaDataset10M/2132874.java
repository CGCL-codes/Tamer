package fr.ign.cogit.appli.geopensim.comportement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.log4j.Logger;
import fr.ign.cogit.appli.geopensim.agent.Agent;
import fr.ign.cogit.appli.geopensim.agent.AgentGeographiqueCollection;
import fr.ign.cogit.appli.geopensim.agent.meso.AgentZoneElementaireBatie;
import fr.ign.cogit.appli.geopensim.agent.micro.AgentBatiment;
import fr.ign.cogit.appli.geopensim.agent.micro.AgentTroncon;
import fr.ign.cogit.appli.geopensim.agent.micro.AgentTronconRoute;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.contrib.cartetopo.Arc;
import fr.ign.cogit.geoxygene.contrib.cartetopo.CarteTopo;
import fr.ign.cogit.geoxygene.contrib.cartetopo.Chargeur;
import fr.ign.cogit.geoxygene.contrib.cartetopo.Noeud;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPosition;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPositionList;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_LineString;

/**
 * @author Julien Perret et Florence Curie
 *
 */
public class ComportementConstructionRoute extends Comportement {

    private static Logger logger = Logger.getLogger(ComportementConstructionRoute.class.getName());

    private double angleMax = 45.0;

    private int nbRays = 3;

    @Override
    public void declencher(Agent agent) {
        super.declencher(agent);
        if (agent instanceof AgentZoneElementaireBatie) {
            if (logger.isDebugEnabled()) logger.debug("Début de la construction d'une route dans la zone élémentaire " + agent);
            AgentZoneElementaireBatie agentZoneElementaireBatie = (AgentZoneElementaireBatie) agent;
            if (logger.isDebugEnabled()) logger.debug("Distance route = " + agentZoneElementaireBatie.getDistanceALaRoute());
            CarteTopo carte = new CarteTopo("Carte troncons");
            Chargeur.importClasseGeo(new FT_FeatureCollection<AgentTroncon>(agentZoneElementaireBatie.getTroncons()), carte, true);
            carte.creeNoeudsManquants(1);
            carte.creeTopologieArcsNoeuds(1);
            carte.creeTopologieFaces();
            List<GM_LineString> lines = new ArrayList<GM_LineString>();
            double angleInRadians = angleMax * Math.PI / (360.0 * nbRays);
            for (Arc arc : carte.getPopArcs()) {
                if (arc.isPendant() && ((arc.getNoeudIni().arcs().size() == 1) || (arc.getNoeudFin().arcs().size() == 1))) {
                    Noeud noeud = (arc.getNoeudIni().arcs().size() == 1) ? arc.getNoeudIni() : arc.getNoeudFin();
                    IDirectPosition initialPosition = noeud.getGeometrie().getPosition();
                    IDirectPosition previousPosition = (arc.getGeometrie().getControlPoint(0).equals(initialPosition)) ? arc.getGeometrie().getControlPoint(1) : arc.getGeometrie().getControlPoint(arc.getGeometrie().sizeControlPoint() - 2);
                    double dx = initialPosition.getX() - previousPosition.getX();
                    double dy = initialPosition.getY() - previousPosition.getY();
                    double length = Math.sqrt(dx * dx + dy * dy);
                    double theta = Math.atan2(dy, dx);
                    dx /= length;
                    dy /= length;
                    length = 20;
                    for (int i = -nbRays; i < nbRays + 1; i++) {
                        DirectPosition finalPosition = new DirectPosition(initialPosition.getX() + length * Math.cos(theta + i * angleInRadians), initialPosition.getY() + length * Math.sin(theta + i * angleInRadians));
                        GM_LineString newLine = new GM_LineString(new DirectPositionList(Arrays.asList(initialPosition, finalPosition)));
                        lines.add(newLine);
                    }
                }
            }
            List<GM_LineString> intersectedLines = new ArrayList<GM_LineString>();
            for (GM_LineString line : lines) {
                for (AgentBatiment batiment : agentZoneElementaireBatie.getBatiments()) {
                    if (line.intersects(batiment.getGeom())) {
                        intersectedLines.add(line);
                        break;
                    }
                }
            }
            lines.removeAll(intersectedLines);
            if (logger.isDebugEnabled()) logger.debug(lines.size() + " lines proposed");
            for (GM_LineString line : lines) {
                if (logger.isDebugEnabled()) logger.debug("line = " + line);
                AgentTronconRoute agentTronconRoute = new AgentTronconRoute();
                agentTronconRoute.setGeom(line);
                agentTronconRoute.setSimulated(true);
                agentZoneElementaireBatie.getTroncons().add(agentTronconRoute);
                agentTronconRoute.getZonesElementaires().add(agentZoneElementaireBatie);
                AgentGeographiqueCollection.getInstance().getTronconsRoute().add(agentTronconRoute);
                agentZoneElementaireBatie.setPointLePlusLoinDeLaRoute(null);
                if (logger.isDebugEnabled()) logger.debug("Distance route après construction = " + agentZoneElementaireBatie.getDistanceALaRoute());
            }
            if (logger.isDebugEnabled()) logger.debug("Fin de la construction d'une route dans la zone élémentaire " + agent);
        } else {
            logger.error("Agent de type " + agent.getClass() + " au lieu de AgentZoneElementaireBatie");
        }
    }
}
