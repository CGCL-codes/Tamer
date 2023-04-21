package tr.view.contexts;

import org.openide.nodes.Node;

/**
 * Delete context cookie.
 *
 * @author <a href="mailto:jimoore@netspace.net.au">Jeremy Moore</a>
 */
public interface DeleteContextCookie extends Node.Cookie {

    public void deleteContext();
}
