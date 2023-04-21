package com.picturehistory.core.jersey.resources;

import java.util.Date;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import com.picturehistory.core.jersey.exception.InvalidPinException;
import com.picturehistory.core.jersey.exception.InvalidUserException;
import com.picturehistory.core.jersey.model.User;
import com.picturehistory.core.jersey.services.UserService;
import com.picturehistory.core.jersey.util.PIN;
import com.picturehistory.core.jersey.util.Response;

@Path("/user")
@Component
public class UserResource {

    private final Logger logger = LoggerFactory.getLogger(UserResource.class);

    @Autowired
    private UserService service;

    @GET
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public User getUserByIdNumber(@Context UriInfo uri, @HeaderParam("idNumber") String idNumber) {
        logger.info(uri.getPath() + " : getUserByIdNumber(" + idNumber + ")");
        if (idNumber == null) {
            logger.info("provided User is null");
            return null;
        }
        User ret = service.getUserByIdNumber(idNumber);
        return ret;
    }

    @Path("/account")
    @GET
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public User getUserByUserId(@Context UriInfo uri, @HeaderParam("userId") String userId, @QueryParam("detach") @DefaultValue("") String detach) {
        logger.info(uri.getPath() + " : getUserByUserId(" + userId + ", " + "detach=" + detach + ")");
        logger.info("trying to retrieve a user object with userId = " + userId);
        if (userId == null) {
            logger.info("provided User is null");
            return null;
        }
        User ret = null;
        if (detach.equals("")) {
            ret = service.getUserByUserId(userId);
        } else {
            ret = service.getDetachedUserByUserId(userId);
        }
        logger.info(ret.toString());
        return ret;
    }

    @POST
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public PIN createUser(@Context UriInfo uri, @HeaderParam("idNumber") String idNumber, @HeaderParam("deviceSN") String deviceSN, @HeaderParam("password") String password, @HeaderParam("userId") String userId) {
        logger.info(uri.getPath() + " : createUser(" + idNumber + ", " + password + ", " + deviceSN + ", " + userId + ")");
        PIN ret = service.store(userId, password, idNumber, deviceSN);
        return ret;
    }

    @Path("/duplicate")
    @GET
    public Response checkDuplicatePhoneNumber(@Context UriInfo uri, @HeaderParam("idNumber") String idNumber) {
        logger.info(uri.getPath() + " : checkDuplicatePhoneNumber(" + idNumber + ")");
        if (idNumber == null) {
            logger.info("provided idNumber is null");
            return null;
        }
        Response ret = new Response();
        User user = service.getUserByIdNumber(idNumber);
        if (user != null) {
            ret.setMessage("DUPLICATE");
        } else {
            ret.setMessage("OK");
        }
        ret.setResult(Response.OK);
        return ret;
    }

    @Path("/duplicate/account")
    @GET
    public Response checkDuplicateUserId(@Context UriInfo uri, @HeaderParam("userid") String userId) {
        logger.info(uri.getPath() + " : checkDuplicateUserId(" + userId + ")");
        if (userId == null) {
            logger.info("provided userId is null");
            return null;
        }
        Response ret = new Response();
        User user = service.getUserByUserId(userId);
        if (user != null) {
            ret.setMessage("DUPLICATE");
        } else {
            ret.setMessage("OK");
        }
        ret.setResult(Response.OK);
        return ret;
    }

    @Path("/auth")
    @POST
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public PIN handleLogin(@Context UriInfo uri, @HeaderParam("idNumber") String idNumber, @HeaderParam("pin") String pin) {
        logger.info(uri.getPath() + " : handleLogin(" + idNumber + ", " + pin + ")");
        PIN ret = new PIN();
        if (idNumber == null) {
            logger.info("provided idNumber is null");
            ret.setMessage("provided idNumber is null");
            ret.setResult(PIN.FAILED);
        } else if (pin == null) {
            logger.info("provided pin is null");
            ret.setMessage("provided pin is null");
            ret.setResult(PIN.FAILED);
        } else {
            try {
                ret = service.login(idNumber, pin);
            } catch (InvalidUserException e) {
                ret.setMessage("User cannot be found: " + e.getMessage());
                ret.setResult(PIN.FAILED);
            } catch (InvalidPinException e) {
                ret.setMessage("PIN is not valid: " + e.getMessage());
                ret.setResult(PIN.FAILED);
            }
        }
        return ret;
    }

    @Path("/validate")
    @POST
    public PIN validateUser(@Context UriInfo uri, @HeaderParam("userId") String userId, @HeaderParam("password") String password) {
        logger.info(uri.getPath() + " : validateUser (" + userId + ", " + password + ")");
        PIN ret = new PIN();
        if (userId == null) {
            ret.setResult(PIN.FAILED);
            ret.setMessage("provided userId is null");
            return null;
        } else if (password == null) {
            ret.setResult(PIN.FAILED);
            ret.setMessage("provided password is null");
            return null;
        } else {
            ret = service.validateUser(userId, password);
        }
        return ret;
    }
}
