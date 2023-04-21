package net.playbesiege.path;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public abstract class AbstractPathFinder implements PathFinder {

    public AbstractPathFinder(AStarNode node, AStarNode node1) {
        listeners = new ArrayList<PathListener>();
        start = node;
        goals = Collections.singletonList(node1);
    }

    public AbstractPathFinder(AStarNode node, Collection<AStarNode> collection) {
        listeners = new ArrayList<PathListener>();
        start = node;
        goals = new ArrayList<AStarNode>(collection);
    }

    public void abort() {
        aborted = true;
    }

    public void run() {
        List<AStarNode> list = findPath();
        PathEvent pathevent = new PathEvent(this, list);
        PathListener pathlistener;
        for (Iterator<PathListener> iterator = listeners.iterator(); iterator.hasNext(); pathlistener.done(pathevent)) pathlistener = (PathListener) iterator.next();
    }

    protected void fireConsidered() {
        PathEvent pathevent = new PathEvent(this, null);
        PathListener pathlistener;
        for (Iterator<PathListener> iterator = listeners.iterator(); iterator.hasNext(); pathlistener.considered(pathevent)) pathlistener = (PathListener) iterator.next();
    }

    public void addPathListener(PathListener pathlistener) {
        listeners.add(pathlistener);
    }

    public void removePathListener(PathListener pathlistener) {
        listeners.remove(pathlistener);
    }

    protected AStarNode start;

    protected Collection<AStarNode> goals;

    protected List<PathListener> listeners;

    protected boolean aborted;
}
