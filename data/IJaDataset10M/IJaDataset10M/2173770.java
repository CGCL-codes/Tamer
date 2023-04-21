package com.mindtree.techworks.infix.plugins.sfnetmvnrepo.mapper.sfnetsftp;

import java.util.StringTokenizer;
import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;
import org.codehaus.plexus.context.Context;
import org.codehaus.plexus.context.ContextException;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.Contextualizable;
import com.mindtree.techworks.infix.plugins.sfnetmvnrepo.SfNetMvnMojoInfo;
import com.mindtree.techworks.infix.plugins.sfnetmvnrepo.mapper.MapGenerationException;
import com.mindtree.techworks.infix.plugins.sfnetmvnrepo.mapper.MapGenerator;
import com.mindtree.techworks.infix.plugins.sfnetmvnrepo.model.repoconfig.SourceForgeFRSMapper;
import com.mindtree.techworks.infix.plugins.sfnetmvnrepo.model.repomap.Directory;
import com.mindtree.techworks.infix.plugins.sfnetmvnrepo.model.repomap.RepositoryMap;

/**
 * Parses the SourceForge FRS system to generate the map
 * 
 * @author Bindul Bhowmik
 * @version $Revision: 91 $ $Date: 2010-12-14 17:31:05 -0500 (Tue, 14 Dec 2010) $
 */
@Component(role = MapGenerator.class, hint = "sfnet-frs")
public class SfNetFRSParserMapGenerator implements MapGenerator<SourceForgeFRSMapper>, Contextualizable {

    /**
	 * The role hint to get this mapper
	 */
    public static final String ROLE_HINT = "sfnet-frs";

    /**
	 * Used to look up the user info component
	 */
    private PlexusContainer container;

    @Override
    public RepositoryMap generateRepositoryMap(SourceForgeFRSMapper configuration, SfNetMvnMojoInfo mojoInfo) throws MapGenerationException {
        RepositoryMap repoMap = new RepositoryMap();
        repoMap.setRedirectBase(configuration.getBaseUrl());
        Directory mapWorkingDir = processBaseGroupId(repoMap, configuration.getBaseGroupId());
        try {
            DirectoryScanner dirScanner = container.lookup(DirectoryScanner.class, "sftp");
            dirScanner.initialize(configuration, mojoInfo);
            dirScanner.scan(mapWorkingDir);
        } catch (ComponentLookupException e) {
            mojoInfo.getLog().error("Error parsing FRS!", e);
            throw new MapGenerationException("Error getting scanner", e);
        }
        return repoMap;
    }

    /**
	 * Creates directory tree for base group id if present
	 */
    private Directory processBaseGroupId(RepositoryMap repoMap, String baseGroupId) {
        Directory workingDir = repoMap;
        if (!isNullOrEmpty(baseGroupId)) {
            StringTokenizer tokenizer = new StringTokenizer(baseGroupId, ".");
            while (tokenizer.hasMoreTokens()) {
                String groupPart = tokenizer.nextToken();
                Directory newDir = new Directory();
                newDir.setId(groupPart);
                newDir.setName(groupPart);
                workingDir.addDirectory(newDir);
                workingDir = newDir;
            }
        }
        if (workingDir != repoMap) {
            workingDir.setRedirectBase(repoMap.getRedirectBase());
        }
        return workingDir;
    }

    protected static boolean isNullOrEmpty(String test) {
        return (null == test || test.trim().length() == 0);
    }

    @Override
    public void contextualize(Context context) throws ContextException {
        this.container = (PlexusContainer) context.get("plexus");
    }
}
