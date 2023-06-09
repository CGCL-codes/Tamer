package se.kth.cid.component.lookup;

import se.kth.cid.util.*;
import se.kth.cid.xml.*;
import se.kth.cid.component.*;
import java.net.*;
import java.util.*;

/** This is a helper class for TableURLLookup.
 *
 *  It is used to load a TableURLLookup's table from an XML file.
 *  The XML file must  like:
 *  <Patches>
 *    <Patch NAME="cid.kth.se" URL="http://www.nada.kth.se/cid/"/>
 *    ...
 *  </Patches>
 *
 *  Please note that the XML doc must refer to the DTD
 *  via known URL or be standalone with included DTD.
 *  An example header would be:
 *  <?xml version="1.0" encoding="ISO-8859-1" standalone="yes"?>
 *  <!DOCTYPE Patches 
 *  [<!ELEMENT Patches (Patch*)>
 *   <!ELEMENT Patch    EMPTY>
 *   <!ATTLIST Patch
 *      NAME            CDATA #REQUIRED
 *      URL             CDATA #REQUIRED>]>
 *
 *  Doing without a DTD is theoretically possible, but will not work because of
 *  assumptions regarding CDATA and child elements in our simple DOM builder.
 *
 *  @author Mikael Nilsson
 *  @version $Revision: 155 $
 */
public class TableURLLoader {

    /** The XML loader to use.
   */
    XmlLoader loader;

    /** Constructs a TableURLLoader.
   */
    public TableURLLoader() {
        loader = new XmlLoader(null);
    }

    /** Constructs a new TableURLLookup with the given table loaded.
   *
   *  @param tableURL the URL of the table to load.
   *  @return a new TableURLLookup.
   *  @exception ComponentException if anything went wrong.
   */
    public TableURLLookup loadLookup(URL tableURL) throws ComponentException {
        TableURLLookup lookup = new TableURLLookup();
        loadLookup(lookup, tableURL);
        return lookup;
    }

    /** Loads the given table into an already existing TableURLLookup.
   *
   * @param lookup the TableURLLookup to extend
   * @param tableURL the URL of the tael to load.
   * @exception ComponentException if anything went wrong.
   */
    public void loadLookup(TableURLLookup lookup, URL tableURL) throws ComponentException {
        XmlDocument doc;
        try {
            doc = loader.parse(tableURL);
        } catch (XmlLoaderException e) {
            throw new ComponentException("Could not load lookup table " + tableURL + ":\n" + e.getMessage());
        }
        XmlElement[] patchEls = doc.getRoot().getSubElements("Patch");
        for (int i = 0; i < patchEls.length; i++) {
            String patch = patchEls[i].getAttribute("NAME");
            String urlStr = patchEls[i].getAttribute("URL");
            if (patch == null) throw new ComponentException("Missing PATCH attribute in table file!");
            if (patch == null) throw new ComponentException("Missing URL attribute in table file!");
            try {
                lookup.putBasePath(patch, new URL(urlStr));
            } catch (MalformedURLException e) {
                throw new ComponentException("The patch " + patch + " did not map to a valid URL in the lookup table");
            }
        }
    }
}
