package spdrender.net;

import spdrender.parser.SceneParser;
import spdrender.parser.SceneParserException;
import spdrender.raytracer.Scene;
import spdrender.raytracer.LoadBalancer;
import spdrender.raytracer.RayTracer;
import spdrender.FrameBufferWindow;
import java.net.*;
import java.io.*;

/**
 * Implementation of the distributed render server engine.
 * @author Maximiliano Monterrubio Gutierrez
 */
public class RenderHost {

    public static long INIT_SLEEP_TIME = 500;

    private int port;

    private ServerSocket ss;

    private int w, h;

    private LoadBalancer lb;

    private int nodes, registered;

    private int nodeconfig[][];

    private RenderServerThread[] threads;

    private File scene;

    private String errorMsg;

    private boolean error;

    private int[][] segments;

    private double fb[][][];

    private FrameBufferWindow fbw;

    private int pieceCount;

    private long startTime;

    private class RenderServerThread extends Thread {

        private PrintWriter pw;

        private BufferedReader br;

        private RenderHost rh;

        private FileInputStream fis;

        private Socket s;

        private int id;

        private long sleepTime;

        /**
         * Creates a new server thread for managing a single client connection.
         * @param rh The upper class.
         * @param s The socket got from the <code>accept</code> method.
         * @param id A thread identifier.
         * @param nodeconfig The node configuration table to update.
         * @throws java.io.IOException When a network IO Error occurs.
         */
        public RenderServerThread(RenderHost rh, Socket s, int id, int[][] nodeconfig) throws IOException {
            super("RenderServerThread " + id);
            this.rh = rh;
            this.id = id;
            this.s = s;
            this.sleepTime = RenderHost.INIT_SLEEP_TIME;
        }

        @Override
        public void run() {
            try {
                pw = new PrintWriter(s.getOutputStream(), true);
                br = new BufferedReader(new InputStreamReader(s.getInputStream()));
                fis = new FileInputStream(RenderHost.this.scene);
                long fc = RenderHost.this.scene.length();
                pw.println(fc);
                while (fc-- > 0) {
                    s.getOutputStream().write(fis.read());
                }
                String[] clientConfig = br.readLine().split(",");
                int cores = Integer.valueOf(clientConfig[0]);
                int clock = Integer.valueOf(clientConfig[1]);
                rh.registerNode(id, clock, cores);
                while (!rh.nodesReady()) {
                    try {
                        Thread.sleep(sleepTime);
                        sleepTime <<= 1;
                    } catch (InterruptedException e) {
                        System.err.println("Thread interrupted!");
                        break;
                    }
                }
                RenderHost.this.sendWorkLoad(id, pw);
                if (startTime == 0) {
                    startTime = System.currentTimeMillis();
                }
                int pnumber;
                int tp = lb.getPieces().length;
                while (pieceCount < tp) {
                    pnumber = 0;
                    try {
                        pnumber = Integer.valueOf(br.readLine());
                    } catch (NumberFormatException e) {
                        break;
                    }
                    int cols = LoadBalancer.getTileCount(w);
                    int vs = (pnumber / cols) * RayTracer.TILE_SIZE;
                    int ve = (pnumber / cols + 1) * RayTracer.TILE_SIZE;
                    int hs = (pnumber % cols) * RayTracer.TILE_SIZE;
                    int he = (pnumber % cols + 1) * RayTracer.TILE_SIZE;
                    ve = ve > h ? h : ve;
                    he = he > w ? w : he;
                    for (int q = vs; q < ve; ++q) {
                        String[] rowData = br.readLine().split(",");
                        for (int j = hs; j < he; ++j) {
                            String[] pixelData = rowData[j - hs].split("\\|");
                            for (int k = 0; k < 3; ++k) {
                                fb[j][q][k] = Float.valueOf(pixelData[k]);
                            }
                        }
                    }
                    fbw.writeSection(fb, hs, vs, he - hs, ve - vs);
                    pieceCount++;
                }
                long end = System.currentTimeMillis() - startTime;
                fbw.timeLabel.setText(end / 1000 + " secs");
                fbw.bufferUpdater.terminate();
                pw.close();
                br.close();
                s.close();
            } catch (IOException e) {
                rh.errorMsg = e.getMessage();
                rh.error = true;
            }
        }
    }

