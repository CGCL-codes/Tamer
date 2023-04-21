package com.jeantessier.classreader;

import java.util.*;

public interface Exceptions_attribute extends Attribute_info {

    public Collection<? extends Class_info> getExceptions();
}
