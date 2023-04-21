package org.systemsbiology.apps.gui.server;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import java.sql.Clob;

/**
* MediumClobFile Object.
* <p> Maximum size is 2^24 - 1 or 16,777,215 bytes, 16MB
* <p> 
* Copyright (C) 2010 by Institute for Systems Biology,
* Seattle, Washington, USA.  All rights reserved.
*
* This source code is distributed under the GNU Lesser
* General Public License, the text of which is available at:
*   http://www.gnu.org/copyleft/lesser.html
*/
@Entity
public class MediumClobFile {

    @Id
    @GeneratedValue
    private Long fileID;

    @Lob
    @Column(length = 16777215)
    private Clob data;

    private String fileName;

    public MediumClobFile() {
    }

    public void setData(Clob data) {
        this.data = data;
    }

    public Clob getData() {
        return data;
    }

    public void setFileID(Long fileID) {
        this.fileID = fileID;
    }

    public Long getFileID() {
        return fileID;
    }

    public String toString() {
        String retString = "fileID: " + this.fileID + " ; fileName" + this.fileName + "\n";
        return retString;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}
