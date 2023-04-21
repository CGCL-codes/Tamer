package net.sf.jmd.metarepresentation.impl;

import java.util.List;
import net.sf.jmd.metarepresentation.IMember;
import net.sf.jmd.metarepresentation.IMetaRepresentation;
import net.sf.jmd.metarepresentation.IModel;
import net.sf.jmd.metarepresentation.IModelItem;
import net.sf.jmd.metarepresentation.IOperation;
import net.sf.jmd.metarepresentation.IParameter;

public class MetaRepresentation implements IMetaRepresentation {

    private static MetaRepresentation instance = null;

    private IModel amModel;

    private IModel dmModel;

    private MetaRepresentation() {
        amModel = new Model();
        amModel.setType(Model.Type.AM);
        dmModel = new Model();
        dmModel.setType(Model.Type.DM);
    }

    /**
     * the metarepresentation is a singleton in order to let the level1 checks
     * find the lists of interfaces.
     * @return
     */
    public static IMetaRepresentation getInstance() {
        if (instance == null) {
            instance = new MetaRepresentation();
        }
        return instance;
    }

    /**
     * {@inheritDoc}
     *
     * @see de.dlr.sistec.modi.metamodel.IMetaRepresentation#getAmModel()
     */
    public IModel getAmModel() {
        return amModel;
    }

    /**
     * {@inheritDoc}
     *
     * @see de.dlr.sistec.modi.metamodel.IMetaRepresentation#setAmModel(de.dlr
     *          .sistec.modi.metamodel.Model)
     */
    public void setAmModel(final IModel newArchitectsModel) {
        this.amModel = newArchitectsModel;
    }

    /**
     * {@inheritDoc}
     *
     * @see de.dlr.sistec.modi.metamodel.IMetaRepresentation#getDmModel()
     */
    public IModel getDmModel() {
        return dmModel;
    }

    /**
     * {@inheritDoc}
     *
     * @see de.dlr.sistec.modi.metamodel.IMetaRepresentation#setDmModel(de.dlr
     *          .sistec.modi.metamodel.Model)
     */
    public void setDmModel(final IModel newDevelopersModel) {
        this.dmModel = newDevelopersModel;
    }

    /**
     * {@inheritDoc}
     *
     * @see de.dlr.sistec.modi.metamodel.IMetaRepresentation#getModelItems(de
     *          .dlr.sistec.modi.metamodel.Model.PersistenceType)
     */
    public List<IModelItem> getModelItems(final Model.Type type) {
        List<IModelItem> result = null;
        if (type == Model.Type.AM) {
            result = amModel.getItems();
        }
        if (type == Model.Type.DM) {
            result = dmModel.getItems();
        }
        return result;
    }

    /**
     * {@inheritDoc}
     *
     * @see de.dlr.sistec.modi.metamodel.IMetaRepresentation#getMembers(de.dlr
     *          .sistec.modi.metamodel.ModelItem)
     */
    public List<IMember> getMembers(final IModelItem modelItem) {
        List<IMember> result = modelItem.getMembers();
        return result;
    }

    /**
     * {@inheritDoc}
     *
     * @see de.dlr.sistec.modi.metamodel.IMetaRepresentation#getParameters(de
     *          .dlr.sistec.modi.metamodel.Operation)
     */
    public List<IParameter> getParameters(final IOperation operation) {
        List<IParameter> result = operation.getParameters();
        return result;
    }

    /**
     * The toString method for MetaRepresentation.
     * @return a String representation of this class.
     */
    public String toString() {
        return "AM Model: " + amModel + "\n\n----------------------------\n\n" + "DM Model: " + dmModel;
    }
}
