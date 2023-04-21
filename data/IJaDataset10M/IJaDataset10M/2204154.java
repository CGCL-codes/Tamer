package net.sf.smartcrib.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import javax.script.ScriptException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.logging.Logger;
import net.sf.smartcrib.media.*;
import net.sf.smartcrib.*;

/**
 * A Spring controller for all the possible SmartCrib commands sent by the client.
 */
@Controller
public class CommandSpringController {

    private static transient Logger logger = Logger.getLogger("smartcrib.history");

    private static final String SWITCH_BUTTON_PREFIX = "dmxDeviceSwitch";

    private static final String DIMMER_DOWN_BUTTON_PREFIX = "dmxDeviceDimmerDown";

    private static final String DIMMER_UP_BUTTON_PREFIX = "dmxDeviceDimmerUp";

    private static final String DIMMER_MIN_BUTTON_PREFIX = "dmxDeviceDimmerMin";

    private static final String DIMMER_MAX_BUTTON_PREFIX = "dmxDeviceDimmerMax";

    private static final String PLAY_BUTTON = "play";

    private static final String PAUSE_BUTTON = "pause";

    private static final String STOP_BUTTON = "stop";

    private static final String SKIP_FWD_BUTTON = "forward";

    private static final String SKIP_BACK_BUTTON = "backward";

    private static final String PREVIOUS_BUTTON = "previous";

    private static final String NEXT_BUTTON = "next";

    private static final String VOLUME_UP_BUTTON = "volumeUp";

    private static final String VOLUME_DOWN_BUTTON = "volumeDown";

    private static final String SELECT_BUTTON = "select";

    /** The persister used to retrieve/store from the data layer. */
    @Autowired
    private SmartCribManager smartCribManager;

    /** For the lights(DMX) page.
     * @return the spring model/view
     */
    @RequestMapping(value = "/dmxDevices.do", method = RequestMethod.GET)
    public ModelAndView dmxDevices() {
        ModelAndView mv = new ModelAndView("dmxDevices");
        SmartCrib smartCrib = smartCribManager.getSmartCribInstance();
        mv.addObject("dmxDevices", smartCrib.getDevices());
        return mv;
    }

    /** For the lights(DMX) commands.
     * @param request the HTTP request
     * @return the spring model/view
     */
    @RequestMapping(value = "/dmxDevices.do", method = RequestMethod.POST)
    public ModelAndView dmxDeviceCommand(HttpServletRequest request) {
        String command = null;
        int step = 10;
        try {
            Enumeration enumeration = request.getParameterNames();
            while (enumeration.hasMoreElements()) {
                String paramName = (String) enumeration.nextElement();
                if (paramName.endsWith(".x") || paramName.endsWith(".y")) {
                    paramName = paramName.substring(0, paramName.length() - 2);
                }
                if (paramName.startsWith(SWITCH_BUTTON_PREFIX)) {
                    int id = Integer.parseInt(paramName.substring(SWITCH_BUTTON_PREFIX.length()));
                    command = "smartcrib.findDeviceById(" + id + ").switchValue()";
                } else if (paramName.startsWith(DIMMER_DOWN_BUTTON_PREFIX)) {
                    int id = Integer.parseInt(paramName.substring(DIMMER_DOWN_BUTTON_PREFIX.length()));
                    command = "smartcrib.findDeviceById(" + id + ").setValue(smartcrib.findDeviceById(" + id + ").getValue() - " + step + ")";
                } else if (paramName.startsWith(DIMMER_UP_BUTTON_PREFIX)) {
                    int id = Integer.parseInt(paramName.substring(DIMMER_UP_BUTTON_PREFIX.length()));
                    command = "smartcrib.findDeviceById(" + id + ").setValue(smartcrib.findDeviceById(" + id + ").getValue() + " + step + ")";
                } else if (paramName.startsWith(DIMMER_MIN_BUTTON_PREFIX)) {
                    int id = Integer.parseInt(paramName.substring(DIMMER_MIN_BUTTON_PREFIX.length()));
                    command = "smartcrib.findDeviceById(" + id + ").setValue(smartcrib.findDeviceById(" + id + ").getMinValue())";
                } else if (paramName.startsWith(DIMMER_MAX_BUTTON_PREFIX)) {
                    int id = Integer.parseInt(paramName.substring(DIMMER_MAX_BUTTON_PREFIX.length()));
                    command = "smartcrib.findDeviceById(" + id + ").setValue(smartcrib.findDeviceById(" + id + ").getMaxValue())";
                }
                if (command != null) {
                    SmartCrib smartCrib = smartCribManager.getSmartCribInstance();
                    try {
                        smartCrib.execute(command);
                    } catch (ScriptException e) {
                    }
                    break;
                }
            }
        } catch (NumberFormatException exc) {
        }
        return dmxDevices();
    }

