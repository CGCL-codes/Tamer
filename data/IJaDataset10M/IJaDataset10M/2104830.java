package org.activebpel.rt.bpel.def.validation;

import javax.xml.namespace.QName;
import org.activebpel.rt.bpel.AeMessages;

/**
 * Interface definition for error reporting, primarily for validation errors.
 */
public interface IAeValidationDefs {

    /** Empty QName object with an empty selection localPart */
    public static final QName EMPTY_QNAME = new QName("", "(none)");

    /** Error message for a compensateScope activity that is missing its target attribute value */
    public static final String ERROR_COMPENSATE_SCOPE_EMPTY = AeMessages.getString("IAeValidationDefs.CompensateScopeMissingTarget");

    /** Error message for a validate activity that is missing its list of variables to validate */
    public static final String ERROR_EMPTY_VALIDATE = AeMessages.getString("IAeValidationDefs.EmptyValidate");

    /** Error message for a copy operation that will result in mismatchedAssignmentFailure at runtime */
    public static final String ERROR_MISMATCHED_ASSIGNMENT_FAILURE = AeMessages.getString("IAeValidationDefs.MismatchedAssignmentFailure");

    public static final String FIELD_UNDEFINED = "(none)";

    public static final String EXCEPTION_DURING_VALIDATION = AeMessages.getString("IAeValidationDefs.5");

    /** Error for a null strategy for a copy operation's from */
    public static final String ERROR_UNSUPPORTED_COPYOP_FROM = AeMessages.getString("IAeValidationDefs.UnsupportedCopyOperation.From");

    /** Error for a null strategy for a copy operation's to */
    public static final String ERROR_UNSUPPORTED_COPYOP_TO = AeMessages.getString("IAeValidationDefs.UnsupportedCopyOperation.To");

    /** Error message for assigning to a plink that doesn't define a partnerRole */
    public static final String ERROR_PLINK_ASSIGN_TO = AeMessages.getString("IAeValidationDefs.Plink.To.NoPartnerRole");

    /** Error message for assigning from a plink that doesn't have a myRole */
    public static final String ERROR_PLINK_ASSIGN_FROM_MYROE = AeMessages.getString("IAeValidationDefs.Plink.From.NoMyRole");

    /** Error message for assigning from a plink that doesn't have a partnerRole */
    public static final String ERROR_PLINK_ASSIGN_FROM_PARTNERROE = AeMessages.getString("IAeValidationDefs.Plink.From.NoPartnerRole");

    /** Error message for nesting an isolated scope */
    public static final String ERROR_NESTED_ISOLATED_SCOPE = AeMessages.getString("IAeValidationDefs.NestedIsolatedScope");

    /** 
    * Error message for an isolated scope within an FCT handler that compensates 
    * an isolated scope. 
    */
    public static final String ERROR_NESTED_ISOLATED_SCOPE_FCT_SOURCE = AeMessages.getString("IAeValidationDefs.NestedIsolatedScope_source");

    /** 
    * Error message for an isolated scope that is being compensated from an
    * isolated scope within an FCT handler. 
    */
    public static final String ERROR_NESTED_ISOLATED_SCOPE_FCT_TARGET = AeMessages.getString("IAeValidationDefs.NestedIsolatedScope_target");

    /** Warning message for a bpws isolated scope that is not a leaf scope */
    public static final String WARNING_BPWS_SERIALIZABLE_LEAF = AeMessages.getString("IAeValidationDefs.BPWS.IsolatedScope");

    public static final String ERROR_TO_EXPR_FORMAT_INVALID = AeMessages.getString("IAeValidationDefs.InvalidExpressionToSpec");

    /**
    * Non-abstract processes need at least one activity (either a Pick or a Receive) with the 
    * createInstance flag set to true.
    */
    public static final String ERROR_NO_CREATE = AeMessages.getString("IAeValidationDefs.6");

    /**
    * A required field is missing a value.
    * 
    * Arg 0 is the field name.
    */
    public static final String ERROR_FIELD_MISSING = AeMessages.getString("IAeValidationDefs.7");

    /**
    * A required activity is missing from a container.
    * 
    * Arg 0 is the container name.
    */
    public static final String ERROR_ACTIVITY_MISSING = AeMessages.getString("IAeValidationDefs.8");

