package com.dotmarketing.portlets.calendar.business;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.ParseException;
import com.dotmarketing.beans.Permission;
import com.dotmarketing.business.APILocator;
import com.dotmarketing.business.CacheLocator;
import com.dotmarketing.business.FactoryLocator;
import com.dotmarketing.cache.FieldsCache;
import com.dotmarketing.cache.StructureCache;
import com.dotmarketing.exception.DotDataException;
import com.dotmarketing.exception.DotSecurityException;
import com.dotmarketing.factories.InodeFactory;
import com.dotmarketing.portlets.calendar.model.Event;
import com.dotmarketing.portlets.calendar.model.EventRecurrence;
import com.dotmarketing.portlets.categories.model.Category;
import com.dotmarketing.portlets.contentlet.business.ContentletAPI;
import com.dotmarketing.portlets.contentlet.business.DotContentletStateException;
import com.dotmarketing.portlets.contentlet.model.Contentlet;
import com.dotmarketing.portlets.entities.model.Entity;
import com.dotmarketing.portlets.languagesmanager.factories.LanguageFactory;
import com.dotmarketing.portlets.languagesmanager.model.Language;
import com.dotmarketing.portlets.structure.factories.FieldFactory;
import com.dotmarketing.portlets.structure.factories.RelationshipFactory;
import com.dotmarketing.portlets.structure.factories.StructureFactory;
import com.dotmarketing.portlets.structure.model.Field;
import com.dotmarketing.portlets.structure.model.Relationship;
import com.dotmarketing.portlets.structure.model.Structure;
import com.dotmarketing.portlets.structure.model.Field.DataType;
import com.dotmarketing.portlets.structure.model.Field.FieldType;
import com.dotmarketing.services.StructureServices;
import com.dotmarketing.util.LuceneHits;
import com.dotmarketing.util.UtilMethods;
import com.dotmarketing.util.WebKeys;
import com.liferay.portal.model.User;

public class EventFactoryImpl extends EventFactory {

    private ContentletAPI conAPI = APILocator.getContentletAPI();

    /**
	 * Search implemented using lucene
	 * @throws DotSecurityException 
	 * @throws NumberFormatException 
	 */
    @Override
    protected List<Event> find(Date fromDate, Date toDate, String[] tags, String[] keywords, List<Category> categories, boolean liveOnly, boolean includeArchived, int offset, int limit, User user, boolean respectFrontendRoles) throws DotDataException, NumberFormatException, DotSecurityException {
        if (keywords == null) keywords = new String[0];
        if (tags == null) tags = new String[0];
        Structure eventStructure = getEventStructure();
        Field startDateF = eventStructure.getFieldVar("startDate");
        Field endDateF = eventStructure.getFieldVar("endDate");
        Field titleF = eventStructure.getFieldVar("title");
        Field descriptionF = eventStructure.getFieldVar("description");
        Field tagsF = eventStructure.getFieldVar("tags");
        String fromDateQuery = new SimpleDateFormat("yyyyMMddHHmmss").format(fromDate);
        String toDateQuery = new SimpleDateFormat("yyyyMMddHHmmss").format(toDate);
        StringBuffer query = new StringBuffer("+type:content +structureInode:" + eventStructure.getInode() + " +" + startDateF.getFieldContentlet() + ":[19000101000000" + " TO " + toDateQuery + "] " + " +" + endDateF.getFieldContentlet() + ":[" + fromDateQuery + " TO 30000101000000] ");
        if (liveOnly) query.append(" +live:true"); else if (includeArchived) query.append(" +(working:true deleted:true)"); else query.append(" +working:true +deleted:false");
        for (String keyword : keywords) {
            if (UtilMethods.isSet(keyword)) {
                query.append(" +(" + titleF.getFieldContentlet() + ": " + keyword.replaceAll("\"", "") + "* " + descriptionF.getFieldContentlet() + ": " + keyword.replaceAll("\"", "") + "* " + tagsF.getFieldContentlet() + ": " + keyword.replaceAll("\"", "") + "*)");
            }
        }
        for (String tag : tags) {
            if (UtilMethods.isSet(tag)) {
                query.append(" +(" + tagsF.getFieldContentlet() + ":" + tag.replaceAll("\"", "").replaceAll(":", "") + "*)");
            }
        }
        if (categories != null) {
            query.append(categoriesQueryFilter(categories));
        }
        return findInLucene(query.toString(), liveOnly, offset, limit, user, respectFrontendRoles);
    }

