package nl.utwente.ewi.hmi.deira.iam.vvciam;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import nl.utwente.ewi.hmi.deira.iam.vvciam.match.MatchTracker;
import nl.utwente.ewi.hmi.deira.queue.Event;
import nl.utwente.ewi.hmi.deira.queue.VVCEvent;

/** Klasse genereert Events aan de hand van robosoccer situaties.
 * @version
 * @author Buursma, Hendriksen, Ten Hoeve, Van der Kooi
 */
public class EventGenerator implements Observer {

    /** Array met de gebieden die de 2 goals beslaan. <br>
     *  Als <code>goal[x].contains(object)</code> dan ligt object in het doel.<br>
     *  <code>goal[x]</code> ligt in <code>goalArea[x]</code>
     */
    public static final Area[] goal = { new Area(new Rectangle(-400, 700, 400, 400)), new Area(new Rectangle(2200, 700, 400, 400)) };

    /** Array met de 2 goalareas (gebied voor goal.)<br>
     *  <code>goalArea[x]</code> ligt in <code>penaltyArea[x]</code>
     */
    public static final Area[] goalArea = { new Area(new Rectangle(0, 650, 150, 500)), new Area(new Rectangle(2050, 650, 150, 500)) };

    /** Array met de 2 penaltyreas.<br>
     *  <code>penaltyArea[x]</code> ligt in <code>side[x]</code>
     */
    public static final Area[] penaltyArea = { new Area(new Rectangle(0, 500, 350, 800)), new Area(new Rectangle(1850, 500, 350, 800)) };

    /** Array met de 2 helften van het veld.
      */
    public static final Area[] side = { new Area(new Rectangle(0, 0, 1100, 1800)), new Area(new Rectangle(1100, 0, 1100, 1800)) };

    /** Array met 4 veld-kwarten. <br>
      * Tijdens een freeball mogen er, behalve de 2 nemende robots,  geen robots in het bijbehorden kwart.
      * <code>quarter[x]</code> hoort bij <code> freeballpositie[x]</code><br>
      * <code>quarter[0]</code> en <code>quarter[1]</code> liggen binnen <code>side[0]</code><br>
      * <code>quarter[2]</code> en <code>quarter[3]</code> liggen binnen <code>side[1]</code><br>      
      */
    public static final Area[] quarter = { new Area(new Rectangle(0, 0, 1100, 900)), new Area(new Rectangle(0, 900, 1100, 1800)), new Area(new Rectangle(1100, 0, 1100, 900)), new Area(new Rectangle(1100, 900, 1100, 1800)) };

    /** Het gebied van de middencirkel.*/
    public static final Area centerCircle = new Area(new Ellipse2D.Double(850, 650, 500, 500));

    /** Positie van de middenstip. */
    public static final Point centerPoint = new Point(1100, 900);

    /** Array met de 4 freeballposities. <br>
      * Bij een freeball ligt de bal op 1 van deze posities.<br>
      * <code>freeballPosition[x]</code> valt binnen <code> quarter[x] </code>
      */
    public static final Point[] freeballPosition = { new Point(550, 300), new Point(550, 1500), new Point(1650, 300), new Point(1650, 1500) };

    /** Array met de (voor 2 robots) verplichte robot-posities bij een freeball.<br>
     *  Als bij een freeball de bal op <code>freeballPosition[0]</code> ligt, dan staan de robots op <code>freeballRobotPosition[0][0]</code> en <code>freeballRobotPosition[0][1]</code>.
     */
    public static final Point[][] freeballRobotPosition = { { new Point(550 - 250, 300), new Point(550 + 250, 300) }, { new Point(550 - 250, 1500), new Point(550 + 250, 1500) }, { new Point(1650 - 250, 300), new Point(1650 + 250, 300) }, { new Point(1650 - 250, 1500), new Point(1650 + 250, 1500) } };

    /** Array met de 2 penalty stippen. De eerste van binnen side[0].
     */
    public static final Point[] penaltyPosition = { new Point(375, 900), new Point(2200 - 375, 900) };

