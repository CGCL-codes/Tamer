package poemtrainer.model;

import java.util.*;

public interface ModelListener extends EventListener {

    public void poetSetupChanged(ModelEvent evt);

    public void newPoemAvailable(ModelEvent evt);
}
