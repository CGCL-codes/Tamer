package org.openxava.test.model;

import java.math.*;
import java.rmi.RemoteException;

public interface IIngredient extends org.openxava.model.IModel {

    public static final String PROPERTY_oid = "oid";

    String getOid() throws RemoteException;

    public static final String PROPERTY_name = "name";

    String getName() throws RemoteException;

    void setName(String name) throws RemoteException;

    org.openxava.test.model.IFormula getFavouriteFormula() throws RemoteException;

    void setFavouriteFormula(org.openxava.test.model.IFormula newFavouriteFormula) throws RemoteException;

    org.openxava.test.model.IIngredient getPartOf() throws RemoteException;

    void setPartOf(org.openxava.test.model.IIngredient newPartOf) throws RemoteException;
}
