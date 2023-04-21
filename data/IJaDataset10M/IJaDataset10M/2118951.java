package net.virtualinfinity.atrobots.robot;

import net.virtualinfinity.atrobots.ArenaObjectVisitor;
import net.virtualinfinity.atrobots.arena.*;
import net.virtualinfinity.atrobots.arenaobjects.DamageInflicter;
import net.virtualinfinity.atrobots.computer.*;
import net.virtualinfinity.atrobots.hardware.HasHeading;
import net.virtualinfinity.atrobots.hardware.HasOverburner;
import net.virtualinfinity.atrobots.hardware.armor.Armor;
import net.virtualinfinity.atrobots.hardware.armor.ArmorDepletionListener;
import net.virtualinfinity.atrobots.hardware.heatsinks.HeatSinks;
import net.virtualinfinity.atrobots.hardware.mines.MineLayer;
import net.virtualinfinity.atrobots.hardware.missiles.Missile;
import net.virtualinfinity.atrobots.hardware.missiles.MissileFactory;
import net.virtualinfinity.atrobots.hardware.radio.Transceiver;
import net.virtualinfinity.atrobots.hardware.scanning.ScanResult;
import net.virtualinfinity.atrobots.hardware.scanning.ScanSource;
import net.virtualinfinity.atrobots.hardware.scanning.radar.Radar;
import net.virtualinfinity.atrobots.hardware.scanning.sonar.Sonar;
import net.virtualinfinity.atrobots.hardware.shield.Shield;
import net.virtualinfinity.atrobots.hardware.throttle.Throttle;
import net.virtualinfinity.atrobots.hardware.transponder.Transponder;
import net.virtualinfinity.atrobots.hardware.turret.Turret;
import net.virtualinfinity.atrobots.interrupts.Destructable;
import net.virtualinfinity.atrobots.measures.*;
import net.virtualinfinity.atrobots.ports.PortHandler;
import net.virtualinfinity.atrobots.snapshots.ArenaObjectSnapshot;
import net.virtualinfinity.atrobots.snapshots.RobotSnapshot;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Daniel Pitts
 */
public class Robot extends TangibleArenaObject implements Resettable, HasHeading, Destructable, HasOverburner, MissileFactory, ArmorDepletionListener, ScanSource, DamageInflicter {

    private final HeatSinks heatSinks = new HeatSinks();

    private final Odometer odometer = new Odometer();

    private final String name;

    private final int id;

    private final RobotScore score;

    private Throttle throttle;

    private Computer computer;

    private Turret turret;

    private Transponder transponder;

    private Transceiver transceiver;

    private Duration lastDamageGiven = Duration.fromCycles(0);

    private Duration lastDamageTaken = Duration.fromCycles(0);

    private Armor armor;

    private Radar radar;

    private Sonar sonar;

    private Temperature shutdownLevel;

    private MineLayer mineLayer;

    private Shield shield;

    private boolean overburn;

    private HardwareBus hardwareBus;

    private static final RelativeAngle STEERING_SPEED = RelativeAngle.fromBygrees(8);

    private final Position oldPosition = new Position();

    private int roundKills;

    private final List<RobotListener> robotListeners = new ArrayList<RobotListener>();

    private final Heading desiredHeading = new Heading(heading.getAngle());

    {
        position.setOdometer(odometer);
    }

    public Robot(String name, int id, RobotScore score) {
        this.name = name;
        this.id = id;
        this.score = score;
        this.roundKills = 0;
    }

    public void setComputer(Computer computer) {
        this.computer = computer;
        computer.setId(getId());
        computer.setName(getName());
    }

    public Computer getComputer() {
        return computer;
    }

    public void destruct() {
        getArmor().destruct();
    }

    public Turret getTurret() {
        return turret;
    }

    public void setOverburn(boolean overburn) {
        this.overburn = overburn;
    }

    public Transponder getTransponder() {
        return transponder;
    }

    public Speed getSpeed() {
        return speed;
    }

    public Duration getLastDamageGiven() {
        return lastDamageGiven;
    }

    public Duration getLastDamageTaken() {
        return lastDamageTaken;
    }

