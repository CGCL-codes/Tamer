package org.rubypeople.rdt.refactoring.core.renamemodule;

import java.util.ArrayList;
import java.util.Collection;
import org.jruby.ast.IScopingNode;
import org.rubypeople.rdt.refactoring.core.ModuleNodeProvider;
import org.rubypeople.rdt.refactoring.editprovider.FileMultiEditProvider;
import org.rubypeople.rdt.refactoring.editprovider.IMultiFileEditProvider;
import org.rubypeople.rdt.refactoring.editprovider.MultiFileEditProvider;
import org.rubypeople.rdt.refactoring.editprovider.ScopingNodeRenameEditProvider;
import org.rubypeople.rdt.refactoring.nodewrapper.ModuleNodeWrapper;

public class RenameModuleEditProvider implements IMultiFileEditProvider {

    private final RenameModuleConfig config;

    public RenameModuleEditProvider(RenameModuleConfig config) {
        this.config = config;
    }

    private ScopingNodeRenameEditProvider getModuleEditProvider() {
        ArrayList<IScopingNode> modules = new ArrayList<IScopingNode>();
        for (ModuleNodeWrapper node : config.getModuleParts()) {
            modules.add(node.getWrappedNode());
        }
        return new ScopingNodeRenameEditProvider(modules, config.getNewName());
    }

    private ModuleMethodDefRenameEditProvider getModuleModuleMethodDefEditProvider() {
        return new ModuleMethodDefRenameEditProvider(ModuleNodeProvider.getAllModuleMethodDefinitions(config.getModuleParts()), config.getNewName());
    }

    private IncludeRenameEditProvider getIncludeRenameEditProvider() {
        return new IncludeRenameEditProvider(config);
    }

    public Collection<FileMultiEditProvider> getFileEditProviders() {
        MultiFileEditProvider fileEdits = new MultiFileEditProvider();
        fileEdits.addEditProviders(getModuleEditProvider().getEditProviders());
        fileEdits.addEditProviders(getModuleModuleMethodDefEditProvider().getEditProviders());
        fileEdits.addEditProviders(getIncludeRenameEditProvider().getEditProviders());
        return fileEdits.getFileEditProviders();
    }
}
