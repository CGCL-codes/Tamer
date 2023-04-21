package aino.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.IOException;

public abstract class Reader extends FileIO {

    private static final String CS = "US-ASCII";

    protected Reader(File file) throws IOException {
        this(new BufferedReader(new InputStreamReader(new FileInputStream(file), CS)));
    }

    protected Reader(BufferedReader file) throws IOException {
        super();
        try {
            this.read(file);
        } finally {
            file.close();
        }
    }

    public abstract boolean isNotEmpty();

    public abstract void read(BufferedReader r) throws IOException;
}
