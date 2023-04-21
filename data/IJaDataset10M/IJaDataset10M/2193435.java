package org.syrup.functions;

import org.syrup.Context;
import org.syrup.Function;
import org.syrup.Result;
import org.syrup.helpers.ResultImpl;
import org.syrup.helpers.Utils;
import java.util.logging.Logger;

/**
 * Copies the first input to the first output without consuming the first input.
 * 
 * @author Robbert van Dalen
 */
public class Constant implements Function {

    static final String COPYRIGHT = "Copyright 2005 Robbert van Dalen." + "At your option, you may copy, distribute, or make derivative works under " + "the terms of The Artistic License. This License may be found at " + "http://www.opensource.org/licenses/artistic-license.php. " + "THERE IS NO WARRANTY; USE THIS PRODUCT AT YOUR OWN RISK.";

    private static final Logger logger = Logger.getLogger("org.syrup.functions.Constant");

    /**
     */
    public Result execute(Context context) {
        try {
            ResultImpl im = null;
            if (Utils.isFull(context.in_1_link())) {
                return new ResultImpl(context, false, true, context.in_1_link().content(), null);
            } else {
                throw new Exception("first input must be filled");
            }
        } catch (Throwable e1) {
            return new ResultImpl(context, true, true, null, org.syrup.helpers.Utils.manageError(logger, e1, "Concat error"));
        }
    }
}
