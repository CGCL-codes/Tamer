package hackerz.xmlParsing;

import java.util.List;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

/**
 * @author Steffen Gates Feb 9, 2011
 */
public class ServerList {

    @ElementList(inline = true)
    public List<Server> server;
}
