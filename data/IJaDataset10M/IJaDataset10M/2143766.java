package net.kylelemons.halo3;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.kylelemons.halo3.GameList.GameType;
import net.kylelemons.halo3.MapList.Map;

/**
 * @author eko
 * 
 */
public class SimpleWebInterface implements Runnable {

    public static final int DEFAULT_PORT = 1880;

    public static final int MAX_CONNECTIONS = 1024;

    public static final String TITLE_FONT_SIZE = "48pt";

    public static final String NAME_FONT_SIZE = "24pt";

    private int m_port;

    private Team[] m_teams;

    private GameType m_game;

    private Map m_map;

    private static Logger logger = Logger.getLogger("net.kylelemons.halo3");

    class doComms implements Runnable {

        private Socket server;

        private String line;

        doComms(Socket server) {
            this.server = server;
        }

        public void run() {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(server.getInputStream()));
                PrintStream out = new PrintStream(server.getOutputStream());
                while ((line = in.readLine()) != null && !line.equals("")) {
                    System.out.println("Headers: " + line);
                }
                out.println("HTTP/1.0 200 GOOD");
                out.println("");
                if (m_game == null) {
                    out.println("<html>");
                    out.println("<body>");
                    out.println("Please wait, no game has been generated.");
                    out.println("</body>");
                    out.println("</html>");
                } else {
                    out.println("<html>");
                    out.println("<head>");
                    out.println("<title>Mobile Halo3 Game</title>");
                    out.println("<meta name='viewport' content='width=480; initial-scale=0.6666; maximum-scale=1.0; minimum-scale=0.6666' />");
                    out.println("<style>");
                    out.println("BODY");
                    out.println("{");
                    out.println("  padding: 0px;");
                    out.println("  margin: 0px;");
                    out.println("  color: white;");
                    out.println("  background: black;");
                    out.println("}");
                    out.println("TABLE");
                    out.println("{");
                    out.println("  width: 100%;");
                    out.println("}");
                    out.println("TH");
                    out.println("{");
                    out.println("  padding: 4px;");
                    out.println("  width: 100%;");
                    out.println("  font-size: " + TITLE_FONT_SIZE + ";");
                    out.println("}");
                    out.println("TD");
                    out.println("{");
                    out.println("  padding: 6px;");
                    out.println("  width: 100%;");
                    out.println("  font-size: " + NAME_FONT_SIZE + ";");
                    out.println("  text-align: center;");
                    out.println("}");
                    for (int i = 0; i < TeamGrid.TeamColors.length; ++i) {
                        String color = TeamGrid.TeamNames[i].toLowerCase();
                        Color bg = TeamGrid.TeamColors[i];
                        Color fg = TeamGrid.TeamText[i];
                        out.println("TH." + color);
                        out.println("{");
                        out.println("  color: " + getWebColor(bg) + ";");
                        out.println("  background: " + getWebColor(fg) + ";");
                        out.println("}");
                        out.println("TD." + color);
                        out.println("{");
                        out.println("  color: " + getWebColor(fg) + ";");
                        out.println("  background: " + getWebColor(bg) + ";");
                        out.println("}");
                    }
                    out.println("TH.whiteonblack");
                    out.println("{");
                    out.println("  color: " + getWebColor(Color.WHITE) + ";");
                    out.println("  background: " + getWebColor(Color.BLACK) + ";");
                    out.println("}");
                    out.println("</style>");
                    out.println("</head>");
                    out.println("<body>");
                    out.println("<table id=\"gametable\">");
                    out.println("<tr><th class=\"whiteonblack\">" + m_game + " on " + m_map + "</th></tr>");
                    for (int t = 0; t < m_teams.length; ++t) {
                        String color = TeamGrid.TeamNames[t].toLowerCase();
                        out.println("<!-- Team #" + (t + 1) + ": " + color + " -->");
                        Team team = m_teams[t];
                        ArrayList<String> members = team.getMembers();
                        out.println("<tr><th class=\"" + color + "\">" + team.getTeamName() + "</th></tr>");
                        for (int i = 0; i < team.size(); ++i) out.println("<tr><td class=\"" + color + "\">" + members.get(i) + "</th></tr>");
                    }
                    out.println("</table>");
                    out.println("</body>");
                    out.println("</html>");
                }
                server.close();
            } catch (IOException ioe) {
                logger.log(Level.WARNING, "IOException on socket listen", ioe);
            }
        }

        /**
     * @param fg
     * @return
     */
        private String getWebColor(Color fg) {
            String hex;
            String red = Integer.toString(fg.getRed(), 16);
            while (red.length() < 2) red = "0" + red;
            String green = Integer.toString(fg.getGreen(), 16);
            while (green.length() < 2) green = "0" + green;
            String blue = Integer.toString(fg.getBlue(), 16);
            while (blue.length() < 2) blue = "0" + blue;
            hex = "#" + red + green + blue;
            return hex;
        }
    }

    SimpleWebInterface(int port) {
        m_port = port;
    }

    SimpleWebInterface() {
        this(DEFAULT_PORT);
    }

    public void run() {
        int i = 0;
        try {
            ServerSocket listener = new ServerSocket(m_port);
            Socket server;
            while ((i++ < MAX_CONNECTIONS) || (MAX_CONNECTIONS == 0)) {
                server = listener.accept();
                doComms conn_c = new doComms(server);
                Thread t = new Thread(conn_c);
                t.start();
            }
        } catch (BindException e) {
            logger.warning("Could not bind webserver socket (probably already in use)");
        } catch (IOException ioe) {
            logger.log(Level.WARNING, "IOException on socket listen", ioe);
        }
    }

    public void setGame(Team[] teams, GameType game, Map map) {
        m_teams = teams;
        m_game = game;
        m_map = map;
    }
}
