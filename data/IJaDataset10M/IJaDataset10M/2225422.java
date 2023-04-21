package org.snipsnap.snip.storage;

import snipsnap.api.snip.Snip;
import snipsnap.api.storage.SnipStorage;
import org.snipsnap.snip.SnipPostNameComparator;
import org.snipsnap.snip.storage.query.QueryKit;
import org.snipsnap.snip.storage.query.SnipComparator;
import org.snipsnap.snip.storage.query.SnipQuery;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;

/**
 * Wrapper with finders for in-memory searching. Can
 * be used with JDBC...Storage when all Snips are
 * kept in memory (via MemorySnipStorage).
 *
 * @author Stephan J. Schmidt
 * @version $Id: QuerySnipStorage.java 1849 2006-02-07 21:08:46Z leo $
 */
public class QuerySnipStorage implements SnipStorage {

    private static Comparator nameComparator = new SnipComparator() {

        public int compare(snipsnap.api.snip.Snip s1, Snip s2) {
            return s1.getName().compareTo(s2.getName());
        }
    };

    private static Comparator snipPostNameComparator = new SnipPostNameComparator();

    private static Comparator nameWithoutPathComparator = new SnipComparator() {

        public int compare(Snip s1, snipsnap.api.snip.Snip s2) {
            return getName(s1).compareTo(getName(s2));
        }

        public String getName(Snip snip) {
            String name = snip.getName();
            int index = name.lastIndexOf("/");
            if (-1 != index) {
                return name.substring(index + 1);
            } else {
                return name;
            }
        }
    };

    private static Comparator nameComparatorDesc = new SnipComparator() {

        public int compare(Snip s1, snipsnap.api.snip.Snip s2) {
            return s2.getName().compareTo(s1.getName());
        }
    };

    private static Comparator mTimeComparatorDesc = new SnipComparator() {

        public int compare(Snip s1, snipsnap.api.snip.Snip s2) {
            return s2.getMTime().compareTo(s1.getMTime());
        }
    };

    private static Comparator cTimeComparator = new SnipComparator() {

        public int compare(Snip s1, Snip s2) {
            return s1.getCTime().compareTo(s2.getCTime());
        }
    };

    private static Comparator hotnessComparator = new SnipComparator() {

        public int compare(Snip s1, Snip s2) {
            return s1.getAccess().getViewCount() < s2.getAccess().getViewCount() ? 1 : -1;
        }
    };

    private SnipStorage storage;

    public QuerySnipStorage(SnipStorage storage) {
        this.storage = storage;
    }

    public Snip[] match(String pattern) {
        return storage.match(pattern);
    }

    public Snip[] match(String start, String end) {
        return storage.match(start, end);
    }

    public Snip storageLoad(String name) {
        return storage.storageLoad(name);
    }

    public void storageStore(Snip snip) {
        storage.storageStore(snip);
    }

    public void storageStore(List snips) {
        storage.storageStore(snips);
    }

    public Snip storageCreate(String name, String content) {
        return storage.storageCreate(name, content);
    }

    public void storageRemove(snipsnap.api.snip.Snip snip) {
        storage.storageRemove(snip);
    }

    public int storageCount() {
        return storage.storageAll().size();
    }

    public List storageAll() {
        return storage.storageAll();
    }

    public List storageAll(String applicationOid) {
        return storage.storageAll(applicationOid);
    }

    public List storageByHotness(int size) {
        return QueryKit.querySorted(storage.storageAll(), hotnessComparator, size);
    }

    public List storageByUser(final String login) {
        return QueryKit.querySorted(storage.storageAll(), new SnipQuery() {

            public boolean fit(Snip snip) {
                return (login.equals(snip.getCUser()));
            }
        }, nameWithoutPathComparator);
    }

    public List storageByDateSince(final Timestamp date) {
        return QueryKit.query(storage.storageAll(), new SnipQuery() {

            public boolean fit(Snip snip) {
                return (date.before(snip.getMTime()));
            }
        });
    }

    public List storageByRecent(String applicationOid, int size) {
        return QueryKit.querySorted(storage.storageAll(applicationOid), mTimeComparatorDesc, size);
    }

    public List storageByRecent(int size) {
        return QueryKit.querySorted(storage.storageAll(), mTimeComparatorDesc, size);
    }

    public List storageByComments(final Snip parent) {
        return QueryKit.querySorted(storage.storageAll(), new SnipQuery() {

            public boolean fit(Snip snip) {
                return (parent == snip.getCommentedSnip());
            }
        }, cTimeComparator);
    }

    public List storageByParent(final Snip parent) {
        return QueryKit.query(storage.storageAll(), new SnipQuery() {

            public boolean fit(Snip snip) {
                return (parent == snip.getParent());
            }
        });
    }

    public List storageByParentNameOrder(final Snip parent, int count) {
        List list = QueryKit.querySorted(storage.storageAll(), new SnipQuery() {

            public boolean fit(Snip snip) {
                return (parent == snip.getParent());
            }
        }, nameComparatorDesc, count);
        return list;
    }

    public List storageByParentModifiedOrder(Snip parent, int count) {
        List result = storageByParent(parent);
        Collections.sort(result, mTimeComparatorDesc);
        return result.subList(0, Math.min(count, result.size()));
    }

    public List storageByDateInName(final String nameSpace, final String start, final String end) {
        final String queryStart = nameSpace + "/" + start + "/";
        final String queryEnd = nameSpace + "/" + end + "/";
        List blogWithParent = QueryKit.querySorted(storage.storageAll(), new SnipQuery() {

            public boolean fit(Snip snip) {
                String name = snip.getName();
                Snip parent = snip.getParent();
                boolean blogWithParent = (start.compareTo(name) <= 0 && end.compareTo(name) >= 0 && null != parent && nameSpace.equals(parent.getName()));
                return blogWithParent;
            }
        }, nameComparatorDesc);
        List blogWithNameSpace = new ArrayList(Arrays.asList(match(queryStart, queryEnd)));
        Collections.sort(blogWithNameSpace, snipPostNameComparator);
        blogWithNameSpace.addAll(blogWithParent);
        return blogWithNameSpace;
    }
}
