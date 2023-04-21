package com.yuksekisler.interfaces.web;

import static org.junit.Assert.assertTrue;
import java.io.IOException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import com.yuksekisler.domain.equipment.Brand;
import com.yuksekisler.domain.equipment.Category;
import com.yuksekisler.domain.equipment.Equipment;

public class EquipmentControllerTest {

    private static final MediaType MEDIA_TYPE = new MediaType("application", "json");

    private final String EQUIPMENT_STRING = "{\"productName\":\"product1\",\"productCode\":\"product1\",\"category\":{\"name\":\"a\",\"id\":1,\"description\":\"a\",\"version\":0,\"erased\":true},\"brand\":{\"name\":\"a\",\"id\":1,\"description\":\"a\",\"version\":0,\"erased\":false},\"stockEntrance\":0,\"bestBeforeDate\":0,\"productionDate\":0}";

    public static MappingJacksonHttpMessageConverter converter = new MappingJacksonHttpMessageConverter();

    public static ObjectMapper objectMapper = new ObjectMapper();

    @BeforeClass
    public static void prepareConverter() {
        converter.setObjectMapper(objectMapper);
    }

    @Test
    public void testJsonDeserialize() throws JsonParseException, JsonMappingException, IOException {
        assertTrue(converter.canRead(Category.class, MEDIA_TYPE));
        objectMapper.readValue("{\"name\":\"a\",\"id\":1,\"description\":\"a\",\"version\":0,\"erased\":false}", Category.class);
        assertTrue(converter.canRead(Brand.class, MEDIA_TYPE));
        objectMapper.readValue("{\"name\":\"a\",\"id\":1,\"description\":\"a\",\"version\":0,\"erased\":false}", Brand.class);
        assertTrue(converter.canRead(Equipment.class, MEDIA_TYPE));
        objectMapper.readValue(EQUIPMENT_STRING, Equipment.class);
    }

    @Test
    public void testJsonSerialize() {
        assertTrue(converter.canWrite(Equipment.class, MEDIA_TYPE));
        assertTrue(converter.canWrite(Category.class, MEDIA_TYPE));
        assertTrue(converter.canWrite(Brand.class, MEDIA_TYPE));
    }
}
