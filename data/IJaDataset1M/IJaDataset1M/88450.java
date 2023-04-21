package com.sphinx4;

import ade.ADEServer;
import com.interfaces.SpeechRecognitionServer;
import java.rmi.*;
import java.util.ArrayList;

/**
 * Defines a basic set of methods that must be defined in an
 * ADEServer that utilizes Sphinx4 speech recognition.
 */
public interface Sphinx4BaseServer extends ADEServer, SpeechRecognitionServer {

    /** This method will change Sphinx4's configuration.
	 * @param config the path to the XML configuration file.
	 * @throws RemoteException if the remote call fails. */
    public void changeSphinx4Configuration(String config) throws RemoteException;

    /** This method will change the JSGF grammar Sphinx4 is using.
	 * @param grammar The name of the grammar to use.
	 * @throws RemoteException if the remote call fails.
	 */
    public void changeSphinx4Grammar(String grammar) throws RemoteException;

    /** Gets Sphinx4's most recently recognized text and time of recognition.
	 * Note that elements must be cast appropriately upon return (a String
	 * and a long).
	 * @return An {@link java.util.ArrayList ArrayList}, where the first
	 * element is the recognized text (a String, initially "") and the second
	 * element the time of recognition (a long, which is -1 until something is
	 * recognized). */
    public ArrayList getSphinx4Text() throws RemoteException;
}
