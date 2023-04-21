package org.mitre.dm.qud.domain;

import java.util.*;
import java.util.logging.*;
import org.mitre.midiki.state.*;
import org.mitre.midiki.logic.*;
import org.mitre.dm.*;

/**
 * Provides a CellHandlers object for access to a DomainImplementation.
 * The specifics of the domain must be filled in by concrete subclasses.
 *
 * @author <a href="mailto:cburke@mitre.org">Carl Burke</a>
 * @version 1.0
 * @since 1.0
 * @see DomainImplementation
 */
public abstract class DomainCell extends DomainImplementation {

    private static Logger logger = Logger.getLogger("org.mitre.dm.qud.domain.DomainCell");

    protected QueryHandler reduceTransducer;

    protected QueryHandler abstractTransducer;

    protected QueryHandler relevantAnswerTransducer;

    protected QueryHandler relevantCategoryTransducer;

    protected QueryHandler relevantToTaskTransducer;

    protected QueryHandler relevantToTasksTransducer;

    protected QueryHandler defaultTransducer;

    protected QueryHandler dominatesTransducer;

    protected QueryHandler planTransducer;

    protected QueryHandler planTaskTransducer;

    protected QueryHandler sysactionTransducer;

    protected QueryHandler allAnswersTransducer;

    protected MethodHandler instantiatePlanTransducer;

    protected QueryHandler nextStepTransducer;

    protected MethodHandler advancePlanTransducer;

    protected MethodHandler resetPlanTransducer;

    protected MethodHandler setAnswerTransducer;

    protected QueryHandler getAnswerTransducer;

    protected MethodHandler retractAnswerTransducer;

    protected QueryHandler wantsAnswerTransducer;

