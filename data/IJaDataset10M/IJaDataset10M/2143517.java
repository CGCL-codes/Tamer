package org.tigr.cloe.model.facade.assemblerFacade;

/**
 *
 * An interface to be implemented by classes representing messages
 * from the assembler.
 *
 * <p>
 * Copyright &copy; 2002 The Institute for Genomic Research (TIGR).
 * <p>
 * All rights reserved.
 * 
 * <pre>
 * $RCSfile: IAssemblerStatusMessage.java,v $
 * $Revision: 1.3 $
 * $Date: 2002/05/15 15:43:16 $
 * $Author: mcovarr $
 * </pre>
 * 
 *
 * @author Miguel Covarrubias
 * @version 1.0 */
public interface IAssemblerStatusMessage {

    /**
     * Does this message correspond to an assembler error?
     *
     *
     * @return a <code>boolean</code> value
     *
     * @exception AssemblerException if an error occurs
     * */
    public boolean isAssemblerError() throws AssemblerException;

    /**
     * Does this message correspond to an assembler warning?
     *
     *
     * @return a <code>boolean</code> value
     *
     * @exception AssemblerException if an error occurs
     *
     */
    public boolean isAssemblerWarning() throws AssemblerException;

    /**
     * Does this message correspond to an application warning?
     *
     *
     * @return a <code>boolean</code> value
     *
     * @exception AssemblerException if an error occurs
     *
     */
    public boolean isApplicationWarning() throws AssemblerException;

    /**
     * Does this message correspond to an application error?
     *
     *
     * @return a <code>boolean</code> value
     *
     * @exception AssemblerException if an error occurs
     *
     */
    public boolean isApplicationError() throws AssemblerException;

    /**
     * Getter for the message text, if any.
     *
     *
     * @return a <code>String</code> value
     *
     * @exception AssemblerException if an error occurs
     *
     */
    public String getMessageText() throws AssemblerException;

    /**
     * Getter for the message id.
     *
     *
     * @return an <code>int</code> value
     *
     * @exception AssemblerException if an error occurs
     *
     */
    public int getMessageID() throws AssemblerException;
}
