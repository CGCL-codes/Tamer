package com.sun.opengl.impl.glu.mipmap;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import java.nio.*;

/**
 *
 * @author  Administrator
 */
public class Image {

    /** Creates a new instance of Image */
    public Image() {
    }

    public static short getShortFromByteArray(byte[] array, int index) {
        short s;
        s = (short) (array[index] << 8);
        s |= (short) (0x00FF & array[index + 1]);
        return (s);
    }

    public static int getIntFromByteArray(byte[] array, int index) {
        int i;
        i = (array[index] << 24) & 0xFF000000;
        i |= (array[index + 1] << 16) & 0x00FF0000;
        i |= (array[index + 2] << 8) & 0x0000FF00;
        i |= (array[index + 3]) & 0x000000FF;
        return (i);
    }

    public static float getFloatFromByteArray(byte[] array, int index) {
        int i = getIntFromByteArray(array, index);
        return (Float.intBitsToFloat(i));
    }

    public static void fill_image(PixelStorageModes psm, int width, int height, int format, int type, boolean index_format, ByteBuffer userdata, ShortBuffer newimage) {
        int components;
        int element_size;
        int rowsize;
        int padding;
        int groups_per_line;
        int group_size;
        int elements_per_line;
        int start;
        int iter = 0;
        int iter2;
        int i, j, k;
        boolean myswap_bytes;
        Extract extract = null;
        switch(type) {
            case (GL2.GL_UNSIGNED_BYTE_3_3_2):
                extract = new Extract332();
                break;
            case (GL2.GL_UNSIGNED_BYTE_2_3_3_REV):
                extract = new Extract233rev();
                break;
            case (GL2.GL_UNSIGNED_SHORT_5_6_5):
                extract = new Extract565();
                break;
            case (GL2.GL_UNSIGNED_SHORT_5_6_5_REV):
                extract = new Extract565rev();
                break;
            case (GL2.GL_UNSIGNED_SHORT_4_4_4_4):
                extract = new Extract4444();
                break;
            case (GL2.GL_UNSIGNED_SHORT_4_4_4_4_REV):
                extract = new Extract4444rev();
                break;
            case (GL2.GL_UNSIGNED_SHORT_5_5_5_1):
                extract = new Extract5551();
                break;
            case (GL2.GL_UNSIGNED_SHORT_1_5_5_5_REV):
                extract = new Extract1555rev();
                break;
            case (GL2.GL_UNSIGNED_INT_8_8_8_8):
                extract = new Extract8888();
                break;
            case (GL2.GL_UNSIGNED_INT_8_8_8_8_REV):
                extract = new Extract8888rev();
                break;
            case (GL2.GL_UNSIGNED_INT_10_10_10_2):
                extract = new Extract1010102();
                break;
            case (GL2.GL_UNSIGNED_INT_2_10_10_10_REV):
                extract = new Extract2101010rev();
                break;
        }
        myswap_bytes = psm.getUnpackSwapBytes();
        components = Mipmap.elements_per_group(format, type);
        if (psm.getUnpackRowLength() > 0) {
            groups_per_line = psm.getUnpackRowLength();
        } else {
            groups_per_line = width;
        }
        if (type == GL2.GL_BITMAP) {
            int bit_offset;
            int current_bit;
            rowsize = (groups_per_line * components + 7) / 8;
            padding = (rowsize % psm.getUnpackAlignment());
            if (padding != 0) {
                rowsize += psm.getUnpackAlignment() - padding;
            }
            start = psm.getUnpackSkipRows() * rowsize + (psm.getUnpackSkipPixels() * components / 8);
            elements_per_line = width * components;
            iter2 = 0;
            for (i = 0; i < height; i++) {
                iter = start;
                userdata.position(iter);
                bit_offset = (psm.getUnpackSkipPixels() * components) % 8;
                for (j = 0; j < elements_per_line; j++) {
                    if (psm.getUnpackLsbFirst()) {
                        userdata.position(iter);
                        current_bit = (userdata.get() & 0x000000FF) & (1 << bit_offset);
                    } else {
                        current_bit = (userdata.get() & 0x000000FF) & (1 << (7 - bit_offset));
                    }
                    if (current_bit != 0) {
                        if (index_format) {
                            newimage.position(iter2);
                            newimage.put((short) 1);
                        } else {
                            newimage.position(iter2);
                            newimage.put((short) 65535);
                        }
                    } else {
                        newimage.position(iter2);
                        newimage.put((short) 0);
                    }
                    bit_offset++;
                    if (bit_offset == 8) {
                        bit_offset = 0;
                        iter++;
                    }
                    iter2++;
                }
                start += rowsize;
            }
        } else {
            element_size = Mipmap.bytes_per_element(type);
            group_size = element_size * components;
            if (element_size == 1) {
                myswap_bytes = false;
            }
            rowsize = groups_per_line * group_size;
            padding = (rowsize % psm.getUnpackAlignment());
            if (padding != 0) {
                rowsize += psm.getUnpackAlignment() - padding;
            }
            start = psm.getUnpackSkipRows() * rowsize + psm.getUnpackSkipPixels() * group_size;
            elements_per_line = width * components;
            iter2 = 0;
            for (i = 0; i < height; i++) {
                iter = start;
                userdata.position(iter);
                for (j = 0; j < elements_per_line; j++) {
                    Type_Widget widget = new Type_Widget();
                    float[] extractComponents = new float[4];
                    userdata.position(iter);
                    switch(type) {
                        case (GL2.GL_UNSIGNED_BYTE_3_3_2):
                            extract.extract(false, userdata, extractComponents);
                            for (k = 0; k < 3; k++) {
                                newimage.put(iter2++, (short) (extractComponents[k] * 65535));
                            }
                            break;
                        case (GL2.GL_UNSIGNED_BYTE_2_3_3_REV):
                            extract.extract(false, userdata, extractComponents);
                            for (k = 0; k < 3; k++) {
                                newimage.put(iter2++, (short) (extractComponents[k] * 65535));
                            }
                            break;
                        case (GL2.GL_UNSIGNED_BYTE):
                            if (index_format) {
                                newimage.put(iter2++, (short) (0x000000FF & userdata.get()));
                            } else {
                                newimage.put(iter2++, (short) (0x000000FF & userdata.get() * 257));
                            }
                            break;
                        case (GL2.GL_BYTE):
                            if (index_format) {
                                newimage.put(iter2++, userdata.get());
                            } else {
                                newimage.put(iter2++, (short) (userdata.get() * 516));
                            }
                            break;
                        case (GL2.GL_UNSIGNED_SHORT_5_6_5):
                            extract.extract(myswap_bytes, userdata, extractComponents);
                            for (k = 0; k < 3; k++) {
                                newimage.put(iter2++, (short) (extractComponents[k] * 65535));
                            }
                            break;
                        case (GL2.GL_UNSIGNED_SHORT_5_6_5_REV):
                            extract.extract(myswap_bytes, userdata, extractComponents);
                            for (k = 0; k < 3; k++) {
                                newimage.put(iter2++, (short) (extractComponents[k] * 65535));
                            }
                            break;
                        case (GL2.GL_UNSIGNED_SHORT_4_4_4_4):
                            extract.extract(myswap_bytes, userdata, extractComponents);
                            for (k = 0; k < 4; k++) {
                                newimage.put(iter2++, (short) (extractComponents[k] * 65535));
                            }
                            break;
                        case (GL2.GL_UNSIGNED_SHORT_4_4_4_4_REV):
                            extract.extract(myswap_bytes, userdata, extractComponents);
                            for (k = 0; k < 4; k++) {
                                newimage.put(iter2++, (short) (extractComponents[k] * 65535));
                            }
                            break;
                        case (GL2.GL_UNSIGNED_SHORT_5_5_5_1):
                            extract.extract(myswap_bytes, userdata, extractComponents);
                            for (k = 0; k < 4; k++) {
                                newimage.put(iter2++, (short) (extractComponents[k] * 65535));
                            }
                            break;
                        case (GL2.GL_UNSIGNED_SHORT_1_5_5_5_REV):
                            extract.extract(myswap_bytes, userdata, extractComponents);
                            for (k = 0; k < 4; k++) {
                                newimage.put(iter2++, (short) (extractComponents[k] * 65535));
                            }
                            break;
                        case (GL2.GL_UNSIGNED_SHORT):
                        case (GL2.GL_SHORT):
                            if (myswap_bytes) {
                                widget.setUB1(userdata.get());
                                widget.setUB0(userdata.get());
                            } else {
                                widget.setUB0(userdata.get());
                                widget.setUB1(userdata.get());
                            }
                            if (type == GL2.GL_SHORT) {
                                if (index_format) {
                                    newimage.put(iter2++, widget.getS0());
                                } else {
                                    newimage.put(iter2++, (short) (widget.getS0() * 2));
                                }
                            } else {
                                newimage.put(iter2++, widget.getUS0());
                            }
                            break;
                        case (GL2.GL_UNSIGNED_INT_8_8_8_8):
                            extract.extract(myswap_bytes, userdata, extractComponents);
                            for (k = 0; k < 4; k++) {
                                newimage.put(iter2++, (short) (extractComponents[k] * 65535));
                            }
                            break;
                        case (GL2.GL_UNSIGNED_INT_8_8_8_8_REV):
                            extract.extract(myswap_bytes, userdata, extractComponents);
                            for (k = 0; k < 4; k++) {
                                newimage.put(iter2++, (short) (extractComponents[k] * 65535));
                            }
                            break;
                        case (GL2.GL_UNSIGNED_INT_10_10_10_2):
                            extract.extract(myswap_bytes, userdata, extractComponents);
                            for (k = 0; k < 4; k++) {
                                newimage.put(iter2++, (short) (extractComponents[k] * 65535));
                            }
                            break;
                        case (GL2.GL_UNSIGNED_INT_2_10_10_10_REV):
                            extract.extract(myswap_bytes, userdata, extractComponents);
                            for (k = 0; k < 4; k++) {
                                newimage.put(iter2++, (short) (extractComponents[k] * 65535));
                            }
                            break;
                        case (GL2.GL_INT):
                        case (GL2.GL_UNSIGNED_INT):
                        case (GL2.GL_FLOAT):
                            if (myswap_bytes) {
                                widget.setUB3(userdata.get());
                                widget.setUB2(userdata.get());
                                widget.setUB1(userdata.get());
                                widget.setUB0(userdata.get());
                            } else {
                                widget.setUB0(userdata.get());
                                widget.setUB1(userdata.get());
                                widget.setUB2(userdata.get());
                                widget.setUB3(userdata.get());
                            }
                            if (type == GL2.GL_FLOAT) {
                                if (index_format) {
                                    newimage.put(iter2++, (short) widget.getF());
                                } else {
                                    newimage.put(iter2++, (short) (widget.getF() * 65535));
                                }
                            } else if (type == GL2.GL_UNSIGNED_INT) {
                                if (index_format) {
                                    newimage.put(iter2++, (short) (widget.getUI()));
                                } else {
                                    newimage.put(iter2++, (short) (widget.getUI() >> 16));
                                }
                            } else {
                                if (index_format) {
                                    newimage.put(iter2++, (short) (widget.getI()));
                                } else {
                                    newimage.put(iter2++, (short) (widget.getI() >> 15));
                                }
                            }
                            break;
                    }
                    iter += element_size;
                }
                start += rowsize;
                iter = start;
            }
            if (!Mipmap.isTypePackedPixel(type)) {
                assert (iter2 == (width * height * components));
            } else {
                assert (iter2 == (width * height * Mipmap.elements_per_group(format, 0)));
            }
            assert (iter == (rowsize * height + psm.getUnpackSkipRows() * rowsize + psm.getUnpackSkipPixels() * group_size));
        }
    }

