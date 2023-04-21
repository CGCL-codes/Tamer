package com.idna.trace.utils.parameters.client;

public class FormParametersImpl implements FormParameters {

    private BusinessFormParameters businessFormParameters;

    private PeopleFormParameters peopleFormParameters;

    private RequestInfoParameters requestInfoParameters;

    @Override
    public BusinessFormParameters getBusinessFormParameters() {
        return businessFormParameters;
    }

    @Override
    public PeopleFormParameters getPeopleFormParameters() {
        return peopleFormParameters;
    }

    @Override
    public RequestInfoParameters getRequestInfoParameters() {
        return requestInfoParameters;
    }

    @Override
    public void setBusinessFormParameters(BusinessFormParameters businessFormParameters) {
        this.businessFormParameters = businessFormParameters;
    }

    @Override
    public void setPeopleFormParameters(PeopleFormParameters peopleFormParameters) {
        this.peopleFormParameters = peopleFormParameters;
    }

    @Override
    public void setRequestInfoParameters(RequestInfoParameters requestInfoParameters) {
        this.requestInfoParameters = requestInfoParameters;
    }
}
