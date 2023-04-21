package org.deved.antlride.antlr.runtime310;

import java.io.File;
import java.io.IOException;
import org.deved.antlride.antlr.runtime310.AntlrRuntime310;
import org.deved.antlride.core.AntlrConstants;
import org.deved.antlride.core.AntlrCore;
import org.deved.antlride.core.antlr.IANTLRRuntimeService;
import org.deved.antlride.core.util.AntlrCoreHelper;
import org.eclipse.core.runtime.IPath;
import org.osgi.framework.Bundle;

public class ANTLRRuntimeServiceImpl310 implements IANTLRRuntimeService {

    private static final String LIB = "lib";

    private static final String ANTLR_3 = "antlr-3.1.jar";

    private static final String ANTLR_3_RUNTIME = "antlr-runtime-3.1.jar";

    private static final String ANTLR_2 = "antlr-2.7.7.jar";

    private static final String STRING_TEMPLATE = "stringtemplate-3.2.jar";

    private static final String[] RUNTIME_JARS = { ANTLR_2, ANTLR_3, ANTLR_3_RUNTIME, STRING_TEMPLATE };

    public IPath[] deployRuntime(IPath home, IPath grammarPath) {
        IPath homePath = getDeployPath(home);
        IPath[] buildpath = internalDeployRuntime(homePath);
        File grammarFolder = new File(homePath.append(grammarPath.removeLastSegments(1)).toOSString());
        if (!grammarFolder.exists()) {
            grammarFolder.mkdirs();
        }
        return buildpath;
    }

    public IPath getDeployPath(IPath home) {
        StringBuilder runtimeVersion = new StringBuilder("r_");
        runtimeVersion.append(getVersion().replace('.', '_'));
        return home.append(runtimeVersion.toString());
    }

    protected IPath[] internalDeployRuntime(IPath homePath) {
        IPath libPath = homePath.append(LIB);
        IPath[] buildpath = new IPath[RUNTIME_JARS.length];
        File libFolder = new File(libPath.toOSString());
        if (!libFolder.exists()) {
            libFolder.mkdirs();
        }
        Bundle bundle = AntlrRuntime310.getDefault().getBundle();
        for (int i = 0; i < RUNTIME_JARS.length; i++) {
            try {
                IPath bpath = libPath.append(RUNTIME_JARS[i]);
                buildpath[i] = bpath;
                File file = bpath.toFile();
                String bundleFile = LIB + '/' + RUNTIME_JARS[i];
                AntlrCoreHelper.copyFileFromBundle(bundle, bundleFile, file);
            } catch (IOException e) {
                AntlrCore.error(e);
            }
        }
        return buildpath;
    }

    public String getVersion() {
        return AntlrConstants.ANTLR_RUNTIME_3_1_0;
    }

    public String getDescription() {
        return "v3.1.0";
    }

    public String getReleaseDate() {
        return "ANTLR version 3.1.0, released August 12, 2008";
    }
}
