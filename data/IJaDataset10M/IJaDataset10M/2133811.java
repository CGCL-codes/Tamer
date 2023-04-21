package LayerD.CodeDOM;

import LayerD.CodeDOM.XplParser.ParseException;
import java.io.*;
import java.util.*;
import java.text.*;

public class XplOutputModuleCapabilities extends XplNode implements XplNodeListCallbacks {

    boolean p_IsCaseSensitive;

    boolean p_AllowDefaultSafeArrays;

    boolean p_AllowDisableArrayLimitsChecks;

    boolean p_AllowUseSinglePointAsNamespaceSeparator;

    boolean p_AllowUseSimpleMemberAccessAsNamespaceSeparator;

    boolean p_AllowMultipleInheritance;

    boolean p_UseUniversalObjectBase;

    boolean p_AllowSetUniversalObjectBase;

    boolean p_AllowDisableUnifiedTypeSystem;

    boolean p_AllowEnableIntegerOverflowExceptions;

    boolean p_AllowEnableFloatOperationsExceptions;

    boolean p_RequireIntegerOverflowExceptions;

    boolean p_RequireFloatOperationsExceptions;

    boolean p_FullDecimalImplementation;

    boolean p_SupportASCIIChar;

    boolean p_SupportASCIIString;

    boolean p_SupportUnsignedIntegers;

    boolean p_SupportExtendedValueTypes;

    boolean p_SupportExtendedReferenceTypes;

    boolean p_SupportFunctionPointerTypes;

    boolean p_SupportExtendedTypes;

    boolean p_AllowSetASCIIStringClass;

    boolean p_AllowSetStringClass;

    boolean p_AllowDisableNullReferenceCheckOnMemberAccess;

    boolean p_SupportPointerArithmetics;

    boolean p_AllowSetNullIntegerValue;

    int p_NullIntegerValue;

    boolean p_AllowDisableBaseTypesArrayItemsInitialization;

    boolean p_AllowDisableStaticsVarsInitialization;

    boolean p_AllowDisableInstanceVarsInitialization;

    boolean p_AllowIgnorePreviousAssingmentRules;

    boolean p_AllowDisableAddressOfExpressionRequirementOnOutArguments;

    boolean p_AllowFinalizeMethod;

    boolean p_AllowNonNormalVirtualFunctionCallsOnConstructorsBody;

    boolean p_AllowSelectiveVirtualMembers;

    boolean p_AllowNonLimitedGotos;

    boolean p_AllowSetRuntimeTypeInformation;

    boolean p_AllowSetRuntimeReflection;

    boolean p_AllowLimitRuntimeReflectionToModuleOnly;

    boolean p_AllowResumeExceptionModel;

    boolean p_AllowTerminationExceptionModel;

    boolean p_AllowFinallyBlocks;

    boolean p_AllowResumeNext;

    boolean p_AllowMixedExceptionModel;

    boolean p_AllowAnyTypeExceptions;

    boolean p_AllowStackVarsAsExceptions;

    boolean p_AllowRuntimeChecksFragmentedConfiguration;

    String p_SupportedOperators;

    XplNodeList p_GarbageCollector;

    public XplOutputModuleCapabilities() {
        p_IsCaseSensitive = true;
        p_AllowDefaultSafeArrays = false;
        p_AllowDisableArrayLimitsChecks = false;
        p_AllowUseSinglePointAsNamespaceSeparator = false;
        p_AllowUseSimpleMemberAccessAsNamespaceSeparator = false;
        p_AllowMultipleInheritance = false;
        p_UseUniversalObjectBase = true;
        p_AllowSetUniversalObjectBase = false;
        p_AllowDisableUnifiedTypeSystem = false;
        p_AllowEnableIntegerOverflowExceptions = false;
        p_AllowEnableFloatOperationsExceptions = false;
        p_RequireIntegerOverflowExceptions = false;
        p_RequireFloatOperationsExceptions = false;
        p_FullDecimalImplementation = true;
        p_SupportASCIIChar = false;
        p_SupportASCIIString = false;
        p_SupportUnsignedIntegers = true;
        p_SupportExtendedValueTypes = true;
        p_SupportExtendedReferenceTypes = true;
        p_SupportFunctionPointerTypes = true;
        p_SupportExtendedTypes = true;
        p_AllowSetASCIIStringClass = false;
        p_AllowSetStringClass = false;
        p_AllowDisableNullReferenceCheckOnMemberAccess = false;
        p_SupportPointerArithmetics = false;
        p_AllowSetNullIntegerValue = false;
        p_NullIntegerValue = 0;
        p_AllowDisableBaseTypesArrayItemsInitialization = false;
        p_AllowDisableStaticsVarsInitialization = false;
        p_AllowDisableInstanceVarsInitialization = false;
        p_AllowIgnorePreviousAssingmentRules = true;
        p_AllowDisableAddressOfExpressionRequirementOnOutArguments = false;
        p_AllowFinalizeMethod = true;
        p_AllowNonNormalVirtualFunctionCallsOnConstructorsBody = false;
        p_AllowSelectiveVirtualMembers = true;
        p_AllowNonLimitedGotos = false;
        p_AllowSetRuntimeTypeInformation = true;
        p_AllowSetRuntimeReflection = false;
        p_AllowLimitRuntimeReflectionToModuleOnly = false;
        p_AllowResumeExceptionModel = false;
        p_AllowTerminationExceptionModel = true;
        p_AllowFinallyBlocks = true;
        p_AllowResumeNext = true;
        p_AllowMixedExceptionModel = false;
        p_AllowAnyTypeExceptions = false;
        p_AllowStackVarsAsExceptions = true;
        p_AllowRuntimeChecksFragmentedConfiguration = false;
        p_SupportedOperators = "";
        p_GarbageCollector = new XplNodeList();
        p_GarbageCollector.set_Parent(this);
        p_GarbageCollector.set_CheckNodeCallback(this);
    }

    public XplOutputModuleCapabilities(boolean n_IsCaseSensitive, boolean n_AllowDefaultSafeArrays, boolean n_AllowDisableArrayLimitsChecks, boolean n_AllowUseSinglePointAsNamespaceSeparator, boolean n_AllowUseSimpleMemberAccessAsNamespaceSeparator, boolean n_AllowMultipleInheritance, boolean n_UseUniversalObjectBase, boolean n_AllowSetUniversalObjectBase, boolean n_AllowDisableUnifiedTypeSystem, boolean n_AllowEnableIntegerOverflowExceptions, boolean n_AllowEnableFloatOperationsExceptions, boolean n_RequireIntegerOverflowExceptions, boolean n_RequireFloatOperationsExceptions, boolean n_FullDecimalImplementation, boolean n_SupportASCIIChar, boolean n_SupportASCIIString, boolean n_SupportUnsignedIntegers, boolean n_SupportExtendedValueTypes, boolean n_SupportExtendedReferenceTypes, boolean n_SupportFunctionPointerTypes, boolean n_SupportExtendedTypes, boolean n_AllowSetASCIIStringClass, boolean n_AllowSetStringClass, boolean n_AllowDisableNullReferenceCheckOnMemberAccess, boolean n_SupportPointerArithmetics, boolean n_AllowSetNullIntegerValue, boolean n_AllowDisableBaseTypesArrayItemsInitialization, boolean n_AllowDisableStaticsVarsInitialization, boolean n_AllowDisableInstanceVarsInitialization, boolean n_AllowIgnorePreviousAssingmentRules, boolean n_AllowDisableAddressOfExpressionRequirementOnOutArguments, boolean n_AllowFinalizeMethod, boolean n_AllowNonNormalVirtualFunctionCallsOnConstructorsBody, boolean n_AllowSelectiveVirtualMembers, boolean n_AllowNonLimitedGotos, boolean n_AllowSetRuntimeTypeInformation, boolean n_AllowSetRuntimeReflection, boolean n_AllowLimitRuntimeReflectionToModuleOnly, boolean n_AllowResumeExceptionModel, boolean n_AllowTerminationExceptionModel, boolean n_AllowFinallyBlocks, boolean n_AllowResumeNext, boolean n_AllowMixedExceptionModel, boolean n_AllowAnyTypeExceptions, boolean n_AllowStackVarsAsExceptions, boolean n_AllowRuntimeChecksFragmentedConfiguration) {
        p_IsCaseSensitive = true;
        p_AllowDefaultSafeArrays = false;
        p_AllowDisableArrayLimitsChecks = false;
        p_AllowUseSinglePointAsNamespaceSeparator = false;
        p_AllowUseSimpleMemberAccessAsNamespaceSeparator = false;
        p_AllowMultipleInheritance = false;
        p_UseUniversalObjectBase = true;
        p_AllowSetUniversalObjectBase = false;
        p_AllowDisableUnifiedTypeSystem = false;
        p_AllowEnableIntegerOverflowExceptions = false;
        p_AllowEnableFloatOperationsExceptions = false;
        p_RequireIntegerOverflowExceptions = false;
        p_RequireFloatOperationsExceptions = false;
        p_FullDecimalImplementation = true;
        p_SupportASCIIChar = false;
        p_SupportASCIIString = false;
        p_SupportUnsignedIntegers = true;
        p_SupportExtendedValueTypes = true;
        p_SupportExtendedReferenceTypes = true;
        p_SupportFunctionPointerTypes = true;
        p_SupportExtendedTypes = true;
        p_AllowSetASCIIStringClass = false;
        p_AllowSetStringClass = false;
        p_AllowDisableNullReferenceCheckOnMemberAccess = false;
        p_SupportPointerArithmetics = false;
        p_AllowSetNullIntegerValue = false;
        p_NullIntegerValue = 0;
        p_AllowDisableBaseTypesArrayItemsInitialization = false;
        p_AllowDisableStaticsVarsInitialization = false;
        p_AllowDisableInstanceVarsInitialization = false;
        p_AllowIgnorePreviousAssingmentRules = true;
        p_AllowDisableAddressOfExpressionRequirementOnOutArguments = false;
        p_AllowFinalizeMethod = true;
        p_AllowNonNormalVirtualFunctionCallsOnConstructorsBody = false;
        p_AllowSelectiveVirtualMembers = true;
        p_AllowNonLimitedGotos = false;
        p_AllowSetRuntimeTypeInformation = true;
        p_AllowSetRuntimeReflection = false;
        p_AllowLimitRuntimeReflectionToModuleOnly = false;
        p_AllowResumeExceptionModel = false;
        p_AllowTerminationExceptionModel = true;
        p_AllowFinallyBlocks = true;
        p_AllowResumeNext = true;
        p_AllowMixedExceptionModel = false;
        p_AllowAnyTypeExceptions = false;
        p_AllowStackVarsAsExceptions = true;
        p_AllowRuntimeChecksFragmentedConfiguration = false;
        p_SupportedOperators = "";
        set_IsCaseSensitive(n_IsCaseSensitive);
        set_AllowDefaultSafeArrays(n_AllowDefaultSafeArrays);
        set_AllowDisableArrayLimitsChecks(n_AllowDisableArrayLimitsChecks);
        set_AllowUseSinglePointAsNamespaceSeparator(n_AllowUseSinglePointAsNamespaceSeparator);
        set_AllowUseSimpleMemberAccessAsNamespaceSeparator(n_AllowUseSimpleMemberAccessAsNamespaceSeparator);
        set_AllowMultipleInheritance(n_AllowMultipleInheritance);
        set_UseUniversalObjectBase(n_UseUniversalObjectBase);
        set_AllowSetUniversalObjectBase(n_AllowSetUniversalObjectBase);
        set_AllowDisableUnifiedTypeSystem(n_AllowDisableUnifiedTypeSystem);
        set_AllowEnableIntegerOverflowExceptions(n_AllowEnableIntegerOverflowExceptions);
        set_AllowEnableFloatOperationsExceptions(n_AllowEnableFloatOperationsExceptions);
        set_RequireIntegerOverflowExceptions(n_RequireIntegerOverflowExceptions);
        set_RequireFloatOperationsExceptions(n_RequireFloatOperationsExceptions);
        set_FullDecimalImplementation(n_FullDecimalImplementation);
        set_SupportASCIIChar(n_SupportASCIIChar);
        set_SupportASCIIString(n_SupportASCIIString);
        set_SupportUnsignedIntegers(n_SupportUnsignedIntegers);
        set_SupportExtendedValueTypes(n_SupportExtendedValueTypes);
        set_SupportExtendedReferenceTypes(n_SupportExtendedReferenceTypes);
        set_SupportFunctionPointerTypes(n_SupportFunctionPointerTypes);
        set_SupportExtendedTypes(n_SupportExtendedTypes);
        set_AllowSetASCIIStringClass(n_AllowSetASCIIStringClass);
        set_AllowSetStringClass(n_AllowSetStringClass);
        set_AllowDisableNullReferenceCheckOnMemberAccess(n_AllowDisableNullReferenceCheckOnMemberAccess);
        set_SupportPointerArithmetics(n_SupportPointerArithmetics);
        set_AllowSetNullIntegerValue(n_AllowSetNullIntegerValue);
        set_AllowDisableBaseTypesArrayItemsInitialization(n_AllowDisableBaseTypesArrayItemsInitialization);
        set_AllowDisableStaticsVarsInitialization(n_AllowDisableStaticsVarsInitialization);
        set_AllowDisableInstanceVarsInitialization(n_AllowDisableInstanceVarsInitialization);
        set_AllowIgnorePreviousAssingmentRules(n_AllowIgnorePreviousAssingmentRules);
        set_AllowDisableAddressOfExpressionRequirementOnOutArguments(n_AllowDisableAddressOfExpressionRequirementOnOutArguments);
        set_AllowFinalizeMethod(n_AllowFinalizeMethod);
        set_AllowNonNormalVirtualFunctionCallsOnConstructorsBody(n_AllowNonNormalVirtualFunctionCallsOnConstructorsBody);
        set_AllowSelectiveVirtualMembers(n_AllowSelectiveVirtualMembers);
        set_AllowNonLimitedGotos(n_AllowNonLimitedGotos);
        set_AllowSetRuntimeTypeInformation(n_AllowSetRuntimeTypeInformation);
        set_AllowSetRuntimeReflection(n_AllowSetRuntimeReflection);
        set_AllowLimitRuntimeReflectionToModuleOnly(n_AllowLimitRuntimeReflectionToModuleOnly);
        set_AllowResumeExceptionModel(n_AllowResumeExceptionModel);
        set_AllowTerminationExceptionModel(n_AllowTerminationExceptionModel);
        set_AllowFinallyBlocks(n_AllowFinallyBlocks);
        set_AllowResumeNext(n_AllowResumeNext);
        set_AllowMixedExceptionModel(n_AllowMixedExceptionModel);
        set_AllowAnyTypeExceptions(n_AllowAnyTypeExceptions);
        set_AllowStackVarsAsExceptions(n_AllowStackVarsAsExceptions);
        set_AllowRuntimeChecksFragmentedConfiguration(n_AllowRuntimeChecksFragmentedConfiguration);
        p_GarbageCollector = new XplNodeList();
        p_GarbageCollector.set_Parent(this);
        p_GarbageCollector.set_CheckNodeCallback(this);
    }

