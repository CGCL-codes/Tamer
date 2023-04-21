package client;

import jline.ConsoleReader;
import java.util.*;
import java.text.SimpleDateFormat;
import org.cyberaide.core.CoGObject;
import org.cyberaide.core.*;
import org.cyberaide.set.CoGSet;
import server.*;

/**
 * Class providing processing of options for command "vo"
 */
public class VOCLI extends CommandPlugin {

    public static final String USER = "user";

    public static final String SSONAME = "ssoname";

    public static final String RESOURCE = "resource";

    public static final String PROXY = "proxy";

    public static final String VO = "vo";

    public static final String VO_GROUP = "group";

    public static final String VO_ROLE = "role";

    public static final String VO_REQUEST = "request";

    public static final String VO_MEMBERSHIP = "membership";

    public static final String PERMISSION = "permission";

    private final String ATT_VO = "this.id,name,owner,date.start,date.end";

    private final String FMT_VO = "%-14s %-14s %-14s %-14s %-14s";

    private final String ATT_USER = "this.id,username,firstname,lastname";

    private final String FMT_USER = "%-14s %-19s %-19s %-19s";

    private final String ATT_RESOURCE = "this.id,name,date.end,service";

    private final String FMT_RESOURCE = "%-12s %-12s %-12s %-35s";

    private final String ATT_SSONAME = "this.id,name,system";

    private final String FMT_SSONAME = "%-24s %-24s %-24s";

    private final String ATT_VO_GROUP = "this.id,name";

    private final String FMT_VO_GROUP = "%-14s %-19s";

    private final String ATT_VO_ROLE = "this.id,name,group,permissions";

    private final String FMT_VO_ROLE = "%-12s %-12s %-12s %-35s";

    private final String ATT_VO_MEMBERSHIP = "this.id,username,group,role";

    private final String FMT_VO_MEMBERSHIP = "%-14s %-19s %-19s %-19s";

    private final String ATT_VO_REQUEST = "this.id,username,date,comment,yes,no";

    private final String FMT_VO_REQUEST = "%-12s %-12s %-14s %-23s %-5s %-5s";

    private String voname = null;

    private String username = null;

    private CyberaideShellInterface shell;

    /**
	 * Default Constructor 
     */
    public VOCLI(CyberaideShellInterface shell) {
        setPrompt("vo");
        this.shell = shell;
    }

    /**
	 * Sets the current options list
	 */
    public void initOptions() {
        addOption("set", "set", true);
        addOption("list", "list", true);
        addOption("a", "attributes", true);
        addOption("group", "group", true);
        addOption("role", "role", true);
        addOption("vo", "vo", true);
        addOption("u", "user", true);
        addOption("create", "create", true);
        addOption("fromfile", "fromfile", true);
        addOption("info", "info", true);
        addOption("edit", "edit", true);
        addOption("id", "id", true);
        addOption("delete", "delete", true);
        addOption("add", "add", true);
        addOption("remove", "remove", true);
        addOption("request", "request", true);
        addOption("comment", "comment", true);
        addOption("vote", "vote", true);
        addOption("decision", "decision", true);
        addOption("share", "share", true);
        addOption("revoke", "revoke", true);
        addOption("retrieve", "retrieve", true);
        addOption("s", "server", true);
        addOption("p", "port", true);
        addOption("l", "lifetime", true);
        addOption("update", "update", true);
        addOption("run", "run", true);
        addOption("exec", "executable", true);
    }

    /**
	 * Parses users' desired actions specified on the command line
	 */
    public void execute() {
        if (hasOption("set")) {
            if (getOption("set").getValues().length < 1) {
                log.error("TYPE is not defined");
                return;
            } else if (getOption("set").getValues()[0].equals("vo")) {
                if (getOption("set").getValues().length > 1) {
                    voname = getOption("set").getValues()[1];
                    log.info("VO context is set to: " + voname);
                } else {
                    voname = null;
                    log.info("VO context is reset");
                }
            } else if (getOption("set").getValues()[0].equals("user")) {
                if (getOption("set").getValues().length > 1) {
                    username = getOption("set").getValues()[1];
                    log.info("Username context is set to: " + username);
                } else {
                    username = null;
                    log.info("Username context is reset");
                }
            } else {
                log.error("Type is not supported");
            }
        } else if (hasOption("list")) {
            list();
        } else if (hasOption("create")) {
            create();
        } else if (hasOption("info")) {
            info();
        } else if (hasOption("edit")) {
            edit();
        } else if (hasOption("delete")) {
            delete();
        } else if (hasOption("add")) {
            addObject();
        } else if (hasOption("remove")) {
            removeObject();
        } else if (hasOption("request")) {
            request();
        } else if (hasOption("vote")) {
            vote();
        } else if (hasOption("share")) {
            share();
        } else if (hasOption("revoke")) {
            revoke();
        } else if (hasOption("retrieve")) {
            retrieve();
        } else if (hasOption("update")) {
            update();
        } else if (hasOption("run")) {
            runJob();
        } else {
            log.error("Specified command is not implemented yet");
        }
    }

