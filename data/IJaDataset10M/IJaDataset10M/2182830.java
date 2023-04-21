package com.peterhi.web.server.commands;

import com.peterhi.StatusCode;
import com.peterhi.data.Account;
import com.peterhi.data.DB;
import com.peterhi.util.Str;
import com.peterhi.web.client.WebCommandResult;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.hibernate.Session;

/**
 *
 * @author YUN TAO
 */
public class WebAccCommand implements WebCommand {

    private static final String LIST = "list";

    private static final String ADD = "add";

    private static final String REMOVE = "remove";

    private static final String ENABLE = "enable";

    private static final String DISABLE = "disable";

    private static final String LOGIN = "login";

    private static final String LOGOUT = "logout";

    private static final String REACTIVATE = "reactivate";

    private static final String NAME = "name";

    private static final String EMAIL = "email";

    private static final String PASS = "pass";

    private Options options;

    public WebAccCommand() {
        options = new Options();
        options.addOption(LIST, false, "list all accounts");
        options.addOption(ADD, false, "add an account");
        options.addOption(REMOVE, false, "remove an account");
        options.addOption(ENABLE, false, "enable an account");
        options.addOption(DISABLE, false, "disable an account");
        options.addOption(LOGIN, false, "set login to an account");
        options.addOption(LOGOUT, false, "set logout to an account");
        options.addOption(REACTIVATE, false, "reactivate the account and send an activation email");
        Option name = OptionBuilder.withArgName(NAME).hasArg().withDescription("account name").create(NAME);
        Option email = OptionBuilder.withArgName(EMAIL).hasArg().withDescription("account email").create(EMAIL);
        Option pass = OptionBuilder.withArgName(PASS).hasArg().withDescription("account password").create(PASS);
        options.addOption(name);
        options.addOption(email);
        options.addOption(pass);
    }

    public WebCommandResult execute(Object sender, String command) {
        try {
            CommandLineParser parser = new PosixParser();
            CommandLine cmdLine = parser.parse(options, command.split(" "));
            if (cmdLine.hasOption(LIST)) {
                return list();
            } else if (cmdLine.hasOption(ADD)) {
                return add(cmdLine);
            } else if (cmdLine.hasOption(REMOVE)) {
                return remove(cmdLine);
            } else if (cmdLine.hasOption(ENABLE)) {
                return setEnabled(cmdLine, true);
            } else if (cmdLine.hasOption(DISABLE)) {
                return setEnabled(cmdLine, false);
            } else if (cmdLine.hasOption(LOGIN)) {
                return setLogin(cmdLine, true);
            } else if (cmdLine.hasOption(LOGOUT)) {
                return setLogin(cmdLine, false);
            } else if (cmdLine.hasOption(REACTIVATE)) {
                return reactivate(cmdLine);
            } else {
                return new WebCommandResult(usage(), StatusCode.NotFound.ordinal());
            }
        } catch (UnrecognizedOptionException ex) {
            ex.printStackTrace();
            return new WebCommandResult(usage(), StatusCode.Fail.ordinal());
        } catch (Exception ex) {
            ex.printStackTrace();
            return new WebCommandResult(ex.toString(), StatusCode.Fail.ordinal());
        }
    }

