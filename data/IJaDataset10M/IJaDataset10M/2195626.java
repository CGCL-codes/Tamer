package com.nitbcn.service.cont;

import com.nitbcn.lib.bd.AccesSQL;
import com.nitbcn.lib.utils.HttpValidator;
import com.nitbcn.lib.bus.Source;
import com.nitbcn.lib.dao.DAOSource;
import com.nitbcn.lib.xml.NitbcnXMLDocument;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Raimon Bosch
 */
public class ActionModifySource extends Action {

    /** Creates a new instance of ActionModifySource */
    public ActionModifySource(HttpServletRequest request, NitbcnXMLDocument xml_response) {
        super(request, xml_response);
    }

    @Override
    public int performanceAction() {
        String date, url, name, title, type, source;
        boolean correct_source = true;
        String reply = "";
        date = request.getParameter("date");
        url = request.getParameter("url");
        name = request.getParameter("name");
        title = request.getParameter("title");
        type = request.getParameter("type");
        source = request.getParameter("source");
        if (source == null) source = "0";
        if (date != null && !date.equals("") && url != null && !url.equals("") && name != null && !name.equals("") && type != null && !type.equals("0") && source != null && !source.equals("0")) {
            int val = HttpValidator.validate(url);
            if (val != 1) {
                reply += "Se produjo una excepcion validando la url del club. La url podria estar mal formada o el codigo de respuesta HTTP ser incorrecto. Codigo error: " + val + ".\n";
                correct_source = false;
            }
            if (correct_source) {
                Source s = new Source(date, name, title, url, type);
                DAOSource ds = new DAOSource(new AccesSQL());
                int code = 0;
                try {
                    code = ds.updateSource(s, Integer.parseInt(source));
                } catch (NumberFormatException ex) {
                    reply += "Error de formato en el id de la fuente.\n";
                    correct_source = false;
                }
                if (code == 1) {
                    reply += "Fuente modificada correctamente.\n";
                } else {
                    reply += "Error durante la modificacion de la fuente.\n";
                    correct_source = false;
                }
            }
        } else {
            correct_source = false;
        }
        if (!correct_source) {
            xml_response.addChild("date", date);
            xml_response.addChild("name", name);
            xml_response.addChild("url", url);
            xml_response.addChild("title", title);
            xml_response.addChild("type", type);
            reply += "Faltan datos en el formulario.\n";
        } else {
            xml_response.addChild("date", "");
            xml_response.addChild("name", "");
            xml_response.addChild("url", "");
            xml_response.addChild("title", "");
            xml_response.addChild("type", "");
        }
        xml_response.setMessage(reply);
        return 0;
    }
}
