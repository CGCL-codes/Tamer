package bexee.model.process;

import java.util.List;
import javax.wsdl.Definition;

/**
 * This class represents a BPEL process with all necessary additional
 * information, e.g. its WSDL description, partner WSDLs etc.
 * 
 * @version $Revision: 1.1 $, $Date: 2004/12/15 14:18:09 $
 * @author Patric Fornasier
 * @author Pawel Kowalski
 */
public interface BPELProcess {

    /**
     * Add a partner's WSDL description.
     * 
     * @param wsdl
     */
    public void addPartnerWSDL(Definition wsdl);

    /**
     * Add partner's WSDL descriptions.
     * 
     * @param wsdl
     */
    public void addPartnerWSDL(List wsdl);

    /**
     * Get a list of all receive activities which might cause a process instance
     * creation.
     * 
     * @return
     */
    public List getCreationReceives();

    /**
     * Convencience method to get the name of the underlying process. If this
     * method is called, before a <code>Process</code> is set, it will return
     * null.
     * 
     * @return the name of the underlying <code>Process</code> or null.
     */
    public String getName();

    /**
     * Get a list of Partner's WSDLs.
     * 
     * @return
     */
    public List getPartnerWSDL();

    /**
     * Get the pure BPEL process.
     * 
     * @return
     */
    public Process getProcess();

    /**
     * Get the pure BPEL prcess' WSDL.
     * 
     * @return
     */
    public Definition getWSDL();

    /**
     * Is this process synchronous. A BPEL process is synchronous iff it has a
     * Reply activity.
     * 
     * @return
     */
    public boolean isSynchronous();

    /**
     * Set the pure BPEL process.
     * 
     * @param process
     */
    public void setProcess(Process process);

    /**
     * Set this <code>BPELProcess</code>' synchronous property.
     * 
     * @param synchronous
     */
    public void setSynchronous(boolean synchronous);

    /**
     * Set this <code>BPELProcess</code>' own WSDL description.
     * 
     * @param wsdl
     */
    public void setWSDL(Definition wsdl);
}
