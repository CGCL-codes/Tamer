package com.jot.system.messages.p2p;

import static org.junit.Assert.assertTrue;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.junit.Test;
import com.jot.system.BucketMgr;
import com.jot.system.JotContext;
import com.jot.system.Mid;
import com.jot.system.WidgetMgrHelper;
import com.jot.system.WidgetServerBucket;
import com.jot.system.messages.AutoRpc;
import com.jot.system.nio.JotSocket;
import com.jot.system.pjson.Guid;
import com.jot.system.pjson.PJSONName;
import com.jot.system.pjson.PjsonFactory;
import com.jot.system.pjson.PjsonParseUtil;
import com.jot.system.pjson.PjsonWriteUtil;
import com.jot.system.utils.TwoWayMapping;
import com.jot.system.utils.TwoWayTreeMapping;

@PJSONName("SetWidgetBuckets")
public class SetWidgetBuckets extends AutoRpc {

    static Logger logger = Logger.getLogger(SetWidgetBuckets.class);

    public BucketMgr<WidgetServerBucket> bucketMgr;

    public SetWidgetBuckets(JotContext ggg, Guid dest) {
        super(ggg, dest);
    }

    public SetWidgetBuckets(JotSocket jport) {
        super(jport);
    }

    public SetWidgetBuckets() {
    }

    public void set() {
        if (rpc("set")) return;
        bucketMgr.checksum = WidgetMgrHelper.calcChecksum(bucketMgr);
        if (!bucketMgr.checksum.equals(g.widgetServers.getBucketMgr().checksum)) {
            g.widgetServers.setBucketMgr(bucketMgr);
            ;
            logger.info("have widget Mgr set " + g.peer.getName().toStringTrimmed() + " sum= " + g.widgetServers.getBucketMgr().checksum);
        }
    }

    public void writePjson(PjsonWriteUtil writer) {
        StringBuilder dest = new StringBuilder();
        dest.append("" + bucketMgr.bucketCount + ',');
        dest.append("{");
        WidgetMgrHelper.dump2JSON(bucketMgr, dest);
        dest.append('}');
        writer.insertPjsonLiteral(dest.toString().getBytes());
    }

    public void parse(PjsonParseUtil parser) throws Exception {
        g = (JotContext) parser.global;
        TwoWayMapping<Mid, Integer> server2buckets = new TwoWayTreeMapping<Mid, Integer>();
        int count = parser.readInt();
        bucketMgr = new BucketMgr<WidgetServerBucket>(count);
        parser.skipOpenCurly();
        while (parser.peek() != '}') {
            Mid port = (Mid) parser.parseObject(Mid.factory);
            if (g != null) {
                JotSocket ssss = g.getPeerPort(port.name);
                if (ssss != null) port = ssss.peer.id;
            }
            parser.skipColon();
            parser.skipOpenBracket();
            while (parser.peek() != ']') {
                int i = parser.readInt();
                server2buckets.add(port, new Integer(i));
            }
            parser.skipCloseBracket();
            parser.skipComma();
        }
        parser.skipCloseCurly();
        WidgetMgrHelper.makeFromTwoWay(bucketMgr, server2buckets);
    }

    public static class SetWidgetBucketsFactory extends PjsonFactory {

        public Object make(PjsonParseUtil parser) throws Exception {
            com.jot.system.messages.p2p.SetWidgetBuckets tmp = new com.jot.system.messages.p2p.SetWidgetBuckets();
            tmp.parse(parser);
            return tmp;
        }
    }

    public static PjsonFactory factory = new SetWidgetBucketsFactory();

    public void inittest() {
        if (bucketMgr != null) return;
        List<Mid> servers = new ArrayList<Mid>();
        for (int i = 0; i < 7; i++) {
            servers.add(new Mid(new Guid("news" + i), 12));
        }
        bucketMgr = WidgetMgrHelper.makeWidgetServerTable(servers, 8 * 8);
    }

    @Test
    public void serialize() throws Exception {
        inittest();
        PjsonWriteUtil w = new PjsonWriteUtil();
        w.writeGenericObject(this);
        String result = w.toString();
        System.out.println(result);
        PjsonParseUtil p = new PjsonParseUtil(result);
        Object rrr = p.parseGenericObject();
        PjsonWriteUtil w2 = new PjsonWriteUtil();
        w2.writeGenericObject(rrr);
        String r2 = w2.toString();
        System.out.println(r2);
        assertTrue(result.equals(r2));
    }

    @Test
    public void serialize2() throws Exception {
        inittest();
    }

    @Test
    public void serializeEmpty() throws Exception {
        List<Mid> servers = new ArrayList<Mid>();
        for (int i = 0; i < 0; i++) {
            servers.add(new Mid(new Guid("news" + i), 2));
        }
        bucketMgr = WidgetMgrHelper.makeWidgetServerTable(servers, 8 * 8);
        PjsonWriteUtil w = new PjsonWriteUtil();
        w.writeGenericObject(this);
        String result = w.toString();
        System.out.println(result);
        PjsonParseUtil p = new PjsonParseUtil(result);
        Object rrr = p.parseGenericObject();
        PjsonWriteUtil w2 = new PjsonWriteUtil();
        w2.writeGenericObject(rrr);
        String r2 = w2.toString();
        System.out.println(r2);
        assertTrue(result.equals(r2));
    }

    public static void main(String[] args) {
        SetWidgetBuckets ooo = new SetWidgetBuckets();
        try {
            ooo.serialize();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
