package com.kenstevens.stratdom.site.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.kenstevens.stratdom.model.SentMessage;
import com.kenstevens.stratdom.site.Command;
import com.kenstevens.stratdom.site.SiteForm;
import com.kenstevens.stratdom.site.SiteResponse;
import com.kenstevens.stratdom.site.StratSite;

@Scope("prototype")
@Component
public class SendMessageCommand extends Command {

    private SentMessage message;

    @Autowired
    StratSite stratSite;

    public SendMessageCommand(SentMessage message) {
        this.message = message;
    }

    @Override
    public SiteResponse execute() throws Exception {
        SiteForm sendmailform = stratSite.getSendmailForm();
        sendmailform.setParameter(FORM_MESSAGE_TARGET, message.getPlayer());
        sendmailform.setParameter(FORM_MESSAGE, message.getBody());
        sendmailform.setCheckbox(FORM_REQUEST_ALLIANCE, message.isAllianceRequested());
        sendmailform.setCheckbox(FORM_SEND_MAP, message.isWorldMapReceived());
        stratSite.submit(sendmailform);
        return stratSite.getCurrentPage();
    }

    @Override
    public String getDescription() {
        return "Send Message to " + message.getPlayer();
    }
}
