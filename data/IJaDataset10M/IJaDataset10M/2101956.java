package org.herasaf.xacml.core.targetMatcher.impl;

import java.util.List;
import org.herasaf.xacml.SyntaxException;
import org.herasaf.xacml.core.ProcessingException;
import org.herasaf.xacml.core.context.RequestInformation;
import org.herasaf.xacml.core.context.impl.RequestType;
import org.herasaf.xacml.core.function.Function;
import org.herasaf.xacml.core.policy.MissingAttributeException;
import org.herasaf.xacml.core.policy.impl.ActionType;
import org.herasaf.xacml.core.policy.impl.ActionsType;
import org.herasaf.xacml.core.policy.impl.AttributeDesignatorType;
import org.herasaf.xacml.core.policy.impl.EnvironmentType;
import org.herasaf.xacml.core.policy.impl.EnvironmentsType;
import org.herasaf.xacml.core.policy.impl.Match;
import org.herasaf.xacml.core.policy.impl.ResourceType;
import org.herasaf.xacml.core.policy.impl.ResourcesType;
import org.herasaf.xacml.core.policy.impl.SubjectType;
import org.herasaf.xacml.core.policy.impl.SubjectsType;
import org.herasaf.xacml.core.policy.impl.TargetType;
import org.herasaf.xacml.core.targetMatcher.TargetMatcher;

/**
 * Implementation of the {@link TargetMatcher} interface.
 *
 * @author Florian Huonder
 * @version 1.0
 */
public class TargetMatcherImpl implements TargetMatcher {

    /**
	 *
	 */
    private static final long serialVersionUID = 9099144198373918560L;

    public boolean match(RequestType request, TargetType target, RequestInformation reqInfo) throws SyntaxException, ProcessingException, MissingAttributeException {
        if (target != null) {
            boolean subjectsMatches = subjectsMatch(target.getSubjects(), request, reqInfo);
            boolean resourcesMatches = resourcesMatch(target.getResources(), request, reqInfo);
            boolean actionsMatches = actionMatch(target.getActions(), request, reqInfo);
            boolean environmentsMatches = environmentMatch(target.getEnvironments(), request, reqInfo);
            return subjectsMatches && resourcesMatches && actionsMatches && environmentsMatches;
        }
        return true;
    }

    private boolean subjectsMatch(SubjectsType subjects, RequestType request, RequestInformation reqInfo) throws ProcessingException, SyntaxException, MissingAttributeException {
        if (subjects == null) {
            return true;
        }
        for (int i = 0; i < subjects.getSubjects().size(); i++) {
            SubjectType targetSubject = subjects.getSubjects().get(i);
            boolean matches = match(targetSubject.getSubjectMatches(), request, reqInfo);
            if (matches) {
                return true;
            }
        }
        return false;
    }

    private boolean resourcesMatch(ResourcesType resources, RequestType request, RequestInformation reqInfo) throws ProcessingException, SyntaxException, MissingAttributeException {
        if (resources == null) {
            return true;
        }
        for (int i = 0; i < resources.getResources().size(); i++) {
            ResourceType targetResource = resources.getResources().get(i);
            boolean matches = match(targetResource.getResourceMatches(), request, reqInfo);
            if (matches) {
                return true;
            }
        }
        return false;
    }

    private boolean actionMatch(ActionsType actions, RequestType request, RequestInformation reqInfo) throws ProcessingException, SyntaxException, MissingAttributeException {
        if (actions == null) {
            return true;
        }
        for (int i = 0; i < actions.getActions().size(); i++) {
            ActionType targetAction = actions.getActions().get(i);
            boolean matches = match(targetAction.getActionMatches(), request, reqInfo);
            if (matches) {
                return true;
            }
        }
        return false;
    }

    private boolean environmentMatch(EnvironmentsType environments, RequestType request, RequestInformation reqInfo) throws ProcessingException, SyntaxException, MissingAttributeException {
        if (environments == null) {
            return true;
        }
        for (int i = 0; i < environments.getEnvironments().size(); i++) {
            EnvironmentType targetEnvironment = environments.getEnvironments().get(i);
            boolean matches = match(targetEnvironment.getEnvironmentMatches(), request, reqInfo);
            if (matches) {
                return true;
            }
        }
        return false;
    }

    private boolean match(List<? extends Match> matches, RequestType request, RequestInformation reqInfo) throws ProcessingException, SyntaxException, MissingAttributeException {
        for (int i = 0; i < matches.size(); i++) {
            Match match = matches.get(i);
            Function matchFunction = match.getMatchFunction();
            AttributeDesignatorType designator = match.getAttributeDesignator();
            List<?> requestAttributeValues = (List<?>) designator.handle(request, reqInfo);
            if (requestAttributeValues.size() == 0) {
                return false;
            }
            boolean matchMatches = false;
            for (int k = 0; k < requestAttributeValues.size(); k++) {
                Object requestAttributeValue = requestAttributeValues.get(k);
                matchMatches = (Boolean) matchFunction.handle(designator.getDataType().convertTo((String) match.getAttributeValue().getContent().get(0)), requestAttributeValue);
                if (matchMatches) {
                    break;
                }
            }
            if (!matchMatches) {
                return false;
            }
        }
        return true;
    }
}
