package com.genia.toolbox.projects.csv.bean;

import java.util.List;

/**
 * a mutable {@link FolderDescriptor}.
 */
public interface MutableFolderDescriptor extends FolderDescriptor {

    /**
   * setter for the folderName property.
   * 
   * @param folderName
   *          the folderName to set
   */
    public abstract void setFolderName(String folderName);

    /**
   * setter for the dependantFolders property.
   * 
   * @param dependantFolders
   *          the dependantFolders to set
   */
    public abstract void setDependantFolders(List<String> dependantFolders);

    /**
   * setter for the fields property.
   * 
   * @param fields
   *          the fields to set
   */
    public abstract void setFields(List<FieldDescriptor> fields);

    /**
   * setter for the entities property.
   * 
   * @param entities
   *          the entities to set
   */
    public abstract void setEntities(List<EntityDescriptor> entities);

    /**
   * setter for the mappingDescriptor property.
   * 
   * @param mappingDescriptor
   *          the mappingDescriptor to set
   */
    abstract void setMappingDescriptor(MappingDescriptor mappingDescriptor);

    /**
   * setter for the actions property.
   * 
   * @param actions
   *          the actions to set
   */
    public abstract void setActions(List<Action> actions);
}
