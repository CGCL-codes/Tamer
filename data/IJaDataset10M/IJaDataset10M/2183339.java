package gov.nasa.gsfc.visbard.util;

import gov.nasa.gsfc.visbard.model.VisbardMain;
import gov.nasa.gsfc.visbard.repository.resource.ResourceInfoFactory;
import javax.swing.JFileChooser;

public class VisbardFileChooser extends JFileChooser {

    public VisbardFileChooser(boolean load) {
        super(VisbardMain.getDataDir());
        if (!(VisbardMain.getDataDir().isDirectory())) {
            System.out.println("default data directory is invalid");
        }
        if (load) {
            String ext[] = ResourceInfoFactory.getInstance().getRecognizableExtensions();
            ExtFileFilter all = new ExtFileFilter("All ViSBARD Files");
            for (int i = 0; i < ext.length; i++) {
                ExtFileFilter fltr = new ExtFileFilter("*." + ext[i]);
                all.addExt(ext[i]);
                fltr.addExt(ext[i]);
                addChoosableFileFilter(fltr);
            }
            addChoosableFileFilter(all);
            setFileFilter(all);
        }
        setMultiSelectionEnabled(true);
    }
}
