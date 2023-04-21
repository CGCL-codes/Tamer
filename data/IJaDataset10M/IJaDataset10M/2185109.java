package org.fudaa.dodico.corba.planification;

/**
   * Une interface pour une segmentation temporelle.
   */
public final class ISegmentationTempsHolder implements org.omg.CORBA.portable.Streamable {

    public org.fudaa.dodico.corba.planification.ISegmentationTemps value = null;

    public ISegmentationTempsHolder() {
    }

    public ISegmentationTempsHolder(org.fudaa.dodico.corba.planification.ISegmentationTemps initialValue) {
        value = initialValue;
    }

    public void _read(org.omg.CORBA.portable.InputStream i) {
        value = org.fudaa.dodico.corba.planification.ISegmentationTempsHelper.read(i);
    }

    public void _write(org.omg.CORBA.portable.OutputStream o) {
        org.fudaa.dodico.corba.planification.ISegmentationTempsHelper.write(o, value);
    }

    public org.omg.CORBA.TypeCode _type() {
        return org.fudaa.dodico.corba.planification.ISegmentationTempsHelper.type();
    }
}
