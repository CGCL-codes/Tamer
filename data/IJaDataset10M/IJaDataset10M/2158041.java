/*
 * PowerFolder, the OpenSource Workflow Server
 *
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */

package org.powerfolder.workflow.lifecycle;

//base classes
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Properties;

import org.powerfolder.PFRuntimeException;
import org.powerfolder.apps.WorkflowApplication;
import org.powerfolder.apps.WorkflowScript;
import org.powerfolder.apps.WorkflowWebFile;
import org.powerfolder.apps.WorkflowWebPage;
import org.powerfolder.utils.misc.MiscHelper;

public class GenericFileWorkflowApplication implements WorkflowApplication {

    private final static String DEFAULT_DESCRIPTION =
        "Put your description here for application ";
    private File file = null;
    private String description = null;
    private File descriptionFile = null;
    private Properties properties = null;
    private File propertiesFile = null;
    private GenericFileWorkflowScript script = null;
    private ArrayList webPages = null;
    private ArrayList webFiles = null;
    
    protected GenericFileWorkflowApplication(File inFile) {
        
        //initialization
        this.file = inFile;
        this.webPages = new ArrayList();
        this.webFiles = new ArrayList();
        
        //script
        this.script = new GenericFileWorkflowScript(
            new File(this.file, "script.xml"), this.file.getName());
        
        //description
        this.description = DEFAULT_DESCRIPTION + this.file.getName();
        this.descriptionFile = new File(this.file, "description.txt");
        
        //properties
        this.properties = new Properties();
        //this.properties.setProperty("PF_VERSION", VersionHelper.PF_VERSION);
        this.propertiesFile = new File(this.file, "application.properties");
        
        //web pages
        File webPagesDir = getWebPagesDirectory(this.file);
        //System.out.println("webPagesDir = " + webPagesDir.getAbsolutePath());
        File webPages[] = webPagesDir.listFiles();
        for (int i = 0; i < webPages.length; i++) {
            File nextWebPage = webPages[i];
            if (nextWebPage.isFile()) {
                //&& nextWebPage.getName().endsWith(".jsp")) {
                this.webPages.add(
                    new GenericFileWorkflowWebPage(nextWebPage));
            }
        }
        
        //web files
        File webFilesDir = getWebFilesDirectory(this.file);
        //System.out.println("webFilesDir = " + webFilesDir.getAbsolutePath());
        File webFiles[] = webFilesDir.listFiles();
        for (int i = 0; i < webFiles.length; i++) {
            File nextWebFile = webFiles[i];
            if (nextWebFile.isFile()) {
                this.webFiles.add(
                    new GenericFileWorkflowWebFile(nextWebFile));
            }
        }
    }
    
    protected void load() {
        
        try {
        
            if (!this.file.exists()) {
                this.file.mkdirs();
            }

            this.script.load();

            this.description = MiscHelper.readTextFile(this.descriptionFile);
            if (this.propertiesFile.exists()) {
                this.properties = new Properties();
                this.properties.load(new FileInputStream(this.propertiesFile));
            }

            for (int i = 0; i < this.webPages.size(); i++) {
                GenericFileWorkflowWebPage gfwwp =
                    (GenericFileWorkflowWebPage)this.webPages.get(i);
                gfwwp.load();
            }

            for (int i = 0; i < this.webFiles.size(); i++) {
                GenericFileWorkflowWebFile gfwwf =
                    (GenericFileWorkflowWebFile)this.webFiles.get(i);
                gfwwf.load();
            }
        }
        catch (IOException ioe) {
            throw new PFRuntimeException(ioe);
        }
    }

