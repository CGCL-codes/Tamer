package de.powerstaff.business.dao;

import java.util.Collection;
import de.powerstaff.business.entity.NewsletterMail;
import de.powerstaff.business.entity.WebProject;

public interface WebsiteDAO {

    Collection<WebProject> getCurrentProjects();

    Collection<NewsletterMail> getConfirmedMails();

    void delete(WebProject aProject);

    WebProject getById(Long aId);

    void saveOrUpdate(WebProject aProject);

    void saveOrUpdate(NewsletterMail aMail);

    void save(WebProject aWebProject);
}