    @Override
    protected List<Event> find(Date fromDate, String[] tags, String keyword, List<Category> categories, boolean liveOnly, int start, int limit, User user, boolean respectFrontendRoles) throws DotDataException, NumberFormatException, DotSecurityException {
        Structure eventStructure = getEventStructure();
        Field startDateF = eventStructure.getFieldVar("startDate");
        Field titleF = eventStructure.getFieldVar("title");
        Field descriptionF = eventStructure.getFieldVar("description");
        Field tagsF = eventStructure.getFieldVar("tags");
        String fromDateQuery = new SimpleDateFormat("yyyyMMdd").format(fromDate) + "000000";
        String toDateQuery = "99991231000000";
        StringBuffer query = new StringBuffer("+type:content +deleted:false +structureInode:" + eventStructure.getInode() + " " + startDateF.getFieldContentlet() + ":[" + fromDateQuery + " TO " + toDateQuery + "]" + (liveOnly ? " +live:true" : " +working:true"));
        if (UtilMethods.isSet(keyword)) {
            query.append(" +(" + titleF.getFieldContentlet() + ": " + keyword + "* " + descriptionF.getFieldContentlet() + ": " + keyword + "* " + tagsF.getFieldContentlet() + ": " + keyword + "*)");
        }
        if (categories != null) {
            query.append(categoriesQueryFilter(categories));
        }
        return findInLucene(query.toString(), liveOnly, start, limit, user, respectFrontendRoles);
    }

    private String categoriesQueryFilter(List<Category> categories) {
        StringBuffer luceneQuery = new StringBuffer();
        if (categories.size() > 0) {
            luceneQuery.append(" +(");
            for (Category cat : categories) {
                luceneQuery.append(" c" + cat.getInode() + "c:on ");
            }
            luceneQuery.append(") ");
        }
        return luceneQuery.toString();
    }

    @Override
    protected List<Event> find(Date fromDate, Date toDate, User owner, User user, boolean respectFrontendRoles) throws DotDataException, NumberFormatException, DotSecurityException {
        Structure eventStructure = getEventStructure();
        Field startDateF = eventStructure.getFieldVar("startDate");
        String fromDateQuery = new SimpleDateFormat("yyyyMMdd").format(fromDate) + "000000";
        String toDateQuery = new SimpleDateFormat("yyyyMMdd").format(toDate) + "000000";
        String query = "+type:content +deleted:false +structureInode:" + eventStructure.getInode() + " " + startDateF.getFieldContentlet() + ":[" + fromDateQuery + " TO " + toDateQuery + "] +working:true +owner:" + owner.getUserId();
        return findInLucene(query, false, -1, -1, user, respectFrontendRoles);
    }

    @Override
    protected Event find(long identifier, boolean live, User user, boolean respectFrontendRoles) throws DotDataException, DotSecurityException {
        Event ev = null;
        Language lang = LanguageFactory.getDefaultLanguage();
        Contentlet cont = conAPI.findContentletByIdentifier(identifier, live, lang.getId(), user, respectFrontendRoles);
        if (cont == null) return null;
        ev = convertToEvent(cont);
        return ev;
    }

    @Override
    protected void save(Event ev, User user) throws DotDataException, DotSecurityException {
        save(ev, null, null, user);
    }

