package org.fudaa.dodico.corba.mascaret;

/**
* org/fudaa/dodico/corba/mascaret/SParamPhysTracer.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from C:/devel/fudaa/fudaa_devel/dodico/idl/code/mascaret.idl
* mercredi 14 janvier 2009 02 h 05 CET
*/
public final class SParamPhysTracer implements org.omg.CORBA.portable.IDLEntity {

    public String nomParamPhys = null;

    public double valeurParamPhys = (double) 0;

    public SParamPhysTracer() {
    }

    public SParamPhysTracer(String _nomParamPhys, double _valeurParamPhys) {
        nomParamPhys = _nomParamPhys;
        valeurParamPhys = _valeurParamPhys;
    }
}