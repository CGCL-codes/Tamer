package com.ago;

import com.meterware.httpunit.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Wget {

    public Wget() {
    }

    public static void main(String args[]) throws Exception {
        String dir = ".";
        if (args.length > 0) dir = args[0];
        WebConversation wc = new WebConversation();
        String url = "http://education.yahoo.com/college/math_problem";
        com.meterware.httpunit.WebRequest req = new GetMethodWebRequest(url);
        WebResponse resp = wc.getResponse(req);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy");
        String name = dir + File.separator + sdf.format(new Date());
        System.out.println(name);
        FileWriter fw = new FileWriter(name + "-problem.html");
        fw.write(resp.getText());
        fw.close();
        resp = wc.getResponse("http://education.yahoo.com/college/math_problem/correct.html");
        fw = new FileWriter(name + "-answer.html");
        fw.write(resp.getText());
        fw.close();
        System.out.println(resp.getText());
    }
}
