package edu.jas.gb;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Collections;
import java.util.concurrent.Semaphore;
import org.apache.log4j.Logger;
import edu.jas.poly.ExpVector;
import edu.jas.poly.GenPolynomial;
import edu.jas.structure.RingElem;
import edu.jas.util.ChannelFactory;
import edu.jas.util.DistHashTable;
import edu.jas.util.DistHashTableServer;
import edu.jas.util.SocketChannel;
import edu.jas.util.Terminator;
import edu.jas.util.ThreadPool;

/**
 * Groebner Base distributed algorithm. Implements a distributed memory parallel
 * version of Groebner bases. Using pairlist class, distributed tasks do
 * reduction. Makes some effort to produce the same sequence of critical pairs
 * as in the sequential version. However already reduced pairs are not rereduced
 * if new polynomials appear.
 * @param <C> coefficient type
 * @author Heinz Kredel
 */
public class GroebnerBaseSeqPairDistributed<C extends RingElem<C>> extends GroebnerBaseAbstract<C> {

    private static final Logger logger = Logger.getLogger(GroebnerBaseSeqPairDistributed.class);

    /**
     * Number of threads to use.
     */
    protected final int threads;

    /**
     * Default number of threads.
     */
    protected static final int DEFAULT_THREADS = 2;

    /**
     * Pool of threads to use.
     */
    protected final ThreadPool pool;

    /**
     * Default server port.
     */
    protected static final int DEFAULT_PORT = 4711;

    /**
     * Server port to use.
     */
    protected final int port;

    /**
     * Constructor.
     */
    public GroebnerBaseSeqPairDistributed() {
        this(DEFAULT_THREADS, DEFAULT_PORT);
    }

    /**
     * Constructor.
     * @param threads number of threads to use.
     */
    public GroebnerBaseSeqPairDistributed(int threads) {
        this(threads, new ThreadPool(threads), DEFAULT_PORT);
    }

    /**
     * Constructor.
     * @param threads number of threads to use.
     * @param red parallelism aware reduction engine
     */
    public GroebnerBaseSeqPairDistributed(int threads, Reduction<C> red) {
        this(threads, new ThreadPool(threads), DEFAULT_PORT, red);
    }

    /**
     * Constructor.
     * @param threads number of threads to use.
     * @param port server port to use.
     * @param red parallelism aware reduction engine
     */
    public GroebnerBaseSeqPairDistributed(int threads, int port, Reduction<C> red) {
        this(threads, new ThreadPool(threads), port, red);
    }

    /**
     * Constructor.
     * @param threads number of threads to use.
     * @param port server port to use.
     */
    public GroebnerBaseSeqPairDistributed(int threads, int port) {
        this(threads, new ThreadPool(threads), port);
    }

    /**
     * Constructor.
     * @param threads number of threads to use.
     * @param pool ThreadPool to use.
     * @param port server port to use.
     */
    public GroebnerBaseSeqPairDistributed(int threads, ThreadPool pool, int port) {
        this(threads, pool, port, new ReductionPar<C>());
    }

    /**
     * Constructor.
     * @param threads number of threads to use.
     * @param pool ThreadPool to use.
     * @param port server port to use.
     * @param red parallelism aware reduction engine
     */
    public GroebnerBaseSeqPairDistributed(int threads, ThreadPool pool, int port, Reduction<C> red) {
        super(red);
        if (!(red instanceof ReductionPar)) {
            logger.warn("parallel GB should use parallel aware reduction");
        }
        if (threads < 1) {
            threads = 1;
        }
        this.threads = threads;
        this.pool = pool;
        this.port = port;
    }

    /**
     * Cleanup and terminate ThreadPool.
     */
    @Override
    public void terminate() {
        if (pool == null) {
            return;
        }
        pool.terminate();
    }

