package clutrfree;

import java.io.File;
import java.io.IOException;

public interface Module {

    String getDescription();

    boolean testType(File dirFile);

    int nClusters(File dirFile) throws IOException;

    BasicData readData(File dirfile) throws IOException;
}
