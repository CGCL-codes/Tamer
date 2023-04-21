package com.appspot.ajnweb.controller.ajax;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;
import net.sf.json.JSON;
import net.sf.json.JSONSerializer;
import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import com.appspot.ajnweb.model.Tweet;
import com.appspot.ajnweb.service.DailyService;

/**
 * 日付指定で取得する。
 * @author shin1ogawa
 */
public class DayController extends Controller {

    static final int limit = 20;

    static final Logger logger = Logger.getLogger(DayController.class.getName());

    @Override
    protected Navigation run() {
        Integer page = asInteger("page");
        if (page == null) {
            page = 0;
        }
        int offset = page * 20;
        if (offset < 0) {
            offset = 0;
        }
        int toIndex = offset + limit;
        int year = asInteger("year");
        int month = asInteger("month");
        int day = asInteger("day");
        List<Tweet> list = DailyService.getDailyTweets(year, month, day);
        int max = (list.size() - 1) / limit;
        if (offset >= list.size()) {
            offset = list.size();
        }
        if (toIndex >= list.size()) {
            toIndex = list.size();
        }
        list = list.subList(offset, toIndex);
        try {
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            JSON json = JSONSerializer.toJSON(new ListAndPagingDto<Tweet>(max, page, list));
            json.write(response.getWriter());
            response.flushBuffer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
