package org.marcont.services.definitions.timeEntry;

import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.rdf.listeners.StatementListener;
import com.hp.hpl.jena.vocabulary.RDF;
import com.ibm.adtech.jastor.*;
import com.ibm.adtech.jastor.util.*;

/**
 * Implementation of {@link org.marcont.services.definitions.timeEntry.CalendarClockDescription}
 * Use the org.marcont.services.definitions.timeEntry.time_DASH_entry_DOT_owlFactory to create instances of this class.
 * <p>(URI: http://www.isi.edu/~pan/damltime/time-entry.owl#CalendarClockDescription)</p>
 * <br>
 */
public class CalendarClockDescriptionImpl extends com.ibm.adtech.jastor.ThingImpl implements org.marcont.services.definitions.timeEntry.CalendarClockDescription {

    private static com.hp.hpl.jena.rdf.model.Property minuteProperty = ResourceFactory.createProperty("http://www.isi.edu/~pan/damltime/time-entry.owl#minute");

    private java.lang.String minute;

    private static com.hp.hpl.jena.rdf.model.Property dayOfWeekFieldProperty = ResourceFactory.createProperty("http://www.isi.edu/~pan/damltime/time-entry.owl#dayOfWeekField");

    private java.lang.String dayOfWeekField;

    private static com.hp.hpl.jena.rdf.model.Property timeZoneProperty = ResourceFactory.createProperty("http://www.isi.edu/~pan/damltime/time-entry.owl#timeZone");

    private org.marcont.services.definitions.timeEntry.TimeZone timeZone;

    private static com.hp.hpl.jena.rdf.model.Property secondProperty = ResourceFactory.createProperty("http://www.isi.edu/~pan/damltime/time-entry.owl#second");

    private java.math.BigDecimal second;

    private static com.hp.hpl.jena.rdf.model.Property dayOfYearFieldProperty = ResourceFactory.createProperty("http://www.isi.edu/~pan/damltime/time-entry.owl#dayOfYearField");

    private java.lang.String dayOfYearField;

    private static com.hp.hpl.jena.rdf.model.Property dayProperty = ResourceFactory.createProperty("http://www.isi.edu/~pan/damltime/time-entry.owl#day");

    private java.lang.String day;

    private static com.hp.hpl.jena.rdf.model.Property weekProperty = ResourceFactory.createProperty("http://www.isi.edu/~pan/damltime/time-entry.owl#week");

    private java.lang.String week;

    private static com.hp.hpl.jena.rdf.model.Property hourProperty = ResourceFactory.createProperty("http://www.isi.edu/~pan/damltime/time-entry.owl#hour");

    private java.lang.String hour;

    private static com.hp.hpl.jena.rdf.model.Property monthProperty = ResourceFactory.createProperty("http://www.isi.edu/~pan/damltime/time-entry.owl#month");

    private java.lang.String month;

    private static com.hp.hpl.jena.rdf.model.Property unitTypeProperty = ResourceFactory.createProperty("http://www.isi.edu/~pan/damltime/time-entry.owl#unitType");

    private org.marcont.services.definitions.timeEntry.TemporalUnit unitType;

    private static com.hp.hpl.jena.rdf.model.Property yearProperty = ResourceFactory.createProperty("http://www.isi.edu/~pan/damltime/time-entry.owl#year");

    private java.lang.String year;

    private static com.hp.hpl.jena.rdf.model.Property intContainsProperty = ResourceFactory.createProperty("http://www.isi.edu/~pan/damltime/time-entry.owl#intContains");

    private java.util.ArrayList intContains;

    private static com.hp.hpl.jena.rdf.model.Property afterProperty = ResourceFactory.createProperty("http://www.isi.edu/~pan/damltime/time-entry.owl#after");

    private java.util.ArrayList after;

    private static com.hp.hpl.jena.rdf.model.Property intStartedByProperty = ResourceFactory.createProperty("http://www.isi.edu/~pan/damltime/time-entry.owl#intStartedBy");

    private java.util.ArrayList intStartedBy;

    private static com.hp.hpl.jena.rdf.model.Property intFinishedByProperty = ResourceFactory.createProperty("http://www.isi.edu/~pan/damltime/time-entry.owl#intFinishedBy");

    private java.util.ArrayList intFinishedBy;

    private static com.hp.hpl.jena.rdf.model.Property intOverlappedByProperty = ResourceFactory.createProperty("http://www.isi.edu/~pan/damltime/time-entry.owl#intOverlappedBy");

    private java.util.ArrayList intOverlappedBy;

    CalendarClockDescriptionImpl(Resource resource, Model model) throws JastorException {
        super(resource, model);
        setupModelListener();
    }

    static CalendarClockDescriptionImpl getCalendarClockDescription(Resource resource, Model model) throws JastorException {
        return new CalendarClockDescriptionImpl(resource, model);
    }

    static CalendarClockDescriptionImpl createCalendarClockDescription(Resource resource, Model model) throws JastorException {
        CalendarClockDescriptionImpl impl = new CalendarClockDescriptionImpl(resource, model);
        if (!impl._model.contains(new com.hp.hpl.jena.rdf.model.impl.StatementImpl(impl._resource, RDF.type, CalendarClockDescription.TYPE))) impl._model.add(impl._resource, RDF.type, CalendarClockDescription.TYPE);
        impl.addSuperTypes();
        impl.addHasValueValues();
        return impl;
    }

    void addSuperTypes() {
    }

    void addHasValueValues() {
    }

    private void setupModelListener() {
        listeners = new java.util.ArrayList();
        org.marcont.services.definitions.timeEntry.time_DASH_entry_DOT_owlFactory.registerThing(this);
    }

    public java.util.List listStatements() {
        java.util.List list = new java.util.ArrayList();
        StmtIterator it = null;
        it = _model.listStatements(_resource, minuteProperty, (RDFNode) null);
        while (it.hasNext()) {
            list.add(it.next());
        }
        it = _model.listStatements(_resource, dayOfWeekFieldProperty, (RDFNode) null);
        while (it.hasNext()) {
            list.add(it.next());
        }
        it = _model.listStatements(_resource, timeZoneProperty, (RDFNode) null);
        while (it.hasNext()) {
            list.add(it.next());
        }
        it = _model.listStatements(_resource, secondProperty, (RDFNode) null);
        while (it.hasNext()) {
            list.add(it.next());
        }
        it = _model.listStatements(_resource, dayOfYearFieldProperty, (RDFNode) null);
        while (it.hasNext()) {
            list.add(it.next());
        }
        it = _model.listStatements(_resource, dayProperty, (RDFNode) null);
        while (it.hasNext()) {
            list.add(it.next());
        }
        it = _model.listStatements(_resource, weekProperty, (RDFNode) null);
        while (it.hasNext()) {
            list.add(it.next());
        }
        it = _model.listStatements(_resource, hourProperty, (RDFNode) null);
        while (it.hasNext()) {
            list.add(it.next());
        }
        it = _model.listStatements(_resource, monthProperty, (RDFNode) null);
        while (it.hasNext()) {
            list.add(it.next());
        }
        it = _model.listStatements(_resource, unitTypeProperty, (RDFNode) null);
        while (it.hasNext()) {
            list.add(it.next());
        }
        it = _model.listStatements(_resource, yearProperty, (RDFNode) null);
        while (it.hasNext()) {
            list.add(it.next());
        }
        it = _model.listStatements(_resource, intContainsProperty, (RDFNode) null);
        while (it.hasNext()) {
            list.add(it.next());
        }
        it = _model.listStatements(_resource, afterProperty, (RDFNode) null);
        while (it.hasNext()) {
            list.add(it.next());
        }
        it = _model.listStatements(_resource, intStartedByProperty, (RDFNode) null);
        while (it.hasNext()) {
            list.add(it.next());
        }
        it = _model.listStatements(_resource, intFinishedByProperty, (RDFNode) null);
        while (it.hasNext()) {
            list.add(it.next());
        }
        it = _model.listStatements(_resource, intOverlappedByProperty, (RDFNode) null);
        while (it.hasNext()) {
            list.add(it.next());
        }
        it = _model.listStatements(_resource, RDF.type, org.marcont.services.definitions.timeEntry.CalendarClockDescription.TYPE);
        while (it.hasNext()) {
            list.add(it.next());
        }
        return list;
    }

    public void clearCache() {
        minute = null;
        dayOfWeekField = null;
        timeZone = null;
        second = null;
        dayOfYearField = null;
        day = null;
        week = null;
        hour = null;
        month = null;
        unitType = null;
        year = null;
        intContains = null;
        after = null;
        intStartedBy = null;
        intFinishedBy = null;
        intOverlappedBy = null;
    }

    private com.hp.hpl.jena.rdf.model.Literal createLiteral(Object obj) {
        return _model.createTypedLiteral(obj);
    }

    public java.lang.String getMinute() throws JastorException {
        if (minute != null) return minute;
        com.hp.hpl.jena.rdf.model.Statement stmt = _model.getProperty(_resource, minuteProperty);
        if (stmt == null) return null;
        if (!stmt.getObject().canAs(com.hp.hpl.jena.rdf.model.Literal.class)) throw new JastorInvalidRDFNodeException(uri() + ": minute getProperty() in org.marcont.services.definitions.timeEntry.CalendarClockDescription model not Literal", stmt.getObject());
        com.hp.hpl.jena.rdf.model.Literal literal = (com.hp.hpl.jena.rdf.model.Literal) stmt.getObject().as(com.hp.hpl.jena.rdf.model.Literal.class);
        Object obj = Util.fixLiteral(true, literal, "java.lang.String", "http://www.w3.org/2001/XMLSchema#nonNegativeInteger");
        minute = (java.lang.String) obj;
        return minute;
    }

    public void setMinute(java.lang.String minute) throws JastorException {
        if (_model.contains(_resource, minuteProperty)) {
            _model.removeAll(_resource, minuteProperty, null);
        }
        this.minute = minute;
        if (minute != null) {
            _model.add(_model.createStatement(_resource, minuteProperty, createLiteral(minute)));
        }
    }

