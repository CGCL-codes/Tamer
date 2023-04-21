package com.googlecode.jazure.sdk.task;

import java.io.Serializable;

public interface Result extends Serializable, Searchable {

    boolean successful();
}
