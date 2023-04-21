package org.tigr.seq.seqdata;

import java.util.*;
import org.tigr.Facade.*;

/**
 *
 * This essentially just wraps an assembly that is associated with one
 * or more components involved in the viewing of the assembly so that
 * all components can be informed if there of a change in the assembly
 * being viewed.
 *
 * <p>
 * Copyright &copy; 2001 The Institute for Genomic Research (TIGR).
 * <p>
 * All rights reserved.
 * 
 * <pre>
 * $RCSfile: AssemblyCoordinator.java,v $
 * $Revision: 1.4 $
 * $Date: 2003/05/14 19:15:56 $
 * $Author: mholmes $
 * </pre>
 * 
 *
 * @author Miguel Covarrubias
 * @version 1.0 */
public class AssemblyCoordinator implements IAssemblyCoordinator {

    /**
     * The current assembly for this coordinator.
     *
     *
     */
    private IAssemblyFacade assembly;

    /**
     * A list of listeners for coordinator changes
     *
     *
     */
    private List<IAssemblyCoordinatorListener> listeners = new ArrayList<IAssemblyCoordinatorListener>();

    /**
     * Creates a new <code>AssemblyCoordinator</code> instance.
     *
     *
     */
    public AssemblyCoordinator() {
    }

    /**
     * Add a listener for coordinator changes.
     *
     *
     * @param pListener an <code>IAssemblyCoordinatorListener</code> value
     * 
     */
    public void addAssemblyCoordinatorListener(IAssemblyCoordinatorListener pListener) {
        this.listeners.add(pListener);
    }

    /**
     * Remove a listener for coordinator changes.
     *
     *
     * @param pListener an <code>IAssemblyCoordinatorListener</code> value
     * 
     */
    public void removeAssemblyCoordinatorListener(IAssemblyCoordinatorListener pListener) {
        this.listeners.remove(pListener);
    }

    /**
     * Get the current assembly
     *
     *
     * @return an <code>IBaseAssembly</code> value
     *
     */
    public IAssemblyFacade getAssembly() {
        return this.assembly;
    }

    /**
     * Set the current assembly, firing a coordinator change to any
     * registered listeners.
     *
     *
     * @param pNewAssembly an <code>IBaseAssembly</code> value
     * 
     */
    public void setAssembly(IAssemblyFacade pNewAssembly) {
        IAssemblyFacade oldAssembly = this.assembly;
        this.assembly = pNewAssembly;
        this.fireAssemblyChange(oldAssembly);
    }

    /**
     * Fire an assembly coordinator change to all registered
     * listeners.
     *
     *
     * @param pOldAssembly an <code>IBaseAssembly</code> value
     * 
     */
    private void fireAssemblyChange(IBaseAssembly pOldAssembly) {
        IAssemblyCoordinatorListener listener;
        Iterator iter = this.listeners.iterator();
        while (iter.hasNext()) {
            listener = (IAssemblyCoordinatorListener) iter.next();
            listener.assemblyCoordinatorChange(this.assembly, pOldAssembly);
        }
    }
}