    public java.lang.String getDayOfWeekField() throws JastorException {
        if (dayOfWeekField != null) return dayOfWeekField;
        com.hp.hpl.jena.rdf.model.Statement stmt = _model.getProperty(_resource, dayOfWeekFieldProperty);
        if (stmt == null) return null;
        if (!stmt.getObject().canAs(com.hp.hpl.jena.rdf.model.Literal.class)) throw new JastorInvalidRDFNodeException(uri() + ": dayOfWeekField getProperty() in org.marcont.services.definitions.timeEntry.CalendarClockDescription model not Literal", stmt.getObject());
        com.hp.hpl.jena.rdf.model.Literal literal = (com.hp.hpl.jena.rdf.model.Literal) stmt.getObject().as(com.hp.hpl.jena.rdf.model.Literal.class);
        Object obj = Util.fixLiteral(true, literal, "java.lang.String", "http://www.w3.org/2001/XMLSchema#nonNegativeInteger");
        dayOfWeekField = (java.lang.String) obj;
        return dayOfWeekField;
    }

    public void setDayOfWeekField(java.lang.String dayOfWeekField) throws JastorException {
        if (_model.contains(_resource, dayOfWeekFieldProperty)) {
            _model.removeAll(_resource, dayOfWeekFieldProperty, null);
        }
        this.dayOfWeekField = dayOfWeekField;
        if (dayOfWeekField != null) {
            _model.add(_model.createStatement(_resource, dayOfWeekFieldProperty, createLiteral(dayOfWeekField)));
        }
    }

    public org.marcont.services.definitions.timeEntry.TimeZone getTimeZone() throws JastorException {
        if (timeZone != null) return timeZone;
        com.hp.hpl.jena.rdf.model.Statement stmt = _model.getProperty(_resource, timeZoneProperty);
        if (stmt == null) return null;
        if (!stmt.getObject().canAs(com.hp.hpl.jena.rdf.model.Resource.class)) throw new JastorInvalidRDFNodeException(uri() + ": timeZone getProperty() in org.marcont.services.definitions.timeEntry.CalendarClockDescription model not Resource", stmt.getObject());
        com.hp.hpl.jena.rdf.model.Resource resource = (com.hp.hpl.jena.rdf.model.Resource) stmt.getObject().as(com.hp.hpl.jena.rdf.model.Resource.class);
        timeZone = org.marcont.services.definitions.timeEntry.time_DASH_entry_DOT_owlFactory.getTimeZone(resource, _model);
        return timeZone;
    }

    public void setTimeZone(org.marcont.services.definitions.timeEntry.TimeZone timeZone) throws JastorException {
        if (_model.contains(_resource, timeZoneProperty)) {
            _model.removeAll(_resource, timeZoneProperty, null);
        }
        this.timeZone = timeZone;
        if (timeZone != null) {
            _model.add(_model.createStatement(_resource, timeZoneProperty, timeZone.resource()));
        }
    }

    public org.marcont.services.definitions.timeEntry.TimeZone setTimeZone() throws JastorException {
        if (_model.contains(_resource, timeZoneProperty)) {
            _model.removeAll(_resource, timeZoneProperty, null);
        }
        org.marcont.services.definitions.timeEntry.TimeZone timeZone = org.marcont.services.definitions.timeEntry.time_DASH_entry_DOT_owlFactory.createTimeZone(_model.createResource(), _model);
        this.timeZone = timeZone;
        _model.add(_model.createStatement(_resource, timeZoneProperty, timeZone.resource()));
        return timeZone;
    }

    public org.marcont.services.definitions.timeEntry.TimeZone setTimeZone(com.hp.hpl.jena.rdf.model.Resource resource) throws JastorException {
        if (_model.contains(_resource, timeZoneProperty)) {
            _model.removeAll(_resource, timeZoneProperty, null);
        }
        org.marcont.services.definitions.timeEntry.TimeZone timeZone = org.marcont.services.definitions.timeEntry.time_DASH_entry_DOT_owlFactory.getTimeZone(resource, _model);
        this.timeZone = timeZone;
        _model.add(_model.createStatement(_resource, timeZoneProperty, timeZone.resource()));
        return timeZone;
    }

    public java.math.BigDecimal getSecond() throws JastorException {
        if (second != null) return second;
        com.hp.hpl.jena.rdf.model.Statement stmt = _model.getProperty(_resource, secondProperty);
        if (stmt == null) return null;
        if (!stmt.getObject().canAs(com.hp.hpl.jena.rdf.model.Literal.class)) throw new JastorInvalidRDFNodeException(uri() + ": second getProperty() in org.marcont.services.definitions.timeEntry.CalendarClockDescription model not Literal", stmt.getObject());
        com.hp.hpl.jena.rdf.model.Literal literal = (com.hp.hpl.jena.rdf.model.Literal) stmt.getObject().as(com.hp.hpl.jena.rdf.model.Literal.class);
        Object obj = Util.fixLiteral(true, literal, "java.math.BigDecimal", "http://www.w3.org/2001/XMLSchema#decimal");
        second = (java.math.BigDecimal) obj;
        return second;
    }

    public void setSecond(java.math.BigDecimal second) throws JastorException {
        if (_model.contains(_resource, secondProperty)) {
            _model.removeAll(_resource, secondProperty, null);
        }
        this.second = second;
        if (second != null) {
            _model.add(_model.createStatement(_resource, secondProperty, createLiteral(second)));
        }
    }

    public java.lang.String getDayOfYearField() throws JastorException {
        if (dayOfYearField != null) return dayOfYearField;
        com.hp.hpl.jena.rdf.model.Statement stmt = _model.getProperty(_resource, dayOfYearFieldProperty);
        if (stmt == null) return null;
        if (!stmt.getObject().canAs(com.hp.hpl.jena.rdf.model.Literal.class)) throw new JastorInvalidRDFNodeException(uri() + ": dayOfYearField getProperty() in org.marcont.services.definitions.timeEntry.CalendarClockDescription model not Literal", stmt.getObject());
        com.hp.hpl.jena.rdf.model.Literal literal = (com.hp.hpl.jena.rdf.model.Literal) stmt.getObject().as(com.hp.hpl.jena.rdf.model.Literal.class);
        Object obj = Util.fixLiteral(true, literal, "java.lang.String", "http://www.w3.org/2001/XMLSchema#nonNegativeInteger");
        dayOfYearField = (java.lang.String) obj;
        return dayOfYearField;
    }

    public void setDayOfYearField(java.lang.String dayOfYearField) throws JastorException {
        if (_model.contains(_resource, dayOfYearFieldProperty)) {
            _model.removeAll(_resource, dayOfYearFieldProperty, null);
        }
        this.dayOfYearField = dayOfYearField;
        if (dayOfYearField != null) {
            _model.add(_model.createStatement(_resource, dayOfYearFieldProperty, createLiteral(dayOfYearField)));
        }
    }

    public java.lang.String getDay() throws JastorException {
        if (day != null) return day;
        com.hp.hpl.jena.rdf.model.Statement stmt = _model.getProperty(_resource, dayProperty);
        if (stmt == null) return null;
        if (!stmt.getObject().canAs(com.hp.hpl.jena.rdf.model.Literal.class)) throw new JastorInvalidRDFNodeException(uri() + ": day getProperty() in org.marcont.services.definitions.timeEntry.CalendarClockDescription model not Literal", stmt.getObject());
        com.hp.hpl.jena.rdf.model.Literal literal = (com.hp.hpl.jena.rdf.model.Literal) stmt.getObject().as(com.hp.hpl.jena.rdf.model.Literal.class);
        Object obj = Util.fixLiteral(true, literal, "java.lang.String", "http://www.w3.org/2001/XMLSchema#gDay");
        day = (java.lang.String) obj;
        return day;
    }

    public void setDay(java.lang.String day) throws JastorException {
        if (_model.contains(_resource, dayProperty)) {
            _model.removeAll(_resource, dayProperty, null);
        }
        this.day = day;
        if (day != null) {
            _model.add(_model.createStatement(_resource, dayProperty, createLiteral(day)));
        }
    }

    public java.lang.String getWeek() throws JastorException {
        if (week != null) return week;
        com.hp.hpl.jena.rdf.model.Statement stmt = _model.getProperty(_resource, weekProperty);
        if (stmt == null) return null;
        if (!stmt.getObject().canAs(com.hp.hpl.jena.rdf.model.Literal.class)) throw new JastorInvalidRDFNodeException(uri() + ": week getProperty() in org.marcont.services.definitions.timeEntry.CalendarClockDescription model not Literal", stmt.getObject());
        com.hp.hpl.jena.rdf.model.Literal literal = (com.hp.hpl.jena.rdf.model.Literal) stmt.getObject().as(com.hp.hpl.jena.rdf.model.Literal.class);
        Object obj = Util.fixLiteral(true, literal, "java.lang.String", "http://www.w3.org/2001/XMLSchema#nonNegativeInteger");
        week = (java.lang.String) obj;
        return week;
    }

    public void setWeek(java.lang.String week) throws JastorException {
        if (_model.contains(_resource, weekProperty)) {
            _model.removeAll(_resource, weekProperty, null);
        }
        this.week = week;
        if (week != null) {
            _model.add(_model.createStatement(_resource, weekProperty, createLiteral(week)));
        }
    }

    public java.lang.String getHour() throws JastorException {
        if (hour != null) return hour;
        com.hp.hpl.jena.rdf.model.Statement stmt = _model.getProperty(_resource, hourProperty);
        if (stmt == null) return null;
        if (!stmt.getObject().canAs(com.hp.hpl.jena.rdf.model.Literal.class)) throw new JastorInvalidRDFNodeException(uri() + ": hour getProperty() in org.marcont.services.definitions.timeEntry.CalendarClockDescription model not Literal", stmt.getObject());
        com.hp.hpl.jena.rdf.model.Literal literal = (com.hp.hpl.jena.rdf.model.Literal) stmt.getObject().as(com.hp.hpl.jena.rdf.model.Literal.class);
        Object obj = Util.fixLiteral(true, literal, "java.lang.String", "http://www.w3.org/2001/XMLSchema#nonNegativeInteger");
        hour = (java.lang.String) obj;
        return hour;
    }

    public void setHour(java.lang.String hour) throws JastorException {
        if (_model.contains(_resource, hourProperty)) {
            _model.removeAll(_resource, hourProperty, null);
        }
        this.hour = hour;
        if (hour != null) {
            _model.add(_model.createStatement(_resource, hourProperty, createLiteral(hour)));
        }
    }

