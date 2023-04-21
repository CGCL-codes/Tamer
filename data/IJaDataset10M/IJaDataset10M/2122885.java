package org.monet.modelling.ide.contentypes.describers;

import java.io.IOException;
import java.io.InputStream;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.content.IContentDescriber;
import org.eclipse.core.runtime.content.IContentDescription;

public class DeclarationServiceContentDescription implements IContentDescriber {

    @Override
    public int describe(InputStream contents, IContentDescription description) throws IOException {
        return 0;
    }

    @Override
    public QualifiedName[] getSupportedOptions() {
        return null;
    }
}