    /** A forEach, onEvent, onAlarm requires a child scope */
    public static final String ERROR_REQUIRES_SCOPE_CHILD = AeMessages.getString("IAeValidationDefs.RequiresScopeChild");

    /** Implicit variables are not allowed to have explicit declarations */
    public static final String ERROR_IMPLICIT_VARIABLE_EXPLICITLY_DEFINED = AeMessages.getString("IAeValidationDefs.ImplicitVariableDefined");

    /**
    * Assign: must have at least one Copy.
    */
    public static final String ERROR_NO_COPY = AeMessages.getString("IAeValidationDefs.9");

    /**
    * Assign: from can only be opaque in abstract process.
    */
    public static final String ERROR_OPAQUE_NOT_ALLOWED = AeMessages.getString("IAeValidationDefs.10");

    /**
    * OpaqueAcitity: OpaqueAcitity can be only in abstract process.
    */
    public static final String ERROR_OPAQUE_ACTIVITY_NOT_ALLOWED = AeMessages.getString("IAeValidationDefs.OpaqueActivityNotAllowed");

    /**
    * Switch: must have at least one Case.
    */
    public static final String SWITCH_MISSING_CASE = AeMessages.getString("IAeValidationDefs.SwitchMissingCase");

    /**
    * If: must have at least one condition.
    */
    public static final String IF_MISSING_CONDITION = AeMessages.getString("IAeValidationDefs.IfMissingCondition");

    /**
    * Pick: must have at least one onMessage.
    */
    public static final String ERROR_NO_ONMESSAGE = AeMessages.getString("IAeValidationDefs.12");

    /**
    * Pick: if more than one onMessage, then there should be no onAlarm.
    */
    public static final String ERROR_ALARM_ON_CREATEINSTANCE = AeMessages.getString("IAeValidationDefs.13");

    /**
    * If more than one Create Instance, their correlation sets need to match.
    */
    public static final String ERROR_CS_MISMATCH = AeMessages.getString("IAeValidationDefs.14");

    /**
    * Pick: if more than one onMessage, then createInstance should be true.
    */
    public static final String ERROR_MULT_ONMSG_CREATE = AeMessages.getString("IAeValidationDefs.15");

    /**
    * Link definition is invalid.
    * 
    * Arg 0 is the link name. 
    */
    public static final String ERROR_BAD_LINK = AeMessages.getString("IAeValidationDefs.16");

    /**
    * Link has more than one source activity.
    * 
    * Arg 0 is the link name. 
    */
    public static final String ERROR_MULTI_SRC_LINK = AeMessages.getString("IAeValidationDefs.70");

    /**
    * Link has more than one target activity.
    * 
    * Arg 0 is the link name. 
    */
    public static final String ERROR_MULTI_TARGET_LINK = AeMessages.getString("IAeValidationDefs.71");

    /**
    * Link boundary crossing error.
    * 
    * Arg 0 is the link name.
    * Arg 1 is the boundary type.
    */
    public static final String ERROR_LINK_CROSSING = AeMessages.getString("IAeValidationDefs.17");

    /**
    * Bad Link from Scope to a child target activity.
    * 
    * Arg 0 is the link name.
    */
    public static final String ERROR_SCOPE_LINK = AeMessages.getString("IAeValidationDefs.BAD_LINK_ERROR");

    /**
    * Link is part of an invalid graph cycle.
    * 
    * Arg 0 is the link name.
    */
    public static final String ERROR_LINK_CYCLE = AeMessages.getString("IAeValidationDefs.18");

    /**
    * Terminate is not valid in an abstract process.
    */
    public static final String ERROR_TERM_NOT_ALLOWED = AeMessages.getString("IAeValidationDefs.19");

    /**
    * Type specification for variable is missing.
    * 
    * Arg 0 is the variable name.
    */
    public static final String ERROR_VAR_HAS_NO_TYPE = AeMessages.getString("IAeValidationDefs.20");

    /**
    * Partner link is unresolved and can't be found
    * 
    * Arg 0 is the partner link name.
    */
    public static final String ERROR_PARTNER_LINK_NOT_FOUND = AeMessages.getString("IAeValidationDefs.21");

    /**
    * Partner link has no my role
    * 
    * Arg 0 is the partner link name.
    */
    public static final String ERROR_PARTNER_LINK_MISSING_MYROLE = AeMessages.getString("IAeValidationDefs.22");

