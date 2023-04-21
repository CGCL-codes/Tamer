package ch.epfl.lsr.adhoc.routing.ncode.feedback;

import java.nio.ByteBuffer;
import java.util.*;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.optimization.OptimizationException;
import org.apache.commons.math.optimization.RealPointValuePair;
import org.apache.commons.math.optimization.linear.LinearConstraint;
import org.apache.commons.math.optimization.linear.LinearObjectiveFunction;
import org.apache.commons.math.optimization.linear.Relationship;
import org.apache.commons.math.optimization.linear.SimplexSolver;
import util.bloom.*;
import ch.epfl.lsr.adhoc.routing.ncode.*;
import ch.epfl.lsr.adhoc.runtime.FrancRuntime;
import ch.epfl.lsr.adhoc.runtime.Parameters;
import ch.epfl.lsr.adhoc.runtime.Message;
import ch.epfl.lsr.adhoc.runtime.MessagePool;
import ch.epfl.lsr.adhoc.runtime.SendMessageFailedException;
import ch.epfl.lsr.adhoc.runtime.ParamDoesNotExistException;

/**
 * This layer implements : network coding.
 * <p>
 * Every message received, is stored in a buffer. At the time of transmission 
 * a random linear combination of packets in buffer is sent.
 * The message is stored in the buffer, when:
 * <ul>
 * <li>message not already received (srcNode & seqNumber)</li>
 * <li>TTL &gt; 0</li>
 * <li>srcNode not this node</li>
 * <li>dstNode not this node</li>
 * * </ul>
 * <p>
 * A decoded message is propagated up the stack, if its destination is this node, or
 * if it is a broadcast (destination node number is 0).
 * The message will be propagated up the stack, if:
 * <ul>
 * <li>The message is for this node</li>
 * <li>Or the message is a broadcast</li>
 * </ul>
 * <p>
 * @author Kave Salamatian
 * @version 1.0
 */
@SuppressWarnings("deprecation")
public class NcodeFB extends Ncode {

    private Parameters params;

    public char msgTypeFB;

    /** Used to retransmit a message after a random delay */
    private DelayedTransmission dt;

    protected NCFBDatagramPool ncfbpool;

    /** The node if of this node. */
    private long myNodeID;

    /** A reference to message pool. */
    public MessagePool mp;

    public Hashtable<String, FbData> neighbourhood = new Hashtable<String, FbData>();

    public BloomFilter decodedBF;

    public BloomFilter variableBF;

    public static int unreceivedWeight = 10;

    public static int variableWeight = 1;

    public static int capacity = 5;

    public static int activityDeadline = 100000000;

    public int flag;

    public int recv_beacon = 0;

    public int rejPkt = 0;

    /** Default constructor */
    public NcodeFB(String name, Parameters params) {
        super(name, params);
        this.params = params;
    }

