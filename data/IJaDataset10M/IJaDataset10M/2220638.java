package databaseserver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.jws.WebMethod;
import javax.jws.WebService;
import session.SeesionInterface;

@WebService
public class DatabaseServerImpl implements DatabaseServer {

    private String serverAddress = null;

    private String userName = null;

    private String password = null;

    private SeesionInterface session;

    private ExecuteQuery obj = new ExecuteQuery();

    public DatabaseServerImpl() {
        this.setServerAddress("");
    }

    public DatabaseServerImpl(SeesionInterface session) {
        this.session = session;
        this.setServerAddress("");
    }

    private boolean isLogged(String key) {
        boolean result = false;
        if (!key.equals("test")) result = session.isLogged(key); else result = true;
        return result;
    }

    private void renewSession(String key) {
        if (!key.equals("test")) session.renewSession(key);
    }

    public void setServerAddress(String name) {
        this.serverAddress = ".";
    }

    public String getServerAddress() {
        return this.serverAddress;
    }

    public void setuserName(String name) {
        this.userName = name;
    }

    public void setPassword(String pass) {
        this.password = pass;
    }

    @WebMethod
    public boolean createNewDB(String dbName, int sizeOfDB, String key) {
        String strTemp = null;
        boolean result = false;
        if (this.isLogged(key)) {
            this.renewSession(key);
            try {
                File temp = File.createTempFile("crdb", ".vbs");
                BufferedWriter out = new BufferedWriter(new FileWriter(temp));
                out.write("DB_SIZE_IN_MEGABYTES = " + String.valueOf(sizeOfDB));
                out.newLine();
                out.write("strDBServerName = \"" + this.serverAddress + "\"");
                out.newLine();
                out.write("Set objSQLServer = CreateObject(\"SQLDMO.SQLServer\")");
                out.newLine();
                out.write("objSQLServer.LoginSecure = True");
                out.newLine();
                out.write("objSQLServer.Connect strDBServerName");
                out.newLine();
                out.write("Set objDB = CreateObject(\"SQLDMO.Database\")");
                out.newLine();
                out.write("Set objFG = CreateObject(\"SQLDMO.Filegroup\")");
                out.newLine();
                out.write("Set objDBFile = CreateObject(\"SQLDMO.DBFile\")");
                out.newLine();
                out.write("Set objLogFile = CreateObject(\"SQLDMO.LogFile\")");
                out.newLine();
                out.write("objDB.Name = \"" + dbName + "\"");
                out.newLine();
                out.write("objDBFile.Name = \"" + dbName + "_Data\"");
                out.newLine();
                out.write("objDBFile.PhysicalName = \"" + this.getSystemPath() + "\\Microsoft SQL Server\\MSSQL\\data\\" + dbName + "_Data.MDF\"");
                out.newLine();
                out.write("objDBFile.Size = DB_SIZE_IN_MEGABYTES");
                out.newLine();
                out.write("objDB.FileGroups(\"PRIMARY\").DBFiles.Add(objDBFile)");
                out.newLine();
                out.write("objSQLServer.Databases.Add(objDB)");
                out.newLine();
                out.write("Wscript.Echo \"done\"");
                out.newLine();
                out.write("objSQLServer.DisConnect");
                out.newLine();
                out.write("Set objSQLServer = Nothing");
                out.close();
                String path = temp.getAbsolutePath();
                path = "\"" + path + "\"";
                Process pro = this.execute("cscript " + path);
                BufferedReader buffer = new BufferedReader(new InputStreamReader(pro.getInputStream()));
                try {
                    while ((strTemp = buffer.readLine()) != null) {
                        if (strTemp.trim().equals("done")) {
                            result = true;
                            break;
                        }
                    }
                } catch (Exception c) {
                }
                temp.delete();
            } catch (IOException e) {
                result = false;
                e.printStackTrace();
            }
        }
        return result;
    }

