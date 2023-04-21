package L1;

import L1.MessageEvent;

/**
 * A transition trigger associated with an any receive event specifies that the transition is to be triggered by the receipt of any message that is not explicitly referenced in another transition from the same vertex.
 */
public interface AnyReceiveEvent extends MessageEvent {
}
