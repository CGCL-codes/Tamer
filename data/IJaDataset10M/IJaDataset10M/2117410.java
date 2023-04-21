package edu.ipfw.nitrogo.session;

import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.StringTokenizer;
import javax.xml.rpc.ServiceException;
import org.apache.commons.beanutils.BeanUtils;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Startup;
import org.jboss.seam.log.Log;
import edu.ipfw.nitro.ws.consumer.UserDTO;
import edu.ipfw.nitro.ws.consumer.UserHome;
import edu.ipfw.nitro.ws.consumer.UserHomeServiceLocator;
import edu.ipfw.nitro.ws.consumer.VehicleDTO;

@Name("userWSClient")
@Scope(ScopeType.SESSION)
@Startup
public class UserWSClient {

    @Logger
    private Log log;

    private UserHome userService = null;

    public UserWSClient() {
        UserHome service = null;
        UserHomeServiceLocator locator = new UserHomeServiceLocator();
        String tmpServicePort = System.getProperty("userservice.url");
        try {
            service = locator.getUserHomePort(new URL(tmpServicePort));
        } catch (ServiceException e) {
            log.error("UserHome Service could not be initialized correctly!");
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        if (service != null) userService = service;
    }

    public UserDTO invokeAuthenticate(String email) {
        log.info("In UserWSClient:invokeAuthentication(" + email + ")");
        UserDTO u = null;
        try {
            log.info("UserWSClient:invokeAuthentication invoking service");
            long start = System.currentTimeMillis();
            u = (UserDTO) userService.authenticate(email);
            log.info("UserWSClient:invokeAuthentication service took: " + (System.currentTimeMillis() - start) + " ms");
        } catch (RemoteException e) {
            log.error("UserWSClient:invokeAuthentication resulted in exception:" + e.getMessage());
            e.printStackTrace();
        }
        return u;
    }

    public Collection<String> invokeFindUserRoles(int id) {
        log.info("In UserWSClient:invokeFindUserRoles(" + id + ")");
        Collection<String> roles = new ArrayList<String>();
        String tmp = "";
        try {
            log.info("UserWSClient:invokeFindUserRoles invoking service");
            long start = System.currentTimeMillis();
            tmp = userService.findUserRoles(id);
            log.info("UserWSClient:invokeFindUserRoles service took: " + (System.currentTimeMillis() - start) + " ms");
        } catch (RemoteException e) {
            log.error("UserWSClient:invokeFindUserRoles resulted in exception:" + e.getMessage());
            e.printStackTrace();
        }
        if (tmp.length() > 0) {
            StringTokenizer st = new StringTokenizer(tmp);
            while (st.hasMoreTokens()) {
                roles.add(st.nextToken());
            }
        }
        return roles;
    }

    public Collection<VehicleDTO> invokeFindVehicleByUserId(int id) {
        log.info("In UserWSClient:invokeFindVehicleByUserId(" + id + ")");
        Collection<VehicleDTO> userVehicles = new ArrayList<VehicleDTO>();
        VehicleDTO[] vehicles = null;
        try {
            log.info("UserWSClient:invokeFindVehicleByUserId invoking service");
            long start = System.currentTimeMillis();
            vehicles = userService.findVehiclesByUserId(id);
            log.info("UserWSClient:invokeFindVehicleByUserId service took: " + (System.currentTimeMillis() - start) + " ms");
        } catch (RemoteException e) {
            log.error("UserWSClient:invokeFindVehicleByUserId resulted in exception:" + e.getMessage());
            e.printStackTrace();
        }
        if (vehicles != null) {
            for (int i = 0; i < vehicles.length; i++) {
                userVehicles.add(vehicles[i]);
            }
        }
        return userVehicles;
    }

    public boolean invokeUserExists(String email) {
        log.info("In UserWSClient.invokeUserExists(" + email + ")");
        boolean exists = false;
        try {
            log.info("UserWSClient.invokeUserExists invoking service");
            long start = System.currentTimeMillis();
            exists = userService.userExists(email);
            log.info("UserWSClient.invokeUserExists service took: " + (System.currentTimeMillis() - start));
        } catch (RemoteException e) {
            log.error("UserWSClient.invokeUserExists resulted in exception:" + e.getMessage());
            e.printStackTrace();
        }
        log.info("Exists: " + exists);
        return exists;
    }

    public boolean invokePersistUser(User user) {
        log.info("In UserWSClient.invokePersistUser(" + user.getEmail() + ")");
        boolean success = false;
        try {
            log.info("UserWSClient.invokePersistUser invoking service");
            long start = System.currentTimeMillis();
            UserDTO u = new UserDTO();
            BeanUtils.copyProperties(u, user);
            success = userService.persistUser(u);
            log.info("UserWSClient.invokePersistUser service took: " + (System.currentTimeMillis() - start));
        } catch (RemoteException e) {
            log.error("UserWSClient.invokePersistUser resulted in exception:" + e.getMessage());
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            log.error("UserWSClient.invokePersistUser copyProperties resulted in exception:" + e.getMessage());
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            log.error("UserWSClient.invokePersistUser copyProperties resulted in exception:" + e.getMessage());
            e.printStackTrace();
        }
        return success;
    }

    public boolean invokePersistVehicleForUser(VehicleDTO vdto, int userId) {
        log.info("In UserWSClient.invokePersistVehicleForUser(" + userId + ")");
        boolean success = false;
        try {
            log.info("UserWSClient.invokePersistVehicleForUser invoking service");
            long start = System.currentTimeMillis();
            success = userService.persistUserVehicle(vdto, userId);
            log.info("UserWSClient.invokePersistVehicleForUser service took: " + (System.currentTimeMillis() - start));
        } catch (RemoteException e) {
            log.error("UserWSClient.invokePersistVehicleForUser resulted in exception:" + e.getMessage());
            e.printStackTrace();
        }
        return success;
    }
}
