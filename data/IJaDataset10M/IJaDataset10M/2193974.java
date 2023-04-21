package org.dbe.composer.wfengine.bpel.def.io;

import org.dbe.composer.wfengine.bpel.def.SdlCorrelationContainerDef;
import org.dbe.composer.wfengine.bpel.def.SdlCorrelationSetContainerDef;
import org.dbe.composer.wfengine.bpel.def.SdlCorrelationSetDef;
import org.dbe.composer.wfengine.bpel.def.SdlDefaultFaultHandlerDef;
import org.dbe.composer.wfengine.bpel.def.SdlEventHandlersDef;
import org.dbe.composer.wfengine.bpel.def.SdlFaultHandlerDef;
import org.dbe.composer.wfengine.bpel.def.SdlFaultHandlersContainerDef;
import org.dbe.composer.wfengine.bpel.def.SdlPartnerDef;
import org.dbe.composer.wfengine.bpel.def.SdlPartnerLinkDef;
import org.dbe.composer.wfengine.bpel.def.SdlPartnerLinksContainerDef;
import org.dbe.composer.wfengine.bpel.def.SdlPartnersContainerDef;
import org.dbe.composer.wfengine.bpel.def.SdlProcessDef;
import org.dbe.composer.wfengine.bpel.def.SdlScopeCompensatorContainerDef;
import org.dbe.composer.wfengine.bpel.def.SdlScopeDef;
import org.dbe.composer.wfengine.bpel.def.SdlVariableDef;
import org.dbe.composer.wfengine.bpel.def.SdlVariablesContainerDef;
import org.dbe.composer.wfengine.bpel.def.activity.SdlActivityAssignDef;
import org.dbe.composer.wfengine.bpel.def.activity.SdlActivityCompensateDef;
import org.dbe.composer.wfengine.bpel.def.activity.SdlActivityEmptyDef;
import org.dbe.composer.wfengine.bpel.def.activity.SdlActivityFlowDef;
import org.dbe.composer.wfengine.bpel.def.activity.SdlActivityInvokeDef;
import org.dbe.composer.wfengine.bpel.def.activity.SdlActivityPickDef;
import org.dbe.composer.wfengine.bpel.def.activity.SdlActivityReceiveDef;
import org.dbe.composer.wfengine.bpel.def.activity.SdlActivityReplyDef;
import org.dbe.composer.wfengine.bpel.def.activity.SdlActivityScopeDef;
import org.dbe.composer.wfengine.bpel.def.activity.SdlActivitySequenceDef;
import org.dbe.composer.wfengine.bpel.def.activity.SdlActivitySwitchDef;
import org.dbe.composer.wfengine.bpel.def.activity.SdlActivityTerminateDef;
import org.dbe.composer.wfengine.bpel.def.activity.SdlActivityThrowDef;
import org.dbe.composer.wfengine.bpel.def.activity.SdlActivityWaitDef;
import org.dbe.composer.wfengine.bpel.def.activity.SdlActivityWhileDef;
import org.dbe.composer.wfengine.bpel.def.activity.support.SdlAssignCopyDef;
import org.dbe.composer.wfengine.bpel.def.activity.support.SdlAssignFromDef;
import org.dbe.composer.wfengine.bpel.def.activity.support.SdlAssignToDef;
import org.dbe.composer.wfengine.bpel.def.activity.support.SdlCorrelationDef;
import org.dbe.composer.wfengine.bpel.def.activity.support.SdlLinkContainerDef;
import org.dbe.composer.wfengine.bpel.def.activity.support.SdlLinkDef;
import org.dbe.composer.wfengine.bpel.def.activity.support.SdlOnAlarmDef;
import org.dbe.composer.wfengine.bpel.def.activity.support.SdlOnMessageDef;
import org.dbe.composer.wfengine.bpel.def.activity.support.SdlOtherwiseDef;
import org.dbe.composer.wfengine.bpel.def.activity.support.SdlSourceDef;
import org.dbe.composer.wfengine.bpel.def.activity.support.SdlSwitchCaseDef;
import org.dbe.composer.wfengine.bpel.def.activity.support.SdlTargetDef;

/**
 * Defines all of the class constants used by IAeBpelDefWriters,
 * IAeBpelDefReaders, and the corresponding registries.
 * <br />
 * This is used in place of Class.forName where we are constrained by
 * current obfuscation.
 */
public interface ISdlBpelClassConstants {

