package org.dyndns.doujindb.plug;

import javax.swing.Icon;
import javax.swing.JComponent;

/**  
* Plugin.java - Every plugin must implement this - No Exceptions
* @author  nozomu
* @version 1.0
*/
public interface Plugin {

    public Icon getIcon();

    public String getName();

    public String getDescription();

    public String getVersion();

    public String getAuthor();

    public String getWeblink();

    public JComponent getUI();
}
