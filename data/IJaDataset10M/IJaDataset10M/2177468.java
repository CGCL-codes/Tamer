package ti.plato.logcontrol;

import java.io.Externalizable;

/**
 * This interface is created to represent the state of an object in the IPageModel.
 * 
 * For TTIF Trace Streams, the state is a Boolean object for checked or unchecked.
 * Same goes for the Primitives.  For GSP traces, the state to save could be a
 * numerical value.
 *
 */
public interface IPageModelItemState extends Externalizable {
}
