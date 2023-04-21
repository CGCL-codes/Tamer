package testers;

import static p.MapUtils.InterceptVector;
import java.awt.Point;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import n.BasicNetworkInterface;
import p.divline_t;
import p.intercept_t;
import p.mobj_t;
import rr.line_t;
import rr.sector_t;
import awt.OldAWTDoom;
import b.BotGame;
import b.Reachable;
import b.SearchNode_t;
import b.bot_t;
import b.BotGame.ObjKind;
import b.BotGame.ReachableGroup;
import b.BotGame.SeenNode;
import b.BotPaths.BotPath;
import b.SearchNode_t.BlockingSector;
import defines.card_t;
import doom.DoomContext;
import doom.DoomMain;
import doom.doomdata_t;
import doom.player_t;
import doom.think_t;
import doom.thinker_t;
import doom.ticcmd_t;

public class BotTester {

    static DoomMain D4, D2, D1;

    static Object synchronizer = new Object();

    public static class NetInterfaceTester extends BasicNetworkInterface {

        DoomContext DC;

        public NetInterfaceTester(DoomContext DC) {
            super(DC);
        }

        private doomdata_t sendData = new doomdata_t();

        private doomdata_t recvData = new doomdata_t();

        @Override
        public void sendSocketPacket(DatagramSocket ds, DatagramPacket dp) throws IOException {
            DoomMain receiver;
            if (DM.consoleplayer == 0) {
                receiver = D2;
            } else {
                receiver = D4;
            }
            NetInterfaceTester N = (NetInterfaceTester) receiver.DNI;
            N.addReceivedPacket(dp);
            sendData.unpack(dp.getData());
        }

        LinkedList<DatagramPacket> al = new LinkedList<DatagramPacket>();

        public void addReceivedPacket(DatagramPacket dp) {
            synchronized (synchronizer) {
                al.addLast(dp);
            }
        }

        @Override
        public void socketGetPacket(DatagramSocket ds, DatagramPacket dp) throws IOException {
            synchronized (synchronizer) {
                if (al.size() < 1) throw new SocketTimeoutException();
                DatagramPacket pop = al.removeFirst();
                dp.setData(pop.getData());
                dp.setLength(pop.getLength());
                dp.setSocketAddress(pop.getSocketAddress());
                recvData.unpack(dp.getData());
            }
        }

        public String dataToStr(doomdata_t dt) {
            StringBuffer sb = new StringBuffer();
            sb.append("STIC: " + dt.starttic + " NTIC: " + dt.numtics + " CMDS:\r\n");
            for (int i = 0; i < dt.numtics; i++) {
                ticcmd_t tc = dt.cmds[i];
                sb.append("    FMOVE: " + tc.forwardmove + " CONS: " + tc.consistancy + "\r\n");
            }
            return sb.toString();
        }
    }

    public static class DoomMainTester extends DoomMain {

        public player_t playerBot;

        public DoomMainTester(String[] args) {
            this.Init();
            this.VI = new OldAWTDoom(this, null, null);
            this.DNI = new NetInterfaceTester(this);
            this.myargv = args;
            this.myargc = this.myargv.length;
            this.Start();
            this.bgame.BN.B_InitNodes();
            playerBot = this.players[0];
            playerBot.bot = new bot_t(this.bgame, playerBot);
            playerBot.bot.walkReachable = new Reachable.WalkReachable(bgame, playerBot);
        }

        @Override
        public void DoomLoop() {
            System.out.println("Overriding DoomLoop");
        }
    }

    public static void testE1L2() {
        DoomMainTester DM = new DoomMainTester(new String[] { "", "-warp", "12" });
        line_t unlockLine = findLine(DM, new int[] { 489, -1 });
        sector_t sec = DM.bgame.BSL.sectorsTaggedBy(unlockLine).get(0);
        sec.ceilingheight = 20709376;
        mobj_t mo = findThing(DM, -2097152, -56623104);
        DM.playerBot.mo.x = 100962571;
        DM.playerBot.mo.y = -19975541;
        DM.playerBot.cards[card_t.it_redcard.ordinal()] = true;
        DM.bgame.FindReachableNodes(DM.playerBot);
        SearchNode_t srcNode = DM.bgame.botNodeArray[135][27];
        SearchNode_t destNode = DM.bgame.botNodeArray[58][44];
        ReachableGroup contained = (DM.playerBot.bot.botPaths.nodesContained(srcNode, destNode));
        BotPath path = DM.playerBot.bot.botPaths.B_FindPath(srcNode, destNode, DM.playerBot.bot.walkReachable, DM.playerBot.bot.walkReachable, new HashSet<BlockingSector>(), contained);
        assrt(contained != null && path != null);
        SeenNode SN = DM.bgame.new SeenNode(DM.playerBot, mo);
        SN.setKind(ObjKind.item);
        SN.findPath();
        DM.playerBot.bot.botPaths.B_FindPath(srcNode, destNode, DM.playerBot.bot.walkReachable, DM.playerBot.bot.walkReachable, new HashSet<BlockingSector>(), contained);
        ArrayList<SearchNode_t> srcNodes = DM.bgame.BN.findNodesFromPoint(DM.playerBot, new Point(DM.playerBot.mo.x, DM.playerBot.mo.y), DM.playerBot.bot.walkReachable);
        ArrayList<SearchNode_t> destNodes = SN.getNodes();
        assrt((SN.path != null) == (contained != null));
        System.out.println("test");
    }

    public static void testDoomI() {
        testE1L2();
    }

