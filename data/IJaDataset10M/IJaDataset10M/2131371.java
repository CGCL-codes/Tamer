package org.thechiselgroup.choosel.core.client.resources.ui;

import org.thechiselgroup.choosel.core.client.resources.DelegatingResourceSet;
import org.thechiselgroup.choosel.core.client.resources.ResourceSet;
import org.thechiselgroup.choosel.core.client.resources.ResourceSetDelegateChangedEvent;
import org.thechiselgroup.choosel.core.client.resources.ResourceSetDelegateChangedEventHandler;
import org.thechiselgroup.choosel.core.client.resources.UnmodifiableResourceSet;
import org.thechiselgroup.choosel.core.client.util.Disposable;
import org.thechiselgroup.choosel.core.client.visualization.model.extensions.HighlightingModel;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.shared.HandlerRegistration;

public class HighlightingResourceSetAvatarFactory extends DelegatingResourceSetAvatarFactory {

    private HighlightingModel hoverModel;

    public HighlightingResourceSetAvatarFactory(ResourceSetAvatarFactory delegate, HighlightingModel hoverModel) {
        super(delegate);
        assert hoverModel != null;
        this.hoverModel = hoverModel;
    }

    protected void addToHover(ResourceSetAvatar avatar) {
        hoverModel.setHighlightedResourceSet(avatar.getResourceSet());
    }

    @Override
    public ResourceSetAvatar createAvatar(ResourceSet resources) {
        final ResourceSetAvatar avatar = delegate.createAvatar(resources);
        final HandlerRegistration mouseOverHandlerRegistration = avatar.addMouseOverHandler(new MouseOverHandler() {

            @Override
            public void onMouseOver(MouseOverEvent event) {
                addToHover(avatar);
            }
        });
        final HandlerRegistration mouseOutHandlerRegistration = avatar.addMouseOutHandler(new MouseOutHandler() {

            @Override
            public void onMouseOut(MouseOutEvent event) {
                removeFromHover();
            }
        });
        final HandlerRegistration containerChangedHandler = hoverModel.addEventHandler(new ResourceSetDelegateChangedEventHandler() {

            @Override
            public void onResourceSetContainerChanged(ResourceSetDelegateChangedEvent event) {
                avatar.setHover(shouldHighlight(avatar, event.getResourceSet()));
            }
        });
        avatar.addDisposable(new Disposable() {

            @Override
            public void dispose() {
                mouseOverHandlerRegistration.removeHandler();
                mouseOutHandlerRegistration.removeHandler();
                containerChangedHandler.removeHandler();
            }
        });
        return avatar;
    }

    protected void removeFromHover() {
        hoverModel.setHighlightedResourceSet(null);
    }

    protected boolean shouldHighlight(ResourceSetAvatar avatar, ResourceSet resources) {
        ResourceSet dragAvatarResources = avatar.getResourceSet();
        while (dragAvatarResources instanceof UnmodifiableResourceSet) {
            dragAvatarResources = ((DelegatingResourceSet) dragAvatarResources).getDelegate();
        }
        while (resources instanceof UnmodifiableResourceSet) {
            resources = ((DelegatingResourceSet) resources).getDelegate();
        }
        return resources == dragAvatarResources;
    }
}
