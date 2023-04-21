package com.objectwave.appArch;

import java.beans.*;
import com.objectwave.event.*;

public abstract class PresentationModel extends EventSupport implements PresentationModelIF {

    /**
	*/
    public void addPresentationModel(PresentationModelIF pm) {
        if (pm == this) return;
        addConsumer(pm);
        addProducer(pm);
    }
}