    /**
   * Initializes the network coding layer.
   * <p>
   * The following configuration parameters are read:
   * <ul>
   * <li><b>layer.Flooding.bufferLength</b> - The number of entries for
   *     identifying the most recently received messages</li>
   * <li><b>layers.Flooding.prT</b> - The probability that a message is retransmitted</li>
   * <li><b>layers.Flooding.delayMay</b> - The maximum delay for retransmitting a message</li>
   * </ul>
   *
   */
    public void initialize(FrancRuntime runtime) {
        int delayMax = 0;
        try {
            delayMax = params.getInt("delayMax");
        } catch (ParamDoesNotExistException pdnee) {
            throw new RuntimeException("Error reading configuration value delayMax: " + pdnee.getMessage());
        }
        if (delayMax < 0) throw new RuntimeException("Configuration variable delayMax cannot be negative");
        NCFBGlobals.nodeID_ = runtime.getNodeID();
        NCGlobals.nodeID_ = runtime.getNodeID();
        this.dt = new DelayedTransmission(this, delayMax);
        this.myNodeID = runtime.getNodeID();
        this.mp = runtime.getMessagePool();
        this.cfpool = new CoefEltPool();
        this.ncfbpool = new NCFBDatagramPool(cfpool);
        GF = NCFBGlobals.GF;
        varList = new Vector<Pkt_ID>();
        decodedList = new Vector<Pkt_ID>();
        decodedBF = new BloomFilter(320, 5, 1);
        byte[] nID = ByteBuffer.allocate(8).putLong(runtime.getNodeID()).array();
        variableBF = new BloomFilter(320, 5, 1);
        nID[0] = (byte) (nID[0] + 1);
        rank = 0;
        try {
            NCFBGlobals.MaxBufLength = params.getInt("MaximumDataUnit");
        } catch (ParamDoesNotExistException pdnee) {
            throw new RuntimeException("Error reading configuration value MDU: " + pdnee.getMessage());
        }
        try {
            MCLU = params.getInt("MaximumCLU");
        } catch (ParamDoesNotExistException pdnee) {
            throw new RuntimeException("Error reading configuration value MCLU: " + pdnee.getMessage());
        }
        try {
            capacity = params.getInt("Capacity");
        } catch (ParamDoesNotExistException pdnee) {
            throw new RuntimeException("Error reading configuration value capacity: " + pdnee.getMessage());
        }
        try {
            unreceivedWeight = params.getInt("UnreceivedWeight");
        } catch (ParamDoesNotExistException pdnee) {
            throw new RuntimeException("Error reading configuration value UnreceivedWeight: " + pdnee.getMessage());
        }
        try {
            variableWeight = params.getInt("VariableWeight");
        } catch (ParamDoesNotExistException pdnee) {
            throw new RuntimeException("Error reading configuration value UnreceivedWeight: " + pdnee.getMessage());
        }
        try {
            activityDeadline = params.getInt("ActivityDeadline");
        } catch (ParamDoesNotExistException pdnee) {
            throw new RuntimeException("Error reading configuration value ActivityDeadline: " + pdnee.getMessage());
        }
        String msgName = null;
        try {
            msgName = params.getString("msgType");
        } catch (ParamDoesNotExistException pdnee) {
            pdnee.printStackTrace();
            throw new RuntimeException("Could not read configuration parameter 'msgType' " + "for NCode layer: " + pdnee.getMessage());
        }
        if (msgName == null) {
            throw new RuntimeException("Could not read configuration parameter 'msgType' " + "for NCode layer");
        }
        try {
            msgType = mp.getMessageType(msgName);
        } catch (Exception ex) {
            throw new RuntimeException("Message type '" + msgName + "' not found");
        }
        rnd = new Random(System.currentTimeMillis());
        PktIndex = 0;
        logger = NCFBGlobals.logger;
    }

    public void NetCod_Module(int mdu, int mclu, int HCL, int port) throws GaloisException {
        try {
            base = new GaloisField(2);
            GF = new ExtendedGaloisField(base, 'a', 8);
        } catch (GaloisException e) {
            e.printStackTrace();
        }
        rank = 0;
        try {
            MCLU = params.getInt("MaximumCLU");
        } catch (ParamDoesNotExistException pdnee) {
            throw new RuntimeException("Error reading configuration value MCLU: " + pdnee.getMessage());
        }
        try {
            HCL = params.getInt("HCL");
        } catch (ParamDoesNotExistException pdnee) {
            throw new RuntimeException("Error reading configuration value HCL: " + pdnee.getMessage());
        }
        String msgName = null;
        try {
            msgName = params.getString("msgType");
        } catch (ParamDoesNotExistException pdnee) {
            pdnee.printStackTrace();
            throw new RuntimeException("Could not read configuration parameter 'msgType' " + "for NCode layer: " + pdnee.getMessage());
        }
        if (msgName == null) {
            throw new RuntimeException("Could not read configuration parameter 'msgType' " + "for NCode layer");
        }
        try {
            msgType = mp.getMessageType(msgName);
        } catch (Exception ex) {
            throw new RuntimeException("Message type '" + msgName + "' not found");
        }
        msgName = null;
        try {
            msgName = params.getString("msgTypeFB");
        } catch (ParamDoesNotExistException pdnee) {
            pdnee.printStackTrace();
            throw new RuntimeException("Could not read configuration parameter 'msgTypeFB' " + "for NCode layer: " + pdnee.getMessage());
        }
        if (msgName == null) {
            throw new RuntimeException("Could not read configuration parameter 'msgTypeFB' " + "for NCode layer");
        }
        try {
            msgTypeFB = mp.getMessageType(msgName);
        } catch (Exception ex) {
            throw new RuntimeException("Message type '" + msgName + "' not found");
        }
        rnd = new Random(System.currentTimeMillis());
    }

