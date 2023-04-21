package model.planet;

import model.utils.directions.Direction;
import model.utils.directions.DirectionHorizontal;
import model.planet.skills.LaserShot;
import model.planet.skills.BuildTunnel;
import model.planet.skills.Drill;
import model.planet.skills.FlyingSaucer;
import model.planet.skills.Teleport;
import model.planet.skills.Skill;
import model.planet.skills.Freeze;
import model.planet.elements.DirtStatus;
import model.planet.elements.StartSpaceShip;
import model.planet.elements.BlackHole;
import model.planet.elements.Dirt;
import model.planet.elements.Tunnel;
import model.planet.elements.Air;
import model.planet.elements.EndSpaceShip;
import model.planet.elements.Fire;
import model.planet.elements.LaserBeam;
import model.planet.elements.Rock;
import model.planet.elements.Ice;
import model.planet.elements.Pooglin;
import model.*;
import model.states.StateManager;
import model.states.State;
import model.states.FallingState;
import java.io.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.SAXParser;
import java.util.*;

/**
 * Clase encargada de leer desde archivos los planeta (niveles), crearlos y
 * prepararlos para ser jugados
 * @author Administrator
 */
public class PlanetBuilder {

    /**
     * M�todo con el que se procesa el archivo para obtener un Planet
     *
     * @param fileName
     */
    public static void build(String fileName) {
        Planet.clean();
        Planet p = Planet.getInstance();
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            SAXParser saxParser = factory.newSAXParser();
            saxParser.parse(new File(fileName), new PlanetSAXHandler(p));
        } catch (Throwable err) {
            err.printStackTrace();
        }
    }
}

class PlanetSAXHandler extends DefaultHandler {

    Planet planet;

    AuxPooglin auxPooglin;

    public PlanetSAXHandler(Planet p) {
        this.planet = p;
    }

    public synchronized void startDocument() throws SAXException {
    }

