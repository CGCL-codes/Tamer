package demo;

import java.io.Serializable;

/**
 *  A node in a patch tree
 * 
 *  Patches can be defined in groups. 
 *  
 *  A node either has data which is a set of child nodes or 
 *  data which is a MyPatch.
 * 
 * @author Paul John Leonard
 *
 */
public class Node implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    String name;

    Serializable data;

    String[] keynames = null;

    public Node(String name, Serializable data) {
        this.name = name;
        this.data = data;
    }

    public String[] getKeyNames() {
        return keynames;
    }

    public String toString() {
        return name;
    }

    public Serializable getData() {
        return data;
    }
}
