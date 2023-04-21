package Tflame;

import java.util.Hashtable;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import traitmap.login.UserInformation;
import Tflame.Abstracts.BaseActionAbstract;

/** 
 * TraitMap�f�[�^�x�[�X�y�щ{���V�X�e��, �Q�O�O�R�N�P��<br>
 * �S���ҁF�L�c�N�Y�A�Q�m���m���x�[�X�����J���`�[���E�C���t�H�}�e�B�N�X��Վ{�݁EGSC�ERIKEN
 * 
 * @version 2.0
 * @author isobe
 */
public final class MainServlet extends HttpServlet {

    static Logger log = Logger.getLogger(MainServlet.class);

    public void init() {
        ServletContext sc = getServletContext();
        Garage.setServletContext(sc);
        Document configDoc = Utils.getDocFile("/WEB-INF/Tconfig.xml");
        Garage.setConfigDoc(configDoc);
    }

    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        String loginKey = getServletContext().getInitParameter("LOGIN");
        UserInformation info = (UserInformation) request.getSession().getAttribute(loginKey);
        String req_uri = request.getRequestURI();
        FormBean fb = new FormBean();
        try {
            fb.initFormBean(req_uri, request, response);
            fb.setUserInformation(info);
            String[] actionNames = Garage.getActions(req_uri);
            fb = this.invokeActions(actionNames, fb);
            String outputType = Garage.getOutputType(req_uri);
            Viewer vw = new Viewer();
            vw.output(fb, outputType);
        } catch (Exception e) {
            log.error(e);
            fb.setErrorLog("Tf.Ms.e2", e);
        }
        fb.writeErrorLog();
    }

    private FormBean invokeActions(String[] actionNames, FormBean fb) {
        for (int i = 0; i < actionNames.length; i++) {
            String type = Garage.getType(actionNames[i]);
            if (type.equals("Base")) {
                String _class = Garage.getClass(actionNames[i]);
                try {
                    Class cl = Class.forName(_class);
                    BaseActionAbstract ba = (BaseActionAbstract) cl.newInstance();
                    fb = ba.Action(fb);
                } catch (NullPointerException npe) {
                    log.error("error at: " + _class, npe);
                } catch (Exception e) {
                    log.error("error at: " + _class, e);
                    fb.setErrorLog("Tf.Ms.e3", e);
                }
            } else if (type.equals("Xsl")) {
                Hashtable paramTable = Garage.getXslParamTable(actionNames[i]);
                String _class = Garage.getClass(actionNames[i]);
                XslAction xsla = new XslAction();
                fb = xsla.Action(_class, paramTable, fb);
            } else if (type.equals("Xsd")) {
                String _class = Garage.getClass(actionNames[i]);
                ValidaterAction val = new ValidaterAction(_class, fb);
                if (!val.validate()) {
                    fb = val.getErrorFormBean();
                    return fb;
                }
            }
        }
        return fb;
    }
}
