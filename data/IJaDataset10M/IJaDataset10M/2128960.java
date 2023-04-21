package org.openuss.newsletter;

import java.util.List;
import org.openuss.security.User;
import org.openuss.security.UserInfo;

/**
 * @author Ingo Dueppe
 */
public interface NewsletterService {

    public void subscribe(NewsletterInfo newsletter, UserInfo user);

    public void unsubscribe(NewsletterInfo newsletter, UserInfo user);

    public void unsubscribe(SubscriberInfo subscriber);

    public void setBlockingState(SubscriberInfo subscriber);

    public void saveMail(NewsletterInfo newsletter, MailDetail mail);

    public void deleteMail(NewsletterInfo newsletter, MailDetail mail) throws NewsletterApplicationException;

    public void sendPreview(NewsletterInfo newsletter, MailDetail mail);

    /**
	 * Sets mail into state INQUEUE
	 */
    public void sendMail(NewsletterInfo newsletter, MailDetail mail);

    public void addNewsletter(org.openuss.foundation.DomainObject domainObject, String name);

    public void updateNewsletter(NewsletterInfo newsletter);

    public void updateMail(org.openuss.foundation.DomainObject domainObject, MailDetail mail);

    public String exportSubscribers(NewsletterInfo newsletter);

    public void cancelSending(MailInfo mail);

    public void markAsSend(MailInfo mail);

    public List<SubscriberInfo> getSubscribers(NewsletterInfo newsletter);

    public List<MailInfo> getMails(NewsletterInfo newsletter, boolean withDeleted);

    public MailDetail getMail(MailInfo mail);

    public NewsletterInfo getNewsletter(org.openuss.foundation.DomainObject domainObject);

    public NewsletterInfo getNewsletter(MailDetail mail);

    public void removeAllSubscriptions(User user);
}
