package interfaces;

/**
 * IXMLManager.java
 * 
 * interface the xml operations, for adding support for different file
 * versions in later software versions
 * 
 * @author Andy Dunkel andy.dunkel"at"ekiwi.de
 * @author published under the terms and conditions of the
 *      GNU General Public License,
 *      for details see file gpl.txt in the distribution
 *      package of this software
 *
 */
public interface IXMLManager {

    public void saveDocument() throws Exception;

    public void loadDocument(String filename) throws Exception;
}
