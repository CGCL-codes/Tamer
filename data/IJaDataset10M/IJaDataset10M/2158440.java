package org.gerhardb.jibs.textPad.rankPad;

import java.io.*;

class RecentFile implements Serializable {

    String filePath;

    int y;

    RecentFile(File f, int yPosition) {
        this.filePath = f.getAbsolutePath();
        this.y = yPosition;
    }

    @Override
    public String toString() {
        return this.filePath;
    }

    @Override
    public int hashCode() {
        return this.filePath.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o instanceof RecentFile) {
            RecentFile other = (RecentFile) o;
            if (other.filePath.equals(this.filePath)) {
                return true;
            }
        }
        return false;
    }

    static byte[] toBytesRecentFile(RecentFile o) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(o);
        return baos.toByteArray();
    }

    static RecentFile fromBytesRecentFile(byte raw[]) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bais = new ByteArrayInputStream(raw);
        ObjectInputStream ois = new ObjectInputStream(bais);
        Object o = ois.readObject();
        return (RecentFile) o;
    }
}
