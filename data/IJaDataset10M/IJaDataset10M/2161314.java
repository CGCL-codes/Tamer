package com.googlecode.g2re.servlet;

import com.googlecode.g2re.HTMLReportBuilder;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.Arrays;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

/**
 *
 * @author Brad Rydzewski
 */
public class HTMLReportView implements ServletView {

    public static final HTMLReportView INSTANCE = new HTMLReportView();

    protected HTMLReportView() {
    }

    public void buildOld(ServletViewArgs args) {
        try {
            String html = HTMLReportBuilder.build(args.getReportFile(), args.getParams(), true);
            args.getOutputStream().print(html);
            args.getResponse().setContentType("text/html;charset=UTF-8");
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void build(ServletViewArgs args) {
        InputStream is = null;
        BufferedReader br = null;
        StringBuilder builder = new StringBuilder();
        String line;
        try {
            String html = HTMLReportBuilder.build(args.getReportFile(), args.getParams(), true);
            is = ErrorView.class.getResourceAsStream("/HTMLReportView.html");
            br = new BufferedReader(new InputStreamReader(is));
            while (null != (line = br.readLine())) {
                builder.append(line).append("\n");
            }
            StringWriter writer = new StringWriter();
            VelocityContext context = new VelocityContext();
            context.put("args", args);
            context.put("html", html);
            Velocity.evaluate(context, writer, "", builder.toString());
            args.getOutputStream().print(writer.toString());
            args.getResponse().setContentType("text/html;charset=UTF-8");
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
