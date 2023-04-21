package org.rob.confjsflistener.multiphaselistener.config;

/**
 * @author Roberto
 *
 */
public interface CheckPhaseBooleanParameter {

    /**
	 * @return the global
	 */
    public Boolean getGlobal();

    /**
	 * @return the restoreView
	 */
    public Boolean getRestoreView();

    /**
	 * @return the applyRequestValues
	 */
    public Boolean getApplyRequestValues();

    /**
	 * @return the processValidations
	 */
    public Boolean getProcessValidations();

    /**
	 * @return the updateModel
	 */
    public Boolean getUpdateModel();

    /**
	 * @return the invokeApplication
	 */
    public Boolean getInvokeApplication();

    /**
	 * @return the renderResponse
	 */
    public Boolean getRenderResponse();

    /**
	 * @param global the global to set
	 */
    public void setGlobal(Boolean global);

    /**
	 * @param restoreView the restoreView to set
	 */
    public void setRestoreView(Boolean restoreView);

    /**
	 * @param applyRequestValues the applyRequestValues to set
	 */
    public void setApplyRequestValues(Boolean applyRequestValues);

    /**
	 * @param processValidations the processValidations to set
	 */
    public void setProcessValidations(Boolean processValidations);

    /**
	 * @param updateModel the updateModel to set
	 */
    public void setUpdateModel(Boolean updateModel);

    /**
	 * @param invokeApplication the invokeApplication to set
	 */
    public void setInvokeApplication(Boolean invokeApplication);

    /**
	 * @param renderResponse the renderResponse to set
	 */
    public void setRenderResponse(Boolean renderResponse);
}
