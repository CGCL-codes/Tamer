package org.jpedal.objects.raw;

public class FDFObject extends PdfObject {

    String F = null;

    byte[] rawF;

    private byte[][] Fields;

    public FDFObject(String ref) {
        super(ref);
    }

    public FDFObject(int ref, int gen) {
        super(ref, gen);
    }

    public FDFObject(int type) {
        super(type);
    }

    public boolean getBoolean(int id) {
        switch(id) {
            default:
                return super.getBoolean(id);
        }
    }

    public void setBoolean(int id, boolean value) {
        switch(id) {
            default:
                super.setBoolean(id, value);
        }
    }

    public PdfObject getDictionary(int id) {
        switch(id) {
            default:
                return super.getDictionary(id);
        }
    }

    public void setIntNumber(int id, int value) {
        switch(id) {
            default:
                super.setIntNumber(id, value);
        }
    }

    public int getInt(int id) {
        switch(id) {
            default:
                return super.getInt(id);
        }
    }

    public void setDictionary(int id, PdfObject value) {
        value.setID(id);
        switch(id) {
            default:
                super.setDictionary(id, value);
        }
    }

    public int setConstant(int pdfKeyType, int keyStart, int keyLength, byte[] raw) {
        int PDFvalue = PdfDictionary.Unknown;
        int id = 0, x = 0, next;
        try {
            for (int i2 = keyLength - 1; i2 > -1; i2--) {
                next = raw[keyStart + i2];
                next = next - 48;
                id = id + ((next) << x);
                x = x + 8;
            }
            switch(id) {
                default:
                    PDFvalue = super.setConstant(pdfKeyType, id);
                    if (PDFvalue == -1) {
                        if (debug) {
                            byte[] bytes = new byte[keyLength];
                            System.arraycopy(raw, keyStart, bytes, 0, keyLength);
                            System.out.println("key=" + new String(bytes) + " " + id + " not implemented in setConstant in " + this);
                            System.out.println("final public static int " + new String(bytes) + "=" + id + ";");
                        }
                    }
                    break;
            }
        } catch (Exception ee) {
            ee.printStackTrace();
        }
        switch(pdfKeyType) {
            default:
                super.setConstant(pdfKeyType, id);
        }
        return PDFvalue;
    }

    public int getParameterConstant(int key) {
        switch(key) {
            default:
                return super.getParameterConstant(key);
        }
    }

    public PdfArrayIterator getMixedArray(int id) {
        switch(id) {
            default:
                return super.getMixedArray(id);
        }
    }

    public double[] getDoubleArray(int id) {
        switch(id) {
            default:
                return super.getDoubleArray(id);
        }
    }

    public void setDoubleArray(int id, double[] value) {
        switch(id) {
            default:
                super.setDoubleArray(id, value);
        }
    }

    public int[] getIntArray(int id) {
        switch(id) {
            default:
                return super.getIntArray(id);
        }
    }

    public void setIntArray(int id, int[] value) {
        switch(id) {
            default:
                super.setIntArray(id, value);
        }
    }

    public void setMixedArray(int id, byte[][] value) {
        switch(id) {
            default:
                super.setMixedArray(id, value);
        }
    }

    public float[] getFloatArray(int id) {
        switch(id) {
            default:
                return super.getFloatArray(id);
        }
    }

    public void setFloatArray(int id, float[] value) {
        switch(id) {
            default:
                super.setFloatArray(id, value);
        }
    }

    public void setName(int id, byte[] value) {
        switch(id) {
            default:
                super.setName(id, value);
        }
    }

    public void setTextStreamValue(int id, byte[] value) {
        switch(id) {
            case PdfDictionary.F:
                rawF = value;
                break;
            default:
                super.setTextStreamValue(id, value);
        }
    }

    public String getName(int id) {
        switch(id) {
            default:
                return super.getName(id);
        }
    }

    public String getTextStreamValue(int id) {
        switch(id) {
            case PdfDictionary.F:
                if (F == null && rawF != null) F = new String(rawF);
                return F;
            default:
                return super.getTextStreamValue(id);
        }
    }

    /**
     * unless you need special fucntions,
     * use getStringValue(int id) which is faster
     */
    public String getStringValue(int id, int mode) {
        byte[] data = null;
        switch(id) {
        }
        switch(mode) {
            case PdfDictionary.STANDARD:
                if (data != null) return new String(data); else return null;
            case PdfDictionary.LOWERCASE:
                if (data != null) return new String(data); else return null;
            case PdfDictionary.REMOVEPOSTSCRIPTPREFIX:
                if (data != null) {
                    int len = data.length;
                    if (len > 6 && data[6] == '+') {
                        int length = len - 7;
                        byte[] newData = new byte[length];
                        System.arraycopy(data, 7, newData, 0, length);
                        return new String(newData);
                    } else return new String(data);
                } else return null;
            default:
                throw new RuntimeException("Value not defined in getName(int,mode) in " + this);
        }
    }

    public byte[][] getKeyArray(int id) {
        switch(id) {
            case PdfDictionary.Fields:
                return Fields;
            default:
                return super.getKeyArray(id);
        }
    }

    public void setKeyArray(int id, byte[][] value) {
        switch(id) {
            case PdfDictionary.Fields:
                Fields = value;
            default:
                super.setKeyArray(id, value);
        }
    }

    public boolean decompressStreamWhenRead() {
        return false;
    }

    public int getObjectType() {
        return PdfDictionary.FDF;
    }
}