    public Transceiver getTransceiver() {
        return transceiver;
    }

    public Odometer getOdometer() {
        return odometer;
    }

    public Throttle getThrottle() {
        return throttle;
    }

    public HeatSinks getHeatSinks() {
        return heatSinks;
    }

    public Heading getHeading() {
        return heading;
    }

    public PortHandler getTurretOffsetSensor() {
        return new PortHandler() {

            public short read() {
                return (short) getTurretShift();
            }
        };
    }

    public int getTurretShift() {
        return getTurret().getHeading().getAngle().getAngleCounterClockwiseTo(getHeading().getAngle()).getBygrees();
    }

    public Armor getArmor() {
        return armor;
    }

    public Radar getRadar() {
        return radar;
    }

    public PortHandler getAimTurretPort() {
        return new PortHandler() {

            public void write(short value) {
                setTurretOffset(RelativeAngle.fromBygrees(value));
            }
        };
    }

    private void setTurretOffset(RelativeAngle angle) {
        getTurret().getHeading().setAngle(getHeading().getAngle().counterClockwise(angle));
    }

    public Heading getDesiredHeading() {
        return desiredHeading;
    }

    public Sonar getSonar() {
        return sonar;
    }

    public PortHandler getOverburnLatchPort() {
        return new PortHandler() {

            public short read() {
                return (short) (isOverburn() ? 1 : 0);
            }

            public void write(short value) {
                setOverburn(value != 0);
            }
        };
    }

    public boolean isOverburn() {
        return overburn;
    }

    public PortHandler getShutdownLevelLatchPort() {
        return new PortHandler() {

            public short read() {
                return (short) hardwareBus.getShutdownLevel();
            }

            public void write(short value) {
                hardwareBus.setShutdownLevel(value);
            }
        };
    }

    public MineLayer getMineLayer() {
        return mineLayer;
    }

    public Shield getShield() {
        return shield;
    }

    public void setTurret(Turret turret) {
        this.turret = turret;
    }

    public void setTransponder(Transponder transponder) {
        this.transponder = transponder;
    }

    public void setTransceiver(Transceiver transceiver) {
        this.transceiver = transceiver;
    }

    public void setArmor(Armor armor) {
        this.armor = armor;
        armor.setArmorDepletionListener(this);
    }

    public void setRadar(Radar radar) {
        this.radar = radar;
    }

    public void setSonar(Sonar sonar) {
        this.sonar = sonar;
    }

    public void setMineLayer(MineLayer mineLayer) {
        this.mineLayer = mineLayer;
    }

    public void setShield(Shield shield) {
        this.shield = shield;
    }

    public HardwareBus getHardwareBus() {
        return hardwareBus;
    }

    public void setHardwareBus(HardwareBus hardwareBus) {
        this.hardwareBus = hardwareBus;
    }

    public void reset() {
        setOverburn(false);
    }

    public ScanResult scan(AngleBracket angleBracket, double maxDistance, boolean calculateAccuracy, boolean includeTargetDetails) {
        final RobotScanResult scanResult = doScan(angleBracket, maxDistance, calculateAccuracy);
        if (scanResult.successful()) {
            getComputer().getRegisters().getTargetId().set((short) scanResult.getMatch().transponder.getId());
            if (includeTargetDetails) {
                final AbsoluteAngle matchAngle = scanResult.getMatch().getHeading().getAngle();
                final AbsoluteAngle turretAngle = getTurret().getHeading().getAngle();
                getComputer().getRegisters().getTargetHeading().set((short) matchAngle.getAngleCounterClockwiseTo(turretAngle).getBygrees());
                getComputer().getRegisters().getTargetThrottle().set((short) scanResult.getMatch().getThrottle().getPower());
                getComputer().getRegisters().getTargetVelocity().set((short) Math.round(scanResult.getMatch().getSpeed().times(Duration.ONE_CYCLE) * 100));
            }
        }
        return scanResult;
    }

