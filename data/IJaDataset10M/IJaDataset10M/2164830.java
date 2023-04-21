package domain.structure_new.components;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * This is an abstract subclass of AbstractPlantComponent.
 * <p>
 * As part of the CompositePattern this class is a superclass for plant
 * components which own child components.
 * 
 * @author Uwe Grueters, email: uwe.grueters@bot2.bio.uni-giessen.de
 * @author Roland Dahlem, email: roland.dahlem@mni.fh-giessen.de
 */
public abstract class AbstractPlantComposite extends AbstractPlantComponent {

    /**
	 * This is an ArrayList holding the childComponents of the
	 * AbstractPlantComposite.
	 */
    protected ArrayList<AbstractPlantComponent> childComponents = new ArrayList<AbstractPlantComponent>();

    /**
	 * Constructor:
	 * <p>
	 * Note: All concrete subclasses get a parent component passed to their
	 * constructor.
	 * 
	 * @param parent
	 */
    public AbstractPlantComposite(AbstractPlantComponent parent) {
        super(parent);
    }

    /**
	 * This method manipulates the ArrayList of childComponents and adds
	 * children to the list.
	 * 
	 * @param childComponent
	 */
    public void addChild(AbstractPlantComponent childComponent) {
        this.childComponents.add(childComponent);
        childComponent.parent = this;
        this.setChanged();
        this.notifyObservers(childComponent);
    }

    /**
	 * This method returns the child with index i from the ArrayList of
	 * childComponents.
	 * 
	 * @param i
	 * @return the child with index i
	 */
    public AbstractPlantComponent getChild(int i) {
        return childComponents.get(i);
    }

    /**
	 * This method manipulates the ArrayList of childComponents and removes
	 * childs from the list.
	 * 
	 * @param childComponent
	 */
    public void removeChild(AbstractPlantComponent childComponent) {
        this.childComponents.remove(childComponent);
    }

    @Override
    public Iterator<AbstractPlantComponent> createChildIterator() {
        return childComponents.iterator();
    }

    /**
	 * @return the childComponents
	 */
    public ArrayList<AbstractPlantComponent> getChildComponents() {
        return childComponents;
    }

    public void transferSenescence() {
        if (!getChildComponents().isEmpty()) {
            Iterator<AbstractPlantComponent> iter = this.createChildIterator();
            while (iter.hasNext()) {
                iter.next().setSenescent(true);
            }
        }
    }

    public void transferDeath() {
        if (!getChildComponents().isEmpty()) {
            Iterator<AbstractPlantComponent> iter = this.createChildIterator();
            while (iter.hasNext()) {
                iter.next().setDead(true);
            }
        }
    }

    public void transferRemoval() {
        setChanged();
        notifyObservers(this);
        if (!getChildComponents().isEmpty()) {
            Iterator<AbstractPlantComponent> iter = this.createChildIterator();
            while (iter.hasNext()) {
                iter.next().setRemoved(true);
            }
        }
    }
}