    /**
   * Starts the threads for this layer and for retransmitting messages after a
   * certain delay.
   */
    public void startup() {
        start();
        dt.start();
    }

    /**
   * This method implements what to do when we receive a message
   * it up in the stack.
   * For more details see the class description.
   * <p>
   * @param msg The message to handle
   */
    protected void handleMessage(Message msg) {
        if (msg == null) {
            System.out.println("> # message is null");
            return;
        }
        if (msg.getSrcNode() == myNodeID) {
            return;
        }
        if (msg.getType() == msgType) {
            int len = ((NCodeFBMessage) msg).getInBuf(inBuf);
            NCFBdatagram ncfb = ncfbpool.getNCFBdatagram();
            ncfb.setNodeID(msg.getSrcNode());
            try {
                if (ncfb.unserialize(inBuf, cfpool) > 0) {
                    logger.info("NID: " + myNodeID + " TIME: " + System.currentTimeMillis() % 1000000 + " RCVFROM: " + msg.getSrcNode() + " IND: " + ncfb.getIndex() + " #COEF: " + ncfb.coefs_list.size());
                    if (!neighbourhood.containsKey(msg.getSrcNode())) {
                        neighbourhood.put(String.valueOf(msg.getSrcNode()), ncfb.fbData);
                    } else {
                        neighbourhood.remove(String.valueOf(msg.getSrcNode()));
                        neighbourhood.put(String.valueOf(msg.getSrcNode()), ncfb.fbData);
                    }
                    if (ncfb.dataLength == 0) {
                        recv_beacon++;
                    } else {
                        recvPkt++;
                        decode(ncfb);
                    }
                } else {
                }
            } catch (RuntimeException e) {
                logger.debug("Fragmentation happened !!");
            }
            mp.freeMessage(msg);
        } else {
            super.handleMessage(msg);
        }
    }

    public int sendMessage(Message msg) throws SendMessageFailedException {
        Coef_Elt coef;
        NCFBdatagram tmpNCFBdatagram = (NCFBdatagram) ncfbpool.getNCFBdatagram();
        if (msg.getType() == 5) {
            if (msg.getTTL() <= 0) return 0;
            msg.setNextHop(0);
            return super.sendMessage(msg);
        } else {
            logger.info("NID: " + myNodeID + " TIME: " + System.currentTimeMillis() % 1000000 + " APPLI: IN " + msg);
            int l = msg.getByteArray(tmpNCFBdatagram.Buf);
            tmpNCFBdatagram.setLength(l);
            tmpNCFBdatagram.setDecoded();
            tmpNCFBdatagram.setIndex(PktIndex);
            coef = cfpool.getCoefElt(1, PktIndex++);
            tmpNCFBdatagram.coefs_list.put(coef.key(), coef);
            decodedBuf.add(tmpNCFBdatagram);
            decodedList.add(coef.getID());
            mp.freeMessage(msg);
        }
        return 1;
    }

