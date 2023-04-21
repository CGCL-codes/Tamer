package com.sun.opengl.impl.mipmap;

import javax.media.opengl.GL;
import java.nio.*;

/**
 *
 * @author  Administrator
 */
public class HalveImage {

    private static final int BOX2 = 2;

    private static final int BOX4 = 4;

    private static final int BOX8 = 8;

    public static void halveImage(int components, int width, int height, ShortBuffer datain, ShortBuffer dataout) {
        int i, j, k;
        int newwidth, newheight;
        int delta;
        int t = 0;
        short temp = 0;
        newwidth = width / 2;
        newheight = height / 2;
        delta = width * components;
        for (i = 0; i < newheight; i++) {
            for (j = 0; j < newwidth; j++) {
                for (k = 0; k < components; k++) {
                    datain.position(t);
                    temp = datain.get();
                    datain.position(t + components);
                    temp += datain.get();
                    datain.position(t + delta);
                    temp += datain.get();
                    datain.position(t + delta + components);
                    temp += datain.get();
                    temp += 2;
                    temp /= 4;
                    dataout.put(temp);
                    t++;
                }
                t += components;
            }
            t += delta;
        }
    }

    public static void halveImage_ubyte(int components, int width, int height, ByteBuffer datain, ByteBuffer dataout, int element_size, int ysize, int group_size) {
        int i, j, k;
        int newwidth, newheight;
        int s;
        int t;
        if (width == 1 || height == 1) {
            assert (!(width == 1 && height == 1));
            halve1Dimage_ubyte(components, width, height, datain, dataout, element_size, ysize, group_size);
            return;
        }
        newwidth = width / 2;
        newheight = height / 2;
        s = 0;
        t = 0;
        int temp = 0;
        for (i = 0; i < newheight; i++) {
            for (j = 0; j < newwidth; j++) {
                for (k = 0; k < components; k++) {
                    datain.position(t);
                    temp = (0x000000FF & datain.get());
                    datain.position(t + group_size);
                    temp += (0x000000FF & datain.get());
                    datain.position(t + ysize);
                    temp += (0x000000FF & datain.get());
                    datain.position(t + ysize + group_size);
                    temp += (0x000000FF & datain.get()) + 2;
                    dataout.put((byte) (temp / 4));
                    t += element_size;
                }
                t += group_size;
            }
            t += ysize;
        }
    }

    public static void halve1Dimage_ubyte(int components, int width, int height, ByteBuffer datain, ByteBuffer dataout, int element_size, int ysize, int group_size) {
        int halfWidth = width / 2;
        int halfHeight = height / 2;
        int src = 0;
        int dest = 0;
        int jj;
        int temp = 0;
        assert (width == 1 || height == 1);
        assert (width != height);
        if (height == 1) {
            assert (width != 1);
            halfHeight = 1;
            for (jj = 0; jj < halfWidth; jj++) {
                int kk;
                for (kk = 0; kk < components; kk++) {
                    datain.position(src);
                    temp = (0x000000FF & datain.get());
                    datain.position(src + group_size);
                    temp += (0x000000FF & datain.get());
                    temp /= 2;
                    dataout.put((byte) temp);
                    src += element_size;
                    dest++;
                }
                src += group_size;
            }
            int padBytes = ysize - (width * group_size);
            src += padBytes;
        } else if (width == 1) {
            int padBytes = ysize - (width * group_size);
            assert (height != 1);
            halfWidth = 1;
            for (jj = 0; jj < halfHeight; jj++) {
                int kk;
                for (kk = 0; kk < components; kk++) {
                    datain.position(src);
                    temp = (0x000000FF & datain.get());
                    datain.position(src + ysize);
                    temp += (0x000000FF & datain.get());
                    temp /= 2;
                    dataout.put((byte) temp);
                    src += element_size;
                    dest++;
                }
                src += padBytes;
                src += ysize;
            }
        }
        assert (src == ysize * height);
        assert (dest == components * element_size * halfWidth * halfHeight);
    }

    public static void halveImage_byte(int components, int width, int height, ByteBuffer datain, ByteBuffer dataout, int element_size, int ysize, int group_size) {
        int i, j, k;
        int newwidth, newheight;
        int s = 0;
        int t = 0;
        byte temp = (byte) 0;
        if (width == 1 || height == 1) {
            assert (!(width == 1 && height == 1));
            halve1Dimage_byte(components, width, height, datain, dataout, element_size, ysize, group_size);
            return;
        }
        newwidth = width / 2;
        newheight = height / 2;
        for (i = 0; i < newheight; i++) {
            for (j = 0; j < newwidth; j++) {
                for (k = 0; k < components; k++) {
                    datain.position(t);
                    temp = datain.get();
                    datain.position(t + group_size);
                    temp += datain.get();
                    datain.position(t + ysize);
                    temp += datain.get();
                    datain.position(t + ysize + group_size);
                    temp += datain.get();
                    temp += 2;
                    temp /= 4;
                    dataout.put(temp);
                    t += element_size;
                }
                t += group_size;
            }
            t += ysize;
        }
    }

    public static void halve1Dimage_byte(int components, int width, int height, ByteBuffer datain, ByteBuffer dataout, int element_size, int ysize, int group_size) {
        int halfWidth = width / 2;
        int halfHeight = width / 2;
        int src = 0;
        int dest = 0;
        int jj;
        byte temp = (byte) 0;
        assert (width == 1 || height == 1);
        assert (width != height);
        if (height == 1) {
            assert (width != 1);
            halfHeight = 1;
            for (jj = 0; jj < halfWidth; jj++) {
                int kk;
                for (kk = 0; kk < components; kk++) {
                    datain.position(src);
                    temp = datain.get();
                    datain.position(src + group_size);
                    temp += datain.get();
                    temp /= 2;
                    dataout.put(temp);
                    src += element_size;
                    dest++;
                }
                src += group_size;
            }
            int padBytes = ysize - (width * group_size);
            src += padBytes;
        } else if (width == 1) {
            int padBytes = ysize - (width * group_size);
            assert (height != 1);
            halfWidth = 1;
            for (jj = 0; jj < halfHeight; jj++) {
                int kk;
                for (kk = 0; kk < components; kk++) {
                    datain.position(src);
                    temp = datain.get();
                    datain.position(src + ysize);
                    temp += datain.get();
                    temp /= 2;
                    src += element_size;
                    dest++;
                }
                src += padBytes;
                src += ysize;
            }
            assert (src == ysize * height);
        }
        assert (dest == components * element_size * halfWidth * halfHeight);
    }

