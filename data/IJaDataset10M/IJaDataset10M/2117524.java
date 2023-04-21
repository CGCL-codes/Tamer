package com.skruk.elvis.admin.manage.resources;

import com.skruk.elvis.admin.connect.ConnectionManager;
import com.skruk.elvis.admin.gui.*;
import com.skruk.elvis.admin.plugin.ResourcesPlugin;
import com.skruk.elvis.admin.structure.*;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

/**
 * @author     skruk
 *
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 * @created    20 lipiec 2004
 */
public class StructureConnector {

    /** */
    private ResourceEntry rootEntry = null;

    /** */
    private java.util.List listEntries = new java.util.ArrayList();

    /**  Description of the Field */
    private long totalSize = 0;

    /**  Description of the Field */
    private String xmlStructure = null;

    /**  Description of the Field */
    private String uploadId = null;

    /**
	 * @param  _entry
	 */
    public StructureConnector(ResourceEntry _entry) {
        this.rootEntry = _entry;
    }

    /**
	 * @return
	 */
    public long prepareToSendBinaries() {
        try {
            ConnectionManager manager = ConnectionManager.getInstance();
            if (!manager.isConnected()) {
                manager.connect();
            }
            String sessionid = manager.getSessionId();
            this.uploadId = manager.getRemoteAdmin().beginResourceUpload(sessionid);
            System.out.println("[DEBUG] uploadId = " + this.uploadId);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        this.totalSize = this.scanForBinaries(listEntries, this.rootEntry);
        return this.totalSize;
    }

    /**
	 * @param  entry
	 * @param  _listEntries  Description of the Parameter
	 * @return
	 */
    protected long scanForBinaries(java.util.List _listEntries, ResourceEntry entry) {
        long result = 0;
        if (entry.isStructureElement()) {
            ResourceEntry[] entries = entry.getEntries();
            for (int i = 0; entries != null && i < entries.length; i++) {
                result += scanForBinaries(_listEntries, entries[i]);
            }
        }
        if (entry.getSource().getElvisType() == StructureEntry.SE_ELVIS_TYPE_FILE && entry.getValue() instanceof java.io.File) {
            java.io.File file = (java.io.File) entry.getValue();
            if (file != null) {
                _listEntries.add(entry);
                result += file.length();
            }
        }
        return result;
    }

    /**
	 * Description of the Method
	 *
	 *
	 * @param  main     Description of the Parameter
	 * @param  file     Description of the Parameter
	 * @param  display  Description of the Parameter
	 */
    public void sendFiles(Progressable main, Progressable file, Displayable display) {
        java.util.Iterator it = this.listEntries.iterator();
        main.setMax((int) this.totalSize);
        main.beginProgress();
        while (it.hasNext()) {
            ResourceEntry entry = (ResourceEntry) it.next();
            java.io.File binary = (java.io.File) (entry).getValue();
            display.setText(ResourcesPlugin.getInstance().getFormater().getText("send_file_label", new Object[] { binary.getName() }));
            file.setMax((int) binary.length());
            file.beginProgress();
            String name = null;
            try {
                name = ConnectionManager.getInstance().sendFile(binary, main, file, this.uploadId);
            } catch (Exception ex) {
                System.err.println("[DEBUG] Error occured during upload: " + ex);
                ex.printStackTrace();
            }
            if (name != null) {
                entry.setRemoteName(name);
            }
        }
        main.endProgress();
        file.endProgress();
        display.setText("");
    }

    /**
	 *  Description of the Method
	 *
	 * @param  progress  Description of the Parameter
	 */
    public void sendStructure(Progressable progress) {
        try {
            ConnectionManager.getInstance().sendStructure(this.xmlStructure, progress, this.uploadId);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        progress.endProgress();
    }

    /**
	 *  Description of the Method
	 *
	 * @param  rootNode  Description of the Parameter
	 * @return           Description of the Return Value
	 */
    public String createXMLStructure(ResourceEntryTreeNode rootNode) {
        synchronized (StructureConnector.class) {
            if (this.xmlStructure == null) {
                Element rootElement = rootNode.xmlTree();
                XMLOutputter outputter = new XMLOutputter("\t", true);
                this.xmlStructure = outputter.outputString(rootElement);
            }
        }
        return this.xmlStructure;
    }

    /**
	 *  Description of the Method
	 *
	 * @return    Description of the Return Value
	 */
    public String finalizeTransfer() {
        String result = null;
        try {
            result = ConnectionManager.getInstance().getRemoteAdmin().finishResourceUpload(ConnectionManager.getInstance().getSessionId(), this.uploadId);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }
}
