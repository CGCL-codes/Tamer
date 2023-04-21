package org.dea;

import org.dea.facade.DEAFacades;
import org.dea.util.ProcessOutput;

public interface DistributedEasyAccept {

    public DEAFacades generateFacades(String sourceDir, String facadeFullQualifiedName, String xmlMapppingFile) throws Exception;

    public ProcessOutput runDistributedEasyAccept(String projectPath, String xmlMapppingFile, String classPath, DEAFacades facades, String easyAcceptScript) throws Exception;
}
