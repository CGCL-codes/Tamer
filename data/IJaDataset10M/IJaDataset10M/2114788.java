package gg.de.sbmp3.forms;

import org.apache.struts.action.ActionForm;

/**
 * Created: 03.07.2004  18:32:48
 */
public class PlaylistEntryFormBean extends ActionForm {

    private int playlistId = 0;

    private int fileId = 0;

    private int[] fileIds = new int[0];

    private String action = null;

    private String deletebt = null;

    public int getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(int playlistId) {
        this.playlistId = playlistId;
    }

    public int[] getFileIds() {
        return fileIds;
    }

    public void setFileIds(int[] fileIds) {
        this.fileIds = fileIds;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    public String getDeletebt() {
        return deletebt;
    }

    public void setDeletebt(String deletebt) {
        this.deletebt = deletebt;
    }
}