    protected void store() {
        
        try {
            //System.out.println("Storing application at - "
            //    + this.file.getAbsolutePath());

            if (!this.file.exists()) {
                this.file.mkdirs();
            }

            this.script.store();

            this.properties.store(new FileOutputStream(this.propertiesFile),
                "Properties for application '" + getName() + "'");

            MiscHelper.writeTextFile(this.descriptionFile, this.description);

            //create pages and files directories
            getWebPagesDirectory(this.file);
            getWebFilesDirectory(this.file);

            for (int i = 0; i < this.webPages.size(); i++) {
                GenericFileWorkflowWebPage gfwwp =
                    (GenericFileWorkflowWebPage)this.webPages.get(i);
                gfwwp.store();
            }

            for (int i = 0; i < this.webFiles.size(); i++) {
                GenericFileWorkflowWebFile gfwwf =
                    (GenericFileWorkflowWebFile)this.webFiles.get(i);
                gfwwf.store();
            }
        }
        catch (IOException ioe) {
            throw new PFRuntimeException(ioe);
        }
    }
    
    private File getWebPagesDirectory(File inFile) {
        File outValue = null;
        outValue = new File(inFile, "pages");
        if (!outValue.exists()) {
            outValue.mkdirs();
        }
        return outValue;
    }
    
    private File getWebFilesDirectory(File inFile) {
        File outValue = null;
        outValue = new File(inFile, "files");
        if (!outValue.exists()) {
            outValue.mkdirs();
        }
        return outValue;
    }
    
    public String getName() {
        return this.file.getName();
    }
    
    public String getDescription() {
        
        String outValue = null;
        
        //if (this.description != null) {
        //}
        //else {
        //    File d = new File(this.file, "description.txt");
        //    String text = MiscHelper.readTextFile(d);
        //    this.description = text;
        //}
        outValue = this.description;
        
        return outValue;
    }
    
    public void setDescription(String inDescription) {
        this.description = inDescription;
        //File d = new File(this.file, "description.txt");
        //MiscHelper.writeTextFile(d, inDescription);
    }
    
    public WorkflowScript getScript() {
        
        WorkflowScript outValue = null;
        
        //if (this.script != null) {
        //}
        //else {
        //    File d = new File(this.file, "script.xml");
        //    String text = MiscHelper.readTextFile(d);
        //    this.script = text;
        //}
        outValue = this.script;
        
        return outValue;
    }
    
    //public void setScript(String inScript) {
    //    this.script = inScript;
    //    File d = new File(this.file, "script.xml");
    //    MiscHelper.writeTextFile(d, inScript);
    //}
    
    public String getFormattedDate() {
        return MiscHelper.formatTime(this.file.lastModified());
    }

    public int getWebPageCount() {
        return this.webPages.size();
    }
    
    public WorkflowWebPage getWebPage(int inIndex) {
        return (WorkflowWebPage)this.webPages.get(inIndex);
    }
    
    public WorkflowWebPage getWebPage(String inPage) {
        
        WorkflowWebPage outValue = null;
        
        for (int i = 0; i < this.webPages.size(); i++) {
            WorkflowWebPage wwp = (WorkflowWebPage)this.webPages.get(i);
            if (wwp.getName().equalsIgnoreCase(inPage)) {
                outValue = wwp;
                break;
            }
        }
        
        return outValue;
    }
    
    public int getWebFileCount() {
        return this.webFiles.size();
    }
    
    public WorkflowWebFile getWebFile(int inIndex) {
        return (WorkflowWebFile)this.webFiles.get(inIndex);
    }
    
    public WorkflowWebFile getWebFile(String inFile) {
        
        WorkflowWebFile outValue = null;
        
        for (int i = 0; i < this.webFiles.size(); i++) {
            WorkflowWebFile wwf = (WorkflowWebFile)this.webFiles.get(i);
            if (wwf.getName().equalsIgnoreCase(inFile)) {
                outValue = wwf;
                break;
            }
        }
        
        return outValue;
    }
    