    public static void main(String[] args) {
        testDoomI();
        D1 = new DoomMainTester(new String[] { "", "-warp", "1" });
        D2 = new DoomMainTester(new String[] { "", "-warp", "2" });
        D4 = new DoomMainTester(new String[] { "", "-warp", "4" });
        int[] sh = BotGame.shiftDxDy(new Point(25, 25), new Point(50, 50), 5);
        assrt(sh[0] == (int) (5 / Math.sqrt(2)));
        assrt(sh[1] == (int) (-5 / Math.sqrt(2)));
    }

    public static Point nodeToPoint(DoomMain DM, SearchNode_t node) {
        return new Point(DM.bgame.BN.posX2x(node.x), DM.bgame.BN.posY2y(node.y));
    }

    public static sector_t findSector(DoomMain DM, int id) {
        for (sector_t sec : DM.LL.sectors) {
            if (sec.id == id) {
                return sec;
            }
        }
        return null;
    }

    public static line_t findLine(DoomMain DM, int[] sidenum) {
        for (line_t line : DM.LL.lines) {
            if (line.sidenum.length == 2 && sidenum[0] == line.sidenum[0] && sidenum[1] == line.sidenum[1]) {
                return line;
            }
        }
        return null;
    }

    public static mobj_t findThing(DoomMain DM, int x, int y) {
        thinker_t currentthinker = DM.P.thinkercap.next;
        while (currentthinker != DM.P.thinkercap) {
            if (currentthinker.function == think_t.P_MobjThinker) {
                mobj_t mo = (mobj_t) currentthinker;
                if (mo.x == x && mo.y == y) return mo;
            }
            currentthinker = currentthinker.next;
        }
        return null;
    }

    public static void traverseTester(DoomMain DM) {
        sector_t secLava = findSector(DM, 24);
        line_t lineLava = findLine(DM, new int[] { 111, 112 });
        Point p1 = new Point((lineLava.v1x + lineLava.v2x) / 2, lineLava.v1y + 10);
        Point p2 = new Point((lineLava.v1x + lineLava.v2x) / 2, lineLava.v1y - 10);
        Point p3 = new Point((lineLava.v1x + lineLava.v2x) / 2, lineLava.v1y);
        D4.bgame.pointOnLine(p3, lineLava);
        divline_t trac = new divline_t();
        trac.x = p1.x;
        trac.y = p1.y;
        trac.dx = p2.x - p1.x;
        trac.dy = p2.y - p1.y;
        divline_t trac2 = new divline_t();
        trac2.x = p2.x;
        trac2.y = p2.y;
        trac2.dx = p1.x - p2.x;
        trac2.dy = p1.y - p2.y;
        divline_t dl1 = new divline_t();
        dl1.MakeDivline(lineLava);
        InterceptVector(trac, dl1);
        InterceptVector(trac2, dl1);
        LinkedList<intercept_t> q = D4.bgame.QueuePathTraverse(p1, p2);
        assrt(q.size() == 1 && q.get(0).line == lineLava);
        LinkedList<intercept_t> q2 = D4.bgame.CorrectedQueuePathTraverse(p1, p2);
        assrt(q2.size() == 1 && q2.get(0).line == lineLava);
        LinkedList<intercept_t> q3 = D4.bgame.QueuePathTraverse(p1, p3);
        assrt(q3.size() == 1 && q3.get(0).line == lineLava);
        LinkedList<intercept_t> q4 = D4.bgame.CorrectedQueuePathTraverse(p1, p3);
        assrt(q4.size() == 1 && q4.get(0).line == lineLava);
        LinkedList<intercept_t> q5 = D4.bgame.QueuePathTraverse(p3, p1);
        assrt(q5.size() == 1 && q5.get(0).line == lineLava);
        LinkedList<intercept_t> q6 = D4.bgame.CorrectedQueuePathTraverse(p3, p1);
        assrt(q6.size() == 0);
        LinkedList<intercept_t> q7 = D4.bgame.QueuePathTraverse(p2, p1);
        assrt(q7.size() == 1 && q7.get(0).line == lineLava);
        LinkedList<intercept_t> q8 = D4.bgame.CorrectedQueuePathTraverse(p2, p1);
        assrt(q8.size() == 1 && q8.get(0).line == lineLava);
        LinkedList<intercept_t> q9 = D4.bgame.QueuePathTraverse(p3, p2);
        assrt(q9.size() == 0);
        LinkedList<intercept_t> q10 = D4.bgame.CorrectedQueuePathTraverse(p3, p2);
        assrt(q10.size() == 0);
        LinkedList<intercept_t> q11 = D4.bgame.QueuePathTraverse(p2, p3);
        assrt(q11.size() == 0);
        LinkedList<intercept_t> q12 = D4.bgame.CorrectedQueuePathTraverse(p2, p3);
        assrt(q12.size() == 1 && q12.get(0).line == lineLava);
        LinkedList<BlockingSector> bs1 = D4.bgame.TraversedSecLines(p1, p2);
        assrt(bs1.size() == 1);
        LinkedList<BlockingSector> bs2 = D4.bgame.TraversedSecLines(p2, p1);
        assrt(bs2.size() == 1);
        assrt(bs1.get(0).srcSect == bs2.get(0).destSect);
        assrt(bs2.get(0).srcSect == bs1.get(0).destSect);
        LinkedList<BlockingSector> bs3 = D4.bgame.TraversedSecLines(p1, p3);
        assrt(bs3.size() == 1);
        LinkedList<BlockingSector> bs4 = D4.bgame.TraversedSecLines(p3, p1);
        assrt(bs4.size() == 0);
        assrt(bs1.get(0).srcSect == bs3.get(0).srcSect);
        assrt(bs1.get(0).destSect == bs3.get(0).destSect);
    }

    static void assrt(boolean b) {
        if (!b) {
            System.out.println("Assert failed");
        }
    }
}
