package net.tinyos.packet;

class BaudRate {

    static void init() throws Exception {
        Platform.add(Platform.x, "mica", 19200);
        Platform.add(Platform.x, "mica2", 57600);
        Platform.add(Platform.x, "mica2dot", 19200);
        Platform.add(Platform.x, "telos", 115200);
        Platform.add(Platform.x, "telosb", 115200);
        Platform.add(Platform.x, "tinynode", 115200);
        Platform.add(Platform.x, "tmote", 115200);
        Platform.add(Platform.x, "micaz", 57600);
        Platform.add(Platform.x, "eyesIFX", 57600);
        Platform.add(Platform.x, "intelmote2", 115200);
        Platform.add(Platform.x, "iris", 57600);
        Platform.add(Platform.x, "shimmer", 115200);
    }
}
