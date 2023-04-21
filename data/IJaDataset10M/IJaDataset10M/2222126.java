package uk.ac.ncl.cs.instantsoap.wsapi.impl;

import org.bjv2.util.serviceprovider.*;
import uk.ac.ncl.cs.instantsoap.mapprocessor.*;
import uk.ac.ncl.cs.instantsoap.wsapi.*;
import static uk.ac.ncl.cs.instantsoap.wsapi.Wsapi.*;
import java.util.*;

/**
 * A service that echoes inputs to outputs.
 *
 * @author Matthew Pocock
 */
@SpiProvider
public class EchoingMapProcessor implements MapProcessor {

    private String application;

    private Set<MetaData> slots;

    public EchoingMapProcessor() {
        this("echoMap", asSet(metaData("name", "your name"), metaData("age", "your age (in years")));
    }

    public EchoingMapProcessor(String application, Set<MetaData> slots) {
        this.application = application;
        this.slots = slots;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public Set<MetaData> getSlots() {
        return slots;
    }

    public void setSlots(Set<MetaData> slots) {
        this.slots = slots;
    }

    public boolean handlesApplication(String application) {
        return application != null && this.application != null && application.equals(this.application);
    }

    public List<String> listApplications() {
        return Collections.singletonList(application);
    }

    public MetaData describeApplication(String application) throws UnknownApplicationException {
        return metaData(application, "Echo all inputs to outputs");
    }

    public Map<String, String> process(String application, Map<String, String> inputs) throws JobExecutionException, UnknownApplicationException {
        return inputs;
    }

    public void validate(String application, Map<String, String> inputs) throws InvalidJobSpecificationException, UnknownApplicationException {
    }

    public Set<MetaData> getInputs(String application) throws UnknownApplicationException {
        return slots;
    }

    public Set<MetaData> getOutputs(String application) throws UnknownApplicationException {
        return slots;
    }
}
