package uk.ac.ncl.neresc.dynasoar.hostProvider.SQLServer2005StoredProcInstaller;

import java.io.Serializable;

/**
 * @author Charles Kubicek
 */
public class Config implements Serializable {

    public Config() {
        assemblyName = "assemblyWithOneSP";
        Config.StoredProc sp1 = new StoredProc();
        sp1.name = "uspGetPersonInfoAssemblyVersion";
        Config.StoredProc.Param par1 = sp1.new Param();
        par1.name = "id";
        par1.SQLServerParameterType = "nvarchar";
        sp1.params = new Config.StoredProc.Param[] { par1 };
        procs = new Config.StoredProc[] { sp1 };
    }

    public SQLServer2005SPInstaller.StoredProcedureType type = SQLServer2005SPInstaller.StoredProcedureType.DOTNET;

    public Database db = new Database();

    public ExistingElementsStrategies strategies = new ExistingElementsStrategies();

    public String assemblyName = null;

    public StoredProc[] procs = null;

    public class Database implements Serializable {

        public String jdbcDriverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

        public String site = "thinkpeat";

        public String jdbcUrl = "jdbc:sqlserver://" + site;

        public String databaseName = "AdventureWorks";

        public String hpUserName = "hp";

        public String hpPassword = "hppassword";
    }

    public class ExistingElementsStrategies implements Serializable {

        public SQLServer2005SPInstaller.EXISTING_STORED_PROC_STRATEGY storedProcStrategy = SQLServer2005SPInstaller.EXISTING_STORED_PROC_STRATEGY.OVERWRITE_EXISTING_PROCEDURE;

        public SQLServer2005SPInstaller.EXISTING_ASSEMBLY_STRATEGY assemblyStrategy = SQLServer2005SPInstaller.EXISTING_ASSEMBLY_STRATEGY.DELETE_EXISTING_ASSEMBLY;

        public SQLServer2005SPInstaller.EXISTING_ENDPOINT_STRATEGY endpointStrategy = SQLServer2005SPInstaller.EXISTING_ENDPOINT_STRATEGY.ADD_METHODS_TO_EXISTING_ENDPOINT;
    }

    public class StoredProc implements Serializable {

        public String name;

        public StoredProc.Param[] params;

        public class Param implements Serializable {

            public String name;

            public String SQLServerParameterType;
        }

        public String toString() {
            String ret = name;
            for (StoredProc.Param par : params) {
                ret += " @" + par.name + " " + par.SQLServerParameterType + " ";
            }
            return ret;
        }
    }
}
