package net.sf.RecordEditor.Avro.Test.TestField;

@SuppressWarnings("all")
public class fields extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {

    public static final org.apache.avro.Schema SCHEMA$ = org.apache.avro.Schema.parse("{\"type\":\"record\",\"name\":\"fields\",\"namespace\":\"net.sf.RecordEditor.Avro.Test.TestField\",\"fields\":[{\"name\":\"f01\",\"type\":\"int\"},{\"name\":\"f02\",\"type\":\"int\"},{\"name\":\"f03\",\"type\":\"int\"},{\"name\":\"f04\",\"type\":\"int\"},{\"name\":\"f05\",\"type\":\"int\"},{\"name\":\"f06\",\"type\":\"long\"},{\"name\":\"f07\",\"type\":\"long\"},{\"name\":\"f08\",\"type\":\"long\"},{\"name\":\"f09\",\"type\":\"long\"},{\"name\":\"f10\",\"type\":\"long\"},{\"name\":\"f11\",\"type\":\"float\"},{\"name\":\"f12\",\"type\":\"double\"},{\"name\":\"f13\",\"type\":\"boolean\"},{\"name\":\"f14\",\"type\":[\"null\",\"boolean\"]},{\"name\":\"f15\",\"type\":\"string\"},{\"name\":\"f16\",\"type\":[\"null\",\"string\"]},{\"name\":\"f17\",\"type\":{\"type\":\"enum\",\"name\":\"SaleType\",\"symbols\":[\"RETURN\",\"OTHER\",\"SALE\"]}},{\"name\":\"f18\",\"type\":[\"null\",\"SaleType\"]},{\"name\":\"f19\",\"type\":\"bytes\"},{\"name\":\"f20\",\"type\":[\"null\",\"bytes\"]}]}");

    public int f01;

    public int f02;

    public int f03;

    public int f04;

    public int f05;

    public long f06;

    public long f07;

    public long f08;

    public long f09;

    public long f10;

    public float f11;

    public double f12;

    public boolean f13;

    public java.lang.Boolean f14;

    public org.apache.avro.util.Utf8 f15;

    public org.apache.avro.util.Utf8 f16;

    public net.sf.RecordEditor.Avro.Test.TestField.SaleType f17;

    public net.sf.RecordEditor.Avro.Test.TestField.SaleType f18;

    public java.nio.ByteBuffer f19;

    public java.nio.ByteBuffer f20;

    public org.apache.avro.Schema getSchema() {
        return SCHEMA$;
    }

    public java.lang.Object get(int field$) {
        switch(field$) {
            case 0:
                return f01;
            case 1:
                return f02;
            case 2:
                return f03;
            case 3:
                return f04;
            case 4:
                return f05;
            case 5:
                return f06;
            case 6:
                return f07;
            case 7:
                return f08;
            case 8:
                return f09;
            case 9:
                return f10;
            case 10:
                return f11;
            case 11:
                return f12;
            case 12:
                return f13;
            case 13:
                return f14;
            case 14:
                return f15;
            case 15:
                return f16;
            case 16:
                return f17;
            case 17:
                return f18;
            case 18:
                return f19;
            case 19:
                return f20;
            default:
                throw new org.apache.avro.AvroRuntimeException("Bad index");
        }
    }

    @SuppressWarnings(value = "unchecked")
    public void put(int field$, java.lang.Object value$) {
        switch(field$) {
            case 0:
                f01 = (java.lang.Integer) value$;
                break;
            case 1:
                f02 = (java.lang.Integer) value$;
                break;
            case 2:
                f03 = (java.lang.Integer) value$;
                break;
            case 3:
                f04 = (java.lang.Integer) value$;
                break;
            case 4:
                f05 = (java.lang.Integer) value$;
                break;
            case 5:
                f06 = (java.lang.Long) value$;
                break;
            case 6:
                f07 = (java.lang.Long) value$;
                break;
            case 7:
                f08 = (java.lang.Long) value$;
                break;
            case 8:
                f09 = (java.lang.Long) value$;
                break;
            case 9:
                f10 = (java.lang.Long) value$;
                break;
            case 10:
                f11 = (java.lang.Float) value$;
                break;
            case 11:
                f12 = (java.lang.Double) value$;
                break;
            case 12:
                f13 = (java.lang.Boolean) value$;
                break;
            case 13:
                f14 = (java.lang.Boolean) value$;
                break;
            case 14:
                f15 = (org.apache.avro.util.Utf8) value$;
                break;
            case 15:
                f16 = (org.apache.avro.util.Utf8) value$;
                break;
            case 16:
                f17 = (net.sf.RecordEditor.Avro.Test.TestField.SaleType) value$;
                break;
            case 17:
                f18 = (net.sf.RecordEditor.Avro.Test.TestField.SaleType) value$;
                break;
            case 18:
                f19 = (java.nio.ByteBuffer) value$;
                break;
            case 19:
                f20 = (java.nio.ByteBuffer) value$;
                break;
            default:
                throw new org.apache.avro.AvroRuntimeException("Bad index");
        }
    }
}
