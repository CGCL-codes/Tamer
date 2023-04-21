package org.acme.acciones;

import java.rmi.*;
import java.sql.*;
import java.util.*;
import javax.ejb.*;
import org.openxava.actions.*;
import org.openxava.jpa.*;
import org.openxava.model.*;
import org.openxava.model.meta.*;
import org.openxava.util.*;
import org.openxava.validators.*;
import org.openxava.view.*;

/**
 * To save a collection element. <p>
 * 
 * The case of collections of entities with @AsEmbedded (or with as-aggregate="true")
 * is treated by {@link AddElementsToCollectionAction}. <p>
 * 
 * @author Javier Paniza
 */
public class GrabarDetalleProductos extends CollectionElementViewBaseAction implements IChainActionWithArgv {

    public void execute() throws Exception {
        Map containerKey = saveIfNotExists(getCollectionElementView().getParent());
        if (XavaPreferences.getInstance().isMapFacadeAutoCommit()) {
            getView().setKeyEditable(false);
        }
        if (isEntityReferencesCollection()) saveEntity(containerKey); else saveAggregate(containerKey);
        getView().setKeyEditable(false);
        getCollectionElementView().setCollectionEditingRow(-1);
        getCollectionElementView().clear();
        resetDescriptionsCache();
        getView().recalculateProperties();
    }

    private void validateMaximum() throws ValidationException, XavaException {
        MetaCollection metaCollection = getMetaCollection();
        int maximum = metaCollection.getMaximum();
        if (maximum > 0) {
            if (getCollectionElementView().getCollectionValues().size() >= maximum) {
                Messages errors = new Messages();
                errors.add("maximum_elements", new Integer(maximum), metaCollection.getName(), metaCollection.getMetaModel().getName());
                throw new ValidationException(errors);
            }
        }
    }

    private void saveEntity(Map containerKey) throws Exception {
        if (getCollectionElementView().isEditable()) {
            Map parentKey = new HashMap();
            MetaCollection metaCollection = getMetaCollection();
            parentKey.put(metaCollection.getMetaReference().getRole(), containerKey);
            Map values = getCollectionElementView().getValues();
            values.putAll(parentKey);
            addMessage("HolaC");
            try {
                MapFacade.setValues(getCollectionElementView().getModelName(), getCollectionElementView().getKeyValues(), values);
                addMessage("entity_modified", getCollectionElementView().getModelName());
                addMessage("Hola1");
            } catch (ObjectNotFoundException ex) {
                validateMaximum();
                MapFacade.create(getCollectionElementView().getModelName(), values);
                addMessage("entity_created_and_associated", getCollectionElementView().getModelName(), getCollectionElementView().getParent().getModelName());
                addMessage("Hola2");
            }
        } else {
            associateEntity(getCollectionElementView().getKeyValues());
            addMessage("entity_associated", getCollectionElementView().getModelName(), getCollectionElementView().getParent().getModelName());
            addMessage("Hola3");
        }
    }

    protected void associateEntity(Map keyValues) throws ValidationException, XavaException, ObjectNotFoundException, FinderException, RemoteException {
        validateMaximum();
        MapFacade.addCollectionElement(getCollectionElementView().getParent().getMetaModel().getName(), getCollectionElementView().getParent().getKeyValues(), getCollectionElementView().getMemberName(), keyValues);
        addMessage("HolaB");
    }

    private MetaCollection getMetaCollection() throws ElementNotFoundException, XavaException {
        return getCollectionElementView().getParent().getMetaModel().getMetaCollection(getCollectionElementView().getMemberName());
    }

