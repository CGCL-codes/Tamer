package com.crypticbit.ipa.entity.concept.wrapper.impl;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import com.crypticbit.ipa.central.LogFactory;
import com.crypticbit.ipa.entity.concept.Event;
import com.crypticbit.ipa.entity.concept.GeoLocation;
import com.crypticbit.ipa.entity.concept.Who;
import com.crypticbit.ipa.entity.concept.wrapper.ConceptFactory;
import com.crypticbit.ipa.entity.concept.wrapper.DescriptionTag;
import com.crypticbit.ipa.entity.concept.wrapper.Tag;
import com.crypticbit.ipa.entity.concept.wrapper.WhenTag;
import com.crypticbit.ipa.entity.concept.wrapper.WhereTag;
import com.crypticbit.ipa.entity.concept.wrapper.WhoTag;
import com.crypticbit.ipa.entity.concept.wrapper.WhoTag.Field;
import com.crypticbit.ipa.entity.concept.wrapper.WhoTag.WhoConceptable;
import com.crypticbit.ipa.results.Location;

public class EventImpl implements Event {

    private ConceptFactory conceptFactory;

    public EventImpl(Location fileLocation, ConceptFactory conceptFactory) {
        this.fileLocation = fileLocation;
        this.conceptFactory = conceptFactory;
    }

    private Location fileLocation;

    private String description;

    private Map<Tag, WhenImpl> when = new HashMap<Tag, WhenImpl>() {

        public String toString() {
            if (size() == 0) return "";
            if (size() == 1) return values().iterator().next().toString(); else {
                Date earliestDate = null;
                Date latestDate = null;
                for (final Date dd : values()) {
                    if (earliestDate == null || dd.before(earliestDate)) earliestDate = dd;
                    if (latestDate == null || dd.after(latestDate)) latestDate = dd;
                }
                return earliestDate + " - " + latestDate;
            }
        }
    };

    private Map<Tag, WhereImpl> where = new HashMap<Tag, WhereImpl>() {

        public String toString() {
            if (size() == 0) return "";
            if (size() == 1) return values().iterator().next().toString(); else return size() + " locations";
        }
    };

    private Map<Tag, WhoImpl> who = new HashMap<Tag, WhoImpl>() {

        public String toString() {
            if (size() == 0) return "";
            if (size() == 1) return values().iterator().next().toString(); else return size() + " people (" + super.toString() + ")";
        }
    };

    private String what;

    boolean empty = true;

    @Override
    public Map<Tag, ? extends Date> getWhen() {
        return when;
    }

    @Override
    public Map<Tag, ? extends GeoLocation> getLocations() {
        return where;
    }

    @Override
    public String getWhat() {
        return what;
    }

    public void setWhat(String what) {
        this.what = what;
    }

    @Override
    public Map<Tag, ? extends Who> getWho() {
        return who;
    }

    public boolean isEmpty() {
        return empty;
    }

    public String toString() {
        return when + "\n" + where + "\n" + who;
    }

    public enum Concept {

        WHEN(WhenTag.class) {

            @Override
            public void addToEvent(ConceptFactory conceptFactory, EventImpl event, Object annotation, Object value, Tag tagPrefix, Object object) {
                if (value != null) {
                    Tag tag = tagPrefix.add(((WhenTag) annotation).tag());
                    WhenTag.Field field = ((WhenTag) annotation).field();
                    if (!event.when.containsKey(tag)) event.when.put(tag, new WhenImpl());
                    field.setValue(event.when.get(tag), value);
                }
            }
        }
        , WHERE(WhereTag.class) {

            @Override
            public void addToEvent(ConceptFactory conceptFactory, EventImpl event, Object annotation, Object value, Tag tagPrefix, Object object) {
                if (value != null) {
                    Tag tag = tagPrefix.add(((WhereTag) annotation).tag());
                    WhereTag.Field field = ((WhereTag) annotation).field();
                    if (!event.where.containsKey(tag)) event.where.put(tag, new WhereImpl());
                    field.setValue(event.where.get(tag), value);
                }
            }
        }
        , WHO(WhoTag.class) {

            @Override
            public void addToEvent(ConceptFactory conceptFactory, EventImpl event, Object annotation, Object value, Tag tagPrefix, Object object) {
                if (value != null) {
                    Tag tag = tagPrefix.add(((WhoTag) annotation).tag());
                    WhoTag.Field field = ((WhoTag) annotation).field();
                    if (field == Field.LOOKUP) {
                        String methodName = ((WhoTag) annotation).fieldMethod();
                        try {
                            Method m = object.getClass().getMethod(methodName);
                            Object result = m.invoke(object);
                            if (result != null) {
                                if (result instanceof Field) field = (Field) result;
                                if (result instanceof WhoConceptable) field = ((WhoConceptable) result).getConceptType();
                            }
                        } catch (Exception e) {
                            LogFactory.getLogger().log(Level.WARNING, "Problem processing concepts", e);
                        }
                    }
                    if (field != null && field != Field.LOOKUP) {
                        if (!event.who.containsKey(tag)) {
                            event.who.put(tag, new WhoImpl(conceptFactory));
                        }
                        field.setValue(event.who.get(tag), (String) value);
                    }
                }
            }
        }
        , DESCRIPTION(DescriptionTag.class) {

            @Override
            public void addToEvent(ConceptFactory conceptFactory, EventImpl event, Object annotation, Object value, Tag tagPrefix, Object object) {
                event.setDescription(value.toString());
            }
        }
        ;

        public final Class annotationClass;

        Concept(Class annotationClass) {
            this.annotationClass = annotationClass;
        }

        public abstract void addToEvent(ConceptFactory conceptFactory, EventImpl event, Object annotation, Object value, Tag tagPrefix, Object object);
    }

    public void add(Concept concept, Object annotation, Object value, Tag tagPrefix, Object object) {
        if (value != null) {
            concept.addToEvent(conceptFactory, this, annotation, value, tagPrefix, object);
            empty = false;
        }
    }

    protected void setDescription(String string) {
        this.description = string;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public Location getFileLocation() {
        return fileLocation;
    }
}
