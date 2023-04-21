package test;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Locale;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import net.infordata.ifw2.msgs.MessageTypeEnum;
import net.infordata.ifw2.msgs.SimpleMessage;
import net.infordata.ifw2m.mdl.NotFoundException;
import net.infordata.ifw2m.mdl.flds.APojoLookupValidator;
import net.infordata.ifw2m.mdl.flds.ComparablePojoField;
import net.infordata.ifw2m.mdl.flds.DateField;
import net.infordata.ifw2m.mdl.flds.FieldDefinition;
import net.infordata.ifw2m.mdl.flds.FieldMetaData;
import net.infordata.ifw2m.mdl.flds.FieldSet;
import net.infordata.ifw2m.mdl.flds.FieldStateEnum;
import net.infordata.ifw2m.mdl.flds.IFieldSet;
import net.infordata.ifw2m.mdl.flds.NumberField;
import net.infordata.ifw2m.mdl.flds.PojoField;
import net.infordata.ifw2m.mdl.flds.PojoFieldMetaData;
import net.infordata.ifw2m.mdl.flds.PojoFieldSet;
import net.infordata.ifw2m.mdl.flds.StringField;
import net.infordata.ifw2m.mdl.flds.TextField;

/**
 * Generated code for the test suite <b>TVarious</b> located at
 * <i>/ifw2m.test/src/test/TVarious.testsuite</i>.
 */
public class TVarious extends TestCase {

    /**
   * Constructor for TVarious.
   * @param name
   */
    public TVarious(String name) {
        super(name);
    }

    /**
   * Returns the JUnit test suite that implements the <b>TVarious</b> definition.
   */
    public static Test suite() {
        TestSuite tVarious = new TestSuite("TVarious");
        tVarious.addTest(new TVarious("fields"));
        tVarious.addTest(new TVarious("fielSets"));
        tVarious.addTest(new TVarious("pojoFieldSets"));
        tVarious.addTest(new TVarious("pojoValidator"));
        return tVarious;
    }

    /**
   * @see junit.framework.TestCase#setUp()
   */
    @Override
    protected void setUp() throws Exception {
    }

    /**
   * @see junit.framework.TestCase#tearDown()
   */
    @Override
    protected void tearDown() throws Exception {
    }

    enum MT {

        MSG1
    }

    ;

    /**
   * fields
   * @throws Exception
   */
    public void fields() throws Exception {
        TextField tf = new TextField();
        tf.setText("primo");
        tf.setMessage(MT.MSG1, new SimpleMessage("BO", MessageTypeEnum.WARNING));
        assertEquals("primo", tf.getValue());
        assertTrue(tf.hasMessage(MessageTypeEnum.WARNING));
        assertFalse(tf.hasMessage(MessageTypeEnum.ERROR));
        tf.setState(FieldStateEnum.UNEDITABLE);
        tf.setValue("primo");
        tf.setValue("terzo");
        tf.setText("terzo");
        try {
            tf.setText("quarto");
            assertTrue(false);
        } catch (IllegalStateException ex) {
        }
        tf.setState(FieldStateEnum.UNTOUCHABLE);
        try {
            tf.setValue("quinto");
            assertTrue(false);
        } catch (IllegalStateException ex) {
        }
        assertTrue(tf.hasMessage(MessageTypeEnum.WARNING));
        tf.setState(FieldStateEnum.SHADOW);
        assertEquals(null, tf.getValue());
        assertFalse(tf.hasMessage(MessageTypeEnum.WARNING));
        {
            NumberField df = new NumberField(NumberFormat.getInstance(Locale.ITALY));
            df.setValue(0.10);
            assertEquals("0,1", df.getText());
            df.setText("10,12300");
            assertEquals(10.123, df.getValue());
            assertTrue(Double.class.isAssignableFrom(df.getValue().getClass()));
            df.setState(FieldStateEnum.SHADOW);
            assertEquals(null, df.getValue());
        }
        {
            NumberField bdf = new NumberField(BigDecimal.class);
            bdf.setValue(new BigDecimal("0.1"));
            assertEquals("0,1", bdf.getText());
            bdf.setText("10,12300");
            assertEquals(10.123, bdf.getValue().doubleValue());
            assertTrue(BigDecimal.class.isAssignableFrom(bdf.getValue().getClass()));
            bdf.setState(FieldStateEnum.SHADOW);
            assertEquals(null, bdf.getValue());
            bdf.setState(FieldStateEnum.NORMAL);
            bdf.setText("aaa");
            assertEquals("aaa", bdf.getText());
            assertNull(bdf.getValue());
        }
        {
            Calendar cal = Calendar.getInstance(Locale.ITALY);
            DateField df = new DateField();
            df.setText("17/05/1968");
            cal.set(1968, 04, 17, 0, 0, 0);
            assertTrue(new DateField(cal.getTime()).compareTo(df) == 0);
            cal.set(1994, 04, 28);
            df.setValue(cal.getTime());
            assertEquals("28/05/1994", df.getText());
            df.setText("aaa");
            assertEquals("aaa", df.getText());
            assertNull(df.getValue());
        }
        {
            ComparablePojoField<Timestamp> fs = new ComparablePojoField<Timestamp>(Timestamp.class);
            fs.setValue(new Timestamp(10000));
            ComparablePojoField<Timestamp> fs2 = new ComparablePojoField<Timestamp>(Timestamp.class);
            fs2.setValue(new Timestamp(10001));
            assertTrue(fs2.compareTo(fs) > 0);
        }
    }

