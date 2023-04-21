package org.silicolife.data;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import org.silicolife.exceptions.SystemSLException;
import org.silicolife.exceptions.SystemSLExceptionMessages;
import org.silicolife.exceptions.UserSLException;
import org.silicolife.exceptions.UserSLExceptionMessages;

public class SLFileRepository {

    private HashMap<String, SLFile> keyMap = new HashMap<String, SLFile>();

    private HashMap<String, SLFile> nameMap = new HashMap<String, SLFile>();

    public void put(SLFile file) throws SystemSLException, UserSLException {
        String key = file.getKey();
        if (exists(key)) {
            throw new SystemSLException(SystemSLExceptionMessages.FILE_KEY_EXISTS, new String[] { key });
        }
        String name = file.getName();
        if (existsByName(name)) {
            throw new UserSLException(UserSLExceptionMessages.FILE_NAME_EXISTS, new String[] { name });
        }
        keyMap.put(file.getKey(), file);
        nameMap.put(file.getName(), file);
    }

    public SLFile get(String key) throws SystemSLException {
        SLFile file = keyMap.get(key);
        if (file == null) {
            throw new SystemSLException(SystemSLExceptionMessages.FILE_KEY_NOT_FOUND, new String[] { key });
        }
        return file;
    }

    public void del(String key) throws SystemSLException {
        SLFile file = get(key);
        nameMap.remove(file.getName());
        keyMap.remove(key);
    }

    public boolean exists(String key) throws SystemSLException {
        boolean exists = false;
        try {
            exists = get(key) != null;
        } catch (SystemSLException ex) {
            if (ex.getErrorCode() == SystemSLExceptionMessages.FILE_KEY_NOT_FOUND) {
                exists = false;
            } else {
                throw ex;
            }
        }
        return exists;
    }

    public SLFile getByName(String name) throws UserSLException {
        SLFile file = nameMap.get(name);
        if (file == null) {
            throw new UserSLException(UserSLExceptionMessages.FILE_NAME_NOT_FOUND, new String[] { name });
        }
        return file;
    }

    public void delByName(String name) throws SystemSLException, UserSLException {
        SLFile file = get(name);
        nameMap.remove(name);
        keyMap.remove(file.getKey());
    }

    public boolean existsByName(String name) throws UserSLException {
        boolean exists = false;
        try {
            exists = getByName(name) != null;
        } catch (UserSLException ex) {
            if (ex.getErrorCode() == UserSLExceptionMessages.FILE_NAME_NOT_FOUND) {
                exists = false;
            } else {
                throw ex;
            }
        }
        return exists;
    }

    public int size() {
        return keyMap.size();
    }

    public Set<String> keySet() {
        return keyMap.keySet();
    }

    public Collection<SLFile> values() {
        return keyMap.values();
    }
}
