package org.jalgo.module.avl.algorithm;

import org.jalgo.main.util.Messages;
import org.jalgo.module.avl.datastructure.*;
import org.jalgo.module.avl.*;

/**
 * @author Ulrike Fischer
 * 
 * The class <code>Search</codes> searches a key in the tree.
 */
public class Search extends MacroCommand implements Constants {

    private WorkNode wn;

    private boolean firstundo = false;

    /**
	 * @param wn reference to the position in the tree, holds the search key
	 */
    @SuppressWarnings("unchecked")
    public Search(WorkNode wn) {
        super();
        name = Messages.getString("avl", "Alg_name.Search");
        this.wn = wn;
        if (wn.getNextToMe() == null) {
            results.add(0, Messages.getString("avl", "Tree_empty_key_not_found"));
            results.add(1, "search1");
            results.add(2, NOTFOUND);
            wn.setVisualizationStatus(Visualizable.INVISIBLE);
        } else {
            commands.add(CommandFactory.createCompareKey(wn));
            results.add(0, wn.getKey() + Messages.getString("avl", "Key_search"));
            results.add(1, "search1");
            results.add(2, WORKING);
            wn.getNextToMe().setVisualizationStatus(Visualizable.FOCUSED);
        }
    }

    /**
	 * Searches the key of the WorkNode in the tree, returns FOUND if the key
	 * was found, WORKING if the algorithm isn't yet finished or NOTFOUND if the
	 * key was not found. In the last case Search also returns the position to
	 * insert (LEFT or RIGHT).
	 */
    @SuppressWarnings("unchecked")
    public void perform() {
        results.clear();
        Command c = commands.get(currentPosition);
        c.perform();
        currentPosition++;
        if (c instanceof NoOperation) {
            results.add(0, Messages.getString("avl", "Key_found"));
            results.add(1, "search1");
            results.add(2, FOUND);
            wn.getNextToMe().setVisualizationStatus(Visualizable.NORMAL);
            return;
        }
        Integer compareresult = (Integer) c.getResults().get(0);
        switch(compareresult) {
            case 0:
                {
                    results.add(0, wn.getKey() + " = " + wn.getNextToMe().getKey());
                    results.add(1, "search1");
                    results.add(2, WORKING);
                    wn.setVisualizationStatus(Visualizable.INVISIBLE);
                    setNodesTo(wn.getNextToMe(), Visualizable.NORMAL);
                    wn.getNextToMe().setVisualizationStatus(Visualizable.FOCUSED | Visualizable.LINE_NORMAL);
                    if (currentPosition == commands.size()) commands.add(CommandFactory.createNoOperation());
                    firstundo = true;
                    break;
                }
            case -1:
                {
                    if (wn.getNextToMe().getLeftChild() == null) {
                        results.add(0, wn.getKey() + " < " + wn.getNextToMe().getKey() + Messages.getString("avl", "Search_not_found"));
                        results.add(1, "search1");
                        results.add(2, NOTFOUND);
                        results.add(3, LEFT);
                        wn.setVisualizationStatus(Visualizable.INVISIBLE);
                        setNodesTo(wn.getNextToMe(), Visualizable.NORMAL);
                        firstundo = true;
                    } else {
                        results.add(0, wn.getKey() + " < " + wn.getNextToMe().getKey() + Messages.getString("avl", "Search_step_to_left"));
                        results.add(1, "search1");
                        results.add(2, WORKING);
                        wn.setNextToMe(wn.getNextToMe().getLeftChild());
                        wn.getNextToMe().setVisualizationStatus(Visualizable.FOCUSED);
                        if (currentPosition == commands.size()) commands.add(CommandFactory.createCompareKey(wn));
                    }
                    break;
                }
            case 1:
                {
                    if (wn.getNextToMe().getRightChild() == null) {
                        results.add(0, wn.getKey() + " > " + wn.getNextToMe().getKey() + Messages.getString("avl", "Search_not_found"));
                        results.add(1, "search1");
                        results.add(2, NOTFOUND);
                        results.add(3, RIGHT);
                        wn.setVisualizationStatus(Visualizable.INVISIBLE);
                        setNodesTo(wn.getNextToMe(), Visualizable.NORMAL);
                        firstundo = true;
                    } else {
                        results.add(0, wn.getKey() + " > " + wn.getNextToMe().getKey() + Messages.getString("avl", "Search_step_to_right"));
                        results.add(1, "search1");
                        results.add(2, WORKING);
                        wn.setNextToMe(wn.getNextToMe().getRightChild());
                        wn.getNextToMe().setVisualizationStatus(Visualizable.FOCUSED);
                        if (currentPosition == commands.size()) commands.add(CommandFactory.createCompareKey(wn));
                    }
                    break;
                }
        }
    }

    /**
	 * Realizes undo by changing the position of the WorkNode
	 */
    @SuppressWarnings("unchecked")
    public void undo() {
        results.clear();
        results.add(Messages.getString("avl", "Step_undone"));
        results.add("search1");
        currentPosition--;
        Command c = commands.get(currentPosition);
        if (c instanceof NoOperation) wn.getNextToMe().setVisualizationStatus(Visualizable.FOCUSED | Visualizable.LINE_NORMAL); else if (firstundo) {
            wn.setVisualizationStatus(Visualizable.NORMAL);
            setNodesTo(wn.getNextToMe(), Visualizable.FOCUSED);
            firstundo = false;
        } else {
            wn.getNextToMe().setVisualizationStatus(Visualizable.NORMAL);
            wn.setNextToMe(wn.getNextToMe().getParent());
        }
    }

    /**
	 * Performs all single steps.
	 */
    public void performBlockStep() {
        while (this.hasNext()) perform();
    }

    /**
	 * Realizes undo for all performs.
	 */
    public void undoBlockStep() {
        while (this.hasPrevious()) undo();
    }

    private void setNodesTo(Node n, int status) {
        while (n != null) {
            n.setVisualizationStatus(status);
            n = n.getParent();
        }
    }
}