    @Override
    protected void save(Event ev, List<Category> categories, List<Permission> permissions, User user) throws DotDataException, DotSecurityException {
        if (categories == null) {
            categories = new ArrayList<Category>();
        }
        if (permissions == null) {
            permissions = new ArrayList<Permission>();
        }
        boolean isNew = false;
        Language language = LanguageFactory.getDefaultLanguage();
        Structure eventStructure = StructureCache.getStructureByName("Event");
        Contentlet contentlet = new Contentlet();
        if (ev.getInode() > 0) contentlet = conAPI.find(ev.getInode(), user, true);
        if (contentlet.getInode() == 0) {
            contentlet.setLanguageId(language.getId());
            contentlet.setStructureInode(eventStructure.getInode());
            isNew = true;
        } else {
            contentlet = conAPI.checkout(contentlet.getInode(), user, true);
        }
        Map<String, Object> m = ev.getMap();
        m.remove("recurrence");
        conAPI.copyProperties(contentlet, m);
        if (isNew) {
            contentlet.setLive(ev.isLive());
            contentlet.setWorking(ev.isWorking());
            contentlet.setArchived(ev.isArchived());
        }
        contentlet = conAPI.checkin(contentlet, new HashMap<Relationship, List<Contentlet>>(), categories, permissions, user, true);
        ev.setInode(contentlet.getInode());
        ev.setIdentifier(contentlet.getIdentifier());
    }

    @Override
    protected void archive(Event event, User user, boolean respectFrontendRoles) throws DotDataException, DotSecurityException {
        Contentlet cont = new Contentlet();
        cont = conAPI.find(event.getInode(), user, respectFrontendRoles);
        try {
            conAPI.archive(cont, user, respectFrontendRoles);
        } catch (Exception e) {
            throw new DotDataException("Unable to archive the event.", e);
        }
    }

    @Override
    protected void delete(Event event, User user, boolean respectFrontendRoles) throws DotDataException, DotSecurityException {
        Contentlet cont = new Contentlet();
        cont = conAPI.find(event.getInode(), user, respectFrontendRoles);
        try {
            conAPI.delete(cont, user, respectFrontendRoles);
        } catch (Exception e) {
            throw new DotDataException("Unable to archive the event.", e);
        }
    }

    @Override
    protected void publish(Event event, User user, boolean respectFrontendRoles) throws DotDataException, DotSecurityException {
        Contentlet cont = new Contentlet();
        cont = conAPI.find(event.getInode(), user, respectFrontendRoles);
        try {
            conAPI.publish(cont, user, respectFrontendRoles);
        } catch (Exception e) {
            throw new DotDataException("Unable to archive the event.", e);
        }
    }

    @Override
    protected void unarchive(Event event, User user, boolean respectFrontendRoles) throws DotDataException, DotSecurityException {
        Contentlet cont = new Contentlet();
        cont = conAPI.find(event.getInode(), user, respectFrontendRoles);
        try {
            conAPI.unarchive(cont, user, respectFrontendRoles);
        } catch (Exception e) {
            throw new DotDataException("Unable to archive the event.", e);
        }
    }

    @Override
    protected void unpublish(Event event, User user, boolean respectFrontendRoles) throws DotDataException, DotSecurityException {
        Contentlet cont = new Contentlet();
        cont = conAPI.find(event.getInode(), user, respectFrontendRoles);
        try {
            conAPI.unpublish(cont, user, respectFrontendRoles);
        } catch (Exception e) {
            throw new DotDataException("Unable to unpublish the event.", e);
        }
    }

    /**
	 * Returns the event structure
	 */
    @Override
    protected Structure getBuildingStructure() {
        Structure eventStructure = StructureCache.getStructureByName(BUILDING_STRUCTURE_NAME);
        return eventStructure;
    }

    @Override
    protected Structure getEventStructure() {
        Structure eventStructure = StructureCache.getStructureByName(EVENT_STRUCTURE_NAME);
        return eventStructure;
    }

