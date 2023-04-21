package sf2.vm;

import java.util.Iterator;

public interface VMM extends Iterable<VirtualMachine> {

    public void start() throws VMException;

    public void shutdown();

    public String getName();

    public boolean isAvailable();

    public boolean isRunning();

    public Iterator<VirtualMachine> iterator();

    public void register(VirtualMachine vm);

    public void unregister(VirtualMachine vm);
}
