package pikes.core;

import java.io.IOException;

public abstract class AbstractPrintable implements Printable {

    @Override
    public final String toString() {
        StringBuilder builder = new StringBuilder();
        try {
            print(builder);
            return builder.toString();
        } catch (IOException e) {
            return e.getMessage();
        }
    }
}
