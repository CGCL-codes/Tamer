package ssv.interaction.model;

import java.util.List;

public interface Interaction {

    public Instance getTarget();

    public Instance getCaller();

    public Instance getResult();

    public boolean isExit();

    public boolean isStatic();

    public String getSimpleName();

    public <T extends Instance> List<T> getArguments();

    public String getName();
}
