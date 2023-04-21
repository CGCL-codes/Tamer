package net.sourceforge.blogentis.modules.fragments;

import java.util.Arrays;
import net.sourceforge.blogentis.storage.VelocityFragment;
import org.apache.velocity.context.Context;

public class ListComments extends VelocityFragment {

    public VelocityFragment invoke(Context context, Object param1) {
        if (param1 instanceof Object[]) {
            param1 = Arrays.asList((Object[]) param1);
        }
        context.put("commentList", param1);
        return this;
    }
}
