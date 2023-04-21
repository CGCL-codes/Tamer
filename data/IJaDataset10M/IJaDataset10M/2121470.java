package antiquity.gw.impl;

import antiquity.util.Extent;
import antiquity.util.XdrUtils;
import antiquity.util.AntiquityUtils;
import antiquity.gw.api.gw_create_args;
import antiquity.gw.api.gw_append_args;
import antiquity.gw.api.gw_truncate_args;
import antiquity.gw.api.gw_snapshot_args;
import antiquity.gw.api.gw_put_args;
import antiquity.gw.api.gw_get_blocks_args;
import antiquity.gw.api.gw_get_extent_args;
import antiquity.gw.api.gw_get_certificate_args;
import antiquity.gw.api.gw_create_result;
import antiquity.gw.api.gw_append_result;
import antiquity.gw.api.gw_truncate_result;
import antiquity.gw.api.gw_snapshot_result;
import antiquity.gw.api.gw_put_result;
import antiquity.gw.api.gw_get_blocks_result;
import antiquity.gw.api.gw_get_extent_result;
import antiquity.gw.api.gw_get_certificate_result;
import antiquity.gw.api.gw_guid;
import antiquity.gw.api.gw_status;
import antiquity.gw.api.gw_public_key;
import antiquity.gw.api.gw_data_block;
import antiquity.gw.api.gw_certificate;
import antiquity.gw.api.gw_signed_certificate;
import antiquity.gw.api.gw_signed_signature;
import static antiquity.gw.api.gw_api.GW_API;
import static antiquity.gw.api.gw_api.GW_API_VERSION;
import static antiquity.gw.api.gw_api.gw_null_1;
import static antiquity.gw.api.gw_api.gw_put_1;
import static antiquity.gw.api.gw_api.gw_create_1;
import static antiquity.gw.api.gw_api.gw_append_1;
import static antiquity.gw.api.gw_api.gw_truncate_1;
import static antiquity.gw.api.gw_api.gw_snapshot_1;
import static antiquity.gw.api.gw_api.gw_get_extent_1;
import static antiquity.gw.api.gw_api.gw_get_blocks_1;
import static antiquity.gw.api.gw_api.gw_get_certificate_1;
import antiquity.rpc.api.RpcCall;
import antiquity.rpc.api.RpcReply;
import antiquity.rpc.api.RpcRegisterReq;
import antiquity.rpc.api.RpcRegisterResp;
import static antiquity.rpc.api.ProcInfo.ProcKey;
import static antiquity.rpc.api.ProcInfo.ProcValue;
import ostore.util.NodeId;
import ostore.util.SecureHash;
import ostore.util.StandardStage;
import ostore.dispatch.Filter;
import org.acplt.oncrpc.XdrAble;
import org.acplt.oncrpc.XdrVoid;
import org.apache.log4j.Level;
import seda.sandStorm.api.QueueElementIF;
import seda.sandStorm.api.ConfigDataIF;
import seda.sandStorm.api.EventHandlerException;
import seda.sandStorm.api.StagesInitializedSignal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.HashMap;
import java.net.InetAddress;
import java.security.MessageDigest;

/**
 * An event-driven gateway that implements the two-level naming-api.
 * This stage uses only local hashtables to store and/or serve 
 * blocks and extents.
 *
 * @author Hakim Weatherspoon
 * @version $Id: DummyASyncServer.java,v 1.2 2007/02/25 05:40:30 hweather Exp $
 */
public class DummyASyncServer extends ostore.util.StandardStage {

    /** Unique identifier for <code>this</code> stage. */
    private static final long appId = bamboo.router.Router.app_id(DummyASyncServer.class);

    private Map<SecureHash, Extent<gw_public_key, gw_guid, gw_signed_certificate>> key_extents;

    private Map<SecureHash, Extent<gw_public_key, gw_guid, gw_signed_certificate>> hash_extents;

    private Map<SecureHash, gw_data_block> block_map;

    /** Block events b/c DD was not ready */
    private Vector<QueueElementIF> _blockedEvents;

