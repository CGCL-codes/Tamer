package common;

class Access {

    private static int x;

    static class SI {

        private static int y = x;
    }

    static void m() {
        int z = SI.y;
    }
}
