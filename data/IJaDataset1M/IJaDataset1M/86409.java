package org.ccnx.ccn.test;

import java.io.File;
import java.util.ArrayList;
import org.ccnx.ccn.CCNHandle;
import org.ccnx.ccn.impl.repo.Policy;
import org.ccnx.ccn.impl.repo.RepositoryException;
import org.ccnx.ccn.impl.repo.RepositoryInfo;
import org.ccnx.ccn.impl.repo.RepositoryStoreBase;
import org.ccnx.ccn.profiles.nameenum.NameEnumerationResponse;
import org.ccnx.ccn.protocol.ContentName;
import org.ccnx.ccn.protocol.ContentObject;
import org.ccnx.ccn.protocol.Interest;
import org.ccnx.ccn.protocol.MalformedContentNameStringException;

/**
 * Test repository backend. Should not be used in production code.
 */
public class BitBucketRepository extends RepositoryStoreBase {

    public boolean checkPolicyUpdate(ContentObject co) throws RepositoryException {
        return false;
    }

    public ContentObject getContent(Interest interest) throws RepositoryException {
        return null;
    }

    public NameEnumerationResponse getNamesWithPrefix(Interest i, ContentName responseName) {
        return null;
    }

    public byte[] getRepoInfo(ArrayList<ContentName> names) {
        try {
            return (new RepositoryInfo("1.0", "/parc.com/csl/ccn/Repos", "Repository")).encode();
        } catch (Exception e) {
        }
        return null;
    }

    public static String getUsage() {
        return null;
    }

    public void initialize(String repositoryRoot, File policyFile, String localName, String globalPrefix, String nameSpace, CCNHandle handle) throws RepositoryException {
    }

    public NameEnumerationResponse saveContent(ContentObject content) throws RepositoryException {
        return null;
    }

    public void setPolicy(Policy policy) {
    }

    public ArrayList<ContentName> getNamespace() {
        ArrayList<ContentName> al = new ArrayList<ContentName>();
        try {
            al.add(ContentName.fromNative("/"));
        } catch (MalformedContentNameStringException e) {
        }
        return al;
    }

    public boolean diagnostic(String name) {
        return false;
    }

    public void shutDown() {
    }

    public Policy getPolicy() {
        return null;
    }

    @Override
    public String getVersion() {
        return null;
    }

    public Object getStatus(String type) {
        return null;
    }

    public boolean hasContent(ContentName name) throws RepositoryException {
        return false;
    }

    public boolean bulkImport(String name) throws RepositoryException {
        return false;
    }

    public void policyUpdate() throws RepositoryException {
    }
}