    /**
     * Distributed Groebner base. Slaves maintain pairlist.
     * @param modv number of module variables.
     * @param F polynomial list.
     * @return GB(F) a Groebner base of F or null, if a IOException occurs.
     */
    public List<GenPolynomial<C>> GB(int modv, List<GenPolynomial<C>> F) {
        final int DL_PORT = port + 100;
        ChannelFactory cf = new ChannelFactory(port);
        cf.init();
        DistHashTableServer<Integer> dls = new DistHashTableServer<Integer>(DL_PORT);
        dls.init();
        logger.debug("dist-list server running");
        GenPolynomial<C> p;
        List<GenPolynomial<C>> G = new ArrayList<GenPolynomial<C>>();
        CriticalPairList<C> pairlist = null;
        boolean oneInGB = false;
        int l = F.size();
        int unused;
        ListIterator<GenPolynomial<C>> it = F.listIterator();
        while (it.hasNext()) {
            p = it.next();
            if (p.length() > 0) {
                p = p.monic();
                if (p.isONE()) {
                    oneInGB = true;
                    G.clear();
                    G.add(p);
                }
                if (!oneInGB) {
                    G.add(p);
                }
                if (pairlist == null) {
                    pairlist = new CriticalPairList<C>(modv, p.ring);
                }
                if (p.isONE()) {
                    unused = pairlist.putOne();
                } else {
                    unused = pairlist.put(p);
                }
            } else {
                l--;
            }
        }
        if (l <= 1) {
        }
        logger.debug("looking for clients");
        DistHashTable<Integer, GenPolynomial<C>> theList = new DistHashTable<Integer, GenPolynomial<C>>("localhost", DL_PORT);
        List<GenPolynomial<C>> al = pairlist.getList();
        for (int i = 0; i < al.size(); i++) {
            GenPolynomial<C> nn = theList.put(new Integer(i), al.get(i));
            if (nn != null) {
                logger.info("double polynomials " + i + ", nn = " + nn + ", al(i) = " + al.get(i));
            }
        }
        Terminator fin = new Terminator(threads);
        ReducerServerSeqPair<C> R;
        for (int i = 0; i < threads; i++) {
            R = new ReducerServerSeqPair<C>(fin, cf, theList, G, pairlist);
            pool.addJob(R);
        }
        logger.debug("main loop waiting");
        fin.waitDone();
        int ps = theList.size();
        G = pairlist.getList();
        if (ps != G.size()) {
            logger.info("#distributed list = " + theList.size() + " #pairlist list = " + G.size());
        }
        long time = System.currentTimeMillis();
        List<GenPolynomial<C>> Gp;
        Gp = minimalGB(G);
        time = System.currentTimeMillis() - time;
        logger.info("parallel gbmi = " + time);
        G = Gp;
        cf.terminate();
        logger.info("theList.terminate()");
        theList.terminate();
        logger.info("dls.terminate()");
        dls.terminate();
        logger.info("" + pairlist);
        return G;
    }

    /**
     * GB distributed client.
     * @param host the server runns on.
     * @throws IOException
     */
    public void clientPart(String host) throws IOException {
        ChannelFactory cf = new ChannelFactory(port + 10);
        cf.init();
        SocketChannel pairChannel = cf.getChannel(host, port);
        final int DL_PORT = port + 100;
        DistHashTable<Integer, GenPolynomial<C>> theList = new DistHashTable<Integer, GenPolynomial<C>>(host, DL_PORT);
        ReducerClientSeqPair<C> R = new ReducerClientSeqPair<C>(pairChannel, theList);
        R.run();
        pairChannel.close();
        theList.terminate();
        cf.terminate();
        return;
    }

