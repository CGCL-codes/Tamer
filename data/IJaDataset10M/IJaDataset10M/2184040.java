package org.gridtrust.enforce.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.herasaf.xacml.DataIntegrityException;
import org.herasaf.xacml.EvaluatableNotFoundException;
import org.herasaf.xacml.SyntaxException;
import org.herasaf.xacml.core.attributeFinder.AttributeFinder;
import org.herasaf.xacml.core.combiningAlgorithm.policy.PolicyUnorderedCombiningAlgorithm;
import org.herasaf.xacml.core.context.RequestCtx;
import org.herasaf.xacml.core.context.RequestInformation;
import org.herasaf.xacml.core.context.RequestInformationFactory;
import org.herasaf.xacml.core.context.ResponseCtx;
import org.herasaf.xacml.core.context.ResponseCtxFactory;
import org.herasaf.xacml.core.context.StatusCode;
import org.herasaf.xacml.core.context.impl.DecisionType;
import org.herasaf.xacml.core.evaluatablepreprocess.EvaluatablePreprocess;
import org.herasaf.xacml.core.policy.Evaluatable;
import org.herasaf.xacml.core.policy.EvaluatableID;
import org.herasaf.xacml.core.referenceloader.ReferenceLoader;
import org.herasaf.xacml.pdp.PDP;
import org.herasaf.xacml.pdp.locator.Locator;
import org.herasaf.xacml.pdp.locator.impl.index.impl.PolicyContainer;
import org.herasaf.xacml.pdp.persistence.DataAccessException;

public class EnforcerPDP implements PDP {

    private AttributeFinder attributeFinder;

    private volatile Locator locator;

    private ReferenceLoader referenceLoader;

    private PolicyUnorderedCombiningAlgorithm policyCombiningAlgorithm;

    private List<Evaluatable> evaluatables;

    private EvaluatablePreprocess evaluatablePreprocess;

    private RequestInformationFactory requestInformationFactory;

    public void setAttributeFinder(AttributeFinder attributeFinder) {
        this.attributeFinder = attributeFinder;
    }

    public void setLocator(Locator locator) {
        this.locator = locator;
    }

    public void setReferenceLoader(ReferenceLoader referenceLoader) {
        this.referenceLoader = referenceLoader;
    }

    public void setEvaluatablePreprocess(EvaluatablePreprocess evaluatablePreprocess) {
        this.evaluatablePreprocess = evaluatablePreprocess;
    }

    public void setRequestInformationFactory(RequestInformationFactory requestInformationFactory) {
        this.requestInformationFactory = requestInformationFactory;
    }

    public void setPolicyCombiningAlgorithm(PolicyUnorderedCombiningAlgorithm policyCombiningAlgorithm) {
        this.policyCombiningAlgorithm = policyCombiningAlgorithm;
    }

    /**
	 * {@inheritDoc} <br>
	 * <br>
	 * The {@link #evaluate(RequestCtx)} of the {@link PDPImpl} has the
	 * following flow:
	 * <ol>
	 * <li> The {@link RequestType} is passed to the {@link Locator} which
	 * returns a {@link PolicyContainer} containing the {@link Evaluatable}s
	 * and the references on remote-{@link Evaluatable}s.
	 * <li> The {@link List} containing the references to the remote-{@link Evaluatable}s
	 * is passed to the {@link ReferenceLoader} which returns a {@link Map} of
	 * {@link Evaluatable}s with their ID as key.
	 * <li> A new {@link RequestInformation} object is created with the obtained
	 * {@link Map} of remote-{@link Evaluatable}s in it.
	 * <li> The {@link RequestInformation} together with the {@link RequestType}
	 * and the {@link List} of local-{@link Evaluatable}s is passed to the
	 * root-{@link PolicyCombiningAlgorithm}.
	 * <li> A new {@link ResponseCtx} is created containing the decision.
	 * afterwards it is returned.</li>
	 * </ol>
	 */
    public ResponseCtx evaluate(RequestCtx request) {
        PolicyContainer policyContainer;
        try {
            policyContainer = locator.locate(request.getRequest());
        } catch (SyntaxException e) {
            return ResponseCtxFactory.create(request.getRequest(), DecisionType.INDETERMINATE, StatusCode.SYNTAX_ERROR);
        }
        RequestInformation reqInfo = null;
        reqInfo = requestInformationFactory.createRequestInformation(policyContainer.getPolicyRefs(), attributeFinder);
        DecisionType decision = policyCombiningAlgorithm.evaluateEvaluatableList(request.getRequest(), new ArrayList<Evaluatable>(policyContainer.getPolicies()), reqInfo);
        return ResponseCtxFactory.create(request.getRequest(), decision, reqInfo);
    }

    public Evaluatable getEvaluatable(EvaluatableID id) throws EvaluatableNotFoundException {
        for (Evaluatable eval : evaluatables) {
            if (eval.getId().equals(id)) return eval;
        }
        throw new EvaluatableNotFoundException();
    }

    public synchronized void deploy(Collection<Evaluatable> evals) throws DataAccessException, DataIntegrityException, SyntaxException {
        Iterator<Evaluatable> evalsIterator = evals.iterator();
        evaluatables = new ArrayList<Evaluatable>();
        while (evalsIterator.hasNext()) {
            evaluatables.add(evalsIterator.next());
        }
        evaluatablePreprocess.process(evaluatables);
        locator.initialize(evaluatables);
        referenceLoader.initialize(evaluatables);
        evaluatablePreprocess.process(evals);
        evaluatables.addAll(evals);
        locator.initialize(evaluatables);
        referenceLoader.initialize(evaluatables);
    }

    public synchronized void deploy(Evaluatable evaluatable) throws DataAccessException, DataIntegrityException, SyntaxException {
        evaluatablePreprocess.process(evaluatable);
        evaluatables.add(evaluatable);
        locator.initialize(evaluatables);
        referenceLoader.initialize(evaluatables);
    }

    public synchronized void undeploy(EvaluatableID id) throws DataAccessException, SyntaxException {
        for (Evaluatable eval : evaluatables) {
            if (eval.getId().equals(id)) {
                evaluatables.remove(eval);
                break;
            }
        }
        locator.initialize(evaluatables);
        referenceLoader.initialize(evaluatables);
    }

    public synchronized void undeploy(Collection<EvaluatableID> ids) throws DataAccessException, SyntaxException {
        for (EvaluatableID id : ids) {
            for (Evaluatable eval : evaluatables) {
                if (eval.getId().equals(id)) {
                    evaluatables.remove(eval);
                    break;
                }
            }
        }
        locator.initialize(evaluatables);
        referenceLoader.initialize(evaluatables);
    }

    public synchronized void undeployAll() throws DataAccessException, SyntaxException {
        evaluatables.clear();
        locator.initialize(evaluatables);
        referenceLoader.initialize(evaluatables);
    }
}