    public static void halveImage_ushort(int components, int width, int height, ByteBuffer datain, ShortBuffer dataout, int element_size, int ysize, int group_size, boolean myswap_bytes) {
        int i, j, k, l;
        int newwidth, newheight;
        int s = 0;
        int t = 0;
        int temp = 0;
        if (width == 1 || height == 1) {
            assert (!(width == 1 && height == 1));
            halve1Dimage_ushort(components, width, height, datain, dataout, element_size, ysize, group_size, myswap_bytes);
            return;
        }
        newwidth = width / 2;
        newheight = height / 2;
        if (!myswap_bytes) {
            for (i = 0; i < newheight; i++) {
                for (j = 0; j < newwidth; j++) {
                    for (k = 0; k < components; k++) {
                        datain.position(t);
                        temp = (0x0000FFFF & datain.getShort());
                        datain.position(t + group_size);
                        temp += (0x0000FFFF & datain.getShort());
                        datain.position(t + ysize);
                        temp += (0x0000FFFF & datain.getShort());
                        datain.position(t + ysize + group_size);
                        temp += (0x0000FFFF & datain.getShort());
                        dataout.put((short) ((temp + 2) / 4));
                        t += element_size;
                    }
                    t += group_size;
                }
                t += ysize;
            }
        } else {
            for (i = 0; i < newheight; i++) {
                for (j = 0; j < newwidth; j++) {
                    for (k = 0; k < components; k++) {
                        datain.position(t);
                        temp = (0x0000FFFF & Mipmap.GLU_SWAP_2_BYTES(datain.getShort()));
                        datain.position(t + group_size);
                        temp += (0x0000FFFF & Mipmap.GLU_SWAP_2_BYTES(datain.getShort()));
                        datain.position(t + ysize);
                        temp += (0x0000FFFF & Mipmap.GLU_SWAP_2_BYTES(datain.getShort()));
                        datain.position(t + ysize + group_size);
                        temp += (0x0000FFFF & Mipmap.GLU_SWAP_2_BYTES(datain.getShort()));
                        dataout.put((short) ((temp + 2) / 4));
                        t += element_size;
                    }
                    t += group_size;
                }
                t += ysize;
            }
        }
    }

    public static void halve1Dimage_ushort(int components, int width, int height, ByteBuffer datain, ShortBuffer dataout, int element_size, int ysize, int group_size, boolean myswap_bytes) {
        int halfWidth = width / 2;
        int halfHeight = height / 2;
        int src = 0;
        int dest = 0;
        int jj;
        assert (width == 1 || height == 1);
        assert (width != height);
        if (height == 1) {
            assert (width != 1);
            halfHeight = 1;
            for (jj = 0; jj < halfWidth; jj++) {
                int kk;
                for (kk = 0; kk < halfHeight; kk++) {
                    int[] ushort = new int[BOX2];
                    if (myswap_bytes) {
                        datain.position(src);
                        ushort[0] = (0x0000FFFF & Mipmap.GLU_SWAP_2_BYTES(datain.getShort()));
                        datain.position(src + group_size);
                        ushort[1] = (0x0000FFFF & Mipmap.GLU_SWAP_2_BYTES(datain.getShort()));
                    } else {
                        datain.position(src);
                        ushort[0] = (0x0000FFFF & datain.getShort());
                        datain.position(src + group_size);
                        ushort[1] = (0x0000FFFF & datain.getShort());
                    }
                    dataout.put((short) ((ushort[0] + ushort[1]) / 2));
                    src += element_size;
                    dest += 2;
                }
                src += group_size;
            }
            int padBytes = ysize - (width * group_size);
            src += padBytes;
        } else if (width == 1) {
            int padBytes = ysize - (width * group_size);
            assert (height != 1);
            halfWidth = 1;
            for (jj = 0; jj < halfHeight; jj++) {
                int kk;
                for (kk = 0; kk < components; kk++) {
                    int[] ushort = new int[BOX2];
                    if (myswap_bytes) {
                        datain.position(src);
                        ushort[0] = (0x0000FFFF & Mipmap.GLU_SWAP_2_BYTES(datain.getShort()));
                        datain.position(src + ysize);
                        ushort[0] = (0x0000FFFF & Mipmap.GLU_SWAP_2_BYTES(datain.getShort()));
                    } else {
                        datain.position(src);
                        ushort[0] = (0x0000FFFF & datain.getShort());
                        datain.position(src + ysize);
                        ushort[1] = (0x0000FFFF & datain.getShort());
                    }
                    dataout.put((short) ((ushort[0] + ushort[1]) / 2));
                    src += element_size;
                    dest += 2;
                }
                src += padBytes;
                src += ysize;
            }
            assert (src == ysize * height);
        }
        assert (dest == components * element_size * halfWidth * halfHeight);
    }

