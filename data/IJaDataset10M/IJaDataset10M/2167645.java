package lu.pragmaconsult.appstorebot.itunes;

import static junit.framework.Assert.assertEquals;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import lu.pragmaconsult.appstorebot.BotContext;
import org.junit.Test;

public class ComputeReportDatesBotStateTest {

    private ComputeReportDatesBotState state = new ComputeReportDatesBotState();

    @Test
    public void testProcessResponseBody() throws Exception {
        BotContext context = new BotContext();
        context.setVerbose(true);
        state.processResponseBody(context, null);
        SimpleDateFormat format = new SimpleDateFormat(Constants.DATE_FORMAT);
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DAY_OF_YEAR, -1);
        assertEquals(format.format(c.getTime()), context.getContextAttribute("reportDates"));
        assertEquals("0", context.getContextAttribute("reportDatesIndex"));
    }

    @Test
    public void testProcessResponseBodyAsDate() throws Exception {
        BotContext context = new BotContext();
        context.setVerbose(true);
        context.setDate("12/12/2010");
        state.processResponseBody(context, null);
        assertEquals("12/12/2010", context.getContextAttribute("reportDates"));
        assertEquals("0", context.getContextAttribute("reportDatesIndex"));
    }
}
