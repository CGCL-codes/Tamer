package net.sf.RecordEditor.Avro.Test.IO;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import org.apache.avro.Schema;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericData;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.avro.util.Utf8;
import net.sf.JRecord.Common.Constants;
import net.sf.JRecord.Common.FieldDetail;
import net.sf.JRecord.Details.AbstractLine;
import net.sf.JRecord.Details.LayoutDetail;
import net.sf.JRecord.External.CopybookLoader;
import net.sf.JRecord.IO.AbstractLineReader;
import net.sf.JRecord.IO.CobolIoProvider;
import net.sf.JRecord.Numeric.Convert;
import net.sf.RecordEditor.Avro.Test.Sales6.Deptartment;
import net.sf.RecordEditor.Avro.Test.Sales6.Order;
import net.sf.RecordEditor.Avro.Test.Sales6.PaymentType;
import net.sf.RecordEditor.Avro.Test.Sales6.Product;
import net.sf.RecordEditor.Avro.Test.Sales6.SaleType;
import net.sf.RecordEditor.Avro.Test.Sales6.Store;
import net.sf.RecordEditor.Avro.Test.Sales6.Summary;

public class WriteSales6 {

    private static final int KEYCODE_IDX = 3;

    private static final int DEPARTMENT_IDX = 2;

    private static final int STORE_IDX = 1;

    private String installDir = TestConst.INPUT_DIR;

    private String salesFile = installDir + "DTAR020_Sorted.bin";

    private String salesFileOut = TestConst.OUTPUT_DIR + "protoStoreSales6.bin";

    private String copybookName = TestConst.COBOL_DIR + "DTAR020.cbl";

    private PaymentType[] payments = { PaymentType.CASH, PaymentType.CHEQUE, PaymentType.CREDIT_CARD, PaymentType.DEBIT_CARD };

    DataFileWriter<Store> out;

    Writer w;

    Store bldStore = null;

    Deptartment bldDept = null;

    Product bldProd = null;

    Order order = null;

    Store aSale = null;

    int lastStore = -1;

    int lastDept = -1;

    int lastKeycode = -1;

    int store, dept, qty, keycode;

    BigDecimal price;

    int[] sumQty = { 0, 0, 0, 0 };

    int[] sumCount = { 0, 0, 0, 0 };

    long[] sumPrice = { 0, 0, 0, 0 };

    /**
     * Example of LineReader / LineWrite classes
     */
    @SuppressWarnings("unchecked")
    private WriteSales6() {
        super();
        int lineNum = 0;
        AbstractLine<LayoutDetail> saleRecord;
        try {
            int fileStructure = Constants.IO_FIXED_LENGTH;
            CobolIoProvider ioProvider = CobolIoProvider.getInstance();
            AbstractLineReader<LayoutDetail> reader = ioProvider.getLineReader(fileStructure, Convert.FMT_MAINFRAME, CopybookLoader.SPLIT_NONE, copybookName, salesFile);
            int fldNum = 0;
            int paymentIdx = 0;
            SaleType saleType;
            FieldDetail keycodeField = reader.getLayout().getField(0, fldNum++);
            FieldDetail storeField = reader.getLayout().getField(0, fldNum++);
            FieldDetail dateField = reader.getLayout().getField(0, fldNum++);
            FieldDetail deptField = reader.getLayout().getField(0, fldNum++);
            FieldDetail qtyField = reader.getLayout().getField(0, fldNum++);
            FieldDetail salesField = reader.getLayout().getField(0, fldNum++);
            out = new DataFileWriter<Store>(new SpecificDatumWriter<Store>(Store.SCHEMA$));
            out.create(Store.SCHEMA$, new File(salesFileOut));
            while ((saleRecord = reader.read()) != null) {
                store = saleRecord.getFieldValue(storeField).asInt();
                dept = saleRecord.getFieldValue(deptField).asInt();
                keycode = saleRecord.getFieldValue(keycodeField).asInt();
                qty = saleRecord.getFieldValue(qtyField).asInt();
                price = saleRecord.getFieldValue(salesField).asBigDecimal().multiply(BigDecimal.valueOf(1000));
                if (store != lastStore) {
                    storeChanged();
                } else if (dept != lastDept) {
                    deptChanged();
                } else if (keycode != lastKeycode) {
                    keycodeChanged();
                }
                saleType = SaleType.SALE;
                if (qty == 0) {
                    saleType = SaleType.OTHER;
                } else if (qty < 0) {
                    saleType = SaleType.RETURN;
                }
                lineNum += 1;
                bldProd = new Product();
                bldProd.keycode = keycode;
                bldProd.saleDate = saleRecord.getFieldValue(dateField).asInt();
                bldProd.quantity = qty;
                bldProd.price = price.longValue();
                bldProd.saleType = saleType;
                if (paymentIdx < payments.length) {
                    bldProd.paymentType = payments[paymentIdx];
                    paymentIdx += 1;
                } else {
                    bldProd.paymentType = null;
                    paymentIdx = 0;
                }
                bldDept.product.add(bldProd);
                sumCount[KEYCODE_IDX] += 1;
                sumQty[KEYCODE_IDX] += qty;
                sumPrice[KEYCODE_IDX] += price.longValue();
                lastStore = store;
                lastDept = dept;
                lastKeycode = keycode;
            }
            storeChanged();
            reader.close();
            out.close();
            System.out.println("Written: " + lineNum);
        } catch (Exception e) {
            System.out.println("~~> " + lineNum + " " + e.getMessage());
            System.out.println();
            e.printStackTrace();
        }
    }

    private void storeChanged() throws IOException {
        deptChanged();
        if (lastStore >= 0) {
            bldStore.summary = (buildSummary(STORE_IDX));
            aSale = bldStore;
            out.append(bldStore);
        }
        bldStore = new Store();
        bldStore.department = new GenericData.Array<Deptartment>(10, Schema.createArray(Deptartment.SCHEMA$));
        bldStore.order = new GenericData.Array<Order>(10, Schema.createArray(Order.SCHEMA$));
        bldStore.store = store;
        bldStore.name = new Utf8("Store: " + store);
        inc(STORE_IDX);
    }

    private void deptChanged() {
        keycodeChanged();
        if (lastDept >= 0) {
            bldDept.summary = (buildSummary(DEPARTMENT_IDX));
            bldStore.department.add(bldDept);
        }
        bldDept = new Deptartment();
        bldDept.product = new GenericData.Array<Product>(10, Schema.createArray(Product.SCHEMA$));
        bldDept.department = (dept);
        bldDept.name = new Utf8("Department: " + dept);
        inc(DEPARTMENT_IDX);
    }

    private void keycodeChanged() {
        if (lastKeycode >= 0) {
            if (sumQty[KEYCODE_IDX] > 0) {
                order = new Order();
                order.keycode = (lastKeycode);
                order.quantity = (sumQty[KEYCODE_IDX]);
                bldStore.order.add(order);
            }
            inc(KEYCODE_IDX);
        }
    }

    private Summary buildSummary(int idx) {
        Summary summary = new Summary();
        summary.count = (sumCount[idx]);
        summary.price = (sumPrice[idx]);
        summary.quantity = (sumQty[idx]);
        return summary;
    }

    private void inc(int idx) {
        sumQty[idx - 1] += sumQty[idx];
        sumPrice[idx - 1] += sumPrice[idx];
        sumCount[idx - 1] += sumCount[idx];
        sumQty[idx] = 0;
        sumPrice[idx] = 0;
        sumCount[idx] = 0;
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        new WriteSales6();
    }
}
