package net.sf.buildbox.devmodel.impl;

import net.sf.buildbox.buildrobot.api.StructureDao;
import net.sf.buildbox.buildrobot.model.CodeRoot;
import net.sf.buildbox.buildrobot.model.VcsLocation;
import java.util.*;

public class StructureDaoConfigImpl implements StructureDao {

    private Map<VcsLocation, CodeRoot> coderootsByLocation = new LinkedHashMap<VcsLocation, CodeRoot>();

    public List<CodeRoot> listCodeRoots() {
        return new ArrayList<CodeRoot>(coderootsByLocation.values());
    }

    public Set<String> listCodeRootPathsFor(String vcsId) {
        final Set<String> result = new HashSet<String>();
        for (CodeRoot codeRoot : coderootsByLocation.values()) {
            final VcsLocation vcsLocation = codeRoot.getVcsLocation();
            if (vcsLocation.getVcsId().equals(vcsId)) {
                result.add(vcsLocation.getLocationPath());
            }
        }
        return result;
    }

    public CodeRoot findCodeRootContaining(VcsLocation vcsLocation) {
        final String vcsLocationStr = vcsLocation.toString();
        for (Map.Entry<VcsLocation, CodeRoot> entry : coderootsByLocation.entrySet()) {
            final VcsLocation coderoot = entry.getKey();
            final String coderootStr = coderoot.toString();
            if (coderootStr.equals(vcsLocationStr)) {
                return entry.getValue();
            }
            if (vcsLocationStr.startsWith(coderootStr + "/")) {
                return entry.getValue();
            }
        }
        return null;
    }

    public void setCoderoots(Set<CodeRoot> coderoots) {
        final List<CodeRoot> sortedCoderoots = new ArrayList<CodeRoot>(coderoots);
        Collections.sort(sortedCoderoots, new Comparator<CodeRoot>() {

            public int compare(CodeRoot p1, CodeRoot p2) {
                return p1.getVcsLocation().toString().compareTo(p2.getVcsLocation().toString());
            }
        });
        for (CodeRoot coderoot : sortedCoderoots) {
            coderootsByLocation.put(coderoot.getVcsLocation(), coderoot);
        }
    }

    public void createCodeRoot(CodeRoot codeRoot) {
        throw new UnsupportedOperationException("cannot write back to config");
    }

    public void updateCodeRoot(CodeRoot transientInstance) {
        throw new UnsupportedOperationException("cannot write back to config");
    }

    public void deleteCodeRoot(CodeRoot codeRoot) {
        throw new UnsupportedOperationException("cannot write back to config");
    }
}
