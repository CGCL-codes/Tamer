package com.bluebrim.base.shared;

import java.rmi.*;
import java.util.*;
import com.bluebrim.collection.shared.*;

public interface CoVerifiableCollectionIF extends CoPropertyChangeListener, List, Remote {

    public void each(CoCollections.EachDo doer);

    public void verifyAgainstElements(Object el);
}
