package com.googlecode.webmvc.web.servlet.generics.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import com.googlecode.webmvc.web.servlet.generics.util.GenericsUtil;

/** 
 * <p>
 *  Generic implementation of spring's
 *  {@link org.springframework.web.servlet.mvc.BaseCommandController}.
 * </p>
 *
 * <p>
 *  Features added to this controller not present in the one
 *  provided by the spring framework are:
 *  <ul>
 *      <li>defaultView configuration parameter</li>
 *      <li>The commandClass is automatically determined and therefore doesn't need to be set</li>
 *  </ul>
 * </p>
 * @param <T>
 */
public abstract class BaseCommandController<T> extends org.springframework.web.servlet.mvc.BaseCommandController {

    private String defaultView;

    /**
     * Creates the BaseCommandController.
     */
    public BaseCommandController() {
        super.setCommandClass(GenericsUtil.getTypeVariableClassByName(this.getClass(), BaseCommandController.class, "T", true));
    }

    /**
     * {@inheritDoc}
     */
    protected abstract ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    protected final void onBindAndValidate(HttpServletRequest request, Object command, BindException errors) throws Exception {
        onBindAndValidate((T) command, request, errors);
    }

    /**
     * Generic version of
     * {@link #onBindAndValidate(HttpServletRequest, Object, BindException)}.
     * @param command the command object
     * @param request the request
     * @param errors the errors
     * @throws Exception on error
     */
    protected void onBindAndValidate(T command, HttpServletRequest request, BindException errors) throws Exception {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    protected final void onBind(HttpServletRequest request, Object command, BindException errors) throws Exception {
        onBind((T) command, errors, request);
    }

    /**
     * Generic version of 
     * {@link #onBind(HttpServletRequest, Object, BindException)}.
     * @param command the command object
     * @param errors the errors
     * @param request the request
     * @throws Exception on error
     */
    protected void onBind(T command, BindException errors, HttpServletRequest request) throws Exception {
        onBind(request, command);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    protected final void onBind(HttpServletRequest request, Object command) throws Exception {
        onBind((T) command, request);
    }

    /**
     * Generic version of
     * {@link #onBind(HttpServletRequest, Object)}.
     * @param command the command object
     * @param request the request
     * @throws Exception on error
     */
    protected void onBind(T command, HttpServletRequest request) throws Exception {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    protected final ServletRequestDataBinder createBinder(HttpServletRequest request, Object command) throws Exception {
        return createBinder((T) command, request);
    }

    /**
     * Generic implementation of
     * {@see #createBinder(HttpServletRequest, Object)}.
     * @param command the command
     * @param request the request
     * @return the ServletRequestDataBinder
     * @throws Exception on error
     */
    protected ServletRequestDataBinder createBinder(T command, HttpServletRequest request) throws Exception {
        return super.createBinder(request, command);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    protected final boolean suppressValidation(HttpServletRequest request, Object command, BindException errors) {
        return suppressValidation((T) command, request, errors);
    }

    /**
     * Generic implementation of
     * {@see #suppressValidation(HttpServletRequest, Object, BindException)}.
     * @param command the command
     * @param request the request
     * @param errors the errors
     * @return true or false ?
     */
    protected boolean suppressValidation(T command, HttpServletRequest request, BindException errors) {
        return super.suppressValidation(request, command, errors);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    protected final boolean suppressValidation(HttpServletRequest request, Object command) {
        return suppressValidation((T) command, request);
    }

    /**
     * Generic implementation of
     * {@see #suppressValidation(Object, HttpServletRequest)}.
     * @param command the command
     * @param request the request
     * @return true or false ?
     */
    protected boolean suppressValidation(T command, HttpServletRequest request) {
        return super.suppressValidation(request, command);
    }

    /**
     * @return the defaultView
     */
    public String getDefaultView() {
        return defaultView;
    }

    /**
     * @param defaultView the defaultView to set
     */
    public void setDefaultView(String defaultView) {
        this.defaultView = defaultView;
    }
}