    public static void halveImage_short(int components, int width, int height, ByteBuffer datain, ShortBuffer dataout, int element_size, int ysize, int group_size, boolean myswap_bytes) {
        int i, j, k, l;
        int newwidth, newheight;
        int s = 0;
        int t = 0;
        short temp = (short) 0;
        if (width == 1 || height == 1) {
            assert (!(width == 1 && height == 1));
            halve1Dimage_short(components, width, height, datain, dataout, element_size, ysize, group_size, myswap_bytes);
            return;
        }
        newwidth = width / 2;
        newheight = height / 2;
        if (!myswap_bytes) {
            for (i = 0; i < newheight; i++) {
                for (j = 0; j < newwidth; j++) {
                    for (k = 0; k < components; k++) {
                        datain.position(t);
                        temp = datain.getShort();
                        datain.position(t + group_size);
                        temp += datain.getShort();
                        datain.position(t + ysize);
                        temp += datain.getShort();
                        datain.position(t + ysize + group_size);
                        temp += datain.getShort();
                        temp += 2;
                        temp /= 4;
                        dataout.put((short) temp);
                        t += element_size;
                    }
                    t += group_size;
                }
                t += ysize;
            }
        } else {
            for (i = 0; i < newheight; i++) {
                for (j = 0; j < newwidth; j++) {
                    for (k = 0; k < components; k++) {
                        short b;
                        int buf;
                        datain.position(t);
                        temp = Mipmap.GLU_SWAP_2_BYTES(datain.getShort());
                        datain.position(t + group_size);
                        temp += Mipmap.GLU_SWAP_2_BYTES(datain.getShort());
                        datain.position(t + ysize);
                        temp += Mipmap.GLU_SWAP_2_BYTES(datain.getShort());
                        datain.position(t + ysize + group_size);
                        temp += Mipmap.GLU_SWAP_2_BYTES(datain.getShort());
                        temp += 2;
                        temp /= 4;
                        dataout.put(temp);
                        t += element_size;
                    }
                    t += group_size;
                }
                t += ysize;
            }
        }
    }

    public static void halve1Dimage_short(int components, int width, int height, ByteBuffer datain, ShortBuffer dataout, int element_size, int ysize, int group_size, boolean myswap_bytes) {
        int halfWidth = width / 2;
        int halfHeight = height / 2;
        int src = 0;
        int dest = 0;
        int jj;
        assert (width == 1 || height == 1);
        assert (width != height);
        if (height == 1) {
            assert (width != 1);
            halfHeight = 1;
            for (jj = 0; jj < halfWidth; jj++) {
                int kk;
                for (kk = 0; kk < components; kk++) {
                    short[] sshort = new short[BOX2];
                    if (myswap_bytes) {
                        datain.position(src);
                        sshort[0] = Mipmap.GLU_SWAP_2_BYTES(datain.getShort());
                        datain.position(src + group_size);
                        sshort[1] = Mipmap.GLU_SWAP_2_BYTES(datain.getShort());
                    } else {
                        datain.position(src);
                        sshort[0] = datain.getShort();
                        datain.position(src + group_size);
                        sshort[1] = datain.getShort();
                    }
                    dataout.put((short) ((sshort[0] + sshort[1]) / 2));
                    src += element_size;
                    dest += 2;
                }
                src += group_size;
            }
            int padBytes = ysize - (width * group_size);
            src += padBytes;
        } else if (width == 1) {
            int padBytes = ysize - (width * group_size);
            assert (height != 1);
            halfWidth = 1;
            for (jj = 0; jj < halfHeight; jj++) {
                int kk;
                for (kk = 0; kk < components; kk++) {
                    short[] sshort = new short[BOX2];
                    if (myswap_bytes) {
                        datain.position(src);
                        sshort[0] = Mipmap.GLU_SWAP_2_BYTES(datain.getShort());
                        datain.position(src + ysize);
                        sshort[1] = Mipmap.GLU_SWAP_2_BYTES(datain.getShort());
                    } else {
                        datain.position(src);
                        sshort[0] = datain.getShort();
                        datain.position(src + ysize);
                        sshort[1] = datain.getShort();
                    }
                    dataout.put((short) ((sshort[0] + sshort[1]) / 2));
                    src += element_size;
                    dest += 2;
                }
                src += padBytes;
                src += ysize;
            }
            assert (src == ysize * height);
        }
        assert (dest == (components * element_size * halfWidth * halfHeight));
    }

    public static void halveImage_uint(int components, int width, int height, ByteBuffer datain, IntBuffer dataout, int element_size, int ysize, int group_size, boolean myswap_bytes) {
        int i, j, k, l;
        int newwidth, newheight;
        int s = 0;
        int t = 0;
        double temp = 0;
        if (width == 1 || height == 1) {
            assert (!(width == 1 && height == 1));
            halve1Dimage_uint(components, width, height, datain, dataout, element_size, ysize, group_size, myswap_bytes);
            return;
        }
        newwidth = width / 2;
        newheight = height / 2;
        if (!myswap_bytes) {
            for (i = 0; i < newheight; i++) {
                for (j = 0; j < newwidth; j++) {
                    for (k = 0; k < components; k++) {
                        datain.position(t);
                        temp = (0x000000007FFFFFFFL & datain.getInt());
                        datain.position(t + group_size);
                        temp += (0x000000007FFFFFFFL & datain.getInt());
                        datain.position(t + ysize);
                        temp += (0x000000007FFFFFFFL & datain.getInt());
                        datain.position(t + ysize + group_size);
                        temp += (0x000000007FFFFFFFL & datain.getInt());
                        dataout.put((int) ((temp / 4) + 0.5));
                        t += element_size;
                    }
                    t += group_size;
                }
                t += ysize;
            }
        } else {
            for (i = 0; i < newheight; i++) {
                for (j = 0; j < newwidth; j++) {
                    for (k = 0; k < components; k++) {
                        double buf;
                        datain.position(t);
                        buf = (0x00000000FFFFFFFF & Mipmap.GLU_SWAP_4_BYTES(datain.getInt()));
                        datain.position(t + group_size);
                        buf += (0x00000000FFFFFFFF & Mipmap.GLU_SWAP_4_BYTES(datain.getInt()));
                        datain.position(t + ysize);
                        buf += (0x00000000FFFFFFFF & Mipmap.GLU_SWAP_4_BYTES(datain.getInt()));
                        datain.position(t + ysize + group_size);
                        buf += (0x00000000FFFFFFFF & Mipmap.GLU_SWAP_4_BYTES(datain.getInt()));
                        temp /= 4;
                        temp += 0.5;
                        dataout.put((int) temp);
                        t += element_size;
                    }
                    t += group_size;
                }
                t += ysize;
            }
        }
    }