    private WebCommandResult add(CommandLine cmdLine) {
        String name = cmdLine.getOptionValue(NAME);
        String email = cmdLine.getOptionValue(EMAIL);
        String password = cmdLine.getOptionValue(PASS);
        if (Str.empty(name)) {
            return new WebCommandResult("please specify -" + NAME, StatusCode.NotFound.ordinal());
        }
        if (Str.empty(email)) {
            return new WebCommandResult("please specify -" + EMAIL, StatusCode.NotFound.ordinal());
        }
        if (Str.empty(password)) {
            return new WebCommandResult("please specify -" + PASS, StatusCode.NotFound.ordinal());
        }
        Session s = null;
        try {
            s = DB.begin();
            Account account = new Account(email);
            account.setAccName(name);
            account.setAccPass(password.toCharArray());
            s.save(account);
            DB.commit(s);
            return new WebCommandResult("ok (id: " + account.getAccId() + ")", StatusCode.OK.ordinal());
        } catch (Exception ex) {
            ex.printStackTrace();
            if (s != null) {
                try {
                    DB.rollback(s);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return new WebCommandResult(ex.toString(), StatusCode.Fail.ordinal());
        }
    }

    private WebCommandResult setEnabled(CommandLine cmdLine, boolean value) {
        String email = cmdLine.getOptionValue(EMAIL);
        if (Str.empty(email)) {
            return new WebCommandResult("please specify -" + EMAIL, StatusCode.NotFound.ordinal());
        }
        Session s = null;
        try {
            s = DB.begin();
            Account account = (Account) DB.queryOne(s, Account.class, Account.F_ACC_EMAIL, email);
            account.setAccEnabled(value);
            s.update(account);
            DB.commit(s);
            return new WebCommandResult("ok (id: " + account.getAccId() + ")", StatusCode.OK.ordinal());
        } catch (Exception ex) {
            ex.printStackTrace();
            if (s != null) {
                try {
                    DB.rollback(s);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return new WebCommandResult(ex.toString(), StatusCode.Fail.ordinal());
        }
    }

    private WebCommandResult setLogin(CommandLine cmdLine, boolean value) {
        String email = cmdLine.getOptionValue(EMAIL);
        if (Str.empty(email)) {
            return new WebCommandResult("please specify -" + EMAIL, StatusCode.NotFound.ordinal());
        }
        Session s = null;
        try {
            s = DB.begin();
            Account account = (Account) DB.queryOne(s, Account.class, Account.F_ACC_EMAIL, email);
            account.setAccOnline(value);
            s.update(account);
            DB.commit(s);
            return new WebCommandResult("ok (id: " + account.getAccId() + ")", StatusCode.OK.ordinal());
        } catch (Exception ex) {
            ex.printStackTrace();
            if (s != null) {
                try {
                    DB.rollback(s);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return new WebCommandResult(ex.toString(), StatusCode.Fail.ordinal());
        }
    }

    private WebCommandResult remove(CommandLine cmdLine) {
        String email = cmdLine.getOptionValue(EMAIL);
        if (Str.empty(email)) {
            return new WebCommandResult("please specify -" + EMAIL, StatusCode.NotFound.ordinal());
        }
        Session s = null;
        try {
            s = DB.begin();
            Account account = (Account) DB.queryOne(s, Account.class, Account.F_ACC_EMAIL, email);
            if (account != null) {
                s.delete(account);
            }
            DB.commit(s);
            if (account == null) {
                return new WebCommandResult("no such email: " + email, StatusCode.NotFound.ordinal());
            }
            return new WebCommandResult("ok (id: " + account.getAccId() + ")", StatusCode.OK.ordinal());
        } catch (Exception ex) {
            ex.printStackTrace();
            if (s != null) {
                try {
                    DB.rollback(s);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return new WebCommandResult(ex.toString(), StatusCode.Fail.ordinal());
        }
    }

    private WebCommandResult list() {
        StringBuffer sb = new StringBuffer();
        Session s = null;
        try {
            s = DB.begin();
            List list = DB.getAll(s, Account.class);
            if (list.size() <= 0) {
                sb.append("list is empty");
            } else {
                for (Object o : list) {
                    sb.append(o.toString());
                    sb.append("\n");
                }
            }
            DB.commit(s);
            return new WebCommandResult(sb.toString(), StatusCode.OK.ordinal());
        } catch (Exception ex) {
            ex.printStackTrace();
            if (s != null) {
                try {
                    DB.rollback(s);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return new WebCommandResult(ex.toString(), StatusCode.Fail.ordinal());
        }
    }

    private WebCommandResult reactivate(CommandLine cmdLine) {
        if (!cmdLine.hasOption(NAME)) {
            return new WebCommandResult("please specify -" + NAME, StatusCode.NotFound.ordinal());
        }
        String name = cmdLine.getOptionValue(NAME);
        return null;
    }

    public String usage() {
        HelpFormatter formatter = new HelpFormatter();
        StringWriter writer = new StringWriter();
        PrintWriter printer = new PrintWriter(writer);
        String usage = String.format("acc [-%s|-%s|-%s|-%s|-%s|-%s|-%s]", LIST, ADD, REMOVE, ENABLE, DISABLE, LOGIN, LOGOUT);
        String header = "perform various peterhi account operations";
        String footer = "Copyright (c) Yun Tao Hai";
        formatter.printHelp(printer, 100, usage, header, options, 0, 1, footer);
        return writer.toString();
    }
}
