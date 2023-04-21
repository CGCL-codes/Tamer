package Info.util.impl;

import Info.util.ElementValue;
import java.util.ArrayList;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.hpsf.PropertySetFactory;
import org.apache.poi.hpsf.SummaryInformation;
import org.apache.poi.poifs.eventfilesystem.POIFSReader;
import org.apache.poi.poifs.eventfilesystem.POIFSReaderEvent;
import org.apache.poi.poifs.eventfilesystem.POIFSReaderListener;
import services.OwlManager;

/**
 *
 * @author wassim
 */
public class MiscImpl implements CalculateImplementor {

    private ArrayList<ElementValue> elements = new ArrayList<ElementValue>();

    private File file;

    public MiscImpl(File filename) {
        this.file = filename;
    }

    public void calculateImpl() {
        try {
            POIFSReader r = new POIFSReader();
            MyPOIFSReaderListener Bijo = new MyPOIFSReaderListener();
            r.registerListener(Bijo, "\005SummaryInformation");
            r.read(new FileInputStream(file));
            elements.addAll(Bijo.getElements());
        } catch (Exception e) {
            Logger.getLogger(MiscImpl.class.getName()).log(Level.SEVERE, null, e);
            elements.add(new ElementValue(OwlManager.extensionProperty, file.getName().substring(file.getName().indexOf('.') + 1)));
            elements.add(new ElementValue(OwlManager.sizeProperty, Long.toString(file.length() / 1024)));
        }
    }

    public ArrayList<ElementValue> getElementsImpl() {
        calculateImpl();
        return elements;
    }
}

class MyPOIFSReaderListener implements POIFSReaderListener {

    private ArrayList<ElementValue> elements = new ArrayList<ElementValue>();

    public void processPOIFSReaderEvent(POIFSReaderEvent event) {
        SummaryInformation si = null;
        try {
            si = (SummaryInformation) PropertySetFactory.create(event.getStream());
        } catch (Exception ex) {
            throw new RuntimeException("Property set stream \"" + event.getPath() + event.getName() + "\": " + ex);
        }
        elements.add(new ElementValue(OwlManager.titleProperty, si.getTitle()));
        elements.add(new ElementValue(OwlManager.documentAuthorProperty, si.getAuthor()));
        elements.add(new ElementValue(OwlManager.commentsProperty, si.getComments()));
        elements.add(new ElementValue(OwlManager.keywordsProperty, si.getKeywords()));
        elements.add(new ElementValue(OwlManager.numberOfPagesProperty, Integer.toString(si.getPageCount())));
        elements.add(new ElementValue(OwlManager.dateProperty, si.getCreateDateTime().toString()));
    }

    public ArrayList<ElementValue> getElements() {
        return elements;
    }
}
