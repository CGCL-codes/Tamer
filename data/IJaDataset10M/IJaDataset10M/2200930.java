package org.norecess.nolatte.ast.support;

import java.io.Serializable;
import java.util.List;
import org.norecess.nolatte.ast.IIdentifier;

public interface IParameters extends Serializable {

    List<IIdentifier> getPositionals();

    List<IIdentifier> getNamed();

    boolean hasRest();

    IIdentifier getRest();

    IParameters addPositional(IIdentifier... identifiers);

    IParameters addNamed(IIdentifier... identifiers);

    IParameters addRest(IIdentifier identifier);
}
