package moxie.nfs;

import static moxie.nfs.NfsUtils.Procedure;
import moxie.fs.FsUtils;
import moxie.fs.MoxieHandler;
import moxie.net.PriorityRpcCall;
import moxie.net.RpcPriorityScheduler;
import moxie.net.RpcScheduler;
import antiquity.client.ClientUtils;
import antiquity.rpc.api.RpcCall;
import antiquity.rpc.api.RpcReply;
import antiquity.rpc.api.RpcRegisterReq;
import antiquity.rpc.api.RpcRegisterResp;
import antiquity.rpc.api.ProcInfo.ProcKey;
import antiquity.rpc.api.ProcInfo.ProcValue;
import antiquity.rpc.impl.RpcClientStage;
import antiquity.util.XdrUtils;
import fuse.FuseDirEnt;
import fuse.FuseException;
import fuse.FuseStat;
import fuse.FuseStatfs;
import fuse.FuseFtype;
import ostore.util.ByteArrayInputBuffer;
import ostore.util.ByteArrayOutputBuffer;
import ostore.util.ByteUtils;
import ostore.util.CountBuffer;
import ostore.util.NodeId;
import ostore.util.QuickSerializable;
import ostore.util.QSException;
import ostore.util.SecureHash;
import ostore.util.SHA1Hash;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import org.acplt.oncrpc.XdrVoid;
import seda.sandStorm.api.ConfigDataIF;
import seda.sandStorm.api.QueueElementIF;
import seda.sandStorm.api.SinkIF;
import seda.sandStorm.api.StagesInitializedSignal;
import static bamboo.util.Curry.*;

public class NfsClient extends MoxieHandler {

    private static final int NFS_GETDIR_PACKET_SIZE = 4096;

    private static final int MAX_FD = 1024;

    private boolean stages_initialized = false;

    private NodeId server_addr;

    private long rpc_app_id;

    private RpcClientStage rpc_stage;

    private fhandle root_fh;

    private Map<NfsObjectId, fhandle> fhandle_map;

    private Set<Long> fd_set;

    private boolean CACHE_ATTRS = false;

    private int ATTR_CACHE_TIMEOUT_MS = 30 * 1000;

    private Map<String, AttrCacheEntry> attr_cache;

    private boolean CACHE_DATA = false;

    private String DATA_CACHE_ROOT = "/tmp";

    private int DATA_CACHE_PATH_LENGTH = 20;

    public void init(ConfigDataIF config) throws Exception {
        event_types = new Object[] { seda.sandStorm.api.StagesInitializedSignal.class };
        super.init(config);
        String debug_level_st = config.getString("DebugLevel");
        Level debug_level = Level.toLevel(debug_level_st, Level.WARN);
        logger.warn("Setting debug level to " + debug_level + ".");
        logger.setLevel(debug_level);
        logger.info("Reading server address...");
        String server_name = config.getString("ServerName");
        int server_port = config.getInt("ServerPort");
        server_addr = new NodeId(server_port, InetAddress.getByName(server_name));
        logger.info("Reading server address...done: server=" + server_addr);
        logger.info("Obtaining references to other stages...");
        rpc_stage = (RpcClientStage) lookup_stage(config, "RpcClient");
        assert (rpc_stage != null);
        rpc_app_id = bamboo.router.Router.app_id(NfsClient.class);
        logger.info("Obtaining references to other stages...done.");
        fd_set = new HashSet<Long>();
        for (long fd = 0; fd < MAX_FD; fd++) fd_set.add(fd);
        logger.info("Configuring caching policies...");
        if (config.contains("CacheAttributes")) CACHE_ATTRS = config.getBoolean("CacheAttributes");
        if (config.contains("AttributeCacheTimeoutMs")) ATTR_CACHE_TIMEOUT_MS = config.getInt("AttributeCacheTimeoutMs");
        if (config.contains("CacheData")) CACHE_DATA = config.getBoolean("CacheData");
        if (config.contains("DataCacheRoot")) DATA_CACHE_ROOT = config.getString("DataCacheRoot");
        logger.info("  CacheAttributes=" + CACHE_ATTRS);
        logger.info("  AttributeCacheTimeoutMs=" + ATTR_CACHE_TIMEOUT_MS);
        logger.info("  CacheData=" + CACHE_DATA);
        logger.info("  DataCacheRoot=" + DATA_CACHE_ROOT);
        logger.info("Configuring caching policies...done.");
        fhandle_map = new HashMap<NfsObjectId, fhandle>();
        attr_cache = new HashMap<String, AttrCacheEntry>();
        return;
    }

    public void handleEvent(QueueElementIF event) {
        if (event instanceof StagesInitializedSignal) {
            logger.info("Received StagesInitializedSignal.");
            stages_initialized = true;
        } else {
            BUG("Unexpected event: " + event);
        }
        return;
    }

    protected void mount(String mnt_path, String[] args, Thunk1<FuseException> fuse_cb) {
        if (logger.isInfoEnabled()) {
            String arg_st = new String();
            for (int i = 0; i < args.length; ++i) arg_st += ", arg[" + i + "]=" + args[i];
            logger.info("mount - start: mnt_path=" + mnt_path + ", num_args=" + args.length + arg_st);
        }
        registerRpcProcedures(curry(mount_rpc_reg_cb, mnt_path, fuse_cb));
        return;
    }

    private Thunk2<String, Thunk1<FuseException>> mount_rpc_reg_cb = new Thunk2<String, Thunk1<FuseException>>() {

        public void run(String mnt_path, Thunk1<FuseException> fuse_cb) {
            diropargs args = new diropargs();
            args.dir = NfsUtils.FHANDLE_NULL;
            args.name = new filename();
            args.name.value = FsUtils.FILE_SEPARATOR;
            Procedure proc = NfsUtils.Procedure.NFSPROC_LOOKUP;
            ProcKey proc_key = NfsUtils.getProcKey(proc);
            Object nonce = null;
            SinkIF sink = null;
            RpcCall rpc_call = new RpcCall(server_addr, proc_key, args, rpc_app_id, nonce, sink);
            rpc_call.cb = curry(mount_lookup_cb, mnt_path, fuse_cb);
            rpc_stage.handleEvent(rpc_call);
            return;
        }
    };

    private Thunk4<String, Thunk1<FuseException>, RpcCall, RpcReply> mount_lookup_cb = new Thunk4<String, Thunk1<FuseException>, RpcCall, RpcReply>() {

        public void run(String mnt_path, Thunk1<FuseException> fuse_cb, RpcCall rpc_call, RpcReply rpc_reply) {
            diropres reply_args = (diropres) rpc_reply.reply;
            int status = reply_args.status;
            if (status != stat.NFS_OK) BUG("Unhandled error: status=" + status);
            fhandle fh = reply_args.diropok.file;
            fattr attr = reply_args.diropok.attributes;
            if (logger.isDebugEnabled()) logger.debug("mount - retrieved root fhandle: fh=" + XdrUtils.toString(fh));
            root_fh = fh;
            if (logger.isInfoEnabled()) logger.info("mount - done: mnt_path=" + mnt_path);
            FuseException fe = null;
            fuse_cb.run(fe);
            return;
        }
    };

    protected void unmount(String mnt_path, Thunk1<FuseException> fuse_cb) {
        if (logger.isInfoEnabled()) logger.info("unmount - start: mnt_path=" + mnt_path);
        if (logger.isInfoEnabled()) logger.info("unmount - done: mnt_path=" + mnt_path);
        FuseException fe = null;
        fuse_cb.run(fe);
        return;
    }

    ;

    protected void statfs(Thunk2<FuseException, FuseStatfs> fuse_cb) {
        if (logger.isInfoEnabled()) logger.info("statfs - start:");
        FuseStatfs statfs = new FuseStatfs();
        statfs.blockSize = 0;
        statfs.blocks = 0;
        statfs.blocksFree = 0;
        statfs.files = 0;
        statfs.filesFree = 0;
        statfs.namelen = 0;
        if (logger.isInfoEnabled()) logger.info("statfs - done:");
        FuseException fe = null;
        fuse_cb.run(fe, statfs);
        return;
    }

    protected void getattr(String path, Thunk2<FuseException, FuseStat> fuse_cb) {
        if (logger.isInfoEnabled()) logger.info("getattr - start: path=" + path);
        Thunk1<fhandle> cb = curry(getattr_fhandle_cb, path, fuse_cb);
        resolveFhandle(path, cb);
        return;
    }