    public static void halve1Dimage_uint(int components, int width, int height, ByteBuffer datain, IntBuffer dataout, int element_size, int ysize, int group_size, boolean myswap_bytes) {
        int halfWidth = width / 2;
        int halfHeight = height / 2;
        int src = 0;
        int dest = 0;
        int jj;
        assert (width == 1 || height == 1);
        assert (width != height);
        if (height == 1) {
            assert (width != 1);
            halfHeight = 1;
            for (jj = 0; jj < halfWidth; jj++) {
                int kk;
                for (kk = 0; kk < halfHeight; kk++) {
                    long[] uint = new long[BOX2];
                    if (myswap_bytes) {
                        datain.position(src);
                        uint[0] = (0x00000000FFFFFFFF & Mipmap.GLU_SWAP_4_BYTES(datain.getInt()));
                        datain.position(src + group_size);
                        uint[1] = (0x00000000FFFFFFFF & Mipmap.GLU_SWAP_4_BYTES(datain.getInt()));
                    } else {
                        datain.position(src);
                        uint[0] = (0x00000000FFFFFFFF & datain.getInt());
                        datain.position(src + group_size);
                        uint[1] = (0x00000000FFFFFFFF & datain.getInt());
                    }
                    dataout.put((int) ((uint[0] + uint[1]) / 2.0));
                    src += element_size;
                    dest += 4;
                }
                src += group_size;
            }
            int padBytes = ysize - (width * group_size);
            src += padBytes;
        } else if (width == 1) {
            int padBytes = ysize - (width * group_size);
            assert (height != 1);
            halfWidth = 1;
            for (jj = 0; jj < halfHeight; jj++) {
                int kk;
                for (kk = 0; kk < components; kk++) {
                    long[] uint = new long[BOX2];
                    if (myswap_bytes) {
                        datain.position(src);
                        uint[0] = (0x00000000FFFFFFFF & Mipmap.GLU_SWAP_4_BYTES(datain.getInt()));
                        datain.position(src + group_size);
                        uint[0] = (0x00000000FFFFFFFF & Mipmap.GLU_SWAP_4_BYTES(datain.getInt()));
                    } else {
                        datain.position(src);
                        uint[0] = (0x00000000FFFFFFFF & datain.getInt());
                        datain.position(src + ysize);
                        uint[1] = (0x00000000FFFFFFFF & datain.getInt());
                    }
                    dataout.put((int) ((uint[0] + uint[1]) / 2.0));
                    src += element_size;
                    dest += 4;
                }
                src += padBytes;
                src += ysize;
            }
            assert (src == ysize * height);
        }
        assert (dest == components * element_size * halfWidth * halfHeight);
    }

    public static void halveImage_int(int components, int width, int height, ByteBuffer datain, IntBuffer dataout, int element_size, int ysize, int group_size, boolean myswap_bytes) {
        int i, j, k, l;
        int newwidth, newheight;
        int s = 0;
        int t = 0;
        int temp = 0;
        if (width == 1 || height == 1) {
            assert (!(width == 1 && height == 1));
            halve1Dimage_int(components, width, height, datain, dataout, element_size, ysize, group_size, myswap_bytes);
            return;
        }
        newwidth = width / 2;
        newheight = height / 2;
        if (!myswap_bytes) {
            for (i = 0; i < newheight; i++) {
                for (j = 0; j < newwidth; j++) {
                    for (k = 0; k < components; k++) {
                        datain.position(t);
                        temp = datain.getInt();
                        datain.position(t + group_size);
                        temp += datain.getInt();
                        datain.position(t + ysize);
                        temp += datain.getInt();
                        datain.position(t + ysize + group_size);
                        temp += datain.getInt();
                        temp = (int) ((temp / 4.0f) + 0.5f);
                        dataout.put(temp);
                        t += element_size;
                    }
                    t += group_size;
                }
                t += ysize;
            }
        } else {
            for (i = 0; i < newheight; i++) {
                for (j = 0; j < newwidth; j++) {
                    for (k = 0; k < components; k++) {
                        long b;
                        float buf;
                        datain.position(t);
                        b = (0x00000000FFFFFFFF & Mipmap.GLU_SWAP_4_BYTES(datain.getInt()));
                        buf = b;
                        datain.position(t + group_size);
                        b = (0x00000000FFFFFFFF & Mipmap.GLU_SWAP_4_BYTES(datain.getInt()));
                        buf += b;
                        datain.position(t + ysize);
                        b = (0x00000000FFFFFFFF & Mipmap.GLU_SWAP_4_BYTES(datain.getInt()));
                        buf += b;
                        datain.position(t + ysize + group_size);
                        b = (0x00000000FFFFFFFF & Mipmap.GLU_SWAP_4_BYTES(datain.getInt()));
                        buf += b;
                        dataout.put((int) ((buf / 4.0f) + 0.5f));
                        t += element_size;
                    }
                    t += group_size;
                }
                t += ysize;
            }
        }
    }

    public static void halve1Dimage_int(int components, int width, int height, ByteBuffer datain, IntBuffer dataout, int element_size, int ysize, int group_size, boolean myswap_bytes) {
        int halfWidth = width / 2;
        int halfHeight = height / 2;
        int src = 0;
        int dest = 0;
        int jj;
        assert (width == 1 || height == 1);
        assert (width != height);
        if (height == 1) {
            assert (width != 1);
            halfHeight = 1;
            for (jj = 0; jj < halfWidth; jj++) {
                int kk;
                for (kk = 0; kk < components; kk++) {
                    long[] uint = new long[BOX2];
                    if (myswap_bytes) {
                        datain.position(src);
                        uint[0] = (0x00000000FFFFFFFF & Mipmap.GLU_SWAP_4_BYTES(datain.getInt()));
                        datain.position(src + group_size);
                        uint[1] = (0x00000000FFFFFFFF & Mipmap.GLU_SWAP_4_BYTES(datain.getInt()));
                    } else {
                        datain.position(src);
                        uint[0] = (0x00000000FFFFFFFF & datain.getInt());
                        datain.position(src + group_size);
                        uint[1] = (0x00000000FFFFFFFF & datain.getInt());
                    }
                    dataout.put((int) (((float) uint[0] + (float) uint[1]) / 2.0f));
                    src += element_size;
                    dest += 4;
                }
                src += group_size;
            }
            int padBytes = ysize - (width * group_size);
            src += padBytes;
        } else if (width == 1) {
            int padBytes = ysize - (width * group_size);
            assert (height != 1);
            halfWidth = 1;
            for (jj = 0; jj < halfHeight; jj++) {
                int kk;
                for (kk = 0; kk < components; kk++) {
                    long[] uint = new long[BOX2];
                    if (myswap_bytes) {
                        datain.position(src);
                        uint[0] = (0x00000000FFFFFFFF & Mipmap.GLU_SWAP_4_BYTES(datain.getInt()));
                        datain.position(src + ysize);
                        uint[1] = (0x00000000FFFFFFFF & Mipmap.GLU_SWAP_4_BYTES(datain.getInt()));
                    } else {
                        datain.position(src);
                        uint[0] = (0x00000000FFFFFFFF & datain.getInt());
                        datain.position(src + ysize);
                        uint[1] = (0x00000000FFFFFFFF & datain.getInt());
                    }
                    dataout.put((int) (((float) uint[0] + (float) uint[1]) / 2.0f));
                    src += element_size;
                    dest += 4;
                }
                src += padBytes;
                src += ysize;
            }
            assert (src == ysize * height);
        }
        assert (dest == (components * element_size * halfWidth * halfHeight));
    }

