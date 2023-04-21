package Domini;

import java.sql.*;
import Errors.*;
import CompDomini.*;

/**
 * Representa una plantilla condici� de tasca
 * <p>T�tol: Projecte PP</p>
 * <p>Descripci�: Projecte PP primavera 2002</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Cl�ster 3</p>
 * @author Frederic P�rez Ordeig
 * @version 0.01
 */
public class plantillaCondicioTasca extends objecteID {

    protected String szSQL, szNom, szExplicacio;

    protected int nParams, nMajorMenorIgual, nComparat;

    protected Object oLlistaParams[];

    /**
   * Constructora amb nom, sql, num params, majormenorigual i numero comparat
   * @param p_szNom
   * @param p_szSQL
   * @param p_nParams
   * @param p_nMajorMenorIgual
   * @param p_nComparat
   * @throws excepcio
   */
    public plantillaCondicioTasca(String p_szNom, String p_szSQL, int p_nParams, int p_nMajorMenorIgual, int p_nComparat) throws excepcio {
        setNom(p_szNom);
        setSQL(p_szSQL);
        setParams(p_nParams);
        setMajorMenorIgual(p_nMajorMenorIgual);
        setComparat(p_nComparat);
        omplert();
    }

    /**
   * retorna la instrucci� SQL
   * @return String
   */
    public String getSQL() {
        return szSQL;
    }

    /**
   * Posa la instrucci� SQL
   * @param p_szSQL
   * @throws excepcio
   */
    public void setSQL(String p_szSQL) throws excepcio {
        if (p_szSQL != null && p_szSQL.length() > 0) szSQL = p_szSQL; else throw new excepcio(excepcio.LLEU, "La instrucci� SQL no �s correcta", excepcio.ERRORPARAMETRE);
    }

    /**
   * Retorna el nom de la plantilla
   * @return String
   */
    public String getNom() {
        return szNom;
    }

    /**
   * Posa el nom de la plantilla
   * @param p_szNom
   * @throws excepcio
   */
    public void setNom(String p_szNom) throws excepcio {
        if (p_szNom != null && p_szNom.length() > 0) szNom = p_szNom; else throw new excepcio(excepcio.LLEU, "El nom de la plantilla de condicio no �s correcte", excepcio.ERRORPARAMETRE);
    }

    /**
   * Retorna la eplicaci�
   * @return String
   */
    public String getExplicacio() {
        return szExplicacio;
    }

    /**
   * Posa la explicaci�
   * @param p_szExplicacio
   * @throws excepcio
   */
    public void setExplicacio(String p_szExplicacio) throws excepcio {
        if (szExplicacio != null) szExplicacio = p_szExplicacio; else throw new excepcio(excepcio.LLEU, "La explicaci� no �s correcta", excepcio.ERRORPARAMETRE);
    }

    /**
   * Retorna el numero de par�metres de la instrucci� sQL
   * @return int
   */
    public int getParams() {
        return nParams;
    }

    /**
   * Posa el n�mero de par�metres de la instrucci� SQL
   * @param p_nParams
   * @throws excepcio
   */
    public void setParams(int p_nParams) throws excepcio {
        if (p_nParams >= 0) nParams = p_nParams; else throw new excepcio(excepcio.LLEU, "Numero de par�metres incorrecte", excepcio.ERRORPARAMETRE);
    }

    /**
   * Retorna el majormenorigual com a condicioTasca
   * @return int
   */
    public int getMajorMenorIgual() {
        return nMajorMenorIgual;
    }

    /**
   * Posa el majormenorigual com a condicioTasca
   * @param p_nMajorMenorIgual
   * @throws excepcio
   */
    public void setMajorMenorIgual(int p_nMajorMenorIgual) throws excepcio {
        if (p_nMajorMenorIgual >= -2 && p_nMajorMenorIgual <= 2) nMajorMenorIgual = p_nMajorMenorIgual; else throw new excepcio(excepcio.LLEU, "El criteri de comparaci� no �s correcte", excepcio.ERRORPARAMETRE);
    }

    /**
   * Retorna el nombre comparat
   * @return int
   */
    public int getComparat() {
        return nComparat;
    }

    /**
   * Posa el nombre comparat
   * @param p_nComparat
   */
    public void setComparat(int p_nComparat) {
        nComparat = p_nComparat;
    }

    /**
   * Retorna una condici� segons la plantilla
   * @param p_oLlistaParams
   * @return condicioTasca
   * @throws excepcio
   */
    public condicioTasca getCondicioTasca(Object p_oLlistaParams[]) throws excepcio {
        return (new condicioTasca(szSQL, nMajorMenorIgual, nComparat, p_oLlistaParams));
    }

    /**
   * Retorna el nom del domini
   * @return String
   */
    public String getNomDomini() {
        return "Plantilla condicio tasca";
    }

    public Object[] getLlistaParams() {
        return this.oLlistaParams;
    }

    public void setLlistaParams(Object[] p_oLlistaParams) {
        oLlistaParams = p_oLlistaParams;
    }
}
