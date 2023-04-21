package ru.caffeineim.protocols.icq.packet.sent.ssi;

import java.util.Iterator;
import ru.caffeineim.protocols.icq.Flap;
import ru.caffeineim.protocols.icq.RawData;
import ru.caffeineim.protocols.icq.Snac;
import ru.caffeineim.protocols.icq.Tlv;
import ru.caffeineim.protocols.icq.contacts.ContactListItem;
import ru.caffeineim.protocols.icq.contacts.Group;
import ru.caffeineim.protocols.icq.exceptions.ConvertStringException;
import ru.caffeineim.protocols.icq.tool.StringTools;

/**
 * <p>Created by 15.08.07
 *   @author Samolisov Pavel
 */
public class SsiUpdateGroupHeader extends Flap {

    /**
     * Creates a new instance of SsiUpdateGroupHeader
     * @throws ConvertStringException
     */
    public SsiUpdateGroupHeader(Group grp) throws ConvertStringException {
        super(2);
        Snac snac = new Snac(0x13, 0x09, 0x00, 0x00, 0x00000009);
        byte[] groupId = StringTools.stringToByteArray(grp.getId());
        snac.addRawDataToSnac(new RawData(groupId.length, RawData.WORD_LENGHT));
        snac.addRawDataToSnac(new RawData(groupId));
        snac.addRawDataToSnac(new RawData((short) grp.getGroupId(), RawData.WORD_LENGHT));
        snac.addRawDataToSnac(new RawData(0x0000, RawData.WORD_LENGHT));
        snac.addRawDataToSnac(new RawData(0x0001, RawData.WORD_LENGHT));
        if (grp.getContainedItems() != null && grp.getContainedItems().size() > 0) {
            snac.addRawDataToSnac(new RawData(4 + grp.getContainedItems().size() * RawData.WORD_LENGHT, RawData.WORD_LENGHT));
            Tlv tlv = new Tlv(0x00C8);
            for (Iterator iter = grp.getContainedItems().iterator(); iter.hasNext(); ) {
                tlv.appendRawDataToTlv(new RawData(((ContactListItem) iter.next()).getItemId(), RawData.WORD_LENGHT));
            }
            snac.addTlvToSnac(tlv);
        } else {
            snac.addRawDataToSnac(new RawData(0x0000, RawData.WORD_LENGHT));
        }
        addSnac(snac);
    }
}