    private Thunk3<String, Thunk2<FuseException, FuseStat>, fhandle> getattr_fhandle_cb = new Thunk3<String, Thunk2<FuseException, FuseStat>, fhandle>() {

        public void run(String path, Thunk2<FuseException, FuseStat> fuse_cb, fhandle fh) {
            if (logger.isDebugEnabled()) logger.debug("getattr - resolved fhandle: fh=" + XdrUtils.toString(fh));
            if (fh == null) {
                if (logger.isInfoEnabled()) logger.info("getattr - done: no such file: path=" + path);
                FuseException fe = new FuseException("Does not exist");
                fe.initErrno(FuseException.ENOENT);
                FuseStat fstat = null;
                fuse_cb.run(fe, fstat);
                return;
            }
            if (CACHE_ATTRS) {
                if (attr_cache.containsKey(path)) {
                    AttrCacheEntry entry = attr_cache.get(path);
                    fattr attrs = entry.attr;
                    if (logger.isInfoEnabled()) logger.info("getattr - found attributes in cache: " + " path=" + path);
                    getAttrDone(path, fuse_cb, fh, attrs);
                    return;
                }
            }
            Procedure proc = NfsUtils.Procedure.NFSPROC_GETATTR;
            ProcKey proc_key = NfsUtils.getProcKey(proc);
            Object nonce = null;
            SinkIF sink = null;
            RpcCall rpc_call = new RpcCall(server_addr, proc_key, fh, rpc_app_id, nonce, sink);
            rpc_call.cb = curry(getattr_server_cb, path, fuse_cb, fh);
            rpc_stage.handleEvent(rpc_call);
            return;
        }
    };

    private Thunk5<String, Thunk2<FuseException, FuseStat>, fhandle, RpcCall, RpcReply> getattr_server_cb = new Thunk5<String, Thunk2<FuseException, FuseStat>, fhandle, RpcCall, RpcReply>() {

        public void run(String path, Thunk2<FuseException, FuseStat> fuse_cb, fhandle nfs_fh, RpcCall rpc_call, RpcReply rpc_reply) {
            attrstat reply_args = (attrstat) rpc_reply.reply;
            int status = reply_args.status;
            if (logger.isInfoEnabled()) logger.info("getattr - received response from server: " + "path=" + path);
            if (status == stat.NFSERR_NOENT) {
                if (logger.isInfoEnabled()) logger.info("getattr - done, does not exist: " + "path=" + path);
                FuseException fe = new FuseException("Does not exist");
                fe.initErrno(FuseException.ENOENT);
                FuseStat fstat = null;
                fuse_cb.run(fe, fstat);
                return;
            }
            if (status != stat.NFS_OK) BUG("Unhandled error: status=" + status);
            fattr attrs = reply_args.attributes;
            if (CACHE_ATTRS) attrCacheUpdate(path, attrs);
            getAttrDone(path, fuse_cb, nfs_fh, attrs);
            return;
        }
    };

    private void getAttrDone(String path, Thunk2<FuseException, FuseStat> fuse_cb, fhandle nfs_fh, fattr attrs) {
        FuseStat fstat = new FuseStat();
        int type = attrs.type;
        int mode = attrs.mode;
        fstat.mode = mode;
        fstat.size = attrs.size;
        fstat.uid = attrs.uid;
        fstat.gid = attrs.gid;
        fstat.atime = attrs.atime.seconds;
        fstat.mtime = attrs.mtime.seconds;
        fstat.ctime = attrs.ctime.seconds;
        fstat.blocks = attrs.blocks;
        fstat.nlink = 1;
        if (logger.isInfoEnabled()) logger.info("getattr - done: path=" + path + " stat=" + fstat);
        FuseException fe = null;
        fuse_cb.run(fe, fstat);
        return;
    }

    protected void create(String path, int mode, Thunk1<FuseException> fuse_cb) {
        if (logger.isInfoEnabled()) logger.info("create - start: path=" + path + " mode=0o" + Integer.toOctalString(mode));
        String parent = FsUtils.getParent(path);
        Thunk1<fhandle> cb = curry(create_fhandle_cb, path, mode, fuse_cb);
        resolveFhandle(parent, cb);
        return;
    }

    private Thunk4<String, Integer, Thunk1<FuseException>, fhandle> create_fhandle_cb = new Thunk4<String, Integer, Thunk1<FuseException>, fhandle>() {

        public void run(String path, Integer mode, Thunk1<FuseException> fuse_cb, fhandle parent_fh) {
            if (logger.isDebugEnabled()) logger.debug("create - resolved parent fhandle: fh=" + XdrUtils.toString(parent_fh));
            String parent = FsUtils.getParent(path);
            String child = FsUtils.getNextPathComponent(path, parent);
            int now_sec = (int) (System.currentTimeMillis() / 1000);
            createargs args = new createargs();
            args.where = new diropargs();
            args.where.dir = parent_fh;
            args.where.name = new filename();
            args.where.name.value = child;
            args.attributes = NfsUtils.newImpotentSattr();
            args.attributes.mode = mode;
            args.attributes.uid = 1000;
            args.attributes.gid = 1000;
            args.attributes.size = 0;
            args.attributes.atime = new timeval();
            args.attributes.atime.seconds = now_sec;
            args.attributes.atime.useconds = 0;
            args.attributes.mtime = new timeval();
            args.attributes.mtime.seconds = now_sec;
            args.attributes.mtime.useconds = 0;
            Procedure proc = NfsUtils.Procedure.NFSPROC_CREATE;
            ProcKey proc_key = NfsUtils.getProcKey(proc);
            Object nonce = null;
            SinkIF sink = null;
            RpcCall rpc_call = new RpcCall(server_addr, proc_key, args, rpc_app_id, nonce, sink);
            rpc_call.cb = curry(create_server_cb, path, mode, fuse_cb);
            rpc_stage.handleEvent(rpc_call);
            return;
        }
    };

    private Thunk5<String, Integer, Thunk1<FuseException>, RpcCall, RpcReply> create_server_cb = new Thunk5<String, Integer, Thunk1<FuseException>, RpcCall, RpcReply>() {

        public void run(String path, Integer mode, Thunk1<FuseException> fuse_cb, RpcCall rpc_call, RpcReply rpc_reply) {
            if (logger.isDebugEnabled()) logger.debug("create - received server response: " + "path=" + path + " mode=0o" + Integer.toOctalString(mode));
            diropres reply_args = (diropres) rpc_reply.reply;
            int status = reply_args.status;
            if (status != stat.NFS_OK) BUG("Unhandled error: status=" + status);
            fhandle nfs_fh = reply_args.diropok.file;
            fattr attrs = reply_args.diropok.attributes;
            if (CACHE_ATTRS) attrCacheUpdate(path, attrs);
            if (logger.isDebugEnabled()) logger.debug("create - done: path=" + path);
            FuseException fe = null;
            fuse_cb.run(fe);
            return;
        }
    };

    protected void open(String path, int flags, Thunk2<FuseException, Long> fuse_cb) {
        if (logger.isInfoEnabled()) logger.info("open - start: path=" + path + " flags=0x" + Integer.toHexString(flags));
        Thunk1<fhandle> cb = curry(open_fhandle_cb, path, flags, fuse_cb);
        resolveFhandle(path, cb);
        return;
    }

    private Thunk4<String, Integer, Thunk2<FuseException, Long>, fhandle> open_fhandle_cb = new Thunk4<String, Integer, Thunk2<FuseException, Long>, fhandle>() {

        public void run(String path, Integer flags, Thunk2<FuseException, Long> fuse_cb, fhandle fh) {
            if (logger.isDebugEnabled()) logger.debug("open - resolved fhandle: fh=" + XdrUtils.toString(fh));
            if (logger.isInfoEnabled()) logger.info("open - done: path=" + path + " flags=0x" + Integer.toHexString(flags));
            FuseException fe = null;
            long fd = getFreeFd();
            fuse_cb.run(fe, fd);
            return;
        }
    };

    protected void close(String path, long fhandle, Thunk1<FuseException> fuse_cb) {
        if (logger.isInfoEnabled()) logger.info("close - start: path=" + path + " fhandle=" + fhandle);
        if (logger.isInfoEnabled()) logger.info("close - done: path=" + path + " fhandle=" + fhandle);
        FuseException fe = null;
        fuse_cb.run(fe);
        return;
    }

    protected void release(String path, long fhandle, int flags, Thunk1<FuseException> fuse_cb) {
        if (logger.isInfoEnabled()) logger.info("release - start: path=" + path + " fhandle=" + fhandle + " flags=0x" + Integer.toHexString(flags));
        freeFd(fhandle);
        if (logger.isInfoEnabled()) logger.info("release - done: path=" + path + " fhandle=" + fhandle + " flags=0x" + Integer.toHexString(flags));
        FuseException fe = null;
        fuse_cb.run(fe);
        return;
    }

    protected void read(String path, long fhandle, ByteBuffer buffer, long offset, Thunk1<FuseException> fuse_cb) {
        int length = buffer.capacity();
        if (logger.isInfoEnabled()) logger.info("read - start: path=" + path + " fhandle=" + fhandle + " offset=" + offset + " length=" + length);
        Thunk1<fhandle> cb = curry(read_fhandle_cb, path, fhandle, buffer, offset, fuse_cb);
        resolveFhandle(path, cb);
        return;
    }

