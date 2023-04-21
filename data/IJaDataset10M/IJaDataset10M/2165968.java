package edu.byu.ece.bitwidth.ptolemy.actor;

import edu.byu.ece.bitwidth.ptolemy.BitwidthDirector;
import edu.byu.ece.bitwidth.ptolemy.data.BitwidthToken;
import edu.byu.ece.bitwidth.ptolemy.strategies.CostFunction;
import ptolemy.actor.TypedIOPort;
import ptolemy.data.Token;
import ptolemy.kernel.CompositeEntity;
import ptolemy.kernel.util.IllegalActionException;
import ptolemy.kernel.util.NameDuplicationException;
import ptolemy.kernel.util.Workspace;

public class Multiply extends BitwidthActor {

    public Multiply(CompositeEntity container, String name) throws IllegalActionException, NameDuplicationException {
        super(container, name);
        a = new TypedIOPort(this, "a", true, false);
        b = new TypedIOPort(this, "b", true, false);
        output = new TypedIOPort(this, "output", false, true);
        output.setTypeAtLeast(a);
        output.setTypeAtLeast(b);
    }

    /** Input for tokens to be subtracted.  This is a multiport, and its
     *  type is inferred from the connections.
     */
    public TypedIOPort a;

    /** Output port.  The type is inferred from the connections.
     */
    public TypedIOPort output;

    /** Input for tokens to be added.  This is a multiport, and its
     *  type is inferred from the connections.
     */
    public TypedIOPort b;

    /** Override the base class to set type constraints on the ports.
     *  @param workspace The workspace into which to clone.
     *  @return A new instance of AddSubtract.
     *  @exception CloneNotSupportedException If a derived class contains
     *   an attribute that cannot be cloned.
     */
    public Object clone(Workspace workspace) throws CloneNotSupportedException {
        Multiply newObject = (Multiply) super.clone(workspace);
        newObject.output.setTypeAtLeast(newObject.a);
        newObject.output.setTypeAtLeast(newObject.b);
        return newObject;
    }

    /** If there is at least one token on the input ports, add
     *  tokens from the <i>plus</i> port, subtract tokens from the
     *  <i>minus</i> port, and send the result to the
     *  <i>output</i> port. At most one token is read
     *  from each channel, so if more than one token is pending, the
     *  rest are left for future firings.  If none of the input
     *  channels has a token, do nothing.  If none of the plus channels
     *  have tokens, then the tokens on the minus channels are subtracted
     *  from a zero token of the same type as the first token encountered
     *  on the minus channels.
     *
     *  @exception IllegalActionException If there is no director,
     *   or if addition and subtraction are not supported by the
     *   available tokens.
     */
    public void fire() throws IllegalActionException {
        super.fire();
        inA = a.get(0);
        inB = b.get(0);
        if (!(inA instanceof BitwidthToken)) inA = ((BitwidthDirector) getDirector()).newTokenInstance(inA);
        out = quantize(inA.multiply(inB));
        output.send(0, out);
    }

    private Token inA = null;

    private Token inB = null;

    private Token out = null;

    @Override
    public double getCost() {
        return CostFunction.calculateCost(inA, inB, out, this);
    }
}
