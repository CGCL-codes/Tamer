package scrum.client.collaboration;

import scrum.client.common.TooltipBuilder;

public class DeleteSubjectAction extends GDeleteSubjectAction {

    public DeleteSubjectAction(scrum.client.collaboration.Subject subject) {
        super(subject);
    }

    @Override
    public String getLabel() {
        return "Delete";
    }

    @Override
    protected void updateTooltip(TooltipBuilder tb) {
        tb.setText("Delete this Subject and all it's comments permanently.");
        if (!subject.getProject().isScrumMaster(getCurrentUser())) tb.addRemark(TooltipBuilder.NOT_SCRUMMASTER);
    }

    @Override
    public boolean isExecutable() {
        return true;
    }

    @Override
    public boolean isPermitted() {
        if (!getCurrentProject().isScrumMaster(getCurrentUser())) return false;
        return true;
    }

    @Override
    protected void onExecute() {
        getDao().deleteSubject(subject);
        addUndo(new Undo());
    }

    class Undo extends ALocalUndo {

        @Override
        public String getLabel() {
            return "Undo Delete " + subject.getReference() + " " + subject.getLabel();
        }

        @Override
        protected void onUndo() {
            getDao().createSubject(subject);
        }
    }
}