    @Override
    protected Structure getLocationStructure() {
        Structure eventStructure = StructureCache.getStructureByName(FACILITY_STRUCTURE_NAME);
        return eventStructure;
    }

    @Override
    protected Event reloadEvent(Event event, boolean live, User user, boolean respectFrontendRoles) throws DotDataException, DotSecurityException {
        return find(event.getIdentifier(), live, user, respectFrontendRoles);
    }

    @Override
    protected void addFileToEvent(Event event, Long fileId, String relationName, User currentUser, boolean respectFrontendRoles) throws DotSecurityException, DotDataException {
        Contentlet cont = conAPI.find(event.getInode(), currentUser, respectFrontendRoles);
        conAPI.addFileToContentlet(cont, fileId, relationName, currentUser, respectFrontendRoles);
    }

    private static final String EVENT_STRUCTURE_NAME = "Event";

    private static final String EVENT_STRUCTURE_DESCRIPTION = "Calendar Events";

    private static final String BUILDING_STRUCTURE_NAME = "Building";

    private static final String BUILDING_STRUCTURE_DESCRIPTION = "Buildings";

    private static final String FACILITY_STRUCTURE_NAME = "Facility";

    private static final String FACILITY_STRUCTURE_DESCRIPTION = "Facilities";

    static {
        initStructures();
    }