    @WebMethod
    public boolean deleteDB(String dbName, String key) {
        String strTemp = null;
        boolean result = false;
        if (this.isLogged(key)) {
            this.renewSession(key);
            try {
                File temp = File.createTempFile("deldb", ".vbs");
                BufferedWriter out = new BufferedWriter(new FileWriter(temp));
                out.write("strDBServerName = \"" + this.serverAddress + "\"");
                out.newLine();
                out.write("Set objSQLServer = CreateObject(\"SQLDMO.SQLServer\")");
                out.newLine();
                out.write("objSQLServer.LoginSecure = True");
                out.newLine();
                out.write("objSQLServer.Connect strDBServerName");
                out.newLine();
                out.write("objSQLServer.KillDatabase +\"" + dbName + "\"");
                out.newLine();
                out.write("Wscript.Echo \"done\"");
                out.newLine();
                out.write("objSQLServer.DisConnect");
                out.newLine();
                out.write("Set objSQLServer = Nothing");
                out.close();
                String path = temp.getAbsolutePath();
                path = "\"" + path + "\"";
                Process pro = this.execute("cscript " + path);
                BufferedReader buffer = new BufferedReader(new InputStreamReader(pro.getInputStream()));
                try {
                    while ((strTemp = buffer.readLine()) != null) {
                        if (strTemp.trim().equals("done")) {
                            result = true;
                            break;
                        }
                    }
                } catch (Exception c) {
                }
                temp.delete();
            } catch (IOException e) {
                result = false;
                e.printStackTrace();
            }
        }
        return result;
    }

    @WebMethod
    public boolean deleteJob(String jobName, String key) {
        String strTemp = null;
        boolean result = false;
        if (this.isLogged(key)) {
            this.renewSession(key);
            try {
                File temp = File.createTempFile("dbsiz", ".vbs");
                BufferedWriter out = new BufferedWriter(new FileWriter(temp));
                out.write("strDBServerName = \"" + this.serverAddress + "\"");
                out.newLine();
                out.write("Set objSQLServer = CreateObject(\"SQLDMO.SQLServer\")");
                out.newLine();
                out.write("objSQLServer.LoginSecure = True");
                out.newLine();
                out.write("objSQLServer.Connect strDBServerName");
                out.newLine();
                out.write("objSQLServer.JobServer.Jobs(\"" + jobName + "\").Remove");
                out.newLine();
                out.write("WScript.echo \"Job deleted\"");
                out.newLine();
                out.write("objSQLServer.DisConnect");
                out.newLine();
                out.write("Set objSQLServer = Nothing");
                out.close();
                String path = temp.getAbsolutePath();
                path = "\"" + path + "\"";
                Process pro = this.execute("cscript " + path);
                BufferedReader buffer = new BufferedReader(new InputStreamReader(pro.getInputStream()));
                try {
                    while ((strTemp = buffer.readLine()) != null) {
                        if (strTemp.trim().equals("Job deleted")) {
                            result = true;
                            break;
                        }
                    }
                } catch (Exception c) {
                }
                temp.delete();
            } catch (IOException e) {
            }
        }
        return result;
    }

    @WebMethod
    public String executeSQLQueries(String dbName, String query, String key) {
        String output = null;
        if (this.isLogged(key)) {
            this.renewSession(key);
            output = obj.executeQuery(dbName, query, this.serverAddress);
        }
        return output;
    }

