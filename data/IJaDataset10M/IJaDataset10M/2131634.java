package com.rooster.action.candidate.single_page;

import java.io.PrintWriter;
import javax.servlet.http.HttpServletResponse;

public class SendResponse {

    public static void send(HttpServletResponse res, String sOutPut) {
        try {
            res.setContentType("text/html");
            PrintWriter out = res.getWriter();
            out.write(sOutPut);
            out.flush();
            out.close();
        } catch (Exception e) {
        }
    }
}