    /**
    * Partner link has no partner role
    * 
    * Arg 0 is the partner link name.
    */
    public static final String ERROR_PARTNER_LINK_MISSING_PARTNERROLE = AeMessages.getString("IAeValidationDefs.23");

    /**
    * Partner link has no myrole
    * 
    * Arg 0 is the partner link name.
    * Arg 1 is the partner link type role.
    * Arg 2 is the portType for role.
    * Arg 3 is the portType for operation.
    */
    public static final String ERROR_PORTTYPE_MISMATCH = AeMessages.getString("IAeValidationDefs.24");

    /**
    * Scope is unresolved.
    * 
    * Arg 0 is the compensate activity's scope.
    */
    public static final String ERROR_SCOPE_NOT_FOUND = AeMessages.getString("IAeValidationDefs.25");

    /**
    * More than one Scope with same name.
    * 
    * Arg 0 is the compensate activity's target scope.
    */
    public static final String TOO_MANY_SCOPES_FOUND = AeMessages.getString("IAeValidationDefs.DUPLICATE_SCOPE");

    /**
    * Variable is unresolved and can't be found.
    * 
    * Arg 0 is the variable name.
    */
    public static final String ERROR_VAR_NOT_FOUND = AeMessages.getString("IAeValidationDefs.26");

    /**
    * A message part is not declared and can't be found.
    * 
    * Arg 0 is the part name.
    * Arg 1:2 is the message type's QName nsURI:localPart.
    */
    public static final String ERROR_VAR_PART_NOT_FOUND = AeMessages.getString("IAeValidationDefs.27");

    /**
    * A property alias references a message part that doesn't exist
    * 
    * Arg 0:1 message qname
    * Arg 2:3 property qname
    * Arg 4 part name
    */
    public static final String ERROR_PROPERTY_ALIAS_BAD_PART = AeMessages.getString("IAeValidationDefs.PropertyAliasBadPart");

    /**
    * A specified correlation set is unresolved and can't be found.
    * 
    * Arg 0 is the name of the correlation set.
    */
    public static final String ERROR_CORR_SET_NOT_FOUND = AeMessages.getString("IAeValidationDefs.28");

    /**
    * A partner link is missing partner link type.
    * 
    * Arg 0 is the partner link's name.
    */
    public static final String ERROR_PARTNER_LINK_HAS_NO_TYPE = AeMessages.getString("IAeValidationDefs.29");

    /**
    * A partner link type is unresolved and can't be found.
    * 
    * Arg 0:1 is the partner link type's QName nsURI:localPart.
    */
    public static final String ERROR_PARTNER_LINK_TYPE_NOT_FOUND = AeMessages.getString("IAeValidationDefs.30");

    /**
    * A specified role is unresolved and can't be found.
    * 
    * Arg 0 is the role's name.
    * Arg 1 is the partner link type local name.
    */
    public static final String ERROR_ROLE_NOT_FOUND = AeMessages.getString("IAeValidationDefs.31");

    /** 
    * Plink w/o partner role has initializePartnerRole attribute 
    * Arg 0 is the plink's name.
    */
    public static final String ERROR_INIT_PARTNER_ROLE_NOT_ALLOWED = AeMessages.getString("IAeValidationDefs.InitializePartnerRoleNotAllowed");

    /**
    * A specified role has no port type assignment.
    * 
    * Arg 0 is the role's name.
    * Arg 1 is the partner link type local name.
    */
    public static final String ERROR_ROLE_HAS_NO_PORTTYPE = AeMessages.getString("IAeValidationDefs.32");

    /**
    * A specified port type is unresolved and can't be found.
    * 
    * Arg 0:1 is the port type's QName nsURI:localPart.
    */
    public static final String ERROR_PORT_TYPE_NOT_FOUND = AeMessages.getString("IAeValidationDefs.33");

    /** 
    * An element variable is being used for message data consumption or production where
    * a message variable is required 
    */
    public static final String ERROR_ELEMENT_VARIABLE_NOT_ALLOWED = AeMessages.getString("IAeValidationDefs.ElementVariableNotAllowed");

    /** 
    * The wrong element variable is being used to send/receive message data.
    * 
    * Arg 0 operation
    *     1:2 actual element variable qname
    *     3:4 expected message qname
    *     5:6 expected element qname
    */
    public static final String ERROR_WRONG_ELEMENT_VARIABLE = AeMessages.getString("IAeValidationDefs.WrongElementVariable");

