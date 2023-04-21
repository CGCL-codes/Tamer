package org.genxdm.xs.types;

/**
 * Indicates that this type is a processing-instruction node.
 * 
 */
public interface ProcessingInstructionNodeType extends NodeType {

    /**
     * Returns the name of the processing-instruction.
     */
    String getName();
}
