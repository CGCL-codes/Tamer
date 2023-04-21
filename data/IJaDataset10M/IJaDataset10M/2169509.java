package org.netbeans.module.flexbean.modules.project;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectManager;
import org.netbeans.module.flexbean.api.platform.FlexPlatform;
import org.netbeans.module.flexbean.modules.project.properties.FlexProjectPropertiesBuilder;
import org.netbeans.spi.project.support.ant.AntProjectHelper;
import org.netbeans.spi.project.support.ant.GeneratedFilesHelper;
import org.netbeans.spi.project.support.ant.ProjectGenerator;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileSystem;
import org.openide.filesystems.FileUtil;
import org.openide.filesystems.Repository;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author arnaud
 */
public final class AntProjectHelperBuilder {

    private String _projectName;

    private String _mainClass;

    private File _projectFolder;

    private FlexPlatform _flexSdk;

    private String _subProjectType = FlexProject.PROJECT_TYPE_COMPONENT;

    public AntProjectHelper build() throws IOException {
        final AntProjectHelper[] antProjectHelper = new AntProjectHelper[1];
        final FileObject projectFolderFO = FileUtil.createFolder(_projectFolder);
        projectFolderFO.getFileSystem().runAtomicAction(new FileSystem.AtomicAction() {

            public void run() throws IOException {
                antProjectHelper[0] = _createAntProjectHelper(_projectName, projectFolderFO, "src", "build", _flexSdk, _mainClass);
                final Project flexProject = ProjectManager.getDefault().findProject(projectFolderFO);
                ProjectManager.getDefault().saveProject(flexProject);
                final FileObject srcFolderFO = projectFolderFO.createFolder("src");
                FileObject dest = projectFolderFO.createData(GeneratedFilesHelper.BUILD_XML_PATH);
                final URL url = AntProjectHelperBuilder.class.getResource("resources/build-impl.xml");
                InputStream in = url.openStream();
                OutputStream out = dest.getOutputStream();
                FileUtil.copy(in, out);
                in.close();
                out.close();
                if (_mainClass != null) {
                    _createMainClass(srcFolderFO, _mainClass);
                }
                return;
            }
        });
        return antProjectHelper[0];
    }

    public void setProjectName(String name) {
        _projectName = name;
    }

    public void setProjectFolder(File folder) {
        _projectFolder = folder;
    }

    public void setFlexPlatform(FlexPlatform platform) {
        _flexSdk = platform;
    }

    public void setMainFile(String mainClass) {
        _mainClass = mainClass;
        _subProjectType = FlexProject.PROJECT_TYPE_APPLICATION;
    }

    private void _createMainClass(FileObject sourcesFolder, String main) throws IOException {
        FileObject mainTemplate = Repository.getDefault().getDefaultFileSystem().findResource("Templates/Flex/Privileged/File.mxml");
        DataObject t = DataObject.find(mainTemplate);
        DataFolder folder = DataFolder.findFolder(sourcesFolder);
        t.createFromTemplate(folder, main);
    }

    private AntProjectHelper _createAntProjectHelper(String name, FileObject projectFolderFO, String srcDirName, String buildDirName, FlexPlatform flexSdk, String mainClass) throws IOException {
        final AntProjectHelper antProjectHelper = ProjectGenerator.createProject(projectFolderFO, FlexProject.FLEXPROJECTTYPE);
        Element data = antProjectHelper.getPrimaryConfigurationData(true);
        Document doc = data.getOwnerDocument();
        final FlexProjectPropertiesBuilder builder = new FlexProjectPropertiesBuilder(antProjectHelper);
        final Element nameEl = doc.createElementNS(FlexProject.PROJECT_CONFIGURATION_NAMESPACE, "name");
        nameEl.appendChild(doc.createTextNode(name));
        data.appendChild(nameEl);
        final Element srcEl = doc.createElementNS(FlexProject.PROJECT_CONFIGURATION_NAMESPACE, "source");
        srcEl.appendChild(doc.createTextNode(srcDirName));
        builder.setSourceDirectory(srcDirName);
        data.appendChild(srcEl);
        antProjectHelper.putPrimaryConfigurationData(data, true);
        builder.setBuildDirectory(buildDirName);
        builder.setFlexSdkRef(flexSdk.getName());
        builder.setProjectName(_projectName);
        builder.setProjectType(_subProjectType);
        if (_mainClass != null) {
            builder.setMainClass(_mainClass);
        }
        builder.build();
        return antProjectHelper;
    }
}
