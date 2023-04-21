package com.daffodilwoods.daffodildb.server.datadictionarysystem;

import java.util.ArrayList;
import com.daffodilwoods.database.resource.*;

public interface _PrimaryAndUniqueConstraintCharacteristics {

    _UniqueConstraint getPrimaryConstraints() throws DException;

    _UniqueConstraint[] getUniqueConstraints() throws DException;

    _UniqueConstraint[] getConstraints() throws DException;
}
