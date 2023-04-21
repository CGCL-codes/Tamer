package de.karlsruhe.rz.unicore.plugins.nastran;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.*;
import javax.swing.ImageIcon;
import com.pallas.unicore.extensions.*;
import com.pallas.unicore.container.*;
import com.pallas.unicore.container.errorspec.*;
import com.pallas.unicore.resourcemanager.*;
import org.unicore.Identifier;
import org.unicore.ajo.*;
import org.unicore.resources.*;
import org.unicore.sets.*;

/**
 *  Container that executes a nastran job on the vsite and manages file imports and exports. Inherit from
 *  ExecuteContainer to include file imports and exports, as well as stdin/stdout and stderr.
 *
 *  Copyright 2002 Rechenzentrum Universit�t Karlsruhe TH
 *
 *@author     Filip Polsakiewicz
 *@created    15. Janaur 2002
 *@version    $Id: NastranContainer.java,v 1.1.1.1 2005/11/08 13:00:32 bschuller Exp $
 */
public class NastranContainer extends UserContainer implements Serializable {

    private String importDirectory, exportDirectory;

    private String keywordString = "";

    private FileImport bulkDataImport;

    private String nastranDirectory;

    private String rcFileContent, dbsId, outId;

    private String nastranVersion;

    private boolean restart = false, scratch = false;

    private static ResourceBundle res = ResourceBundle.getBundle("de.karlsruhe.rz.unicore.plugins.nastran.ResourceStrings");

    static final long serialVersionUID = 4167955261798674857L;

    private static String[][] persistentXMLFields = { { "BULKDATAIMPORT", "bulkDataImport" }, { "RCFILECONTENT", "rcFileContent" }, { "KEYWORDSTRING", "keywordString" }, { "NASTRANVERSION", "nastranVersion" }, { "NASTRANDIRECTORY", "nastranDirectory" }, { "RESTART", "restart" }, { "SCRATCH", "scratch" }, { "DBSID", "dbsId" }, { "OUTID", "outId" } };

    /**
	 *  Constructor
	 *
	 *@param  parentContainer  parent job group
	 */
    public NastranContainer(GroupContainer parentContainer) {
        super(parentContainer);
    }

    /**
         * Returns Containervalue for dbs-id
         * @return The containers value for dbsId
         */
    public String getDbsId() {
        return dbsId;
    }

    /**
         * Returns Containervalue for OutId
         * @return The containers outId value
         */
    public String getOutId() {
        return outId;
    }

    /**
         * Returns container value for restart
         * @return The containers Restart Value
         */
    public boolean getRestart() {
        return restart;
    }

    /**
         * Returns the containers scratch value
         * @return The containers scratch value
         */
    public boolean getScratch() {
        return scratch;
    }

    /**
         * Gets the Keyword-String
         * @return the Keyword String
         */
    public String getKeywordString() {
        return keywordString;
    }

    /**
          * Returns chosen Nastran-Version
          * @return Nastran Version String
          */
    public String getNastranVersion() {
        return nastranVersion;
    }

    /**
         * Gets the ImportDirectory
         * @return ImportDirectory
         */
    public String getImportDirectory() {
        return importDirectory;
    }

    /**
         * Gets the ExportDirectory
         * @return ExportDirectory
         */
    public String getExportDirectory() {
        return exportDirectory;
    }

    /**
          * Returns RC-File Content
          * @return The Content of the RC-File
          */
    public String getRcFileContent() {
        return rcFileContent;
    }

    /**
          * Returns Nastran Directory
          * @return The Nastran Directory
          */
    public String getNastranDirectory() {
        return nastranDirectory;
    }

    /**
          * Returns BulkDataImport
          * @return The BulkDataImport
          */
    public FileImport getBulkDataImport() {
        return bulkDataImport;
    }

    /**
         * Sets Containers DBS-ID
         * @param dbsId The new DBS-ID value
         */
    public final void setDbsId(String dbsId) {
        this.dbsId = dbsId;
    }

    /**
         * Sets Containers OUT-ID
         * @param outId The new OUT-ID value
         */
    public final void setOutId(String outId) {
        this.outId = outId;
    }

    /**
         * Sets Containers Restart value
         * @param restart The new RESTART value
         */
    public final void setRestart(boolean restart) {
        this.restart = restart;
    }

    /**
         * Sets Conatiners Scratch value
         * @param scratch The new SCRATCH value
         */
    public final void setScratch(boolean scratch) {
        this.scratch = scratch;
    }

    /**
         * Sets Containers NastranVersion value
         * @param version The new Version value
         */
    public final void setNastranVersion(String version) {
        this.nastranVersion = version;
    }

    /**
         * Sets Containers RcFileContent value
         * @param content The new Content value
         */
    public final void setRcFileContent(String content) {
        this.rcFileContent = content;
    }

    /**
         * Sets Containers KeywordString value
         * @param KeywordString The new KeywordString value
         */
    public final void setKeywordString(String KeywordString) {
        this.keywordString = KeywordString;
    }

    /**
         * Sets Containers NastranDirectory
         * @param nastranDirectory New nastranDirectory value
         */
    public final void setNastranDirectory(String nastranDirectory) {
        this.nastranDirectory = nastranDirectory;
    }

    /**
         * Sets containers BulkDataImport
         * @param bulkDataImport The new BulkDataImport value
         */
    public final void setBulkDataImport(FileImport bulkDataImport) {
        this.bulkDataImport = bulkDataImport;
    }

    /**
         * Sets Import-Directory
         * @param _dir ImportDirectory name
         */
    public void setImportDirectory(String _dir) {
        this.importDirectory = _dir;
    }

