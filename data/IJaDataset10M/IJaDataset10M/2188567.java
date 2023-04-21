package org.tbase.servlet;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.net.*;
import java.util.*;
import org.tbase.html.*;
import org.tbase.util.*;
import org.tbase.sql.*;

/**
 * @author Ron Bakker
 * @version $Id: TBaseDispatcher.java,v 1.5 2006/03/27 06:07:34 ron_bakker Exp $
 */
public class TBaseDispatcher extends HttpServlet {

    static final long serialVersionUID = 0x0;

    static final long REQUESTTYPE_GET = 0x1;

    static final long REQUESTTYPE_POST = 0x2;

    static final int ERR_NOPARSER = 0x1001;

    private String m_parserline = new String();

    private String m_parsername = new String();

    private TBaseServiceContainer m_container = null;

    public void init() throws ServletException {
    }

    public TBaseDatabase getDatabase(String name) {
        return m_container.getDatabase(name);
    }

    public void msg(String msg, HttpServletRequest request) {
        super.log(msg);
        String errmsg = (String) request.getAttribute("requestError");
        errmsg = errmsg + msg + "\n";
        request.setAttribute("requestError", errmsg);
    }

    private TemplateParser getParser(String templatename, HttpServletRequest request) {
        BufferedReader input = null;
        try {
            input = new BufferedReader(new FileReader(templatename));
            m_parserline = input.readLine();
            m_parsername = m_parserline.substring(8, m_parserline.length() - 1);
        } catch (FileNotFoundException ex) {
            msg("Template file not found: " + templatename, request);
            msg("Exception Message: " + ex.getMessage(), request);
            return null;
        } catch (IOException ex) {
            msg("Template file not found: " + templatename, request);
            msg("Exception Message: " + ex.getMessage(), request);
            return null;
        } finally {
            try {
                if (input != null) {
                    input.close();
                }
            } catch (IOException ex) {
                msg("Template file not found: " + templatename, request);
                msg("Exception Message: " + ex.getMessage(), request);
                return null;
            }
        }
        try {
            Class aClass = Class.forName(m_parsername);
            return (TemplateParser) aClass.newInstance();
        } catch (Exception e) {
            msg("Cannot instantiate parser: " + m_parsername, request);
            msg("Exception Message: " + e.getMessage(), request);
            return null;
        }
    }

    private String getError(HttpServletRequest request) {
        return (String) request.getAttribute("requestError");
    }

    private String getMimeType(String format) {
        if (format.equalsIgnoreCase("pdf")) return "application/pdf"; else if (format.equalsIgnoreCase("audio_basic")) return "audio/basic"; else if (format.equalsIgnoreCase("audio_wav")) return "audio/wav"; else if (format.equalsIgnoreCase("image_gif")) return "image/gif"; else if (format.equalsIgnoreCase("image_jpeg")) return "image/jpeg"; else if (format.equalsIgnoreCase("image_bmp")) return "image/bmp"; else if (format.equalsIgnoreCase("image_x-png")) return "image/x-png"; else if (format.equalsIgnoreCase("msdownload")) return "application/x-msdownload"; else if (format.equalsIgnoreCase("video_avi")) return "video/avi"; else if (format.equalsIgnoreCase("video_mpeg")) return "video/mpeg"; else if (format.equalsIgnoreCase("text_css")) return "text/css"; else if (format.equalsIgnoreCase("javascript")) return "text/javascript"; else if (format.equalsIgnoreCase("html")) return "text/html"; else if (format.equalsIgnoreCase("xml")) return "text/xml"; else return null;
    }

