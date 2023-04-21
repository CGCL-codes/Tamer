package org.elf.weblayer.controllers;

import java.util.*;
import org.elf.common.*;
import org.elf.datalayer.*;
import org.elf.datalayer.kernel.impl.security.*;
import org.elf.datalayer.kernel.services.security.*;
import org.elf.weblayer.*;
import org.elf.weblayer.dictionary.*;

/**
 *
 * @author  <a href="mailto:logongas@users.sourceforge.net">Lorenzo Gonz�lez</a>
 */
public class ControllerDLSession extends Controller {

    String _defaultConnection;

    WebConnections _webConnections;

    public ControllerDLSession() {
        _webConnections = createWebConnections();
        _defaultConnection = WLSession.getDictionary().getDefWebConnections().getDefaultWebConnectionName();
    }

    public void debug(String msg) {
        DLSession.getLogger().debug(msg);
    }

    public RecordSet executeQuery(String webCommand, Parameters parameters) {
        return DLSession.getConnection().executeQuery(getDLCommandFromWLCommand(webCommand, parameters), parameters);
    }

    public PageableRecordSet executePageableQuery(String webCommand, Parameters parameters, long pageSize) {
        PageableRecordSet prst = DLSession.getConnection().executePageableQuery(getDLCommandFromWLCommand(webCommand, parameters), parameters, (int) pageSize);
        String key = this.getClass().getName() + "." + prst.getUID();
        WLSession.getApplicationData().setData(key, prst);
        return prst;
    }

    public PageableRecordSet setPageableQuery(PageableRecordSet prst) {
        String key = this.getClass().getName() + "." + prst.getUID();
        WLSession.getApplicationData().setData(key, prst);
        return prst;
    }

    public List getRecordSetPage(String recordSetID, long numPage) {
        String key = this.getClass().getName() + "." + recordSetID;
        PageableRecordSet prst = (PageableRecordSet) WLSession.getApplicationData().getData(key);
        prst.gotoBeginningOfPage((int) numPage);
        ArrayList<Object> rows = new ArrayList<Object>();
        int numRows = 0;
        while (prst.next() && (numRows < prst.getPageSize())) {
            ArrayList<Object> row = new ArrayList<Object>();
            for (int i = 0; i < prst.getColumnCount(); i++) {
                row.add(prst.getObject(i));
            }
            rows.add(row);
            numRows++;
        }
        return rows;
    }

    public void closePageableRecordSet(String recordSetID) {
        String key = this.getClass().getName() + "." + recordSetID;
        PageableRecordSet prst = (PageableRecordSet) WLSession.getApplicationData().getData(key);
        prst.close();
        prst = null;
        WLSession.getApplicationData().removeData(key);
    }

    public UserIdentification createSession(String login, String password) {
        AuthenticatorImplUserPassword aiup = new AuthenticatorImplUserPassword(login, password);
        Session session;
        session = DLSession.getDLSystem().createSession(aiup);
        if (session != null) {
            getHttpServletRequest().getSession().invalidate();
            getHttpServletRequest().getSession(true).setAttribute(Session.class.getName(), session);
            return session.getUserIdentification();
        } else {
            return null;
        }
    }

    public UserIdentification destroyCurrentSession() {
        Session session = (Session) getHttpServletRequest().getSession().getAttribute(Session.class.getName());
        if (session != null) {
            DLSession.getDLSystem().destroySession(session);
        }
        getHttpServletRequest().getSession().invalidate();
        session = DLSession.getDLSystem().createSessionPublic();
        getHttpServletRequest().getSession(true).setAttribute(Session.class.getName(), session);
        return session.getUserIdentification();
    }

    public Object getConfigValue(String name) {
        return DLSession.getConfig().getValue(name);
    }

    public void setConfigValue(String name, Object value) {
        DLSession.getConfig().setValue(name, value);
    }