    public java.lang.String getMonth() throws JastorException {
        if (month != null) return month;
        com.hp.hpl.jena.rdf.model.Statement stmt = _model.getProperty(_resource, monthProperty);
        if (stmt == null) return null;
        if (!stmt.getObject().canAs(com.hp.hpl.jena.rdf.model.Literal.class)) throw new JastorInvalidRDFNodeException(uri() + ": month getProperty() in org.marcont.services.definitions.timeEntry.CalendarClockDescription model not Literal", stmt.getObject());
        com.hp.hpl.jena.rdf.model.Literal literal = (com.hp.hpl.jena.rdf.model.Literal) stmt.getObject().as(com.hp.hpl.jena.rdf.model.Literal.class);
        Object obj = Util.fixLiteral(true, literal, "java.lang.String", "http://www.w3.org/2001/XMLSchema#gMonth");
        month = (java.lang.String) obj;
        return month;
    }

    public void setMonth(java.lang.String month) throws JastorException {
        if (_model.contains(_resource, monthProperty)) {
            _model.removeAll(_resource, monthProperty, null);
        }
        this.month = month;
        if (month != null) {
            _model.add(_model.createStatement(_resource, monthProperty, createLiteral(month)));
        }
    }

    public org.marcont.services.definitions.timeEntry.TemporalUnit getUnitType() throws JastorException {
        if (unitType != null) return unitType;
        com.hp.hpl.jena.rdf.model.Statement stmt = _model.getProperty(_resource, unitTypeProperty);
        if (stmt == null) return null;
        if (!stmt.getObject().canAs(com.hp.hpl.jena.rdf.model.Resource.class)) throw new JastorInvalidRDFNodeException(uri() + ": unitType getProperty() in org.marcont.services.definitions.timeEntry.CalendarClockDescription model not Resource", stmt.getObject());
        com.hp.hpl.jena.rdf.model.Resource resource = (com.hp.hpl.jena.rdf.model.Resource) stmt.getObject().as(com.hp.hpl.jena.rdf.model.Resource.class);
        unitType = org.marcont.services.definitions.timeEntry.time_DASH_entry_DOT_owlFactory.getTemporalUnit(resource, _model);
        return unitType;
    }

    public void setUnitType(org.marcont.services.definitions.timeEntry.TemporalUnit unitType) throws JastorException {
        if (_model.contains(_resource, unitTypeProperty)) {
            _model.removeAll(_resource, unitTypeProperty, null);
        }
        this.unitType = unitType;
        if (unitType != null) {
            _model.add(_model.createStatement(_resource, unitTypeProperty, unitType.resource()));
        }
    }

    public org.marcont.services.definitions.timeEntry.TemporalUnit setUnitType() throws JastorException {
        if (_model.contains(_resource, unitTypeProperty)) {
            _model.removeAll(_resource, unitTypeProperty, null);
        }
        org.marcont.services.definitions.timeEntry.TemporalUnit unitType = org.marcont.services.definitions.timeEntry.time_DASH_entry_DOT_owlFactory.createTemporalUnit(_model.createResource(), _model);
        this.unitType = unitType;
        _model.add(_model.createStatement(_resource, unitTypeProperty, unitType.resource()));
        return unitType;
    }

    public org.marcont.services.definitions.timeEntry.TemporalUnit setUnitType(com.hp.hpl.jena.rdf.model.Resource resource) throws JastorException {
        if (_model.contains(_resource, unitTypeProperty)) {
            _model.removeAll(_resource, unitTypeProperty, null);
        }
        org.marcont.services.definitions.timeEntry.TemporalUnit unitType = org.marcont.services.definitions.timeEntry.time_DASH_entry_DOT_owlFactory.getTemporalUnit(resource, _model);
        this.unitType = unitType;
        _model.add(_model.createStatement(_resource, unitTypeProperty, unitType.resource()));
        return unitType;
    }

    public java.lang.String getYear() throws JastorException {
        if (year != null) return year;
        com.hp.hpl.jena.rdf.model.Statement stmt = _model.getProperty(_resource, yearProperty);
        if (stmt == null) return null;
        if (!stmt.getObject().canAs(com.hp.hpl.jena.rdf.model.Literal.class)) throw new JastorInvalidRDFNodeException(uri() + ": year getProperty() in org.marcont.services.definitions.timeEntry.CalendarClockDescription model not Literal", stmt.getObject());
        com.hp.hpl.jena.rdf.model.Literal literal = (com.hp.hpl.jena.rdf.model.Literal) stmt.getObject().as(com.hp.hpl.jena.rdf.model.Literal.class);
        Object obj = Util.fixLiteral(true, literal, "java.lang.String", "http://www.w3.org/2001/XMLSchema#gYear");
        year = (java.lang.String) obj;
        return year;
    }

    public void setYear(java.lang.String year) throws JastorException {
        if (_model.contains(_resource, yearProperty)) {
            _model.removeAll(_resource, yearProperty, null);
        }
        this.year = year;
        if (year != null) {
            _model.add(_model.createStatement(_resource, yearProperty, createLiteral(year)));
        }
    }

    private void initIntContains() throws JastorException {
        this.intContains = new java.util.ArrayList();
        StmtIterator it = _model.listStatements(_resource, intContainsProperty, (RDFNode) null);
        while (it.hasNext()) {
            com.hp.hpl.jena.rdf.model.Statement stmt = (com.hp.hpl.jena.rdf.model.Statement) it.next();
            if (!stmt.getObject().canAs(com.hp.hpl.jena.rdf.model.Resource.class)) throw new JastorInvalidRDFNodeException(uri() + ": One of the http://www.isi.edu/~pan/damltime/time-entry.owl#intContains properties in CalendarClockDescription model not a Resource", stmt.getObject());
            com.hp.hpl.jena.rdf.model.Resource resource = (com.hp.hpl.jena.rdf.model.Resource) stmt.getObject().as(com.hp.hpl.jena.rdf.model.Resource.class);
            if (true) {
                com.ibm.adtech.jastor.Thing intContains = com.ibm.adtech.jastor.ThingFactory.getThing(resource, _model);
                this.intContains.add(intContains);
            }
        }
    }

    public java.util.Iterator getIntContains() throws JastorException {
        if (intContains == null) initIntContains();
        return new com.ibm.adtech.jastor.util.CachedPropertyIterator(intContains, _resource, intContainsProperty, true);
    }

    public void addIntContains(com.ibm.adtech.jastor.Thing intContains) throws JastorException {
        if (this.intContains == null) initIntContains();
        if (this.intContains.contains(intContains)) {
            this.intContains.remove(intContains);
            this.intContains.add(intContains);
            return;
        }
        this.intContains.add(intContains);
        _model.add(_model.createStatement(_resource, intContainsProperty, intContains.resource()));
    }

    public com.ibm.adtech.jastor.Thing addIntContains() throws JastorException {
        com.ibm.adtech.jastor.Thing intContains = com.ibm.adtech.jastor.ThingFactory.createThing(_model.createResource(), _model);
        if (this.intContains == null) initIntContains();
        this.intContains.add(intContains);
        _model.add(_model.createStatement(_resource, intContainsProperty, intContains.resource()));
        return intContains;
    }

    public com.ibm.adtech.jastor.Thing addIntContains(com.hp.hpl.jena.rdf.model.Resource resource) throws JastorException {
        com.ibm.adtech.jastor.Thing intContains = com.ibm.adtech.jastor.ThingFactory.getThing(resource, _model);
        if (this.intContains == null) initIntContains();
        if (this.intContains.contains(intContains)) return intContains;
        this.intContains.add(intContains);
        _model.add(_model.createStatement(_resource, intContainsProperty, intContains.resource()));
        return intContains;
    }

    public void removeIntContains(com.ibm.adtech.jastor.Thing intContains) throws JastorException {
        if (this.intContains == null) initIntContains();
        if (!this.intContains.contains(intContains)) return;
        if (!_model.contains(_resource, intContainsProperty, intContains.resource())) return;
        this.intContains.remove(intContains);
        _model.removeAll(_resource, intContainsProperty, intContains.resource());
    }

    private void initAfter() throws JastorException {
        this.after = new java.util.ArrayList();
        StmtIterator it = _model.listStatements(_resource, afterProperty, (RDFNode) null);
        while (it.hasNext()) {
            com.hp.hpl.jena.rdf.model.Statement stmt = (com.hp.hpl.jena.rdf.model.Statement) it.next();
            if (!stmt.getObject().canAs(com.hp.hpl.jena.rdf.model.Resource.class)) throw new JastorInvalidRDFNodeException(uri() + ": One of the http://www.isi.edu/~pan/damltime/time-entry.owl#after properties in CalendarClockDescription model not a Resource", stmt.getObject());
            com.hp.hpl.jena.rdf.model.Resource resource = (com.hp.hpl.jena.rdf.model.Resource) stmt.getObject().as(com.hp.hpl.jena.rdf.model.Resource.class);
            if (true) {
                com.ibm.adtech.jastor.Thing after = com.ibm.adtech.jastor.ThingFactory.getThing(resource, _model);
                this.after.add(after);
            }
        }
    }

    public java.util.Iterator getAfter() throws JastorException {
        if (after == null) initAfter();
        return new com.ibm.adtech.jastor.util.CachedPropertyIterator(after, _resource, afterProperty, true);
    }

    public void addAfter(com.ibm.adtech.jastor.Thing after) throws JastorException {
        if (this.after == null) initAfter();
        if (this.after.contains(after)) {
            this.after.remove(after);
            this.after.add(after);
            return;
        }
        this.after.add(after);
        _model.add(_model.createStatement(_resource, afterProperty, after.resource()));
    }

    public com.ibm.adtech.jastor.Thing addAfter() throws JastorException {
        com.ibm.adtech.jastor.Thing after = com.ibm.adtech.jastor.ThingFactory.createThing(_model.createResource(), _model);
        if (this.after == null) initAfter();
        this.after.add(after);
        _model.add(_model.createStatement(_resource, afterProperty, after.resource()));
        return after;
    }

    public com.ibm.adtech.jastor.Thing addAfter(com.hp.hpl.jena.rdf.model.Resource resource) throws JastorException {
        com.ibm.adtech.jastor.Thing after = com.ibm.adtech.jastor.ThingFactory.getThing(resource, _model);
        if (this.after == null) initAfter();
        if (this.after.contains(after)) return after;
        this.after.add(after);
        _model.add(_model.createStatement(_resource, afterProperty, after.resource()));
        return after;
    }

