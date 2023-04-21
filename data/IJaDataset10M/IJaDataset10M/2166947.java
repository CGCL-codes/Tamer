package edu.columbia.hypercontent.editors.vcard.commands;

import edu.columbia.hypercontent.editors.BaseSessionData;
import edu.columbia.hypercontent.editors.ICommand;
import edu.columbia.hypercontent.editors.vcard.SessionData;

/**
 * Created by IntelliJ IDEA.
 * User: alex
 * Date: Sep 5, 2003
 * Time: 5:25:41 PM
 * To change this template use Options | File Templates.
 */
public class AddEmail implements ICommand {

    public void execute(BaseSessionData baseSession) throws Exception {
        SessionData session = (SessionData) baseSession;
        session.card.newObject(session.card.EMAIL);
    }
}
