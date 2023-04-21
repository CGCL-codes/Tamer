package model_test;

import java.util.List;
import junit.framework.Assert;
import model.OnlineVokabel;
import org.junit.Test;

public class OnlineVokabelTest {

    @Test
    public void readOnline() {
        OnlineVokabel ov = new OnlineVokabel("break");
        Assert.assertNotNull(ov);
        List<String> german = ov.getGermanList();
        Assert.assertNotNull(german);
        List<String> english = ov.getEnglishList();
        Assert.assertNotNull(english);
        Assert.assertEquals(english.size(), german.size());
        for (int i = 0; i < german.size(); i++) System.out.println(english.get(i) + " - " + german.get(i));
    }
}
