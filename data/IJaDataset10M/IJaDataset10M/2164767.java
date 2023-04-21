package org.omg.CosTradingRepos.ServiceTypeRepositoryPackage;

/***/
public final class PropStruct implements org.omg.CORBA.portable.IDLEntity {

    public PropStruct() {
    }

    public PropStruct(String _ob_a0, org.omg.CORBA.TypeCode _ob_a1, PropertyMode _ob_a2) {
        name = _ob_a0;
        value_type = _ob_a1;
        mode = _ob_a2;
    }

    public String name;

    public org.omg.CORBA.TypeCode value_type;

    public PropertyMode mode;
}
