package atp.reporter.data;

import atp.reporter.product.RLineKeyObject;

/**
 * @author Shteinke_KE
 * ������ �������� ������� ���
 */
public class ROKOObjectFull {

    public final int id;

    public final int type;

    public final int parent;

    public final String name;

    public ROKOObjectFull(int parent, int id, int type, String name) {
        this.id = id;
        this.parent = parent;
        this.type = type;
        this.name = name;
    }

    public boolean equals(Object obj) {
        return obj instanceof ROKOObjectFull && ((ROKOObjectFull) obj).id == id;
    }

    public int hashCode() {
        return id;
    }

    public String toString() {
        return "O: (" + parent + "," + id + "," + type + "," + name + ")";
    }

    public static class RLineKeyObjectOKO extends RLineKeyObject {

        public RLineKeyObjectOKO(ROKOObjectFull object) {
            super(object);
        }

        public String getTitle() {
            return ((ROKOObjectFull) object).name;
        }
    }
}
