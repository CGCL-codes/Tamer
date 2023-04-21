package integration.pulldata;

import integration.pull.tablename.IntegratePool;
import utility.CapitalChar;

public class Add extends Pulloperation {

    public Add() {
        request = "master_table";
    }

    @SuppressWarnings("unchecked")
    public boolean performOperation() {
        generateProductHash();
        generateModuleHash();
        IntegratePool integratePool;
        try {
            Class C = Class.forName("integration.pull.tablename." + CapitalChar.makeFirstCharCapital(poolTablename));
            integratePool = (IntegratePool) C.newInstance();
            integratePool.setColumnName(columnName);
            integratePool.setDatabase(dbsql);
            integratePool.setIntegrationObject(integrationObject);
            integratePool.setNewColumnValue(columnValue);
            integratePool.setTableName(tableName);
            integratePool.setProcess(process);
            return integratePool.insert(productTableHash, ModuleHash);
        } catch (Exception e) {
        }
        return false;
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) {
    }
}