    public void removeWebPage(String inPage) {

        //File webPagesDir = getWebPagesDirectory(this.file);
        //String webPageName = getWebPage(inPage).getName();
        //File webPage = new File(webPagesDir, webPageName);
        //System.out.println("webPage = " + webPage.getAbsolutePath());
        //System.out.println("this.webPages.size() = " + this.webPages.size());
        //MiscHelper.deleteFileOrDirectory(webPage);
        
        for (int i = 0; i < this.webPages.size(); i++) {
            WorkflowWebPage wwp = (WorkflowWebPage)this.webPages.get(i);
            if (wwp.getName().equalsIgnoreCase(inPage)) {
                this.webPages.remove(i);
                break;
            }
        }
    }
    
    public void removeWebFile(String inFile) {
        
        //File webFilesDir = getWebFilesDirectory(this.file);
        //String webFileName = getWebFile(inFile).getName();
        //File webFile = new File(webFilesDir, webFileName);
        //System.out.println("webPage = " + webPage.getAbsolutePath());
        //System.out.println("this.webPages.size() = " + this.webPages.size());
        //MiscHelper.deleteFileOrDirectory(webFile);
        
        for (int i = 0; i < this.webFiles.size(); i++) {
            WorkflowWebFile wwf = (WorkflowWebFile)this.webFiles.get(i);
            if (wwf.getName().equalsIgnoreCase(inFile)) {
                this.webFiles.remove(i);
                break;
            }
        }
    }
    
    public void addWebFile(String inName, byte inContent[]) {
        //try {
            WorkflowWebFile wwf = getWebFile(inName);
            if (wwf != null) {
                removeWebFile(wwf.getName());
            }
            File webFilesDir = getWebFilesDirectory(this.file);
            File webFile = new File(webFilesDir, inName);
            //
            //FileOutputStream fos = new FileOutputStream(webFile);
            //fos.write(inContent);
            //fos.flush();
            //fos.close();
            
            GenericFileWorkflowWebFile gfwwf =
                new GenericFileWorkflowWebFile(webFile);
            gfwwf.setContent(inContent);
            this.webFiles.add(gfwwf);
        //}
        //catch (IOException ioe) {
        //    throw new PFRuntimeException(ioe);
        //}
    }

    public void addWebPage(String inName) {
        //try {
            WorkflowWebPage wwp = getWebPage(inName);
            if (wwp == null) {
                File webPagesDir = getWebPagesDirectory(this.file);
                File webPage = new File(webPagesDir, inName);
                //
                //FileWriter fw = new FileWriter(webPage);
                //fw.write(DEFAULT_WEB_PAGE);
                //fw.flush();
                //fw.close();

                this.webPages.add(
                    new GenericFileWorkflowWebPage(webPage));
            }
        //}
        //catch (IOException ioe) {
        //    throw new PFRuntimeException(ioe);
        //}
    }

    
    public String[] getPropertyNames() {
        
        String outValue[] = null;
        
        Enumeration enum = this.properties.propertyNames();
        
        ArrayList propNames = new ArrayList();
        
        while (enum.hasMoreElements()) {
            propNames.add((String)enum.nextElement());
        }
        
        outValue = new String[propNames.size()];
        for (int i = 0; i < propNames.size(); i++) {
            outValue[i] = (String)propNames.get(i);
        }
        
        return outValue;
    }
    
    public String getPropertyValue(String inName) {

        String outValue = "";
        
        if (this.properties.getProperty(inName.toUpperCase()) != null) {
            outValue = this.properties.getProperty(inName.toUpperCase());
        }
        
        return outValue;
    }
    
    public void setPropertyValue(String inName, String inValue) {
        this.properties.setProperty(inName.toUpperCase(), inValue);
    }
    
    public boolean isPropertyPresent(String inName) {
        return (this.properties.getProperty(inName.toUpperCase()) != null);
    }
    
    public void removeProperty(String inName) {
        //this.properties.setProperty(inName.toUpperCase(), null);
        this.properties.remove(inName.toUpperCase());
    }
}
