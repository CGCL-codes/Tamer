package org.spantus.segment.online;

import org.spantus.core.threshold.SegmentEvent;
import org.spantus.logger.Logger;
import org.spantus.segment.online.rule.DecisionCtx;
import org.spantus.segment.online.rule.RuleBaseEnum;
import org.spantus.segment.online.rule.RuleBaseService;
import org.spantus.segment.online.rule.RuleServiceFactory;

/**
 * 
 * 
 * @author Mindaugas Greibus
 * 
 * @since 0.0.1
 * 
 * Created 2008.11.27
 * 
 */
public class DecisionSegmentatorOnline extends MultipleSegmentatorListenerOnline {

    private DecisionCtx decisionContext;

    private RuleBaseService ruleBaseService;

    @SuppressWarnings("unused")
    private Logger log = Logger.getLogger(DecisionSegmentatorOnline.class);

    @Override
    protected void segmentDetected(SegmentEvent event) {
        getDecisionContext().setState(true);
        processState(event);
    }

    @Override
    protected void noiseDetected(SegmentEvent event) {
        getDecisionContext().setState(false);
        processState(event);
    }

    public void processState(SegmentEvent event) {
        Long time = event.getTime();
        Long sample = event.getSample();
        DecisionCtx ctx = getDecisionContext();
        ctx.setTime(time);
        ctx.setSample(sample);
        if (ctx.getState() == null) return;
        String actionStr = getRuleBaseService().testOnRuleBase(ctx);
        RuleBaseEnum.action action = RuleBaseEnum.action.valueOf(actionStr);
        switch(action) {
            case processNoise:
                onProcessNoise(ctx, event);
                break;
            case startSegmentFound:
                onStartSegmentFound(ctx, event);
                break;
            case startSegmentApproved:
                onStartSegmentApproved(ctx, event);
                break;
            case processSegment:
                onProcessSegment(ctx, event);
                break;
            case endSegmentFound:
                onEndSegmentFound(ctx, event);
                break;
            case endSegmentApproved:
                onEndSegmentApproved(ctx, event);
                break;
            case joinToSegment:
                onJoinToSegment(ctx, event);
                break;
            case deleteSegment:
                onDeleteSegment(ctx, event);
                break;
            default:
                throw new RuntimeException("Not implemented");
        }
    }

    public void onProcessNoise(DecisionCtx ctx, SegmentEvent event) {
    }

    public void onStartSegmentFound(DecisionCtx ctx, SegmentEvent event) {
        ctx.setMarker(createSegment(event));
        finazlizeSegment(ctx.getMarker(), event);
        ctx.setSegmentState(RuleBaseEnum.state.start);
        debugAction("onStartSegmentFound", ctx);
    }

    public void onStartSegmentApproved(DecisionCtx ctx, SegmentEvent event) {
        super.onStartSegment(ctx.getMarker());
        finazlizeSegment(ctx.getMarker(), event);
        ctx.setSegmentState(RuleBaseEnum.state.segment);
        debugAction("onStartSegmentApproved", ctx);
    }

    public void onProcessSegment(DecisionCtx ctx, SegmentEvent event) {
        finazlizeSegment(ctx.getMarker(), event);
        ctx.setSegmentState(RuleBaseEnum.state.segment);
        debugAction("onProcessSegment", ctx);
    }

    public void onEndSegmentFound(DecisionCtx ctx, SegmentEvent event) {
        finazlizeSegment(ctx.getMarker(), event);
        ctx.setSegmentState(RuleBaseEnum.state.end);
        debugAction("onEndSegmentFound", ctx);
    }

    public void onEndSegmentApproved(DecisionCtx ctx, SegmentEvent event) {
        Long expandedStart = ctx.getMarker().getStart() - getParam().getExpandStart();
        Long expandedLength = ctx.getMarker().getLength() + getParam().getExpandEnd();
        ctx.getMarker().setStart(expandedStart);
        ctx.getMarker().setLength(expandedLength);
        debugAction("onEndSegmentApproved", ctx);
        onSegmentEnded(ctx.getMarker());
        ctx.setMarker(null);
        ctx.setSegmentState(null);
    }

    public void onJoinToSegment(DecisionCtx ctx, SegmentEvent event) {
        finazlizeSegment(ctx.getMarker(), event);
        ctx.setSegmentState(RuleBaseEnum.state.segment);
        debugAction("onJoinToSegment", ctx);
    }

    public void onDeleteSegment(DecisionCtx ctx, SegmentEvent event) {
        if (ctx.getMarker() != null) debugAction("onDeleteSegment", ctx);
        setCurrentMarker(null);
        ctx.setMarker(null);
        ctx.setSegmentState(null);
    }

    public DecisionCtx getDecisionContext() {
        if (decisionContext == null) {
            decisionContext = new DecisionCtx();
        }
        return decisionContext;
    }

    public OnlineDecisionSegmentatorParam getParam() {
        return getDecisionContext().getParam();
    }

    public void setParam(OnlineDecisionSegmentatorParam param) {
        getDecisionContext().setParam(param);
    }

    public RuleBaseService getRuleBaseService() {
        if (ruleBaseService == null) {
            ruleBaseService = RuleServiceFactory.createRuleBaseService();
        }
        return ruleBaseService;
    }

    public void setRuleBaseService(RuleBaseService ruleBaseService) {
        this.ruleBaseService = ruleBaseService;
    }

    protected void debugAction(String msg, DecisionCtx ctx) {
    }
}
