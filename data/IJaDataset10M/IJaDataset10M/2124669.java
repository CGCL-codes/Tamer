package org.xteam.cs.gui.lex;

import org.xteam.cs.gui.IResourceAdapterFactory;
import org.xteam.cs.gui.views.IResourceAdapter;
import org.xteam.cs.model.ProjectResource;

public class TestLexerResourceAdapterFactory implements IResourceAdapterFactory {

    @Override
    public Class<? extends ProjectResource> getManagedClass() {
        return TestLexerResource.class;
    }

    @Override
    public IResourceAdapter createAdapter(ProjectResource resource) {
        return new TestLexerResourceAdapter((TestLexerResource) resource);
    }
}
