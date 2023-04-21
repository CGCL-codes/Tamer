package com.jujunie.project1901;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import com.jujunie.project1901.VCheck.Output;
import com.jujunie.service.log.Log;
import com.jujunie.service.web.DisplayException;
import com.jujunie.service.web.WriterDisplayHandlerXML;

public class DIListCheckHTML extends DIBase implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 2832780971617837273L;

    private static final Log LOG = Log.getInstance(DIListCheckHTML.class);

    @Override
    protected void init() throws DisplayException {
        LOG.enter("init");
        VCheck current = null;
        List<VCheck> checks = new LinkedList<VCheck>();
        List<String> messages = null;
        ResourceBundle lang = ResourceBundle.getBundle("MessagesBundle", super.getRequest().getLocale());
        Exercice ex = super.getSessionManager().getCurrentExercice();
        for (Member m : super.getSessionManager().getMembers().getList()) {
            MemberManager.reattach(m);
            messages = m.validate(ex, lang);
            if (messages.size() > 0) {
                current = new VCheck(m.getLastName() + ' ' + m.getFirstName());
                current.setMessages(messages);
                checks.add(current);
            }
            try {
                MemberManager.set(m);
            } catch (Project1901UserException e) {
                throw new DisplayException("Error while saving updated validity information", e);
            }
        }
        current = new VCheck(checks);
        current.setOutput(Output.HTML);
        super.getRequest().setAttribute(WriterDisplayHandlerXML.WRITER_KEY, current);
        LOG.exit("init");
    }
}
