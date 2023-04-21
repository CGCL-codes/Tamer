package org.nocrala.tools.asterionjsf.listener;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import org.nocrala.tools.asterionjsf.exception.ApplicationException;
import org.nocrala.tools.asterionjsf.exception.AsterionConfigurationException;
import org.nocrala.tools.asterionjsf.exception.CouldNotExecuteMethodException;
import org.nocrala.tools.asterionjsf.exception.CouldNotInstantiateManagedBeanException;
import org.nocrala.tools.asterionjsf.model.AsterionJsfSessionState;
import org.nocrala.tools.asterionjsf.model.AsterionViewsAndBeanNames;
import org.nocrala.tools.asterionjsf.model.ManagedBeanDefinition;
import org.nocrala.tools.asterionjsf.model.ManagedBeanScope;
import org.nocrala.tools.asterionjsf.util.Log;

public class AsterionPhaseListener implements PhaseListener {

    private static final long serialVersionUID = -4165114108328401335L;

    private static final String ASTERION_JSF_SESSION_STATE = "ASTERION_JSF_SESSION_STATE";

    private static final int MAX_OUTCOMES_PER_JSF_REQUEST = 100;

    private static final String ASTERION_NAVIGATION_EXCEPTION_OUTCOME = "ASTERION_NAVIGATION_EXCEPTION_OUTCOME";

    public PhaseId getPhaseId() {
        return PhaseId.ANY_PHASE;
    }

    public void beforePhase(final PhaseEvent event) {
        FacesContext ctx = event.getFacesContext();
        try {
            logPhaseInformation(event, ctx);
            int totalOutcomes = 0;
            if (event.getPhaseId().equals(PhaseId.RENDER_RESPONSE)) {
                AsterionJsfSessionState state = retrieveState(ctx);
                String viewId = getViewId(ctx);
                String firstViewId = viewId;
                String outcome = null;
                do {
                    if (Log.isInfoEnabled()) {
                        Log.info("----> LOOP starting: viewId='" + viewId + "' previous '" + state.getCurrentViewId());
                    }
                    if (viewId != null) {
                        if (!viewId.equals(state.getCurrentViewId())) {
                            if (state.getCurrentViewId() != null) {
                                outcome = executeNavigateOut(state.getCurrentViewId(), ctx);
                                if (outcome != null) {
                                    totalOutcomes = addOutcome(ctx, totalOutcomes, firstViewId);
                                    ctx.getApplication().getNavigationHandler().handleNavigation(ctx, null, outcome);
                                    viewId = getViewId(ctx);
                                    if (Log.isInfoEnabled()) {
                                        Log.info(">>> outcome=" + outcome + " new viewId=" + viewId);
                                    }
                                    state.setCurrentViewId(viewId);
                                }
                            }
                            state.setCurrentViewId(viewId);
                            do {
                                if (viewId == null) {
                                    outcome = null;
                                } else {
                                    outcome = executeNavigateIn(viewId, ctx);
                                    if (outcome != null) {
                                        totalOutcomes = addOutcome(ctx, totalOutcomes, firstViewId);
                                        ctx.getApplication().getNavigationHandler().handleNavigation(ctx, null, outcome);
                                        viewId = getViewId(ctx);
                                        if (Log.isInfoEnabled()) {
                                            Log.info(">>> outcome=" + outcome + " new viewId=" + viewId);
                                        }
                                        state.setCurrentViewId(viewId);
                                    }
                                }
                            } while (outcome != null);
                        }
                        if (viewId == null) {
                            outcome = null;
                        } else {
                            outcome = executePreRender(viewId, ctx);
                            if (outcome != null) {
                                totalOutcomes = addOutcome(ctx, totalOutcomes, firstViewId);
                                ctx.getApplication().getNavigationHandler().handleNavigation(ctx, null, outcome);
                                viewId = getViewId(ctx);
                                if (Log.isInfoEnabled()) {
                                    Log.info(">>> outcome=" + outcome + " new viewId=" + viewId);
                                }
                            }
                        }
                    }
                } while (outcome != null);
            }
        } catch (ApplicationException e) {
            if (Log.isErrorEnabled()) {
                Log.error(e.getCause());
            }
        } catch (AsterionConfigurationException e) {
            if (Log.isErrorEnabled()) {
                Log.error(e);
            }
            ctx.getApplication().getNavigationHandler().handleNavigation(ctx, null, ASTERION_NAVIGATION_EXCEPTION_OUTCOME);
        } catch (CouldNotInstantiateManagedBeanException e) {
            if (Log.isErrorEnabled()) {
                Log.error(e);
            }
            ctx.getApplication().getNavigationHandler().handleNavigation(ctx, null, ASTERION_NAVIGATION_EXCEPTION_OUTCOME);
        } catch (CouldNotExecuteMethodException e) {
            if (Log.isErrorEnabled()) {
                Log.error(e);
            }
            ctx.getApplication().getNavigationHandler().handleNavigation(ctx, null, ASTERION_NAVIGATION_EXCEPTION_OUTCOME);
        } catch (RuntimeException e) {
            if (Log.isErrorEnabled()) {
                Log.error(e.getCause());
            }
        }
    }

