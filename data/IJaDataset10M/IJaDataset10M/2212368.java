package net.bervini.rasael.galacticfreedom.game.space.entities;

import net.bervini.rasael.galacticfreedom.game.space.entities.flags.ScriptEntity;
import net.bervini.rasael.galacticfreedom.game.space.entities.weapons.Shot;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import net.bervini.rasael.galacticfreedom.Main;
import net.bervini.rasael.galacticfreedom.game.ai.EntityAI;
import net.bervini.rasael.galacticfreedom.game.ai.Path;
import net.bervini.rasael.galacticfreedom.game.ai.PathGenerator;
import net.bervini.rasael.galacticfreedom.game.missions.Mission;
import net.bervini.rasael.galacticfreedom.game.space.entities.flags.RunnableEntity;
import net.bervini.rasael.galacticfreedom.game.space.entities.flags.TargetableEntity;
import net.bervini.rasael.galacticfreedom.gui.PixelProgressBar;

/**
 *
 * @author Rasael Bervini
 */
public class NPCEntity extends FactionSpaceShip implements ScriptEntity, TargetableEntity, RunnableEntity, EntityAI {

    private int AIState = AI_STATE_IDLE;

    private SpaceEntity targetEntity = null;

    private Path idleOrbitPath;

    private double eggX;

    private double eggY;

    private int shipID;

    private String scriptOnDead = null;

    private String uniqueID;

    public NPCEntity(Main main) {
        super(main);
        lastSonarCheck = System.currentTimeMillis();
    }

    public void attack(SpaceEntity target) {
        setAIState(AI_STATE_ATTACK, target);
    }

    @Override
    public void collide(SpaceEntity spaceEntity) {
        super.collide(spaceEntity);
    }

    public static NPCEntity createNPCEntity(ResultSet rs, Main main) throws SQLException {
        final NPCEntity newNPCEntity = new NPCEntity(main);
        _loadNPCSpaceShip(newNPCEntity, rs, main);
        newNPCEntity.updateFactionsStandings(main);
        newNPCEntity.setMain(main);
        return newNPCEntity;
    }

    int randomRotationDirection = 1;

    public static synchronized void _loadNPCSpaceShip(NPCEntity newNPCEntity, ResultSet rs, Main main) throws SQLException {
        newNPCEntity.setX(rs.getDouble("npcs_x"));
        newNPCEntity.setY(rs.getDouble("npcs_y"));
        newNPCEntity.setCorporationId(rs.getInt("npcs_corporationId"));
        newNPCEntity.setEggX(rs.getInt("npcs_eggX"));
        newNPCEntity.setEggY(rs.getInt("npcs_eggY"));
        Path path = PathGenerator.generateCirclePath(newNPCEntity.getEggX(), newNPCEntity.getEggY(), 500, 0.1);
        newNPCEntity.setIdleOrbitPath(path);
        int shipId = rs.getInt("npcs_shipId");
        String sql = "SELECT * FROM `ships` WHERE `ID` = ? LIMIT 1;";
        PreparedStatement pstmt = main.getConnection().prepareStatement(sql);
        pstmt.setInt(1, shipId);
        ResultSet result = pstmt.executeQuery();
        if (result.next()) {
            _loadSpaceShipData(newNPCEntity, result);
        } else {
            System.out.println("ERROR LOADING THE NPC SPACESHIP!");
        }
        newNPCEntity.setID(rs.getInt("npcs_id"));
        newNPCEntity.setShipID(shipId);
        newNPCEntity.setUniqueID(rs.getString("npcs_uid"));
        if (rs.getInt("npcs_AIState") == EntityAI.AI_STATE_DEAD) {
            newNPCEntity.setAIState(AI_STATE_DEAD);
        }
        newNPCEntity.setScriptOnDead(rs.getString("npcs_scriptOnDead"));
        double totalHP = rs.getInt("npcs_shipHP");
        if (totalHP == -1) {
            totalHP = newNPCEntity.calculateTotalMaximumHitPoints();
        }
        if (totalHP > newNPCEntity.calculateStructureHitPoints()) {
            newNPCEntity.setStructureHitPoints(newNPCEntity.calculateStructureHitPoints());
            totalHP -= newNPCEntity.calculateStructureHitPoints();
        } else {
            newNPCEntity.setStructureHitPoints(totalHP);
            totalHP = 0;
        }
        if (totalHP > newNPCEntity.calculateArmorHitPoints()) {
            newNPCEntity.setArmorHitPoints(newNPCEntity.calculateArmorHitPoints());
            totalHP = totalHP - newNPCEntity.calculateArmorHitPoints();
        } else {
            newNPCEntity.setArmorHitPoints(totalHP);
            totalHP = 0;
        }
        if (totalHP > newNPCEntity.calculateShieldHitPoints()) {
            newNPCEntity.setShieldHitPoints(newNPCEntity.calculateShieldHitPoints());
            totalHP = totalHP - newNPCEntity.calculateShieldHitPoints();
        } else {
            newNPCEntity.setShieldHitPoints(totalHP);
        }
        if (newNPCEntity.getBaseMaximumSpeed() == 0) {
            newNPCEntity.idleType = IDLE_ORBIT_ROTATE_ON_ITSELF;
            if ((int) Math.random() == 0) {
                newNPCEntity.randomRotationDirection = -1;
            }
        }
    }

