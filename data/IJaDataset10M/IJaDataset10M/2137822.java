package es.caib.regweb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.TimeZone;
import java.util.Vector;
import javax.ejb.CreateException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import org.apache.log4j.Category;

/**
 * Ejb per esborrar els registres antics de totes les taules de registre d'accessos 
 * @author  Sebastià Matas
 * @version 1.0
 */
public class EsborraRegAnticsLopdBean implements SessionBean {

    private static final Category log = Category.getInstance(EsborraRegAnticsLopdBean.class.getName());

    private SessionContext sessioEjb;

    /** Crea una nova instància de EsborraRegAnticsLopd */
    public EsborraRegAnticsLopdBean() {
    }

    /** 
	 * Esborram els registres antics del log de la LOPD
	 * @param taula a esborrar
	 * @param camp de data a emprar per filtrar
	 */
    private void esborraDadesAntiguesLOPDEntrades(String nomTaula, String campData) throws java.rmi.RemoteException {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        ResultSet rsHist = null;
        PreparedStatement psHist = null;
        Vector registrosVector = new Vector();
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        String DATE_FORMAT = "yyyyMMdd";
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(DATE_FORMAT);
        sdf.setTimeZone(TimeZone.getDefault());
        log.debug("Ara: " + sdf.format(cal.getTime()));
        GregorianCalendar calendario = new GregorianCalendar();
        calendario.add(Calendar.DATE, -(365 * 2 + 1));
        log.info("Taula de la que s'esborraran els registres antics: " + nomTaula);
        log.info("Data a esborrar: " + Integer.parseInt(sdf.format(calendario.getTime())));
        nomTaula = nomTaula.toUpperCase();
        campData = campData.toUpperCase();
        int contador = 1;
        String sentenciaSql = "SELECT * FROM " + nomTaula + " WHERE FZ" + campData + "DATAC<? " + "ORDER BY FZ" + campData + "DATAC, FZ" + campData + "HORAC, FZ" + campData + "ANOEN, FZ" + campData + "NUMEN ";
        log.debug(sentenciaSql);
        try {
            conn = ToolsBD.getConn();
            ps = conn.prepareStatement(sentenciaSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ps.setInt(contador++, Integer.parseInt(sdf.format(calendario.getTime())));
            rs = ps.executeQuery();
            while (rs.next()) {
                DateFormat yyyymmdd = new SimpleDateFormat("yyyyMMdd");
                DateFormat ddmmyyyy = new SimpleDateFormat("dd/MM/yyyy");
                java.util.Date fechaEESS = null;
                RegistroESlopdSeleccionado registro = new RegistroESlopdSeleccionado();
                registro.setTipusAcces(rs.getString("FZ" + campData + "TIPAC"));
                registro.setUsuCanvi(rs.getString("FZ" + campData + "CUSU"));
                String fechaES = String.valueOf(rs.getInt("FZ" + campData + "DATAC"));
                try {
                    fechaEESS = yyyymmdd.parse(fechaES);
                    registro.setDataCanvi(ddmmyyyy.format(fechaEESS));
                } catch (Exception e) {
                    registro.setDataCanvi(fechaES);
                }
                registro.setHoraCanvi(parsejarHora(String.valueOf(rs.getInt("FZ" + campData + "HORAC"))));
                registro.setNombreRegistre(String.valueOf(rs.getInt("FZ" + campData + "NUMEN")));
                registro.setAnyRegistre(String.valueOf(rs.getInt("FZ" + campData + "ANOEN")));
                registro.setOficinaRegistre(String.valueOf(rs.getInt("FZ" + campData + "CAGCO")));
                String textoOficina = null;
                String sentenciaSqlHistOfi = "SELECT * FROM BHAGECO01 WHERE FHACAGCO=? AND FHAFALTA<=? " + "AND ( (FHAFBAJA>= ? AND FHAFBAJA !=0) OR FHAFBAJA = 0)";
                psHist = conn.prepareStatement(sentenciaSqlHistOfi);
                psHist.setString(1, String.valueOf(rs.getInt("FZ" + campData + "CAGCO")));
                psHist.setString(2, fechaES);
                psHist.setString(3, fechaES);
                rsHist = psHist.executeQuery();
                if (rsHist != null) rsHist.close();
                if (psHist != null) psHist.close();
                registro.setOficinaRegistreDesc(textoOficina);
                log.info("Eliminant registre: " + registro.toString());
                registrosVector.addElement(registro);
            }
            rs.close();
            ps.close();
            contador = 1;
            sentenciaSql = "DELETE FROM " + nomTaula + " WHERE FZ" + campData + "DATAC<? ";
            log.debug(sentenciaSql);
            ps = conn.prepareStatement(sentenciaSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ps.setInt(contador++, Integer.parseInt(sdf.format(calendario.getTime())));
            log.info("Esborrats " + ps.executeUpdate() + " registres de la taula " + nomTaula);
        } catch (Exception e) {
            log.error("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ToolsBD.closeConn(conn, ps, rs);
        }
    }

    /** 
	 * Parsejam d'hora agafada de BBDD
	 * @param hora En format String
	 * @return Hora parsejada amb el format: HH:MM:SS:sss
	 */
    private String parsejarHora(String hora) {
        switch(hora.length()) {
            case 9:
                return hora.substring(0, 2) + ":" + hora.substring(2, 4) + ":" + hora.substring(4, 6) + ":" + hora.substring(6);
            case 8:
                return "0" + hora.substring(0, 1) + ":" + hora.substring(1, 3) + ":" + hora.substring(3, 5) + ":" + hora.substring(5);
            case 7:
                return "00:" + hora.substring(0, 2) + ":" + hora.substring(2, 4) + ":" + hora.substring(4);
            case 6:
                return "00:0" + hora.substring(0, 1) + ":" + hora.substring(1, 3) + ":" + hora.substring(3);
            case 5:
                return "00:00:" + hora.substring(0, 2) + ":" + hora.substring(2);
            case 4:
                return "00:00:0" + hora.substring(0, 1) + ":" + hora.substring(1);
            case 1:
            case 2:
            case 3:
                return "00:00:00" + hora;
        }
        return hora;
    }

    public void esborraDadesAntiguesLOPD() throws java.rmi.RemoteException {
        log.debug("Timout expired!");
        log.debug("Aquí hauriem d'esborrar els registres antics de les taules de registre d'accessos.!");
        esborraDadesAntiguesLOPDEntrades("BZENLPD", "T");
        esborraDadesAntiguesLOPDEntrades("BZSALPD", "U");
        esborraDadesAntiguesLOPDEntrades("BZMOLPD", "V");
    }

    public void ejbCreate() throws CreateException {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public void ejbRemove() {
    }

    public void setSessionContext(SessionContext ctx) {
        sessioEjb = ctx;
    }
}
