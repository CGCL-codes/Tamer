package org.jpedal.objects.raw;

import org.jpedal.color.ColorSpaces;

public class ColorSpaceObject extends PdfObject {

    String Name = null;

    byte[] rawName = null;

    PdfObject alternateSpace, IndexedColorSpace, Lookup, Process, tintTransform;

    int Alternate = PdfDictionary.Unknown, colorType = PdfDictionary.Unknown;

    private byte[][] rawComponents;

    private float[] BlackPoint, Gamma, WhitePoint;

    float GammaInCalGrey = -1, N = -1;

    private int hival = -1;

    public ColorSpaceObject(String ref) {
        super(ref);
    }

    public ColorSpaceObject(int ref, int gen) {
        super(ref, gen);
    }

    public ColorSpaceObject(int ref, int gen, boolean isIndexed) {
        super(ref, gen);
        this.isIndexed = isIndexed;
    }

    public ColorSpaceObject(int type) {
        super(type);
    }

    public ColorSpaceObject(byte[] bytes) {
        super(bytes);
        colorType = PdfDictionary.getIntKey(0, bytes.length, bytes);
    }

    public byte[][] getStringArray(int id) {
        switch(id) {
            case PdfDictionary.Components:
                return deepCopy(rawComponents);
            default:
                return super.getStringArray(id);
        }
    }

    public void setStringArray(int id, byte[][] value) {
        switch(id) {
            case PdfDictionary.Components:
                rawComponents = value;
                break;
            default:
                super.setStringArray(id, value);
        }
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
            case PdfDictionary.AlternateSpace:
                return alternateSpace;
            case PdfDictionary.Indexed:
                return IndexedColorSpace;
            case PdfDictionary.Lookup:
                return Lookup;
            case PdfDictionary.Process:
                return Process;
            case PdfDictionary.tintTransform:
                return tintTransform;
            default:
                return super.getDictionary(id);
        }
    }

    public void setIntNumber(int id, int value) {
        switch(id) {
            case PdfDictionary.hival:
                hival = value;
                break;
            default:
                super.setIntNumber(id, value);
        }
    }

    public int getInt(int id) {
        switch(id) {
            case PdfDictionary.hival:
                return hival;
            default:
                return super.getInt(id);
        }
    }

    public void setDictionary(int id, PdfObject value) {
        value.setID(id);
        switch(id) {
            case PdfDictionary.AlternateSpace:
                alternateSpace = value;
                break;
            case PdfDictionary.Indexed:
                IndexedColorSpace = value;
                break;
            case PdfDictionary.Lookup:
                Lookup = value;
                break;
            case PdfDictionary.tintTransform:
                tintTransform = value;
                break;
            case PdfDictionary.Process:
                Process = value;
                break;
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
                case PdfDictionary.G:
                    PDFvalue = ColorSpaces.DeviceGray;
                    break;
                case PdfDictionary.RGB:
                    PDFvalue = ColorSpaces.DeviceRGB;
                    break;
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
            case PdfDictionary.Alternate:
                Alternate = PDFvalue;
                break;
            case PdfDictionary.ColorSpace:
                colorType = PDFvalue;
                break;
        }
        return PDFvalue;
    }

    public int getParameterConstant(int key) {
        int def = PdfDictionary.Unknown;
        switch(key) {
            case PdfDictionary.Alternate:
                return Alternate;
            case PdfDictionary.ColorSpace:
                return colorType;
            default:
                super.getParameterConstant(key);
        }
        return def;
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

    /**
     * set values (access with getArrayIterator(int id))
     */
    public void setMixedArray(int id, byte[][] value) {
        switch(id) {
            default:
                super.setMixedArray(id, value);
        }
    }

    public float[] getFloatArray(int id) {
        switch(id) {
            case PdfDictionary.BlackPoint:
                return deepCopy(BlackPoint);
            case PdfDictionary.Gamma:
                return deepCopy(Gamma);
            case PdfDictionary.WhitePoint:
                return deepCopy(WhitePoint);
            default:
                return super.getFloatArray(id);
        }
    }

    public void setFloatNumber(int id, float value) {
        switch(id) {
            case PdfDictionary.Gamma:
                GammaInCalGrey = value;
                break;
            case PdfDictionary.N:
                N = value;
                break;
            default:
                super.setFloatNumber(id, value);
        }
    }

    public float getFloatNumber(int id) {
        switch(id) {
            case PdfDictionary.Gamma:
                return GammaInCalGrey;
            case PdfDictionary.N:
                return N;
            default:
                return super.getFloatNumber(id);
        }
    }

    public void setFloatArray(int id, float[] value) {
        switch(id) {
            case PdfDictionary.BlackPoint:
                BlackPoint = value;
                break;
            case PdfDictionary.Gamma:
                Gamma = value;
                break;
            case PdfDictionary.WhitePoint:
                WhitePoint = value;
                break;
            default:
                super.setFloatArray(id, value);
        }
    }

    public void setName(int id, byte[] value) {
        switch(id) {
            case PdfDictionary.Name:
                rawName = value;
                break;
            default:
                super.setName(id, value);
        }
    }

    public byte[] getStringValueAsByte(int id) {
        switch(id) {
            case PdfDictionary.Name:
                return rawName;
            default:
                return super.getStringValueAsByte(id);
        }
    }

    public void setTextStreamValue(int id, byte[] value) {
        switch(id) {
            default:
                super.setTextStreamValue(id, value);
        }
    }

    public String getName(int id) {
        switch(id) {
            case PdfDictionary.Name:
                if (Name == null && rawName != null) Name = new String(rawName);
                return Name;
            default:
                return super.getName(id);
        }
    }

    public String getTextStreamValue(int id) {
        switch(id) {
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

    public int getObjectType() {
        return PdfDictionary.ColorSpace;
    }

    public boolean decompressStreamWhenRead() {
        return true;
    }
}
