package org.omg.CosNaming;

import gnu.CORBA.Minor;
import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.CompletionStatus;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.InvokeHandler;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.ResponseHandler;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.Servant;

/**
 * The binding iterator servant, used in POA-based naming service
 * implementations.
 * 
 * @since 1.4 
 *
 * @author Audrius Meskauskas, Lithuania (AudriusA@Bioinformatics.org)
 */
public abstract class BindingIteratorPOA extends Servant implements BindingIteratorOperations, InvokeHandler {

    /** @inheritDoc */
    public String[] _all_interfaces(POA poa, byte[] object_ID) {
        return new String[] { BindingIteratorHelper.id() };
    }

    /**
   * Call the required method.
   */
    public OutputStream _invoke(String method, InputStream in, ResponseHandler rh) {
        OutputStream out = null;
        if (method.equals("next_n")) {
            int amount = in.read_ulong();
            BindingListHolder a_list = new BindingListHolder();
            boolean result = next_n(amount, a_list);
            out = rh.createReply();
            out.write_boolean(result);
            BindingListHelper.write(out, a_list.value);
        } else if (method.equals("next_one")) {
            BindingHolder a_binding = new BindingHolder();
            boolean result = next_one(a_binding);
            out = rh.createReply();
            out.write_boolean(result);
            BindingHelper.write(out, a_binding.value);
        } else if (method.equals("destroy")) {
            destroy();
            out = rh.createReply();
        } else throw new BAD_OPERATION(method, Minor.Method, CompletionStatus.COMPLETED_MAYBE);
        return out;
    }

    /**
   * Get the CORBA object that delegates calls to this servant. The servant must
   * be already connected to an ORB.
   */
    public BindingIterator _this() {
        return BindingIteratorHelper.narrow(super._this_object());
    }

    /**
   * Get the CORBA object that delegates calls to this servant. Connect to the
   * given ORB, if needed.
   */
    public BindingIterator _this(org.omg.CORBA.ORB orb) {
        return BindingIteratorHelper.narrow(super._this_object(orb));
    }
}
