package net.taylor.tracker.entity.editor;

import javax.ejb.Stateful;
import net.taylor.richfaces.RootTreeNode;
import net.taylor.seam.AbstractEditorBean;
import net.taylor.seam.ComboBox;
import net.taylor.tracker.entity.ChangePath;
import net.taylor.tracker.entity.editor.ChangeSetFinderBean.ChangeSetComboBox;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;

/**
 * The implementation of the Editor interface.
 *
 * @author jgilbert
 * @generated
 */
@Name("changePathEditor")
@Stateful
public class ChangePathEditorBean extends AbstractEditorBean<ChangePath> implements ChangePathEditor {

    /** @generated */
    private static final long serialVersionUID = 1L;

    /** @generated */
    public RootTreeNode initChangePathTree() {
        return initRoot(new ChangePathTreeNode(null, getInstance()));
    }

    /** @generated */
    @Factory(value = "changePath", scope = ScopeType.EVENT, autoCreate = true)
    public ChangePath initChangePath() {
        return getInstance();
    }

    /** @generated */
    @Override
    protected ChangePath createInstance() {
        ChangePath result = new ChangePath();
        return result;
    }

    /** @generated */
    public boolean isSimpleEntity() {
        return true;
    }

    /** @generated */
    protected void prePersist() {
        super.prePersist();
        if (getInstance().getChangeSet() != null) {
            getInstance().getChangeSet().addChangePath(getInstance());
        }
    }

    /** @generated */
    protected void preRemove() {
        super.preRemove();
        if (getInstance().getChangeSet() != null) {
            getInstance().getChangeSet().removeChangePath(getInstance());
        }
    }

    /**
	 * ------------------------------------------------------------------------
	 * --- ChangeSet ManyToOne Association
	 * ------------------------------------------------------------------------
	 * 
	 * @generated
	 */
    @Out
    protected ComboBox changeSetComboBox = new ChangeSetComboBox<ChangePathEditor, ChangePath>(this, "changeSet") {

        public boolean isRendered() {
            return getComponent().isTop();
        }
    };
}