    @SuppressWarnings("deprecation")
    private static void initStructures() {
        Structure eventStructure = StructureCache.getStructureByName(EVENT_STRUCTURE_NAME);
        List<Field> fields = FieldsCache.getFieldsByStructureName(EVENT_STRUCTURE_NAME);
        boolean fieldAdded = false;
        if (eventStructure == null || eventStructure.getInode() == 0) {
            eventStructure = new Structure();
            eventStructure.setDefaultStructure(false);
            eventStructure.setName(EVENT_STRUCTURE_NAME);
            eventStructure.setDescription(EVENT_STRUCTURE_DESCRIPTION);
            eventStructure.setFixed(true);
            StructureFactory.saveStructure(eventStructure);
            Entity entity = new Entity();
            entity.setEntityName(eventStructure.getName());
            InodeFactory.saveInode(entity);
            eventStructure.addParent(entity, WebKeys.Structure.STRUCTURE_ENTITY);
        }
        Field title = null;
        Field description = null;
        Field startDate = null;
        Field endDate = null;
        Field link = null;
        Field location = null;
        Field image = null;
        Field file = null;
        Field tags = null;
        for (Field field : fields) {
            if (field.getVelocityVarName().equals("title")) {
                title = field;
            } else if (field.getVelocityVarName().equals("description")) {
                description = field;
            } else if (field.getVelocityVarName().equals("startDate")) {
                startDate = field;
            } else if (field.getVelocityVarName().equals("endDate")) {
                endDate = field;
            } else if (field.getVelocityVarName().equals("link")) {
                link = field;
            } else if (field.getVelocityVarName().equals("location")) {
                location = field;
            } else if (field.getVelocityVarName().equals("image")) {
                image = field;
            } else if (field.getVelocityVarName().equals("file")) {
                file = field;
            } else if (field.getVelocityVarName().equals("tags")) {
                tags = field;
            }
        }
        if (title == null) {
            title = new Field("Title", FieldType.TEXT, DataType.TEXT, eventStructure, true, true, true, 1, true, false, true);
            FieldFactory.saveField(title);
            fieldAdded = true;
        }
        if (description == null) {
            description = new Field("Description", FieldType.WYSIWYG, DataType.LONG_TEXT, eventStructure, false, false, true, 2, true, false, true);
            FieldFactory.saveField(description);
            fieldAdded = true;
        }
        if (startDate == null) {
            startDate = new Field("Start Date", FieldType.DATE_TIME, DataType.DATE, eventStructure, true, true, true, 3, true, false, true);
            FieldFactory.saveField(startDate);
            fieldAdded = true;
        }
        if (endDate == null) {
            endDate = new Field("End Date", FieldType.DATE_TIME, DataType.DATE, eventStructure, true, true, true, 4, true, false, true);
            FieldFactory.saveField(endDate);
            fieldAdded = true;
        }
        if (link == null) {
            link = new Field("Link", FieldType.TEXT, DataType.TEXT, eventStructure, false, false, false, 5, true, false, false);
            FieldFactory.saveField(link);
            fieldAdded = true;
        }
        if (location == null) {
            location = new Field("Location", FieldType.TEXT, DataType.TEXT, eventStructure, false, false, false, 6, true, false, true);
            FieldFactory.saveField(location);
            fieldAdded = true;
        }
        if (image == null) {
            image = new Field("Image", FieldType.IMAGE, DataType.INTEGER, eventStructure, false, false, false, 7, true, false, false);
            FieldFactory.saveField(image);
            fieldAdded = true;
        }
        if (file == null) {
            file = new Field("File", FieldType.FILE, DataType.INTEGER, eventStructure, false, false, false, 8, true, false, false);
            FieldFactory.saveField(file);
            fieldAdded = true;
        }
        if (tags == null) {
            tags = new Field("Tags", FieldType.TAG, DataType.TEXT, eventStructure, false, false, true, 9, true, false, true);
            FieldFactory.saveField(tags);
            fieldAdded = true;
        }
        if (fieldAdded) {
            FieldsCache.removeFields(eventStructure);
            StructureCache.removeStructure(eventStructure);
            StructureServices.removeStructureFile(eventStructure);
            StructureFactory.saveStructure(eventStructure);
        }
        initEventEventRelation(eventStructure);
        Structure buildingStructure = StructureCache.getStructureByName(BUILDING_STRUCTURE_NAME);
        fields = FieldsCache.getFieldsByStructureName(BUILDING_STRUCTURE_NAME);
        fieldAdded = false;
        if (buildingStructure == null || buildingStructure.getInode() == 0) {
            buildingStructure = new Structure();
            buildingStructure.setDefaultStructure(false);
            buildingStructure.setName(BUILDING_STRUCTURE_NAME);
            buildingStructure.setDescription(BUILDING_STRUCTURE_DESCRIPTION);
            buildingStructure.setFixed(true);
            buildingStructure.setStructureType(Structure.STRUCTURE_TYPE_CONTENT);
            StructureFactory.saveStructure(buildingStructure);
            Entity entity = new Entity();
            entity.setEntityName(buildingStructure.getName());
            InodeFactory.saveInode(entity);
            buildingStructure.addParent(entity, WebKeys.Structure.STRUCTURE_ENTITY);
        }
        Field buildingTitle = null;
        Field buildingType = null;
        Field buildingAddress = null;
        Field buildingShortDescription = null;
        Field buildingDescription = null;
        Field buildingPhoto1 = null;
        Field buildingPhoto2 = null;
        Field buildingPhoto3 = null;
        for (Field field : fields) {
            if (field.getVelocityVarName().equals("title")) {
                buildingTitle = field;
            } else if (field.getVelocityVarName().equals("type")) {
                buildingType = field;
            } else if (field.getVelocityVarName().equals("address")) {
                buildingAddress = field;
            } else if (field.getVelocityVarName().equals("shortDescription")) {
                buildingShortDescription = field;
            } else if (field.getVelocityVarName().equals("description")) {
                buildingDescription = field;
            } else if (field.getVelocityVarName().equals("photo1")) {
                buildingPhoto1 = field;
            } else if (field.getVelocityVarName().equals("photo2")) {
                buildingPhoto2 = field;
            } else if (field.getVelocityVarName().equals("photo3")) {
                buildingPhoto3 = field;
            }
        }
        if (buildingTitle == null) {
            buildingTitle = new Field("Title", FieldType.TEXT, DataType.TEXT, buildingStructure, true, true, true, 2, true, false, true);
            FieldFactory.saveField(buildingTitle);
            fieldAdded = true;
        }
        if (buildingType == null) {
            buildingType = new Field("Type", FieldType.CHECKBOX, DataType.TEXT, buildingStructure, true, true, true, 3, true, false, true);
            buildingType.setValues("Residence Hall|Residence Hall\r\nDining Hall|Dining Hall\r\nAcademic Department|Academic Department\r\nAdministrative Office|Administrative Office\r\nClassroom|Classroom\r\nComputer Lab|Computer Lab\r\nLecture Hall|Lecture Hall\r\nLibrary|Library\r\nScience/Research|Science/Research\r\nPerformance Space|Performance Space\r\nPractice/Rehearsal Space|Practice/Rehearsal Space\r\nMuseum/Gallery|Museum/Gallery\r\nStudio|Studio\r\nAthletic/Fitness|Athletic/Fitness\r\nRecreational|Recreational\r\nPublic Space|Public Space\r\nHistoric Site|Historic Site");
            FieldFactory.saveField(buildingType);
            fieldAdded = true;
        }
        if (buildingAddress == null) {
            buildingAddress = new Field("Address", FieldType.TEXT, DataType.TEXT, buildingStructure, true, false, true, 4, true, false, false);
            FieldFactory.saveField(buildingAddress);
            fieldAdded = true;
        }
        if (buildingShortDescription == null) {
            buildingShortDescription = new Field("Short Description", FieldType.TEXT_AREA, DataType.LONG_TEXT, buildingStructure, true, false, true, 5, true, false, true);
            FieldFactory.saveField(buildingShortDescription);
            fieldAdded = true;
        }
        if (buildingDescription == null) {
            buildingDescription = new Field("Description", FieldType.WYSIWYG, DataType.LONG_TEXT, buildingStructure, true, false, true, 6, true, false, false);
            FieldFactory.saveField(buildingDescription);
            fieldAdded = true;
        }
        if (buildingPhoto1 == null) {
            buildingPhoto1 = new Field("Photo 1", FieldType.IMAGE, DataType.INTEGER, buildingStructure, false, false, false, 7, true, false, false);
            FieldFactory.saveField(buildingPhoto1);
            fieldAdded = true;
        }
        if (buildingPhoto2 == null) {
            buildingPhoto2 = new Field("Photo 2", FieldType.IMAGE, DataType.INTEGER, buildingStructure, false, false, false, 8, true, false, false);
            FieldFactory.saveField(buildingPhoto2);
            fieldAdded = true;
        }
        if (buildingPhoto3 == null) {
            buildingPhoto3 = new Field("Photo 3", FieldType.IMAGE, DataType.INTEGER, buildingStructure, false, false, false, 9, true, false, false);
            FieldFactory.saveField(buildingPhoto3);
            fieldAdded = true;
        }
        if (fieldAdded) {
            FieldsCache.removeFields(buildingStructure);
            StructureCache.removeStructure(buildingStructure);
            StructureServices.removeStructureFile(buildingStructure);
            StructureFactory.saveStructure(buildingStructure);
        }
        Structure facilityStructure = StructureCache.getStructureByName(FACILITY_STRUCTURE_NAME);
        fields = FieldsCache.getFieldsByStructureName(FACILITY_STRUCTURE_NAME);
        fieldAdded = false;
        if (facilityStructure == null || facilityStructure.getInode() == 0) {
            facilityStructure = new Structure();
            facilityStructure.setDefaultStructure(false);
            facilityStructure.setName(FACILITY_STRUCTURE_NAME);
            facilityStructure.setDescription(FACILITY_STRUCTURE_DESCRIPTION);
            facilityStructure.setFixed(true);
            facilityStructure.setStructureType(Structure.STRUCTURE_TYPE_CONTENT);
            StructureFactory.saveStructure(facilityStructure);
            Entity entity = new Entity();
            entity.setEntityName(facilityStructure.getName());
            InodeFactory.saveInode(entity);
            facilityStructure.addParent(entity, WebKeys.Structure.STRUCTURE_ENTITY);
        }
        Field facilityTitle = null;
        Field facilityRoomId = null;
        Field facilityShortDescription = null;
        Field facilityDescription = null;
        Field facilityVideoStill = null;
        Field facilityVideo = null;
        Field facilityPhoto1 = null;
        Field facilityPhoto2 = null;
        Field facilityPhoto3 = null;
        for (Field field : fields) {
            if (field.getVelocityVarName().equals("title")) {
                facilityTitle = field;
            } else if (field.getVelocityVarName().equals("roomId")) {
                facilityRoomId = field;
            } else if (field.getVelocityVarName().equals("shortDescription")) {
                facilityShortDescription = field;
            } else if (field.getVelocityVarName().equals("description")) {
                facilityDescription = field;
            } else if (field.getVelocityVarName().equals("videoStill")) {
                facilityVideoStill = field;
            } else if (field.getVelocityVarName().equals("video")) {
                facilityVideo = field;
            } else if (field.getVelocityVarName().equals("photo1")) {
                facilityPhoto1 = field;
            } else if (field.getVelocityVarName().equals("photo2")) {
                facilityPhoto2 = field;
            } else if (field.getVelocityVarName().equals("photo3")) {
                facilityPhoto3 = field;
            }
        }
        if (facilityTitle == null) {
            facilityTitle = new Field("Title", FieldType.TEXT, DataType.TEXT, facilityStructure, true, true, true, 1, true, false, true);
            FieldFactory.saveField(facilityTitle);
            fieldAdded = true;
        }
        if (facilityRoomId == null) {
            facilityRoomId = new Field("Room ID", FieldType.TEXT, DataType.TEXT, facilityStructure, false, true, true, 2, true, false, true);
            FieldFactory.saveField(facilityRoomId);
            fieldAdded = true;
        }
        if (facilityShortDescription == null) {
            facilityShortDescription = new Field("Short Description", FieldType.TEXT_AREA, DataType.LONG_TEXT, facilityStructure, true, false, true, 3, true, false, true);
            FieldFactory.saveField(facilityShortDescription);
            fieldAdded = true;
        }
        if (facilityDescription == null) {
            facilityDescription = new Field("Description", FieldType.WYSIWYG, DataType.LONG_TEXT, facilityStructure, true, false, true, 4, true, false, false);
            FieldFactory.saveField(facilityDescription);
            fieldAdded = true;
        }
        if (facilityVideoStill == null) {
            facilityVideoStill = new Field("Video Still", FieldType.IMAGE, DataType.INTEGER, facilityStructure, false, false, false, 5, true, false, false);
            FieldFactory.saveField(facilityVideoStill);
            fieldAdded = true;
        }
        if (facilityVideo == null) {
            facilityVideo = new Field("Video", FieldType.FILE, DataType.INTEGER, facilityStructure, false, false, false, 6, true, false, false);
            FieldFactory.saveField(facilityVideo);
            fieldAdded = true;
        }
        if (facilityPhoto1 == null) {
            facilityPhoto1 = new Field("Photo 1", FieldType.IMAGE, DataType.INTEGER, facilityStructure, false, false, false, 7, true, false, false);
            FieldFactory.saveField(facilityPhoto1);
            fieldAdded = true;
        }
        if (facilityPhoto2 == null) {
            facilityPhoto2 = new Field("Photo 2", FieldType.IMAGE, DataType.INTEGER, facilityStructure, false, false, false, 8, true, false, false);
            FieldFactory.saveField(facilityPhoto2);
            fieldAdded = true;
        }
        if (facilityPhoto3 == null) {
            facilityPhoto3 = new Field("Photo 3", FieldType.IMAGE, DataType.INTEGER, facilityStructure, false, false, false, 9, true, false, false);
            FieldFactory.saveField(facilityPhoto3);
            fieldAdded = true;
        }
        if (fieldAdded) {
            FieldsCache.removeFields(facilityStructure);
            StructureCache.removeStructure(facilityStructure);
            StructureServices.removeStructureFile(facilityStructure);
            StructureFactory.saveStructure(facilityStructure);
        }
        initBuidlingFacilityRelation(buildingStructure, facilityStructure);
    }