    /**
     * Minimal ordered groebner basis.
     * @param Fp a Groebner base.
     * @return a reduced Groebner base of Fp.
     */
    @Override
    public List<GenPolynomial<C>> minimalGB(List<GenPolynomial<C>> Fp) {
        GenPolynomial<C> a;
        ArrayList<GenPolynomial<C>> G;
        G = new ArrayList<GenPolynomial<C>>(Fp.size());
        ListIterator<GenPolynomial<C>> it = Fp.listIterator();
        while (it.hasNext()) {
            a = it.next();
            if (a.length() != 0) {
                G.add(a);
            }
        }
        if (G.size() <= 1) {
            return G;
        }
        ExpVector e;
        ExpVector f;
        GenPolynomial<C> p;
        ArrayList<GenPolynomial<C>> F;
        F = new ArrayList<GenPolynomial<C>>(G.size());
        boolean mt;
        while (G.size() > 0) {
            a = G.remove(0);
            e = a.leadingExpVector();
            it = G.listIterator();
            mt = false;
            while (it.hasNext() && !mt) {
                p = it.next();
                f = p.leadingExpVector();
                mt = e.multipleOf(f);
            }
            it = F.listIterator();
            while (it.hasNext() && !mt) {
                p = it.next();
                f = p.leadingExpVector();
                mt = e.multipleOf(f);
            }
            if (!mt) {
                F.add(a);
            } else {
            }
        }
        G = F;
        if (G.size() <= 1) {
            return G;
        }
        Collections.reverse(G);
        MiReducerServerSeqPair<C>[] mirs = (MiReducerServerSeqPair<C>[]) new MiReducerServerSeqPair[G.size()];
        int i = 0;
        F = new ArrayList<GenPolynomial<C>>(G.size());
        while (G.size() > 0) {
            a = G.remove(0);
            List<GenPolynomial<C>> R = new ArrayList<GenPolynomial<C>>(G.size() + F.size());
            R.addAll(G);
            R.addAll(F);
            mirs[i] = new MiReducerServerSeqPair<C>(R, a);
            pool.addJob(mirs[i]);
            i++;
            F.add(a);
        }
        G = F;
        F = new ArrayList<GenPolynomial<C>>(G.size());
        for (i = 0; i < mirs.length; i++) {
            a = mirs[i].getNF();
            F.add(a);
        }
        return F;
    }
}

/**
 * Distributed server reducing worker threads.
 * @param <C> coefficient type
 */
class ReducerServerSeqPair<C extends RingElem<C>> implements Runnable {

    private final Terminator pool;

    private final ChannelFactory cf;

    private SocketChannel pairChannel;

    private final DistHashTable<Integer, GenPolynomial<C>> theList;

    private final CriticalPairList<C> pairlist;

    private static final Logger logger = Logger.getLogger(ReducerServerSeqPair.class);

    ReducerServerSeqPair(Terminator fin, ChannelFactory cf, DistHashTable<Integer, GenPolynomial<C>> dl, List<GenPolynomial<C>> G, CriticalPairList<C> L) {
        pool = fin;
        this.cf = cf;
        theList = dl;
        pairlist = L;
    }