    /** 
    * The wrong message variable is being used to send/receive message data.
    * 
    * Arg 0 operation
    *     1:2 actual msg variable qname
    *     3:4 expected message qname
    */
    public static final String ERROR_WRONG_MESSAGE_VARIABLE = AeMessages.getString("IAeValidationDefs.WrongMessageVariable");

    /** A message variable is required (bpws 1.1 error message) */
    public static final String ERROR_MESSAGE_VARIABLE_REQUIRED = AeMessages.getString("IAeValidationDefs.MessageVariableRequired");

    /** wsio activities cannot have simple/complex type variables for their message data consumption/production */
    public static final String ERROR_TYPE_VARIABLE_NOT_ALLOWED = AeMessages.getString("IAeValidationDefs.TypeVariableNotAllowed");

    /** wsio activities that consumes messages does not have a valid strategy for consuming the message data */
    public static final String ERROR_MESSAGE_CONSUMER_STRATEGY = AeMessages.getString("IAeValidationDefs.MessageConsumerStrategyNotSet");

    /** wsio activities that produces messages does not have a valid strategy for producing the message data */
    public static final String ERROR_MESSAGE_PRODUCER_STRATEGY = AeMessages.getString("IAeValidationDefs.MessageProducerStrategyNotSet");

    /** 
    * wsio activities that consumes messages does not have a valid strategy for consuming the message data
    * Arg {0}:{1} message type
    *     {2} - operation  
    */
    public static final String ERROR_EMPTY_MESSAGE_CONSUMER_STRATEGY_NOT_ALLOWED = AeMessages.getString("IAeValidationDefs.EmptyMessageConsumerStrategyNotAllowed");

    /** 
    * wsio activities that consumes messages does not have a valid strategy for producing the message data
    * Arg {0}:{1} message type
    *     {2} - operation  
    */
    public static final String ERROR_EMPTY_MESSAGE_PRODUCER_STRATEGY_NOT_ALLOWED = AeMessages.getString("IAeValidationDefs.EmptyMessageProducerStrategyNotAllowed");

    /**
    * A specified operation with given input and is unresolved and can't be found.
    * 
    * Arg 0 is the operation's name.
    * Arg 1:2 is the QName of the invalid input message type.
    * Arg 3:4 is the QName of the invalid output message type
    */
    public static final String ERROR_OPERATION_INOUT_NOT_FOUND = AeMessages.getString("IAeValidationDefs.34");

    /**
    * A specified operation with given input is unresolved and can't be found.
    * 
    * Arg 0 is the operation's name.
    * Arg 1:2 is the QName of the invalid input message type.
    */
    public static final String ERROR_OPERATION_IN_NOT_FOUND = AeMessages.getString("IAeValidationDefs.35");

    /**
    * A specified operation with given output is unresolved and can't be found.
    * 
    * Arg 0 is the operation's name.
    * Arg 1:2 is the QName of the invalid output message type
    */
    public static final String ERROR_OPERATION_OUT_NOT_FOUND = AeMessages.getString("IAeValidationDefs.36");

    /**
    * A specified operation for the given port type cannot be found
    * 
    * Arg 0 is the operation's name.
    * Arg 1:2 is the QName of the port type
    */
    public static final String ERROR_OPERATION_NOT_FOUND = AeMessages.getString("IAeValidationDefs.OperationNotFound");

    /** A correlation pattern is only allowed to be used on an invoke */
    public static final String ERROR_CORRELATION_PATTERN_NOT_ALLOWED = AeMessages.getString("IAeValidationDefs.PatternNotAllowed");

    /** A correlation pattern is required for an invoke */
    public static final String ERROR_CORRELATION_PATTERN_REQUIRED = AeMessages.getString("IAeValidationDefs.PatternRequired");

    /**
    * A correlation pattern is set to OUT or OUT-IN, but the invoke is a one-way
    * 
    */
    public static final String ERROR_CORRELATION_OUT_PATTERN_MISMATCH = AeMessages.getString("IAeValidationDefs.38");

    /** A correlation pattern value is not valid. */
    public static final String ERROR_CORRELATION_INVALID_PATTERN = AeMessages.getString("IAeValidationDefs.InvalidPattern");

