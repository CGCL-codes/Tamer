package com.softsizo.data;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class RepositoryTest {

    private static final String UUID = "123456789";

    private static final String REPO_ROOT = "http://repo.test.com";

    private static final String BRANCH = "/trunk/module";

    private Repository repository;

    @Before
    public void setUpRepository() {
        repository = new Repository().setLatestRevision(99).setRoot(REPO_ROOT).setUrl(REPO_ROOT + BRANCH).setUuid(UUID);
    }

    @Test
    public void testGetLatestRevision() {
        assertEquals(99, this.repository.getLatestRevision());
    }

    @Test
    public void testSetLatestRevision() {
        assertEquals(100, this.repository.setLatestRevision(100).getLatestRevision());
    }

    @Test
    public void testGetRoot() {
        assertEquals(REPO_ROOT, this.repository.getRoot());
    }

    @Test
    public void testSetRoot() {
        assertEquals("http://new.svn.server", this.repository.setRoot("http://new.svn.server").getRoot());
    }

    @Test
    public void testGetUuid() {
        assertEquals(UUID, this.repository.getUuid());
    }

    @Test
    public void testSetUuid() {
        assertEquals("987654321", this.repository.setUuid("987654321").getUuid());
    }

    @Test
    public void testGetBranch() {
        assertEquals(BRANCH, this.repository.getBranch());
    }

    @Test
    public void testSetUrl() {
        assertEquals(REPO_ROOT + BRANCH + "/dir", this.repository.setUrl(REPO_ROOT + BRANCH + "/dir").getUrl());
    }

    @Test
    public void testGetUrl() {
        assertEquals(REPO_ROOT + BRANCH, this.repository.getUrl());
    }
}
