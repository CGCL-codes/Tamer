package pubweb.bsp;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URL;
import padrmi.ObjectInputStreamWithLoader;
import padrmi.URLClassLoaderFactory;

public class Message implements Comparable<Message>, Serializable {

    private static final long serialVersionUID = 1L;

    private int superstep;

    private int source;

    private int destination;

    private long sequence;

    private URL codebase;

    private Serializable content;

    public Message() {
    }

    public Message(int superstep, int source, int destination, long sequence, Serializable content, URL codebase) {
        this.superstep = superstep;
        this.source = source;
        this.destination = destination;
        this.sequence = sequence;
        this.content = content;
        this.codebase = codebase;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeInt(superstep);
        out.writeInt(source);
        out.writeInt(destination);
        out.writeLong(sequence);
        out.writeObject(codebase);
        ObjectOutputStream objectStream = new ObjectOutputStream(out);
        objectStream.writeObject(content);
        objectStream.flush();
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        superstep = in.readInt();
        source = in.readInt();
        destination = in.readInt();
        sequence = in.readLong();
        codebase = (URL) in.readObject();
        ClassLoader loader = URLClassLoaderFactory.getURLClassLoader(codebase);
        ObjectInputStream objectStream = new ObjectInputStreamWithLoader(in, loader);
        content = (Serializable) objectStream.readObject();
    }

    public int hashCode() {
        return superstep + source + destination + (int) sequence;
    }

    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof Message)) return false;
        Message msg = (Message) obj;
        return superstep == msg.superstep && source == msg.source && destination == msg.destination && sequence == msg.sequence;
    }

    public int compareTo(Message msg) {
        if (superstep != msg.superstep) return superstep - msg.superstep; else if (source != msg.source) return source - msg.source; else if (destination != msg.destination) return destination - msg.destination; else if (sequence != msg.sequence) return (int) (sequence - msg.sequence); else return 0;
    }

    public String toString() {
        return "BSP message: superstep = " + superstep + "; source = " + source + "; destination = " + destination + "; sequence no. = " + sequence;
    }

    public int getSuperstep() {
        return superstep;
    }

    public int getSource() {
        return source;
    }

    public int getDestination() {
        return destination;
    }

    public long getSequence() {
        return sequence;
    }

    public Serializable getContent() {
        return content;
    }
}
