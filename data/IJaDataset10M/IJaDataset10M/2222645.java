package net.sf.RecordEditor.Avro.Test.Sales6;

@SuppressWarnings("all")
public class Product extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {

    public static final org.apache.avro.Schema SCHEMA$ = org.apache.avro.Schema.parse("{\"type\":\"record\",\"name\":\"Product\",\"namespace\":\"net.sf.RecordEditor.Avro.Test.Sales6\",\"fields\":[{\"name\":\"keycode\",\"type\":\"int\"},{\"name\":\"saleDate\",\"type\":\"int\"},{\"name\":\"quantity\",\"type\":\"int\"},{\"name\":\"price\",\"type\":\"long\"},{\"name\":\"saleType\",\"type\":{\"type\":\"enum\",\"name\":\"SaleType\",\"symbols\":[\"RETURN\",\"OTHER\",\"SALE\"]}},{\"name\":\"paymentType\",\"type\":[\"null\",{\"type\":\"enum\",\"name\":\"PaymentType\",\"symbols\":[\"CASH\",\"CREDIT_CARD\",\"DEBIT_CARD\",\"CHEQUE\"]}]}]}");

    public int keycode;

    public int saleDate;

    public int quantity;

    public long price;

    public net.sf.RecordEditor.Avro.Test.Sales6.SaleType saleType;

    public net.sf.RecordEditor.Avro.Test.Sales6.PaymentType paymentType;

    public org.apache.avro.Schema getSchema() {
        return SCHEMA$;
    }

    public java.lang.Object get(int field$) {
        switch(field$) {
            case 0:
                return keycode;
            case 1:
                return saleDate;
            case 2:
                return quantity;
            case 3:
                return price;
            case 4:
                return saleType;
            case 5:
                return paymentType;
            default:
                throw new org.apache.avro.AvroRuntimeException("Bad index");
        }
    }

    @SuppressWarnings(value = "unchecked")
    public void put(int field$, java.lang.Object value$) {
        switch(field$) {
            case 0:
                keycode = (java.lang.Integer) value$;
                break;
            case 1:
                saleDate = (java.lang.Integer) value$;
                break;
            case 2:
                quantity = (java.lang.Integer) value$;
                break;
            case 3:
                price = (java.lang.Long) value$;
                break;
            case 4:
                saleType = (net.sf.RecordEditor.Avro.Test.Sales6.SaleType) value$;
                break;
            case 5:
                paymentType = (net.sf.RecordEditor.Avro.Test.Sales6.PaymentType) value$;
                break;
            default:
                throw new org.apache.avro.AvroRuntimeException("Bad index");
        }
    }
}
