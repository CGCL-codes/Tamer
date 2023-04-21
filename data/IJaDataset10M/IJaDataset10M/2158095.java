package modules.taskModule.entityDefinitions;

import java.util.Hashtable;
import java.util.Vector;
import modules.taskModule.actions.AddFieldToOrm;
import newtonERP.common.ActionLink;
import newtonERP.module.AbstractEntity;
import newtonERP.module.AbstractOrmEntity;
import newtonERP.orm.Orm;
import newtonERP.orm.associations.AccessorManager;
import newtonERP.orm.fields.Fields;
import newtonERP.orm.fields.field.Field;
import newtonERP.orm.fields.field.FieldFactory;
import newtonERP.orm.fields.field.FieldType;
import newtonERP.orm.fields.field.property.NaturalKey;
import newtonERP.orm.fields.field.property.ReadOnly;
import newtonERP.orm.fields.field.type.FieldInt;
import newtonERP.viewers.viewerData.BaseViewerData;
import newtonERP.viewers.viewerData.ListViewerData;

/**
 * Représente un champ
 * 
 * @author Guillaume Lacasse
 */
public class FieldEntity extends AbstractOrmEntity {

    /**
	 */
    public FieldEntity() {
        super();
        setVisibleName("Champ");
        AccessorManager.addAccessor(this, new FieldTypeEntity());
        AccessorManager.addAccessor(this, new EntityEntity());
    }

    @Override
    protected Fields preInitFields() {
        return initFields();
    }

    @Override
    public Fields initFields() {
        Field visibleName = FieldFactory.newField(FieldType.STRING, "visibleName");
        visibleName.addProperty(new NaturalKey());
        Vector<Field> fieldList = new Vector<Field>();
        fieldList.add(FieldFactory.newField(FieldType.STRING, "name"));
        fieldList.add(visibleName);
        fieldList.add(new FieldInt("Type", new FieldTypeEntity().getForeignKeyName()));
        Field entityID = new FieldInt("Entité", new EntityEntity().getForeignKeyName());
        entityID.addProperty(new ReadOnly());
        fieldList.add(entityID);
        fieldList.add(FieldFactory.newField(FieldType.BOOL, "readOnly"));
        fieldList.add(FieldFactory.newField(FieldType.BOOL, "hidden"));
        fieldList.add(FieldFactory.newField(FieldType.BOOL, "naturalKey"));
        Field dynamicField = FieldFactory.newField(FieldType.BOOL, "dynamicField");
        fieldList.add(dynamicField);
        return new Fields(fieldList);
    }

    @Override
    public AbstractEntity newUI(Hashtable<String, String> parameters) {
        getFields().setDefaultValue(false);
        String entityEntityAccessorName = new EntityEntity().getForeignKeyName();
        if (parameters.containsKey(entityEntityAccessorName)) {
            String entityEntityIdString = parameters.get(entityEntityAccessorName);
            int entityEntityId = Integer.parseInt(entityEntityIdString);
            EntityEntity entityEntity = getEntityEntity(entityEntityId);
            if (!entityEntity.containsPrimaryKeyField()) {
                String primaryKeyName = entityEntity.buildPrimaryKeyName();
                if (!parameters.contains("name")) {
                    parameters.put("name", primaryKeyName);
                    parameters.put("visibleName", "Numéro");
                }
            }
        }
        return editUI(parameters);
    }

    private EntityEntity getEntityEntity(int entityPrimaryKey) {
        EntityEntity entityEntity = new EntityEntity();
        entityEntity.setData(entityEntity.getPrimaryKeyName(), entityPrimaryKey);
        entityEntity = (EntityEntity) Orm.getInstance().selectUnique(entityEntity);
        return entityEntity;
    }

    @Override
    public ListViewerData getList(Hashtable<String, String> parameters) {
        parameters.put(getPrimaryKeyName(), "&");
        ListViewerData entityList = super.getList(parameters);
        entityList.removeGlobalActions("Nouveau " + getVisibleName());
        entityList.addSpecificActionButtonList(new ActionLink("Mettre dans Orm", new AddFieldToOrm(), parameters));
        return entityList;
    }

    /**
	 * @return retourne un vrai field
	 */
    public Field getFieldInstance() {
        FieldTypeEntity fieldTypeEntity = getFieldTypeEntity();
        String name = getDataString("name");
        String visibleName = getDataString("visibleName");
        String type = fieldTypeEntity.getDataString("systemName");
        Boolean readOnly = (Boolean) getData("readOnly");
        Boolean hidden = (Boolean) getData("hidden");
        Boolean naturalKey = (Boolean) getData("naturalKey");
        Boolean dynamicField = (Boolean) getData("dynamicField");
        Field field;
        if (type.equals("FieldBool")) {
            field = FieldFactory.newField(FieldType.BOOL, name);
        } else if (type.equals("FieldCurrency")) {
            field = FieldFactory.newField(FieldType.CURRENCY, name);
        } else if (type.equals("FieldDate")) {
            field = FieldFactory.newField(FieldType.DATE, name);
        } else if (type.equals("FieldDateTime")) {
            field = FieldFactory.newField(FieldType.DATE_TIME, name);
        } else if (type.equals("FieldDouble")) {
            field = FieldFactory.newField(FieldType.DOUBLE, name);
        } else if (type.equals("FieldInt")) {
            field = FieldFactory.newField(FieldType.INT, name);
        } else if (type.equals("FieldString")) {
            field = FieldFactory.newField(FieldType.STRING, name);
        } else if (type.equals("FieldText")) {
            field = FieldFactory.newField(FieldType.TEXT, name);
        } else if (type.equals("FieldTime")) {
            field = FieldFactory.newField(FieldType.TIME, name);
        } else {
            field = FieldFactory.newField(FieldType.STRING, name);
        }
        if (hidden) {
            field.addProperty(new newtonERP.orm.fields.field.property.hidden());
        }
        if (naturalKey) {
            field.addProperty(new NaturalKey());
        }
        field.addProperty(new ReadOnly());
        field.setDynamicField(dynamicField);
        return field;
    }

    private FieldTypeEntity getFieldTypeEntity() {
        return (FieldTypeEntity) getSingleAccessor(new FieldTypeEntity().getForeignKeyName());
    }

    /**
	 * BaseAction Delete, on ne veut pas deleter de Field
	 * 
	 * @param parameters parametre suplementaire
	 * @return todo: qu'Est-ce que l'on devrai retourné en general?
	 */
    @Override
    public AbstractEntity deleteUI(Hashtable<String, String> parameters) {
        FieldEntity fieldEntity = (FieldEntity) Orm.getInstance().selectUnique(this);
        EntityEntity entityEntity = (EntityEntity) fieldEntity.getSingleAccessor(new EntityEntity().getForeignKeyName());
        if (Orm.getInstance().isEntityExists(entityEntity.getDataString("systemName"))) {
            BaseViewerData editUI = editUI(parameters);
            editUI.addAlertMessage("Impossible d'effacer ce champ car l'entité à laquelle il appartient est déjà dans la base de donnée.");
            return editUI;
        }
        return super.deleteUI(parameters);
    }
}
