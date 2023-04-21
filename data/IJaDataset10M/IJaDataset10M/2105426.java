package com.smartwish.batch.domain.filestore;

import java.io.File;
import java.io.InputStream;

public interface FileStoreDao {

    Number storeFileAndReturnId(File file) throws Exception;

    InputStream getStoredFile(Number id);

    int removeStoredFile(Number id);
}
