package fr.ign.cogit.appli.geopensim.comportement;

import java.util.Arrays;
import org.apache.log4j.Logger;
import fr.ign.cogit.appli.geopensim.agent.Agent;
import fr.ign.cogit.appli.geopensim.agent.meso.AgentZoneElementaireBatie;
import fr.ign.cogit.appli.geopensim.agent.micro.AgentBatiment;
import fr.ign.cogit.appli.geopensim.agent.micro.AgentTroncon;
import fr.ign.cogit.appli.geopensim.agent.micro.AgentTronconRoute;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IPolygon;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPosition;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPositionList;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_LineString;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_Polygon;
import fr.ign.cogit.geoxygene.util.algo.JtsAlgorithms;

/**
 * @author Julien Perret
 *
 */
public class ComportementDeplacementBatiment extends Comportement {

    private static Logger logger = Logger.getLogger(ComportementDeplacementBatiment.class.getName());

    static double distanceMinimum = 10;

    @Override
    public void declencher(Agent agent) {
        super.declencher(agent);
        if (agent instanceof AgentBatiment) {
            if (logger.isDebugEnabled()) logger.debug("ComportementDeplacementBatiment");
            AgentBatiment agentBatiment = (AgentBatiment) agent;
            AgentZoneElementaireBatie agentZoneElementaireBatie = agentBatiment.getGroupeBatimentsLePlusProche().getZoneElementaireBatie();
            if (agentBatiment.isSimulated()) {
                if (logger.isDebugEnabled()) logger.debug("Batiment initial = " + agentBatiment.getGeom());
                double distanceMin = Double.MAX_VALUE;
                for (AgentTroncon troncon : agentZoneElementaireBatie.getTroncons()) {
                    if (AgentTronconRoute.class.isAssignableFrom(troncon.getClass())) {
                        double distance = agentBatiment.getGeom().distance(troncon.getGeom());
                        if (distance < distanceMin) {
                            distanceMin = distance;
                            agentBatiment.setRouteLaPlusProche((AgentTronconRoute) troncon);
                            agentBatiment.setDistanceRouteLaPlusProche(distanceMin);
                        }
                    }
                }
                double tx = 0;
                double ty = 0;
                IDirectPosition centroid = agentBatiment.getGeom().centroid();
                if (agentBatiment.getRouteLaPlusProche() != null) {
                    if (logger.isDebugEnabled()) logger.debug("DistanceRouteLaPlusProche=" + agentBatiment.getDistanceRouteLaPlusProche());
                    if (agentBatiment.getDistanceRouteLaPlusProche() > distanceMinimum) {
                        IDirectPosition pointLePlusProche = JtsAlgorithms.getClosestPoint(centroid, (GM_LineString) agentBatiment.getRouteLaPlusProche().getGeom());
                        IDirectPosition pointLePlusProcheBatiment = JtsAlgorithms.getClosestPoint(pointLePlusProche, ((GM_Polygon) agentBatiment.getGeom()).exteriorLineString());
                        tx = (pointLePlusProche.getX() - pointLePlusProcheBatiment.getX());
                        ty = (pointLePlusProche.getY() - pointLePlusProcheBatiment.getY());
                        if ((tx != 0) || (ty != 0)) {
                            double longueur = Math.sqrt(tx * tx + ty * ty);
                            tx *= (longueur - distanceMinimum) / longueur;
                            ty *= (longueur - distanceMinimum) / longueur;
                        }
                    } else if (agentBatiment.getDistanceRouteLaPlusProche() > 0) {
                        IDirectPosition pointLePlusProche = JtsAlgorithms.getClosestPoint(centroid, (GM_LineString) agentBatiment.getRouteLaPlusProche().getGeom());
                        IDirectPosition pointLePlusProcheBatiment = JtsAlgorithms.getClosestPoint(pointLePlusProche, ((GM_Polygon) agentBatiment.getGeom()).exteriorLineString());
                        tx = (pointLePlusProche.getX() - pointLePlusProcheBatiment.getX());
                        ty = (pointLePlusProche.getY() - pointLePlusProcheBatiment.getY());
                        if (logger.isDebugEnabled()) {
                            logger.debug("vecteur = " + new GM_LineString(new DirectPositionList(Arrays.asList(pointLePlusProche, pointLePlusProcheBatiment))));
                        }
                        if ((tx != 0) || (ty != 0)) {
                            double longueur = Math.sqrt(tx * tx + ty * ty);
                            tx *= (distanceMinimum - longueur) / longueur;
                            ty *= (distanceMinimum - longueur) / longueur;
                        }
                    } else {
                    }
                } else {
                    if (logger.isDebugEnabled()) logger.debug("Pas de RouteLaPlusProche");
                }
                IDirectPosition vecteur = getVecteurIntersecte(agentBatiment.getGeom(), (GM_Polygon) agentZoneElementaireBatie.getGeom());
                tx += vecteur.getX();
                ty += vecteur.getY();
                int nbBatimentIntersectes = 0;
                for (AgentBatiment agentBatimentZone : agentZoneElementaireBatie.getBatiments()) {
                    if ((!agentBatimentZone.equals(agentBatiment)) && (agentBatimentZone.getGeom().intersects(agentBatiment.getGeom()))) {
                        nbBatimentIntersectes++;
                        IGeometry intersection = agentBatimentZone.getGeom().intersection(agentBatiment.getGeom());
                        double decalage = Math.sqrt(intersection.area());
                        IDirectPosition centroidBatimentZone = agentBatimentZone.getGeom().centroid();
                        double dx = centroidBatimentZone.getX() - centroid.getX();
                        double dy = centroidBatimentZone.getY() - centroid.getY();
                        double distanceEntreCentroides = Math.sqrt(dx * dx + dy * dy);
                        tx += tx * decalage / distanceEntreCentroides;
                        ty += ty * decalage / distanceEntreCentroides;
                    }
                }
                if ((tx != 0) || (ty != 0)) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("translation du batiment = " + tx + "  " + ty);
                        logger.debug(nbBatimentIntersectes + " batiments intersectés");
                    }
                    agentBatiment.setGeom(agentBatiment.getGeom().translate(tx, ty, 0));
                    if (logger.isDebugEnabled()) logger.debug("Batiment final = " + agentBatiment.getGeom());
                }
                distanceMin = Double.MAX_VALUE;
                for (AgentTroncon troncon : agentZoneElementaireBatie.getTroncons()) {
                    if (AgentTronconRoute.class.isAssignableFrom(troncon.getClass())) {
                        double distance = agentBatiment.getGeom().distance(troncon.getGeom());
                        if (distance < distanceMin) {
                            distanceMin = distance;
                            agentBatiment.setRouteLaPlusProche((AgentTronconRoute) troncon);
                            agentBatiment.setDistanceRouteLaPlusProche(distance);
                        }
                    }
                }
                if (logger.isDebugEnabled()) logger.debug("DistanceRouteLaPlusProche après déplacement=" + agentBatiment.getDistanceRouteLaPlusProche());
            }
        } else {
            logger.error("Agent de type " + agent.getClass() + " au lieu de AgentBatiment");
        }
    }

    /**
	 * @param geomBatiment
	 * @param geomZone
	 * @return
	 */
    private IDirectPosition getVecteurIntersecte(IGeometry geomBatiment, IPolygon geomZone) {
        IGeometry difference = geomBatiment.difference(geomZone);
        double tx = 0;
        double ty = 0;
        if (difference.isPolygon() && (difference instanceof GM_Polygon)) {
            GM_Polygon polygoneDifference = (GM_Polygon) difference;
            IDirectPosition pointLePlusLoin = JtsAlgorithms.getFurthestPoint(geomZone.exteriorLineString(), polygoneDifference.exteriorLineString());
            IDirectPosition pointLePlusProche = JtsAlgorithms.getClosestPoint(pointLePlusLoin, geomZone.exteriorLineString());
            tx = (pointLePlusProche.getX() - pointLePlusLoin.getX());
            ty = (pointLePlusProche.getY() - pointLePlusLoin.getY());
            if (logger.isDebugEnabled()) {
                logger.debug("polygoneIntersection = " + polygoneDifference);
                logger.debug("vecteur = " + new GM_LineString(new DirectPositionList(Arrays.asList((IDirectPosition) pointLePlusProche, pointLePlusLoin))));
            }
            if ((tx != 0) || (ty != 0)) {
                double longueur = Math.sqrt(tx * tx + ty * ty);
                tx *= (longueur + distanceMinimum) / longueur;
                ty *= (longueur + distanceMinimum) / longueur;
            }
        }
        return new DirectPosition(tx, ty);
    }
}
