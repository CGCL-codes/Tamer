package com.rbnb.api;

import com.rbnb.compat.Utilities;

class OutputStream extends java.io.OutputStream implements com.rbnb.api.BuildInterface, java.io.DataOutput {

    /**
     * binary mode?
     * <p>
     * If this field is set, the serialization is done using binary values for
     * all numeric types and indexes for array and parameter values.
     * <p>
     * If this field is clear, the serialization is done using text values for
     * all numeric types, array, and parameter values.
     * <p>
     *
     * @author Ian Brown
     *
     * @since V1.0
     * @version 02/02/2001
     */
    private boolean binary = false;

    /**
     * the build date.
     * <p>
     *
     * @author Ian Brown
     *
     * @since V2.0
     * @version 11/06/2002
     */
    private java.util.Date buildDate = null;

    /**
     * the build version.
     * <p>
     *
     * @author Ian Brown
     *
     * @since V2.0
     * @version 11/06/2002
     */
    private String buildVersion = null;

    /**
     * stage writes for a future flush?
     * <p>
     *
     * @author Ian Brown
     *
     * @since V2.0
     * @version 07/26/2001
     */
    private boolean stage = false;

    /**
     * arrays to buffer multibyte objects.
     * <p>
     *
     * @author Ian Brown
     *
     * @since V2.0
     * @version 09/27/2001
     */
    private byte[][] mbArray = { new byte[8], new byte[8] };

    /**
     * buffer array to use.
     * <p>
     *
     * @author Ian Brown
     *
     * @since V2.0
     * @version 09/27/2001
     */
    private int mbIdx = 0;

    /**
     * number of bytes written at the last flush.
     * <p>
     *
     * @author Ian Brown
     *
     * @since V1.0
     * @version 02/02/2001
     */
    private long lastFlush = 0;

    /**
     * number of bytes written to the stream.
     * <p>
     *
     * @author Ian Brown
     *
     * @since V1.0
     * @version 02/02/2001
     */
    private long written = 0;

    /**
     * output stream to write to.
     * <p>
     *
     * @author Ian Brown
     *
     * @since V2.0
     * @version 03/05/2001
     */
    private java.io.OutputStream os = null;

    /**
     * staged output.
     * <p>
     *
     * @author Ian Brown
     *
     * @since V2.0
     * @version 07/26/2001
     */
    private java.util.Vector staged = null;

    /**
     * the real underlying output sream.
     * <p>
     *
     * @author Ian Brown
     *
     * @since V2.0
     * @version 03/16/2001
     */
    private java.io.OutputStream ros = null;

    static final String[] BOOLEANS = { "FALSE", "TRUE" };

    OutputStream(java.io.OutputStream oStreamI, boolean binaryI, int sizeI) throws java.io.IOException {
        ros = oStreamI;
        if (sizeI == 0) {
            os = oStreamI;
        } else {
            os = new java.io.DataOutputStream(oStreamI);
        }
        setBinary(binaryI);
    }

    private final void addStaged(byte[] entryI) {
        if (staged == null) {
            staged = new java.util.Vector();
        }
        staged.addElement(entryI);
    }

    final void addStaged(java.util.Vector vectorI) {
        if (staged == null) {
            staged = new java.util.Vector();
        }
        staged.addElement(vectorI);
    }

    final void addStaged(String[] parametersI, int parameterI, boolean bracketI) {
        if (staged == null) {
            staged = new java.util.Vector();
        }
        staged.addElement(parametersI);
        staged.addElement(new Integer(bracketI ? -(parameterI + 1) : parameterI));
    }

    final void addStaged(Serializable serializableI, String[] parametersI, int parameterI) {
        if (staged == null) {
            staged = new java.util.Vector();
        }
        staged.addElement(serializableI);
        staged.addElement(parametersI);
        staged.addElement(new Integer(parameterI));
    }

    public void close() {
        os = null;
    }

    public void flush() throws java.io.IOException {
        setStage(false, false);
        if (getWritten() != getLastFlush()) {
            if (!getBinary()) {
                writeString("\r\n");
            }
            try {
                os.flush();
                if (ros != os) {
                    ros.flush();
                }
            } catch (java.lang.NullPointerException e) {
                throw new java.io.IOException(e.getMessage());
            }
            setLastFlush(getWritten());
        }
    }

    final boolean getBinary() {
        return (binary);
    }

    public final java.util.Date getBuildDate() {
        return (buildDate);
    }

    public final String getBuildVersion() {
        return (buildVersion);
    }