    private void saveAggregate(Map containerKey) throws Exception {
        if (getCollectionElementView().getKeyValuesWithValue().isEmpty()) {
            createAggregate(containerKey);
            addMessage("HolaAA");
            Connection con = null;
            try {
                con = DataSourceConnectionProvider.getByComponent("Productos").getConnection();
                PreparedStatement ps = con.prepareStatement("SELECT sum(total) as totales FROM productos where facturas_codigo=?");
                ps.setInt(1, new Integer(getCollectionElementView().getParent().getValue("codigo").toString()));
                ResultSet rs = ps.executeQuery();
                Integer result;
                Integer gvtotal;
                if (!Is.emptyString(getCollectionElementView().getValueString("total"))) {
                    gvtotal = new Integer(getCollectionElementView().getValueString("total"));
                } else {
                    gvtotal = new Integer("0");
                }
                if (rs.next()) {
                    result = new Integer(rs.getString(1)) + gvtotal;
                } else {
                    result = new Integer("0");
                }
                addMessage("aggregate_created", getCollectionElementView().getValueString("total") + " lala " + result);
                System.out.print("gvtotal" + getCollectionElementView().getValueString("total"));
                ps.close();
                getCollectionElementView().getParent().setValue("neto", result.toString());
                float theiva = Float.parseFloat(getCollectionElementView().getParent().getValue("ivaporcentaje").toString());
                float eliva = new Float(result) * (theiva / 100);
                getCollectionElementView().getParent().setValue("ivavalor", Float.toString(eliva));
                float eltotal = new Float(result) + eliva;
                getCollectionElementView().getParent().setValue("total", (int) eltotal);
            } catch (Exception ex) {
                Integer result = new Integer(getCollectionElementView().getValueString("total"));
                getCollectionElementView().getParent().setValue("neto", result.toString());
                float theiva = Float.parseFloat(getCollectionElementView().getParent().getValue("ivaporcentaje").toString());
                float eliva = new Float(result) * (theiva / 100);
                getCollectionElementView().getParent().setValue("ivavalor", Float.toString(eliva));
                float eltotal = new Float(result) + eliva;
                getCollectionElementView().getParent().setValue("total", (int) eltotal);
            } finally {
                try {
                    con.close();
                } catch (Exception ex) {
                }
            }
        } else {
            try {
                MapFacade.setValues(getCollectionElementView().getModelName(), getCollectionElementView().getKeyValues(), getCollectionElementView().getValues());
                addMessage("aggregate_modified", getCollectionElementView().getModelName());
                addMessage("Hola4");
            } catch (ObjectNotFoundException ex) {
                createAggregate(containerKey);
            }
        }
    }

    private void createAggregate(Map containerKey) throws Exception {
        addMessage("Hola5");
        validateMaximum();
        int row = getCollectionElementView().getCollectionValues().size();
        MapFacade.createAggregate(getCollectionElementView().getModelName(), containerKey, row + 1, getCollectionElementView().getValues());
        addMessage("aggregate_created", getCollectionElementView().getModelName());
    }

    private boolean isAutoCommit() {
        return XavaPreferences.getInstance().isMapFacadeAutoCommit() || XavaPreferences.getInstance().isMapFacadeAsEJB();
    }

    /**
	 * @return The saved object 
	 */
    protected Map saveIfNotExists(View view) throws Exception {
        if (getView() == view) {
            if (view.isKeyEditable()) {
                Map key = MapFacade.createReturningKey(getModelName(), view.getValues());
                addMessage("entity_created", getModelName());
                addMessage("Hola6");
                view.addValues(key);
                return key;
            } else {
                return view.getKeyValues();
            }
        } else {
            if (view.getKeyValuesWithValue().isEmpty()) {
                Map parentKey = saveIfNotExists(view.getParent());
                Map key = MapFacade.createAggregateReturningKey(view.getModelName(), parentKey, 0, view.getValues());
                addMessage("aggregate_created", view.getModelName());
                addMessage("Hola");
                view.addValues(key);
                return key;
            } else {
                return view.getKeyValues();
            }
        }
    }

    public String getNextAction() throws Exception {
        return getErrors().contains() ? null : getCollectionElementView().getNewCollectionElementAction();
    }

    public String getNextActionArgv() throws Exception {
        return "viewObject=" + getViewObject();
    }
}