    /** All local stages have been initialized */
    private boolean _stagesInitialized;

    /** All remote procedure calls have been registered */
    private boolean _registered;

    /** {@link java.security.MessageDigest}. */
    private MessageDigest _md;

    /** Constructor: Creates a new <code>DummyASyncServer</code> stage. */
    public DummyASyncServer() throws Exception {
        event_types = new Class[] { seda.sandStorm.api.StagesInitializedSignal.class };
        key_extents = new HashMap<SecureHash, Extent<gw_public_key, gw_guid, gw_signed_certificate>>();
        hash_extents = new HashMap<SecureHash, Extent<gw_public_key, gw_guid, gw_signed_certificate>>();
        block_map = new HashMap<SecureHash, gw_data_block>();
        _blockedEvents = new Vector<QueueElementIF>();
        _md = MessageDigest.getInstance("SHA");
        logger.info("Initializing DummyASyncServer...");
    }

    /** Specified by seda.sandStorm.api.EventHandlerIF */
    public void init(ConfigDataIF config) throws Exception {
        final String method_tag = tag + ".init";
        super.init(config);
        Class[] rpc_msg_types = new Class[] { antiquity.rpc.api.RpcCall.class, antiquity.rpc.api.RpcRegisterResp.class };
        for (Class clazz : rpc_msg_types) {
            Filter filter = new Filter();
            if (!filter.requireType(clazz)) BUG(tag + ": could not require type " + clazz.getName());
            if (antiquity.rpc.api.RpcCall.class.isAssignableFrom(clazz)) {
                if (!filter.requireValue("inbound", new Boolean(true))) BUG(tag + ": could not require inbound = true for " + clazz.getName());
            }
            if (!filter.requireValue("appId", appId)) BUG(tag + ": could not require appId = " + appId + " for " + clazz.getName());
            if (DEBUG) logger.info(tag + ": subscribing to " + clazz);
            classifier.subscribe(filter, my_sink);
        }
        String debug_level_st = config_get_string(config, "DebugLevel");
        Level debug_level = Level.toLevel(debug_level_st, Level.WARN);
        logger.warn("Setting debug level to " + debug_level + ".");
        logger.setLevel(debug_level);
        logger.info(method_tag + ": " + "init done");
    }

    /** Specified by seda.sandStorm.api.EventHandlerIF */
    public void handleEvent(QueueElementIF item) throws EventHandlerException {
        final String method_tag = tag + ".handleEvent";
        if (item instanceof StagesInitializedSignal) {
            handleStagesInitializedSignal((StagesInitializedSignal) item);
            return;
        } else if (item instanceof RpcRegisterResp) {
            handleRpcRegisterResp((RpcRegisterResp) item);
            return;
        } else if (!_stagesInitialized || !_registered) {
            if (DEBUG) logger.info(method_tag + ": " + ": Queueing event " + item + " until stage initialized and registered.");
            _blockedEvents.add(item);
            return;
        } else if (item instanceof RpcCall) {
            RpcCall req = (RpcCall) item;
            switch(req.proc.getProcNum()) {
                case gw_null_1:
                    handle_gw_null(req);
                    break;
                case gw_put_1:
                    handle_gw_put(req);
                    break;
                case gw_create_1:
                    handle_gw_create(req);
                    break;
                case gw_append_1:
                    handle_gw_append(req);
                    break;
                case gw_truncate_1:
                    handle_gw_truncate(req);
                    break;
                case gw_snapshot_1:
                    handle_gw_snapshot(req);
                    break;
                case gw_get_extent_1:
                    handle_gw_get_extent(req);
                    break;
                case gw_get_blocks_1:
                    handle_gw_get_blocks(req);
                    break;
                case gw_get_certificate_1:
                    handle_gw_get_certificate(req);
                    break;
                default:
                    BUG(method_tag + "received an unregistered remote procedured call.");
            }
        } else {
            BUG(method_tag + ": " + "got unknown event: " + item);
        }
        return;
    }

