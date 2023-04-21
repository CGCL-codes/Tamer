package org.horen.task.search;

import org.horen.core.db.SearchCondition;

public abstract class TaskSearch extends SearchCondition {

    @Override
    public String getIdName() {
        return "task_id";
    }

    @Override
    public String getJoinDef(String strTableName) {
        if (strTableName.equals("workingtime")) {
            return "LEFT OUTER JOIN workingtime ON task.task_id = workingtime.task_id";
        }
        if (strTableName.equals("priority_model")) {
            return "LEFT OUTER JOIN priority_model ON task.priority_model = priority_model.pm_id";
        }
        if (strTableName.equals("type")) {
            return "LEFT OUTER JOIN type ON task.type_id=type.type_id";
        }
        if (strTableName.equals("task_label_map")) {
            return "LEFT OUTER JOIN task_label_map ON task.task_id = task_label_map.task_id";
        }
        if (strTableName.equals("label")) {
            return "LEFT OUTER JOIN label ON task_label_map.label_id=label.label_id";
        }
        return "";
    }

    @Override
    public String getMainTable() {
        return "task";
    }
}