    /**
     * "list" option processing
     */
    private void list() {
        String type = getOption("list").getValues()[0];
        CoGSet listset = null;
        if (type.equals(VO)) {
            String userContext = getUser();
            if (userContext == null) {
                listset = (CoGSet) Convert.byte2obj(shell.listVO());
            } else {
                listset = (CoGSet) Convert.byte2obj(shell.listUserVO(userContext));
            }
        } else if (type.equals(USER)) {
            String voContext = getVO();
            if (voContext == null) {
                listset = (CoGSet) Convert.byte2obj(shell.listUser());
            } else {
                listset = (CoGSet) Convert.byte2obj(shell.listVOUser(voContext));
                type = VO_MEMBERSHIP;
            }
        } else if (type.equals(RESOURCE)) {
            String userContext = getUser();
            String voContext = getVO();
            if (userContext != null) {
                listset = (CoGSet) Convert.byte2obj(shell.listResource(userContext));
            } else if (voContext != null) {
                listset = (CoGSet) Convert.byte2obj(shell.listVOResource(voContext));
            } else {
                log.error("User or VO is not defined");
                return;
            }
        } else if (type.equals(SSONAME)) {
            String userContext = getUser();
            if (userContext == null) {
                log.error("User is not defined");
                return;
            } else {
                listset = (CoGSet) Convert.byte2obj(shell.listSSOName(userContext));
            }
        } else if (type.equals(VO_GROUP)) {
            String voContext = getVO();
            if (voContext == null) {
                log.error("VO is not defined");
                return;
            } else {
                listset = (CoGSet) Convert.byte2obj(shell.listGroup(voContext));
            }
        } else if (type.equals(VO_ROLE)) {
            String voContext = getVO();
            if (voContext == null) {
                log.error("VO is not defined");
                return;
            } else {
                if (hasOption("group")) {
                    listset = (CoGSet) Convert.byte2obj(shell.listRole(voContext, getOption("group").getValues()[0]));
                } else {
                    listset = (CoGSet) Convert.byte2obj(shell.listRole(voContext, null));
                }
            }
        } else if (type.equals(VO_REQUEST)) {
            String voContext = getVO();
            if (voContext == null) {
                log.error("VO is not defined");
                return;
            } else {
                listset = (CoGSet) Convert.byte2obj(shell.listRequest(voContext));
            }
        } else {
            log.error("Unrecognized type " + type);
            return;
        }
        if (hasOption("attributes")) {
            printCustom(listset, getOption("attributes").getValues());
        } else {
            printDefault(listset, type);
        }
    }