    public void run() {
        logger.debug("reducer server running");
        try {
            pairChannel = cf.getChannel();
        } catch (InterruptedException e) {
            logger.debug("get pair channel interrupted");
            e.printStackTrace();
            return;
        }
        if (logger.isDebugEnabled()) {
            logger.debug("pairChannel = " + pairChannel);
        }
        CriticalPair<C> pair;
        GenPolynomial<C> H = null;
        boolean set = false;
        boolean goon = true;
        int polIndex = -1;
        int red = 0;
        int sleeps = 0;
        while (goon) {
            logger.debug("receive request");
            Object req = null;
            try {
                req = pairChannel.receive();
            } catch (IOException e) {
                goon = false;
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                goon = false;
                e.printStackTrace();
            }
            if (req == null) {
                goon = false;
                break;
            }
            if (!(req instanceof GBSPTransportMessReq)) {
                goon = false;
                break;
            }
            if (logger.isDebugEnabled()) {
                logger.debug("find pair");
                logger.debug("pool.hasJobs() " + pool.hasJobs() + " pairlist.hasNext() " + pairlist.hasNext());
            }
            while (!pairlist.hasNext()) {
                pairlist.update();
                if (!set) {
                    pool.beIdle();
                    set = true;
                }
                if (!pool.hasJobs() && !pairlist.hasNext()) {
                    goon = false;
                    break;
                }
                try {
                    sleeps++;
                    if (sleeps % 10 == 0) {
                        logger.info(" reducer is sleeping");
                    }
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    goon = false;
                    break;
                }
            }
            if (!pairlist.hasNext() && !pool.hasJobs()) {
                goon = false;
                break;
            }
            if (set) {
                set = false;
                pool.notIdle();
            }
            pair = pairlist.getNext();
            if (logger.isDebugEnabled()) {
                logger.debug("send pair = " + pair);
                logger.info("theList keys " + theList.keySet());
            }
            if (logger.isDebugEnabled()) {
                logger.info("inWork " + pairlist.inWork());
            }
            GBSPTransportMess msg = null;
            if (pair != null) {
                msg = new GBSPTransportMessPairIndex(pair);
            } else {
                msg = new GBSPTransportMess();
            }
            try {
                pairChannel.send(msg);
            } catch (IOException e) {
                e.printStackTrace();
                goon = false;
                break;
            }
            pairlist.update();
            Object rh = null;
            try {
                rh = pairChannel.receive();
            } catch (IOException e) {
                e.printStackTrace();
                goon = false;
                break;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                goon = false;
                break;
            }
            if (logger.isDebugEnabled()) {
                logger.info("received H polynomial rh = " + rh);
            }
            if (rh == null) {
                if (pair != null) {
                    polIndex = pairlist.record(pair, null);
                }
                pairlist.update();
            } else if (rh instanceof GBSPTransportMessPoly) {
                red++;
                H = ((GBSPTransportMessPoly<C>) rh).pol;
                if (H == null) {
                    if (pair != null) {
                        polIndex = pairlist.record(pair, H);
                    }
                    pairlist.update();
                } else {
                    if (H.isZERO()) {
                        polIndex = pairlist.record(pair, H);
                    } else {
                        if (H.isONE()) {
                            pairlist.putOne();
                            theList.put(new Integer(0), H);
                            goon = false;
                        } else {
                            polIndex = pairlist.record(pair, H);
                            theList.put(new Integer(polIndex), H);
                        }
                    }
                }
            } else {
                if (pair != null) {
                    polIndex = pairlist.record(pair, null);
                }
                if (logger.isDebugEnabled()) {
                    logger.debug("invalid message " + rh);
                }
            }
        }
        logger.info("terminated, done " + red + " reductions");
        logger.debug("send end");
        try {
            pairChannel.send(new GBSPTransportMessEnd());
        } catch (IOException e) {
            if (logger.isDebugEnabled()) {
                e.printStackTrace();
            }
        }
        pool.beIdle();
        pairChannel.close();
    }
}

/**
 * Distributed GB transport message.
 */
class GBSPTransportMess implements Serializable {

    /**
     * toString.
     */
    @Override
    public String toString() {
        return "" + this.getClass().getName();
    }
}

/**
 * Distributed GB transport message for requests.
 */
class GBSPTransportMessReq extends GBSPTransportMess {

    public GBSPTransportMessReq() {
    }
}

/**
 * Distributed GB transport message for termination.
 */
class GBSPTransportMessEnd extends GBSPTransportMess {

    public GBSPTransportMessEnd() {
    }
}

/**
 * Distributed GB transport message for polynomial.
 */
class GBSPTransportMessPoly<C extends RingElem<C>> extends GBSPTransportMess {

    /**
     * The polynomial for transport.
     */
    public final GenPolynomial<C> pol;

    /**
     * GBSPTransportMessPoly.
     * @param p polynomial to transfered.
     */
    public GBSPTransportMessPoly(GenPolynomial<C> p) {
        this.pol = p;
    }

    /**
     * toString.
     */
    @Override
    public String toString() {
        return super.toString() + "( " + pol + " )";
    }
}

