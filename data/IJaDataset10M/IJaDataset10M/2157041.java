package nz.ac.waikato.modeljunit.storytest;

import java.util.List;

public interface Suggestion {

    public List<String> getFields();

    public void selected();

    public boolean equals(Object o);

    public int hashCode();
}