    public void generateEncoded() {
        NCFBdatagram tmpEncDatagram;
        try {
            if (!decodedBuf.isEmpty()) {
                tmpEncDatagram = encode();
                synchronized (tmpEncDatagram) {
                    decodedBF.clear();
                    byte[] nID = ByteBuffer.allocate(8).putLong(NCFBGlobals.nodeID_).array();
                    for (int i = 0; i < decodedList.size(); decodedBF.add(new Key(decodedList.elementAt(i++).toString().getBytes()))) ;
                    variableBF.clear();
                    nID[0] = (byte) (nID[0] + 1);
                    for (int i = 0; i < varList.size(); variableBF.add(new Key(varList.elementAt(i++).toString().getBytes()))) ;
                    tmpEncDatagram.fbData.setData(rank, varList.size(), decodedList.size(), capacity - varList.size(), decodedBF, variableBF);
                    if (tmpEncDatagram != null && !tmpEncDatagram.isNull()) {
                        tmpEncDatagram.setIndex(PktIndex++);
                        int len = tmpEncDatagram.serialize(encBuf);
                        NCodeFBMessage tm = (NCodeFBMessage) mp.getMessage(msgType);
                        tm.setOutbuf(encBuf, len);
                        tm.setDstNode(0);
                        tm.setTTL(1);
                        logger.info("NID: " + myNodeID + " TIME: " + System.currentTimeMillis() % 1000000 + " SEND: E " + " IND: " + tmpEncDatagram.getIndex() + " #COEF: " + tmpEncDatagram.coefs_list.size());
                        send(tm);
                        sendPkt++;
                        ncfbpool.freeNCFBdatagram(tmpEncDatagram);
                    }
                }
            }
        } catch (GaloisException e) {
            logger.error("NetCod_Module: generate Encoded: " + e);
        } catch (SendMessageFailedException e) {
            e.printStackTrace();
        }
    }