    public XplOutputModuleCapabilities(boolean n_IsCaseSensitive, boolean n_AllowDefaultSafeArrays, boolean n_AllowDisableArrayLimitsChecks, boolean n_AllowUseSinglePointAsNamespaceSeparator, boolean n_AllowUseSimpleMemberAccessAsNamespaceSeparator, boolean n_AllowMultipleInheritance, boolean n_UseUniversalObjectBase, boolean n_AllowSetUniversalObjectBase, boolean n_AllowDisableUnifiedTypeSystem, boolean n_AllowEnableIntegerOverflowExceptions, boolean n_AllowEnableFloatOperationsExceptions, boolean n_RequireIntegerOverflowExceptions, boolean n_RequireFloatOperationsExceptions, boolean n_FullDecimalImplementation, boolean n_SupportASCIIChar, boolean n_SupportASCIIString, boolean n_SupportUnsignedIntegers, boolean n_SupportExtendedValueTypes, boolean n_SupportExtendedReferenceTypes, boolean n_SupportFunctionPointerTypes, boolean n_SupportExtendedTypes, boolean n_AllowSetASCIIStringClass, boolean n_AllowSetStringClass, boolean n_AllowDisableNullReferenceCheckOnMemberAccess, boolean n_SupportPointerArithmetics, boolean n_AllowSetNullIntegerValue, int n_NullIntegerValue, boolean n_AllowDisableBaseTypesArrayItemsInitialization, boolean n_AllowDisableStaticsVarsInitialization, boolean n_AllowDisableInstanceVarsInitialization, boolean n_AllowIgnorePreviousAssingmentRules, boolean n_AllowDisableAddressOfExpressionRequirementOnOutArguments, boolean n_AllowFinalizeMethod, boolean n_AllowNonNormalVirtualFunctionCallsOnConstructorsBody, boolean n_AllowSelectiveVirtualMembers, boolean n_AllowNonLimitedGotos, boolean n_AllowSetRuntimeTypeInformation, boolean n_AllowSetRuntimeReflection, boolean n_AllowLimitRuntimeReflectionToModuleOnly, boolean n_AllowResumeExceptionModel, boolean n_AllowTerminationExceptionModel, boolean n_AllowFinallyBlocks, boolean n_AllowResumeNext, boolean n_AllowMixedExceptionModel, boolean n_AllowAnyTypeExceptions, boolean n_AllowStackVarsAsExceptions, boolean n_AllowRuntimeChecksFragmentedConfiguration, String n_SupportedOperators) {
        set_IsCaseSensitive(n_IsCaseSensitive);
        set_AllowDefaultSafeArrays(n_AllowDefaultSafeArrays);
        set_AllowDisableArrayLimitsChecks(n_AllowDisableArrayLimitsChecks);
        set_AllowUseSinglePointAsNamespaceSeparator(n_AllowUseSinglePointAsNamespaceSeparator);
        set_AllowUseSimpleMemberAccessAsNamespaceSeparator(n_AllowUseSimpleMemberAccessAsNamespaceSeparator);
        set_AllowMultipleInheritance(n_AllowMultipleInheritance);
        set_UseUniversalObjectBase(n_UseUniversalObjectBase);
        set_AllowSetUniversalObjectBase(n_AllowSetUniversalObjectBase);
        set_AllowDisableUnifiedTypeSystem(n_AllowDisableUnifiedTypeSystem);
        set_AllowEnableIntegerOverflowExceptions(n_AllowEnableIntegerOverflowExceptions);
        set_AllowEnableFloatOperationsExceptions(n_AllowEnableFloatOperationsExceptions);
        set_RequireIntegerOverflowExceptions(n_RequireIntegerOverflowExceptions);
        set_RequireFloatOperationsExceptions(n_RequireFloatOperationsExceptions);
        set_FullDecimalImplementation(n_FullDecimalImplementation);
        set_SupportASCIIChar(n_SupportASCIIChar);
        set_SupportASCIIString(n_SupportASCIIString);
        set_SupportUnsignedIntegers(n_SupportUnsignedIntegers);
        set_SupportExtendedValueTypes(n_SupportExtendedValueTypes);
        set_SupportExtendedReferenceTypes(n_SupportExtendedReferenceTypes);
        set_SupportFunctionPointerTypes(n_SupportFunctionPointerTypes);
        set_SupportExtendedTypes(n_SupportExtendedTypes);
        set_AllowSetASCIIStringClass(n_AllowSetASCIIStringClass);
        set_AllowSetStringClass(n_AllowSetStringClass);
        set_AllowDisableNullReferenceCheckOnMemberAccess(n_AllowDisableNullReferenceCheckOnMemberAccess);
        set_SupportPointerArithmetics(n_SupportPointerArithmetics);
        set_AllowSetNullIntegerValue(n_AllowSetNullIntegerValue);
        set_NullIntegerValue(n_NullIntegerValue);
        set_AllowDisableBaseTypesArrayItemsInitialization(n_AllowDisableBaseTypesArrayItemsInitialization);
        set_AllowDisableStaticsVarsInitialization(n_AllowDisableStaticsVarsInitialization);
        set_AllowDisableInstanceVarsInitialization(n_AllowDisableInstanceVarsInitialization);
        set_AllowIgnorePreviousAssingmentRules(n_AllowIgnorePreviousAssingmentRules);
        set_AllowDisableAddressOfExpressionRequirementOnOutArguments(n_AllowDisableAddressOfExpressionRequirementOnOutArguments);
        set_AllowFinalizeMethod(n_AllowFinalizeMethod);
        set_AllowNonNormalVirtualFunctionCallsOnConstructorsBody(n_AllowNonNormalVirtualFunctionCallsOnConstructorsBody);
        set_AllowSelectiveVirtualMembers(n_AllowSelectiveVirtualMembers);
        set_AllowNonLimitedGotos(n_AllowNonLimitedGotos);
        set_AllowSetRuntimeTypeInformation(n_AllowSetRuntimeTypeInformation);
        set_AllowSetRuntimeReflection(n_AllowSetRuntimeReflection);
        set_AllowLimitRuntimeReflectionToModuleOnly(n_AllowLimitRuntimeReflectionToModuleOnly);
        set_AllowResumeExceptionModel(n_AllowResumeExceptionModel);
        set_AllowTerminationExceptionModel(n_AllowTerminationExceptionModel);
        set_AllowFinallyBlocks(n_AllowFinallyBlocks);
        set_AllowResumeNext(n_AllowResumeNext);
        set_AllowMixedExceptionModel(n_AllowMixedExceptionModel);
        set_AllowAnyTypeExceptions(n_AllowAnyTypeExceptions);
        set_AllowStackVarsAsExceptions(n_AllowStackVarsAsExceptions);
        set_AllowRuntimeChecksFragmentedConfiguration(n_AllowRuntimeChecksFragmentedConfiguration);
        set_SupportedOperators(n_SupportedOperators);
        p_GarbageCollector = new XplNodeList();
        p_GarbageCollector.set_Parent(this);
        p_GarbageCollector.set_CheckNodeCallback(this);
    }

