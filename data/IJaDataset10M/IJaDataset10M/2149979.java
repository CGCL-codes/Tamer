package ca.tatham.senssim;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.TTCCLayout;
import ca.tatham.senssim.diffusion.AbstractDiffusionNode;
import ca.tatham.senssim.util.Address;

public abstract class Simulation {

    private static Simulation instance;

    protected static final String DIVIDER = "******************************************************";

    private static final char[] PADDING = { ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ' };

    private final Logger log = Logger.getRootLogger();

    private int m_duration;

    private int m_density;

    private AbstractDiffusionNode[][] m_grid;

    private final LinkedList<AbstractDiffusionNode> m_sinks = new LinkedList<AbstractDiffusionNode>();

    private BufferedWriter m_output;

    private int m_totalNodes;

    private int m_tickTime = 0;

    private boolean m_ack = false;

    protected Simulation() {
    }

    public static void log(String msg) {
        instance.log.info(getTime() + " -- " + msg);
    }

    public void suite(SensSimOptions options, String csvFilename) throws IOException {
        File file = new File("SensSim.log");
        if (file.exists()) {
            if (!file.delete()) {
                System.err.println("Could not delete " + file);
                System.exit(1);
            }
        }
        Logger.getRootLogger().addAppender(new FileAppender(new TTCCLayout(), "SensSim.log"));
        BasicConfigurator.configure();
        BufferedWriter csv = new BufferedWriter(new FileWriter(csvFilename));
        for (int gridSize : options.getGridSizes()) {
            setup(options, gridSize);
            Statistics stats = run();
            stats.writeHeader(csv);
            stats.writeData(csv);
        }
        csv.close();
    }

    protected abstract AbstractDiffusionNode[][] createGrid(int gridSize);

    public final void setup(SensSimOptions options, int gridSize) throws IOException {
        m_totalNodes = 0;
        m_sinks.clear();
        m_tickTime = 0;
        m_ack = options.isAck();
        m_duration = options.getDuration();
        File output = new File(options.getOutput());
        File parentFile = output.getParentFile();
        if (parentFile != null && !parentFile.exists()) {
            parentFile.mkdirs();
        }
        m_output = new BufferedWriter(new FileWriter(output));
        m_grid = createGrid(gridSize);
        m_density = options.getDensity();
        instance = this;
    }

    public final Statistics run() throws IOException {
        buildGrid(m_density);
        Statistics stats = new Statistics(m_density);
        log("Simulation starting for " + m_duration + " ticks and " + m_totalNodes + " nodes");
        simulate();
        log("Simulation complete");
        writeByteStats(m_grid);
        writePacketStats(m_grid);
        m_output.close();
        gatherStatistics(stats, m_grid);
        stats.log(log);
        return stats;
    }

    private Statistics gatherStatistics(Statistics stats, AbstractDiffusionNode[][] grid) {
        for (AbstractDiffusionNode[] row : m_grid) {
            for (AbstractDiffusionNode node : row) {
                if (node != null) {
                    stats.gather(node);
                }
            }
        }
        return stats;
    }

    private void simulate() {
        m_tickTime = 0;
        while (m_tickTime < m_duration) {
            for (Link link : Link.s_allLinks) {
                link.tick();
            }
            for (Node[] row : m_grid) {
                for (Node node : row) {
                    if (node != null) {
                        node.tick();
                    }
                }
            }
            if (m_tickTime % 100000 == 0) {
                log("Completed " + m_tickTime + " ticks");
            }
            m_tickTime++;
        }
    }

    public static int getTime() {
        return instance.m_tickTime;
    }

    public static boolean useAcks() {
        return instance.m_ack;
    }

    private void writeByteStats(Node[][] grid) throws IOException {
        try {
            writeln(DIVIDER);
            writeln("*** BYTES RECEIVED/BYTES SENT *** ");
            for (Node[] row : grid) {
                for (Node node : row) {
                    if (node != null) {
                        writeCell((node.isAlive() ? " " : "X") + node.getOpenSymbol() + node.getBytesReceived() + '/' + node.getBytesSent() + node.getCloseSymbol(), 20);
                    } else {
                        writeCell("", 15);
                    }
                }
                writeln();
            }
            writeln(DIVIDER);
        } catch (Exception e) {
            log.error("Could not write byte stats", e);
        }
    }

    protected void writeln() throws IOException {
        m_output.newLine();
    }

    protected void writeln(String line) throws IOException {
        m_output.write(line);
        writeln();
    }

    protected void writeCell(String cell, int width) throws IOException {
        if (cell.length() > width) {
            throw new IllegalArgumentException("Cell is too wide: " + cell);
        }
        m_output.write(cell);
        if (cell.length() < width) {
            m_output.write(PADDING, 0, width - cell.length());
        }
    }

    protected abstract AbstractDiffusionNode createSimpleNode(Address address, int initialPower);

    protected abstract AbstractDiffusionNode createSensorNode(Address address, int initialPower);

    protected abstract AbstractDiffusionNode createSinkNode(Address address);

    /**
   * Build a simple grid, with all nodes in each square in a link
   * 
   * @param density
   * @throws IOException
   */
    private void buildGrid(int density) throws IOException {
        int gridSize = m_grid.length;
        createNodes(gridSize, density);
        createLinks(gridSize);
        m_output.flush();
    }

    private void createLinks(int gridSize) {
        Link[][] xlinks = new Link[gridSize - 1][gridSize - 1];
        Link[][] hlinks = new Link[gridSize][gridSize];
        Link[][] vlinks = new Link[gridSize][gridSize];
        for (int i = 0; i < gridSize - 1; i++) {
            for (int j = 0; j < gridSize - 1; j++) {
                xlinks[i][j] = new Link("X[" + i + "," + j + "]");
                xlinks[i][j].addNode(m_grid[i][j]);
                xlinks[i][j].addNode(m_grid[i + 1][j]);
                xlinks[i][j].addNode(m_grid[i][j + 1]);
                xlinks[i][j].addNode(m_grid[i + 1][j + 1]);
                hlinks[i][j] = new Link("H[" + i + "," + j + "]");
                hlinks[i][j].addNode(m_grid[i][j]);
                hlinks[i][j].addNode(m_grid[i + 1][j]);
                vlinks[j][i] = new Link("V[" + i + "," + j + "]");
                vlinks[j][i].addNode(m_grid[j][i]);
                vlinks[j][i].addNode(m_grid[j][i + 1]);
            }
        }
    }

    private void createNodes(int gridSize, int density) throws IOException {
        Random densityGenerator = new Random();
        double exists = density / 100d;
        for (int i = 0; i < gridSize; i++) {
            m_output.write(i + ": ");
            for (int j = 0; j < gridSize; j++) {
                int power = Integer.MAX_VALUE;
                if (i == gridSize - 1) {
                    m_grid[i][j] = createSensorNode(new Address(10, 0, i, j), power);
                    m_totalNodes++;
                    m_output.write(" $" + i + "," + j + "$ ");
                } else if (i == 0 && j % 2 == 0) {
                    AbstractDiffusionNode sink = createSinkNode(new Address(10, 0, i, j));
                    m_grid[i][j] = sink;
                    m_sinks.add(sink);
                    m_totalNodes++;
                    m_output.write(" *" + i + "," + j + "* ");
                } else {
                    if (densityGenerator.nextDouble() < exists) {
                        m_grid[i][j] = createSimpleNode(new Address(10, 0, i, j), power);
                        m_totalNodes++;
                        m_output.write(" ." + i + "," + j + ". ");
                    }
                }
            }
            m_output.newLine();
        }
    }

    protected void writePacketStats(Node[][] aGrid) throws IOException {
        AbstractDiffusionNode[][] grid = (AbstractDiffusionNode[][]) aGrid;
        writeln(DIVIDER);
        writeSent(grid);
        writeReceived(grid);
        writeln(DIVIDER);
    }

    private void writeSent(AbstractDiffusionNode[][] grid) throws IOException {
        writeln("*** Interest/Data Sent *** ");
        for (AbstractDiffusionNode[] row : grid) {
            for (AbstractDiffusionNode node : row) {
                if (node != null) {
                    writeCell("" + node.getOpenSymbol() + node.getInterestsSent() + '/' + node.getDataSent() + node.getCloseSymbol(), 10);
                } else {
                    writeCell("", 10);
                }
            }
            writeln();
        }
    }

    private void writeReceived(AbstractDiffusionNode[][] grid) throws IOException {
        writeln("*** Interest/Data Received *** ");
        for (AbstractDiffusionNode[] row : grid) {
            for (AbstractDiffusionNode node : row) {
                if (node != null) {
                    writeCell("" + node.getOpenSymbol() + node.getInterestsReceived() + '/' + node.getDataReceived() + node.getCloseSymbol(), 10);
                } else {
                    writeCell("", 10);
                }
            }
            writeln();
        }
    }
}
