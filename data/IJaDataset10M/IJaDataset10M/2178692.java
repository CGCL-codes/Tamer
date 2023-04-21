package edu.mobbuzz.page.menu.rsschannel;

import com.sun.lwuit.Command;
import com.sun.lwuit.Component;
import com.sun.lwuit.Container;
import com.sun.lwuit.Font;
import com.sun.lwuit.Form;
import com.sun.lwuit.Label;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.layouts.BorderLayout;
import com.sun.lwuit.layouts.BoxLayout;
import edu.mobbuzz.bean.RSSChannel;
import edu.mobbuzz.facade.RSSChannelFacade;
import edu.mobbuzz.page.menu.Menu;
import edu.mobbuzz.util.string.MenuString;

public class RSSChannelDetail extends Menu {

    private Form rssChannelForm;

    private Container rssChannelPanel;

    private Label menuTitle;

    private Label titleLbl, sourceLbl;

    private Label detailTitleLbl, detailSourceLbl;

    private Command backCmd;

    private RSSChannel rssChannel;

    public RSSChannelDetail() {
        rssChannelForm = new Form();
        rssChannelPanel = new Container();
        menuTitle = new Label(getMenuTitle());
        titleLbl = new Label();
        sourceLbl = new Label();
        detailTitleLbl = new Label();
        detailSourceLbl = new Label();
        backCmd = new Command("Back") {

            public void actionPerformed(ActionEvent evt) {
                backToRSSChannelPage();
            }
        };
    }

    public void showPage() {
        menuTitle.setAlignment(Component.CENTER);
        menuTitle.getStyle().setMargin(5, 0, 5, 0);
        menuTitle.getStyle().setFont(Font.createSystemFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_SMALL));
        menuTitle.getStyle().setBgTransparency(0);
        titleLbl.setText("Title :");
        titleLbl.getStyle().setMargin(5, 0, 10, 2);
        titleLbl.getStyle().setFont(Font.createSystemFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_SMALL));
        titleLbl.getStyle().setBgTransparency(0);
        detailTitleLbl.getStyle().setMargin(2, 0, 10, 10);
        detailTitleLbl.setText(getRssChannel().getTitle());
        sourceLbl.setText("URL :");
        sourceLbl.getStyle().setMargin(5, 0, 10, 2);
        sourceLbl.getStyle().setFont(Font.createSystemFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_SMALL));
        sourceLbl.getStyle().setBgTransparency(0);
        detailSourceLbl.getStyle().setMargin(2, 0, 10, 10);
        detailSourceLbl.setText(getRssChannel().getSource());
        rssChannelPanel.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        rssChannelPanel.addComponent(menuTitle);
        rssChannelPanel.addComponent(titleLbl);
        rssChannelPanel.addComponent(detailTitleLbl);
        rssChannelPanel.addComponent(sourceLbl);
        rssChannelPanel.addComponent(detailSourceLbl);
        rssChannelForm.setLayout(new BorderLayout());
        rssChannelForm.addComponent(BorderLayout.CENTER, rssChannelPanel);
        rssChannelForm.addCommand(backCmd);
        rssChannelForm.setCommandListener(getActionlistener());
        rssChannelForm.show();
    }

    public void backToRSSChannelPage() {
        RSSChannelPage rssChannelPage = new RSSChannelPage();
        rssChannelPage.setExitCmd(getExitCmd());
        rssChannelPage.setActionlistener(getActionlistener());
        rssChannelPage.showPage();
    }

    public RSSChannel getRssChannel() {
        return rssChannel;
    }

    public void setRssChannel(RSSChannel rssChannel) {
        this.rssChannel = rssChannel;
    }

    public String getMenuTitle() {
        return MenuString.RSSCHANNEL;
    }
}
