package net.sf.julie.types.io;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import net.sf.julie.Immediate;
import net.sf.julie.Interpretable;
import net.sf.julie.SchemeException;

public class OutputPort extends Immediate {

    public static final OutputPort SYSTEM_OUT = new OutputPort(System.out);

    private OutputStream stream;

    public OutputPort(OutputStream stream) {
        this.stream = stream;
    }

    public OutputPort(File file) {
        try {
            this.stream = new BufferedOutputStream(new FileOutputStream(file));
        } catch (FileNotFoundException e) {
            throw new SchemeException(e.getMessage());
        }
    }

    public void write(String str) {
        try {
            stream.write(str.getBytes());
            stream.flush();
        } catch (IOException e) {
            throw new SchemeException(e.getMessage());
        }
    }

    public void writeObject(Interpretable obj) {
        try {
            stream.write(String.valueOf(obj).getBytes());
            stream.flush();
        } catch (IOException e) {
            throw new SchemeException(e.getMessage());
        }
    }
}
