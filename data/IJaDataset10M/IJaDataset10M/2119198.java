package com.jplt.cobosoda;

public interface IMutation {

    public static final String VERSION = "$Revision: 1.2 $";

    public static final String ID = "$Id: IMutation.java,v 1.2 2008/04/15 03:03:48 potatono Exp $";

    public Model mutate(Model m);
}