    private static enum NAMES {

        id, description, age, wage, cid
    }

    ;

    private static FieldSet FSP = new FieldSet();

    private static PojoFieldSet<Pojo> PFSP = new PojoFieldSet<Pojo>(Pojo.class);

    static {
        FSP.addField(new FieldDefinition(new FieldMetaData(NAMES.id.name()).setMandatory(true), new StringField()));
        FSP.addField(new FieldDefinition(new FieldMetaData(NAMES.description.name()), new TextField()));
        FSP.addField(new FieldDefinition(new FieldMetaData(NAMES.age.name()), new NumberField(NumberFormat.getIntegerInstance())));
        FSP.addField(new FieldDefinition(new FieldMetaData(NAMES.wage.name()), new NumberField(NumberFormat.getCurrencyInstance())));
        PFSP.addField(new FieldDefinition(new PojoFieldMetaData(NAMES.id.name()).setMandatory(true), new StringField()));
        PFSP.addField(new FieldDefinition(new PojoFieldMetaData(NAMES.description.name()), new TextField()));
        PFSP.addField(new FieldDefinition(new PojoFieldMetaData(NAMES.age.name()), new NumberField(NumberFormat.getIntegerInstance())));
        PFSP.addField(new FieldDefinition(new PojoFieldMetaData(NAMES.wage.name()), new NumberField(NumberFormat.getInstance())));
        PFSP.addField(new FieldDefinition(new PojoFieldMetaData("subPojoSid", "subPojo.sid", "label"), new StringField()));
        PFSP.addField(new FieldDefinition(new PojoFieldMetaData("version"), new NumberField(NumberFormat.getIntegerInstance())));
    }