    /** Marge waarbinnen een bal of robot op een bepaald punt wordt beschouwd. <br>
        areaWidth is de diameter van een cirkel rond een Point (bijvoorbeeld penaltyPosition). 
        Objecten binnen deze cirkel worden beschouwd op dit punt te liggen.  */
    public static final int areaWidth = 80;

    private MatchTracker match;

    private java.util.List<Situation> sits;

    private Set<String> currentEvents;

    private Set<String> formerEvents;

    private nl.utwente.ewi.hmi.deira.iam.vvciam.match.Robot possession;

    private nl.utwente.ewi.hmi.deira.iam.vvciam.match.Robot lastPossession;

    private nl.utwente.ewi.hmi.deira.iam.vvciam.match.Robot preLastPossession;

    private String lastBallPossession;

    private boolean freeBallEvent;

    private boolean goalEvent;

    private boolean penaltyKickEvent;

    private boolean goalKickEvent;

    private boolean freeBallTaken;

    private boolean penaltyKickTaken;

    private boolean goalKickTaken;

    private boolean ballPossession;

    private boolean shotontarget;

    private boolean keepersave;

    private boolean defend;

    protected nl.utwente.ewi.hmi.deira.mmm.MMM mmm;

    private int eventcounter;

    /** Cre�ert een EventGenerator 
    */
    public EventGenerator(MatchTracker match) {
        this.match = match;
        sits = new ArrayList<Situation>();
        currentEvents = new HashSet<String>();
        formerEvents = new HashSet<String>();
        possession = null;
        lastPossession = null;
        preLastPossession = null;
        freeBallEvent = false;
        goalEvent = false;
        penaltyKickEvent = false;
        goalKickEvent = false;
        freeBallTaken = false;
        penaltyKickTaken = false;
        goalKickTaken = false;
        ballPossession = false;
        lastBallPossession = "";
        shotontarget = false;
        keepersave = false;
        defend = false;
    }

    /** Methode dient alleen aangeroepen te worden via het Observer/Observable pattern, door een analyzer.FileReader instantie.
     *  De aanroep van deze methode zet het analyseren van spelSituaties, en het genereren van de daaruit volgende events in werking.
     * @param arg een instantie van Situation met de actuele spelsituatie.
     */
    public void update(Observable o, Object arg) {
        compareLogs((Situation) arg);
    }