    /**
   * <CODE>handleStagesInitializedSignal</CODE> does nothing.
   *
   * @param signal {@link seda.sandStorm.api.StagesInitializedSignal} */
    private void handleStagesInitializedSignal(StagesInitializedSignal signal) {
        final String method_tag = tag + ".handleStagesInitializedSignal";
        if (DEBUG) logger.info(method_tag + ": " + "called " + signal);
        _stagesInitialized = true;
        RpcRegisterReq req = new RpcRegisterReq(AntiquityUtils.getGWProcedureMap(), false, appId, null, my_sink);
        dispatch(req);
        return;
    }

    /**
   * <CODE>handleRpcRegisteredResp</CODE> verifies that all remote
   * procedure calls were registered.
   *
   * @param resp {@link antiquity.rpc.api.RpcRegisterResp} */
    private void handleRpcRegisterResp(RpcRegisterResp resp) {
        final String method_tag = tag + ".handleRpcRegisterResp";
        if (DEBUG) logger.info(method_tag + ": " + "called " + resp);
        _registered = true;
        for (Map.Entry<ProcKey, Integer> entry : resp.responses.entrySet()) {
            if (!RpcRegisterResp.SUCCESS.equals((Integer) entry.getValue())) BUG(method_tag + ": proc " + entry + " was not registered.");
        }
        if (_stagesInitialized && _registered) initializeDummyASyncServer();
    }

    /**
   * <CODE>initializeDummyASyncServer</CODE> unblocks all blocked events.
   * That is, <I>all systems go</I>!
   * <I>INVARIANT</I> All stages initialized and rpc handlers registered. */
    private void initializeDummyASyncServer() {
        final String method_tag = tag + ".initializeDummyASyncServer";
        if (DEBUG) logger.info(method_tag + ": called\n");
        logger.info(method_tag + ": " + "Accepting requests...");
        while (_blockedEvents.size() > 0) {
            try {
                handleEvent((QueueElementIF) _blockedEvents.remove(0));
            } catch (EventHandlerException ehe) {
                BUG(ehe);
            }
        }
    }

    /**********************************************************************/
    private void handle_gw_null(RpcCall req) {
        final String method_tag = tag + ".handle_gw_null";
        if (logger.isInfoEnabled()) logger.info(method_tag + ": " + "called " + req);
        XdrVoid args = (XdrVoid) req.args;
        RpcReply resp = new RpcReply(args, req);
        dispatch(resp);
        return;
    }

    private void handle_gw_put(RpcCall req) {
        final String method_tag = tag + ".handle_gw_put";
        gw_put_args args = (gw_put_args) req.args;
        if (logger.isInfoEnabled()) logger.info(method_tag + ": " + "called " + req);
        gw_signed_certificate cert = args.cert;
        gw_data_block[] blocks = args.data;
        if (logger.isDebugEnabled()) logger.debug(method_tag + ": " + "Putting " + blocks.length + " blocks.");
        Extent<gw_public_key, gw_guid, gw_signed_certificate> ext = new Extent<gw_public_key, gw_guid, gw_signed_certificate>(cert.cert.public_key, gw_signed_certificate.class, _md);
        ext.setCertificate(cert);
        gw_guid[] block_names = ext.appendBlocks(blocks);
        gw_guid verifier = ext.getHashName();
        SecureHash verifier_sha1 = AntiquityUtils.guidToSecureHash(verifier);
        if (logger.isDebugEnabled()) logger.debug(method_tag + ": " + "Creating hash-verified extent " + verifier_sha1 + ".");
        hash_extents.put(verifier_sha1, ext);
        for (int i = 0; i < block_names.length; ++i) {
            block_map.put(AntiquityUtils.guidToSecureHash(block_names[i]), blocks[i]);
        }
        gw_put_result result = new gw_put_result();
        result.status = gw_status.GW_STATUS_OK;
        result.verifier = verifier;
        result.proof = AntiquityUtils.GW_NULL_SOUNDNESS_PROOF;
        RpcReply resp = new RpcReply(result, req);
        dispatch(resp);
    }

