package com.daffodilwoods.daffodildb.server.datasystem.interfaces;

import java.util.Comparator;
import com.daffodilwoods.database.resource.DException;

public interface _Comparator {

    Comparator getComparator();

    Comparator getObjectComparator() throws DException;
}