    /**
    * A specified property is unresolved and can't be found.
    * 
    * Arg 0:1 is the property's QName nsURI:localPart.
    */
    public static final String ERROR_PROP_NOT_FOUND = AeMessages.getString("IAeValidationDefs.39");

    /**
    * A specified message type is unresolved and can't be found.
    * 
    * Arg 0:1 is the message type's QName nsURI:localPart.
    */
    public static final String ERROR_MSG_SPEC_NOT_FOUND = AeMessages.getString("IAeValidationDefs.40");

    /**
    * A specified element is unresolved and can't be found.
    * 
    * Arg 0:1 is the element's QName nsURI:localPart.
    */
    public static final String ERROR_ELEMENT_SPEC_NOT_FOUND = AeMessages.getString("IAeValidationDefs.41");

    /**
    * A specified type specification is unresolved and can't be found.
    * 
    * Arg 0:1 is the type specification's QName nsURI:localPart.
    */
    public static final String ERROR_TYPE_SPEC_NOT_FOUND = AeMessages.getString("IAeValidationDefs.42");

    /**
    * One or more property aliases for a variable are undefined.
    * 
    * Arg 0 is the variable name.
    * Arg 1 is the variable's type name.
    * Arg 2 is a comma-delimited list of the names of properties that are missing aliases.
    */
    public static final String ERROR_CORRELATION_PROP_ALIASES_NOT_FOUND = AeMessages.getString("IAeValidationDefs.0");

    /**
    * Correlation set has no properties
    * 
    * Arg 0 is the correlation set name.
    */
    public static final String ERROR_CORR_SET_PROPS_NOT_FOUND = AeMessages.getString("IAeValidationDefs.45");

    /**
    * The propertyAlias used for correlation is for a complex type but it doesn't specify a query. 
    *  This will fail at runtime unless the type is an "anyType" and is a simple type at runtime. 
    * 
    * Arg 0:1 message qname
    * Arg 2:3 property qname
    * Arg 4 part
    */
    public static final String ERROR_NO_QUERY_FOR_PROP_ALIAS = AeMessages.getString("IAeValidationDefs.MissingPropertyAliasQuery");

    /**
    * An invalid expression (unexpected error) was encountered. Arg 0 is the invalid expression. Arg 1 is the reason.
    */
    public static final String ERROR_INVALID_EXPRESSION = AeMessages.getString("IAeValidationDefs.INVALID_EXPRESSION");

    public static final String ERROR_EMPTY_START_EXPRESSION = AeMessages.getString("IAeValidationDefs.EMPTY_START_EXPRESSION");

    public static final String ERROR_EMPTY_FINAL_EXPRESSION = AeMessages.getString("IAeValidationDefs.EMPTY_FINAL_EXPRESSION");

    public static final String ERROR_EMPTY_COMPLETION_CONDITION_EXPRESSION = AeMessages.getString("IAeValidationDefs.EMPTY_COMPLETION_CONDITION_EXPRESSION");

    /**
    * An invalid XPath expression was encountered. Arg 0 is the invalid expression. Arg 1 is the reason.
    */
    public static final String ERROR_INVALID_XPATH = AeMessages.getString("AeXPathExpressionValidator.INVALID_XPATH");

    /** signals an illegal part attribute for a copy operation's from/to */
    public static final String ERROR_PART_USAGE = AeMessages.getString("IAeValidationDefs.ERROR_PART_USAGE");

    /**
    * Variable Type Mismatch for an Activity (i.e., the variable used doesn't match the type 
    * needed by the activity.
    * 
    * Arg 0 is the variable name.
    * Arg 1:2 is the QName of the variable's type.
    * Arg 3:4 is the QName of the activity's required type.
    */
    public static final String ERROR_VAR_TYPE_MISMATCH = AeMessages.getString("IAeValidationDefs.49");

    /** 
    * Variable type mismatch where reference was to a message part but the variable def is not a message
    * 
    *  Arg 0 = var name
    *  Arg 1:2 = var type Qname
    *  Arg 3 = message part
    */
    public static final String ERROR_VAR_TYPE_MISMATCH_MESSAGE = AeMessages.getString("IAeValidationDefs.ExpectedMessageType");

    /** 
    * Variable type mismatch where reference was to an element but the variable def is not an element
    * 
    *  Arg 0 = var name
    *  Arg 1:2 = var type Qname
    */
    public static final String ERROR_VAR_TYPE_MISMATCH_ELEMENT = AeMessages.getString("IAeValidationDefs.ExpectedElement");

