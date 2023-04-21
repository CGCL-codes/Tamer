package com.cell.persistance;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

public abstract class PersistanceManager {

    public abstract ObjectInputStream createReadStream(Reader reader) throws IOException;

    public abstract ObjectOutputStream createWriteStream(Writer writer) throws IOException;

    public abstract ObjectInputStream createReadStream(InputStream reader) throws IOException;

    public abstract ObjectOutputStream createWriteStream(OutputStream writer) throws IOException;
}
