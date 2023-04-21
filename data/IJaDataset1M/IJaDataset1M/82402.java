package org.codeandmagic.affected.service;

import java.util.List;
import org.codeandmagic.affected.svn.SvnException;
import org.codeandmagic.affected.svn.SvnProject;

public interface SvnProjectService {

    SvnProject get(String name) throws SvnException;

    List<SvnProject> getAll();

    SvnProject create(String name, String url, long lastCheckedVersion) throws SvnException;

    boolean save(SvnProject project);

    boolean delete(String name) throws SvnException;
}
