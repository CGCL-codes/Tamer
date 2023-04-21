package dblp.social.preparser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import dblp.social.exceptions.FileExistsException;
import dblp.social.utility.Glitterizer;

/**
 * This class defines a structure used to memorize the pre-parse execution output in an xml file.
 * It keeps track of: the source xml file and all the generated _part files. 
 * For each _part file its length and the number of inserted checkpoints is stored.
 * The fields last-checkpoint and last-offset are also initialized.
 * 
 * @author Staffiero
 *
 */
public class XMLPartsInfo {

    private File inputFile = null;

    private ArrayList<PartInfo> partFiles = null;

    private String subfolder = null;

    private int chckptStep = 0;

    public static final String INFO_FILENAME = "_partsInfo.xml";

    public static final String INFO_FILESUBFOLDER = "partsInfo";

    /**
	 * The constructor to be used.
	 * @param inputFile: a File object which points the XML file to be pre-parsed
	 * 
	 */
    public XMLPartsInfo(File input, String subfolder, int step) {
        this.inputFile = input;
        this.chckptStep = step;
        this.subfolder = subfolder;
        this.partFiles = new ArrayList<PartInfo>();
    }

    /**
	 * Getter
	 * @return the input file pointer
	 */
    public File getInputFile() {
        return inputFile;
    }

    /**
	 * Setter, sets the input file pointer
	 * @param inputFile
	 */
    public void setInputFile(File inputFile) {
        this.inputFile = inputFile;
    }

    /**
	 * Getter
	 * @return the part files list
	 */
    public ArrayList<PartInfo> getPartFiles() {
        return partFiles;
    }

    /**
	 * Setter, sets the part files list
	 * @param partFiles
	 */
    public void setPartFiles(ArrayList<PartInfo> partFiles) {
        this.partFiles = partFiles;
    }

    /**
	 * Adds informations of a generated _part file to the current list.
	 * It is supposed to be called after the _part file has been closed, in order to receive the right informations about its lenght and number of checkpoints
	 * @param filename: the _part file name
	 * @param checkpoints: the number of checkpoints inserted into the _part file
	 * @param length: the _part file length
	 */
    public void appendPartFile(String filename, long checkpoints, long length) {
        PartInfo pi = new PartInfo(filename, checkpoints, length);
        this.getPartFiles().add(pi);
    }

    /**
	 * Saves the gathered informations to an xml file. 
	 * This xml file should be used to know exactly which _part files are supposed to be sent to the parser, given a source xml file.
	 */
    public String savePartFileInfo() throws FileExistsException, IOException {
        File dir;
        if (this.inputFile.getParent() != null) {
            dir = new File(this.inputFile.getParent() + "/" + subfolder + "/" + INFO_FILESUBFOLDER);
        } else {
            dir = new File(subfolder + "/" + INFO_FILESUBFOLDER);
        }
        if (!dir.exists() || !dir.isDirectory()) dir.mkdirs();
        File file = new File(dir.getAbsolutePath() + "/" + (this.inputFile.getName().substring(0, this.inputFile.getName().lastIndexOf("."))) + INFO_FILENAME);
        if (file.exists()) throw new FileExistsException("The part file descriptor: " + file.getName() + " already exists"); else file.createNewFile();
        FileWriter output = new FileWriter(file);
        StringBuffer buffer = new StringBuffer();
        long totalChpts = 0;
        buffer.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>" + "\n<xml-parts-info>\n" + "\t<source-file>\n" + "\t\t<path>" + this.inputFile.getAbsolutePath() + "</path>\n" + "\t\t<length>" + Glitterizer.clearFormatLenght(this.inputFile.length()) + "</length>\n" + "\t</source-file>\n");
        for (PartInfo i : this.partFiles) {
            totalChpts = totalChpts + i.getCheckpoints();
            buffer.append("\t<part-file>\n");
            buffer.append("\t\t<path>" + i.getFilePath() + "</path>\n");
            buffer.append("\t\t<checkpoints>" + i.getCheckpoints() + "</checkpoints>\n");
            buffer.append("\t\t<last-checkpoint>0</last-checkpoint>\n");
            buffer.append("\t\t<last-offset>0</last-offset>\n");
            buffer.append("\t\t<complete>false</complete>\n");
            buffer.append("\t\t<length>" + Glitterizer.clearFormatLenght(i.getLength()) + "</length>\n");
            buffer.append("\t</part-file>\n");
        }
        buffer.append("\t<total-checkpoints>" + totalChpts + "</total-checkpoints>\n");
        buffer.append("\t<checkpoints-step>" + this.chckptStep + "</checkpoints-step>\n");
        buffer.append("</xml-parts-info>");
        output.write(buffer.toString());
        output.close();
        return file.getAbsolutePath();
    }
}
