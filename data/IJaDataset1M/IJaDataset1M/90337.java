package net.sf.gratepic.gui.user;

import net.sf.gratepic.gui.*;
import net.sf.gratepic.gui.comp.StatusMessage;
import net.sf.gratepic.gui.comp.TopComponentUtils;
import net.sf.gratepic.gui.comp.PhotoRetriever;
import net.sf.gratepic.gui.comp.VisualPhoto;
import net.sf.gratepic.gui.groups.GroupPhotosTopComponent;
import net.sf.gratepic.gui.groups.GroupNode;
import net.sf.gratepic.gui.groups.BatchPostingPanel;
import flickr.response.ResponseObject;
import flickr.service.Flickr;
import flickr.service.SwingFlickrResponseHandler;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.util.NbBundle;

/**
 * @author leon
 */
public class PostToGroupsAction extends AbstractAction {

    private PhotoRetriever photoRetriever;

    public PostToGroupsAction(PhotoRetriever photoRetriever) {
        super(NbBundle.getMessage(PostToGroupsAction.class, "postToGroups"));
        this.photoRetriever = photoRetriever;
    }

    public void actionPerformed(ActionEvent e) {
        postToGroups(photoRetriever.getPhoto());
    }

    private void postToGroups(VisualPhoto vp) {
        if (vp == null) {
            return;
        }
        BatchPostingPanel bpp = new BatchPostingPanel();
        String post = NbBundle.getMessage(PostToGroupsAction.class, "post");
        DialogDescriptor dd = new DialogDescriptor(bpp, NbBundle.getMessage(PostToGroupsAction.class, "postToGroups"), true, new Object[] { post, DialogDescriptor.CANCEL_OPTION }, post, DialogDescriptor.DEFAULT_ALIGN, null, null);
        dd.setHelpCtx(null);
        Dialog dial = DialogDisplayer.getDefault().createDialog(dd);
        dial.setVisible(true);
        if (!post.equals(dd.getValue())) {
            return;
        }
        GroupNode[] gns = bpp.getSelectedNodes();
        if (gns.length == 0) {
            StatusMessage.show(NbBundle.getMessage(PostToGroupsAction.class, "cannotPostBecauseNoGroupsSelected"));
            return;
        }
        for (final GroupNode gn : gns) {
            final ProgressHandle ph = ProgressHandleFactory.createHandle(NbBundle.getMessage(InMainPackage.class, "postingPhotoTo", gn.getGroup().getName()));
            Flickr.get().getPoolsService().addPhoto(gn.getGroup().getId(), vp.getPhoto(), new SwingFlickrResponseHandler<ResponseObject>() {

                public void doBeforeStart() {
                    ph.start();
                }

                public void doCallFailed(Exception ex) {
                    FlickrErrorHandler.handle(NbBundle.getMessage(InMainPackage.class, "photoNotPostedTo", gn.getGroup().getName()), ex);
                }

                public void doAfterFinish() {
                    ph.finish();
                }

                public void doCallFinished(ResponseObject ro) {
                    TopComponentUtils.openGroupPhotosComponent(gn.getGroup());
                }
            });
        }
    }
}
