package javab.print;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.io.File;
import java.util.ArrayList;
import javab.bling.BMenuItem;
import javab.bling.BGUI.BGUI;
import javab.ootil.Ootil;
import javab.ootil.config.LookAndFeelConfigurationOptions;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * @author Brett Geren
 *
 */
public class PrintTester extends BGUI {

    private JComponent middle;

    private JList list;

    private int pageWidth, pageHeight;

    public static final File ICON_LOCATION = new File("Images/PrintTester.ico");

    public static final BufferedImage ICON = Ootil.createImage("PrintTester.png");

    public static final String TITLE = "Print Tester";

    public static final String LICENSE_NAME = "openBSD";

    public static final int VERSION_MAJOR = 1;

    public static final int VERSION_MINOR = 0;

    public static final int VERSION_REVISION = 0;

    public static final boolean IS_PRE_RELEASE_VERSION = true;

    public static final String PRE_RELEASE_ID = "alpha";

    public PrintTester(PrintTestable p) {
        setLayout(new BorderLayout());
        ArrayList<ImageIcon> icons = makePages(p);
        if (icons.size() > 0) {
            pageWidth = icons.get(0).getIconWidth();
            pageHeight = icons.get(0).getIconWidth();
        }
        makeToolBar();
        add(makeWest(icons), BorderLayout.WEST);
        setMain(null);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private ArrayList<ImageIcon> makePages(PrintTestable p) {
        PageFormat pf = p.getTestPageFormat();
        ArrayList<ImageIcon> images = new ArrayList<ImageIcon>();
        int ret = Printable.PAGE_EXISTS;
        for (int page = 0; ret == Printable.PAGE_EXISTS; page++) {
            BufferedImage img = new BufferedImage((int) pf.getImageableWidth(), (int) pf.getImageableHeight(), BufferedImage.TYPE_4BYTE_ABGR);
            try {
                ret = p.getPrintable().print(img.createGraphics(), pf, page);
            } catch (PrinterException e) {
                e.printStackTrace();
            }
            images.add(new ImageIcon(img));
        }
        return images;
    }

    private void makeToolBar() {
        JMenuBar bar = new JMenuBar();
        setJMenuBar(bar);
        JMenu file = new JMenu("File");
        file.setMnemonic(KeyEvent.VK_F);
        bar.add(file);
        JMenuItem exit = new BMenuItem("Exit", KeyEvent.VK_E, KeyEvent.VK_F4, ActionEvent.ALT_MASK, this);
        file.add(exit);
        JMenu options = new JMenu("Options");
        options.setMnemonic(KeyEvent.VK_O);
        bar.add(options);
        options.add(makeLookAndFeelMenu());
        JMenu helpMenu = new JMenu("Help");
        helpMenu.setMnemonic(KeyEvent.VK_H);
        bar.add(helpMenu);
        JMenuItem help = new BMenuItem("Help", KeyEvent.VK_H, KeyEvent.VK_F1, 0, this);
        JMenuItem about = new BMenuItem("About", KeyEvent.VK_A, KeyEvent.VK_F9, 0, this);
        helpMenu.add(help);
        helpMenu.add(about);
    }

    private JComponent makeWest(ArrayList<ImageIcon> icons) {
        Page[] pages = new Page[icons.size()];
        for (int i = 0; i < icons.size(); i++) {
            ImageIcon icon = icons.get(i);
            pages[i] = new Page(icon, i);
        }
        list = new JList(pages);
        list.setVisibleRowCount(-1);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setLayoutOrientation(JList.VERTICAL);
        list.addListSelectionListener(new SelectionListener());
        return new JScrollPane(list);
    }

    private void setMain(JComponent main) {
        if (main == null) {
            main = new JPanel() {

                public Dimension getMinimumSize() {
                    return getPreferredSize();
                }

                public Dimension getPreferredSize() {
                    return new Dimension(pageWidth, pageHeight);
                }
            };
        }
        if (middle != null) {
            remove(middle);
        }
        middle = main;
        add(middle, BorderLayout.CENTER);
        validate();
        repaint();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("exit")) exit(); else super.actionPerformed(e);
    }

    public void exit() {
        super.exit();
        System.exit(0);
    }

    public String getLicenseInformation() {
        return "<br> Copyright (c) 2009, Brett Geren" + "<br>All rights reserved." + "<br>" + "<br>Redistribution and use in source and binary forms, " + "<br>with or without modification, are permitted " + "<br>provided that the following conditions are met:" + "<br>" + "<ul>" + "<br>   <li> Redistributions of source code must retain " + "<br>	     the above copyright notice, " + "<br>        this list of conditions and the following " + "<br>        disclaimer." + "<br>   <li> Redistributions in binary form " + "<br>        must reproduce the above copyright notice, " + "<br>        this list of conditions and the following " + "<br>        disclaimer in the documentation " + "<br>        and/or other materials provided with " + "<br>        the distribution." + "<br>   <li> Neither the name of the Brett Geren nor the " + "<br>        names of its contributors may be " + "<br>        used to endorse or promote products derived " + "<br>        from this software without " + "<br>        specific prior written permission." + "</ul>" + "<br>" + "<br>THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT " + "<br>HOLDERS AND CONTRIBUTORS \"AS IS\" AND ANY " + "<br>EXPRESS OR IMPLIED WARRANTIES, INCLUDING, " + "<br>BUT NOT LIMITED TO, THE IMPLIED WARRANTIES " + "<br>OF MERCHANTABILITY AND FITNESS FOR A " + "<br>PARTICULAR PURPOSE ARE DISCLAIMED. IN NO " + "<br>EVENT SHALL THE COPYRIGHT HOLDER OR " + "<br>CONTRIBUTORS BE LIABLE FOR ANY DIRECT, " + "<br>INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR " + "<br>CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT " + "<br>LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS " + "<br>OR SERVICES; LOSS OF USE, DATA, OR PROFITS; " + "<br>OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND " + "<br>ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, " + "<br>STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE " + "<br>OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE " + "<br>OF THIS SOFTWARE, EVEN IF ADVISED OF THE " + "<br>POSSIBILITY OF SUCH DAMAGE.";
    }

    public BufferedImage getIcon() {
        return ICON;
    }

    public String getProgramTitle() {
        return TITLE;
    }

    public LookAndFeelConfigurationOptions getConfigOptions() {
        return null;
    }

    public void help() {
        showFeatureNotAddedMessage(this);
    }

    public int getVersionMajor() {
        return VERSION_MAJOR;
    }

    public int getVersionMinor() {
        return VERSION_MINOR;
    }

    public int getVersionRevision() {
        return VERSION_REVISION;
    }

    public boolean isPreRelease() {
        return IS_PRE_RELEASE_VERSION;
    }

    public String getPreReleaseID() {
        if (!isPreRelease()) {
            return null;
        } else {
            return PRE_RELEASE_ID;
        }
    }

    public String getBuildNumber() {
        return null;
    }

    public boolean supportsBuildNumber() {
        return false;
    }

    private class SelectionListener implements ListSelectionListener {

        public void valueChanged(ListSelectionEvent e) {
            Page p = (Page) list.getSelectedValue();
            setMain(p);
        }
    }
}