    public void removeAfter(com.ibm.adtech.jastor.Thing after) throws JastorException {
        if (this.after == null) initAfter();
        if (!this.after.contains(after)) return;
        if (!_model.contains(_resource, afterProperty, after.resource())) return;
        this.after.remove(after);
        _model.removeAll(_resource, afterProperty, after.resource());
    }

    private void initIntStartedBy() throws JastorException {
        this.intStartedBy = new java.util.ArrayList();
        StmtIterator it = _model.listStatements(_resource, intStartedByProperty, (RDFNode) null);
        while (it.hasNext()) {
            com.hp.hpl.jena.rdf.model.Statement stmt = (com.hp.hpl.jena.rdf.model.Statement) it.next();
            if (!stmt.getObject().canAs(com.hp.hpl.jena.rdf.model.Resource.class)) throw new JastorInvalidRDFNodeException(uri() + ": One of the http://www.isi.edu/~pan/damltime/time-entry.owl#intStartedBy properties in CalendarClockDescription model not a Resource", stmt.getObject());
            com.hp.hpl.jena.rdf.model.Resource resource = (com.hp.hpl.jena.rdf.model.Resource) stmt.getObject().as(com.hp.hpl.jena.rdf.model.Resource.class);
            if (true) {
                com.ibm.adtech.jastor.Thing intStartedBy = com.ibm.adtech.jastor.ThingFactory.getThing(resource, _model);
                this.intStartedBy.add(intStartedBy);
            }
        }
    }

    public java.util.Iterator getIntStartedBy() throws JastorException {
        if (intStartedBy == null) initIntStartedBy();
        return new com.ibm.adtech.jastor.util.CachedPropertyIterator(intStartedBy, _resource, intStartedByProperty, true);
    }

    public void addIntStartedBy(com.ibm.adtech.jastor.Thing intStartedBy) throws JastorException {
        if (this.intStartedBy == null) initIntStartedBy();
        if (this.intStartedBy.contains(intStartedBy)) {
            this.intStartedBy.remove(intStartedBy);
            this.intStartedBy.add(intStartedBy);
            return;
        }
        this.intStartedBy.add(intStartedBy);
        _model.add(_model.createStatement(_resource, intStartedByProperty, intStartedBy.resource()));
    }

    public com.ibm.adtech.jastor.Thing addIntStartedBy() throws JastorException {
        com.ibm.adtech.jastor.Thing intStartedBy = com.ibm.adtech.jastor.ThingFactory.createThing(_model.createResource(), _model);
        if (this.intStartedBy == null) initIntStartedBy();
        this.intStartedBy.add(intStartedBy);
        _model.add(_model.createStatement(_resource, intStartedByProperty, intStartedBy.resource()));
        return intStartedBy;
    }

    public com.ibm.adtech.jastor.Thing addIntStartedBy(com.hp.hpl.jena.rdf.model.Resource resource) throws JastorException {
        com.ibm.adtech.jastor.Thing intStartedBy = com.ibm.adtech.jastor.ThingFactory.getThing(resource, _model);
        if (this.intStartedBy == null) initIntStartedBy();
        if (this.intStartedBy.contains(intStartedBy)) return intStartedBy;
        this.intStartedBy.add(intStartedBy);
        _model.add(_model.createStatement(_resource, intStartedByProperty, intStartedBy.resource()));
        return intStartedBy;
    }

    public void removeIntStartedBy(com.ibm.adtech.jastor.Thing intStartedBy) throws JastorException {
        if (this.intStartedBy == null) initIntStartedBy();
        if (!this.intStartedBy.contains(intStartedBy)) return;
        if (!_model.contains(_resource, intStartedByProperty, intStartedBy.resource())) return;
        this.intStartedBy.remove(intStartedBy);
        _model.removeAll(_resource, intStartedByProperty, intStartedBy.resource());
    }

    private void initIntFinishedBy() throws JastorException {
        this.intFinishedBy = new java.util.ArrayList();
        StmtIterator it = _model.listStatements(_resource, intFinishedByProperty, (RDFNode) null);
        while (it.hasNext()) {
            com.hp.hpl.jena.rdf.model.Statement stmt = (com.hp.hpl.jena.rdf.model.Statement) it.next();
            if (!stmt.getObject().canAs(com.hp.hpl.jena.rdf.model.Resource.class)) throw new JastorInvalidRDFNodeException(uri() + ": One of the http://www.isi.edu/~pan/damltime/time-entry.owl#intFinishedBy properties in CalendarClockDescription model not a Resource", stmt.getObject());
            com.hp.hpl.jena.rdf.model.Resource resource = (com.hp.hpl.jena.rdf.model.Resource) stmt.getObject().as(com.hp.hpl.jena.rdf.model.Resource.class);
            if (true) {
                com.ibm.adtech.jastor.Thing intFinishedBy = com.ibm.adtech.jastor.ThingFactory.getThing(resource, _model);
                this.intFinishedBy.add(intFinishedBy);
            }
        }
    }

    public java.util.Iterator getIntFinishedBy() throws JastorException {
        if (intFinishedBy == null) initIntFinishedBy();
        return new com.ibm.adtech.jastor.util.CachedPropertyIterator(intFinishedBy, _resource, intFinishedByProperty, true);
    }

    public void addIntFinishedBy(com.ibm.adtech.jastor.Thing intFinishedBy) throws JastorException {
        if (this.intFinishedBy == null) initIntFinishedBy();
        if (this.intFinishedBy.contains(intFinishedBy)) {
            this.intFinishedBy.remove(intFinishedBy);
            this.intFinishedBy.add(intFinishedBy);
            return;
        }
        this.intFinishedBy.add(intFinishedBy);
        _model.add(_model.createStatement(_resource, intFinishedByProperty, intFinishedBy.resource()));
    }

    public com.ibm.adtech.jastor.Thing addIntFinishedBy() throws JastorException {
        com.ibm.adtech.jastor.Thing intFinishedBy = com.ibm.adtech.jastor.ThingFactory.createThing(_model.createResource(), _model);
        if (this.intFinishedBy == null) initIntFinishedBy();
        this.intFinishedBy.add(intFinishedBy);
        _model.add(_model.createStatement(_resource, intFinishedByProperty, intFinishedBy.resource()));
        return intFinishedBy;
    }

    public com.ibm.adtech.jastor.Thing addIntFinishedBy(com.hp.hpl.jena.rdf.model.Resource resource) throws JastorException {
        com.ibm.adtech.jastor.Thing intFinishedBy = com.ibm.adtech.jastor.ThingFactory.getThing(resource, _model);
        if (this.intFinishedBy == null) initIntFinishedBy();
        if (this.intFinishedBy.contains(intFinishedBy)) return intFinishedBy;
        this.intFinishedBy.add(intFinishedBy);
        _model.add(_model.createStatement(_resource, intFinishedByProperty, intFinishedBy.resource()));
        return intFinishedBy;
    }

    public void removeIntFinishedBy(com.ibm.adtech.jastor.Thing intFinishedBy) throws JastorException {
        if (this.intFinishedBy == null) initIntFinishedBy();
        if (!this.intFinishedBy.contains(intFinishedBy)) return;
        if (!_model.contains(_resource, intFinishedByProperty, intFinishedBy.resource())) return;
        this.intFinishedBy.remove(intFinishedBy);
        _model.removeAll(_resource, intFinishedByProperty, intFinishedBy.resource());
    }

    private void initIntOverlappedBy() throws JastorException {
        this.intOverlappedBy = new java.util.ArrayList();
        StmtIterator it = _model.listStatements(_resource, intOverlappedByProperty, (RDFNode) null);
        while (it.hasNext()) {
            com.hp.hpl.jena.rdf.model.Statement stmt = (com.hp.hpl.jena.rdf.model.Statement) it.next();
            if (!stmt.getObject().canAs(com.hp.hpl.jena.rdf.model.Resource.class)) throw new JastorInvalidRDFNodeException(uri() + ": One of the http://www.isi.edu/~pan/damltime/time-entry.owl#intOverlappedBy properties in CalendarClockDescription model not a Resource", stmt.getObject());
            com.hp.hpl.jena.rdf.model.Resource resource = (com.hp.hpl.jena.rdf.model.Resource) stmt.getObject().as(com.hp.hpl.jena.rdf.model.Resource.class);
            if (true) {
                com.ibm.adtech.jastor.Thing intOverlappedBy = com.ibm.adtech.jastor.ThingFactory.getThing(resource, _model);
                this.intOverlappedBy.add(intOverlappedBy);
            }
        }
    }

    public java.util.Iterator getIntOverlappedBy() throws JastorException {
        if (intOverlappedBy == null) initIntOverlappedBy();
        return new com.ibm.adtech.jastor.util.CachedPropertyIterator(intOverlappedBy, _resource, intOverlappedByProperty, true);
    }

    public void addIntOverlappedBy(com.ibm.adtech.jastor.Thing intOverlappedBy) throws JastorException {
        if (this.intOverlappedBy == null) initIntOverlappedBy();
        if (this.intOverlappedBy.contains(intOverlappedBy)) {
            this.intOverlappedBy.remove(intOverlappedBy);
            this.intOverlappedBy.add(intOverlappedBy);
            return;
        }
        this.intOverlappedBy.add(intOverlappedBy);
        _model.add(_model.createStatement(_resource, intOverlappedByProperty, intOverlappedBy.resource()));
    }

    public com.ibm.adtech.jastor.Thing addIntOverlappedBy() throws JastorException {
        com.ibm.adtech.jastor.Thing intOverlappedBy = com.ibm.adtech.jastor.ThingFactory.createThing(_model.createResource(), _model);
        if (this.intOverlappedBy == null) initIntOverlappedBy();
        this.intOverlappedBy.add(intOverlappedBy);
        _model.add(_model.createStatement(_resource, intOverlappedByProperty, intOverlappedBy.resource()));
        return intOverlappedBy;
    }

    public com.ibm.adtech.jastor.Thing addIntOverlappedBy(com.hp.hpl.jena.rdf.model.Resource resource) throws JastorException {
        com.ibm.adtech.jastor.Thing intOverlappedBy = com.ibm.adtech.jastor.ThingFactory.getThing(resource, _model);
        if (this.intOverlappedBy == null) initIntOverlappedBy();
        if (this.intOverlappedBy.contains(intOverlappedBy)) return intOverlappedBy;
        this.intOverlappedBy.add(intOverlappedBy);
        _model.add(_model.createStatement(_resource, intOverlappedByProperty, intOverlappedBy.resource()));
        return intOverlappedBy;
    }

