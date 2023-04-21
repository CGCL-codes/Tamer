package es.caib.bpm.beans.home;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import es.caib.bpm.beans.local.BPMEngineLocal;

public interface BPMEngineLocalHome extends EJBLocalHome {

    public BPMEngineLocal create() throws CreateException;
}
