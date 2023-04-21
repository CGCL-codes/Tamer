package eu.more.triggerservice.generated;

import org.soda.dpws.DPWSContext;
import org.soda.dpws.DPWSException;

public interface TriggerServicePort {

    String TNS = "http://www.ist-more.org/TriggerService/";

    public eu.more.triggerservice.generated.jaxb.ControlStarted StartControl(DPWSContext context, eu.more.triggerservice.generated.jaxb.StartControl parameters) throws DPWSException;

    public eu.more.triggerservice.generated.jaxb.ControlStopped StopControl(DPWSContext context) throws DPWSException;

    public eu.more.triggerservice.generated.jaxb.ValidateParameterResponse ValidateParameter(DPWSContext context) throws DPWSException;

    public eu.more.triggerservice.generated.jaxb.EndConditionFulfiledResponse EndConditionFulfiled(DPWSContext context) throws DPWSException;

    public eu.more.triggerservice.generated.jaxb.DataConditionFulfiledResponse DataConditionFulfiled(DPWSContext context) throws DPWSException;

    public eu.more.triggerservice.generated.jaxb.ControlStarted StartDefaultControl(DPWSContext context) throws DPWSException;
}