    private RobotScanResult doScan(AngleBracket angleBracket, double maxDistance, boolean calculateAccuracy) {
        Position position = getPosition();
        final RobotScanner robotScanner = new RobotScanner(this, position, angleBracket, maxDistance, calculateAccuracy);
        getArena().visitActiveRobots(robotScanner);
        final RobotScanResult scanResult = robotScanner.toScanResult();
        final Scan object = new Scan(angleBracket, maxDistance, scanResult.successful(), scanResult.getMatchPositionVector(), calculateAccuracy && scanResult.successful(), scanResult.getAccuracy());
        getArena().addIntangible(object);
        object.getPosition().copyFrom(position);
        return scanResult;
    }

    protected ArenaObjectSnapshot createSpecificSnapshot() {
        final RobotSnapshot robotSnapshot = new RobotSnapshot();
        robotSnapshot.setTemperature(getHeatSinks().getTemperature());
        robotSnapshot.setArmor(getArmor().getRemaining());
        robotSnapshot.setOverburn(isOverburn());
        robotSnapshot.setActiveShield(getShield().isActive());
        robotSnapshot.setHeading(getHeading().getAngle());
        robotSnapshot.setTurretHeading(getTurret().getHeading().getAngle());
        robotSnapshot.setName(getName());
        robotSnapshot.setId(getId());
        robotSnapshot.setRoundKills(getRoundKills());
        robotSnapshot.setTotalKills(getTotalKills());
        robotSnapshot.setTotalDeaths(getTotalDeaths());
        robotSnapshot.setTotalWins(getTotalWins());
        robotSnapshot.setTotalTies(getTotalTies());
        robotSnapshot.setLastMessage(getComputer().getLastMessage());
        return robotSnapshot;
    }

    @Override
    public void checkCollision(TangibleArenaObject robot) {
        if (robot.getPosition().getVectorTo(position).getMagnitudeSquared() < 64) {
            collides();
            robot.collides();
        }
    }

    public void collides() {
        position.copyFrom(oldPosition);
        if (speed.times(Duration.ONE_CYCLE) > 2) {
            armor.inflictDamage(1);
        }
        throttle.setPower(0);
        throttle.setDesiredPower(0);
        computer.getRegisters().getCollisionCount().increment();
    }

    public void inflictDamage(DamageInflicter cause, double damageAmount) {
        if (!isDead()) {
            final double unabsorbedAmount = shield.absorbDamage(damageAmount);
            if (unabsorbedAmount > 0) {
                lastDamageTaken = getArena().getRoundTimer().getTime();
                cause.inflictedDamage(unabsorbedAmount);
            }
            armor.inflictDamage(unabsorbedAmount);
            if (isDead()) {
                cause.killedRobot();
            }
        }
    }

    public void armorDepleted() {
        if (!isDead()) {
            for (RobotListener listener : robotListeners) {
                listener.died(this);
            }
            die();
            getArena().explosion(this, new LinearDamageFunction(position, isOverburn() ? 1.3 : 1, 25.0));
        }
    }

    public void update(Duration duration) {
        oldPosition.copyFrom(position);
        super.update(duration);
        getThrottle().update(duration);
        getHeading().moveToward(getDesiredHeading(), STEERING_SPEED);
        getComputer().update(duration);
        if (heatSinks.getTemperature().getLogScale() >= 500) {
            destruct();
        } else if (heatSinks.getTemperature().getLogScale() >= 475) {
            armor.inflictDamage(duration.getCycles() / 4d);
        } else if (heatSinks.getTemperature().getLogScale() >= 450) {
            armor.inflictDamage(duration.getCycles() / 8d);
        } else if (heatSinks.getTemperature().getLogScale() >= 400) {
            armor.inflictDamage(duration.getCycles() / 16d);
        } else if (heatSinks.getTemperature().getLogScale() >= 350) {
            armor.inflictDamage(duration.getCycles() / 32d);
        } else if (heatSinks.getTemperature().getLogScale() >= 300) {
            armor.inflictDamage(duration.getCycles() / 64d);
        }
        heatSinks.cool(isOverburn() ? getCoolTemp(duration).times(0.66) : getCoolTemp(duration));
        shield.update(duration);
        if (position.getX() < 4 || position.getX() > 1000 - 4 || position.getY() < 4 || position.getY() > 1000 - 4) {
            collides();
        }
    }