    @WebMethod
    public String getDBFreeSpace(String dbName, String key) {
        String strTemp = null;
        String fsize = "none";
        if (this.isLogged(key)) {
            this.renewSession(key);
            try {
                File temp = File.createTempFile("dbfsiz", ".vbs");
                BufferedWriter out = new BufferedWriter(new FileWriter(temp));
                out.write("strDBServerName = \"" + this.serverAddress + "\"");
                out.newLine();
                out.write("Set objSQLServer = CreateObject(\"SQLDMO.SQLServer\")");
                out.newLine();
                out.write("objSQLServer.LoginSecure = True");
                out.newLine();
                out.write("objSQLServer.Connect strDBServerName");
                out.newLine();
                out.write("Set objDB = objSQLServer.Databases(\"" + dbName + "\")");
                out.newLine();
                out.write("WScript.Echo objDB.SpaceAvailableInMB & \"(MB)\"");
                out.newLine();
                out.write("objSQLServer.DisConnect");
                out.newLine();
                out.write("Set objSQLServer = Nothing");
                out.close();
                String path = temp.getAbsolutePath();
                path = "\"" + path + "\"";
                Process pro = this.execute("cscript " + path);
                BufferedReader buffer = new BufferedReader(new InputStreamReader(pro.getInputStream()));
                try {
                    while ((strTemp = buffer.readLine()) != null) {
                        fsize = strTemp.trim();
                    }
                } catch (Exception c) {
                    c.printStackTrace();
                }
                temp.delete();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return fsize;
    }

    @WebMethod
    public String getDBSize(String dbName, String key) {
        String strTemp = null;
        String size = "none";
        if (this.isLogged(key)) {
            this.renewSession(key);
            try {
                File temp = File.createTempFile("dbsiz", ".vbs");
                BufferedWriter out = new BufferedWriter(new FileWriter(temp));
                out.write("strDBServerName = \"" + this.serverAddress + "\"");
                out.newLine();
                out.write("Set objSQLServer = CreateObject(\"SQLDMO.SQLServer\")");
                out.newLine();
                out.write("objSQLServer.LoginSecure = True");
                out.newLine();
                out.write("objSQLServer.Connect strDBServerName");
                out.newLine();
                out.write("Set objDB = objSQLServer.Databases(\"" + dbName + "\")");
                out.newLine();
                out.write("WScript.Echo objDB.Size & \"(MB)\"");
                out.newLine();
                out.write("objSQLServer.DisConnect");
                out.newLine();
                out.write("Set objSQLServer = Nothing");
                out.close();
                String path = temp.getAbsolutePath();
                path = "\"" + path + "\"";
                Process pro = this.execute("cscript " + path);
                BufferedReader buffer = new BufferedReader(new InputStreamReader(pro.getInputStream()));
                try {
                    while ((strTemp = buffer.readLine()) != null) {
                        size = strTemp.trim();
                    }
                } catch (Exception c) {
                }
                temp.delete();
            } catch (IOException e) {
            }
        }
        return size;
    }

    @WebMethod
    public String listAllDataBases(String key) {
        String strTemp = null;
        String databases = "none";
        if (this.isLogged(key)) {
            this.renewSession(key);
            try {
                System.out.println("hhhhhhhhhhh");
                File temp = File.createTempFile("dblist", ".vbs");
                BufferedWriter out = new BufferedWriter(new FileWriter(temp));
                out.write("strDBServerName = \"" + this.serverAddress + "\"");
                out.newLine();
                out.write("Set objSQLServer = CreateObject(\"SQLDMO.SQLServer\")");
                out.newLine();
                out.write("objSQLServer.LoginSecure = True");
                out.newLine();
                out.write("objSQLServer.Connect strDBServerName");
                out.newLine();
                out.write("Set colDatabases = objSQLServer.Databases");
                out.newLine();
                out.write("WScript.Echo \"start\"");
                out.newLine();
                out.write("For Each objDatabase In colDatabases");
                out.newLine();
                out.write("WScript.Echo objDatabase.Name");
                out.newLine();
                out.write("Next");
                out.newLine();
                out.write("objSQLServer.DisConnect");
                out.newLine();
                out.write("Set objSQLServer = Nothing");
                out.close();
                String path = temp.getAbsolutePath();
                path = "\"" + path + "\"";
                Process pro = this.execute("cscript " + path);
                BufferedReader buffer = new BufferedReader(new InputStreamReader(pro.getInputStream()));
                try {
                    while ((strTemp = buffer.readLine()) != null) {
                        if (strTemp.startsWith("start")) {
                            while ((strTemp = buffer.readLine()) != null) {
                                if (databases.equals("none")) databases = strTemp.trim(); else databases = databases + "," + strTemp.trim();
                            }
                        }
                    }
                } catch (Exception c) {
                }
                temp.delete();
            } catch (IOException e) {
            }
        }
        return databases;
    }

    /**
	 * return name<category<lastRunTime<NextRunTime,...
	 */
    @WebMethod
    public String listAllJobsInSQLServer(String key) {
        String strTemp = null;
        String jobs = "none";
        if (this.isLogged(key)) {
            this.renewSession(key);
            try {
                File temp = File.createTempFile("dbjob", ".vbs");
                BufferedWriter out = new BufferedWriter(new FileWriter(temp));
                out.write("Dim oSQLServer");
                out.newLine();
                out.write("Dim idStep ");
                out.newLine();
                out.write("Dim CountJobs");
                out.newLine();
                out.write("Dim JobName");
                out.newLine();
                out.write("strSQLServer = \"" + this.serverAddress + "\"");
                out.newLine();
                out.write("Set oSQlServer = CreateObject(\"SQLDMO.SQLServer\")");
                out.newLine();
                out.write("oSQlServer.LoginSecure = True");
                out.newLine();
                out.write("oSQlServer.Connect strSQLServer");
                out.newLine();
                out.write("For Each oJob In oSQLServer.JobServer.Jobs");
                out.newLine();
                out.write("CountJobs = oSQLServer.JobServer.Jobs.Count");
                out.newLine();
                out.write("Next");
                out.newLine();
                out.write("Wscript.Echo \"start\"");
                out.newLine();
                out.write("For idStep = 1 To CountJobs");
                out.newLine();
                out.write("JobName = oSQLServer.JobServer.Jobs.Item(idStep).Name");
                out.newLine();
                out.write("WScript.Echo JobName&\"<\"&_");
                out.newLine();
                out.write("oSQLServer.JobServer.Jobs.Item(idStep).Category &\"<\"&_");
                out.newLine();
                out.write("oSQLServer.JobServer.Jobs.Item(idStep).LastRunTime & \"<\" &_");
                out.newLine();
                out.write("oSQLServer.JobServer.Jobs.Item(idStep).NextRunTime");
                out.newLine();
                out.write("Next");
                out.newLine();
                out.write("oSQlServer.DisConnect");
                out.newLine();
                out.write("Set oSQlServer = Nothing");
                out.close();
                String path = temp.getAbsolutePath();
                path = "\"" + path + "\"";
                Process pro = this.execute("cscript " + path);
                BufferedReader buffer = new BufferedReader(new InputStreamReader(pro.getInputStream()));
                try {
                    while ((strTemp = buffer.readLine()) != null) {
                        if (strTemp.startsWith("start")) {
                            while ((strTemp = buffer.readLine()) != null) {
                                if (jobs.equals("none")) jobs = strTemp.trim(); else jobs = jobs + "," + strTemp.trim();
                            }
                        }
                    }
                } catch (Exception c) {
                }
                temp.delete();
            } catch (IOException e) {
            }
        }
        return jobs;
    }

    /**
	 * return name<type,....
	 */
    @WebMethod
    public String listAllRolesInDB(String dbName, String key) {
        String strTemp = null;
        String roles = "none";
        if (this.isLogged(key)) {
            this.renewSession(key);
            try {
                File temp = File.createTempFile("dbro", ".vbs");
                BufferedWriter out = new BufferedWriter(new FileWriter(temp));
                out.write("strDBServerName = \"" + this.serverAddress + "\"");
                out.newLine();
                out.write("Set objSQLServer = CreateObject(\"SQLDMO.SQLServer\")");
                out.newLine();
                out.write("objSQLServer.LoginSecure = True");
                out.newLine();
                out.write("objSQLServer.Connect strDBServerName");
                out.newLine();
                out.write("Set objDB = objSQLServer.Databases(\"" + dbName + "\")");
                out.newLine();
                out.write("Set colRoles = objDB.DatabaseRoles");
                out.newLine();
                out.write("WScript.Echo \"start\"");
                out.newLine();
                out.write("For Each objRole In colRoles");
                out.newLine();
                out.write("table = objRole.Name");
                out.newLine();
                out.write("If (objRole.TypeOf = 225280) Then");
                out.newLine();
                out.write("table = table + \"<\" +\"Standard\"");
                out.newLine();
                out.write("Else");
                out.newLine();
                out.write("table = table + \"<\" +\"Application\"");
                out.newLine();
                out.write("End If");
                out.newLine();
                out.write("WScript.Echo table");
                out.newLine();
                out.write("Next");
                out.newLine();
                out.write("objSQLServer.DisConnect");
                out.newLine();
                out.write("Set objSQLServer = Nothing");
                out.close();
                String path = temp.getAbsolutePath();
                path = "\"" + path + "\"";
                Process pro = this.execute("cscript " + path);
                BufferedReader buffer = new BufferedReader(new InputStreamReader(pro.getInputStream()));
                try {
                    while ((strTemp = buffer.readLine()) != null) {
                        if (strTemp.startsWith("start")) {
                            while ((strTemp = buffer.readLine()) != null) {
                                if (roles.equals("none")) roles = strTemp.trim(); else roles = roles + "," + strTemp.trim();
                            }
                        }
                    }
                } catch (Exception c) {
                }
                temp.delete();
            } catch (IOException e) {
            }
        }
        return roles;
    }

    /**
	 * Return Name<Owner<Creation date,.....
	 */
    @WebMethod
    public String listAllRulesInDB(String dbName, String key) {
        String strTemp = null;
        String rules = "none";
        if (this.isLogged(key)) {
            this.renewSession(key);
            try {
                File temp = File.createTempFile("dbru", ".vbs");
                BufferedWriter out = new BufferedWriter(new FileWriter(temp));
                out.write("strDBServerName = \"" + this.serverAddress + "\"");
                out.newLine();
                out.write("Set objSQLServer = CreateObject(\"SQLDMO.SQLServer\")");
                out.newLine();
                out.write("objSQLServer.LoginSecure = True");
                out.newLine();
                out.write("objSQLServer.Connect strDBServerName");
                out.newLine();
                out.write("Set objDB = objSQLServer.Databases(\"" + dbName + "\")");
                out.newLine();
                out.write("Set colRules = objDB.Rules");
                out.newLine();
                out.write("WScript.Echo \"start\"");
                out.newLine();
                out.write("For Each objRule In colRules");
                out.newLine();
                out.write("table = objRule.Name");
                out.newLine();
                out.write("table = table + \"<\" +objRule.Owner");
                out.newLine();
                out.write("table = table + \"<\" +objRule.CreateDate");
                out.newLine();
                out.write("WScript.Echo table");
                out.newLine();
                out.write("Next");
                out.newLine();
                out.write("objSQLServer.DisConnect");
                out.newLine();
                out.write("Set objSQLServer = Nothing");
                out.close();
                String path = temp.getAbsolutePath();
                path = "\"" + path + "\"";
                Process pro = this.execute("cscript " + path);
                BufferedReader buffer = new BufferedReader(new InputStreamReader(pro.getInputStream()));
                try {
                    while ((strTemp = buffer.readLine()) != null) {
                        if (strTemp.startsWith("start")) {
                            while ((strTemp = buffer.readLine()) != null) {
                                if (rules.equals("none")) rules = strTemp.trim(); else rules = rules + "," + strTemp.trim();
                            }
                        }
                    }
                } catch (Exception c) {
                }
                temp.delete();
            } catch (IOException e) {
            }
        }
        return rules;
    }

    /**
	 * REturn Name<Owner<Type<Creation date,...
	 */
    @WebMethod
    public String listAllStoredProcedureInDB(String dbName, String key) {
        String strTemp = null;
        String sp = "none";
        if (this.isLogged(key)) {
            this.renewSession(key);
            try {
                File temp = File.createTempFile("dbsp", ".vbs");
                BufferedWriter out = new BufferedWriter(new FileWriter(temp));
                out.write("strDBServerName = \"" + this.serverAddress + "\"");
                out.newLine();
                out.write("Set objSQLServer = CreateObject(\"SQLDMO.SQLServer\")");
                out.newLine();
                out.write("objSQLServer.LoginSecure = True");
                out.newLine();
                out.write("objSQLServer.Connect strDBServerName");
                out.newLine();
                out.write("Set objDB = objSQLServer.Databases(\"" + dbName + "\")");
                out.newLine();
                out.write("Set colStoredProcedures = objDB.StoredProcedures");
                out.newLine();
                out.write("WScript.Echo \"start\"");
                out.newLine();
                out.write("For Each objStoredProcedure In colStoredProcedures");
                out.newLine();
                out.write("table = objStoredProcedure.Name");
                out.newLine();
                out.write("table = table + \"<\" +objStoredProcedure.Owner");
                out.newLine();
                out.write("If (objStoredProcedure.TypeOf = 2) Then");
                out.newLine();
                out.write("table = table + \"<\" +\"System\"");
                out.newLine();
                out.write("Else");
                out.newLine();
                out.write("table = table + \"<\" +\"user\"");
                out.newLine();
                out.write("End If");
                out.newLine();
                out.write("table = table + \"<\" +objStoredProcedure.CreateDate");
                out.newLine();
                out.write("WScript.Echo table");
                out.newLine();
                out.write("Next");
                out.newLine();
                out.write("objSQLServer.DisConnect");
                out.newLine();
                out.write("Set objSQLServer = Nothing");
                out.close();
                String path = temp.getAbsolutePath();
                path = "\"" + path + "\"";
                Process pro = this.execute("cscript " + path);
                BufferedReader buffer = new BufferedReader(new InputStreamReader(pro.getInputStream()));
                try {
                    while ((strTemp = buffer.readLine()) != null) {
                        if (strTemp.startsWith("start")) {
                            while ((strTemp = buffer.readLine()) != null) {
                                if (sp.equals("none")) sp = strTemp.trim(); else sp = sp + "," + strTemp.trim();
                            }
                        }
                    }
                } catch (Exception c) {
                }
                temp.delete();
            } catch (IOException e) {
            }
        }
        return sp;
    }

    /**
	 * 
	 * Return Name<Owner<Type<created Date,Name<Owner<Type<created Date,...
	 */
    @WebMethod
    public String listAllTablesInDB(String dbName, String key) {
        String strTemp = null;
        String tables = "none";
        if (this.isLogged(key)) {
            this.renewSession(key);
            try {
                File temp = File.createTempFile("dbt", ".vbs");
                BufferedWriter out = new BufferedWriter(new FileWriter(temp));
                out.write("strDBServerName = \"" + this.serverAddress + "\"");
                out.newLine();
                out.write("Set objSQLServer = CreateObject(\"SQLDMO.SQLServer\")");
                out.newLine();
                out.write("objSQLServer.LoginSecure = True");
                out.newLine();
                out.write("objSQLServer.Connect strDBServerName");
                out.newLine();
                out.write("Set objDB = objSQLServer.Databases(\"" + dbName + "\")");
                out.newLine();
                out.write("Set colTables = objDB.Tables");
                out.newLine();
                out.write("WScript.Echo \"start\"");
                out.newLine();
                out.write("For Each objTable In colTables");
                out.newLine();
                out.write("table = objTable.Name");
                out.newLine();
                out.write("table = table + \"<\" +objTable.Owner");
                out.newLine();
                out.write("If (objTable.TypeOf = 2) Then");
                out.newLine();
                out.write("table = table + \"<\" +\"System\"");
                out.newLine();
                out.write("Else");
                out.newLine();
                out.write("table = table + \"<\" +\"user\"");
                out.newLine();
                out.write("End If");
                out.newLine();
                out.write("table = table + \"<\" +objTable.CreateDate");
                out.newLine();
                out.write("WScript.Echo table");
                out.newLine();
                out.write("Next");
                out.newLine();
                out.write("objSQLServer.DisConnect");
                out.newLine();
                out.write("Set objSQLServer = Nothing");
                out.close();
                String path = temp.getAbsolutePath();
                path = "\"" + path + "\"";
                Process pro = this.execute("cscript " + path);
                BufferedReader buffer = new BufferedReader(new InputStreamReader(pro.getInputStream()));
                try {
                    while ((strTemp = buffer.readLine()) != null) {
                        if (strTemp.startsWith("start")) {
                            while ((strTemp = buffer.readLine()) != null) {
                                if (tables.equals("none")) tables = strTemp.trim(); else tables = tables + "," + strTemp.trim();
                            }
                        }
                    }
                } catch (Exception c) {
                }
                temp.delete();
            } catch (IOException e) {
            }
        }
        return tables;
    }

    @WebMethod
    public String listAllUsersInDB(String dbName, String key) {
        String strTemp = null;
        String users = "none";
        if (this.isLogged(key)) {
            this.renewSession(key);
            try {
                File temp = File.createTempFile("dbu", ".vbs");
                BufferedWriter out = new BufferedWriter(new FileWriter(temp));
                out.write("strDBServerName = \"" + this.serverAddress + "\"");
                out.newLine();
                out.write("Set objSQLServer = CreateObject(\"SQLDMO.SQLServer\")");
                out.newLine();
                out.write("objSQLServer.LoginSecure = True");
                out.newLine();
                out.write("objSQLServer.Connect strDBServerName");
                out.newLine();
                out.write("Set objDB = objSQLServer.Databases(\"" + dbName + "\")");
                out.newLine();
                out.write("Set colUsers = objDB.Users");
                out.newLine();
                out.write("WScript.Echo \"start\"");
                out.newLine();
                out.write("For Each objUser In colUsers");
                out.newLine();
                out.write("WScript.Echo objUser.Name & \"&\" & objUser.Login");
                out.newLine();
                out.write("Next");
                out.newLine();
                out.write("objSQLServer.DisConnect");
                out.newLine();
                out.write("Set objSQLServer = Nothing");
                out.close();
                String path = temp.getAbsolutePath();
                path = "\"" + path + "\"";
                Process pro = this.execute("cscript " + path);
                BufferedReader buffer = new BufferedReader(new InputStreamReader(pro.getInputStream()));
                try {
                    while ((strTemp = buffer.readLine()) != null) {
                        if (strTemp.startsWith("start")) {
                            while ((strTemp = buffer.readLine()) != null) {
                                if (users.equals("none")) users = strTemp.trim(); else users = users + "," + strTemp.trim();
                            }
                        }
                    }
                } catch (Exception c) {
                }
                temp.delete();
            } catch (IOException e) {
            }
        }
        return users;
    }

    /**
	 * REturn Name<owner<type<creation date,....
	 */
    @WebMethod
    public String listAllViewsInDB(String dbName, String key) {
        String strTemp = null;
        String views = "none";
        if (this.isLogged(key)) {
            this.renewSession(key);
            try {
                File temp = File.createTempFile("dbv", ".vbs");
                BufferedWriter out = new BufferedWriter(new FileWriter(temp));
                out.write("strDBServerName = \"" + this.serverAddress + "\"");
                out.newLine();
                out.write("Set objSQLServer = CreateObject(\"SQLDMO.SQLServer\")");
                out.newLine();
                out.write("objSQLServer.LoginSecure = True");
                out.newLine();
                out.write("objSQLServer.Connect strDBServerName");
                out.newLine();
                out.write("Set colViews = objSQLServer.Databases(\"" + dbName + "\").Views");
                out.newLine();
                out.write("WScript.Echo \"start\"");
                out.newLine();
                out.write("For Each objView In colViews");
                out.newLine();
                out.write("table = objView.Name");
                out.newLine();
                out.write("table = table + \"<\" +objView.Owner");
                out.newLine();
                out.write("If (objView.TypeOf = 2) Then");
                out.newLine();
                out.write("table = table + \"<\" +\"System\"");
                out.newLine();
                out.write("Else");
                out.newLine();
                out.write("table = table + \"<\" +\"user\"");
                out.newLine();
                out.write("End If");
                out.newLine();
                out.write("table = table + \"<\" +objView.CreateDate");
                out.newLine();
                out.write("WScript.Echo table");
                out.newLine();
                out.write("Next");
                out.newLine();
                out.write("objSQLServer.DisConnect");
                out.newLine();
                out.write("Set objSQLServer = Nothing");
                out.close();
                String path = temp.getAbsolutePath();
                path = "\"" + path + "\"";
                Process pro = this.execute("cscript " + path);
                BufferedReader buffer = new BufferedReader(new InputStreamReader(pro.getInputStream()));
                try {
                    while ((strTemp = buffer.readLine()) != null) {
                        if (strTemp.startsWith("start")) {
                            while ((strTemp = buffer.readLine()) != null) {
                                if (views.equals("none")) views = strTemp.trim(); else views = views + "," + strTemp.trim();
                            }
                        }
                    }
                } catch (Exception c) {
                }
                temp.delete();
            } catch (IOException e) {
            }
        }
        return views;
    }

    @WebMethod
    public boolean startJob(String jobName, String key) {
        String strTemp = null;
        boolean result = false;
        if (this.isLogged(key)) {
            this.renewSession(key);
            try {
                File temp = File.createTempFile("dbsiz", ".vbs");
                BufferedWriter out = new BufferedWriter(new FileWriter(temp));
                out.write("strDBServerName = \"" + this.serverAddress + "\"");
                out.newLine();
                out.write("Set objSQLServer = CreateObject(\"SQLDMO.SQLServer\")");
                out.newLine();
                out.write("objSQLServer.LoginSecure = True");
                out.newLine();
                out.write("objSQLServer.Connect strDBServerName");
                out.newLine();
                out.write("objSQLServer.JobServer.Jobs(\"" + jobName + "\").Start");
                out.newLine();
                out.write("WScript.echo \"Job Started\"");
                out.newLine();
                out.write("objSQLServer.DisConnect");
                out.newLine();
                out.write("Set objSQLServer = Nothing");
                out.close();
                String path = temp.getAbsolutePath();
                path = "\"" + path + "\"";
                Process pro = this.execute("cscript " + path);
                BufferedReader buffer = new BufferedReader(new InputStreamReader(pro.getInputStream()));
                try {
                    while ((strTemp = buffer.readLine()) != null) {
                        if (strTemp.trim().equals("Job Started")) {
                            result = true;
                            break;
                        }
                    }
                } catch (Exception c) {
                }
                temp.delete();
            } catch (IOException e) {
            }
        }
        return result;
    }

    @WebMethod
    public boolean stopJob(String jobName, String key) {
        String strTemp = null;
        boolean result = false;
        if (this.isLogged(key)) {
            this.renewSession(key);
            try {
                File temp = File.createTempFile("dbsiz", ".vbs");
                BufferedWriter out = new BufferedWriter(new FileWriter(temp));
                out.write("strDBServerName = \"" + this.serverAddress + "\"");
                out.newLine();
                out.write("Set objSQLServer = CreateObject(\"SQLDMO.SQLServer\")");
                out.newLine();
                out.write("objSQLServer.LoginSecure = True");
                out.newLine();
                out.write("objSQLServer.Connect strDBServerName");
                out.newLine();
                out.write("objSQLServer.JobServer.Jobs(\"" + jobName + "\").Stop");
                out.newLine();
                out.write("WScript.echo \"Job stoped\"");
                out.newLine();
                out.write("objSQLServer.DisConnect");
                out.newLine();
                out.write("Set objSQLServer = Nothing");
                out.close();
                String path = temp.getAbsolutePath();
                path = "\"" + path + "\"";
                Process pro = this.execute("cscript " + path);
                BufferedReader buffer = new BufferedReader(new InputStreamReader(pro.getInputStream()));
                try {
                    while ((strTemp = buffer.readLine()) != null) {
                        if (strTemp.trim().equals("Job stoped")) {
                            result = true;
                            break;
                        }
                    }
                } catch (Exception c) {
                }
                temp.delete();
            } catch (IOException e) {
            }
        }
        return result;
    }

    private Process execute(String command) {
        Process pro = null;
        try {
            command = "cmd.exe " + "/c " + command;
            pro = Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pro;
    }

    private String getSystemPath() {
        String programFilesPath = null;
        programFilesPath = System.getenv("ProgramFiles");
        return programFilesPath;
    }

    @WebMethod
    public boolean startSQLSERVER(String key) {
        boolean start = false;
        String strTemp = null;
        if (this.isLogged(key)) {
            this.renewSession(key);
            try {
                File temp = File.createTempFile("dbsp", ".vbs");
                BufferedWriter out = new BufferedWriter(new FileWriter(temp));
                out.write("strDBServerName = \"" + this.serverAddress + "\"");
                out.newLine();
                out.write("Set objSQLServer = CreateObject(\"SQLDMO.SQLServer\")");
                out.newLine();
                out.write("objSQLServer.LoginSecure = True");
                out.newLine();
                out.write("objSQLServer.Name = strDBServerName");
                out.newLine();
                out.write("objSQLServer.Start False");
                out.newLine();
                out.write("WScript.Echo \"done\"");
                out.newLine();
                out.write("objSQLServer.DisConnect");
                out.newLine();
                out.write("Set objSQLServer = Nothing");
                out.close();
                String path = temp.getAbsolutePath();
                path = "\"" + path + "\"";
                Process pro = this.execute("cscript " + path);
                BufferedReader buffer = new BufferedReader(new InputStreamReader(pro.getInputStream()));
                try {
                    while ((strTemp = buffer.readLine()) != null) {
                        if (strTemp.trim().equals("done")) {
                            start = true;
                            break;
                        }
                    }
                } catch (Exception c) {
                }
                temp.delete();
            } catch (IOException e) {
                start = false;
            }
        }
        return start;
    }

    @WebMethod
    public boolean stopSQLSERVER(String key) {
        boolean stop = false;
        String strTemp = null;
        if (this.isLogged(key)) {
            this.renewSession(key);
            try {
                File temp = File.createTempFile("dbsp", ".vbs");
                BufferedWriter out = new BufferedWriter(new FileWriter(temp));
                out.write("strDBServerName = \"" + this.serverAddress + "\"");
                out.newLine();
                out.write("Set objSQLServer = CreateObject(\"SQLDMO.SQLServer\")");
                out.newLine();
                out.write("objSQLServer.LoginSecure = True");
                out.newLine();
                out.write("objSQLServer.Connect strDBServerName");
                out.newLine();
                out.write("WScript.Echo \"done\"");
                out.newLine();
                out.write("objSQLServer.Shutdown");
                out.newLine();
                out.write("objSQLServer.DisConnect");
                out.newLine();
                out.write("Set objSQLServer = Nothing");
                out.close();
                String path = temp.getAbsolutePath();
                path = "\"" + path + "\"";
                Process pro = this.execute("cscript " + path);
                BufferedReader buffer = new BufferedReader(new InputStreamReader(pro.getInputStream()));
                try {
                    while ((strTemp = buffer.readLine()) != null) {
                        if (strTemp.trim().equals("done")) {
                            stop = true;
                            break;
                        }
                    }
                } catch (Exception c) {
                }
                temp.delete();
            } catch (IOException e) {
                stop = false;
            }
        }
        return stop;
    }
}
