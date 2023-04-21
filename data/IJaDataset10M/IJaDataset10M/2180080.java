package org.stars.daostars.core.runtime;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.stars.dao.exception.DaoException;
import org.stars.daostars.core.PropertyDescriptorEx;
import org.stars.daostars.core.SqlParameter;
import org.stars.daostars.core.SqlParameterType;
import org.stars.database.DatabaseInformation;
import org.stars.database.connectionpool.ConnectionHelper;
import org.stars.security.UserIdentity;
import java.beans.MethodDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.List;

/**
 * 
 * 
 * @author Francesco Benincasa (908099)
 * @date 05/nov/07, 18:17:14
 * 
 */
public abstract class DaoRuntimeHelper {

    /**
	 * Logger
	 * 
	 * @return logger
	 */
    protected static Log getLog() {
        return LogFactory.getLog(DaoRuntimeHelper.class);
    }

    /**
	 * Recupera le informazioni relative alla connessione utilizzata.
	 * Nel caso di errore viene restituito un descrittore con informazioni
	 * non valide. Non da mai eccezione
	 * 
	 * @param conn
	 * 		connessione aperta
	 * @return
	 * 		DatabaseInformation
	 */
    public static DatabaseInformation getDatabaseInformation(Connection conn) {
        DatabaseInformation ret = new DatabaseInformation();
        try {
            ret = ConnectionHelper.getDatabaseInformation(conn);
        } catch (Exception e) {
        }
        return ret;
    }

    /**
	 * Calls the setter method on the target object for the given property. If
	 * no setter method exists for the property, this method does nothing.
	 * 
	 * @param prop
	 *            The property to set.
	 * @param args
	 *            The value to pass into the setter.
	 * @throws Exception
	 *             if an error occurs setting the property.
	 * @return value read
	 */
    protected static Object callStaticMethod(MethodDescriptor prop, Object[] args) throws Exception {
        Method method = prop.getMethod();
        if (method == null) {
            return null;
        }
        try {
            Object result;
            if (args.length == 1) {
                result = method.invoke(null, args[0]);
            } else {
                result = method.invoke(null, args[0], args[1]);
            }
            return result;
        } catch (IllegalArgumentException e) {
            throw new DaoException("Cannot invoke method " + prop.getName() + ": " + e.getMessage());
        } catch (IllegalAccessException e) {
            throw new DaoException("Cannot invoke method " + prop.getName() + ": " + e.getMessage());
        } catch (InvocationTargetException e) {
            throw new DaoException("Cannot invoke method " + prop.getName() + ": " + e.getMessage());
        }
    }

    /**
	 * Restituisce il valore dell'attributo dello user che attualmente sta
	 * utilizzando il dao. Nel caso in cui il dao non è utilizzato da nessun
	 * user, viene restituito <code>null</code>.
	 * 
	 * @param nomeAttributo
	 *            nome dell'attributo.
	 * @return valore dell'attributo o null.
	 */
    protected static Object getUserAttribute(UserIdentity user, String nomeAttributo) {
        if (user == null) return null;
        return user.getAttribute(nomeAttributo);
    }

    /**
	 * Dato un array ed un indice, recupera l'elemento a quel dato indice. Nel
	 * caso tale indice non esista, viene restituito <code>null</code>.
	 * 
	 * @param array
	 *            array dei valori
	 * @param index
	 *            indice da usare
	 * @return valore dell'elemento da recuperare
	 * @throws Exception
	 *             in caso di errore
	 */
    protected static Object getValueInArray(Object[] array, int index) throws Exception {
        if (index < array.length) return array[index];
        return null;
    }

    /**
	 * dato l'elenco dei parametri
	 * 
	 * @param properties
	 * @param params
	 * @return array di int contente gli indici degli attributi
	 * @throws Exception
	 *             in caso di errore
	 */
    protected static int[] mapAttributeToMethod(PropertyDescriptorEx[] properties, List<SqlParameter> params, String sQueryName) throws Exception {
        int indice = 0;
        int[] ret = new int[params.size()];
        for (SqlParameter param : params) {
            ret[indice] = -1;
            if (param.getType() != SqlParameterType.TYPE_ATTRIBUTE) {
                indice++;
                continue;
            }
            String sNomeFunzione = param.getAttribute();
            for (int i = 0; i < properties.length; i++) {
                String sNomeMetodo = properties[i].getName();
                if (sNomeMetodo.equalsIgnoreCase(sNomeFunzione)) {
                    ret[indice] = i;
                    break;
                }
            }
            if (ret[indice] == -1) {
                String msg = "Attribute " + sNomeFunzione + " doesn't exist in the bean used as argument of query " + sQueryName;
                Log log = getLog();
                log.error(msg);
                throw (new DaoException(msg));
            }
            indice++;
        }
        return ret;
    }

    /**
	 * @param properties
	 * @param params
	 * @return array
	 * @throws Exception
	 *             in caso di errore
	 */
    protected static int[] mapFunctionToMethod(MethodDescriptor[] properties, List<SqlParameter> params, String sQueryName) throws Exception {
        int indice = 0;
        int[] ret = new int[params.size()];
        for (SqlParameter param : params) {
            ret[indice] = -1;
            String sNomeFunzione = param.getFunction();
            if (sNomeFunzione.length() == 0) {
                indice++;
                continue;
            }
            for (int i = 0; i < properties.length; i++) {
                if (properties[i].getName().equalsIgnoreCase(sNomeFunzione)) {
                    ret[indice] = i;
                    break;
                }
            }
            if (ret[indice] == -1) {
                String msg = "Can not found method " + sNomeFunzione + " for the creation of the query " + sQueryName;
                Log log = getLog();
                log.error(msg);
                throw (new DaoException(msg));
            }
            indice++;
        }
        return ret;
    }
}
