package freestyleLearningGroup.independent.gui;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import freestyleLearningGroup.independent.util.FLGInternationalization;

/**
 * FLGMediaFileChooser.
 */
public class FLGMediaFileChooser extends JFileChooser {

    public static final int PICTURE = 0;

    public static final int AUDIO = 1;

    public static final int VIDEO = 2;

    public static final int PDF = 3;

    public static final int PPT = 4;

    public static final int EXTERNAL = 5;

    public static final int XML = 6;

    public static final int OPEN_OFFICE_STARWRITER = 7;

    private FLGInternationalization internationalization;

    public static final int FOLDER = 8;

    public FLGMediaFileChooser(int mediaType) {
        internationalization = new FLGInternationalization("freestyleLearningGroup.independent.gui.internationalization", getClass().getClassLoader());
        switch(mediaType) {
            case PICTURE:
                {
                    setFileFilter(new PictureFileFilter());
                    break;
                }
            case VIDEO:
                {
                    setFileFilter(new VideoFileFilter());
                    break;
                }
            case AUDIO:
                {
                    setFileFilter(new AudioFileFilter());
                    break;
                }
            case PDF:
                {
                    setFileFilter(new PdfFileFilter());
                    break;
                }
            case PPT:
                {
                    setFileFilter(new PowerpointFileFilter());
                    break;
                }
            case XML:
                {
                    setFileFilter(new XMLFileFilter());
                    break;
                }
            case OPEN_OFFICE_STARWRITER:
                {
                    setFileFilter(new StarWriterFileFilter());
                    break;
                }
            case FOLDER:
                {
                    setDialogTitle(internationalization.getString("dialog.title.folderImport"));
                    setFileFilter(new FolderFileFilter());
                    setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    break;
                }
            default:
                {
                    setFileFilter(new ExternalFileFilter());
                }
        }
    }

    public boolean showDialog() {
        int returnValue = showOpenDialog(FLGUIUtilities.getMainFrame());
        return returnValue == APPROVE_OPTION;
    }

    public boolean showDialog(File currentDirectory) {
        if (currentDirectory != null) this.setCurrentDirectory(currentDirectory);
        int returnValue = showOpenDialog(FLGUIUtilities.getMainFrame());
        return returnValue == APPROVE_OPTION;
    }

    public String getFileExtension() {
        String fileExtension = null;
        File f = getSelectedFile();
        if (f != null) {
            String fileName = f.getName().toLowerCase();
            fileExtension = fileName.substring(fileName.lastIndexOf("."));
        }
        return fileExtension;
    }

    class FolderFileFilter extends FileFilter {

        public boolean accept(File f) {
            if (f.isDirectory()) return true; else return false;
        }

        public String getDescription() {
            return internationalization.getString("fileFilter.folder.description");
        }
    }

    class PictureFileFilter extends FileFilter {

        public boolean accept(File f) {
            if (f.isDirectory()) return true; else {
                String fileName = f.getName().toLowerCase();
                return fileName.endsWith(".png") || fileName.endsWith(".jpg") || fileName.endsWith(".gif");
            }
        }

        public String getDescription() {
            return internationalization.getString("fileFilter.picture.description");
        }
    }

    class VideoFileFilter extends FileFilter {

        public boolean accept(File f) {
            if (f.isDirectory()) return true; else {
                String fileName = f.getName().toLowerCase();
                return (fileName.endsWith(".mov") || fileName.endsWith(".mpeg") || fileName.endsWith(".mpg"));
            }
        }

        public String getDescription() {
            return internationalization.getString("fileFilter.video.description");
        }
    }

    class AudioFileFilter extends FileFilter {

        public boolean accept(File f) {
            if (f.isDirectory()) return true; else {
                String fileName = f.getName().toLowerCase();
                return fileName.endsWith(".wav") || fileName.endsWith(".mp2") || fileName.endsWith(".mp3") || fileName.endsWith(".midi");
            }
        }

        public String getDescription() {
            return internationalization.getString("fileFilter.audio.description");
        }
    }

    class PdfFileFilter extends FileFilter {

        public boolean accept(File f) {
            if (f.isDirectory()) return true; else {
                String fileName = f.getName().toLowerCase();
                return fileName.endsWith(".pdf");
            }
        }

        public String getDescription() {
            return internationalization.getString("fileFilter.pdf.description");
        }
    }

    class PowerpointFileFilter extends FileFilter {

        public boolean accept(File f) {
            if (f.isDirectory()) return true; else {
                String fileName = f.getName().toLowerCase();
                return (fileName.endsWith(".ppt") || fileName.endsWith(".pps"));
            }
        }

        public String getDescription() {
            return internationalization.getString("fileFilter.ppt.description");
        }
    }

    class XMLFileFilter extends FileFilter {

        public boolean accept(File f) {
            if (f.isDirectory()) return true; else {
                String fileName = f.getName().toLowerCase();
                return (fileName.endsWith(".xml"));
            }
        }

        public String getDescription() {
            return internationalization.getString("fileFilter.xml.description");
        }
    }

    class StarWriterFileFilter extends FileFilter {

        public boolean accept(File f) {
            if (f.isDirectory()) return true; else {
                String fileName = f.getName().toLowerCase();
                return (fileName.endsWith(".sxw"));
            }
        }

        public String getDescription() {
            return internationalization.getString("fileFilter.sxw.description");
        }
    }

    class ExternalFileFilter extends FileFilter {

        public boolean accept(File f) {
            return true;
        }

        public String getDescription() {
            return internationalization.getString("fileFilter.external.description");
        }
    }
}
