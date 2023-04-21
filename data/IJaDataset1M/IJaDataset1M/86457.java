package org.paralit.isf.core.search.indexer;

import org.paralit.isf.core.search.IndexPolicyInfo;
import org.paralit.isf.core.search.workflow.ModifyTask;
import org.paralit.isf.core.search.workflow.WorkFlow;

/**
 * 更新索引器，用以更新文件在索引中保存的权限信息。
 * @author rmfish
 * 
 */
public class Modifier {

    private static Modifier modifier;

    public ModifyIndex modifyIndex;

    private Modifier() {
    }

    /**获得更新实例。
     * @return
     */
    public static Modifier getInstance() {
        if (modifier == null) {
            return new Modifier();
        }
        return modifier;
    }

    /**
     * 根据传入的文件ID以及需要更新的权限信息对该文件索引进行更新。
     * @param id
     * @param policyInfo
     */
    public void modifyindex(int id, IndexPolicyInfo policyInfo) {
        this.modifyIndex = new ModifyIndex(id, policyInfo);
        WorkFlow.addWork(new ModifyTask(this.modifyIndex));
    }
}
