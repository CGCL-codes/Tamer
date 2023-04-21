package net.sourceforge.freejava.meta.build;

public class VersionInfo implements Comparable<VersionInfo> {

    public String name;

    public long time;

    public int revision[];

    public String author;

    public String state;

    private String revString;

    public String getVersion() {
        if (revString == null) {
            StringBuffer buf = new StringBuffer(revision.length * 8);
            for (int i = 0; i < revision.length; i++) {
                if (i != 0) buf.append('.');
                buf.append(revision[i]);
            }
            revString = buf.toString();
        }
        return revString;
    }

    public String getDate() {
        return VersionDate.formatDate(time);
    }

    public String getTime() {
        return VersionDate.formatTime(time);
    }

    @Override
    public String toString() {
        return name + "-" + getVersion();
    }

    @Override
    public int compareTo(VersionInfo o) {
        if (o == null) return 1;
        int cmp = name.compareTo(o.name);
        if (cmp != 0) return cmp;
        for (int i = 0; i < revision.length; i++) {
            if (i >= o.revision.length) return 1;
            cmp = revision[i] - o.revision[i];
            if (cmp != 0) return cmp;
        }
        return 0;
    }
}
