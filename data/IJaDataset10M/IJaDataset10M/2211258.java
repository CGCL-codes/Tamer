package com.entelience.test.test08provider;

import org.junit.*;
import static org.junit.Assert.*;
import com.entelience.test.OurDbTestCase;
import java.util.List;
import com.entelience.provider.net.DbIP;
import com.entelience.objects.net.NetIP;
import com.entelience.objects.net.NetIPRange;
import com.entelience.sql.Db;
import java.util.concurrent.Future;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.List;
import java.util.ArrayList;

/**
 * test class for DbIP
**/
public class test13DbIP extends OurDbTestCase {

    private static long salt = System.currentTimeMillis();

    private static String ip;

    private static String part1;

    private static String part2;

    private static Integer ipId;

    private static String rfcIp;

    @Test
    public void test00_clean_data() throws Exception {
        db.begin();
        db.executeSql("TRUNCATE  net.t_ip");
        db.commit();
    }

    @Test
    public void test01_create_ip() throws Exception {
        db.begin();
        String digit = Long.toString(salt);
        part1 = digit.substring(digit.length() - 2);
        if (part1.startsWith("0")) {
            part1 = part1.substring(2);
            if (part1.equals("")) part1 = "0";
        }
        part2 = digit.substring(digit.length() - 1);
        if (part2.startsWith("0")) part2 = part1;
        ip = "15." + part1 + ".0." + part2;
        ipId = DbIP.addNetIP(db, ip);
        assertNotNull(ipId);
        db.commit();
    }

    @Test
    public void test02_get_ip() throws Exception {
        db.enter();
        NetIP nip = DbIP.getNetIP(db, ip);
        assertNotNull(nip);
        assertEquals(nip.getIp(), ip);
        db.exit();
    }

    @Test
    public void test03_create_1918_ip() throws Exception {
        db.begin();
        rfcIp = "10." + part1 + ".0." + part2;
        int id = DbIP.addNetIP(db, rfcIp);
        db.commit();
    }

    @Test
    public void test04_get_rfc_1918_ip() throws Exception {
        db.enter();
        NetIP nip = DbIP.getNetIP(db, rfcIp);
        assertNotNull(nip);
        assertEquals(nip.getIp(), rfcIp);
        assertTrue("" + rfcIp, nip.isRfc1918());
        db.exit();
    }

    @Test(expected = Exception.class)
    public void test05_fail_add_ip() throws Exception {
        db.begin();
        DbIP.addNetIP(db, (String) null);
    }

    @Test
    public void test06_get_ip_by_id() throws Exception {
        db.enter();
        NetIP nip = DbIP.getNetIP(db, ipId);
        assertNotNull(nip);
        assertEquals(nip.getIp(), ip);
        db.exit();
    }

    @Test
    public void test07_get_ip_id() throws Exception {
        db.enter();
        Integer id = DbIP.getIpId(db, ip);
        assertNotNull(id);
        assertTrue(id.compareTo(ipId) == 0);
        db.exit();
    }

    @Test
    public void test08_list_rfc1918_ranges() throws Exception {
        db.enter();
        List<NetIPRange> l = DbIP.getRfc1918Ranges(db);
        assertEquals(3, l.size());
        db.exit();
    }

    @Test
    public void test09_add_again_ip() throws Exception {
        db.begin();
        assertEquals(ipId, DbIP.addNetIP(db, ip));
        db.commit();
    }

    class TestCallable implements Callable<Integer> {

        private Db db;

        public TestCallable(Db db) throws Exception {
            this.db = (Db) db.clone();
        }

        public Integer call() throws Exception {
            db.disableTx();
            db.begin();
            for (int i = 0; i < 10; ++i) {
                Integer id0 = DbIP.addNetIP(db, "10.15.1." + i);
                assertNotNull(DbIP.getNetIP(db, id0));
            }
            db.commit();
            return Integer.valueOf(1);
        }
    }

    @Test
    public void test10_multi_threads_add_netip() throws Exception {
        try {
            ExecutorService executor = Executors.newFixedThreadPool(4);
            CompletionService<Integer> ecs = new ExecutorCompletionService<Integer>(executor);
            List<Future<Integer>> list = new ArrayList<Future<Integer>>();
            for (int i = 0; i < 10; ++i) {
                Callable<Integer> testCallable = new TestCallable(db);
                list.add(ecs.submit(testCallable));
            }
            int n = 10;
            do {
                Integer i = ecs.take().get();
                --n;
            } while (n > 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