    /** Functie beschouwt de Situation, vergelijkt met eerdere Situations en genereert eventueel Events. 
     */
    private void compareLogs(Situation sit) {
        formerEvents = currentEvents;
        currentEvents = new HashSet<String>();
        if (sit == null) {
            RSEvent e = EndEvent.checkEvent(match, sits, this);
            processEvent(e);
        } else {
            if (sits.size() >= 1) {
                sit.calculateArrayListData(sits.get(sits.size() - 1));
            }
            sits.add(sit);
            possession = determinePossession(sit);
            if (possession != null && possession != lastPossession) {
                preLastPossession = lastPossession;
                lastPossession = possession;
            }
            if (lastPossession == null) {
                System.out.println("dit kan alleen aan het begin van de wedstrijd");
            }
            if (sit.getMoment() == 1) {
                RSEvent e = StartEvent.checkEvent(match, sits, this);
                processEvent(e);
            }
            FreeBallTakenEvent fbte = null;
            if (freeBallTaken) {
                fbte = FreeBallTakenEvent.checkEvent(match, sits, this);
            }
            FreeBallEvent fbe = FreeBallEvent.checkEvent(match, sits, this);
            freeBallEvent = freeBallEvent || fbe != null;
            FreeKickEvent fke = checkFreeKick(sit);
            GoalKickEvent gke = GoalKickEvent.checkEvent(match, sits, this);
            GoalDisallowedEvent gde = null;
            if (goalEvent && gke != null) {
                gde = GoalDisallowedEvent.checkEvent(match, sits, this);
                gke = null;
                goalKickEvent = true;
            } else if (gke != null) {
                goalKickEvent = true;
            }
            GoalEvent ge = GoalEvent.checkEvent(match, sits, this);
            if (ge != null && !freeBallEvent && !goalKickEvent && !goalEvent) goalEvent = true; else {
                ge = null;
            }
            KickOffEvent koe = KickOffEvent.checkEvent(match, sits, this);
            PenaltyKickEvent pke = PenaltyKickEvent.checkEvent(match, sits, this);
            penaltyKickEvent = penaltyKickEvent || pke != null;
            BoringEvent be = BoringEvent.checkEvent(match, sits, this);
            PassEvent pe = Math.random() > 0.99 ? PassEvent.checkEvent(match, sits, this) : null;
            PenaltyKickTakenEvent pkte = null;
            if (penaltyKickTaken) {
                pkte = PenaltyKickTakenEvent.checkEvent(match, sits, this);
            }
            GoalKickTakenEvent gkte = null;
            if (goalKickTaken) {
                gkte = GoalKickTakenEvent.checkEvent(match, sits, this);
            }
            ShotOnTargetEvent sote = null;
            if (!shotontarget) {
                sote = ShotOnTargetEvent.checkEvent(match, sits, this);
                if (sote != null) {
                    shotontarget = true;
                }
            }
            KeeperSaveEvent kse = null;
            if (keepersave) {
                kse = KeeperSaveEvent.checkEvent(match, sits, this);
            }
            DefendEvent de = null;
            if (defend) {
                de = DefendEvent.checkEvent(match, sits, this);
            }
            BallPossessionEvent bpe = BallPossessionEvent.checkEvent(match, sits, this);
            if (bpe != null && Math.random() > 0.94 && !(bpe.getRobot1().equals(lastBallPossession))) {
                lastBallPossession = bpe.getRobot1();
            } else {
                bpe = null;
            }
            StatsEvent se = Math.random() > 0.9999 ? StatsEvent.checkEvent(match, sits, this) : null;
            if (fbe != null) {
                processEvent(fbe);
            } else if (fke != null) {
                processEvent(fke);
            } else if (ge != null) {
                processEvent(ge);
            } else if (gke != null) {
                processEvent(gke);
            } else if (gde != null) {
                processEvent(gde);
                goalEvent = false;
                match.goalDisallowed();
            } else if (koe != null) {
                processEvent(koe);
                goalEvent = false;
            } else if (pke != null) {
                processEvent(pke);
            } else if (be != null) {
                processEvent(be);
            } else if (pe != null) {
                processEvent(pe);
            } else if (fbte != null) {
                processEvent(fbte);
                freeBallTaken = false;
            } else if (pkte != null) {
                processEvent(pkte);
                penaltyKickTaken = false;
            } else if (gkte != null) {
                processEvent(gkte);
                goalKickTaken = false;
            } else if (sote != null) {
                processEvent(sote);
            } else if (kse != null) {
                processEvent(kse);
                keepersave = false;
            } else if (bpe != null) {
                processEvent(bpe);
            } else if (se != null) {
                processEvent(se);
            } else if (de != null) {
                processEvent(de);
                defend = false;
            }
        }
    }

    private void processEvent(RSEvent e) {
        currentEvents.add(e.getType());
        if (!formerEvents.contains(e.getType())) {
            Event ev = transformVSEvent(e);
            incrementEventID();
            mmm.processEvent(ev);
        }
    }

    private FreeKickEvent checkFreeKick(Situation sit) {
        FreeKickEvent returnEvent = null;
        return returnEvent;
    }

    private Area around(Point p, int diameter) {
        return new Area(new Ellipse2D.Double(p.getX() - diameter / 2, p.getY() - diameter / 2, (double) diameter, (double) diameter));
    }

