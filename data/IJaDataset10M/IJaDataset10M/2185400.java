package net.sf.fileexchange.util.http;

import java.io.PrintStream;
import net.sf.fileexchange.util.http.ResourceBuilder.GeneratedResource;

public class ErrorResource {

    public static GeneratedResource create(ResponseCode code, String message) {
        ResourceBuilder builder = new ResourceBuilder();
        builder.setContentType("text/html");
        final PrintStream out = builder.getPrintStream();
        out.append("<html>");
        out.append("<head>");
        out.append("<title>");
        out.append(code.getName());
        out.append("</title>");
        out.append("<head>");
        out.append("<body>");
        out.append("<h1>");
        out.append("<center>");
        out.append("HTTP-Error " + code.getCode() + ": " + code.getName());
        out.append("</center>");
        out.append("</h1>");
        if (message != null) {
            out.append("<center>");
            out.append(message);
            out.append("</center>");
        }
        out.append("</body>");
        out.append("</html>");
        return builder.build();
    }
}
