package org.zkoss.zkand.factory;

import org.xml.sax.Attributes;
import org.zkoss.zkand.UiManager;
import org.zkoss.zkand.ZkComponent;
import org.zkoss.zkand.ui.ZkDesktop;
import org.zkoss.zkand.ui.ZkTextView;
import android.content.Context;
import android.util.Log;

/**
 * An UiFactory that create a TextView Ui component.
 * @author robbiecheng
 *
 */
public class TextViewFactory extends AbstractUiFactory {

    private static final String LTAG = TextViewFactory.class.getName();

    public TextViewFactory(String name) {
        super(name);
    }

    public ZkComponent create(Context context, ZkComponent parent, String tag, Attributes attrs, String hostURL, String pathURL) {
        final String id = attrs.getValue("id");
        final String label = attrs.getValue("lb");
        final String text = attrs.getValue("tx");
        Log.d(LTAG, "create : " + tag + ",parent:" + parent + ",zid:" + id);
        final ZkDesktop zk = ((ZkComponent) parent).getZkDesktop();
        final ZkTextView component = new ZkTextView(context, zk, id, label, text);
        UiManager.applyViewProperties(parent, component, attrs);
        return component;
    }
}
