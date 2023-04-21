package com.ufnasoft.dms.server;

import com.ufnasoft.dms.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.log4j.Logger;
import java.io.*;
import java.util.*;

public class UpdateFileName extends InitDMS {

    Logger logger1 = Logger.getLogger(UpdateFileName.class);

    public void init(ServletConfig servletconfig) throws ServletException {
        super.init(servletconfig);
    }

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        res.setContentType("text/html; charset=UTF-8");
        ServletOutputStream out = res.getOutputStream();
        String serverfilename = req.getParameter("serverfilename");
        String clientfilename = req.getParameter("clientfilename");
        String uploadFilesFolder = dms_home + "+FS+www+FSdatafiles";
        try {
            File f = new File(uploadFilesFolder + FS + clientfilename);
            File f1 = new File(uploadFilesFolder + FS + serverfilename);
            f.renameTo(f1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        out.println("done");
    }
}
