package com.serena.xmlbridge.adapter.qc9.gen;

import com4j.*;

/**
 * Services to perform customization tasks, such as adding users to user groups, defining user-defined fields, and defining user access privileges.
 */
@IID("{9D4F53EF-41A2-4059-8AB3-13BBCA8333E8}")
public interface ICustomization extends com.serena.xmlbridge.adapter.qc9.gen.IObjectLockingSupport {

    /**
     * Loads all customization data from the server into the client cache. This includes actions, fields, lists, modules, permissions, users, and user groups.
     */
    @VTID(10)
    void load();

    /**
     * Posts all customization data changes to the project database.
     */
    @VTID(11)
    void commit();

    /**
     * The CustomizationFields object.
     */
    @VTID(12)
    @ReturnValue(type = NativeType.Dispatch)
    com4j.Com4jObject fields();

    /**
     * The CustomizationLists object.
     */
    @VTID(13)
    @ReturnValue(type = NativeType.Dispatch)
    com4j.Com4jObject lists();

    /**
     * The CustomizationPermissions object.
     */
    @VTID(14)
    @ReturnValue(type = NativeType.Dispatch)
    com4j.Com4jObject permissions();

    /**
     * The CustomizationUsers object.
     */
    @VTID(15)
    @ReturnValue(type = NativeType.Dispatch)
    com4j.Com4jObject users();

    /**
     * The CustomizationUsersGroups object.
     */
    @VTID(16)
    @ReturnValue(type = NativeType.Dispatch)
    com4j.Com4jObject usersGroups();

    /**
     * The CustomizationActions object.
     */
    @VTID(17)
    @ReturnValue(type = NativeType.Dispatch)
    com4j.Com4jObject actions();

    /**
     * The CustomizationModules object.
     */
    @VTID(18)
    @ReturnValue(type = NativeType.Dispatch)
    com4j.Com4jObject modules();

    /**
     * The CustomizationMailConditions Object.
     */
    @VTID(19)
    @ReturnValue(type = NativeType.Dispatch)
    com4j.Com4jObject mailConditions();

    /**
     * Checks if the program can use the maximum allowable user-defined fields (99 user-defined fields and 3 memo fields).
     */
    @VTID(20)
    int extendedUDFSupport();
}