    public static void halveImage_float(int components, int width, int height, ByteBuffer datain, FloatBuffer dataout, int element_size, int ysize, int group_size, boolean myswap_bytes) {
        int i, j, k, l;
        int newwidth, newheight;
        int s = 0;
        int t = 0;
        float temp = 0.0f;
        if (width == 1 || height == 1) {
            assert (!(width == 1 && height == 1));
            halve1Dimage_float(components, width, height, datain, dataout, element_size, ysize, group_size, myswap_bytes);
            return;
        }
        newwidth = width / 2;
        newheight = height / 2;
        if (!myswap_bytes) {
            for (i = 0; i < newheight; i++) {
                for (j = 0; j < newwidth; j++) {
                    for (k = 0; k < components; k++) {
                        datain.position(t);
                        temp = datain.getFloat();
                        datain.position(t + group_size);
                        temp += datain.getFloat();
                        datain.position(t + ysize);
                        temp += datain.getFloat();
                        datain.position(t + ysize + group_size);
                        temp /= 4.0f;
                        dataout.put(temp);
                        t += element_size;
                    }
                    t += group_size;
                }
                t += ysize;
            }
        } else {
            for (i = 0; i < newheight; i++) {
                for (j = 0; j < newwidth; j++) {
                    for (k = 0; k < components; k++) {
                        float buf;
                        datain.position(t);
                        buf = Mipmap.GLU_SWAP_4_BYTES(datain.getFloat());
                        datain.position(t + group_size);
                        buf += Mipmap.GLU_SWAP_4_BYTES(datain.getFloat());
                        datain.position(t + ysize);
                        buf += Mipmap.GLU_SWAP_4_BYTES(datain.getFloat());
                        datain.position(t + ysize + group_size);
                        buf += Mipmap.GLU_SWAP_4_BYTES(datain.getFloat());
                        dataout.put(buf / 4.0f);
                        t += element_size;
                    }
                    t += group_size;
                }
                t += ysize;
            }
        }
    }

    public static void halve1Dimage_float(int components, int width, int height, ByteBuffer datain, FloatBuffer dataout, int element_size, int ysize, int group_size, boolean myswap_bytes) {
        int halfWidth = width / 2;
        int halfHeight = height / 2;
        int src = 0;
        int dest = 0;
        int jj;
        assert (width == 1 || height == 1);
        assert (width != height);
        if (height == 1) {
            assert (width != 1);
            halfHeight = 1;
            for (jj = 0; jj < halfWidth; jj++) {
                int kk;
                for (kk = 0; kk < components; kk++) {
                    float[] sfloat = new float[BOX2];
                    if (myswap_bytes) {
                        datain.position(src);
                        sfloat[0] = Mipmap.GLU_SWAP_4_BYTES(datain.getFloat());
                        datain.position(src + group_size);
                        sfloat[1] = Mipmap.GLU_SWAP_4_BYTES(datain.getFloat());
                    } else {
                        datain.position(src);
                        sfloat[0] = datain.getFloat();
                        datain.position(src + group_size);
                        sfloat[1] = datain.getFloat();
                    }
                    dataout.put((sfloat[0] + sfloat[1]) / 2.0f);
                    src += element_size;
                    dest += 4;
                }
                src += group_size;
            }
            int padBytes = ysize - (width * group_size);
            src += padBytes;
        } else if (width == 1) {
            int padBytes = ysize - (width * group_size);
            assert (height != 1);
            halfWidth = 1;
            for (jj = 0; jj < halfHeight; jj++) {
                int kk;
                for (kk = 0; kk < components; kk++) {
                    float[] sfloat = new float[BOX2];
                    if (myswap_bytes) {
                        datain.position(src);
                        sfloat[0] = Mipmap.GLU_SWAP_4_BYTES(datain.getFloat());
                        datain.position(src + ysize);
                        sfloat[1] = Mipmap.GLU_SWAP_4_BYTES(datain.getFloat());
                    } else {
                        datain.position(src);
                        sfloat[0] = datain.getFloat();
                        datain.position(src + ysize);
                        sfloat[1] = datain.getFloat();
                    }
                    dataout.put((sfloat[0] + sfloat[1]) / 2.0f);
                    src += element_size;
                    dest += 4;
                }
                src += padBytes;
                src += ysize;
            }
            assert (src == ysize * height);
        }
        assert (dest == (components * element_size * halfWidth * halfHeight));
    }

