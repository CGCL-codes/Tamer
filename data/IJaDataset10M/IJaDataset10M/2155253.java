package org.paw.handler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;
import sunlabs.brazil.server.Handler;
import sunlabs.brazil.server.Request;
import sunlabs.brazil.server.Server;
import sunlabs.brazil.util.http.HttpInputStream;

/**
 *
 * @author  dboster
 */
public class UrlHandler implements Handler {

    private static final String HOSTS = "hosts";

    String prefix;

    String urlPattern;

    int items;

    private static List list;

    char[] data;

    int data_length;

    boolean isSorted;

    /** Creates a new instance of UrlHandler */
    public UrlHandler() {
    }

    public boolean init(Server server, String prefix) {
        this.prefix = prefix;
        Properties props = server.props;
        list = new ArrayList();
        items = 0;
        data_length = 0;
        isSorted = false;
        try {
            String hosts = props.getProperty(prefix + HOSTS);
            if (hosts.charAt(0) == '@') {
                loadHosts(server, props, prefix, hosts.substring(1));
            } else {
            }
        } catch (Exception e) {
            server.log(Server.LOG_DIAGNOSTIC, prefix, "error in hosts: " + e.getMessage());
        }
        return true;
    }

    public boolean respond(Request request) throws IOException {
        if (items > 1) {
            if (inBannedSiteList(request) >= 0) {
                request.log(Server.LOG_DIAGNOSTIC, prefix, request.url);
                return sendReplacementPage(request);
            }
        } else request.log(Server.LOG_WARNING, prefix, "List is empty!");
        return false;
    }

    public boolean sendReplacementPage(Request request) throws IOException {
        request.sendResponse("Bad URL, go away!", "text/html");
        return true;
    }

    public void loadHosts(Server server, Properties props, String prefix, String file) {
        server.log(Server.LOG_DIAGNOSTIC, prefix, "URL File: '" + file + "'");
        if (isCacheFileNewer(file) > 0) {
            file = file + ".processed";
            isSorted = true;
        }
        try {
            FileInputStream in = new FileInputStream(file);
            int len = in.available();
            data = new char[len];
            String line;
            HttpInputStream fin = new HttpInputStream(in);
            if (!isSorted) {
                server.log(Server.LOG_LOG, prefix, "Loading list from unsorted file");
                while ((line = fin.readLine()) != null) {
                    line = line.trim();
                    if ((line.length() == 0) || (line.charAt(0) == '#')) {
                        continue;
                    }
                    list.add(new Integer(data_length));
                    for (int i = 0; i < line.length(); i++) {
                        data[data_length + i] = line.charAt(i);
                    }
                    data[data_length + line.length()] = '\0';
                    data_length += line.length() + 1;
                    items++;
                }
            } else {
                server.log(Server.LOG_LOG, prefix, "Loading list from cache file");
                while ((line = fin.readLine()) != null) {
                    line = line.trim();
                    list.add(new Integer(data_length));
                    for (int i = 0; i < line.length(); i++) {
                        data[data_length + i] = line.charAt(i);
                    }
                    data[data_length + line.length()] = '\0';
                    data_length += line.length() + 1;
                    items++;
                }
            }
        } catch (Exception e) {
        }
        if (!isSorted) {
            server.log(Server.LOG_INFORMATIONAL, prefix, "Sorting cache file");
            endsWithSort();
            server.log(Server.LOG_LOG, prefix, "Creating cache file");
            createCacheFile(file);
        }
    }

    public int isCacheFileNewer(String filename) {
        try {
            File listFile = new File(filename);
            File sortedFile = new File((filename + ".processed"));
            if (sortedFile.lastModified() > listFile.lastModified()) return 1;
        } catch (Exception e) {
            return -1;
        }
        return 0;
    }

    private boolean createCacheFile(String file) {
        String cacheFile = file + ".processed";
        try {
            FileWriter fout = new FileWriter(cacheFile);
            for (int i = 0; i < items; i++) fout.write(getString(((Integer) list.get(i)).intValue()));
            fout.close();
        } catch (java.io.IOException ie) {
            System.out.println(ie.getMessage());
            return true;
        }
        return false;
    }

    private void endsWithSort() {
        System.out.println();
        if (items < 2 || isSorted) return;
        QuickSort.sort(list, new StringComparator());
        isSorted = true;
    }

    private String getString(final int loc) {
        String retVal = "";
        int cur = loc;
        char m;
        for (; ; ) {
            m = data[cur];
            if (m != '\0') retVal += m; else break;
            cur++;
        }
        retVal += '\n';
        return retVal;
    }

    public int inBannedSiteList(Request request) {
        request.log(Server.LOG_DIAGNOSTIC, prefix, "Checking Unformatted URL: '" + request.url + "'");
        StringTokenizer st = new StringTokenizer(request.url);
        String url = "";
        while (st.hasMoreTokens()) {
            url = url + st.nextToken();
        }
        url = url.toLowerCase();
        int i = url.indexOf("//");
        if (i >= 0) {
            url = url.substring((url.indexOf("//") + 2), (url.length()));
        }
        i = url.indexOf("/");
        if (i >= 0) {
            url = url.substring(0, i);
        }
        request.log(Server.LOG_DIAGNOSTIC, prefix, "Checking Formatted URL: '" + url + "'");
        while (url.indexOf(46) >= 0) {
            i = findInList(url);
            if (i >= 0) {
                return i;
            }
            url = url.substring((url.indexOf(46) + 1), (url.length()));
            request.log(Server.LOG_DIAGNOSTIC, prefix, request.url);
        }
        i = findInList(url);
        if (i >= 0) {
            return i;
        }
        return -1;
    }

