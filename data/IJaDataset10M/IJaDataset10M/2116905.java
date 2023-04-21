package net.sf.gratepic.gui;

import flickr.service.FlickrUtils;
import java.awt.Image;
import java.io.Serializable;
import javax.swing.JComponent;
import org.netbeans.core.spi.multiview.MultiViewDescription;
import org.netbeans.core.spi.multiview.MultiViewElement;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;

public class YourSetsMultiViewDescription implements MultiViewDescription, Serializable {

    private static final long serialVersionUID = 1;

    public static final String PREFERRED_ID = "your_" + UserSetsComponent.class.getSimpleName();

    public YourSetsMultiViewDescription() {
    }

    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_ALWAYS;
    }

    public String getDisplayName() {
        return NbBundle.getMessage(UserSetsComponent.class, "sets");
    }

    public Image getIcon() {
        return null;
    }

    public HelpCtx getHelpCtx() {
        return null;
    }

    public String preferredID() {
        return PREFERRED_ID;
    }

    public MultiViewElement createElement() {
        return new YourSetsElement();
    }

    private static class YourSetsElement extends AbstractMultiViewElement<UserSetsComponent> {

        public UserSetsComponent createVisualRepresentation() {
            final UserSetsComponent component = new UserSetsComponent(FlickrUtils.getCurrentUserId(), true);
            PhotoRetriever photoRetriever = new PhotoRetriever() {

                public VisualPhoto getPhoto() {
                    return component.getSelectedPhoto();
                }
            };
            component.setDefaultAction(new PostToGroupsAction(photoRetriever));
            component.setContextActions(new OpenInBrowserAction(photoRetriever));
            return component;
        }

        public JComponent createToolBarRepresentation() {
            return getVisualRepresentation().getToolBar();
        }

        @Override
        public void componentShowing() {
            super.componentShowing();
            getVisualRepresentation().setIconLoadingEnabled(true);
        }

        @Override
        public void componentHidden() {
            super.componentHidden();
            getVisualRepresentation().setIconLoadingEnabled(false);
        }
    }
}