    private static void initEventEventRelation(Structure eventStructure) {
        Relationship relationship = RelationshipFactory.getRelationshipByRelationTypeValue("Event-Event");
        if (relationship == null) {
            relationship = new Relationship();
            relationship.setCardinality(0);
            relationship.setChildRelationName("Event");
            relationship.setParentRelationName("Event");
            relationship.setChildStructureInode(eventStructure.getInode());
            relationship.setParentStructureInode(eventStructure.getInode());
            relationship.setRelationTypeValue("Event-Event");
            relationship.setParentRequired(false);
            relationship.setChildRequired(false);
            RelationshipFactory.saveRelationship(relationship);
        }
    }

    private static void initBuidlingFacilityRelation(Structure buildingStructure, Structure facilityStructure) {
        Relationship relationship = RelationshipFactory.getRelationshipByRelationTypeValue("Building-Facility");
        if (relationship == null || relationship.getInode() == 0) {
            relationship = new Relationship();
            relationship.setCardinality(0);
            relationship.setParentRelationName("Building");
            relationship.setChildRelationName("Facility");
            relationship.setParentStructureInode(buildingStructure.getInode());
            relationship.setChildStructureInode(facilityStructure.getInode());
            relationship.setRelationTypeValue("Building-Facility");
            relationship.setParentRequired(true);
            relationship.setChildRequired(false);
            RelationshipFactory.saveRelationship(relationship);
        }
    }

