package org.rubypeople.rdt.refactoring.core.movefield;

import java.util.Collection;
import org.rubypeople.rdt.refactoring.core.renamefield.FieldRenamer;
import org.rubypeople.rdt.refactoring.core.renamefield.RenameFieldConditionChecker;
import org.rubypeople.rdt.refactoring.core.renamefield.RenameFieldConfig;
import org.rubypeople.rdt.refactoring.documentprovider.DocumentWithIncluding;
import org.rubypeople.rdt.refactoring.editprovider.EditProvider;
import org.rubypeople.rdt.refactoring.editprovider.FileEditProvider;
import org.rubypeople.rdt.refactoring.editprovider.FileMultiEditProvider;
import org.rubypeople.rdt.refactoring.editprovider.IMultiFileEditProvider;
import org.rubypeople.rdt.refactoring.editprovider.MultiFileEditProvider;

public class MoveFieldEditProvider implements IMultiFileEditProvider {

    private final MoveFieldConfig config;

    public MoveFieldEditProvider(MoveFieldConfig config) {
        this.config = config;
    }

    public Collection<FileMultiEditProvider> getFileEditProviders() {
        MultiFileEditProvider providers = new MultiFileEditProvider();
        addTargetAccessorGenerator(providers);
        addSourceAccessorGenerator(providers);
        addFieldRenamers(providers);
        return providers.getFileEditProviders();
    }

    private void addFieldRenamers(MultiFileEditProvider providers) {
        RenameFieldConfig renameFieldConfig = new RenameFieldConfig(new DocumentWithIncluding(config.getDocumentProvider()), config.getPos());
        new RenameFieldConditionChecker(renameFieldConfig);
        renameFieldConfig.setDoRenameAccessorMethods(false);
        renameFieldConfig.setDoRenameAccessors(false);
        renameFieldConfig.setNewName(config.getTargetReference() + '.' + config.getSelectedFieldName());
        FieldRenamer renamer = new FieldRenamer(renameFieldConfig);
        for (FileMultiEditProvider fileMultiEditProvider : renamer.getFileEditProviders()) {
            for (EditProvider editProvider : fileMultiEditProvider.getEditProviders()) {
                providers.addEditProvider(new FileEditProvider(fileMultiEditProvider.getFileName(), editProvider));
            }
        }
    }

    private void addTargetAccessorGenerator(MultiFileEditProvider providers) {
        GenerateAccessorsAtTarget generateAccessors = new GenerateAccessorsAtTarget(config.getDocumentProvider(), config.getTargetClass(), config.getSelectedFieldName());
        providers.addEditProvider(new FileEditProvider(generateAccessors.getFileName(), generateAccessors.getEditProvider()));
    }

    private void addSourceAccessorGenerator(MultiFileEditProvider providers) {
        GenerateAccessorAtSource accessorAtSource = new GenerateAccessorAtSource(config);
        for (EditProvider edit : accessorAtSource.getEditProviders()) {
            providers.addEditProvider(new FileEditProvider(config.getDocumentProvider().getActiveFileName(), edit));
        }
    }
}
