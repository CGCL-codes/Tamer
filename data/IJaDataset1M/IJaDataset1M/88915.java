package nbench.engine.dbgw;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import nbench.common.BackendEngine;
import nbench.common.BackendEngineClient;
import nbench.common.NBenchException;
import nbench.common.NameAndType;
import nbench.common.ValueType;
import nbench.engine.helper.ActionSpec;
import nbench.engine.helper.TrSpec;

/**
 * @author heyman
 *
 */
public class DBGWBackendEngine implements BackendEngine {

    private Properties props;

    private static int STATUS_CREATED = 0;

    private static int STATUS_CONFIGURED = 1;

    private static int STATUS_CONSOLIDATED = 2;

    private int status;

    private String conf;

    private static String sqlmap_header = "<?xml version=\"1.0\" encoding=\"EUC-KR\" ?>\n" + "<sqls>" + "<using-db module-id=\"dsp\"/>\n";

    private static String sqlmap_footer = "</sqls>\n";

    private HashMap<String, List<ActionSpec>> action_specs_map;

    HashMap<String, TrSpec> prepared_map;

    String dbgw_ip;

    int dbgw_port;

    int dbgw_con_timeout;

    public DBGWBackendEngine() {
        this.status = STATUS_CREATED;
    }

    private NBenchException error(String message) {
        return new NBenchException("[DBGW BE]" + message);
    }

    private void add_action_spec(String spec, String body) {
        ActionSpec as = new ActionSpec(spec, body);
        List<ActionSpec> actions = action_specs_map.get(as.id);
        if (actions == null) {
            ArrayList<ActionSpec> nb = new ArrayList<ActionSpec>(2);
            nb.add(as);
            action_specs_map.put(as.id, nb);
        } else {
            actions.add(as);
        }
    }

    private void loadActionSpecs(String path) throws NBenchException {
        action_specs_map = ActionSpec.load(path);
    }

    public void configure(Properties props) throws NBenchException {
        this.props = props;
        if (status != STATUS_CREATED) throw error("invalid status");
        conf = props.getProperty("configure");
        if (conf == null) throw error("should have 'configure' property");
        prepared_map = new HashMap<String, TrSpec>();
        loadActionSpecs(props.getProperty("actions"));
        status = STATUS_CONFIGURED;
    }

    private void make_sqlmap_for_dbgw(File dir, String name) throws Exception {
        File sqlmapfile = new File(dir.toString(), File.separator + name);
        FileOutputStream fout = new FileOutputStream(sqlmapfile);
        StringBuffer sb = new StringBuffer();
        sb.append(sqlmap_header);
        for (TrSpec trs : prepared_map.values()) {
            for (ActionSpec action_spec : trs.action_specs) {
                action_spec.sqlmapid = trs.name;
                switch(action_spec.stmt_type) {
                    case ActionSpec.SELECT:
                        sb.append("<select name=\"" + trs.name + "\">\n");
                        break;
                    case ActionSpec.DELETE:
                        sb.append("<delete name=\"" + trs.name + "\">\n");
                        break;
                    case ActionSpec.UPDATE:
                        sb.append("<update name=\"" + trs.name + "\">\n");
                        break;
                    case ActionSpec.INSERT:
                        sb.append("<insert name=\"" + trs.name + "\">\n");
                        break;
                    case ActionSpec.OTHER:
                    default:
                        throw error("unkown action type:" + action_spec.stmt_type);
                }
            }
            for (NameAndType nat : trs.in) {
                String type;
                switch(nat.getType()) {
                    case ValueType.INT:
                        type = "int";
                        break;
                    case ValueType.NUMERIC:
                        type = "double";
                        break;
                    case ValueType.STRING:
                        type = "string";
                        break;
                    case ValueType.TIMESTAMP:
                        type = "date";
                        break;
                    default:
                        throw error("Unknown Data type");
                }
                sb.append("<param name=\"" + nat.getName() + "\"" + " type=\"" + type + "\"/>\n");
            }
            for (ActionSpec action_spec : trs.action_specs) {
                sb.append(make_query(action_spec.body));
            }
            for (ActionSpec action_spec : trs.action_specs) {
                switch(action_spec.stmt_type) {
                    case ActionSpec.SELECT:
                        sb.append("</select>\n");
                        break;
                    case ActionSpec.DELETE:
                        sb.append("</delete>\n");
                        break;
                    case ActionSpec.UPDATE:
                        sb.append("</update>\n");
                        break;
                    case ActionSpec.INSERT:
                        sb.append("</insert>\n");
                        break;
                    case ActionSpec.OTHER:
                        throw error("unkown action type:" + action_spec.stmt_type);
                    default:
                }
            }
        }
        sb.append(sqlmap_footer);
        fout.write(sb.toString().getBytes());
        fout.close();
    }

    private String make_query(String query) throws Exception {
        StringBuffer sb = new StringBuffer();
        StringReader sr = new StringReader(query);
        sb.append("<![CDATA[ \n");
        int c;
        int state = 0;
        while ((c = sr.read()) >= 0) {
            char C = (char) c;
            if (C == '#') {
                if (state == 0) {
                    sb.append(":");
                    state = 1;
                } else {
                    state = 0;
                }
            } else {
                sb.append(C);
            }
        }
        sb.append("]]>\n");
        return sb.toString();
    }

    public void consolidateForRun() throws NBenchException {
        dbgw_ip = props.getProperty("dbgw_ip");
        dbgw_port = Integer.parseInt(props.getProperty("dbgw_port"));
        dbgw_con_timeout = Integer.parseInt(props.getProperty("dbgw_con_timeout"));
        try {
            File mydir = new File(props.getProperty("basedir") + File.separator + "engine");
            mydir.mkdir();
            File[] files = mydir.listFiles();
            for (int i = 0; i < files.length; i++) {
                File f = files[i];
                if (f.isFile()) f.delete();
            }
            make_sqlmap_for_dbgw(mydir, "sqls.xml");
        } catch (Exception e) {
            e.printStackTrace();
            throw error(e.toString());
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String in = null;
        try {
            System.out.println("Press Enter Key when you ready");
            in = br.readLine();
        } catch (Exception e) {
            e.printStackTrace();
        }
        status = STATUS_CONSOLIDATED;
    }

    public BackendEngineClient createClient() throws NBenchException {
        if (status != STATUS_CONSOLIDATED) throw error("invalid status");
        return new DBGWBackendEngineClient(this);
    }

    public void prepareForStatement(String name, List<NameAndType> in, List<NameAndType> out) throws NBenchException {
        if (status != STATUS_CONFIGURED) {
            throw error("invalid status");
        }
        List<ActionSpec> actions = action_specs_map.get(name);
        if (actions == null) throw error("no action is specified for action:" + name);
        TrSpec pre = new TrSpec(name, in, out, actions);
        prepared_map.put(name, pre);
    }
}
