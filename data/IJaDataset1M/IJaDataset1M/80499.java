package moxie.net;

import antiquity.rpc.api.RpcCall;
import antiquity.rpc.api.RpcReply;
import antiquity.rpc.api.RpcRegisterReq;
import antiquity.rpc.api.RpcRegisterResp;
import antiquity.rpc.api.ProcInfo.ProcKey;
import antiquity.rpc.api.ProcInfo.ProcValue;
import antiquity.rpc.impl.RpcServerStage;
import ostore.util.NodeId;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import java.util.Map;
import java.util.HashMap;
import org.acplt.oncrpc.XdrVoid;
import seda.sandStorm.api.ConfigDataIF;
import seda.sandStorm.api.QueueElementIF;
import seda.sandStorm.api.SinkIF;
import seda.sandStorm.api.StagesInitializedSignal;
import static bamboo.util.Curry.*;

public class PingServer extends ostore.util.StandardStage {

    private boolean stages_initialized = false;

    private NodeId rpc_node_id;

    private RpcServerStage rpc_server_stage;

    public void init(ConfigDataIF config) throws Exception {
        event_types = new Object[] { seda.sandStorm.api.StagesInitializedSignal.class };
        super.init(config);
        String debug_level_st = config.getString("DebugLevel");
        Level debug_level = Level.toLevel(debug_level_st, Level.WARN);
        logger.warn("Setting debug level to " + debug_level + ".");
        logger.setLevel(debug_level);
        int rpc_server_port = config.getInt("RpcServerPort");
        rpc_node_id = new NodeId(rpc_server_port, my_node_id.getAddress());
        return;
    }

    public void handleEvent(QueueElementIF event) {
        if (event instanceof StagesInitializedSignal) {
            logger.info("Received StagesInitializedSignal.");
            stages_initialized = true;
            rpc_server_stage = RpcServerStage.getInstance(rpc_node_id);
            assert (rpc_server_stage != null);
            registerRpcProcedures();
        } else {
            BUG("Unexpected event: " + event);
        }
        return;
    }

    private void registerRpcProcedures() {
        if (logger.isInfoEnabled()) logger.info("Registering RPC procedures.");
        boolean client = false;
        long rpc_app_id = bamboo.router.Router.app_id(PingServer.class);
        Object user_data = null;
        SinkIF sink = (SinkIF) null;
        RpcRegisterReq rpc_register_req = new RpcRegisterReq(getPingProcedureMap(), client, rpc_app_id, user_data, sink);
        rpc_register_req.cb = register_rpc_proc_done;
        rpc_register_req.handlers = new HashMap<ProcKey, Thunk1<RpcCall>>();
        for (ProcKey key : rpc_register_req.procedures.keySet()) rpc_register_req.handlers.put(key, handle_rpc_call);
        rpc_server_stage.handleEvent(rpc_register_req);
        return;
    }

    private Thunk2<RpcRegisterReq, RpcRegisterResp> register_rpc_proc_done = new Thunk2<RpcRegisterReq, RpcRegisterResp>() {

        public void run(RpcRegisterReq rpc_register_req, RpcRegisterResp rpc_register_resp) {
            if (logger.isInfoEnabled()) logger.info("Done register RPC messages.");
            for (Map.Entry<ProcKey, Integer> entry : rpc_register_resp.responses.entrySet()) if (!RpcRegisterResp.SUCCESS.equals(entry.getValue())) BUG("Failed to register RPC procedure: " + " proc_key=" + entry.getKey());
            return;
        }
    };

    private Thunk1<RpcCall> handle_rpc_call = new Thunk1<RpcCall>() {

        public void run(RpcCall rpc_call) {
            int proc_num = rpc_call.proc.getProcNum();
            switch(proc_num) {
                case ping.ping_null_1:
                    handleNull(rpc_call);
                    break;
                case ping.ping_1:
                    handlePing(rpc_call);
                    break;
                default:
                    logger.fatal("Received unexpected request: " + "proc_num=" + proc_num);
                    break;
            }
            return;
        }
    };

    private void handleNull(RpcCall rpc_call) {
        logger.info("ping_null - start:");
        XdrVoid args = XdrVoid.XDR_VOID;
        RpcReply rpc_reply = new RpcReply(args, rpc_call);
        rpc_server_stage.handleEvent(rpc_reply);
        logger.info("ping_null - done:");
        return;
    }

    private void handlePing(RpcCall rpc_call) {
        ping_args args = (ping_args) rpc_call.args;
        String msg = args.msg;
        logger.info("ping - start: msg=\"" + msg + "\"");
        ping_result result = new ping_result();
        result.status = PingStatus.PING_STATUS_OK;
        result.payload = new byte[args.server_payload_size];
        RpcReply rpc_reply = new RpcReply(result, rpc_call);
        rpc_server_stage.handleEvent(rpc_reply);
        logger.info("ping - done: msg=\"" + msg + "\"");
        return;
    }

    private static Map<ProcKey, ProcValue> ping_proc_map;

    public static Map<ProcKey, ProcValue> getPingProcedureMap() {
        if (ping_proc_map != null) return ping_proc_map;
        int PING_API = ping.PING_API;
        int PING_API_VERSION = ping.PING_API_VERSION;
        ping_proc_map = new HashMap<ProcKey, ProcValue>();
        ping_proc_map.put(new ProcKey(PING_API, PING_API_VERSION, ping.ping_null_1), new ProcValue(XdrVoid.class, XdrVoid.class));
        ping_proc_map.put(new ProcKey(PING_API, PING_API_VERSION, ping.ping_1), new ProcValue(ping_args.class, ping_result.class));
        return ping_proc_map;
    }
}