    /**
   * fielSets
   * @throws Exception
   */
    public void fielSets() throws Exception {
        FieldSet fsp = FSP.createClone(false);
        try {
            FSP.addField(new FieldDefinition(new FieldMetaData(NAMES.cid.name()), new StringField()));
            assertTrue(false);
        } catch (IllegalStateException ex) {
        }
        try {
            fsp.getFieldMetaData(NAMES.id.name()).setPreferredSize(10);
            assertTrue(false);
        } catch (IllegalStateException ex) {
        }
        FieldSet cfsp = FSP.createClone(true);
        cfsp.addField(new FieldDefinition(new FieldMetaData(NAMES.cid.name()), new StringField()));
        assertTrue(FSP.shareStructureWith(fsp));
        assertFalse(FSP.hasSuperStructureOf(fsp));
        assertFalse(FSP.shareStructureWith(cfsp));
        assertTrue(FSP.hasSuperStructureOf(cfsp));
        fsp.get(NAMES.id.name()).setValue("vp");
        fsp.get(NAMES.description.name()).setValue("Valentino Proietti");
        fsp.get(NAMES.age.name()).setValue(39);
        fsp.get(NAMES.wage.name()).setValue(12345.67);
        assertNull(FSP.get(NAMES.id.name()).getValue());
        assertEquals("39", fsp.get(NAMES.age.name()).getText());
        fsp.validate();
        assertFalse(fsp.hasMessage());
        fsp.get(NAMES.wage.name()).setText("aaaa");
        fsp.validate();
        assertTrue(fsp.get(NAMES.wage.name()).hasMessage(MessageTypeEnum.ERROR));
        assertFalse(fsp.hasMessage(MessageTypeEnum.ERROR));
        assertTrue(fsp.hasValidationMessages(MessageTypeEnum.ERROR));
        fsp.get(NAMES.id.name()).setText(null);
        fsp.validate();
        assertTrue(fsp.get(NAMES.id.name()).hasMessage(MessageTypeEnum.ERROR));
        fsp.get(NAMES.id.name()).setValue("vp");
        fsp.validate();
        assertFalse(fsp.get(NAMES.id.name()).hasMessage(MessageTypeEnum.ERROR));
        fsp.setFieldsState(FieldStateEnum.SHADOW);
        assertNull(fsp.get(NAMES.id.name()).getValue());
        fsp.validate();
        assertFalse(fsp.get(NAMES.id.name()).hasMessage(MessageTypeEnum.ERROR));
        fsp.setFieldsState(null);
    }

    protected static class Pojo {

        private String ivId;

        private String ivDescription;

        private Long ivAge;

        private Double ivWage;

        private SubPojo ivSubPojo = new SubPojo();

        public Pojo() {
        }

        public Long getVersion() {
            return 1234L;
        }

        public String getId() {
            return ivId;
        }

        public void setId(String id) {
            ivId = id;
        }

        public String getDescription() {
            return ivDescription;
        }

        public void setDescription(String description) {
            ivDescription = description;
        }

        public Long getAge() {
            return ivAge;
        }

        public void setAge(Long age) {
            ivAge = age;
        }

        public Double getWage() {
            return ivWage;
        }

        public void setWage(Double wage) {
            ivWage = wage;
        }

        public void setSubPojo(SubPojo value) {
            ivSubPojo = value;
        }

        public SubPojo getSubPojo() {
            return ivSubPojo;
        }
    }

    protected static class SubPojo {

        private String ivSid;

        private String ivDescr;

        public SubPojo() {
        }

        public SubPojo(String sid, String descr) {
            ivSid = sid;
            ivDescr = descr;
        }

        public String getSid() {
            return ivSid;
        }

        public void setSid(String sid) {
            ivSid = sid;
        }

        public String getDescription() {
            return ivDescr;
        }
    }