    public static void halveImagePackedPixel(int components, Extract extract, int width, int height, ByteBuffer datain, ByteBuffer dataout, int pixelSizeInBytes, int rowSizeInBytes, boolean isSwap) {
        if (width == 1 || height == 1) {
            assert (!(width == 1 && height == 1));
            halve1DimagePackedPixel(components, extract, width, height, datain, dataout, pixelSizeInBytes, rowSizeInBytes, isSwap);
            return;
        }
        int ii, jj;
        int halfWidth = width / 2;
        int halfHeight = height / 2;
        int src = 0;
        int padBytes = rowSizeInBytes - (width * pixelSizeInBytes);
        int outIndex = 0;
        for (ii = 0; ii < halfHeight; ii++) {
            for (jj = 0; jj < halfWidth; jj++) {
                float totals[] = new float[4];
                float extractTotals[][] = new float[BOX4][4];
                int cc;
                datain.position(src);
                extract.extract(isSwap, datain, extractTotals[0]);
                datain.position(src + pixelSizeInBytes);
                extract.extract(isSwap, datain, extractTotals[1]);
                datain.position(src + rowSizeInBytes);
                extract.extract(isSwap, datain, extractTotals[2]);
                datain.position(src + rowSizeInBytes + pixelSizeInBytes);
                extract.extract(isSwap, datain, extractTotals[3]);
                for (cc = 0; cc < components; cc++) {
                    int kk = 0;
                    totals[cc] = 0.0f;
                    for (kk = 0; kk < BOX4; kk++) {
                        totals[cc] += extractTotals[kk][cc];
                    }
                    totals[cc] /= BOX4;
                }
                extract.shove(totals, outIndex, dataout);
                outIndex++;
                src += pixelSizeInBytes + pixelSizeInBytes;
            }
            src += padBytes;
            src += rowSizeInBytes;
        }
        assert (src == rowSizeInBytes * height);
        assert (outIndex == halfWidth * halfHeight);
    }

    public static void halve1DimagePackedPixel(int components, Extract extract, int width, int height, ByteBuffer datain, ByteBuffer dataout, int pixelSizeInBytes, int rowSizeInBytes, boolean isSwap) {
        int halfWidth = width / 2;
        int halfHeight = height / 2;
        int src = 0;
        int jj;
        assert (width == 1 || height == 1);
        assert (width != height);
        if (height == 1) {
            int outIndex = 0;
            assert (width != 1);
            halfHeight = 1;
            for (jj = 0; jj < halfWidth; jj++) {
                float[] totals = new float[4];
                float[][] extractTotals = new float[BOX2][4];
                int cc;
                datain.position(src);
                extract.extract(isSwap, datain, extractTotals[0]);
                datain.position(src + pixelSizeInBytes);
                extract.extract(isSwap, datain, extractTotals[1]);
                for (cc = 0; cc < components; cc++) {
                    int kk = 0;
                    totals[cc] = 0.0f;
                    for (kk = 0; kk < BOX2; kk++) {
                        totals[cc] += extractTotals[kk][cc];
                    }
                    totals[cc] /= BOX2;
                }
                extract.shove(totals, outIndex, dataout);
                outIndex++;
                src += pixelSizeInBytes + pixelSizeInBytes;
            }
            int padBytes = rowSizeInBytes - (width * pixelSizeInBytes);
            src += padBytes;
            assert (src == rowSizeInBytes);
            assert (outIndex == halfWidth * halfHeight);
        } else if (width == 1) {
            int outIndex = 0;
            assert (height != 1);
            halfWidth = 1;
            for (jj = 0; jj < halfHeight; jj++) {
                float[] totals = new float[4];
                float[][] extractTotals = new float[BOX2][4];
                int cc;
                datain.position(src);
                extract.extract(isSwap, datain, extractTotals[0]);
                datain.position(src + rowSizeInBytes);
                extract.extract(isSwap, datain, extractTotals[1]);
                for (cc = 0; cc < components; cc++) {
                    int kk = 0;
                    totals[cc] = 0.0f;
                    for (kk = 0; kk < BOX2; kk++) {
                        totals[cc] += extractTotals[kk][cc];
                    }
                    totals[cc] /= BOX2;
                }
                extract.shove(totals, outIndex, dataout);
                outIndex++;
                src += rowSizeInBytes + rowSizeInBytes;
            }
            assert (src == rowSizeInBytes);
            assert (outIndex == halfWidth * halfHeight);
        }
    }

    public static void halveImagePackedPixelSlice(int components, Extract extract, int width, int height, int depth, ByteBuffer dataIn, ByteBuffer dataOut, int pixelSizeInBytes, int rowSizeInBytes, int imageSizeInBytes, boolean isSwap) {
        int ii, jj;
        int halfWidth = width / 2;
        int halfHeight = height / 2;
        int halfDepth = depth / 2;
        int src = 0;
        int padBytes = rowSizeInBytes - (width * pixelSizeInBytes);
        int outIndex = 0;
        assert ((width == 1 || height == 1) && depth >= 2);
        if (width == height) {
            assert (width == 1 && height == 1);
            assert (depth >= 2);
            for (ii = 0; ii < halfDepth; ii++) {
                float totals[] = new float[4];
                float extractTotals[][] = new float[BOX2][4];
                int cc;
                dataIn.position(src);
                extract.extract(isSwap, dataIn, extractTotals[0]);
                dataIn.position(src + imageSizeInBytes);
                extract.extract(isSwap, dataIn, extractTotals[1]);
                for (cc = 0; cc < components; cc++) {
                    int kk;
                    totals[cc] = 0.0f;
                    for (kk = 0; kk < BOX2; kk++) {
                        totals[cc] += extractTotals[kk][cc];
                    }
                    totals[cc] /= BOX2;
                }
                extract.shove(totals, outIndex, dataOut);
                outIndex++;
                src += imageSizeInBytes + imageSizeInBytes;
            }
        } else if (height == 1) {
            assert (width != 1);
            for (ii = 0; ii < halfDepth; ii++) {
                for (jj = 0; jj < halfWidth; jj++) {
                    float totals[] = new float[4];
                    float extractTotals[][] = new float[BOX4][4];
                    int cc;
                    dataIn.position(src);
                    extract.extract(isSwap, dataIn, extractTotals[0]);
                    dataIn.position(src + pixelSizeInBytes);
                    extract.extract(isSwap, dataIn, extractTotals[1]);
                    dataIn.position(src + imageSizeInBytes);
                    extract.extract(isSwap, dataIn, extractTotals[2]);
                    dataIn.position(src + pixelSizeInBytes + imageSizeInBytes);
                    extract.extract(isSwap, dataIn, extractTotals[3]);
                    for (cc = 0; cc < components; cc++) {
                        int kk;
                        totals[cc] = 0.0f;
                        for (kk = 0; kk < BOX4; kk++) {
                            totals[cc] += extractTotals[kk][cc];
                        }
                        totals[cc] /= (float) BOX4;
                    }
                    extract.shove(totals, outIndex, dataOut);
                    outIndex++;
                    src += imageSizeInBytes + imageSizeInBytes;
                }
            }
        } else if (width == 1) {
            assert (height != 1);
            for (ii = 0; ii < halfDepth; ii++) {
                for (jj = 0; jj < halfWidth; jj++) {
                    float totals[] = new float[4];
                    float extractTotals[][] = new float[BOX4][4];
                    int cc;
                    dataIn.position(src);
                    extract.extract(isSwap, dataIn, extractTotals[0]);
                    dataIn.position(src + rowSizeInBytes);
                    extract.extract(isSwap, dataIn, extractTotals[1]);
                    dataIn.position(src + imageSizeInBytes);
                    extract.extract(isSwap, dataIn, extractTotals[2]);
                    dataIn.position(src + rowSizeInBytes + imageSizeInBytes);
                    extract.extract(isSwap, dataIn, extractTotals[3]);
                    for (cc = 0; cc < components; cc++) {
                        int kk;
                        totals[cc] = 0.0f;
                        for (kk = 0; kk < BOX4; kk++) {
                            totals[cc] += extractTotals[kk][cc];
                        }
                        totals[cc] /= (float) BOX4;
                    }
                    extract.shove(totals, outIndex, dataOut);
                    outIndex++;
                    src += imageSizeInBytes + imageSizeInBytes;
                }
            }
        }
    }

