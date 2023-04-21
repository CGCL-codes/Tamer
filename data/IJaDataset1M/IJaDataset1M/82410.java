package org.loon.framework.game.simple.extend.db;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.loon.framework.game.simple.extend.db.index.IndexList;
import org.loon.framework.game.simple.extend.db.type.TypeUtils;
import org.loon.framework.game.simple.utils.StringUtils;

/**
 * Copyright 2008
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * 
 * @project loonframework
 * @author chenpeng
 * @email：ceponline@yahoo.com.cn
 * @version 0.1.2
 */
public class IndexBuilder {

    private static final Map cacheMap = Collections.synchronizedMap(new HashMap(1000));

    private final File dataFile;

    private AccessData data;

    private final String passWord;

    private final int type;

    private IndexList getIndex(String indexName) {
        IndexList index;
        try {
            Serializer serializerValue = TypeUtils.switchSerializer(type);
            index = data.getIndex(indexName, TypeUtils.STRING, serializerValue);
            if (index == null) {
                index = data.makeIndex(indexName, TypeUtils.STRING, serializerValue);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return index;
    }

    private IndexBuilder(final String fileName, final String password, final int type) {
        this.dataFile = new File(fileName);
        this.passWord = password;
        this.type = type;
    }

    public static IndexBuilder make(final String fileName, final String password, final int type) {
        final String nowKey = (fileName + type).intern();
        Object object = cacheMap.get(nowKey);
        if (object == null) {
            cacheMap.put(nowKey, (object = new IndexBuilder(fileName, password, type)));
        }
        return (IndexBuilder) object;
    }

    public Set getKeys(String indexName) {
        Set keys = null;
        openIndex();
        keys = getIndex(StringUtils.replaceIgnoreCase(indexName, MDB.TAG_SUFFIX, "")).getKeys();
        closeIndex();
        return keys;
    }

    public void instert(String indexName, Object value) {
        openIndex();
        getIndex(StringUtils.replaceIgnoreCase(indexName, MDB.TAG_SUFFIX, "")).put(indexName, value);
        closeIndex();
    }

    public Object delete(String indexName) {
        openIndex();
        Object obj = getIndex(StringUtils.replaceIgnoreCase(indexName, MDB.TAG_SUFFIX, "")).remove(indexName);
        closeIndex();
        return obj;
    }

    public Object select(String indexName) {
        openIndex();
        Object obj = getIndex(StringUtils.replaceIgnoreCase(indexName, MDB.TAG_SUFFIX, "")).get(indexName);
        closeIndex();
        return obj;
    }

    private void openIndex() {
        try {
            data = new AccessData(dataFile, passWord, !dataFile.exists());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void closeIndex() {
        if (data == null) return;
        try {
            data.close();
            data.getAccess().close();
            data = null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