    public synchronized void startElement(String namespaceURI, String localName, String qName, Attributes atts) {
        String stringField;
        if (qName.equals("planet")) {
            stringField = atts.getValue("availablepooglins");
            planet.setAvailablePooglins(Integer.parseInt(stringField));
            stringField = atts.getValue("pooglinscreateinterval");
            planet.setPooglinCreateInterval(Integer.parseInt(stringField));
            stringField = atts.getValue("pooglinstosave");
            planet.setPooglinsToSave(Integer.parseInt(stringField));
            stringField = atts.getValue("pooglinssaved");
            planet.setPooglinsSaved(Integer.parseInt(stringField));
            stringField = atts.getValue("timeleft");
            planet.setSecondsLeft(Integer.parseInt(stringField));
        } else if (qName.equals("availablehabilities")) {
        } else if (qName.equals("skill")) {
            stringField = atts.getValue("name");
            String name = stringField;
            stringField = atts.getValue("count");
            int count = Integer.parseInt(stringField);
            planet.setSkill(name, count);
        } else if (qName.equals("map")) {
            stringField = atts.getValue("rows");
            int rows = Integer.parseInt(stringField);
            stringField = atts.getValue("cols");
            int cols = Integer.parseInt(stringField);
            planet.getMap().init(cols, rows);
        } else if (qName.equals("air")) {
            stringField = atts.getValue("row");
            int row = Integer.parseInt(stringField);
            stringField = atts.getValue("col");
            int col = Integer.parseInt(stringField);
            planet.getMap().addFixedElement(new Air(row - 1, col - 1));
        } else if (qName.equals("blackhole")) {
            stringField = atts.getValue("row");
            int row = Integer.parseInt(stringField);
            stringField = atts.getValue("col");
            int col = Integer.parseInt(stringField);
            planet.getMap().addFixedElement(new BlackHole(row - 1, col - 1));
        } else if (qName.equals("dirt")) {
            stringField = atts.getValue("row");
            int row = Integer.parseInt(stringField);
            stringField = atts.getValue("col");
            int col = Integer.parseInt(stringField);
            stringField = atts.getValue("erosion");
            int erosion;
            try {
                erosion = Integer.parseInt(stringField);
            } catch (NumberFormatException nfe) {
                erosion = DirtStatus.DEFAULT_EROSION_LEVEL;
            }
            planet.getMap().addFixedElement(new Dirt(row - 1, col - 1, erosion));
        } else if (qName.equals("endspaceship")) {
            stringField = atts.getValue("row");
            int row = Integer.parseInt(stringField);
            stringField = atts.getValue("col");
            int col = Integer.parseInt(stringField);
            planet.getMap().addFixedElement(new EndSpaceShip(row - 1, col - 1));
        } else if (qName.equals("fire")) {
            stringField = atts.getValue("row");
            int row = Integer.parseInt(stringField);
            stringField = atts.getValue("col");
            int col = Integer.parseInt(stringField);
            planet.getMap().addFixedElement(new Fire(row - 1, col - 1));
        } else if (qName.equals("ice")) {
            stringField = atts.getValue("row");
            int row = Integer.parseInt(stringField);
            stringField = atts.getValue("col");
            int col = Integer.parseInt(stringField);
            planet.getMap().addFixedElement(new Ice(row - 1, col - 1));
        } else if (qName.equals("rock")) {
            stringField = atts.getValue("row");
            int row = Integer.parseInt(stringField);
            stringField = atts.getValue("col");
            int col = Integer.parseInt(stringField);
            planet.getMap().addFixedElement(new Rock(row - 1, col - 1));
        } else if (qName.equals("startspaceship")) {
            stringField = atts.getValue("row");
            int row = Integer.parseInt(stringField);
            stringField = atts.getValue("col");
            int col = Integer.parseInt(stringField);
            planet.getMap().addFixedElement(new StartSpaceShip(row - 1, col - 1));
        } else if (qName.equals("pooglin")) {
            auxPooglin = new AuxPooglin();
            stringField = atts.getValue("x");
            int x = Integer.parseInt(stringField);
            stringField = atts.getValue("y");
            int y = Integer.parseInt(stringField);
            stringField = atts.getValue("state");
            State state;
            try {
                state = StateManager.getStateByCode(stringField);
            } catch (RuntimeException rte) {
                state = new FallingState();
            }
            stringField = atts.getValue("walking");
            DirectionHorizontal walkingDirection = (DirectionHorizontal) Direction.getDirection(stringField);
            stringField = atts.getValue("currentskill");
            String currentSkill = stringField;
            auxPooglin.pos = new Position(x, y);
            auxPooglin.state = state;
            auxPooglin.walkingDirection = walkingDirection;
            if (currentSkill != null) auxPooglin.currentSkill = Skill.getSkillByCode(currentSkill);
        } else if (qName.equals("laser")) {
            stringField = atts.getValue("x");
            int x = Integer.parseInt(stringField);
            stringField = atts.getValue("y");
            int y = Integer.parseInt(stringField);
            stringField = atts.getValue("direction");
            DirectionHorizontal direction = (DirectionHorizontal) Direction.getDirection(stringField);
            LaserBeam laserBeam = new LaserBeam(direction, new Position(x, y));
            planet.getMap().addLaserBeam(laserBeam);
        } else if (qName.equals("tunnel")) {
            stringField = atts.getValue("x");
            int x = Integer.parseInt(stringField);
            stringField = atts.getValue("y");
            int y = Integer.parseInt(stringField);
            stringField = atts.getValue("fromdirection");
            DirectionHorizontal fromDirection = (DirectionHorizontal) Direction.getDirection(stringField);
            stringField = atts.getValue("blocksize");
            int blockSize = Integer.parseInt(stringField);
            ;
            Tunnel tunnel = new Tunnel(new Position(x, y), fromDirection, blockSize);
            planet.getMap().addTunnel(tunnel);
        } else if (qName.equals("freeze")) {
            auxPooglin.skills.add(new Freeze());
        } else if (qName.equals("flyingsaucer")) {
            auxPooglin.skills.add(new FlyingSaucer());
        } else if (qName.equals("lasershot")) {
            auxPooglin.skills.add(new LaserShot());
        } else if (qName.equals("teleport")) {
            auxPooglin.skills.add(new Teleport());
        } else if (qName.equals("buildtunnel")) {
            auxPooglin.skills.add(new BuildTunnel());
        } else if (qName.equals("drill")) {
            auxPooglin.skills.add(new Drill());
        }
    }

    public synchronized void endElement(String namespaceURI, String localName, String qName) {
        if (qName.equals("pooglin")) {
            Pooglin pooglin = Pooglin.buildFromFile(auxPooglin.pos, auxPooglin.state, auxPooglin.walkingDirection, auxPooglin.skills.iterator(), auxPooglin.currentSkill);
            planet.getMap().addPooglin(pooglin);
        } else if (qName.equals("habilities")) {
        }
    }
}

class AuxPooglin {

    Position pos;

    State state;

    DirectionHorizontal walkingDirection;

    Skill currentSkill;

    Vector skills = new Vector();
}
