package net.sf.RecordEditor.Avro.Test.IO;

import java.io.File;
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
import net.sf.RecordEditor.Avro.Test.Sales11.PaymentType;
import net.sf.RecordEditor.Avro.Test.Sales11.SaleType;
import net.sf.RecordEditor.Avro.Test.Sales11.sale;
import net.sf.RecordEditor.Avro.Test.Sales7.Product;

public class WriteSales11 {

    private String installDir = TestConst.INPUT_DIR;

    private String salesFile = installDir + "DTAR020.bin";

    private String salesFileOut = TestConst.OUTPUT_DIR + "protoSales11.bin";

    private String copybookName = TestConst.COBOL_DIR + "DTAR020.cbl";

    private PaymentType[] payments = { PaymentType.CASH, PaymentType.CHEQUE, PaymentType.CREDIT_CARD, PaymentType.DEBIT_CARD };

    /**
     * Example of LineReader / LineWrite classes
     */
    private WriteSales11() {
        super();
        int lineNum = 0;
        AbstractLine<LayoutDetail> saleRecord;
        try {
            int fileStructure = Constants.IO_FIXED_LENGTH;
            CobolIoProvider ioProvider = CobolIoProvider.getInstance();
            AbstractLineReader<LayoutDetail> reader = ioProvider.getLineReader(fileStructure, Convert.FMT_MAINFRAME, CopybookLoader.SPLIT_NONE, copybookName, salesFile);
            DataFileWriter<sale> out = new DataFileWriter<sale>(new SpecificDatumWriter<sale>(sale.SCHEMA$));
            out.create(sale.SCHEMA$, new File(salesFileOut));
            int qty;
            int fldNum = 0;
            int paymentIdx = 0;
            SaleType saleType;
            String ss;
            FieldDetail keycodeField = reader.getLayout().getField(0, fldNum++);
            FieldDetail storeField = reader.getLayout().getField(0, fldNum++);
            FieldDetail dateField = reader.getLayout().getField(0, fldNum++);
            FieldDetail deptField = reader.getLayout().getField(0, fldNum++);
            FieldDetail qtyField = reader.getLayout().getField(0, fldNum++);
            FieldDetail salesField = reader.getLayout().getField(0, fldNum++);
            sale saleRec;
            while ((saleRecord = reader.read()) != null) {
                lineNum += 1;
                BigDecimal price = saleRecord.getFieldValue(salesField).asBigDecimal().multiply(BigDecimal.valueOf(1000));
                saleRec = new sale();
                saleRec.strArray = new GenericData.Array<Utf8>(5, Schema.createArray(Product.SCHEMA$.getField("strArray").schema()));
                qty = saleRecord.getFieldValue(qtyField).asInt();
                saleType = SaleType.SALE;
                if (qty == 0) {
                    saleType = SaleType.OTHER;
                } else if (qty < 0) {
                    saleType = SaleType.RETURN;
                }
                saleRec.keycode = saleRecord.getFieldValue(keycodeField).asInt();
                saleRec.store = saleRecord.getFieldValue(storeField).asInt();
                saleRec.department = (saleRecord.getFieldValue(deptField).asInt());
                saleRec.saleDate = (saleRecord.getFieldValue(dateField).asInt());
                saleRec.quantity = (qty);
                saleRec.price = (price.longValue());
                saleRec.saleType = saleType;
                ss = "";
                for (int j = 0; j < paymentIdx * 3 / 2; j++) {
                    saleRec.strArray.add(new Utf8(ss));
                    ss = ss + " " + j;
                }
                if (paymentIdx != 0) {
                    saleRec.priceFloat = (saleRecord.getFieldValue(salesField).asFloat());
                }
                if (paymentIdx < payments.length) {
                    saleRec.paymentType = (payments[paymentIdx]);
                    paymentIdx += 1;
                    saleRec.priceDouble = (saleRecord.getFieldValue(salesField).asDouble());
                } else {
                    paymentIdx = 0;
                }
                out.append(saleRec);
            }
            reader.close();
            out.close();
            System.out.println("Written: " + lineNum);
        } catch (Exception e) {
            System.out.println("~~> " + lineNum + " " + e.getMessage());
            System.out.println();
            e.printStackTrace();
        }
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        new WriteSales11();
    }
}
