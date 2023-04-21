package apollo.datamodel;

import java.util.*;

public interface AssemblyFeatureI {

    public abstract int getFragmentOffset();

    public abstract void setFragmentOffset(int start);

    public abstract String getChromosome();

    public abstract void setChromosome(String chr);
}
