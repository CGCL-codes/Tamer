package com.idna.trace.utils.parameters.client;

public interface FormParameters {

    public BusinessFormParameters getBusinessFormParameters();

    public PeopleFormParameters getPeopleFormParameters();

    public RequestInfoParameters getRequestInfoParameters();

    public void setBusinessFormParameters(BusinessFormParameters businessFormParameters);

    public void setPeopleFormParameters(PeopleFormParameters peopleFormParameters);

    public void setRequestInfoParameters(RequestInfoParameters requestInfoParameters);
}
