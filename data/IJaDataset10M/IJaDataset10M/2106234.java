package com.inet.qlcbcc.controller;

import static org.webos.core.util.Base64Utils.decodeString;
import javax.servlet.http.HttpServletRequest;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.webos.core.convert.TypeDescriptor;
import org.webos.core.message.ResponseMessage;
import org.webos.core.option.Option;
import com.inet.qlcbcc.dto.Id;
import com.inet.qlcbcc.facade.AuthenticationModeFacade;
import com.inet.qlcbcc.facade.AuthenticationModeModificationException;
import com.inet.qlcbcc.domain.AuthenticationMode;

/**
 * AdminAuthenticationModeController.
 *
 * @author <a href="mailto:ntvy@inetcloud.vn">Vy Nguyen</a>
 * @version $Id: AdminAuthenticationModeController.java 2011-04-29 14:56:42Z nguyen_vt $
 *
 * @since 1.0
 */
@Controller
@RequestMapping("/admin/auth-mode")
public class AdminAuthenticationModeController extends AbstractController {

    @Autowired
    @Qualifier("authenticationModeFacade")
    private AuthenticationModeFacade authModeFacade;

    /**
   * Process the /admin/auth-mode/index.iws mapping.
   * 
   * @param request the given {@link HttpServletRequest} object.
   * @return the authentication mode view.
   */
    @RequestMapping(value = "/index.iws", method = { RequestMethod.GET })
    public String index(Model model) {
        ResponseMessage authModes = authModeFacade.findAll();
        if (ResponseMessage.Status.SUCCESS.equals(authModes.getStatus()) && authModes.getData().isDefined()) {
            model.addAttribute("items", authModes.getData().get());
        }
        return "admin/auth-mode/index";
    }

    @RequestMapping(value = "/request-id.iws", method = { RequestMethod.POST })
    @ResponseBody
    public ResponseMessage requestId() {
        return authModeFacade.requestId();
    }

    /**
   * Find all authentication mode.
   *
   * @return the authentication mode.
   */
    @RequestMapping(value = "/find-all.iws", method = { RequestMethod.GET })
    @ResponseBody
    public ResponseMessage findAll() {
        return authModeFacade.findAll();
    }

    @RequestMapping(value = "/test.iws/{id}", method = { RequestMethod.GET })
    @ResponseBody
    public ResponseMessage test(@PathVariable("id") String id) {
        return authModeFacade.test(id);
    }

    /**
   * Save authentication mode.
   *
   * @return the authentication mode.
   */
    @RequestMapping(value = "/save.iws", method = { RequestMethod.POST })
    @ResponseBody
    public ResponseMessage save(@RequestBody AuthenticationMode authMode) {
        try {
            return authModeFacade.save(authMode);
        } catch (AuthenticationModeModificationException ex) {
            if (logger.isDebugEnabled()) {
                logger.debug("An error occurs during saving the authentication mode, overcome this.", ex);
            } else if (logger.isWarnEnabled()) {
                logger.warn("An error occurs during saving the authentication mode with message [{}], overcome this.", ex.getMessage());
            }
            return createFailureResponse(ex.getMessage());
        } catch (UnauthorizedException ex) {
            return createFailureResponse("dkkd.auth_mode.unauthorized_access", "auth:create", TypeDescriptor.valueOf(String.class));
        } catch (Exception ex) {
            if (logger.isDebugEnabled()) {
                logger.debug("An error occurs during saving the authentication mode, overcome this.", ex);
            } else if (logger.isWarnEnabled()) {
                logger.warn("An error occurs during saving the authentication mode with message [{}], overcome this.", ex.getMessage());
            }
            return createFailureResponse("dkkd.auth_mode.save");
        }
    }

