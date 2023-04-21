package org.parallelj.designer.extension.adapters;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.parallelj.designer.extension.edit.parts.ForEachLoopExtendedEditPart;
import org.parallelj.model.JoinType;
import org.parallelj.model.ParallelJPackage;
import org.parallelj.model.SplitType;

public class ForEachLoopAdapter extends AdapterImpl {

    /**
	 * Involved AbstractGraphicalEditPart, wrapping EObject to modify.
	 */
    private AbstractGraphicalEditPart editPart;

    /**
	 * Created new adapter for ForEachLoopExtendedEditPart passed as parameter.
	 * 
	 * @param editPart
	 *            ForEachLoopExtendedEditPart
	 */
    public ForEachLoopAdapter(AbstractGraphicalEditPart editPart) {
        this.editPart = editPart;
    }

    /**
	 * Notifies when specific attribute/reference is modified. Here, function is
	 * triggered for changing SPILT/JOIN icon, when any link (incoming/outgoing)
	 * added/removed from diagram or any change in SPLIT or JOIN property.
	 * 
	 * @param notification
	 */
    @Override
    public final void notifyChanged(Notification notification) {
        if (notification.getEventType() == Notification.ADD || notification.getEventType() == Notification.REMOVE) {
            ((ForEachLoopExtendedEditPart) this.editPart).updateSplitJoin();
        } else if (notification.getEventType() == Notification.SET) {
            Object currentFeature = notification.getFeature();
            if (currentFeature instanceof EAttribute && currentFeature == ParallelJPackage.eINSTANCE.getProcedure_Join()) {
                JoinType newJoin = (JoinType) notification.getNewValue();
                ((ForEachLoopExtendedEditPart) this.editPart).setJoinIcon(newJoin.getName());
            } else if (currentFeature instanceof EAttribute && currentFeature == ParallelJPackage.eINSTANCE.getProcedure_Split()) {
                SplitType newSplit = (SplitType) notification.getNewValue();
                ((ForEachLoopExtendedEditPart) this.editPart).setSplitIcon(newSplit.getName());
            }
        }
    }
}
