package org.dcm4chex.archive.ejb.session;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.dcm4cheri.util.StringUtils;
import org.dcm4chex.archive.ejb.interfaces.FileDTO;
import org.dcm4chex.archive.ejb.interfaces.FileLocal;
import org.dcm4chex.archive.ejb.interfaces.FileLocalHome;

/**
 * 
 * @author gunter.zeilinger@tiani.com
 * @version $Revision: 1003 $ $Date: 2004-02-21 17:27:17 -0500 (Sat, 21 Feb 2004) $
 * @since 14.01.2004
 * 
 * @ejb.bean
 *  name="PurgeFile"
 *  type="Stateless"
 *  view-type="remote"
 *  jndi-name="ejb/PurgeFile"
 * 
 * @ejb.transaction-type 
 *  type="Container"
 * 
 * @ejb.transaction 
 *  type="Required"
 * 
 * @ejb.ejb-ref
 *  ejb-name="File" 
 *  view-type="local"
 *  ref-name="ejb/File" 
 * 
 */
public abstract class PurgeFileBean implements SessionBean {

    private FileLocalHome fileHome;

    public void setSessionContext(SessionContext arg0) throws EJBException, RemoteException {
        Context jndiCtx = null;
        try {
            jndiCtx = new InitialContext();
            fileHome = (FileLocalHome) jndiCtx.lookup("java:comp/env/ejb/File");
        } catch (NamingException e) {
            throw new EJBException(e);
        } finally {
            if (jndiCtx != null) {
                try {
                    jndiCtx.close();
                } catch (NamingException ignore) {
                }
            }
        }
    }

    public void unsetSessionContext() {
        fileHome = null;
    }

    /**
     * @ejb.interface-method
     */
    public void deleteFile(int file_pk) throws RemoteException, EJBException, RemoveException {
        fileHome.remove(new Integer(file_pk));
    }

    /**
     * @ejb.interface-method
     */
    public FileDTO[] findDereferencedFiles(String retrieveAETs) throws FinderException {
        Collection c = fileHome.findDereferenced();
        if (c.isEmpty()) {
            return new FileDTO[0];
        }
        Collection retrieveAETList = Arrays.asList(StringUtils.split(retrieveAETs, '\\'));
        Collection retval = new ArrayList(c.size());
        for (Iterator it = c.iterator(); it.hasNext(); ) {
            FileLocal file = (FileLocal) it.next();
            HashSet set = new HashSet(Arrays.asList(StringUtils.split(file.getRetrieveAETs(), '\\')));
            set.retainAll(retrieveAETList);
            if (!set.isEmpty()) {
                retval.add(file.getFileDTO());
            }
        }
        return (FileDTO[]) retval.toArray(new FileDTO[retval.size()]);
    }
}
