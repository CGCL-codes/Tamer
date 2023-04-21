package org.ofbiz.service.engine;

import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import javolution.util.FastMap;
import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.GeneralException;
import org.ofbiz.base.util.UtilDateTime;
import org.ofbiz.base.util.UtilProperties;
import org.ofbiz.base.util.UtilValidate;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.finder.PrimaryKeyFinder;
import org.ofbiz.entity.model.ModelEntity;
import org.ofbiz.entity.model.ModelField;
import org.ofbiz.service.DispatchContext;
import org.ofbiz.service.GenericServiceException;
import org.ofbiz.service.ModelParam;
import org.ofbiz.service.ModelService;
import org.ofbiz.service.ServiceDispatcher;
import org.ofbiz.service.ServiceUtil;

/**
 * Standard Java Static Method Service Engine
 */
public final class EntityAutoEngine extends GenericAsyncEngine {

    public static final String module = EntityAutoEngine.class.getName();

    public EntityAutoEngine(ServiceDispatcher dispatcher) {
        super(dispatcher);
    }

    /**
     * @see org.ofbiz.service.engine.GenericEngine#runSyncIgnore(java.lang.String, org.ofbiz.service.ModelService, java.util.Map)
     */
    public void runSyncIgnore(String localName, ModelService modelService, Map<String, Object> context) throws GenericServiceException {
        runSync(localName, modelService, context);
    }

