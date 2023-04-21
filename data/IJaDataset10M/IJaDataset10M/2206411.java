package cn.myapps.version.transfer.transferype;

import java.util.ArrayList;
import java.util.Collection;
import cn.myapps.version.transfer.ApplicationTransfer;
import cn.myapps.version.transfer.BillDefiTransfer;
import cn.myapps.version.transfer.OperationTransfer;
import cn.myapps.version.transfer.PageTransfer;
import cn.myapps.version.transfer.PermissionTransfer;
import cn.myapps.version.transfer.ResourceTransfer;
import cn.myapps.version.transfer.SummaryCfgTransfer;
import cn.myapps.version.transfer.UserDefinedTransfer;
import cn.myapps.version.transfer.ViewTransfer;

public class TransferType {

    public static String APPLICATIONTRANSFER = "cn.myapps.version.transfer.ApplicationTransfer";

    public static String RESOURCETRANSFER = "cn.myapps.version.transfer.ResourceTransfer";

    public static String VIEWTRANSFER = "cn.myapps.version.transfer.ViewTransfer";

    public static String PERMISSIONTRANSFER = "cn.myapps.version.transfer.PermissionTransfer";

    public static String PAGETRANSFER = "cn.myapps.version.transfer.PageTransfer";

    public static String BILLDEFITRANSFER = "cn.myapps.version.transfer.BillDefiTransfer";

    public static String SUMMARYCFGTRANSFER = "cn.myapps.version.transfer.SummaryCfgTransfer";

    public static String USERDEFINEDTRANSFER = "cn.myapps.version.transfer.UserDefinedTransfer";

    private static Collection<String> transferTypes = new ArrayList<String>();

    static {
        transferTypes.add(ApplicationTransfer.class.getName());
        transferTypes.add(ResourceTransfer.class.getName());
        transferTypes.add(ViewTransfer.class.getName());
        transferTypes.add(PermissionTransfer.class.getName());
        transferTypes.add(PageTransfer.class.getName());
        transferTypes.add(BillDefiTransfer.class.getName());
        transferTypes.add(SummaryCfgTransfer.class.getName());
        transferTypes.add(OperationTransfer.class.getName());
        transferTypes.add(UserDefinedTransfer.class.getName());
    }

    public static Collection<String> getAllTransferTypes() {
        return transferTypes;
    }

    public static void addTransferType(String transferType) {
        transferTypes.add(transferType);
    }
}
