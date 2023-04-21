package com.ivis.xprocess.web.framework;

import java.io.File;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import com.ivis.xprocess.core.Parameter;
import com.ivis.xprocess.core.Pattern;
import com.ivis.xprocess.framework.XchangeElement;
import com.ivis.xprocess.framework.vcs.auth.VCSPasswordAuth;
import com.ivis.xprocess.web.framework.LicenseManager.Perspective;

public interface ISessionDatasource extends IWebDataSource {

    TransientElement getTransientElement(String uuid);

    TransientElement getTransientElementFromMap(String uuid);

    void requestCommit(File[] files) throws Exception;

    void saveAndCommitDirtyElements();

    void setDirty(TransientXchangeElement txe);

    void setClean(TransientXchangeElement txe);

    SVNClientManager getClientManager();

    void commit(File[] files);

    void logout();

    VCSPasswordAuth getVCSAuth();

    Perspective[] getPerspectives();

    void setPerspectives(Perspective[] perspectives);

    Pattern instantiatePattern(Pattern pattern, XchangeElement parent, Parameter... parameters) throws Exception;
}
