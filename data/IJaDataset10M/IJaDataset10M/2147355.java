package edu.kgi.biobridge.sbwdriver;

import edu.kgi.biobridge.gum.ECallFailure;
import edu.caltech.sbw.DataBlockWriter;

/**
 * The BlockWriteable interface is used internally, and signifies
 * that a data object can write itself to a DataBlockWriter, eliminating
 * the need for a separate packer/unpacker system.
 * 
 * @author Cameron Wellock
 */
interface BlockWriteable {

    /**
 * Write the object to a DataBlockWriter.
 * @param writer Writer object to write to.
 * @throws ECallFailure
 */
    public void writeTo(DataBlockWriter writer) throws ECallFailure;
}
