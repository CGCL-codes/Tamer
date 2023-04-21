package org.mil.bean.bo;

import java.util.Calendar;
import org.mil.bean.dom.GeneralTrackDO;
import org.mil.util.TextUtil;

public class GeneralTrack extends Track {

    private String format;

    private int duration;

    private int overall_bit_rate;

    private String movie_name;

    private Calendar encoded_date;

    private String writing_application;

    private String writing_library;

    public void load(GeneralTrackDO source) {
        format = source.getFormat();
        overall_bit_rate = (int) TextUtil.quantify(source.getOverall_bit_rate(), "Kbps");
        movie_name = source.getMovie_name();
        encoded_date = TextUtil.parseDate(source.getEncoded_date());
        writing_application = source.getWriting_application();
        writing_library = source.getWriting_library();
    }

    public String getFormat() {
        return format;
    }

    public int getDuration() {
        return duration;
    }

    public int getOverall_bit_rate() {
        return overall_bit_rate;
    }

    public String getMovie_name() {
        return movie_name;
    }

    public Calendar getEncoded_date() {
        return encoded_date;
    }

    public String getWriting_application() {
        return writing_application;
    }

    public String getWriting_library() {
        return writing_library;
    }
}
