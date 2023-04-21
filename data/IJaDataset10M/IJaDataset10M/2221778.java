package db.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Marc Rohe, 06.05.2007
 */
public class UmsatzDTO {

    private int jahr;

    private double wert;

    private int fkFid;

    /**
     * Konstruktor mit Einzel-Parametern:
     * @param jahr int
     * @param wert double wert
     * @param fid int Fremdschl�ssel
     */
    public UmsatzDTO(int jahr, double wert, int fid) {
        this.jahr = jahr;
        this.wert = wert;
        this.fkFid = fid;
    }

    /**
     * Konstruktor mit Resultset als Parameter:
     * @param rs ResultSet
     */
    public UmsatzDTO(ResultSet rs) {
        try {
            this.jahr = rs.getInt("jahr");
            this.wert = rs.getDouble("wert");
            this.fkFid = rs.getInt("fk_fid");
        } catch (SQLException e) {
            System.err.print("DB-Fehler: " + e);
        }
    }

    /**
     * 
     * @param jahr
     */
    public void setJahr(int jahr) {
        this.jahr = jahr;
    }

    /**
     * 
     * @return
     */
    public int getJahr() {
        return jahr;
    }

    /**
     * 
     * @param wert
     */
    public void setWert(double wert) {
        this.wert = wert;
    }

    /**
     * 
     * @return
     */
    public double getWert() {
        return wert;
    }

    /**
     * 
     * @param fkFid
     */
    public void setFkFid(int fkFid) {
        this.fkFid = fkFid;
    }

    /**
     * 
     * @return
     */
    public int getFkFid() {
        return fkFid;
    }

    /**
     * 
     * @return
     */
    public String toString() {
        return "Jahr " + this.jahr + ":\t " + this.wert;
    }
}
