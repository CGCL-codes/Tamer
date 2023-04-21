package avrora.sim.radio;

import avrora.sim.Simulation;
import cck.help.HelpCategory;
import cck.util.Options;
import java.io.*;
import java.util.*;

/**
 * handles node positions.
 *
 * @author Olaf Landsiedel
 * @author Rodolfo de Paz
 * @author Daniel Minder
 */
public abstract class Topology extends HelpCategory {

    public static class Position {

        public double x, y, z, rho;

        public Position(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.rho = 0;
        }

        public Position(double x, double y, double z, double rho) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.rho = rho;
        }
    }

    protected final ArrayList positions;

    protected final ArrayList nodes;

    /**
     * new topology
     *
     * @param h the help item for the topology as string
     * @param o the options representing the known and unknown options from the command line
     */
    protected Topology(String h) {
        super("topology", h);
        addSection("TOPOLOGY OVERVIEW", help);
        addOptionSection("Help for the options accepted by this topology is below.", options);
        positions = new ArrayList();
        nodes = new ArrayList();
    }

    public Position getPosition(int id) {
        return ((Position) positions.get(id));
    }

    public void addNode(Simulation.Node node) {
        nodes.add(node);
    }

    public void processOptions(Options o) {
        options.process(o);
    }

    public abstract void start();
}
