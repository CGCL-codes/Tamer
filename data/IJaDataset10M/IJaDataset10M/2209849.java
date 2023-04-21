package org.ramadda.geodata.data;

import org.ramadda.repository.*;
import org.ramadda.repository.auth.*;
import org.ramadda.repository.type.*;
import org.w3c.dom.*;
import ucar.unidata.sql.Clause;
import ucar.unidata.sql.SqlUtil;
import ucar.unidata.sql.SqlUtil;
import ucar.unidata.util.DateUtil;
import ucar.unidata.util.HtmlUtil;
import ucar.unidata.util.HttpServer;
import ucar.unidata.util.IOUtil;
import ucar.unidata.util.LogUtil;
import ucar.unidata.util.Misc;
import ucar.unidata.util.StringUtil;
import ucar.unidata.util.WikiUtil;
import ucar.unidata.xml.XmlUtil;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;

/**
 *
 *
 * @author IDV Development Team
 * @version $Revision: 1.3 $
 */
public class OpendapLinkTypeHandler extends GenericTypeHandler {

    /** _more_ */
    public static final String TYPE_OPENDAPLINK = "opendaplink";

    /**
     * _more_
     *
     * @param repository _more_
     * @param entryNode _more_
     *
     * @throws Exception _more_
     */
    public OpendapLinkTypeHandler(Repository repository, Element entryNode) throws Exception {
        super(repository, entryNode);
    }

    /**
     * _more_
     *
     * @param request _more_
     * @param entry _more_
     *
     * @return _more_
     *
     * @throws Exception _more_
     */
    public String xxxgetEntryResourceUrl(Request request, Entry entry) throws Exception {
        String fileTail = IOUtil.stripExtension(getStorageManager().getFileTail(entry)) + ".html";
        System.err.println("getEntry:" + fileTail);
        return HtmlUtil.url(request.url(getRepository().URL_ENTRY_GET) + "/" + fileTail, ARG_ENTRYID, entry.getId());
    }

    /**
     * _more_
     *
     * @param request _more_
     * @param entry _more_
     *
     * @return _more_
     *
     * @throws Exception _more_
     */
    public String getResourceUrl(Request request, Entry entry) throws Exception {
        Resource resource = entry.getResource();
        return resource.getPath() + ".html";
    }
}
