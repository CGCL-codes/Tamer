package org.ladybug.core.db.filebased;

import static org.junit.Assert.assertEquals;
import java.io.IOException;
import org.junit.Before;
import org.junit.Test;
import org.ladybug.core.db.bytestreamers.MyByteArrayInputStream;
import org.ladybug.utils.Constants;

/**
 * @author Aurelian Pop
 */
public class SchemaTest {

    private SchemaBuilder builder;

    @Before
    public void setUp() {
        builder = new SchemaBuilder();
    }

    @Test
    public void testBuildingReleaseTableSchema() throws IOException {
        final Schema schema = builder.buildReleaseTableSchema();
        assertEquals(4, schema.getFieldCount());
        assertEquals(1 + FieldType.INT.getBytesPerUnit() + FieldType.LONG.getBytesPerUnit() + (Constants.DEFAULT_NAMED_ITEM_NAME_LENGTH + Constants.DEFAULT_NAMED_ITEM_DESC_LENGTH) * FieldType.STRING.getBytesPerUnit(), schema.getRecordByteLength());
        int i = 0;
        assertEquals(FieldType.INT, schema.getField(i).getFieldType());
        assertEquals("Priority", schema.getField(i).getName());
        i++;
        assertEquals(FieldType.LONG, schema.getField(i).getFieldType());
        assertEquals("KeyId", schema.getField(i).getName());
        i++;
        assertEquals(FieldType.STRING, schema.getField(i).getFieldType());
        assertEquals("Name", schema.getField(i).getName());
        i++;
        assertEquals(FieldType.STRING, schema.getField(i).getFieldType());
        assertEquals("Description", schema.getField(i).getName());
        final byte[] header = schema.buildHeader((byte) 1);
        MyByteArrayInputStream mybais = new MyByteArrayInputStream(header);
        assertEquals(1, mybais.read());
        assertEquals(header.length, mybais.readShort());
        assertEquals(schema.getFieldCount(), mybais.read());
        mybais.close();
        final Schema recreatedSchema = Schema.buildSchema(header);
        assertEquals(schema, recreatedSchema);
        final byte[] releaseRec = schema.buildRecord("1", "101", "Cucu", "Bau");
        mybais = new MyByteArrayInputStream(releaseRec);
        assertEquals(1, mybais.readInt());
        assertEquals(101, mybais.readLong());
        assertEquals("Cucu", mybais.readString(Constants.DEFAULT_NAMED_ITEM_NAME_LENGTH));
        assertEquals("Bau", mybais.readString(Constants.DEFAULT_NAMED_ITEM_DESC_LENGTH));
    }

    @Test
    public void testBuildingFeatureTableSchema() throws IOException {
        final Schema schema = builder.buildFeatureTableSchema();
        assertEquals(5, schema.getFieldCount());
        assertEquals(1 + FieldType.INT.getBytesPerUnit() + 2 * FieldType.LONG.getBytesPerUnit() + (Constants.DEFAULT_NAMED_ITEM_NAME_LENGTH + Constants.DEFAULT_NAMED_ITEM_DESC_LENGTH) * FieldType.STRING.getBytesPerUnit(), schema.getRecordByteLength());
        int i = 0;
        assertEquals(FieldType.INT, schema.getField(i).getFieldType());
        assertEquals("Priority", schema.getField(i).getName());
        i++;
        assertEquals(FieldType.LONG, schema.getField(i).getFieldType());
        assertEquals("KeyId", schema.getField(i).getName());
        i++;
        assertEquals(FieldType.LONG, schema.getField(i).getFieldType());
        assertEquals("ParentId", schema.getField(i).getName());
        i++;
        assertEquals(FieldType.STRING, schema.getField(i).getFieldType());
        assertEquals("Name", schema.getField(i).getName());
        i++;
        assertEquals(FieldType.STRING, schema.getField(i).getFieldType());
        assertEquals("Description", schema.getField(i).getName());
        final byte[] header = schema.buildHeader((byte) 1);
        MyByteArrayInputStream mybais = new MyByteArrayInputStream(header);
        assertEquals(1, mybais.read());
        assertEquals(header.length, mybais.readShort());
        assertEquals(schema.getFieldCount(), mybais.read());
        mybais.close();
        final Schema recreatedSchema = Schema.buildSchema(header);
        assertEquals(schema, recreatedSchema);
        final byte[] featureRec = schema.buildRecord("1", "101", "2", "Cucu", "Bau");
        mybais = new MyByteArrayInputStream(featureRec);
        assertEquals(1, mybais.readInt());
        assertEquals(101, mybais.readLong());
        assertEquals(2, mybais.readLong());
        assertEquals("Cucu", mybais.readString(Constants.DEFAULT_NAMED_ITEM_NAME_LENGTH));
        assertEquals("Bau", mybais.readString(Constants.DEFAULT_NAMED_ITEM_DESC_LENGTH));
    }

