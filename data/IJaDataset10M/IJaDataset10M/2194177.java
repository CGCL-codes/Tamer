package unbbayes.prs.prm;

import java.util.Collection;
import java.util.List;
import unbbayes.prs.INode;
import unbbayes.prs.prm.builders.IAttributeDescriptorBuilder;
import unbbayes.prs.prm.builders.IForeignKeyBuilder;

/**
 * This interface represents a PRM class (an entity).
 * You may want to create a graph for entity-relationship, thus,
 * this interface extends INode
 * @author Shou Matsumoto
 *
 */
public interface IPRMClass extends INode {

    /**
	 * The name to identify a set of primary keys of this class
	 * @return
	 */
    public String getPrimaryKeyName();

    /**
	 * The name to identify a set of primary keys of this class
	 * @param name
	 */
    public void setPrimaryKeyName(String name);

    /**
	 * Obtains the PRM project hosting this class/entity
	 * @return
	 */
    public IPRM getPRM();

    /**
	 * Sets the PRM project hosting this class/entity
	 * @param prm
	 */
    public void setPRM(IPRM prm);

    /**
	 * Obtains a list of objects instantiating this class
	 * (obtains rows of this entity/table)
	 * @return
	 */
    public List<IPRMObject> getPRMObjects();

    /**
	 * Sets a list of objects instantiating this class
	 * (obtains rows of this entity/table)
	 * @param objects
	 */
    public void setPRMObjects(List<IPRMObject> objects);

    /**
	 * Obtains a list of attribute's descriptors.
	 * An attribute of a class is similar to a column in a entity-table.
	 * @return
	 */
    public List<IAttributeDescriptor> getAttributeDescriptors();

    /**
	 * Sets a list of attribute's descriptors.
	 * An attribute of a class is similar to a column in a entity-table.
	 * @param attributeDescriptors
	 */
    public void setAttributeDescriptors(List<IAttributeDescriptor> attributeDescriptors);

    /**
	 * Obtains a collection of references from other entities to this
	 * entity.
	 * @return
	 */
    public Collection<IForeignKey> getIncomingForeignKeys();

    /**
	 * Sets a collection of references from other entities to this
	 * entity.
	 * @param foreignKeys
	 */
    public void setIncomingForeignKeys(Collection<IForeignKey> foreignKeys);

    /**
	 * Obtains all {@link IAttributeDescriptor} having
	 * {@link IAttributeDescriptor#isPrimaryKey()} == true.
	 * This is just a facade method.
	 * @return
	 */
    public Collection<IAttributeDescriptor> getPrimaryKeys();

    /**
	 * Obtains all {@link IForeignKey} going from this class
	 * @return
	 */
    public List<IForeignKey> getForeignKeys();

    /**
	 * Obtains all {@link IForeignKey} going from this class.
	 * @return
	 */
    public void setForeignKeys(List<IForeignKey> foreignKeys);

    /**
	 * The superclass of this class, if inheritance is enabled
	 * @return
	 */
    public IPRMClass getPRMSuperClass();

    /**
	 * The superclass of this class, if inheritance is enabled
	 * @param superClass
	 */
    public void setPRMSuperClass(IPRMClass superClass);

    /**
	 * Builder to instantiate an attribute in this class
	 * @return the attributeDescriptorBuilder
	 */
    public IAttributeDescriptorBuilder getAttributeDescriptorBuilder();

    /**
	 * Builder to instantiate an attribute in this class
	 * @param attributeDescriptorBuilder the attributeDescriptorBuilder to set
	 */
    public void setAttributeDescriptorBuilder(IAttributeDescriptorBuilder attributeDescriptorBuilder);

    /**
	 * Builder to instantiate a foreign key.
	 * @return
	 */
    public IForeignKeyBuilder getForeignKeyBuilder();

    /**
	 * Builder to instantiate a foreign key.
	 * @param foreignKeyBuilder
	 */
    public void setForeignKeyBuilder(IForeignKeyBuilder foreignKeyBuilder);

    /**
	 * Searches an attribute from {@link #getAttributeDescriptors()} 
	 * by its name.
	 * @param name
	 * @return
	 */
    public IAttributeDescriptor findAttributeDescriptorByName(String name);
}
