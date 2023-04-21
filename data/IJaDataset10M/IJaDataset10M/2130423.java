package com.internetcds.jdbc.tds;

public class PacketColumnOrderResult extends PacketResult {

    public static final String cvsVersion = "$Id: PacketColumnOrderResult.java,v 1.2 2001-08-31 12:47:20 curthagenlocher Exp $";

    public PacketColumnOrderResult() {
        super(TdsDefinitions.TDS_ORDER);
    }
}
