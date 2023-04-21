package fhj.itm05.seminarswe.web.utils;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;
import junit.framework.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test class for the booking import
 * @author woelsv
 */
public class BookingCSVImportControllerTest {

    static int id;

    static String date;

    static String category;

    static String description;

    static float amount;

    static String user;

    static List<Map<String, Object>> elementList;

    @BeforeClass
    public static void testCSVParser() {
        File csvFile = new File("C:/booking.csv");
        try {
            elementList = CSVParser.getInstance().parseCSV(csvFile);
            for (int i = 0; i < elementList.size(); i++) {
                System.out.println("Current line: " + i);
                Map<String, Object> line = elementList.get(i);
                id = (Integer.valueOf((String) line.get("ID")));
                date = (String) line.get("Date");
                category = (String) line.get("Category");
                description = (String) line.get("Description");
                amount = (Float.valueOf((String) line.get("Amount")));
                user = (String) line.get("User");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void checkNumberOfLines() {
        Assert.assertEquals(1, elementList.size());
    }

    @Test
    public void checkId() {
        Assert.assertEquals(0, id);
    }

    @Test
    public void checkDate() {
        Assert.assertEquals("2008-01-07", date);
    }

    @Test
    public void checkCategory() {
        Assert.assertEquals("food", category);
    }

    @Test
    public void checkDescription() {
        Assert.assertEquals("kebap", description);
    }

    @Test
    public void checkAmount() {
        float am = (float) 10.0;
        Assert.assertEquals(am, amount);
    }

    @Test
    public void checkUser() {
        Assert.assertEquals("max", user);
    }
}