    /** For the player page
     * @return the spring model/view
     */
    @RequestMapping(value = "/player.do", method = RequestMethod.GET)
    public ModelAndView player() {
        ModelAndView mv = new ModelAndView("player");
        Player player = smartCribManager.getSmartCribInstance().getPlayer();
        mv.addObject("player", player);
        Collection<Playlist> playlists = smartCribManager.getSmartCribInstance().getPlaylists();
        mv.addObject("playlists", playlists);
        Iterator<Playlist> playlistIterator = playlists.iterator();
        if (player.getPlaylist() == null && playlistIterator.hasNext()) {
            player.openPlaylist(playlistIterator.next());
        }
        return mv;
    }

    /** For the player commands
     * @param request the HTTP request
     * @return the spring model/view
     */
    @RequestMapping(value = "/player.do", method = RequestMethod.POST)
    public ModelAndView playerCommand(HttpServletRequest request) {
        String command = null;
        int step = 10;
        try {
            Enumeration enumeration = request.getParameterNames();
            while (enumeration.hasMoreElements()) {
                String paramName = (String) enumeration.nextElement();
                if (paramName.endsWith(".x") || paramName.endsWith(".y")) {
                    paramName = paramName.substring(0, paramName.length() - 2);
                }
                if (paramName.equals(PLAY_BUTTON) || paramName.equals(PAUSE_BUTTON)) {
                    command = "player.togglePlay()";
                } else if (paramName.equals(STOP_BUTTON)) {
                    command = "player.stop()";
                } else if (paramName.equals(SKIP_FWD_BUTTON)) {
                    command = "player.setTimePosition(player.getTimePosition() + " + step + ")";
                } else if (paramName.equals(SKIP_BACK_BUTTON)) {
                    command = "player.setTimePosition(player.getTimePosition() - " + step + ")";
                } else if (paramName.equals(PREVIOUS_BUTTON)) {
                    command = "player.openPrevious()";
                } else if (paramName.equals(NEXT_BUTTON)) {
                    command = "player.openNext()";
                } else if (paramName.equals(VOLUME_UP_BUTTON)) {
                    command = "player.setVolume(player.getVolume() + " + step + ")";
                } else if (paramName.equals(VOLUME_DOWN_BUTTON)) {
                    command = "player.setVolume(player.getVolume() - " + step + ")";
                } else if (paramName.equals(SELECT_BUTTON)) {
                    String playlistName = request.getParameter("playlist");
                    String filePath = request.getParameter("file");
                    command = "player.openPlaylist(smartcrib.findPlaylistByName(\"" + playlistName + "\"));" + "player.open(player.getPlaylist().findFileByPath(\"" + filePath + "\"))";
                }
                if (command != null) {
                    SmartCrib smartCrib = smartCribManager.getSmartCribInstance();
                    try {
                        smartCrib.execute(command);
                    } catch (ScriptException e) {
                    }
                    break;
                }
            }
        } catch (NumberFormatException exc) {
        }
        return player();
    }

    /**
     * For executing script commands. This will be sent from AJAX.
     * @param command the HTML parameter containing the script command, if not specified, then simply do nothing
     * @param response the HTML response where the returned value (from the command execution) is printed
     * @return null to tell spring that no view will be used to render as the command returned value
     * is simply printed in the result
     * @throws IOException if any I/O error occurs while output the result
     */
    @RequestMapping(value = "/command.do")
    public ModelAndView executeCommand(@RequestParam(value = "command", required = false) String command, HttpServletResponse response) throws IOException {
        if (command != null) {
            SmartCrib smartCrib = smartCribManager.getSmartCribInstance();
            try {
                Object returned = smartCrib.execute(command);
                if (returned != null) response.getWriter().print(returned);
            } catch (ScriptException exc) {
                response.getWriter().print(exc.getMessage());
                response.flushBuffer();
            }
        }
        return null;
    }

    /**
     * A command to get the items in a playlist.
     * @param playlistName an HTML parameter containing the playlist name
     * @param response the HTML response where the items are printed, each on a separate line
     * @return null to tell spring that no view will be used to render as the command returned value 
     * is simply printed in the result
     * @throws IOException if any I/O error occurs while output the result
     */
    @RequestMapping(value = "/getPlaylistItems.do")
    public ModelAndView getPlaylistItems(@RequestParam(value = "playlist") String playlistName, HttpServletResponse response) throws IOException {
        SmartCrib smartCrib = smartCribManager.getSmartCribInstance();
        net.sf.smartcrib.media.Playlist playlist = smartCrib.findPlaylistByName(playlistName);
        for (File file : playlist.getFiles()) {
            response.getWriter().println(file.getAbsolutePath());
        }
        response.flushBuffer();
        return null;
    }

    /** For rendering the history ites,
     * @return the model/view
     */
    @RequestMapping(value = "/history.do")
    public ModelAndView viewHistory() {
        ModelAndView mv = new ModelAndView("history");
        mv.addObject("historyItems", smartCribManager.getHistory());
        return mv;
    }
}
