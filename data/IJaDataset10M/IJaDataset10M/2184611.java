package edu.model.dao;

public class FactoryDAO {

    /***********************************************************************************
	 * Crea y devuelve el DAO 
	 **********************************************************************************/
    public static Dao getDAO(String nombre) {
        try {
            Class clase = Class.forName(nombre);
            return (Dao) clase.newInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
