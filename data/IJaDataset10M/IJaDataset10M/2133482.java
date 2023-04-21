package net.sf.sageplugins.webserver;

import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.sageplugins.sageutils.SageApi;

/**
 * @author Owner
 *
 */
public class FavoriteCommandServlet extends SageServlet {

    /**
     * 
     */
    private static final long serialVersionUID = 7208810244349974154L;

    Vector<String> commands = new Vector<String>();

    /**
	 * 
	 */
    public FavoriteCommandServlet() {
        commands.add("Add");
        commands.add("Remove");
        commands.add("Update");
        commands.add("CreatePriority");
        commands.add("IncreasePriority");
        commands.add("DecreasePriority");
    }

    protected void doServletGet(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String command = req.getParameter("command");
        if ((command == null) || (command.length() == 0)) {
            htmlHeaders(resp);
            noCacheHeaders(resp);
            PrintWriter out = resp.getWriter();
            xhtmlHeaders(out);
            out.println("<head>");
            jsCssImport(req, out);
            out.println("<title>InternalCommand</title></head>");
            out.println("<body>");
            printTitle(out, "Error");
            out.println("<div id=\"content\">");
            out.println("<h3>No command passed</h3>");
            out.println("</div>");
            printMenu(req, out);
            out.println("</body></html>");
            out.close();
            return;
        }
        if (commands.indexOf(command) < 0) {
            htmlHeaders(resp);
            noCacheHeaders(resp);
            PrintWriter out = resp.getWriter();
            xhtmlHeaders(out);
            out.println("<head>");
            jsCssImport(req, out);
            out.println("<title>InternalCommand</title></head>");
            out.println("<body>");
            printTitle(out, "Error");
            out.println("<div id=\"content\">");
            out.println("<h3>Invalid command passed</h3>");
            out.println("</div>");
            printMenu(req, out);
            out.println("</body></html>");
            out.close();
            return;
        }
        String id = req.getParameter("FavoriteId");
        String category = req.getParameter("category");
        String subCategory = req.getParameter("subcategory");
        String keyword = req.getParameter("keyword");
        String person = req.getParameter("person");
        String roleForPerson = req.getParameter("roleforperson");
        String title = req.getParameter("title");
        String run = req.getParameter("run");
        String[] channelIDs = req.getParameterValues("channelID");
        String network = req.getParameter("network");
        String autoDelete = req.getParameter("autodelete");
        String keepAtMost = req.getParameter("keepatmost");
        String startPad = req.getParameter("startpad");
        String startPadOffsetType = req.getParameter("StartPadOffsetType");
        String endPad = req.getParameter("endpad");
        String endPadOffsetType = req.getParameter("EndPadOffsetType");
        String quality = req.getParameter("quality");
        boolean isAutoConvert = "yes".equals(req.getParameter("autoConvert"));
        boolean isDeleteAfterFavoriteAutomaticConversion = "yes".equals(req.getParameter("deleteOriginal"));
        String favoriteAutomaticConversionFormat = req.getParameter("transcodeMode");
        boolean isFavoriteAutomaticConversionDestinationOriginalDir = "yes".equals(req.getParameter("origDestDir"));
        String favoriteAutomaticConversionDestination = req.getParameter("destDir");
        String parentalRating = req.getParameter("parentalrating");
        String rated = req.getParameter("rated");
        String year = req.getParameter("year");
        String day = req.getParameter("day");
        String time = req.getParameter("time");
        String favoritePriorityRelation = req.getParameter("favoritepriorityrelation");
        String relativeFavoriteId = req.getParameter("relativefavoriteid");
        Boolean firstRunsOnly = new Boolean("First Runs".equals(run));
        Boolean reRunsOnly = new Boolean("ReRuns".equals(run));
        StringBuilder callsigns = new StringBuilder();
        if (channelIDs != null) {
            for (int i = 0; i < channelIDs.length; i++) {
                String channelID = channelIDs[i];
                if ((channelID != null) && (channelID.trim().length() > 0)) {
                    try {
                        Integer channelInt = new Integer(channelID);
                        Object channel = SageApi.Api("GetChannelForStationID", channelInt);
                        if (channel == null) throw new IllegalArgumentException("Unknown channel for parameter: channelID='" + channelID + "'.");
                        String callsign = SageApi.StringApi("GetChannelName", new Object[] { channel });
                        if (i > 0) {
                            callsigns.append(";");
                        }
                        callsigns.append(callsign);
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException("Invalid command parameter: channelID='" + channelID + "'.");
                    }
                }
            }
        }
        try {
            if ((command.equals("Add")) || (command.equals("Update"))) {
                Favorite favorite = Favorite.save(Integer.parseInt(id), category, subCategory, keyword, person, roleForPerson, title, firstRunsOnly, reRunsOnly, callsigns.toString(), network, autoDelete, keepAtMost, startPad, startPadOffsetType, endPad, endPadOffsetType, quality, isAutoConvert, isDeleteAfterFavoriteAutomaticConversion, favoriteAutomaticConversionFormat, isFavoriteAutomaticConversionDestinationOriginalDir, favoriteAutomaticConversionDestination, parentalRating, rated, year, day, time, favoritePriorityRelation, relativeFavoriteId);
                id = String.valueOf(favorite.getID());
            } else if (command.equals("Remove")) {
                Favorite.remove(Integer.parseInt(id));
            } else if (command.equals("CreatePriority")) {
                String lowerPriorityFavoriteParam = req.getParameter("LowerId");
                String higherPriorityFavoriteParam = req.getParameter("HigherId");
                int lowerPriorityFavoriteId = Integer.parseInt(lowerPriorityFavoriteParam);
                int higherPriorityFavoriteId = Integer.parseInt(higherPriorityFavoriteParam);
                Favorite.createFavoritePriority(higherPriorityFavoriteId, lowerPriorityFavoriteId);
            } else if (command.equals("IncreasePriority")) {
                String favoriteParam = req.getParameter("FavoriteId");
                Favorite favorite = new Favorite(Integer.parseInt(favoriteParam));
                favorite.increasePriority();
            } else if (command.equals("DecreasePriority")) {
                String favoriteParam = req.getParameter("FavoriteId");
                Favorite favorite = new Favorite(Integer.parseInt(favoriteParam));
                favorite.decreasePriority();
            } else {
                throw new IllegalArgumentException("Invalid command parameter: '" + command + "'.");
            }
        } catch (Exception e) {
            htmlHeaders(resp);
            noCacheHeaders(resp);
            PrintWriter out = resp.getWriter();
            xhtmlHeaders(out);
            out.println("<head>");
            jsCssImport(req, out);
            out.println("<title>FavoriteCommand</title></head>");
            out.println("<body>");
            printTitle(out, "Error");
            out.println("<div id=\"content\">");
            out.println("<h3>Unable to perform action " + command + " on Favorite</h3>");
            out.println("id:" + id + " -- " + e.toString());
            e.printStackTrace();
            out.println("</div>");
            printMenu(req, out);
            out.println("</body></html>");
            out.close();
            return;
        }
        String returnto = req.getParameter("returnto");
        if (req.getParameter("RetImage") != null) {
            resp.setContentType("image/png");
            noCacheHeaders(resp);
            OutputStream os = resp.getOutputStream();
            BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
            javax.imageio.ImageIO.write(img, "png", os);
            os.close();
        } else if (returnto != null) {
            Thread.sleep(100);
            resp.sendRedirect(returnto);
            return;
        } else {
            htmlHeaders(resp);
            noCacheHeaders(resp);
            PrintWriter out = resp.getWriter();
            xhtmlHeaders(out);
            out.println("<head>");
            jsCssImport(req, out);
            out.println("<title>InternalCommand</title></head>");
            out.println("<body>");
            printTitle(out, "");
            out.println("<div id=\"content\">");
            out.print("Applied command: " + command + " on favorite");
            out.println("</div>");
            printMenu(req, out);
            out.println("</body></html>");
            out.close();
        }
        return;
    }
}