    @Override
    public void target() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isRunning() {
        return true;
    }

    long lastSonarCheck = 0;

    Point2D targetPoint = null;

    Point2D nextIdlePoint = null;

    int keepAngle = 0;

    public static final int IDLE_ORBIT_ORIGIN = 0;

    public static final int IDLE_ORBIT_ROTATE_ON_ITSELF = 1;

    int idleType = IDLE_ORBIT_ORIGIN;

    @Override
    public void doOperations() {
        switch(getAIState()) {
            case AI_STATE_IDLE:
                switch(idleType) {
                    case IDLE_ORBIT_ORIGIN:
                        setAccelerating(true);
                        if (nextIdlePoint == null) {
                            nextIdlePoint = getIdleOrbitPath().first();
                        }
                        if (getLocation().distance(nextIdlePoint) < 5) {
                            nextIdlePoint = getIdleOrbitPath().next();
                        }
                        targetPoint = nextIdlePoint;
                        double degAngle = PathGenerator.getAngleBetweenPoints(getLocation(), nextIdlePoint) + 90;
                        setFixedFacingAngle(degAngle);
                        int sonarStrenght = 1000;
                        if (System.currentTimeMillis() - lastSonarCheck > 500) {
                            ArrayList<SpaceEntity> sonarEntities = getMain().getSpace().getObjectsInCircleBound(getX(), getY(), sonarStrenght);
                            for (int i = 0; i < sonarEntities.size(); i++) {
                                if (sonarEntities.get(i) instanceof FactionSpaceShip) {
                                    final FactionSpaceShip spaceShip = (FactionSpaceShip) sonarEntities.get(i);
                                    if (isEnemy(spaceShip.getCorporationId())) {
                                        attack(spaceShip);
                                        lastSonarCheck = System.currentTimeMillis();
                                        break;
                                    }
                                }
                            }
                            lastSonarCheck = System.currentTimeMillis();
                        }
                        break;
                    case IDLE_ORBIT_ROTATE_ON_ITSELF:
                        setFixedFacingAngle((double) (getFacingAngle() + randomRotationDirection));
                        int minDistance = 1000;
                        ArrayList<SpaceEntity> nearEntities = getMain().getSpace().getObjectsInCircleBound(getX(), getY(), minDistance);
                        double nearestDistance = Double.POSITIVE_INFINITY;
                        FactionSpaceShip nearestSpaceShip = null;
                        for (int i = 0; i < nearEntities.size(); i++) {
                            if (nearEntities.get(i) instanceof FactionSpaceShip && !(this.equals(nearEntities.get(i)))) {
                                final FactionSpaceShip spaceShip = (FactionSpaceShip) nearEntities.get(i);
                                double distance = getLocation().distance(spaceShip.getLocation());
                                if (Math.min(nearestDistance, distance) == distance) {
                                    nearestDistance = distance;
                                    nearestSpaceShip = spaceShip;
                                }
                            }
                        }
                        if (nearestSpaceShip != null) {
                            setFixedFacingAngle(PathGenerator.getAngleBetweenPoints(getLocation(), nearestSpaceShip.getLocation()) + 90);
                        }
                        sonarStrenght = 1500;
                        if (System.currentTimeMillis() - lastSonarCheck > 500) {
                            ArrayList<SpaceEntity> sonarEntities = getMain().getSpace().getObjectsInCircleBound(getX(), getY(), sonarStrenght);
                            for (int i = 0; i < sonarEntities.size(); i++) {
                                if (sonarEntities.get(i) instanceof FactionSpaceShip) {
                                    final FactionSpaceShip spaceShip = (FactionSpaceShip) sonarEntities.get(i);
                                    if (isEnemy(spaceShip.getCorporationId())) {
                                        System.out.println("Targeting " + spaceShip);
                                        attack(spaceShip);
                                        lastSonarCheck = System.currentTimeMillis();
                                        break;
                                    }
                                }
                            }
                            lastSonarCheck = System.currentTimeMillis();
                        }
                        break;
                }
            case AI_STATE_DEAD:
                break;
            case AI_STATE_ATTACK:
                if (targetEntity == null || targetEntity.isDead()) {
                    setAIState(AI_STATE_IDLE);
                    break;
                }
                Point2D targetLocation = targetEntity.getLocation();
                double degAngle = PathGenerator.getAngleBetweenPoints(getLocation(), targetLocation) + 90;
                int minDistance = Math.max(getWidth(), getHeight()) + 2;
                ArrayList<SpaceEntity> nearEntities = getMain().getSpace().getObjectsInCircleBound(getX(), getY(), minDistance * 2);
                double nearestDistance = Double.POSITIVE_INFINITY;
                FactionSpaceShip nearestSpaceShip = null;
                for (int i = 0; i < nearEntities.size(); i++) {
                    if (nearEntities.get(i) instanceof FactionSpaceShip && !(this.equals(nearEntities.get(i)))) {
                        final FactionSpaceShip spaceShip = (FactionSpaceShip) nearEntities.get(i);
                        double distance = getLocation().distance(spaceShip.getLocation());
                        if (Math.min(nearestDistance, distance) == distance) {
                            nearestDistance = distance;
                            nearestSpaceShip = spaceShip;
                        }
                    }
                }
                if (nearestSpaceShip != null) {
                    int randomDirection = (int) Math.random();
                    if (randomDirection == 0) {
                        randomDirection = -1;
                    }
                    if (nearestDistance < minDistance && getSpeed() > 0 && getWidth() < 100 && getHeight() < 100) {
                        setX(getX() + Math.random() * minDistance);
                        setY(getY() + Math.random() * minDistance);
                        degAngle = (Math.random() * 360);
                    }
                }
                setFixedFacingAngle(degAngle);
                Shot newShot = createShot();
                if (newShot != null) {
                    getMain().getSpace().addObject(newShot);
                }
                double distanceFromEgg = getLocation().distance(getEggX(), getEggY());
                if (distanceFromEgg > 2000) {
                    targetEntity = null;
                    setAIState(AI_STATE_IDLE);
                }
                break;
        }
        super.doOperations();
    }

