package gurpsbeans;

import java.beans.*;

/**
 * Title: 			 jOpenRPG
 * Description:  jOpenRPG is an OpenRPG compatible online gaming client.
 * Copyright: 	 Copyright (c) 2001
 * Company: 		 Etherstorm Software
 * @author $Author$
 * @version $Revision: 1.1.1.1 $
 */
public class GurpsSkillDifficultyPropertyEditor extends PropertyEditorSupport {

    public GurpsSkillDifficultyPropertyEditor() {
    }

    public static final String[] tagStrings = { "Easy", "Average", "Hard", "Very Hard" };

    public String[] getTags() {
        return tagStrings;
    }

    public String getJavaInitializationString() {
        return "\"" + getAsText() + "\"";
    }

    public void setAsText(String text) throws IllegalArgumentException {
        setValue(text);
    }
}