/**
 * Distributed GB transport message for pairs.
 */
class GBSPTransportMessPair<C extends RingElem<C>> extends GBSPTransportMess {

    public final CriticalPair<C> pair;

    /**
     * GBSPTransportMessPair.
     * @param p pair for transfer.
     */
    public GBSPTransportMessPair(CriticalPair<C> p) {
        this.pair = p;
    }

    /**
     * toString.
     */
    @Override
    public String toString() {
        return super.toString() + "( " + pair + " )";
    }
}

/**
 * Distributed GB transport message for index pairs.
 */
class GBSPTransportMessPairIndex extends GBSPTransportMess {

    public final Integer i;

    public final Integer j;

    /**
     * GBSPTransportMessPairIndex.
     * @param p pair for transport.
     */
    public GBSPTransportMessPairIndex(CriticalPair p) {
        if (p == null) {
            throw new NullPointerException("pair may not be null");
        }
        this.i = new Integer(p.i);
        this.j = new Integer(p.j);
    }

    /**
     * GBSPTransportMessPairIndex.
     * @param i first index.
     * @param j second index.
     */
    public GBSPTransportMessPairIndex(int i, int j) {
        this.i = new Integer(i);
        this.j = new Integer(j);
    }

    /**
     * GBSPTransportMessPairIndex.
     * @param i first index.
     * @param j second index.
     */
    public GBSPTransportMessPairIndex(Integer i, Integer j) {
        this.i = i;
        this.j = j;
    }

    /**
     * toString.
     */
    @Override
    public String toString() {
        return super.toString() + "( " + i + "," + j + " )";
    }
}

/**
 * Distributed clients reducing worker threads.
 */
class ReducerClientSeqPair<C extends RingElem<C>> implements Runnable {

    private final SocketChannel pairChannel;

    private final DistHashTable<Integer, GenPolynomial<C>> theList;

    private final ReductionPar<C> red;

    private static final Logger logger = Logger.getLogger(ReducerClientSeqPair.class);

    ReducerClientSeqPair(SocketChannel pc, DistHashTable<Integer, GenPolynomial<C>> dl) {
        pairChannel = pc;
        theList = dl;
        red = new ReductionPar<C>();
    }