    /**
   * Update authentication mode.
   *
   * @return the authentication mode.
   */
    @RequestMapping(value = "/update.iws", method = { RequestMethod.PUT })
    @ResponseBody
    public ResponseMessage update(@RequestBody AuthenticationMode authMode) {
        try {
            return authModeFacade.update(authMode);
        } catch (AuthenticationModeModificationException ex) {
            if (logger.isDebugEnabled()) {
                logger.debug("An error occurs during updating the authentication mode, overcome this.", ex);
            } else if (logger.isWarnEnabled()) {
                logger.warn("An error occurs during updating the authentication mode with message [{}], overcome this.", ex.getMessage());
            }
            return createFailureResponse(ex.getMessage());
        } catch (UnauthorizedException ex) {
            return createFailureResponse("dkkd.auth_mode.unauthorized_access", "auth:update", TypeDescriptor.valueOf(String.class));
        } catch (Exception ex) {
            if (logger.isDebugEnabled()) {
                logger.debug("An error occurs during updating the authentication mode, overcome this.", ex);
            } else if (logger.isWarnEnabled()) {
                logger.warn("An error occurs during updating the authentication mode with message [{}], overcome this.", ex.getMessage());
            }
            return createFailureResponse("dkkd.auth_mode.update");
        }
    }

    /**
   * Delete authentication mode.
   *
   * @return the authentication mode.
   */
    @RequestMapping(value = "/delete.iws/{id}", method = { RequestMethod.DELETE })
    @ResponseBody
    public ResponseMessage delete(@PathVariable("id") String id) {
        try {
            return authModeFacade.delete(id);
        } catch (AuthenticationModeModificationException ex) {
            if (logger.isDebugEnabled()) {
                logger.debug("An error occurs during deleting the authentication mode with id [{}], overcome this.", new Object[] { id, ex });
            } else if (logger.isWarnEnabled()) {
                logger.warn("An error occurs during deleting the authentication mode with id [{}] with message [{}], overcome this.", new Object[] { id, ex.getMessage() });
            }
            return createFailureResponse(ex.getMessage());
        } catch (UnauthorizedException ex) {
            return createFailureResponse("dkkd.auth_mode.unauthorized_access", "auth:delete", TypeDescriptor.valueOf(String.class));
        } catch (Exception ex) {
            if (logger.isDebugEnabled()) {
                logger.debug("An error occurs during deleting the authentication mode with id [{}], overcome this.", new Object[] { id, ex });
            } else if (logger.isWarnEnabled()) {
                logger.warn("An error occurs during deleting the authentication mode with id [{}] with message [{}], overcome this.", new Object[] { id, ex.getMessage() });
            }
            return createFailureResponse("dkkd.auth_mode.delete");
        }
    }

    /**
   * Process the /admin-create-authentication-mode.iws mapping.
   * 
   * @param request the given {@link HttpServletRequest} object.
   * @return the create new authentication mode view.
   */
    @RequestMapping(value = "/create.iws", method = { RequestMethod.GET })
    public String create(Model model) {
        ResponseMessage response = authModeFacade.requestId();
        if (response.getStatus().equals(ResponseMessage.Status.SUCCESS)) {
            model.addAttribute("id", ((Id) response.getData().get()).getId());
        }
        return "admin/auth-mode/create";
    }

    /**
   * Process the /admin-edit-authentication-mode.iws mapping.
   * 
   * @param request the given {@link HttpServletRequest} object.
   * @return the create new authentication mode view.
   */
    @RequestMapping(value = "/edit.iws/{id}", method = { RequestMethod.GET })
    public String edit(@PathVariable("id") String id, Model model) {
        ResponseMessage response = authModeFacade.findById(id);
        model.addAttribute("status", response.getStatus().getCode());
        Option<String> message = response.getMessage();
        if (message.isDefined()) {
            model.addAttribute("message", message.get());
        }
        Option<Object> data = response.getData();
        if (data.isDefined()) {
            AuthenticationMode item = (AuthenticationMode) data.get();
            item.setPassword(decodeString(item.getPassword()));
            model.addAttribute("item", item);
        }
        return "admin/auth-mode/edit";
    }
}
