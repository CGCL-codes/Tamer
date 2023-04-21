package org.blueoxygen.cimande.controller;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.ExceptionMapping;
import org.apache.struts2.convention.annotation.ExceptionMappings;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.apache.struts2.rest.ContentTypeHandlerManager;
import org.apache.struts2.rest.handler.ContentTypeHandler;
import org.blueoxygen.cimande.entity.Person;
import org.blueoxygen.cimande.entity.User;
import org.blueoxygen.cimande.service.PersonService;
import org.blueoxygen.cimande.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.inject.Container;
import com.opensymphony.xwork2.inject.Inject;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;

/**
 * @author Dian Aditya
 * 
 */
@Actions({ @Action("/user/login"), @Action("/user/register") })
@Results({ @Result(name = "success", type = "velocity", location = "/default.vm") })
@ExceptionMappings({ @ExceptionMapping(exception = "java.lang.NoSuchMethodException", result = "success") })
public class UserController extends ActionSupport implements ModelDriven<Object> {

    private User user = new User();

    private Map<String, Object> model = new HashMap<String, Object>();

    private HttpServletResponse response;

    @Autowired
    private UserService userService;

    private ContentTypeHandler handler = null;

    public UserController() {
        response = ServletActionContext.getResponse();
    }

    @SkipValidation
    public String loginPost() {
        user = userService.getByUserPassword(user.getUsername(), DigestUtils.md5Hex(user.getPassword().getBytes()));
        if (user == null) {
            model.put("encoding", response.getCharacterEncoding());
            model.put("content-type", response.getContentType());
            model.put("status", "failed");
            return SUCCESS;
        }
        user.setSecret(UUID.randomUUID().toString());
        userService.save(user);
        model = new HashMap<String, Object>();
        model.put("encoding", response.getCharacterEncoding());
        model.put("content-type", response.getContentType());
        model.put("status", "success");
        model.put("secret", user.getSecret());
        model.put("id", user.getPerson().getId());
        return SUCCESS;
    }

    @Validations(requiredStrings = { @RequiredStringValidator(fieldName = "username", message = "username"), @RequiredStringValidator(fieldName = "password", message = "password") })
    public String registerPost() {
        Person person = user.getPerson();
        user.setPassword(DigestUtils.md5Hex(user.getPassword().getBytes()));
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date birthdate = new Date();
        try {
            birthdate = format.parse(ServletActionContext.getRequest().getParameter("bbirthdate"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        person.setBirthDate(birthdate);
        user.setPerson(person);
        userService.save(user);
        model = new HashMap<String, Object>();
        model.put("encoding", response.getCharacterEncoding());
        model.put("content-type", response.getContentType());
        model.put("status", "success");
        model.put("username", user.getUsername());
        return SUCCESS;
    }

    @Inject
    public void setManager(Container container) {
        ContentTypeHandlerManager manager = container.getInstance(ContentTypeHandlerManager.class);
        handler = manager.getHandlerForRequest(ServletActionContext.getRequest());
    }

    public String getLoginResult() {
        StringWriter writer = new StringWriter();
        try {
            handler.fromObject(getModel(), "", writer);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e);
        }
        System.out.println(writer.toString());
        return writer.toString();
    }

    @Override
    public Object getModel() {
        return model.size() > 0 ? model : user;
    }
}