    private void handle_gw_create(RpcCall req) {
        final String method_tag = tag + ".handle_gw_create";
        gw_create_args args = (gw_create_args) req.args;
        if (logger.isInfoEnabled()) logger.info(method_tag + ": " + "called " + req);
        gw_signed_certificate cert = args.cert;
        Extent<gw_public_key, gw_guid, gw_signed_certificate> ext = new Extent<gw_public_key, gw_guid, gw_signed_certificate>(cert.cert.public_key, gw_signed_certificate.class, _md);
        ext.setCertificate(cert);
        gw_guid verifier = ext.getHashName();
        SecureHash verifier_sha1 = AntiquityUtils.guidToSecureHash(verifier);
        if (logger.isDebugEnabled()) logger.debug(method_tag + ": " + "Creating key-verified extent " + verifier_sha1 + ".");
        key_extents.put(verifier_sha1, ext);
        gw_create_result result = new gw_create_result();
        result.status = gw_status.GW_STATUS_OK;
        result.extent_key = verifier;
        result.verifier = verifier;
        result.proof = AntiquityUtils.GW_NULL_SOUNDNESS_PROOF;
        RpcReply resp = new RpcReply(result, req);
        dispatch(resp);
    }

    private void handle_gw_append(RpcCall req) {
        final String method_tag = tag + ".handle_gw_append";
        gw_append_args args = (gw_append_args) req.args;
        if (logger.isInfoEnabled()) logger.info(method_tag + ": " + "called " + req);
        gw_signed_certificate cert = args.cert;
        gw_data_block[] blocks = args.data;
        gw_guid extent_key = AntiquityUtils.computeGuid(cert.cert.public_key, gw_guid.class, _md);
        SecureHash extent_key_sha1 = AntiquityUtils.guidToSecureHash(extent_key);
        if (logger.isDebugEnabled()) logger.debug(method_tag + ": " + "Appending data to extent " + extent_key_sha1 + ".");
        Extent<gw_public_key, gw_guid, gw_signed_certificate> ext = key_extents.get(extent_key_sha1);
        if (ext == null) {
            BUG(method_tag + ": " + "No such extent.");
        }
        gw_guid[] block_names = ext.appendBlocks(blocks);
        ext.setCertificate(cert);
        gw_guid verifier = ext.getHashName();
        for (int i = 0; i < block_names.length; ++i) {
            block_map.put(AntiquityUtils.guidToSecureHash(block_names[i]), blocks[i]);
        }
        gw_append_result result = new gw_append_result();
        result.status = gw_status.GW_STATUS_OK;
        result.extent_key = extent_key;
        result.verifier = verifier;
        result.proof = AntiquityUtils.GW_NULL_SOUNDNESS_PROOF;
        RpcReply resp = new RpcReply(result, req);
        dispatch(resp);
    }

    private void handle_gw_truncate(RpcCall req) {
        final String method_tag = tag + ".handle_gw_truncate";
        gw_truncate_args args = (gw_truncate_args) req.args;
        if (logger.isInfoEnabled()) logger.info(method_tag + ": " + "called " + req);
        gw_signed_certificate cert = args.cert;
        gw_guid extent_key = AntiquityUtils.computeGuid(cert.cert.public_key, gw_guid.class, _md);
        SecureHash extent_key_sha1 = AntiquityUtils.guidToSecureHash(extent_key);
        if (logger.isDebugEnabled()) logger.debug(method_tag + ": " + "Truncating data of extent " + extent_key_sha1 + ".");
        Extent<gw_public_key, gw_guid, gw_signed_certificate> ext = key_extents.get(extent_key_sha1);
        if (ext == null) {
            BUG(method_tag + ": " + "No such extent.");
        }
        ext.truncate();
        ext.setCertificate(cert);
        gw_guid verifier = ext.getHashName();
        gw_truncate_result result = new gw_truncate_result();
        result.status = gw_status.GW_STATUS_OK;
        result.extent_key = extent_key;
        result.verifier = verifier;
        result.proof = AntiquityUtils.GW_NULL_SOUNDNESS_PROOF;
        RpcReply resp = new RpcReply(result, req);
        dispatch(resp);
    }

