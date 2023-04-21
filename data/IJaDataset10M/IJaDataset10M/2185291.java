package net.sf.sageplugins.webserver;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.SocketException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.sageplugins.sageutils.SageApi;
import net.sf.sageplugins.sagexmlinfo.SageXmlWriter;

/**
 * @author Owner
 *
 * 
 *
 */
public class HomeServlet extends SageServlet {

    /**
     * 
     */
    private static final long serialVersionUID = -9207342014673529480L;

    protected void doServletGet(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        if (req.getPathInfo() != null && req.getPathInfo().length() > 0) {
            resp.sendRedirect(req.getServletPath());
            return;
        }
        String xml = req.getParameter("xml");
        if (xml != null) {
            if (xml.equalsIgnoreCase("currplaying")) {
                String context = req.getParameter("context");
                if (context == null) context = "SAGETV_PROCESS_LOCAL_UI";
                Object sageAiring = SageApi.ApiUI(context, "GetCurrentMediaFile");
                SendXmlResult(req, resp, sageAiring, "currently_playing.xml");
                return;
            } else if (xml.equalsIgnoreCase("currrecording")) {
                Object captureDevInputs = SageApi.Api("GetConfiguredCaptureDeviceInputs", null);
                Map<?, ?> captureDevs = (Map<?, ?>) SageApi.Api("GroupByMethod", new Object[] { captureDevInputs, "GetCaptureDeviceForInput" });
                SageXmlWriter xmlwriter = new SageXmlWriter();
                resp.setContentType("text/xml; charset=" + charset);
                resp.setHeader("Content-Disposition", "attachment; filename=currently_recording.xml");
                resp.setBufferSize(8192);
                noCacheHeaders(resp);
                OutputStream outs = getGzippedOutputStream(req, resp);
                try {
                    if (!captureDevs.isEmpty()) {
                        for (Iterator<?> i = captureDevs.keySet().iterator(); i.hasNext(); ) {
                            Object capDev = i.next();
                            Object recfile = SageApi.Api("GetCaptureDeviceCurrentRecordFile", new Object[] { capDev });
                            if (recfile != null) xmlwriter.add(recfile);
                        }
                    }
                    try {
                        xmlwriter.write(outs, charset);
                    } catch (SocketException e) {
                    }
                    outs.close();
                } catch (Throwable e) {
                    log("Exception while processing servlet " + this.getClass().getName(), e);
                    if (!resp.isCommitted()) {
                        resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                        resp.setContentType("text/html");
                        PrintWriter out = resp.getWriter();
                        out.println();
                        out.println();
                        out.println("<body><pre>");
                        out.println("Exception while processing servlet:\r\n" + e.toString());
                        e.printStackTrace(out);
                        out.println("</pre>");
                        out.close();
                    }
                }
                return;
            } else if (xml.equalsIgnoreCase("nextrecordings")) {
                Object filelist = SageApi.Api("GetScheduledRecordings");
                if ((filelist != null) && (SageApi.Size(filelist) > 0)) {
                    filelist = SageApi.Api("FilterByBoolMethod", new Object[] { filelist, "IsFileCurrentlyRecording", Boolean.FALSE });
                    long nextRecordingDateMillis = new Airing(SageApi.GetElement(filelist, 0)).getStartMillis();
                    filelist = SageApi.Api("FilterByMethod", new Object[] { filelist, "GetAiringStartTime", new Long(nextRecordingDateMillis), Boolean.TRUE });
                }
                SendXmlResult(req, resp, filelist, "next_recordings.xml");
                return;
            }
        }
        htmlHeaders(resp);
        noCacheHeaders(resp);
        PrintWriter out = getGzippedWriter(req, resp);
        try {
            xhtmlHeaders(out);
            out.println("<head>");
            jsCssImport(req, out);
            out.println("<title>SageTV Web Interface</title>");
            out.println("</head>");
            out.println("<body>");
            printTitle(out, "");
            out.println("<div id=\"content\">");
            boolean usechannellogos = GetOption(req, "UseChannelLogos", "true").equalsIgnoreCase("true");
            boolean showMarkers = GetOption(req, "ShowMarkers", "true").equalsIgnoreCase("true");
            boolean showRatings = GetOption(req, "ShowRatings", "true").equalsIgnoreCase("true");
            boolean showEpisodeID = GetOption(req, "ShowEpisodeID", "false").equalsIgnoreCase("true");
            boolean showFileSize = GetOption(req, "ShowFileSize", "true").equalsIgnoreCase("true");
            String[] UiContexts = GetUIContextNames();
            String[] ConnectedClients = (String[]) SageApi.Api("GetConnectedClients");
            StringBuffer xmlurl = new StringBuffer();
            xmlurl.append(req.getContextPath() + req.getServletPath());
            xmlurl.append('?');
            if (req.getQueryString() != null) xmlurl.append(req.getQueryString().replaceAll("&", "&amp;")).append("&amp;");
            xmlurl.append("xml=");
            if (UiContexts.length > 0 || (ConnectedClients.length > 0 && SAGE_MAJOR_VERSION >= 7.0)) {
                out.println("<h2>Currently Watching:</h2>");
                if (Arrays.asList(UiContexts).contains("SAGETV_PROCESS_LOCAL_UI")) {
                    out.println("<h3>On Local SageTV instance:" + "<a href=\"" + xmlurl + "currplaying&amp;context=SAGETV_PROCESS_LOCAL_UI\" title=\"Return currently playing in XML\"><img src=\"xml_button.png\" alt=\"[XML]\"/></a>\r\n" + "<a href=\"webremote.html?context=SAGETV_PROCESS_LOCAL_UI&amp;contextname=Local%20SageTV\" title=\"Webremote for local SageTV\" target=\"webremote\">[WebRemote]</a>" + "</h3>");
                    Object sageAiring = SageApi.ApiUI("SAGETV_PROCESS_LOCAL_UI", "GetCurrentMediaFile");
                    if (sageAiring != null) {
                        out.println("<div class=\"airings\">");
                        new Airing(sageAiring).printAiringTableCell(req, out, false, usechannellogos, showMarkers, showRatings, showEpisodeID, showFileSize, null, null, null);
                        if (!((Boolean) SageApi.ApiUI("SAGETV_PROCESS_LOCAL_UI", "IsPlaying")).booleanValue()) out.print("(Paused) ");
                        out.println("<a href=\"SageCommand?command=Stop&amp;context=SAGETV_PROCESS_LOCAL_UI&returnto=" + req.getRequestURI() + "\">Stop Playback</a><p>");
                        out.println("</div>");
                    } else out.println("Nothing");
                }
                for (int ui = 0; ui < UiContexts.length; ui++) {
                    String context = UiContexts[ui];
                    if (!context.equals("SAGETV_PROCESS_LOCAL_UI")) {
                        out.println("<h3>On Sage Extender at: <a href=\"ExtenderDetails?context=" + URLEncoder.encode(context, charset) + "\">" + UiContextProperties.getProperty(context, "name") + "</a>:");
                        out.println("<a href=\"" + xmlurl + "currplaying&amp;context=" + URLEncoder.encode(context, charset) + "\" title=\"Return currently playing in XML\"><img src=\"xml_button.png\" alt=\"[XML]\"/></a>");
                        out.println("<a href=\"webremote.html?context=" + URLEncoder.encode(context, charset) + "&contextname=" + URLEncoder.encode(UiContextProperties.getProperty(context, "name"), charset) + "\" target=\"webremote\" title=\"Webremote for Sage Extender\" >" + "[WebRemote]</a>\r\n");
                        if (ExtenderCommandServlet.isExtender(context)) {
                            out.println(" <a onclick=\"" + "confirmAction('" + "Are you sure you want to Power Off this Extender?" + "'," + "'ExtenderCommand?context=" + URLEncoder.encode(context, charset) + "&command=poweroff&returnto=Home&amp;confirm=yes');" + " return false;\"" + "href=\"ExtenderCommand?context=" + URLEncoder.encode(context, charset) + "&command=poweroff&returnto=Home\" title=\"Power off this Extender\" >" + "[Power Off]</a>\r\n");
                            out.println(" <a onclick=\"" + "confirmAction('" + "Are you sure you want to Reboot this Extender?" + "'," + "'ExtenderCommand?context=" + URLEncoder.encode(context, charset) + "&command=reboot&returnto=Home&amp;confirm=yes');" + " return false;\"" + "href=\"ExtenderCommand?context=" + URLEncoder.encode(context, charset) + "&command=reboot&returnto=Home\" title=\"Reboot this Extender\" >" + "[Reboot]</a>\r\n");
                        } else {
                            out.println(" <a onclick=\"" + "confirmAction('" + "Are you sure you want to Exit this Placeshifter?" + "'," + "'ExtenderCommand?context=" + URLEncoder.encode(context, charset) + "&command=exit&returnto=Home&amp;confirm=yes');" + " return false;\"" + "href=\"ExtenderCommand?context=" + URLEncoder.encode(context, charset) + "&command=exit&returnto=Home\" title=\"Tell this Placeshifter to Exit\" >" + "[Exit]</a>\r\n");
                        }
                        out.println("</h3>");
                        Object sageAiring = SageApi.ApiUI(context, "GetCurrentMediaFile");
                        if (sageAiring != null) {
                            out.println("<div class=\"airings\">");
                            new Airing(sageAiring).printAiringTableCell(req, out, false, usechannellogos, showMarkers, showRatings, showEpisodeID, showFileSize, null, null, null);
                            if (!((Boolean) SageApi.ApiUI(context, "IsPlaying")).booleanValue()) out.print("(Paused) ");
                            out.println("<a href=\"SageCommand?command=Stop&amp;context=" + URLEncoder.encode(context, charset) + "&returnto=" + req.getRequestURI() + "\">Stop Playback</a><p>");
                            out.println("</div>");
                        } else out.println("Nothing");
                    }
                }
                if (SAGE_MAJOR_VERSION >= 7.0) {
                    if (ConnectedClients != null && ConnectedClients.length > 0) {
                        for (int i = 0; i < ConnectedClients.length; i++) {
                            String context = ConnectedClients[i];
                            out.println("<h3>On Sage Client at: <a href=\"ExtenderDetails?context=" + URLEncoder.encode(context, charset) + "\">" + UiContextProperties.getProperty(context, "name") + "</a>:");
                            out.println("<a href=\"" + xmlurl + "currplaying&amp;context=" + URLEncoder.encode(context, charset) + "\" title=\"Return currently playing in XML\"><img src=\"xml_button.png\" alt=\"[XML]\"/></a>");
                            out.println("<a href=\"webremote.html?context=" + URLEncoder.encode(context, charset) + "&contextname=" + URLEncoder.encode(UiContextProperties.getProperty(context, "name"), charset) + "\" target=\"webremote\" title=\"Webremote for Sage Extender\" >" + "[WebRemote]</a>\r\n");
                            out.println("</h3>");
                            Object sageAiring = SageApi.ApiUI(context, "GetCurrentMediaFile");
                            if (sageAiring != null) {
                                out.println("<div class=\"airings\">");
                                new Airing(sageAiring).printAiringTableCell(req, out, false, usechannellogos, showMarkers, showRatings, showEpisodeID, showFileSize, null, null, null);
                                if (!((Boolean) SageApi.ApiUI(context, "IsPlaying")).booleanValue()) out.print("(Paused) ");
                                out.println("<a href=\"SageCommand?command=Stop&amp;context=" + URLEncoder.encode(context, charset) + "&returnto=" + req.getRequestURI() + "\">Stop Playback</a><p>");
                                out.println("</div>");
                            } else {
                                out.println("Nothing");
                            }
                        }
                    }
                }
                out.println("<hr/>");
            }
            out.println("<h2>Currently Recording\r\n" + "<a href=\"" + xmlurl + "currrecording\" title=\"Return currently recording in XML\"><img src=\"xml_button.png\" alt=\"[XML]\"/></a>\r\n" + "</h2>");
            Object captureDevInputs = SageApi.Api("GetConfiguredCaptureDeviceInputs", null);
            Map<?, ?> captureDevs = (Map<?, ?>) SageApi.Api("GroupByMethod", new Object[] { captureDevInputs, "GetCaptureDeviceForInput" });
            if (!captureDevs.isEmpty()) {
                for (Iterator<?> i = captureDevs.keySet().iterator(); i.hasNext(); ) {
                    Object capDev = i.next();
                    Object recfile = SageApi.Api("GetCaptureDeviceCurrentRecordFile", new Object[] { capDev });
                    if (recfile != null) {
                        out.print("<p>" + capDev.toString() + ":</p>");
                        out.println("<div class=\"airings\">");
                        new Airing(recfile).printAiringTableCell(req, out, false, usechannellogos, showMarkers, showRatings, showEpisodeID, showFileSize, null, null, null);
                        out.println("</div>");
                    } else {
                        out.print("<p>Nothing on " + capDev.toString() + "</p>");
                    }
                }
            } else {
                out.println("<p>No encoders configured</p>");
            }
            out.println("<hr/>");
            out.println("<h2><a href=\"/sage/RecordingSchedule\" title=\"Recording Schedule\">Next Upcoming Recordings</a>\r\n" + "<a href=\"" + xmlurl + "nextrecordings\" title=\"Next upcoming recordings in XML\"><img src=\"xml_button.png\" alt=\"[XML]\"/></a>\r\n" + "</h2>");
            Object filelist = SageApi.Api("GetScheduledRecordings");
            Object allConflicts = SageApi.Api("GetAiringsThatWontBeRecorded", new Object[] { Boolean.FALSE });
            Object unresolvedConflicts = SageApi.Api("GetAiringsThatWontBeRecorded", new Object[] { Boolean.TRUE });
            if (filelist != null) filelist = SageApi.Api("FilterByBoolMethod", new Object[] { filelist, "IsFileCurrentlyRecording", Boolean.FALSE });
            if ((filelist != null) && (SageApi.Size(filelist) > 0)) {
                long nextRecordingDateMillis = new Airing(SageApi.GetElement(filelist, 0)).getStartMillis();
                Calendar nextRecordingDateCal = Calendar.getInstance();
                nextRecordingDateCal.setTimeInMillis(nextRecordingDateMillis);
                Date nextRecordingDate = new Date(nextRecordingDateMillis);
                Calendar today = Calendar.getInstance();
                Calendar tomorrow = Calendar.getInstance();
                tomorrow.add(Calendar.DAY_OF_YEAR, 1);
                filelist = SageApi.Api("FilterByMethod", new Object[] { filelist, "GetAiringStartTime", new Long(nextRecordingDateMillis), Boolean.TRUE });
                SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM d, yyyy");
                DateFormat timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT);
                if ((today.get(Calendar.YEAR) == nextRecordingDateCal.get(Calendar.YEAR)) && (today.get(Calendar.MONTH) == nextRecordingDateCal.get(Calendar.MONTH)) && (today.get(Calendar.DAY_OF_MONTH) == nextRecordingDateCal.get(Calendar.DAY_OF_MONTH))) {
                    out.println("<h3>Today at " + timeFormat.format(nextRecordingDate) + "</h3>");
                } else if ((tomorrow.get(Calendar.YEAR) == nextRecordingDateCal.get(Calendar.YEAR)) && (tomorrow.get(Calendar.MONTH) == nextRecordingDateCal.get(Calendar.MONTH)) && (tomorrow.get(Calendar.DAY_OF_MONTH) == nextRecordingDateCal.get(Calendar.DAY_OF_MONTH))) {
                    out.println("<h3>Tomorrow at " + timeFormat.format(nextRecordingDate) + "</h3>");
                } else {
                    out.println("<h3>" + dateFormat.format(nextRecordingDate) + " at " + timeFormat.format(nextRecordingDate) + "</h3>");
                }
                Object encoders = SageApi.Api("GetActiveCaptureDevices");
                for (int i = 0; i < SageApi.Size(filelist); i++) {
                    Airing airing = new Airing(SageApi.GetElement(filelist, i));
                    for (int j = 0; j < SageApi.Size(encoders); j++) {
                        Object encoder = SageApi.GetElement(encoders, j);
                        Object encoderAirings = SageApi.Api("GetScheduledRecordingsForDeviceForTime", new Object[] { encoder, new Long(airing.getStartMillis()), new Long(airing.getEndMillis()) });
                        if (encoderAirings != null) {
                            for (int k = 0; k < SageApi.Size(encoderAirings); k++) {
                                Airing encoderAiring = new Airing(SageApi.GetElement(encoderAirings, k));
                                if (airing.id == encoderAiring.id) {
                                    out.print("<p>" + encoder.toString() + ":</p>");
                                    break;
                                }
                            }
                        }
                    }
                    out.println("<div class=\"airings\">");
                    airing.printAiringTableCell(req, out, false, usechannellogos, showMarkers, showRatings, showEpisodeID, showFileSize, filelist, allConflicts, unresolvedConflicts);
                    out.println("</div>");
                }
            } else {
                out.print("<p>No Upcoming Recordings</p>");
            }
            out.println("<hr/>");
            Object UnresolvedConflicts = SageApi.Api("GetAiringsThatWontBeRecorded", new Object[] { Boolean.TRUE });
            Object missedairings = SageApi.Api("GetAiringsThatWontBeRecorded", new Object[] { Boolean.FALSE });
            int numconflicts = SageApi.Size(missedairings);
            int numunresolved = SageApi.Size(UnresolvedConflicts);
            int numresolved = numconflicts - numunresolved;
            if (numconflicts > 0) {
                out.println("<h2><a href=\"Conflicts\">");
                if (numunresolved > 0) {
                    out.println("<img src=\"conflicticon.gif\" alt=\"Unresolved Conflict\" title=\"Unresolved Conflict\"/> " + numunresolved + " Unresolved Conflicts");
                    if (numresolved > 0) out.println("<br/>");
                }
                if (numresolved > 0) out.println("<img src=\"resolvedconflicticon.gif\" alt=\"Resolved Conflict\" title=\"Resolved Conflict\"/>" + numresolved + " Resolved Conflicts</a></h2>");
                out.println("</a></h2><hr/>");
            }
            if (SAGE_MAJOR_VERSION < 7.0) {
                if (ConnectedClients != null && ConnectedClients.length > 0) {
                    out.println("<h2>Connected Clients:</h2>");
                    out.println(Arrays.asList(ConnectedClients));
                    out.println("<hr/>");
                }
            }
            DecimalFormat fmt = new DecimalFormat("0.00");
            out.println("<h2>Video Disk Space</h2>");
            double diskavail = ((Long) SageApi.Api("GetTotalDiskspaceAvailable", null)).doubleValue() / 1000000000.0;
            double diskused = ((Long) SageApi.Api("GetUsedVideoDiskspace", null)).doubleValue() / 1.0E9;
            int numpartials = 0;
            double partialsspace = 0.0;
            boolean partialsFree = SageApi.GetBooleanProperty("nielm/diskbar_partials_free", true);
            if (partialsFree) {
                Object mediafiles = SageApi.Api("GetMediaFiles");
                mediafiles = SageApi.Api("FilterByBoolMethod", new Object[] { mediafiles, "IsTVFile", Boolean.TRUE });
                mediafiles = SageApi.Api("FilterByBoolMethod", new Object[] { mediafiles, "IsLibraryFile", Boolean.FALSE });
                mediafiles = SageApi.Api("FilterByBoolMethod", new Object[] { mediafiles, "IsCompleteRecording", Boolean.FALSE });
                mediafiles = SageApi.Api("FilterByBoolMethod", new Object[] { mediafiles, "IsManualRecord", Boolean.FALSE });
                numpartials = SageApi.Size(mediafiles);
                for (int num = 0; num < numpartials; num++) {
                    Object file = SageApi.GetElement(mediafiles, num);
                    partialsspace += ((Long) SageApi.Api("GetSize", new Object[] { file })).doubleValue();
                }
                partialsspace /= 1.0E9;
            }
            int diskusedpc = new Double(100.0 * (diskused - partialsspace) / (diskavail + diskused)).intValue();
            int numautodelete = 0;
            double autodeletespace = 0.0;
            Object mediafiles = SageApi.Api("GetMediaFiles");
            mediafiles = SageApi.Api("FilterByBoolMethod", new Object[] { mediafiles, "IsTVFile", Boolean.TRUE });
            mediafiles = SageApi.Api("FilterByBoolMethod", new Object[] { mediafiles, "IsLibraryFile", Boolean.FALSE });
            mediafiles = SageApi.Api("FilterByBoolMethod", new Object[] { mediafiles, "IsManualRecord", Boolean.FALSE });
            mediafiles = SageApi.Api("FilterByBoolMethod", new Object[] { mediafiles, "IsCompleteRecording", Boolean.TRUE });
            mediafiles = SageApi.Api("FilterByBoolMethod", new Object[] { mediafiles, "IsFileCurrentlyRecording", Boolean.FALSE });
            numautodelete = SageApi.Size(mediafiles);
            for (int num = 0; num < SageApi.Size(mediafiles); num++) {
                Object mediafile = SageApi.GetElement(mediafiles, num);
                Object sageFavorite = SageApi.Api("GetFavoriteForAiring", mediafile);
                if ((sageFavorite == null) || (SageApi.booleanApi("IsAutoDelete", new Object[] { sageFavorite }))) {
                    autodeletespace += ((Long) SageApi.Api("GetSize", new Object[] { mediafile })).doubleValue();
                } else {
                    numautodelete--;
                }
            }
            autodeletespace /= 1.0E9;
            long lookahead = ((long) SageApi.GetIntProperty("nielm/diskbar_lookahead_hours", 48)) * 60 * 60 * 1000;
            long now = new Date().getTime();
            Object recordings = SageApi.Api("GetScheduledRecordings");
            recordings = SageApi.Api("FilterByRange", new Object[] { recordings, "GetAiringEndTime", new Long(now), new Long(now + lookahead), Boolean.TRUE });
            if (SageApi.GetBooleanProperty("nielm/diskbar_lookahead_only_requested", true)) {
                Object manrec = SageApi.Api("FilterByBoolMethod", new Object[] { recordings, "IsManualRecord", Boolean.TRUE });
                Object faverec = SageApi.Api("FilterByBoolMethod", new Object[] { recordings, "IsFavorite", Boolean.TRUE });
                recordings = SageApi.Api("DataUnion", new Object[] { manrec, faverec });
            }
            double bytesneeded = 0;
            for (int num = 0; num < SageApi.Size(recordings); num++) {
                Object sageAiring = SageApi.GetElement(recordings, num);
                Object quality = SageApi.Api("GetRecordingQuality", new Object[] { sageAiring });
                Long duration = (Long) SageApi.Api("GetScheduleDuration", new Object[] { sageAiring });
                if (SageApi.Size(quality) <= 0) quality = SageApi.Api("GetDefaultRecordingQuality");
                Long bitrate = (Long) SageApi.Api("GetRecordingQualityBitrate", new Object[] { quality });
                bytesneeded += (bitrate.doubleValue() / 8.0 * duration.doubleValue() / 1000.0);
            }
            double diskneeded = (bytesneeded / 1.0E9);
            int diskreqpc = new Double(100.0 * diskneeded / (diskavail + diskused)).intValue();
            out.println("<table style=\"border:2px groove\" cellpadding=\"0\" cellspacing=\"1\" border=\"0\" width=\"200px\"><tr>");
            if (diskusedpc > 0) {
                if (diskusedpc > SageApi.GetIntProperty("nielm/diskbar_used_space_warn", 90)) out.print("<td bgcolor=\"#ffaa00\" title=\"Disk space used\" style=\"background-color:#ffaa00\" width=\"" + Integer.toString(diskusedpc) + "%\">"); else out.print("<td bgcolor=\"#00aa00\" title=\"Disk space used\" style=\"background-color:#00aa00\" width=\"" + Integer.toString(diskusedpc) + "%\">");
                out.println("&nbsp;</td>");
            }
            if (diskreqpc > 0) {
                if ((diskusedpc + diskreqpc) > SageApi.GetIntProperty("nielm/diskbar_needed_space_warn", 95)) {
                    if ((diskusedpc + diskreqpc) > 100) {
                        diskreqpc = 100 - diskusedpc;
                        out.print("<td bgcolor=\"#b00100\" title=\"Disk space required\"  style=\"background-color:#b00100\" width=\"" + Integer.toString((diskreqpc)) + "%\">");
                        out.println("&nbsp;</td>");
                    } else {
                        out.print("<td bgcolor=\"#b00100\" title=\"Disk space required\" style=\"background-color:#b00100\" width=\"" + Integer.toString((diskreqpc)) + "%\">");
                        out.println("&nbsp;</td>\r\n<td>&nbsp;</td>");
                    }
                } else {
                    out.print("<td bgcolor=\"#ffff00\" title=\"Disk space required\" style=\"background-color:#ffff00\" width=\"" + Integer.toString((diskreqpc)) + "%\">");
                    out.println("&nbsp;</td>\r\n<td>&nbsp;</td>");
                }
            } else {
                out.println("<td>&nbsp;</td>");
            }
            out.println("</tr></table>");
            out.println("<ul><li>Available: " + fmt.format(diskavail) + " GB</li>");
            out.println("<li>Used: " + fmt.format(diskused) + " GB");
            if ((partialsFree && numpartials > 0) || numautodelete > 0) {
                out.print("<ul>");
                if (partialsFree && numpartials > 0) {
                    out.print("<li><a href=\"Search?SearchString=&amp;searchType=TVFiles&amp;grouping=GetAiringTitle&amp;partials=only&amp;autodelete=any&amp;sort1=airdate_asc&amp;pagelen=" + GetOption(req, "pagelen", Integer.toString(AiringList.DEF_NUM_ITEMS)) + "\" ");
                    out.println("title=\"Click this to show the autodeletable recordings that are partially recorded.\">[Partial Recordings]</a>: " + numpartials + " recording" + (numpartials == 1 ? "" : "s") + " using " + fmt.format(partialsspace) + " GB</li>");
                }
                if (numautodelete > 0) {
                    out.print("<li><a href=\"Search?SearchString=&amp;searchType=TVFiles&amp;grouping=GetAiringTitle&amp;partials=none&amp;autodelete=set&amp;sort1=airdate_asc&amp;pagelen=" + GetOption(req, "pagelen", Integer.toString(AiringList.DEF_NUM_ITEMS)) + "\" ");
                    out.println("title=\"Click this to show the complete recordings that can be automatically deleted if more space is needed.\">[Auto-Delete Recordings]</a>: " + numautodelete + " recording" + (numautodelete == 1 ? "" : "s") + " using " + fmt.format(autodeletespace) + " GB</li>");
                }
                out.println("</ul>");
            }
            out.print("</li><li>Next " + Long.toString(lookahead / 3600000) + "hrs has " + Integer.toString(SageApi.Size(recordings)) + " upcoming ");
            if (SageApi.GetBooleanProperty("nielm/diskbar_lookahead_only_requested", true)) out.print("requested ");
            out.println("recordings requiring: " + fmt.format(diskneeded) + " GB");
            out.println("</li><li>Total Video Content: " + fmt.format(((Long) SageApi.Api("GetTotalVideoDuration", null)).doubleValue() / 3600000.0) + "hrs</li></ul>");
            out.println("<hr/>");
            out.println("<h2>Program Guide:</h2>");
            long LastUpdate = ((Long) SageApi.Api("GetLastEPGDownloadTime", null)).longValue();
            out.println("<p>Last EPG Update: " + DateFormat.getDateTimeInstance().format(new Date(LastUpdate)) + "</p>");
            out.println("<p>Next EPG Update In: " + fmt.format(((Long) SageApi.Api("GetTimeUntilNextEPGDownload", null)).doubleValue() / 3600000.0) + "hrs</p>");
            if (System.currentTimeMillis() - LastUpdate > 60 * 1000) out.println("<p><a href=\"GlobalCommand?command=ForceEpgUpdate\" title=\"Click this to force an EPG update to occur immediately on all lineups\">[Force EPG update]</a></p>");
            out.println("<hr/>");
            out.println("<h2>Media Library:</h2>");
            out.println("<ul>");
            {
                mediafiles = SageApi.Api("GetMediaFiles");
                Object[] libraryfiles = (Object[]) SageApi.Api("FilterByBoolMethod", new Object[] { mediafiles, "IsLibraryFile", Boolean.TRUE });
                mediafiles = SageApi.Api("FilterByBoolMethod", new Object[] { libraryfiles, "IsMusicFile", Boolean.TRUE });
                out.println("<li>Music: " + SageApi.Size(SageApi.Api("GetAlbums")) + " albums / " + SageApi.Size(mediafiles) + " tracks" + "</li>");
                mediafiles = SageApi.Api("FilterByBoolMethod", new Object[] { libraryfiles, "IsVideoFile", Boolean.TRUE });
                mediafiles = SageApi.Api("FilterByBoolMethod", new Object[] { mediafiles, "IsTVFile", Boolean.FALSE });
                out.println("<li>Video: " + SageApi.Size(mediafiles) + " files" + "</li>");
                mediafiles = SageApi.Api("FilterByBoolMethod", new Object[] { libraryfiles, "IsDVD", Boolean.TRUE });
                out.println("<li>DVDs: " + SageApi.Size(mediafiles) + " files" + "</li>");
                mediafiles = SageApi.Api("FilterByBoolMethod", new Object[] { libraryfiles, "IsPictureFile", Boolean.TRUE });
                out.println("<li>Pictures: " + SageApi.Size(mediafiles) + " files" + "</li>");
                out.println("<li>Total Media Library Duration: " + SageApi.Api("PrintDuration", SageApi.Api("GetTotalLibraryDuration")) + "</li>");
            }
            out.println("</ul>");
            out.println("<p><a href=\"GlobalCommand?command=UpdateMediaLibrary\" title=\"Click this to rescan the imported media directories\">[Rescan Media Library]</a></p>");
            printFooter(req, out);
            out.println("</div>");
            printMenu(out);
            out.println("</body></html>");
            out.close();
        } catch (Throwable e) {
            if (!resp.isCommitted()) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.setContentType("text/html");
            }
            out.println();
            out.println();
            out.println("<body><pre>");
            out.println("Exception while processing servlet:\r\n" + e.toString());
            e.printStackTrace(out);
            out.println("</pre>");
            out.close();
            log("Exception while processing servlet", e);
        }
    }
}