    private void streamBinaryData(String urlstr, String format, ServletOutputStream outstr, HttpServletResponse resp) {
        String ErrorStr = null;
        try {
            resp.setContentType(getMimeType(format));
            BufferedInputStream bis = null;
            BufferedOutputStream bos = null;
            try {
                URL url = new URL(urlstr);
                URLConnection urlc = url.openConnection();
                int length = urlc.getContentLength();
                resp.setContentLength(length);
                InputStream in = urlc.getInputStream();
                bis = new BufferedInputStream(in);
                bos = new BufferedOutputStream(outstr);
                byte[] buff = new byte[length];
                int bytesRead;
                while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                    bos.write(buff, 0, bytesRead);
                }
            } catch (Exception e) {
                e.printStackTrace();
                ErrorStr = "Error Streaming the Data";
                outstr.print(ErrorStr);
            } finally {
                if (bis != null) {
                    bis.close();
                }
                if (bos != null) {
                    bos.close();
                }
                if (outstr != null) {
                    outstr.flush();
                    outstr.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String _getTemplate(String templatename, ArrayList<String> arguments) {
        String usetemplate = templatename;
        boolean exist = false;
        while (!exist) {
            File tfile = new File(usetemplate);
            exist = tfile.exists();
            if (exist) {
                if (tfile.isDirectory()) usetemplate = usetemplate + "/index.tb";
            } else {
                String[] fragments = usetemplate.split("/");
                arguments.add(fragments[fragments.length - 1]);
                usetemplate = "";
                for (int i = 0; i < fragments.length - 1; i++) usetemplate = usetemplate + "/" + fragments[i];
            }
        }
        return usetemplate;
    }

    private void doRequest(HttpServletRequest request, HttpServletResponse response, long requesttype) throws ServletException, IOException {
        String ru = request.getRequestURI();
        int index = ru.indexOf("/", 1);
        if (index == -1) {
            response.sendRedirect(ru + "/");
            return;
        }
        String localfile = ru.substring(index, ru.length());
        if (localfile.length() > 3) {
            if (ru.substring(ru.length() - 4, ru.length()).equals(new String(".jpg"))) {
                streamBinaryData("file:///" + this.getServletContext().getRealPath("") + localfile, "image_jpeg", response.getOutputStream(), response);
                return;
            }
            if (ru.substring(ru.length() - 4, ru.length()).equals(new String(".css"))) {
                streamBinaryData("file:///" + this.getServletContext().getRealPath("") + localfile, "text_css", response.getOutputStream(), response);
                return;
            }
            if (ru.substring(ru.length() - 4, ru.length()).equals(new String(".png"))) {
                streamBinaryData("file:///" + this.getServletContext().getRealPath("") + localfile, "image_x-png", response.getOutputStream(), response);
                return;
            }
            if (ru.substring(ru.length() - 4, ru.length()).equals(new String(".gif"))) {
                streamBinaryData("file:///" + this.getServletContext().getRealPath("") + localfile, "image_gif", response.getOutputStream(), response);
                return;
            }
            if (ru.substring(ru.length() - 3, ru.length()).equals(new String(".js"))) {
                streamBinaryData("file:///" + this.getServletContext().getRealPath("") + localfile, "javascript", response.getOutputStream(), response);
                return;
            }
        }
        String templatename = this.getServletContext().getRealPath("") + localfile;
        ArrayList<String> args = new ArrayList<String>();
        templatename = _getTemplate(templatename, args);
        Stack<String> arguments = new Stack<String>();
        for (int i = args.size() - 1; i > -1; i--) {
            arguments.push(args.get(i));
        }
        String errormessages = new String();
        request.setAttribute("requestError", errormessages);
        request.setAttribute("requestServlet", this);
        request.setAttribute("requestType", new Long(requesttype));
        request.setAttribute("requestArguments", arguments);
        m_container = TBaseServiceContainer.initialize(getServletContext());
        response.addHeader("Content-Type", "text/html");
        TemplateParser templateparser = getParser(templatename, request);
        if (templateparser == null) {
            String error = getError(request);
            response.getWriter().println("<div class=\"tbaseerror\">");
            response.getWriter().println(error.replaceAll("\n", "<br/>"));
            response.getWriter().println("</div>");
        } else response.getWriter().println(templateparser.parseTemplate(templatename, request, response));
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doRequest(request, response, REQUESTTYPE_GET);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doRequest(request, response, REQUESTTYPE_POST);
    }
}
