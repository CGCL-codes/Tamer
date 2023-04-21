package czestmyr.jjsched;

/**
 * This class provides help by returning strings for various help topics or JJSched commands
 */
public class Help {

    /**
     * Returns the help string that is printed out when no help topic is specified.
     * @return      The basic help string
     */
    public static String getBasicHelp() {
        return "JJSCHED - Java Jabber Scheduler\n" + "A simple XMPP scheduling application, version 0.1\n" + "Copyright Cestmir 'Czestmyr' Houska, 2008\n" + "For list of available commands, send 'help commands'\n" + "For help on a given command, send 'help command'\n" + "For help on other topics, send 'help topics'\n" + "For information about a quick tutorial, send 'help tutorial'\n\n" + "DISCLAIMER: The information you provide to JJSched (plans, description, etc...) will be visible by anyone\n" + "who is able to get access to server. Please, don't store personal or confidential information on JJSched.";
    }

    /**
     * Returns help on given topic.
     *
     * @param   topic   The topic for which we want to see the help
     * @return          The string with help for teh given topic
     */
    public static String getTopicHelp(String topic) {
        if (topic.equals("topics")) {
            return "List of available help topics. To get help for a given topic, type 'help topic'\n" + "commands\n" + "groupspec\n" + "timespec\n" + "topics\n" + "tutorial\n" + "tutorial2\n" + "tutorial3\n" + "tutorial4";
        }
        if (topic.equals("commands")) {
            return "List of available commands. Type 'help command' to get help on a given command\n" + "admin\n" + "echo\n" + "group\n" + "help\n" + "list\n" + "offset\n" + "plan\n" + "register\n" + "setoffset\n" + "subscribe\n" + "time\n" + "unregister\n" + "unsubscribe\n" + "quit";
        }
        if (topic.equals("groupspec")) {
            return "Groupspec specifies the group in the \"group\" and other commands.\nIt can have one of two forms:\n1>> /groupname/\n2>> number /groupnumber/\nThe first one lets you specify the group by writing its name whereas the second one\nallows you to specify the group by its ID number.\nThis also implies that you cannot have a group named \"number\", but who cares :)\n" + "Notes:\nDon't forget to write /groupname/ as one token. More specifically,\nif the name consists of more words, you have to put in into parentheses:\n\"Example of a group name\"\n" + "See also:\n" + "group";
        }
        if (topic.equals("timespec")) {
            return "Timespec is a time specifier that is used in various commands.\nIn terms of formal grammars, it is defined as follows:\nTIMESPEC = ORIGIN [+ TIMEDIFF] [- TIMEDIFF]\nORIGIN = {now|tomorrow|next week|TIME}\nTIME = Time is described by a string that has this format: [dd.MM[.yyyy]/]HH:mm[:ss]\nif you don't use the date part of TIME, the current day will be used\nTIMEDIFF = /number/ {seconds|minutes|hours|days|weeks} [TIMEDIFF]\n" + "Notes:\nExamples:\nnow\n\"tomorrow + 1 hours 30 minutes\"\n24.12/19:30:00\n24.12.2008/19:30\n18:00\nDon't forget to enclose the time specifier in double quotes so that it parses as one token\n" + "See also:\n" + "plan";
        }
        if (topic.equals("tutorial")) {
            return "Welcome to JJSched tutorial\nJJSched (pronounce \"jay-jay-sched\") means Java-Jabber-Scheduler\nThis short tutorial should teach you how to use JJSched\nYou will learn the basic commands that you need to use JJSched\nIf you feel curious about any command, type 'help command' to get more information on that command\nTyping 'help commands' will print list of all the available commands\nTo read the next page of this tutorial, type 'help tutorial2'\n" + "See also:\n" + "tutorial2";
        }
        if (topic.equals("tutorial2")) {
            return "So you are now registered to JJSched and would like to know how you can start using it, right?\nFirst, you need to check, whether the server has correct information about your time zone.\nTo see the time that the server thinks is your current local time, type 'time me'\nThe server will respond to you with some time value. If this value is your local time, then you\nare ready to start using JJSched and can go on to page 3. However, if this time differs from your\nlocal time, you need to adjust your time offset. Let's suppose that your local time should be\n14.05.1968/12:20:00, but the server responds with 14.05.1968/13:20:00. This means that your local\ntime should be one hour less. The solution is simple. You just type 'setoffset me \"- 1 hours\"'.\nYou can also specify minutes or even seconds: 'setoffset me \"1 hours 30 minutes 45 seconds\"'\nThe double quotes around the time expression are important, so don't forget them.\nTo read the next page of this tutorial, type 'help tutorial3'\n" + "See also:\n" + "tutorial3";
        }
        if (topic.equals("tutorial3")) {
            return "JJSched tutorial / page 3\nNow that your time offset is correct, you can try planning events in JJSched for yourself and\nJJSched will inform you about the planned event when the time comes.\nTo try this out, type 'plan at \"now + 10 seconds\"'. Wait for ten seconds and then you will\nreceive a message from JJSched, informing you that 'You have a plan for now without a description!'\nThis message is a default message, since you didn't provide any other message. If you want JJSched\nto tell you for instance that 'You need to have a break!', you can do this by typing\n'plan at \"now + 20 minutes\" description \"You need to have a break!\"' and JJSched will remind\nyou of your planned break in 20 minutes.\nTo read the next page of this tutorial, type 'help tutorial4'\n" + "See also:\n" + "tutorial4";
        }
        if (topic.equals("tutorial4")) {
            return "JJSched tutorial / page 4\nNow you know most of the important stuff. Another useful thing is subscribing to user groups\nIf you know about a user group existing on the JJSched server and you know its name (for instance\n\"The ultimate group\"), you can subscribe to this group by typing 'subscribe \"The ultimate group\"'\nFrom now on, you will receive all planned events from this group until you type\n'unsubscribe \"The ultimate group\"'\nThis is the end of this short introduction to JJSched. To learn more, study the help. (type 'help')\n" + "See also:\n" + "help";
        }
        if (topic.equals("admin")) {
            return "Modifies administrator privileges\n" + "1>> admin /user/ /privileges/\n" + "Variant (1): Set the administrator privileges for the given user.\nUser should be specified by the jabber ID.\nPrivileges is an ORed number of following options: 1-admins, 2-users, 4-groups, 8-global settings\n" + "Notes:\nOnly admins with privilege to modify admins can use this command\n";
        }
        if (topic.equals("echo")) {
            return "Echoes back its arguments\n" + "1>> echo /whatever/\n" + "Variant (1): Sends back the command that would be executed if only /whatever/ was sent.\nUseful for determining the contents of macros.\n" + "Notes:\ne.g.: To see contents of macro 'quickplan', send 'echo quickplan'\n";
        }
        if (topic.equals("group")) {
            return "Manipulates or views the user groups\n" + "1>> group add /name/ /description/\n" + "2>> group modify /groupspec/ [description /newdescription/] [name /newname/] [selfsubscription {true|false}]\n" + "3>> group listusers /groupspec/\n" + "4>> group subscribe /groupspec/ /user/\n" + "5>> group unsubscribe /groupspec/ /user/\n" + "6>> group privileges /groupspec/ /user/ /newprivileges/\n" + "7>> group remove /groupspec/\n" + "8>> group listevents /groupspec/\n" + "Variant (1): Adds a new group with the specified name and description\n" + "Variant (2): Modifies the group's description, name or the possibility to (un)subscribe for ordinary users\n" + "Variant (3): Lists all users of the specified group\n" + "Variant (4): Allows the global group administrator or the group administrator to subscribe a user to the group\n" + "Variant (5): Is similar to variant (4), only it unsubscribes the users\n" + "Variant (6): Changes the privileges of a given user for the group. The user has to be subscribed to the group.\nCan be a combination of 1 - modify group, 2 - list group users and events, 4 - plan group events\n" + "Variant (7): Deletes the entire group, its users and planned events (be careful!)\n" + "Variant (8): Lists all planned events for this group. (If you have the privileges to do so)\n" + "Notes:\nVariants 2-8 need you to specify the group in question somehow.\nFor help on this, see groupspec\n" + "See also:\n" + "groupspec, subscribe, unsubscribe";
        }
        if (topic.equals("help")) {
            return "Provides on-line help\n" + "1>> help [/topic/]\n" + "Variant (1): Retrieves either basic help or help on the given topic or command\n" + "Notes:\nNotes on help syntax: arguments are denoted by a phrase, describing the argument\nand enclosed in slash-brackets: //. See syntax of help at the top to know what i mean.\nUnmandatory arguments or parameters are enclosed in square brackets: []\nA set of keywords, you have to choose from, is described by putting these keywords in\ncurly brackets and separating them by vertical bars like this: {keyword1|keyword2|keyword3}\nIf there are more possibilities of invoking the command, each of them is preceded by \"N>>\"\nwhere 'N' denotes a number that is used later in the command description to refer to this\nvariant of the given command.\n" + "See also:\n" + "tutorial";
        }
        if (topic.equals("list")) {
            return "Lists various entities\n" + "1>> list {users|admins|groups|macros|events}\n" + "Variant (1): Prints out a list of various entites\nEntities can be either of users, admins, groups, macros or your planned events.\nFor info about listing group planned events, look at the group command.\nListing group users and admins is also made with group command.\n" + "See also:\n" + "group";
        }
        if (topic.equals("offset")) {
            return "Prints a user's time offset\n" + "1>> offset [/user/]\n" + "Variant (1): Sends back the time offset of the given user.\nIf no user is specified or user is 'me', your own offset is sent.\nIf you are an admin with the privilege to modify users, you can view other users'\noffsets as well.\n" + "See also:\n" + "setoffset, time";
        }
        if (topic.equals("plan")) {
            return "Plans an event\n" + "1>> plan at /timespec/ [{user|group} /name or groupspec/] [description /message/]\n" + "Variant (1): Plans an event at the given time for the given user with the given description\nNote that the arguments can come in any order\nArguments:\n/timespec/ describes the time for the event. The time should be in the sending user time\nand will be converted to server time. See 'help timespec' for more info.\n{user|group} /name or groupspec/ describes the single user or whole group that will be informed when this event occurs.\nIf not specified, this argument equals to the sending user. See \"groupspec\" for more info on specifying groups.\n/message/ serves as a message that will be shown when the event occurs. Don't forget to enclose\nthe message in double quotes: \"message\" if it contains spaces or line breaks.\n" + "See also:\n" + "timespec, groupspec";
        }
        if (topic.equals("register")) {
            return "Registers users on this server\n" + "1>> register /user/\n" + "Variant (1): Registers a user to the server, so that the user can start using JJSCHED.\n/user/ can be either 'me' or a Jabber ID, such as user@jabber.net\nThe server can forbid users to register themselves.\nRegistration of other users than 'me' can only be done by the administrator with given privileges\n" + "See also:\n" + "unregister";
        }
        if (topic.equals("setoffset")) {
            return "Sets a user's time offset\n" + "1>> setoffset /user/ /timediff/\n" + "Variant (1): Sets the time offset of local time of the user.\ntimediff is the same as TIMEDIFF in timespec (type 'help timespec' for more info)\n" + "See also:\n" + "offset, time, timespec";
        }
        if (topic.equals("subscribe")) {
            return "Subscribes you to a group\n" + "1>> subscribe /groupname/\n" + "Variant (1): Subscribes you to the group with name /groupname/\nUnlike the \"group subscribe\" command, this command allows you to subscribe only yourself.\nIf the group doesn't allow self-subscription, you will have to write to the admin of the group.\n" + "See also:\n" + "unsubscribe, group";
        }
        if (topic.equals("time")) {
            return "Prints the current time\n" + "1>> time [/user/]\n" + "Variant (1): Sends back the actual server time or a given user's time.\nIf you send this command without the parameters, it returns the server time.\nOr you can type 'time me' to see your time, as the server sees it, according to your offset.\nOr, if you're an admin with the right to modify users, you can see local time of any registered user.\n" + "See also:\n" + "offset, setoffset";
        }
        if (topic.equals("unregister")) {
            return "Unregisters users from this server\n" + "1>> unregister /user/\n" + "Variant (1): Unregisters a user from the server, deleting all his data (macros and events)\n/user/ can be either 'me' or a Jabber ID, such as user@jabber.net\nAny user can do 'unregister me', so be careful!\nUnregistration of other users than 'me' can only be done by the administrator with\nthe privilege to modify users\n" + "See also:\n" + "register";
        }
        if (topic.equals("unsubscribe")) {
            return "Unsubscribes you from a group\n" + "1>> unsubscribe /groupname/\n" + "Variant (1): Unubscribes you from the group with name /groupname/\nUnlike the \"group unsubscribe\" command, this command allows you to unsubscribe only yourself.\n" + "See also:\n" + "subscribe, group";
        }
        if (topic.equals("quit")) {
            return "Quits the application when in console\n" + "1>> quit\n" + "Variant (1): Quits JJSched\n" + "Notes:\nThis command only works from console\n";
        }
        return "No help on that command or command does not exist";
    }
}
