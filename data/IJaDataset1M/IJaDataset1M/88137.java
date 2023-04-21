package freetm.client.ui.trees.hierarchy;

import freetm.client.ui.trees.ModelGeneral;
import net.mygwt.ui.client.widget.tree.TreeItem;

/**
 *
 * @author Yorgos
 */
public class ModelHieRoot extends ModelGeneral {

    private ModelHieTopicTypes m_topicTypes;

    private ModelHieOccurrenceTypes m_occurrenceTypes;

    private ModelHieRoleTypes m_roleTypes;

    private ModelHieAssociationTypes m_associationTypes;

    private ModelHieTopicsWithoutType m_topicsWithoutType;

    private ModelHieAssociationsWithoutType m_assocWithoutType;

    private ModelHieFavourites m_favourites;

    /** Creates a new instance of TreeItemTopic */
    public ModelHieRoot() {
        this.add(getModelTopicTypes());
        this.add(getModelOccurrenceTypes());
        this.add(getModelRoleTypes());
        this.add(getModelAssociationTypes());
        this.add(getModelTopicsWithoutType());
        this.add(getModelAssocWithoutType());
        this.add(getModelFavourites());
    }

    public void refreshFromServer(final TreeItem treeItem) {
    }

    public String toString() {
        return "Hierarchies";
    }

    public ModelHieTopicTypes getModelTopicTypes() {
        if (m_topicTypes == null) m_topicTypes = new ModelHieTopicTypes();
        return m_topicTypes;
    }

    public ModelHieOccurrenceTypes getModelOccurrenceTypes() {
        if (m_occurrenceTypes == null) m_occurrenceTypes = new ModelHieOccurrenceTypes();
        return m_occurrenceTypes;
    }

    public ModelHieRoleTypes getModelRoleTypes() {
        if (m_roleTypes == null) m_roleTypes = new ModelHieRoleTypes();
        return m_roleTypes;
    }

    public ModelHieAssociationTypes getModelAssociationTypes() {
        if (m_associationTypes == null) m_associationTypes = new ModelHieAssociationTypes();
        return m_associationTypes;
    }

    public ModelHieTopicsWithoutType getModelTopicsWithoutType() {
        if (m_topicsWithoutType == null) m_topicsWithoutType = new ModelHieTopicsWithoutType();
        return m_topicsWithoutType;
    }

    public ModelHieAssociationsWithoutType getModelAssocWithoutType() {
        if (m_assocWithoutType == null) m_assocWithoutType = new ModelHieAssociationsWithoutType();
        return m_assocWithoutType;
    }

    public ModelHieFavourites getModelFavourites() {
        if (m_favourites == null) m_favourites = new ModelHieFavourites();
        return m_favourites;
    }

    public void clear() {
        getModelTopicTypes().clearAll();
        getModelOccurrenceTypes().clearAll();
        getModelRoleTypes().clearAll();
        getModelAssociationTypes().clearAll();
        getModelTopicsWithoutType().clearAll();
        getModelAssocWithoutType().clearAll();
        getModelFavourites().clearAll();
    }
}