    private int addOutcome(final FacesContext ctx, final int totalOutcomes, final String firstViewId) {
        int tot = totalOutcomes + 1;
        if (tot > MAX_OUTCOMES_PER_JSF_REQUEST) {
            if (Log.isErrorEnabled()) {
                Log.error("\n=======\nERROR: Too many outcomes (redirections) when " + "processing Asterion navigation methods initiated on view '" + firstViewId + "'. " + "The total outcomes received exceeded the limit of " + MAX_OUTCOMES_PER_JSF_REQUEST + " " + "per a single JSF request. " + "Maybe this is due to an endless loop " + "in the Asterion navigation methods?\n=======");
            }
            throw new RuntimeException("Navigation error detected.");
        }
        return tot;
    }

    public void afterPhase(final PhaseEvent event) {
        FacesContext ctx = event.getFacesContext();
        logPhaseInformation(event, ctx);
    }

    private AsterionJsfSessionState retrieveState(final FacesContext ctx) {
        AsterionJsfSessionState state;
        try {
            state = (AsterionJsfSessionState) ctx.getExternalContext().getSessionMap().get(ASTERION_JSF_SESSION_STATE);
            if (state == null) {
                state = new AsterionJsfSessionState();
                ctx.getExternalContext().getSessionMap().put(ASTERION_JSF_SESSION_STATE, state);
            }
            return state;
        } catch (ClassCastException e) {
            state = new AsterionJsfSessionState();
            state.setCurrentViewId(getViewId(ctx));
            return state;
        }
    }

    private String getViewId(final FacesContext ctx) {
        return ctx.getViewRoot() == null ? null : ctx.getViewRoot().getViewId();
    }

    private String executeNavigateIn(final String viewId, final FacesContext ctx) throws CouldNotInstantiateManagedBeanException, CouldNotExecuteMethodException, ApplicationException, AsterionConfigurationException {
        if (Log.isInfoEnabled()) {
            Log.info("-> navigateIn()");
        }
        return executeBeanMethod(viewId, ctx, "navigateIn");
    }

    private String executePreRender(final String viewId, final FacesContext ctx) throws CouldNotInstantiateManagedBeanException, CouldNotExecuteMethodException, ApplicationException, AsterionConfigurationException {
        if (Log.isErrorEnabled()) {
            Log.info("-> preRender()");
        }
        return executeBeanMethod(viewId, ctx, "preRender");
    }

    private String executeNavigateOut(final String viewId, final FacesContext ctx) throws CouldNotInstantiateManagedBeanException, CouldNotExecuteMethodException, ApplicationException, AsterionConfigurationException {
        if (Log.isErrorEnabled()) {
            Log.info("-> navigateOut()");
        }
        return executeBeanMethod(viewId, ctx, "navigateOut");
    }