    /**
    * A fault name is unresolved and can't be found.
    * 
    * Arg 0 is the fault name.
    */
    public static final String ERROR_FAULT_NAME_NOT_FOUND = AeMessages.getString("IAeValidationDefs.50");

    /**
    * A fault handler has not activity.
    */
    public static final String ERROR_EMPTY_FAULT_HANDLER = AeMessages.getString("IAeValidationDefs.51");

    public static final String ERROR_EMPTY_CONTAINER = AeMessages.getString("IAeValidationDefs.EmptyContainer");

    /** Error for a compensation handler on the root scope of a FCT handler */
    public static final String ERROR_ROOT_SCOPE_FCT_HANDLER = AeMessages.getString("IAeValidationDefs.RootScopeFCTHandler");

    /**
    * Error message for illegal catch pattern for BPWS
    */
    public static final String ERROR_BPWS_CATCH_PATTERN = AeMessages.getString("IAeValidationDefs.BPWS_CATCH_PATTERN");

    /**
    * Error message for illegal catch pattern for WSBPEL
    */
    public static final String ERROR_WSBPEL_CATCH_PATTERN = AeMessages.getString("IAeValidationDefs.WSBPEL_CATCH_PATTERN");

    /**
    * Error message for illegal fault handler constructs in WSBPEL.
    */
    public static final String ERROR_ILLEGAL_FH_CONSTRUCTS = AeMessages.getString("IAeValidationDefs.ERROR_ILLEGAL_FH_CONSTRUCTS");

    /**
    *  A fault handler catch a standard BPEL fault while the exitOnStandardFault is set to yes in scope/process.
    */
    public static final String ERROR_ILLEGAL_CATCH_FOR_EXIT_ON_STD_FAULT = AeMessages.getString("IAeValidationDefs.ERROR_ILLEGAL_CATCH_FOR_EXIT_ON_STD_FAULT");

    /**
    * A compensate activity is in the wrong place.
    * 
    * fixme this error message needs to be broken into a 2.0 and a 1.1 version
    */
    public static final String ERROR_MISPLACED_COMPENSATE = AeMessages.getString("IAeValidationDefs.52");

    /**
    * A compensate activity is in the wrong place.
    */
    public static final String ERROR_MISPLACED_RETHROW = AeMessages.getString("IAeValidationDefs.MisplacedRethrow");

    /**
    * A part type specification is unresolved or can't be found.
    * 
    * Arg 0 is partname
    * Arg 1:2 is the message type specification's QName nsURI:localPart.
    * Arg 3 Exception message
    */
    public static final String ERROR_DISCOVERING_PART_TYPE_SPECS = AeMessages.getString("IAeValidationDefs.53");

    /**
    * The object name is invalid, ie: not a valid NCName.
    * 
    * Arg 0 The invalid name
    */
    public static final String ERROR_INVALID_NAME = AeMessages.getString("IAeValidationDefs.69");

    /**
    * No validator could be found for the expression language.
    * 
    * Arg 0 - the expression language.
    */
    public static final String ERROR_NO_VALIDATOR_FOR_LANGUAGE = AeMessages.getString("IAeValidationDefs.NO_VALIDATOR_FOR_EXPRLANG_ERROR");

    /** Error when we encounter multiple children but only expected one */
    public static final String ERROR_MULTIPLE_CHILDREN_FOUND = AeMessages.getString("IAeValidationDefs.MultipleChildrenError");

    /** Error when an empty 'eventHandlers' construct is found. */
    public static final String ERROR_EMPTY_EVENT_HANDLER = AeMessages.getString("IAeValidationDefs.MissingEventHandlerError");

    /**
    * A fault variable MUST be an element or messageType
    * 
    * Arg 0 is the fault variable name.
    */
    public static final String ERROR_FAULT_TYPE = AeMessages.getString("IAeValidationDefs.FaultType");

    /** A fault variable MUST be a messageType for BPWS 1.1 */
    public static final String ERROR_FAULT_MESSAGETYPE_REQUIRED = AeMessages.getString("IAeValidationDefs.FaultMessageTypeRequired");

    public static final String WARNING_TEST = "WarningTest";

    /**
    * A defined partner link is never used.
    * 
    * Arg 0 is the partner link name.
    */
    public static final String WARNING_PARTNER_LINK_NOT_USED = AeMessages.getString("IAeValidationDefs.56");

