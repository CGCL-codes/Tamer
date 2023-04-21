package br.com.devx.scenery;

import org.mortbay.jetty.servlet.ServletHolder;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.FilterHolder;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.Handler;
import br.com.devx.scenery.web.*;

public class Main {

    public static void main(String[] args) throws Exception {
        int port = 8080;
        int i = 0;
        TargetApp app = AppsConfig.getInstance().getTargetApp();
        while (i < args.length) {
            String arg = args[i];
            if ("-l".equals(arg)) {
                port = Integer.parseInt(args[++i]);
            } else if ("-p".equals(arg)) {
                app.setPath(args[++i]);
            } else if ("-u".equals(arg)) {
                app.setUrl(args[++i]);
            } else if ("/?".equals(arg) || "/h".equals(arg) || "-?".equals(arg) || "-h".equals(arg) || "--help".equals(arg)) {
                usage();
                return;
            } else {
                usage();
                return;
            }
            i++;
        }
        Server server = new Server(port);
        Context root = new Context(server, "/", Context.SESSIONS);
        root.addFilter(new FilterHolder(new SceneryFilter()), "/*", Handler.DEFAULT);
        root.addServlet(new ServletHolder(new BrowseServlet()), "/browse.do");
        root.addServlet(new ServletHolder(new ConfigServlet()), "/*");
        server.start();
    }

    private static void usage() {
        System.err.println("Usage: sm [-l port] [-p path] [-u url]\n" + "Where: \n" + "\t-l\tlistening port. Default = 8080\n" + "\t-p\tpath to target webapp. You can see and config that at config.do\n" + "\t-u\tURL to be accessed if a file isn't found on the given <path> (see -p). Default = file://<path>");
    }
}
