package desmoj.core.report;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * Prints the given messages as HTML files to disc. The HTML file contains a
 * table with each message being displayed in a row. This class only reads the
 * message's description attribute and stores it. Other types of messages should
 * be handled by special output classes derived from this class.
 * 
 * @version DESMO-J, Ver. 2.3.4beta copyright (c) 2012
 * @author Tim Lechler
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License. You
 * may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 *
 */
public abstract class StandardFileOut implements MessageReceiver {

    /**
	 * The name of the file produced by this StandardFileOut.
	 */
    private String _name;

    /**
	 * The actual FileWriter used to wirte the messages to disc.
	 */
    protected Writer fileOut;

    /**
	 * Creates a StandardFileOut to print messages sent to this object to a file
	 * to disc enclosing them in a HTML for the given experiment. The parameters
	 * specify the Experiment that this messagereceiver serves for and the name
	 * of the file to be created.
	 * 
	 * @param fileName
	 *            String : The name of the file to write output to
	 */
    public StandardFileOut(String fileName) {
        super();
        if (!(fileName.endsWith(".html")) && !(fileName.endsWith(".HTML"))) _name = fileName + ".html"; else _name = fileName;
        try {
            fileOut = new BufferedWriter(new FileWriter(_name));
        } catch (IOException ioEx) {
            System.out.println("IOException thrown : " + ioEx);
            System.out.println("description: Can't create file " + _name + ".html");
            System.out.println("origin     : Experiment auxiliaries");
            System.out.println("location   : constructor of class StandardFileOut");
            System.out.println("hint       : Check access to the file and" + " that it is not in use by some other application.");
            System.out.println("The System will not be shut down. But the file " + fileName + " can not be created and important data " + "might be lost!");
        }
    }

    /**
	 * Implement this method to define how the information carried in the given
	 * message is represented in a file.
	 */
    public abstract void receive(Message m);

    /**
	 * Implement this method to define how the information carried in the given
	 * reporter is represented in a file.
	 */
    public abstract void receive(Reporter r);
}