    @Test
    public void testBuildingUserStoryTableSchema() throws IOException {
        final Schema schema = builder.buildUserStoryTableSchema();
        assertEquals(7, schema.getFieldCount());
        assertEquals(1 + FieldType.INT.getBytesPerUnit() + 2 * FieldType.LONG.getBytesPerUnit() + (Constants.DEFAULT_NAMED_ITEM_NAME_LENGTH + Constants.DEFAULT_FEATURE_ACTOR_LENGTH + Constants.DEFAULT_FEATURE_ACTION_LENGTH + Constants.DEFAULT_FEATURE_PURPOSE_LENGTH) * FieldType.STRING.getBytesPerUnit(), schema.getRecordByteLength());
        int i = 0;
        assertEquals(FieldType.INT, schema.getField(i).getFieldType());
        assertEquals("Priority", schema.getField(i).getName());
        i++;
        assertEquals(FieldType.LONG, schema.getField(i).getFieldType());
        assertEquals("KeyId", schema.getField(i).getName());
        i++;
        assertEquals(FieldType.LONG, schema.getField(i).getFieldType());
        assertEquals("ParentId", schema.getField(i).getName());
        i++;
        assertEquals(FieldType.STRING, schema.getField(i).getFieldType());
        assertEquals("Name", schema.getField(i).getName());
        i++;
        assertEquals(FieldType.STRING, schema.getField(i).getFieldType());
        assertEquals("Actor", schema.getField(i).getName());
        i++;
        assertEquals(FieldType.STRING, schema.getField(i).getFieldType());
        assertEquals("Action", schema.getField(i).getName());
        i++;
        assertEquals(FieldType.STRING, schema.getField(i).getFieldType());
        assertEquals("Purpose", schema.getField(i).getName());
        final byte[] header = schema.buildHeader((byte) 1);
        MyByteArrayInputStream mybais = new MyByteArrayInputStream(header);
        assertEquals(1, mybais.read());
        assertEquals(header.length, mybais.readShort());
        assertEquals(schema.getFieldCount(), mybais.read());
        mybais.close();
        final Schema recreatedSchema = Schema.buildSchema(header);
        assertEquals(schema, recreatedSchema);
        final byte[] userStoryRec = schema.buildRecord("1", "101", "2", "Cucu", "Bau1", "Bau2", "Bau3");
        mybais = new MyByteArrayInputStream(userStoryRec);
        assertEquals(1, mybais.readInt());
        assertEquals(101, mybais.readLong());
        assertEquals(2, mybais.readLong());
        assertEquals("Cucu", mybais.readString(Constants.DEFAULT_NAMED_ITEM_NAME_LENGTH));
        assertEquals("Bau1", mybais.readString(Constants.DEFAULT_FEATURE_ACTOR_LENGTH));
        assertEquals("Bau2", mybais.readString(Constants.DEFAULT_FEATURE_ACTION_LENGTH));
        assertEquals("Bau3", mybais.readString(Constants.DEFAULT_FEATURE_PURPOSE_LENGTH));
    }
}
