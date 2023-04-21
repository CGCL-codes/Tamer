package org.apache.ws.jaxme.pm.impl;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import javax.xml.bind.DatatypeConverter;
import javax.xml.bind.JAXBException;
import org.apache.ws.jaxme.JMManager;
import org.apache.ws.jaxme.Observer;
import org.apache.ws.jaxme.PM;
import org.apache.ws.jaxme.PMException;
import org.apache.ws.jaxme.PMParams;

/** <p>Abstract base class for persistence managers.</p>
 *
 * @author <a href="mailto:joe@ispsoft.de">Jochen Wiedmann</a>
 */
public abstract class PMImpl implements PM {

    private JMManager manager;

    /** <p>Creates a new instance of PMImpl.java.</p>
   */
    public PMImpl() {
    }

    public void init(JMManager pManager) throws JAXBException {
        manager = pManager;
    }

    public JMManager getManager() {
        return manager;
    }

    public void select(Observer pObserver, String pQuery) throws JAXBException {
        select(pObserver, pQuery, null);
    }

    public Iterator select(String pQuery) throws JAXBException {
        return select(pQuery, null);
    }

    /** The <code>ListObserver</code> is a basic
   * {@link Observer}, which collects result objects
   * in a {@link List}.
   */
    public static class ListObserver implements Observer {

        private List list;

        /** Creates a new <code>ListObserver</code>, which writes
     * the received objects into the {@link List}
     * <code>pList</code>.
     */
        public ListObserver(List pList) {
            list = pList;
        }

        public void notify(Object pObservable) {
            list.add(pObservable);
        }
    }

    public Iterator select(String pQuery, PMParams pPlaceHolderArgs) throws JAXBException {
        List result = new ArrayList();
        ListObserver observer = new ListObserver(result);
        select(observer, pQuery, pPlaceHolderArgs);
        return result.iterator();
    }

    public Object create() throws JAXBException {
        return manager.getElementJ();
    }

    protected String parseQuery(String pQuery, PMParams pPlaceHolderArgs) throws PMException {
        if (pPlaceHolderArgs == null) {
            return pQuery;
        }
        Iterator iter = pPlaceHolderArgs.getParams();
        if (!iter.hasNext()) {
            return pQuery;
        }
        StringBuffer sb = new StringBuffer();
        boolean inStr = false;
        char delim = 0;
        for (int i = 0; i < pQuery.length(); i++) {
            char c = pQuery.charAt(i);
            if (inStr) {
                if (c == delim) {
                    inStr = false;
                }
                sb.append(c);
            } else {
                switch(c) {
                    case '\'':
                        inStr = true;
                        delim = c;
                        sb.append(c);
                        break;
                    case '?':
                        if (!iter.hasNext()) {
                            throw new PMException("Number of placeholder marks exceeds number of actual parameters");
                        }
                        PMParams.Param param = (PMParams.Param) iter.next();
                        switch(param.getType()) {
                            case Types.VARCHAR:
                                sb.append('\'');
                                sb.append(param.getValue());
                                sb.append('\'');
                                break;
                            case Types.BIGINT:
                            case Types.INTEGER:
                            case Types.SMALLINT:
                            case Types.TINYINT:
                                sb.append(param.getValue());
                                break;
                            case Types.TIMESTAMP:
                                sb.append('\'');
                                sb.append(DatatypeConverter.printDateTime((Calendar) param.getValue()));
                                sb.append('\'');
                                break;
                            case Types.DATE:
                                sb.append('\'');
                                sb.append(DatatypeConverter.printDate((Calendar) param.getValue()));
                                sb.append('\'');
                                break;
                            case Types.TIME:
                                sb.append('\'');
                                sb.append(DatatypeConverter.printTime((Calendar) param.getValue()));
                                sb.append('\'');
                                break;
                            default:
                                throw new PMException("Invalid parameter type: " + param.getType());
                        }
                        break;
                    default:
                        sb.append(c);
                        break;
                }
            }
        }
        if (inStr) {
            throw new PMException("Failed to parse query, expected trailing " + delim + " character: " + pQuery);
        }
        if (iter.hasNext()) {
            throw new PMException("Number of actual parameters exceeds number of placeholder marks.");
        }
        return sb.toString();
    }
}
