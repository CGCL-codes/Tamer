package cai.flow.struct;

import java.io.IOException;
import java.util.LinkedList;
import java.util.ListIterator;
import cai.flow.collector.interpretator.PT_Error;
import cai.flow.collector.interpretator.PT_ExecError;
import cai.flow.collector.interpretator.RuleSet;
import cai.flow.packets.Flow;
import cai.utils.Program;
import cai.utils.Syslog;

class Rule {

    public String protocol;

    public RuleSet program;

    public Rule(String protocol, RuleSet program) {
        this.protocol = protocol;
        this.program = program;
    }
}

public abstract class DataProtocol {

    private static LinkedList protocol_list = init_protocol_list();

    private static LinkedList init_protocol_list() {
        LinkedList ret = new LinkedList();
        try {
            Program program = new Program("Protocols");
            ListIterator e = program.listIterator();
            while (e.hasNext()) {
                String[] obj = (String[]) e.next();
                try {
                    RuleSet rule = RuleSet.create(obj[1], obj[0]);
                    ret.add(new Rule(obj[0], rule));
                } catch (PT_Error exc) {
                    program.error(exc.toString());
                }
            }
        } catch (IOException exc) {
            Syslog.log.syslog(Syslog.LOG_ERR, "I/O error while reading Protocols definition");
            exc.printStackTrace();
        }
        return ret;
    }

    public static String aggregate(Flow flow) {
        ListIterator e = protocol_list.listIterator(0);
        while (e.hasNext()) {
            Rule rule = (Rule) e.next();
            try {
                if (rule.program.exec(flow)) {
                    return rule.protocol;
                }
            } catch (PT_ExecError exc) {
                Syslog.log.syslog(Syslog.LOG_ERR, "BUG: aggregate protocol: " + exc.toString());
            }
        }
        return "Other";
    }
}