    public XplOutputModuleCapabilities(XplNodeList n_GarbageCollector) {
        p_IsCaseSensitive = true;
        p_AllowDefaultSafeArrays = false;
        p_AllowDisableArrayLimitsChecks = false;
        p_AllowUseSinglePointAsNamespaceSeparator = false;
        p_AllowUseSimpleMemberAccessAsNamespaceSeparator = false;
        p_AllowMultipleInheritance = false;
        p_UseUniversalObjectBase = true;
        p_AllowSetUniversalObjectBase = false;
        p_AllowDisableUnifiedTypeSystem = false;
        p_AllowEnableIntegerOverflowExceptions = false;
        p_AllowEnableFloatOperationsExceptions = false;
        p_RequireIntegerOverflowExceptions = false;
        p_RequireFloatOperationsExceptions = false;
        p_FullDecimalImplementation = true;
        p_SupportASCIIChar = false;
        p_SupportASCIIString = false;
        p_SupportUnsignedIntegers = true;
        p_SupportExtendedValueTypes = true;
        p_SupportExtendedReferenceTypes = true;
        p_SupportFunctionPointerTypes = true;
        p_SupportExtendedTypes = true;
        p_AllowSetASCIIStringClass = false;
        p_AllowSetStringClass = false;
        p_AllowDisableNullReferenceCheckOnMemberAccess = false;
        p_SupportPointerArithmetics = false;
        p_AllowSetNullIntegerValue = false;
        p_NullIntegerValue = 0;
        p_AllowDisableBaseTypesArrayItemsInitialization = false;
        p_AllowDisableStaticsVarsInitialization = false;
        p_AllowDisableInstanceVarsInitialization = false;
        p_AllowIgnorePreviousAssingmentRules = true;
        p_AllowDisableAddressOfExpressionRequirementOnOutArguments = false;
        p_AllowFinalizeMethod = true;
        p_AllowNonNormalVirtualFunctionCallsOnConstructorsBody = false;
        p_AllowSelectiveVirtualMembers = true;
        p_AllowNonLimitedGotos = false;
        p_AllowSetRuntimeTypeInformation = true;
        p_AllowSetRuntimeReflection = false;
        p_AllowLimitRuntimeReflectionToModuleOnly = false;
        p_AllowResumeExceptionModel = false;
        p_AllowTerminationExceptionModel = true;
        p_AllowFinallyBlocks = true;
        p_AllowResumeNext = true;
        p_AllowMixedExceptionModel = false;
        p_AllowAnyTypeExceptions = false;
        p_AllowStackVarsAsExceptions = true;
        p_AllowRuntimeChecksFragmentedConfiguration = false;
        p_SupportedOperators = "";
        p_GarbageCollector = new XplNodeList();
        p_GarbageCollector.set_Parent(this);
        p_GarbageCollector.set_CheckNodeCallback(this);
        if (n_GarbageCollector != null) for (XplNode node = n_GarbageCollector.FirstNode(); node != null; node = n_GarbageCollector.NextNode()) {
            p_GarbageCollector.InsertAtEnd(node);
        }
    }

    public XplOutputModuleCapabilities(boolean n_IsCaseSensitive, boolean n_AllowDefaultSafeArrays, boolean n_AllowDisableArrayLimitsChecks, boolean n_AllowUseSinglePointAsNamespaceSeparator, boolean n_AllowUseSimpleMemberAccessAsNamespaceSeparator, boolean n_AllowMultipleInheritance, boolean n_UseUniversalObjectBase, boolean n_AllowSetUniversalObjectBase, boolean n_AllowDisableUnifiedTypeSystem, boolean n_AllowEnableIntegerOverflowExceptions, boolean n_AllowEnableFloatOperationsExceptions, boolean n_RequireIntegerOverflowExceptions, boolean n_RequireFloatOperationsExceptions, boolean n_FullDecimalImplementation, boolean n_SupportASCIIChar, boolean n_SupportASCIIString, boolean n_SupportUnsignedIntegers, boolean n_SupportExtendedValueTypes, boolean n_SupportExtendedReferenceTypes, boolean n_SupportFunctionPointerTypes, boolean n_SupportExtendedTypes, boolean n_AllowSetASCIIStringClass, boolean n_AllowSetStringClass, boolean n_AllowDisableNullReferenceCheckOnMemberAccess, boolean n_SupportPointerArithmetics, boolean n_AllowSetNullIntegerValue, boolean n_AllowDisableBaseTypesArrayItemsInitialization, boolean n_AllowDisableStaticsVarsInitialization, boolean n_AllowDisableInstanceVarsInitialization, boolean n_AllowIgnorePreviousAssingmentRules, boolean n_AllowDisableAddressOfExpressionRequirementOnOutArguments, boolean n_AllowFinalizeMethod, boolean n_AllowNonNormalVirtualFunctionCallsOnConstructorsBody, boolean n_AllowSelectiveVirtualMembers, boolean n_AllowNonLimitedGotos, boolean n_AllowSetRuntimeTypeInformation, boolean n_AllowSetRuntimeReflection, boolean n_AllowLimitRuntimeReflectionToModuleOnly, boolean n_AllowResumeExceptionModel, boolean n_AllowTerminationExceptionModel, boolean n_AllowFinallyBlocks, boolean n_AllowResumeNext, boolean n_AllowMixedExceptionModel, boolean n_AllowAnyTypeExceptions, boolean n_AllowStackVarsAsExceptions, boolean n_AllowRuntimeChecksFragmentedConfiguration, XplNodeList n_GarbageCollector) {
        p_IsCaseSensitive = true;
        p_AllowDefaultSafeArrays = false;
        p_AllowDisableArrayLimitsChecks = false;
        p_AllowUseSinglePointAsNamespaceSeparator = false;
        p_AllowUseSimpleMemberAccessAsNamespaceSeparator = false;
        p_AllowMultipleInheritance = false;
        p_UseUniversalObjectBase = true;
        p_AllowSetUniversalObjectBase = false;
        p_AllowDisableUnifiedTypeSystem = false;
        p_AllowEnableIntegerOverflowExceptions = false;
        p_AllowEnableFloatOperationsExceptions = false;
        p_RequireIntegerOverflowExceptions = false;
        p_RequireFloatOperationsExceptions = false;
        p_FullDecimalImplementation = true;
        p_SupportASCIIChar = false;
        p_SupportASCIIString = false;
        p_SupportUnsignedIntegers = true;
        p_SupportExtendedValueTypes = true;
        p_SupportExtendedReferenceTypes = true;
        p_SupportFunctionPointerTypes = true;
        p_SupportExtendedTypes = true;
        p_AllowSetASCIIStringClass = false;
        p_AllowSetStringClass = false;
        p_AllowDisableNullReferenceCheckOnMemberAccess = false;
        p_SupportPointerArithmetics = false;
        p_AllowSetNullIntegerValue = false;
        p_NullIntegerValue = 0;
        p_AllowDisableBaseTypesArrayItemsInitialization = false;
        p_AllowDisableStaticsVarsInitialization = false;
        p_AllowDisableInstanceVarsInitialization = false;
        p_AllowIgnorePreviousAssingmentRules = true;
        p_AllowDisableAddressOfExpressionRequirementOnOutArguments = false;
        p_AllowFinalizeMethod = true;
        p_AllowNonNormalVirtualFunctionCallsOnConstructorsBody = false;
        p_AllowSelectiveVirtualMembers = true;
        p_AllowNonLimitedGotos = false;
        p_AllowSetRuntimeTypeInformation = true;
        p_AllowSetRuntimeReflection = false;
        p_AllowLimitRuntimeReflectionToModuleOnly = false;
        p_AllowResumeExceptionModel = false;
        p_AllowTerminationExceptionModel = true;
        p_AllowFinallyBlocks = true;
        p_AllowResumeNext = true;
        p_AllowMixedExceptionModel = false;
        p_AllowAnyTypeExceptions = false;
        p_AllowStackVarsAsExceptions = true;
        p_AllowRuntimeChecksFragmentedConfiguration = false;
        p_SupportedOperators = "";
        set_IsCaseSensitive(n_IsCaseSensitive);
        set_AllowDefaultSafeArrays(n_AllowDefaultSafeArrays);
        set_AllowDisableArrayLimitsChecks(n_AllowDisableArrayLimitsChecks);
        set_AllowUseSinglePointAsNamespaceSeparator(n_AllowUseSinglePointAsNamespaceSeparator);
        set_AllowUseSimpleMemberAccessAsNamespaceSeparator(n_AllowUseSimpleMemberAccessAsNamespaceSeparator);
        set_AllowMultipleInheritance(n_AllowMultipleInheritance);
        set_UseUniversalObjectBase(n_UseUniversalObjectBase);
        set_AllowSetUniversalObjectBase(n_AllowSetUniversalObjectBase);
        set_AllowDisableUnifiedTypeSystem(n_AllowDisableUnifiedTypeSystem);
        set_AllowEnableIntegerOverflowExceptions(n_AllowEnableIntegerOverflowExceptions);
        set_AllowEnableFloatOperationsExceptions(n_AllowEnableFloatOperationsExceptions);
        set_RequireIntegerOverflowExceptions(n_RequireIntegerOverflowExceptions);
        set_RequireFloatOperationsExceptions(n_RequireFloatOperationsExceptions);
        set_FullDecimalImplementation(n_FullDecimalImplementation);
        set_SupportASCIIChar(n_SupportASCIIChar);
        set_SupportASCIIString(n_SupportASCIIString);
        set_SupportUnsignedIntegers(n_SupportUnsignedIntegers);
        set_SupportExtendedValueTypes(n_SupportExtendedValueTypes);
        set_SupportExtendedReferenceTypes(n_SupportExtendedReferenceTypes);
        set_SupportFunctionPointerTypes(n_SupportFunctionPointerTypes);
        set_SupportExtendedTypes(n_SupportExtendedTypes);
        set_AllowSetASCIIStringClass(n_AllowSetASCIIStringClass);
        set_AllowSetStringClass(n_AllowSetStringClass);
        set_AllowDisableNullReferenceCheckOnMemberAccess(n_AllowDisableNullReferenceCheckOnMemberAccess);
        set_SupportPointerArithmetics(n_SupportPointerArithmetics);
        set_AllowSetNullIntegerValue(n_AllowSetNullIntegerValue);
        set_AllowDisableBaseTypesArrayItemsInitialization(n_AllowDisableBaseTypesArrayItemsInitialization);
        set_AllowDisableStaticsVarsInitialization(n_AllowDisableStaticsVarsInitialization);
        set_AllowDisableInstanceVarsInitialization(n_AllowDisableInstanceVarsInitialization);
        set_AllowIgnorePreviousAssingmentRules(n_AllowIgnorePreviousAssingmentRules);
        set_AllowDisableAddressOfExpressionRequirementOnOutArguments(n_AllowDisableAddressOfExpressionRequirementOnOutArguments);
        set_AllowFinalizeMethod(n_AllowFinalizeMethod);
        set_AllowNonNormalVirtualFunctionCallsOnConstructorsBody(n_AllowNonNormalVirtualFunctionCallsOnConstructorsBody);
        set_AllowSelectiveVirtualMembers(n_AllowSelectiveVirtualMembers);
        set_AllowNonLimitedGotos(n_AllowNonLimitedGotos);
        set_AllowSetRuntimeTypeInformation(n_AllowSetRuntimeTypeInformation);
        set_AllowSetRuntimeReflection(n_AllowSetRuntimeReflection);
        set_AllowLimitRuntimeReflectionToModuleOnly(n_AllowLimitRuntimeReflectionToModuleOnly);
        set_AllowResumeExceptionModel(n_AllowResumeExceptionModel);
        set_AllowTerminationExceptionModel(n_AllowTerminationExceptionModel);
        set_AllowFinallyBlocks(n_AllowFinallyBlocks);
        set_AllowResumeNext(n_AllowResumeNext);
        set_AllowMixedExceptionModel(n_AllowMixedExceptionModel);
        set_AllowAnyTypeExceptions(n_AllowAnyTypeExceptions);
        set_AllowStackVarsAsExceptions(n_AllowStackVarsAsExceptions);
        set_AllowRuntimeChecksFragmentedConfiguration(n_AllowRuntimeChecksFragmentedConfiguration);
        p_GarbageCollector = new XplNodeList();
        p_GarbageCollector.set_Parent(this);
        p_GarbageCollector.set_CheckNodeCallback(this);
        if (n_GarbageCollector != null) for (XplNode node = n_GarbageCollector.FirstNode(); node != null; node = n_GarbageCollector.NextNode()) {
            p_GarbageCollector.InsertAtEnd(node);
        }
    }