    /**
    * A defined variable is never used.
    * 
    * Arg 0 is the variable name.
    */
    public static final String WARNING_VARIABLE_NOT_USED = AeMessages.getString("IAeValidationDefs.57");

    /**
    * A defined variable is never read.
    * 
    * Arg 0 is the variable name.
    */
    public static final String WARNING_VARIABLE_NOT_READ = AeMessages.getString("IAeValidationDefs.VariableNotRead");

    /**
    * A defined variable is never written to.
    * 
    * Arg 0 is the variable name.
    */
    public static final String WARNING_VARIABLE_NO_INIT = AeMessages.getString("IAeValidationDefs.VariableImproperIO");

    /**
    * A defined variable is never written to.
    * 
    * Arg 0 is the variable name.
    */
    public static final String WARNING_VARIABLE_NOT_WRITTEN_TO = AeMessages.getString("IAeValidationDefs.VariableNotWrittenTo");

    /** A message variable can only be initialized from a variable of the same message type */
    public static final String WARNING_INVALID_MESSAGE_VARIABLE_INIT = AeMessages.getString("IAeValidationDefs.InvalidMessageVariableInit");

    /**
    * A defined correlation set is never used.
    * 
    * Arg 0 is the correlation set name.
    */
    public static final String WARNING_CORR_SET_NOT_USED = AeMessages.getString("IAeValidationDefs.58");

    /**
    * A defined variable is never used.
    * 
    * Arg 0 is extension.
    */
    public static final String WARNING_EXTENSION_NOT_USED = AeMessages.getString("IAeValidationDefs.ExtensionNotUsedError");

    /**
    * Link not used.
    * 
    * Arg 0 is the link name. 
    */
    public static final String WARNING_LINK_NOT_USED = AeMessages.getString("IAeValidationDefs.UnusedLinkWarning");

    /**
    * Import is missing it's location specifier.
    */
    public static final String WARNING_MISSING_IMPORT_LOCATION = AeMessages.getString("IAeValidationDefs.59");

    /**
    * Namespace has an invalid location specifier.
    * 
    * Arg 0 is the namespace prefix.
    */
    public static final String WARNING_INVALID_IMPORT_LOCATION = AeMessages.getString("IAeValidationDefs.60");

    /**
    * Receive: no correlation set assigned and the Receive is not a create instance.
    */
    public static final String WARNING_NO_CORR_SET_NO_CREATE = AeMessages.getString("IAeValidationDefs.61");

    /**
    * A fault name is unresolved and can't be found.
    * 
    * Arg 0 is the fault name.
    */
    public static final String WARN_FAULT_NAME_NOT_CAUGHT = AeMessages.getString("IAeValidationDefs.62");

    /**
    * Enabled instance compensation set to No - compensation handler invalid.
    */
    public static final String WARN_COMPENSATION_HANDLER_NOT_ENABLED = AeMessages.getString("IAeValidationDefs.63");

    /**
    * Missing single quotes to specify a constant variable name.
    * 
    * Arg 1 is the variable name.
    */
    public static final String WARN_NON_CONST_VARNAME = AeMessages.getString("IAeValidationDefs.64");

    /**
    * A variable type has been overloaded within a scope definition
    * 
    * Arg 0 is the variable name.
    */
    public static final String WARNING_VARIABLE_TYPE_OVERLOADED = AeMessages.getString("IAeValidationDefs.65");

    /**
    * A variable has supplied a query expression without a required part name
    * 
    * Arg 0 is the variable name.
    */
    public static final String WARNING_VARIABLE_PART_REQUIRED = AeMessages.getString("IAeValidationDefs.66");

    /**
    * A variable has supplied a query expression for a simple type variable
    * 
    * Arg 0 is the variable name.
    */
    public static final String WARNING_VARIABLE_QUERY_NOT_SUPPORTED = AeMessages.getString("IAeValidationDefs.67");

    /**
    * Invalid literal found in join conditio expression.
    * 
    * Arg 0 is the invalid literal.
    */
    public static final String INVALID_LITERAL_IN_JOIN_CONDITION = AeMessages.getString("IAeValidationDefs.75");

