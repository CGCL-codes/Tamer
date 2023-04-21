package com.feyaSoft.home.web.source;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.OutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import com.feyaSoft.home.services.source.SourceServiceImpl;
import com.feyaSoft.home.web.base.BaseController;
import com.feyaSoft.home.web.common.JsonMVCUtil;

/**
 * @author Fenqiang Zhuang
 * @Oct 8, 2007
 * 
 * This file is used to .....
 */
public class SourceController extends BaseController {

    private static final Logger log = Logger.getLogger(SourceController.class);

    private SourceServiceImpl sourceService;

    /**
	 * load fold/files now. And return JSON data result to response
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
    public ModelAndView loadFiles(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String node = null;
        if (request.getParameter("node") != null) {
            node = request.getParameter("node");
        }
        boolean isAllow = false;
        if ((node.indexOf("scripts") > 0 && node.indexOf("public") > 0) || (node.indexOf("home") > 0 && node.indexOf("src") > 0)) {
            isAllow = true;
        }
        if (node != null && isAllow) {
            SourceJson sourceJson = sourceService.getFiles(node);
            if (sourceJson != null) {
                SourceUtil.jsonResponse(sourceJson, request, response);
            }
        }
        return null;
    }

    /**
	 * load file now...
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
    public ModelAndView loadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String node_id = null;
        StringBuffer fileText = new StringBuffer();
        if (request.getParameter("nodeId") != null) {
            node_id = request.getParameter("nodeId");
        } else {
            return null;
        }
        try {
            String thisLine;
            FileReader fis = new FileReader(node_id);
            BufferedReader br = new BufferedReader(fis);
            while ((thisLine = br.readLine()) != null) {
                fileText.append(br.readLine() + "<br>");
            }
            br.close();
            fis.close();
            JsonMVCUtil.jsonHtmlResponse(fileText.toString(), request, response);
        } catch (Exception e) {
            log.error("call load file failed...");
            e.printStackTrace();
        }
        return null;
    }

    /**
	 * handle download action
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
    public ModelAndView download(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String node_id = null;
        String node_name = null;
        if (request.getParameter("nodeId") != null) {
            node_id = request.getParameter("nodeId");
            node_name = request.getParameter("nodeName");
        } else {
            log.info("nodeId is null, return nothing to client");
            return null;
        }
        try {
            File file = new File(node_id);
            FileInputStream inputStream = new FileInputStream(file);
            response.setHeader("Pragma", "public");
            response.setHeader("Content-Disposition", "attachment; filename=" + node_name);
            response.setHeader("Cache-Control", "cache");
            response.setHeader("Cache-Control", "must-revalidate");
            response.setContentType("application/binary");
            OutputStream outputStream = response.getOutputStream();
            byte[] buffer = new byte[2048];
            int i = 0;
            while ((i = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, i);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setSourceService(SourceServiceImpl sourceService) {
        this.sourceService = sourceService;
    }
}