    public void run() {
        if (logger.isDebugEnabled()) {
            logger.debug("pairChannel = " + pairChannel + "reducer client running");
        }
        CriticalPair<C> pair = null;
        GenPolynomial<C> pi;
        GenPolynomial<C> pj;
        GenPolynomial<C> S;
        GenPolynomial<C> H = null;
        boolean goon = true;
        int reduction = 0;
        Integer pix;
        Integer pjx;
        while (goon) {
            Object req = new GBSPTransportMessReq();
            if (logger.isDebugEnabled()) {
                logger.debug("send request = " + req);
            }
            try {
                pairChannel.send(req);
            } catch (IOException e) {
                goon = false;
                e.printStackTrace();
                break;
            }
            if (logger.isDebugEnabled()) {
                logger.debug("receive pair, goon = " + goon);
            }
            Object pp = null;
            try {
                pp = pairChannel.receive();
            } catch (IOException e) {
                goon = false;
                if (logger.isDebugEnabled()) {
                    e.printStackTrace();
                }
                break;
            } catch (ClassNotFoundException e) {
                goon = false;
                e.printStackTrace();
            }
            if (logger.isDebugEnabled()) {
                logger.info("received pair = " + pp);
            }
            H = null;
            if (pp == null) {
                continue;
            }
            if (pp instanceof GBSPTransportMessEnd) {
                goon = false;
                continue;
            }
            if (pp instanceof GBSPTransportMessPair || pp instanceof GBSPTransportMessPairIndex) {
                pi = pj = null;
                if (pp instanceof GBSPTransportMessPair) {
                    pair = ((GBSPTransportMessPair<C>) pp).pair;
                    if (pair != null) {
                        pi = pair.pi;
                        pj = pair.pj;
                    }
                }
                if (pp instanceof GBSPTransportMessPairIndex) {
                    pix = ((GBSPTransportMessPairIndex) pp).i;
                    pjx = ((GBSPTransportMessPairIndex) pp).j;
                    pi = theList.getWait(pix);
                    pj = theList.getWait(pjx);
                }
                if (logger.isDebugEnabled()) {
                    logger.info("pi = " + pi.leadingExpVector() + ", pj = " + pj.leadingExpVector());
                }
                if (pi != null && pj != null) {
                    S = red.SPolynomial(pi, pj);
                    if (S.isZERO()) {
                        H = S;
                    } else {
                        if (logger.isDebugEnabled()) {
                            logger.debug("ht(S) = " + S.leadingExpVector());
                        }
                        H = red.normalform(theList, S);
                        reduction++;
                        if (H.isZERO()) {
                        } else {
                            H = H.monic();
                            if (logger.isDebugEnabled()) {
                                logger.info("ht(H) = " + H.leadingExpVector());
                            }
                        }
                    }
                }
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug("invalid message = " + pp);
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (logger.isDebugEnabled()) {
                logger.info("send H polynomial = " + H);
            }
            try {
                pairChannel.send(new GBSPTransportMessPoly<C>(H));
            } catch (IOException e) {
                goon = false;
                e.printStackTrace();
                break;
            }
        }
        logger.info("terminated, done " + reduction + " reductions");
        pairChannel.close();
    }
}

/**
 * Distributed server reducing worker threads for minimal GB Not jet distributed
 * but threaded.
 */
class MiReducerServerSeqPair<C extends RingElem<C>> implements Runnable {

    private final List<GenPolynomial<C>> G;

    private GenPolynomial<C> H;

    private final Semaphore done = new Semaphore(0);

    private final Reduction<C> red;

    private static final Logger logger = Logger.getLogger(MiReducerServerSeqPair.class);

    MiReducerServerSeqPair(List<GenPolynomial<C>> G, GenPolynomial<C> p) {
        this.G = G;
        H = p;
        red = new ReductionPar<C>();
    }

    /**
     * getNF. Blocks until the normal form is computed.
     * @return the computed normal form.
     */
    public GenPolynomial<C> getNF() {
        try {
            done.acquire();
        } catch (InterruptedException e) {
        }
        return H;
    }

    public void run() {
        if (logger.isDebugEnabled()) {
            logger.debug("ht(H) = " + H.leadingExpVector());
        }
        H = red.normalform(G, H);
        done.release();
        if (logger.isDebugEnabled()) {
            logger.debug("ht(H) = " + H.leadingExpVector());
        }
    }
}

/**
 * Distributed clients reducing worker threads for minimal GB. Not jet used.
 */
class MiReducerClientSeqPair<C extends RingElem<C>> implements Runnable {

    private final List<GenPolynomial<C>> G;

    private GenPolynomial<C> H;

    private final Reduction<C> red;

    private final Semaphore done = new Semaphore(0);

    private static final Logger logger = Logger.getLogger(MiReducerClientSeqPair.class);

    MiReducerClientSeqPair(List<GenPolynomial<C>> G, GenPolynomial<C> p) {
        this.G = G;
        H = p;
        red = new ReductionPar<C>();
    }

    /**
     * getNF. Blocks until the normal form is computed.
     * @return the computed normal form.
     */
    public GenPolynomial<C> getNF() {
        try {
            done.acquire();
        } catch (InterruptedException u) {
            Thread.currentThread().interrupt();
        }
        return H;
    }

    public void run() {
        if (logger.isDebugEnabled()) {
            logger.debug("ht(H) = " + H.leadingExpVector());
        }
        H = red.normalform(G, H);
        done.release();
        if (logger.isDebugEnabled()) {
            logger.debug("ht(H) = " + H.leadingExpVector());
        }
    }
}
