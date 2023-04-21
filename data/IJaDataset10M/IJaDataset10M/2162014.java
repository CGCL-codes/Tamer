package test.junit.datastructure;

import java.util.Calendar;
import java.util.Date;

/**
 * Classe de test permettant de g�n�rer des beans avec des propri�t�s d'un peu tous les types,
 * afin de v�rifier le bon traitement de chaque type de donn�es dans la g�n�ration de fichier XL. 
 * 
 * @author jbeausseron@sqli.com
 */
public class SimpleBean {

    private String chaine;

    private String description;

    private Integer entier;

    private Double reelDouble;

    private Calendar utilCalendar;

    private Date utilDate;

    private java.sql.Date sqlDate;

    private java.sql.Timestamp sqlTimeStamp;

    private java.math.BigDecimal bigDecimal;

    private Boolean booleen;

    /**
     * Construit un bean � partir des param�tres en entr�e.
     * 
     * @param chaine
     * @param pDescription
     * @param pReelDouble
     * @param pEntier
     * @param pUtilDate
     * @param pUtilCalendar
     * @param pSQLDate
     * @param pSQLTimeStamp
     * @param pBigDecimal
     * @param pBoolean
     */
    public SimpleBean(String chaine, String pDescription, double pReelDouble, int pEntier, Date pUtilDate, Calendar pUtilCalendar, java.sql.Date pSQLDate, java.sql.Timestamp pSQLTimeStamp, java.math.BigDecimal pBigDecimal, boolean pBoolean) {
        this.chaine = chaine;
        this.description = pDescription;
        this.reelDouble = new Double(pReelDouble);
        this.entier = new Integer(pEntier);
        this.utilDate = pUtilDate;
        this.utilCalendar = pUtilCalendar;
        this.sqlDate = pSQLDate;
        this.sqlTimeStamp = pSQLTimeStamp;
        this.bigDecimal = pBigDecimal;
        this.booleen = new Boolean(pBoolean);
    }

    /**
     * @return valeur de la propri�t�
     */
    public String getChaine() {
        return chaine;
    }

    /**
     * @return valeur de la propri�t�
     */
    public Integer getEntier() {
        return entier;
    }

    /**
     * @return valeur de la propri�t�
     */
    public Double getReelDouble() {
        return reelDouble;
    }

    /**
     * @return valeur de la propri�t�
     */
    public Date getUtilDate() {
        return utilDate;
    }

    /**
     * @param string valeur de la propri�t�
     */
    public void setChaine(String string) {
        chaine = string;
    }

    /**
     * @param integer valeur de la propri�t�
     */
    public void setEntier(Integer integer) {
        entier = integer;
    }

    /**
     * @param double1 valeur de la propri�t�
     */
    public void setReelDouble(Double double1) {
        reelDouble = double1;
    }

    /**
     * @param date valeur de la propri�t�
     */
    public void setUtilDate(Date date) {
        utilDate = date;
    }

    /**
     * @return valeur de la propri�t�
     */
    public Calendar getUtilCalendar() {
        return utilCalendar;
    }

    /**
     * @param calendar valeur de la propri�t�
     */
    public void setUtilCalendar(Calendar calendar) {
        utilCalendar = calendar;
    }

    /**
     * @return valeur de la propri�t�
     */
    public java.sql.Date getSqlDate() {
        return sqlDate;
    }

    /**
     * @param date valeur de la propri�t�
     */
    public void setSqlDate(java.sql.Date date) {
        sqlDate = date;
    }

    /**
     * @return valeur de la propri�t�
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param string valeur de la propri�t�
     */
    public void setDescription(String string) {
        description = string;
    }

    /**
     * @return valeur de la propri�t�
     */
    public java.sql.Timestamp getSqlTimeStamp() {
        return sqlTimeStamp;
    }

    /**
     * @param timestamp valeur de la propri�t�
     */
    public void setSqlTimeStamp(java.sql.Timestamp timestamp) {
        sqlTimeStamp = timestamp;
    }

    /**
     * @return valeur de la propri�t�
     */
    public java.math.BigDecimal getBigDecimal() {
        return bigDecimal;
    }

    /**
     * @return valeur de la propri�t�
     */
    public Boolean getBooleen() {
        return booleen;
    }

    /**
     * @param decimal valeur de la propri�t�
     */
    public void setBigDecimal(java.math.BigDecimal decimal) {
        bigDecimal = decimal;
    }

    /**
     * @param boolean1 valeur de la propri�t�
     */
    public void setBooleen(Boolean boolean1) {
        booleen = boolean1;
    }
}
