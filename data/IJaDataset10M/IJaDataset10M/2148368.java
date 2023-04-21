package gg.de.sbmp3.forms;

import org.apache.struts.action.ActionForm;

/**
 * Created: 01.07.2004  21:01:42
 */
public class PlaylistFormBean extends ActionForm {

    private int playlistId = 0;

    private String name = "";

    private int parentId = 0;

    private int visibility = 2;

    private String comment = "";

    private String changebt;

    private String deletebt;

    public int getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(int playlistId) {
        this.playlistId = playlistId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    public String getChangebt() {
        return null;
    }

    public void setChangebt(String changebt) {
        this.changebt = changebt;
    }

    public String getDeletebt() {
        return null;
    }

    public void setDeletebt(String deletebt) {
        this.deletebt = deletebt;
    }

    public boolean isChangeAction() {
        return (changebt != null);
    }

    public boolean isDeleteAction() {
        return (deletebt != null);
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
