package com.neurogrid.simulation.root;

/**
 * Copyright (C) 2000 NeuroGrid <sam@neurogrid.com><br><br>
 *
 * This interface defines functionality for message handling objects.<br><br>
 *
 * Change History<br>
 * ------------------------------------------------------------------------------------<br>
 * 0.0   17/Mar/2003    sam       Created file<br>
 *
 * @author Sam Joseph (sam@neurogrid.com)
 */
public interface MessageHandler {

    /**
     * handle a message arriving at a particular node
     *
     * @param p_message    the incoming message
     * @param p_node       the node doing the processing
     *
     * @return boolean     flag to indicate a successful operation
	 * @throws Exception   a general exception
     */
    public boolean handleMessage(Message p_message, Node p_node) throws Exception;

    /**
     * inject a message into a particular node
     *
     * @param p_message    the incoming message
     * @param p_node       the node doing the processing
     *
     * @return boolean     flag to indicate a successful operation
 	 * @throws Exception   a general exception
     */
    public boolean injectMessage(Message p_message, Node p_node) throws Exception;
}
