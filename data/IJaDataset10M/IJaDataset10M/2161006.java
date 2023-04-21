package openfield.jackrabbit.cookies;

import openfield.jackrabbit.RepNodeVersion;
import org.openide.nodes.Node;

/**
 *
 * @author shader
 */
public interface GetVersionCookie extends Node.Cookie {

    RepNodeVersion getRepNodeVersion();
}
