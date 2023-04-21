package freetm.client.ui.frameworktest;

import gwtm.client.GwtmStatics;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import net.mygwt.ui.client.Style;
import net.mygwt.ui.client.widget.TabItem;
import net.mygwt.ui.client.widget.WidgetContainer;
import net.mygwt.ui.client.widget.layout.FillLayout;

/**
 *
 * @author Yorgos
 */
public class TabItemAbout extends TabItem {

    /** Creates a new instance of TabItemTolog */
    public TabItemAbout() {
        super(Style.CLOSE);
        setText("About FreeTM");
        VerticalPanel h = new VerticalPanel();
        h.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        h.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        h.setWidth("100%");
        h.setHeight("100%");
        HorizontalPanel v = new HorizontalPanel();
        v.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        v.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        v.setWidth("100%");
        v.setHeight("100%");
        h.add(v);
        String s = "<b>FreeTM</b> is a Free Topic Map Editor, based on <a href='http://sourceforge.net/projects/gwtm/' target='_blank'> GWTM</a> (Topic Maps for GWT) library, ";
        s += "that uses <a href='http://tinytim.sourceforge.net/' target='_blank'>tinyTIM</a> library for handling and storing Topic Maps on the server.<br>";
        s += "FreeTM is developed by the Departments of Electronics, TEI Thessalonikis, Greece  and it is free to be used for Topic Maps editing.<br>";
        s += "Current version: " + GwtmStatics.getInstance().getVersion() + ".<br><br>";
        s += "<a href='http://www.el.teithe.gr/' target='_blank'>http://www.el.teithe.gr/</a>";
        s += "<br><br><br><br><br><br>";
        s += "<DIV align='left'>";
        s += "<=========<br>";
        s += "<i>";
        s += "Select from the left the Topics that<br>";
        s += "you want, to start your navigation<br>";
        s += "</i>";
        s += "</DIV>";
        s += "<br>";
        s += "<DIV align='center'>";
        s += "====>.<====<br>";
        s += "<i>";
        s += "Navigate in the Center from Topic to Topic,<br>";
        s += "through theirs Roles that they play in Associations.<br>";
        s += "(Use of Explorer)<br>";
        s += "</i>";
        s += "</DIV>";
        s += "<br>";
        s += "<DIV align='right'>";
        s += "                               =========><br>";
        s += "<i>";
        s += "         See and edit from the right, the<br>";
        s += "  properties of the selected Topic in the<br>";
        s += "navigation area (Explorer, in the center)<br>";
        s += "</i>";
        s += "</DIV>";
        HTML html = new HTML(s);
        h.add(html);
        WidgetContainer container = this.getContainer();
        container.add(h);
        container.setLayout(new FillLayout(8));
    }
}