    public void removeIntOverlappedBy(com.ibm.adtech.jastor.Thing intOverlappedBy) throws JastorException {
        if (this.intOverlappedBy == null) initIntOverlappedBy();
        if (!this.intOverlappedBy.contains(intOverlappedBy)) return;
        if (!_model.contains(_resource, intOverlappedByProperty, intOverlappedBy.resource())) return;
        this.intOverlappedBy.remove(intOverlappedBy);
        _model.removeAll(_resource, intOverlappedByProperty, intOverlappedBy.resource());
    }

    private java.util.ArrayList listeners;

    public void registerListener(ThingListener listener) {
        if (!(listener instanceof CalendarClockDescriptionListener)) throw new IllegalArgumentException("ThingListener must be instance of CalendarClockDescriptionListener");
        if (listeners == null) setupModelListener();
        if (!this.listeners.contains(listener)) {
            this.listeners.add((CalendarClockDescriptionListener) listener);
        }
    }

    public void unregisterListener(ThingListener listener) {
        if (!(listener instanceof CalendarClockDescriptionListener)) throw new IllegalArgumentException("ThingListener must be instance of CalendarClockDescriptionListener");
        if (listeners == null) return;
        if (this.listeners.contains(listener)) {
            listeners.remove(listener);
        }
    }

