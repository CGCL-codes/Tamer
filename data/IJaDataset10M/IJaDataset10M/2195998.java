package examples;

import fr.esrf.tangoatk.widget.image.*;
import javax.swing.*;
import java.awt.event.*;
import fr.esrf.tangoatk.widget.attribute.ImageControlAdapter;
import fr.esrf.tangoatk.core.INumberImage;
import fr.esrf.tangoatk.core.AttributeList;
import fr.esrf.tangoatk.widget.util.ErrorHistory;
import fr.esrf.tangoatk.widget.util.ATKMenuBar;

public class Image extends JFrame {

    ImageViewer viewer;

    ImageControlAdapter adapter;

    INumberImage image;

    AttributeList list;

    IRasterConverter converter;

    ErrorHistory errorHistory;

    ATKMenuBar menu;

    public Image(String attribute) throws Exception {
        viewer = new ImageViewer();
        adapter = new ImageControlAdapter();
        list = new AttributeList();
        errorHistory = new ErrorHistory();
        menu = new ATKMenuBar();
        converter = new GrayscaleColorConverter(GrayscaleColorConverter._256_COLORS);
        list.addErrorListener(errorHistory);
        menu.setErrorHistory(errorHistory);
        menu.setQuitHandler(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        image = (INumberImage) list.add(attribute);
        adapter.setViewer(viewer);
        adapter.setModel(image);
        viewer.setRasterConverter(converter);
        viewer.addImageManipulator(new ConvolveFilter());
        list.startRefresher();
        setJMenuBar(menu);
        getContentPane().add(viewer);
        pack();
        show();
    }

    public static void main(String[] args) {
        try {
            new Image(args[0]);
        } catch (Exception e) {
            System.out.println("Couldn't create imageviewer: " + e.getMessage());
        }
    }
}