    @SuppressWarnings("unchecked")
    private String executeBeanMethod(final String viewId, final FacesContext ctx, String methodName) throws CouldNotInstantiateManagedBeanException, CouldNotExecuteMethodException, ApplicationException, AsterionConfigurationException {
        ManagedBeanDefinition beanDef = AsterionViewsAndBeanNames.getBeanDefinition(viewId);
        if (beanDef == null) {
            return null;
        }
        Object bean = retrieveBean(beanDef, ctx);
        try {
            Method m = beanDef.getBeanClass().getMethod(methodName, (Class[]) null);
            if (!m.getReturnType().equals(String.class)) {
                throw new CouldNotExecuteMethodException("Invalid method '" + methodName + "' on bean '" + beanDef.getName() + "' of class '" + beanDef.getBeanClass().getName() + "'. This method must return a java.lang.String.");
            }
            String result = (String) m.invoke(bean, (Object[]) null);
            return result;
        } catch (SecurityException e) {
            throw new CouldNotExecuteMethodException("Could not execute method '" + methodName + "' on bean '" + beanDef.getName() + "' of class '" + beanDef.getBeanClass().getName() + "'.", e);
        } catch (NoSuchMethodException e) {
            return null;
        } catch (IllegalArgumentException e) {
            throw new CouldNotExecuteMethodException("Could not execute method '" + methodName + "' on bean '" + beanDef.getName() + "' of class '" + beanDef.getBeanClass().getName() + "'.", e);
        } catch (IllegalAccessException e) {
            throw new CouldNotExecuteMethodException("Could not execute method '" + methodName + "' on bean '" + beanDef.getName() + "' of class '" + beanDef.getBeanClass().getName() + "'.", e);
        } catch (InvocationTargetException e) {
            throw new ApplicationException(e.getCause());
        }
    }

    private Object retrieveBean(final ManagedBeanDefinition beanDef, final FacesContext ctx) throws CouldNotInstantiateManagedBeanException {
        Object bean = null;
        if (beanDef.getScope() == ManagedBeanScope.ApplicationScope) {
            bean = ctx.getExternalContext().getApplicationMap().get(beanDef.getName());
        } else if (beanDef.getScope() == ManagedBeanScope.SessionScope) {
            bean = ctx.getExternalContext().getSessionMap().get(beanDef.getName());
        } else if (beanDef.getScope() == ManagedBeanScope.RequestScope) {
            bean = ctx.getExternalContext().getRequestMap().get(beanDef.getName());
        }
        if (bean == null) {
            bean = instantiateBean(beanDef, ctx);
        }
        return bean;
    }

    private Object instantiateBean(final ManagedBeanDefinition beanDef, final FacesContext ctx) throws CouldNotInstantiateManagedBeanException {
        Object bean;
        try {
            bean = beanDef.getBeanClass().newInstance();
        } catch (InstantiationException e) {
            throw new CouldNotInstantiateManagedBeanException("Could not instantiate bean '" + beanDef.getName() + "' of class '" + beanDef.getBeanClass().getName() + "' in scope '" + beanDef.getScope() + "'.", e);
        } catch (IllegalAccessException e) {
            throw new CouldNotInstantiateManagedBeanException("Could not instantiate bean '" + beanDef.getName() + "' of class '" + beanDef.getBeanClass().getName() + "' in scope '" + beanDef.getScope() + "'.", e);
        }
        if (beanDef.getScope() == ManagedBeanScope.ApplicationScope) {
            ctx.getExternalContext().getApplicationMap().put(beanDef.getName(), bean);
        } else if (beanDef.getScope() == ManagedBeanScope.SessionScope) {
            ctx.getExternalContext().getSessionMap().put(beanDef.getName(), bean);
        } else if (beanDef.getScope() == ManagedBeanScope.RequestScope) {
            ctx.getExternalContext().getRequestMap().put(beanDef.getName(), bean);
        }
        return bean;
    }

    private void logPhaseInformation(final PhaseEvent event, final FacesContext ctx) {
        if (Log.isDebugEnabled()) {
            Log.debug("Before Phase: " + event.getPhaseId() + " renderResponse=" + ctx.getRenderResponse() + " responseComplete=" + ctx.getResponseComplete() + " viewId=" + getViewId(ctx));
        }
    }
}
