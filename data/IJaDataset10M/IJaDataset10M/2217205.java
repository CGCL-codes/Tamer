package org.agile.dfs.name.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.agile.dfs.core.common.UuidHexGenerator;
import org.agile.dfs.core.entity.DfsSchema;
import org.agile.dfs.core.entity.FileItem;
import org.agile.dfs.dao.IBatisTemplate;
import org.agile.dfs.name.exception.NameNodeException;
import org.agile.dfs.util.ObjectUtil;
import org.agile.dfs.util.ServiceFactory;
import org.agile.dfs.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileItemManager {

    private static final Logger logger = LoggerFactory.getLogger(FileItemManager.class);

    private static final IBatisTemplate template = ServiceFactory.findService(IBatisTemplate.class);

    private static final UuidHexGenerator generator = ServiceFactory.findService(UuidHexGenerator.class);

    private static final TableLocator locator = ServiceFactory.findService(TableLocator.class);

    public FileItem create(DfsSchema schema, FileItem item) {
        item.setStatus(FileItem.STATUS_INIT);
        item.setId(generator.generate());
        Map<String, Object> map = ObjectUtil.toMap(item);
        String table = locator.fileTable(schema);
        map.put("table", table);
        template.insert("dfs.file.insert", map);
        return item;
    }

    public FileItem update(DfsSchema schema, FileItem item) {
        Map<String, Object> map = ObjectUtil.toMap(item);
        String table = locator.fileTable(schema);
        map.put("table", table);
        map.put("id", item.getId());
        template.insert("dfs.file.update", map);
        return item;
    }

    public FileItem mkfile(DfsSchema schema, String fullPath, boolean mkp) {
        int pos = fullPath.lastIndexOf("/");
        if (pos >= fullPath.length() - 1) {
            throw new NameNodeException("Not exist name in " + fullPath);
        }
        String p = fullPath.substring(0, pos);
        String n = fullPath.substring(pos + 1);
        FileItem parent = this.findByPath(schema, p);
        if (parent == null) {
            if (!mkp) {
                throw new NameNodeException("Parent directory " + p + " not exist! ");
            } else {
                parent = mkdir(schema, p, true);
            }
        }
        FileItem item = new FileItem();
        item.setName(n);
        item.setParentId(parent.getId());
        item.setType(FileItem.TYPE_FILE);
        return create(schema, item);
    }

    public FileItem mkdir(DfsSchema schema, String fullPath, boolean mkp) {
        int pos = fullPath.lastIndexOf("/");
        if (pos >= fullPath.length() - 1) {
            logger.warn("Not exist name in " + fullPath);
            return null;
        }
        String p = fullPath.substring(0, pos);
        String n = fullPath.substring(pos + 1);
        FileItem parent = this.findByPath(schema, p);
        if (parent == null) {
            if (!mkp) {
                logger.warn("Parent directory " + p + " not exist! ");
                return null;
            } else {
                String[] paths = StringUtil.simpleSplit(fullPath, '/');
                FileItem tp = this.findRoot(schema);
                String tb = "";
                for (int i = 0, len = paths.length; i < len; i++) {
                    String name = paths[i];
                    String tps = tb + "/" + name;
                    FileItem curr = this.findByPidAndName(schema, tp.getId(), name);
                    if (curr == null) {
                        FileItem item = new FileItem();
                        item.setName(name);
                        item.setParentId(tp.getId());
                        item.setType(FileItem.TYPE_DIR);
                        curr = create(schema, item);
                    }
                    tb = tps;
                    tp = curr;
                }
                return tp;
            }
        } else {
            FileItem item = new FileItem();
            item.setName(n);
            item.setParentId(parent.getId());
            item.setType(FileItem.TYPE_DIR);
            create(schema, item);
            return item;
        }
    }

    public boolean deleteById(DfsSchema schema, String id) {
        String table = locator.fileTable(schema);
        Map<String, String> map = new HashMap<String, String>();
        map.put("table", table);
        map.put("id", id);
        template.delete("dfs.file.delete.id", map);
        return true;
    }

    public boolean deleteByParentId(DfsSchema schema, String pid) {
        String table = locator.fileTable(schema);
        Map<String, String> map = new HashMap<String, String>();
        map.put("table", table);
        map.put("parentId", pid);
        template.delete("dfs.file.delete.parentId", map);
        return true;
    }

    public FileItem findByPath(DfsSchema schema, String fullPath) {
        String[] paths = StringUtil.simpleSplit(fullPath, '/');
        FileItem parent = this.findRoot(schema);
        String base = "";
        for (int i = 0, len = paths.length; i < len; i++) {
            String name = paths[i];
            String p = base + "/" + name;
            FileItem curr = this.findByPidAndName(schema, parent.getId(), name);
            if (curr == null) {
                return null;
            }
            base = p;
            parent = curr;
        }
        return parent;
    }

    @SuppressWarnings("unchecked")
    public List<FileItem> findByParentId(DfsSchema schema, String parentId) {
        String table = locator.fileTable(schema);
        Map<String, String> map = new HashMap<String, String>();
        map.put("table", table);
        map.put("parentId", parentId);
        return template.findListByParameter("dfs.file.select.pid", map);
    }

    public FileItem findRoot(DfsSchema schema) {
        String table = locator.fileTable(schema);
        Map<String, String> map = new HashMap<String, String>();
        map.put("table", table);
        map.put("parentId", "0000");
        map.put("name", "ROOT");
        FileItem root = (FileItem) template.findByParameter("dfs.file.select.pid2name", map);
        if (root == null) {
            logger.error("Root is null, schema " + schema.getName());
        }
        return root;
    }

    private FileItem findByPidAndName(DfsSchema schema, String pid, String name) {
        String table = locator.fileTable(schema);
        Map<String, String> map = new HashMap<String, String>();
        map.put("table", table);
        map.put("parentId", pid);
        map.put("name", name);
        return (FileItem) template.findByParameter("dfs.file.select.pid2name", map);
    }
}
