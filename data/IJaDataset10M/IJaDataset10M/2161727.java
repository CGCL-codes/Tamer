package com.liferay.portal.auth;

/**
 * <a href="BasicPrincipalFinder.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 * @version $Revision: 1.5 $
 *
 */
public class BasicPrincipalFinder implements PrincipalFinder {

    public String fromLiferay(String name) throws PrincipalFinderException {
        return name;
    }

    public String toLiferay(String name) throws PrincipalFinderException {
        return name;
    }
}