    private Thunk6<String, Long, ByteBuffer, Long, Thunk1<FuseException>, fhandle> read_fhandle_cb = new Thunk6<String, Long, ByteBuffer, Long, Thunk1<FuseException>, fhandle>() {

        public void run(String path, Long fhandle, ByteBuffer buffer, Long offset, Thunk1<FuseException> fuse_cb, fhandle fh) {
            int length = buffer.capacity();
            if (logger.isDebugEnabled()) logger.debug("read - resolved fhandle: path=" + path + " fh=" + XdrUtils.toString(fh));
            int num_reqs = (length / nfs.MAXDATA) + (((length % nfs.MAXDATA) != 0) ? 1 : 0);
            Map<Integer, byte[]> result_map = new HashMap<Integer, byte[]>();
            if (logger.isDebugEnabled()) logger.debug("read - sending requests to server: " + "num_reqs=" + num_reqs);
            for (long nfs_offset = offset; nfs_offset < offset + length; nfs_offset += nfs.MAXDATA) {
                if (CACHE_DATA) {
                    byte[] data = dataCacheRead(path, (int) nfs_offset);
                    if (data != null) {
                        if (logger.isDebugEnabled()) logger.debug("read - found block in cache: " + "fhandle=" + fhandle + " offset=" + nfs_offset);
                        result_map.put((int) nfs_offset, data);
                        continue;
                    }
                }
                readargs args = new readargs();
                args.file = fh;
                args.offset = (int) nfs_offset;
                args.count = (int) (offset + length - nfs_offset < nfs.MAXDATA ? offset + length - nfs_offset : nfs.MAXDATA);
                args.totalcount = 0;
                if (logger.isDebugEnabled()) logger.debug("read - sending request to server: " + "fhandle=" + fhandle + " offset=" + args.offset + " count=" + args.count);
                Procedure proc = NfsUtils.Procedure.NFSPROC_READ;
                ProcKey proc_key = NfsUtils.getProcKey(proc);
                Object nonce = null;
                SinkIF sink = null;
                RpcCall rpc_call = new RpcCall(server_addr, proc_key, args, rpc_app_id, nonce, sink);
                rpc_call.cb = curry(read_server_cb, path, fhandle, buffer, offset, fuse_cb, num_reqs, result_map);
                rpc_stage.handleEvent(rpc_call);
            }
            if (result_map.size() == num_reqs) readDone(path, fhandle, buffer, offset, fuse_cb, num_reqs, result_map);
            return;
        }
    };

    private Thunk9<String, Long, ByteBuffer, Long, Thunk1<FuseException>, Integer, Map<Integer, byte[]>, RpcCall, RpcReply> read_server_cb = new Thunk9<String, Long, ByteBuffer, Long, Thunk1<FuseException>, Integer, Map<Integer, byte[]>, RpcCall, RpcReply>() {

        public void run(String path, Long fhandle, ByteBuffer buffer, Long offset, Thunk1<FuseException> fuse_cb, Integer num_reqs, Map<Integer, byte[]> result_map, RpcCall rpc_call, RpcReply rpc_reply) {
            readargs req_args = (readargs) rpc_call.args;
            int req_offset = req_args.offset;
            int req_count = req_args.count;
            if (logger.isDebugEnabled()) logger.debug("read - received server response: " + " fhandle=" + fhandle + " offset=" + req_offset + " count=" + req_count);
            readres reply_args = (readres) rpc_reply.reply;
            int status = reply_args.status;
            if (status != stat.NFS_OK) BUG("Unhandled error: status=" + status);
            if (CACHE_DATA) dataCacheUpdate(path, req_offset, reply_args);
            byte[] data = reply_args.readopok.data.value;
            result_map.put(req_offset, data);
            if (result_map.size() == num_reqs) readDone(path, fhandle, buffer, offset, fuse_cb, num_reqs, result_map);
            return;
        }
    };

    private void readDone(String path, Long fhandle, ByteBuffer buffer, Long offset, Thunk1<FuseException> fuse_cb, Integer num_reqs, Map<Integer, byte[]> result_map) {
        int length = buffer.capacity();
        if (logger.isDebugEnabled()) logger.debug("read - retrieved all blocks: " + " fhandle=" + fhandle + " offset=" + offset + " length=" + length);
        for (long nfs_offset = offset; nfs_offset < offset + length; nfs_offset += nfs.MAXDATA) {
            byte[] data_block = result_map.get((int) nfs_offset);
            assert (data_block != null) : ("No read result: offset=" + nfs_offset);
            int bytes_to_read = data_block.length;
            if (nfs_offset + bytes_to_read > offset + length) bytes_to_read = (int) (offset + length - nfs_offset);
            buffer.put(data_block, 0, bytes_to_read);
        }
        if (logger.isInfoEnabled()) logger.info("read - done: path=" + path + " fhandle=" + fhandle + " offset=" + offset + " length=" + length);
        FuseException fe = null;
        fuse_cb.run(fe);
        return;
    }

    protected void write(String path, long fhandle, ByteBuffer buffer, long offset, Thunk1<FuseException> fuse_cb) {
        int length = buffer.capacity();
        if (logger.isInfoEnabled()) logger.info("write - start: path=" + path + " fhandle=" + fhandle + " offset=" + offset + " length=" + length);
        Thunk1<fhandle> cb = curry(write_fhandle_cb, path, fhandle, buffer, offset, fuse_cb);
        resolveFhandle(path, cb);
        return;
    }

    private Thunk6<String, Long, ByteBuffer, Long, Thunk1<FuseException>, fhandle> write_fhandle_cb = new Thunk6<String, Long, ByteBuffer, Long, Thunk1<FuseException>, fhandle>() {

        public void run(String path, Long fhandle, ByteBuffer buffer, Long offset, Thunk1<FuseException> fuse_cb, fhandle nfs_fh) {
            if (logger.isDebugEnabled()) logger.debug("write - resolved fhandle: path=" + path + " fh=" + XdrUtils.toString(nfs_fh));
            byte[] data = new byte[buffer.limit()];
            buffer.rewind();
            buffer.get(data);
            writeargs args = new writeargs();
            args.file = nfs_fh;
            args.data = new nfsdata();
            args.data.value = data;
            args.offset = (int) offset.longValue();
            args.beginoffset = 0;
            args.totalcount = 0;
            Procedure proc = NfsUtils.Procedure.NFSPROC_WRITE;
            ProcKey proc_key = NfsUtils.getProcKey(proc);
            Object nonce = null;
            SinkIF sink = null;
            RpcCall rpc_call = new RpcCall(server_addr, proc_key, args, rpc_app_id, nonce, sink);
            rpc_call.cb = curry(write_server_cb, path, fhandle, buffer, offset, fuse_cb);
            rpc_stage.handleEvent(rpc_call);
            return;
        }
    };

    private Thunk7<String, Long, ByteBuffer, Long, Thunk1<FuseException>, RpcCall, RpcReply> write_server_cb = new Thunk7<String, Long, ByteBuffer, Long, Thunk1<FuseException>, RpcCall, RpcReply>() {

        public void run(String path, Long fhandle, ByteBuffer buffer, Long offset, Thunk1<FuseException> fuse_cb, RpcCall rpc_call, RpcReply rpc_reply) {
            int length = buffer.capacity();
            if (logger.isDebugEnabled()) logger.debug("write - received server response: " + " fhandle=" + fhandle + " offset=" + offset + " length=" + length);
            attrstat reply_args = (attrstat) rpc_reply.reply;
            int status = reply_args.status;
            if (status != stat.NFS_OK) BUG("Unhandled error: status=" + status);
            fattr attrs = reply_args.attributes;
            if (CACHE_ATTRS) attrCacheUpdate(path, attrs);
            if (logger.isInfoEnabled()) logger.info("write - done: path=" + path + " fhandle=" + fhandle + " offset=" + offset + " length=" + length);
            FuseException fe = null;
            fuse_cb.run(fe);
            return;
        }
    };

    protected void fsync(String path, long fhandle, boolean data_sync, Thunk1<FuseException> fuse_cb) {
        if (logger.isInfoEnabled()) logger.info("fsync - start: path=" + path + " fhandle=" + fhandle + " data_sync=" + data_sync);
        if (logger.isInfoEnabled()) logger.info("fsync - done: path=" + path + " fhandle=" + fhandle + " data_sync=" + data_sync);
        FuseException fe = null;
        fuse_cb.run(fe);
        return;
    }

    protected void truncate(String path, long length, Thunk1<FuseException> fuse_cb) {
        if (logger.isInfoEnabled()) logger.info("truncate - start: path=" + path + " length=" + length);
        Thunk1<fhandle> cb = curry(truncate_fhandle_cb, path, length, fuse_cb);
        resolveFhandle(path, cb);
        return;
    }

    private Thunk4<String, Long, Thunk1<FuseException>, fhandle> truncate_fhandle_cb = new Thunk4<String, Long, Thunk1<FuseException>, fhandle>() {

        public void run(String path, Long length, Thunk1<FuseException> fuse_cb, fhandle nfs_fh) {
            if (logger.isDebugEnabled()) logger.debug("truncate - resolved fhandle: path=" + path + " fh=" + XdrUtils.toString(nfs_fh));
            sattrargs args = new sattrargs();
            args.file = nfs_fh;
            args.attributes = NfsUtils.newImpotentSattr();
            args.attributes.size = (int) length.longValue();
            Procedure proc = NfsUtils.Procedure.NFSPROC_SETATTR;
            ProcKey proc_key = NfsUtils.getProcKey(proc);
            Object nonce = null;
            SinkIF sink = null;
            RpcCall rpc_call = new RpcCall(server_addr, proc_key, args, rpc_app_id, nonce, sink);
            rpc_call.cb = curry(truncate_server_cb, path, length, fuse_cb);
            rpc_stage.handleEvent(rpc_call);
            return;
        }
    };

