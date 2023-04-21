package net.virtualinfinity.atrobots.compiler;

import net.virtualinfinity.atrobots.computer.HardwareBus;
import net.virtualinfinity.atrobots.hardware.armor.Armor;
import net.virtualinfinity.atrobots.hardware.mines.MineLayer;
import net.virtualinfinity.atrobots.hardware.missiles.MissileLauncher;
import net.virtualinfinity.atrobots.hardware.radio.Transceiver;
import net.virtualinfinity.atrobots.hardware.scanning.radar.Radar;
import net.virtualinfinity.atrobots.hardware.scanning.scanner.Scanner;
import net.virtualinfinity.atrobots.hardware.scanning.sonar.Sonar;
import net.virtualinfinity.atrobots.hardware.shield.Shield;
import net.virtualinfinity.atrobots.hardware.throttle.Throttle;
import net.virtualinfinity.atrobots.hardware.transponder.Transponder;
import net.virtualinfinity.atrobots.hardware.turret.Turret;
import java.util.Map;

/**
 * A factory for various configurable hardware components.
 *
 * @author Daniel Pitts
 */
public class HardwareSpecification {

    private final Map<String, Integer> configs;

    public static final String SCANNER = "scanner";

    public static final String WEAPON = "weapon";

    public static final String ARMOR = "armor";

    public static final String ENGINE = "engine";

    public static final String HEATSINKS = "heatsinks";

    public static final String MINES = "mines";

    public static final String SHIELD = "shield";

    public HardwareSpecification(Map<String, Integer> configs) {
        this.configs = configs;
    }

    public Armor createArmor() {
        return new Armor(chooseFor(ARMOR, 50, 66, 100, 120, 130, 150));
    }

    public MineLayer createMineLayer() {
        return new MineLayer(chooseFor(MINES, 2, 4, 6, 10, 16, 24));
    }

    public Radar createRadar() {
        return new Radar();
    }

    public Shield createShield() {
        return new Shield(chooseFor(SHIELD, 1.0, 1.0, 1.0, 2.0 / 3, 1.0 / 2, 1.0 / 3));
    }

    public Sonar createSonar() {
        return new Sonar();
    }

    public Transceiver createTransceiver() {
        return new Transceiver();
    }

    public Transponder createTransponder() {
        return new Transponder();
    }

    public Turret createTurret() {
        return new Turret();
    }

    private Scanner createScanner() {
        return new Scanner(chooseFor(SCANNER, 250, 350, 500, 700, 1000, 1500));
    }

    private Throttle createThrottle() {
        return new Throttle(chooseFor(ENGINE, 0.5, 0.8, 1.0, 1.12, 1.35, 1.50) * chooseFor(ARMOR, 1.33, 1.20, 1.00, 0.85, 0.75, 0.66));
    }

    private <T> T chooseFor(String name, T... values) {
        return values[Math.max(0, Math.min(configs.get(name), values.length))];
    }

    /**
     * Configure the specific hardware context based on this spec.
     *
     * @param robotConfigurer the context to configure.
     */
    public void buildRobotConfigurer(RobotConfigurer robotConfigurer) {
        robotConfigurer.setThrottle(createThrottle());
        robotConfigurer.setCoolMultiplier(chooseFor(HEATSINKS, 0.75, 1.00, 1.125, 1.25, 1.33, 1.50));
        robotConfigurer.setArmor(createArmor());
        robotConfigurer.setMineLayer(createMineLayer());
        robotConfigurer.setRadar(createRadar());
        robotConfigurer.setShield(createShield());
        robotConfigurer.setSonar(createSonar());
        robotConfigurer.setTransceiver(createTransceiver());
        robotConfigurer.setTransponder(createTransponder());
        robotConfigurer.setTurret(createTurret());
        robotConfigurer.setMissileLauncher(new MissileLauncher());
        robotConfigurer.setMissileLauncherPower(chooseFor(WEAPON, .5, .8, 1.0, 1.2, 1.35, 1.5));
        robotConfigurer.setScanner(createScanner());
        robotConfigurer.setHardwareBus(new HardwareBus());
    }
}
