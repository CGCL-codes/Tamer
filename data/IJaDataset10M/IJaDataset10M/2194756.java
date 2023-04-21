package org.kablink.teaming.module.file;

import java.text.ParseException;
import java.util.Date;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.document.DateTools;
import org.kablink.teaming.domain.EntityIdentifier.EntityType;
import org.kablink.util.search.Constants;

/**
 * This class represents those pieces of data associated with a specific file that
 * is stored in Lucene index. This class must not include any field or property 
 * that requires database lookup. It should only contain those fields that are
 * obtainable entirely from the Lucene index.
 *  
 * @author jong
 *
 */
public class FileIndexData {

    private static Log logger = LogFactory.getLog(FileIndexData.class);

    private String name;

    private String id;

    private String title;

    private Long binderId;

    private EntityType owningEntityType;

    private Long owningEntityId;

    private Long creatorId;

    private String creatorName;

    private Long modifierId;

    private String modifierName;

    private Date createdDate;

    private Date modifiedDate;

    public FileIndexData(org.apache.lucene.document.Document doc) throws IllegalArgumentException {
        name = doc.get(Constants.FILENAME_FIELD);
        id = doc.get(Constants.FILE_ID_FIELD);
        title = doc.get(Constants.TITLE_FIELD);
        binderId = Long.valueOf(doc.get(Constants.BINDER_ID_FIELD));
        String owningEntityTypeStr = doc.get(Constants.ENTITY_FIELD);
        owningEntityType = entityTypeFromString(owningEntityTypeStr);
        owningEntityId = Long.valueOf(doc.get(Constants.DOCID_FIELD));
        creatorId = Long.valueOf(doc.get(Constants.CREATORID_FIELD));
        creatorName = doc.get(Constants.CREATOR_NAME_FIELD);
        modifierId = Long.valueOf(doc.get(Constants.MODIFICATIONID_FIELD));
        modifierName = doc.get(Constants.MODIFICATION_NAME_FIELD);
        String dateStr = doc.get(Constants.CREATION_DATE_FIELD);
        try {
            createdDate = DateTools.stringToDate(dateStr);
        } catch (ParseException e) {
            logger.warn("Error parsing creation date [" + dateStr + "] for file [" + id + "]. Setting it to current date");
        }
        dateStr = doc.get(Constants.MODIFICATION_DATE_FIELD);
        try {
            modifiedDate = DateTools.stringToDate(dateStr);
        } catch (ParseException e) {
            logger.warn("Error parsing modification date [" + dateStr + "] for file [" + id + "]. Setting it to current date");
        }
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Long getBinderId() {
        return binderId;
    }

    public EntityType getOwningEntityType() {
        return owningEntityType;
    }

    public Long getOwningEntityId() {
        return owningEntityId;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public Long getModifierId() {
        return modifierId;
    }

    public String getModifierName() {
        return modifierName;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    private EntityType entityTypeFromString(String entityTypeStr) throws IllegalArgumentException {
        if (entityTypeStr == null) throw new IllegalArgumentException("Entity type is null");
        return EntityType.valueOf(entityTypeStr);
    }
}
