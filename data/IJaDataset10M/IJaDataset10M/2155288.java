package eulergui.n3model;

import java.net.URI;

/**
 *
 * @author luc peuvrier
 *
 */
public interface IVariable extends Comparable<IVariable> {

    boolean isUri();

    boolean isBlankNode();

    IBlankNode getBlankNode();

    String getBlankNodeIdentifier();

    IURI getIUri();

    URI getUri();

    boolean isUniversal();

    boolean isExistential();
}
