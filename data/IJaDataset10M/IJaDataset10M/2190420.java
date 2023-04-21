package org.isurf.gdssu.globalregistry.bl;

import java.util.List;
import javax.ejb.Local;
import org.isurf.gdssu.globalregistry.db.entities.Item;
import org.isurf.gdssu.globalregistry.db.entities.Party;
import org.isurf.gs1.bmslib_2_0_2.MessageType;

/**
 *
 * @author nodari
 */
@Local
public interface MessageHandler2_0_2Local {

    public MessageType getMessageFromXML(String xml_globalSearch, String pgln);

    public String globalSearchData(String xml_globalSearch, String partygln);

    public String RequestResponseItem(List<Item> itemList, String partycall);

    public String RequestResponse(List<Party> foundparty, String partycall);
}