    public static void halveImageSlice(int components, ExtractPrimitive extract, int width, int height, int depth, ByteBuffer dataIn, ByteBuffer dataOut, int elementSizeInBytes, int groupSizeInBytes, int rowSizeInBytes, int imageSizeInBytes, boolean isSwap) {
        int ii, jj;
        int halfWidth = width / 2;
        int halfHeight = height / 2;
        int halfDepth = depth / 2;
        int src = 0;
        int padBytes = rowSizeInBytes - (width * groupSizeInBytes);
        int outIndex = 0;
        assert ((width == 1 || height == 1) && depth >= 2);
        if (width == height) {
            assert (width == 1 && height == 1);
            assert (depth >= 2);
            for (ii = 0; ii < halfDepth; ii++) {
                int cc;
                for (cc = 0; cc < components; cc++) {
                    double[] totals = new double[4];
                    double[][] extractTotals = new double[BOX2][4];
                    int kk;
                    dataIn.position(src);
                    extractTotals[0][cc] = extract.extract(isSwap, dataIn);
                    dataIn.position(src + imageSizeInBytes);
                    extractTotals[1][cc] = extract.extract(isSwap, dataIn);
                    totals[cc] = 0.0f;
                    for (kk = 0; kk < BOX2; kk++) {
                        totals[cc] += extractTotals[kk][cc];
                    }
                    totals[cc] /= (double) BOX2;
                    extract.shove(totals[cc], outIndex, dataOut);
                    outIndex++;
                    src += elementSizeInBytes;
                }
                src += rowSizeInBytes;
            }
            assert (src == rowSizeInBytes * height * depth);
            assert (outIndex == halfDepth * components);
        } else if (height == 1) {
            assert (width != 1);
            for (ii = 0; ii < halfDepth; ii++) {
                for (jj = 0; jj < halfWidth; jj++) {
                    int cc;
                    for (cc = 0; cc < components; cc++) {
                        int kk;
                        double totals[] = new double[4];
                        double extractTotals[][] = new double[BOX4][4];
                        dataIn.position(src);
                        extractTotals[0][cc] = extract.extract(isSwap, dataIn);
                        dataIn.position(src + groupSizeInBytes);
                        extractTotals[1][cc] = extract.extract(isSwap, dataIn);
                        dataIn.position(src + imageSizeInBytes);
                        extractTotals[2][cc] = extract.extract(isSwap, dataIn);
                        dataIn.position(src + imageSizeInBytes + groupSizeInBytes);
                        extractTotals[3][cc] = extract.extract(isSwap, dataIn);
                        totals[cc] = 0.0f;
                        for (kk = 0; kk < BOX4; kk++) {
                            totals[cc] += extractTotals[kk][cc];
                        }
                        totals[cc] /= (double) BOX4;
                        extract.shove(totals[cc], outIndex, dataOut);
                        outIndex++;
                        src += elementSizeInBytes;
                    }
                    src += elementSizeInBytes;
                }
                src += padBytes;
                src += rowSizeInBytes;
            }
            assert (src == rowSizeInBytes * height * depth);
            assert (outIndex == halfWidth * halfDepth * components);
        } else if (width == 1) {
            assert (height != 1);
            for (ii = 0; ii < halfDepth; ii++) {
                for (jj = 0; jj < halfHeight; jj++) {
                    int cc;
                    for (cc = 0; cc < components; cc++) {
                        int kk;
                        double totals[] = new double[4];
                        double extractTotals[][] = new double[BOX4][4];
                        dataIn.position(src);
                        extractTotals[0][cc] = extract.extract(isSwap, dataIn);
                        dataIn.position(src + rowSizeInBytes);
                        extractTotals[1][cc] = extract.extract(isSwap, dataIn);
                        dataIn.position(src + imageSizeInBytes);
                        extractTotals[2][cc] = extract.extract(isSwap, dataIn);
                        dataIn.position(src + imageSizeInBytes + groupSizeInBytes);
                        extractTotals[3][cc] = extract.extract(isSwap, dataIn);
                        totals[cc] = 0.0f;
                        for (kk = 0; kk < BOX4; kk++) {
                            totals[cc] += extractTotals[kk][cc];
                        }
                        totals[cc] /= (double) BOX4;
                        extract.shove(totals[cc], outIndex, dataOut);
                        outIndex++;
                        src += elementSizeInBytes;
                    }
                    src += padBytes;
                    src += rowSizeInBytes;
                }
                src += imageSizeInBytes;
            }
            assert (src == rowSizeInBytes * height * depth);
            assert (outIndex == halfWidth * halfDepth * components);
        }
    }

