package services;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import model.Blomoaccess;
import model.Blomoflow;
import model.Blomorhythm;
import model.Blomorights;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.execution.RequestContextHolder;
import blomo.progressing.ConfigurationProgressing;
import blomo.util.HibernateUtil;

/**
 * @author Malte Schulze
 *
 */
@Service
public class GeneralFlowService {

    private ScriptService scriptService;

    private Logger logger = LoggerFactory.getLogger(GeneralFlowService.class);

    /**
	 * @author Malte Schulze
	 *
	 */
    public enum FlowState {

        /**
		 * The user has no right to access the flow(while others may). 
		 */
        accessNotAllowed, /**
		 * The flow is deactivated or not existent.
		 */
        flowIsInactive, /**
		 * The flow is currently not available for the user,
		 * but will be or has been available at another time.
		 */
        flowIsCurrentlyInactive, /**
		 * flow can currently be accessed by the user.
		 */
        flowIsAccessible
    }

    /**
	 * Determines the state of the submitted flow based
	 * on the user role and parameters.
	 * @param flowName
	 * @param blomoRoleName
	 * @param parameters
	 * @return the state of the flow
	 */
    @SuppressWarnings("unchecked")
    public FlowState isFlowActive(String flowName, String blomoRoleName, Map<Object, Object> parameters) {
        Session session = HibernateUtil.getSessionFactory(blomoRoleName).getCurrentSession();
        boolean commit = false;
        Transaction t = session.getTransaction();
        if (!t.isActive()) {
            commit = true;
            t.begin();
        }
        FlowState flowState = FlowState.flowIsInactive;
        try {
            Criteria criteria = session.createCriteria("model.Blomoflow");
            criteria.setMaxResults(1);
            criteria.add(Restrictions.eq("name", flowName));
            List<Blomoflow> result = criteria.list();
            if (result != null && result.size() == 0) flowState = FlowState.flowIsAccessible;
            if (result != null && result.size() == 1) {
                Blomoflow flow = result.get(0);
                if (!flow.getActive()) {
                    flowState = FlowState.flowIsInactive;
                } else {
                    Set<Blomoaccess> accesses = flow.getBLoMoAccessesReadOnly();
                    if (accesses.size() == 0) flowState = FlowState.flowIsAccessible;
                    for (Blomoaccess access : accesses) {
                        boolean hasRight = true;
                        for (Blomorights right : access.getBLoMoRightsReadOnly()) {
                            hasRight = false;
                            String script = right.getScript();
                            Boolean isWhitelist = right.getWhitelist();
                            boolean exprResult = scriptService.executeBooleanExpr(script, parameters);
                            if (isWhitelist == Boolean.TRUE && exprResult) {
                                hasRight = true;
                                break;
                            } else if (isWhitelist == Boolean.FALSE && !exprResult) {
                                hasRight = true;
                                break;
                            }
                        }
                        if (!hasRight) {
                            flowState = FlowState.accessNotAllowed;
                        } else {
                            boolean isActive = true;
                            for (Blomorhythm rhythm : access.getBLoMoRhythmsReadOnly()) {
                                isActive = false;
                                Date from = rhythm.getFrom();
                                Date current = new Date();
                                if (from != null) {
                                    if (from.after(current)) continue;
                                }
                                Date to = rhythm.getFrom();
                                if (to != null) {
                                    if (to.before(current)) continue;
                                }
                                String script = rhythm.getScript();
                                Boolean isWhitelist = rhythm.getWhitelist();
                                parameters.put("from", from);
                                parameters.put("to", to);
                                boolean exprResult = scriptService.executeBooleanExpr(script, parameters);
                                if (isWhitelist == Boolean.TRUE && !exprResult) {
                                    isActive = true;
                                    break;
                                } else if (isWhitelist == Boolean.FALSE && !exprResult) {
                                    isActive = true;
                                    break;
                                }
                            }
                            if (!isActive) {
                                flowState = FlowState.flowIsCurrentlyInactive;
                            } else {
                                flowState = FlowState.flowIsAccessible;
                            }
                        }
                        if (flowState.compareTo(FlowState.flowIsAccessible) == 0) break;
                    }
                }
            }
            if (commit) t.commit();
        } catch (HibernateException e) {
            e.printStackTrace();
            if (commit) t.rollback();
            flowState = FlowState.flowIsInactive;
        }
        return flowState;
    }

    /**
	 * @param uploadConfiguration something like: test1;c:/test/;pdf,jpeg,gif,png#test2;c:/test/;pdf
	 * 
	 */
    public void prepareForUpload(String uploadConfiguration) {
        RequestContext context = RequestContextHolder.getRequestContext();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getNativeRequest();
        String flowId = UUID.randomUUID().toString();
        context.getFlowScope().put("flowId", flowId);
        ConfigurationProgressing conf = new ConfigurationProgressing();
        String pageIds[] = uploadConfiguration.split("#");
        for (String pageId : pageIds) {
            String parts[] = pageId.split(";");
            String key = parts[0];
            Map<String, Serializable> confParas = new HashMap<String, Serializable>();
            for (int i = 1; i < parts.length; i++) {
                String property[] = parts[i].split("=");
                if (property.length < 2) logger.error("Property(" + parts[i] + ") is missing format name=value! Configuration was " + uploadConfiguration + "."); else {
                    confParas.put(property[0], property[1]);
                }
            }
            conf.setConfForPageId(key, confParas);
        }
        request.getSession().setAttribute(flowId, conf);
    }

    /**
	 * 
	 */
    public void cleanSessionFromUpload() {
        RequestContext context = RequestContextHolder.getRequestContext();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getNativeRequest();
        String flowId = context.getFlowScope().getRequiredString("flowId");
        HttpSession session = request.getSession();
        ConfigurationProgressing uplConf = (ConfigurationProgressing) session.getAttribute(flowId);
        session.removeAttribute(flowId);
        Set<String> pageIds = uplConf.getPageIds();
        for (String pageId : pageIds) {
            session.removeAttribute(flowId + pageId);
        }
    }

    /**
	 * @param scriptService the scriptService to set
	 */
    public void setScriptService(ScriptService scriptService) {
        this.scriptService = scriptService;
    }
}