    /**
     * Transforma la SQL que se usa en la WebLayer a una SQL que usa la DataLayer
     * Esto se hace para evitar fallo de seguridad
     */
    private String getDLCommandFromWLCommand(String webCommand, Parameters parameters) {
        WebConnection webConnection = getWebConnection(webCommand);
        return webConnection.getDLCommandFromWLCommand(getRealWebCommand(webCommand), parameters);
    }

    /**
    * Dado un comando obtiene la conexi�n sobre la que tiene que ejecutarse.
    * <p>
    * Por defecto todos los comandos se ejecutar� sobre la conexi�n por defecto.
    * Para seleccionar la conexi�n que se desea utilizar es necesario
    * prejijar al comando el siguiente String:
    * <p>
    * (connecionName)
    * <p>
    * Donde <b>connectionName</b> ser� el nombre de la conexi�n sobre la que
    * se ejecutar� el comando.
    * @param command Comando del que se obtiene la conexi�n
    * @return Conexi�n sobre la que se ejecutar� el comado
    */
    private WebConnection getWebConnection(String webCommand) {
        WebConnection webConnection;
        String trimCommand = webCommand.trim();
        int fin = webCommand.indexOf(")");
        String connectionName;
        if (trimCommand.charAt(0) == '(') {
            connectionName = webCommand.substring(1, fin);
            if (connectionName.trim().length() == 0) {
                connectionName = _defaultConnection;
            }
            if ((connectionName == null) || connectionName.trim().equals("")) {
                throw new RuntimeException("El nombre de la WebConnection no puede estar vacio.Deb indicar un nombre o usar la de por defecto:" + webCommand);
            }
        } else {
            connectionName = _defaultConnection;
            if ((connectionName == null) || connectionName.trim().equals("")) {
                throw new RuntimeException("No existe la WebConnection por defecto.Debe indicar el nombre de la conexi�n en la Query:" + webCommand);
            }
        }
        webConnection = _webConnections.getWebConnection(connectionName);
        if (webConnection == null) {
            throw new RuntimeException("No existe la WebConnection:" + connectionName + " en la Query:" + webCommand);
        }
        return webConnection;
    }

    /**
     * Dado un comando retorna el propia comando pero eliminadole el prefijo
     * de la conexi�n Web a utilizar.
     * <p>
     * Por defecto todos los comandos se ejecutar� sobre la conexi�n Web por defecto.
     * Para seleccionar la conexi�n que se desea utilizar es necesario
     * prejijar al comando el siguiente String:
     * <p>
     * (connecionName)
     * <p>
     * Donde <b>connectionName</b> ser� el nombre de la conexi�n sobre la que
     * se ejecutar� el comando.
     * 
     * @param webCommand Comando de las conexiones Web
     * @return String con el comando pero ya no contiene el prefijo de la conexi�n
     */
    private String getRealWebCommand(String webCommand) {
        String realWebCommand;
        if (webCommand.trim().charAt(0) == '(') {
            realWebCommand = webCommand.substring(webCommand.indexOf(")") + 1, webCommand.length());
        } else {
            realWebCommand = webCommand;
        }
        return realWebCommand.trim();
    }

    private WebConnections createWebConnections() {
        DefWebConnections defWebConnections = WLSession.getDictionary().getDefWebConnections();
        WebConnections webConnections = new WebConnections();
        Iterator it = defWebConnections.values().iterator();
        while (it.hasNext()) {
            DefWebConnection defWebConnection = (DefWebConnection) it.next();
            WebConnection webConnection;
            try {
                webConnection = (WebConnection) Class.forName(defWebConnection.getFQCN()).newInstance();
            } catch (Exception ex) {
                throw new RuntimeException("No se ha podido crear la instancia de la clase:" + defWebConnection.getFQCN());
            }
            BeanUtil.loadBeanPrimitivePropertiesFromMap(defWebConnection.getParameters(), webConnection);
            webConnections.addWebConnection(defWebConnection.getName(), webConnection);
        }
        return webConnections;
    }
}
