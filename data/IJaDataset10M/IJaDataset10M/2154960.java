package org.xaware.server.engine.channel.file;

/**
 * $Id$
 * XAware is a Data Integration and Data Abstraction Layer software program.
 * Copyright © 2002, 2003, 2004, 2005, 2006, 2007 XAware, Inc.
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of version 2 of the GNU General Public License as
 * published by the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * for more details.
 * 
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * You can contact XAware, Inc. at 5555 Tech Center Drive, Suite 200,
 * Colorado Springs, CO 80919 or info@xaware.com. 
 */
public class FileDriverData {

    /** Name of File to read or write */
    String fileName = null;

    /** read, write or append */
    String mode;

    /**
     * @return the mode
     */
    public String getMode() {
        return mode;
    }

    /**
     * @param mode
     *            the mode to set
     */
    public void setMode(String mode) {
        this.mode = mode;
    }

    /**
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @param fileName
     *            the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