    final long getFilePointer() throws java.io.IOException {
        if (!(ros instanceof RandomAccessOutputStream)) {
            throw new java.lang.IllegalStateException("The underlying stream does not support file pointers.");
        }
        return (((RandomAccessOutputStream) ros).getFilePointer());
    }

    private final long getLastFlush() {
        return (lastFlush);
    }

    final boolean getStage() {
        return (stage);
    }

    final long getWritten() {
        return (written);
    }

    public final void removeStaged(int indexI) {
        if (staged != null) {
            for (int length = staged.size(); length > indexI; --length) {
                staged.removeElementAt(length - 1);
            }
        }
    }

    final void setBinary(boolean binaryI) throws java.io.IOException {
        binary = binaryI;
        if (binary && !(ros instanceof RandomAccessOutputStream)) {
            write(0);
            flush();
        }
    }

    public final void setBuildDate(java.util.Date buildDateI) {
        buildDate = buildDateI;
    }

    public final void setBuildVersion(String buildVersionI) {
        buildVersion = buildVersionI;
    }

    final void setLastFlush(long lastFlushI) {
        lastFlush = lastFlushI;
    }

    public final int setStage(boolean stageI, boolean clearI) {
        stage = stageI;
        if (clearI) {
            staged = null;
        }
        return ((staged == null) ? 0 : staged.size());
    }

    final void setWritten(long writtenI) {
        written = writtenI;
    }

    public final int size() {
        return ((int) getWritten());
    }

    public final void write(byte bI[], int offI, int lenI) throws java.io.IOException {
        if (getStage()) {
            java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
            baos.write(bI, offI, lenI);
            addStaged(baos.toByteArray());
            baos.close();
        } else {
            writeStaged();
            os.write(bI, offI, lenI);
            setWritten(getWritten() + lenI);
        }
    }

    public void write(int bI) throws java.io.IOException {
        if (getStage()) {
            java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
            baos.write(bI);
            addStaged(baos.toByteArray());
            baos.close();
        } else {
            writeStaged();
            os.write(bI);
            setWritten(getWritten() + 1);
        }
    }

    public void writeBoolean(boolean vI) throws java.io.IOException {
        writeElement(BOOLEANS, vI ? 1 : 0);
    }

    public void writeByte(int vI) throws java.io.IOException {
        if (getBinary()) {
            write(vI);
        } else {
            writeString((new Byte((byte) vI)).toString());
        }
    }

    public void writeBytes(String vI) throws java.io.IOException {
        write(vI.getBytes());
    }

    public void writeChar(int vI) throws java.io.IOException {
        if (getBinary()) {
            write((vI >> 8) & 0xff);
            write((vI) & 0xff);
        } else {
            write(vI);
        }
    }

    public void writeChars(String vI) throws java.io.IOException {
        for (int idx = 0; idx < vI.length(); ++idx) {
            writeChar((int) vI.charAt(idx));
        }
    }

    public void writeDouble(double vI) throws java.io.IOException {
        if (getBinary()) {
            writeLong(Double.doubleToLongBits(vI));
        } else {
            writeString(Double.toString(vI));
        }
    }

    public void writeElement(Object arrayI, int indexI) throws java.io.IOException {
        if (!arrayI.getClass().isArray()) {
            throw new java.lang.IllegalArgumentException(arrayI + " is not an array.");
        }
        if (getBinary()) {
            int elements = Utilities.getArrayLength(arrayI);
            if (elements <= Byte.MAX_VALUE) {
                write(indexI);
            } else if (elements <= Short.MAX_VALUE) {
                writeShort(indexI);
            } else {
                writeInt(indexI);
            }
        } else {
            if (arrayI instanceof boolean[]) {
                writeBoolean(((boolean[]) arrayI)[indexI]);
            } else if (arrayI instanceof byte[]) {
                writeByte(((byte[]) arrayI)[indexI]);
            } else if (arrayI instanceof double[]) {
                writeDouble(((double[]) arrayI)[indexI]);
            } else if (arrayI instanceof float[]) {
                writeFloat(((float[]) arrayI)[indexI]);
            } else if (arrayI instanceof int[]) {
                writeInt(((int[]) arrayI)[indexI]);
            } else if (arrayI instanceof long[]) {
                writeLong(((long[]) arrayI)[indexI]);
            } else if (arrayI instanceof short[]) {
                writeShort(((short[]) arrayI)[indexI]);
            } else if (arrayI instanceof String[]) {
                writeString(((String[]) arrayI)[indexI]);
            }
        }
    }

