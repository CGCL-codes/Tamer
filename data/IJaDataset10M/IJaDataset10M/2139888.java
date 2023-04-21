package tools.io;

import java.io.*;
import java.util.*;

public class AllFileNameFilter implements FilenameFilter {

    public boolean accept(File dir, String name) {
        return true;
    }
}
