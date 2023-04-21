package org.norecess.nolatte.interpreters;

import org.norecess.nolatte.ast.Datum;
import org.norecess.nolatte.ast.IGroupOfData;
import org.norecess.nolatte.ast.visitors.DatumVisitor;
import org.norecess.nolatte.environments.IEnvironment;
import org.norecess.nolatte.types.IDataTypeFilter;

@Deprecated
public interface IApplicationFactory {

    DatumVisitor<Datum> create(IDataTypeFilter dataTypeFilter, IEnvironment environment, IGroupOfData data);
}