    /**
     * "create" option processing
     */
    private void create() {
        String type = getOption("create").getValues()[0];
        if (type.equals(VO)) {
            if (hasOption("attributes")) {
                if (getOption("create").getValues().length < 2) {
                    log.error("Option -create must have two arguments: " + "TYPE and NAME");
                    return;
                }
                CoGObject newvo = new CoGObject();
                String[] args = getOption("attributes").getValues();
                newvo.set("name", getOption("create").getValues()[1]);
                for (String arg : args) {
                    String[] att = arg.split("=", 2);
                    newvo.set(att[0], att[1]);
                }
                if (hasOption("user")) {
                    shell.createVO(Convert.obj2byte(newvo), getOptionValue("user"));
                } else {
                    log.error("VO owner is not specified");
                }
            } else if (hasOption("fromfile")) {
                CoGSet newvos = new CoGSet();
                try {
                    newvos.loadFromFile(getOption("fromfile").getValues()[0], newvos.ASCII);
                } catch (Exception e) {
                    log.error("Cannot read file: " + e.getMessage());
                    return;
                }
                if (hasOption("user")) {
                    shell.createVOSet(Convert.obj2byte(newvos), getOptionValue("user"));
                } else {
                    log.error("VO owner is not specified");
                }
            } else {
                if (getOption("create").getValues().length < 2) {
                    log.error("Option -create must have two arguments: " + "TYPE and NAME");
                    return;
                }
                CoGObject newvo = new CoGObject();
                newvo.set("name", getOption("create").getValues()[1]);
                if (hasOption("user")) {
                    shell.createVO(Convert.obj2byte(newvo), getOptionValue("user"));
                } else {
                    log.error("VO owner is not specified");
                }
            }
        } else if (type.equals(USER)) {
            if (hasOption("attributes")) {
                if (getOption("create").getValues().length < 2) {
                    log.error("Option -create must have two arguments: " + "TYPE and NAME");
                    return;
                }
                CoGObject newuser = new CoGObject();
                String[] args = getOption("attributes").getValues();
                newuser.set("username", getOption("create").getValues()[1]);
                for (String arg : args) {
                    String[] att = arg.split("=", 2);
                    newuser.set(att[0], att[1]);
                }
                shell.createUser(Convert.obj2byte(newuser));
            } else if (hasOption("fromfile")) {
                CoGSet newusers = new CoGSet();
                try {
                    newusers.loadFromFile(getOption("fromfile").getValues()[0], newusers.ASCII);
                } catch (Exception e) {
                    log.error("Cannot read file: " + e.getMessage());
                    return;
                }
                shell.createUserSet(Convert.obj2byte(newusers));
            } else {
                if (getOption("create").getValues().length < 2) {
                    log.error("Option -create must have two arguments: " + "TYPE and NAME");
                    return;
                }
                CoGObject newuser = new CoGObject();
                newuser.set("username", getOption("create").getValues()[1]);
                shell.createUser(Convert.obj2byte(newuser));
            }
        } else if (type.equals(RESOURCE) || type.equals(SSONAME)) {
            String userContext = getUser();
            if (userContext == null) {
                log.error("User is not defined");
                return;
            }
            if (hasOption("attributes")) {
                if (getOption("create").getValues().length < 2) {
                    log.error("Option -create must have two arguments: " + "TYPE and NAME");
                    return;
                }
                CoGObject newobject = new CoGObject();
                String[] args = getOption("attributes").getValues();
                newobject.set("name", getOption("create").getValues()[1]);
                newobject.set("owner", userContext);
                for (String arg : args) {
                    String[] att = arg.split("=", 2);
                    newobject.set(att[0], att[1]);
                }
                if (type.equals(SSONAME)) {
                    shell.createSSOName(Convert.obj2byte(newobject));
                } else if (type.equals(RESOURCE)) {
                    shell.createResource(Convert.obj2byte(newobject));
                }
            } else if (hasOption("fromfile")) {
                CoGSet newobjects = new CoGSet();
                try {
                    newobjects.loadFromFile(getOption("fromfile").getValues()[0], newobjects.ASCII);
                } catch (Exception e) {
                    log.error("Cannot read file: " + e.getMessage());
                    return;
                }
                if (type.equals(SSONAME)) {
                    shell.createSSONameSet(Convert.obj2byte(newobjects));
                } else if (type.equals(RESOURCE)) {
                    shell.createResourceSet(Convert.obj2byte(newobjects));
                }
            } else {
                if (getOption("create").getValues().length < 2) {
                    log.error("Option -create must have two arguments: " + "TYPE and NAME");
                    return;
                }
                CoGObject newobject = new CoGObject();
                newobject.set("name", getOption("create").getValues()[1]);
                newobject.set("owner", userContext);
                if (type.equals(SSONAME)) {
                    shell.createSSOName(Convert.obj2byte(newobject));
                } else if (type.equals(RESOURCE)) {
                    shell.createResource(Convert.obj2byte(newobject));
                }
            }
        } else if (type.equals(VO_GROUP) || type.equals(VO_ROLE)) {
            String voContext = getVO();
            if (voContext == null) {
                log.error("VO is not defined");
                return;
            }
            if (hasOption("attributes")) {
                if (getOption("create").getValues().length < 2) {
                    log.error("Option -create must have two arguments: " + "TYPE and NAME");
                    return;
                }
                CoGObject newobject = new CoGObject();
                String[] args = getOption("attributes").getValues();
                newobject.set("name", getOption("create").getValues()[1]);
                newobject.set("vo", voContext);
                for (String arg : args) {
                    String[] att = arg.split("=", 2);
                    newobject.set(att[0], att[1]);
                }
                if (type.equals(VO_ROLE) && hasOption("group")) {
                    newobject.set("group", getOption("group").getValues()[0]);
                }
                if (type.equals(VO_GROUP)) {
                    shell.createGroup(Convert.obj2byte(newobject));
                } else if (type.equals(VO_ROLE)) {
                    shell.createRole(Convert.obj2byte(newobject));
                }
            } else if (hasOption("fromfile")) {
                CoGSet newobjects = new CoGSet();
                try {
                    newobjects.loadFromFile(getOption("fromfile").getValues()[0], newobjects.ASCII);
                } catch (Exception e) {
                    log.error("Cannot read file: " + e.getMessage());
                    return;
                }
                if (type.equals(VO_GROUP)) {
                    shell.createGroupSet(Convert.obj2byte(newobjects));
                } else if (type.equals(VO_ROLE)) {
                    shell.createRoleSet(Convert.obj2byte(newobjects));
                }
            } else {
                if (getOption("create").getValues().length < 2) {
                    log.error("Option -create must have two arguments: " + "TYPE and NAME");
                    return;
                }
                CoGObject newobject = new CoGObject();
                newobject.set("name", getOption("create").getValues()[1]);
                newobject.set("vo", voContext);
                if (type.equals(VO_ROLE) && hasOption("group")) {
                    newobject.set("group", getOption("group").getValues()[0]);
                }
                if (type.equals(VO_GROUP)) {
                    shell.createGroup(Convert.obj2byte(newobject));
                } else if (type.equals(VO_ROLE)) {
                    shell.createRole(Convert.obj2byte(newobject));
                }
            }
        } else {
            log.error("Unrecognized type " + type);
            return;
        }
    }