    /**
         * Sets Export Directory
         * @param _dir ExportDirectory name
         */
    public void setExportDirectory(String _dir) {
        this.exportDirectory = _dir;
    }

    /**
	 *  Add container specific errors to error set of super classes and select appropriate icon for JPA
	 *  display.
	 *
	 *@return    ErrorSet set containing all errors including those from super classes
	 */
    public ErrorSet checkContents() {
        ErrorSet err = super.checkContents();
        if ((rcFileContent == null || rcFileContent.length() == 0) || rcFileContent.equals("\n")) {
            err.add(new UError(getIdentifier(), res.getString("MISSING_RC")));
        }
        boolean softwareAvailable = false;
        ResourceSet siteResources = ResourceManager.getResourceSet(getVsite()).getResourceSetClone();
        ResourceEnumeration siteEnum = siteResources.elements();
        while (siteEnum.hasMoreElements()) {
            Resource siteResource = (Resource) siteEnum.nextElement();
            if (siteResource instanceof SoftwareResource) {
                String siteSoftware = ((SoftwareResource) siteResource).getName();
                if (siteSoftware.startsWith(NastranPlugin.RESOURCE_IDENTIFIER)) {
                    softwareAvailable = true;
                }
            }
        }
        if (!softwareAvailable) {
            String vsiteName = "Unknown";
            if (getVsite() != null) {
                vsiteName = getVsite().getName();
            }
            err.add(new UError(getIdentifier(), "Nastran plugin is not supported at site " + vsiteName));
        }
        if (bulkDataImport == null) {
            err.add(new UError(getIdentifier(), res.getString("MISSING_BULK")));
        }
        setErrors(err);
        return err;
    }

    /**
         * Builds Task ActionGroup
         */
    public void buildActionGroup() {
        ActionGroup importGroup = new ActionGroup(getName() + "_IMPORT");
        ImportFactory importFactory = new ImportFactory(getFileImports(), importGroup, this);
        importFactory.build();
        ActionGroup exportGroup = new ActionGroup(getName() + "_EXPORT");
        ExportFactory exportFactory = new ExportFactory(getFileExports(), exportGroup, this);
        exportFactory.build();
        ActionGroup nastranGroup = new ActionGroup(getName() + "_NASTRAN");
        buildExecuteGroup(nastranGroup);
        actionGroup = new ActionGroup(getName());
        actionGroup.add(nastranGroup);
        if (importGroup.getActions().hasMoreElements()) {
            actionGroup.add(importGroup);
            actionGroup.add(new Dependency(importGroup, nastranGroup));
        }
        if (exportGroup.getActions().hasMoreElements()) {
            actionGroup.add(exportGroup);
            actionGroup.add(new Dependency(nastranGroup, exportGroup));
        }
        setIdentifier(actionGroup.getId());
    }

    /**
	 *  Build ActionGroup for nastran execution.
	 *
	 *@param  nastranGroup  Description of Parameter
	 */
    protected void buildExecuteGroup(ActionGroup nastranGroup) {
        OptionSet env = getEnv();
        if (env == null) {
            env = new OptionSet();
        }
        String uspaceRcFileName = null;
        try {
            if (this.getNastranVersion().indexOf("20", 0) != -1) {
                String nVersion = this.getNastranVersion();
                uspaceRcFileName = ".nast" + nVersion.substring(nVersion.indexOf("20", 0), nVersion.indexOf("20", 0) + 4) + "rc";
            } else if (this.getNastranVersion().indexOf("70.7", 0) != -1) {
                uspaceRcFileName = ".nast707rc";
            }
        } catch (java.lang.NullPointerException npe) {
            System.err.println("Error");
        }
        System.err.println(uspaceRcFileName);
        byte[][] contents = new byte[1][];
        contents[0] = rcFileContent.getBytes();
        String[] files = { uspaceRcFileName };
        if (files == null || files[0].trim().equals("")) {
            files = new String[1];
            files[0] = getName() + "_NASTRANIMPORT_" + System.currentTimeMillis();
        }
        env.add(new Option("DBS", getDbsId()));
        env.add(new Option("OUT", getOutId()));
        if (getRestart()) {
            env.add(new Option("RESTART", "yes"));
        } else {
            env.add(new Option("RESTART", "no"));
        }
        if (getScratch()) {
            env.add(new Option("SCRATCH", "yes"));
        } else {
            env.add(new Option("SCRATCH", "no"));
        }
        env.add(new Option("BDF", getBulkDataImport().getDestinationName()));
        env.add(new Option("RC_FILE", getRcFileContent()));
        IncarnateFiles importTask = new IncarnateFiles(getName() + "_INCARNATEFILES_" + System.currentTimeMillis(), new ResourceSet(), files, contents, true);
        MakePortfolio makePortfolio = new MakePortfolio(getName() + "_MP_NASTRAN_" + System.currentTimeMillis(), null, new ResourceSet(), files);
        nastranGroup.add(importTask);
        nastranGroup.add(makePortfolio);
        nastranGroup.add(new Dependency(importTask, makePortfolio));
        ResourceSet taskResourceSet = getResourceSet().getResourceSetClone();
        taskResourceSet.add(getPreinstalledSoftware());
        UserTask executeTask = new UserTask(getName(), null, taskResourceSet, env, getCommandLine(), null, getRedirectStdout(), getRedirectStderr(), isVerboseOn(), isVersionOn(), makePortfolio.getPortfolio(), getMeasureTime(), getDebug(), getProfile());
        nastranGroup.add(executeTask);
        nastranGroup.add(new Dependency(makePortfolio, executeTask));
        if (getRedirectStdout()) {
            executeTask.setStdoutFileName(getStdout());
        }
        if (getRedirectStderr()) {
            executeTask.setStderrFileName(getStderr());
        }
    }
}