    private Thunk5<String, Long, Thunk1<FuseException>, RpcCall, RpcReply> truncate_server_cb = new Thunk5<String, Long, Thunk1<FuseException>, RpcCall, RpcReply>() {

        public void run(String path, Long length, Thunk1<FuseException> fuse_cb, RpcCall rpc_call, RpcReply rpc_reply) {
            if (logger.isDebugEnabled()) logger.debug("truncate - received server response: " + "path=" + path + " length=" + length);
            attrstat reply_args = (attrstat) rpc_reply.reply;
            int status = reply_args.status;
            if (status != stat.NFS_OK) BUG("Unhandled error: status=" + status);
            fattr attrs = reply_args.attributes;
            if (CACHE_ATTRS) attrCacheUpdate(path, attrs);
            if (logger.isInfoEnabled()) logger.info("truncate - done: path=" + path + " length=" + length);
            FuseException fe = null;
            fuse_cb.run(fe);
            return;
        }
    };

    protected void getdir(String path, Thunk2<FuseException, FuseDirEnt[]> fuse_cb) {
        if (logger.isInfoEnabled()) logger.info("getdir - start: path=" + path);
        Thunk1<fhandle> cb = curry(getdir_fhandle_cb, path, fuse_cb);
        resolveFhandle(path, cb);
        return;
    }

    private Thunk3<String, Thunk2<FuseException, FuseDirEnt[]>, fhandle> getdir_fhandle_cb = new Thunk3<String, Thunk2<FuseException, FuseDirEnt[]>, fhandle>() {

        public void run(String path, Thunk2<FuseException, FuseDirEnt[]> fuse_cb, fhandle nfs_fh) {
            if (logger.isDebugEnabled()) logger.debug("getdir - resolved fhandle: path=" + path + " fh=" + XdrUtils.toString(nfs_fh));
            nfscookie cookie = new nfscookie();
            cookie.value = new byte[nfs.COOKIESIZE];
            Arrays.fill(cookie.value, (byte) 0x00);
            List<entry> dir_ents = new LinkedList<entry>();
            getdirServer(path, fuse_cb, nfs_fh, cookie, dir_ents);
            return;
        }
    };

    private void getdirServer(String path, Thunk2<FuseException, FuseDirEnt[]> fuse_cb, fhandle nfs_fh, nfscookie cookie, List<entry> dir_ents) {
        readdirargs args = new readdirargs();
        args.dir = nfs_fh;
        args.cookie = cookie;
        args.count = NFS_GETDIR_PACKET_SIZE;
        Procedure proc = NfsUtils.Procedure.NFSPROC_READDIR;
        ProcKey proc_key = NfsUtils.getProcKey(proc);
        Object nonce = null;
        SinkIF sink = null;
        RpcCall rpc_call = new RpcCall(server_addr, proc_key, args, rpc_app_id, nonce, sink);
        rpc_call.cb = curry(getdir_server_cb, path, fuse_cb, nfs_fh, cookie, dir_ents);
        rpc_stage.handleEvent(rpc_call);
        return;
    }

    private Thunk7<String, Thunk2<FuseException, FuseDirEnt[]>, fhandle, nfscookie, List<entry>, RpcCall, RpcReply> getdir_server_cb = new Thunk7<String, Thunk2<FuseException, FuseDirEnt[]>, fhandle, nfscookie, List<entry>, RpcCall, RpcReply>() {

        public void run(String path, Thunk2<FuseException, FuseDirEnt[]> fuse_cb, fhandle nfs_fh, nfscookie cookie, List<entry> dirents, RpcCall rpc_call, RpcReply rpc_reply) {
            if (logger.isDebugEnabled()) logger.debug("getdir - received server response: " + "path=" + path);
            readdirres reply_args = (readdirres) rpc_reply.reply;
            int status = reply_args.status;
            if (status != stat.NFS_OK) BUG("Unhandled error: status=" + status);
            entry[] children = reply_args.readdirok.entries;
            for (entry e : children) dirents.add(e);
            boolean eof = reply_args.readdirok.eof;
            if (eof) {
                getdirDone(path, fuse_cb, dirents);
            } else {
                nfscookie new_cookie = children[children.length - 1].cookie;
                getdirServer(path, fuse_cb, nfs_fh, new_cookie, dirents);
            }
            return;
        }
    };

    private void getdirDone(String path, Thunk2<FuseException, FuseDirEnt[]> fuse_cb, List<entry> dirents) {
        FuseDirEnt[] fuse_dirents = new FuseDirEnt[dirents.size()];
        for (int i = 0; i < dirents.size(); ++i) {
            entry dirent = dirents.get(i);
            fuse_dirents[i] = new FuseDirEnt();
            fuse_dirents[i].name = dirent.name.value;
            fuse_dirents[i].inode = 0;
        }
        if (logger.isInfoEnabled()) logger.info("getdir - done: path=" + path + " num_children=" + fuse_dirents.length);
        FuseException fe = null;
        fuse_cb.run(fe, fuse_dirents);
        return;
    }

    protected void mkdir(String path, int flags, Thunk1<FuseException> fuse_cb) {
        if (logger.isInfoEnabled()) logger.info("mkdir - start: path=" + path + " flags=0o" + Integer.toOctalString(flags));
        String parent = FsUtils.getParent(path);
        String child = FsUtils.getNextPathComponent(path, parent);
        Thunk1<fhandle> cb = curry(mkdir_fhandle_cb, path, flags, fuse_cb);
        resolveFhandle(parent, cb);
        return;
    }

    private Thunk4<String, Integer, Thunk1<FuseException>, fhandle> mkdir_fhandle_cb = new Thunk4<String, Integer, Thunk1<FuseException>, fhandle>() {

        public void run(String path, Integer flags, Thunk1<FuseException> fuse_cb, fhandle nfs_fh) {
            if (logger.isDebugEnabled()) logger.debug("mkdir - resolved fhandle: path=" + path + " fh=" + XdrUtils.toString(nfs_fh));
            String parent = FsUtils.getParent(path);
            String child = FsUtils.getNextPathComponent(path, parent);
            int now_sec = (int) (System.currentTimeMillis() / 1000);
            createargs args = new createargs();
            args.where = new diropargs();
            args.where.dir = nfs_fh;
            args.where.name = new filename();
            args.where.name.value = child;
            args.attributes = NfsUtils.newImpotentSattr();
            args.attributes = NfsUtils.newImpotentSattr();
            args.attributes.mode = flags & nfs.MODE_MASK_PERM_ALL;
            args.attributes.uid = 1000;
            args.attributes.gid = 1000;
            args.attributes.size = -1;
            args.attributes.atime = new timeval();
            args.attributes.atime.seconds = now_sec;
            args.attributes.atime.useconds = 0;
            args.attributes.mtime = new timeval();
            args.attributes.mtime.seconds = now_sec;
            args.attributes.mtime.useconds = 0;
            Procedure proc = NfsUtils.Procedure.NFSPROC_MKDIR;
            ProcKey proc_key = NfsUtils.getProcKey(proc);
            Object nonce = null;
            SinkIF sink = null;
            RpcCall rpc_call = new RpcCall(server_addr, proc_key, args, rpc_app_id, nonce, sink);
            rpc_call.cb = curry(mkdir_server_cb, path, flags, fuse_cb);
            rpc_stage.handleEvent(rpc_call);
            return;
        }
    };

    private Thunk5<String, Integer, Thunk1<FuseException>, RpcCall, RpcReply> mkdir_server_cb = new Thunk5<String, Integer, Thunk1<FuseException>, RpcCall, RpcReply>() {

        public void run(String path, Integer flags, Thunk1<FuseException> fuse_cb, RpcCall rpc_call, RpcReply rpc_reply) {
            if (logger.isDebugEnabled()) logger.debug("mkdir - received server response: " + "path=" + path);
            diropres reply_args = (diropres) rpc_reply.reply;
            int status = reply_args.status;
            if (status != stat.NFS_OK) BUG("Unhandled error: status=" + status);
            fhandle dir_fh = reply_args.diropok.file;
            fattr attrs = reply_args.diropok.attributes;
            if (CACHE_ATTRS) attrCacheUpdate(path, attrs);
            if (logger.isInfoEnabled()) logger.info("mkdir - done: path=" + path);
            FuseException fe = null;
            fuse_cb.run(fe);
            return;
        }
    };

    protected void rmdir(String path, Thunk1<FuseException> fuse_cb) {
        if (logger.isInfoEnabled()) logger.info("rmdir - start: path=" + path);
        String parent = FsUtils.getParent(path);
        String child = FsUtils.getNextPathComponent(path, parent);
        Thunk1<fhandle> cb = curry(rmdir_fhandle_cb, path, fuse_cb);
        resolveFhandle(parent, cb);
        return;
    }

