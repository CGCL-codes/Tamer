package org.acaraus.triviachatbot.qeditor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import org.acaraus.triviachatbot.datastructs.TriviaQuestion;
import org.acaraus.triviachatbot.datastructs.TriviaQuestionSet;
import org.eclipse.swt.dnd.ByteArrayTransfer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.TransferData;

public class QuestionTypeTransfer extends ByteArrayTransfer {

    private static final String MYTYPENAME = "question";

    private static final int MYTYPEID = registerType(MYTYPENAME);

    private static QuestionTypeTransfer _instance = new QuestionTypeTransfer();

    public QuestionTypeTransfer() {
    }

    public void javaToNative(Object object, TransferData transferData) {
        if (!checkMyType(object) || !isSupportedType(transferData)) {
            DND.error(DND.ERROR_INVALID_DATA);
        }
        TriviaQuestionSet quest = (TriviaQuestionSet) object;
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            DataOutputStream writeOut = new DataOutputStream(out);
            for (TriviaQuestion q : quest) {
                byte[] buffer = q.getQuestion().getBytes();
                writeOut.writeInt(buffer.length);
                writeOut.write(buffer);
                buffer = q.getAnswer().getBytes();
                writeOut.writeInt(buffer.length);
                writeOut.write(buffer);
                buffer = q.getHint1().getBytes();
                writeOut.writeInt(buffer.length);
                writeOut.write(buffer);
                buffer = q.getHint2().getBytes();
                writeOut.writeInt(buffer.length);
                writeOut.write(buffer);
                buffer = q.getHint3().getBytes();
                writeOut.writeInt(buffer.length);
                writeOut.write(buffer);
                buffer = q.getHint4().getBytes();
                writeOut.writeInt(buffer.length);
                writeOut.write(buffer);
                buffer = q.isAutoHint() ? "true".getBytes() : "false".getBytes();
                writeOut.writeInt(buffer.length);
                writeOut.write(buffer);
                writeOut.writeInt(q.getNumberOfHints());
            }
            byte[] buffer = out.toByteArray();
            writeOut.close();
            super.javaToNative(buffer, transferData);
        } catch (IOException e) {
        }
    }

    public Object nativeToJava(TransferData transferData) {
        if (isSupportedType(transferData)) {
            byte[] buffer = (byte[]) super.nativeToJava(transferData);
            if (buffer == null) return null;
            TriviaQuestionSet myData = new TriviaQuestionSet();
            try {
                ByteArrayInputStream in = new ByteArrayInputStream(buffer);
                DataInputStream readIn = new DataInputStream(in);
                while (readIn.available() > 20) {
                    TriviaQuestion datum = new TriviaQuestion();
                    int size = readIn.readInt();
                    byte[] name = new byte[size];
                    readIn.read(name);
                    datum.setQuestion(new String(name));
                    size = readIn.readInt();
                    name = new byte[size];
                    readIn.read(name);
                    datum.setAnswer(new String(name));
                    size = readIn.readInt();
                    name = new byte[size];
                    readIn.read(name);
                    datum.setHint1(new String(name));
                    size = readIn.readInt();
                    name = new byte[size];
                    readIn.read(name);
                    datum.setHint2(new String(name));
                    size = readIn.readInt();
                    name = new byte[size];
                    readIn.read(name);
                    datum.setHint3(new String(name));
                    size = readIn.readInt();
                    name = new byte[size];
                    readIn.read(name);
                    datum.setHint4(new String(name));
                    size = readIn.readInt();
                    name = new byte[size];
                    readIn.read(name);
                    datum.setAutoHint(Boolean.parseBoolean(new String(name)));
                    datum.setNumberOfHints(readIn.readInt());
                    myData.add(datum);
                }
                readIn.close();
            } catch (IOException ex) {
                return null;
            }
            return myData;
        }
        return null;
    }

    boolean checkMyType(Object object) {
        if (object == null || !(object instanceof TriviaQuestionSet)) {
            return false;
        }
        return true;
    }

    protected int[] getTypeIds() {
        return new int[] { MYTYPEID };
    }

    protected String[] getTypeNames() {
        return new String[] { MYTYPENAME };
    }

    public static QuestionTypeTransfer getInstance() {
        return _instance;
    }
}