    /**
     * "info" option processing
     */
    private void info() {
        String type = getOptionValue("info");
        if (type.equals(VO)) {
            String voContext = null;
            String voID = null;
            if (getOption("info").getValues().length > 1) {
                voContext = getOption("info").getValues()[1];
            } else {
                voContext = getVO();
                if (voContext == null) {
                    if (hasOption("id")) {
                        voID = getOption("id").getValues()[0];
                    } else {
                        log.error("VO name or id is not specified");
                        return;
                    }
                }
            }
            CoGObject res = (CoGObject) Convert.byte2obj(shell.getVO(voContext, voID));
            if (res != null) {
                printResult(res, type, voContext, voID, getUser(), null);
            } else {
                log.error("VO is not found");
                return;
            }
        } else if (type.equals(USER)) {
            String userContext = null;
            String userID = null;
            if (getOption("info").getValues().length > 1) {
                userContext = getOption("info").getValues()[1];
            } else {
                userContext = getUser();
                if (userContext == null) {
                    if (hasOption("id")) {
                        userID = getOption("id").getValues()[0];
                    } else {
                        log.error("User name or id is not specified");
                        return;
                    }
                }
            }
            CoGObject res = (CoGObject) Convert.byte2obj(shell.getUser(userContext, userID));
            if (res != null) {
                printResult(res, type + " profile", getVO(), null, userContext, userID);
            } else {
                log.error("User is not found");
                return;
            }
            String voContext = getVO();
            if (voContext != null) {
                res = (CoGObject) Convert.byte2obj(shell.getVOUser(voContext, userContext, userID));
                if (res != null) {
                    printResult(res, type + " membership", voContext, null, userContext, userID);
                }
            }
        } else if (type.equals(RESOURCE) || type.equals(SSONAME)) {
            String userContext = getUser();
            if (userContext == null) {
                log.error("User is not defined");
                return;
            }
            String objectName = null;
            String objectID = null;
            if (getOption("info").getValues().length > 1) {
                objectName = getOption("info").getValues()[1];
            } else {
                if (objectName == null) {
                    if (hasOption("id")) {
                        objectID = getOption("id").getValues()[0];
                    } else {
                        log.error("Object name or id is not specified");
                        return;
                    }
                }
            }
            if (type.equals(RESOURCE)) {
                CoGObject res = (CoGObject) Convert.byte2obj(shell.getResource(userContext, objectName, objectID));
                if (res != null) {
                    printResult(res, type, getVO(), null, userContext, null);
                } else {
                    log.error("Resource is not found");
                    return;
                }
            } else if (type.equals(SSONAME)) {
                CoGObject res = (CoGObject) Convert.byte2obj(shell.getSSOName(userContext, objectName, objectID));
                if (res != null) {
                    printResult(res, type, getVO(), null, userContext, null);
                } else {
                    log.error("SSO name is not found");
                    return;
                }
            }
        } else if (type.equals(VO_GROUP) || type.equals(VO_ROLE) || type.equals(VO_REQUEST)) {
            String voContext = getVO();
            if (voContext == null) {
                log.error("VO is not defined");
                return;
            }
            String objectName = null;
            String objectID = null;
            if (getOption("info").getValues().length > 1) {
                objectName = getOption("info").getValues()[1];
            } else {
                if (objectName == null) {
                    if (hasOption("id")) {
                        objectID = getOption("id").getValues()[0];
                    } else {
                        log.error("Object name or id is not specified");
                        return;
                    }
                }
            }
            if (type.equals(VO_GROUP)) {
                CoGObject res = (CoGObject) Convert.byte2obj(shell.getGroup(voContext, objectName, objectID));
                if (res != null) {
                    printResult(res, type, voContext, null, getUser(), null);
                } else {
                    log.error("Group is not found");
                    return;
                }
            } else if (type.equals(VO_ROLE)) {
                CoGObject res = (CoGObject) Convert.byte2obj(shell.getRole(voContext, objectName, objectID));
                if (res != null) {
                    printResult(res, type, voContext, null, getUser(), null);
                } else {
                    log.error("Role is not found");
                    return;
                }
            } else if (type.equals(VO_REQUEST)) {
                CoGObject res = (CoGObject) Convert.byte2obj(shell.getRequest(voContext, objectName, objectID));
                if (res != null) {
                    printResult(res, type, voContext, null, getUser(), null);
                } else {
                    log.error("Request is not found");
                    return;
                }
            }
        } else {
            log.error("Unrecognized type " + type);
            return;
        }
    }

