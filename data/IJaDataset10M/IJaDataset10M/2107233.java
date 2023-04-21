package ai.computerAI.computerAIs;

import java.awt.Font;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import javax.media.opengl.GLAutoDrawable;
import com.sun.opengl.util.j2d.TextRenderer;
import sgEngine.SGEngine;
import utilities.Location;
import world.World;
import world.owner.Owner;
import world.unit.Unit;
import ai.computerAI.ComputerAI;

public final class CrusherAI extends ComputerAI {

    boolean drawUI = false;

    ArrayList<Location> buildLocations;

    Location attackPoint;

    boolean attackingMovingTarget = false;

    int maxWorkers = 2;

    int workers;

    int maxLightTanks = 999;

    int lightTanks;

    int maxsctpPlants = 3;

    int sctpPlants;

    int maxRefineries = 3;

    int refineries;

    int maxFactories = 4;

    int factories;

    int maxEnergyDepots = 1;

    int energyDepots;

    int maxMetalDepots = 1;

    int metalDepots;

    TextRenderer tr;

    public CrusherAI(Owner o, World w, SGEngine sge) {
        super(o, w, sge);
        Font font = new Font("SansSerif", Font.BOLD, 12);
        tr = new TextRenderer(font, true, false);
    }

    public void performAIFunctions() {
        determineState();
        LinkedList<Unit> units = getUnits();
        Iterator<Unit> i = units.iterator();
        while (i.hasNext()) {
            Unit u = i.next();
            if (u.getAction().getName().equals("idle")) {
                if (u.is("worker")) {
                    orderWorker(u);
                } else if (u.is("factory")) {
                    orderFactory(u);
                } else if (u.is("light tank")) {
                    orderLightTank(u);
                }
            }
        }
    }

    /**
	 * orders the light tanks
	 * @param u
	 */
    private void orderLightTank(Unit u) {
        if (attackPoint != null && lightTanks > 50) {
            if (attackingMovingTarget) {
                forceMoveUnit(u, getRegionLocation(attackPoint, 300, 300));
            } else {
                moveUnit(u, getRegionLocation(attackPoint, 100, 100));
            }
        } else if (buildLocations.size() > 0) {
            int index = (int) (Math.random() * buildLocations.size());
            forceMoveUnit(u, getRegionLocation(buildLocations.get(index), 200, 200));
        }
    }

    /**
	 * gives the factories commands
	 * @param u the factory
	 */
    private void orderFactory(Unit u) {
        if (workers < maxWorkers) {
            buildAt("worker", u, u.getLocation());
            workers++;
        } else if (lightTanks < maxLightTanks) {
            buildAt("light tank", u, u.getLocation());
            lightTanks++;
        }
        if (o.getEnergy() / o.getEnergyMax() < .33) {
            u.setOnline(false);
            maxsctpPlants += 2;
            maxRefineries += 2;
            maxEnergyDepots++;
            maxMetalDepots++;
        } else {
            u.setOnline(true);
        }
    }

    /**
	 * gives the worker commands
	 * @param u the worker
	 * @param buildLocations
	 */
    private void orderWorker(Unit u) {
        if (buildLocations.size() > 0) {
            int index = (int) (Math.random() * buildLocations.size());
            Location l = getRegionLocation(buildLocations.get(index), 100, 100);
            if (factories < maxFactories && sctpPlants != 0 && refineries != 0) {
                buildAt("factory", u, l);
                factories++;
            } else if (sctpPlants < maxsctpPlants && refineries != 0) {
                buildAt("sctp plant", u, l);
                sctpPlants++;
            } else if (refineries < maxRefineries) {
                buildAt("refinery", u, l);
                refineries++;
            } else if (energyDepots < maxEnergyDepots) {
                buildAt("energy depot", u, l);
                energyDepots++;
            } else if (metalDepots < maxMetalDepots) {
                buildAt("metal depot", u, l);
                metalDepots++;
            } else {
                maxFactories++;
                maxEnergyDepots++;
                maxMetalDepots++;
                orderWorker(u);
            }
        } else {
            buildAt("sctp plant", u, u.getLocation());
            sctpPlants++;
        }
    }

    /**
	 * determines the game state, counts its units, determines build locations, etc
	 */
    private void determineState() {
        workers = 0;
        lightTanks = 0;
        sctpPlants = 0;
        refineries = 0;
        factories = 0;
        energyDepots = 0;
        metalDepots = 0;
        ArrayList<Location> al = new ArrayList<Location>();
        LinkedList<Unit> units = getUnits();
        Iterator<Unit> i = units.iterator();
        while (i.hasNext()) {
            Unit u = i.next();
            if (u.getMovement() == 0) {
                al.add(u.getLocation());
            }
            if (u.is("factory")) {
                factories++;
            } else if (u.is("worker")) {
                workers++;
            } else if (u.is("light tank")) {
                lightTanks++;
            } else if (u.is("sctp plant")) {
                sctpPlants++;
            } else if (u.is("refinery")) {
                refineries++;
            } else if (u.is("energy depot")) {
                energyDepots++;
            } else if (u.is("metal depot")) {
                metalDepots++;
            }
        }
        buildLocations = al;
        attackPoint = null;
        attackingMovingTarget = false;
        Location secondaryAttack = null;
        Iterator<String> it = getEnemyUnits().keySet().iterator();
        while (it.hasNext() && attackPoint == null) {
            i = getEnemyUnits().get(it.next()).iterator();
            while (i.hasNext() && attackPoint == null) {
                Unit u = i.next();
                if (u.getMovement() == 0) {
                    attackPoint = u.getLocation();
                } else if (secondaryAttack == null) {
                    secondaryAttack = u.getLocation();
                }
            }
        }
        if (attackPoint == null) {
            attackPoint = secondaryAttack;
            if (attackPoint != null) {
                attackingMovingTarget = true;
            }
        }
    }

    /**
	 * determines a random location somewhere inside the specified region
	 * @param center the center of the region
	 * @param width the width of the region
	 * @param depth the depth of the region
	 * @return returns a location
	 */
    private Location getRegionLocation(Location center, double width, double depth) {
        return new Location(center.x + Math.random() * width - width / 2, 0, center.z + Math.random() * depth - depth / 2);
    }

    public void drawUI(GLAutoDrawable d) {
        if (drawUI) {
            tr.beginRendering((int) d.getWidth(), (int) d.getHeight());
            tr.setColor(255, 0, 0, 255);
            tr.draw("Energy: " + o.getEnergy(), (int) (d.getWidth() - 110), (int) (d.getHeight() - 20));
            tr.draw("Metal: " + o.getMetal(), (int) (d.getWidth() - 110), (int) (d.getHeight() - 40));
            tr.endRendering();
        }
    }
}
