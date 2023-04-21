package org.monet.backoffice.presentation.user.renders;

import java.util.HashMap;
import org.monet.kernel.model.BaseObject;
import org.monet.kernel.model.Node;
import org.monet.kernel.model.NodeLink;
import org.monet.kernel.model.Task;
import org.monet.kernel.model.TaskList;
import org.monet.kernel.model.definition.NodeViewDeclaration;
import org.monet.kernel.model.definition.NodeViewDeclaration.ShowSet;

public class NodeViewRender extends Render {

    protected Node node;

    public NodeViewRender(NodeLink nodeLink) {
        super(nodeLink);
    }

    @Override
    public void setTarget(BaseObject target) {
        if (target instanceof Node) node = (Node) target;
    }

    @Override
    protected void init() {
        this.initView();
    }

    protected boolean isViewSet(NodeViewDeclaration view) {
        return view.getShowSet() != null;
    }

    protected void initViewSetNotes(NodeViewDeclaration view, HashMap<String, String> contentMap) {
        HashMap<String, String> map = new HashMap<String, String>();
        HashMap<String, String> notes = this.node.getNotes();
        String notesResult = "";
        contentMap.put("type", "notes");
        if (notes.size() <= 0) {
            notesResult = block("content.notes$empty", map);
        }
        for (String note : notes.values()) {
            map.put("note", note);
            notesResult += block("content.notes$note", map);
            map.clear();
        }
        contentMap.put("notes", notesResult);
        addMark("content", block("content.notes", contentMap));
    }

    protected void initViewSetTasks(NodeViewDeclaration view, HashMap<String, String> contentMap) {
        HashMap<String, String> map = new HashMap<String, String>();
        TaskList taskList = this.node.getLinkedTasks();
        String tasksResult = "";
        contentMap.put("type", "tasks");
        if (taskList.getCount() <= 0) {
            tasksResult = block("content.tasks$empty", map);
        }
        for (Task task : taskList.get().values()) {
            if (task.isFinished()) map.put("state", "finished"); else if (task.isAborted()) map.put("state", "aborted"); else if (task.isPendingForAction()) map.put("state", "pending");
            map.put("id", task.getId());
            map.put("label", task.getLabel());
            map.put("definitionLabel", task.getDefinition().getLabel());
            map.put("description", (!(task.isFinished() || task.isAborted())) ? task.getDescription() : "");
            map.put("createDate", task.getCreateDate());
            map.put("finishdate", task.getFinishDate());
            map.put("finished", (task.isFinished()) ? block("content.tasks$task$finished", map) : "");
            map.put("aborted", (task.isAborted()) ? block("content.tasks$task$aborted", map) : "");
            tasksResult += block("content.tasks$task", map);
            map.clear();
        }
        contentMap.put("tasks", tasksResult);
        addMark("content", block("content.tasks", contentMap));
    }

    protected void initViewSet(NodeViewDeclaration view) {
        HashMap<String, String> map = new HashMap<String, String>();
        ShowSet showSet = view.getShowSet();
        map.put("clec", "clec");
        map.put("idNode", this.node.getId());
        map.put("from", this.getParameter("from"));
        map.put("view", view.getCode());
        map.put("page", this.getParameter("page"));
        map.put("npp", this.getParameter("npp"));
        switch(showSet.getSet()) {
            case NOTES:
                this.initViewSetNotes(view, map);
                break;
            case LINKS_IN:
                map.put("type", "linksin");
                addMark("content", block("content.linksIn", map));
                break;
            case LINKS_OUT:
                map.put("type", "linksout");
                addMark("content", block("content.linksOut", map));
                break;
            case REVISIONS:
                map.put("type", "revisions");
                addMark("content", block("content.revisions", map));
                break;
            case TASKS:
                this.initViewSetTasks(view, map);
                break;
        }
    }

    protected void initView() {
    }

    @Override
    protected void build() {
        loadCanvas("view.node");
    }
}
