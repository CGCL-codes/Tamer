package com.drx;

import com.documentum.fc.client.IDfSession;

public class DrxGetUsersGroups implements IDrxTest {

    public String name() {
        return "Users, Groups and Sessions";
    }

    public String description() {
        return "Users/Groups/Sessions - get users, group membership, roles, aliases and current sessions";
    }

    public DrxGetUsersGroups(IDfSession session) {
        String dql = "";
        try {
            DrxUtils.DrxWrite(DrxUtils.Head1("Users, Groups, Roles, Aliases and Sessions", "Users_Groups"));
            DrxUtils.writeToConsole("Users, Groups and Aliases...");
            DrxUtils.DrxWrite(DrxUtils.Head2("Total Users", "Total_Users"));
            DrxUtils.writeToConsole("\tTotal Users");
            dql = "select count(*) as _count_ from dm_user where r_is_group = FALSE";
            DrxUtils.DrxWrite(DrxUtils.Head3("Total Count: " + DrxUtils.runSingleQuery(dql, session)));
            DrxUtils.DrxWrite(DrxUtils.Head2("Active Users", "Active_Users"));
            DrxUtils.writeToConsole("\tActive Users");
            dql = "select count(*) as _count_ from dm_user where r_is_group = FALSE and user_state=0";
            DrxUtils.DrxWrite(DrxUtils.Head3("Active Count: " + DrxUtils.runSingleQuery(dql, session)));
            dql = "select r_object_id,user_name,user_login_name,user_os_name,last_login_utc_time,failed_auth_attempt,user_db_name,user_state,description,default_folder,user_source,user_admin,user_privileges,client_capability,user_group_name,user_delegation,home_docbase,globally_managed,acl_name from dm_user where r_is_group = FALSE and user_state=0 order by user_name";
            DrxUtils.DrxWrite(DrxUtils.doQuery(dql, session));
            DrxUtils.DrxWrite(DrxUtils.Head2("Inactive Users", "Inactive_Users"));
            DrxUtils.writeToConsole("\tInactive Users");
            dql = "select count(*) as _count_ from dm_user where r_is_group = FALSE and user_state=1";
            DrxUtils.DrxWrite(DrxUtils.Head3("Inactive Count: " + DrxUtils.runSingleQuery(dql, session)));
            dql = "select r_object_id,user_name,user_login_name,user_os_name,last_login_utc_time,failed_auth_attempt,user_db_name,user_state,description,default_folder,user_source,user_admin,user_privileges,client_capability,user_group_name,user_delegation,home_docbase,globally_managed,acl_name from dm_user where r_is_group = FALSE and user_state=1 order by user_name";
            DrxUtils.DrxWrite(DrxUtils.doQuery(dql, session));
            DrxUtils.DrxWrite(DrxUtils.Head2("Locked Out Users", "Locked_Out_Users"));
            DrxUtils.writeToConsole("\tLocked Out Users");
            dql = "select count(*) as _count_ from dm_user where r_is_group = FALSE and user_state=2";
            DrxUtils.DrxWrite(DrxUtils.Head3("Locked Out Count: " + DrxUtils.runSingleQuery(dql, session)));
            dql = "select r_object_id,user_name,user_login_name,user_os_name,last_login_utc_time,failed_auth_attempt,user_db_name,user_state,description,default_folder,user_source,user_admin,user_privileges,client_capability,user_group_name,user_delegation,home_docbase,globally_managed,acl_name from dm_user where r_is_group = FALSE and user_state=2 order by user_name";
            DrxUtils.DrxWrite(DrxUtils.doQuery(dql, session));
            DrxUtils.DrxWrite(DrxUtils.Head2("Inactive and Locked Out Users", "Inactive_and_Locked_Out_Users"));
            DrxUtils.writeToConsole("\tInactive and Locked Out Users");
            dql = "select count(*) as _count_ from dm_user where r_is_group = FALSE and user_state=3";
            DrxUtils.DrxWrite(DrxUtils.Head3("Locked and Inactive Count: " + DrxUtils.runSingleQuery(dql, session)));
            dql = "select r_object_id,user_name,user_login_name,user_os_name,last_login_utc_time,failed_auth_attempt,user_db_name,user_state,description,default_folder,user_source,user_admin,user_privileges,client_capability,user_group_name,user_delegation,home_docbase,globally_managed,acl_name from dm_user where r_is_group = FALSE and user_state=3 order by user_name";
            DrxUtils.DrxWrite(DrxUtils.doQuery(dql, session));
            DrxUtils.DrxWrite(DrxUtils.Head2("Groups", "Groups"));
            DrxUtils.writeToConsole("\tGroups");
            dql = "select count(*) as _count_ from dm_group where lower(group_class) = 'group'";
            DrxUtils.DrxWrite(DrxUtils.Head3("Count: " + DrxUtils.runSingleQuery(dql, session)));
            dql = "select r_object_id,group_name,description,group_admin,group_source,owner_name,is_private,users_names,groups_names from dm_group where lower(group_class) = 'group' order by group_name";
            DrxUtils.DrxWrite(DrxUtils.doQuery(dql, session));
            DrxUtils.DrxWrite(DrxUtils.Head2("Roles", "Roles"));
            DrxUtils.writeToConsole("\tRoles");
            dql = "select count(*) as _count_ from dm_group where lower(group_class) = 'role'";
            DrxUtils.DrxWrite(DrxUtils.Head3("Count: " + DrxUtils.runSingleQuery(dql, session)));
            dql = "select r_object_id,group_name as role_name,description,group_admin,group_source,owner_name,is_private,i_all_users_names as users_names,groups_names from dm_group where lower(group_class) = 'role' order by group_name";
            DrxUtils.DrxWrite(DrxUtils.doQuery(dql, session));
            DrxUtils.DrxWrite(DrxUtils.Head2("Aliases", "Aliases"));
            DrxUtils.writeToConsole("\tAliases");
            dql = "select count(*) as _count_ from dm_alias_set";
            DrxUtils.DrxWrite(DrxUtils.Head3("Count: " + DrxUtils.runSingleQuery(dql, session)));
            dql = "select r_object_id,object_name,owner_name,alias_name,alias_value,alias_category,alias_description,object_description from dm_alias_set order by object_name";
            DrxUtils.DrxWrite(DrxUtils.doQuery(dql, session));
            DrxUtils.DrxWrite(DrxUtils.Head2("Current Sessions", "Current_Sessions"));
            DrxUtils.writeToConsole("\tCurrent Sessions");
            dql = "execute show_sessions";
            DrxUtils.DrxWrite(DrxUtils.doQuery(dql, session));
            DrxUtils.endHead1("Users, Groups, Roles, Aliases and Sessions", "Users_Groups");
        } catch (Exception e) {
            DrxUtils.DrxWriteError(this.name(), e.getMessage());
        }
    }
}
