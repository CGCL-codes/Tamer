package de.beas.explicanto.client.sec.actions;

import java.text.MessageFormat;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.IActionDelegate2;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import de.beas.explicanto.client.I18N;
import de.beas.explicanto.client.rcp.dialogs.ExplicantoMessageDialog;
import de.beas.explicanto.client.sec.control.TimelineCell;
import de.beas.explicanto.client.sec.dialogs.MemberDialog;
import de.beas.explicanto.client.sec.dialogs.NewDirectionDialog;
import de.beas.explicanto.client.sec.jaxb.Member;
import de.beas.explicanto.client.sec.jaxb.Scene;
import de.beas.explicanto.client.sec.model.ScreenplayModel;
import de.beas.explicanto.client.sec.views.model.MemberConsts;

/**
 * AddMemberAndOccurenceAction
 * <P>
 * Adds a new castMember and then creates an occurence on the selected frame
 * @author Alexandru.Gyulai
 * @version 1.0
 *
 */
public class AddMemberAndOccurenceAction implements IEditorActionDelegate, IActionDelegate2 {

    private static Log log = LogFactory.getLog(AddMemberAndOccurenceAction.class);

    protected IEditorPart activeEditor;

    private TimelineCell cell = null;

    private String newAudio = "", newImage = "", newScreenText = "", newProp = "", newDirection = "", newOccurence = "";

    public void setActiveEditor(IAction action, IEditorPart targetEditor) {
        this.activeEditor = targetEditor;
    }

    public void run(IAction action) {
        log.error(" Unexpected method invocation");
    }

    public void selectionChanged(IAction action, ISelection selection) {
        if (!(selection instanceof IStructuredSelection)) {
            cell = null;
            return;
        }
        IStructuredSelection currentSelection = (IStructuredSelection) selection;
        if (!(currentSelection.getFirstElement() instanceof TimelineCell)) {
            cell = null;
            return;
        }
        cell = (TimelineCell) currentSelection.getFirstElement();
        String mt = cell.getMemberType();
        if (mt == null) {
            action.setEnabled(false);
            return;
        }
        if (mt.equals(MemberConsts.AUDIO_CLASS)) {
            action.setText(newAudio);
        } else if (mt.equals(MemberConsts.IMAGE_CLASS)) {
            action.setText(newImage);
        } else if (mt.equals(MemberConsts.SCREENTEXT_CLASS)) {
            action.setText(newScreenText);
        } else if (mt.equals(MemberConsts.PROP_CLASS)) {
            action.setText(newProp);
        } else if (mt.equals(MemberConsts.DIRECTION_CLASS)) {
            action.setText(newDirection);
        } else {
            action.setEnabled(false);
        }
    }

    public void init(IAction action) {
        log.debug("init AddMemberAndOccurenceAction");
        newOccurence = translate("sec.scene.addNewOccurence");
        action.setText(newOccurence);
        newAudio = translate("sec.scene.addNewAudio");
        newImage = translate("sec.scene.addNewImage");
        newScreenText = translate("sec.scene.addNewScreenText");
        newProp = translate("sec.scene.addNewProp");
        newDirection = translate("sec.scene.addNewDirection");
    }

    public void dispose() {
    }

    public void runWithEvent(IAction action, Event event) {
        log.debug("AddMemberAndOccurenceAction runWithEvent");
        Object modelObj = activeEditor.getEditorInput().getAdapter(ScreenplayModel.class);
        if (modelObj == null) {
            log.error("(ScreenplayModel == null)");
            return;
        }
        ScreenplayModel model = (ScreenplayModel) modelObj;
        if (cell == null) {
            log.debug("cell == null");
            return;
        }
        Scene scene = model.getScene(cell.getSceneUid());
        if (scene == null) {
            log.error("(scene == null)");
            return;
        }
        long parentFrame = cell.getExactFrame();
        String mt = cell.getMemberType();
        if (mt == null) {
            return;
        }
        if (mt.equals(MemberConsts.AUDIO_CLASS) || mt.equals(MemberConsts.IMAGE_CLASS) || mt.equals(MemberConsts.SCREENTEXT_CLASS) || mt.equals(MemberConsts.PROP_CLASS)) {
            MemberDialog dialog = new MemberDialog(activeEditor.getSite().getShell(), model.getDataObject().getScreenplay().getCharacters(), null, mt, model);
            dialog.setBlockOnOpen(true);
            if (dialog.open() != Window.OK) {
                return;
            }
            Member newMember = dialog.getMember();
            model.addCastMember(newMember);
            if (parentFrame <= scene.getTimelines().size()) {
                String overlappings = model.findOverlappings(scene.getUid(), -1, -1, parentFrame, 1, newMember.getMemberClass(), false);
                boolean deleted = false;
                if (overlappings.length() > 0) {
                    String msg = MessageFormat.format(I18N.translate("sec.scene.multidelete.confirm"), new String[] { overlappings });
                    if (ExplicantoMessageDialog.openQuestion(activeEditor.getSite().getShell(), msg)) {
                        model.findOverlappings(scene.getUid(), -1, -1, parentFrame, 1, newMember.getMemberClass(), true);
                        deleted = true;
                    }
                }
                if (overlappings.length() == 0 || deleted) {
                    model.addOccurence(scene.getUid(), newMember.getUid(), parentFrame, 1);
                }
            } else {
                model.addFrame(scene.getUid(), parentFrame, false);
                model.addOccurence(scene.getUid(), newMember.getUid(), parentFrame, 1);
            }
        } else if (mt.equals(MemberConsts.DIRECTION_CLASS)) {
            NewDirectionDialog dialog = new NewDirectionDialog(activeEditor.getSite().getShell(), "");
            dialog.setBlockOnOpen(true);
            if (dialog.open() == Window.CANCEL) {
                return;
            }
            String directionText = dialog.getInformation();
            if (parentFrame <= scene.getTimelines().size()) {
                String overlappings = model.findOverlappings(scene.getUid(), -1, -1, parentFrame, 1, MemberConsts.DIRECTION_CLASS, false);
                boolean deleted = false;
                if (overlappings.length() > 0) {
                    String msg = MessageFormat.format(I18N.translate("sec.scene.multidelete.confirm"), new String[] { overlappings });
                    if (ExplicantoMessageDialog.openQuestion(activeEditor.getSite().getShell(), msg)) {
                        model.findOverlappings(scene.getUid(), -1, -1, parentFrame, 1, MemberConsts.DIRECTION_CLASS, true);
                        deleted = true;
                    }
                }
                if (overlappings.length() == 0 || deleted) {
                    model.addDirection(scene.getUid(), directionText, parentFrame, 1);
                }
            } else {
                model.addFrame(scene.getUid(), parentFrame, false);
                model.addDirection(scene.getUid(), directionText, parentFrame, 1);
            }
        }
    }

    public String translate(String string) {
        return I18N.translate(string);
    }

    protected boolean isEmpty(String text) {
        if ((text == null) || (text.length() == 0)) {
            return true;
        }
        return false;
    }
}
