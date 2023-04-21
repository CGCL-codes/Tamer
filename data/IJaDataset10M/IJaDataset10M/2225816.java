package it.aco.mandragora.serviceFacade.context.sessionFacade.remoteFacade.SLSB.crud;

import it.aco.mandragora.serviceFacade.context.ContextServiceFacade;
import it.aco.mandragora.exception.ServiceLocatorException;
import it.aco.mandragora.exception.ServiceFacadeException;
import it.aco.mandragora.common.ServiceLocator;
import javax.ejb.SessionContext;
import javax.ejb.SessionBean;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import org.apache.commons.chain.Context;

public class ContextCrudSLSBFacadeBean implements SessionBean {

    private SessionContext sc;

    protected static String ContextServiceFacadeFactoryClassName = "ContextCrudSLSBFacadeBean.ContextServiceFacadeFactoryClass";

    protected static String ContextServiceFacadeClassName = "ContextCrudSLSBFacadeBean.ContextServiceFacadeClass";

    private static org.apache.log4j.Category log = org.apache.log4j.Logger.getLogger(ContextCrudSLSBFacadeBean.class.getName());

    public ContextCrudSLSBFacadeBean() {
    }

    public void ejbCreate() {
    }

    public void ejbRemove() {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public void setSessionContext(SessionContext sc) {
        this.sc = sc;
    }

    protected String getContextServiceFacadeFactoryClassName(Context context) throws RemoteException {
        try {
            if (context == null) return ContextServiceFacadeFactoryClassName;
            String contextServiceFacadeFactoryClassName = (String) context.get("contextServiceFacadeFactoryClassName");
            if (contextServiceFacadeFactoryClassName == null) contextServiceFacadeFactoryClassName = ContextServiceFacadeFactoryClassName;
            return contextServiceFacadeFactoryClassName;
        } catch (Exception e) {
            log.error("Exception caught in ContextCrudSLSBFacadeBean.getContextServiceFacadeFactoryClassName(Context context): " + e.toString());
            throw new RemoteException("RemoteException thrown in ContextCrudSLSBFacadeBean.getContextServiceFacadeFactoryClassName(Context context)" + e.toString(), e);
        }
    }

    protected String getContextServiceFacadeClassName(Context context) throws RemoteException {
        try {
            if (context == null) return ContextServiceFacadeClassName;
            String contextServiceFacadeClassName = (String) context.get("contextServiceFacadeClassName");
            if (contextServiceFacadeClassName == null) contextServiceFacadeClassName = ContextServiceFacadeClassName;
            return contextServiceFacadeClassName;
        } catch (Exception e) {
            log.error("Exception caught in ContextCrudSLSBFacadeBean.getContextServiceFacadeClassName(Context context): " + e.toString());
            throw new RemoteException("RemoteException thrown in ContextCrudSLSBFacadeBean.getContextServiceFacadeClassName(Context context)" + e.toString(), e);
        }
    }

    protected ContextServiceFacade getContextServiceFacade(Context context) throws RemoteException {
        try {
            return ServiceLocator.getInstance().getContextServiceFacade(getContextServiceFacadeFactoryClassName(context), getContextServiceFacadeClassName(context));
        } catch (ServiceLocatorException e) {
            log.error("ServiceLocatorException caught in ContextCrudSLSBFacadeBean.getContextServiceFacade(Context context): " + e.toString());
            throw new RemoteException("RemoteException thrown in ContextCrudSLSBFacadeBean.getContextServiceFacade(Context context)" + e.toString(), e);
        }
    }

    /***********************R E A D start******************/
    public Object findByPrimaryKey(Context context) throws RemoteException {
        try {
            ContextServiceFacade contextServiceFacade = getContextServiceFacade(context);
            return contextServiceFacade.findByPrimaryKey(context);
        } catch (ServiceFacadeException e) {
            log.error("ServiceFacadeException caught in ContextCrudSLSBFacadeBean.findByPrimaryKey(Context context): " + e.toString());
            throw new RemoteException("RemoteException thrown in ContextCrudSLSBFacadeBean.findByPrimaryKey(Context context)" + e.toString(), e);
        } catch (Exception e) {
            log.error("ServiceFacadeException caught in ContextCrudSLSBFacadeBean.findByPrimaryKey(Context context): " + e.toString());
            throw new RemoteException("RemoteException thrown in ContextCrudSLSBFacadeBean.findByPrimaryKey(Context context)" + e.toString(), e);
        }
    }

    public Collection findCollectionByTemplate(Context context) throws RemoteException {
        try {
            ContextServiceFacade contextServiceFacade = getContextServiceFacade(context);
            return contextServiceFacade.findCollectionByTemplate(context);
        } catch (ServiceFacadeException e) {
            log.error("ServiceFacadeException caught in ContextCrudSLSBFacadeBean.findCollectionByTemplate(Context context): " + e.toString());
            throw new RemoteException("RemoteException thrown in ContextCrudSLSBFacadeBean.findCollectionByTemplate(Context context)" + e.toString(), e);
        } catch (Exception e) {
            log.error("Exception caught in ContextCrudSLSBFacadeBean.findCollectionByTemplate(Context context): " + e.toString());
            throw new RemoteException("RemoteException thrown in ContextCrudSLSBFacadeBean.findCollectionByTemplate(Context context)" + e.toString(), e);
        }
    }

    /**
     * @deprecated use {@link #findCollectionByTemplate(org.apache.commons.chain.Context context)}
     */
    public Collection findOrderedCollectionByTemplate(Context context) throws RemoteException {
        try {
            ContextServiceFacade contextServiceFacade = getContextServiceFacade(context);
            return contextServiceFacade.findCollectionByTemplate(context);
        } catch (ServiceFacadeException e) {
            log.error("ServiceFacadeException caught in ContextCrudSLSBFacadeBean.findOrderedCollectionByTemplate(Context context): " + e.toString());
            throw new RemoteException("RemoteException thrown in ContextCrudSLSBFacadeBean.findOrderedCollectionByTemplate(Context context)" + e.toString(), e);
        } catch (Exception e) {
            log.error("Exception caught in ContextCrudSLSBFacadeBean.findOrderedCollectionByTemplate(Context context): " + e.toString());
            throw new RemoteException("RemoteException thrown in ContextCrudSLSBFacadeBean.findOrderedCollectionByTemplate(Context context)" + e.toString(), e);
        }
    }

    public Object findObjectByTemplate(Context context) throws RemoteException {
        try {
            ContextServiceFacade contextServiceFacade = getContextServiceFacade(context);
            return contextServiceFacade.findObjectByTemplate(context);
        } catch (ServiceFacadeException e) {
            log.error("ServiceFacadeException caught in ContextCrudSLSBFacadeBean.findObjectByTemplate(Context context): " + e.toString());
            throw new RemoteException("RemoteException thrown in ContextCrudSLSBFacadeBean.findObjectByTemplate(Context context)" + e.toString(), e);
        } catch (Exception e) {
            log.error("Exception caught in ContextCrudSLSBFacadeBean.findObjectByTemplate(Context context): " + e.toString());
            throw new RemoteException("RemoteException thrown in ContextCrudSLSBFacadeBean.findObjectByTemplate(Context context)" + e.toString(), e);
        }
    }

    public void retrieveReference(Context context) throws RemoteException {
        try {
            ContextServiceFacade contextServiceFacade = getContextServiceFacade(context);
            contextServiceFacade.retrieveReference(context);
        } catch (ServiceFacadeException e) {
            log.error("ServiceFacadeException caught in ContextCrudSLSBFacadeBean.retrieveReference(Context context): " + e.toString());
            throw new RemoteException("RemoteException thrown in ContextCrudSLSBFacadeBean.retrieveReference(Context context)" + e.toString(), e);
        } catch (Exception e) {
            log.error("Exception caught in ContextCrudSLSBFacadeBean.retrieveReference(Context context): " + e.toString());
            throw new RemoteException("RemoteException thrown in ContextCrudSLSBFacadeBean.retrieveReference(Context context)" + e.toString(), e);
        }
    }

    public void retrieveAllReferences(Context context) throws RemoteException {
        try {
            ContextServiceFacade contextServiceFacade = getContextServiceFacade(context);
            contextServiceFacade.retrieveAllReferences(context);
        } catch (ServiceFacadeException e) {
            log.error("ServiceFacadeException caught in ContextCrudSLSBFacadeBean.retrieveAllReferences(Context context): " + e.toString());
            throw new RemoteException("RemoteException thrown in ContextCrudSLSBFacadeBean.retrieveAllReferences(Context context)" + e.toString(), e);
        } catch (Exception e) {
            log.error("Exception caught in ContextCrudSLSBFacadeBean.retrieveAllReferences(Context context): " + e.toString());
            throw new RemoteException("RemoteException thrown in ContextCrudSLSBFacadeBean.retrieveAllReferences(Context context)" + e.toString(), e);
        }
    }

    public void retrieveAllReferencesInCollection(Context context) throws RemoteException {
        try {
            ContextServiceFacade contextServiceFacade = getContextServiceFacade(context);
            contextServiceFacade.retrieveAllReferencesInCollection(context);
        } catch (ServiceFacadeException e) {
            log.error("ServiceFacadeException caught in ContextCrudSLSBFacadeBean.retrieveAllReferencesInCollection(Context context): " + e.toString());
            throw new RemoteException("RemoteException thrown in ContextCrudSLSBFacadeBean.retrieveAllReferencesInCollection(Context context)" + e.toString(), e);
        } catch (Exception e) {
            log.error("Exception caught in ContextCrudSLSBFacadeBean.retrieveAllReferencesInCollection(Context context): " + e.toString());
            throw new RemoteException("RemoteException thrown in ContextCrudSLSBFacadeBean.retrieveAllReferencesInCollection(Context context)" + e.toString(), e);
        }
    }

    public void retrieveReferenceInCollection(Context context) throws RemoteException {
        try {
            ContextServiceFacade contextServiceFacade = getContextServiceFacade(context);
            contextServiceFacade.retrieveReferenceInCollection(context);
        } catch (ServiceFacadeException e) {
            log.error("ServiceFacadeException caught in ContextCrudSLSBFacadeBean.retrieveReferenceInCollection(Context context): " + e.toString());
            throw new RemoteException("RemoteException thrown in ContextCrudSLSBFacadeBean.retrieveReferenceInCollection(Context context)" + e.toString(), e);
        } catch (Exception e) {
            log.error("Exception caught in ContextCrudSLSBFacadeBean.retrieveReferenceInCollection(Context context): " + e.toString());
            throw new RemoteException("RemoteException thrown in ContextCrudSLSBFacadeBean.retrieveReferenceInCollection(Context context)" + e.toString(), e);
        }
    }

    public void retrievePathReference(Context context) throws RemoteException {
        try {
            ContextServiceFacade contextServiceFacade = getContextServiceFacade(context);
            contextServiceFacade.retrievePathReference(context);
        } catch (ServiceFacadeException e) {
            log.error("ServiceFacadeException caught in ContextCrudSLSBFacadeBean.retrievePathReference(Context context): " + e.toString());
            throw new RemoteException("RemoteException thrown in ContextCrudSLSBFacadeBean.retrievePathReference(Context context)" + e.toString(), e);
        } catch (Exception e) {
            log.error("Exception caught in ContextCrudSLSBFacadeBean.retrievePathReference(Context context): " + e.toString());
            throw new RemoteException("RemoteException thrown in ContextCrudSLSBFacadeBean.retrievePathReference(Context context)" + e.toString(), e);
        }
    }

    public void retrieveNullPathReference(Context context) throws RemoteException {
        try {
            ContextServiceFacade contextServiceFacade = getContextServiceFacade(context);
            contextServiceFacade.retrieveNullPathReference(context);
        } catch (ServiceFacadeException e) {
            log.error("ServiceFacadeException caught in ContextCrudSLSBFacadeBean.retrieveNullPathReference(Context context): " + e.toString());
            throw new RemoteException("RemoteException thrown in ContextCrudSLSBFacadeBean.retrieveNullPathReference(Context context)" + e.toString(), e);
        } catch (Exception e) {
            log.error("Exception caught in ContextCrudSLSBFacadeBean.retrieveNullPathReference(Context context): " + e.toString());
            throw new RemoteException("RemoteException thrown in ContextCrudSLSBFacadeBean.retrieveNullPathReference(Context context)" + e.toString(), e);
        }
    }

    public void retrieveNullReference(Context context) throws RemoteException {
        try {
            ContextServiceFacade contextServiceFacade = getContextServiceFacade(context);
            contextServiceFacade.retrieveNullReference(context);
        } catch (ServiceFacadeException e) {
            log.error("ServiceFacadeException caught in ContextCrudSLSBFacadeBean.retrieveNullReference(Context context): " + e.toString());
            throw new RemoteException("RemoteException thrown in ContextCrudSLSBFacadeBean.retrieveNullReference(Context context)" + e.toString(), e);
        } catch (Exception e) {
            log.error("Exception caught in ContextCrudSLSBFacadeBean.retrieveNullReference(Context context): " + e.toString());
            throw new RemoteException("RemoteException thrown in ContextCrudSLSBFacadeBean.retrieveNullReference(Context context)" + e.toString(), e);
        }
    }

    public void retrieveAllNullReferences(Context context) throws RemoteException {
        try {
            ContextServiceFacade contextServiceFacade = getContextServiceFacade(context);
            contextServiceFacade.retrieveAllNullReferences(context);
        } catch (ServiceFacadeException e) {
            log.error("ServiceFacadeException caught in ContextCrudSLSBFacadeBean.retrieveAllNullReferences(Context context): " + e.toString());
            throw new RemoteException("RemoteException thrown in ContextCrudSLSBFacadeBean.retrieveAllNullReferences(Context context)" + e.toString(), e);
        } catch (Exception e) {
            log.error("Exception caught in ContextCrudSLSBFacadeBean.retrieveAllNullReferences(Context context): " + e.toString());
            throw new RemoteException("RemoteException thrown in ContextCrudSLSBFacadeBean.retrieveAllNullReferences(Context context)" + e.toString(), e);
        }
    }

    public Collection findCollectionByNullFields(Context context) throws RemoteException {
        try {
            ContextServiceFacade contextServiceFacade = getContextServiceFacade(context);
            return contextServiceFacade.findCollectionByNullFields(context);
        } catch (ServiceFacadeException e) {
            log.error("ServiceFacadeException caught in ContextCrudSLSBFacadeBean.findCollectionByNullFields(Context context): " + e.toString());
            throw new RemoteException("RemoteException thrown in ContextCrudSLSBFacadeBean.findCollectionByNullFields(Context context)" + e.toString(), e);
        } catch (Exception e) {
            log.error("Exception caught in ContextCrudSLSBFacadeBean.findCollectionByNullFields(Context context): " + e.toString());
            throw new RemoteException("RemoteException thrown in ContextCrudSLSBFacadeBean.findCollectionByNullFields(Context context)" + e.toString(), e);
        }
    }

    /**
     * @deprecated use {@link #findCollectionByLogicCondition(org.apache.commons.chain.Context context)}
     */
    public Collection findLimitedOrderedCollectionByLogicCondition(Context context) throws RemoteException {
        try {
            ContextServiceFacade contextServiceFacade = getContextServiceFacade(context);
            return contextServiceFacade.findCollectionByLogicCondition(context);
        } catch (ServiceFacadeException e) {
            log.error("ServiceFacadeException caught in ContextCrudSLSBFacadeBean.findLimitedOrderedCollectionByLogicCondition(Context context): " + e.toString());
            throw new RemoteException("RemoteException thrown in ContextCrudSLSBFacadeBean.findLimitedOrderedCollectionByLogicCondition(Context context)" + e.toString(), e);
        } catch (Exception e) {
            log.error("Exception caught in ContextCrudSLSBFacadeBean.findLimitedOrderedCollectionByLogicCondition(Context context): " + e.toString());
            throw new RemoteException("RemoteException thrown in ContextCrudSLSBFacadeBean.findLimitedOrderedCollectionByLogicCondition(Context context) " + e.toString(), e);
        }
    }

    public Collection findCollectionByLogicCondition(Context context) throws RemoteException {
        try {
            ContextServiceFacade contextServiceFacade = getContextServiceFacade(context);
            return contextServiceFacade.findCollectionByLogicCondition(context);
        } catch (ServiceFacadeException e) {
            log.error("ServiceFacadeException caught in ContextCrudSLSBFacadeBean.findCollectionByLogicCondition(Context context): " + e.toString());
            throw new RemoteException("RemoteException thrown in ContextCrudSLSBFacadeBean.findCollectionByLogicCondition(Context context)" + e.toString(), e);
        } catch (Exception e) {
            log.error("Exception caught in ContextCrudSLSBFacadeBean.findCollectionByLogicCondition(Context context): " + e.toString());
            throw new RemoteException("RemoteException thrown in ContextCrudSLSBFacadeBean.findCollectionByLogicCondition(Context context) " + e.toString(), e);
        }
    }

    public Collection findCollectionByOrValues(Context context) throws RemoteException {
        try {
            ContextServiceFacade contextServiceFacade = getContextServiceFacade(context);
            return contextServiceFacade.findCollectionByOrValues(context);
        } catch (ServiceFacadeException e) {
            log.error("ServiceFacadeException caught in ContextCrudSLSBFacadeBean.findCollectionByOrValues(Context context): " + e.toString());
            throw new RemoteException("RemoteException thrown in ContextCrudSLSBFacadeBean.findCollectionByOrValues(Context context)" + e.toString(), e);
        } catch (Exception e) {
            log.error("Exception caught in ContextCrudSLSBFacadeBean.findCollectionByOrValues(Context context): " + e.toString());
            throw new RemoteException("RemoteException thrown in ContextCrudSLSBFacadeBean.findCollectionByOrValues(Context context) " + e.toString(), e);
        }
    }

    public Collection findCollectionByAndFieldsOperatorValues(Context context) throws RemoteException {
        try {
            ContextServiceFacade contextServiceFacade = getContextServiceFacade(context);
            return contextServiceFacade.findCollectionByAndFieldsOperatorValues(context);
        } catch (ServiceFacadeException e) {
            log.error("ServiceFacadeException caught in ContextCrudSLSBFacadeBean.findCollectionByAndFieldsOperatorValues(Context context): " + e.toString());
            throw new RemoteException("RemoteException thrown in ContextCrudSLSBFacadeBean.findCollectionByAndFieldsOperatorValues(Context context)" + e.toString(), e);
        } catch (Exception e) {
            log.error("Exception caught in ContextCrudSLSBFacadeBean.findCollectionByAndFieldsOperatorValues(Context context): " + e.toString());
            throw new RemoteException("RemoteException thrown in ContextCrudSLSBFacadeBean.findCollectionByAndFieldsOperatorValues(Context context) " + e.toString(), e);
        }
    }

    public Collection findCollectionByArrayOfFieldsOperatorsMatrixAndOrValues(Context context) throws RemoteException {
        try {
            ContextServiceFacade contextServiceFacade = getContextServiceFacade(context);
            return contextServiceFacade.findCollectionByArrayOfFieldsOperatorsMatrixAndOrValues(context);
        } catch (ServiceFacadeException e) {
            log.error("ServiceFacadeException caught in ContextCrudSLSBFacadeBean.findCollectionByArrayOfFieldsOperatorsMatrixAndOrValues(Context context): " + e.toString());
            throw new RemoteException("RemoteException thrown in ContextCrudSLSBFacadeBean.findCollectionByArrayOfFieldsOperatorsMatrixAndOrValues(Context context)" + e.toString(), e);
        } catch (Exception e) {
            log.error("Exception caught in ContextCrudSLSBFacadeBean.findCollectionByArrayOfFieldsOperatorsMatrixAndOrValues(Context context): " + e.toString());
            throw new RemoteException("RemoteException thrown in ContextCrudSLSBFacadeBean.findCollectionByArrayOfFieldsOperatorsMatrixAndOrValues(Context context) " + e.toString(), e);
        }
    }

    public Collection findCollectionByFieldsNotEqualsToValues(Context context) throws RemoteException {
        try {
            ContextServiceFacade contextServiceFacade = getContextServiceFacade(context);
            return contextServiceFacade.findCollectionByFieldsNotEqualsToValues(context);
        } catch (ServiceFacadeException e) {
            log.error("ServiceFacadeException caught in ContextCrudSLSBFacadeBean.findCollectionByFieldsNotEqualsToValues(Context context): " + e.toString());
            throw new RemoteException("RemoteException thrown in ContextCrudSLSBFacadeBean.findCollectionByFieldsNotEqualsToValues(Context context)" + e.toString(), e);
        } catch (Exception e) {
            log.error("Exception caught in ContextCrudSLSBFacadeBean.findCollectionByFieldsNotEqualsToValues(Context context): " + e.toString());
            throw new RemoteException("RemoteException thrown in ContextCrudSLSBFacadeBean.findCollectionByFieldsNotEqualsToValues(Context context) " + e.toString(), e);
        }
    }

    public Collection searchValueInFields(Context context) throws RemoteException {
        try {
            ContextServiceFacade contextServiceFacade = getContextServiceFacade(context);
            return contextServiceFacade.searchValueInFields(context);
        } catch (ServiceFacadeException e) {
            log.error("ServiceFacadeException caught in ContextCrudSLSBFacadeBean.searchValueInFields(Context context): " + e.toString());
            throw new RemoteException("RemoteException thrown in ContextCrudSLSBFacadeBean.searchValueInFields(Context context)" + e.toString(), e);
        } catch (Exception e) {
            log.error("Exception caught in ContextCrudSLSBFacadeBean.searchValueInFields(Context context): " + e.toString());
            throw new RemoteException("RemoteException thrown in ContextCrudSLSBFacadeBean.searchValueInFields(Context context) " + e.toString(), e);
        }
    }

    public Collection findCollectionByFieldInCollection(Context context) throws RemoteException {
        try {
            ContextServiceFacade contextServiceFacade = getContextServiceFacade(context);
            return contextServiceFacade.findCollectionByFieldInCollection(context);
        } catch (ServiceFacadeException e) {
            log.error("ServiceFacadeException caught in ContextCrudSLSBFacadeBean.findCollectionByFieldInCollection(Context context): " + e.toString());
            throw new RemoteException("RemoteException thrown in ContextCrudSLSBFacadeBean.findCollectionByFieldInCollection(Context context)" + e.toString(), e);
        } catch (Exception e) {
            log.error("Exception caught in ContextCrudSLSBFacadeBean.findCollectionByFieldInCollection(Context context): " + e.toString());
            throw new RemoteException("RemoteException thrown in ContextCrudSLSBFacadeBean.findCollectionByFieldInCollection(Context context) " + e.toString(), e);
        }
    }

    public Collection getCollectionOfStoredItemsNotInBean(Context context) throws RemoteException {
        try {
            ContextServiceFacade contextServiceFacade = getContextServiceFacade(context);
            return contextServiceFacade.getCollectionOfStoredItemsNotInBean(context);
        } catch (ServiceFacadeException e) {
            log.error("ServiceFacadeException caught in ContextCrudSLSBFacadeBean.getCollectionOfStoredItemsNotInBean(Context context): " + e.toString());
            throw new RemoteException("RemoteException thrown in ContextCrudSLSBFacadeBean.getCollectionOfStoredItemsNotInBean(Context context)" + e.toString(), e);
        } catch (Exception e) {
            log.error("Exception caught in ContextCrudSLSBFacadeBean.getCollectionOfStoredItemsNotInBean(Context context): " + e.toString());
            throw new RemoteException("RemoteException thrown in ContextCrudSLSBFacadeBean.getCollectionOfStoredItemsNotInBean(Context context) " + e.toString(), e);
        }
    }

    public Collection getStoredCollection(Context context) throws RemoteException {
        try {
            ContextServiceFacade contextServiceFacade = getContextServiceFacade(context);
            return contextServiceFacade.getStoredCollection(context);
        } catch (ServiceFacadeException e) {
            log.error("ServiceFacadeException caught in ContextCrudSLSBFacadeBean.getStoredCollection(Context context): " + e.toString());
            throw new RemoteException("RemoteException thrown in ContextCrudSLSBFacadeBean.getStoredCollection(Context context)" + e.toString(), e);
        } catch (Exception e) {
            log.error("Exception caught in ContextCrudSLSBFacadeBean.getStoredCollection(Context context): " + e.toString());
            throw new RemoteException("RemoteException thrown in ContextCrudSLSBFacadeBean.getStoredCollection(Context context) " + e.toString(), e);
        }
    }

    public Iterator getReportQueryIterator(Context context) throws RemoteException {
        try {
            ContextServiceFacade contextServiceFacade = getContextServiceFacade(context);
            return contextServiceFacade.getReportQueryIterator(context);
        } catch (ServiceFacadeException e) {
            log.error("ServiceFacadeException caught in ContextCrudSLSBFacadeBean.getReportQueryIterator(Context context): " + e.toString());
            throw new RemoteException("RemoteException thrown in ContextCrudSLSBFacadeBean.getReportQueryIterator(Context context)" + e.toString(), e);
        } catch (Exception e) {
            log.error("Exception caught in ContextCrudSLSBFacadeBean.getReportQueryIterator(Context context): " + e.toString());
            throw new RemoteException("RemoteException thrown in ContextCrudSLSBFacadeBean.getReportQueryIterator(Context context) " + e.toString(), e);
        }
    }

    /*********************** C R E A T E  start******************/
    public Object insert(Context context) throws RemoteException {
        try {
            ContextServiceFacade contextServiceFacade = getContextServiceFacade(context);
            return contextServiceFacade.insert(context);
        } catch (ServiceFacadeException e) {
            log.error("ServiceFacadeException caught in ContextCrudSLSBFacadeBean.insert(Context context): " + e.toString());
            throw new RemoteException("RemoteException thrown in ContextCrudSLSBFacadeBean.insert(Context context)" + e.toString(), e);
        } catch (Exception e) {
            log.error("Exception caught in ContextCrudSLSBFacadeBean.insert(Context context): " + e.toString());
            throw new RemoteException("RemoteException thrown in ContextCrudSLSBFacadeBean.insert(Context context)" + e.toString(), e);
        }
    }

    /*********************** U P D A T E  start******************/
    public Object update(Context context) throws RemoteException {
        try {
            ContextServiceFacade contextServiceFacade = getContextServiceFacade(context);
            return contextServiceFacade.update(context);
        } catch (ServiceFacadeException e) {
            log.error("ServiceFacadeException caught in ContextCrudSLSBFacadeBean.update(Context context): " + e.toString());
            throw new RemoteException("RemoteException thrown in ContextCrudSLSBFacadeBean.update(Context context)" + e.toString(), e);
        } catch (Exception e) {
            log.error("Exception caught in ContextCrudSLSBFacadeBean.update(Context context): " + e.toString());
            throw new RemoteException("RemoteException thrown in ContextCrudSLSBFacadeBean.update(Context context)" + e.toString(), e);
        }
    }

    public void updateCollection(Context context) throws RemoteException {
        try {
            ContextServiceFacade contextServiceFacade = getContextServiceFacade(context);
            contextServiceFacade.updateCollection(context);
        } catch (ServiceFacadeException e) {
            log.error("ServiceFacadeException caught in ContextCrudSLSBFacadeBean.updateCollection(Context context): " + e.toString());
            throw new RemoteException("RemoteException thrown in ContextCrudSLSBFacadeBean.updateCollection(Context context)" + e.toString(), e);
        } catch (Exception e) {
            log.error("Exception caught in ContextCrudSLSBFacadeBean.updateCollection(Context context): " + e.toString());
            throw new RemoteException("RemoteException thrown in ContextCrudSLSBFacadeBean.updateCollection(Context context)" + e.toString(), e);
        }
    }

    public Object updateCollectionReferences(Context context) throws RemoteException {
        try {
            ContextServiceFacade contextServiceFacade = getContextServiceFacade(context);
            return contextServiceFacade.updateCollectionReferences(context);
        } catch (ServiceFacadeException e) {
            log.error("ServiceFacadeException caught in ContextCrudSLSBFacadeBean.updateCollectionReferences(Context context): " + e.toString());
            throw new RemoteException("RemoteException thrown in ContextCrudSLSBFacadeBean.updateCollectionReferences(Context context)" + e.toString(), e);
        } catch (Exception e) {
            log.error("Exception caught in ContextCrudSLSBFacadeBean.updateCollectionReferences(Context context): " + e.toString());
            throw new RemoteException("RemoteException thrown in ContextCrudSLSBFacadeBean.updateCollectionReferences(Context context)" + e.toString(), e);
        }
    }

    public Object updateCollectionReference(Context context) throws RemoteException {
        try {
            ContextServiceFacade contextServiceFacade = getContextServiceFacade(context);
            return contextServiceFacade.updateCollectionReference(context);
        } catch (ServiceFacadeException e) {
            log.error("ServiceFacadeException caught in ContextCrudSLSBFacadeBean.updateCollectionReference(Context context): " + e.toString());
            throw new RemoteException("RemoteException thrown in ContextCrudSLSBFacadeBean.updateCollectionReference(Context context)" + e.toString(), e);
        } catch (Exception e) {
            log.error("Exception caught in ContextCrudSLSBFacadeBean.updateCollectionReference(Context context): " + e.toString());
            throw new RemoteException("RemoteException thrown in ContextCrudSLSBFacadeBean.updateCollectionReference(Context context)" + e.toString(), e);
        }
    }

    public void storePathCascade(Context context) throws RemoteException {
        try {
            ContextServiceFacade contextServiceFacade = getContextServiceFacade(context);
            contextServiceFacade.storePathCascade(context);
        } catch (ServiceFacadeException e) {
            log.error("ServiceFacadeException caught in ContextCrudSLSBFacadeBean.storePathCascade(Context context): " + e.toString());
            throw new RemoteException("RemoteException thrown in ContextCrudSLSBFacadeBean.storePathCascade(Context context)" + e.toString(), e);
        } catch (Exception e) {
            log.error("Exception caught in ContextCrudSLSBFacadeBean.storePathCascade(Context context): " + e.toString());
            throw new RemoteException("RemoteException thrown in ContextCrudSLSBFacadeBean.storePathCascade(Context context)" + e.toString(), e);
        }
    }

    public void storePathsCascade(Context context) throws RemoteException {
        try {
            ContextServiceFacade contextServiceFacade = getContextServiceFacade(context);
            contextServiceFacade.storePathsCascade(context);
        } catch (ServiceFacadeException e) {
            log.error("ServiceFacadeException caught in ContextCrudSLSBFacadeBean.storePathsCascade(Context context): " + e.toString());
            throw new RemoteException("RemoteException thrown in ContextCrudSLSBFacadeBean.storePathsCascade(Context context)" + e.toString(), e);
        } catch (Exception e) {
            log.error("Exception caught in ContextCrudSLSBFacadeBean.storePathsCascade(Context context): " + e.toString());
            throw new RemoteException("RemoteException thrown in ContextCrudSLSBFacadeBean.storePathsCascade(Context context)" + e.toString(), e);
        }
    }

    public Object updateCreateTrees(Context context) throws RemoteException {
        try {
            ContextServiceFacade contextServiceFacade = getContextServiceFacade(context);
            return contextServiceFacade.updateCreateTrees(context);
        } catch (ServiceFacadeException e) {
            log.error("ServiceFacadeException caught in ContextCrudSLSBFacadeBean.updateCreateTrees(Context context): " + e.toString());
            throw new RemoteException("RemoteException thrown in ContextCrudSLSBFacadeBean.updateCreateTrees(Context context)" + e.toString(), e);
        } catch (Exception e) {
            log.error("Exception caught in ContextCrudSLSBFacadeBean.updateCreateTrees(Context context): " + e.toString());
            throw new RemoteException("RemoteException thrown in ContextCrudSLSBFacadeBean.updateCreateTrees(Context context) " + e.toString(), e);
        }
    }

    /*********************** D E L E T E start******************/
    public void delete(Context context) throws RemoteException {
        try {
            ContextServiceFacade contextServiceFacade = getContextServiceFacade(context);
            contextServiceFacade.delete(context);
        } catch (ServiceFacadeException e) {
            log.error("ServiceFacadeException caught in ContextCrudSLSBFacadeBean.delete(Context context): " + e.toString());
            throw new RemoteException("RemoteException thrown in ContextCrudSLSBFacadeBean.delete(Context context)" + e.toString(), e);
        } catch (Exception e) {
            log.error("Exception caught in ContextCrudSLSBFacadeBean.delete(Context context): " + e.toString());
            throw new RemoteException("RemoteException thrown in ContextCrudSLSBFacadeBean.delete(Context context)" + e.toString(), e);
        }
    }

    public void deleteCollection(Context context) throws RemoteException {
        try {
            ContextServiceFacade contextServiceFacade = getContextServiceFacade(context);
            contextServiceFacade.deleteCollection(context);
        } catch (ServiceFacadeException e) {
            log.error("ServiceFacadeException caught in ContextCrudSLSBFacadeBean.deleteCollection(Context context): " + e.toString());
            throw new RemoteException("RemoteException thrown in ContextCrudSLSBFacadeBean.deleteCollection(Context context)" + e.toString(), e);
        } catch (Exception e) {
            log.error("Exception caught in ContextCrudSLSBFacadeBean.deleteCollection(Context context): " + e.toString());
            throw new RemoteException("RemoteException thrown in ContextCrudSLSBFacadeBean.deleteCollection(Context context)" + e.toString(), e);
        }
    }

    public void deleteMToNRelationshipCollection(Context context) throws RemoteException {
        try {
            ContextServiceFacade contextServiceFacade = getContextServiceFacade(context);
            contextServiceFacade.deleteMToNRelationshipCollection(context);
        } catch (ServiceFacadeException e) {
            log.error("ServiceFacadeException caught in ContextCrudSLSBFacadeBean.deleteMToNRelationshipCollection(Context context): " + e.toString());
            throw new RemoteException("RemoteException thrown in ContextCrudSLSBFacadeBean.deleteMToNRelationshipCollection(Context context)" + e.toString(), e);
        } catch (Exception e) {
            log.error("Exception caught in ContextCrudSLSBFacadeBean.deleteMToNRelationshipCollection(Context context): " + e.toString());
            throw new RemoteException("RemoteException thrown in ContextCrudSLSBFacadeBean.deleteMToNRelationshipCollection(Context context)" + e.toString(), e);
        }
    }

    public void deleteItemsNotInCollectionsInPath(Context context) throws RemoteException {
        try {
            ContextServiceFacade contextServiceFacade = getContextServiceFacade(context);
            contextServiceFacade.deleteItemsNotInCollectionsInPath(context);
        } catch (ServiceFacadeException e) {
            log.error("ServiceFacadeException caught in ContextCrudSLSBFacadeBean.deleteItemsNotInCollectionsInPath(Context context): " + e.toString());
            throw new RemoteException("RemoteException thrown in ContextCrudSLSBFacadeBean.deleteItemsNotInCollectionsInPath(Context context)" + e.toString(), e);
        } catch (Exception e) {
            log.error("Exception caught in ContextCrudSLSBFacadeBean.deleteItemsNotInCollectionsInPath(Context context): " + e.toString());
            throw new RemoteException("RemoteException thrown in ContextCrudSLSBFacadeBean.deleteItemsNotInCollectionsInPath(Context context)" + e.toString(), e);
        }
    }

    public void deleteItemsNotInCollectionsInPaths(Context context) throws RemoteException {
        try {
            ContextServiceFacade contextServiceFacade = getContextServiceFacade(context);
            contextServiceFacade.deleteItemsNotInCollectionsInPaths(context);
        } catch (ServiceFacadeException e) {
            log.error("ServiceFacadeException caught in ContextCrudSLSBFacadeBean.deleteItemsNotInCollectionsInPaths(Context context): " + e.toString());
            throw new RemoteException("RemoteException thrown in ContextCrudSLSBFacadeBean.deleteItemsNotInCollectionsInPaths(Context context)" + e.toString(), e);
        } catch (Exception e) {
            log.error("Exception caught in ContextCrudSLSBFacadeBean.deleteItemsNotInCollectionsInPaths(Context context): " + e.toString());
            throw new RemoteException("RemoteException thrown in ContextCrudSLSBFacadeBean.deleteItemsNotInCollectionsInPaths(Context context)" + e.toString(), e);
        }
    }

    public void deletePathCascade(Context context) throws RemoteException {
        try {
            ContextServiceFacade contextServiceFacade = getContextServiceFacade(context);
            contextServiceFacade.deletePathCascade(context);
        } catch (ServiceFacadeException e) {
            log.error("ServiceFacadeException caught in ContextCrudSLSBFacadeBean.deletePathCascade(Context context): " + e.toString());
            throw new RemoteException("RemoteException thrown in ContextCrudSLSBFacadeBean.deletePathCascade(Context context)" + e.toString(), e);
        } catch (Exception e) {
            log.error("Exception caught in ContextCrudSLSBFacadeBean.deletePathCascade(Context context): " + e.toString());
            throw new RemoteException("RemoteException thrown in ContextCrudSLSBFacadeBean.deletePathCascade(Context context)" + e.toString(), e);
        }
    }

    public void deletePathsCascade(Context context) throws RemoteException {
        try {
            ContextServiceFacade contextServiceFacade = getContextServiceFacade(context);
            contextServiceFacade.deletePathsCascade(context);
        } catch (ServiceFacadeException e) {
            log.error("ServiceFacadeException caught in ContextCrudSLSBFacadeBean.deletePathsCascade(Context context): " + e.toString());
            throw new RemoteException("RemoteException thrown in ContextCrudSLSBFacadeBean.deletePathsCascade(Context context)" + e.toString(), e);
        } catch (Exception e) {
            log.error("Exception caught in ContextCrudSLSBFacadeBean.deletePathsCascade(Context context): " + e.toString());
            throw new RemoteException("RemoteException thrown in ContextCrudSLSBFacadeBean.deletePathsCascade(Context context)" + e.toString(), e);
        }
    }
}
