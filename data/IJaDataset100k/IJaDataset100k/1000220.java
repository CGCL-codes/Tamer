package uk.co.christhomson.coherence.viewer.sample.objects;

import java.io.IOException;
import java.util.Date;
import com.tangosol.io.pof.PofReader;
import com.tangosol.io.pof.PofWriter;
import com.tangosol.io.pof.PortableObject;

public class TestKey5 implements PortableObject {

    private Date k1 = null;

    private Date k2 = null;

    public TestKey5() {
    }

    public TestKey5(Date k1, Date k2) {
        this.k1 = k1;
        this.k2 = k2;
    }

    public Date getK1() {
        return k1;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + k1.hashCode();
        result = prime * result + k2.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        TestKey5 other = (TestKey5) obj;
        if (!k1.equals(other.k1)) return false;
        if (!k2.equals(other.k2)) return false;
        return true;
    }

    @Override
    public String toString() {
        return "TestKey5 [k1=" + k1 + ",k2=" + k2 + "]";
    }

    public void readExternal(PofReader reader) throws IOException {
        long t1 = reader.readLong(0);
        if (t1 != 0) k1 = new Date(t1);
        long t2 = reader.readLong(1);
        if (t2 != 0) k2 = new Date(t2);
    }

    public void writeExternal(PofWriter writer) throws IOException {
        long t1 = 0;
        if (k1 != null) t1 = k1.getTime();
        writer.writeLong(0, t1);
        long t2 = 0;
        if (k2 != null) t2 = k2.getTime();
        writer.writeLong(1, t2);
    }
}