    public XplOutputModuleCapabilities(boolean n_IsCaseSensitive, boolean n_AllowDefaultSafeArrays, boolean n_AllowDisableArrayLimitsChecks, boolean n_AllowUseSinglePointAsNamespaceSeparator, boolean n_AllowUseSimpleMemberAccessAsNamespaceSeparator, boolean n_AllowMultipleInheritance, boolean n_UseUniversalObjectBase, boolean n_AllowSetUniversalObjectBase, boolean n_AllowDisableUnifiedTypeSystem, boolean n_AllowEnableIntegerOverflowExceptions, boolean n_AllowEnableFloatOperationsExceptions, boolean n_RequireIntegerOverflowExceptions, boolean n_RequireFloatOperationsExceptions, boolean n_FullDecimalImplementation, boolean n_SupportASCIIChar, boolean n_SupportASCIIString, boolean n_SupportUnsignedIntegers, boolean n_SupportExtendedValueTypes, boolean n_SupportExtendedReferenceTypes, boolean n_SupportFunctionPointerTypes, boolean n_SupportExtendedTypes, boolean n_AllowSetASCIIStringClass, boolean n_AllowSetStringClass, boolean n_AllowDisableNullReferenceCheckOnMemberAccess, boolean n_SupportPointerArithmetics, boolean n_AllowSetNullIntegerValue, int n_NullIntegerValue, boolean n_AllowDisableBaseTypesArrayItemsInitialization, boolean n_AllowDisableStaticsVarsInitialization, boolean n_AllowDisableInstanceVarsInitialization, boolean n_AllowIgnorePreviousAssingmentRules, boolean n_AllowDisableAddressOfExpressionRequirementOnOutArguments, boolean n_AllowFinalizeMethod, boolean n_AllowNonNormalVirtualFunctionCallsOnConstructorsBody, boolean n_AllowSelectiveVirtualMembers, boolean n_AllowNonLimitedGotos, boolean n_AllowSetRuntimeTypeInformation, boolean n_AllowSetRuntimeReflection, boolean n_AllowLimitRuntimeReflectionToModuleOnly, boolean n_AllowResumeExceptionModel, boolean n_AllowTerminationExceptionModel, boolean n_AllowFinallyBlocks, boolean n_AllowResumeNext, boolean n_AllowMixedExceptionModel, boolean n_AllowAnyTypeExceptions, boolean n_AllowStackVarsAsExceptions, boolean n_AllowRuntimeChecksFragmentedConfiguration, String n_SupportedOperators, XplNodeList n_GarbageCollector) {
        set_IsCaseSensitive(n_IsCaseSensitive);
        set_AllowDefaultSafeArrays(n_AllowDefaultSafeArrays);
        set_AllowDisableArrayLimitsChecks(n_AllowDisableArrayLimitsChecks);
        set_AllowUseSinglePointAsNamespaceSeparator(n_AllowUseSinglePointAsNamespaceSeparator);
        set_AllowUseSimpleMemberAccessAsNamespaceSeparator(n_AllowUseSimpleMemberAccessAsNamespaceSeparator);
        set_AllowMultipleInheritance(n_AllowMultipleInheritance);
        set_UseUniversalObjectBase(n_UseUniversalObjectBase);
        set_AllowSetUniversalObjectBase(n_AllowSetUniversalObjectBase);
        set_AllowDisableUnifiedTypeSystem(n_AllowDisableUnifiedTypeSystem);
        set_AllowEnableIntegerOverflowExceptions(n_AllowEnableIntegerOverflowExceptions);
        set_AllowEnableFloatOperationsExceptions(n_AllowEnableFloatOperationsExceptions);
        set_RequireIntegerOverflowExceptions(n_RequireIntegerOverflowExceptions);
        set_RequireFloatOperationsExceptions(n_RequireFloatOperationsExceptions);
        set_FullDecimalImplementation(n_FullDecimalImplementation);
        set_SupportASCIIChar(n_SupportASCIIChar);
        set_SupportASCIIString(n_SupportASCIIString);
        set_SupportUnsignedIntegers(n_SupportUnsignedIntegers);
        set_SupportExtendedValueTypes(n_SupportExtendedValueTypes);
        set_SupportExtendedReferenceTypes(n_SupportExtendedReferenceTypes);
        set_SupportFunctionPointerTypes(n_SupportFunctionPointerTypes);
        set_SupportExtendedTypes(n_SupportExtendedTypes);
        set_AllowSetASCIIStringClass(n_AllowSetASCIIStringClass);
        set_AllowSetStringClass(n_AllowSetStringClass);
        set_AllowDisableNullReferenceCheckOnMemberAccess(n_AllowDisableNullReferenceCheckOnMemberAccess);
        set_SupportPointerArithmetics(n_SupportPointerArithmetics);
        set_AllowSetNullIntegerValue(n_AllowSetNullIntegerValue);
        set_NullIntegerValue(n_NullIntegerValue);
        set_AllowDisableBaseTypesArrayItemsInitialization(n_AllowDisableBaseTypesArrayItemsInitialization);
        set_AllowDisableStaticsVarsInitialization(n_AllowDisableStaticsVarsInitialization);
        set_AllowDisableInstanceVarsInitialization(n_AllowDisableInstanceVarsInitialization);
        set_AllowIgnorePreviousAssingmentRules(n_AllowIgnorePreviousAssingmentRules);
        set_AllowDisableAddressOfExpressionRequirementOnOutArguments(n_AllowDisableAddressOfExpressionRequirementOnOutArguments);
        set_AllowFinalizeMethod(n_AllowFinalizeMethod);
        set_AllowNonNormalVirtualFunctionCallsOnConstructorsBody(n_AllowNonNormalVirtualFunctionCallsOnConstructorsBody);
        set_AllowSelectiveVirtualMembers(n_AllowSelectiveVirtualMembers);
        set_AllowNonLimitedGotos(n_AllowNonLimitedGotos);
        set_AllowSetRuntimeTypeInformation(n_AllowSetRuntimeTypeInformation);
        set_AllowSetRuntimeReflection(n_AllowSetRuntimeReflection);
        set_AllowLimitRuntimeReflectionToModuleOnly(n_AllowLimitRuntimeReflectionToModuleOnly);
        set_AllowResumeExceptionModel(n_AllowResumeExceptionModel);
        set_AllowTerminationExceptionModel(n_AllowTerminationExceptionModel);
        set_AllowFinallyBlocks(n_AllowFinallyBlocks);
        set_AllowResumeNext(n_AllowResumeNext);
        set_AllowMixedExceptionModel(n_AllowMixedExceptionModel);
        set_AllowAnyTypeExceptions(n_AllowAnyTypeExceptions);
        set_AllowStackVarsAsExceptions(n_AllowStackVarsAsExceptions);
        set_AllowRuntimeChecksFragmentedConfiguration(n_AllowRuntimeChecksFragmentedConfiguration);
        set_SupportedOperators(n_SupportedOperators);
        p_GarbageCollector = new XplNodeList();
        p_GarbageCollector.set_Parent(this);
        p_GarbageCollector.set_CheckNodeCallback(this);
        if (n_GarbageCollector != null) for (XplNode node = n_GarbageCollector.FirstNode(); node != null; node = n_GarbageCollector.NextNode()) {
            p_GarbageCollector.InsertAtEnd(node);
        }
    }

    public XplNode Clone() {
        XplOutputModuleCapabilities copy = new XplOutputModuleCapabilities();
        copy.set_IsCaseSensitive(this.p_IsCaseSensitive);
        copy.set_AllowDefaultSafeArrays(this.p_AllowDefaultSafeArrays);
        copy.set_AllowDisableArrayLimitsChecks(this.p_AllowDisableArrayLimitsChecks);
        copy.set_AllowUseSinglePointAsNamespaceSeparator(this.p_AllowUseSinglePointAsNamespaceSeparator);
        copy.set_AllowUseSimpleMemberAccessAsNamespaceSeparator(this.p_AllowUseSimpleMemberAccessAsNamespaceSeparator);
        copy.set_AllowMultipleInheritance(this.p_AllowMultipleInheritance);
        copy.set_UseUniversalObjectBase(this.p_UseUniversalObjectBase);
        copy.set_AllowSetUniversalObjectBase(this.p_AllowSetUniversalObjectBase);
        copy.set_AllowDisableUnifiedTypeSystem(this.p_AllowDisableUnifiedTypeSystem);
        copy.set_AllowEnableIntegerOverflowExceptions(this.p_AllowEnableIntegerOverflowExceptions);
        copy.set_AllowEnableFloatOperationsExceptions(this.p_AllowEnableFloatOperationsExceptions);
        copy.set_RequireIntegerOverflowExceptions(this.p_RequireIntegerOverflowExceptions);
        copy.set_RequireFloatOperationsExceptions(this.p_RequireFloatOperationsExceptions);
        copy.set_FullDecimalImplementation(this.p_FullDecimalImplementation);
        copy.set_SupportASCIIChar(this.p_SupportASCIIChar);
        copy.set_SupportASCIIString(this.p_SupportASCIIString);
        copy.set_SupportUnsignedIntegers(this.p_SupportUnsignedIntegers);
        copy.set_SupportExtendedValueTypes(this.p_SupportExtendedValueTypes);
        copy.set_SupportExtendedReferenceTypes(this.p_SupportExtendedReferenceTypes);
        copy.set_SupportFunctionPointerTypes(this.p_SupportFunctionPointerTypes);
        copy.set_SupportExtendedTypes(this.p_SupportExtendedTypes);
        copy.set_AllowSetASCIIStringClass(this.p_AllowSetASCIIStringClass);
        copy.set_AllowSetStringClass(this.p_AllowSetStringClass);
        copy.set_AllowDisableNullReferenceCheckOnMemberAccess(this.p_AllowDisableNullReferenceCheckOnMemberAccess);
        copy.set_SupportPointerArithmetics(this.p_SupportPointerArithmetics);
        copy.set_AllowSetNullIntegerValue(this.p_AllowSetNullIntegerValue);
        copy.set_NullIntegerValue(this.p_NullIntegerValue);
        copy.set_AllowDisableBaseTypesArrayItemsInitialization(this.p_AllowDisableBaseTypesArrayItemsInitialization);
        copy.set_AllowDisableStaticsVarsInitialization(this.p_AllowDisableStaticsVarsInitialization);
        copy.set_AllowDisableInstanceVarsInitialization(this.p_AllowDisableInstanceVarsInitialization);
        copy.set_AllowIgnorePreviousAssingmentRules(this.p_AllowIgnorePreviousAssingmentRules);
        copy.set_AllowDisableAddressOfExpressionRequirementOnOutArguments(this.p_AllowDisableAddressOfExpressionRequirementOnOutArguments);
        copy.set_AllowFinalizeMethod(this.p_AllowFinalizeMethod);
        copy.set_AllowNonNormalVirtualFunctionCallsOnConstructorsBody(this.p_AllowNonNormalVirtualFunctionCallsOnConstructorsBody);
        copy.set_AllowSelectiveVirtualMembers(this.p_AllowSelectiveVirtualMembers);
        copy.set_AllowNonLimitedGotos(this.p_AllowNonLimitedGotos);
        copy.set_AllowSetRuntimeTypeInformation(this.p_AllowSetRuntimeTypeInformation);
        copy.set_AllowSetRuntimeReflection(this.p_AllowSetRuntimeReflection);
        copy.set_AllowLimitRuntimeReflectionToModuleOnly(this.p_AllowLimitRuntimeReflectionToModuleOnly);
        copy.set_AllowResumeExceptionModel(this.p_AllowResumeExceptionModel);
        copy.set_AllowTerminationExceptionModel(this.p_AllowTerminationExceptionModel);
        copy.set_AllowFinallyBlocks(this.p_AllowFinallyBlocks);
        copy.set_AllowResumeNext(this.p_AllowResumeNext);
        copy.set_AllowMixedExceptionModel(this.p_AllowMixedExceptionModel);
        copy.set_AllowAnyTypeExceptions(this.p_AllowAnyTypeExceptions);
        copy.set_AllowStackVarsAsExceptions(this.p_AllowStackVarsAsExceptions);
        copy.set_AllowRuntimeChecksFragmentedConfiguration(this.p_AllowRuntimeChecksFragmentedConfiguration);
        copy.set_SupportedOperators(this.p_SupportedOperators);
        for (XplNode node = p_GarbageCollector.FirstNode(); node != null; node = p_GarbageCollector.NextNode()) {
            copy.get_GarbageCollector().InsertAtEnd(node.Clone());
        }
        copy.set_Name(this.get_Name());
        return (XplNode) copy;
    }

