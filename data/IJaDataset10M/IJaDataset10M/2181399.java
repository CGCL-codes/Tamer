package jokeboxjunior.core.controller;

import cb_commonobjects.datastore.AbstractSQLDataProvider;
import cb_commonobjects.datastore.AbstractSQLDataProvider.SearchExpression.SearchEntity;
import cb_commonobjects.logging.GlobalLog;
import jokeboxjunior.core.model.AbstractModelObject;
import jokeboxjunior.core.model.links.AbstractModelLink;

/**
 *
 * @author B1
 */
public abstract class AbstractLinkController extends AbstractController {

    public abstract AbstractModelLink[] getObjects(Long thisId1_RenameMe, Long thisId2_RenameMe);

    public abstract AbstractModelObject[] getLinkedObjects1(Long thisId_RenameMe);

    public abstract AbstractModelObject[] getLinkedObjects2(Long thisId_RenameMe);

    public abstract AbstractModelLink linkObjects(Long thisId1_RenameMe, Long thisId2_RenameMe);

    public abstract boolean existsLink(Long thisId1_RenameMe, Long thisId2_RenameMe);

    protected abstract AbstractModelLink _loadLinkedObjects(AbstractModelLink thisObject);

    protected abstract boolean _checkLinkedObjects(AbstractModelLink thisObject);

    public AbstractModelLink loadLinkedObjects(AbstractModelLink thisObject) {
        AbstractModelLink myReturnObj = _loadLinkedObjects(thisObject);
        if (!_checkLinkedObjects(myReturnObj)) {
            GlobalLog.logError(java.util.ResourceBundle.getBundle("jokeboxjunior/core/res/ErrorMessages").getString("ERROR_CHECKING_LINK_OBJECT_") + this.myDefaultObject.getClass().getSimpleName() + " Id: " + thisObject.getId());
            myReturnObj = null;
        }
        return myReturnObj;
    }

    public AbstractModelLink[] _getObjectLinks(String thisFieldName1, Long thisId1, String thisFieldName2, Long thisId2) {
        AbstractSQLDataProvider.SearchExpression mySearchExpression = new AbstractSQLDataProvider.SearchExpression();
        mySearchExpression.add(new SearchEntity(thisFieldName1, thisId1));
        mySearchExpression.add(new SearchEntity(thisFieldName2, thisId2));
        return (AbstractModelLink[]) _searchObjectsByAttrib(mySearchExpression, null);
    }

    protected AbstractModelObject[] _getLinkedObjects(String thisFieldName, Long thisId, String thisAttribName, AbstractController thisLoadController) {
        AbstractModelObject[] myReturnObjs;
        AbstractModelLink myActLink;
        int i = 0;
        AbstractModelObject[] myLinkObjects = _searchObjectsByAttrib(thisFieldName, thisId, AbstractSQLDataProvider.COMP_EX_EQUALS, null);
        myReturnObjs = thisLoadController._getNewObjectArray(myLinkObjects.length);
        for (AbstractModelObject myActObj : myLinkObjects) {
            myActLink = (AbstractModelLink) myActObj;
            Long myForeignId = myActLink.getAttribLong(thisAttribName);
            myReturnObjs[i] = thisLoadController.getObject(myForeignId);
            i++;
        }
        return myReturnObjs;
    }

    protected boolean _existsLink(String thisFieldName1, Long thisId1, String thisFieldName2, Long thisId2) {
        AbstractSQLDataProvider.SearchExpression mySearchEx = new AbstractSQLDataProvider.SearchExpression();
        return (this._searchObjectIdsByAttrib(mySearchEx, thisFieldName2).length > 0);
    }

    protected AbstractModelLink _linkObjects(String thisFieldName1, Long thisId1, String thisFieldName2, Long thisId2) {
        AbstractModelLink myReturnObj = null;
        if (!_existsLink(thisFieldName1, thisId1, thisFieldName2, thisId2)) {
            String[] myAttribs = new String[] { thisFieldName1, thisFieldName2 };
            Long[] myValues = new Long[] { thisId1, thisId2 };
            myReturnObj = (AbstractModelLink) this._createNewObject(myAttribs, myValues, true);
        }
        return myReturnObj;
    }

    /**
     * @deprecated 
     * @param thisAttrib
     * @param thisValue
     * @return
     */
    @Override
    public boolean existsObject(String thisAttrib, Object thisValue) {
        throw new UnsupportedOperationException(java.util.ResourceBundle.getBundle("jokeboxjunior/core/res/ErrorMessages").getString("NOT_SUPPORTED_YET."));
    }

    /**
     * @deprecated
     * @param thisAttrib
     * @param thisValue
     * @return
     */
    @Override
    public AbstractModelObject getObject(String thisAttrib, Object thisValue) {
        throw new UnsupportedOperationException(java.util.ResourceBundle.getBundle("jokeboxjunior/core/res/ErrorMessages").getString("NOT_SUPPORTED_YET."));
    }

    /**
     * @deprecated
     * @param thisAttrib
     * @param thisValue
     * @return
     */
    @Override
    public AbstractModelObject[] searchObjectsByAttrib(String thisAttrib, Object thisValue, String thisComparator, String thisOrderByAttrib) {
        throw new UnsupportedOperationException(java.util.ResourceBundle.getBundle("jokeboxjunior/core/res/ErrorMessages").getString("NOT_SUPPORTED_YET."));
    }

    /**
     * @deprecated 
     * @param thisAttribs
     * @param thisValues
     * @param thisDoSave
     * @return
     */
    @Override
    public AbstractModelObject createNewObject(String[] thisAttribs, Object[] thisValues, boolean thisDoSave) {
        throw new UnsupportedOperationException(java.util.ResourceBundle.getBundle("jokeboxjunior/core/res/ErrorMessages").getString("NOT_SUPPORTED_YET."));
    }
}
