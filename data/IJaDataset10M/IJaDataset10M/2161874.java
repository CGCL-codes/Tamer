package org.maverickdbms.server.database;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Vector;
import org.maverickdbms.basic.ConstantString;
import org.maverickdbms.basic.MaverickException;
import org.maverickdbms.basic.Factory;
import org.maverickdbms.basic.Field;
import org.maverickdbms.basic.File;
import org.maverickdbms.basic.List;
import org.maverickdbms.basic.Program;
import org.maverickdbms.basic.MaverickString;
import org.maverickdbms.basic.util.Tree;

class ClientConnection extends Connection {

    private Client client;

    private Tree programs;

    ClientConnection(Factory factory, Client client, Socket socket) throws IOException {
        super(factory, socket);
        this.client = client;
        this.programs = factory.getTree(Tree.TYPE_AVL);
    }

    public Field readField(DataInputStream dis) throws IOException {
        int p_handle = dis.readInt();
        int f_handle = dis.readInt();
        int fd_handle = dis.readInt();
        ClientProgram p = (ClientProgram) programs.probe(new ClientProgram(p_handle));
        return ((ClientDictionaryFile) p.getFile(f_handle)).getField(fd_handle);
    }

    public File readFile(DataInputStream dis) throws IOException {
        int p_handle = dis.readInt();
        int f_handle = dis.readInt();
        return ((ClientProgram) programs.probe(new ClientProgram(p_handle))).getFile(f_handle);
    }

    public List readList(DataInputStream dis) throws IOException {
        int type = dis.readInt();
        if (type == Protocol.TYPE_MV_LIST_REFERENCE) {
            int p_handle = dis.readInt();
            int lt_handle = dis.readInt();
            ClientProgram p = (ClientProgram) programs.probe(new ClientProgram(p_handle));
            return p.getList(lt_handle);
        } else if (type == Protocol.TYPE_MV_LIST_RECORDS) {
            int count = dis.readInt();
            String[] arr = new String[count];
            for (int i = 0; i < count; i++) {
                arr[i] = dis.readUTF();
            }
            return new RecordList(arr);
        }
        return null;
    }

    public Program readProgram(DataInputStream dis) throws IOException {
        int handle = dis.readInt();
        return (Program) programs.probe(new ClientProgram(handle));
    }

    public void writeField(DataOutputStream dos, Field file) throws MaverickException {
        throw new MaverickException(0, "writeField() called!!!");
    }

    public void writeFile(DataOutputStream dos, File file) throws MaverickException {
        throw new MaverickException(0, "writeFile() called!!!");
    }

    public void writeList(DataOutputStream dos, List list) throws MaverickException {
        throw new MaverickException(0, "writelist() called!!!");
    }

    public void writeProgram(DataOutputStream dos, Program program) {
        System.err.println("writeProgram() called!!!");
    }

    public int[] getReceiveTypes(int command) {
        return Protocol.REQUEST_TYPES[command];
    }

    public int[] getSendTypes(int command) {
        return Protocol.RESPONSE_TYPES[command];
    }
}
