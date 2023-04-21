package unbbayes.gui.msbn.resources;

import java.util.ArrayList;
import unbbayes.gui.resources.GuiResources;

/**
 * <p>Title: UnBBayes</p>
 * <p>Description: Resources file for unbbayes.gui.msbn package. Localization = english.</p>
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company: UnB</p>
 * @author Shou Matsumoto
 * @version 1.0
 * @since 02/14/2010
 */
public class Resources extends GuiResources {

    /** 
	 *  Override getContents and provide an array, where each item in the array is a pair
	 *	of objects. The first element of each pair is a String key,
	 *	and the second is the value associated with that key.
	 *
	 * @return The resources' contents
	 */
    public Object[][] getContents() {
        ArrayList<Object[]> list = new ArrayList<Object[]>();
        for (Object[] objects : super.getContents()) {
            list.add(objects);
        }
        for (Object[] objects2 : this.contents) {
            list.add(objects2);
        }
        return list.toArray(new Object[0][0]);
    }

    /**
	 * The resources
	 */
    static final Object[][] contents = { { "newMsbnToolTip", "New MSBN" }, { "newMSBN", "New MSBN" }, { "newMSBNMn", "M" }, { "MSBNModuleName", "Multiple Sectioned Bayesian Network" } };
}