    public int get_TypeName() {
        return CodeDOMTypes.XplOutputModuleCapabilities;
    }

    public boolean Write(XplWriter writer) throws IOException, CodeDOM_Exception {
        boolean result = true;
        writer.WriteStartElement(this.get_Name());
        if (p_IsCaseSensitive != true) writer.WriteAttributeString("IsCaseSensitive", CodeDOM_Utils.Att_ToString(p_IsCaseSensitive));
        if (p_AllowDefaultSafeArrays != false) writer.WriteAttributeString("AllowDefaultSafeArrays", CodeDOM_Utils.Att_ToString(p_AllowDefaultSafeArrays));
        if (p_AllowDisableArrayLimitsChecks != false) writer.WriteAttributeString("AllowDisableArrayLimitsChecks", CodeDOM_Utils.Att_ToString(p_AllowDisableArrayLimitsChecks));
        if (p_AllowUseSinglePointAsNamespaceSeparator != false) writer.WriteAttributeString("AllowUseSinglePointAsNamespaceSeparator", CodeDOM_Utils.Att_ToString(p_AllowUseSinglePointAsNamespaceSeparator));
        if (p_AllowUseSimpleMemberAccessAsNamespaceSeparator != false) writer.WriteAttributeString("AllowUseSimpleMemberAccessAsNamespaceSeparator", CodeDOM_Utils.Att_ToString(p_AllowUseSimpleMemberAccessAsNamespaceSeparator));
        if (p_AllowMultipleInheritance != false) writer.WriteAttributeString("AllowMultipleInheritance", CodeDOM_Utils.Att_ToString(p_AllowMultipleInheritance));
        if (p_UseUniversalObjectBase != true) writer.WriteAttributeString("UseUniversalObjectBase", CodeDOM_Utils.Att_ToString(p_UseUniversalObjectBase));
        if (p_AllowSetUniversalObjectBase != false) writer.WriteAttributeString("AllowSetUniversalObjectBase", CodeDOM_Utils.Att_ToString(p_AllowSetUniversalObjectBase));
        if (p_AllowDisableUnifiedTypeSystem != false) writer.WriteAttributeString("AllowDisableUnifiedTypeSystem", CodeDOM_Utils.Att_ToString(p_AllowDisableUnifiedTypeSystem));
        if (p_AllowEnableIntegerOverflowExceptions != false) writer.WriteAttributeString("AllowEnableIntegerOverflowExceptions", CodeDOM_Utils.Att_ToString(p_AllowEnableIntegerOverflowExceptions));
        if (p_AllowEnableFloatOperationsExceptions != false) writer.WriteAttributeString("AllowEnableFloatOperationsExceptions", CodeDOM_Utils.Att_ToString(p_AllowEnableFloatOperationsExceptions));
        if (p_RequireIntegerOverflowExceptions != false) writer.WriteAttributeString("RequireIntegerOverflowExceptions", CodeDOM_Utils.Att_ToString(p_RequireIntegerOverflowExceptions));
        if (p_RequireFloatOperationsExceptions != false) writer.WriteAttributeString("RequireFloatOperationsExceptions", CodeDOM_Utils.Att_ToString(p_RequireFloatOperationsExceptions));
        if (p_FullDecimalImplementation != true) writer.WriteAttributeString("FullDecimalImplementation", CodeDOM_Utils.Att_ToString(p_FullDecimalImplementation));
        if (p_SupportASCIIChar != false) writer.WriteAttributeString("SupportASCIIChar", CodeDOM_Utils.Att_ToString(p_SupportASCIIChar));
        if (p_SupportASCIIString != false) writer.WriteAttributeString("SupportASCIIString", CodeDOM_Utils.Att_ToString(p_SupportASCIIString));
        if (p_SupportUnsignedIntegers != true) writer.WriteAttributeString("SupportUnsignedIntegers", CodeDOM_Utils.Att_ToString(p_SupportUnsignedIntegers));
        if (p_SupportExtendedValueTypes != true) writer.WriteAttributeString("SupportExtendedValueTypes", CodeDOM_Utils.Att_ToString(p_SupportExtendedValueTypes));
        if (p_SupportExtendedReferenceTypes != true) writer.WriteAttributeString("SupportExtendedReferenceTypes", CodeDOM_Utils.Att_ToString(p_SupportExtendedReferenceTypes));
        if (p_SupportFunctionPointerTypes != true) writer.WriteAttributeString("SupportFunctionPointerTypes", CodeDOM_Utils.Att_ToString(p_SupportFunctionPointerTypes));
        if (p_SupportExtendedTypes != true) writer.WriteAttributeString("SupportExtendedTypes", CodeDOM_Utils.Att_ToString(p_SupportExtendedTypes));
        if (p_AllowSetASCIIStringClass != false) writer.WriteAttributeString("AllowSetASCIIStringClass", CodeDOM_Utils.Att_ToString(p_AllowSetASCIIStringClass));
        if (p_AllowSetStringClass != false) writer.WriteAttributeString("AllowSetStringClass", CodeDOM_Utils.Att_ToString(p_AllowSetStringClass));
        if (p_AllowDisableNullReferenceCheckOnMemberAccess != false) writer.WriteAttributeString("AllowDisableNullReferenceCheckOnMemberAccess", CodeDOM_Utils.Att_ToString(p_AllowDisableNullReferenceCheckOnMemberAccess));
        if (p_SupportPointerArithmetics != false) writer.WriteAttributeString("SupportPointerArithmetics", CodeDOM_Utils.Att_ToString(p_SupportPointerArithmetics));
        if (p_AllowSetNullIntegerValue != false) writer.WriteAttributeString("AllowSetNullIntegerValue", CodeDOM_Utils.Att_ToString(p_AllowSetNullIntegerValue));
        if (p_NullIntegerValue != 0) writer.WriteAttributeString("NullIntegerValue", CodeDOM_Utils.Att_ToString(p_NullIntegerValue));
        if (p_AllowDisableBaseTypesArrayItemsInitialization != false) writer.WriteAttributeString("AllowDisableBaseTypesArrayItemsInitialization", CodeDOM_Utils.Att_ToString(p_AllowDisableBaseTypesArrayItemsInitialization));
        if (p_AllowDisableStaticsVarsInitialization != false) writer.WriteAttributeString("AllowDisableStaticsVarsInitialization", CodeDOM_Utils.Att_ToString(p_AllowDisableStaticsVarsInitialization));
        if (p_AllowDisableInstanceVarsInitialization != false) writer.WriteAttributeString("AllowDisableInstanceVarsInitialization", CodeDOM_Utils.Att_ToString(p_AllowDisableInstanceVarsInitialization));
        if (p_AllowIgnorePreviousAssingmentRules != true) writer.WriteAttributeString("AllowIgnorePreviousAssingmentRules", CodeDOM_Utils.Att_ToString(p_AllowIgnorePreviousAssingmentRules));
        if (p_AllowDisableAddressOfExpressionRequirementOnOutArguments != false) writer.WriteAttributeString("AllowDisableAddressOfExpressionRequirementOnOutArguments", CodeDOM_Utils.Att_ToString(p_AllowDisableAddressOfExpressionRequirementOnOutArguments));
        if (p_AllowFinalizeMethod != true) writer.WriteAttributeString("AllowFinalizeMethod", CodeDOM_Utils.Att_ToString(p_AllowFinalizeMethod));
        if (p_AllowNonNormalVirtualFunctionCallsOnConstructorsBody != false) writer.WriteAttributeString("AllowNonNormalVirtualFunctionCallsOnConstructorsBody", CodeDOM_Utils.Att_ToString(p_AllowNonNormalVirtualFunctionCallsOnConstructorsBody));
        if (p_AllowSelectiveVirtualMembers != true) writer.WriteAttributeString("AllowSelectiveVirtualMembers", CodeDOM_Utils.Att_ToString(p_AllowSelectiveVirtualMembers));
        if (p_AllowNonLimitedGotos != false) writer.WriteAttributeString("AllowNonLimitedGotos", CodeDOM_Utils.Att_ToString(p_AllowNonLimitedGotos));
        if (p_AllowSetRuntimeTypeInformation != true) writer.WriteAttributeString("AllowSetRuntimeTypeInformation", CodeDOM_Utils.Att_ToString(p_AllowSetRuntimeTypeInformation));
        if (p_AllowSetRuntimeReflection != false) writer.WriteAttributeString("AllowSetRuntimeReflection", CodeDOM_Utils.Att_ToString(p_AllowSetRuntimeReflection));
        if (p_AllowLimitRuntimeReflectionToModuleOnly != false) writer.WriteAttributeString("AllowLimitRuntimeReflectionToModuleOnly", CodeDOM_Utils.Att_ToString(p_AllowLimitRuntimeReflectionToModuleOnly));
        if (p_AllowResumeExceptionModel != false) writer.WriteAttributeString("AllowResumeExceptionModel", CodeDOM_Utils.Att_ToString(p_AllowResumeExceptionModel));
        if (p_AllowTerminationExceptionModel != true) writer.WriteAttributeString("AllowTerminationExceptionModel", CodeDOM_Utils.Att_ToString(p_AllowTerminationExceptionModel));
        if (p_AllowFinallyBlocks != true) writer.WriteAttributeString("AllowFinallyBlocks", CodeDOM_Utils.Att_ToString(p_AllowFinallyBlocks));
        if (p_AllowResumeNext != true) writer.WriteAttributeString("AllowResumeNext", CodeDOM_Utils.Att_ToString(p_AllowResumeNext));
        if (p_AllowMixedExceptionModel != false) writer.WriteAttributeString("AllowMixedExceptionModel", CodeDOM_Utils.Att_ToString(p_AllowMixedExceptionModel));
        if (p_AllowAnyTypeExceptions != false) writer.WriteAttributeString("AllowAnyTypeExceptions", CodeDOM_Utils.Att_ToString(p_AllowAnyTypeExceptions));
        if (p_AllowStackVarsAsExceptions != true) writer.WriteAttributeString("AllowStackVarsAsExceptions", CodeDOM_Utils.Att_ToString(p_AllowStackVarsAsExceptions));
        if (p_AllowRuntimeChecksFragmentedConfiguration != false) writer.WriteAttributeString("AllowRuntimeChecksFragmentedConfiguration", CodeDOM_Utils.Att_ToString(p_AllowRuntimeChecksFragmentedConfiguration));
        if (p_SupportedOperators != "") writer.WriteAttributeString("SupportedOperators", CodeDOM_Utils.Att_ToString(p_SupportedOperators));
        if (p_GarbageCollector != null) if (!p_GarbageCollector.Write(writer)) result = false;
        writer.WriteEndElement();
        return result;
    }

