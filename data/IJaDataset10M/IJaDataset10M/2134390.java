package UniKrak;

import UniKrak.Config.*;
import UniKrak.Graph.*;
import UniKrak.Gui.*;
import UniKrak.Gui.GuiPlugins.*;
import UniKrak.Lang.*;
import UniKrak.Pathfind.*;
import UniKrak.Position.*;
import UniKrak.Position.Ethernet.*;
import UniKrak.Position.WiFi.*;
import UniKrak.SVG.*;
import UniKrak.Search.*;
import java.io.*;
import java.util.*;
import java.awt.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.text.DateFormat;
import org.w3c.dom.svg.SVGDocument;

public class UniKrak extends HttpServlet {

    private HttpServletRequest request;

    private HttpServletResponse response;

    private PrintWriter out;

    private Cookie cookies[];

    private Cookie cookie;

    private DateFormat dateformat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);

    private Hashtable persistSessions = new Hashtable();

    private Pathfinder pf = new Pathfinder();

    private Searcher searcher;

    private String[][] words;

    private Graph g = null;

    private LangWrapper lw;

    private SessionData session = null;

    private EthernetFinder ethernetfinder = new EthernetFinder("http://harley.kom.auc.dk/unikrak/lookup.php?ip=");

    private boolean successfulInit = false;

    private SVGDocument[] maps = null;

    private String msg = "";

    private GuiInterface gui = new StandardGui();

    private Configuration config;

    public void init() {
        System.out.println("-- STARTING UNIKRAK SERVICE --");
        java.net.URL marker = getClass().getResource("UniKrak.class");
        if (marker == null) {
            System.out.println("Could not find marker file, which should be in the servlet root dir");
        }
        String url = marker.getPath().replaceAll("%20", " ");
        for (int i = 0; i < 4; i++) {
            int kp = url.lastIndexOf("/");
            if (kp == -1) break;
            url = url.substring(0, kp);
        }
        String UniKrakPath = url + "/";
        System.out.println("UniKrak Base path: " + UniKrakPath);
        System.out.println("Loading configuration file: " + UniKrakPath + "UniKrak.properties");
        Configuration.setConfigFile(UniKrakPath + "UniKrak.properties");
        config = Configuration.getInstance();
        config.appPath = UniKrakPath;
        try {
            System.out.println("Setting up languages");
            lw = new LangWrapper();
            words = lw.parseDir(config.appPath + config.langfilesPath);
            System.out.println("Language loading succedded");
        } catch (Exception ex) {
            System.out.println("Language loading failed");
            ex.printStackTrace();
        }
        try {
            System.out.println("Setting up graph");
            g = Graph.loadGraphText(config.appPath + config.graphName);
            System.out.println("Graph loading succedded");
        } catch (Exception ex) {
            System.out.println("Graph loading failed");
            ex.printStackTrace();
        }
        try {
            System.out.println("Setting up searching");
            searcher = new Searcher(g);
            System.out.println("Search loading succedded");
        } catch (Exception ex) {
            System.out.println("Search loading failed");
            ex.printStackTrace();
        }
        try {
            if (lw != null && words != null && g != null && searcher != null) {
                successfulInit = true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (successfulInit) System.out.println("Init successful - UniKrak is ready"); else destroy();
        System.gc();
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.request = request;
        this.response = response;
        RequestData parameters;
        response.setDateHeader("Expires", -1);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=-1");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache, no-store");
        response.setContentType("text/html;charset=UTF-8");
        parameters = new RequestData(request);
        PositionMethod oldMethod;
        if (parameters.trackerClient) {
            processTrackerRequest(parameters);
        } else {
            out = response.getWriter();
            cookies = request.getCookies();
            cookie = null;
            if (cookies != null) {
                for (int i = 0; i < cookies.length; i++) {
                    if (cookies[i].getName().equals("UniKrak")) {
                        cookie = cookies[i];
                    }
                }
            }
            if (cookie == null) {
                UUID temp = UUID.randomUUID();
                cookie = new Cookie("UniKrak", temp.toString());
                cookie.setMaxAge(17280000);
                response.addCookie(cookie);
            }
            session = null;
            String kill;
            if ((kill = request.getParameter("killSession")) != null && kill.equalsIgnoreCase("true")) {
                session = new SessionData(cookie.getValue());
            }
            if (session == null && cookie != null) {
                session = (SessionData) persistSessions.get(cookie.getValue());
            }
            if (session == null) {
                session = new SessionData(cookie.getValue());
            }
            session.appPath = "http://" + request.getLocalName() + ((request.getLocalPort() == 80) ? "" : (":" + request.getLocalPort())) + request.getContextPath() + "/UniKrak";
            if (session.firstVisit) {
                session.mapX = config.defaultX;
                session.mapY = config.defaultY;
                session.mapZoom = config.defaultZoom;
                session.floor = config.defaultFloor;
                session.updateMap = true;
            }
            session.lastPage = session.currentPage;
            session.currentPage = parameters.page;
            session.resetVolatile();
            if (session.wifiPositionUpdated) session.updatePosition = 1;
            if (parameters.page != -1) session.page = parameters.page;
            if (parameters.mapX != -1) session.mapX = parameters.mapX;
            if (parameters.mapY != -1) session.mapY = parameters.mapY;
            if (parameters.mapZoom != -1) {
                session.mapZoom = parameters.mapZoom;
                session.updateMap = true;
            }
            if (parameters.floor != -1) {
                session.floor = parameters.floor;
                session.updateMap = true;
            }
            if (parameters.resetPosition) {
                session.userPosition = null;
                session.userPositionMethod = PositionMethod.MANUAL;
                session.updatePosition = -1;
            }
            if (parameters.search != null) {
                if (parameters.search.equals("")) {
                    session.message = getWord(26);
                } else {
                    session.searchString = parameters.search;
                    session.latestResults = searcher.search(session.searchString);
                }
            }
            if (parameters.getBookmark != -1) {
                if (parameters.getBookmark >= 0 && parameters.getBookmark < session.userBookmarks.size()) session.selectedBookmark = parameters.getBookmark; else if (parameters.getBookmark == -2) session.selectedBookmark = -1;
            }
            if (parameters.deleteBookmark != -1) {
                if (parameters.deleteBookmark >= 0 && parameters.deleteBookmark < session.userBookmarks.size()) session.userBookmarks.remove(parameters.deleteBookmark);
                session.selectedBookmark = -1;
            }
            if (parameters.setLang != null) {
                for (int i = 0; i < words.length; i++) {
                    if (words[i][0].equals(parameters.setLang)) {
                        session.currentLanguage = i;
                        session.updateLanguage = true;
                        break;
                    }
                }
            }
            if (parameters.posManual != null) {
                if (session.userPositionMethod == PositionMethod.MANUAL) {
                    if (!parameters.posManual.equals("")) {
                        int[] tmp = g.findNodes(parameters.posManual, 1, false);
                        if (tmp != null) {
                            session.userPosition = new Position(g, tmp[0]);
                            session.updatePosition = 1;
                            session.lastMethod = session.userPositionMethod;
                        } else session.message = getWord(1) + " " + parameters.posString + " " + getWord(2) + ".";
                    }
                }
            }
            oldMethod = session.userPositionMethod;
            if (parameters.posMethod != null) {
                if (parameters.posMethod.equals("wifi")) {
                    if (session.userPositionMethod != PositionMethod.WIFI) session.lastMethod = session.userPositionMethod;
                    session.userPositionMethod = PositionMethod.WIFI;
                    session.showWifiInstructions = true;
                } else if (parameters.posMethod.equals("ethernet")) {
                    if (session.userPositionMethod != PositionMethod.ETHERNET) session.lastMethod = session.userPositionMethod;
                    session.userPositionMethod = PositionMethod.ETHERNET;
                    session.wifiCouldNotFindPos = false;
                    session.WifiRequested = false;
                } else if (parameters.posMethod.equals("manual")) {
                    if (session.userPositionMethod != PositionMethod.MANUAL) session.lastMethod = session.userPositionMethod;
                    session.userPositionMethod = PositionMethod.MANUAL;
                    session.wifiCouldNotFindPos = false;
                    session.WifiRequested = false;
                }
            }
            if (parameters.posIndex != -1) {
                if (parameters.posIndex >= 0 && parameters.posIndex < g.nodeList.length) {
                    session.userPosition = new Position(g, parameters.posIndex);
                    session.userPositionMethod = PositionMethod.MANUAL;
                    session.updatePosition = 1;
                    session.lastMethod = session.userPositionMethod;
                }
            }
            if (parameters.posCoords != null) {
                if (session.lastMap != null) {
                    try {
                        float nearestDist = 1e10f;
                        int nearestNode = 0;
                        float currentDist;
                        double a, b;
                        double fzoom = 4.0f * (double) Math.exp(-0.405f * (double) session.mapZoom);
                        double svgtopngRatio = 1000.0f * fzoom / session.lastMap.getRootElement().getWidth().getBaseVal().getValue();
                        double ax = parameters.posCoords[0] / svgtopngRatio;
                        double ay = parameters.posCoords[1] / svgtopngRatio;
                        for (int i = 0; i < g.nodeList.length; i++) {
                            if (g.nodeList[i].floor == session.floor) {
                                a = g.nodeList[i].x - ax;
                                b = g.nodeList[i].y - ay;
                                currentDist = (float) Math.sqrt(a * a + b * b);
                                if (currentDist < nearestDist) {
                                    nearestDist = currentDist;
                                    nearestNode = i;
                                }
                            }
                        }
                        session.userPosition = new Position(g, nearestNode);
                        session.updatePosition = 1;
                        session.userPositionMethod = PositionMethod.MANUAL;
                        session.lastMethod = session.userPositionMethod;
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
            if (parameters.resetDestination) {
                session.destination = null;
                session.updatePosition = -1;
            }
            if (parameters.destString != null) {
                if (!parameters.destString.equals("")) {
                    int[] tmp = g.findNodes(parameters.destString, 1, false);
                    if (tmp != null) {
                        session.destination = new Position(g, tmp[0]);
                        session.updatePosition = 2;
                    } else session.message = getWord(1) + " " + parameters.destString + " " + getWord(2) + ".";
                }
            }
            if (parameters.destIndex != -1) {
                if (parameters.destIndex >= 0 && parameters.destIndex < g.nodeList.length && g.nodeList[parameters.destIndex].type == 1) {
                    session.destination = new Position(g, parameters.destIndex);
                    session.updatePosition = 2;
                }
            }
            if (parameters.destCoords != null) {
                if (session.lastMap != null) {
                    try {
                        float nearestDist = 1e10f;
                        int nearestNode = 0;
                        float currentDist;
                        double a, b;
                        double fzoom = 4.0f * (double) Math.exp(-0.405f * (double) session.mapZoom);
                        double svgtopngRatio = 1000.0f * fzoom / session.lastMap.getRootElement().getWidth().getBaseVal().getValue();
                        double ax = parameters.destCoords[0] / svgtopngRatio;
                        double ay = parameters.destCoords[1] / svgtopngRatio;
                        for (int i = 0; i < g.nodeList.length; i++) {
                            if (g.nodeList[i].floor == session.floor) {
                                a = g.nodeList[i].x - ax;
                                b = g.nodeList[i].y - ay;
                                currentDist = (float) Math.sqrt(a * a + b * b);
                                if (currentDist < nearestDist) {
                                    nearestDist = currentDist;
                                    nearestNode = i;
                                }
                            }
                        }
                        session.destination = new Position(g, nearestNode);
                        session.updatePosition = 2;
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
            if (parameters.showIndex != -1) {
                if (parameters.showIndex == -2) session.showRoom = null; else if (parameters.showIndex >= 0) session.showRoom = new Position(g, parameters.showIndex);
                session.updatePosition = 4;
            }
            if (parameters.viaString != null) {
                if (!parameters.viaString.equals("")) {
                    int[] tmp = g.findNodes(parameters.viaString, 1, false);
                    if (tmp != null) {
                        if (session.viaPoints == null) session.viaPoints = new ArrayList();
                        session.viaPoints.add(new Position(g, tmp[0]));
                        session.updatePosition = 3;
                    } else session.message = getWord(1) + " " + parameters.viaString + " " + getWord(2) + ".";
                }
            }
            if (parameters.deleteVia != -1) {
                if (parameters.deleteVia >= 0 && parameters.deleteVia < session.viaPoints.size()) {
                    session.viaPoints.remove(parameters.deleteVia);
                    session.updatePosition = -1;
                }
            }
            if (parameters.deleteAllVias) {
                session.viaPoints.clear();
                session.updatePosition = -1;
            }
            if (parameters.viaIndex != -1) {
                if (parameters.viaIndex >= 0 && parameters.viaIndex < g.nodeList.length && g.nodeList[parameters.viaIndex].type == 1) {
                    if (session.viaPoints == null) session.viaPoints = new ArrayList();
                    session.viaPoints.add(new Position(g, parameters.viaIndex));
                    session.updatePosition = 3;
                }
            }
            if (parameters.upVia != -1) {
                if (parameters.upVia > 0 && parameters.upVia < session.viaPoints.size()) {
                    Position p0 = session.viaPoints.get(parameters.upVia - 1);
                    session.viaPoints.set(parameters.upVia - 1, session.viaPoints.get(parameters.upVia));
                    session.viaPoints.set(parameters.upVia, p0);
                }
            }
            if (parameters.downVia != -1) {
                if (parameters.downVia >= 0 && parameters.downVia < session.viaPoints.size() - 1) {
                    Position p0 = session.viaPoints.get(parameters.downVia + 1);
                    session.viaPoints.set(parameters.downVia + 1, session.viaPoints.get(parameters.downVia));
                    session.viaPoints.set(parameters.downVia, p0);
                }
            }
            if (parameters.pathType != -1) {
                session.pathType = parameters.pathType;
                session.updatePath = true;
            } else if (session.updatePosition > 0 && session.pathType < 2) {
                if (session.userPosition != null && session.destination != null) session.pathType = 1; else session.pathType = -1;
                session.updatePath = true;
            }
            if (parameters.bmValue != -1 && parameters.bmName == null) {
                if (session.latestResults != null) {
                    if (parameters.bmValue >= 0 && parameters.bmValue < session.latestResults.length) session.savingBookMark = parameters.bmValue;
                }
            }
            if (parameters.bmName != null && parameters.bmValue != -1) {
                if (parameters.bmName.length() > 0 && parameters.bmValue >= 0 && parameters.bmValue < session.latestResults.length) {
                    session.userBookmarks.add(new Bookmark(parameters.bmName, session.latestResults[parameters.bmValue], 3));
                    session.message = getWord(52);
                    session.savingBookMark = -1;
                }
            }
            if (parameters.warpCoords != null && parameters.warpCoords.length == 2) {
                session.warp = parameters.warpCoords;
            }
            if (session.userPositionMethod == PositionMethod.ETHERNET) {
                String ip = request.getRemoteAddr();
                String room = ethernetfinder.findRoom(ip);
                if (room != null) {
                    int[] roomId = g.findNodes(room, 1, true);
                    if (roomId != null && roomId.length > 0 && roomId[0] != -1) {
                        session.userPosition = new Position(g, roomId[0]);
                        session.updatePosition = 1;
                        session.lastMethod = session.userPositionMethod;
                    } else {
                        session.message = getWord(3) + "." + ((session.userPosition == null) ? "" : " " + getWord(4));
                        session.userPositionMethod = oldMethod;
                    }
                } else {
                    session.message = getWord(3) + "." + ((session.userPosition == null) ? "" : " " + getWord(4));
                    session.userPositionMethod = oldMethod;
                }
            } else if (session.userPositionMethod == PositionMethod.WIFI) {
                if (parameters.instrCancel) {
                    session.userPositionMethod = session.lastMethod;
                    session.showWifiInstructions = false;
                } else if (parameters.instrOK) {
                    if (session.wifiPositionUpdated) {
                        session.showWifiInstructions = false;
                        session.WifiRequested = true;
                    } else {
                        if (session.wifiCouldNotFindPos) {
                            session.message = getWord(5);
                            session.userPositionMethod = session.lastMethod;
                            session.showWifiInstructions = false;
                        } else {
                            session.WifiMessage = getWord(70);
                            session.WifiRequested = true;
                        }
                    }
                }
                if (session.wifiPositionUpdated) {
                    session.wifiPositionChanged = true;
                    session.showWifiInstructions = false;
                }
            }
            if (session.showRoom != null) {
                boolean found = false;
                if (session.latestResults != null) {
                    for (int i = 0; i < session.latestResults.length; i++) {
                        try {
                            if (session.latestResults[i].room.index == session.showRoom.getNodeId()) {
                                found = true;
                                break;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                if (session.userBookmarks != null) {
                    Iterator<Bookmark> bmIter = session.userBookmarks.iterator();
                    while (bmIter.hasNext()) {
                        Bookmark b = bmIter.next();
                        if (b.data != null && b.data.room != null && b.data.room.index == session.showRoom.getNodeId()) {
                            found = true;
                            break;
                        }
                    }
                }
                if (!found) session.showRoom = null;
            }
            if (session.userPosition != null && !session.userPosition.equals(session.lastPosition)) {
                session.lastPosition = session.userPosition;
                session.updatePosition = 1;
            }
            if (parameters.floor == -1 && session.updatePosition > 0) {
                switch(session.updatePosition) {
                    case 1:
                        if (session.userPosition != null) session.floor = g.nodeList[session.userPosition.getNodeId()].floor;
                        break;
                    case 2:
                        if (session.destination != null && session.userPosition == null) session.floor = g.nodeList[session.destination.getNodeId()].floor;
                        break;
                    case 3:
                        break;
                    case 4:
                        if (session.showRoom != null) session.floor = g.nodeList[session.showRoom.getNodeId()].floor;
                        break;
                }
            }
            findPaths();
            out.print(gui.drawMainPage(session, g, words));
            session.firstVisit = false;
            out.close();
        }
        if (cookie != null && session != null) {
            persistSessions.put(cookie.getValue(), session);
        }
    }

    private void processTrackerRequest(RequestData parameters) {
        if (parameters.trackerUUID != null) {
            System.out.println("Got positioning data on UUID: " + parameters.trackerUUID);
            int method = 0;
            int acceptableNodeTypes = -1;
            String acceptableSSIDs = "AAU";
            session = (SessionData) persistSessions.get(parameters.trackerUUID);
            if (session != null && session.userPositionMethod == PositionMethod.WIFI) {
                try {
                    int numAPs = Integer.parseInt(request.getParameter("numAPs"));
                    System.out.println("got data about " + numAPs + " access points");
                    if (numAPs > 0) {
                        AccessPoint[] nearAPs = new AccessPoint[numAPs];
                        Position[] locations = new Position[numAPs];
                        int bestRssi = -Integer.MAX_VALUE;
                        int bestNode = 0;
                        for (int i = 0; i < numAPs; i++) {
                            String bssid = request.getParameter("apid" + i);
                            String ssid = request.getParameter("apssid" + i);
                            int rssi = Integer.parseInt(request.getParameter("aprssi" + i));
                            if (acceptableSSIDs == null || ssid.equals(acceptableSSIDs)) {
                                if (rssi > bestRssi) {
                                    bestRssi = rssi;
                                    bestNode = i;
                                }
                            }
                            nearAPs[i] = new AccessPoint(bssid, ssid, rssi);
                        }
                        int[] res;
                        switch(method) {
                            case 0:
                                boolean foundRoom = false;
                                res = g.findNodes(nearAPs[bestNode].getBSSID(), 2, true);
                                System.out.println("lala" + res.length);
                                if (res != null && res.length > 0) {
                                    for (int j = 0; j < g.nodeList[res[0]].edges.length; j++) {
                                        if (Math.abs(g.edgeList[g.nodeList[res[0]].edges[j]].cost) < 0.1) {
                                            int n = g.getOtherNode(res[0], g.nodeList[res[0]].edges[j]);
                                            if (g.nodeList[n].type == acceptableNodeTypes || acceptableNodeTypes < 0) {
                                                session.userPosition = new Position(g, n);
                                                session.wifiPositionUpdated = true;
                                                foundRoom = true;
                                                System.out.println("hep");
                                                break;
                                            }
                                        }
                                    }
                                    if (!foundRoom) {
                                        int n = g.findClosestNode(g.nodeList[res[0]].x, g.nodeList[res[0]].y, acceptableNodeTypes, g.nodeList[res[0]].floor);
                                        if (session.userPosition.getNodeId() == n) session.wifiPositionChanged = false; else session.wifiPositionChanged = true;
                                        session.userPosition = new Position(g, n);
                                        session.wifiPositionUpdated = true;
                                    }
                                }
                                break;
                            case 1:
                                double xSum = 0.0, ySum = 0.0;
                                int count = 0;
                                int floorSum = 0;
                                for (int i = 0; i < nearAPs.length; i++) {
                                    res = g.findNodes(nearAPs[i].getBSSID(), 2, true);
                                    if (res != null && res.length > 0) {
                                        xSum += g.nodeList[res[0]].x;
                                        ySum += g.nodeList[res[0]].y;
                                        floorSum += g.nodeList[res[0]].floor;
                                        count++;
                                    }
                                }
                                double xMean = xSum / count;
                                double yMean = ySum / count;
                                double fMean = ((double) floorSum) / count;
                                int floor = (int) Math.round(fMean);
                                int n = g.findClosestNode(xMean, yMean, acceptableNodeTypes, floor);
                                session.userPosition = new Position(g, n);
                                session.wifiPositionUpdated = true;
                                break;
                            case 2:
                                double[] weights = new double[nearAPs.length];
                                double[] I = new double[nearAPs.length];
                                double Isum = 0.0;
                                double alpha = 2.0;
                                double r0 = 1.0;
                                double p0 = -15;
                                for (int i = 0; i < nearAPs.length; i++) {
                                    double p = 3;
                                    double r = r0 * Math.pow(10.0, (p0 - p) / (10.0 * alpha));
                                    I[i] = 1.0 / r;
                                    Isum += I[i];
                                }
                                for (int i = 0; i < I.length; i++) {
                                    weights[i] = I[i] / Isum;
                                }
                                Vertex userPos = new Vertex(0.0, 0.0);
                                boolean found = false;
                                double floorD = 0.0;
                                for (int i = 0; i < nearAPs.length; i++) {
                                    res = g.findNodes(nearAPs[i].getBSSID(), 2, true);
                                    if (res != null && res.length > 0) {
                                        userPos.add(new Vertex(g.nodeList[res[0]].x * weights[i], g.nodeList[res[0]].y * weights[i]));
                                        floorD += g.nodeList[res[0]].floor * weights[i];
                                        found = true;
                                    }
                                }
                                floor = (int) Math.round(floorD);
                                if (found) {
                                    int node = g.findClosestNode(userPos.x, userPos.y, 1, floor);
                                    if (session.userPosition.getNodeId() == node) session.wifiPositionChanged = false; else session.wifiPositionChanged = true;
                                    session.userPosition = new Position(g, node);
                                    session.wifiPositionUpdated = true;
                                }
                                break;
                        }
                        persistSessions.put(parameters.trackerUUID, session);
                    } else {
                        session.wifiCouldNotFindPos = true;
                    }
                } catch (Exception ex) {
                    session.wifiCouldNotFindPos = true;
                    ex.printStackTrace();
                }
            }
        }
    }

    String getWord(int index) {
        if (words == null) return "&lt;No languages loaded&gt;";
        if (words[session.currentLanguage] == null) return "&lt;Language missing&gt;";
        if (index >= words[session.currentLanguage].length) return "&lt;String missing&gt;";
        return words[session.currentLanguage][index];
    }

    private void findPaths() {
        if (session.updatePosition != 0 || session.updatePath) {
            switch(session.pathType) {
                case -1:
                    if (session.updatePosition != 0) {
                        session.paths = null;
                    }
                    break;
                case 1:
                    if (session.userPosition != null && session.destination != null) {
                        session.paths = new PathDisplay(g.getCoordinates(pf.findSimpleRoute(g, session.userPosition.getNodeId(), session.destination.getNodeId())));
                        session.updateMap = true;
                    } else {
                        session.paths = null;
                    }
                    break;
                case 2:
                    if (session.userPosition != null && session.destination != null) {
                        int[] waypoints;
                        if (session.viaPoints == null) waypoints = new int[2]; else waypoints = new int[session.viaPoints.size() + 2];
                        waypoints[0] = session.userPosition.getNodeId();
                        if (session.viaPoints != null && session.viaPoints.size() > 0) {
                            Iterator<Position> vIter = session.viaPoints.iterator();
                            for (int i = 1; vIter.hasNext() && i < waypoints.length - 1; i++) {
                                waypoints[i] = vIter.next().getNodeId();
                            }
                        }
                        waypoints[waypoints.length - 1] = session.destination.getNodeId();
                        session.paths = new PathDisplay(g.getCoordinates(pf.findSequenceRoute(g, waypoints)));
                        session.updateMap = true;
                    } else {
                        session.paths = null;
                    }
                    break;
                case 3:
                    if (session.userPosition != null && session.destination != null) {
                        int[] waypoints;
                        if (session.viaPoints == null) waypoints = new int[2]; else waypoints = new int[session.viaPoints.size() + 2];
                        waypoints[0] = session.userPosition.getNodeId();
                        if (session.viaPoints != null && session.viaPoints.size() > 0) {
                            Iterator<Position> vIter = session.viaPoints.iterator();
                            for (int i = 1; vIter.hasNext() && i < waypoints.length - 1; i++) {
                                waypoints[i] = vIter.next().getNodeId();
                            }
                        }
                        waypoints[waypoints.length - 1] = session.destination.getNodeId();
                        session.paths = new PathDisplay(g.getCoordinates(pf.findComplexRoute(g, waypoints)));
                        session.updateMap = true;
                    } else {
                        session.paths = null;
                    }
                    break;
            }
        }
    }

    private int renderMap(Vertex pos) {
        int pathType = 0;
        CoordSet[][] path = null;
        int x = config.defaultX;
        int y = config.defaultY;
        int zoom = config.defaultZoom;
        String tmp, tmp2, tmp3;
        boolean selectPosition = false;
        boolean newFloor = false;
        String tmpPathRoom = null;
        SVGDocument map = null;
        int floor = 1;
        int picno = session.picno;
        try {
            map = null;
            SVGRender svgrender = SVGRender.getInstance();
            if (maps == null) {
                maps = new SVGDocument[config.highestFloor - config.lowestFloor + 1];
                for (int i = 0; i < maps.length; i++) maps[i] = svgrender.getMap(i);
            }
            map = maps[session.floor - 1];
            if (map == null) {
                map = svgrender.getMap(0);
            }
            if (map == null) {
                System.out.println("SEVERE ERROR: Map is NULL =:O\nI'll just lie down and die");
                return -1;
            }
            x = session.mapX;
            y = session.mapY;
            zoom = session.mapZoom;
            float fzoom = 4.0f * (float) Math.exp(-0.405f * (float) zoom);
            float svgtopngRatio = 1000.0f * fzoom / map.getRootElement().getWidth().getBaseVal().getValue();
            if (session.updatePosition != 0 || session.updateMap || session.firstVisit || session.paths == null || !session.paths.mapUpdated) {
                System.out.println("Rendering new map");
                int currentFloor;
                picno = (picno + 1) % 10;
                session.picno = picno;
                int mFloor = session.floor;
                svgrender.cleanMap(map);
                if (session.paths != null) {
                    switch(session.paths.pathType) {
                        case 0:
                            break;
                        case 1:
                            if (session.paths.simplePath != null) {
                                CoordSet[] subpath;
                                currentFloor = mFloor;
                                int subPathStart = 0;
                                for (int i = 0; i < session.paths.simplePath.length; i++) {
                                    if (session.paths.simplePath[i].floor != currentFloor || i == session.paths.simplePath.length - 1) {
                                        subpath = new CoordSet[i - subPathStart + 1];
                                        for (int j = subPathStart; j <= i; j++) {
                                            subpath[j - subPathStart] = session.paths.simplePath[j];
                                        }
                                        svgrender.addRoute(map, subpath, config.svg_path_color, (currentFloor == mFloor));
                                        subPathStart = i;
                                        currentFloor = session.paths.simplePath[i].floor;
                                    }
                                }
                            }
                            break;
                        case 2:
                        case 3:
                            if (session.paths.complexPath != null) {
                                for (int p = 0; p < session.paths.complexPath.length; p++) {
                                    CoordSet[] subpath;
                                    currentFloor = mFloor;
                                    int subPathStart = 0;
                                    for (int i = 0; i < session.paths.complexPath[p].length; i++) {
                                        if (session.paths.complexPath[p][i].floor != currentFloor || i == session.paths.complexPath[p].length - 1) {
                                            subpath = new CoordSet[i - subPathStart + 1];
                                            for (int j = subPathStart; j <= i; j++) {
                                                subpath[j - subPathStart] = session.paths.complexPath[p][j];
                                            }
                                            svgrender.addRoute(map, subpath, config.svg_path_color, (currentFloor == mFloor));
                                            subPathStart = i;
                                            currentFloor = session.paths.complexPath[p][i].floor;
                                        }
                                    }
                                }
                            }
                            break;
                    }
                    session.paths.mapUpdated = true;
                }
                if (session.userPosition != null) {
                    Node n = g.nodeList[session.userPosition.getNodeId()];
                    svgrender.addCircle(map, (int) n.x, (int) n.y, config.svg_start_color, (n.floor == mFloor));
                    svgrender.addText(map, (int) n.x, (int) n.y, java.awt.Color.BLACK, "Floor " + n.floor);
                }
                if (session.destination != null) {
                    Node n = g.nodeList[session.destination.getNodeId()];
                    svgrender.addCircle(map, (int) n.x, (int) n.y, config.svg_end_color, (n.floor == mFloor));
                    svgrender.addText(map, (int) n.x, (int) n.y, java.awt.Color.BLACK, "Floor " + n.floor);
                }
                if (session.showRoom != null) {
                    Node n = g.nodeList[session.showRoom.getNodeId()];
                    svgrender.addCircle(map, (int) n.x, (int) n.y, config.svg_show_color, (n.floor == mFloor));
                    svgrender.addText(map, (int) n.x, (int) n.y, java.awt.Color.BLACK, "Floor " + n.floor);
                }
                if (session.viaPoints != null && session.viaPoints.size() > 0) {
                    Iterator<Position> pIter = session.viaPoints.iterator();
                    while (pIter.hasNext()) {
                        Position p = pIter.next();
                        if (p != null) {
                            svgrender.addCircle(map, (int) g.nodeList[p.getNodeId()].x, (int) g.nodeList[p.getNodeId()].y, config.svg_via_color, (g.nodeList[p.getNodeId()].floor == mFloor));
                        }
                    }
                }
                svgrender.renderMap(config.appPath + config.rendermapPath + cookie.getValue() + "-" + Integer.toString(picno) + ".png", map, zoom, new Rectangle(1000, 1000, 500, 500));
                session.svgx = x;
                session.svgy = y;
                session.svgzoom = zoom;
                session.lastMap = map;
            }
            int mapX = (int) (svgtopngRatio * (float) x);
            int mapY = (int) (svgtopngRatio * (float) y);
            float opacity = 0.5f;
            FileWriter f = new FileWriter(config.appPath + config.rendermapPath + cookie.getValue() + "-" + Integer.toString(picno) + ".html");
            f.write("<html>\n<head>\n");
            f.write("<script language='JavaScript'>");
            f.write("posArrayX=new Array();");
            f.write("posArrayY=new Array();");
            f.write("typeArray=new Array();");
            int index = 0;
            if (session.userPosition != null && session.userPosition.getNodeId() != -1) {
                f.write("posArrayX[" + index + "]=" + (svgtopngRatio * session.userPosition.getCoordSet(g).x) + ";\n");
                f.write("posArrayY[" + index + "]=" + (svgtopngRatio * session.userPosition.getCoordSet(g).y) + ";\n");
                f.write("typeArray[" + index + "]=1;\n");
                index++;
            }
            if (session.destination != null && session.destination.getNodeId() != -1) {
                f.write("posArrayX[" + index + "]=" + (svgtopngRatio * session.destination.getCoordSet(g).x) + ";\n");
                f.write("posArrayY[" + index + "]=" + (svgtopngRatio * session.destination.getCoordSet(g).y) + ";\n");
                f.write("typeArray[" + index + "]=2;\n");
                index++;
            }
            if (session.viaPoints != null && session.viaPoints.size() > 0) {
                Iterator<Position> pIter = session.viaPoints.iterator();
                while (pIter.hasNext()) {
                    Position p = pIter.next();
                    if (p != null) {
                        f.write("posArrayX[" + index + "]=" + (svgtopngRatio * p.getCoordSet(g).x) + ";\n");
                        f.write("posArrayY[" + index + "]=" + (svgtopngRatio * p.getCoordSet(g).y) + ";\n");
                        f.write("typeArray[" + index + "]=3;\n");
                        index++;
                    }
                }
            }
            f.write("</script>\n");
            f.write("<script language='JavaScript' src='mapnav.js'></script>\n");
            f.write("<meta http-equiv='cache-control' content='no-cache'>\n<meta http-equiv='pragma' content='no-cache'>\n<meta http-equiv='expires' content='0'>");
            f.write("</head>\n");
            if (pos == null) f.write("<body onLoad=\"init(document.getElementById(\'map\'), document.getElementById(\'box\'), document.getElementById(\'tooltip\'), document.getElementById(\'dragPoint\'), posArrayX, posArrayY, typeArray);\" onUnload=\"exit(e);\">\n"); else f.write("<body onLoad=\"init(document.getElementById(\'map\'), document.getElementById(\'box\'), document.getElementById(\'tooltip\'), document.getElementById(\'dragPoint\'), posArrayX, posArrayY, typeArray); mapWarp(" + (pos.x) + "," + (pos.y) + ")\" onUnload=\"exit(e);\">\n");
            f.write("<img id=\"map\" src=\"" + cookie.getValue() + "-" + Integer.toString(picno) + ".png\" style=\"position:absolute;left:0;top:0;cursor:pointer\">\n");
            f.write("<div id=\"box\" style=\"position:absolute;top:0;left:0;width:100;height:100;border: 1px solid red;visibility:hidden\"></div>\n");
            f.write("<div id=\"tooltip\" style=\"position:absolute; top:0; left:0; width:200;font-family:sans-serif; font-size:12; border:1px solid black; visibility:hidden;\"><div style=\"position:absolute;top:0;left:0;width:200;height:100%;background-color:#aaccff; opacity:" + opacity + ";filter: alpha(opacity=" + (int) (opacity * 100) + "); -moz-opacity: " + opacity + "\"></div>" + getWord(34) + "<br>" + getWord(38) + "</div>\n");
            f.write("<img id=\"dragPoint\" src=\"\" width=\"9\" height=\"9\" style=\"position:absolute;visibility:hidden;\">");
            f.write("</body></html>\n");
            f.flush();
            f.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return picno;
    }

    /** Handles the HTTP <code>GET</code> method.
   * @param request servlet request
   * @param response servlet response
   */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
        System.gc();
    }

    /** Handles the HTTP <code>POST</code> method.
   * @param request servlet request
   * @param response servlet response
   */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
        System.gc();
    }

    /** Returns a short description of the servlet.
   */
    public String getServletInfo() {
        return "Short description";
    }
}