    /**
     * Creates a new server instance.
     * @param fbw The framebuffer window used to see and save the render.
     * @param scene The scene XML filename to render.
     * @param port The TCP listening port.
     * @param nodes The number of connections (render nodes) to serve.  
     * NOTE:  You MUST connect al the nodes in order to start the render job.
     * @throws java.io.IOException When a IO error occurs in the network or while
     * trying to read the scene file.
     * @throws spdrender.parser.SceneParserException In case the scene file is an invalid XML document.
     */
    public RenderHost(FrameBufferWindow fbw, File scene, int port, int nodes) throws IOException, SceneParserException {
        this.fbw = fbw;
        registered = pieceCount = 0;
        this.scene = scene;
        startTime = 0;
        SceneParser sp = new SceneParser(scene);
        Scene sc = sp.parseScene();
        w = sc.getRenderWidth();
        h = sc.getRenderHeight();
        lb = new LoadBalancer(w, h);
        fb = new double[w][h][3];
        this.port = port;
        ss = new ServerSocket(port);
        this.nodes = nodes;
        nodeconfig = new int[nodes][2];
        threads = new RenderServerThread[nodes];
        error = false;
        errorMsg = null;
        segments = null;
    }

    /**
     * Starts the server.  NOTE:  This method blocks the calling thread to
     * accept the connection.  If you want to avoid thread locking, use an
     * anonymous <code>Runnable</code> and start a thread who executes this
     * method.
     * @throws java.io.IOException In case an IO Error occurs.
     */
    public void startServer() throws IOException {
        int accept = 0;
        while (accept < nodes) {
            Socket s = ss.accept();
            RenderServerThread rst = new RenderServerThread(this, s, accept, nodeconfig);
            threads[accept] = rst;
            rst.start();
            ++accept;
        }
        try {
            for (int i = 0; i < nodes; ++i) {
                threads[i].join();
            }
        } catch (InterruptedException e) {
        }
        ss.close();
    }

    /**
     * Evaluates if all the nodes have connected succesfully to the
     * server.  This predicate is used to start sending workload to each
     * node.
     * @return <code>true</code> in case all the expected nodes are connected.
     */
    public boolean nodesReady() {
        return registered == nodes;
    }

    /** Sends the workload to the specified thread through the 
     * given stream.
     * 
     * @param id Thread id.
     * @param out The output stream of the socket related to the specified thread.
     */
    public void sendWorkLoad(int id, PrintWriter out) {
        int start = segments[id][0];
        int end = segments[id][1];
        int[] pieces = lb.getPieces();
        int i;
        for (i = start; i < end - 1; ++i) {
            out.print(pieces[i] + ",");
        }
        out.println(pieces[i]);
    }

    /**
     * Evalutes if the server saw an error.
     * @return <code>true</code> in case an error occurred.
     */
    public boolean errors() {
        return error;
    }

    /** Retrieves a descriptive error message in case the server detects
     * one.
     * @return An error message, <code>null</code> in case no error occurred.
     */
    public String getErrorMsg() {
        return errorMsg;
    }

    /**
     * Registers a node in the workload table.  
     * @param id Thread id.
     * @param clock Clock speed in Mhz.
     * @param threads Number of threads to execute on that node.
     */
    public void registerNode(int id, int clock, int threads) {
        ++registered;
        nodeconfig[id][0] = clock;
        nodeconfig[id][1] = threads;
        if (registered == nodes) {
            segments = lb.getProportionalSegments(nodeconfig);
        }
    }
}