    public static void empty_image(PixelStorageModes psm, int width, int height, int format, int type, boolean index_format, ShortBuffer oldimage, ByteBuffer userdata) {
        int components;
        int element_size;
        int rowsize;
        int padding;
        int groups_per_line;
        int group_size;
        int elements_per_line;
        int start;
        int iter = 0;
        int iter2;
        int i, j, k;
        boolean myswap_bytes;
        Extract extract = null;
        switch(type) {
            case (GL2.GL_UNSIGNED_BYTE_3_3_2):
                extract = new Extract332();
                break;
            case (GL2.GL_UNSIGNED_BYTE_2_3_3_REV):
                extract = new Extract233rev();
                break;
            case (GL2.GL_UNSIGNED_SHORT_5_6_5):
                extract = new Extract565();
                break;
            case (GL2.GL_UNSIGNED_SHORT_5_6_5_REV):
                extract = new Extract565rev();
                break;
            case (GL2.GL_UNSIGNED_SHORT_4_4_4_4):
                extract = new Extract4444();
                break;
            case (GL2.GL_UNSIGNED_SHORT_4_4_4_4_REV):
                extract = new Extract4444rev();
                break;
            case (GL2.GL_UNSIGNED_SHORT_5_5_5_1):
                extract = new Extract5551();
                break;
            case (GL2.GL_UNSIGNED_SHORT_1_5_5_5_REV):
                extract = new Extract1555rev();
                break;
            case (GL2.GL_UNSIGNED_INT_8_8_8_8):
                extract = new Extract8888();
                break;
            case (GL2.GL_UNSIGNED_INT_8_8_8_8_REV):
                extract = new Extract8888rev();
                break;
            case (GL2.GL_UNSIGNED_INT_10_10_10_2):
                extract = new Extract1010102();
                break;
            case (GL2.GL_UNSIGNED_INT_2_10_10_10_REV):
                extract = new Extract2101010rev();
                break;
        }
        myswap_bytes = psm.getPackSwapBytes();
        components = Mipmap.elements_per_group(format, type);
        if (psm.getPackRowLength() > 0) {
            groups_per_line = psm.getPackRowLength();
        } else {
            groups_per_line = width;
        }
        if (type == GL2.GL_BITMAP) {
            int bit_offset;
            int current_bit;
            rowsize = (groups_per_line * components + 7) / 8;
            padding = (rowsize % psm.getPackAlignment());
            if (padding != 0) {
                rowsize += psm.getPackAlignment() - padding;
            }
            start = psm.getPackSkipRows() * rowsize + psm.getPackSkipPixels() * components / 8;
            elements_per_line = width * components;
            iter2 = 0;
            for (i = 0; i < height; i++) {
                iter = start;
                bit_offset = (psm.getPackSkipPixels() * components) % 8;
                for (j = 0; j < elements_per_line; j++) {
                    if (index_format) {
                        current_bit = oldimage.get(iter2) & 1;
                    } else {
                        if (oldimage.get(iter2) < 0) {
                            current_bit = 1;
                        } else {
                            current_bit = 0;
                        }
                    }
                    if (current_bit != 0) {
                        if (psm.getPackLsbFirst()) {
                            userdata.put(iter, (byte) ((userdata.get(iter) | (1 << bit_offset))));
                        } else {
                            userdata.put(iter, (byte) ((userdata.get(iter) | (7 - bit_offset))));
                        }
                    } else {
                        if (psm.getPackLsbFirst()) {
                            userdata.put(iter, (byte) ((userdata.get(iter) & ~(1 << bit_offset))));
                        } else {
                            userdata.put(iter, (byte) ((userdata.get(iter) & ~(7 - bit_offset))));
                        }
                    }
                    bit_offset++;
                    if (bit_offset == 8) {
                        bit_offset = 0;
                        iter++;
                    }
                    iter2++;
                }
                start += rowsize;
            }
        } else {
            float shoveComponents[] = new float[4];
            element_size = Mipmap.bytes_per_element(type);
            group_size = element_size * components;
            if (element_size == 1) {
                myswap_bytes = false;
            }
            rowsize = groups_per_line * group_size;
            padding = (rowsize % psm.getPackAlignment());
            if (padding != 0) {
                rowsize += psm.getPackAlignment() - padding;
            }
            start = psm.getPackSkipRows() * rowsize + psm.getPackSkipPixels() * group_size;
            elements_per_line = width * components;
            iter2 = 0;
            for (i = 0; i < height; i++) {
                iter = start;
                for (j = 0; j < elements_per_line; j++) {
                    Type_Widget widget = new Type_Widget();
                    switch(type) {
                        case (GL2.GL_UNSIGNED_BYTE_3_3_2):
                            for (k = 0; k < 3; k++) {
                                shoveComponents[k] = oldimage.get(iter2++) / 65535.0f;
                            }
                            extract.shove(shoveComponents, 0, userdata);
                            break;
                        case (GL2.GL_UNSIGNED_BYTE_2_3_3_REV):
                            for (k = 0; k < 3; k++) {
                                shoveComponents[k] = oldimage.get(iter2++) / 65535.0f;
                            }
                            extract.shove(shoveComponents, 0, userdata);
                            break;
                        case (GL2.GL_UNSIGNED_BYTE):
                            if (index_format) {
                                userdata.put(iter, (byte) oldimage.get(iter2++));
                            } else {
                                userdata.put(iter, (byte) (oldimage.get(iter2++)));
                            }
                            break;
                        case (GL2.GL_BYTE):
                            if (index_format) {
                                userdata.put(iter, (byte) oldimage.get(iter2++));
                            } else {
                                userdata.put(iter, (byte) (oldimage.get(iter2++)));
                            }
                            break;
                        case (GL2.GL_UNSIGNED_SHORT_5_6_5):
                            for (k = 0; k < 3; k++) {
                                shoveComponents[k] = oldimage.get(iter2++) / 65535.0f;
                            }
                            extract.shove(shoveComponents, 0, widget.getBuffer());
                            if (myswap_bytes) {
                                userdata.put(iter, widget.getUB1());
                                userdata.put(iter + 1, widget.getUB0());
                            } else {
                                userdata.put(iter, widget.getUB0());
                                userdata.put(iter + 1, widget.getUB1());
                            }
                            break;
                        case (GL2.GL_UNSIGNED_SHORT_5_6_5_REV):
                            for (k = 0; k < 3; k++) {
                                shoveComponents[k] = oldimage.get(iter2++) / 65535.0f;
                            }
                            extract.shove(shoveComponents, 0, widget.getBuffer());
                            if (myswap_bytes) {
                                userdata.put(iter, widget.getUB1());
                                userdata.put(iter + 1, widget.getUB0());
                            } else {
                                userdata.put(iter, widget.getUB0());
                                userdata.put(iter, widget.getUB1());
                            }
                            break;
                        case (GL2.GL_UNSIGNED_SHORT_4_4_4_4):
                            for (k = 0; k < 4; k++) {
                                shoveComponents[k] = oldimage.get(iter2++) / 65535.0f;
                            }
                            extract.shove(shoveComponents, 0, widget.getBuffer());
                            if (myswap_bytes) {
                                userdata.put(iter, widget.getUB1());
                                userdata.put(iter + 1, widget.getUB0());
                            } else {
                                userdata.put(iter, widget.getUB0());
                                userdata.put(iter + 1, widget.getUB1());
                            }
                            break;
                        case (GL2.GL_UNSIGNED_SHORT_4_4_4_4_REV):
                            for (k = 0; k < 4; k++) {
                                shoveComponents[k] = oldimage.get(iter2++) / 65535.0f;
                            }
                            extract.shove(shoveComponents, 0, widget.getBuffer());
                            if (myswap_bytes) {
                                userdata.put(iter, widget.getUB1());
                                userdata.put(iter + 1, widget.getUB0());
                            } else {
                                userdata.put(iter, widget.getUB0());
                                userdata.put(iter + 1, widget.getUB1());
                            }
                            break;
                        case (GL2.GL_UNSIGNED_SHORT_5_5_5_1):
                            for (k = 0; k < 4; k++) {
                                shoveComponents[k] = oldimage.get(iter2++) / 65535.0f;
                            }
                            extract.shove(shoveComponents, 0, widget.getBuffer());
                            if (myswap_bytes) {
                                userdata.put(iter, widget.getUB1());
                                userdata.put(iter + 1, widget.getUB0());
                            } else {
                                userdata.put(iter, widget.getUB0());
                                userdata.put(iter + 1, widget.getUB1());
                            }
                            break;
                        case (GL2.GL_UNSIGNED_SHORT_1_5_5_5_REV):
                            for (k = 0; k < 4; k++) {
                                shoveComponents[k] = oldimage.get(iter2++) / 65535.0f;
                            }
                            extract.shove(shoveComponents, 0, widget.getBuffer());
                            if (myswap_bytes) {
                                userdata.put(iter, widget.getUB1());
                                userdata.put(iter + 1, widget.getUB0());
                            } else {
                                userdata.put(iter, widget.getUB0());
                                userdata.put(iter + 1, widget.getUB1());
                            }
                            break;
                        case (GL2.GL_UNSIGNED_SHORT):
                        case (GL2.GL_SHORT):
                            if (type == GL2.GL_SHORT) {
                                if (index_format) {
                                    widget.setS0(oldimage.get(iter2++));
                                } else {
                                    widget.setS0((short) (oldimage.get(iter2++) >> 1));
                                }
                            } else {
                                widget.setUS0(oldimage.get(iter2++));
                            }
                            if (myswap_bytes) {
                                userdata.put(iter, widget.getUB1());
                                userdata.put(iter + 1, widget.getUB0());
                            } else {
                                userdata.put(iter, widget.getUB0());
                                userdata.put(iter + 1, widget.getUB1());
                            }
                            break;
                        case (GL2.GL_UNSIGNED_INT_8_8_8_8):
                            for (k = 0; k < 4; k++) {
                                shoveComponents[k] = oldimage.get(iter2++) / 65535.0f;
                            }
                            extract.shove(shoveComponents, 0, widget.getBuffer());
                            if (myswap_bytes) {
                                userdata.put(iter + 3, widget.getUB0());
                                userdata.put(iter + 2, widget.getUB1());
                                userdata.put(iter + 1, widget.getUB2());
                                userdata.put(iter, widget.getUB3());
                            } else {
                                userdata.putInt(iter, widget.getUI());
                            }
                            break;
                        case (GL2.GL_UNSIGNED_INT_8_8_8_8_REV):
                            for (k = 0; k < 4; k++) {
                                shoveComponents[k] = oldimage.get(iter2++) / 65535.0f;
                            }
                            extract.shove(shoveComponents, 0, widget.getBuffer());
                            if (myswap_bytes) {
                                userdata.put(iter + 3, widget.getUB0());
                                userdata.put(iter + 2, widget.getUB1());
                                userdata.put(iter + 2, widget.getUB2());
                                userdata.put(iter, widget.getUB3());
                            } else {
                                userdata.putInt(iter, widget.getUI());
                            }
                            break;
                        case (GL2.GL_UNSIGNED_INT_10_10_10_2):
                            for (k = 0; k < 4; k++) {
                                shoveComponents[k] = oldimage.get(iter2++) / 65535.0f;
                            }
                            extract.shove(shoveComponents, 0, widget.getBuffer());
                            if (myswap_bytes) {
                                userdata.put(iter + 3, widget.getUB0());
                                userdata.put(iter + 2, widget.getUB1());
                                userdata.put(iter + 1, widget.getUB2());
                                userdata.put(iter, widget.getUB3());
                            } else {
                                userdata.putInt(iter, widget.getUI());
                            }
                            break;
                        case (GL2.GL_UNSIGNED_INT_2_10_10_10_REV):
                            for (k = 0; k < 4; k++) {
                                shoveComponents[k] = oldimage.get(iter2++) / 65535.0f;
                            }
                            extract.shove(shoveComponents, 0, widget.getBuffer());
                            if (myswap_bytes) {
                                userdata.put(iter + 3, widget.getUB0());
                                userdata.put(iter + 2, widget.getUB1());
                                userdata.put(iter + 1, widget.getUB2());
                                userdata.put(iter, widget.getUB3());
                            } else {
                                userdata.putInt(iter, widget.getUI());
                            }
                            break;
                        case (GL2.GL_INT):
                        case (GL2.GL_UNSIGNED_INT):
                        case (GL2.GL_FLOAT):
                            if (type == GL2.GL_FLOAT) {
                                if (index_format) {
                                    widget.setF(oldimage.get(iter2++));
                                } else {
                                    widget.setF(oldimage.get(iter2++) / 65535.0f);
                                }
                            } else if (type == GL2.GL_UNSIGNED_INT) {
                                if (index_format) {
                                    widget.setUI(oldimage.get(iter2++));
                                } else {
                                    widget.setUI(oldimage.get(iter2++) * 65537);
                                }
                            } else {
                                if (index_format) {
                                    widget.setI(oldimage.get(iter2++));
                                } else {
                                    widget.setI((oldimage.get(iter2++) * 65537) / 2);
                                }
                            }
                            if (myswap_bytes) {
                                userdata.put(iter + 3, widget.getUB0());
                                userdata.put(iter + 2, widget.getUB1());
                                userdata.put(iter + 1, widget.getUB2());
                                userdata.put(iter, widget.getUB3());
                            } else {
                                userdata.put(iter, widget.getUB0());
                                userdata.put(iter + 1, widget.getUB1());
                                userdata.put(iter + 2, widget.getUB2());
                                userdata.put(iter + 3, widget.getUB3());
                            }
                            break;
                    }
                    iter += element_size;
                }
                start += rowsize;
                iter = start;
            }
            if (!Mipmap.isTypePackedPixel(type)) {
                assert (iter2 == width * height * components);
            } else {
                assert (iter2 == width * height * Mipmap.elements_per_group(format, 0));
            }
            assert (iter == rowsize * height + psm.getPackSkipRows() * rowsize + psm.getPackSkipPixels() * group_size);
        }
    }

