package egovframework.com.utl.fcc.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import egovframework.com.cmm.service.EgovFileMngUtil;
import egovframework.com.cmm.service.EgovProperties;

@Controller
public class EgovFileDownController {

    /**
     * 파일 다운로드 기능을 제공한다.
     */
    @RequestMapping(value = "/EgovFileDown.do")
    public String handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String stordFilePath = EgovProperties.getProperty("Globals.fileStorePath");
        String filename = request.getParameter("filename");
        String original = request.getParameter("original");
        if ("".equals(filename)) {
            request.setAttribute("message", "File not found.");
            return "egovframework/com/utl/fcc/EgovFileDown";
        }
        if ("".equals(original)) {
            original = filename;
        }
        request.setAttribute("downFile", stordFilePath + filename);
        request.setAttribute("orginFile", original);
        EgovFileMngUtil.downFile(request, response);
        return "egovframework/com/utl/fcc/EgovFileDown";
    }
}
