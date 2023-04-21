/**
 * PLAYHigraphView.java - play.higraph.view - PLAY
 * 
 * Created on Feb 14, 2012 by Kai Zhu
 */
package play.higraph.view;

import higraph.swing.HigraphJComponent;
import higraph.view.HigraphView;
import higraph.view.ViewFactory;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import play.higraph.model.PLAYEdge;
import play.higraph.model.PLAYEdgeLabel;
import play.higraph.model.PLAYHigraph;
import play.higraph.model.PLAYNode;
import play.higraph.model.PLAYPayload;
import play.higraph.model.PLAYSubgraph;
import play.higraph.model.PLAYWholeGraph;
import play.ide.util.Observable;
import play.ide.util.Observer;
import tm.backtrack.BTTimeManager;

/**
 * @author Kai Zhu
 * 
 */
public class PLAYHigraphView
	extends
	HigraphView<PLAYPayload, PLAYEdgeLabel, PLAYHigraph, PLAYWholeGraph, PLAYSubgraph, PLAYNode, PLAYEdge>
	implements Observable {

    private PLAYNodeView currentNodeView;

    private List<PLAYNodeView> deletedNodeViewList;

    private List<Observer> observers;

    private boolean isChanged;

    protected PLAYHigraphView(
	    ViewFactory<PLAYPayload, PLAYEdgeLabel, PLAYHigraph, PLAYWholeGraph, PLAYSubgraph, PLAYNode, PLAYEdge> viewFactory,
	    PLAYHigraph theGraph, Component display, BTTimeManager timeMan) {
	super(viewFactory, theGraph, display, timeMan);
	this.currentNodeView = null;
	this.deletedNodeViewList = new ArrayList<>();
	this.isChanged = false;
	this.observers = new ArrayList<Observer>();
    }

    /**
     * @see higraph.view.HigraphView#refresh()
     */
    @Override
    public void refresh() {
	super.refresh();
	HigraphJComponent playHigraphJComponent = (HigraphJComponent) super
		.getDisplay();
	Dimension dimension = playHigraphJComponent.getPreferredSize();
	playHigraphJComponent.scrollRectToVisible(new Rectangle(0,
		dimension.height, 100, 100));
	this.getDisplay().revalidate();
	this.setChanged();
	this.notifyObservers(this.deletedNodeViewList);
    }

    /**
     * @return the currentNodeView
     */
    public PLAYNodeView getCurrentNodeView() {
	return currentNodeView;
    }

    /**
     * @param currentNodeView
     *            the currentNodeView to set
     */
    public void setCurrentNodeView(PLAYNodeView currentNodeView) {
	this.currentNodeView = currentNodeView;
    }

    /**
     * @return the deletedNodeView
     */
    public PLAYNodeView getDeletedNodeView() {
	PLAYNodeView nodeView = null;
	if (!this.deletedNodeViewList.isEmpty()) {
	    nodeView = this.deletedNodeViewList.get(0);
	    this.deletedNodeViewList.remove(0);
	}
	return nodeView;
    }

    /**
     * @param deletedNodeView
     *            the deletedNodeView to set
     */
    public void setDeletedNodeView(PLAYNodeView deletedNodeView) {
	this.deletedNodeViewList.add(0, deletedNodeView);
    }

    /**
     * @see play.ide.util.Observable#addObserver(play.ide.util.Observer)
     */
    @Override
    public synchronized void addObserver(Observer observer) {
	this.observers.add(observer);
    }

    /**
     * @see play.ide.util.Observable#deleteObserver(play.ide.util.Observer)
     */
    @Override
    public synchronized void deleteObserver(Observer observer) {
	this.observers.add(observer);
    }

    /**
     * @see play.ide.util.Observable#setChanged()
     */
    @Override
    public synchronized void setChanged() {
	this.isChanged = true;
    }

    /**
     * @see play.ide.util.Observable#clearChanged()
     */
    @Override
    public synchronized void clearChanged() {
	this.isChanged = false;
    }

    /**
     * @see play.ide.util.Observable#hasChanged()
     */
    @Override
    public synchronized boolean hasChanged() {
	return this.isChanged;
    }

    /**
     * @see play.ide.util.Observable#notifyObservers(java.lang.Object)
     */
    @Override
    public void notifyObservers(Object arg) {
	Observer[] tempObservers;
	synchronized (this) {
	    if (this.isChanged) {
		tempObservers = this.observers.toArray(new Observer[0]);
		this.clearChanged();
	    } else {
		return;
	    }
	}

	for (Observer observer : tempObservers) {
	    observer.update(this, arg);
	}
    }

}
