package es.seat131.viewerfree.command.mail;

import java.io.IOException;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import es.seat131.viewerfree.dto.Message;
import es.seat131.viewerfree.service.IEmailService;
import es.seat131.viewerfree.util.HttpServletRequestUtil;

public abstract class AbstractMessageCommand extends GmailCommand {

    protected Message getMessage(HttpServletRequest request, IEmailService emailService) throws IOException, MessagingException {
        return emailService.getMessage(HttpServletRequestUtil.getIdMessage(request));
    }
}
