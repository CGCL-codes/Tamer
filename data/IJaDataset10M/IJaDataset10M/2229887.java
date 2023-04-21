package de.erdesignerng.model.serializer.repository;

import de.erdesignerng.model.Model;
import de.erdesignerng.model.Table;
import de.erdesignerng.model.serializer.repository.entities.ModelEntity;
import de.erdesignerng.model.serializer.repository.entities.RepositoryEntity;
import de.erdesignerng.model.serializer.repository.entities.TableEntity;
import org.hibernate.Session;
import java.util.Map;

/**
 * Serializer for tables.
 *
 * @author mirkosertic
 */
public class DictionaryTableSerializer extends DictionaryBaseSerializer {

    public static final DictionaryTableSerializer SERIALIZER = new DictionaryTableSerializer();

    public void serialize(Model aModel, Session aSession, RepositoryEntity aDictionary) {
        Map<String, ModelEntity> theTables = deletedRemovedInstances(aModel.getTables(), aDictionary.getTables());
        for (Table theTable : aModel.getTables()) {
            boolean existing = true;
            TableEntity theExisting = (TableEntity) theTables.get(theTable.getSystemId());
            if (theExisting == null) {
                theExisting = new TableEntity();
                existing = false;
            }
            copyBaseAttributes(theTable, theExisting);
            theExisting.setSchema(theTable.getSchema());
            DictionaryAttributeSerializer.SERIALIZER.serialize(theTable, theExisting);
            DictionaryIndexSerializer.SERIALIZER.serialize(theTable, theExisting);
            if (!existing) {
                aDictionary.getTables().add(theExisting);
            }
        }
    }

    public void deserialize(Model aModel, RepositoryEntity aRepositoryEntity) {
        for (TableEntity theTableEntity : aRepositoryEntity.getTables()) {
            Table theTable = new Table();
            theTable.setOwner(aModel);
            copyBaseAttributes(theTableEntity, theTable);
            theTable.setSchema(theTableEntity.getSchema());
            DictionaryAttributeSerializer.SERIALIZER.deserialize(aModel, theTable, theTableEntity);
            DictionaryIndexSerializer.SERIALIZER.deserialize(aModel, theTable, theTableEntity);
            aModel.getTables().add(theTable);
        }
    }
}
