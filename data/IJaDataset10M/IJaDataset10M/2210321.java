package com.jspx.service.xtree;

import com.jspx.sober.SoberSupport;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: 陈原
 * Date: 2005-11-14
 * Time: 17:54:34
 *
 */
public interface ManTreeDAO extends SoberSupport {

    Map<String, ManTree> getManTreeMap(final String manId);

    List getManTree(final String manId);

    String[] getManTreeArray(String manId);

    boolean deleteForManId(final String manId);

    boolean deleteAll();

    boolean fixTreeItem(final String[] treeItemId);

    String getNamespace();

    void setNamespace(String namespace);
}