    private void handle_gw_snapshot(RpcCall req) {
        final String method_tag = tag + ".handle_gw_snapshot";
        gw_snapshot_args args = (gw_snapshot_args) req.args;
        if (logger.isInfoEnabled()) logger.info(method_tag + ": " + "called " + req);
        gw_signed_certificate cert = args.cert;
        gw_guid extent_key = AntiquityUtils.computeGuid(cert.cert.public_key, gw_guid.class, _md);
        SecureHash extent_key_sha1 = AntiquityUtils.guidToSecureHash(extent_key);
        if (logger.isDebugEnabled()) logger.debug(method_tag + ": " + "Snapshoting extent " + extent_key_sha1 + ".");
        Extent<gw_public_key, gw_guid, gw_signed_certificate> ext_key = key_extents.get(extent_key_sha1);
        if (ext_key == null) {
            BUG(method_tag + ": " + "No such extent.");
        }
        Extent<gw_public_key, gw_guid, gw_signed_certificate> ext_hash = null;
        try {
            ext_hash = (Extent<gw_public_key, gw_guid, gw_signed_certificate>) ext_key.clone();
        } catch (CloneNotSupportedException e) {
            BUG("cannot clone " + ext_key);
        }
        ext_hash.setCertificate(cert);
        gw_guid verifier = ext_hash.getHashName();
        SecureHash verifier_sha1 = AntiquityUtils.guidToSecureHash(verifier);
        hash_extents.put(verifier_sha1, ext_hash);
        gw_snapshot_result result = new gw_snapshot_result();
        result.status = gw_status.GW_STATUS_OK;
        result.extent_key_old = extent_key;
        result.verifier = verifier;
        result.proof = AntiquityUtils.GW_NULL_SOUNDNESS_PROOF;
        RpcReply resp = new RpcReply(result, req);
        dispatch(resp);
    }

    private void handle_gw_get_extent(RpcCall req) {
        final String method_tag = tag + ".handle_gw_get_extent";
        if (logger.isInfoEnabled()) logger.info(method_tag + ": " + "called " + req);
        gw_get_extent_args args = (gw_get_extent_args) req.args;
        gw_guid verifier = args.extent_key;
        if (logger.isDebugEnabled()) logger.debug(method_tag + ": " + "Getting extent " + XdrUtils.toString(verifier));
        SecureHash verifier_sha1 = AntiquityUtils.guidToSecureHash(verifier);
        Extent<gw_public_key, gw_guid, gw_signed_certificate> extent = null;
        if (key_extents.containsKey(verifier_sha1)) extent = key_extents.get(verifier_sha1); else if (hash_extents.containsKey(verifier_sha1)) extent = hash_extents.get(verifier_sha1); else {
            gw_get_extent_result result = new gw_get_extent_result();
            result.status = gw_status.GW_STATUS_ERR;
            result.cert = AntiquityUtils.GW_NULL_SIGNED_CERT;
            result.block_names = new gw_guid[0];
            result.data = new gw_data_block[0];
            RpcReply resp = new RpcReply(result, req);
            dispatch(resp);
            return;
        }
        gw_guid[] block_names = extent.getBlockNames();
        if (logger.isDebugEnabled()) logger.debug(method_tag + ": " + "Extent " + XdrUtils.toString(verifier) + " has " + block_names.length + " blocks.");
        List<gw_data_block> list = new ArrayList<gw_data_block>();
        for (gw_guid name : block_names) {
            list.add(block_map.get(AntiquityUtils.guidToSecureHash(name)));
        }
        gw_data_block blocks[] = new gw_data_block[block_names.length];
        blocks = list.toArray(blocks);
        gw_get_extent_result result = new gw_get_extent_result();
        result.status = gw_status.GW_STATUS_OK;
        result.cert = extent.getCertificate();
        result.block_names = block_names;
        result.data = blocks;
        RpcReply resp = new RpcReply(result, req);
        dispatch(resp);
        return;
    }

