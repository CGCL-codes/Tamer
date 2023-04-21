package net.virtualhockey.vhtags.makemove;

import java.util.List;
import vh.data.moveday.VHMoveDay;
import vh.error.VHException;
import net.virtualhockey.utils.TemplateManager;
import net.virtualhockey.vhtags.universal.AbstractLogicProvider;
import net.virtualhockey.vhtags.universal.UniversalTagHandler;

/**
 * Allows to select the currently displayed MMD.
 * 
 * @version $Id: MMDSelector.java 93 2007-04-06 10:52:58Z janjanke $
 * @author jjanke
 */
public class MMDSelector extends AbstractLogicProvider {

    private static final String TEMPLATE_TAG_OPTIONS = "SELECTABLE_OPTIONS";

    private static final String TEMPLATE_TAG_MMD_NUMBER = "MMD_NUMBER";

    /** Currently active template for a not selected HTML select option. */
    private TemplateManager d_templateOption;

    /** Currently active template for a selected HTML select option. */
    private TemplateManager d_templateSelectedOption;

    public String doLogic(UniversalTagHandler tagHandler) throws VHException {
        List<VHMoveDay> listMMDs = VHMoveDay.getOutstandingMMDsOfCurrentSeason();
        int nSelectedMMD = Integer.parseInt((String) tagHandler.getTagAttribute("selectedMMD"));
        if (listMMDs.isEmpty()) return (String) tagHandler.getTagAttribute("seasonIsOverPattern");
        StringBuilder sbOptions = new StringBuilder();
        for (int i = 0; i < listMMDs.size(); i++) {
            VHMoveDay mmd = listMMDs.get(i);
            if (mmd.getNum() == nSelectedMMD) sbOptions.append(getSelectedOption(tagHandler, mmd)); else sbOptions.append(getOption(tagHandler, mmd));
        }
        TemplateManager template = new TemplateManager((String) tagHandler.getTagAttribute("selectorPattern"));
        template.replace(TEMPLATE_TAG_OPTIONS, sbOptions.toString());
        return template.toString();
    }

    /**
   * Builds a MMD option that is not selected.
   * 
   * @param tagHandler reference to the actual JSP tag
   * @param mmd the MMD for which the option should be created
   * @return the created HTML select option
   */
    private String getOption(UniversalTagHandler tagHandler, VHMoveDay mmd) {
        if (d_templateOption == null) d_templateOption = new TemplateManager((String) tagHandler.getTagAttribute("optionPattern")); else d_templateOption.reset();
        d_templateOption.replace(TEMPLATE_TAG_MMD_NUMBER, String.valueOf(mmd.getNum()));
        return d_templateOption.toString();
    }

    /**
   * Builds a MMD option that is not selected.
   * 
   * @param tagHandler reference to the actual JSP tag
   * @param mmd the MMD for which the option should be created
   * @return the created HTML select option
   */
    private String getSelectedOption(UniversalTagHandler tagHandler, VHMoveDay mmd) {
        if (d_templateSelectedOption == null) d_templateSelectedOption = new TemplateManager((String) tagHandler.getTagAttribute("selectedOptionPattern")); else d_templateSelectedOption.reset();
        d_templateSelectedOption.replace(TEMPLATE_TAG_MMD_NUMBER, String.valueOf(mmd.getNum()));
        return d_templateSelectedOption.toString();
    }
}