    public static void fillImage3D(PixelStorageModes psm, int width, int height, int depth, int format, int type, boolean indexFormat, ByteBuffer userImage, ShortBuffer newImage) {
        boolean myswapBytes;
        int components;
        int groupsPerLine;
        int elementSize;
        int groupSize;
        int rowSize;
        int padding;
        int elementsPerLine;
        int rowsPerImage;
        int imageSize;
        int start, rowStart;
        int iter = 0;
        int iter2 = 0;
        int ww, hh, dd, k;
        Type_Widget widget = new Type_Widget();
        float extractComponents[] = new float[4];
        Extract extract = null;
        switch(type) {
            case (GL2.GL_UNSIGNED_BYTE_3_3_2):
                extract = new Extract332();
                break;
            case (GL2.GL_UNSIGNED_BYTE_2_3_3_REV):
                extract = new Extract233rev();
                break;
            case (GL2.GL_UNSIGNED_SHORT_5_6_5):
                extract = new Extract565();
                break;
            case (GL2.GL_UNSIGNED_SHORT_5_6_5_REV):
                extract = new Extract565rev();
                break;
            case (GL2.GL_UNSIGNED_SHORT_4_4_4_4):
                extract = new Extract4444();
                break;
            case (GL2.GL_UNSIGNED_SHORT_4_4_4_4_REV):
                extract = new Extract4444rev();
                break;
            case (GL2.GL_UNSIGNED_SHORT_5_5_5_1):
                extract = new Extract5551();
                break;
            case (GL2.GL_UNSIGNED_SHORT_1_5_5_5_REV):
                extract = new Extract1555rev();
                break;
            case (GL2.GL_UNSIGNED_INT_8_8_8_8):
                extract = new Extract8888();
                break;
            case (GL2.GL_UNSIGNED_INT_8_8_8_8_REV):
                extract = new Extract8888rev();
                break;
            case (GL2.GL_UNSIGNED_INT_10_10_10_2):
                extract = new Extract1010102();
                break;
            case (GL2.GL_UNSIGNED_INT_2_10_10_10_REV):
                extract = new Extract2101010rev();
                break;
        }
        myswapBytes = psm.getUnpackSwapBytes();
        components = Mipmap.elements_per_group(format, type);
        if (psm.getUnpackRowLength() > 0) {
            groupsPerLine = psm.getUnpackRowLength();
        } else {
            groupsPerLine = width;
        }
        elementSize = Mipmap.bytes_per_element(type);
        groupSize = elementSize * components;
        if (elementSize == 1) {
            myswapBytes = false;
        }
        if (psm.getUnpackImageHeight() > 0) {
            rowsPerImage = psm.getUnpackImageHeight();
        } else {
            rowsPerImage = height;
        }
        rowSize = groupsPerLine * groupSize;
        padding = rowSize % psm.getUnpackAlignment();
        if (padding != 0) {
            rowSize += psm.getUnpackAlignment() - padding;
        }
        imageSize = rowsPerImage * rowSize;
        start = psm.getUnpackSkipRows() * rowSize + psm.getUnpackSkipPixels() * groupSize + psm.getUnpackSkipImages() * imageSize;
        elementsPerLine = width * components;
        iter2 = 0;
        for (dd = 0; dd < depth; dd++) {
            rowStart = start;
            for (hh = 0; hh < height; hh++) {
                iter = rowStart;
                for (ww = 0; ww < elementsPerLine; ww++) {
                    switch(type) {
                        case (GL2.GL_UNSIGNED_BYTE):
                            if (indexFormat) {
                                newImage.put(iter2++, (short) (0x000000FF & userImage.get(iter)));
                            } else {
                                newImage.put(iter2++, (short) ((0x000000FF & userImage.get(iter)) * 257));
                            }
                            break;
                        case (GL2.GL_BYTE):
                            if (indexFormat) {
                                newImage.put(iter2++, userImage.get(iter));
                            } else {
                                newImage.put(iter2++, (short) (userImage.get(iter) * 516));
                            }
                            break;
                        case (GL2.GL_UNSIGNED_BYTE_3_3_2):
                            userImage.position(iter);
                            extract.extract(false, userImage, extractComponents);
                            for (k = 0; k < 3; k++) {
                                newImage.put(iter2++, (short) (extractComponents[k] * 65535));
                            }
                            break;
                        case (GL2.GL_UNSIGNED_BYTE_2_3_3_REV):
                            userImage.position(iter);
                            extract.extract(false, userImage, extractComponents);
                            for (k = 0; k < 3; k++) {
                                newImage.put(iter2++, (short) (extractComponents[k] * 65535));
                            }
                            break;
                        case (GL2.GL_UNSIGNED_SHORT_5_6_5):
                            userImage.position(iter);
                            extract.extract(myswapBytes, userImage, extractComponents);
                            for (k = 0; k < 4; k++) {
                                newImage.put(iter2++, (short) (extractComponents[k] * 65535));
                            }
                            break;
                        case (GL2.GL_UNSIGNED_SHORT_5_6_5_REV):
                            userImage.position(iter);
                            extract.extract(myswapBytes, userImage, extractComponents);
                            for (k = 0; k < 4; k++) {
                                newImage.put(iter2++, (short) (extractComponents[k] * 65535));
                            }
                            break;
                        case (GL2.GL_UNSIGNED_SHORT_4_4_4_4):
                            userImage.position(iter);
                            extract.extract(myswapBytes, userImage, extractComponents);
                            for (k = 0; k < 4; k++) {
                                newImage.put(iter2++, (short) (extractComponents[k] * 65535));
                            }
                            break;
                        case (GL2.GL_UNSIGNED_SHORT_4_4_4_4_REV):
                            userImage.position(iter);
                            extract.extract(myswapBytes, userImage, extractComponents);
                            for (k = 0; k < 4; k++) {
                                newImage.put(iter2++, (short) (extractComponents[k] * 65535));
                            }
                            break;
                        case (GL2.GL_UNSIGNED_SHORT_5_5_5_1):
                            userImage.position(iter);
                            extract.extract(myswapBytes, userImage, extractComponents);
                            for (k = 0; k < 4; k++) {
                                newImage.put(iter2++, (short) (extractComponents[k] * 65535));
                            }
                            break;
                        case (GL2.GL_UNSIGNED_SHORT_1_5_5_5_REV):
                            userImage.position(iter);
                            extract.extract(myswapBytes, userImage, extractComponents);
                            for (k = 0; k < 4; k++) {
                                newImage.put(iter2++, (short) (extractComponents[k] * 65535));
                            }
                            break;
                        case (GL2.GL_UNSIGNED_SHORT):
                        case (GL2.GL_SHORT):
                            if (myswapBytes) {
                                widget.setUB0(userImage.get(iter + 1));
                                widget.setUB1(userImage.get(iter));
                            } else {
                                widget.setUB0(userImage.get(iter));
                                widget.setUB1(userImage.get(iter + 1));
                            }
                            if (type == GL2.GL_SHORT) {
                                if (indexFormat) {
                                    newImage.put(iter2++, widget.getUS0());
                                } else {
                                    newImage.put(iter2++, (short) (widget.getUS0() * 2));
                                }
                            } else {
                                newImage.put(iter2++, widget.getUS0());
                            }
                            break;
                        case (GL2.GL_UNSIGNED_INT_8_8_8_8):
                            userImage.position(iter);
                            extract.extract(myswapBytes, userImage, extractComponents);
                            for (k = 0; k < 4; k++) {
                                newImage.put(iter2++, (short) (extractComponents[k] * 65535));
                            }
                            break;
                        case (GL2.GL_UNSIGNED_INT_8_8_8_8_REV):
                            userImage.position(iter);
                            extract.extract(myswapBytes, userImage, extractComponents);
                            for (k = 0; k < 4; k++) {
                                newImage.put(iter2++, (short) (extractComponents[k] * 65535));
                            }
                            break;
                        case (GL2.GL_UNSIGNED_INT_10_10_10_2):
                            userImage.position(iter);
                            extract.extract(myswapBytes, userImage, extractComponents);
                            for (k = 0; k < 4; k++) {
                                newImage.put(iter2++, (short) (extractComponents[k] * 65535));
                            }
                            break;
                        case (GL2.GL_UNSIGNED_INT_2_10_10_10_REV):
                            extract.extract(myswapBytes, userImage, extractComponents);
                            for (k = 0; k < 4; k++) {
                                newImage.put(iter2++, (short) (extractComponents[k] * 65535));
                            }
                            break;
                        case (GL2.GL_INT):
                        case (GL2.GL_UNSIGNED_INT):
                        case (GL2.GL_FLOAT):
                            if (myswapBytes) {
                                widget.setUB0(userImage.get(iter + 3));
                                widget.setUB1(userImage.get(iter + 2));
                                widget.setUB2(userImage.get(iter + 1));
                                widget.setUB3(userImage.get(iter));
                            } else {
                                widget.setUB0(userImage.get(iter));
                                widget.setUB1(userImage.get(iter + 1));
                                widget.setUB2(userImage.get(iter + 2));
                                widget.setUB3(userImage.get(iter + 3));
                            }
                            if (type == GL2.GL_FLOAT) {
                                if (indexFormat) {
                                    newImage.put(iter2++, (short) widget.getF());
                                } else {
                                    newImage.put(iter2++, (short) (widget.getF() * 65535.0f));
                                }
                            } else if (type == GL2.GL_UNSIGNED_INT) {
                                if (indexFormat) {
                                    newImage.put(iter2++, (short) widget.getUI());
                                } else {
                                    newImage.put(iter2++, (short) (widget.getUI() >> 16));
                                }
                            } else {
                                if (indexFormat) {
                                    newImage.put(iter2++, (short) widget.getI());
                                } else {
                                    newImage.put(iter2++, (short) (widget.getI() >> 15));
                                }
                            }
                            break;
                        default:
                            assert (false);
                    }
                    iter += elementSize;
                }
                rowStart += rowSize;
                iter = rowStart;
            }
            start += imageSize;
        }
        if (!Mipmap.isTypePackedPixel(type)) {
            assert (iter2 == width * height * depth * components);
        } else {
            assert (iter2 == width * height * depth * Mipmap.elements_per_group(format, 0));
        }
        assert (iter == rowSize * height * depth + psm.getUnpackSkipRows() * rowSize + psm.getUnpackSkipPixels() * groupSize + psm.getUnpackSkipImages() * imageSize);
    }

