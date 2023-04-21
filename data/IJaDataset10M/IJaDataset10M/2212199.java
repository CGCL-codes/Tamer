package com.nhncorp.cubridqa.replication.configuration;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.nhncorp.cubridqa.console.ConsoleAgent;
import com.nhncorp.cubridqa.replication.OutputLogger;
import com.nhncorp.cubridqa.replication.config.ShellFileMaker;
import com.nhncorp.cubridqa.replication.parameters.Distribution;
import com.nhncorp.cubridqa.replication.parameters.Master;
import com.nhncorp.cubridqa.replication.parameters.ReplicationParameters;
import com.nhncorp.cubridqa.replication.parameters.Slave;
import com.nhncorp.cubridqa.replication.util.BaseInfo;
import com.nhncorp.cubridqa.replication.util.SystemConst;
import com.nhncorp.cubridqa.replication.xmldoc.ConfigurationXMLReader;
import com.nhncorp.cubridqa.replication.xmldoc.DatabaseXMLReader;
import com.nhncorp.cubridqa.replication.xmldoc.ServerXMLReader;

/**
 * 
 * @ClassName: ConfigurationReader
 * @Description: used for reading the configuration file and get parameters in
 *               it.
 * 
 * 
 * @date 2009-9-1
 * @version V1.0 Copyright (C) www.nhn.com
 */
public class ConfigurationReader {

    private static Logger logger = (new OutputLogger("replication", "configuration")).getLogger();

    private final String conf_Path = "replication/conf";

    /**
	 * 
	 * @param filename
	 * @throws Exception
	 */
    public Map readModuleFrom() throws Exception {
        String filename = SystemConst.QA_PATH + "replication/" + "/shell/expect_module.xml";
        ConsoleAgent.addMessage("Read the module  file now !" + SystemConst.LINE_SEPERATOR);
        logger.log(Level.INFO, "Read the module  file now !");
        Map expectModule = new HashMap();
        expectModule = ShellFileMaker.xmlReader(filename);
        if (expectModule == null) {
            ConsoleAgent.addMessage("Error: Read module error!" + SystemConst.LINE_SEPERATOR);
            logger.log(Level.SEVERE, "Error: Read module error!");
            throw new Exception("Error: Read module error!");
        }
        return expectModule;
    }

    /**
	 * get all configuration parameters from xml
	 * 
	 * @return
	 * @throws Exception
	 */
    public List getAllConfigurationParameters() throws Exception {
        ConsoleAgent.addMessage("Read configurations files" + SystemConst.LINE_SEPERATOR);
        String para_path = SystemConst.QA_PATH + this.conf_Path + "/parameters";
        List masters = new ArrayList();
        File[] mst_folders = this.getSubFoldersInFolder(para_path);
        if (mst_folders == null) {
            ConsoleAgent.addMessage("Error:master folder is null" + SystemConst.LINE_SEPERATOR);
            logger.log(Level.SEVERE, "Error:master folder is null");
            throw new Exception("Error:master folder is null");
        }
        for (int i = 0; i < mst_folders.length; i++) {
            File mst_folder = mst_folders[i];
            masters.add(this.getAllConfigurationParameters(mst_folder.getAbsolutePath()));
        }
        return masters;
    }

    /**
	 * get master info according to ip and dbname
	 * 
	 * @param ip
	 * @param mastername
	 * @return
	 * @throws Exception
	 */
    public ReplicationParameters getMasterConfigurationParameters(String ip, String mastername) throws Exception {
        ConsoleAgent.addMessage("Read configurations files" + SystemConst.LINE_SEPERATOR);
        String para_path = SystemConst.QA_PATH + this.conf_Path + "/parameters";
        File[] mst_folders = this.getSubFoldersInFolder(para_path);
        if (mst_folders == null) {
            ConsoleAgent.addMessage("Error:master folder is null" + SystemConst.LINE_SEPERATOR);
            logger.log(Level.SEVERE, "Error:master folder is null");
            throw new Exception("Error:master folder is null");
        }
        for (int i = 0; i < mst_folders.length; i++) {
            File mst_folder = mst_folders[i];
            if (mst_folder.getName().equals("master_" + ip + "_" + mastername)) {
                ReplicationParameters mst = this.getXMLParameters(null, mst_folder);
                return mst;
            }
        }
        return null;
    }

    /**
	 * get all info according to the path of file
	 * 
	 * @param path
	 * @return
	 * @throws Exception
	 */
    public ReplicationParameters getAllConfigurationParameters(String path) throws Exception {
        File master_path = new File(path);
        ReplicationParameters master = this.getXMLParameters(null, master_path);
        if (master == null) {
            ConsoleAgent.addMessage("Error:get parameters error" + SystemConst.LINE_SEPERATOR);
            logger.log(Level.SEVERE, "Error:get parameters error");
            throw new Exception("Error:get parameters error");
        }
        return master;
    }

    /**
	 * get instance of class according to the name of file
	 * 
	 * @param filename
	 * @return
	 */
    private ReplicationParameters getInstance(String filename) {
        if (filename.indexOf("master") >= 0) return new Master(); else if (filename.indexOf("distribution") >= 0) return new Distribution(); else if (filename.indexOf("slave") >= 0) return new Slave(); else return null;
    }

    /**
	 * add params to parent params
	 * 
	 * @param parent
	 * @param para
	 * @param filename
	 * @return
	 */
    private ReplicationParameters addToPara(ReplicationParameters parent, ReplicationParameters para, String filename) {
        if (parent == null) return null;
        if (filename.indexOf("distribution") >= 0) {
            ((Master) parent).getDistributions().add(para);
        } else if (filename.indexOf("slave") >= 0) {
            ((Distribution) parent).getSlaves().add(para);
        }
        return parent;
    }

