package org.modelibra.wicket.concept.navigation;

import java.util.Arrays;
import java.util.List;
import org.apache.wicket.ajax.AbstractAjaxTimerBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.PackageResource;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.time.Duration;
import org.modelibra.IEntities;
import org.modelibra.IEntity;
import org.modelibra.wicket.container.DmPanel;
import org.modelibra.wicket.view.View;
import org.modelibra.wicket.view.ViewModel;

/**
 * AjaxEntitySlideshowPanel for slideshow navigation trough entities in
 * viewModel. There is a slide timer, controlled by play and stop links, which
 * uses time (secs) selected in timerUpdateIntervalChoice as its update
 * interval. See getNavigatedPanelId() and getNewPanel(ViewModel viewModel, View
 * view) on how to use this panel.
 * 
 * @author Vedad Kirlic
 * @version 2008-02-07
 */
@SuppressWarnings("serial")
public abstract class AjaxEntitySlideshowPanel extends DmPanel {

    private static final Integer DEFAULT_UPDATE_INTERVAL = 5;

    private static final List<Integer> UPDATE_INTERVAL_LIST = Arrays.asList(5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55, 60);

    private ViewModel viewModel;

    private View view;

    private boolean play = false;

    private SlideTimer slideTimer;

    private AjaxLink playLink;

    private AjaxLink stopLink;

    /**
	 * Constructs a slideshow navigation panel with the slide timer.
	 * 
	 * @param viewModel
	 *            view model
	 * @param view
	 *            view
	 */
    public AjaxEntitySlideshowPanel(final ViewModel viewModel, final View view) {
        super(view.getWicketId());
        this.viewModel = viewModel;
        this.view = view;
        slideTimer = new SlideTimer(Duration.seconds(DEFAULT_UPDATE_INTERVAL));
        add(slideTimer);
        Integer slideTimerUpdateInterval = DEFAULT_UPDATE_INTERVAL;
        DropDownChoice timerUpdateIntervalChoice = new DropDownChoice("timerUpdateIntervalChoice", new Model(slideTimerUpdateInterval), UPDATE_INTERVAL_LIST);
        timerUpdateIntervalChoice.add(new AjaxFormComponentUpdatingBehavior("onchange") {

            @Override
            protected void onUpdate(final AjaxRequestTarget target) {
                slideTimer.changeUpdateInterval((Integer) getComponent().getModelObject());
            }
        });
        add(timerUpdateIntervalChoice);
        playLink = new AjaxLink("playLink") {

            @Override
            public void onClick(final AjaxRequestTarget target) {
                play = true;
                stopLink.setVisible(true);
                playLink.setVisible(false);
                target.addComponent(playLink);
                target.addComponent(stopLink);
            }
        };
        playLink.add(new Image("play", PackageResource.get(this.getClass().getSuperclass(), "media-playback-start.png")));
        add(playLink.setOutputMarkupId(true).setOutputMarkupPlaceholderTag(true));
        stopLink = new AjaxLink("stopLink") {

            @Override
            public void onClick(final AjaxRequestTarget target) {
                play = false;
                stopLink.setVisible(false);
                playLink.setVisible(true);
                target.addComponent(playLink);
                target.addComponent(stopLink);
            }
        };
        stopLink.add(new Image("stop", PackageResource.get(this.getClass().getSuperclass(), "media-playback-stop.png")));
        add(stopLink.setOutputMarkupId(true).setOutputMarkupPlaceholderTag(true).setVisible(false));
    }

    /**
	 * Sets the current entity.
	 * 
	 * @param currentEntity
	 *            current entity
	 */
    private void setCurrentEntity(IEntity<?> currentEntity) {
        viewModel.setEntity(currentEntity);
    }

    /**
	 * Gets the next entity based on the current entity.
	 * 
	 * @return next entity
	 */
    private IEntity<?> getNext() {
        IEntities entities = viewModel.getEntities();
        IEntity<?> currentEntity = viewModel.getEntity();
        IEntity<?> nextEntity = entities.next(currentEntity);
        if (nextEntity == null) {
            nextEntity = entities.first();
        }
        return nextEntity;
    }

    /**
	 * Replaces the current panel with the new one (retrieved by
	 * getNavigatedPanel).
	 * 
	 * @param target
	 *            Ajax request target
	 */
    private void replacePanel(final AjaxRequestTarget target) {
        Panel currentNavigatedPanel = (Panel) getParent().get(getNavigatedPanelId());
        view.setWicketId(currentNavigatedPanel.getId());
        Panel newNavigatedPanel = getNavigatedPanel(viewModel, view);
        newNavigatedPanel.setOutputMarkupId(true);
        currentNavigatedPanel.replaceWith(newNavigatedPanel);
        target.addComponent(newNavigatedPanel);
    }

    /**
	 * Ajax slide timer.
	 */
    private class SlideTimer extends AbstractAjaxTimerBehavior {

        /**
		 * Constructs a slide timer using the slide update interval.
		 * 
		 * @param updateInterval
		 *            update interval
		 */
        private SlideTimer(final Duration updateInterval) {
            super(updateInterval);
        }

        /**
		 * Implements on timer.
		 * 
		 * @param target
		 *            Ajax request target
		 */
        @Override
        protected void onTimer(final AjaxRequestTarget target) {
            if (play) {
                setCurrentEntity(getNext());
                replacePanel(target);
            }
        }

        /**
		 * Changes the update interval.
		 * 
		 * @param updateInterval
		 *            update interval
		 */
        private void changeUpdateInterval(Integer updateInterval) {
            setUpdateInterval(Duration.seconds(updateInterval));
        }
    }

    /**
	 * Gets id for navigated panel. Implement this method to provide id of panel
	 * to be navigated. Need to be valid id of one of panels added to common
	 * parent.
	 * 
	 * @return navigated panel id
	 */
    protected abstract String getNavigatedPanelId();

    /**
	 * Gets new instance of navigated panel. Implement this method to provide
	 * new instance of navigated panel that will be used to replace current
	 * instance of navigated panel
	 * 
	 * @param viewModel
	 * @param view
	 * 
	 * @return new instance of navigated panel
	 */
    protected abstract Panel getNavigatedPanel(final ViewModel viewModel, final View view);
}