    private Event convertToEvent(Contentlet cont) throws DotDataException, DotContentletStateException, DotSecurityException {
        Event ev = new Event();
        ev.setStructureInode(getEventStructure().getInode());
        Map<String, Object> contentletMap = cont.getMap();
        conAPI.copyProperties(ev, contentletMap);
        EventRecurrenceFactory recurrenceFactory = FactoryLocator.getEventRecurrenceFactory();
        EventRecurrence recurrence = recurrenceFactory.findEventRecurrence(ev);
        ev.setRecurrence(recurrence);
        return ev;
    }

    private List<Event> findInLucene(String query, boolean liveOnly, int start, int limit, User user, boolean respectFrontendRoles) throws DotDataException, NumberFormatException, DotSecurityException {
        Structure eventStructure = getEventStructure();
        Field startDate = eventStructure.getFieldVar("startDate");
        new ArrayList<Event>();
        try {
            LuceneHits hits = conAPI.indexSearch(query, limit, start, startDate.getFieldContentlet(), user, respectFrontendRoles);
            List<Long> inodesList = new ArrayList<Long>();
            for (int i = 0; i < hits.length(); i++) {
                Document doc = hits.doc(i);
                inodesList.add(Long.parseLong(doc.get("inode")));
            }
            List<Event> events = find(inodesList);
            return events;
        } catch (ParseException e) {
            throw new DotDataException("Unable to search on lucene index. query = " + query, e);
        }
    }

    private List<Event> find(List<Long> inodesList) throws DotDataException, DotContentletStateException, DotSecurityException {
        Map<Long, Event> eventsMap = new HashMap<Long, Event>();
        List<Contentlet> conts = conAPI.findContentlets(inodesList);
        for (Contentlet con : conts) {
            eventsMap.put(con.getInode(), convertToEvent(con));
        }
        List<Event> events = new ArrayList<Event>();
        for (Long inode : inodesList) {
            events.add(eventsMap.get(inode));
        }
        return events;
    }
}
