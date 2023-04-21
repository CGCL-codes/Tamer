package org.fudaa.fudaa.all;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JFrame;
import com.memoire.bu.BuInformationsDocument;
import com.memoire.fu.FuLib;
import org.fudaa.ebli.ressource.EbliResource;
import org.fudaa.ebli.volume.BCartouche;

/**
 * @version $Revision: 1.3 $ $Date: 2006-09-22 15:47:11 $ by $Author: deniger $
 * @author
 */
public final class TestCartouche {

    private TestCartouche() {
    }

    public static void main(final String[] _args) {
        final MyFrame frame = new MyFrame();
        frame.getContentPane().setLayout(new FlowLayout());
        frame.setSize(new Dimension(512, 512));
        frame.setVisible(true);
    }
}

class MyFrame extends JFrame {

    BCartouche cartouche_;

    BuInformationsDocument info_;

    public MyFrame() {
        super();
        info_ = new BuInformationsDocument();
        info_.name = "Etude";
        info_.version = "0.01";
        info_.organization = "CETMEF";
        info_.author = System.getProperty("user.name");
        info_.contact = info_.author + "@cetmef.equipement.gouv.fr";
        info_.date = FuLib.date();
        info_.logo = EbliResource.EBLI.getIcon("minlogo.gif");
        cartouche_ = new BCartouche();
        cartouche_.setName("cqCARTOUCHE");
        cartouche_.setInformations(info_);
        cartouche_.setForeground(Color.black);
        cartouche_.setBackground(new Color(255, 255, 224));
        cartouche_.setFont(new Font("SansSerif", Font.PLAIN, 10));
        cartouche_.setLocation(100, 100);
    }

    public void paint(final Graphics _g) {
        super.paint(_g);
        cartouche_.paint(_g);
    }
}
