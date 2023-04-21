package com.privilege.displayable;

import java.lang.reflect.*;
import java.util.*;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import com.privilege.common.BeanTool;
import com.privilege.entity.AttributeComparator;
import com.privilege.entity.Entity;
import com.privilege.model.ButtonModel;
import com.privilege.model.DefaultDWRModel;
import com.privilege.model.DefaultEntityDWRModel;
import com.privilege.model.EntityModel;
import com.privilege.model.Model;

/**
 * @deprecated EntityTable Should be used instead
 * @author ankhmambi
 *
 */
public abstract class AbstractEntityComponent implements EntityComponent {

    public static final String SUBMIT_BUTTON_NAME = "Save";

    public static final String SUBMIT_BUTTON_ID = "Button";

    public static final String SUBMIT_BUTTON_PROPERTY_NAME = "Submit";

    private final String HEADER_SUFFIX_ID = "header";

    private final String TITLE_SUFFIX_ID = "title";

    private List<List<Component>> data = new ArrayList();

    private String entityName;

    private String entityPackage;

    private String previousPropertyName;

    private boolean reverse = false;

    private static final Logger logger = Logger.getLogger(AbstractEntityComponent.class);

    protected AbstractEntityComponent(EntityModel model) {
    }

    protected AbstractEntityComponent(String entityName, List entities) {
        if (entityName != null) this.entityName = entityName;
        sort(entities, getDefaultPropertyToSort());
    }

    public void sort(List list, String propertyToSortBy) {
        if (data != null) data.clear();
        if (propertyToSortBy == null) {
            propertyToSortBy = getDefaultPropertyToSort();
            previousPropertyName = null;
        }
        reverse = (propertyToSortBy.equals(previousPropertyName)) ? (!reverse) : false;
        Collections.sort(list, new AttributeComparator(propertyToSortBy, reverse));
        createContent(list);
    }

    protected void createContent(List entities) {
        for (int i = 0; i < entities.size(); i++) {
            Object entity = entities.get(i);
            if (entity instanceof Entity) createComponents((Entity) entity);
        }
    }

    protected void createComponents(Entity entity) {
        if (entityName == null) entityName = entity.getClass().getSimpleName();
        if (entityPackage == null) entityPackage = entity.getClass().getPackage().getName();
        Class entityClass = entity.getClass();
        Method[] methodList = entityClass.getMethods();
        int size = methodList.length;
        Component[] content = new Component[size];
        for (int j = 0; j < size; j++) {
            String methodName = methodList[j].getName();
            Class returnType = methodList[j].getReturnType();
            String propertyName = BeanTool.getPropertyName(methodName);
            Object returnedValue = BeanTool.invokeGetMethod(methodList[j], entity);
            int index = getEntityToDisplay().indexOf(propertyName);
            if (index != -1 && returnedValue != null) content[index] = createComponent(entity, propertyName, returnedValue);
        }
        data.add(Arrays.asList(content));
    }

    private Component createComponent(Entity entity, String propertyName, Object propertyValue) {
        Component component = null;
        ComponentUsage propertyUsage = getPropertyUsage(propertyName);
        if (propertyUsage != null) {
            String componentId = createId(propertyName) + entity.getId();
            component = (propertyValue instanceof Entity) ? getFactory().getComponent(entity.getClass().getSimpleName(), "id", componentId, propertyValue, propertyUsage) : getFactory().getComponent(componentId, propertyValue, propertyUsage);
        }
        return component;
    }

    public String createId(String propertyName) {
        return createId(propertyName, this.EMPTY_STRING);
    }

    public String createId(String propertyName, String suffix) {
        return String.valueOf(getEntityName() + propertyName + getFactory().getComponentType(getPropertyUsage(propertyName)) + suffix).toLowerCase();
    }

    protected abstract Factory getFactory();

    protected abstract List<String> getHeaderToDisplay();

    protected abstract List<String> getEntityToDisplay();

    protected abstract List<ComponentUsage> getEntityComponentUsage();

    protected abstract String getDefaultPropertyToSort();

    public List<List<Component>> getContent() {
        return data;
    }

    public List<Component> getTitle() {
        List<Component> header = new ArrayList<Component>();
        int columnCount = getPropertiesCount();
        for (int i = 0; i < columnCount; i++) {
            String propertyName = getEntityToDisplay().get(i);
            String componentId = createId(propertyName, TITLE_SUFFIX_ID);
            Model model = getFactory().getModel(componentId, getEntityName());
            if (i == 0) header.add(getFactory().getComponent(model, ComponentUsage.ID)); else header.add(getFactory().getEmptyComponent());
        }
        return header;
    }

    public List<Component> getHeader() {
        List<Component> header = new ArrayList<Component>();
        int columnCount = getPropertiesCount();
        for (int i = 0; i < columnCount; i++) {
            String propertyName = getEntityToDisplay().get(i);
            String componentId = createId(propertyName, HEADER_SUFFIX_ID);
            Model model = getFactory().getModel(componentId, getHeaderToDisplay().get(i));
            header.add(getFactory().getComponent(model, ComponentUsage.ID));
        }
        return header;
    }

    public List<Component> getModifier() {
        List<Component> modifier = new ArrayList<Component>();
        int columnCount = getPropertiesCount();
        for (int i = 0; i < columnCount; i++) {
            String propertyName = getEntityToDisplay().get(i);
            String componentId = createId(propertyName);
            ComponentUsage usage = getPropertyUsage(propertyName);
            Object voidEntity = BeanTool.getEmptyObject(getEntityPackage() + "." + getEntityName());
            String methodName = BeanTool.getGetterMethodName(propertyName);
            Method method = BeanTool.getMethod(voidEntity, methodName);
            Model model = getFactory().getModel(componentId, this.EMPTY_STRING);
            Object propertyEntity = (usage.equals(ComponentUsage.ENTITY)) ? BeanTool.getEmptyObject(getPropertyPackage(propertyName) + "." + BeanTool.getClassName(propertyName)) : null;
            Component component = ((propertyEntity instanceof Entity) && propertyEntity != null) ? getFactory().getComponent(BeanTool.getClassName(propertyName), getOptionText(propertyName), componentId, null, usage) : getFactory().getComponent(model, usage);
            modifier.add(component);
        }
        return modifier;
    }

    protected abstract String getOptionText(String propertyName);

    protected abstract String getPropertyPackage(String propertyName);

    protected abstract String getEntityPackage();

    public List<Component> getSubmitter() {
        List<Component> submitter = new ArrayList<Component>();
        int columnCount = getPropertiesCount();
        for (int i = 0; i < columnCount; i++) {
            Component component = null;
            if (i == columnCount - 1) {
                ButtonModel model = getFactory().getButtonModel(SUBMIT_BUTTON_ID, SUBMIT_BUTTON_NAME);
                model.setAction(createSubmitterAction());
                component = getFactory().getButton(model);
            } else {
                component = getFactory().getEmptyComponent();
            }
            submitter.add(component);
        }
        return submitter;
    }

    public abstract Action createSubmitterAction();

    public ComponentUsage getPropertyUsage(String propertyName) {
        List<String> list = getEntityToDisplay();
        if (list.contains(propertyName)) return getEntityComponentUsage().get(list.indexOf(propertyName));
        return null;
    }

    public int getPropertiesCount() {
        return (getEntityToDisplay() != null) ? getEntityToDisplay().size() : 0;
    }

    public String getEntityName() {
        return entityName;
    }

    public int getDataRowCount() {
        return data.size();
    }
}
