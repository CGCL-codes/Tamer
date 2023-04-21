package org.kablink.teaming.remoting.ws.service.ical;

import java.io.IOException;
import java.io.StringBufferInputStream;
import java.util.List;
import net.fortuna.ical4j.data.ParserException;
import org.dom4j.Document;
import org.dom4j.Node;
import org.kablink.teaming.remoting.ws.BaseService;
import org.kablink.teaming.remoting.ws.RemotingException;
import org.kablink.teaming.util.stringcheck.StringCheckUtil;

public class IcalServiceImpl extends BaseService implements IcalService {

    public void ical_uploadCalendarEntriesWithXML(String accessToken, long folderId, String iCalDataAsXML) {
        Document doc = getDocument(iCalDataAsXML);
        List<Node> entryNodes = (List<Node>) doc.selectNodes("//entry");
        for (Node entryNode : entryNodes) {
            String iCal = StringCheckUtil.check(entryNode.getText());
            try {
                getIcalModule().parseToEntries(folderId, new StringBufferInputStream(iCal));
            } catch (IOException e) {
                throw new RemotingException(e);
            } catch (ParserException e) {
                throw new RemotingException(e);
            }
        }
    }
}
