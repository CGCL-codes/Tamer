package fsm;

import variableinformation.GenericLinkableInformation;

public class TransitionForFSM extends GenericLinkableInformation {

    public String sourceid;

    public String sourcename;

    public GenericLinkableInformation sourceState;

    public String targetid;

    public String targetname;

    public GenericLinkableInformation targetState;

    public String condition;

    public String assignment;
}
