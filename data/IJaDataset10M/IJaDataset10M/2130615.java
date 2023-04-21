package org.jcvi.glk.elvira.report;

import java.util.Set;

public interface Report<T> {

    String getDescription();

    Set<T> getEntries();
}
