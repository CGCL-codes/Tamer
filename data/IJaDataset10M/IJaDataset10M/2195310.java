package org.jcryptool.crypto.flexiprovider.descriptors.meta.interfaces;

public interface IMetaPaddingScheme extends Comparable<IMetaPaddingScheme> {

    public String getClassName();

    public String getID();

    public String getPaddingSchemeName();
}
