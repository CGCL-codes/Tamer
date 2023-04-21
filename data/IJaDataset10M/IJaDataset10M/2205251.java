package com.griddynamics.convergence.demo.utils.remote;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import com.griddynamics.convergence.demo.utils.cluster.ssh.SocketHelper;
import com.griddynamics.convergence.demo.utils.remote.RemoteControlJvm.MasterInterface;
import com.griddynamics.convergence.demo.utils.remote.RemoteControlJvm.SlaveInterface;
import com.griddynamics.convergence.demo.utils.rmi.RemoteMessage;
import com.griddynamics.convergence.demo.utils.rmi.RmiChannel;
import com.griddynamics.convergence.demo.utils.rmi.RmiChannel.OutputChannel;

public class RemoteControlJvmAgent implements SlaveInterface, Runnable, OutputChannel {

    private Executor executor = Executors.newCachedThreadPool();

    private RmiChannel channel;

    private RmiObjectInputStream in;

    private RmiObjectOutputStream out;

    private MasterInterface master;

    public RemoteControlJvmAgent(final Socket socket) throws IOException, ClassNotFoundException {
        InputStream pin = socket.getInputStream();
        OutputStream pout = socket.getOutputStream();
        out = new RmiObjectOutputStream(pout);
        this.channel = new RmiChannel(this, executor, new Class[] { Remote.class });
        this.channel.exportObject(SlaveInterface.class, this);
        synchronized (out) {
            out.writeUnshared(this);
            out.reset();
        }
        ;
        in = new RmiObjectInputStream(pin);
        master = (MasterInterface) in.readObject();
        executor.execute(new Runnable() {

            public void run() {
                try {
                    if (socket.isClosed()) {
                        System.err.println("Socket closed");
                        System.exit(1);
                    }
                    Thread.sleep(300);
                } catch (Exception e) {
                    System.exit(1);
                }
            }
        });
    }

    public static void main(String[] args) {
        try {
            Socket socket;
            if (args[0].toLowerCase().equals("tcp-client")) {
                String serverHost = args[1];
                int serverPort = Integer.parseInt(args[2]);
                socket = SocketHelper.connect(serverHost, serverPort, 5000);
            } else if (args[0].toLowerCase().equals("tcp-server")) {
                int serverPort = Integer.parseInt(args[1]);
                socket = SocketHelper.accept(serverPort, 5000);
            } else {
                System.out.println("Unknown transport " + args[0]);
                System.exit(1);
                throw new Error();
            }
            try {
                new RemoteControlJvmAgent(socket).run();
            } catch (SocketException e) {
                System.err.println(e);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.exit(1);
    }

    public <T> T remoteCall(Callable<T> callable) throws RemoteException {
        try {
            return callable.call();
        } catch (Exception e) {
            throw new RemoteException("Invocation failed", e);
        }
    }

    public void shutdown() {
        executor.execute(new Runnable() {

            public void run() {
                try {
                    in.close();
                } catch (IOException e) {
                }
                try {
                    out.close();
                } catch (IOException e) {
                }
                System.err.println("Shutdown request");
                System.exit(0);
            }
        });
    }

    public void run() {
        while (true) {
            try {
                Object message = in.readObject();
                if (message != null) {
                    if ("close".equals(message)) {
                        try {
                            channel.close();
                        } catch (Exception e) {
                        }
                        try {
                            in.close();
                        } catch (Exception e) {
                        }
                        try {
                            out.close();
                        } catch (Exception e) {
                        }
                        break;
                    }
                    channel.handleRemoteMessage((RemoteMessage) message);
                }
            } catch (SocketException e) {
                System.err.println(e);
                break;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void send(RemoteMessage message) throws IOException {
        synchronized (out) {
            out.writeUnshared(message);
            out.reset();
        }
    }

    private class RmiObjectInputStream extends ObjectInputStream {

        public RmiObjectInputStream(InputStream in) throws IOException {
            super(in);
            enableResolveObject(true);
        }

        @Override
        protected Object resolveObject(Object obj) throws IOException {
            return channel.streamResolveObject(obj);
        }
    }

    private class RmiObjectOutputStream extends ObjectOutputStream {

        public RmiObjectOutputStream(OutputStream in) throws IOException {
            super(in);
            enableReplaceObject(true);
        }

        @Override
        protected Object replaceObject(Object obj) throws IOException {
            return channel.streamReplaceObject(obj);
        }
    }
}
