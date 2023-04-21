package welo.utility;

import java.util.UUID;

public class UUIDHelper {

    public static String getUUID() {
        UUID a = UUID.randomUUID();
        return a.toString();
    }

    public static void main(String[] args) {
        System.out.println(getUUID());
    }
}
