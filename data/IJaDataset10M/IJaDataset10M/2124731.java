package com.whitebearsolutions.imagine.wbsagnitio.replica;

import java.io.File;
import java.util.List;
import java.util.Map;
import com.whitebearsolutions.directory.Entry;

public interface ExternalDirectory {

    public void storeUserEntry(String user, Map<String, String> attributes) throws Exception;

    public void storeGroupEntry(String group, Map<String, String> attributes) throws Exception;

    public void addUserEntry(String user, Map<String, String> attributes) throws Exception;

    public void updateUserEntry(String user, Map<String, String> attributes) throws Exception;

    public void deleteUserEntry(String user) throws Exception;

    public void deleteGroupEntry(String group) throws Exception;

    public List<Entry> getUserGroups(String user) throws Exception;

    public List<String> getUserGroupNames(String user) throws Exception;

    public Entry getUserEntry(String user) throws Exception;

    public List<Entry> searchUserEntry(String user) throws Exception;

    public void memberUserAdd(String name, String uid) throws Exception;

    public void memberUserRemove(String name, String uid) throws Exception;

    public void exportEntries(File log);

    public void importEntries(File log);
}