    private void handle_gw_get_blocks(RpcCall req) {
        final String method_tag = tag + ".handle_gw_get_blocks";
        gw_get_blocks_args args = (gw_get_blocks_args) req.args;
        if (logger.isInfoEnabled()) logger.info(method_tag + ": " + "called " + req);
        gw_guid extent_key = args.extent_key;
        gw_guid[] block_names = args.block_names;
        SecureHash extent_key_sha1 = AntiquityUtils.guidToSecureHash(extent_key);
        if (logger.isDebugEnabled()) logger.debug(method_tag + ": " + "Fetching data from extent " + extent_key_sha1 + ".");
        Extent<gw_public_key, gw_guid, gw_signed_certificate> ext = hash_extents.get(extent_key_sha1);
        if (ext == null) {
            ext = key_extents.get(extent_key_sha1);
        }
        if (ext == null) {
            gw_get_blocks_result result = new gw_get_blocks_result();
            result.status = gw_status.GW_STATUS_ERR_NOT_EXISTS;
            result.data = new gw_data_block[0];
            RpcReply resp = new RpcReply(result, req);
            dispatch(resp);
            return;
        }
        if (logger.isDebugEnabled()) logger.debug(method_tag + ": " + "Fetching " + block_names.length + " blocks.");
        gw_get_blocks_result result = new gw_get_blocks_result();
        result.status = gw_status.GW_STATUS_OK;
        result.data = new gw_data_block[block_names.length];
        for (int i = 0; i < block_names.length; ++i) {
            SecureHash ant_guid = AntiquityUtils.guidToSecureHash(block_names[i]);
            result.data[i] = (gw_data_block) block_map.get(ant_guid);
            if (result.data[i] == null) {
                result.status = gw_status.GW_STATUS_ERR_PARTIAL_GET;
                result.data[i] = AntiquityUtils.GW_NULL_DATA_BLOCK;
            }
        }
        RpcReply resp = new RpcReply(result, req);
        dispatch(resp);
        return;
    }

    private void handle_gw_get_certificate(RpcCall req) {
        final String method_tag = tag + ".handle_gw_get_certificate";
        gw_get_certificate_args args = (gw_get_certificate_args) req.args;
        if (logger.isInfoEnabled()) logger.info(method_tag + ": " + "called " + req);
        gw_guid extent_key = args.extent_key;
        SecureHash extent_key_sha1 = AntiquityUtils.guidToSecureHash(extent_key);
        if (logger.isDebugEnabled()) logger.debug(method_tag + ": " + "Fetching cert from extent " + extent_key_sha1 + ".");
        Extent<gw_public_key, gw_guid, gw_signed_certificate> ext = hash_extents.get(extent_key_sha1);
        if (ext == null) {
            ext = key_extents.get(extent_key_sha1);
        }
        gw_get_certificate_result result = new gw_get_certificate_result();
        if (ext == null) {
            logger.fatal(method_tag + ": No such extent: extent_key=" + extent_key);
            result.status = gw_status.GW_STATUS_ERR_NOT_EXISTS;
            result.cert = AntiquityUtils.GW_NULL_SIGNED_CERT;
            result.proof = AntiquityUtils.GW_NULL_SOUNDNESS_PROOF;
            result.block_names = new gw_guid[0];
            result.latest_config = AntiquityUtils.GW_NULL_SIGNED_CONFIG;
        } else {
            result.status = gw_status.GW_STATUS_OK;
            result.cert = ext.getCertificate();
            result.proof = AntiquityUtils.GW_NULL_SOUNDNESS_PROOF;
            if ((int) args.verbose == 0) result.block_names = new gw_guid[0]; else result.block_names = ext.getBlockNames();
            result.latest_config = AntiquityUtils.GW_NULL_SIGNED_CONFIG;
        }
        RpcReply resp = new RpcReply(result, req);
        dispatch(resp);
        return;
    }
}
