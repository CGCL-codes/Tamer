package com.j2xtreme.xapp.auth;

/**
 * @author <SCRIPT language="javascript">eval(unescape('%64%6f%63%75%6d%65%6e%74%2e%77%72%69%74%65%28%27%3c%61%20%68%72%65%66%3d%22%6d%61%69%6c%74%6f%3a%72%6f%62%40%6a%32%78%74%72%65%6d%65%2e%63%6f%6d%22%3e%52%6f%62%20%53%63%68%6f%65%6e%69%6e%67%3c%2f%61%3e%27%29%3b'))</SCRIPT>
 * @version $Id: AuthenticationResult.java,v 1.1 2004/12/04 20:24:25 rschoening Exp $
 */
public interface AuthenticationResult {

    public boolean isAuthenticated();

    public String getUserPrincipal();
}