    private Thunk3<String, Thunk1<FuseException>, fhandle> rmdir_fhandle_cb = new Thunk3<String, Thunk1<FuseException>, fhandle>() {

        public void run(String path, Thunk1<FuseException> fuse_cb, fhandle nfs_fh) {
            if (logger.isDebugEnabled()) logger.debug("rmdir - resolved fhandle: path=" + path + " fh=" + XdrUtils.toString(nfs_fh));
            String parent = FsUtils.getParent(path);
            String child = FsUtils.getNextPathComponent(path, parent);
            diropargs args = new diropargs();
            args.dir = nfs_fh;
            args.name = new filename();
            args.name.value = child;
            Procedure proc = NfsUtils.Procedure.NFSPROC_RMDIR;
            ProcKey proc_key = NfsUtils.getProcKey(proc);
            Object nonce = null;
            SinkIF sink = null;
            RpcCall rpc_call = new RpcCall(server_addr, proc_key, args, rpc_app_id, nonce, sink);
            rpc_call.cb = curry(rmdir_server_cb, path, fuse_cb);
            rpc_stage.handleEvent(rpc_call);
            return;
        }
    };

    private Thunk4<String, Thunk1<FuseException>, RpcCall, RpcReply> rmdir_server_cb = new Thunk4<String, Thunk1<FuseException>, RpcCall, RpcReply>() {

        public void run(String path, Thunk1<FuseException> fuse_cb, RpcCall rpc_call, RpcReply rpc_reply) {
            if (logger.isDebugEnabled()) logger.debug("rmdir - received server response: " + "path=" + path);
            statres reply_args = (statres) rpc_reply.reply;
            int status = reply_args.status;
            if (status == stat.NFSERR_NOTEMPTY) {
                FuseException fe = new FuseException("Directory not empty.");
                fe.initErrno(FuseException.ENOTEMPTY);
                fuse_cb.run(fe);
                return;
            }
            if (status != stat.NFS_OK) BUG("Unhandled error: status=" + status);
            fhandle parent_fh = ((diropargs) rpc_call.args).dir;
            String child = ((diropargs) rpc_call.args).name.value;
            removeFromFhandleMap(parent_fh, child);
            if (CACHE_ATTRS) {
                String parent_path = FsUtils.getParent(path);
                attrCacheRemove(parent_path);
                attrCacheRemove(path);
            }
            if (logger.isInfoEnabled()) logger.info("rmdir - done: path=" + path);
            FuseException fe = null;
            fuse_cb.run(fe);
            return;
        }
    };

    protected void link(String from, String to, Thunk1<FuseException> fuse_cb) {
        if (logger.isInfoEnabled()) logger.info("link - start: from_path=" + from + " to_path=" + to);
        Thunk1<fhandle> cb = curry(link_from_fhandle_cb, from, to, fuse_cb);
        resolveFhandle(from, cb);
        return;
    }

    private Thunk4<String, String, Thunk1<FuseException>, fhandle> link_from_fhandle_cb = new Thunk4<String, String, Thunk1<FuseException>, fhandle>() {

        public void run(String from, String to, Thunk1<FuseException> fuse_cb, fhandle from_fh) {
            if (logger.isDebugEnabled()) logger.debug("link - resolved 'from' fhandle:" + " from_path=" + from + " to_path=" + to + " from_fh=" + XdrUtils.toString(from_fh));
            String to_parent = FsUtils.getParent(to);
            String to_child = FsUtils.getNextPathComponent(to, to_parent);
            Thunk1<fhandle> cb = curry(link_to_fhandle_cb, from, to, fuse_cb, from_fh);
            resolveFhandle(to_parent, cb);
            return;
        }
    };

    private Thunk5<String, String, Thunk1<FuseException>, fhandle, fhandle> link_to_fhandle_cb = new Thunk5<String, String, Thunk1<FuseException>, fhandle, fhandle>() {

        public void run(String from, String to, Thunk1<FuseException> fuse_cb, fhandle from_fh, fhandle to_fh) {
            if (logger.isDebugEnabled()) logger.debug("link - resolved 'to' fhandle:" + " from_path=" + from + " to_path=" + to + " from_fh=" + XdrUtils.toString(from_fh) + " to_fh=" + XdrUtils.toString(to_fh));
            String to_parent = FsUtils.getParent(to);
            String to_child = FsUtils.getNextPathComponent(to, to_parent);
            linkargs args = new linkargs();
            args.from = from_fh;
            args.to = new diropargs();
            args.to.dir = to_fh;
            args.to.name = new filename();
            args.to.name.value = to_child;
            Procedure proc = NfsUtils.Procedure.NFSPROC_LINK;
            ProcKey proc_key = NfsUtils.getProcKey(proc);
            Object nonce = null;
            SinkIF sink = null;
            RpcCall rpc_call = new RpcCall(server_addr, proc_key, args, rpc_app_id, nonce, sink);
            rpc_call.cb = curry(rename_server_cb, from, to, fuse_cb);
            rpc_stage.handleEvent(rpc_call);
            return;
        }
    };

    private Thunk5<String, String, Thunk1<FuseException>, RpcCall, RpcReply> link_server_cb = new Thunk5<String, String, Thunk1<FuseException>, RpcCall, RpcReply>() {

        public void run(String from, String to, Thunk1<FuseException> fuse_cb, RpcCall rpc_call, RpcReply rpc_reply) {
            if (logger.isDebugEnabled()) logger.debug("rename - received server response: " + "from=" + from + " to=" + to);
            statres reply_args = (statres) rpc_reply.reply;
            int status = reply_args.status;
            if (status != stat.NFS_OK) BUG("Unhandled error: status=" + status);
            if (logger.isInfoEnabled()) logger.info("link - done: from_path=" + from + " to_path=" + to);
            FuseException fe = null;
            fuse_cb.run(fe);
            return;
        }
    };

    protected void symlink(String from, String to, Thunk1<FuseException> fuse_cb) {
        if (logger.isInfoEnabled()) logger.info("symlink - start: from_path=" + from + " to_path=" + to);
        String new_path = to;
        String old_path = from;
        String new_parent = FsUtils.getParent(new_path);
        String new_child = FsUtils.getNextPathComponent(new_path, new_parent);
        Thunk1<fhandle> cb = curry(symlink_new_fhandle_cb, old_path, new_path, fuse_cb);
        resolveFhandle(new_parent, cb);
        return;
    }

    private Thunk4<String, String, Thunk1<FuseException>, fhandle> symlink_new_fhandle_cb = new Thunk4<String, String, Thunk1<FuseException>, fhandle>() {

        public void run(String old_path, String new_path, Thunk1<FuseException> fuse_cb, fhandle new_fh) {
            if (logger.isDebugEnabled()) logger.debug("symlink - resolved 'new' fhandle:" + " old_path=" + old_path + " new_path=" + new_path + " new_fh=" + XdrUtils.toString(new_fh));
            String new_parent = FsUtils.getParent(new_path);
            String new_child = FsUtils.getNextPathComponent(new_path, new_parent);
            symlinkargs args = new symlinkargs();
            args.from = new diropargs();
            args.from.dir = new_fh;
            args.from.name = new filename();
            args.from.name.value = new_child;
            args.to = new path();
            args.to.value = old_path;
            args.attributes = NfsUtils.newImpotentSattr();
            Procedure proc = NfsUtils.Procedure.NFSPROC_SYMLINK;
            ProcKey proc_key = NfsUtils.getProcKey(proc);
            Object nonce = null;
            SinkIF sink = null;
            RpcCall rpc_call = new RpcCall(server_addr, proc_key, args, rpc_app_id, nonce, sink);
            rpc_call.cb = curry(symlink_server_cb, old_path, new_path, fuse_cb);
            rpc_stage.handleEvent(rpc_call);
            return;
        }
    };

    private Thunk5<String, String, Thunk1<FuseException>, RpcCall, RpcReply> symlink_server_cb = new Thunk5<String, String, Thunk1<FuseException>, RpcCall, RpcReply>() {

        public void run(String old_path, String new_path, Thunk1<FuseException> fuse_cb, RpcCall rpc_call, RpcReply rpc_reply) {
            if (logger.isDebugEnabled()) logger.debug("symlink - received server response: " + " old_path=" + old_path + " new_path=" + new_path);
            statres reply_args = (statres) rpc_reply.reply;
            int status = reply_args.status;
            if (status != stat.NFS_OK) BUG("Unhandled error: status=" + status);
            if (logger.isInfoEnabled()) logger.info("symlink - done: old_path=" + old_path + " new_path=" + new_path);
            FuseException fe = null;
            fuse_cb.run(fe);
            return;
        }
    };

    protected void readlink(String path, Thunk2<FuseException, String> fuse_cb) {
        if (logger.isInfoEnabled()) logger.info("readlink - start: path=" + path);
        Thunk1<fhandle> cb = curry(readlink_fhandle_cb, path, fuse_cb);
        resolveFhandle(path, cb);
        return;
    }

    private Thunk3<String, Thunk2<FuseException, String>, fhandle> readlink_fhandle_cb = new Thunk3<String, Thunk2<FuseException, String>, fhandle>() {

        public void run(String path, Thunk2<FuseException, String> fuse_cb, fhandle nfs_fh) {
            if (logger.isDebugEnabled()) logger.debug("readlink - resolved fhandle: path=" + path + " fh=" + XdrUtils.toString(nfs_fh));
            fhandle args = nfs_fh;
            Procedure proc = NfsUtils.Procedure.NFSPROC_READLINK;
            ProcKey proc_key = NfsUtils.getProcKey(proc);
            Object nonce = null;
            SinkIF sink = null;
            RpcCall rpc_call = new RpcCall(server_addr, proc_key, args, rpc_app_id, nonce, sink);
            rpc_call.cb = curry(readlink_server_cb, path, fuse_cb);
            rpc_stage.handleEvent(rpc_call);
            return;
        }
    };

