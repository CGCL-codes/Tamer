package modules.taskModule.entityDefinitions;

import java.util.Hashtable;
import java.util.Vector;
import modules.taskModule.actions.GenerateSourceCode;
import modules.taskModule.actions.ViewModuleSource;
import newtonERP.common.ActionLink;
import newtonERP.common.ListModule;
import newtonERP.module.AbstractOrmEntity;
import newtonERP.module.Module;
import newtonERP.orm.associations.PluralAccessor;
import newtonERP.orm.fields.Fields;
import newtonERP.orm.fields.field.Field;
import newtonERP.orm.fields.field.FieldFactory;
import newtonERP.orm.fields.field.FieldType;
import newtonERP.orm.fields.field.property.NaturalKey;
import newtonERP.viewers.viewerData.ListViewerData;

/**
 * Entité représentant un module
 * 
 * @author Guillaume Lacasse
 */
public class ModuleEntity extends AbstractOrmEntity {

    /**
	 */
    public ModuleEntity() {
        super();
        setVisibleName("Module");
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
        fieldList.add(FieldFactory.newField(FieldType.STRING, "systemName"));
        fieldList.add(visibleName);
        return new Fields(fieldList);
    }

    /**
	 * @return vrai module
	 */
    public Module getModule() {
        return ListModule.getModule(getModuleName());
    }

    private String getModuleName() {
        return getDataString("systemName");
    }

    @Override
    public ListViewerData getList(Hashtable<String, String> parameters) {
        parameters.put(getPrimaryKeyName(), "&");
        ListViewerData entityList = super.getList(parameters);
        entityList.addSpecificActionButtonList(new ActionLink("Voir source", new ViewModuleSource(), parameters));
        entityList.addSpecificActionButtonList(new ActionLink("Générer source", new GenerateSourceCode(), parameters));
        return entityList;
    }

    /**
	 * @return list of entity entity for this module
	 */
    public Vector<EntityEntity> getEntityEntityList() {
        PluralAccessor accessor = getPluralAccessor(new EntityEntity().getSystemName());
        Vector<EntityEntity> entityEntityList = new Vector<EntityEntity>();
        for (AbstractOrmEntity entity : accessor) {
            entityEntityList.add((EntityEntity) entity);
        }
        return entityEntityList;
    }

    /**
	 * @return list of action entity for this module
	 */
    public Vector<ActionEntity> getActionEntityList() {
        PluralAccessor accessor = getPluralAccessor(new ActionEntity().getSystemName());
        Vector<ActionEntity> actionEntityList = new Vector<ActionEntity>();
        for (AbstractOrmEntity entity : accessor) {
            actionEntityList.add((ActionEntity) entity);
        }
        return actionEntityList;
    }
}
