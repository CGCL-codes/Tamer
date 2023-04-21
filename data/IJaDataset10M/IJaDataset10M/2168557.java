package org.monet.modelling.ide.builders.stages;

import java.io.FileInputStream;
import java.util.Collection;
import org.eclipse.core.resources.IResource;
import org.monet.modelling.ide.builders.BuilderResource;
import org.monet.modelling.ide.builders.IGlobalData;
import org.monet.modelling.ide.builders.Module;
import org.monet.modelling.ide.builders.Stage;
import org.monet.modelling.ide.builders.StageState;
import org.monet.modelling.ide.builders.errors.CantOpenFileError;
import org.monet.modelling.ide.builders.errors.NoFilesToCompileError;
import org.monet.modelling.ide.builders.errors.SyntaxError;
import org.monet.modelling.ide.constants.DefinitionTypes;
import org.monet.modelling.ide.library.LibraryFile;
import org.monet.modelling.ide.library.StreamHelper;
import org.monet.modelling.kernel.model.Model;
import org.monet.modelling.kernel.model.Package;
import org.simpleframework.xml.core.AttributeException;
import org.simpleframework.xml.core.ElementException;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.core.ValueRequiredException;

public class InitializeBuilder extends Stage {

    private BuilderResource builderResource;

    private Collection<IResource> resources;

    private IResource currentResource;

    @Override
    public void execute() {
        System.out.println("Initialize");
        this.state = StageState.COMPLETE;
        boolean executingStage = (Boolean) this.globalData.getData(IGlobalData.DEFINITIONS_CHANDED);
        if (!executingStage) return;
        Persister serializer = new Persister();
        this.builderResource = new BuilderResource();
        if (resources == null || resources.isEmpty()) {
            this.problems.add(new NoFilesToCompileError());
            this.state = StageState.NOT_EXECUTED;
            return;
        }
        for (IResource resource : resources) {
            try {
                Module module;
                if (DefinitionTypes.getTypeFromString(resource.getFileExtension()) == null) {
                    if (resource.getName().equals("model.xml")) {
                        deserializeModel(resource.getLocation().toString());
                    }
                    module = new Module(resource);
                    module.setNameDefinition(LibraryFile.getFilenameWithoutExtension(resource.getName()));
                    module.setCodeDefinition(LibraryFile.getFilenameWithoutExtension(resource.getName()));
                    module.setDefinitionType(DefinitionTypes.DEFINITION_OTHERS.toString());
                } else {
                    currentResource = resource;
                    module = new Module(resource);
                    Package packageElement = serializer.read(Package.class, currentResource.getLocation().toFile());
                    module.setDefinitionType(resource.getFileExtension());
                    module.setNameDefinition(packageElement.getDefinition().getName());
                    module.setCodeDefinition(packageElement.getDefinition().getCode());
                    module.setDefinition(packageElement.getDefinition());
                }
                if (resource.getFileExtension().equals("include")) module.setDefinitionType(DefinitionTypes.DEFINITION_OTHERS.toString());
                builderResource.addModule(module);
            } catch (ElementException e) {
                this.problems.add(new SyntaxError(resource, e));
            } catch (IllegalArgumentException e) {
                this.problems.add(new SyntaxError(resource, e));
            } catch (ValueRequiredException e) {
                this.problems.add(new SyntaxError(resource, e));
            } catch (AttributeException e) {
                this.problems.add(new SyntaxError(resource, e));
            } catch (Exception e) {
                System.out.println(String.format("ERROR. File: %s, Message: %s", currentResource.getName(), e.getMessage()));
                this.problems.add(new CantOpenFileError(resource, String.format("Exception type: %s. Message: %s.", e.getClass().getName(), e.getMessage())));
                continue;
            }
        }
    }

    @Override
    public Object getOutData() {
        return this.builderResource;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setInData(Object data) {
        resources = (Collection<IResource>) data;
    }

    private Model deserializeModel(String filename) throws Exception {
        Persister persister = new Persister();
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(filename);
            return persister.read(Model.class, inputStream);
        } finally {
            StreamHelper.close(inputStream);
        }
    }
}
