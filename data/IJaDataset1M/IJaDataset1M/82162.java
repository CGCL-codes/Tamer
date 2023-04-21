package com.avatal.factory.assembler.view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import com.avatal.ConfigurableConstants;
import com.avatal.content.vo.course.FaqVo;
import com.avatal.factory.assembler.AbstractObjectAssembler;
import com.avatal.view.course.FaqView;

/**
 * @author c.ferdinand
 *
 */
public class FaqViewAssembler extends AbstractObjectAssembler {

    private FaqView faqView;

    public FaqViewAssembler() {
        faqView = new FaqView();
    }

    public FaqViewAssembler(FaqView faqView) {
        this.faqView = faqView;
    }

    public void addFaq(FaqVo faq, Locale locale) {
        faqView.put(FaqView.TITLE, faq.getTitle());
        Date date = faq.getTimeCreated();
        if (null != date) {
            SimpleDateFormat sdf = new SimpleDateFormat(ConfigurableConstants.DATE_FORMAT, locale);
            faqView.put(FaqView.TIMECREATED, sdf.format(faq.getTimeCreated()));
        }
        date = faq.getTimeModified();
        if (null != date) {
            SimpleDateFormat sdf = new SimpleDateFormat(ConfigurableConstants.DATE_FORMAT, locale);
            faqView.put(FaqView.TIMEMODIFIED, sdf.format(faq.getTimeModified()));
        }
        faqView.put(FaqView.ID, faq.getId());
        faqView.put(FaqView.STATUS, faq.getObjectState());
        faqView.put(FaqView.DESCRIPTION, faq.getDescription());
        faqView.put(FaqView.TITLE, faq.getTitle());
    }

    /**
     * use this Method to get a Faq ViewBean with embedded threads and messages.
     * the int acts as flag
     * @param forum
     * @param locale
     */
    public void addFaq(FaqVo faq, Locale locale, int i) {
        faqView.put(FaqView.TITLE, faq.getTitle());
        Date date = faq.getTimeCreated();
        if (null != date) {
            SimpleDateFormat sdf = new SimpleDateFormat(ConfigurableConstants.DATE_FORMAT, locale);
            faqView.put(FaqView.TIMECREATED, sdf.format(faq.getTimeCreated()));
        }
        date = faq.getTimeModified();
        if (null != date) {
            SimpleDateFormat sdf = new SimpleDateFormat(ConfigurableConstants.DATE_FORMAT, locale);
            faqView.put(FaqView.TIMEMODIFIED, sdf.format(faq.getTimeModified()));
        }
        faqView.put(FaqView.ID, faq.getId());
        faqView.put(FaqView.STATUS, faq.getObjectState());
        faqView.put(FaqView.DESCRIPTION, faq.getDescription());
        faqView.put(FaqView.TITLE, faq.getTitle());
        ArrayList items = faq.getItems();
        FaqItemListViewAssembler itemAss = new FaqItemListViewAssembler();
        itemAss.addFaqItems(items, locale);
        faqView.put(FaqView.FAQ_ITEMS, itemAss.getFaqItemViewList());
    }

    /**
     * @return
     */
    public FaqView getFaqView() {
        return faqView;
    }
}
