package deduced.idlgenerator;

import java.util.Iterator;
import deduced.generator.GeneratorStep;
import deduced.generator.ProjectData;
import deduced.generator.codeblock.CodeProject;
import deduced.generator.codeblock.Folder;
import deduced.idlconverter.IdlObjectConverter;

/**
 * <p>
 * Title: IdlFileGenerator
 * </p>
 * <p>
 * Description: IdlFileGenerator
 * </p>
 */
public class IdlFileGenerator extends GeneratorStep {

    private IdlNamingParameters _idlNamingParameters;

    public IdlFileGenerator() {
    }

    /**
     * @return Returns the idlNamingParameters.
     */
    public IdlNamingParameters getIdlNamingParameters() {
        return _idlNamingParameters;
    }

    /**
     * @param idlNamingParameters The idlNamingParameters to set.
     */
    public void setIdlNamingParameters(IdlNamingParameters idlNamingParameters) {
        _idlNamingParameters = idlNamingParameters;
    }

    public void generateStep() {
        CodeProject codeProject = getProjectGenerator().getCodeProject();
        Folder rootFolder = codeProject.getRootFolder();
        createIdlFile(rootFolder);
    }

    /**
     * @param destination
     * @param rootFolder
     * @return
     */
    private IdlFile createIdlFile(Folder rootFolder) {
        IdlFile idlFile = (IdlFile) getProjectGenerator().getRootObjectConverter().getConvertedObjectMap().getPropertyValue(IdlObjectConverter.IDL_FILE_INSTANCE.getKey());
        rootFolder.getFileList().addPropertyInstance(null, null, idlFile);
        idlFile.setStereotype(IdlGeneratorConstants.IDL_FILE);
        ProjectData projectData = (ProjectData) getProjectGenerator().getRootObjectConverter().getSourceObject();
        idlFile.setLinkedObject(findConvertedObject(projectData, IdlObjectConverter.IDL_LIBRARY_INSTANCE.getKey()));
        idlFile.getIncludeFileList().addPropertyValue(IdlBasicTypes.IDL_BASIC_FILE);
        Iterator it = projectData.getDependantProjectList().iteratorByValue();
        while (it.hasNext()) {
            ProjectData dependantProject = (ProjectData) it.next();
            IdlFile includeFile = (IdlFile) findConvertedObject(dependantProject, IdlObjectConverter.IDL_FILE_INSTANCE.getKey());
            idlFile.getIncludeFileList().addPropertyValue(includeFile);
        }
        return idlFile;
    }
}