    /**
   * pojoFieldSets
   * @throws Exception
   */
    public void pojoFieldSets() throws Exception {
        PojoFieldSet<Pojo> fsp = PFSP.createClone(false);
        fsp.get(NAMES.id.toString()).setValue("vp");
        fsp.get(NAMES.description.toString()).setValue("Valentino Proietti");
        fsp.get(NAMES.age.toString()).setValue(39);
        fsp.get(NAMES.wage.toString()).setValue(12345.67);
        try {
            fsp.get("version").setValue(12345.67);
            assertTrue(false);
        } catch (IllegalStateException ex) {
        }
        fsp.get("subPojoSid").setValue("svp");
        fsp.validate();
        assertEquals("vp", fsp.getPojo().getId());
        assertEquals(12345.67, fsp.getPojo().getWage());
        assertEquals("svp", fsp.getPojo().getSubPojo().getSid());
        fsp.get(NAMES.wage.toString()).setText("aaaa");
        fsp.validate();
        assertTrue(fsp.get(NAMES.wage.toString()).hasMessage(MessageTypeEnum.ERROR));
        assertFalse(fsp.hasMessage(MessageTypeEnum.ERROR));
        assertTrue(fsp.hasValidationMessages(MessageTypeEnum.ERROR));
        assertNull(fsp.getPojo().getWage());
        fsp.get(NAMES.wage.toString()).setValue(12345.67);
        fsp.get(NAMES.id.toString()).setText(null);
        fsp.validate();
        assertTrue(fsp.get(NAMES.id.toString()).hasMessage(MessageTypeEnum.ERROR));
        assertNull(fsp.getPojo().getId());
        fsp.get(NAMES.id.toString()).setValue("cm");
        assertTrue(fsp.validate());
        assertFalse(fsp.get(NAMES.id.toString()).hasMessage(MessageTypeEnum.ERROR));
        assertEquals("cm", fsp.getPojo().getId());
        fsp = PFSP.createClone(false);
        Pojo pojo = new Pojo();
        pojo.setId("mc");
        pojo.setDescription("Massimiliano Colozzi");
        pojo.setAge(40L);
        pojo.setWage(45678.90);
        pojo.getSubPojo().setSid("smc");
        fsp.setPojo(pojo);
        assertEquals(40, fsp.<Number>get(NAMES.age.toString()).getValue().intValue());
        assertEquals("smc", fsp.get("subPojoSid").getValue());
        assertEquals(1234, fsp.<Number>get("version").getValue().intValue());
    }

    /**
   * pojoValidator
   * @throws Exception
   */
    public void pojoValidator() throws Exception {
        PojoFieldSet<Pojo> fsp = PFSP.createClone(true);
        fsp.addField(new FieldMetaData("pojo").setMandatory(true), new PojoField<SubPojo>(SubPojo.class));
        fsp.addField(new FieldMetaData("pojoSid"), new StringField());
        fsp.addField(new FieldMetaData("pojoDescr"), new StringField());
        fsp.addValidator(new APojoLookupValidator<SubPojo>(SubPojo.class, "pojo", new String[] { "sid", "description" }, new String[] { "pojoSid", "pojoDescr" }) {

            private static final long serialVersionUID = 1L;

            @Override
            protected SubPojo lookupPojo(IFieldSet fs, String... lookupFields) throws NotFoundException {
                if (lookupFields.length <= 0) return null;
                String value = fs.get(lookupFields[0]).getText();
                if (value == null) return null;
                if ("error".equalsIgnoreCase(value)) throw new NotFoundException("ERRORE");
                return new SubPojo(value + "SID", value + "DESCR");
            }
        });
        assertFalse(fsp.validate());
        assertTrue(fsp.get("id").hasMessage(MessageTypeEnum.ERROR));
        fsp.get("id").setValue("aaa");
        assertFalse(fsp.validate());
        fsp.get("pojo").setValue(new SubPojo("sub", "subDescr"));
        assertTrue(fsp.validate());
        assertEquals("sub", fsp.get("pojoSid").getValue());
        assertEquals("subDescr", fsp.get("pojoDescr").getValue());
        fsp.get("pojoSid").setValue(null);
        assertFalse(fsp.validate("pojoSid"));
        assertNull(fsp.get("pojo").getValue());
        assertNull(fsp.get("pojoSid").getValue());
        assertNull(fsp.get("pojoDescr").getValue());
        fsp.get("pojo").setValue(new SubPojo("sub", "subDescr"));
        assertTrue(fsp.validate());
        fsp.get("pojoSid").setValue("error");
        assertFalse(fsp.validate("pojoSid"));
        assertTrue(fsp.get("pojo").hasMessage(MessageTypeEnum.ERROR));
        assertTrue(fsp.get("pojoSid").hasMessage(MessageTypeEnum.ERROR));
        assertTrue(fsp.get("pojoDescr").hasMessage(MessageTypeEnum.ERROR));
        fsp.get("pojoSid").setValue("valore");
        assertTrue(fsp.validate("pojoSid"));
        assertNotNull(fsp.get("pojo").getValue());
        assertEquals("valoreSID", fsp.get("pojoSid").getValue());
        assertEquals("valoreDESCR", fsp.get("pojoDescr").getValue());
    }
}
