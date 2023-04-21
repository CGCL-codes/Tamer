package com.migazzi.dm4j.repository;

import java.util.List;
import com.migazzi.dm4j.common.Artifact;
import com.migazzi.dm4j.common.Dependency;
import com.migazzi.dm4j.common.Type;

public interface Repository {

    public List<String> getOrganisationNames() throws RepositoryAccessException;

    public List<String> getArtifactNames(String organisationName) throws RepositoryAccessException;

    public List<String> getArtifactVersions(String organisationName, String artifactName) throws RepositoryAccessException;

    public List<Type> getArtifactTypes(String organisationName, String artifactName, String version) throws RepositoryAccessException;

    public List<Dependency> getDependencies(String organisationName, String artifactName, String version, Type type) throws RepositoryAccessException;

    public List<Dependency> getDependencies(Artifact artifact) throws RepositoryAccessException;

    public String download(Artifact artifact) throws RepositoryAccessException;

    public String getSourcesFileExtension();

    public boolean exists(Artifact artifact) throws RepositoryAccessException;

    public boolean exists(String organisationName, String artifactName, String version, Type type) throws RepositoryAccessException;
}
