package bbalc.tools;

import nu.xom.*;
import bbalc.com.tools.*;
import bbalc.core.*;
import bbalc.core.exceptions.*;
import bbalc.com.exceptions.*;
import bbalc.control.*;
import bbalc.core.codec.*;
import java.io.*;

/**
 * @author dirk
 */
public class XMLTools {

    public static void addAttribute(Element root, String name, String content) {
        root.addAttribute(new Attribute(name, content));
    }

    public static void addElement(Element root, String name) {
        Element e = new Element(name);
        root.appendChild(e);
    }

    public static void addElement(Element root, String name, int content) {
        addElement(root, name, String.valueOf(content));
    }

    public static void addElement(Element root, String name, String content) {
        Element e = new Element(name);
        e.appendChild(String.valueOf(content));
        root.appendChild(e);
    }

    public static Element getElementField() {
        int surface = Field.getSurface();
        Element root = new Element("Field");
        XMLTools.addElement(root, "FieldWidth", Field.getFieldWidth());
        XMLTools.addElement(root, "FieldHeight", Field.getFieldHeight());
        XMLTools.addElement(root, "WideZone", Field.getWideZone());
        XMLTools.addElement(root, "LineOfScrimmageOffset", Field.getLineOfScrimmageOffset());
        XMLTools.addElement(root, "EndZone", Field.getEndZone());
        XMLTools.addElement(root, "Surface", surface);
        Element squares = new Element("Squares");
        for (int y = 0; y < Field.getFieldHeight(); y++) {
            for (int x = 0; x < Field.getFieldWidth(); x++) {
                try {
                    int currentSurface = Field.getSquareType(x, y);
                    if (currentSurface != surface) {
                        Element square = new Element("Square");
                        square.addAttribute(new Attribute("x", String.valueOf(x)));
                        square.addAttribute(new Attribute("y", String.valueOf(y)));
                        square.appendChild(String.valueOf(currentSurface));
                        squares.appendChild(square);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        root.appendChild(squares);
        return root;
    }

    public static Element getElementFieldPos(FieldPos pos, boolean isBall) {
        Element root = null;
        if (isBall) root = new Element("BallPos"); else root = new Element("FieldPos");
        XMLTools.addElement(root, "x", pos.getX());
        XMLTools.addElement(root, "y", pos.getY());
        return root;
    }

    public static Element getElementGameState(boolean isSavedGame) {
        Element root = new Element("GameState");
        XMLTools.addElement(root, "CurrentState", GameState.getState());
        XMLTools.addElement(root, "Half", GameState.getHalf());
        XMLTools.addElement(root, "ReRollsGuest", GameState.getReRolls(ICodec.TEAM_GUEST));
        XMLTools.addElement(root, "ReRollsHome", GameState.getReRolls(ICodec.TEAM_HOME));
        XMLTools.addElement(root, "ScoreGuest", GameState.getScore(ICodec.TEAM_GUEST));
        XMLTools.addElement(root, "ScoreHome", GameState.getScore(ICodec.TEAM_HOME));
        XMLTools.addElement(root, "TurnGuest", GameState.getTurn(ICodec.TEAM_GUEST));
        XMLTools.addElement(root, "TurnHome", GameState.getTurn(ICodec.TEAM_HOME));
        XMLTools.addElement(root, "Weather", GameState.getWeather());
        XMLTools.addElement(root, "IsKickoffHome", Boolean.toString(GameState.isKickoffHome()));
        XMLTools.addElement(root, "IsTurnHome", Boolean.toString(GameState.isTurnHome()));
        root.appendChild(XMLTools.getElementFieldPos(GameState.getBallPosition(), true));
        Element teams = new Element("Teams");
        Element guest = XMLTools.getElementTeam(GameState.getTeam(ICodec.TEAM_GUEST), isSavedGame);
        Attribute ag = new Attribute("isTeamHome", "false");
        guest.addAttribute(ag);
        teams.appendChild(guest);
        Element home = XMLTools.getElementTeam(GameState.getTeam(ICodec.TEAM_HOME), isSavedGame);
        Attribute ah = new Attribute("isTeamHome", "true");
        home.addAttribute(ah);
        teams.appendChild(home);
        root.appendChild(teams);
        return root;
    }

    public static Element getElementPlayer(Player p, boolean isSavedGame) {
        Element root = new Element("Player");
        XMLTools.addAttribute(root, "name", p.getName());
        XMLTools.addAttribute(root, "position", p.getPosition());
        XMLTools.addElement(root, "Number", p.getNumber());
        XMLTools.addElement(root, "MA", p.getMA());
        XMLTools.addElement(root, "ST", p.getST());
        XMLTools.addElement(root, "AG", p.getAG());
        XMLTools.addElement(root, "AV", p.getAV());
        Element skills = new Element("Skills");
        int[] myskills = p.getSkills();
        if (myskills != null) {
            for (int n = 0; n < myskills.length; n++) {
                XMLTools.addElement(skills, SkillTools.getCategory(myskills[n]), String.valueOf(myskills[n]));
            }
        }
        root.appendChild(skills);
        Element myinj = new Element("Injuries");
        int[] cinj = p.getInjuries();
        if (cinj != null) {
            for (int n = 0; n < cinj.length; n++) {
                XMLTools.addElement(myinj, "Injury", String.valueOf(cinj[n]));
            }
        }
        root.appendChild(myinj);
        XMLTools.addElement(root, "EXP_COMP", p.getCOMP());
        XMLTools.addElement(root, "EXP_TD", p.getTD());
        XMLTools.addElement(root, "EXP_INT", p.getINT());
        XMLTools.addElement(root, "EXP_CAS", p.getCAS());
        XMLTools.addElement(root, "EXP_MVP", p.getMVP());
        if (isSavedGame) {
            XMLTools.addElement(root, "Status", p.getStatus());
        }
        root.appendChild(XMLTools.getElementFieldPos(p.getFieldPos(), false));
        return root;
    }

    public static Element getElementRules() {
        Element root = new Element("Rules");
        XMLTools.addElement(root, "PlayerTeamMax", Rules.getPlayerTeamMax());
        XMLTools.addElement(root, "TurnsPerHalf", Rules.getTurnsPerHalf());
        return root;
    }

    public static Element getElementTeam(ITeam t, boolean isSavedGame) {
        Element root = new Element("Team");
        XMLTools.addAttribute(root, "name", t.getName());
        XMLTools.addAttribute(root, "race", t.getRace());
        XMLTools.addAttribute(root, "coach", t.getCoach());
        XMLTools.addElement(root, "TeamRating", t.getTeamRating());
        XMLTools.addElement(root, "Treasury", t.getTreasury());
        XMLTools.addElement(root, "ReRolls", t.getReRolls());
        XMLTools.addElement(root, "FanFactor", t.getFanFactor());
        XMLTools.addElement(root, "AssistantCoaches", t.getAssistantCoaches());
        XMLTools.addElement(root, "Cheerleaders", t.getCheerleaders());
        XMLTools.addElement(root, "Apothecary", t.getApothecaries());
        XMLTools.addElement(root, "Wizard", t.getWizard());
        Element playerRoot = new Element("Players");
        Player[] myPlayer = t.getPlayer();
        if (myPlayer != null) {
            for (int n = 0; n < myPlayer.length; n++) {
                if (myPlayer[n] != null) {
                    playerRoot.appendChild(XMLTools.getElementPlayer(myPlayer[n], isSavedGame));
                }
            }
        }
        root.appendChild(playerRoot);
        return root;
    }

    public static Document loadXML(String url) throws B2alcException {
        Document doc = null;
        if (url != null) {
            try {
                Builder parser = new Builder();
                doc = parser.build(url);
            } catch (IOException ex) {
                throw new InternalErrorException("XMLTools.loadXML()> Could not connect URL (" + url + ")");
            } catch (ParsingException ex) {
                throw new DecodingException("XMLTools.loadXML()> The document is malformed. (" + url + ")");
            }
        } else {
            throw new InternalErrorException("XMLTools.loadXML()> cannot load file because no filename was provided!");
        }
        return doc;
    }

    public static void writeXML(Document d, OutputStream os) {
        try {
            Serializer serializer = new Serializer(os, "ISO-8859-1");
            serializer.setIndent(4);
            serializer.setMaxLength(80);
            serializer.write(d);
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public static void writeXML(Document d, String path) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(path);
            writeXML(d, fos);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int[] xml2intArr(Element root) throws DecodingException {
        Elements nodes = root.getChildElements();
        int[] res = null;
        if (nodes.size() > 0) {
            res = new int[nodes.size()];
            for (int n = 0; n < nodes.size(); n++) {
                Element node = nodes.get(n);
                res[n] = StringTools.str2int(node.getValue());
            }
        } else res = new int[] { IInjuries.NONE };
        return res;
    }

    public static void load(String filename) {
        try {
            Document d = XMLTools.loadXML(filename);
            System.out.println("loading game...");
            Elements segments = d.getRootElement().getChildElements();
            for (int n = 0; n < segments.size(); n++) {
                Element seg = segments.get(n);
                if ("Field".equals(seg.getLocalName())) {
                    Field.initFromXML(seg);
                } else if ("Rules".equals(seg.getLocalName())) {
                    Rules.initFromXML(seg);
                } else if ("GameState".equals(seg.getLocalName())) {
                    GameState.initFromXML(seg);
                }
            }
            IControlHandler ch = ControlHandler.getInstance();
            ch.initGame(1);
            System.out.println("loading finished.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
