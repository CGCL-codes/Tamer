package nts.io;

public class NullLog implements Log {

    public static final NullLog LOG = new NullLog();

    public final Log add(char ch) {
        return this;
    }

    public final Log add(char ch, int count) {
        return this;
    }

    public final Log add(String str) {
        return this;
    }

    public final Log add(CharCode x) {
        return this;
    }

    public final Log endLine() {
        return this;
    }

    public final Log startLine() {
        return this;
    }

    public final Log flush() {
        return this;
    }

    public final void close() {
    }

    public final Log add(boolean val) {
        return this;
    }

    public final Log add(int num) {
        return this;
    }

    public final Log addEsc() {
        return this;
    }

    public final Log addEsc(String str) {
        return this;
    }

    public final Log add(Loggable x) {
        return this;
    }

    public final Log add(Loggable[] array) {
        return this;
    }

    public final Log add(Loggable[] array, int offset, int length) {
        return this;
    }

    public final Log resetCount() {
        return this;
    }

    public final int getCount() {
        return 0;
    }

    public final Log voidCounter() {
        return LOG;
    }

    public final Log sepRoom(int count) {
        return this;
    }
}
