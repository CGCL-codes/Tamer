package com.google.apps.easyconnect.easyrp.client.basic.logic;

import java.util.Map;
import com.google.common.collect.Maps;

public class GitTree {

    private static Map<String, GitNode> trees = Maps.newHashMap();

    public static GitNode getAcUserStatusLogic(boolean useLocalIdpWhiteList, boolean returnProfileInfo) {
        StringBuilder buf = new StringBuilder();
        buf.append("acUserStatusLogic").append(useLocalIdpWhiteList ? "1" : "0").append(returnProfileInfo ? "1" : "0");
        String key = buf.toString();
        if (trees.get(key) == null) {
            GitNode acUserStatusLogic;
            if (useLocalIdpWhiteList) {
                acUserStatusLogic = buildAcUserStatusLogicWithIdpWhiteList(returnProfileInfo);
            } else {
                acUserStatusLogic = buildAcUserStatusLogicWithoutIdpWhiteList(returnProfileInfo);
            }
            trees.put(key, acUserStatusLogic);
        }
        return trees.get(key);
    }

    public static GitNode getAcLegacySigninLogic(boolean useLocalIdpWhiteList, boolean returnProfileInfo) {
        StringBuilder buf = new StringBuilder();
        buf.append("acLegacySigninLogic").append(useLocalIdpWhiteList ? "1" : "0").append(returnProfileInfo ? "1" : "0");
        String key = buf.toString();
        if (trees.get(key) == null) {
            GitNode acLegacySiginLogic;
            if (useLocalIdpWhiteList) {
                acLegacySiginLogic = buildAcLegacySigninLogicWithIdpWhiteList(returnProfileInfo);
            } else {
                acLegacySiginLogic = buildAcLegacySigninLogicWithoutIdpWhiteList(returnProfileInfo);
            }
            trees.put(key, acLegacySiginLogic);
        }
        return trees.get(key);
    }

    public static GitNode getAcCallbackPopupLogic(boolean useLocalIdpWhiteList, boolean returnProfileInfo) {
        StringBuilder buf = new StringBuilder();
        buf.append("acCallbackLogic").append(useLocalIdpWhiteList ? "1" : "0").append(returnProfileInfo ? "1" : "0");
        String key = buf.toString();
        if (trees.get(key) == null) {
            GitNode acCallbackLogic = buildAcCallbackPopupLogic(useLocalIdpWhiteList, returnProfileInfo);
            trees.put(key, acCallbackLogic);
        }
        return trees.get(key);
    }

    public static GitNode getAcCallbackRedirectLogic(boolean useLocalIdpWhiteList, boolean returnProfileInfo) {
        StringBuilder buf = new StringBuilder();
        buf.append("acCallbackRedirectLogic").append(useLocalIdpWhiteList ? "1" : "0").append(returnProfileInfo ? "1" : "0");
        String key = buf.toString();
        if (trees.get(key) == null) {
            GitNode acCallbackLogic = buildAcCallbackRedirectLogic(useLocalIdpWhiteList, returnProfileInfo);
            trees.put(key, acCallbackLogic);
        }
        return trees.get(key);
    }

    private static GitNode buildAcUserStatusLogicWithIdpWhiteList(boolean returnProfileInfo) {
        GitLogicBuilder builder = GitLogicBuilderFactory.getAcUserStatusLogicBuilder(true, returnProfileInfo);
        builder.start("start", "checkIdentifierType");
        builder.leaf("invalidEmail", "start", "DEFAULT", actions("sendError"));
        builder.decision("validEmail", "start", "email", "checkEmailRegistered");
        builder.decision("unreg", "validEmail", "DEFAULT", "checkDomainType");
        builder.leaf("unregLegacy", "unreg", "legacy", actions("sendUnregisteredLegacy"));
        builder.leaf("unregFed", "unreg", "DEFAULT", actions("sendUnregistered"));
        builder.decision("reg", "validEmail", "registered", "checkAccountType");
        builder.leaf("regLegacy", "reg", "legacy", actions("sendRegisteredLegacy"));
        builder.leaf("regFed", "reg", "DEFAULT", actions("sendRegistered"));
        return builder.build();
    }

    private static GitNode buildAcUserStatusLogicWithoutIdpWhiteList(boolean returnProfileInfo) {
        GitLogicBuilder builder = GitLogicBuilderFactory.getAcUserStatusLogicBuilder(false, returnProfileInfo);
        builder.start("start", "checkIdentifierType");
        builder.leaf("invalidEmail", "start", "DEFAULT", actions("sendError"));
        builder.decision("validEmail", "start", "email", "checkEmailRegistered");
        builder.leaf("unregistered", "validEmail", "DEFAULT", actions("sendUnregistered"));
        builder.decision("checkFed", "validEmail", "registered", "checkAccountType");
        builder.leaf("regLegacy", "checkFed", "legacy", actions("sendRegisteredLegacy"));
        builder.leaf("regFed", "checkFed", "DEFAULT", actions("sendRegistered"));
        return builder.build();
    }

