package org.modelibra.wicket.concept.navigation;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.modelibra.IEntities;
import org.modelibra.IEntity;
import org.modelibra.wicket.container.DmPanel;
import org.modelibra.wicket.view.View;
import org.modelibra.wicket.view.ViewModel;

/**
 * AjaxFallbackEntityNavigatePanel for link navigation trough entities in
 * viewModel. Four links: first, prior, next and last may be used for
 * controlling the navigation. See getNavigatedPanelId() and
 * getNewPanel(ViewModel viewModel, View view) on how to use this panel.
 * 
 * @author Vedad Kirlic
 * @version 2008-01-19
 */
@SuppressWarnings("serial")
public abstract class AjaxFallbackEntityNavigatePanel extends DmPanel {

    private ViewModel viewModel;

    private View view;

    /**
	 * Constructs a slide navigation panel with the first, next, prior and last
	 * links.
	 * 
	 * @param viewModel
	 *            view model
	 * @param view
	 *            view
	 */
    public AjaxFallbackEntityNavigatePanel(final ViewModel viewModel, final View view) {
        super(view.getWicketId());
        this.viewModel = viewModel;
        this.view = view;
        AjaxFallbackLink firstLink = new AjaxFallbackLink("firstLink") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClick(final AjaxRequestTarget target) {
                setCurrentEntity(getFirst());
                replacePanel(target);
            }
        };
        add(firstLink);
        AjaxFallbackLink priorLink = new AjaxFallbackLink("priorLink") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClick(final AjaxRequestTarget target) {
                setCurrentEntity(getPrior());
                replacePanel(target);
            }
        };
        add(priorLink);
        AjaxFallbackLink nextLink = new AjaxFallbackLink("nextLink") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClick(final AjaxRequestTarget target) {
                setCurrentEntity(getNext());
                replacePanel(target);
            }
        };
        add(nextLink);
        AjaxFallbackLink lastLink = new AjaxFallbackLink("lastLink") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClick(final AjaxRequestTarget target) {
                setCurrentEntity(getLast());
                replacePanel(target);
            }
        };
        add(lastLink);
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
	 * Gets the first entity.
	 * 
	 * @return first entity
	 */
    private IEntity<?> getFirst() {
        IEntities<?> entities = viewModel.getEntities();
        return entities.first();
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
	 * Gets the prior entity based on the current entity.
	 * 
	 * @return prior entity
	 */
    private IEntity<?> getPrior() {
        IEntities entities = viewModel.getEntities();
        IEntity<?> currentEntity = viewModel.getEntity();
        IEntity<?> priorEntity = entities.prior(currentEntity);
        if (priorEntity == null) {
            priorEntity = entities.last();
        }
        return priorEntity;
    }

    /**
	 * Gets the last entity.
	 * 
	 * @return last entity
	 */
    private IEntity<?> getLast() {
        IEntities<?> entities = viewModel.getEntities();
        return entities.last();
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
        if (target != null) {
            target.addComponent(newNavigatedPanel);
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