    /**
     * "edit" option processing
     */
    private void edit() {
        String type = getOption("edit").getValues()[0];
        if (type.equals(VO)) {
            CoGObject editvo = new CoGObject();
            String voContext = null;
            String voID = null;
            if (getOption("edit").getValues().length > 1) {
                voContext = getOption("edit").getValues()[1];
            } else {
                voContext = getVO();
                if (voContext == null) {
                    if (hasOption("id")) {
                        voID = getOption("id").getValues()[0];
                    } else {
                        log.error("VO name or id is not specified");
                        return;
                    }
                }
            }
            if (hasOption("attributes")) {
                String[] args = getOption("attributes").getValues();
                for (String arg : args) {
                    String[] att = arg.split("=", 2);
                    editvo.set(att[0], att[1]);
                }
            } else if (hasOption("fromfile")) {
                CoGSet tempvos = new CoGSet();
                try {
                    tempvos.loadFromFile(getOption("fromfile").getValues()[0], tempvos.ASCII);
                } catch (Exception e) {
                    log.error("Cannot read file: " + e.getMessage());
                    return;
                }
                CoGObject tempvo = tempvos.find("name", editvo.get("name"));
                if (tempvo != null) {
                    editvo.setAllAttributes(tempvo.getAllAttributes());
                } else {
                    log.error("VO is not found in the file");
                    return;
                }
            } else {
                log.error("Values are not defined");
                return;
            }
            shell.setVO(voContext, voID, Convert.obj2byte(editvo));
        }
        if (type.equals(USER)) {
            CoGObject edituser = new CoGObject();
            String userContext = null;
            String userID = null;
            if (getOption("edit").getValues().length > 1) {
                userContext = getOption("edit").getValues()[1];
            } else {
                userContext = getUser();
                if (userContext == null) {
                    if (hasOption("id")) {
                        userID = getOption("id").getValues()[0];
                    } else {
                        log.error("User name or id is not specified");
                        return;
                    }
                }
            }
            if (hasOption("attributes")) {
                String[] args = getOption("attributes").getValues();
                for (String arg : args) {
                    String[] att = arg.split("=", 2);
                    edituser.set(att[0], att[1]);
                }
            } else if (hasOption("fromfile")) {
                CoGSet tempusers = new CoGSet();
                try {
                    tempusers.loadFromFile(getOption("fromfile").getValues()[0], tempusers.ASCII);
                } catch (Exception e) {
                    log.error("Cannot read file: " + e.getMessage());
                    return;
                }
                CoGObject tempuser = tempusers.find("username", edituser.get("username"));
                if (tempuser != null) {
                    edituser.setAllAttributes(tempuser.getAllAttributes());
                } else {
                    log.error("User is not found in the file");
                    return;
                }
            } else {
                log.error("Values are not defined");
                return;
            }
            shell.setUser(userContext, userID, Convert.obj2byte(edituser));
        } else if (type.equals(VO_MEMBERSHIP)) {
            String voContext = getVO();
            if (voContext == null) {
                log.error("VO is not defined");
                return;
            }
            CoGObject editobject = new CoGObject();
            String objectName = null;
            String objectID = null;
            if (getOption("edit").getValues().length > 1) {
                objectName = getOption("edit").getValues()[1];
            } else {
                if (objectName == null) {
                    if (hasOption("id")) {
                        objectID = getOption("id").getValues()[0];
                    } else {
                        log.error("Object name or id is not specified");
                        return;
                    }
                }
            }
            if (hasOption("attributes")) {
                String[] args = getOption("attributes").getValues();
                for (String arg : args) {
                    String[] att = arg.split("=", 2);
                    editobject.set(att[0], att[1]);
                }
            } else if (hasOption("fromfile")) {
                CoGSet tempobjects = new CoGSet();
                try {
                    tempobjects.loadFromFile(getOption("fromfile").getValues()[0], tempobjects.ASCII);
                } catch (Exception e) {
                    log.error("Cannot read file: " + e.getMessage());
                    return;
                }
                CoGObject tempobject;
                tempobject = tempobjects.find("username", editobject.get("username"));
                if (tempobject != null) {
                    editobject.setAllAttributes(tempobject.getAllAttributes());
                } else {
                    log.error("Object is not found in the file");
                    return;
                }
            } else {
                log.error("Values are not defined");
                return;
            }
            shell.setVOUser(voContext, objectName, objectID, Convert.obj2byte(editobject));
        } else if (type.equals(RESOURCE) || type.equals(SSONAME)) {
            String userContext = getUser();
            if (userContext == null) {
                log.error("User is not defined");
                return;
            }
            CoGObject editobject = new CoGObject();
            String objectName = null;
            String objectID = null;
            if (getOption("edit").getValues().length > 1) {
                objectName = getOption("edit").getValues()[1];
            } else {
                if (objectName == null) {
                    if (hasOption("id")) {
                        objectID = getOption("id").getValues()[0];
                    } else {
                        log.error("Object name or id is not specified");
                        return;
                    }
                }
            }
            if (hasOption("attributes")) {
                String[] args = getOption("attributes").getValues();
                for (String arg : args) {
                    String[] att = arg.split("=", 2);
                    editobject.set(att[0], att[1]);
                }
            } else if (hasOption("fromfile")) {
                CoGSet tempobjects = new CoGSet();
                try {
                    tempobjects.loadFromFile(getOption("fromfile").getValues()[0], tempobjects.ASCII);
                } catch (Exception e) {
                    log.error("Cannot read file: " + e.getMessage());
                    return;
                }
                CoGObject tempobject = tempobjects.find("name", editobject.get("name"));
                if (tempobject != null) {
                    editobject.setAllAttributes(tempobject.getAllAttributes());
                } else {
                    log.error("Object is not found in the file");
                    return;
                }
            } else {
                log.error("Values are not defined");
                return;
            }
            if (type.equals(RESOURCE)) {
                shell.setResource(userContext, objectName, objectID, Convert.obj2byte(editobject));
            } else if (type.equals(SSONAME)) {
                shell.setSSOName(userContext, objectName, objectID, Convert.obj2byte(editobject));
            }
        } else if (type.equals(VO_GROUP) || type.equals(VO_ROLE)) {
            String voContext = getVO();
            if (voContext == null) {
                log.error("VO is not defined");
                return;
            }
            CoGObject editobject = new CoGObject();
            String objectName = null;
            String objectID = null;
            if (getOption("edit").getValues().length > 1) {
                objectName = getOption("edit").getValues()[1];
            } else {
                if (objectName == null) {
                    if (hasOption("id")) {
                        objectID = getOption("id").getValues()[0];
                    } else {
                        log.error("Object name or id is not specified");
                        return;
                    }
                }
            }
            if (hasOption("attributes")) {
                String[] args = getOption("attributes").getValues();
                for (String arg : args) {
                    String[] att = arg.split("=", 2);
                    editobject.set(att[0], att[1]);
                }
            } else if (hasOption("fromfile")) {
                CoGSet tempobjects = new CoGSet();
                try {
                    tempobjects.loadFromFile(getOption("fromfile").getValues()[0], tempobjects.ASCII);
                } catch (Exception e) {
                    log.error("Cannot read file: " + e.getMessage());
                    return;
                }
                CoGObject tempobject;
                tempobject = tempobjects.find("name", editobject.get("name"));
                if (tempobject != null) {
                    editobject.setAllAttributes(tempobject.getAllAttributes());
                } else {
                    log.error("Object is not found in the file");
                    return;
                }
            } else {
                log.error("Values are not defined");
                return;
            }
            if (type.equals(VO_GROUP)) {
                shell.setGroup(voContext, objectName, objectID, Convert.obj2byte(editobject));
            } else if (type.equals(VO_ROLE)) {
                shell.setRole(voContext, objectName, objectID, Convert.obj2byte(editobject));
            }
        } else {
            log.error("Unrecognized type " + type);
            return;
        }
    }

