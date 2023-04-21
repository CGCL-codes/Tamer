package org.openXpertya.fastrack;

import java.util.ArrayList;

public class FTRolesAndUsersEliminate extends FTModule {

    /** Nombre de la tabla */
    private String[] tablesNames = { "ad_role", "ad_user", "ad_user_roles" };

    /** Ids de compañía que no se deben eliminar los roles */
    private ArrayList<Integer> ids = new ArrayList<Integer>();

    public FTRolesAndUsersEliminate() {
    }

    public FTRolesAndUsersEliminate(String trxName) {
        this.setTrxName(trxName);
    }

    public FTRolesAndUsersEliminate(String trxName, int id) {
        this.setTrxName(trxName);
        this.addId(id);
        this.addId(0);
    }

    public FTRolesAndUsersEliminate(String trxName, int ids[]) {
        this.setTrxName(trxName);
        this.initializeClients(ids);
    }

    public FTRolesAndUsersEliminate(String trxName, ArrayList<Integer> ids) {
        this.setTrxName(trxName);
        this.setIds(ids);
    }

    public void setTablesNames(String[] tablesNames) {
        this.tablesNames = tablesNames;
    }

    public String[] getTablesNames() {
        return tablesNames;
    }

    public void setIds(ArrayList<Integer> ids) {
        this.ids = ids;
    }

    public ArrayList<Integer> getIds() {
        return ids;
    }

    /**
	 * Inicializo los ids de los clientes desde un arreglo de ids
	 * @param ids ids de copañías
	 */
    public void initializeClients(int ids[]) {
        int total = ids.length;
        ArrayList<Integer> lista = new ArrayList<Integer>();
        for (int i = 0; i < total; i++) {
            lista.add(ids[i]);
        }
        this.setIds(lista);
    }

    /**
	 * Resuelve los ids en enteros
	 * @return arreglo con los ids primitivos
	 */
    public int[] idsToPrimitive() {
        int total = this.getIds().size();
        Integer[] codes = new Integer[total];
        this.getIds().toArray(codes);
        int[] idsRetorno = new int[total];
        for (int i = 0; i < total; i++) {
            idsRetorno[i] = codes[i].intValue();
        }
        return idsRetorno;
    }

    /**
	 * Agrego el id de compañía al arreglo de ids
	 * @param id id a agregar en el arreglo
	 */
    public void addId(int id) {
        this.getIds().add(id);
    }

    /**
	 * Elimina los roles WideFast-Track Admin y WideFast-Track User 
	 */
    public void delRolesStandard() throws Exception {
        String sql = "DELETE FROM ad_role WHERE (name = 'WideFast-Track Admin') OR (name = 'WideFast-Track User')";
        ExecuterSql.executeUpdate(sql, this.getTrxName());
    }

    /**
	 * Desactiva los usuarios, roles y roles de usuarios a partir de los id de compañía
	 */
    public void ejecutar() throws Exception {
        int total = this.getTablesNames().length;
        for (int i = 0; i < total; i++) {
            this.changeActiveByClientIds(this.getTablesNames()[i], this.idsToPrimitive(), false);
        }
        this.delRolesStandard();
    }

    public void deshacer() {
    }
}