    public boolean saveStatus() {
        if (getID() == -1) {
            return true;
        }
        try {
            final int totalHP = (int) Math.round(calculateTotalHitpoints());
            String sql = "UPDATE `npcs` SET `npcs_x` = ?,`npcs_y` = ?,`npcs_shipHP` = ?, `npcs_AIState` = ? WHERE `npcs_id` = ?;";
            PreparedStatement pst = getMain().getConnection().prepareStatement(sql);
            pst.setInt(1, getRoundedX());
            pst.setInt(2, getRoundedY());
            pst.setInt(3, totalHP);
            pst.setInt(4, getAIState());
            pst.setInt(5, getID());
            pst.executeUpdate();
            System.out.println("Saved NPC " + getID());
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public void paint(Graphics2D g) {
        super.paint(g);
        final PixelProgressBar ppb = new PixelProgressBar((int) calculateTotalHitpoints(), (int) calculateTotalMaximumHitPoints());
        ppb.setWidth(40);
        ppb.setHeight(5);
        ppb.setLineColor(Color.white);
        final Point onScreen = getMain().toScreen(getLocation());
        ppb.paint(g, onScreen.x - 20, onScreen.y + 5 + (getHeight() / 2));
        if (getMain().isDebuggingMode()) {
            if (targetPoint != null) {
                final Point position = getMain().toScreen(targetPoint);
                g.setColor(new Color(255, 128, 255));
                g.drawLine(onScreen.x, onScreen.y, position.x, position.y);
            }
            final Point eggOnScreen = getMain().toScreen(getEggX(), getEggY());
            g.setColor(Color.lightGray);
            g.drawLine(onScreen.x, onScreen.y, eggOnScreen.x, eggOnScreen.y);
            g.setColor(Color.yellow);
            ArrayList<Point2D> fullPath = idleOrbitPath.getPathPoints();
            for (int i = 0; i < fullPath.size(); i++) {
                final Point position = getMain().toScreen(fullPath.get(i).getX(), fullPath.get(i).getY());
                final int ovalSize = 3;
                final int pointX = position.x - (ovalSize / 2);
                final int pointY = position.y - (ovalSize / 2);
                g.drawOval(pointX, pointY, ovalSize, ovalSize);
                g.drawLine(position.x, position.y, position.x, position.y);
            }
            g.setColor(Color.white);
            if (getAIState() == AI_STATE_ATTACK) {
            }
        }
    }

    @Override
    public int getAIState() {
        return AIState;
    }

    public void setAIState(int AI_STATE) {
        setAIState(AI_STATE, null);
    }

    public void setAIState(int AI_STATE, SpaceEntity targetEntity) {
        AIState = AI_STATE;
        this.targetEntity = targetEntity;
    }

    @Override
    public void die() {
        setAIState(AI_STATE_DEAD);
        if (getID() > 0) {
            saveStatus();
        }
        super.die();
        if (getScriptOnDead() != null) {
            final String missionName = getUniqueID().substring(0, getUniqueID().indexOf("."));
            final Mission mission = getMain().getMission(missionName);
            final StringBuffer script = new StringBuffer(mission.getScript(getScriptOnDead().substring(0, getScriptOnDead().indexOf(".jy"))));
            try {
                getMain().getGameEnvironment().__executeScript(this, script);
                setScriptOnDead(null);
            } catch (Exception e) {
                getMain().showErrorDialog("An error occured while trying to do the OnDead operations of " + getUniqueID() + "!!\n" + e.toString());
                e.printStackTrace();
            }
        } else {
            System.out.println("Is dead without script..");
        }
        final AnimatedEffect explosion = AnimatedEffect.generateRandomExplosion(getX(), getY(), getMain());
        explosion.setFacingAngle((int) (Math.random() * 360));
        getMain().getSpace().addObject(explosion);
        final FloatingSpaceman spaceman = new FloatingSpaceman(getX(), getY(), getMain());
        getMain().getSpace().addObject(spaceman);
        final int maxDebris = 10;
        final int generatedDebris = (int) ((maxDebris / 2) + (Math.random() * (maxDebris / 2)));
        for (int i = 0; i < generatedDebris; i++) {
            final int debNum = i % 3;
            final AnimatedEffect debris = new AnimatedEffect(getX(), getY(), "images/debris-10" + debNum + ".png", 23, 23, 6, getMain());
            debris.setLoop(true);
            debris.setKillAfterTime((int) (Math.random() * 10000));
            debris.setFixedFacingAngle(360 * Math.random());
            debris.setAcceleration(0.4 + Math.round(Math.random() * 10) / 10);
            debris.setSpeed(0.1);
            getMain().getSpace().addObject(debris);
        }
    }

    /**
     * @return the idleOrbitPath
     */
    public Path getIdleOrbitPath() {
        return idleOrbitPath;
    }

    /**
     * @param idleOrbitPath the idleOrbitPath to set
     */
    public void setIdleOrbitPath(Path idleOrbitPath) {
        this.idleOrbitPath = idleOrbitPath;
    }

    /**
     * @return the eggX
     */
    public double getEggX() {
        return eggX;
    }

    /**
     * @param eggX the eggX to set
     */
    public void setEggX(double eggX) {
        this.eggX = eggX;
    }

    /**
     * @return the eggY
     */
    public double getEggY() {
        return eggY;
    }

    /**
     * @param eggY the eggY to set
     */
    public void setEggY(double eggY) {
        this.eggY = eggY;
    }

    /**
     * @return the shipID
     */
    public int getShipID() {
        return shipID;
    }

    /**
     * @param shipID the shipID to set
     */
    public void setShipID(int shipID) {
        this.shipID = shipID;
    }

    /**
     * @return the scriptOnDead
     */
    public String getScriptOnDead() {
        return scriptOnDead;
    }

    /**
     * @param scriptOnDead the scriptOnDead to set
     */
    public void setScriptOnDead(String scriptOnDead) {
        if (scriptOnDead != null) {
            if (scriptOnDead.trim().length() == 0) {
                scriptOnDead = null;
            }
        }
        this.scriptOnDead = scriptOnDead;
    }

    /**
     * @return the uniqueID
     */
    public String getUniqueID() {
        return uniqueID;
    }

    /**
     * @param uniqueID the uniqueID to set
     */
    public void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
    }
}
