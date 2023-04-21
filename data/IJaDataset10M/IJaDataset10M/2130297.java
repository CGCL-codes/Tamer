package org.modelingvalue.modelsync.parser.parser;

import org.eclipse.emf.ecore.EObject;
import org.modelingvalue.modelsync.messages.*;

/**
 * @author Wim Bast
 *
 */
public class SourceElement implements SourceInformation {

    private final EObject element;

    private final CompilerMessageHandler messageHandler;

    /**
	 * @param element
	 */
    public SourceElement(EObject element, CompilerMessageHandler messageHandler) {
        super();
        this.messageHandler = messageHandler;
        this.element = element;
    }

    @Override
    public Object getElement() {
        return element;
    }

    public EObject getEObject() {
        return element;
    }

    /**
	 * @return the messageHandler
	 */
    public CompilerMessageHandler getMessageHandler() {
        return messageHandler;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + element.hashCode();
        result = prime * result + messageHandler.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final SourceElement other = (SourceElement) obj;
        if (!element.equals(other.element)) return false; else if (!messageHandler.equals(other.messageHandler)) return false;
        return true;
    }
}
