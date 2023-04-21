package intellibitz.sted.actions;

import intellibitz.sted.fontmap.Converter;
import intellibitz.sted.ui.STEDWindow;
import intellibitz.sted.ui.TabDesktop;
import intellibitz.sted.util.MenuHandler;
import intellibitz.sted.util.Resources;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.event.TableModelEvent;
import javax.swing.table.TableModel;
import java.awt.event.ActionEvent;
import java.io.File;

public class TransliterateAction extends TableModelListenerAction {

    public TransliterateAction() {
        super();
    }

    /**
     * This fine grain notification tells listeners the exact range of cells,
     * rows, or columns that changed.
     */
    public void tableChanged(TableModelEvent e) {
        setEnabled(((TableModel) e.getSource()).getRowCount() > 0 && getSTEDWindow().getDesktop().getFontMapperDesktopFrame().enableConverterIfFilesLoaded());
    }

    public void actionPerformed(ActionEvent e) {
        final STEDWindow stedWindow = getSTEDWindow();
        final File fileToConvert = stedWindow.getDesktop().getDesktopModel().getInputFile();
        final File convertedFile = stedWindow.getDesktop().getDesktopModel().getOutputFile();
        if (fileToConvert == null || convertedFile == null) {
            fireMessagePosted("Select valid files for both input and output");
            return;
        }
        if (fileToConvert.getName().equals(convertedFile.getName())) {
            fireMessagePosted("Input and Output files are same.. select different files");
            return;
        }
        fireStatusPosted("Begin Converting...");
        final Converter converter = getConverter(stedWindow.getDesktop());
        converter.start();
        setEnabled(false);
    }

    public Converter getConverter(TabDesktop desktop) {
        final Converter converter = new Converter(desktop.getFontMap(), desktop.getDesktopModel().getInputFile(), desktop.getDesktopModel().getOutputFile());
        final JCheckBoxMenuItem preserve = (JCheckBoxMenuItem) MenuHandler.getInstance().getMenuItem(Resources.ACTION_PRESERVE_TAGS);
        converter.setHTMLAware(preserve.isSelected());
        final JCheckBoxMenuItem reverse = (JCheckBoxMenuItem) MenuHandler.getInstance().getMenuItem(Resources.ACTION_TRANSLITERATE_REVERSE);
        converter.setReverseTransliterate(reverse.isSelected());
        converter.addThreadListener(desktop);
        final TransliterateStopAction stop = (TransliterateStopAction) MenuHandler.getInstance().getAction(Resources.ACTION_STOP_NAME);
        stop.setConverter(converter);
        stop.setEnabled(true);
        return converter;
    }
}
