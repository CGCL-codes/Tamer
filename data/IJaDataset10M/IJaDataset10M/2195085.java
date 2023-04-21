package com.soode.openospc.velocity;

import java.util.Hashtable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.velocity.Template;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.context.Context;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.servlet.VelocityServlet;
import com.soode.openospc.properties.ColorCode;
import com.soode.openospc.security.Module;
import com.soode.openospc.util.HtmlInput;

public class ColorCodeManager extends VelocityServlet {

    public Template handleRequest(HttpServletRequest request, HttpServletResponse response, Context context) throws ResourceNotFoundException, ParseErrorException, Exception {
        Template template = null;
        HttpSession session = request.getSession();
        if ((session.getAttribute("user") != null) && (session.getAttribute("roleId") != null) && (session.getAttribute("logId") != null)) {
            Hashtable hUser = (Hashtable) session.getAttribute("user");
            context.put("user", hUser);
            if (request.getParameter("c") != null) {
                String c = request.getParameter("c");
                if (c.equals("addColor")) {
                    ColorCode cc = new ColorCode();
                    String sql = "INSERT INTO ColorCode ( RValue, " + "GValue, " + "BValue, " + "Name, " + "Hexacode) Values (" + "'" + request.getParameter("rValue") + "', " + "'" + request.getParameter("gValue") + "', " + "'" + request.getParameter("bValue") + "', " + "'" + request.getParameter("colorName") + "', " + "'" + request.getParameter("hexacode") + "') ";
                    cc.add(sql);
                    HtmlInput hi = new HtmlInput();
                    context.put("inputList", hi.getFieldList());
                    context.put("urlForward", "color");
                    template = Velocity.getTemplate("specificationmanager/forward.vm");
                } else if (c.equals("adsdColor")) {
                }
            } else {
                ColorCode cc = new ColorCode();
                context.put("colorList", cc.getColorList());
                template = Velocity.getTemplate("colorcodemanager/shwAddColor.vm");
            }
        }
        return template;
    }
}
