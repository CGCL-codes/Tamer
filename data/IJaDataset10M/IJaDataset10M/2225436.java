package build;

import java.io.Serializable;

public abstract class Builder implements Serializable {

    public abstract void Build();

    public abstract void WriteToFile(String filename);
}
