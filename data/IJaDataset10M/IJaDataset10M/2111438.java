package model.planet;

import model.utils.directions.DirectionLeft;
import model.utils.directions.DirectionDown;
import model.utils.directions.DirectionRight;
import model.planet.elements.Blocking;
import model.planet.elements.FreeElement;
import model.planet.elements.BlockRatio;
import model.planet.elements.Killable;
import model.planet.elements.MapElement;
import model.planet.elements.Pooglin;
import model.planet.elements.TunnelTransportable;
import model.planet.elements.Deadly;
import model.planet.elements.Tunnel;
import model.planet.elements.Block;
import model.planet.elements.EndSpaceShip;
import model.planet.elements.Blockable;
import model.planet.elements.Walkable;
import model.*;
import java.util.*;

/**
 * El Enviroment consiste en los elementos que circunda a un dade FreeElement
 * y que pueden llegar a afectarlo. Incluye tanto a FreeElements como
 * FixedElements
 */
public class Enviroment {

    private Vector mapElements = new Vector();

    private Walkable floor = null;

    /**
     * M�todo encargado de procesar cada uno de los elementos del Enviroment
     * y de operar con el FreeElement en caso de que as� corresponda
     *
     * @param p Elemento que ser� procesado junto con su Enviroment
     */
    public void process(FreeElement p) {
        Iterator iterElem = mapElements.iterator();
        while (iterElem.hasNext()) {
            MapElement mapElement = (MapElement) iterElem.next();
            if (p != mapElement) {
                if (p instanceof Killable) {
                    if (mapElement instanceof Deadly) {
                        if (BlockRatio.elementInRadius((Block) mapElement, ((Deadly) mapElement).radiusOfAction().getRadius(), p)) {
                            ((Killable) p).kill();
                        }
                    }
                }
                if (p instanceof Blockable) {
                    if (mapElement instanceof Blocking) {
                        Distance d = Distance.getMinDistance(((MapElement) mapElement).getPosition(), ((MapElement) mapElement).getSize(), p.getPosition(), p.getSize());
                        int distanceX = d.getDistanceX();
                        int distanceY = d.getDistanceY();
                        if ((distanceX < 0 && distanceY <= 0 && distanceY > -3)) {
                            if (mapElement instanceof Walkable) this.setFloor((Walkable) mapElement, p);
                            ((Blockable) p).block(new DirectionDown());
                        }
                        if ((distanceY < 0 && distanceX < 0 && distanceX >= -2)) {
                            if (((MapElement) mapElement).getPosition().getX() > p.getPosition().getX()) {
                                ((Blockable) p).block(new DirectionRight());
                            } else {
                                ((Blockable) p).block(new DirectionLeft());
                            }
                        }
                        if (distanceX < -2 && distanceY < -2) {
                            ((Killable) p).kill();
                        }
                    }
                }
                if (p instanceof TunnelTransportable) {
                    if (mapElement instanceof Tunnel) {
                        Distance d = Distance.getMinDistance(((MapElement) mapElement).getPosition(), ((MapElement) mapElement).getSize(), p.getPosition(), p.getSize());
                        if (d.getDistanceX() < 0 && d.getDistanceY() < 0) ((Tunnel) mapElement).transport((TunnelTransportable) p);
                    }
                }
                if (p instanceof Pooglin) {
                    if (mapElement instanceof EndSpaceShip) {
                        Distance d = Distance.getMinDistance(((MapElement) mapElement).getPosition(), ((MapElement) mapElement).getSize(), p.getPosition(), p.getSize());
                        int distanceX = d.getDistanceX();
                        int distanceY = d.getDistanceY();
                        if (distanceX <= 0 && distanceY <= 0) {
                            PlanetMap.getInstance().removeSavedPooglin((Pooglin) p);
                        }
                    }
                }
            }
        }
    }

    /**
     * Agrega un MapElement al Enviroment
     *
     * @param elem
     */
    public void addMapElement(MapElement elem) {
        mapElements.add(elem);
    }

    private void setFloor(Walkable elem, FreeElement p) {
        if (this.floor == null) {
            this.floor = elem;
        } else {
            int distanceOld = Distance.getCenterDistance((MapElement) this.floor, p).getDistanceX();
            int distanceNew = Distance.getCenterDistance((MapElement) elem, p).getDistanceX();
            if (distanceNew < distanceOld) this.floor = elem;
        }
    }

    /**
     * Retorna el Floor para el elemento. El floor consiste en el element
     * sobre el que est� posicionado el FreeElement
     *
     * @return El floor que es un FixedElement
     */
    public Walkable getFloor() {
        return this.floor;
    }

    /**
     * Retoran un iterador con los elementos del Enviromemt
     *
     * @return iterador del Enviromemt
     */
    public Iterator elements() {
        return mapElements.iterator();
    }

    /**
     * Representaci�n de una posici�n como Enviroment
     *
     * @return Representaci�n como String
     */
    public String toString() {
        String s = "[Enviroment]\n";
        s += "\t[mapElements size=" + mapElements.size() + "]\n";
        s += "\t\t";
        Iterator it = mapElements.iterator();
        while (it.hasNext()) {
            MapElement elem = (MapElement) it.next();
            s += elem + "(" + elem.getPosition() + ")";
        }
        s += "\t[floor " + (floor == null ? "null" : floor) + "]\n";
        return s;
    }
}