    private nl.utwente.ewi.hmi.deira.iam.vvciam.match.Robot determinePossession(Situation sit) {
        nl.utwente.ewi.hmi.deira.iam.vvciam.match.Robot r = null;
        java.util.List<Integer> closeToBall = new ArrayList<Integer>();
        FieldObject ballposition = sit.getBall();
        Area ballArea = around(ballposition, 190);
        for (int j = 0; j < 2; j++) {
            for (int i = 0; i < 5; i++) {
                if (ballArea.contains(sit.getRobot(j, i))) {
                    closeToBall.add(j * 5 + i);
                }
            }
        }
        if (closeToBall.size() == 1) {
            int robotnr = closeToBall.get(0);
            r = match.getTeam(robotnr / 5).getRobot(robotnr % 5);
        } else if (closeToBall.size() > 1) {
            double distance = 2500;
            int closestRobot = 0;
            for (int i = 0; i < closeToBall.size(); i++) {
                int robotnr = closeToBall.get(i);
                FieldObject roboti = sit.getRobot(robotnr / 5, robotnr % 5);
                double distancex = Math.abs(ballposition.getX() - roboti.getX());
                double distancey = Math.abs(ballposition.getY() - roboti.getY());
                double distanceToBall = Math.sqrt(distancex * distancex + distancey * distancey);
                if (distanceToBall < distance) {
                    distance = distanceToBall;
                    closestRobot = robotnr;
                }
            }
            r = match.getTeam(closestRobot / 5).getRobot(closestRobot % 5);
        }
        if (r != null) {
            if (freeBallEvent) {
                freeBallTaken = true;
                freeBallEvent = false;
            } else if (penaltyKickEvent) {
                penaltyKickTaken = true;
                penaltyKickEvent = false;
            } else if (goalKickEvent) {
                goalKickTaken = true;
                goalKickEvent = false;
            }
            if (r.getRobotID() == 0 && shotontarget) {
                keepersave = true;
            } else if (shotontarget && ((match.getTeam(0).contains(lastPossession.getName()) && match.getTeam(1).contains(r.getName())) || (match.getTeam(1).contains(lastPossession.getName()) && match.getTeam(0).contains(r.getName())))) {
                defend = true;
            }
            shotontarget = false;
            ballPossession = true;
        } else {
            ballPossession = false;
        }
        return r;
    }

    /** Geeft de robot die momenteel balbezit heeft.
     *  @return <code>Robot</code> die momenteel balbezit heeft.  Als geen van de Robots balbezit heeft, dan <code>null</code>.
     */
    public nl.utwente.ewi.hmi.deira.iam.vvciam.match.Robot getPossession() {
        return possession;
    }

    /** Geeft de robot die als laatste balbezit had.
     *  @return Laatste <code>Robot</code> met balbezit. Als nog geen van de Robots balbezit heeft gehad, dan <code>null</code>.
     */
    public nl.utwente.ewi.hmi.deira.iam.vvciam.match.Robot getLastPossession() {
        return lastPossession;
    }

    /** Geeft de robot die als 1-na-laatste balbezit had.
     *  @return De 1-na-laatste <code>Robot</code> met balbezit. Als nog geen 2 Robots balbezit hebben gehad, dan <code>null</code>.
     */
    public nl.utwente.ewi.hmi.deira.iam.vvciam.match.Robot getPreLastPossession() {
        return preLastPossession;
    }

    private int incrementEventID() {
        this.eventcounter++;
        return this.eventcounter;
    }

    private VVCEvent transformVSEvent(RSEvent e) {
        VVCEvent returnevent;
        HashMap<String, String> VVCparams = new HashMap<String, String>();
        VVCparams.put("ROBOT1", e.getRobot1());
        VVCparams.put("ROBOT2", e.getRobot2());
        VVCparams.put("TEAM1", e.getTeam1());
        VVCparams.put("TEAM2", e.getTeam2());
        VVCparams.put("SCORE", e.getScore());
        VVCparams.put("LOCATION", e.getScore());
        float importance = (float) (Event.MAX_IMPORTANCE * e.getPriority());
        float decay = 1.0f;
        long time = this.match.getTime() * 1000;
        returnevent = new VVCEvent(eventcounter, time, e.getType(), "", importance, decay, VVCparams);
        return returnevent;
    }

    public nl.utwente.ewi.hmi.deira.mmm.MMM getMmm() {
        return mmm;
    }

    public void setMmm(nl.utwente.ewi.hmi.deira.mmm.MMM mmm) {
        this.mmm = mmm;
    }
}