    private static GitNode buildAcLegacySigninLogicWithIdpWhiteList(boolean returnProfileInfo) {
        GitLogicBuilder builder = GitLogicBuilderFactory.getAcLegacySigninLogicBuilder(true, returnProfileInfo);
        builder.start("start", "checkIdentifierType");
        builder.leaf("invalidEmail", "start", "DEFAULT", actions("sendEmailNotExist"));
        builder.decision("validEmail", "start", "email", "checkEmailRegistered");
        builder.decision("unreg", "validEmail", "DEFAULT", "checkDomainType");
        builder.leaf("unregLegacy", "unreg", "legacy", actions("sendEmailNotExist"));
        builder.leaf("unregFed", "unreg", "DEFAULT", actions("sendFederated"));
        builder.decision("reg", "validEmail", "registered", "checkAccountType");
        builder.leaf("regFed", "reg", "DEFAULT", actions("sendFederated"));
        builder.decision("regLegacy", "reg", "legacy", "checkPasswordCorrect");
        builder.leaf("correct", "regLegacy", "correct", actions("setLoggedIn", "sendOK"));
        builder.leaf("incorrect", "regLegacy", "DEFAULT", actions("sendPasswordError"));
        return builder.build();
    }

    private static GitNode buildAcLegacySigninLogicWithoutIdpWhiteList(boolean returnProfileInfo) {
        GitLogicBuilder builder = GitLogicBuilderFactory.getAcLegacySigninLogicBuilder(false, returnProfileInfo);
        builder.start("start", "checkIdentifierType");
        builder.leaf("invalidEmail", "start", "DEFAULT", actions("sendEmailNotExist"));
        builder.decision("validEmail", "start", "email", "checkEmailRegistered");
        builder.leaf("unreg", "validEmail", "DEFAULT", actions("sendEmailNotExist"));
        builder.decision("reg", "validEmail", "registered", "checkAccountType");
        builder.leaf("regFed", "reg", "DEFAULT", actions("sendFederated"));
        builder.decision("regLegacy", "reg", "legacy", "checkPasswordCorrect");
        builder.leaf("correct", "regLegacy", "correct", actions("setLoggedIn", "sendOK"));
        builder.leaf("incorrect", "regLegacy", "DEFAULT", actions("sendPasswordError"));
        return builder.build();
    }

    private static GitNode buildAcCallbackPopupLogic(boolean useLocalIdpWhiteList, boolean returnProfileInfo) {
        GitLogicBuilder builder = GitLogicBuilderFactory.getAcCallbackPopupLogicBuilder(useLocalIdpWhiteList, returnProfileInfo);
        return buildAcCallbackCommonLogic(builder, useLocalIdpWhiteList, returnProfileInfo);
    }

    private static GitNode buildAcCallbackRedirectLogic(boolean useLocalIdpWhiteList, boolean returnProfileInfo) {
        GitLogicBuilder builder = GitLogicBuilderFactory.getAcCallbackRedirectLogicBuilder(useLocalIdpWhiteList, returnProfileInfo);
        return buildAcCallbackCommonLogic(builder, useLocalIdpWhiteList, returnProfileInfo);
    }

    private static GitNode buildAcCallbackCommonLogic(GitLogicBuilder builder, boolean useLocalIdpWhiteList, boolean returnProfileInfo) {
        builder.start("start", "verifyAssertion");
        builder.leaf("error", "start", "DEFAULT", actions("sendInvalidAssertion"));
        builder.leaf("untrusted", "start", "untrusted", actions("sendInvalidAssertionEmail"));
        builder.decision("trusted", "start", "trusted", "checkRpInputEmail");
        builder.leaf("mismatch", "trusted", "DEFAULT", actions("sendAccountMismatch"));
        builder.decision("match", "trusted", "match", "checkEmailRegistered");
        builder.decision("unreg", "match", "unregistered", "tryCreateAccount");
        builder.leaf("created", "unreg", "created", actions("setLoggedIn", "sendOKRegistered"));
        builder.leaf("notCreated", "unreg", "DEFAULT", actions("saveIdpAssertion", "sendOKUnregistered"));
        builder.decision("reg", "match", "DEFAULT", "checkAccountType");
        builder.leaf("regFed", "reg", "DEFAULT", actions("setLoggedIn", "sendOKRegistered"));
        builder.leaf("regLegacy", "reg", "legacy", actions("upgrade", "setLoggedIn", "sendOKRegistered"));
        return builder.build();
    }

    private static String[] actions(String... actionNames) {
        return actionNames;
    }
}