    /**
	 * get params from xml
	 * 
	 * @param parentPara
	 * @param parentFolder
	 * @return
	 * @throws Exception
	 */
    public ReplicationParameters getXMLParameters(ReplicationParameters parentPara, File parentFolder) throws Exception {
        File xml = this.getXMLInFolder(parentFolder);
        if (xml == null) {
            ConsoleAgent.addMessage("Error: configuration xml is null" + SystemConst.LINE_SEPERATOR);
            logger.log(Level.SEVERE, "Error: configuration xml is null");
            throw new Exception("Error: configuration xml is null");
        }
        ConfigurationXMLReader xmlconf = new ConfigurationXMLReader(xml);
        Map map = xmlconf.parseMap();
        if (map == null) {
            ConsoleAgent.addMessage("Error: configuration map error" + SystemConst.LINE_SEPERATOR);
            logger.log(Level.SEVERE, "Error: configuration map error");
            throw new Exception("Error: configuration map error");
        }
        ReplicationParameters para = this.getInstance(xml.getName());
        if (para == null) {
            ConsoleAgent.addMessage("Error: configuration instance error" + SystemConst.LINE_SEPERATOR);
            logger.log(Level.SEVERE, "Error: configuration instance error");
            throw new Exception("Error: configuration instance error");
        }
        para = this.setParameters(para, map);
        if (parentPara != null) parentPara = this.addToPara(parentPara, para, xml.getName());
        File[] folders = this.getSubFoldersInFolder(parentFolder);
        if (folders == null) return para;
        for (int i = 0; i < folders.length; i++) {
            File folder = folders[i];
            this.getXMLParameters(para, folder);
        }
        return para;
    }

    /**
	 * set params
	 * 
	 * @param parameters
	 * @param map
	 * @return
	 */
    public ReplicationParameters setParameters(ReplicationParameters parameters, Map map) {
        parameters.setDbType((String) map.get("type"));
        parameters = this.setServerInfo((String) ((Map) map.get("server")).get("ip"), (String) ((Map) map.get("db")).get("name"), parameters);
        if ((map.get("parameters") != null) && (!map.get("parameters").equals(""))) parameters.setParameters((Map) map.get("parameters"));
        return parameters;
    }

    /**
	 * set serverinfo
	 * 
	 * @param ipAddress
	 * @return
	 */
    public ReplicationParameters setServerInfo(String ipAddress, String dbName, ReplicationParameters para) {
        String path = SystemConst.QA_PATH + this.conf_Path + "/servers/";
        File[] server_folders = this.getSubFoldersInFolder(path);
        for (int i = 0; i < server_folders.length; i++) {
            File server_folder = server_folders[i];
            if (server_folder.getName().equals(ipAddress)) {
                File serverxml = this.getXMLInFolder(server_folder);
                ServerXMLReader sxr = new ServerXMLReader(serverxml);
                Map smap = sxr.parseMap();
                para.setServer(smap);
                File[] db_folders = this.getSubFoldersInFolder(server_folder);
                for (int j = 0; j < db_folders.length; j++) {
                    File db_folder = db_folders[j];
                    if (db_folder.getName().substring(9).toLowerCase().equals(dbName)) {
                        File databasexml = this.getXMLInFolder(db_folder);
                        DatabaseXMLReader dxr = new DatabaseXMLReader(databasexml);
                        Map dmap = dxr.parseMap();
                        para.setDatabase(dmap);
                        break;
                    }
                }
                return para;
            }
        }
        return null;
    }

    /**
	 * 
	 * @param path
	 * @return
	 */
    public File getXMLInFolder(String path) {
        File file_path = new File(path);
        return this.getXMLInFolder(file_path);
    }

    /**
	 * 
	 * @param path
	 * @return
	 */
    public File[] getXMLsInFolder(String path) {
        File file_path = new File(path);
        return this.getXMLsInFolder(file_path);
    }

    /**
	 * 
	 * @param path
	 * @return
	 */
    public File[] getSubFoldersInFolder(String path) {
        File file_path = new File(path);
        return this.getSubFoldersInFolder(file_path);
    }

    /**
	 * 
	 * @param file_path
	 * @return
	 */
    public File getXMLInFolder(File file_path) {
        if (file_path.exists() && file_path.isDirectory()) {
            FileFilter xmlfilter = BaseInfo.getXMLFileFilter(file_path);
            File[] listfiles = file_path.listFiles(xmlfilter);
            if (listfiles.length > 0) return listfiles[0];
        }
        return null;
    }

    /**
	 * 
	 * @param file_path
	 * @return
	 */
    public File[] getXMLsInFolder(File file_path) {
        if (file_path.exists() && file_path.isDirectory()) {
            FileFilter filter = BaseInfo.getXMLFileFilter(file_path);
            File[] listfiles = file_path.listFiles(filter);
            if (listfiles.length > 0) return listfiles;
        }
        return null;
    }

    /**
	 * 
	 * @param file_path
	 * @return
	 */
    public File[] getSubFoldersInFolder(File file_path) {
        if (file_path.exists() && file_path.isDirectory()) {
            FileFilter filter = BaseInfo.getFolderFilter(file_path);
            File[] listfolders = file_path.listFiles(filter);
            if (listfolders.length > 0) return listfolders;
        }
        return null;
    }
}