    /**
     * "delete" option processing
     */
    private void delete() {
        try {
            ConsoleReader reader = new ConsoleReader();
            reader.setInput(getContext().getIo().__in);
            String confirm = reader.readLine("Are you sure?[y/n]");
            if (!confirm.toLowerCase().startsWith("y")) {
                return;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return;
        }
        String type = getOption("delete").getValues()[0];
        CoGSet delset = null;
        String filename = "";
        if (type.equals(VO)) {
            String voContext = null;
            String voID = null;
            if (getOption("delete").getValues().length > 1) {
                voContext = getOption("delete").getValues()[1];
            } else {
                voContext = getVO();
                if (voContext == null) {
                    if (hasOption("id")) {
                        voID = getOption("id").getValues()[0];
                    } else {
                        log.error("VO name or id is not specified");
                        return;
                    }
                }
            }
            shell.deleteVO(voContext, voID);
        } else if (type.equals(USER)) {
            String userContext = null;
            String userID = null;
            if (getOption("delete").getValues().length > 1) {
                userContext = getOption("delete").getValues()[1];
            } else {
                userContext = getUser();
                if (userContext == null) {
                    if (hasOption("id")) {
                        userID = getOption("id").getValues()[0];
                    } else {
                        log.error("User name or id is not specified");
                        return;
                    }
                }
            }
            shell.deleteUser(userContext, userID);
        } else if (type.equals(RESOURCE) || type.equals(SSONAME)) {
            String userContext = getUser();
            if (userContext == null) {
                log.error("User is not defined");
                return;
            }
            String objectName = null;
            String objectID = null;
            if (getOption("delete").getValues().length > 1) {
                objectName = getOption("delete").getValues()[1];
            } else {
                if (objectName == null) {
                    if (hasOption("id")) {
                        objectID = getOption("id").getValues()[0];
                    } else {
                        log.error("Object name or id is not specified");
                        return;
                    }
                }
            }
            if (type.equals(RESOURCE)) {
                shell.deleteResource(userContext, objectName, objectID);
            } else if (type.equals(SSONAME)) {
                shell.deleteSSOName(userContext, objectName, objectID);
            }
        } else if (type.equals(VO_GROUP) || type.equals(VO_ROLE)) {
            String voContext = getVO();
            if (voContext == null) {
                log.error("VO is not defined: use set or -vo");
                return;
            }
            String objectName = null;
            String objectID = null;
            if (getOption("delete").getValues().length > 1) {
                objectName = getOption("delete").getValues()[1];
            } else {
                if (objectName == null) {
                    if (hasOption("id")) {
                        objectID = getOption("id").getValues()[0];
                    } else {
                        log.error("Object name or id is not specified");
                        return;
                    }
                }
            }
            if (type.equals(VO_GROUP)) {
                shell.deleteGroup(voContext, objectName, objectID);
            } else if (type.equals(VO_ROLE)) {
                shell.deleteRole(voContext, objectName, objectID);
            }
        } else {
            log.error("Unrecognized type " + type);
            return;
        }
    }

    /**
     * "add" option processing
     */
    private void addObject() {
        String type = getOption("add").getValues()[0];
        if (type.equals(USER)) {
            String userName = null;
            String userID = null;
            if (getOption("add").getValues().length > 1) {
                userName = getOption("add").getValues()[1];
                userName = shell.checkUser(userName, null);
            } else {
                if (hasOption("id")) {
                    userID = getOption("id").getValues()[0];
                    userName = shell.checkUser(null, userID);
                } else {
                    log.error("User is not specified");
                    return;
                }
            }
            if (userName != null) {
                String voName = getVO();
                if (voName != null) {
                    CoGObject newvouser = new CoGObject();
                    newvouser.set("username", userName);
                    newvouser.set("vo", voName);
                    if (hasOption("group")) {
                        newvouser.set("group", getOption("group").getValues()[0]);
                    }
                    if (hasOption("role")) {
                        newvouser.set("role", getOption("role").getValues()[0]);
                    }
                    shell.addUser(userName, voName, Convert.obj2byte(newvouser));
                } else {
                    log.error("VO is not specified");
                    return;
                }
            } else {
                log.error("User is not found");
                return;
            }
            return;
        } else if (type.equals(PERMISSION)) {
            log.error("This option is not yet implemented");
            return;
        } else {
            log.error("Unrecognized argument value " + type + " for option -add");
            return;
        }
    }

    /**
     * "remove" option processing
     */
    private void removeObject() {
        String type = getOption("remove").getValues()[0];
        if (type.equals(USER)) {
            String userName = null;
            String userID = null;
            if (getOption("remove").getValues().length > 1) {
                userName = getOption("remove").getValues()[1];
                userName = shell.checkUser(userName, userID);
            } else {
                if (hasOption("id")) {
                    userID = getOption("id").getValues()[0];
                    userName = shell.checkUser(userName, userID);
                } else {
                    log.error("User is not specified");
                    return;
                }
            }
            if (userName != null) {
                String voName = getVO();
                if (voName != null) {
                    shell.removeUser(voName, userName, userID);
                } else {
                    log.error("VO is not specified");
                    return;
                }
            } else {
                log.error("User is not found");
                return;
            }
            return;
        } else if (type.equals(PERMISSION)) {
            log.error("This option is not yet implemented");
            return;
        } else {
            log.error("Unrecognized argument value " + type + " for option -add");
            return;
        }
    }

    /**
     * "request" option processing
     */
    private void request() {
        String type = getOption("request").getValues()[0];
        if (type.equals("membership")) {
            String userName = null;
            if (getOption("request").getValues().length > 1) {
                userName = getOption("request").getValues()[1];
            } else {
                log.error("User name is not specified");
                return;
            }
            if (userName != null) {
                String voName = getVO();
                if (voName != null) {
                    CoGObject newvoreq = new CoGObject();
                    newvoreq.set("username", userName);
                    newvoreq.set("vo", voName);
                    Calendar cal = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                    newvoreq.set("date", sdf.format(cal.getTime()));
                    if (hasOption("comment")) {
                        newvoreq.set("comment", getOption("comment").getValues()[0]);
                    }
                    shell.createRequest(Convert.obj2byte(newvoreq));
                } else {
                    log.error("VO is not specified");
                    return;
                }
            } else {
                log.error("User is not found");
                return;
            }
            return;
        } else {
            log.error("Unrecognized argument value " + type + " for option -add");
            return;
        }
    }

    /**
     * "vote" option processing
     */
    private void vote() {
        String type = getOption("vote").getValues()[0];
        if (type.equals("membership")) {
            String reqID = null;
            if (getOption("vote").getValues().length > 1) {
                reqID = getOption("vote").getValues()[1];
            } else {
                log.error("User name is not specified");
                return;
            }
            if (reqID != null) {
                String voName = getVO();
                if (voName != null) {
                    if (hasOption("decision")) {
                        String decision = getOption("decision").getValues()[0];
                        shell.vote(reqID, decision);
                    } else {
                        log.error("Decision is not specified");
                        return;
                    }
                } else {
                    log.error("VO is not specified");
                    return;
                }
            } else {
                log.error("User is not found");
                return;
            }
            return;
        } else {
            log.error("Unrecognized argument value " + type + " for option -add");
            return;
        }
    }

    /**
     * "share" option processing
     */
    private void share() {
        String type = getOption("share").getValues()[0];
        if (!type.equals(RESOURCE)) {
            log.error("Unrecognized argument value " + type + " for option -share");
            return;
        }
        String userContext = getUser();
        if (userContext == null) {
            log.error("User is not defined: use set or -user");
            return;
        }
        CoGObject editobject = null;
        String objectName = null;
        String objectID = null;
        if (getOption("share").getValues().length > 1) {
            objectName = getOption("share").getValues()[1];
        } else {
            if (hasOption("id")) {
                objectID = getOption("id").getValues()[0];
            } else {
                log.error("Object name or id is not specified");
                return;
            }
        }
        String voName = null;
        if (hasOption("vo")) {
            voName = getOptionValue("vo");
        } else {
            log.error("VO is not specified");
            return;
        }
        shell.shareResource(userContext, objectName, objectID, voName);
    }

    /**
     * "revoke" option processing
     */
    private void revoke() {
        String type = getOption("revoke").getValues()[0];
        if (!type.equals(RESOURCE)) {
            log.error("Unrecognized argument value " + type + " for option -revoke");
            return;
        }
        String userContext = getUser();
        if (userContext == null) {
            log.error("User is not defined: use set or -user");
            return;
        }
        CoGObject editobject = null;
        String objectName = null;
        String objectID = null;
        if (getOption("revoke").getValues().length > 1) {
            objectName = getOption("revoke").getValues()[1];
        } else {
            if (hasOption("id")) {
                objectID = getOption("id").getValues()[0];
            } else {
                log.error("Object name or id is not specified");
                return;
            }
        }
        String voName = null;
        if (hasOption("vo")) {
            voName = getOptionValue("vo");
        } else {
            log.error("VO is not specified");
            return;
        }
        shell.revokeResource(userContext, objectName, objectID, voName);
    }

    /**
     * "retrieve" option processing
     */
    private void retrieve() {
        String type = getOption("retrieve").getValues()[0];
        if (!type.equals(PROXY)) {
            log.error("Unrecognized argument value " + type + " for option -retrieve");
            return;
        }
        String proxyServer = null;
        int proxyPort = 0;
        String username = null;
        String password = null;
        int lifetime = 0;
        String filename = "";
        if (getOption("retrieve").getValues().length > 1) {
            filename = getOption("retrieve").getValues()[1];
        } else {
            log.error("Proxy name is not specified");
            return;
        }
        if (hasOption("server")) {
            proxyServer = getOptionValue("server");
        }
        if (hasOption("port")) {
            String strPort = getOptionValue("port");
            proxyPort = Integer.parseInt(strPort);
        }
        if (hasOption("lifetime")) {
            String strLife = getOptionValue("lifetime");
            lifetime = Integer.parseInt(strLife);
        }
        if (hasOption("user")) {
            username = getOptionValue("user");
        } else {
            username = getUser();
            if (username == null) {
                log.error("Username is required");
                return;
            }
        }
        try {
            ConsoleReader reader = new ConsoleReader();
            reader.setInput(getContext().getIo().__in);
            password = reader.readLine("Enter passphrase:", '*');
        } catch (Exception e) {
            log.error("passphrase exception", e);
        }
        shell.retrieveCertificate(proxyServer, proxyPort, username, password, lifetime, filename);
    }

    /**
     * "update" option processing
     */
    private void update() {
        String type = getOption("update").getValues()[0];
        if (!type.equals(PROXY)) {
            log.error("Unrecognized argument value " + type + " for option -update");
            return;
        }
        String proxyServer = null;
        int proxyPort = 0;
        String username = null;
        String password = null;
        int lifetime = 0;
        String filename = "";
        if (getOption("update").getValues().length > 1) {
            filename = getOption("update").getValues()[1];
        } else {
            log.error("Proxy name is not specified");
            return;
        }
        if (hasOption("server")) {
            proxyServer = getOptionValue("server");
        }
        if (hasOption("port")) {
            String strPort = getOptionValue("port");
            proxyPort = Integer.parseInt(strPort);
        }
        if (hasOption("lifetime")) {
            String strLife = getOptionValue("lifetime");
            lifetime = Integer.parseInt(strLife);
        }
        if (hasOption("user")) {
            username = getOptionValue("user");
        } else {
            username = getUser();
            if (username == null) {
                log.error("Username is required");
                return;
            }
        }
        try {
            ConsoleReader reader = new ConsoleReader();
            reader.setInput(getContext().getIo().__in);
            password = reader.readLine("Enter passphrase:", '*');
        } catch (Exception e) {
            log.error("passphrase exception", e);
        }
        shell.retrieveCertificate(proxyServer, proxyPort, username, password, lifetime, filename);
    }

    /**
     * "run" option processing
     */
    private void runJob() {
        String type = getOption("run").getValues()[0];
        if (type.equals("job")) {
            String voName = getVO();
            if (voName != null) {
                if (hasOption("executable")) {
                    String exec = getOptionValue("executable");
                    printResult(shell.runJob(voName, exec), type, voName, null, getUser(), null);
                } else {
                    log.error("Executable is not specified");
                    return;
                }
            } else {
                log.error("VO is not specified");
                return;
            }
        } else {
            log.error("Unrecognized argument value " + type + " for option -run");
            return;
        }
    }

    /**
     * Print CoGSet with default attrbiutes.
     * @param set : CoGSet
     * @param type : set type
     */
    private void printDefault(CoGSet set, String type) {
        String att, fmt;
        if (type.equals(VO)) {
            att = ATT_VO;
            fmt = FMT_VO;
        } else if (type.equals(VO_GROUP)) {
            att = ATT_VO_GROUP;
            fmt = FMT_VO_GROUP;
        } else if (type.equals(VO_ROLE)) {
            att = ATT_VO_ROLE;
            fmt = FMT_VO_ROLE;
        } else if (type.equals(VO_MEMBERSHIP)) {
            att = ATT_VO_MEMBERSHIP;
            fmt = FMT_VO_MEMBERSHIP;
        } else if (type.equals(VO_REQUEST)) {
            att = ATT_VO_REQUEST;
            fmt = FMT_VO_REQUEST;
        } else if (type.equals(USER)) {
            att = ATT_USER;
            fmt = FMT_USER;
        } else if (type.equals(SSONAME)) {
            att = ATT_SSONAME;
            fmt = FMT_SSONAME;
        } else if (type.equals(VO)) {
            att = ATT_VO;
            fmt = FMT_VO;
        } else if (type.equals(RESOURCE)) {
            att = ATT_RESOURCE;
            fmt = FMT_RESOURCE;
        } else {
            log.error("Type " + type + " is not found");
            return;
        }
        CoGObjectsUtil.printFormatted(set.getListAsArray(), "header=" + att, "attribute=" + att, "format=" + fmt);
    }

    /**
     * Print CoGSet with custom attrbiutes.
     * @param set : CoGSet
     * @param attributes : attributes array
     */
    private void printCustom(CoGSet set, String[] attributes) {
        String att = CoGObjectsUtil.formatPrintAtt(attributes);
        int count = attributes.length;
        CoGObjectsUtil.printFormatted(set.getListAsArray(), "header=" + att, "attribute=" + att, CoGObjectsUtil.formatPrintWidth(count));
    }

    /**
     * Find vo context.
     *
     * @return vo name
     */
    private String getVO() {
        if (hasOption("vo")) {
            return getOption("vo").getValues()[0];
        } else if (voname != null) {
            return voname;
        } else {
            return null;
        }
    }

    /**
     * Find user context.
     * @param option : option name
     *
     * @return user name
     */
    private String getUser() {
        if (hasOption("user")) {
            return getOption("user").getValues()[0];
        } else if (username != null) {
            return username;
        } else {
            return null;
        }
    }

    private void printResult(Object res, String title, String vo, String vo_id, String user, String user_id) {
        String v;
        String u;
        if (vo != null) {
            v = vo;
        } else if (vo_id != null) {
            v = vo_id;
        } else {
            v = "none";
        }
        if (user != null) {
            u = user;
        } else if (user_id != null) {
            u = user_id;
        } else {
            u = "none";
        }
        String eol = System.getProperty("line.separator");
        log.info(title + "(vo: " + v + ", user:" + u + ")" + eol + res);
    }
}
