package org.ontospread.constraints;

import java.util.Set;

public interface OntoSpreadRelationWeight {

    public static final Double DEFAULT_VALUE = 1.0;

    public static final String DEFAULT_URI = "http://ontospread.sf.net#default";

    public static final String DEFAULT_NAMESPACE = "http://ontospread.sf.net#";

    public double getWeight(String qualRelationUri);

    public void setWeight(String qualRelationUri, double value);

    public double getDefault();

    public void setDefault(double value);

    public Set<String> getRelationsUris();
}
