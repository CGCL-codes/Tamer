package com.servengine.formprocessor;

import com.servengine.portal.Portal;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;
import javax.ejb.Local;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;

@Stateful(name = "FormProcessorManager")
@Local
public class FormProcessorManagerBean implements FormProcessorManagerLocal {

    private static Logger logger = Logger.getLogger(FormProcessorManagerBean.class.getName());

    @PersistenceContext(unitName = "mainPersistenceUnit", type = PersistenceContextType.EXTENDED)
    private EntityManager entityManager;

    public void persist(FormField bean) {
        if (bean.getId() == null) entityManager.persist(bean); else entityManager.merge(bean);
    }

    public void persist(Form bean) {
        if (bean.getId() == null) entityManager.persist(bean); else entityManager.merge(bean);
    }

    public void removeFormField(FormField field) {
        entityManager.remove(entityManager.find(FormField.class, field.getId()));
    }

    public void removeForm(Form form) {
        for (Form sibling : form.getSiblings()) {
            sibling.setSibling(null);
            entityManager.persist(sibling);
        }
        entityManager.remove(entityManager.find(Form.class, form.getId()));
    }

    public void removeFormEntry(FormEntry entry) {
        entityManager.remove(entityManager.find(FormEntry.class, entry.getId()));
    }

    @SuppressWarnings("unchecked")
    public List<Form> getForms(Portal portal) {
        List<Form> forms = new ArrayList<Form>();
        Query query = entityManager.createNamedQuery("Form.findByPortal");
        query.setParameter("portal", portal);
        for (Form form : (List<Form>) query.getResultList()) {
            form.setEntryCount((Long) entityManager.createNamedQuery("Form.getEntryCount").setParameter("id", form.getId()).getSingleResult());
            forms.add(form);
        }
        return forms;
    }

    public long getFormEntryCount(Integer formId) {
        return (Long) (entityManager.createNamedQuery("Form.getEntryCount").setParameter("id", formId).getSingleResult());
    }

    public Form getForm(Portal portal, Integer id) {
        if (id == null) return getForm(portal);
        Form form = entityManager.find(Form.class, id);
        if (!form.getPortal().equals(portal)) throw new IllegalArgumentException();
        form.setEntryCount((Long) entityManager.createNamedQuery("Form.getEntryCount").setParameter("id", form.getId()).getSingleResult());
        return form;
    }

    public FormEntry getFormEntry(Portal portal, Integer id) {
        FormEntry entry = entityManager.find(FormEntry.class, id);
        if (!entry.getForm().equals(portal)) throw new IllegalArgumentException();
        return entry;
    }

    public FormField getFormField(Portal portal, Integer id) {
        FormField field = entityManager.find(FormField.class, id);
        if (!field.getForm().equals(portal)) throw new IllegalArgumentException();
        return field;
    }

    public Form getFormByName(Portal portal, String name, String language) {
        Query query = entityManager.createNamedQuery("Form.findByPortalAndName");
        query.setParameter("portal", portal);
        query.setParameter("name", name);
        Form form = (Form) query.getSingleResult();
        if (language != null && !language.equals(form.getLanguage())) {
            Set<Form> allSiblings = new HashSet<Form>();
            if (form.getSiblings() != null) allSiblings.addAll(form.getSiblings());
            if (form.getSibling() != null) allSiblings.add(form.getSibling());
            for (Form siblingForm : allSiblings) if (language.equals(siblingForm.getLanguage())) return siblingForm;
        }
        return form;
    }

    public Form getForm(Portal portal) {
        Query query = entityManager.createNamedQuery("Form.findByPortal");
        query.setParameter("portal", portal);
        Form form = (Form) query.getSingleResult();
        form.setEntryCount((Long) entityManager.createNamedQuery("Form.getEntryCount").setParameter("id", form.getId()).getSingleResult());
        return form;
    }

    public void persist(FormEntry entry) {
        if (entry.getId() == null) entityManager.persist(entry); else entityManager.merge(entry);
    }

    @SuppressWarnings("unchecked")
    public List<FormEntry> getFormEntries(Portal portal, Form form, Date startdate, Date enddate) {
        Query query = entityManager.createNamedQuery("FormEntry.findByFormBetweenDates");
        query.setParameter("form", form);
        query.setParameter("startdate", startdate);
        query.setParameter("enddate", enddate);
        return query.getResultList();
    }

    public String getEntriesAsCSV(Portal portal, Form form, Date startdate, Date enddate) throws IOException {
        char separator = ',';
        String report = "";
        TreeSet<FormField> formFields = new TreeSet<FormField>(form.getFields());
        for (FormField field : formFields) report += (report.length() == 0 ? "" : "" + separator) + field.getName();
        for (FormEntry ent : getFormEntries(portal, form, startdate, enddate)) {
            for (FormFieldEntry fi : new TreeSet<FormFieldEntry>(ent.getFieldEntries())) {
                String line = "";
                if (fi.getValue() != null) {
                    String value = fi.getValue();
                    value = value.replace(separator, ' ');
                    value = value.replace('\t', ' ');
                    value = value.replace('\n', ' ');
                    value = value.replace('\r', ' ');
                    value = value.replace('\f', ' ');
                    line += (line.length() == 0 ? "" : "" + separator) + value;
                } else line += (line.length() == 0 ? " " : "" + separator);
            }
            report += "\n";
        }
        return report;
    }

    @SuppressWarnings("unchecked")
    public List<FormEntry> getFormEntries(Portal portal, Form form, int howMany) {
        Query query = entityManager.createNamedQuery("FormEntry.findByForm");
        query.setParameter("form", form);
        query.setMaxResults((int) howMany);
        return query.getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<Form> getSiteContactForms(Portal portal) {
        return entityManager.createNamedQuery("Form.getSiteContactForms").setParameter("portal", portal).getResultList();
    }

    @SuppressWarnings("unchecked")
    public Form getSiteContactForm(Portal portal, String language) {
        List<Form> forms = (List<Form>) entityManager.createNamedQuery("Form.getSiteContactForms").setParameter("portal", portal).getResultList();
        for (Form form : forms) if (language == null || language.equals(form.getLanguage())) return form;
        logger.warning("No contact form for portal " + portal.getId() + " and language " + language);
        return forms.size() > 0 ? forms.get(0) : null;
    }

    @SuppressWarnings("unchecked")
    public List<FormEntry> getEntriesByFormBetweenDatesAndSearchString(Form form, Date startDate, Date endDate, String searchString) {
        Query query = entityManager.createNamedQuery("FormEntry.findByFormBetweenDates");
        query.setParameter("form", form);
        query.setParameter("startdate", startDate);
        query.setParameter("enddate", endDate);
        query.setParameter("searchString", "%" + searchString.toLowerCase() + "%");
        return query.getResultList();
    }
}
