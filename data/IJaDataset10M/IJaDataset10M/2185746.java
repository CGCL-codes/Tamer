package com.bbs.util;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import com.oreilly.servlet.MultipartRequest;

public class CosUtil {

    private MultipartRequest mr;

    public CosUtil(HttpServletRequest request) {
        String savepath = request.getSession().getServletContext().getRealPath("/") + "upload" + File.separator;
        int max = 1024 * 1024 * 10;
        try {
            mr = new MultipartRequest(request, savepath, max, "UTF-8", new RandomRename());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getParameter(String key) {
        return mr.getParameter(key);
    }

    public Map<String, String> getFileNames() {
        Enumeration<?> filenames = mr.getFileNames();
        String userpic = "defaultheadpic.jpg";
        Map<String, String> map = new HashMap<String, String>();
        while (filenames.hasMoreElements()) {
            String filename = (String) filenames.nextElement();
            if (filename != null) {
                userpic = mr.getFilesystemName(filename);
                map.put(filename, userpic);
            }
        }
        return map;
    }
}
