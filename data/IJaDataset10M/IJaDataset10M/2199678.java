package com.croftsoft.core.util.mail;

import com.croftsoft.core.util.seq.Seq;
import com.croftsoft.core.util.slot.Slot;

/**********************************************************************
    * An interface for sending and receiving messages.
    *   
    * For documentation on interface Mail and class FlipMail, please see
    * the tutorial "Interface Mail" at
    * <a target="_blank"
    * href="http://www.CroftSoft.com/library/tutorials/mail/">
    * http://www.CroftSoft.com/library/tutorials/mail/</a>
    *
    * @version
    *   $Id: Mail.java 98 2011-10-06 19:21:54Z croft $
    * @since
    *   2008-01-27
    * @author
    *   <a href="http://www.CroftSoft.com/">David Wallace Croft</a>
    **********************************************************************/
public interface Mail<Message> extends Seq<Message>, Slot<Message> {
}
