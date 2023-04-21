package ubs.persistence.user;

import java.util.Collection;
import javax.ejb.EJBLocalObject;
import javax.ejb.FinderException;

/**
 * @author Rommel Carvalho
 *
 * This classe is responsible for 
 */
public interface LocalUser extends EJBLocalObject {

    public int getUserId();

    public String getName();

    public String getPhone();

    public String getEmail();

    public String getSex();

    public LocalGroup getGroup();

    public Collection getCases() throws FinderException;

    public Collection getEvidences() throws FinderException;
}