    public CellHandlers initializeHandlers() {
        CellHandlers domainCell = new CellHandlers(ContractDatabase.find("domain"));
        reduceTransducer = new QueryHandler() {

            public boolean query(Collection arguments, Bindings bindings) {
                Object intfid = null;
                Object q = null;
                Object r = null;
                Object p = null;
                Iterator argIt = arguments.iterator();
                if (argIt.hasNext()) intfid = argIt.next();
                if (argIt.hasNext()) q = argIt.next();
                if (argIt.hasNext()) r = argIt.next();
                if (argIt.hasNext()) p = argIt.next();
                return doQuery_domain_reduce(intfid, q, r, p, bindings);
            }
        };
        domainCell.addQueryHandler("reduce", reduceTransducer);
        abstractTransducer = new QueryHandler() {

            public boolean query(Collection arguments, Bindings bindings) {
                Object intfid = null;
                Object a = null;
                Object oldP = null;
                Object q = null;
                Iterator argIt = arguments.iterator();
                if (argIt.hasNext()) intfid = argIt.next();
                if (argIt.hasNext()) a = argIt.next();
                if (argIt.hasNext()) oldP = argIt.next();
                if (argIt.hasNext()) q = argIt.next();
                return doQuery_domain_abstract(intfid, a, oldP, q, bindings);
            }
        };
        domainCell.addQueryHandler("abstract", abstractTransducer);
        relevantAnswerTransducer = new QueryHandler() {

            public boolean query(Collection arguments, Bindings bindings) {
                Object intfid = null;
                Object q = null;
                Object r = null;
                Object p = null;
                Iterator argIt = arguments.iterator();
                if (argIt.hasNext()) intfid = argIt.next();
                if (argIt.hasNext()) q = argIt.next();
                if (argIt.hasNext()) r = argIt.next();
                boolean retval = doQuery_domain_relevant_answer(intfid, q, r, bindings);
                logger.logp(Level.FINER, "org.mitre.dm.qud.domain.DomainCell", "relevantAnswerTransducer.query", "domain_relevant_answer() arguments", arguments);
                logger.logp(Level.FINER, "org.mitre.dm.qud.domain.DomainCell", "relevantAnswerTransducer.query", (retval ? "succeeded" : "failed"));
                return retval;
            }
        };
        domainCell.addQueryHandler("relevant_answer", relevantAnswerTransducer);
        relevantCategoryTransducer = new QueryHandler() {

            public boolean query(Collection arguments, Bindings bindings) {
                Object intfid = null;
                Object q = null;
                Object r = null;
                Object p = null;
                Iterator argIt = arguments.iterator();
                if (argIt.hasNext()) intfid = argIt.next();
                if (argIt.hasNext()) q = argIt.next();
                if (argIt.hasNext()) r = argIt.next();
                logger.logp(Level.FINER, "org.mitre.dm.qud.domain.DomainCell", "relevantCategoryTransducer.query", "domain_relevant_category() arguments", arguments);
                boolean retval = doQuery_domain_relevant_category(intfid, q, r, bindings);
                logger.logp(Level.FINER, "org.mitre.dm.qud.domain.DomainCell", "relevantCategoryTransducer.query", (retval ? "succeeded" : "failed"));
                return retval;
            }
        };
        domainCell.addQueryHandler("relevant_category", relevantCategoryTransducer);
        relevantToTaskTransducer = new QueryHandler() {

            public boolean query(Collection arguments, Bindings bindings) {
                Object intfid = null;
                Object move = null;
                Object task = null;
                Object plan = null;
                Iterator argIt = arguments.iterator();
                if (argIt.hasNext()) intfid = argIt.next();
                if (argIt.hasNext()) move = argIt.next();
                if (argIt.hasNext()) task = argIt.next();
                if (argIt.hasNext()) plan = argIt.next();
                return doQuery_domain_relevant_to_task(intfid, move, task, plan, bindings);
            }
        };
        domainCell.addQueryHandler("relevant_to_task", relevantToTaskTransducer);
        relevantToTasksTransducer = new QueryHandler() {

            public boolean query(Collection arguments, Bindings bindings) {
                Object intfid = null;
                Object move = null;
                Object task = null;
                Iterator argIt = arguments.iterator();
                if (argIt.hasNext()) intfid = argIt.next();
                if (argIt.hasNext()) move = argIt.next();
                if (argIt.hasNext()) task = argIt.next();
                return doQuery_domain_relevant_to_tasks(intfid, move, task, bindings);
            }
        };
        domainCell.addQueryHandler("relevant_to_tasks", relevantToTasksTransducer);
        defaultTransducer = new QueryHandler() {

            public boolean query(Collection arguments, Bindings bindings) {
                Object intfid = null;
                Object task = null;
                Iterator argIt = arguments.iterator();
                if (argIt.hasNext()) intfid = argIt.next();
                if (argIt.hasNext()) task = argIt.next();
                return doQuery_domain_default(intfid, task, bindings);
            }
        };
        domainCell.addQueryHandler("default", defaultTransducer);
        dominatesTransducer = new QueryHandler() {

            public boolean query(Collection arguments, Bindings bindings) {
                Object intfid = null;
                Object oldTask = null;
                Object task = null;
                Iterator argIt = arguments.iterator();
                if (argIt.hasNext()) intfid = argIt.next();
                if (argIt.hasNext()) oldTask = argIt.next();
                if (argIt.hasNext()) task = argIt.next();
                return doQuery_domain_dominates(intfid, oldTask, task, bindings);
            }
        };
        domainCell.addQueryHandler("dominates", dominatesTransducer);
        planTransducer = new QueryHandler() {

            public boolean query(Collection arguments, Bindings bindings) {
                Object intfid = null;
                Object task = null;
                Object plan = null;
                Iterator argIt = arguments.iterator();
                if (argIt.hasNext()) intfid = argIt.next();
                if (argIt.hasNext()) task = argIt.next();
                if (argIt.hasNext()) plan = argIt.next();
                return doQuery_domain_plan(intfid, task, plan, bindings);
            }
        };
        domainCell.addQueryHandler("plan", planTransducer);
        planTaskTransducer = new QueryHandler() {

            public boolean query(Collection arguments, Bindings bindings) {
                Object intfid = null;
                Object plan = null;
                Object task = null;
                Iterator argIt = arguments.iterator();
                if (argIt.hasNext()) intfid = argIt.next();
                if (argIt.hasNext()) plan = argIt.next();
                if (argIt.hasNext()) task = argIt.next();
                return doQuery_domain_plan_task(intfid, plan, task, bindings);
            }
        };
        domainCell.addQueryHandler("plan_task", planTaskTransducer);
        sysactionTransducer = new QueryHandler() {

            public boolean query(Collection arguments, Bindings bindings) {
                Object intfid = null;
                Object a = null;
                Iterator argIt = arguments.iterator();
                if (argIt.hasNext()) intfid = argIt.next();
                if (argIt.hasNext()) a = argIt.next();
                return doQuery_domain_sysaction(intfid, a, bindings);
            }
        };
        domainCell.addQueryHandler("sysaction", sysactionTransducer);
        allAnswersTransducer = new QueryHandler() {

            public boolean query(Collection arguments, Bindings bindings) {
                Object intfid = null;
                Object question = null;
                Object answs = null;
                Iterator argIt = arguments.iterator();
                if (argIt.hasNext()) intfid = argIt.next();
                if (argIt.hasNext()) question = argIt.next();
                if (argIt.hasNext()) answs = argIt.next();
                return doQuery_domain_all_answers(intfid, question, answs, bindings);
            }
        };
        domainCell.addQueryHandler("all_answers", allAnswersTransducer);
        instantiatePlanTransducer = new MethodHandler() {

            public boolean invoke(Collection arguments, Bindings bindings) {
                Object intfid = null;
                Object code = null;
                Object objid = null;
                Iterator argIt = arguments.iterator();
                if (argIt.hasNext()) intfid = argIt.next();
                if (argIt.hasNext()) code = argIt.next();
                if (argIt.hasNext()) objid = argIt.next();
                return doExec_domain_instantiate_plan(intfid, code, objid, bindings);
            }
        };
        domainCell.addMethodHandler("instantiate_plan", instantiatePlanTransducer);
        nextStepTransducer = new QueryHandler() {

            public boolean query(Collection arguments, Bindings bindings) {
                Object intfid = null;
                Object plan = null;
                Object step = null;
                Iterator argIt = arguments.iterator();
                if (argIt.hasNext()) intfid = argIt.next();
                if (argIt.hasNext()) plan = argIt.next();
                if (argIt.hasNext()) step = argIt.next();
                return doQuery_domain_next_step(intfid, plan, step, bindings);
            }
        };
        domainCell.addQueryHandler("next_step", nextStepTransducer);
        advancePlanTransducer = new MethodHandler() {

            public boolean invoke(Collection arguments, Bindings bindings) {
                Object intfid = null;
                Object plan = null;
                Object annotations = null;
                Iterator argIt = arguments.iterator();
                if (argIt.hasNext()) intfid = argIt.next();
                if (argIt.hasNext()) plan = argIt.next();
                if (argIt.hasNext()) annotations = argIt.next();
                return doExec_domain_advance(intfid, plan, annotations, bindings);
            }
        };
        domainCell.addMethodHandler("advance", advancePlanTransducer);
        resetPlanTransducer = new MethodHandler() {

            public boolean invoke(Collection arguments, Bindings bindings) {
                Object intfid = null;
                Object plan = null;
                Iterator argIt = arguments.iterator();
                if (argIt.hasNext()) intfid = argIt.next();
                if (argIt.hasNext()) plan = argIt.next();
                return doExec_domain_reset(intfid, plan, bindings);
            }
        };
        domainCell.addMethodHandler("reset_plan", resetPlanTransducer);
        setAnswerTransducer = new MethodHandler() {

            public boolean invoke(Collection arguments, Bindings bindings) {
                Object intfid = null;
                Object plan = null;
                Object answer = null;
                Object template = null;
                Iterator argIt = arguments.iterator();
                if (argIt.hasNext()) intfid = argIt.next();
                if (argIt.hasNext()) plan = argIt.next();
                if (argIt.hasNext()) answer = argIt.next();
                if (argIt.hasNext()) template = argIt.next();
                return doExec_domain_set_answer(intfid, plan, answer, template, bindings);
            }
        };
        domainCell.addMethodHandler("set_answer", setAnswerTransducer);
        getAnswerTransducer = new QueryHandler() {

            public boolean query(Collection arguments, Bindings bindings) {
                Object intfid = null;
                Object plan = null;
                Object question = null;
                Iterator argIt = arguments.iterator();
                if (argIt.hasNext()) intfid = argIt.next();
                if (argIt.hasNext()) plan = argIt.next();
                if (argIt.hasNext()) question = argIt.next();
                return doQuery_domain_get_answer(intfid, plan, question, bindings);
            }
        };
        domainCell.addQueryHandler("get_answer", getAnswerTransducer);
        retractAnswerTransducer = new MethodHandler() {

            public boolean invoke(Collection arguments, Bindings bindings) {
                Object intfid = null;
                Object plan = null;
                Object answer = null;
                Iterator argIt = arguments.iterator();
                if (argIt.hasNext()) intfid = argIt.next();
                if (argIt.hasNext()) plan = argIt.next();
                if (argIt.hasNext()) answer = argIt.next();
                return doExec_domain_retract_answer(intfid, plan, answer, bindings);
            }
        };
        domainCell.addMethodHandler("retract_answer", retractAnswerTransducer);
        wantsAnswerTransducer = new QueryHandler() {

            public boolean query(Collection arguments, Bindings bindings) {
                Object intfid = null;
                Object plan = null;
                Object question = null;
                Object template = null;
                Iterator argIt = arguments.iterator();
                if (argIt.hasNext()) intfid = argIt.next();
                if (argIt.hasNext()) plan = argIt.next();
                if (argIt.hasNext()) question = argIt.next();
                if (argIt.hasNext()) template = argIt.next();
                return doQuery_domain_wants_answer(intfid, plan, question, template, bindings);
            }
        };
        domainCell.addQueryHandler("wants_answer", wantsAnswerTransducer);
        return domainCell;
    }
}
