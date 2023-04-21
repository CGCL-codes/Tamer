package com.lb.trac.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.mail.Address;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import com.lb.trac.pojo.Allegati;
import com.lb.trac.pojo.Tickets;

/**
 * 
 * @author edoardo
 */
public class TracSetupEventMessagePreparator implements MimeMessagePreparator {

    private Map<RecipientType, List> recipient = new HashMap<RecipientType, List>();

    private List<Address> toAddresses = new ArrayList<Address>();

    private List<Address> ccAddresses = new ArrayList<Address>();

    private List<Address> bccAddresses = new ArrayList<Address>();

    private String from;

    private String message;

    private Allegati[] allegati;

    private String messageId;

    private MimeMessage mimeMessage;

    private Tickets tickets;

    public Allegati[] getAllegati() {
        return allegati;
    }

    public void setAllegati(Allegati[] allegati) {
        this.allegati = allegati;
    }

    public TracSetupEventMessagePreparator() {
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void prepare(MimeMessage mimeMessage) throws Exception {
        Set keys = recipient.keySet();
        Iterator<RecipientType> it = keys.iterator();
        while (it.hasNext()) {
            RecipientType recipientType = it.next();
            List<Address> addresses = recipient.get(recipientType);
            if (addresses.size() > 0) {
                mimeMessage.addRecipients(recipientType, addresses.toArray(new Address[0]));
            }
        }
        mimeMessage.setFrom(new InternetAddress(getFrom()));
        mimeMessage.setSubject("#" + getTickets().getIdTicket().toPlainString() + " - " + getTickets().getUtentiByIdUtente().getSocieta().getDescBreve().toUpperCase() + " - " + getTickets().getStati().getDescrizione() + " - " + getTickets().getTitoloTicket());
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        helper.setText(getMessage(), true);
        if (getAllegati() != null) {
            for (int i = 0; i < getAllegati().length; i++) {
                Allegati allegato = getAllegati()[i];
                helper.addAttachment(allegato.getNome(), new ByteArrayResource(allegato.getAllegato()), allegato.getContentType());
            }
        }
        setMimeMessage(mimeMessage);
    }

    public void addRecipient(RecipientType type, Address address) {
        if (recipient.size() == 0) {
            initRecipient();
        }
        recipient.get(type).add(address);
    }

    public void addRecipient(RecipientType type, List<Address> addresses) {
        Iterator<Address> it = addresses.iterator();
        for (; it.hasNext(); ) {
            Address address = it.next();
            addRecipient(type, address);
        }
    }

    private void initRecipient() {
        recipient.put(RecipientType.TO, toAddresses);
        recipient.put(RecipientType.CC, ccAddresses);
        recipient.put(RecipientType.BCC, bccAddresses);
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public MimeMessage getMimeMessage() {
        return mimeMessage;
    }

    public void setMimeMessage(MimeMessage mimeMessage) {
        this.mimeMessage = mimeMessage;
    }

    public void setTickets(Tickets tickets) {
        this.tickets = tickets;
    }

    public Tickets getTickets() {
        return tickets;
    }
}
