package org.modelingvalue.modelsync.parser.parser;

import org.modelingvalue.modelsync.messages.*;
import org.openarchitectureware.workflow.issues.Issues;
import static org.modelingvalue.modelsync.messages.CompilerMessageType.*;
import org.eclipse.emf.ecore.EObject;
import java.util.*;
import java.io.*;

/**
 * @author Wim Bast
 *
 */
public class XtextMessageHandler implements CompilerMessageHandler {

    private final Issues issues;

    private final Set<CompilerMessage> done = new HashSet<CompilerMessage>();

    private final Properties messages;

    /**
	 * @param element
	 */
    public XtextMessageHandler(Issues issues) {
        super();
        this.issues = issues;
        messages = new Properties();
        try {
            messages.load(new FileReader(XtextMessageHandler.class.getResource("messages.properties").getFile()));
        } catch (FileNotFoundException exc) {
        } catch (IOException exc) {
        }
    }

    private String getString(CompilerMessage message) {
        String string = messages.getProperty(message.getMessageId());
        if (string != null) {
            for (int i = 0; i < message.getMessageDetails().length; i++) {
                string = string.replace("$" + i, message.getMessageDetails()[i]);
            }
            return string;
        } else {
            return message.getMessageId() + " " + Arrays.toString(message.getMessageDetails());
        }
    }

    @Override
    public void handleMessage(CompilerMessage message) {
        if (done.add(message)) {
            if (message.getMessageType() == ERROR) {
                issues.addError(getString(message), message.getSourceInformation().getElement());
            } else {
                issues.addWarning(getString(message), message.getSourceInformation().getElement());
            }
        }
    }
}
