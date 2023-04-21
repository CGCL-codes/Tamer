package org.patterncoder.dataModel;

import java.awt.Image;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Describes a design pattern with the attributes name, description, image and
 * components.<br> 
 * Examples: name = Observer<br> 
 * description = Define a one-to many ...<br> 
 * image = Observer.bmp<br> 
 * components[0] = Observer<br>
 * components[1] = Observable<br> 
 * ... <br>
 *
 * @author Florian Siebler
 */
public class Pattern implements Comparable {

    /**
     * Name of the pattern
     */
    public final String NAME;

    /**
     * Describes the pattern
     */
    public final String DESC;

    /**
     * Name of the image
     */
    public final String IMAGE_DIR;

    /**
     * List of components
     */
    private List<PatternComponent> components = new ArrayList<PatternComponent>();

    /**
     * Image of pattern
     */
    private Image image;

    public Pattern(String name, String desc, String imageDir) {
        this.NAME = name;
        this.DESC = desc.trim();
        this.IMAGE_DIR = imageDir;
    }

    /**
     * Checks if the given component is the last component
     *
     * @param currentComponent Component to check
     * @return true if given coomponent is last
     */
    public boolean isLastComponent(PatternComponent currentComponent) {
        PatternComponent tempComponent = this.components.get(components.size() - 1);
        return tempComponent == currentComponent;
    }

    /**
     * Return a component at requested index
     *
     * @param index Index of component
     * @return Component at requested index
     */
    public PatternComponent getComponent(int index) {
        return components.get(index);
    }

    /**
     * Return the antecessor of a given component
     *
     * @param currentComponent Component whose antecessor is requested
     * @return Requested antecessor
     */
    public PatternComponent getPreviousComponent(PatternComponent currentComponent) {
        int index = components.indexOf(currentComponent);
        return components.get(index - 1);
    }

    /**
     * Return the successor of a given component
     *
     * @param tempComponent Component whose successor is requested
     * @return The successor of the given component
     */
    public PatternComponent getNextComponent(PatternComponent tempComponent) {
        int index = components.indexOf(tempComponent);
        return components.get(index + 1);
    }

    /**
     * Returns the Image of the pattern
     *
     * @return Image of pattern
     */
    public Image getImage() {
        return image;
    }

    /**
     * Sets the image of the pattern
     *
     * @param image Image of the pattern
     */
    public void setImage(Image image) {
        this.image = image;
    }

    /**
     * Return the index of a given component
     *
     * @param component Component whose index is required
     * @return Index of component
     */
    public int getStepNumber(PatternComponent component) {
        return components.indexOf(component);
    }

    /**
     * Return the number of components in the pattern. The number of components
     * equals the number of steps in the wizard
     *
     * @return Number of components
     */
    public int stepCount() {
        return components.size();
    }

    /**
     * Returns the first component of the pattern
     *
     * @return First Compo or null if pattern has no components
     */
    public PatternComponent getFirstComponent() {
        if (components.size() > 0) {
            return components.get(0);
        } else {
            return null;
        }
    }

    /**
     * Returns all components of the pattern
     *
     * @return All components of the pattern
     */
    public PatternComponent[] getAllComponents() {
        PatternComponent[] result = new PatternComponent[components.size()];
        components.toArray(result);
        return result;
    }

    /**
     * Adds a component to the list of components
     *
     * @param component A new component
     */
    public void addComponent(PatternComponent component) {
        this.components.add(component);
        Collections.sort(components);
    }

    @Override
    public int compareTo(Object otherPattern) {
        Pattern tempPattern = (Pattern) otherPattern;
        String otherDescription = tempPattern.DESC;
        return DESC.compareTo(otherDescription);
    }

    @Override
    public String toString() {
        return this.NAME;
    }
}
