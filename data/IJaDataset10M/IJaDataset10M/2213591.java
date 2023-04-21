package com.avatal.factory.assembler.view;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import com.avatal.ConfigurableConstants;
import com.avatal.content.vo.course.FaqItemVo;
import com.avatal.factory.assembler.AbstractObjectAssembler;
import com.avatal.view.course.FaqItemView;

/**
 * @author c.ferdinand
 * Created on jun 12, 2003
 */
public class FaqItemViewAssembler extends AbstractObjectAssembler {

    private FaqItemView itemView;

    public FaqItemViewAssembler() {
        itemView = new FaqItemView();
    }

    public FaqItemViewAssembler(FaqItemView itemView) {
        this.itemView = itemView;
    }

    public void addFaqItem(FaqItemVo message, Locale locale) {
        Date date = message.getTimeCreated();
        if (null != date) {
            SimpleDateFormat sdf = new SimpleDateFormat(ConfigurableConstants.DATE_FORMAT_NEWS, locale);
            itemView.put(FaqItemView.TIMECREATED, sdf.format(date));
        }
        date = message.getTimeModified();
        if (null != date) {
            SimpleDateFormat sdf = new SimpleDateFormat(ConfigurableConstants.DATE_FORMAT_NEWS, locale);
            itemView.put(FaqItemView.TIMEMODIFIED, sdf.format(date));
        }
        itemView.put(FaqItemView.ID, message.getId());
        itemView.put(FaqItemView.OBJECT_STATE, message.getObjectState());
        itemView.put(FaqItemView.QUESTION, message.getTitle());
        itemView.put(FaqItemView.ANSWER, message.getText());
    }

    /**
     * @return
     */
    public FaqItemView getFaqItemView() {
        return itemView;
    }
}