    public static final Class ASSIGN_COPY_CLASS = new SdlAssignCopyDef().getClass();

    public static final Class ASSIGN_TO_CLASS = new SdlAssignToDef().getClass();

    public static final Class ASSIGN_FROM_CLASS = new SdlAssignFromDef().getClass();

    public static final Class CASE_CLASS = new SdlSwitchCaseDef().getClass();

    public static final Class CORRELATION_CLASS = new SdlCorrelationDef().getClass();

    public static final Class CORRELATION_CONTAINER_CLASS = new SdlCorrelationContainerDef().getClass();

    public static final Class CORRELATION_SET_CLASS = new SdlCorrelationSetDef().getClass();

    public static final Class CORRELATION_SETS_CLASS = new SdlCorrelationSetContainerDef().getClass();

    public static final Class DEFAULT_FAULT_HANDLER_CLASS = new SdlDefaultFaultHandlerDef().getClass();

    public static final Class EVENT_HANDLERS_CLASS = new SdlEventHandlersDef().getClass();

    public static final Class FAULT_HANDLER_CLASS = new SdlFaultHandlerDef().getClass();

    public static final Class FAULT_HANDLER_CONTAINER_CLASS = new SdlFaultHandlersContainerDef().getClass();

    public static final Class LINK_CLASS = new SdlLinkDef().getClass();

    public static final Class LINKS_CONTAINER_CLASS = new SdlLinkContainerDef().getClass();

    public static final Class ON_ALARM_CLASS = new SdlOnAlarmDef().getClass();

    public static final Class ON_MESSAGE_CLASS = new SdlOnMessageDef().getClass();

    public static final Class OTHERWISE_CLASS = new SdlOtherwiseDef().getClass();

    public static final Class PARTNER_CLASS = new SdlPartnerDef().getClass();

    public static final Class PARTNER_CONTAINER_CLASS = new SdlPartnersContainerDef().getClass();

    public static final Class PARTNER_LINK_CLASS = new SdlPartnerLinkDef().getClass();

    public static final Class PARNTER_LINKS_CONTAINER_CLASS = new SdlPartnerLinksContainerDef().getClass();

    public static final Class PROCESS_CLASS = new SdlProcessDef().getClass();

    public static final Class SCOPE_CLASS = new SdlScopeDef().getClass();

    public static final Class SCOPE_COMPENSATOR_CONTAINER_CLASS = new SdlScopeCompensatorContainerDef().getClass();

    public static final Class SOURCE_CLASS = new SdlSourceDef().getClass();

    public static final Class TARGET_CLASS = new SdlTargetDef().getClass();

    public static final Class VARIABLE_CLASS = new SdlVariableDef().getClass();

    public static final Class VARIABLE_CONTAINER_CLASS = new SdlVariablesContainerDef().getClass();

    public static final Class ACTIVITY_ASSIGN_CLASS = new SdlActivityAssignDef().getClass();

    public static final Class ACTIVITY_COMPENSATE_CLASS = new SdlActivityCompensateDef().getClass();

    public static final Class ACTIVITY_EMPTY_CLASS = new SdlActivityEmptyDef().getClass();

    public static final Class ACTIVITY_FLOW_CLASS = new SdlActivityFlowDef().getClass();

    public static final Class ACTIVITY_INVOKE_CLASS = new SdlActivityInvokeDef().getClass();

    public static final Class ACTIVITY_PICK_CLASS = new SdlActivityPickDef().getClass();

    public static final Class ACTIVITY_SCOPE_CLASS = new SdlActivityScopeDef().getClass();

    public static final Class ACTIVITY_SEQUENCE_CLASS = new SdlActivitySequenceDef().getClass();

    public static final Class ACTIVITY_SWITCH_CLASS = new SdlActivitySwitchDef().getClass();

    public static final Class ACTIVITY_TERMINATE_CLASS = new SdlActivityTerminateDef().getClass();

    public static final Class ACTIVITY_THROW_CLASS = new SdlActivityThrowDef().getClass();

    public static final Class ACTIVITY_REPLY_CLASS = new SdlActivityReplyDef().getClass();

    public static final Class ACTIVITY_RECEIVE_CLASS = new SdlActivityReceiveDef().getClass();

    public static final Class ACTIVITY_WAIT_CLASS = new SdlActivityWaitDef().getClass();

    public static final Class ACTIVITY_WHILE_CLASS = new SdlActivityWhileDef().getClass();
}
