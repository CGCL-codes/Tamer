package org.openmobster.core.synchronizer.model;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

/**
 * 
 * @author openmobster@gmail.com
 */
public class SyncCommand implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -6100648877132242195L;

    private String cmdId = null;

    private String target = null;

    private String source = null;

    private String meta = null;

    private String numberOfChanges = null;

    private List addCommands = null;

    private List replaceCommands = null;

    private List deleteCommands = null;

    /**
	 * 
	 *
	 */
    public SyncCommand() {
        this.addCommands = new ArrayList();
        this.replaceCommands = new ArrayList();
        this.deleteCommands = new ArrayList();
    }

    public String getCmdId() {
        return cmdId;
    }

    public void setCmdId(String cmdId) {
        this.cmdId = cmdId;
    }

    public String getMeta() {
        return meta;
    }

    public void setMeta(String meta) {
        this.meta = meta;
    }

    public String getNumberOfChanges() {
        return numberOfChanges;
    }

    public void setNumberOfChanges(String numberOfChanges) {
        this.numberOfChanges = numberOfChanges;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public List getAddCommands() {
        if (this.addCommands == null) {
            this.addCommands = new ArrayList();
        }
        return addCommands;
    }

    public void setAddCommands(List addCommands) {
        this.addCommands = addCommands;
    }

    public List getReplaceCommands() {
        if (this.replaceCommands == null) {
            this.replaceCommands = new ArrayList();
        }
        return this.replaceCommands;
    }

    public void setReplaceCommands(List replaceCommands) {
        this.replaceCommands = replaceCommands;
    }

    public List getDeleteCommands() {
        if (this.deleteCommands == null) {
            this.deleteCommands = new ArrayList();
        }
        return this.deleteCommands;
    }

    public void setDeleteCommands(List deleteCommands) {
        this.deleteCommands = deleteCommands;
    }

    /**
	 * 
	 * @return
	 */
    public List filterChunkedCommands() {
        List chunkedCommands = new ArrayList();
        List chunkedAddCommands = new ArrayList();
        for (int i = 0; i < this.getAddCommands().size(); i++) {
            Add cour = (Add) this.getAddCommands().get(i);
            if (cour.isChunked()) {
                chunkedAddCommands.add(cour);
            }
        }
        this.getAddCommands().removeAll(chunkedAddCommands);
        chunkedCommands.addAll(chunkedAddCommands);
        List chunkedReplaceCommands = new ArrayList();
        for (int i = 0; i < this.getReplaceCommands().size(); i++) {
            Replace cour = (Replace) this.getReplaceCommands().get(i);
            if (cour.isChunked()) {
                chunkedReplaceCommands.add(cour);
            }
        }
        this.getReplaceCommands().removeAll(chunkedReplaceCommands);
        chunkedCommands.addAll(chunkedReplaceCommands);
        return chunkedCommands;
    }

    /**
	 * 
	 * @return
	 */
    public List getChunkedCommands() {
        List chunkedCommands = new ArrayList();
        List chunkedAddCommands = new ArrayList();
        for (int i = 0; i < this.getAddCommands().size(); i++) {
            Add cour = (Add) this.getAddCommands().get(i);
            if (cour.isChunked()) {
                chunkedAddCommands.add(cour);
            }
        }
        chunkedCommands.addAll(chunkedAddCommands);
        List chunkedReplaceCommands = new ArrayList();
        for (int i = 0; i < this.getReplaceCommands().size(); i++) {
            Replace cour = (Replace) this.getReplaceCommands().get(i);
            if (cour.isChunked()) {
                chunkedReplaceCommands.add(cour);
            }
        }
        chunkedCommands.addAll(chunkedReplaceCommands);
        return chunkedCommands;
    }

    /**
	 * 
	 * @param command
	 */
    public void addChunkedCommand(AbstractOperation command) {
        if (command instanceof Add) {
            this.getAddCommands().add(command);
        } else if (command instanceof Replace) {
            this.getReplaceCommands().add(command);
        }
    }

    /**
	 * 
	 * @param command
	 */
    public void addOperationCommand(AbstractOperation command) {
        if (command instanceof Add) {
            this.getAddCommands().add(command);
        } else if (command instanceof Replace) {
            this.getReplaceCommands().add(command);
        } else if (command instanceof Delete) {
            this.getDeleteCommands().add(command);
        }
    }

    /**
	 * 
	 * @return
	 */
    public List getAllCommands() {
        List allCommands = new ArrayList();
        allCommands.addAll(this.getAddCommands());
        allCommands.addAll(this.getReplaceCommands());
        allCommands.addAll(this.getDeleteCommands());
        return allCommands;
    }
}
