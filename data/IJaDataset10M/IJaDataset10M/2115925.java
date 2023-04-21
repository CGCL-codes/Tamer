package net.sourceforge.jdbcutils.handlers;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import net.sourceforge.jdbcutils.ResultSetHandler;

/**
 * Trasformatore della prima di riga di un <code>java.sql.ResultSet</code> in
 * un <code>java.util.Map</code>
 * 
 * @author <a href="mailto:simone.tripodi@gmail.com">Simone Tripodi</a>
 * @version $Id: FirstRow2Map.java,v 1.1 2006/08/10 09:48:07 stripodi Exp $
 */
public final class FirstRow2Map implements ResultSetHandler {

    /**
	 * Trasforma la prima di riga di un <code>java.sql.ResultSet</code> in un
	 * <code>java.util.Map</code><br/> Viene usato principalmente all'interno
	 * di <code>net.sourceforge.jdbcutils.ParaStatement</code> per processare
	 * le chiavi che vengono generate automaticamente in fase di update
	 * 
	 * @param rs
	 *            Il <code>java.sql.ResultSet</code> da quale estrarre i dati
	 * @return Un <code>java.util.Map</code> contenente gli oggetti della
	 *         prima riga del <code>java.sql.ResultSet</code>.<br/> Se
	 *         quest'ultimo dovessere essere <code>null</code> o non ha dati
	 *         al suo interno, il <code>java.util.Map</code> sar&agrave; vuoto
	 * @throws SQLException
	 */
    public Object handle(ResultSet rs) throws SQLException {
        Map keys = new HashMap();
        if (rs.next()) {
            ResultSetMetaData rsMeta = rs.getMetaData();
            for (int i = 1; i <= rsMeta.getColumnCount(); i++) {
                keys.put(rsMeta.getColumnName(i), rs.getObject(i));
            }
        }
        return keys;
    }
}