    public void writeFloat(float vI) throws java.io.IOException {
        if (getBinary()) {
            writeInt(Float.floatToIntBits(vI));
        } else {
            writeString(Float.toString(vI));
        }
    }

    public void writeInt(int vI) throws java.io.IOException {
        if (getBinary()) {
            int idx = mbIdx++;
            mbArray[idx][0] = ((byte) ((vI >> 24) & 0xff));
            mbArray[idx][1] = ((byte) ((vI >> 16) & 0xff));
            mbArray[idx][2] = ((byte) ((vI >> 8) & 0xff));
            mbArray[idx][3] = ((byte) ((vI) & 0xff));
            write(mbArray[idx], 0, 4);
            --mbIdx;
        } else {
            writeString(Integer.toString(vI));
        }
    }

    public void writeLong(long vI) throws java.io.IOException {
        if (getBinary()) {
            int idx = mbIdx++;
            mbArray[idx][0] = ((byte) ((vI >> 56) & 0xff));
            mbArray[idx][1] = ((byte) ((vI >> 48) & 0xff));
            mbArray[idx][2] = ((byte) ((vI >> 40) & 0xff));
            mbArray[idx][3] = ((byte) ((vI >> 32) & 0xff));
            mbArray[idx][4] = ((byte) ((vI >> 24) & 0xff));
            mbArray[idx][5] = ((byte) ((vI >> 16) & 0xff));
            mbArray[idx][6] = ((byte) ((vI >> 8) & 0xff));
            mbArray[idx][7] = ((byte) ((vI) & 0xff));
            write(mbArray[idx], 0, 8);
            --mbIdx;
        } else {
            writeString(Long.toString(vI));
        }
    }

    public void writeParameter(String[] parametersI, int parameterI) throws java.io.IOException {
        writeElement(parametersI, parameterI);
    }

    public void writeShort(int vI) throws java.io.IOException {
        if (getBinary()) {
            write((vI >> 8) & 0xff);
            write((vI) & 0xff);
        } else {
            writeString((new Short((short) vI)).toString());
        }
    }

    private void writeStaged() throws java.io.IOException {
        if (staged != null) {
            java.util.Vector oldStaged = staged;
            staged = null;
            for (int idx = 0, eIdx = oldStaged.size(); idx < eIdx; ++idx) {
                Object entry = oldStaged.elementAt(idx);
                if (entry instanceof byte[]) {
                    byte[] bytes = (byte[]) entry;
                    write(bytes, 0, bytes.length);
                } else if (entry instanceof String[]) {
                    String[] parameters = (String[]) entry;
                    int parameter = ((Integer) oldStaged.elementAt(++idx)).intValue();
                    if (parameter >= 0) {
                        writeParameter(parameters, parameter);
                    } else {
                        writeParameter(parameters, -(parameter + 1));
                        Serialize.writeOpenBracket(this);
                    }
                } else if (entry instanceof java.util.Vector) {
                    java.util.Vector vector = (java.util.Vector) entry;
                    for (int idx1 = 0; idx1 < vector.size(); ++idx1) {
                        entry = vector.elementAt(idx1);
                        if (entry instanceof byte[]) {
                            write((byte[]) entry);
                        } else if (entry instanceof Byte) {
                            writeByte(((Byte) entry).byteValue());
                        } else if (entry instanceof Double) {
                            writeDouble(((Double) entry).doubleValue());
                        } else if (entry instanceof Float) {
                            writeFloat(((Float) entry).floatValue());
                        } else if (entry instanceof Integer) {
                            writeInt(((Integer) entry).intValue());
                        } else if (entry instanceof Long) {
                            writeLong(((Long) entry).longValue());
                        } else if (entry instanceof Short) {
                            writeShort(((Short) entry).shortValue());
                        }
                    }
                } else {
                    String[] parameters = (String[]) oldStaged.elementAt(++idx);
                    int parameter = ((Integer) oldStaged.elementAt(++idx)).intValue();
                    ((Serializable) entry).writeStaged(this, parameters, parameter);
                }
            }
        }
    }

    public void writeString(String vI) throws java.io.IOException {
        if (getBinary()) {
            writeUTF(vI);
        } else {
            writeBytes(vI);
            if (!vI.equals("\r\n")) {
                writeChar(' ');
            }
        }
    }

    public void writeUTF(String strI) throws java.io.IOException {
        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
        java.io.DataOutputStream dos = new java.io.DataOutputStream(baos);
        dos.writeUTF(strI);
        byte array[] = baos.toByteArray();
        dos.close();
        write(array);
    }
}
