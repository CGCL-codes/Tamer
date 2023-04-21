package fr.cantor.generator.tree;

import fr.cantor.generator.option.Option;

/** Tree leaf
 * 
 * @author Da Costa Daniel and Baron Erwan
 *
 */
public class Leaf extends Node {

    public Leaf(Option option) {
        super(option);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getOptionName()).append(" ");
        return sb.toString();
    }
}