    public void addedStatement(com.hp.hpl.jena.rdf.model.Statement stmt) {
        if (stmt.getPredicate().equals(minuteProperty)) {
            if (!stmt.getObject().canAs(com.hp.hpl.jena.rdf.model.Literal.class)) return;
            com.hp.hpl.jena.rdf.model.Literal literal = (com.hp.hpl.jena.rdf.model.Literal) stmt.getObject().as(com.hp.hpl.jena.rdf.model.Literal.class);
            minute = (java.lang.String) Util.fixLiteral(true, literal, "java.lang.String", "http://www.w3.org/2001/XMLSchema#nonNegativeInteger");
            if (listeners != null) {
                java.util.ArrayList consumers;
                synchronized (listeners) {
                    consumers = (java.util.ArrayList) listeners.clone();
                }
                for (java.util.Iterator iter = consumers.iterator(); iter.hasNext(); ) {
                    CalendarClockDescriptionListener listener = (CalendarClockDescriptionListener) iter.next();
                    listener.minuteChanged(org.marcont.services.definitions.timeEntry.CalendarClockDescriptionImpl.this);
                }
            }
            return;
        }
        if (stmt.getPredicate().equals(dayOfWeekFieldProperty)) {
            if (!stmt.getObject().canAs(com.hp.hpl.jena.rdf.model.Literal.class)) return;
            com.hp.hpl.jena.rdf.model.Literal literal = (com.hp.hpl.jena.rdf.model.Literal) stmt.getObject().as(com.hp.hpl.jena.rdf.model.Literal.class);
            dayOfWeekField = (java.lang.String) Util.fixLiteral(true, literal, "java.lang.String", "http://www.w3.org/2001/XMLSchema#nonNegativeInteger");
            if (listeners != null) {
                java.util.ArrayList consumers;
                synchronized (listeners) {
                    consumers = (java.util.ArrayList) listeners.clone();
                }
                for (java.util.Iterator iter = consumers.iterator(); iter.hasNext(); ) {
                    CalendarClockDescriptionListener listener = (CalendarClockDescriptionListener) iter.next();
                    listener.dayOfWeekFieldChanged(org.marcont.services.definitions.timeEntry.CalendarClockDescriptionImpl.this);
                }
            }
            return;
        }
        if (stmt.getPredicate().equals(timeZoneProperty)) {
            if (!stmt.getObject().canAs(com.hp.hpl.jena.rdf.model.Resource.class)) return;
            com.hp.hpl.jena.rdf.model.Resource resource = (com.hp.hpl.jena.rdf.model.Resource) stmt.getObject().as(com.hp.hpl.jena.rdf.model.Resource.class);
            timeZone = null;
            if (true) {
                try {
                    timeZone = org.marcont.services.definitions.timeEntry.time_DASH_entry_DOT_owlFactory.getTimeZone(resource, _model);
                } catch (JastorException e) {
                }
            }
            if (listeners != null) {
                java.util.ArrayList consumers;
                synchronized (listeners) {
                    consumers = (java.util.ArrayList) listeners.clone();
                }
                for (java.util.Iterator iter = consumers.iterator(); iter.hasNext(); ) {
                    CalendarClockDescriptionListener listener = (CalendarClockDescriptionListener) iter.next();
                    listener.timeZoneChanged(org.marcont.services.definitions.timeEntry.CalendarClockDescriptionImpl.this);
                }
            }
            return;
        }
        if (stmt.getPredicate().equals(secondProperty)) {
            if (!stmt.getObject().canAs(com.hp.hpl.jena.rdf.model.Literal.class)) return;
            com.hp.hpl.jena.rdf.model.Literal literal = (com.hp.hpl.jena.rdf.model.Literal) stmt.getObject().as(com.hp.hpl.jena.rdf.model.Literal.class);
            second = (java.math.BigDecimal) Util.fixLiteral(true, literal, "java.math.BigDecimal", "http://www.w3.org/2001/XMLSchema#decimal");
            if (listeners != null) {
                java.util.ArrayList consumers;
                synchronized (listeners) {
                    consumers = (java.util.ArrayList) listeners.clone();
                }
                for (java.util.Iterator iter = consumers.iterator(); iter.hasNext(); ) {
                    CalendarClockDescriptionListener listener = (CalendarClockDescriptionListener) iter.next();
                    listener.secondChanged(org.marcont.services.definitions.timeEntry.CalendarClockDescriptionImpl.this);
                }
            }
            return;
        }
        if (stmt.getPredicate().equals(dayOfYearFieldProperty)) {
            if (!stmt.getObject().canAs(com.hp.hpl.jena.rdf.model.Literal.class)) return;
            com.hp.hpl.jena.rdf.model.Literal literal = (com.hp.hpl.jena.rdf.model.Literal) stmt.getObject().as(com.hp.hpl.jena.rdf.model.Literal.class);
            dayOfYearField = (java.lang.String) Util.fixLiteral(true, literal, "java.lang.String", "http://www.w3.org/2001/XMLSchema#nonNegativeInteger");
            if (listeners != null) {
                java.util.ArrayList consumers;
                synchronized (listeners) {
                    consumers = (java.util.ArrayList) listeners.clone();
                }
                for (java.util.Iterator iter = consumers.iterator(); iter.hasNext(); ) {
                    CalendarClockDescriptionListener listener = (CalendarClockDescriptionListener) iter.next();
                    listener.dayOfYearFieldChanged(org.marcont.services.definitions.timeEntry.CalendarClockDescriptionImpl.this);
                }
            }
            return;
        }
        if (stmt.getPredicate().equals(dayProperty)) {
            if (!stmt.getObject().canAs(com.hp.hpl.jena.rdf.model.Literal.class)) return;
            com.hp.hpl.jena.rdf.model.Literal literal = (com.hp.hpl.jena.rdf.model.Literal) stmt.getObject().as(com.hp.hpl.jena.rdf.model.Literal.class);
            day = (java.lang.String) Util.fixLiteral(true, literal, "java.lang.String", "http://www.w3.org/2001/XMLSchema#gDay");
            if (listeners != null) {
                java.util.ArrayList consumers;
                synchronized (listeners) {
                    consumers = (java.util.ArrayList) listeners.clone();
                }
                for (java.util.Iterator iter = consumers.iterator(); iter.hasNext(); ) {
                    CalendarClockDescriptionListener listener = (CalendarClockDescriptionListener) iter.next();
                    listener.dayChanged(org.marcont.services.definitions.timeEntry.CalendarClockDescriptionImpl.this);
                }
            }
            return;
        }
        if (stmt.getPredicate().equals(weekProperty)) {
            if (!stmt.getObject().canAs(com.hp.hpl.jena.rdf.model.Literal.class)) return;
            com.hp.hpl.jena.rdf.model.Literal literal = (com.hp.hpl.jena.rdf.model.Literal) stmt.getObject().as(com.hp.hpl.jena.rdf.model.Literal.class);
            week = (java.lang.String) Util.fixLiteral(true, literal, "java.lang.String", "http://www.w3.org/2001/XMLSchema#nonNegativeInteger");
            if (listeners != null) {
                java.util.ArrayList consumers;
                synchronized (listeners) {
                    consumers = (java.util.ArrayList) listeners.clone();
                }
                for (java.util.Iterator iter = consumers.iterator(); iter.hasNext(); ) {
                    CalendarClockDescriptionListener listener = (CalendarClockDescriptionListener) iter.next();
                    listener.weekChanged(org.marcont.services.definitions.timeEntry.CalendarClockDescriptionImpl.this);
                }
            }
            return;
        }
        if (stmt.getPredicate().equals(hourProperty)) {
            if (!stmt.getObject().canAs(com.hp.hpl.jena.rdf.model.Literal.class)) return;
            com.hp.hpl.jena.rdf.model.Literal literal = (com.hp.hpl.jena.rdf.model.Literal) stmt.getObject().as(com.hp.hpl.jena.rdf.model.Literal.class);
            hour = (java.lang.String) Util.fixLiteral(true, literal, "java.lang.String", "http://www.w3.org/2001/XMLSchema#nonNegativeInteger");
            if (listeners != null) {
                java.util.ArrayList consumers;
                synchronized (listeners) {
                    consumers = (java.util.ArrayList) listeners.clone();
                }
                for (java.util.Iterator iter = consumers.iterator(); iter.hasNext(); ) {
                    CalendarClockDescriptionListener listener = (CalendarClockDescriptionListener) iter.next();
                    listener.hourChanged(org.marcont.services.definitions.timeEntry.CalendarClockDescriptionImpl.this);
                }
            }
            return;
        }
        if (stmt.getPredicate().equals(monthProperty)) {
            if (!stmt.getObject().canAs(com.hp.hpl.jena.rdf.model.Literal.class)) return;
            com.hp.hpl.jena.rdf.model.Literal literal = (com.hp.hpl.jena.rdf.model.Literal) stmt.getObject().as(com.hp.hpl.jena.rdf.model.Literal.class);
            month = (java.lang.String) Util.fixLiteral(true, literal, "java.lang.String", "http://www.w3.org/2001/XMLSchema#gMonth");
            if (listeners != null) {
                java.util.ArrayList consumers;
                synchronized (listeners) {
                    consumers = (java.util.ArrayList) listeners.clone();
                }
                for (java.util.Iterator iter = consumers.iterator(); iter.hasNext(); ) {
                    CalendarClockDescriptionListener listener = (CalendarClockDescriptionListener) iter.next();
                    listener.monthChanged(org.marcont.services.definitions.timeEntry.CalendarClockDescriptionImpl.this);
                }
            }
            return;
        }
        if (stmt.getPredicate().equals(unitTypeProperty)) {
            if (!stmt.getObject().canAs(com.hp.hpl.jena.rdf.model.Resource.class)) return;
            com.hp.hpl.jena.rdf.model.Resource resource = (com.hp.hpl.jena.rdf.model.Resource) stmt.getObject().as(com.hp.hpl.jena.rdf.model.Resource.class);
            unitType = null;
            if (true) {
                try {
                    unitType = org.marcont.services.definitions.timeEntry.time_DASH_entry_DOT_owlFactory.getTemporalUnit(resource, _model);
                } catch (JastorException e) {
                }
            }
            if (listeners != null) {
                java.util.ArrayList consumers;
                synchronized (listeners) {
                    consumers = (java.util.ArrayList) listeners.clone();
                }
                for (java.util.Iterator iter = consumers.iterator(); iter.hasNext(); ) {
                    CalendarClockDescriptionListener listener = (CalendarClockDescriptionListener) iter.next();
                    listener.unitTypeChanged(org.marcont.services.definitions.timeEntry.CalendarClockDescriptionImpl.this);
                }
            }
            return;
        }
        if (stmt.getPredicate().equals(yearProperty)) {
            if (!stmt.getObject().canAs(com.hp.hpl.jena.rdf.model.Literal.class)) return;
            com.hp.hpl.jena.rdf.model.Literal literal = (com.hp.hpl.jena.rdf.model.Literal) stmt.getObject().as(com.hp.hpl.jena.rdf.model.Literal.class);
            year = (java.lang.String) Util.fixLiteral(true, literal, "java.lang.String", "http://www.w3.org/2001/XMLSchema#gYear");
            if (listeners != null) {
                java.util.ArrayList consumers;
                synchronized (listeners) {
                    consumers = (java.util.ArrayList) listeners.clone();
                }
                for (java.util.Iterator iter = consumers.iterator(); iter.hasNext(); ) {
                    CalendarClockDescriptionListener listener = (CalendarClockDescriptionListener) iter.next();
                    listener.yearChanged(org.marcont.services.definitions.timeEntry.CalendarClockDescriptionImpl.this);
                }
            }
            return;
        }
        if (stmt.getPredicate().equals(intContainsProperty)) {
            if (!stmt.getObject().canAs(com.hp.hpl.jena.rdf.model.Resource.class)) return;
            com.hp.hpl.jena.rdf.model.Resource resource = (com.hp.hpl.jena.rdf.model.Resource) stmt.getObject().as(com.hp.hpl.jena.rdf.model.Resource.class);
            if (true) {
                com.ibm.adtech.jastor.Thing _intContains = null;
                try {
                    _intContains = com.ibm.adtech.jastor.ThingFactory.getThing(resource, _model);
                } catch (JastorException e) {
                }
                if (intContains == null) {
                    try {
                        initIntContains();
                    } catch (JastorException e) {
                        e.printStackTrace();
                        return;
                    }
                }
                if (!intContains.contains(_intContains)) intContains.add(_intContains);
                if (listeners != null) {
                    java.util.ArrayList consumersForIntContains;
                    synchronized (listeners) {
                        consumersForIntContains = (java.util.ArrayList) listeners.clone();
                    }
                    for (java.util.Iterator iter = consumersForIntContains.iterator(); iter.hasNext(); ) {
                        CalendarClockDescriptionListener listener = (CalendarClockDescriptionListener) iter.next();
                        listener.intContainsAdded(org.marcont.services.definitions.timeEntry.CalendarClockDescriptionImpl.this, _intContains);
                    }
                }
            }
            return;
        }
        if (stmt.getPredicate().equals(afterProperty)) {
            if (!stmt.getObject().canAs(com.hp.hpl.jena.rdf.model.Resource.class)) return;
            com.hp.hpl.jena.rdf.model.Resource resource = (com.hp.hpl.jena.rdf.model.Resource) stmt.getObject().as(com.hp.hpl.jena.rdf.model.Resource.class);
            if (true) {
                com.ibm.adtech.jastor.Thing _after = null;
                try {
                    _after = com.ibm.adtech.jastor.ThingFactory.getThing(resource, _model);
                } catch (JastorException e) {
                }
                if (after == null) {
                    try {
                        initAfter();
                    } catch (JastorException e) {
                        e.printStackTrace();
                        return;
                    }
                }
                if (!after.contains(_after)) after.add(_after);
                if (listeners != null) {
                    java.util.ArrayList consumersForAfter;
                    synchronized (listeners) {
                        consumersForAfter = (java.util.ArrayList) listeners.clone();
                    }
                    for (java.util.Iterator iter = consumersForAfter.iterator(); iter.hasNext(); ) {
                        CalendarClockDescriptionListener listener = (CalendarClockDescriptionListener) iter.next();
                        listener.afterAdded(org.marcont.services.definitions.timeEntry.CalendarClockDescriptionImpl.this, _after);
                    }
                }
            }
            return;
        }
        if (stmt.getPredicate().equals(intStartedByProperty)) {
            if (!stmt.getObject().canAs(com.hp.hpl.jena.rdf.model.Resource.class)) return;
            com.hp.hpl.jena.rdf.model.Resource resource = (com.hp.hpl.jena.rdf.model.Resource) stmt.getObject().as(com.hp.hpl.jena.rdf.model.Resource.class);
            if (true) {
                com.ibm.adtech.jastor.Thing _intStartedBy = null;
                try {
                    _intStartedBy = com.ibm.adtech.jastor.ThingFactory.getThing(resource, _model);
                } catch (JastorException e) {
                }
                if (intStartedBy == null) {
                    try {
                        initIntStartedBy();
                    } catch (JastorException e) {
                        e.printStackTrace();
                        return;
                    }
                }
                if (!intStartedBy.contains(_intStartedBy)) intStartedBy.add(_intStartedBy);
                if (listeners != null) {
                    java.util.ArrayList consumersForIntStartedBy;
                    synchronized (listeners) {
                        consumersForIntStartedBy = (java.util.ArrayList) listeners.clone();
                    }
                    for (java.util.Iterator iter = consumersForIntStartedBy.iterator(); iter.hasNext(); ) {
                        CalendarClockDescriptionListener listener = (CalendarClockDescriptionListener) iter.next();
                        listener.intStartedByAdded(org.marcont.services.definitions.timeEntry.CalendarClockDescriptionImpl.this, _intStartedBy);
                    }
                }
            }
            return;
        }
        if (stmt.getPredicate().equals(intFinishedByProperty)) {
            if (!stmt.getObject().canAs(com.hp.hpl.jena.rdf.model.Resource.class)) return;
            com.hp.hpl.jena.rdf.model.Resource resource = (com.hp.hpl.jena.rdf.model.Resource) stmt.getObject().as(com.hp.hpl.jena.rdf.model.Resource.class);
            if (true) {
                com.ibm.adtech.jastor.Thing _intFinishedBy = null;
                try {
                    _intFinishedBy = com.ibm.adtech.jastor.ThingFactory.getThing(resource, _model);
                } catch (JastorException e) {
                }
                if (intFinishedBy == null) {
                    try {
                        initIntFinishedBy();
                    } catch (JastorException e) {
                        e.printStackTrace();
                        return;
                    }
                }
                if (!intFinishedBy.contains(_intFinishedBy)) intFinishedBy.add(_intFinishedBy);
                if (listeners != null) {
                    java.util.ArrayList consumersForIntFinishedBy;
                    synchronized (listeners) {
                        consumersForIntFinishedBy = (java.util.ArrayList) listeners.clone();
                    }
                    for (java.util.Iterator iter = consumersForIntFinishedBy.iterator(); iter.hasNext(); ) {
                        CalendarClockDescriptionListener listener = (CalendarClockDescriptionListener) iter.next();
                        listener.intFinishedByAdded(org.marcont.services.definitions.timeEntry.CalendarClockDescriptionImpl.this, _intFinishedBy);
                    }
                }
            }
            return;
        }
        if (stmt.getPredicate().equals(intOverlappedByProperty)) {
            if (!stmt.getObject().canAs(com.hp.hpl.jena.rdf.model.Resource.class)) return;
            com.hp.hpl.jena.rdf.model.Resource resource = (com.hp.hpl.jena.rdf.model.Resource) stmt.getObject().as(com.hp.hpl.jena.rdf.model.Resource.class);
            if (true) {
                com.ibm.adtech.jastor.Thing _intOverlappedBy = null;
                try {
                    _intOverlappedBy = com.ibm.adtech.jastor.ThingFactory.getThing(resource, _model);
                } catch (JastorException e) {
                }
                if (intOverlappedBy == null) {
                    try {
                        initIntOverlappedBy();
                    } catch (JastorException e) {
                        e.printStackTrace();
                        return;
                    }
                }
                if (!intOverlappedBy.contains(_intOverlappedBy)) intOverlappedBy.add(_intOverlappedBy);
                if (listeners != null) {
                    java.util.ArrayList consumersForIntOverlappedBy;
                    synchronized (listeners) {
                        consumersForIntOverlappedBy = (java.util.ArrayList) listeners.clone();
                    }
                    for (java.util.Iterator iter = consumersForIntOverlappedBy.iterator(); iter.hasNext(); ) {
                        CalendarClockDescriptionListener listener = (CalendarClockDescriptionListener) iter.next();
                        listener.intOverlappedByAdded(org.marcont.services.definitions.timeEntry.CalendarClockDescriptionImpl.this, _intOverlappedBy);
                    }
                }
            }
            return;
        }
    }