    public static void emptyImage3D(PixelStorageModes psm, int width, int height, int depth, int format, int type, boolean indexFormat, ShortBuffer oldImage, ByteBuffer userImage) {
        boolean myswapBytes;
        int components;
        int groupsPerLine;
        int elementSize;
        int groupSize;
        int rowSize;
        int padding;
        int start, rowStart, iter;
        int elementsPerLine;
        int iter2;
        int ii, jj, dd, k;
        int rowsPerImage;
        int imageSize;
        Type_Widget widget = new Type_Widget();
        float[] shoveComponents = new float[4];
        Extract extract = null;
        switch(type) {
            case (GL2.GL_UNSIGNED_BYTE_3_3_2):
                extract = new Extract332();
                break;
            case (GL2.GL_UNSIGNED_BYTE_2_3_3_REV):
                extract = new Extract233rev();
                break;
            case (GL2.GL_UNSIGNED_SHORT_5_6_5):
                extract = new Extract565();
                break;
            case (GL2.GL_UNSIGNED_SHORT_5_6_5_REV):
                extract = new Extract565rev();
                break;
            case (GL2.GL_UNSIGNED_SHORT_4_4_4_4):
                extract = new Extract4444();
                break;
            case (GL2.GL_UNSIGNED_SHORT_4_4_4_4_REV):
                extract = new Extract4444rev();
                break;
            case (GL2.GL_UNSIGNED_SHORT_5_5_5_1):
                extract = new Extract5551();
                break;
            case (GL2.GL_UNSIGNED_SHORT_1_5_5_5_REV):
                extract = new Extract1555rev();
                break;
            case (GL2.GL_UNSIGNED_INT_8_8_8_8):
                extract = new Extract8888();
                break;
            case (GL2.GL_UNSIGNED_INT_8_8_8_8_REV):
                extract = new Extract8888rev();
                break;
            case (GL2.GL_UNSIGNED_INT_10_10_10_2):
                extract = new Extract1010102();
                break;
            case (GL2.GL_UNSIGNED_INT_2_10_10_10_REV):
                extract = new Extract2101010rev();
                break;
        }
        iter = 0;
        myswapBytes = psm.getPackSwapBytes();
        components = Mipmap.elements_per_group(format, type);
        if (psm.getPackRowLength() > 0) {
            groupsPerLine = psm.getPackRowLength();
        } else {
            groupsPerLine = width;
        }
        elementSize = Mipmap.bytes_per_element(type);
        groupSize = elementSize * components;
        if (elementSize == 1) {
            myswapBytes = false;
        }
        if (psm.getPackImageHeight() > 0) {
            rowsPerImage = psm.getPackImageHeight();
        } else {
            rowsPerImage = height;
        }
        rowSize = groupsPerLine * groupSize;
        padding = rowSize % psm.getPackAlignment();
        if (padding != 0) {
            rowSize += psm.getPackAlignment() - padding;
        }
        imageSize = rowsPerImage * rowSize;
        start = psm.getPackSkipRows() * rowSize + psm.getPackSkipPixels() * groupSize + psm.getPackSkipImages() * imageSize;
        elementsPerLine = width * components;
        iter2 = 0;
        for (dd = 0; dd < depth; dd++) {
            rowStart = start;
            for (ii = 0; ii < height; ii++) {
                iter = rowStart;
                for (jj = 0; jj < elementsPerLine; jj++) {
                    switch(type) {
                        case (GL2.GL_UNSIGNED_BYTE):
                            if (indexFormat) {
                                userImage.put(iter, (byte) (oldImage.get(iter2++)));
                            } else {
                                userImage.put(iter, (byte) (oldImage.get(iter2++) >> 8));
                            }
                            break;
                        case (GL2.GL_BYTE):
                            if (indexFormat) {
                                userImage.put(iter, (byte) (oldImage.get(iter2++)));
                            } else {
                                userImage.put(iter, (byte) (oldImage.get(iter2++) >> 9));
                            }
                            break;
                        case (GL2.GL_UNSIGNED_BYTE_3_3_2):
                            for (k = 0; k < 3; k++) {
                                shoveComponents[k] = oldImage.get(iter2++) / 65535.0f;
                            }
                            extract.shove(shoveComponents, 0, userImage);
                            break;
                        case (GL2.GL_UNSIGNED_BYTE_2_3_3_REV):
                            for (k = 0; k < 3; k++) {
                                shoveComponents[k] = oldImage.get(iter2++) / 65535.0f;
                            }
                            extract.shove(shoveComponents, 0, userImage);
                            break;
                        case (GL2.GL_UNSIGNED_SHORT_5_6_5):
                            for (k = 0; k < 4; k++) {
                                shoveComponents[k] = oldImage.get(iter2++) / 65535.0f;
                            }
                            extract.shove(shoveComponents, 0, widget.getBuffer());
                            if (myswapBytes) {
                                userImage.putShort(iter, widget.getUB1());
                                userImage.putShort(iter + 1, widget.getUB0());
                            } else {
                                userImage.putShort(iter, widget.getUS0());
                            }
                            break;
                        case (GL2.GL_UNSIGNED_SHORT_5_6_5_REV):
                            for (k = 0; k < 4; k++) {
                                shoveComponents[k] = oldImage.get(iter2++) / 65535.0f;
                            }
                            extract.shove(shoveComponents, 0, widget.getBuffer());
                            if (myswapBytes) {
                                userImage.put(iter, widget.getUB1());
                                userImage.put(iter + 1, widget.getUB0());
                            } else {
                                userImage.putShort(iter, widget.getUS0());
                            }
                            break;
                        case (GL2.GL_UNSIGNED_SHORT_4_4_4_4):
                            for (k = 0; k < 4; k++) {
                                shoveComponents[k] = oldImage.get(iter2++) / 65535.0f;
                            }
                            extract.shove(shoveComponents, 0, widget.getBuffer());
                            if (myswapBytes) {
                                userImage.put(iter, widget.getUB1());
                                userImage.put(iter + 1, widget.getUB0());
                            } else {
                                userImage.putShort(iter, widget.getUS0());
                            }
                            break;
                        case (GL2.GL_UNSIGNED_SHORT_4_4_4_4_REV):
                            for (k = 0; k < 4; k++) {
                                shoveComponents[k] = oldImage.get(iter2++) / 65535.0f;
                            }
                            extract.shove(shoveComponents, 0, widget.getBuffer());
                            if (myswapBytes) {
                                userImage.put(iter, widget.getUB1());
                                userImage.put(iter + 1, widget.getUB0());
                            } else {
                                userImage.putShort(iter, widget.getUS0());
                            }
                            break;
                        case (GL2.GL_UNSIGNED_SHORT_5_5_5_1):
                            for (k = 0; k < 4; k++) {
                                shoveComponents[k] = oldImage.get(iter2++) / 65535.0f;
                            }
                            extract.shove(shoveComponents, 0, widget.getBuffer());
                            if (myswapBytes) {
                                userImage.put(iter, widget.getUB1());
                                userImage.put(iter + 1, widget.getUB0());
                            } else {
                                userImage.putShort(iter, widget.getUS0());
                            }
                            break;
                        case (GL2.GL_UNSIGNED_SHORT_1_5_5_5_REV):
                            for (k = 0; k < 4; k++) {
                                shoveComponents[k] = oldImage.get(iter2++) / 65535.0f;
                            }
                            extract.shove(shoveComponents, 0, widget.getBuffer());
                            if (myswapBytes) {
                                userImage.put(iter, widget.getUB1());
                                userImage.put(iter + 1, widget.getUB0());
                            } else {
                                userImage.putShort(iter, widget.getUS0());
                            }
                            break;
                        case (GL2.GL_UNSIGNED_SHORT):
                        case (GL2.GL_SHORT):
                            if (type == GL2.GL_SHORT) {
                                if (indexFormat) {
                                    widget.setS0((short) oldImage.get(iter2++));
                                } else {
                                    widget.setS0((short) (oldImage.get(iter2++) >> 1));
                                }
                            } else {
                                widget.setUS0((short) oldImage.get(iter2++));
                            }
                            if (myswapBytes) {
                                userImage.put(iter, widget.getUB1());
                                userImage.put(iter + 1, widget.getUB0());
                            } else {
                                userImage.put(iter, widget.getUB0());
                                userImage.put(iter + 1, widget.getUB1());
                            }
                            break;
                        case (GL2.GL_UNSIGNED_INT_8_8_8_8):
                            for (k = 0; k < 4; k++) {
                                shoveComponents[k] = oldImage.get(iter2++) / 65535.0f;
                            }
                            extract.shove(shoveComponents, 0, widget.getBuffer());
                            if (myswapBytes) {
                                userImage.put(iter + 3, widget.getUB0());
                                userImage.put(iter + 2, widget.getUB1());
                                userImage.put(iter + 1, widget.getUB2());
                                userImage.put(iter, widget.getUB3());
                            } else {
                                userImage.putInt(iter, widget.getUI());
                            }
                            break;
                        case (GL2.GL_UNSIGNED_INT_8_8_8_8_REV):
                            for (k = 0; k < 4; k++) {
                                shoveComponents[k] = oldImage.get(iter2++) / 65535.0f;
                            }
                            extract.shove(shoveComponents, 0, widget.getBuffer());
                            if (myswapBytes) {
                                userImage.put(iter + 3, widget.getUB0());
                                userImage.put(iter + 2, widget.getUB1());
                                userImage.put(iter + 1, widget.getUB2());
                                userImage.put(iter, widget.getUB3());
                            } else {
                                userImage.putInt(iter, widget.getUI());
                            }
                            break;
                        case (GL2.GL_UNSIGNED_INT_10_10_10_2):
                            for (k = 0; k < 4; k++) {
                                shoveComponents[k] = oldImage.get(iter2++) / 65535.0f;
                            }
                            extract.shove(shoveComponents, 0, widget.getBuffer());
                            if (myswapBytes) {
                                userImage.put(iter + 3, widget.getUB0());
                                userImage.put(iter + 2, widget.getUB1());
                                userImage.put(iter + 1, widget.getUB2());
                                userImage.put(iter, widget.getUB3());
                            } else {
                                userImage.putInt(iter, widget.getUI());
                            }
                            break;
                        case (GL2.GL_UNSIGNED_INT_2_10_10_10_REV):
                            for (k = 0; k < 4; k++) {
                                shoveComponents[k] = oldImage.get(iter2++) / 65535.0f;
                            }
                            extract.shove(shoveComponents, 0, widget.getBuffer());
                            if (myswapBytes) {
                                userImage.put(iter + 3, widget.getUB0());
                                userImage.put(iter + 2, widget.getUB2());
                                userImage.put(iter + 1, widget.getUB1());
                                userImage.put(iter, widget.getUB0());
                            } else {
                                userImage.putInt(iter, widget.getUI());
                            }
                            break;
                        case (GL2.GL_INT):
                        case (GL2.GL_UNSIGNED_INT):
                        case (GL2.GL_FLOAT):
                            if (type == GL2.GL_FLOAT) {
                                if (indexFormat) {
                                    widget.setF(oldImage.get(iter2++));
                                } else {
                                    widget.setF(oldImage.get(iter2++) / 65535.0f);
                                }
                            } else if (type == GL2.GL_UNSIGNED_INT) {
                                if (indexFormat) {
                                    widget.setUI(oldImage.get(iter2++));
                                } else {
                                    widget.setUI(oldImage.get(iter2++) * 65537);
                                }
                            } else {
                                if (indexFormat) {
                                    widget.setI(oldImage.get(iter2++));
                                } else {
                                    widget.setI((oldImage.get(iter2++) * 65535) / 2);
                                }
                            }
                            if (myswapBytes) {
                                userImage.put(iter + 3, widget.getUB0());
                                userImage.put(iter + 2, widget.getUB1());
                                userImage.put(iter + 1, widget.getUB2());
                                userImage.put(iter, widget.getUB3());
                            } else {
                                userImage.put(iter, widget.getUB0());
                                userImage.put(iter + 1, widget.getUB1());
                                userImage.put(iter + 2, widget.getUB2());
                                userImage.put(iter + 3, widget.getUB3());
                            }
                            break;
                        default:
                            assert (false);
                    }
                    iter += elementSize;
                }
                rowStart += rowSize;
            }
            start += imageSize;
        }
        if (!Mipmap.isTypePackedPixel(type)) {
            assert (iter2 == width * height * depth * components);
        } else {
            assert (iter2 == width * height * depth * Mipmap.elements_per_group(format, 0));
        }
        assert (iter == rowSize * height * depth + psm.getUnpackSkipRows() * rowSize + psm.getUnpackSkipPixels() * groupSize + psm.getUnpackSkipImages() * imageSize);
    }
}