    private Thunk4<String, Thunk2<FuseException, String>, RpcCall, RpcReply> readlink_server_cb = new Thunk4<String, Thunk2<FuseException, String>, RpcCall, RpcReply>() {

        public void run(String path, Thunk2<FuseException, String> fuse_cb, RpcCall rpc_call, RpcReply rpc_reply) {
            if (logger.isDebugEnabled()) logger.debug("readlink - received server response: " + "path=" + path);
            readlinkres reply_args = (readlinkres) rpc_reply.reply;
            int status = reply_args.status;
            if (status != stat.NFS_OK) BUG("Unhandled error: status=" + status);
            String link_dst = reply_args.data.value;
            if (logger.isInfoEnabled()) logger.info("readlink - done: path=" + path + " link_dst=" + link_dst);
            FuseException fe = null;
            fuse_cb.run(fe, link_dst);
            return;
        }
    };

    protected void unlink(String path, Thunk1<FuseException> fuse_cb) {
        if (logger.isInfoEnabled()) logger.info("unlink - start: path=" + path);
        String parent = FsUtils.getParent(path);
        String child = FsUtils.getNextPathComponent(path, parent);
        Thunk1<fhandle> cb = curry(unlink_fhandle_cb, path, fuse_cb);
        resolveFhandle(parent, cb);
        return;
    }

    private Thunk3<String, Thunk1<FuseException>, fhandle> unlink_fhandle_cb = new Thunk3<String, Thunk1<FuseException>, fhandle>() {

        public void run(String path, Thunk1<FuseException> fuse_cb, fhandle nfs_fh) {
            if (logger.isDebugEnabled()) logger.debug("unlink - resolved fhandle: path=" + path + " fh=" + XdrUtils.toString(nfs_fh));
            String parent = FsUtils.getParent(path);
            String child = FsUtils.getNextPathComponent(path, parent);
            diropargs args = new diropargs();
            args.dir = nfs_fh;
            args.name = new filename();
            args.name.value = child;
            Procedure proc = NfsUtils.Procedure.NFSPROC_REMOVE;
            ProcKey proc_key = NfsUtils.getProcKey(proc);
            Object nonce = null;
            SinkIF sink = null;
            RpcCall rpc_call = new RpcCall(server_addr, proc_key, args, rpc_app_id, nonce, sink);
            rpc_call.cb = curry(unlink_server_cb, path, fuse_cb);
            rpc_stage.handleEvent(rpc_call);
            return;
        }
    };

    private Thunk4<String, Thunk1<FuseException>, RpcCall, RpcReply> unlink_server_cb = new Thunk4<String, Thunk1<FuseException>, RpcCall, RpcReply>() {

        public void run(String path, Thunk1<FuseException> fuse_cb, RpcCall rpc_call, RpcReply rpc_reply) {
            if (logger.isDebugEnabled()) logger.debug("unlink - received server response: " + "path=" + path);
            statres reply_args = (statres) rpc_reply.reply;
            int status = reply_args.status;
            if (status != stat.NFS_OK) BUG("Unhandled error: status=" + status);
            fhandle parent_fh = ((diropargs) rpc_call.args).dir;
            String child = ((diropargs) rpc_call.args).name.value;
            removeFromFhandleMap(parent_fh, child);
            if (CACHE_ATTRS) {
                String parent_path = FsUtils.getParent(path);
                attrCacheRemove(parent_path);
                attrCacheRemove(path);
            }
            if (logger.isInfoEnabled()) logger.info("unlink - done: path=" + path);
            FuseException fe = null;
            fuse_cb.run(fe);
            return;
        }
    };

    protected void rename(String from, String to, Thunk1<FuseException> fuse_cb) {
        if (logger.isInfoEnabled()) logger.info("rename - start: from_path=" + from + " to_path=" + to);
        String from_parent = FsUtils.getParent(from);
        String from_child = FsUtils.getNextPathComponent(from, from_parent);
        String to_parent = FsUtils.getParent(to);
        String to_child = FsUtils.getNextPathComponent(to, to_parent);
        Thunk1<fhandle> cb = curry(rename_from_fhandle_cb, from, to, fuse_cb);
        resolveFhandle(from_parent, cb);
        return;
    }

    private Thunk4<String, String, Thunk1<FuseException>, fhandle> rename_from_fhandle_cb = new Thunk4<String, String, Thunk1<FuseException>, fhandle>() {

        public void run(String from, String to, Thunk1<FuseException> fuse_cb, fhandle nfs_fh) {
            if (logger.isDebugEnabled()) logger.debug("rename - resolved from fhandle: " + "from_path=" + from + " to_path=" + to + " from_fh=" + XdrUtils.toString(nfs_fh));
            String from_parent = FsUtils.getParent(from);
            String from_child = FsUtils.getNextPathComponent(from, from_parent);
            String to_parent = FsUtils.getParent(to);
            String to_child = FsUtils.getNextPathComponent(to, to_parent);
            Thunk1<fhandle> cb = curry(rename_to_fhandle_cb, from, to, fuse_cb, nfs_fh);
            resolveFhandle(to_parent, cb);
            return;
        }
    };

    private Thunk5<String, String, Thunk1<FuseException>, fhandle, fhandle> rename_to_fhandle_cb = new Thunk5<String, String, Thunk1<FuseException>, fhandle, fhandle>() {

        public void run(String from, String to, Thunk1<FuseException> fuse_cb, fhandle from_fh, fhandle to_fh) {
            if (logger.isDebugEnabled()) logger.debug("rename - resolved to fhandle: " + "from_path=" + from + " to_path=" + to + " from_fh=" + XdrUtils.toString(from_fh) + " to_fh=" + XdrUtils.toString(to_fh));
            String from_parent = FsUtils.getParent(from);
            String from_child = FsUtils.getNextPathComponent(from, from_parent);
            String to_parent = FsUtils.getParent(to);
            String to_child = FsUtils.getNextPathComponent(to, to_parent);
            renameargs args = new renameargs();
            args.from = new diropargs();
            args.from.dir = from_fh;
            args.from.name = new filename();
            args.from.name.value = from_child;
            args.to = new diropargs();
            args.to.dir = to_fh;
            args.to.name = new filename();
            args.to.name.value = to_child;
            Procedure proc = NfsUtils.Procedure.NFSPROC_RENAME;
            ProcKey proc_key = NfsUtils.getProcKey(proc);
            Object nonce = null;
            SinkIF sink = null;
            RpcCall rpc_call = new RpcCall(server_addr, proc_key, args, rpc_app_id, nonce, sink);
            rpc_call.cb = curry(rename_server_cb, from, to, fuse_cb);
            rpc_stage.handleEvent(rpc_call);
            return;
        }
    };

    private Thunk5<String, String, Thunk1<FuseException>, RpcCall, RpcReply> rename_server_cb = new Thunk5<String, String, Thunk1<FuseException>, RpcCall, RpcReply>() {

        public void run(String from, String to, Thunk1<FuseException> fuse_cb, RpcCall rpc_call, RpcReply rpc_reply) {
            if (logger.isDebugEnabled()) logger.debug("rename - received server response: " + "from=" + from + " to=" + to);
            statres reply_args = (statres) rpc_reply.reply;
            int status = reply_args.status;
            if (status != stat.NFS_OK) BUG("Unhandled error: status=" + status);
            fhandle parent_fh = ((renameargs) rpc_call.args).from.dir;
            String child = ((renameargs) rpc_call.args).from.name.value;
            removeFromFhandleMap(parent_fh, child);
            if (CACHE_ATTRS) {
                String from_parent = FsUtils.getParent(from);
                attrCacheRemove(from_parent);
                attrCacheRemove(from);
                String to_parent = FsUtils.getParent(to);
                attrCacheRemove(to_parent);
            }
            if (logger.isInfoEnabled()) logger.info("rename - done: from=" + from + " to=" + to);
            FuseException fe = null;
            fuse_cb.run(fe);
            return;
        }
    };

    protected void chmod(String path, int mode, Thunk1<FuseException> fuse_cb) {
        if (logger.isInfoEnabled()) logger.info("chmod - start: path=" + path + " mode=0o" + Integer.toOctalString(mode));
        Thunk1<fhandle> cb = curry(chmod_fhandle_cb, path, mode, fuse_cb);
        resolveFhandle(path, cb);
        return;
    }

    private Thunk4<String, Integer, Thunk1<FuseException>, fhandle> chmod_fhandle_cb = new Thunk4<String, Integer, Thunk1<FuseException>, fhandle>() {

        public void run(String path, Integer mode, Thunk1<FuseException> fuse_cb, fhandle nfs_fh) {
            if (logger.isDebugEnabled()) logger.debug("chmod - resolved fhandle: path=" + path + " fh=" + XdrUtils.toString(nfs_fh));
            sattrargs args = new sattrargs();
            args.file = nfs_fh;
            args.attributes = NfsUtils.newImpotentSattr();
            args.attributes.mode = mode;
            Procedure proc = NfsUtils.Procedure.NFSPROC_SETATTR;
            ProcKey proc_key = NfsUtils.getProcKey(proc);
            Object nonce = null;
            SinkIF sink = null;
            RpcCall rpc_call = new RpcCall(server_addr, proc_key, args, rpc_app_id, nonce, sink);
            rpc_call.cb = curry(chmod_server_cb, path, mode, fuse_cb);
            rpc_stage.handleEvent(rpc_call);
            return;
        }
    };