    public void removedStatement(com.hp.hpl.jena.rdf.model.Statement stmt) {
        if (stmt.getPredicate().equals(minuteProperty)) {
            if (!stmt.getObject().canAs(com.hp.hpl.jena.rdf.model.Literal.class)) return;
            com.hp.hpl.jena.rdf.model.Literal literal = (com.hp.hpl.jena.rdf.model.Literal) stmt.getObject().as(com.hp.hpl.jena.rdf.model.Literal.class);
            Object obj = Util.fixLiteral(true, literal, "java.lang.String", "http://www.w3.org/2001/XMLSchema#nonNegativeInteger");
            if (minute != null && minute.equals(obj)) minute = null;
            if (listeners != null) {
                java.util.ArrayList consumers;
                synchronized (listeners) {
                    consumers = (java.util.ArrayList) listeners.clone();
                }
                for (java.util.Iterator iter = consumers.iterator(); iter.hasNext(); ) {
                    CalendarClockDescriptionListener listener = (CalendarClockDescriptionListener) iter.next();
                    listener.minuteChanged(org.marcont.services.definitions.timeEntry.CalendarClockDescriptionImpl.this);
                }
            }
            return;
        }
        if (stmt.getPredicate().equals(dayOfWeekFieldProperty)) {
            if (!stmt.getObject().canAs(com.hp.hpl.jena.rdf.model.Literal.class)) return;
            com.hp.hpl.jena.rdf.model.Literal literal = (com.hp.hpl.jena.rdf.model.Literal) stmt.getObject().as(com.hp.hpl.jena.rdf.model.Literal.class);
            Object obj = Util.fixLiteral(true, literal, "java.lang.String", "http://www.w3.org/2001/XMLSchema#nonNegativeInteger");
            if (dayOfWeekField != null && dayOfWeekField.equals(obj)) dayOfWeekField = null;
            if (listeners != null) {
                java.util.ArrayList consumers;
                synchronized (listeners) {
                    consumers = (java.util.ArrayList) listeners.clone();
                }
                for (java.util.Iterator iter = consumers.iterator(); iter.hasNext(); ) {
                    CalendarClockDescriptionListener listener = (CalendarClockDescriptionListener) iter.next();
                    listener.dayOfWeekFieldChanged(org.marcont.services.definitions.timeEntry.CalendarClockDescriptionImpl.this);
                }
            }
            return;
        }
        if (stmt.getPredicate().equals(timeZoneProperty)) {
            if (!stmt.getObject().canAs(com.hp.hpl.jena.rdf.model.Resource.class)) return;
            com.hp.hpl.jena.rdf.model.Resource resource = (com.hp.hpl.jena.rdf.model.Resource) stmt.getObject().as(com.hp.hpl.jena.rdf.model.Resource.class);
            if (timeZone != null && timeZone.resource().equals(resource)) timeZone = null;
            if (listeners != null) {
                java.util.ArrayList consumers;
                synchronized (listeners) {
                    consumers = (java.util.ArrayList) listeners.clone();
                }
                for (java.util.Iterator iter = consumers.iterator(); iter.hasNext(); ) {
                    CalendarClockDescriptionListener listener = (CalendarClockDescriptionListener) iter.next();
                    listener.timeZoneChanged(org.marcont.services.definitions.timeEntry.CalendarClockDescriptionImpl.this);
                }
            }
            return;
        }
        if (stmt.getPredicate().equals(secondProperty)) {
            if (!stmt.getObject().canAs(com.hp.hpl.jena.rdf.model.Literal.class)) return;
            com.hp.hpl.jena.rdf.model.Literal literal = (com.hp.hpl.jena.rdf.model.Literal) stmt.getObject().as(com.hp.hpl.jena.rdf.model.Literal.class);
            Object obj = Util.fixLiteral(true, literal, "java.math.BigDecimal", "http://www.w3.org/2001/XMLSchema#decimal");
            if (second != null && second.equals(obj)) second = null;
            if (listeners != null) {
                java.util.ArrayList consumers;
                synchronized (listeners) {
                    consumers = (java.util.ArrayList) listeners.clone();
                }
                for (java.util.Iterator iter = consumers.iterator(); iter.hasNext(); ) {
                    CalendarClockDescriptionListener listener = (CalendarClockDescriptionListener) iter.next();
                    listener.secondChanged(org.marcont.services.definitions.timeEntry.CalendarClockDescriptionImpl.this);
                }
            }
            return;
        }
        if (stmt.getPredicate().equals(dayOfYearFieldProperty)) {
            if (!stmt.getObject().canAs(com.hp.hpl.jena.rdf.model.Literal.class)) return;
            com.hp.hpl.jena.rdf.model.Literal literal = (com.hp.hpl.jena.rdf.model.Literal) stmt.getObject().as(com.hp.hpl.jena.rdf.model.Literal.class);
            Object obj = Util.fixLiteral(true, literal, "java.lang.String", "http://www.w3.org/2001/XMLSchema#nonNegativeInteger");
            if (dayOfYearField != null && dayOfYearField.equals(obj)) dayOfYearField = null;
            if (listeners != null) {
                java.util.ArrayList consumers;
                synchronized (listeners) {
                    consumers = (java.util.ArrayList) listeners.clone();
                }
                for (java.util.Iterator iter = consumers.iterator(); iter.hasNext(); ) {
                    CalendarClockDescriptionListener listener = (CalendarClockDescriptionListener) iter.next();
                    listener.dayOfYearFieldChanged(org.marcont.services.definitions.timeEntry.CalendarClockDescriptionImpl.this);
                }
            }
            return;
        }
        if (stmt.getPredicate().equals(dayProperty)) {
            if (!stmt.getObject().canAs(com.hp.hpl.jena.rdf.model.Literal.class)) return;
            com.hp.hpl.jena.rdf.model.Literal literal = (com.hp.hpl.jena.rdf.model.Literal) stmt.getObject().as(com.hp.hpl.jena.rdf.model.Literal.class);
            Object obj = Util.fixLiteral(true, literal, "java.lang.String", "http://www.w3.org/2001/XMLSchema#gDay");
            if (day != null && day.equals(obj)) day = null;
            if (listeners != null) {
                java.util.ArrayList consumers;
                synchronized (listeners) {
                    consumers = (java.util.ArrayList) listeners.clone();
                }
                for (java.util.Iterator iter = consumers.iterator(); iter.hasNext(); ) {
                    CalendarClockDescriptionListener listener = (CalendarClockDescriptionListener) iter.next();
                    listener.dayChanged(org.marcont.services.definitions.timeEntry.CalendarClockDescriptionImpl.this);
                }
            }
            return;
        }
        if (stmt.getPredicate().equals(weekProperty)) {
            if (!stmt.getObject().canAs(com.hp.hpl.jena.rdf.model.Literal.class)) return;
            com.hp.hpl.jena.rdf.model.Literal literal = (com.hp.hpl.jena.rdf.model.Literal) stmt.getObject().as(com.hp.hpl.jena.rdf.model.Literal.class);
            Object obj = Util.fixLiteral(true, literal, "java.lang.String", "http://www.w3.org/2001/XMLSchema#nonNegativeInteger");
            if (week != null && week.equals(obj)) week = null;
            if (listeners != null) {
                java.util.ArrayList consumers;
                synchronized (listeners) {
                    consumers = (java.util.ArrayList) listeners.clone();
                }
                for (java.util.Iterator iter = consumers.iterator(); iter.hasNext(); ) {
                    CalendarClockDescriptionListener listener = (CalendarClockDescriptionListener) iter.next();
                    listener.weekChanged(org.marcont.services.definitions.timeEntry.CalendarClockDescriptionImpl.this);
                }
            }
            return;
        }
        if (stmt.getPredicate().equals(hourProperty)) {
            if (!stmt.getObject().canAs(com.hp.hpl.jena.rdf.model.Literal.class)) return;
            com.hp.hpl.jena.rdf.model.Literal literal = (com.hp.hpl.jena.rdf.model.Literal) stmt.getObject().as(com.hp.hpl.jena.rdf.model.Literal.class);
            Object obj = Util.fixLiteral(true, literal, "java.lang.String", "http://www.w3.org/2001/XMLSchema#nonNegativeInteger");
            if (hour != null && hour.equals(obj)) hour = null;
            if (listeners != null) {
                java.util.ArrayList consumers;
                synchronized (listeners) {
                    consumers = (java.util.ArrayList) listeners.clone();
                }
                for (java.util.Iterator iter = consumers.iterator(); iter.hasNext(); ) {
                    CalendarClockDescriptionListener listener = (CalendarClockDescriptionListener) iter.next();
                    listener.hourChanged(org.marcont.services.definitions.timeEntry.CalendarClockDescriptionImpl.this);
                }
            }
            return;
        }
        if (stmt.getPredicate().equals(monthProperty)) {
            if (!stmt.getObject().canAs(com.hp.hpl.jena.rdf.model.Literal.class)) return;
            com.hp.hpl.jena.rdf.model.Literal literal = (com.hp.hpl.jena.rdf.model.Literal) stmt.getObject().as(com.hp.hpl.jena.rdf.model.Literal.class);
            Object obj = Util.fixLiteral(true, literal, "java.lang.String", "http://www.w3.org/2001/XMLSchema#gMonth");
            if (month != null && month.equals(obj)) month = null;
            if (listeners != null) {
                java.util.ArrayList consumers;
                synchronized (listeners) {
                    consumers = (java.util.ArrayList) listeners.clone();
                }
                for (java.util.Iterator iter = consumers.iterator(); iter.hasNext(); ) {
                    CalendarClockDescriptionListener listener = (CalendarClockDescriptionListener) iter.next();
                    listener.monthChanged(org.marcont.services.definitions.timeEntry.CalendarClockDescriptionImpl.this);
                }
            }
            return;
        }
        if (stmt.getPredicate().equals(unitTypeProperty)) {
            if (!stmt.getObject().canAs(com.hp.hpl.jena.rdf.model.Resource.class)) return;
            com.hp.hpl.jena.rdf.model.Resource resource = (com.hp.hpl.jena.rdf.model.Resource) stmt.getObject().as(com.hp.hpl.jena.rdf.model.Resource.class);
            if (unitType != null && unitType.resource().equals(resource)) unitType = null;
            if (listeners != null) {
                java.util.ArrayList consumers;
                synchronized (listeners) {
                    consumers = (java.util.ArrayList) listeners.clone();
                }
                for (java.util.Iterator iter = consumers.iterator(); iter.hasNext(); ) {
                    CalendarClockDescriptionListener listener = (CalendarClockDescriptionListener) iter.next();
                    listener.unitTypeChanged(org.marcont.services.definitions.timeEntry.CalendarClockDescriptionImpl.this);
                }
            }
            return;
        }
        if (stmt.getPredicate().equals(yearProperty)) {
            if (!stmt.getObject().canAs(com.hp.hpl.jena.rdf.model.Literal.class)) return;
            com.hp.hpl.jena.rdf.model.Literal literal = (com.hp.hpl.jena.rdf.model.Literal) stmt.getObject().as(com.hp.hpl.jena.rdf.model.Literal.class);
            Object obj = Util.fixLiteral(true, literal, "java.lang.String", "http://www.w3.org/2001/XMLSchema#gYear");
            if (year != null && year.equals(obj)) year = null;
            if (listeners != null) {
                java.util.ArrayList consumers;
                synchronized (listeners) {
                    consumers = (java.util.ArrayList) listeners.clone();
                }
                for (java.util.Iterator iter = consumers.iterator(); iter.hasNext(); ) {
                    CalendarClockDescriptionListener listener = (CalendarClockDescriptionListener) iter.next();
                    listener.yearChanged(org.marcont.services.definitions.timeEntry.CalendarClockDescriptionImpl.this);
                }
            }
            return;
        }
        if (stmt.getPredicate().equals(intContainsProperty)) {
            if (!stmt.getObject().canAs(com.hp.hpl.jena.rdf.model.Resource.class)) return;
            com.hp.hpl.jena.rdf.model.Resource resource = (com.hp.hpl.jena.rdf.model.Resource) stmt.getObject().as(com.hp.hpl.jena.rdf.model.Resource.class);
            if (true) {
                com.ibm.adtech.jastor.Thing _intContains = null;
                if (intContains != null) {
                    boolean found = false;
                    for (int i = 0; i < intContains.size(); i++) {
                        com.ibm.adtech.jastor.Thing __item = (com.ibm.adtech.jastor.Thing) intContains.get(i);
                        if (__item.resource().equals(resource)) {
                            found = true;
                            _intContains = __item;
                            break;
                        }
                    }
                    if (found) intContains.remove(_intContains); else {
                        try {
                            _intContains = com.ibm.adtech.jastor.ThingFactory.getThing(resource, _model);
                        } catch (JastorException e) {
                        }
                    }
                } else {
                    try {
                        _intContains = com.ibm.adtech.jastor.ThingFactory.getThing(resource, _model);
                    } catch (JastorException e) {
                    }
                }
                if (listeners != null) {
                    java.util.ArrayList consumersForIntContains;
                    synchronized (listeners) {
                        consumersForIntContains = (java.util.ArrayList) listeners.clone();
                    }
                    for (java.util.Iterator iter = consumersForIntContains.iterator(); iter.hasNext(); ) {
                        CalendarClockDescriptionListener listener = (CalendarClockDescriptionListener) iter.next();
                        listener.intContainsRemoved(org.marcont.services.definitions.timeEntry.CalendarClockDescriptionImpl.this, _intContains);
                    }
                }
            }
            return;
        }
        if (stmt.getPredicate().equals(afterProperty)) {
            if (!stmt.getObject().canAs(com.hp.hpl.jena.rdf.model.Resource.class)) return;
            com.hp.hpl.jena.rdf.model.Resource resource = (com.hp.hpl.jena.rdf.model.Resource) stmt.getObject().as(com.hp.hpl.jena.rdf.model.Resource.class);
            if (true) {
                com.ibm.adtech.jastor.Thing _after = null;
                if (after != null) {
                    boolean found = false;
                    for (int i = 0; i < after.size(); i++) {
                        com.ibm.adtech.jastor.Thing __item = (com.ibm.adtech.jastor.Thing) after.get(i);
                        if (__item.resource().equals(resource)) {
                            found = true;
                            _after = __item;
                            break;
                        }
                    }
                    if (found) after.remove(_after); else {
                        try {
                            _after = com.ibm.adtech.jastor.ThingFactory.getThing(resource, _model);
                        } catch (JastorException e) {
                        }
                    }
                } else {
                    try {
                        _after = com.ibm.adtech.jastor.ThingFactory.getThing(resource, _model);
                    } catch (JastorException e) {
                    }
                }
                if (listeners != null) {
                    java.util.ArrayList consumersForAfter;
                    synchronized (listeners) {
                        consumersForAfter = (java.util.ArrayList) listeners.clone();
                    }
                    for (java.util.Iterator iter = consumersForAfter.iterator(); iter.hasNext(); ) {
                        CalendarClockDescriptionListener listener = (CalendarClockDescriptionListener) iter.next();
                        listener.afterRemoved(org.marcont.services.definitions.timeEntry.CalendarClockDescriptionImpl.this, _after);
                    }
                }
            }
            return;
        }
        if (stmt.getPredicate().equals(intStartedByProperty)) {
            if (!stmt.getObject().canAs(com.hp.hpl.jena.rdf.model.Resource.class)) return;
            com.hp.hpl.jena.rdf.model.Resource resource = (com.hp.hpl.jena.rdf.model.Resource) stmt.getObject().as(com.hp.hpl.jena.rdf.model.Resource.class);
            if (true) {
                com.ibm.adtech.jastor.Thing _intStartedBy = null;
                if (intStartedBy != null) {
                    boolean found = false;
                    for (int i = 0; i < intStartedBy.size(); i++) {
                        com.ibm.adtech.jastor.Thing __item = (com.ibm.adtech.jastor.Thing) intStartedBy.get(i);
                        if (__item.resource().equals(resource)) {
                            found = true;
                            _intStartedBy = __item;
                            break;
                        }
                    }
                    if (found) intStartedBy.remove(_intStartedBy); else {
                        try {
                            _intStartedBy = com.ibm.adtech.jastor.ThingFactory.getThing(resource, _model);
                        } catch (JastorException e) {
                        }
                    }
                } else {
                    try {
                        _intStartedBy = com.ibm.adtech.jastor.ThingFactory.getThing(resource, _model);
                    } catch (JastorException e) {
                    }
                }
                if (listeners != null) {
                    java.util.ArrayList consumersForIntStartedBy;
                    synchronized (listeners) {
                        consumersForIntStartedBy = (java.util.ArrayList) listeners.clone();
                    }
                    for (java.util.Iterator iter = consumersForIntStartedBy.iterator(); iter.hasNext(); ) {
                        CalendarClockDescriptionListener listener = (CalendarClockDescriptionListener) iter.next();
                        listener.intStartedByRemoved(org.marcont.services.definitions.timeEntry.CalendarClockDescriptionImpl.this, _intStartedBy);
                    }
                }
            }
            return;
        }
        if (stmt.getPredicate().equals(intFinishedByProperty)) {
            if (!stmt.getObject().canAs(com.hp.hpl.jena.rdf.model.Resource.class)) return;
            com.hp.hpl.jena.rdf.model.Resource resource = (com.hp.hpl.jena.rdf.model.Resource) stmt.getObject().as(com.hp.hpl.jena.rdf.model.Resource.class);
            if (true) {
                com.ibm.adtech.jastor.Thing _intFinishedBy = null;
                if (intFinishedBy != null) {
                    boolean found = false;
                    for (int i = 0; i < intFinishedBy.size(); i++) {
                        com.ibm.adtech.jastor.Thing __item = (com.ibm.adtech.jastor.Thing) intFinishedBy.get(i);
                        if (__item.resource().equals(resource)) {
                            found = true;
                            _intFinishedBy = __item;
                            break;
                        }
                    }
                    if (found) intFinishedBy.remove(_intFinishedBy); else {
                        try {
                            _intFinishedBy = com.ibm.adtech.jastor.ThingFactory.getThing(resource, _model);
                        } catch (JastorException e) {
                        }
                    }
                } else {
                    try {
                        _intFinishedBy = com.ibm.adtech.jastor.ThingFactory.getThing(resource, _model);
                    } catch (JastorException e) {
                    }
                }
                if (listeners != null) {
                    java.util.ArrayList consumersForIntFinishedBy;
                    synchronized (listeners) {
                        consumersForIntFinishedBy = (java.util.ArrayList) listeners.clone();
                    }
                    for (java.util.Iterator iter = consumersForIntFinishedBy.iterator(); iter.hasNext(); ) {
                        CalendarClockDescriptionListener listener = (CalendarClockDescriptionListener) iter.next();
                        listener.intFinishedByRemoved(org.marcont.services.definitions.timeEntry.CalendarClockDescriptionImpl.this, _intFinishedBy);
                    }
                }
            }
            return;
        }
        if (stmt.getPredicate().equals(intOverlappedByProperty)) {
            if (!stmt.getObject().canAs(com.hp.hpl.jena.rdf.model.Resource.class)) return;
            com.hp.hpl.jena.rdf.model.Resource resource = (com.hp.hpl.jena.rdf.model.Resource) stmt.getObject().as(com.hp.hpl.jena.rdf.model.Resource.class);
            if (true) {
                com.ibm.adtech.jastor.Thing _intOverlappedBy = null;
                if (intOverlappedBy != null) {
                    boolean found = false;
                    for (int i = 0; i < intOverlappedBy.size(); i++) {
                        com.ibm.adtech.jastor.Thing __item = (com.ibm.adtech.jastor.Thing) intOverlappedBy.get(i);
                        if (__item.resource().equals(resource)) {
                            found = true;
                            _intOverlappedBy = __item;
                            break;
                        }
                    }
                    if (found) intOverlappedBy.remove(_intOverlappedBy); else {
                        try {
                            _intOverlappedBy = com.ibm.adtech.jastor.ThingFactory.getThing(resource, _model);
                        } catch (JastorException e) {
                        }
                    }
                } else {
                    try {
                        _intOverlappedBy = com.ibm.adtech.jastor.ThingFactory.getThing(resource, _model);
                    } catch (JastorException e) {
                    }
                }
                if (listeners != null) {
                    java.util.ArrayList consumersForIntOverlappedBy;
                    synchronized (listeners) {
                        consumersForIntOverlappedBy = (java.util.ArrayList) listeners.clone();
                    }
                    for (java.util.Iterator iter = consumersForIntOverlappedBy.iterator(); iter.hasNext(); ) {
                        CalendarClockDescriptionListener listener = (CalendarClockDescriptionListener) iter.next();
                        listener.intOverlappedByRemoved(org.marcont.services.definitions.timeEntry.CalendarClockDescriptionImpl.this, _intOverlappedBy);
                    }
                }
            }
            return;
        }
    }
}
