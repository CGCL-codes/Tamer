package net.sourceforge.sbnvm;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import net.sourceforge.sbnvm.debugger.PacketInterface;
import net.sourceforge.sbnvm.hardware.ArrayRAM;
import net.sourceforge.sbnvm.hardware.Bus;
import net.sourceforge.sbnvm.hardware.NumberInput;
import net.sourceforge.sbnvm.hardware.NumberOutput;
import net.sourceforge.sbnvm.hardware.One;
import net.sourceforge.sbnvm.hardware.Zero;

public class VM {

    Bus bus;

    CPU cpu;

    public VM(int port) throws IOException {
        bus = new Bus();
        populateBus();
        System.out.println("Waiting for debugger...");
        ServerSocket server = new ServerSocket(port);
        Socket socket = server.accept();
        socket.setTcpNoDelay(true);
        ObjectOutputStream os = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream is = new ObjectInputStream(socket.getInputStream());
        PacketInterface.init(is, os);
        System.out.println("Debugger connection established.");
        cpu = new CPU(bus);
    }

    void populateBus() {
        bus.addDevice(0, 1024, new ArrayRAM(0));
        bus.addDevice(-1, -1, new One());
        bus.addDevice(-2, -2, new Zero());
        bus.addDevice(-3, -3, new NumberInput());
        bus.addDevice(-4, -4, new NumberOutput());
    }
}