    public XplNode Read(XplReader reader) throws ParseException, CodeDOM_Exception, IOException {
        this.set_Name(reader.Name());
        if (reader.HasAttributes()) {
            for (int i = 1; i <= reader.AttributeCount(); i++) {
                reader.MoveToAttribute(i);
                if (reader.Name().equals("IsCaseSensitive")) {
                    this.set_IsCaseSensitive(CodeDOM_Utils.StringAtt_To_BOOLEAN(reader.Value()));
                } else if (reader.Name().equals("AllowDefaultSafeArrays")) {
                    this.set_AllowDefaultSafeArrays(CodeDOM_Utils.StringAtt_To_BOOLEAN(reader.Value()));
                } else if (reader.Name().equals("AllowDisableArrayLimitsChecks")) {
                    this.set_AllowDisableArrayLimitsChecks(CodeDOM_Utils.StringAtt_To_BOOLEAN(reader.Value()));
                } else if (reader.Name().equals("AllowUseSinglePointAsNamespaceSeparator")) {
                    this.set_AllowUseSinglePointAsNamespaceSeparator(CodeDOM_Utils.StringAtt_To_BOOLEAN(reader.Value()));
                } else if (reader.Name().equals("AllowUseSimpleMemberAccessAsNamespaceSeparator")) {
                    this.set_AllowUseSimpleMemberAccessAsNamespaceSeparator(CodeDOM_Utils.StringAtt_To_BOOLEAN(reader.Value()));
                } else if (reader.Name().equals("AllowMultipleInheritance")) {
                    this.set_AllowMultipleInheritance(CodeDOM_Utils.StringAtt_To_BOOLEAN(reader.Value()));
                } else if (reader.Name().equals("UseUniversalObjectBase")) {
                    this.set_UseUniversalObjectBase(CodeDOM_Utils.StringAtt_To_BOOLEAN(reader.Value()));
                } else if (reader.Name().equals("AllowSetUniversalObjectBase")) {
                    this.set_AllowSetUniversalObjectBase(CodeDOM_Utils.StringAtt_To_BOOLEAN(reader.Value()));
                } else if (reader.Name().equals("AllowDisableUnifiedTypeSystem")) {
                    this.set_AllowDisableUnifiedTypeSystem(CodeDOM_Utils.StringAtt_To_BOOLEAN(reader.Value()));
                } else if (reader.Name().equals("AllowEnableIntegerOverflowExceptions")) {
                    this.set_AllowEnableIntegerOverflowExceptions(CodeDOM_Utils.StringAtt_To_BOOLEAN(reader.Value()));
                } else if (reader.Name().equals("AllowEnableFloatOperationsExceptions")) {
                    this.set_AllowEnableFloatOperationsExceptions(CodeDOM_Utils.StringAtt_To_BOOLEAN(reader.Value()));
                } else if (reader.Name().equals("RequireIntegerOverflowExceptions")) {
                    this.set_RequireIntegerOverflowExceptions(CodeDOM_Utils.StringAtt_To_BOOLEAN(reader.Value()));
                } else if (reader.Name().equals("RequireFloatOperationsExceptions")) {
                    this.set_RequireFloatOperationsExceptions(CodeDOM_Utils.StringAtt_To_BOOLEAN(reader.Value()));
                } else if (reader.Name().equals("FullDecimalImplementation")) {
                    this.set_FullDecimalImplementation(CodeDOM_Utils.StringAtt_To_BOOLEAN(reader.Value()));
                } else if (reader.Name().equals("SupportASCIIChar")) {
                    this.set_SupportASCIIChar(CodeDOM_Utils.StringAtt_To_BOOLEAN(reader.Value()));
                } else if (reader.Name().equals("SupportASCIIString")) {
                    this.set_SupportASCIIString(CodeDOM_Utils.StringAtt_To_BOOLEAN(reader.Value()));
                } else if (reader.Name().equals("SupportUnsignedIntegers")) {
                    this.set_SupportUnsignedIntegers(CodeDOM_Utils.StringAtt_To_BOOLEAN(reader.Value()));
                } else if (reader.Name().equals("SupportExtendedValueTypes")) {
                    this.set_SupportExtendedValueTypes(CodeDOM_Utils.StringAtt_To_BOOLEAN(reader.Value()));
                } else if (reader.Name().equals("SupportExtendedReferenceTypes")) {
                    this.set_SupportExtendedReferenceTypes(CodeDOM_Utils.StringAtt_To_BOOLEAN(reader.Value()));
                } else if (reader.Name().equals("SupportFunctionPointerTypes")) {
                    this.set_SupportFunctionPointerTypes(CodeDOM_Utils.StringAtt_To_BOOLEAN(reader.Value()));
                } else if (reader.Name().equals("SupportExtendedTypes")) {
                    this.set_SupportExtendedTypes(CodeDOM_Utils.StringAtt_To_BOOLEAN(reader.Value()));
                } else if (reader.Name().equals("AllowSetASCIIStringClass")) {
                    this.set_AllowSetASCIIStringClass(CodeDOM_Utils.StringAtt_To_BOOLEAN(reader.Value()));
                } else if (reader.Name().equals("AllowSetStringClass")) {
                    this.set_AllowSetStringClass(CodeDOM_Utils.StringAtt_To_BOOLEAN(reader.Value()));
                } else if (reader.Name().equals("AllowDisableNullReferenceCheckOnMemberAccess")) {
                    this.set_AllowDisableNullReferenceCheckOnMemberAccess(CodeDOM_Utils.StringAtt_To_BOOLEAN(reader.Value()));
                } else if (reader.Name().equals("SupportPointerArithmetics")) {
                    this.set_SupportPointerArithmetics(CodeDOM_Utils.StringAtt_To_BOOLEAN(reader.Value()));
                } else if (reader.Name().equals("AllowSetNullIntegerValue")) {
                    this.set_AllowSetNullIntegerValue(CodeDOM_Utils.StringAtt_To_BOOLEAN(reader.Value()));
                } else if (reader.Name().equals("NullIntegerValue")) {
                    this.set_NullIntegerValue(CodeDOM_Utils.StringAtt_To_INT(reader.Value()));
                } else if (reader.Name().equals("AllowDisableBaseTypesArrayItemsInitialization")) {
                    this.set_AllowDisableBaseTypesArrayItemsInitialization(CodeDOM_Utils.StringAtt_To_BOOLEAN(reader.Value()));
                } else if (reader.Name().equals("AllowDisableStaticsVarsInitialization")) {
                    this.set_AllowDisableStaticsVarsInitialization(CodeDOM_Utils.StringAtt_To_BOOLEAN(reader.Value()));
                } else if (reader.Name().equals("AllowDisableInstanceVarsInitialization")) {
                    this.set_AllowDisableInstanceVarsInitialization(CodeDOM_Utils.StringAtt_To_BOOLEAN(reader.Value()));
                } else if (reader.Name().equals("AllowIgnorePreviousAssingmentRules")) {
                    this.set_AllowIgnorePreviousAssingmentRules(CodeDOM_Utils.StringAtt_To_BOOLEAN(reader.Value()));
                } else if (reader.Name().equals("AllowDisableAddressOfExpressionRequirementOnOutArguments")) {
                    this.set_AllowDisableAddressOfExpressionRequirementOnOutArguments(CodeDOM_Utils.StringAtt_To_BOOLEAN(reader.Value()));
                } else if (reader.Name().equals("AllowFinalizeMethod")) {
                    this.set_AllowFinalizeMethod(CodeDOM_Utils.StringAtt_To_BOOLEAN(reader.Value()));
                } else if (reader.Name().equals("AllowNonNormalVirtualFunctionCallsOnConstructorsBody")) {
                    this.set_AllowNonNormalVirtualFunctionCallsOnConstructorsBody(CodeDOM_Utils.StringAtt_To_BOOLEAN(reader.Value()));
                } else if (reader.Name().equals("AllowSelectiveVirtualMembers")) {
                    this.set_AllowSelectiveVirtualMembers(CodeDOM_Utils.StringAtt_To_BOOLEAN(reader.Value()));
                } else if (reader.Name().equals("AllowNonLimitedGotos")) {
                    this.set_AllowNonLimitedGotos(CodeDOM_Utils.StringAtt_To_BOOLEAN(reader.Value()));
                } else if (reader.Name().equals("AllowSetRuntimeTypeInformation")) {
                    this.set_AllowSetRuntimeTypeInformation(CodeDOM_Utils.StringAtt_To_BOOLEAN(reader.Value()));
                } else if (reader.Name().equals("AllowSetRuntimeReflection")) {
                    this.set_AllowSetRuntimeReflection(CodeDOM_Utils.StringAtt_To_BOOLEAN(reader.Value()));
                } else if (reader.Name().equals("AllowLimitRuntimeReflectionToModuleOnly")) {
                    this.set_AllowLimitRuntimeReflectionToModuleOnly(CodeDOM_Utils.StringAtt_To_BOOLEAN(reader.Value()));
                } else if (reader.Name().equals("AllowResumeExceptionModel")) {
                    this.set_AllowResumeExceptionModel(CodeDOM_Utils.StringAtt_To_BOOLEAN(reader.Value()));
                } else if (reader.Name().equals("AllowTerminationExceptionModel")) {
                    this.set_AllowTerminationExceptionModel(CodeDOM_Utils.StringAtt_To_BOOLEAN(reader.Value()));
                } else if (reader.Name().equals("AllowFinallyBlocks")) {
                    this.set_AllowFinallyBlocks(CodeDOM_Utils.StringAtt_To_BOOLEAN(reader.Value()));
                } else if (reader.Name().equals("AllowResumeNext")) {
                    this.set_AllowResumeNext(CodeDOM_Utils.StringAtt_To_BOOLEAN(reader.Value()));
                } else if (reader.Name().equals("AllowMixedExceptionModel")) {
                    this.set_AllowMixedExceptionModel(CodeDOM_Utils.StringAtt_To_BOOLEAN(reader.Value()));
                } else if (reader.Name().equals("AllowAnyTypeExceptions")) {
                    this.set_AllowAnyTypeExceptions(CodeDOM_Utils.StringAtt_To_BOOLEAN(reader.Value()));
                } else if (reader.Name().equals("AllowStackVarsAsExceptions")) {
                    this.set_AllowStackVarsAsExceptions(CodeDOM_Utils.StringAtt_To_BOOLEAN(reader.Value()));
                } else if (reader.Name().equals("AllowRuntimeChecksFragmentedConfiguration")) {
                    this.set_AllowRuntimeChecksFragmentedConfiguration(CodeDOM_Utils.StringAtt_To_BOOLEAN(reader.Value()));
                } else if (reader.Name().equals("SupportedOperators")) {
                    this.set_SupportedOperators(CodeDOM_Utils.StringAtt_To_STRING(reader.Value()));
                } else {
                    throw new CodeDOM_Exception("Linea: " + reader.LineNumber() + ".Atributo '" + reader.Name() + "' invalido en elemento '" + this.get_Name() + "'.");
                }
            }
            reader.MoveToElement();
        }
        if (!reader.IsEmptyElement()) {
            reader.Read();
            while (reader.NodeType() != XmlNodeType.ENDELEMENT) {
                XplNode tempNode = null;
                switch(reader.NodeType()) {
                    case XmlNodeType.ELEMENT:
                        if (reader.Name().equals("GarbageCollector")) {
                            tempNode = new XplGarbageCollector();
                            tempNode.Read(reader);
                            this.get_GarbageCollector().InsertAtEnd(tempNode);
                        } else {
                            throw new CodeDOM_Exception("Linea: " + reader.LineNumber() + ".Nombre de nodo '" + reader.Name() + "' inesperado como hijo de elemento '" + this.get_Name() + "'.");
                        }
                        break;
                    case XmlNodeType.ENDELEMENT:
                        break;
                    case XmlNodeType.TEXT:
                        throw new CodeDOM_Exception("Linea: " + reader.LineNumber() + ".No se esperaba texto en este nodo.");
                    default:
                        break;
                }
                reader.Read();
            }
        }
        return this;
    }

    public boolean get_IsCaseSensitive() {
        return p_IsCaseSensitive;
    }

    public boolean get_AllowDefaultSafeArrays() {
        return p_AllowDefaultSafeArrays;
    }

    public boolean get_AllowDisableArrayLimitsChecks() {
        return p_AllowDisableArrayLimitsChecks;
    }

    public boolean get_AllowUseSinglePointAsNamespaceSeparator() {
        return p_AllowUseSinglePointAsNamespaceSeparator;
    }