    public static void halveImage3D(int components, ExtractPrimitive extract, int width, int height, int depth, ByteBuffer dataIn, ByteBuffer dataOut, int elementSizeInBytes, int groupSizeInBytes, int rowSizeInBytes, int imageSizeInBytes, boolean isSwap) {
        assert (depth > 1);
        if (width == 1 || height == 1) {
            assert (1 <= depth);
            halveImageSlice(components, extract, width, height, depth, dataIn, dataOut, elementSizeInBytes, groupSizeInBytes, rowSizeInBytes, imageSizeInBytes, isSwap);
            return;
        }
        int ii, jj, dd;
        int halfWidth = width / 2;
        int halfHeight = height / 2;
        int halfDepth = depth / 2;
        int src = 0;
        int padBytes = rowSizeInBytes - (width * groupSizeInBytes);
        int outIndex = 0;
        for (dd = 0; dd < halfDepth; dd++) {
            for (ii = 0; ii < halfHeight; ii++) {
                for (jj = 0; jj < halfWidth; jj++) {
                    int cc;
                    for (cc = 0; cc < components; cc++) {
                        int kk;
                        double totals[] = new double[4];
                        double extractTotals[][] = new double[BOX8][4];
                        dataIn.position(src);
                        extractTotals[0][cc] = extract.extract(isSwap, dataIn);
                        dataIn.position(src + groupSizeInBytes);
                        extractTotals[1][cc] = extract.extract(isSwap, dataIn);
                        dataIn.position(src + rowSizeInBytes);
                        extractTotals[2][cc] = extract.extract(isSwap, dataIn);
                        dataIn.position(src + rowSizeInBytes + groupSizeInBytes);
                        extractTotals[3][cc] = extract.extract(isSwap, dataIn);
                        dataIn.position(src + imageSizeInBytes);
                        extractTotals[4][cc] = extract.extract(isSwap, dataIn);
                        dataIn.position(src + groupSizeInBytes + imageSizeInBytes);
                        extractTotals[5][cc] = extract.extract(isSwap, dataIn);
                        dataIn.position(src + rowSizeInBytes + imageSizeInBytes);
                        extractTotals[6][cc] = extract.extract(isSwap, dataIn);
                        dataIn.position(src + rowSizeInBytes + imageSizeInBytes + groupSizeInBytes);
                        extractTotals[7][cc] = extract.extract(isSwap, dataIn);
                        totals[cc] = 0.0f;
                        for (kk = 0; kk < BOX8; kk++) {
                            totals[cc] += extractTotals[kk][cc];
                        }
                        totals[cc] /= (double) BOX8;
                        extract.shove(totals[cc], outIndex, dataOut);
                        outIndex++;
                        src += elementSizeInBytes;
                    }
                    src += groupSizeInBytes;
                }
                src += padBytes;
                src += rowSizeInBytes;
            }
            src += imageSizeInBytes;
        }
        assert (src == rowSizeInBytes * height * depth);
        assert (outIndex == halfWidth * halfHeight * halfDepth * components);
    }

    public static void halveImagePackedPixel3D(int components, Extract extract, int width, int height, int depth, ByteBuffer dataIn, ByteBuffer dataOut, int pixelSizeInBytes, int rowSizeInBytes, int imageSizeInBytes, boolean isSwap) {
        if (depth == 1) {
            assert (1 <= width && 1 <= height);
            halveImagePackedPixel(components, extract, width, height, dataIn, dataOut, pixelSizeInBytes, rowSizeInBytes, isSwap);
            return;
        } else if (width == 1 || height == 1) {
            assert (1 <= depth);
            halveImagePackedPixelSlice(components, extract, width, height, depth, dataIn, dataOut, pixelSizeInBytes, rowSizeInBytes, imageSizeInBytes, isSwap);
            return;
        }
        int ii, jj, dd;
        int halfWidth = width / 2;
        int halfHeight = height / 2;
        int halfDepth = depth / 2;
        int src = 0;
        int padBytes = rowSizeInBytes - (width * pixelSizeInBytes);
        int outIndex = 0;
        for (dd = 0; dd < halfDepth; dd++) {
            for (ii = 0; ii < halfHeight; ii++) {
                for (jj = 0; jj < halfWidth; jj++) {
                    float totals[] = new float[4];
                    float extractTotals[][] = new float[BOX8][4];
                    int cc;
                    dataIn.position(src);
                    extract.extract(isSwap, dataIn, extractTotals[0]);
                    dataIn.position(src + pixelSizeInBytes);
                    extract.extract(isSwap, dataIn, extractTotals[1]);
                    dataIn.position(src + rowSizeInBytes);
                    extract.extract(isSwap, dataIn, extractTotals[2]);
                    dataIn.position(src + rowSizeInBytes + pixelSizeInBytes);
                    extract.extract(isSwap, dataIn, extractTotals[3]);
                    dataIn.position(src + imageSizeInBytes);
                    extract.extract(isSwap, dataIn, extractTotals[4]);
                    dataIn.position(src + pixelSizeInBytes + imageSizeInBytes);
                    extract.extract(isSwap, dataIn, extractTotals[5]);
                    dataIn.position(src + rowSizeInBytes + imageSizeInBytes);
                    extract.extract(isSwap, dataIn, extractTotals[6]);
                    dataIn.position(src + rowSizeInBytes + pixelSizeInBytes + imageSizeInBytes);
                    extract.extract(isSwap, dataIn, extractTotals[7]);
                    for (cc = 0; cc < components; cc++) {
                        int kk;
                        totals[cc] = 0.0f;
                        for (kk = 0; kk < BOX8; kk++) {
                            totals[cc] += extractTotals[kk][cc];
                        }
                        totals[cc] /= (float) BOX8;
                    }
                    extract.shove(totals, outIndex, dataOut);
                    outIndex++;
                    src += pixelSizeInBytes + pixelSizeInBytes;
                }
                src += padBytes;
                src += rowSizeInBytes;
            }
            src += imageSizeInBytes;
        }
        assert (src == rowSizeInBytes * height * depth);
        assert (outIndex == halfWidth * halfHeight * halfDepth);
    }
}
