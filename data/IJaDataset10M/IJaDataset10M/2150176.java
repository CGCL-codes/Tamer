package org.metatemplate;

import java.util.List;

public interface WarningListener {

    public void warn(String source, int line, int column, String message);

    public List<String> getWarnings();

    public void clear();
}