    /**
    * Link in joinCondition could not be found.
    * 
    * Arg 0 is the link name.
    * Arg 1 is the joinCondition expression 
    */
    public static final String NO_LINK_FOUND_FOR_JOIN_CONDITION = AeMessages.getString("IAeValidationDefs.76");

    /**
    * Invalid match between number of link names and number of getLinkStatus function calls.
    * 
    * Arg 0 is the joinCondition expression 
    */
    public static final String INVALID_JOIN_CONDITION = AeMessages.getString("IAeValidationDefs.77");

    /**
    * Found a getLinkStatus with no args.
    * 
    * Arg 0 is the entire joinCondition expression.
    */
    public static final String EMPTY_GET_LINK_STATUS_FUNCTION = AeMessages.getString("IAeValidationDefs.72");

    /**
    * Invalid nesting of functions inside joinCondition expression.
    * 
    * Arg 0 is the entire joinCondition expression.
    */
    public static final String INVALID_NESTING_IN_LINK_STATUS_FUNCTION = AeMessages.getString("IAeValidationDefs.73");

    /**
    * Invalid arg present in getLinkStatus function of joinCondition.
    * 
    * Arg 0 is the joinCondition expression.
    */
    public static final String INVALID_ARG_IN_LINK_STATUS_FUNCTION = AeMessages.getString("IAeValidationDefs.74");

    /**
    * A non-persistent process uses subprocess invoke protocol.
    * 
    * Arg 0 is partner link name.
    */
    public static final String WARNING_NONPERSISTENT_SUBPROCESS_NOT_ALLOWED = AeMessages.getString("IAeValidationDefs.NONPERSIST_SUBPROCESS_NOT_ALLOWED");

    /**
    * A non-persistent process uses retry policies.
    * 
    * Arg 0 is partner link name.
    */
    public static final String ERROR_NONPERSISTENT_RETRYPOLICY_NOT_ALLOWED = AeMessages.getString("IAeValidationDefs.NONPERSIST_RETRY_POLICY_NOT_ALLOWED");

    /**
    * A non-persistent process has BPEL constructs (such as OnMessage) which are not allowed.
    * 
    * Arg 0 is BPEL constuct name.
    */
    public static final String ERROR_NONPERSISTENT_ACTIVITY_NOT_ALLOWED = AeMessages.getString("IAeValidationDefs.NONPERSIST_ACTIVITY_NOT_ALLOWED");

    /**
    * A non-persistent process has multiple BPEL Receive activities which is not allowed.
    * 
    * Arg 0 is number of receive activities found.
    */
    public static final String ERROR_NONPERSISTENT_MULTIPLE_RECEIVES_NOT_ALLOWED = AeMessages.getString("IAeValidationDefs.NONPERSIST_MULTIPLE_RECEIVES_NOT_ALLOWED");

    /**
    * A non-persistent process does not have a createInstance receive or pick.
    */
    public static final String ERROR_NONPERSISTENT_CREATE_INSTANCE_NOT_FOUND = AeMessages.getString("IAeValidationDefs.NONPERSIST_CREATE_INSTANCE_NOT_FOUND");

    /**
    * The extension was not understood, but the declaration of the extension indicated mustUnderstand=='yes'.
    */
    public static final String ERROR_DID_NOT_UNDERSTAND_EXTENSION = AeMessages.getString("IAeValidationDefs.UnknownExtensionError");

    /**
    * An extension was found but the namespace was not declared in the list of extensions.
    */
    public static final String ERROR_UNDECLARED_EXTENSION = AeMessages.getString("IAeValidationDefs.UndeclaredExtensionError");

    /**
    * An attribute was found but was not read.
    */
    public static final String ERROR_UNEXPECTED_ATTRIBUTE = AeMessages.getString("IAeValidationDefs.UnexpectedAttribute");

    /**
    * An invalid literal was found.
    */
    public static final String ERROR_INVALID_LITERAL = AeMessages.getString("IAeValidationDefs.InvalidLiteral");

    /**
    * A xsi:schemaLocation attribute was found on the literal.
    */
    public static final String WARNING_SCHEMA_LOCATION_IN_LITERAL = AeMessages.getString("IAeValidationDefs.XsiSchemaLocationFoundInLiteral");

    /**
    * Missing import for a reference. 
    */
    public static final String WARNING_MISSING_IMPORT = AeMessages.getString("IAeValidationDefs.MissingImport");

    public static final String INFO_TEST = "InfoTest";
}