    public int findInList(String string) {
        if (items < 1) {
            return -1;
        }
        return searchREW(0, items - 1, string);
    }

    public int searchREWF(final int a, final int s, final String p) {
        if (a > s) return (-1 - a);
        int m = (a + s) / 2;
        int r = greaterThanEWF(p, list.get(m));
        if (r == 0) return m;
        if (r == -1) return searchREWF(m + 1, s, p);
        if (a == s) return (-1 - a);
        return searchREWF(a, m - 1, p);
    }

    private int greaterThanEWF(String a, Object b) {
        int alen = a.length();
        int blen = myStrLen((((Integer) b).intValue()));
        int maxlen = alen < blen ? alen : blen;
        int apos = alen - 1;
        int bpos = (((Integer) b).intValue()) + blen - 1;
        char achar = ' ';
        char bchar = ' ';
        while (maxlen > 0) {
            achar = a.charAt(apos);
            bchar = data[bpos];
            if (achar > bchar) {
                return -1;
            }
            if (achar < bchar) {
                return 1;
            }
            maxlen--;
            apos--;
            bpos--;
        }
        if (achar > bchar) {
            return -1;
        }
        if (achar < bchar) {
            return 1;
        }
        return 0;
    }

    private int searchREW(final int a, final int s, final String p) {
        if (a > s) return (-1 - a);
        int m = (a + s) / 2;
        int r = greaterThanEW(p, list.get(m));
        if (r == 0) return m;
        if (r == -1) return searchREW(m + 1, s, p);
        if (a == s) return (-1 - a);
        return searchREW(a, m - 1, p);
    }

    private int greaterThanEW(String a, Object b) {
        int alen = a.length();
        int blen = myStrLen((((Integer) b).intValue()));
        int maxlen = alen < blen ? alen : blen;
        int apos = alen - 1;
        int bpos = (((Integer) b).intValue()) + blen - 1;
        char achar = ' ';
        char bchar = ' ';
        while (maxlen > 0) {
            achar = a.charAt(apos);
            bchar = data[bpos];
            if (achar > bchar) {
                return -1;
            }
            if (achar < bchar) {
                return 1;
            }
            maxlen--;
            apos--;
            bpos--;
        }
        if (blen > alen) {
            return 1;
        }
        return 0;
    }

    private int myStrLen(int pos) {
        int cur = pos;
        while (data[cur] != '\0') {
            cur++;
        }
        return (cur - pos);
    }

    class StringComparator implements Comparator {

        public final int compare(Object a, Object b) {
            if ((((Integer) a).intValue() < 0) || (((Integer) b).intValue() < 0)) {
            }
            int alen = myStrLen((((Integer) a).intValue()));
            int blen = myStrLen((((Integer) b).intValue()));
            int maxlen = alen < blen ? alen : blen;
            int apos = (((Integer) a).intValue()) + alen - 1;
            int bpos = (((Integer) b).intValue()) + blen - 1;
            char achar = ' ';
            char bchar = ' ';
            while (maxlen > 0) {
                achar = data[apos];
                bchar = data[bpos];
                if (achar > bchar) {
                    return 1;
                }
                if (achar < bchar) {
                    return -1;
                }
                maxlen--;
                apos--;
                bpos--;
            }
            if (achar > bchar) {
                return 1;
            }
            if (achar < bchar) {
                return -1;
            }
            return 0;
        }

        public final boolean equals(Object a, Object b) {
            int alen = myStrLen((((Integer) a).intValue()));
            int blen = myStrLen((((Integer) b).intValue()));
            int maxlen = alen < blen ? alen : blen;
            int apos = (((Integer) a).intValue()) + alen - 1;
            int bpos = (((Integer) b).intValue()) + blen - 1;
            char achar = ' ';
            char bchar = ' ';
            while (maxlen > 0) {
                achar = data[apos];
                bchar = data[bpos];
                if (achar > bchar) {
                    return true;
                }
                if (achar < bchar) {
                    return false;
                }
                maxlen--;
                apos--;
                bpos--;
            }
            if (achar > bchar) {
                return false;
            }
            if (achar < bchar) {
                return false;
            }
            return true;
        }

        private int myStrLen(int pos) {
            int cur = pos;
            while (data[cur] != '\0') {
                cur++;
            }
            return (cur - pos);
        }
    }
}

class QuickSort {

    private static final boolean debugging = false;

    private Comparator delegate;

    private List userArray;

    public static void sort(List userArray, Comparator delegate) {
        QuickSort h = new QuickSort();
        h.delegate = delegate;
        h.userArray = userArray;
        h.quicksort(0, userArray.size() - 1);
        if (debugging) {
            if (!h.isAlreadySorted()) System.out.println("Sort failed");
        }
        return;
    }

    private void quicksort(int p, int r) {
        if (p < r) {
            int q = partition(p, r);
            if (q == r) {
                q--;
            }
            quicksort(p, q);
            quicksort(q + 1, r);
        }
    }

    private int partition(int lo, int hi) {
        Object pivot = userArray.get(lo);
        while (true) {
            while (delegate.compare(userArray.get(hi), pivot) >= 0 && lo < hi) {
                hi--;
            }
            while (delegate.compare(userArray.get(lo), pivot) < 0 && lo < hi) {
                lo++;
            }
            if (lo < hi) {
                Object T = userArray.get(lo);
                userArray.set(lo, userArray.get(hi));
                userArray.set(hi, T);
            } else return hi;
        }
    }

    private boolean isAlreadySorted() {
        for (int i = 1; i < userArray.size(); i++) {
            if (delegate.compare(userArray.get(i), userArray.get(i - 1)) < 0) return false;
        }
        return true;
    }
}
