package com.mia.sct.transition;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Container;
import com.mia.sct.transition.strategy.ITransitionStrategy;

/**
 * AnimationTransitionLayout
 * 
 * Card layout that provides pluggable animation strategies
 *
 * @author Devon Bryant
 * @since May 17, 2008
 */
@SuppressWarnings("serial")
public class AnimationTransitionLayout extends CardLayout implements ITransitionTimingTarget {

    private ITransitionStrategy transitionStrategy = null;

    private String transitionPanelName = "TRANSITION_PANEL";

    private Container currentParent = null;

    private String endingComponentName = null;

    private boolean nameAddedInternally = false;

    /**
	 * Constructor
	 * @param inTransitionStrategy the transition strategy to use
	 */
    public AnimationTransitionLayout(ITransitionStrategy inTransitionStrategy) {
        super();
        transitionStrategy = inTransitionStrategy;
    }

    /**
	 * Set the transition strategy to use
	 * @param inTransitionStrategy the transition strategy to use
	 */
    public void setTransitionStrategy(ITransitionStrategy inTransitionStrategy) {
        transitionStrategy = inTransitionStrategy;
    }

    /**
	 * Get the current transition strategy
	 * @return the current transition strategy
	 */
    public ITransitionStrategy getTransitionStrategy() {
        return transitionStrategy;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void addLayoutComponent(String name, Component comp) {
        if (transitionStrategy != null) {
            if (transitionPanelName.equals(name) && !nameAddedInternally) {
                while (transitionStrategy.getComponentNames().contains(transitionPanelName)) {
                    transitionPanelName += "1";
                }
            }
            if (!nameAddedInternally) {
                transitionStrategy.addComponent(name, comp);
            }
        }
        nameAddedInternally = false;
        super.addLayoutComponent(name, comp);
    }

    @Override
    public void first(Container parent) {
        currentParent = parent;
        if (!transitionStrategy.getComponentNames().isEmpty()) {
            transitionTo(transitionStrategy.getComponentNames().get(0), TransitionDirection.UNKNOWN);
        }
    }

    @Override
    public void last(Container parent) {
        currentParent = parent;
        if (!transitionStrategy.getComponentNames().isEmpty()) {
            transitionTo(transitionStrategy.getComponentNames().get(transitionStrategy.getComponentNames().size() - 1), TransitionDirection.UNKNOWN);
        }
    }

    @Override
    public void next(Container parent) {
        currentParent = parent;
        int currentIndex = getCurrentComponentIndex(parent);
        int nextIndex = 0;
        if (currentIndex > -1) {
            if (!(currentIndex >= (transitionStrategy.getComponentNames().size() - 1))) {
                nextIndex = currentIndex + 1;
            }
            transitionTo(transitionStrategy.getComponentNames().get(nextIndex), TransitionDirection.FORWARD);
        }
    }

    @Override
    public void previous(Container parent) {
        currentParent = parent;
        int currentIndex = getCurrentComponentIndex(parent);
        int previousIndex = transitionStrategy.getComponentNames().size() - 1;
        if (currentIndex > -1) {
            if (!(currentIndex <= 0)) {
                previousIndex = currentIndex - 1;
            }
            transitionTo(transitionStrategy.getComponentNames().get(previousIndex), TransitionDirection.BACKWARD);
        }
    }

    @Override
    public void removeLayoutComponent(Component comp) {
        super.removeLayoutComponent(comp);
        if (transitionStrategy != null) {
            transitionStrategy.removeComponent(comp);
        }
    }

    @Override
    public void show(Container parent, String name) {
        currentParent = parent;
        transitionTo(name, TransitionDirection.UNKNOWN);
    }

    public void setupForAnimation() {
        nameAddedInternally = true;
        currentParent.add(transitionStrategy.getTransitionDisplayComponent(), transitionPanelName);
        super.show(currentParent, transitionPanelName);
    }

    public void begin() {
    }

    public void end() {
        currentParent.remove(transitionStrategy.getTransitionDisplayComponent());
        super.show(currentParent, endingComponentName);
    }

    public void repeat() {
    }

    public void timingEvent(float arg0) {
    }

    /**
	 * Transition to the next component
	 * @param inComponentName the component name to transition to
	 * @param inDirection the direction of the transition
	 */
    private void transitionTo(String inComponentName, TransitionDirection inDirection) {
        String currentComponentName = getCurrentComponentName(currentParent);
        if (!transitionPanelName.equals(currentComponentName)) {
            setupTransition(currentComponentName, inComponentName, inDirection);
        }
        currentComponentName = null;
    }

    /**
	 * Setup the transition
	 * @param inFromName the component name to start the transition
	 * @param inToName the component name to end the transition
	 * @param inDirection the direction of the transition
	 */
    private void setupTransition(String inFromName, String inToName, TransitionDirection inDirection) {
        if (transitionStrategy != null) {
            if ((inFromName != null) && (inToName != null) && !(inFromName.equals(inToName))) {
                endingComponentName = inToName;
                transitionStrategy.transition(inFromName, inToName, inDirection, this);
            }
        }
    }

    /**
	 * Get the current visible component
	 * @param inParent the parent container to check
	 * @return the current visible component
	 */
    private Component getCurrentComponent(Container inParent) {
        Component result = null;
        for (Component tmpComp : inParent.getComponents()) {
            if (tmpComp.isVisible()) {
                result = tmpComp;
            }
        }
        return result;
    }

    /**
	 * Get the index of the current visible component
	 * @param inParent the parent container to check
	 * @return the index of the current visible component
	 */
    private int getCurrentComponentIndex(Container inParent) {
        int result = -1;
        for (int i = 0; i < inParent.getComponentCount(); i++) {
            if (inParent.getComponent(i).isVisible()) {
                result = i;
            }
        }
        return result;
    }

    /**
	 * Get the name of the visible component
	 * @param inParent the parent container to check
	 * @return the name of the current visible component
	 */
    private String getCurrentComponentName(Container inParent) {
        String result = null;
        Component currentComp = getCurrentComponent(inParent);
        if (transitionStrategy != null) {
            for (String key : transitionStrategy.getComponentNames()) {
                if (transitionStrategy.getComponent(key) == currentComp) {
                    result = key;
                }
            }
        }
        currentComp = null;
        return result;
    }
}
