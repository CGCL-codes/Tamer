package saeed.beans;

import java.sql.*;
import javax.sql.*;
import javax.naming.*;
import javax.servlet.http.*;
import javax.ejb.*;
import java.util.*;
import java.rmi.RemoteException;
import saeed.*;

public interface GroupHome extends EJBHome {

    public Group create(String name, String fname) throws CreateException, RemoteException;

    public Group findByPrimaryKey(String name) throws RemoteException, ObjectNotFoundException;

    public Collection findAll() throws RemoteException, ObjectNotFoundException;
}
