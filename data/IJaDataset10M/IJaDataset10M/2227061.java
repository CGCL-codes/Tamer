package testing;

import java.util.UUID;

public class TestUUID {

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            String id1 = getId();
            String id2 = getIdCompact();
            System.out.println(id1 + " " + id2);
        }
    }

    private static String getId() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    private static String getIdCompact() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replaceAll("-", "");
    }
}
