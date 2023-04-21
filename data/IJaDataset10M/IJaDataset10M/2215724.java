package org.dbe.studio.core.smcore.migration.from1to2.core;

import java.util.Vector;
import org.dbe.studio.core.smcore.modellist.SMModel;

/**
 * @author gdorigo
 *
 */
public class KB1 {

    private String url = null;

    public Vector list() {
        if (url == null) {
            System.out.println("Error URL is null");
        }
        SMModel smModel = new SMModel();
        Object[] o;
        Vector v = new Vector();
        String id;
        try {
            o = smModel.listModels(url);
            for (int i = 0; i < o.length; i++) {
                id = (String) ((Object[]) (o[i]))[0];
                v.add(id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return v;
    }

    public String getModel(String id) {
        SMModel model = new SMModel();
        try {
            return model.getModel(url, id);
        } catch (Exception e) {
            System.out.println("Error KB problem");
        }
        return null;
    }

    /**
     * @return Returns the url.
     */
    public final String getUrl() {
        return url;
    }

    /**
     * @param url The url to set.
     */
    public final void setUrl(String url) {
        this.url = url;
    }

    /**
     * 
     */
    public KB1(String inUrl) {
        super();
        this.url = inUrl;
    }

    /**
     * 
     */
    public KB1() {
        super();
    }
}
