package net.sf.mailoch.services;

import java.util.Set;
import javax.persistence.*;
import net.sf.mailoch.model.EmailMessage;
import net.sf.mailoch.model.MailochEntity;

public class ReceptionBox extends MailochEntity {

    @OneToMany
    Set<EmailMessage> emails;
}