    private Thunk5<String, Integer, Thunk1<FuseException>, RpcCall, RpcReply> chmod_server_cb = new Thunk5<String, Integer, Thunk1<FuseException>, RpcCall, RpcReply>() {

        public void run(String path, Integer mode, Thunk1<FuseException> fuse_cb, RpcCall rpc_call, RpcReply rpc_reply) {
            if (logger.isDebugEnabled()) logger.debug("chmod - received server response: " + "path=" + path);
            attrstat reply_args = (attrstat) rpc_reply.reply;
            int status = reply_args.status;
            if (status != stat.NFS_OK) BUG("Unhandled error: status=" + status);
            fattr attrs = reply_args.attributes;
            if (CACHE_ATTRS) attrCacheUpdate(path, attrs);
            if (logger.isInfoEnabled()) logger.info("chmod - done: path=" + path);
            FuseException fe = null;
            fuse_cb.run(fe);
            return;
        }
    };

    protected void chown(String path, int uid, int gid, Thunk1<FuseException> fuse_cb) {
        if (logger.isInfoEnabled()) logger.info("chown - start: path=" + path + " uid=" + uid + " gid=" + gid);
        Thunk1<fhandle> cb = curry(chown_fhandle_cb, path, uid, gid, fuse_cb);
        resolveFhandle(path, cb);
        return;
    }

    private Thunk5<String, Integer, Integer, Thunk1<FuseException>, fhandle> chown_fhandle_cb = new Thunk5<String, Integer, Integer, Thunk1<FuseException>, fhandle>() {

        public void run(String path, Integer uid, Integer gid, Thunk1<FuseException> fuse_cb, fhandle nfs_fh) {
            if (logger.isDebugEnabled()) logger.debug("chown - resolved fhandle: path=" + path + " fh=" + XdrUtils.toString(nfs_fh));
            sattrargs args = new sattrargs();
            args.file = nfs_fh;
            args.attributes = NfsUtils.newImpotentSattr();
            args.attributes.uid = uid;
            args.attributes.gid = gid;
            Procedure proc = NfsUtils.Procedure.NFSPROC_SETATTR;
            ProcKey proc_key = NfsUtils.getProcKey(proc);
            Object nonce = null;
            SinkIF sink = null;
            RpcCall rpc_call = new RpcCall(server_addr, proc_key, args, rpc_app_id, nonce, sink);
            rpc_call.cb = curry(chown_server_cb, path, uid, gid, fuse_cb);
            rpc_stage.handleEvent(rpc_call);
            return;
        }
    };

    private Thunk6<String, Integer, Integer, Thunk1<FuseException>, RpcCall, RpcReply> chown_server_cb = new Thunk6<String, Integer, Integer, Thunk1<FuseException>, RpcCall, RpcReply>() {

        public void run(String path, Integer uid, Integer gid, Thunk1<FuseException> fuse_cb, RpcCall rpc_call, RpcReply rpc_reply) {
            if (logger.isDebugEnabled()) logger.debug("chown - received server response: " + "path=" + path);
            attrstat reply_args = (attrstat) rpc_reply.reply;
            int status = reply_args.status;
            if (status != stat.NFS_OK) BUG("Unhandled error: status=" + status);
            fattr attrs = reply_args.attributes;
            if (CACHE_ATTRS) attrCacheUpdate(path, attrs);
            if (logger.isInfoEnabled()) logger.info("chown - done: path=" + path);
            FuseException fe = null;
            fuse_cb.run(fe);
            return;
        }
    };

    protected void utime(String path, int atime, int mtime, Thunk1<FuseException> fuse_cb) {
        if (logger.isInfoEnabled()) logger.info("utime - start: path=" + path + " atime=" + atime + " mtime=" + mtime);
        Thunk1<fhandle> cb = curry(utime_fhandle_cb, path, atime, mtime, fuse_cb);
        resolveFhandle(path, cb);
        return;
    }

    private Thunk5<String, Integer, Integer, Thunk1<FuseException>, fhandle> utime_fhandle_cb = new Thunk5<String, Integer, Integer, Thunk1<FuseException>, fhandle>() {

        public void run(String path, Integer atime, Integer mtime, Thunk1<FuseException> fuse_cb, fhandle nfs_fh) {
            if (logger.isDebugEnabled()) logger.debug("utime - resolved fhandle: path=" + path + " fh=" + XdrUtils.toString(nfs_fh));
            sattrargs args = new sattrargs();
            args.file = nfs_fh;
            args.attributes = NfsUtils.newImpotentSattr();
            args.attributes.atime = new timeval();
            args.attributes.atime.seconds = atime;
            args.attributes.atime.useconds = 0;
            args.attributes.mtime = new timeval();
            args.attributes.mtime.seconds = mtime;
            args.attributes.mtime.useconds = 0;
            Procedure proc = NfsUtils.Procedure.NFSPROC_SETATTR;
            ProcKey proc_key = NfsUtils.getProcKey(proc);
            Object nonce = null;
            SinkIF sink = null;
            RpcCall rpc_call = new RpcCall(server_addr, proc_key, args, rpc_app_id, nonce, sink);
            rpc_call.cb = curry(utime_server_cb, path, atime, mtime, fuse_cb);
            rpc_stage.handleEvent(rpc_call);
            return;
        }
    };

    private Thunk6<String, Integer, Integer, Thunk1<FuseException>, RpcCall, RpcReply> utime_server_cb = new Thunk6<String, Integer, Integer, Thunk1<FuseException>, RpcCall, RpcReply>() {

        public void run(String path, Integer atime, Integer mtime, Thunk1<FuseException> fuse_cb, RpcCall rpc_call, RpcReply rpc_reply) {
            if (logger.isDebugEnabled()) logger.debug("utime - received server response: " + "path=" + path);
            attrstat reply_args = (attrstat) rpc_reply.reply;
            int status = reply_args.status;
            if (status != stat.NFS_OK) BUG("Unhandled error: status=" + status);
            fattr attrs = reply_args.attributes;
            if (CACHE_ATTRS) attrCacheUpdate(path, attrs);
            if (logger.isInfoEnabled()) logger.info("utime - done: path=" + path);
            FuseException fe = null;
            fuse_cb.run(fe);
            return;
        }
    };

    private void resolveFhandle(String path, Thunk1<fhandle> resolve_cb) {
        if (logger.isDebugEnabled()) logger.debug("resolve - start: path=" + path);
        String root = FsUtils.FILE_SEPARATOR;
        resolve_fh.run(path, root, root_fh, resolve_cb);
        return;
    }

    private Thunk4<String, String, fhandle, Thunk1<fhandle>> resolve_fh = new Thunk4<String, String, fhandle, Thunk1<fhandle>>() {

        public void run(String path, String parent_path, fhandle parent_fh, Thunk1<fhandle> resolve_cb) {
            if (logger.isDebugEnabled()) logger.debug("resolve - continuing: path=" + path + " parent_path=" + parent_path);
            String child = FsUtils.getNextPathComponent(path, parent_path);
            if (child == null) {
                assert (parent_fh != null) : ("Failed to resolve path to fhandle: path=" + path);
                resolve_cb.run(parent_fh);
                return;
            }
            NfsObjectId child_id = new NfsObjectId(parent_fh, child);
            fhandle fh = fhandle_map.get(child_id);
            if (fh == null) {
                if (logger.isDebugEnabled()) logger.debug("resolve - requesting fh from server: " + "parent_fh=" + XdrUtils.toString(parent_fh) + " child=" + child);
                Thunk1<fhandle> cb = curry(resolve_fh_lookup_cb, path, parent_path, parent_fh, resolve_cb);
                lookup(parent_fh, child, cb);
            } else {
                String child_path = FsUtils.makePathName(parent_path, child);
                resolve_fh.run(path, child_path, fh, resolve_cb);
            }
            return;
        }
    };

    private Thunk5<String, String, fhandle, Thunk1<fhandle>, fhandle> resolve_fh_lookup_cb = new Thunk5<String, String, fhandle, Thunk1<fhandle>, fhandle>() {

        public void run(String path, String parent_path, fhandle parent_fh, Thunk1<fhandle> resolve_cb, fhandle fh) {
            if (fh == null) {
                resolve_cb.run(null);
                return;
            }
            resolve_fh.run(path, parent_path, parent_fh, resolve_cb);
            return;
        }
    };