    public boolean get_AllowUseSimpleMemberAccessAsNamespaceSeparator() {
        return p_AllowUseSimpleMemberAccessAsNamespaceSeparator;
    }

    public boolean get_AllowMultipleInheritance() {
        return p_AllowMultipleInheritance;
    }

    public boolean get_UseUniversalObjectBase() {
        return p_UseUniversalObjectBase;
    }

    public boolean get_AllowSetUniversalObjectBase() {
        return p_AllowSetUniversalObjectBase;
    }

    public boolean get_AllowDisableUnifiedTypeSystem() {
        return p_AllowDisableUnifiedTypeSystem;
    }

    public boolean get_AllowEnableIntegerOverflowExceptions() {
        return p_AllowEnableIntegerOverflowExceptions;
    }

    public boolean get_AllowEnableFloatOperationsExceptions() {
        return p_AllowEnableFloatOperationsExceptions;
    }

    public boolean get_RequireIntegerOverflowExceptions() {
        return p_RequireIntegerOverflowExceptions;
    }

    public boolean get_RequireFloatOperationsExceptions() {
        return p_RequireFloatOperationsExceptions;
    }

    public boolean get_FullDecimalImplementation() {
        return p_FullDecimalImplementation;
    }

    public boolean get_SupportASCIIChar() {
        return p_SupportASCIIChar;
    }

    public boolean get_SupportASCIIString() {
        return p_SupportASCIIString;
    }

    public boolean get_SupportUnsignedIntegers() {
        return p_SupportUnsignedIntegers;
    }

    public boolean get_SupportExtendedValueTypes() {
        return p_SupportExtendedValueTypes;
    }

    public boolean get_SupportExtendedReferenceTypes() {
        return p_SupportExtendedReferenceTypes;
    }

    public boolean get_SupportFunctionPointerTypes() {
        return p_SupportFunctionPointerTypes;
    }

    public boolean get_SupportExtendedTypes() {
        return p_SupportExtendedTypes;
    }

    public boolean get_AllowSetASCIIStringClass() {
        return p_AllowSetASCIIStringClass;
    }

    public boolean get_AllowSetStringClass() {
        return p_AllowSetStringClass;
    }

    public boolean get_AllowDisableNullReferenceCheckOnMemberAccess() {
        return p_AllowDisableNullReferenceCheckOnMemberAccess;
    }

    public boolean get_SupportPointerArithmetics() {
        return p_SupportPointerArithmetics;
    }

    public boolean get_AllowSetNullIntegerValue() {
        return p_AllowSetNullIntegerValue;
    }

    public int get_NullIntegerValue() {
        return p_NullIntegerValue;
    }

    public boolean get_AllowDisableBaseTypesArrayItemsInitialization() {
        return p_AllowDisableBaseTypesArrayItemsInitialization;
    }

    public boolean get_AllowDisableStaticsVarsInitialization() {
        return p_AllowDisableStaticsVarsInitialization;
    }

    public boolean get_AllowDisableInstanceVarsInitialization() {
        return p_AllowDisableInstanceVarsInitialization;
    }

    public boolean get_AllowIgnorePreviousAssingmentRules() {
        return p_AllowIgnorePreviousAssingmentRules;
    }

    public boolean get_AllowDisableAddressOfExpressionRequirementOnOutArguments() {
        return p_AllowDisableAddressOfExpressionRequirementOnOutArguments;
    }

    public boolean get_AllowFinalizeMethod() {
        return p_AllowFinalizeMethod;
    }

    public boolean get_AllowNonNormalVirtualFunctionCallsOnConstructorsBody() {
        return p_AllowNonNormalVirtualFunctionCallsOnConstructorsBody;
    }

    public boolean get_AllowSelectiveVirtualMembers() {
        return p_AllowSelectiveVirtualMembers;
    }

    public boolean get_AllowNonLimitedGotos() {
        return p_AllowNonLimitedGotos;
    }

    public boolean get_AllowSetRuntimeTypeInformation() {
        return p_AllowSetRuntimeTypeInformation;
    }

    public boolean get_AllowSetRuntimeReflection() {
        return p_AllowSetRuntimeReflection;
    }

    public boolean get_AllowLimitRuntimeReflectionToModuleOnly() {
        return p_AllowLimitRuntimeReflectionToModuleOnly;
    }

    public boolean get_AllowResumeExceptionModel() {
        return p_AllowResumeExceptionModel;
    }

    public boolean get_AllowTerminationExceptionModel() {
        return p_AllowTerminationExceptionModel;
    }

    public boolean get_AllowFinallyBlocks() {
        return p_AllowFinallyBlocks;
    }

    public boolean get_AllowResumeNext() {
        return p_AllowResumeNext;
    }

    public boolean get_AllowMixedExceptionModel() {
        return p_AllowMixedExceptionModel;
    }

    public boolean get_AllowAnyTypeExceptions() {
        return p_AllowAnyTypeExceptions;
    }

    public boolean get_AllowStackVarsAsExceptions() {
        return p_AllowStackVarsAsExceptions;
    }

    public boolean get_AllowRuntimeChecksFragmentedConfiguration() {
        return p_AllowRuntimeChecksFragmentedConfiguration;
    }

    public String get_SupportedOperators() {
        return p_SupportedOperators;
    }

    public XplNodeList get_GarbageCollector() {
        return p_GarbageCollector;
    }

    public boolean set_IsCaseSensitive(boolean new_IsCaseSensitive) {
        boolean back_IsCaseSensitive = p_IsCaseSensitive;
        p_IsCaseSensitive = new_IsCaseSensitive;
        return back_IsCaseSensitive;
    }

    public boolean set_AllowDefaultSafeArrays(boolean new_AllowDefaultSafeArrays) {
        boolean back_AllowDefaultSafeArrays = p_AllowDefaultSafeArrays;
        p_AllowDefaultSafeArrays = new_AllowDefaultSafeArrays;
        return back_AllowDefaultSafeArrays;
    }

    public boolean set_AllowDisableArrayLimitsChecks(boolean new_AllowDisableArrayLimitsChecks) {
        boolean back_AllowDisableArrayLimitsChecks = p_AllowDisableArrayLimitsChecks;
        p_AllowDisableArrayLimitsChecks = new_AllowDisableArrayLimitsChecks;
        return back_AllowDisableArrayLimitsChecks;
    }

    public boolean set_AllowUseSinglePointAsNamespaceSeparator(boolean new_AllowUseSinglePointAsNamespaceSeparator) {
        boolean back_AllowUseSinglePointAsNamespaceSeparator = p_AllowUseSinglePointAsNamespaceSeparator;
        p_AllowUseSinglePointAsNamespaceSeparator = new_AllowUseSinglePointAsNamespaceSeparator;
        return back_AllowUseSinglePointAsNamespaceSeparator;
    }

    public boolean set_AllowUseSimpleMemberAccessAsNamespaceSeparator(boolean new_AllowUseSimpleMemberAccessAsNamespaceSeparator) {
        boolean back_AllowUseSimpleMemberAccessAsNamespaceSeparator = p_AllowUseSimpleMemberAccessAsNamespaceSeparator;
        p_AllowUseSimpleMemberAccessAsNamespaceSeparator = new_AllowUseSimpleMemberAccessAsNamespaceSeparator;
        return back_AllowUseSimpleMemberAccessAsNamespaceSeparator;
    }

    public boolean set_AllowMultipleInheritance(boolean new_AllowMultipleInheritance) {
        boolean back_AllowMultipleInheritance = p_AllowMultipleInheritance;
        p_AllowMultipleInheritance = new_AllowMultipleInheritance;
        return back_AllowMultipleInheritance;
    }

    public boolean set_UseUniversalObjectBase(boolean new_UseUniversalObjectBase) {
        boolean back_UseUniversalObjectBase = p_UseUniversalObjectBase;
        p_UseUniversalObjectBase = new_UseUniversalObjectBase;
        return back_UseUniversalObjectBase;
    }

    public boolean set_AllowSetUniversalObjectBase(boolean new_AllowSetUniversalObjectBase) {
        boolean back_AllowSetUniversalObjectBase = p_AllowSetUniversalObjectBase;
        p_AllowSetUniversalObjectBase = new_AllowSetUniversalObjectBase;
        return back_AllowSetUniversalObjectBase;
    }

    public boolean set_AllowDisableUnifiedTypeSystem(boolean new_AllowDisableUnifiedTypeSystem) {
        boolean back_AllowDisableUnifiedTypeSystem = p_AllowDisableUnifiedTypeSystem;
        p_AllowDisableUnifiedTypeSystem = new_AllowDisableUnifiedTypeSystem;
        return back_AllowDisableUnifiedTypeSystem;
    }

    public boolean set_AllowEnableIntegerOverflowExceptions(boolean new_AllowEnableIntegerOverflowExceptions) {
        boolean back_AllowEnableIntegerOverflowExceptions = p_AllowEnableIntegerOverflowExceptions;
        p_AllowEnableIntegerOverflowExceptions = new_AllowEnableIntegerOverflowExceptions;
        return back_AllowEnableIntegerOverflowExceptions;
    }

    public boolean set_AllowEnableFloatOperationsExceptions(boolean new_AllowEnableFloatOperationsExceptions) {
        boolean back_AllowEnableFloatOperationsExceptions = p_AllowEnableFloatOperationsExceptions;
        p_AllowEnableFloatOperationsExceptions = new_AllowEnableFloatOperationsExceptions;
        return back_AllowEnableFloatOperationsExceptions;
    }

    public boolean set_RequireIntegerOverflowExceptions(boolean new_RequireIntegerOverflowExceptions) {
        boolean back_RequireIntegerOverflowExceptions = p_RequireIntegerOverflowExceptions;
        p_RequireIntegerOverflowExceptions = new_RequireIntegerOverflowExceptions;
        return back_RequireIntegerOverflowExceptions;
    }

    public boolean set_RequireFloatOperationsExceptions(boolean new_RequireFloatOperationsExceptions) {
        boolean back_RequireFloatOperationsExceptions = p_RequireFloatOperationsExceptions;
        p_RequireFloatOperationsExceptions = new_RequireFloatOperationsExceptions;
        return back_RequireFloatOperationsExceptions;
    }

    public boolean set_FullDecimalImplementation(boolean new_FullDecimalImplementation) {
        boolean back_FullDecimalImplementation = p_FullDecimalImplementation;
        p_FullDecimalImplementation = new_FullDecimalImplementation;
        return back_FullDecimalImplementation;
    }

    public boolean set_SupportASCIIChar(boolean new_SupportASCIIChar) {
        boolean back_SupportASCIIChar = p_SupportASCIIChar;
        p_SupportASCIIChar = new_SupportASCIIChar;
        return back_SupportASCIIChar;
    }

    public boolean set_SupportASCIIString(boolean new_SupportASCIIString) {
        boolean back_SupportASCIIString = p_SupportASCIIString;
        p_SupportASCIIString = new_SupportASCIIString;
        return back_SupportASCIIString;
    }

    public boolean set_SupportUnsignedIntegers(boolean new_SupportUnsignedIntegers) {
        boolean back_SupportUnsignedIntegers = p_SupportUnsignedIntegers;
        p_SupportUnsignedIntegers = new_SupportUnsignedIntegers;
        return back_SupportUnsignedIntegers;
    }

    public boolean set_SupportExtendedValueTypes(boolean new_SupportExtendedValueTypes) {
        boolean back_SupportExtendedValueTypes = p_SupportExtendedValueTypes;
        p_SupportExtendedValueTypes = new_SupportExtendedValueTypes;
        return back_SupportExtendedValueTypes;
    }

    public boolean set_SupportExtendedReferenceTypes(boolean new_SupportExtendedReferenceTypes) {
        boolean back_SupportExtendedReferenceTypes = p_SupportExtendedReferenceTypes;
        p_SupportExtendedReferenceTypes = new_SupportExtendedReferenceTypes;
        return back_SupportExtendedReferenceTypes;
    }

