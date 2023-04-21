package com.c4j.assembly;

import com.c4j.composition.IFacetReference;
import com.c4j.linker.IReference;
import com.c4j.type.IType;
import com.c4j.workspace.IFragment;

public interface IMain extends IAssemblyElement, IFacetReference {

    @Override
    ILibrary getParent();

    IType getType();

    IFragment getFragment();

    IReference<IFragment> getFragmentReference();
}
