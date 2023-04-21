package tapestryassistant.core;

import java.util.HashMap;
import java.util.List;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;

/**
 * ÿ��java����ӳ���������
 * 
 * @author lw
 */
public class DataMappingManager {

    private static DataMappingManager _instance = new DataMappingManager();

    private HashMap<IProject, DataMapping> mappings = new HashMap<IProject, DataMapping>();

    private HashMap<IProject, List<IPath>> srcMapping = new HashMap<IProject, List<IPath>>();

    public static DataMappingManager instance() {
        return _instance;
    }

    public DataMapping getMapping(IProject proj) {
        return mappings.get(proj);
    }

    public void addMapping(IProject proj, DataMapping mapping) {
        if (proj == null || mapping == null) return;
        if (mappings.containsKey(proj)) return;
        mappings.put(proj, mapping);
    }

    public void updateMapping(IProject proj, DataMapping mapping) {
        if (proj == null || mapping == null) return;
        if (mappings.containsKey(proj)) mappings.remove(proj);
        mappings.put(proj, mapping);
    }

    public void removeMapping(IProject proj) {
        mappings.remove(proj);
    }

    public void removeSrcPath(IProject proj) {
        srcMapping.remove(proj);
    }

    public void addSrcPath(IProject proj, List<IPath> paths) {
        if (proj == null || paths == null) return;
        if (srcMapping.containsKey(proj)) return;
        srcMapping.put(proj, paths);
    }

    public List<IPath> getSrcPath(IProject proj) {
        return srcMapping.get(proj);
    }

    public void updateSrcPath(IProject proj, List<IPath> paths) {
        if (proj == null || paths == null) return;
        if (srcMapping.containsKey(proj)) srcMapping.remove(proj);
        srcMapping.put(proj, paths);
    }
}
