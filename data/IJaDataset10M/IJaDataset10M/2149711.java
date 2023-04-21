package june.tree;

import java.util.*;

public class Parent extends Node {

    public List<Node> kids = new ArrayList<Node>();

    @Override
    public Iterable<Node> getKids() {
        return kids;
    }
}
