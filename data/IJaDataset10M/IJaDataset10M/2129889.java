package org.eesgmbh.gimv.samples.jfreechart.client;

import org.eesgmbh.gimv.client.event.SetViewportPixelBoundsEvent;
import org.eesgmbh.gimv.client.event.SetViewportPixelBoundsEventHandler;
import org.eesgmbh.gimv.samples.jfreechart.client.img.JFreechartSampleDataServiceAsync;
import org.eesgmbh.gimv.samples.jfreechart.shared.ImageDataRequest;
import com.google.gwt.event.shared.HandlerManager;

public abstract class AbstractJFreechartController {

    protected final HandlerManager handlerManager;

    protected final JFreechartSampleDataServiceAsync jfreechartSampleService;

    protected ImageDataRequest currentImageDataRequest;

    public AbstractJFreechartController(HandlerManager handlerManager, JFreechartSampleDataServiceAsync jfreechartSampleService) {
        this.handlerManager = handlerManager;
        this.jfreechartSampleService = jfreechartSampleService;
        this.handlerManager.addHandler(SetViewportPixelBoundsEvent.TYPE, new SetViewportBoundsEventHandlerImpl());
    }

    /**
	 * The viewport dimensions can change (due to window resize in this sample)
	 */
    private class SetViewportBoundsEventHandlerImpl implements SetViewportPixelBoundsEventHandler {

        public void onSetViewportBounds(SetViewportPixelBoundsEvent event) {
            if (currentImageDataRequest != null) {
                currentImageDataRequest.setWidth(event.getBounds().getWidth().intValue());
                currentImageDataRequest.setHeight(event.getBounds().getHeight().intValue());
            }
        }
    }
}
