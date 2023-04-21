package com.servengine.pim;

import com.servengine.portal.Portal;
import com.servengine.user.Role;
import com.servengine.user.User;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;

@Stateful(name = "PIMManager")
public class PIMManagerBean implements PIMManagerLocal {

    protected static String SHOW_ONLY_CURRENT_LANGUAGE_EVENTS_PROPERTY_NAME = "pim_showOnlyCurrentLanguageEvents";

    @PersistenceContext(unitName = "mainPersistenceUnit", type = PersistenceContextType.EXTENDED)
    private EntityManager entityManager;

    public void persist(PIMTodo todo) {
        if (todo.getId() == null) entityManager.persist(todo); else entityManager.merge(todo);
    }

    public void markTodoAsDone(PIMTodo todo, User user, String comment) {
    }

    public void persist(PIMCategory category) {
        if (category.getId() == null) entityManager.persist(category); else entityManager.merge(category);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void removeCategory(PIMCategory expurganda, PIMCategory newCategory) {
        PIMCategory category = entityManager.find(PIMCategory.class, expurganda.getId());
        if (newCategory != null) for (PIMContact contact : category.getContacts()) contact.getCategories().add(newCategory);
        Collection eventsExpurganda = new ArrayList();
        for (PIMEvent event : category.getEvents()) if (newCategory == null) eventsExpurganda.add(event); else event.setCategory(newCategory);
        for (PIMMemo memo : category.getMemos()) if (newCategory == null) eventsExpurganda.add(memo); else memo.setCategory(newCategory);
        for (PIMTodo todo : category.getTodos()) if (newCategory == null) eventsExpurganda.add(todo); else todo.setCategory(newCategory);
        for (Object expurgandum : eventsExpurganda) entityManager.remove(expurgandum);
        entityManager.remove(category);
        entityManager.flush();
    }

    public void persist(PIMEvent event) {
        if (event.getId() == null) entityManager.persist(event); else entityManager.merge(event);
    }

    public void remove(PIMEvent event) {
        entityManager.remove(entityManager.find(PIMEvent.class, event.getId()));
        entityManager.flush();
    }

    public void persist(PIMContact contact) {
        return;
    }

    public void removeContact(PIMContact contact) {
    }

    public void persist(PIMContactField contactfield) {
        return;
    }

    public void removeContactField(PIMContactField field) {
    }

    @SuppressWarnings("unchecked")
    public List<PIMCategory> getCategories(Portal portal) {
        Query query = entityManager.createNamedQuery("PIMCategory.findGlobalByPortal");
        query.setParameter("portal", portal);
        return query.getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<PIMCategory> getCategories(User user) {
        Query query = entityManager.createNamedQuery("PIMCategory.findByUser");
        query.setParameter("user", user);
        return query.getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<PIMEvent> getEvents(Portal portal, String language, PIMCategory category, Collection<Role> roles, User user, Date from, Date to, String searchString, int maxResults) {
        String conditions = "";
        Map<String, Object> parameters = new HashMap<String, Object>();
        if (language != null) {
            conditions += " and o.category.language = :language";
            parameters.put("language", language);
        }
        if (category != null) {
            conditions += " and o.category = :category";
            parameters.put("category", category);
        }
        if (user != null) {
            conditions += " and o.user = :user";
            parameters.put("user", user);
        }
        if (from != null) {
            conditions += " and o.endDate >= :from";
            parameters.put("from", from);
        }
        if (to != null) {
            conditions += " and o.startDate <= :to";
            parameters.put("to", to);
        }
        if (roles != null && roles.size() > 0) {
            conditions += " and o.role in (:roles)";
            parameters.put("roleIds", roles);
        }
        if (searchString != null && searchString.length() > 0) {
            conditions += " and (lower(o.name) like :searchString or lower(o.description) like :searchString)";
            parameters.put("searchString", "%" + searchString.toLowerCase() + "%");
        }
        parameters.put("portal", portal);
        Query query = entityManager.createQuery("select o from PIMEvent o where o.category.portal = :portal " + conditions + " order by o.startDate");
        for (String key : parameters.keySet()) query.setParameter(key, parameters.get(key));
        if (maxResults > 0) query.setMaxResults(maxResults);
        return query.getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<PIMEvent> getDayEvents(Portal portal, Date day) {
        Calendar from = GregorianCalendar.getInstance();
        from.setTime(day);
        from.set(Calendar.HOUR_OF_DAY, 0);
        from.set(Calendar.MINUTE, 0);
        from.set(Calendar.SECOND, 0);
        from.set(Calendar.MILLISECOND, 0);
        Calendar to = GregorianCalendar.getInstance();
        to.setTime(day);
        to.set(Calendar.HOUR_OF_DAY, 23);
        to.set(Calendar.MINUTE, 59);
        to.set(Calendar.SECOND, 59);
        to.set(Calendar.MILLISECOND, 999);
        Query query = entityManager.createNamedQuery("PIMEvent.findBetweenDates");
        query.setParameter("from", from.getTime());
        query.setParameter("to", to.getTime());
        query.setParameter("portal", portal);
        return query.getResultList();
    }

    public PIMEvent getEvent(Portal portal, Integer eventId) {
        PIMEvent event = entityManager.find(PIMEvent.class, eventId);
        if (!event.getCategory().getPortal().equals(portal)) throw new IllegalStateException("That event doesn't belong to your portal");
        return event;
    }

    public PIMEvent getEventByName(Portal portal, String name) {
        return (PIMEvent) entityManager.createNamedQuery("PIMEvent.findByName").setParameter("portal", portal).setParameter("name", name).getSingleResult();
    }

    public PIMCategory getCategory(Portal portal, Integer id) {
        PIMCategory category = entityManager.find(PIMCategory.class, id);
        if (!category.getPortal().equals(portal)) throw new IllegalArgumentException("That category doesn't belong to you");
        return category;
    }

    public int getEventCount(Portal portal) {
        Query query = entityManager.createNamedQuery("PIMEvent.findByPortalid");
        query.setParameter("portal", portal);
        return query.getResultList().size();
    }

    public int getGlobalEventCount(Portal portal) {
        Query query = entityManager.createNamedQuery("PIMEvent.findGlobal");
        query.setParameter("portal", portal);
        return query.getResultList().size();
    }

    @SuppressWarnings("unchecked")
    public Collection<PIMPlace> getMainPlaces(Portal portal, int howMany) {
        TreeSet<PIMPlace> places = new TreeSet<PIMPlace>(new PlacesEventCountComparator());
        Set<PIMPlace> expurganda = new HashSet<PIMPlace>();
        Query query = entityManager.createNamedQuery("PIMPlace.findByPortalid");
        query.setParameter("portal", portal);
        query.setMaxResults(howMany);
        places.addAll(query.getResultList());
        java.util.Date now = new java.util.Date();
        for (PIMPlace place : places) {
            query = entityManager.createNamedQuery("PIMEvent.findUpcoming");
            query.setParameter("portal", portal);
            query.setParameter("now", now);
            query.setParameter("placeId", place.getId());
            query.setMaxResults(howMany);
            place.setUpcomingEventCount(query.getResultList().size());
            if (place.getAka() != null) {
                expurganda.add(place);
                places.add(place.getAka());
            }
        }
        for (PIMPlace expurgandum : expurganda) places.remove(expurgandum);
        return places;
    }

    public Collection<PIMPlace> findPlaces(Portal portal, String name) {
        TreeSet<PIMPlace> places = new TreeSet<PIMPlace>(new PlacesEventCountComparator());
        Query query = entityManager.createNamedQuery("PIMPlace.findByString");
        query.setParameter("portal", portal);
        query.setParameter("name", "%" + name.toLowerCase() + "%");
        for (PIMPlace place : places) places.add(place.getAka() == null ? place : place.getAka());
        return places;
    }

    public PIMPlace getPlace(Portal portal, Integer userId, Integer id) {
        PIMPlace place = entityManager.find(PIMPlace.class, id);
        if (!place.getPortal().equals(portal) || (userId != null && !place.getUser().getId().equals(userId))) throw new IllegalArgumentException();
        return place.getAka() == null ? place : place.getAka();
    }

    public PIMPlace getPlace(Portal portal, Integer id) {
        PIMPlace place = entityManager.find(PIMPlace.class, id);
        if (!place.getPortal().equals(portal)) throw new IllegalArgumentException();
        return place.getAka() == null ? place : place.getAka();
    }

    public void persist(PIMPlace place) {
        if (place.getId() == null) entityManager.persist(place); else entityManager.merge(place);
    }

    public boolean placeBelongsTo(Portal portal, String placeName, String placeType, Integer belongsToPlaceId) {
        try {
            Query query = entityManager.createNamedQuery("PIMPlace.findByNameAndType");
            query.setParameter("portal", portal);
            query.setParameter("name", "%" + placeName.toLowerCase() + "%");
            query.setParameter("type", placeType);
            PIMPlace place = (PIMPlace) query.getSingleResult();
            PIMPlace belongsToPlace = entityManager.find(PIMPlace.class, belongsToPlaceId);
            Collection<PIMPlace> places = belongsToPlace.getSubPlaces();
            places.add(belongsToPlace);
            return places.contains(place);
        } catch (NoResultException e) {
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    public Collection<PIMPlace> getPlaces(Portal portal) {
        return entityManager.createNamedQuery("PIMPlace.findByPortal").setParameter("portal", portal).getResultList();
    }

    public void persist(PIMAction action) {
        if (action.getId() == null) entityManager.persist(action); else entityManager.merge(action);
    }

    public PIMAction getAction(Portal portal, Integer id) {
        PIMAction action = entityManager.find(PIMAction.class, id);
        if (!action.getEvent().getCategory().getPortal().equals(portal)) throw new IllegalArgumentException();
        return action;
    }

    public void persist(PIMActionParticipant participant) {
        if (participant.getId() == null) entityManager.persist(participant); else entityManager.merge(participant);
    }

    public PIMActionParticipant getActionParticipant(Portal portal, Integer id) {
        PIMActionParticipant participant = entityManager.find(PIMActionParticipant.class, id);
        if (!participant.getAction().getEvent().getCategory().getPortal().equals(portal)) throw new IllegalArgumentException();
        return participant;
    }

    public Collection<User> getSupervisorCandidates(PIMCategory category) {
        Set<User> users = new HashSet<User>();
        for (PIMEvent event : entityManager.find(PIMCategory.class, category.getId()).getEvents()) if (event.getRoles() != null) for (Role role : event.getRoles()) users.addAll(role.getUsers());
        return users;
    }

    public void removePlace(PIMPlace place) {
        entityManager.remove(entityManager.find(PIMPlace.class, place.getId()));
    }

    @SuppressWarnings("unchecked")
    public List<PIMEvent> getUserEvents(User user) {
        return entityManager.createNamedQuery("PIMEvent.findByUser").setParameter("user", user).getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<PIMEvent> getEvents(PIMCategory category, int firstResult, int maxResults) {
        return (List<PIMEvent>) entityManager.createNamedQuery("PIMEvent.findByCategory").setParameter("category", category).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<PIMEvent> getEvents(Portal portal) {
        return (List<PIMEvent>) entityManager.createNamedQuery("PIMEvent.findByPortal").setParameter("portal", portal).getResultList();
    }

    public PIMEvent getEvent(Portal portal, Integer id, Set<Role> roles) {
        PIMEvent event = entityManager.find(PIMEvent.class, id);
        if (!event.getCategory().getPortal().equals(portal)) throw new IllegalArgumentException("That category doesn't belong to you");
        for (Role role : event.getCategory().getRoles()) if (!roles.contains(role)) throw new IllegalArgumentException();
        return event;
    }

    class PlacesEventCountComparator implements Comparator<PIMPlace> {

        public int compare(PIMPlace p1, PIMPlace p2) {
            int comparation = -p1.getEvents().size() - p2.getEvents().size();
            return comparation == 0 ? p1.getName().compareToIgnoreCase(p2.getName()) : comparation;
        }
    }

    public TreeSet<PIMEvent> getUpcomingEventsByPlaceNameAndType(Portal portal, String name, String type, int howMany) {
        Query query = entityManager.createNamedQuery("PIMPlace.findByNameAndType");
        query.setParameter("portal", portal);
        query.setParameter("name", "%" + name.toLowerCase() + "%");
        query.setParameter("type", type);
        PIMPlace place = (PIMPlace) query.getSingleResult();
        return getUpcomingEventsByPlace(place, howMany);
    }

    @SuppressWarnings("unchecked")
    public List<PIMEvent> getUpcomingEventsByPortal(Portal portal, String language) {
        Query query = entityManager.createNamedQuery("PIMEvent.findUpcomingByPortal" + (language == null ? "" : "AndLanguage"));
        query.setParameter("portal", portal);
        if (language != null) query.setParameter("language", language);
        query.setParameter("now", new java.util.Date());
        return query.getResultList();
    }

    @SuppressWarnings("unchecked")
    public TreeSet<PIMEvent> getUpcomingEventsByPlace(PIMPlace place, int howMany) {
        Collection<PIMPlace> mainPlaces = new ArrayList<PIMPlace>();
        {
            mainPlaces.add(place);
            if (place.getAka() != null) mainPlaces.add(place.getAka()); else mainPlaces.addAll(place.getAkas());
        }
        TreeSet<PIMEvent> events = new TreeSet<PIMEvent>();
        for (PIMPlace mainPlace : mainPlaces) {
            Query query = entityManager.createNamedQuery("PIMEvent.findUpcomingByPortalAndPlace");
            query.setParameter("portal", place.getPortal());
            query.setParameter("now", new java.util.Date());
            query.setParameter("placeId", mainPlace);
            query.setMaxResults(100);
            for (PIMEvent event : (List<PIMEvent>) query.getResultList()) events.add(event);
            for (PIMPlace tmpPlace : mainPlace.getSubPlaces()) {
                query = entityManager.createNamedQuery("PIMEvent.findUpcomingByPlace");
                query.setParameter("now", new java.util.Date());
                query.setParameter("place", tmpPlace);
                query.setMaxResults(100);
                for (PIMEvent event : (List<PIMEvent>) query.getResultList()) events.add(event);
            }
        }
        TreeSet<PIMEvent> upcomingEvents = new TreeSet<PIMEvent>();
        for (PIMEvent event : events) if (upcomingEvents.size() < howMany) upcomingEvents.add(event); else return upcomingEvents;
        return upcomingEvents;
    }

    public void removeActions(PIMEvent event, Integer[] actionIds) {
        for (Integer id : actionIds) {
            PIMAction expurganda = entityManager.find(PIMAction.class, id);
            if (!expurganda.getEvent().equals(event)) throw new IllegalArgumentException();
            entityManager.remove(expurganda);
        }
    }

    public void removeActionParticipants(PIMAction action, Integer[] ids) {
        for (Integer id : ids) {
            PIMActionParticipant expurganda = entityManager.find(PIMActionParticipant.class, id);
            if (!expurganda.getAction().equals(action)) throw new IllegalArgumentException();
            entityManager.remove(expurganda);
        }
    }

    public PIMCategory getCategory(Portal portal, Integer id, Set<Role> roles) {
        PIMCategory category = entityManager.find(PIMCategory.class, id);
        if (!category.getPortal().equals(portal)) throw new IllegalArgumentException("That category doesn't belong to you");
        for (Role role : category.getRoles()) if (!roles.contains(role)) throw new IllegalArgumentException();
        return category;
    }

    public PIMCategory getCategory(Portal portal, String name) {
        return (PIMCategory) entityManager.createNamedQuery("PIMCategory.findByPortalAndName").setParameter("portal", portal).setParameter("name", name).getSingleResult();
    }

    public PIMCategory getCategory(Portal portal, String name, Set<Role> roles) {
        PIMCategory category = (PIMCategory) entityManager.createNamedQuery("PIMCategory.findByPortalAndName").setParameter("portal", portal).setParameter("name", name).getSingleResult();
        for (Role role : category.getRoles()) if (!roles.contains(role)) throw new IllegalArgumentException();
        return category;
    }

    @SuppressWarnings("unchecked")
    public List<PIMEvent> getEventsByTag(Portal portal, String tagBody) {
        List<PIMEvent> events = entityManager.createNamedQuery("CMEvent.findByPortalAndTag").setParameter("portal", portal).setParameter("tagBody", tagBody).getResultList();
        return events;
    }
}
