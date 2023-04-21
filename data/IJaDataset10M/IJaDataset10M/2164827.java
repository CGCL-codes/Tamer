package abstrasy.libraries.mintk.impl.cap;

import abstrasy.Node;

public interface Shadow_ {

    public void shadow();

    public void unshadow();

    public Node external_shadow(Node node) throws Exception;

    public Node external_unshadow(Node node) throws Exception;
}