    private void lookup(fhandle parent_fh, String child, Thunk1<fhandle> cb) {
        if (logger.isDebugEnabled()) logger.debug("lookup - start: parent_fh=" + XdrUtils.toString(parent_fh) + " child=" + child);
        diropargs args = new diropargs();
        args.dir = parent_fh;
        args.name = new filename();
        args.name.value = child;
        Procedure proc = NfsUtils.Procedure.NFSPROC_LOOKUP;
        ProcKey proc_key = NfsUtils.getProcKey(proc);
        Object nonce = null;
        SinkIF sink = null;
        RpcCall rpc_call = new RpcCall(server_addr, proc_key, args, rpc_app_id, nonce, sink);
        rpc_call.cb = curry(lookup_cb, parent_fh, child, cb);
        rpc_stage.handleEvent(rpc_call);
        return;
    }

    private Thunk5<fhandle, String, Thunk1<fhandle>, RpcCall, RpcReply> lookup_cb = new Thunk5<fhandle, String, Thunk1<fhandle>, RpcCall, RpcReply>() {

        public void run(fhandle parent_fh, String child, Thunk1<fhandle> cb, RpcCall rpc_call, RpcReply rpc_reply) {
            diropres reply_args = (diropres) rpc_reply.reply;
            int status = reply_args.status;
            if (status == stat.NFSERR_NOENT) {
                if (logger.isDebugEnabled()) logger.debug("lookup - done, does not exist: " + "parent_fh=" + XdrUtils.toString(parent_fh) + " child=" + child);
                cb.run((fhandle) null);
                return;
            }
            if (status != stat.NFS_OK) BUG("Unhandled error: status=" + status);
            fhandle fh = reply_args.diropok.file;
            fattr attr = reply_args.diropok.attributes;
            NfsObjectId obj_id = new NfsObjectId(parent_fh, child);
            fhandle_map.put(obj_id, fh);
            if (logger.isDebugEnabled()) logger.debug("lookup - done: " + "parent_fh=" + XdrUtils.toString(parent_fh) + " child=" + child + " child_fh=" + XdrUtils.toString(fh) + " obj_id=" + obj_id);
            cb.run(fh);
            return;
        }
    };

    private void removeFromFhandleMap(fhandle parent_fh, String child) {
        NfsObjectId obj_id = new NfsObjectId(parent_fh, child);
        fhandle_map.remove(obj_id);
        return;
    }

    private void registerRpcProcedures(Runnable cb) {
        if (logger.isInfoEnabled()) logger.info("Registering RPC procedures.");
        Map<ProcKey, ProcValue> proc_map = NfsUtils.getNfsRpcProcedureMap();
        boolean client = true;
        Object nonce = null;
        SinkIF sink = null;
        RpcRegisterReq rpc_reg_req = new RpcRegisterReq(proc_map, client, rpc_app_id, nonce, sink);
        rpc_reg_req.cb = curry(register_rpc_proc_cb, cb);
        rpc_stage.handleEvent(rpc_reg_req);
        return;
    }

    private Thunk3<Runnable, RpcRegisterReq, RpcRegisterResp> register_rpc_proc_cb = new Thunk3<Runnable, RpcRegisterReq, RpcRegisterResp>() {

        public void run(Runnable cb, RpcRegisterReq rpc_call, RpcRegisterResp rpc_reply) {
            if (logger.isInfoEnabled()) logger.info("Done register RPC messages.");
            cb.run();
            return;
        }
    };

    private void sendNull(Runnable cb) {
        logger.info("Sending null operation");
        XdrVoid args = XdrVoid.XDR_VOID;
        Procedure proc = NfsUtils.Procedure.NFSPROC_NULL;
        ProcKey proc_key = NfsUtils.getProcKey(proc);
        Object nonce = null;
        SinkIF sink = null;
        RpcCall rpc_call = new RpcCall(server_addr, proc_key, args, rpc_app_id, nonce, sink);
        rpc_call.cb = curry(null_cb, cb);
        rpc_stage.handleEvent(rpc_call);
        return;
    }

    private Thunk3<Runnable, RpcCall, RpcReply> null_cb = new Thunk3<Runnable, RpcCall, RpcReply>() {

        public void run(Runnable cb, RpcCall rpc_call, RpcReply rpc_reply) {
            logger.info("Received reply to null operation.");
            return;
        }
    };

    private long getFreeFd() {
        assert (fd_set.size() > 0);
        long fd = -1;
        synchronized (fd_set) {
            Iterator<Long> fd_iter = fd_set.iterator();
            fd = fd_iter.next();
            fd_set.remove(fd);
        }
        return fd;
    }

    private void freeFd(long fd) {
        assert (!fd_set.contains(fd));
        fd_set.add(fd);
        return;
    }

    private static class NfsObjectId {

        public SecureHash parent_fh;

        public String child;

        public NfsObjectId(fhandle parent_fh, String child) {
            this.parent_fh = new SHA1Hash(parent_fh.value);
            this.child = child;
            return;
        }

        public String toString() {
            String s = "(NfsObjectId:" + " parent_fh=" + parent_fh + " child=" + child + ")";
            return s;
        }

        public int hashCode() {
            return parent_fh.hashCode();
        }

        public boolean equals(Object other) {
            boolean equal;
            if (other == null) {
                return false;
            }
            if (other == this) {
                return true;
            }
            if (!(other instanceof NfsObjectId)) {
                return false;
            }
            NfsObjectId rhs = (NfsObjectId) other;
            return ((this.parent_fh.equals(rhs.parent_fh)) && (this.child.equals(rhs.child)));
        }
    }

    private static class AttrCacheEntry {

        public fattr attr;

        public Object timer_token;

        public AttrCacheEntry(fattr attr, Object timer_token) {
            this.attr = attr;
            this.timer_token = timer_token;
        }
    }

    private void attrCacheUpdate(String path, fattr attrs) {
        attrCacheRemove(path);
        Object timer_token = acore.registerTimer(ATTR_CACHE_TIMEOUT_MS, curry(attr_cache_rm, path));
        AttrCacheEntry entry = new AttrCacheEntry(attrs, timer_token);
        attr_cache.put(path, entry);
        return;
    }

    private Thunk1<String> attr_cache_rm = new Thunk1<String>() {

        public void run(String path) {
            attrCacheRemove(path);
            return;
        }
    };

    private void attrCacheRemove(String path) {
        AttrCacheEntry entry = attr_cache.remove(path);
        if (entry != null) {
            Object timer_token = entry.timer_token;
            acore.cancelTimer(timer_token);
            dataCachePurge(path);
        }
        return;
    }

    private void dataCacheUpdate(String path, int offset, readres read_res) {
        String cache_path = getCachePath(path) + "." + offset;
        try {
            File cache_file = new File(cache_path);
            File parent_dir = cache_file.getParentFile();
            boolean dirs_created = parent_dir.mkdirs();
            boolean file_created = cache_file.createNewFile();
            FileOutputStream fout = new FileOutputStream(cache_file);
            byte[] data = read_res.readopok.data.value;
            fout.write(data);
            fout.close();
        } catch (Exception e) {
            logger.fatal("Failed to write block to cache: path=" + path + " cache_path=" + cache_path + " except=" + e);
            return;
        }
        return;
    }

    private byte[] dataCacheRead(String path, int offset) {
        String cache_path = getCachePath(path) + "." + offset;
        byte[] data = (byte[]) null;
        try {
            File cache_file = new File(cache_path);
            if (!cache_file.exists()) return data;
            int block_size = (int) cache_file.length();
            data = new byte[block_size];
            FileInputStream fin = new FileInputStream(cache_file);
            int bytes_read = 0;
            while (bytes_read < block_size) {
                bytes_read += fin.read(data, bytes_read, block_size - bytes_read);
            }
            fin.close();
        } catch (Exception e) {
            logger.fatal("Failed to read block from cache: path=" + path + " cache_path=" + cache_path + " except=" + e);
        }
        return data;
    }

    private void dataCachePurge(String path) {
        String cache_path = getCachePath(path);
        try {
            File cache_file = new File(cache_path);
            File parent_dir = cache_file.getParentFile();
            if (!parent_dir.exists()) return;
            FilenameFilter cache_block_filter = new CacheBlockFilenameFilter(cache_file);
            File[] cache_block_files = parent_dir.listFiles(cache_block_filter);
            for (File f : cache_block_files) {
                boolean success = f.delete();
                if (!success) BUG("Failed to delete data block in cache: " + f);
            }
        } catch (Exception e) {
            BUG("Failed to purge file from cache: path=" + path + " cache_path=" + cache_path + " except=" + e);
        }
        return;
    }

    private static class CacheBlockFilenameFilter implements FilenameFilter {

        private File parent_dir;

        private String root_name;

        public CacheBlockFilenameFilter(File cache_file_basename) {
            this.parent_dir = cache_file_basename.getParentFile();
            this.root_name = cache_file_basename.getName();
            return;
        }

        public boolean accept(File dir, String name) {
            return (parent_dir.equals(dir) && name.startsWith(root_name));
        }
    }

    private String getCachePath(String path) {
        SecureHash phash = new SHA1Hash(path);
        String pbytes = ByteUtils.print_bytes(phash.bytes());
        while (pbytes.length() < DATA_CACHE_PATH_LENGTH) pbytes = "0" + pbytes;
        String cache_path = DATA_CACHE_ROOT + "/" + pbytes.substring(pbytes.length() - 2, pbytes.length()) + "/" + pbytes.substring(pbytes.length() - 4, pbytes.length() - 2) + "/" + pbytes;
        return cache_path;
    }
}
