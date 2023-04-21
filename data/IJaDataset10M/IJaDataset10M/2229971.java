package com.htdsoft.noyau;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ArrayHandler;
import org.apache.commons.dbutils.handlers.ArrayListHandler;
import com.htdsoft.action.ActionUtils;
import com.htdsoft.exception.BusinessException;
import com.htdsoft.moteur.Bdd;

public class DaoDevise {

    private Connection conn;

    private Properties propQueries;

    private QueryRunner qRunner;

    public DaoDevise() {
        Bdd bdd = new Bdd();
        conn = bdd.ouvrirBdd();
        qRunner = new QueryRunner();
        propQueries = ActionUtils.loadQueries("devise.properties");
    }

    /**
	* @return : liste de toutes les devises
	* @throws BusinessException
	*/
    public List<Devise> getAllDevises() throws BusinessException {
        List<Devise> list1 = new ArrayList<Devise>();
        List list2 = new ArrayList();
        try {
            list2 = (List) qRunner.query(conn, (String) propQueries.get("req.getAllDevises"), new ArrayListHandler());
        } catch (SQLException e1) {
            e1.printStackTrace();
            throw new BusinessException("Impossible de retourner toutes les devises");
        }
        for (int i = 0; i < list2.size(); i++) {
            Object data[] = (Object[]) list2.get(i);
            Devise uneDevise = new Devise((Integer) data[0], (String) data[1], (String) data[2], (String) data[3]);
            list1.add(uneDevise);
        }
        return list1;
    }

    /**
	* @param uneDevise : la Devise � ajouter
	* @throws BusinessException 
	*/
    public int ajouteDevise(Devise uneDevise) throws BusinessException {
        Object[] paramDevise = new Object[4];
        paramDevise[0] = uneDevise.getIdDevise();
        paramDevise[1] = uneDevise.getPays();
        paramDevise[2] = uneDevise.getNom_devise();
        paramDevise[3] = uneDevise.getCode_devise();
        try {
            return qRunner.update(conn, (String) propQueries.get("req.ajouteDevise"), paramDevise);
        } catch (SQLException e2) {
            e2.printStackTrace();
            throw new BusinessException("Impossible d'ajouter la devise N�" + uneDevise.getIdDevise());
        }
    }

    /**
	* @param idDevise : id de la Devise � supprimer
	* @throws BusinessException 
	*/
    public int supprimeDevise(int idDevise) throws BusinessException {
        try {
            return qRunner.update(conn, (String) propQueries.get("req.supprimeDevise"), idDevise);
        } catch (SQLException e3) {
            e3.printStackTrace();
            throw new BusinessException("Impossible de supprimer la devise N�" + idDevise);
        }
    }

    /**
	* @param uneDevise : la Devise � modifier
	* @throws BusinessException 
	*/
    public int modifieDevise(Devise uneDevise) throws BusinessException {
        Object[] paramDevise = new Object[4];
        paramDevise[0] = uneDevise.getPays();
        paramDevise[1] = uneDevise.getNom_devise();
        paramDevise[2] = uneDevise.getCode_devise();
        paramDevise[3] = uneDevise.getIdDevise();
        try {
            return qRunner.update(conn, (String) propQueries.get("req.modifieDevise"), paramDevise);
        } catch (SQLException e4) {
            e4.printStackTrace();
            throw new BusinessException("Impossible de modifier la devise N�" + uneDevise.getIdDevise());
        }
    }

    /**
	* @param idDevise : id de la Devise recherch�e
	* @return : la Devise trouv�e ou null
	* @throws BusinessException 
	*/
    public Devise getDeviseById(int idDevise) throws BusinessException {
        Object[] paramDevise = new Object[1];
        paramDevise[0] = idDevise;
        Object resulteDevise[] = new Object[4];
        try {
            resulteDevise = (Object[]) qRunner.query(conn, (String) propQueries.get("req.getDeviseById"), paramDevise, new ArrayHandler());
        } catch (SQLException e5) {
            e5.printStackTrace();
            throw new BusinessException("Impossible de recuperer la devise N�" + idDevise);
        }
        Devise laDevise = new Devise((Integer) resulteDevise[0], (String) resulteDevise[1], (String) resulteDevise[2], (String) resulteDevise[3]);
        return laDevise;
    }

    /**
	* @param nom_devise : nom de la Devise recherch�e
	* @param nom_pays : nom du pays de la devise
	* @return : l'idDevise trouv�e ou null
	* @throws BusinessException
	*/
    public int getidDeviseByNames(String nom_devise, String nom_pays) throws BusinessException {
        Object[] paramDevise = new Object[2];
        paramDevise[0] = nom_devise;
        paramDevise[1] = nom_pays;
        Object resulteDevise[] = new Object[1];
        try {
            resulteDevise = (Object[]) qRunner.query(conn, (String) propQueries.get("req.getidDeviseByNames"), paramDevise, new ArrayHandler());
        } catch (SQLException e5) {
            e5.printStackTrace();
            throw new BusinessException("Impossible de recuperer la devise " + nom_devise);
        }
        int idDevise = (Integer) resulteDevise[0];
        return idDevise;
    }

    /**
	* vide la table des Devises
	* @throws BusinessException 
	*/
    public int clearAllDevises() throws BusinessException {
        try {
            return qRunner.update(conn, (String) propQueries.get("req.clearAllDevises"));
        } catch (SQLException e6) {
            e6.printStackTrace();
            throw new BusinessException("Impossible de supprimer toutes les devises");
        }
    }
}
