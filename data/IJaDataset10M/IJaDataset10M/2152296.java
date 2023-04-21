package com.loli.hernandez.modelo.beans;

/**
 * 
 * @author Néstor Hernández Loli
 * @since 14/04/2011
 * Una representación lógica de una base de 
 * datos en el servidor de base datos 
 */
public class Database {

    private String databaseName;

    /**
     * 
     * @return El nombre de la base de datos
     */
    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public Database() {
    }

    /***
     * 
     * @param databaseName Permite setear el nombre
     * de la base de datos
     */
    public Database(String databaseName) {
        this.databaseName = databaseName;
    }

    @Override
    public String toString() {
        return databaseName;
    }
}
