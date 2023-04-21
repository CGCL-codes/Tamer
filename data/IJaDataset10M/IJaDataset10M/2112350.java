package unbbayes.controller.mebn.resources;

import java.util.ArrayList;
import unbbayes.controller.resources.ControllerResources_pt;

/**
 * <p>Title: UnBBayes</p>
 * <p>Description: Arquivo de recurso para o pacote unbbayes.controller. Localization = portuguese.</p>
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company: UnB</p>
 * @author Shou Matsumoto
 * @version 1.0
 * @since 02/13/2010
 */
public class Resources_pt extends ControllerResources_pt {

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
	 * The particular resources for this class
	 */
    static final Object[][] contents = { { "withoutMFrag", "Nenhuma MFrag encontrada" } };
}