    public boolean set_SupportFunctionPointerTypes(boolean new_SupportFunctionPointerTypes) {
        boolean back_SupportFunctionPointerTypes = p_SupportFunctionPointerTypes;
        p_SupportFunctionPointerTypes = new_SupportFunctionPointerTypes;
        return back_SupportFunctionPointerTypes;
    }

    public boolean set_SupportExtendedTypes(boolean new_SupportExtendedTypes) {
        boolean back_SupportExtendedTypes = p_SupportExtendedTypes;
        p_SupportExtendedTypes = new_SupportExtendedTypes;
        return back_SupportExtendedTypes;
    }

    public boolean set_AllowSetASCIIStringClass(boolean new_AllowSetASCIIStringClass) {
        boolean back_AllowSetASCIIStringClass = p_AllowSetASCIIStringClass;
        p_AllowSetASCIIStringClass = new_AllowSetASCIIStringClass;
        return back_AllowSetASCIIStringClass;
    }

    public boolean set_AllowSetStringClass(boolean new_AllowSetStringClass) {
        boolean back_AllowSetStringClass = p_AllowSetStringClass;
        p_AllowSetStringClass = new_AllowSetStringClass;
        return back_AllowSetStringClass;
    }

    public boolean set_AllowDisableNullReferenceCheckOnMemberAccess(boolean new_AllowDisableNullReferenceCheckOnMemberAccess) {
        boolean back_AllowDisableNullReferenceCheckOnMemberAccess = p_AllowDisableNullReferenceCheckOnMemberAccess;
        p_AllowDisableNullReferenceCheckOnMemberAccess = new_AllowDisableNullReferenceCheckOnMemberAccess;
        return back_AllowDisableNullReferenceCheckOnMemberAccess;
    }

    public boolean set_SupportPointerArithmetics(boolean new_SupportPointerArithmetics) {
        boolean back_SupportPointerArithmetics = p_SupportPointerArithmetics;
        p_SupportPointerArithmetics = new_SupportPointerArithmetics;
        return back_SupportPointerArithmetics;
    }

    public boolean set_AllowSetNullIntegerValue(boolean new_AllowSetNullIntegerValue) {
        boolean back_AllowSetNullIntegerValue = p_AllowSetNullIntegerValue;
        p_AllowSetNullIntegerValue = new_AllowSetNullIntegerValue;
        return back_AllowSetNullIntegerValue;
    }

    public int set_NullIntegerValue(int new_NullIntegerValue) {
        int back_NullIntegerValue = p_NullIntegerValue;
        p_NullIntegerValue = new_NullIntegerValue;
        return back_NullIntegerValue;
    }

    public boolean set_AllowDisableBaseTypesArrayItemsInitialization(boolean new_AllowDisableBaseTypesArrayItemsInitialization) {
        boolean back_AllowDisableBaseTypesArrayItemsInitialization = p_AllowDisableBaseTypesArrayItemsInitialization;
        p_AllowDisableBaseTypesArrayItemsInitialization = new_AllowDisableBaseTypesArrayItemsInitialization;
        return back_AllowDisableBaseTypesArrayItemsInitialization;
    }

    public boolean set_AllowDisableStaticsVarsInitialization(boolean new_AllowDisableStaticsVarsInitialization) {
        boolean back_AllowDisableStaticsVarsInitialization = p_AllowDisableStaticsVarsInitialization;
        p_AllowDisableStaticsVarsInitialization = new_AllowDisableStaticsVarsInitialization;
        return back_AllowDisableStaticsVarsInitialization;
    }

    public boolean set_AllowDisableInstanceVarsInitialization(boolean new_AllowDisableInstanceVarsInitialization) {
        boolean back_AllowDisableInstanceVarsInitialization = p_AllowDisableInstanceVarsInitialization;
        p_AllowDisableInstanceVarsInitialization = new_AllowDisableInstanceVarsInitialization;
        return back_AllowDisableInstanceVarsInitialization;
    }

    public boolean set_AllowIgnorePreviousAssingmentRules(boolean new_AllowIgnorePreviousAssingmentRules) {
        boolean back_AllowIgnorePreviousAssingmentRules = p_AllowIgnorePreviousAssingmentRules;
        p_AllowIgnorePreviousAssingmentRules = new_AllowIgnorePreviousAssingmentRules;
        return back_AllowIgnorePreviousAssingmentRules;
    }

    public boolean set_AllowDisableAddressOfExpressionRequirementOnOutArguments(boolean new_AllowDisableAddressOfExpressionRequirementOnOutArguments) {
        boolean back_AllowDisableAddressOfExpressionRequirementOnOutArguments = p_AllowDisableAddressOfExpressionRequirementOnOutArguments;
        p_AllowDisableAddressOfExpressionRequirementOnOutArguments = new_AllowDisableAddressOfExpressionRequirementOnOutArguments;
        return back_AllowDisableAddressOfExpressionRequirementOnOutArguments;
    }

    public boolean set_AllowFinalizeMethod(boolean new_AllowFinalizeMethod) {
        boolean back_AllowFinalizeMethod = p_AllowFinalizeMethod;
        p_AllowFinalizeMethod = new_AllowFinalizeMethod;
        return back_AllowFinalizeMethod;
    }

    public boolean set_AllowNonNormalVirtualFunctionCallsOnConstructorsBody(boolean new_AllowNonNormalVirtualFunctionCallsOnConstructorsBody) {
        boolean back_AllowNonNormalVirtualFunctionCallsOnConstructorsBody = p_AllowNonNormalVirtualFunctionCallsOnConstructorsBody;
        p_AllowNonNormalVirtualFunctionCallsOnConstructorsBody = new_AllowNonNormalVirtualFunctionCallsOnConstructorsBody;
        return back_AllowNonNormalVirtualFunctionCallsOnConstructorsBody;
    }

    public boolean set_AllowSelectiveVirtualMembers(boolean new_AllowSelectiveVirtualMembers) {
        boolean back_AllowSelectiveVirtualMembers = p_AllowSelectiveVirtualMembers;
        p_AllowSelectiveVirtualMembers = new_AllowSelectiveVirtualMembers;
        return back_AllowSelectiveVirtualMembers;
    }

    public boolean set_AllowNonLimitedGotos(boolean new_AllowNonLimitedGotos) {
        boolean back_AllowNonLimitedGotos = p_AllowNonLimitedGotos;
        p_AllowNonLimitedGotos = new_AllowNonLimitedGotos;
        return back_AllowNonLimitedGotos;
    }

    public boolean set_AllowSetRuntimeTypeInformation(boolean new_AllowSetRuntimeTypeInformation) {
        boolean back_AllowSetRuntimeTypeInformation = p_AllowSetRuntimeTypeInformation;
        p_AllowSetRuntimeTypeInformation = new_AllowSetRuntimeTypeInformation;
        return back_AllowSetRuntimeTypeInformation;
    }

    public boolean set_AllowSetRuntimeReflection(boolean new_AllowSetRuntimeReflection) {
        boolean back_AllowSetRuntimeReflection = p_AllowSetRuntimeReflection;
        p_AllowSetRuntimeReflection = new_AllowSetRuntimeReflection;
        return back_AllowSetRuntimeReflection;
    }

    public boolean set_AllowLimitRuntimeReflectionToModuleOnly(boolean new_AllowLimitRuntimeReflectionToModuleOnly) {
        boolean back_AllowLimitRuntimeReflectionToModuleOnly = p_AllowLimitRuntimeReflectionToModuleOnly;
        p_AllowLimitRuntimeReflectionToModuleOnly = new_AllowLimitRuntimeReflectionToModuleOnly;
        return back_AllowLimitRuntimeReflectionToModuleOnly;
    }

    public boolean set_AllowResumeExceptionModel(boolean new_AllowResumeExceptionModel) {
        boolean back_AllowResumeExceptionModel = p_AllowResumeExceptionModel;
        p_AllowResumeExceptionModel = new_AllowResumeExceptionModel;
        return back_AllowResumeExceptionModel;
    }

    public boolean set_AllowTerminationExceptionModel(boolean new_AllowTerminationExceptionModel) {
        boolean back_AllowTerminationExceptionModel = p_AllowTerminationExceptionModel;
        p_AllowTerminationExceptionModel = new_AllowTerminationExceptionModel;
        return back_AllowTerminationExceptionModel;
    }

    public boolean set_AllowFinallyBlocks(boolean new_AllowFinallyBlocks) {
        boolean back_AllowFinallyBlocks = p_AllowFinallyBlocks;
        p_AllowFinallyBlocks = new_AllowFinallyBlocks;
        return back_AllowFinallyBlocks;
    }

    public boolean set_AllowResumeNext(boolean new_AllowResumeNext) {
        boolean back_AllowResumeNext = p_AllowResumeNext;
        p_AllowResumeNext = new_AllowResumeNext;
        return back_AllowResumeNext;
    }

    public boolean set_AllowMixedExceptionModel(boolean new_AllowMixedExceptionModel) {
        boolean back_AllowMixedExceptionModel = p_AllowMixedExceptionModel;
        p_AllowMixedExceptionModel = new_AllowMixedExceptionModel;
        return back_AllowMixedExceptionModel;
    }

    public boolean set_AllowAnyTypeExceptions(boolean new_AllowAnyTypeExceptions) {
        boolean back_AllowAnyTypeExceptions = p_AllowAnyTypeExceptions;
        p_AllowAnyTypeExceptions = new_AllowAnyTypeExceptions;
        return back_AllowAnyTypeExceptions;
    }

    public boolean set_AllowStackVarsAsExceptions(boolean new_AllowStackVarsAsExceptions) {
        boolean back_AllowStackVarsAsExceptions = p_AllowStackVarsAsExceptions;
        p_AllowStackVarsAsExceptions = new_AllowStackVarsAsExceptions;
        return back_AllowStackVarsAsExceptions;
    }

    public boolean set_AllowRuntimeChecksFragmentedConfiguration(boolean new_AllowRuntimeChecksFragmentedConfiguration) {
        boolean back_AllowRuntimeChecksFragmentedConfiguration = p_AllowRuntimeChecksFragmentedConfiguration;
        p_AllowRuntimeChecksFragmentedConfiguration = new_AllowRuntimeChecksFragmentedConfiguration;
        return back_AllowRuntimeChecksFragmentedConfiguration;
    }

    public String set_SupportedOperators(String new_SupportedOperators) {
        String back_SupportedOperators = p_SupportedOperators;
        p_SupportedOperators = new_SupportedOperators;
        return back_SupportedOperators;
    }

    public boolean InsertCallback(XplNodeList nodeList, XplNode node, XplNode parent) {
        if (nodeList == p_GarbageCollector) return acceptInsertNodeCallback_GarbageCollector(node, parent);
        return false;
    }

    public boolean RemoveNodeCallback(XplNodeList nodeList, XplNode node, XplNode parent) {
        return true;
    }

    public boolean acceptInsertNodeCallback_GarbageCollector(XplNode node, XplNode parent) {
        if (node == null) return false;
        if (node.get_Name().equals("GarbageCollector")) {
            if (node.get_ContentTypeName() != CodeDOMTypes.XplGarbageCollector) {
                this.set_ErrorString("El elemento de tipo '" + node.get_ContentTypeName() + "' no es valido como componente de 'XplGarbageCollector'.");
                return false;
            }
            node.set_Parent(parent);
            return true;
        }
        this.set_ErrorString("El elemento de nombre '" + node.get_Name() + "' no es valido como componente de 'XplGarbageCollector'.");
        return false;
    }

    public static final XplGarbageCollector new_GarbageCollector() {
        XplGarbageCollector node = new XplGarbageCollector();
        node.set_Name("GarbageCollector");
        return node;
    }
}
