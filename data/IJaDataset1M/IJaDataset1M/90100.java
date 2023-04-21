package org.nomadpim.module.timetracking.model;

import java.util.List;

public interface ISpentTime {

    List<ISpentTime> getChildren();

    long getMilliseconds();

    String getTitle();
}