    public synchronized NCFBdatagram encode() throws GaloisException {
        int i, L = 0, l, len, I, index;
        Coef_Elt coeff;
        int coef_;
        NCFBdatagram g, gclone = ncfbpool.getNCFBdatagram(), g1;
        Vector<LinearConstraint> constraints = new Vector<LinearConstraint>(10);
        int numVar = 0;
        double[] var, F, X, B;
        double[][] AA;
        NCFBdatagram nc = ncfbpool.getNCFBdatagram();
        synchronized (neighbourhood) {
            synchronized (decodedList) {
                l = (decodedBuf.size() + varList.size());
                len = (decodingBuf.size() + decodedBuf.size());
                L = decodedBuf.size();
                AA = new double[neighbourhood.size()][l];
                B = new double[neighbourhood.size()];
                F = new double[l];
                X = new double[l];
                for (i = 0; i < l; X[i] = 0, F[i++] = 0) ;
                if (!neighbourhood.isEmpty()) {
                    Enumeration<FbData> e = neighbourhood.elements();
                    FbData nfb;
                    index = 0;
                    while (e.hasMoreElements()) {
                        nfb = (FbData) e.nextElement();
                        B[index] = nfb.capacity;
                        if (nfb.active) {
                            if (System.currentTimeMillis() - nfb.time > activityDeadline) {
                                nfb.disactivate();
                            } else {
                                for (i = 0; i < L; i++) {
                                    if (!nfb.bfdecoded.membershipTest(new Key(decodedList.elementAt(i).toString().getBytes()))) {
                                        if (nfb.bfvariable.membershipTest(new Key(decodedList.elementAt(i).toString().getBytes()))) {
                                            F[i] = F[i] + variableWeight;
                                            X[i] = 1;
                                            constraints.add(new LinearConstraint(X, Relationship.LEQ, 1));
                                            X[i] = 0;
                                        } else {
                                            F[i] = F[i] + unreceivedWeight;
                                            X[i] = 1;
                                            constraints.add(new LinearConstraint(X, Relationship.LEQ, 1));
                                            X[i] = 0;
                                            AA[index][i] = 1;
                                        }
                                    }
                                }
                            }
                            synchronized (decodingBuf) {
                                for (i = 0; i < len - L; i++) {
                                    g = (NCFBdatagram) decodingBuf.elementAt(i);
                                    Enumeration ecoef = g.coefs_list.elements();
                                    numVar = g.coefs_list.size();
                                    while (ecoef.hasMoreElements()) {
                                        coeff = (Coef_Elt) ecoef.nextElement();
                                        synchronized (varList) {
                                            I = varList.indexOf(coeff.getID());
                                            if (!nfb.bfdecoded.membershipTest(new Key(varList.elementAt(I).toString().getBytes()))) {
                                                if (nfb.bfvariable.membershipTest(new Key(varList.elementAt(I).toString().getBytes()))) {
                                                    F[I + L] = F[I + L] + (double) variableWeight / numVar;
                                                    X[I + L] = 1;
                                                    constraints.add(new LinearConstraint(X, Relationship.LEQ, 1));
                                                    X[I + L] = 0;
                                                } else {
                                                    X[I + L] = 1;
                                                    constraints.add(new LinearConstraint(X, Relationship.LEQ, 1));
                                                    X[I + L] = 0;
                                                    F[I + L] = F[I + L] + unreceivedWeight / numVar;
                                                    AA[index][I + L] = 1;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            B[index] = (int) nfb.capacity;
                            constraints.add(new LinearConstraint(AA[index], Relationship.LEQ, B[index]));
                            index++;
                        }
                    }
                }
                if (index > 0) {
                    LinearObjectiveFunction lobj = new LinearObjectiveFunction(F, 0.0);
                    SimplexSolver solver = new SimplexSolver();
                    RealPointValuePair solution;
                    try {
                        solution = solver.optimize(lobj, constraints, GoalType.MAXIMIZE, true);
                        var = solution.getPoint();
                        boolean first = true;
                        for (i = 0; i < L; i++) {
                            if (Math.random() < var[i]) {
                                coef_ = rnd.nextInt(255 - 1) + 1;
                                g = (NCFBdatagram) decodedBuf.elementAt(i);
                                gclone = (NCFBdatagram) ncfbpool.clone(g);
                                if (first) {
                                    nc = gclone.product(coef_);
                                    first = false;
                                } else {
                                    nc = nc.sum(gclone.product(coef_), cfpool);
                                }
                            }
                        }
                        float[] score = new float[decodingBuf.size()];
                        for (i = 0; i < decodingBuf.size(); i++) {
                            g = (NCFBdatagram) decodedBuf.elementAt(i);
                            Enumeration ecoef = g.coefs_list.elements();
                            numVar = g.coefs_list.size();
                            while (ecoef.hasMoreElements()) {
                                coeff = (Coef_Elt) ecoef.nextElement();
                                I = varList.indexOf(coeff.getID());
                                score[i] = (float) var[L + I] / numVar;
                            }
                        }
                        for (i = 0; i < decodingBuf.size(); i++) {
                            if (Math.random() < score[i]) {
                                coef_ = rnd.nextInt(255 - 1) + 1;
                                g = (NCFBdatagram) decodingBuf.elementAt(i);
                                gclone = (NCFBdatagram) ncfbpool.clone(g);
                                if (first) {
                                    nc = gclone.product(coef_);
                                    first = false;
                                } else {
                                    nc = nc.sum(gclone.product(coef_), cfpool);
                                }
                            }
                        }
                    } catch (OptimizationException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }
        return nc;
    }

    public synchronized void extractSolved(int M, int N) throws GaloisException, Exception {
        int i, j, k, l, upPivot;
        NCFBdatagram g1, g2;
        Coef_Elt coef;
        Pkt_ID id;
        boolean Solved = true;
        if (M < decodingBuf.size()) {
            for (i = M; i < decodingBuf.size(); i++) {
                decodingBuf.remove(i);
            }
        }
        for (i = M - 1; i >= 0; i--) {
            Solved = true;
            g1 = (NCFBdatagram) decodingBuf.elementAt(i);
            synchronized (g1) {
                Enumeration<Coef_Elt> ecoef = g1.coefs_list.elements();
                while (ecoef.hasMoreElements()) {
                    coef = (Coef_Elt) ecoef.nextElement();
                    cfpool.freeCoef(coef);
                }
                g1.coefs_list.clear();
                id = (Pkt_ID) varList.elementAt(i);
                coef = cfpool.getCoefElt(A[i][i], id.index_);
                coef.setnodeID_(id.getID());
                g1.coefs_list.put(coef.key(), coef);
                for (j = i + 1; j < N; j++) {
                    if (A[i][j] != 0) {
                        Solved = false;
                        id = (Pkt_ID) varList.elementAt(j);
                        coef = cfpool.getCoefElt(A[i][j], id.index_);
                        coef.setnodeID_(id.getID());
                        g1.coefs_list.put(coef.key(), coef);
                    }
                }
                if (Solved) {
                    if (g1.Buf[1] != 32) {
                        logger.error("OHHHHHHHHHHH ERRRRRRRRORRRR !!");
                    }
                    for (k = i - 1; k >= 0; k--) {
                        upPivot = A[k][i];
                        if (upPivot != 0) {
                            for (l = k + 1; l < N; l++) {
                                A[k][l] = GF.minus(A[k][l], GF.product(upPivot, A[i][l]));
                            }
                            g2 = (NCFBdatagram) decodingBuf.elementAt(k);
                            for (l = 0; l < Math.max(g1.dataLength, g2.dataLength); l++) {
                                g2.Buf[l] = int2byte(GF.minus(byte2int(g2.Buf[l]), GF.product(upPivot, byte2int(g1.Buf[l]))));
                            }
                        }
                    }
                    decodedBuf.add(g1);
                    if (g1.coefs_list.size() != 1) {
                        System.out.println("Error in decoded Packet !");
                    }
                    ecoef = g1.coefs_list.elements();
                    coef = (Coef_Elt) ecoef.nextElement();
                    decodedList.add(coef.getID());
                    Message msg = mp.createMessage(g1.Buf, (short) g1.Buf.length);
                    logger.info("NID: " + myNodeID + " TIME: " + System.currentTimeMillis() % 1000000 + " TO APPLI: " + msg);
                    super.handleMessage(msg);
                    Permut_line(A, M - 1, i);
                    Permut_col(A, N - 1, i, M);
                    varList.remove(N - 1);
                    decodingBuf.remove(M - 1);
                    M--;
                    N--;
                }
            }
        }
        if (M > N) {
            logger.fatal("Problem !");
        }
    }

    public synchronized int GausElim(int M, int N) throws GaloisException, Exception {
        NCFBdatagram g, g1, g2;
        int k, i, j, n;
        int pivot;
        rank = 0;
        for (k = 0; k < M; k++) {
            boolean SWAP = false;
            while (!SWAP) {
                if (A[k][k] == 0) {
                    for (n = k + 1; n < N; n++) {
                        if (A[k][n] != 0) {
                            Permut_col(A, k, n, M);
                            SWAP = true;
                            break;
                        }
                    }
                    if (!SWAP) {
                        if (k == (M - 1)) {
                            SWAP = true;
                            M = M - 1;
                            break;
                        } else {
                            M = M - 1;
                            Permut_line(A, M, k);
                        }
                    }
                }
                if (k < M) {
                    SWAP = true;
                    rank++;
                    pivot = A[k][k];
                    if (pivot != 1) {
                        g = (NCFBdatagram) decodingBuf.elementAt(k);
                        for (i = k; i < N; i++) {
                            A[k][i] = GF.divide(A[k][i], pivot);
                        }
                        g.product(GF.divide(1, pivot));
                    }
                    for (i = k + 1; i < M; i++) {
                        if (A[i][k] != 0) {
                            int p = A[i][k];
                            for (j = k; j < N; j++) {
                                A[i][j] = GF.minus(A[i][j], GF.product(p, A[k][j]));
                            }
                            g1 = (NCFBdatagram) decodingBuf.elementAt(i);
                            g2 = ncfbpool.clone((NCFBdatagram) decodingBuf.elementAt(k));
                            g1.minus(g2.product(p), cfpool);
                        }
                    }
                }
            }
        }
        extractSolved(M, N);
        return M;
    }

    public void decode(NCFBdatagram g) {
        int tmpNumCoef = g.coefs_list.size();
        try {
            g = reduce(g);
        } catch (Exception ev) {
            logger.error("NetCode_Module: insert: reduce " + ev);
        }
        if (g.coefs_list.size() == 0) {
            if (g.dataLength > 0) {
                if (simulMode) {
                    logger.info("At time: " + System.currentTimeMillis() + " NODE ID: " + myNodeID + " INDEX: " + g.getIndex() + " #COEF: " + tmpNumCoef + "ACT: Useless packet!!!");
                } else {
                    logger.info("At time: " + System.currentTimeMillis() + " NODE ID: " + myNodeID + " INDEX: " + g.getIndex() + " #COEF: " + tmpNumCoef + "ACT: Useless packet!!!");
                }
                System.out.println("At time: " + System.currentTimeMillis() + " NODE ID: " + myNodeID + " INDEX: " + g.getIndex() + " #COEF:" + tmpNumCoef + "ACT: Useless packet!!!");
            }
            return;
        }
        decodingBuf.insertElementAt(ncfbpool.clone(g), 0);
        try {
            if (update_varList(g)) {
                try {
                    int M = decodingBuf.size();
                    int N = varList.size();
                    if (M > N) {
                        logger.fatal("Problem !");
                    }
                    genA();
                    logger.info("NID: " + myNodeID + " TIME: " + System.currentTimeMillis() % 1000000 + " STATUS: B " + M + " " + N);
                    M = GausElim(M, N);
                    if (M > N) {
                        logger.error("Problem !");
                    }
                    logger.info("NID: " + myNodeID + " TIME: " + System.currentTimeMillis() % 1000000 + " STATUS: A " + decodingBuf.size() + " " + varList.size());
                } catch (Exception e1) {
                    logger.error("NetCode_Module: decode : GausElim" + e1);
                }
            } else {
                rejPkt++;
            }
        } catch (Exception e) {
            System.err.println("NetCode_Module: insert: update_map_list " + e);
        }
    }

    public synchronized NCFBdatagram reduce(NCFBdatagram g) throws GaloisException, Exception {
        NCFBdatagram g1, g2;
        Coef_Elt coef;
        int index;
        Enumeration<Coef_Elt> eg = g.coefs_list.elements();
        while (eg.hasMoreElements()) {
            coef = (Coef_Elt) eg.nextElement();
            index = decodedList.indexOf(coef.getID());
            if (index != -1) {
                g1 = (NCFBdatagram) decodedBuf.elementAt(index);
                g2 = ncfbpool.clone(g1);
                g.minus(g2.product(coef.getCoef()), cfpool);
                ncfbpool.freeNCFBdatagram(g2);
            }
        }
        return g;
    }

    public synchronized void genA() throws GaloisException, Exception {
        NCFBdatagram g;
        Pkt_ID id;
        Coef_Elt coef;
        int N = varList.size();
        int M = decodingBuf.size();
        int index = 0;
        if (M > N) {
            logger.error("Problem !");
        }
        A = new int[M][N];
        int i = 0;
        Enumeration<NCdatagram> eg = decodingBuf.elements();
        while (eg.hasMoreElements()) {
            g = (NCFBdatagram) eg.nextElement();
            Enumeration<Coef_Elt> ecoef = g.coefs_list.elements();
            while (ecoef.hasMoreElements()) {
                coef = (Coef_Elt) ecoef.nextElement();
                id = coef.getID();
                index = varList.indexOf(id);
                if (index == -1) {
                    System.out.println("ERROR in genA");
                }
                A[i][index] = coef.getCoef();
            }
            i++;
        }
    }

    public synchronized boolean update_varList(NCFBdatagram g) throws Exception {
        Coef_Elt coef;
        int newvariables = 0;
        Enumeration<Coef_Elt> eg = g.coefs_list.elements();
        while (eg.hasMoreElements()) {
            coef = (Coef_Elt) eg.nextElement();
            Pkt_ID id = coef.getID();
            if (!varList.contains(id)) {
                newvariables++;
            }
        }
        if (varList.size() + newvariables > capacity) {
            return false;
        } else {
            eg = g.coefs_list.elements();
            while (eg.hasMoreElements()) {
                coef = (Coef_Elt) eg.nextElement();
                Pkt_ID id = coef.getID();
                if (!varList.contains(id)) {
                    varList.add(id);
                }
            }
        }
        return true;
    }

    boolean checkBloomFilter(BloomFilter bf, Vector v) {
        for (int i = 0; i < v.size(); i++) {
            if (!bf.membershipTest(new Key(v.elementAt(i).toString().getBytes()))) {
                return false;
            }
        }
        return true;
    }
}
