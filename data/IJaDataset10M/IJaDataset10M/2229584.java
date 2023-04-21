package library.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import library.LibraryBaseClientStorekeeper;
import library.proxies.AddBookedItems;
import library.proxies.LibraryServerStorekeeper;
import library.utils.StorekeeperInfo;
import library.utils.bean.UserSession;

public class LibrarySocketClientStorekeeper extends LibraryBaseClientStorekeeper {

    protected ExecutorService exec;

    public LibrarySocketClientStorekeeper(Socket sendSocket, ObjectInputStream outInput, ObjectOutputStream outOutput) {
        server = new StorekeeperSocketStub(sendSocket, outInput, outOutput);
        exec = Executors.newFixedThreadPool(1);
        exec.execute((Runnable) server);
    }

    public void setAddable(AddBookedItems addable) {
        ((StorekeeperSocketStub) server).setAddable(addable);
    }

    class StorekeeperSocketStub implements LibraryServerStorekeeper, Runnable {

        protected AddBookedItems addable;

        protected boolean isInitialized;

        protected ServerSocket serverSocket;

        protected Socket receiveSocket;

        protected Socket sendSocket;

        protected ObjectInputStream inInput;

        protected ObjectOutputStream inOutput;

        protected ObjectInputStream outInput;

        protected ObjectOutputStream outOutput;

        public StorekeeperSocketStub(Socket sendSocket, ObjectInputStream outInput, ObjectOutputStream outOutput) {
            this.sendSocket = sendSocket;
            this.outInput = outInput;
            this.outOutput = outOutput;
        }

        public void setAddable(AddBookedItems addable) {
            this.addable = addable;
        }

        @Override
        public void run() {
            int methodID;
            try {
                initialize();
                while (!Thread.interrupted()) {
                    methodID = inInput.readInt();
                    try {
                        handleMethodInvocation(methodID);
                    } catch (Exception e) {
                        Thread.currentThread().interrupt();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    sendSocket.close();
                    receiveSocket.close();
                } catch (IOException e) {
                }
            }
        }

        protected synchronized void setInitialized() {
            isInitialized = true;
        }

        protected synchronized boolean isInitialized() {
            return isInitialized;
        }

        protected void testInitialized() {
            while (!isInitialized()) {
                Thread.yield();
            }
        }

        protected void initialize() throws IOException {
            serverSocket = new ServerSocket(0);
            outOutput.writeInt(serverSocket.getLocalPort());
            outOutput.flush();
            InetAddress sendaddr;
            InetAddress recvaddr;
            InputStream is;
            OutputStream os;
            Socket s = serverSocket.accept();
            sendaddr = sendSocket.getInetAddress();
            recvaddr = s.getInetAddress();
            if (sendaddr.equals(recvaddr)) {
                receiveSocket = s;
                is = receiveSocket.getInputStream();
                os = receiveSocket.getOutputStream();
                inOutput = new ObjectOutputStream(os);
                inInput = new ObjectInputStream(is);
                setInitialized();
            }
        }

        protected void handleMethodInvocation(int methodID) throws IOException, ClassNotFoundException {
            switch(methodID) {
                case LibrarySocketServer.INVOKE_ADD_BOOKED_ITEMS:
                    handleAddBookedItems();
                    break;
            }
        }

        protected void handleAddBookedItems() throws IOException, ClassNotFoundException {
            StorekeeperInfo[] items = (StorekeeperInfo[]) inInput.readObject();
            boolean added = addable.addBookedItems(items);
            inOutput.writeBoolean(added);
            inOutput.flush();
        }

        @Override
        public boolean markBookAsCompleted(int itemID) throws Exception {
            testInitialized();
            outOutput.writeInt(LibrarySocketServer.INVOKE_MARK_AS_COMPLETED);
            outOutput.writeInt(itemID);
            outOutput.flush();
            Boolean booked = outInput.readBoolean();
            return booked;
        }

        @Override
        public boolean logout() throws IOException {
            testInitialized();
            outOutput.writeInt(LibrarySocketServer.INVOKE_LOGOUT);
            outOutput.flush();
            boolean requestPerformed = outInput.readBoolean();
            return requestPerformed;
        }

        @Override
        public boolean storeSession(UserSession usession) throws Exception {
            testInitialized();
            outOutput.writeInt(LibrarySocketServer.INVOKE_STORE_SESSION);
            outOutput.writeObject(usession);
            outOutput.flush();
            boolean requestPerformed = outInput.readBoolean();
            return requestPerformed;
        }

        @Override
        public UserSession readSession() throws Exception {
            testInitialized();
            outOutput.writeInt(LibrarySocketServer.INVOKE_READ_SESSION);
            outOutput.flush();
            UserSession usession = (UserSession) outInput.readObject();
            return usession;
        }
    }
}