    /**
     * @see org.ofbiz.service.engine.GenericEngine#runSync(java.lang.String, org.ofbiz.service.ModelService, java.util.Map)
     */
    public Map<String, Object> runSync(String localName, ModelService modelService, Map<String, Object> parameters) throws GenericServiceException {
        DispatchContext dctx = dispatcher.getLocalContext(localName);
        Map<String, Object> localContext = FastMap.newInstance();
        localContext.put("parameters", parameters);
        Map<String, Object> result = ServiceUtil.returnSuccess();
        if (modelService.invoke == null || (!"create".equals(modelService.invoke) && !"update".equals(modelService.invoke) && !"delete".equals(modelService.invoke))) {
            throw new GenericServiceException("In Service [" + modelService.name + "] the invoke value must be create, update, or delete for entity-auto engine");
        }
        if (UtilValidate.isEmpty(modelService.defaultEntityName)) {
            throw new GenericServiceException("In Service [" + modelService.name + "] you must specify a default-entity-name for entity-auto engine");
        }
        ModelEntity modelEntity = dctx.getDelegator().getModelEntity(modelService.defaultEntityName);
        if (modelEntity == null) {
            throw new GenericServiceException("In Service [" + modelService.name + "] the specified default-entity-name [" + modelService.defaultEntityName + "] is not valid");
        }
        try {
            boolean allPksInOnly = true;
            for (ModelField pkField : modelEntity.getPkFieldsUnmodifiable()) {
                ModelParam pkParam = modelService.getParam(pkField.getName());
                if (pkParam.isOut()) {
                    allPksInOnly = false;
                }
            }
            if ("create".equals(modelService.invoke)) {
                GenericValue newEntity = dctx.getDelegator().makeValue(modelEntity.getEntityName());
                boolean isSinglePk = modelEntity.getPksSize() == 1;
                boolean isDoublePk = modelEntity.getPksSize() == 2;
                Iterator<ModelField> pksIter = modelEntity.getPksIterator();
                ModelField singlePkModeField = isSinglePk ? pksIter.next() : null;
                ModelParam singlePkModelParam = isSinglePk ? modelService.getParam(singlePkModeField.getName()) : null;
                boolean isSinglePkIn = isSinglePk ? singlePkModelParam.isIn() : false;
                boolean isSinglePkOut = isSinglePk ? singlePkModelParam.isOut() : false;
                ModelParam doublePkPrimaryInParam = null;
                ModelParam doublePkSecondaryOutParam = null;
                ModelField doublePkSecondaryOutField = null;
                if (isDoublePk) {
                    ModelField firstPkField = pksIter.next();
                    ModelParam firstPkParam = modelService.getParam(firstPkField.getName());
                    ModelField secondPkField = pksIter.next();
                    ModelParam secondPkParam = modelService.getParam(secondPkField.getName());
                    if (firstPkParam.isIn() && secondPkParam.isOut()) {
                        doublePkPrimaryInParam = firstPkParam;
                        doublePkSecondaryOutParam = secondPkParam;
                        doublePkSecondaryOutField = secondPkField;
                    } else if (firstPkParam.isOut() && secondPkParam.isIn()) {
                        doublePkPrimaryInParam = secondPkParam;
                        doublePkSecondaryOutParam = firstPkParam;
                        doublePkSecondaryOutField = firstPkField;
                    } else {
                    }
                }
                if (isSinglePk && isSinglePkOut && !isSinglePkIn) {
                    String sequencedId = dctx.getDelegator().getNextSeqId(modelEntity.getEntityName());
                    newEntity.set(singlePkModeField.getName(), sequencedId);
                    result.put(singlePkModelParam.name, sequencedId);
                } else if (isSinglePk && isSinglePkOut && isSinglePkIn) {
                    Object pkValue = parameters.get(singlePkModelParam.name);
                    if (UtilValidate.isEmpty(pkValue)) {
                        pkValue = dctx.getDelegator().getNextSeqId(modelEntity.getEntityName());
                    } else {
                        if (pkValue instanceof String) {
                            StringBuffer errorDetails = new StringBuffer();
                            if (!UtilValidate.isValidDatabaseId((String) pkValue, errorDetails)) {
                                return ServiceUtil.returnError("The ID value in the parameter [" + singlePkModelParam.name + "] was not valid: " + errorDetails);
                            }
                        }
                    }
                    newEntity.set(singlePkModeField.getName(), pkValue);
                    result.put(singlePkModelParam.name, pkValue);
                } else if (isDoublePk && doublePkPrimaryInParam != null && doublePkSecondaryOutParam != null) {
                    newEntity.setPKFields(parameters, true);
                    dctx.getDelegator().setNextSubSeqId(newEntity, doublePkSecondaryOutField.getName(), 5, 1);
                    result.put(doublePkSecondaryOutParam.name, newEntity.get(doublePkSecondaryOutField.getName()));
                } else if (allPksInOnly) {
                    newEntity.setPKFields(parameters, true);
                } else {
                    throw new GenericServiceException("In Service [" + modelService.name + "] which uses the entity-auto engine with the create invoke option: " + "could not find a valid combination of primary key settings to do a known create operation; options include: " + "1. a single OUT pk for primary auto-sequencing, " + "2. a single INOUT pk for primary auto-sequencing with optional override, " + "3. a 2-part pk with one part IN (existing primary pk) and one part OUT (the secdonary pk to sub-sequence, " + "4. all pk fields are IN for a manually specified primary key");
                }
                ModelField fromDateField = modelEntity.getField("fromDate");
                if (fromDateField != null && fromDateField.getIsPk()) {
                    ModelParam fromDateParam = modelService.getParam("fromDate");
                    if (fromDateParam == null || (fromDateParam.isOptional() && parameters.get("fromDate") == null)) {
                        newEntity.set("fromDate", UtilDateTime.nowTimestamp());
                    }
                }
                newEntity.setNonPKFields(parameters, true);
                newEntity.create();
            } else if ("update".equals(modelService.invoke)) {
                if (!allPksInOnly) {
                    throw new GenericServiceException("In Service [" + modelService.name + "] which uses the entity-auto engine with the update invoke option not all pk fields have the mode IN");
                }
                GenericValue lookedUpValue = PrimaryKeyFinder.runFind(modelEntity, parameters, dctx.getDelegator(), false, true, null, null);
                if (lookedUpValue == null) {
                    return ServiceUtil.returnError("Value not found, cannot update");
                }
                localContext.put("lookedUpValue", lookedUpValue);
                ModelParam statusIdParam = modelService.getParam("statusId");
                ModelField statusIdField = modelEntity.getField("statusId");
                ModelParam oldStatusIdParam = modelService.getParam("oldStatusId");
                if (statusIdParam != null && statusIdParam.isIn() && oldStatusIdParam != null && oldStatusIdParam.isOut() && statusIdField != null) {
                    result.put("oldStatusId", lookedUpValue.get("statusId"));
                }
                String parameterStatusId = (String) parameters.get("statusId");
                if (statusIdParam != null && statusIdParam.isIn() && UtilValidate.isNotEmpty(parameterStatusId) && statusIdField != null) {
                    String lookedUpStatusId = (String) lookedUpValue.get("statusId");
                    if (UtilValidate.isNotEmpty(lookedUpStatusId) && !parameterStatusId.equals(lookedUpStatusId)) {
                        GenericValue statusValidChange = dctx.getDelegator().findOne("StatusValidChange", true, "statusId", lookedUpStatusId, "statusIdTo", parameterStatusId);
                        if (statusValidChange == null) {
                            return ServiceUtil.returnError(UtilProperties.getMessage("CommonUiLabels", "CommonErrorNoStatusValidChange", localContext, (Locale) parameters.get("locale")));
                        }
                    }
                }
                lookedUpValue.setNonPKFields(parameters, true);
                lookedUpValue.store();
            } else if ("delete".equals(modelService.invoke)) {
                if (!allPksInOnly) {
                    throw new GenericServiceException("In Service [" + modelService.name + "] which uses the entity-auto engine with the delete invoke option not all pk fields have the mode IN");
                }
                GenericValue lookedUpValue = PrimaryKeyFinder.runFind(modelEntity, parameters, dctx.getDelegator(), false, true, null, null);
                if (lookedUpValue != null) {
                    lookedUpValue.remove();
                }
            }
        } catch (GeneralException e) {
            String errMsg = "Error doing entity-auto operation for entity [" + modelEntity.getEntityName() + "] in service [" + modelService.name + "]: " + e.toString();
            Debug.logError(e, errMsg, module);
            return ServiceUtil.returnError(errMsg);
        }
        return result;
    }
}
