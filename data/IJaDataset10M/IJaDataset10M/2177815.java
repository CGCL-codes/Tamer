package ru.caffeineim.protocols.icq.packet.sent.ssi;

import ru.caffeineim.protocols.icq.Flap;
import ru.caffeineim.protocols.icq.Snac;

/**
 * <p>Created by 15.08.07
 *   @author Samolisov Pavel
 */
public class SsiBeginEdit extends Flap {

    /**
	 * Contactlist edit start - begin transaction
	 * 
	 * @param contactId For AIM use Buddy name. For icq use UIN.
	 */
    public SsiBeginEdit() {
        super(2);
        Snac snac = new Snac(0x13, 0x11, 0x00, 0x00, 0x00000011);
        this.addSnac(snac);
    }
}
