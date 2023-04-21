package org.fjank.tests;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.util.jcache.Cache;
import javax.util.jcache.CacheAccessFactory;
import javax.util.jcache.CacheAttributes;
import javax.util.jcache.CacheException;
import javax.util.jcache.CacheException;

/**
 * Compares the performance of hashmap with FKache.
 * 
 * @author Frank Karlstr�m
 */
public class CompareWithHashMap {

    private NumberFormat countFormat = new DecimalFormat("###,###");

    private int testTime = 1000;

    private File file;

    private Map results;

    private Map jcache;

    private Map other;

    public CompareWithHashMap(File file) {
        this.file = file;
    }

    public static void main(String[] args) {
        if (args == null) {
            System.out.println("Missing required argument outputdir.");
            System.exit(0);
        }
        if (args.length == 0) {
            System.out.println("Missing required argument outputdir.");
            System.exit(0);
        }
        String dir = args[0];
        File file = new File(dir, "performance.xml");
        CompareWithHashMap comp = new CompareWithHashMap(file);
        comp.executeTest();
        comp.writeResults();
    }

    private void writeResults() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            StringBuffer res = new StringBuffer("<document>\n" + "<properties>\n" + "<title>Performance comparison with HashMap</title>\n" + "</properties>\n" + "<body>" + "<section name=\"Test harness\"><p>All tests will be successfull " + "50% of the time, the other 50% will not be successfull, since the" + "Maps is pre-initialized for each test with keys/values 0-500, and the tests are runned with" + "keys/values 0-1000." + "The keys are Strings, while the values are integers.</p></section>" + "<section name=\"Performance metrics\">\n" + "<p>This page presents the results of executing a performance test of FKache versus java.util.HashMap.\n");
            res.append("The tests was performed on " + DateFormat.getDateTimeInstance().format(new Date()) + ".\n");
            res.append("The tests was runned on " + System.getProperty("java.runtime.name") + " version " + System.getProperty("java.vm.version") + " by " + System.getProperty("java.vm.vendor") + ".\n");
            res.append("The operating system was " + System.getProperty("os.name") + ".\n");
            res.append("The amount of available memory to the VM was " + Runtime.getRuntime().maxMemory() / 1024 / 1024 + "Mb.\n");
            if (!results.isEmpty()) {
                res.append("<table><tr><th>method</th><th>Number of repeats</th><th>HashMap</th><th>FKache</th></tr>\n");
                Iterator iter = results.keySet().iterator();
                while (iter.hasNext()) {
                    String method = (String) iter.next();
                    float[] fs = (float[]) results.get(method);
                    res.append("<tr><td>" + method + "</td><td>" + countFormat.format((long) fs[0] * 1000) + "</td><td>" + 100 * fs[1] + "%</td><td>" + (long) (100F * fs[2]) + "%</td></tr>\n");
                }
                res.append("</table>");
            }
            res.append("</p>\n</section>\n</body>\n</document>");
            writer.write(res.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException("The results could not be written.");
        }
    }

    private void executeTest() {
        Map val = new HashMap();
        Method[] methods = Map.class.getDeclaredMethods();
        for (int i = 0; i < methods.length; i++) {
            Method method = methods[i];
            if (method.getName().equals("hashCode") || method.getName().equals("equals")) {
                continue;
            }
            Class[] types = method.getParameterTypes();
            String params = "(";
            for (int j = 0; j < types.length; j++) {
                String name = types[j].getName();
                if (name.indexOf(".") != -1) {
                    name = name.substring(name.lastIndexOf(".") + 1);
                }
                params += name;
                if (j != types.length - 1) {
                    params += ", ";
                }
            }
            params += ")";
            val.put(method.getName() + params, execute(method));
        }
        this.results = val;
    }

    private float[] execute(Method meth) {
        long otherTime = 0;
        long fkacheTime = 0;
        initOther();
        initJCacheMap();
        prepare(other);
        prepare(jcache);
        int reps = 1;
        while ((otherTime = repeat(other, meth, reps)) < testTime) {
            if (otherTime == 0) {
                otherTime++;
            }
            long factor = testTime / otherTime;
            if (factor <= 0) {
                factor += 2;
            } else if (factor == 1) {
                factor++;
            }
            System.out.println("increasing with " + factor + " to " + (reps *= factor) + " for " + meth.getName() + ", took:" + otherTime);
        }
        otherTime = repeat(other, meth, reps);
        System.out.println("finished HashMap");
        fkacheTime = repeat(jcache, meth, reps);
        System.out.println("other:" + otherTime + ", FKache:" + fkacheTime);
        return new float[] { reps, 1, ((float) fkacheTime / (float) otherTime) };
    }

    private void prepare(Map map) {
        for (int i = 0; i < 500; i++) {
            map.put("" + i, new Integer(i));
        }
    }

    private long repeat(Map map, Method method, int reps) {
        try {
            Object obj = null;
            boolean b = false;
            int size = 0;
            Collection values = null;
            long time = System.currentTimeMillis();
            if (method.getName().equals("isEmpty")) {
                for (int y = 0; y < reps; y++) {
                    for (int i = 999; i >= 0; i--) {
                        b = map.isEmpty();
                    }
                }
            } else if (method.getName().equals("size")) {
                for (int y = 0; y < reps; y++) {
                    for (int i = 999; i >= 0; i--) {
                        size = map.size();
                    }
                }
            } else if (method.getName().equals("hashCode")) {
                for (int y = 0; y < reps; y++) {
                    for (int i = 999; i >= 0; i--) {
                        size = map.hashCode();
                    }
                }
            } else if (method.getName().equals("containsValue")) {
                Object[][] params = prepareParams(method);
                for (int y = 0; y < reps; y++) {
                    for (int i = 999; i >= 0; i--) {
                        b = map.containsValue(params[i][0]);
                    }
                }
            } else if (method.getName().equals("containsKey")) {
                Object[][] params = prepareParams(method);
                for (int y = 0; y < reps; y++) {
                    for (int i = 999; i >= 0; i--) {
                        b = map.containsKey(params[i][0]);
                    }
                }
            } else if (method.getName().equals("get")) {
                Object[][] params = prepareParams(method);
                for (int y = 0; y < reps; y++) {
                    for (int i = 999; i >= 0; i--) {
                        obj = map.get(params[i][0]);
                    }
                }
            } else if (method.getName().equals("values")) {
                for (int y = 0; y < reps; y++) {
                    for (int i = 999; i >= 0; i--) {
                        values = map.values();
                    }
                }
            } else if (method.getName().equals("equals")) {
                Object[][] params = prepareParams(method);
                for (int y = 0; y < reps; y++) {
                    for (int i = 999; i >= 0; i--) {
                        b = map.equals(params[i][0]);
                    }
                }
            } else if (method.getName().equals("entrySet")) {
                for (int y = 0; y < reps; y++) {
                    for (int i = 999; i >= 0; i--) {
                        values = map.entrySet();
                    }
                }
            } else if (method.getName().equals("clear")) {
                for (int y = 0; y < reps; y++) {
                    for (int i = 999; i >= 0; i--) {
                        map.clear();
                    }
                }
            } else if (method.getName().equals("putAll")) {
                for (int y = 0; y < reps; y++) {
                    for (int i = 999; i >= 0; i--) {
                        map.putAll(testMap);
                    }
                }
            } else if (method.getName().equals("remove")) {
                Object[][] params = prepareParams(method);
                for (int y = 0; y < reps; y++) {
                    for (int i = 999; i >= 0; i--) {
                        obj = map.remove(params[i][0]);
                    }
                }
            } else if (method.getName().equals("put")) {
                Object[][] params = prepareParams(method);
                for (int y = 0; y < reps; y++) {
                    for (int i = 999; i >= 0; i--) {
                        obj = map.put(params[i][0], params[i][1]);
                    }
                }
            } else if (method.getName().equals("keySet")) {
                for (int y = 0; y < reps; y++) {
                    for (int i = 999; i >= 0; i--) {
                        values = map.keySet();
                    }
                }
            }
            long took = System.currentTimeMillis() - time + 1;
            if (obj != null && b && size == 0 && values != null) {
                obj.toString();
            }
            return took;
        } catch (Exception e) {
            IllegalStateException ie = new IllegalStateException(method.toString());
            ie.initCause(e);
            throw ie;
        }
    }

    private Object[][] prepareParams(Method method) throws InstantiationException, IllegalAccessException {
        Class[] types = method.getParameterTypes();
        Object[][] params = new Object[1000][types.length];
        Map testMap = getTestMap();
        for (int i = 0; i < 1000; i++) {
            for (int j = 0; j < types.length; j++) {
                Class type = types[j];
                if (type == Map.class) {
                    params[i][j] = testMap;
                }
                if (j == 0) {
                    if (type == Object.class) {
                        params[i][0] = Integer.toString(i);
                    } else if (params[0] == null) {
                        params[i][0] = type.newInstance();
                    }
                } else if (j == 1) {
                    if (type == Object.class) {
                        params[i][1] = new Integer(i);
                    } else if (params[1] == null) {
                        params[i][1] = type.newInstance();
                    }
                }
            }
        }
        return params;
    }

    private Map testMap = new HashMap();

    private Map getTestMap() {
        if (testMap.isEmpty()) {
            for (int i = 0; i < 1000; i++) {
                testMap.put("" + i, new Integer(i));
            }
        }
        return testMap;
    }

    private void initJCacheMap() {
        try {
            CacheAccessFactory factory = CacheAccessFactory.getInstance();
            Cache cache = factory.getCache(false);
            CacheAttributes attributes = CacheAttributes.getDefaultCacheAttributes();
            attributes.setLocal();
            attributes.setMaxObjects(100000000);
            cache.init(attributes);
            this.jcache = factory.getMapAccess();
        } catch (CacheException e) {
            throw new IllegalStateException(e.getMessage());
        } catch (CacheException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    private void initOther() {
        this.other = new HashMap();
    }
}
