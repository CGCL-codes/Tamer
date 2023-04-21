package com.increg.serveur.servlet;

import com.increg.commun.*;
import com.increg.commun.exception.NoDatabaseException;
import javax.servlet.*;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

/**
 * Compteur de connexion et gestion de la connexion � la base de donn�es
 * Creation date: (31/12/2001 18:26:48)
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class CountSession implements HttpSessionBindingListener {

    /**
     * Contexte 
	 */
    protected javax.servlet.ServletContext srvCtxt;

    /**
     * CountSession constructor comment.
     */
    public CountSession() {
        super();
    }

    /**
     * CountSession constructor comment.
     * @param newSrvCtxt Nouveau contexte
     */
    public CountSession(ServletContext newSrvCtxt) {
        super();
        setSrvCtxt(newSrvCtxt);
    }

    /**
     * Insert the method's description here.
     * Creation date: (31/12/2001 19:20:24)
     * @return javax.servlet.ServletContext
     */
    public javax.servlet.ServletContext getSrvCtxt() {
        return srvCtxt;
    }

    /**
     * Insert the method's description here.
     * Creation date: (31/12/2001 19:20:24)
     * @param newSrvCtxt javax.servlet.ServletContext
     */
    public void setSrvCtxt(javax.servlet.ServletContext newSrvCtxt) {
        srvCtxt = newSrvCtxt;
    }

    /**
     * valueBound method comment.
     * @param arg1 Ev�nement
     */
    public synchronized void valueBound(HttpSessionBindingEvent arg1) {
        Integer count = (Integer) srvCtxt.getAttribute("Count");
        if (count == null) {
            count = new Integer(0);
            DBSession dbConnect;
            try {
                dbConnect = new DBSession();
                srvCtxt.setAttribute("dbSession", dbConnect);
            } catch (NoDatabaseException e) {
                e.printStackTrace();
            }
        }
        srvCtxt.setAttribute("Count", new Integer(count.intValue() + 1));
    }

    /**
     * valueUnbound method comment.
     * @param arg1 Ev�nement
     */
    public synchronized void valueUnbound(HttpSessionBindingEvent arg1) {
        Integer count = (Integer) srvCtxt.getAttribute("Count");
        if ((count == null) || (count.intValue() <= 1)) {
            srvCtxt.removeAttribute("dbSession");
            srvCtxt.removeAttribute("Count");
        } else {
            srvCtxt.setAttribute("Count", new Integer(count.intValue() - 1));
        }
    }
}
