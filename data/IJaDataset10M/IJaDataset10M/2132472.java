package org.netbeans.module.flexbean.modules.project.dependency;

import java.io.File;
import java.util.Properties;
import org.netbeans.api.project.Project;
import org.netbeans.module.flexbean.modules.project.FlexProjectHelper;
import org.netbeans.module.flexbean.modules.project.FlexProjectProperties;
import org.netbeans.spi.project.support.ant.AntProjectHelper;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;

/**
 *
 * @author arnaud
 */
public final class FlexProjectCompoDependency implements FlexProjectDependency {

    public static final String TYPE_PROJECT = "project";

    private String _projectName;

    private boolean _isShared = false;

    private FlexProjectCompoDependency(String name) {
        _projectName = name;
    }

    FlexProjectCompoDependency(Project project) {
        _projectName = project.getProjectDirectory().getName();
    }

    static FlexProjectCompoDependency create(Properties props) {
        FlexProjectCompoDependency dependency = null;
        dependency = new FlexProjectCompoDependency(props.getProperty("project"));
        dependency._isShared = Boolean.valueOf(props.getProperty("isshared"));
        return dependency;
    }

    public String getProjectName() {
        return _projectName;
    }

    public String getResourceLocation() {
        FileObject fo = getResource();
        return FileUtil.toFile(fo).getAbsolutePath();
    }

    public FileObject getResource() {
        final Project project = FlexProjectHelper.findOpenedProject(_projectName);
        final AntProjectHelper helper = project.getLookup().lookup(AntProjectHelper.class);
        final String buildName = helper.getProperties(AntProjectHelper.PROJECT_PROPERTIES_PATH).getProperty(FlexProjectProperties.PROJECT_BUILD_PATH);
        File file = new File(FileUtil.toFile(project.getProjectDirectory()), buildName);
        file = new File(file, _projectName + ".swc");
        return FileUtil.toFileObject(file);
    }

    public String getType() {
        return TYPE_PROJECT;
    }

    public boolean isSharedDependency() {
        return _isShared;
    }

    public String getName() {
        return getProjectName();
    }
}