    private Temperature getCoolTemp(Duration duration) {
        return Temperature.fromLogScale(duration.getCycles() * 1.125);
    }

    public void setThrottle(Throttle throttle) {
        this.throttle = throttle;
        throttle.setSpeed(speed);
        throttle.setHeatSinks(heatSinks);
    }

    public void winRound() {
        for (RobotListener listener : robotListeners) {
            listener.wonRound(this);
        }
    }

    public void tieRound() {
        for (RobotListener listener : robotListeners) {
            listener.tiedRound(this);
        }
    }

    public void killedRobot() {
        for (RobotListener listener : robotListeners) {
            listener.killedRobot(this);
        }
    }

    public void inflictedDamage(double amount) {
        lastDamageGiven = getArena().getRoundTimer().getTime();
        for (RobotListener listener : robotListeners) {
            listener.inflictedDamage(this, amount);
        }
    }

    public String getName() {
        return name;
    }

    public int getTotalKills() {
        return score.getTotalKills();
    }

    public int getRoundKills() {
        return roundKills;
    }

    public int getTotalDeaths() {
        return score.getTotalDeaths();
    }

    public int getId() {
        return id;
    }

    public int getTotalWins() {
        return score.getTotalWins();
    }

    public int getTotalTies() {
        return score.getTotalTies();
    }

    public void addRobotListener(RobotListener robotListener) {
        robotListeners.add(robotListener);
    }

    @Override
    protected void arenaConnected(Arena arena) {
        transceiver.setRadioDispatcher(arena.getRadioDispatcher());
    }

    public InterruptHandler createGetRobotInfoInterruptHandler(MemoryCell speed, MemoryCell lastDamageTaken, MemoryCell lastDamageGiven) {
        return new GetRobotInfoInterrupt(speed, lastDamageTaken, lastDamageGiven);
    }

    public GetRobotStatisticsInterrupt createGetRobotStatisticsInterrupt(MemoryCell totalKills, MemoryCell roundKills, MemoryCell totalDeaths) {
        return new GetRobotStatisticsInterrupt(totalKills, roundKills, totalDeaths);
    }

    public Missile createMissile(AbsoluteAngle heading, Position position, double power) {
        return new Missile(this, position, heading, power, this.isOverburn());
    }

    public void accept(ArenaObjectVisitor arenaObjectVisitor) {
        arenaObjectVisitor.visit(this);
    }

    /**
     * @author Daniel Pitts
     */
    public class GetRobotInfoInterrupt extends InterruptHandler {

        private final MemoryCell speed;

        private final MemoryCell lastDamageTaken;

        private final MemoryCell lastDamageGiven;

        private GetRobotInfoInterrupt(MemoryCell speed, MemoryCell lastDamageTaken, MemoryCell lastDamageGiven) {
            this.speed = speed;
            this.lastDamageTaken = lastDamageTaken;
            this.lastDamageGiven = lastDamageGiven;
        }

        public void handleInterrupt() {
            speed.set((short) Math.round(getSpeed().times(Duration.ONE_CYCLE) * 100));
            lastDamageGiven.set((short) getLastDamageGiven().getCycles());
            lastDamageTaken.set((short) getLastDamageTaken().getCycles());
        }
    }

    /**
     * @author Daniel Pitts
     */
    public class GetRobotStatisticsInterrupt extends InterruptHandler {

        private final MemoryCell totalKills;

        private final MemoryCell roundKills;

        private final MemoryCell totalDeaths;

        private GetRobotStatisticsInterrupt(MemoryCell totalKills, MemoryCell roundKills, MemoryCell totalDeaths) {
            this.totalKills = totalKills;
            this.roundKills = roundKills;
            this.totalDeaths = totalDeaths;
        }

        public void handleInterrupt() {
            totalKills.set((short) getTotalKills());
            roundKills.set((short) getRoundKills());
            totalDeaths.set((short) getTotalDeaths());
        }
    }
}
