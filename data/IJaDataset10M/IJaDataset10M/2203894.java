package com.intersys.bio.paralogs.db;

import com.intersys.bio.paralogs.model.Alignment;
import com.intersys.bio.paralogs.model.BaseTree;
import com.intersys.bio.paralogs.model.BaseTreeNode;
import com.intersys.bio.paralogs.model.Dataset;
import com.intersys.objects.CacheException;
import com.jalapeno.ApplicationContext;
import com.jalapeno.ObjectManager;
import java.lang.reflect.Field;
import java.util.List;

public class InfoDAO {

    protected static final String INFO_NS = "PARALOGS";

    protected static final String username = "_SYSTEM";

    protected static final String password = "SYS";

    private InfoDAO() throws Exception {
    }

    protected static String url() {
        return "jdbc:Cache://localhost:1972/" + INFO_NS;
    }

    protected static String sysURL() {
        return "jdbc:Cache://localhost:1972/%SYS";
    }

    public static Dataset getDataset(int family) throws Exception {
        Dataset ds = null;
        ObjectManager om1 = getDataObjectManager();
        try {
            ds = (Dataset) om1.openByPrimaryKey(Dataset.class, family);
        } catch (CacheException e) {
            if (e.getCode() == 5809) ds = null; else throw e;
        } finally {
            om1.close();
        }
        return ds;
    }

    public static String getMF(String[] proteins) throws Exception {
        return Util.getMF(proteins);
    }

    public static Object getDataObject(Class aClass, Object primaryKey) throws Exception {
        ObjectManager om = getDataObjectManager();
        try {
            Object pojo = om.openByPrimaryKey(aClass, primaryKey);
            return om.detach(pojo);
        } catch (CacheException x) {
            return null;
        } finally {
            om.close();
        }
    }

    public static void saveDataset(Dataset ds) throws Exception {
        ObjectManager om1 = getDataObjectManager();
        try {
            om1.save(ds, true);
        } finally {
            om1.close();
        }
    }

    private static ObjectManager getDataObjectManager() throws Exception {
        String url = url();
        ObjectManager om = ApplicationContext.createObjectManager(url, username, password);
        om.settings().setFetchPolicy(ObjectManager.FETCH_POLICY_EAGER);
        return om;
    }

    public static void removeDatabaseId(Dataset ds) throws Exception {
        Alignment[] als = ds.getAlignment();
        for (Alignment a : als) setIdNull(a);
        BaseTree tree = ds.getProteinTree();
        if (tree != null) {
            setIdNull(tree);
            List<BaseTreeNode> nodes = tree.getNodes();
            for (BaseTreeNode node : nodes) setIdNull(node);
        }
        tree = ds.getSpeciesTree();
        if (tree != null) {
            setIdNull(tree);
            List<BaseTreeNode> nodes = tree.getNodes();
            for (BaseTreeNode node : nodes) setIdNull(node);
        }
    }

    private static void setIdNull(Object obj) throws Exception {
        Class aClass = obj.getClass();
        Field idf = null;
        for (; aClass != null; aClass = aClass.getSuperclass()) {
            try {
                idf = aClass.getDeclaredField("idPlaceHolder");
            } catch (NoSuchFieldException e) {
            }
        }
        idf.setAccessible(true);
        idf.set(obj, null);
    }
}
