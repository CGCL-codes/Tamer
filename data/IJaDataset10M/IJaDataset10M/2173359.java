package org.modelingvalue.modelsync.elements;

import org.modelingvalue.modelsync.messages.*;

/**
 * @author Wim Bast
 *
 */
public class CompilerMessageImpl extends MessageImpl<CompilerMessageType> implements CompilerMessage {

    /**
	 * @param id
	 * @param type
	 * @param sourceInfo
	 * @param details
	 */
    protected CompilerMessageImpl(CompilerMessageType type, String id, SourceInformation sourceInfo, String... details) {
        super(id, type, sourceInfo, details);
    }
}
