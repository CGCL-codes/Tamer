package com.genia.toolbox.projects.csv.bean.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import com.genia.toolbox.projects.csv.bean.Action;
import com.genia.toolbox.projects.csv.bean.EntityDescriptor;
import com.genia.toolbox.projects.csv.bean.FieldDescriptor;
import com.genia.toolbox.projects.csv.bean.MappingDescriptor;
import com.genia.toolbox.projects.csv.bean.MutableAction;
import com.genia.toolbox.projects.csv.bean.MutableEntityDescriptor;
import com.genia.toolbox.projects.csv.bean.MutableFieldDescriptor;
import com.genia.toolbox.projects.csv.bean.MutableFolderDescriptor;

/**
 * implementation of {@link MutableFolderDescriptor}.
 */
public class FolderDescriptorImpl implements MutableFolderDescriptor {

    /**
   * the list of actions to execute.
   */
    private List<Action> actions = new ArrayList<Action>();

    /**
   * the name of the folder that this one depends on.
   */
    private List<String> dependantFolders = new ArrayList<String>();

    /**
   * the list of entities defined in the CSV file.
   */
    private List<EntityDescriptor> entities = new ArrayList<EntityDescriptor>();

    /**
   * the entities indexed by their names.
   */
    private final transient Map<String, EntityDescriptor> entitiesByName = new HashMap<String, EntityDescriptor>();

    /**
   * the fields indexed by their names.
   */
    private final transient Map<String, FieldDescriptor> fieldsByName = new HashMap<String, FieldDescriptor>();

    /**
   * the list of the field contained in the CSV file.
   */
    private List<FieldDescriptor> fields = new ArrayList<FieldDescriptor>();

    /**
   * the name of the csv folder.
   */
    private String folderName;

    /**
   * the {@link MappingDescriptor} that contains this folder.
   */
    private MappingDescriptor mappingDescriptor;

    /**
   * returns the {@link FieldDescriptor} associated to the given name.
   * 
   * @param name
   *          the name of the {@link FieldDescriptor} to retrieve
   * @return the {@link FieldDescriptor} associated to the given name
   */
    public FieldDescriptor getField(String name) {
        return fieldsByName.get(name);
    }

    /**
   * add an action to the list.
   * 
   * @param action
   *          the {@link Action} to add
   */
    public void addAction(MutableAction action) {
        this.actions.add(action);
        action.setFolderDescriptor(this);
    }

    /**
   * add a dependant folder to the list.
   * 
   * @param name
   *          the name of the dependante folder to add
   */
    public void addDependantFolder(String name) {
        this.dependantFolders.add(name);
    }

    /**
   * add a new {@link EntityDescriptor} to the list of entities.
   * 
   * @param entity
   *          the {@link EntityDescriptor} to add
   */
    public void addEntity(MutableEntityDescriptor entity) {
        this.entities.add(entity);
        entity.setFolderDescriptor(this);
        entitiesByName.put(entity.getName(), entity);
    }

    /**
   * add a {@link FieldDescriptor} to the list.
   * 
   * @param field
   *          the {@link FieldDescriptor} to add
   */
    public void addField(MutableFieldDescriptor field) {
        this.fields.add(field);
        field.setFolderDescriptor(this);
        fieldsByName.put(field.getName(), field);
    }

    /**
   * determines if this object is equal to the object argument.
   * 
   * @param obj
   *          the other object to compare
   * @return true if equal, otherwise false
   */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        FolderDescriptorImpl other = (FolderDescriptorImpl) obj;
        return ObjectUtils.nullSafeEquals(this.folderName, other.folderName);
    }

    /**
   * getter for the actions property.
   * 
   * @return the actions
   */
    public List<Action> getActions() {
        return actions;
    }

    /**
   * setter for the actions property.
   * 
   * @param actions
   *          the actions to set
   */
    public void setActions(List<Action> actions) {
        this.actions = actions;
    }

    /**
   * getter for the dependantFolders property.
   * 
   * @return the dependantFolders
   */
    public List<String> getDependantFolders() {
        return dependantFolders;
    }

    /**
   * getter for the entities property.
   * 
   * @return the entities
   */
    public List<EntityDescriptor> getEntities() {
        return entities;
    }

    /**
   * getter for the fields property.
   * 
   * @return the fields
   */
    public List<FieldDescriptor> getFields() {
        return fields;
    }

    /**
   * getter for the folderName property.
   * 
   * @return the folderName
   */
    public String getFolderName() {
        return folderName;
    }

    /**
   * getter for the mappingDescriptor property.
   * 
   * @return the mappingDescriptor
   */
    public MappingDescriptor getMappingDescriptor() {
        return mappingDescriptor;
    }

    /**
   * Returns a hash code value for the object. This method is supported for the
   * benefit of hashtables such as those provided by
   * <code>java.util.Hashtable</code>.
   * 
   * @return the hashCode
   * @see java.lang.Object#hashCode()
   */
    @Override
    public int hashCode() {
        return ObjectUtils.nullSafeHashCode(this.folderName);
    }

    /**
   * setter for the dependantFolders property.
   * 
   * @param dependantFolders
   *          the dependantFolders to set
   */
    public void setDependantFolders(List<String> dependantFolders) {
        this.dependantFolders = dependantFolders;
    }

    /**
   * setter for the entities property.
   * 
   * @param entities
   *          the entities to set
   */
    public void setEntities(List<EntityDescriptor> entities) {
        this.entities = entities;
    }

    /**
   * setter for the fields property.
   * 
   * @param fields
   *          the fields to set
   */
    public void setFields(List<FieldDescriptor> fields) {
        this.fields = fields;
    }

    /**
   * setter for the folderName property.
   * 
   * @param folderName
   *          the folderName to set
   */
    public void setFolderName(String folderName) {
        this.folderName = StringUtils.trimWhitespace(folderName);
    }

    /**
   * setter for the mappingDescriptor property.
   * 
   * @param mappingDescriptor
   *          the mappingDescriptor to set
   */
    public void setMappingDescriptor(MappingDescriptor mappingDescriptor) {
        this.mappingDescriptor = mappingDescriptor;
    }

    /**
   * returns the {@link EntityDescriptor} associated to the given name.
   * 
   * @param name
   *          the name of the {@link EntityDescriptor} to retrieve
   * @return the {@link EntityDescriptor} associated to the given name
   * @see com.genia.toolbox.projects.csv.bean.FolderDescriptor#getEntity(java.lang.String)
   */
    public EntityDescriptor getEntity(String name) {
        return entitiesByName.get(name);
    }
}
