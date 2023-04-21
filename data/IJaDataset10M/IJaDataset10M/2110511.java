package VGL;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 * Nikunj Koolar cs681-3 Fall 2002 - Spring 2003 Project VGL File:
 * Brian White 2008
 * CustomizedFileFilter.java - Instances of this class provide for file filters
 * to show only those file that are supported by the application.
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 * 
 * @author Nikunj Koolar, Brian White
 * @version 1.0 $Id: CustomizedFileFilter.java,v 1.5 2003/05/11 16:00:28 nikunjk
 *          Exp $
 */
public class CustomizedFileFilter extends FileFilter {

    /**
	 * Stores the description of the supported type
	 */
    private String m_Description = null;

    /**
	 * Stores the extension (eg. ".txt",".zip") of the supported file type
	 */
    private String m_Extension = null;

    /**
	 * The constructor
	 * 
	 * @param extension
	 *            the extension of the supported file type
	 * @param description
	 *            the description of the supported file type
	 */
    public CustomizedFileFilter(String extension, String description) {
        m_Description = description;
        m_Extension = "." + extension.toLowerCase();
    }

    /**
	 * This method returns the description of the supported file type
	 * 
	 * @return the description
	 */
    public String getDescription() {
        return m_Description;
    }

    /**
	 * Method that checks the passed file parameter whether its supported by the
	 * this file filter or not
	 * 
	 * @param f
	 *            the file to be checked
	 * @return true if file is supported false otherwise
	 */
    public boolean accept(File f) {
        if (f == null) return false;
        if (f.isDirectory()) return true;
        return f.getName().toLowerCase().endsWith(m_Extension);
    }
}
