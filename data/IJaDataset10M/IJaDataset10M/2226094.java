package br.furb.api.furbspeech.synth;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import br.furb.api.furbspeech.FurbSpeech;
import br.furb.api.furbspeech.util.ComponentUtils;
import br.furb.api.furbspeech.util.OSNotSupportedException;

/**
 * Represents the MBrolaSynthesizer. 
 * More informations about it: http://tcts.fpms.ac.be/synthesis/mbrola.html
 * @author Germano
 */
public class MBrolaSynthesizer implements Synthesizer {

    /**
	 * BR1 voice. Downloaded from Mbrola project web site.
	 */
    public static final File VOICE_BR1 = new File(ComponentUtils.getClearDirAbsolutePath(FurbSpeech.class.getClassLoader().getResource("synthesizer/mbrola/br1").getFile()));

    /**
	 * BR2 voice. Downloaded from Mbrola project web site.
	 */
    public static final File VOICE_BR2 = new File(ComponentUtils.getClearDirAbsolutePath(FurbSpeech.class.getClassLoader().getResource("synthesizer/mbrola/br2").getFile()));

    /**
	 * BR3 voice. Downloaded from Mbrola project web site.
	 */
    public static final File VOICE_BR3 = new File(ComponentUtils.getClearDirAbsolutePath(FurbSpeech.class.getClassLoader().getResource("synthesizer/mbrola/br3").getFile()));

    private File voiceName;

    public void setVoice(File voiceName) {
        this.voiceName = voiceName;
    }

    public void synthesize(File inputFile, File outputFile) {
        if (voiceName == null) {
            voiceName = VOICE_BR1;
        }
        String cmdLine = "<mbrola-bin> -e <voice> <input> <output>";
        cmdLine = cmdLine.replace("<mbrola-bin>", getPreparedFilePath(getMBrolaAppAbsolutePath())).replace("<voice>", getPreparedFilePath(voiceName.getAbsolutePath())).replace("<input>", getPreparedFilePath(inputFile.getAbsolutePath())).replace("<output>", getPreparedFilePath(outputFile.getAbsolutePath()));
        try {
            FurbSpeech.getLogger().debug("Executing command: " + cmdLine);
            Runtime.getRuntime().exec(cmdLine).waitFor();
        } catch (IOException e) {
            FurbSpeech.getLogger().fatal("I/O excetion in the external MBrola application.", e);
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            FurbSpeech.getLogger().fatal("Error while the current thread waits for the end of the mbrola applation process.", e);
            throw new RuntimeException(e);
        }
    }

    private String getPreparedFilePath(String filePath) {
        String osName = (String) System.getProperties().get("os.name");
        if (osName.toLowerCase().indexOf("windows") > -1) {
            return "\"".concat(filePath).concat("\"");
        } else {
            return filePath.replaceAll(" ", "\\ ");
        }
    }

    private String getMBrolaAppAbsolutePath() {
        String bin = null;
        String osName = (String) System.getProperties().get("os.name");
        if (osName.toLowerCase().indexOf("windows") > -1) {
            bin = "mbrola.exe";
        } else if (osName.toLowerCase().indexOf("linux") > -1) {
            bin = "mbrola-linux-i386";
        } else if (osName.toLowerCase().indexOf("mac") > -1) {
            bin = "mbrola-darwin-ppc";
        }
        if (bin != null) {
            URL mbrolaBin = MBrolaSynthesizer.class.getClassLoader().getResource("synthesizer/mbrola/" + bin);
            return new File(ComponentUtils.getClearDirAbsolutePath(mbrolaBin.getFile())).getAbsolutePath();
        } else {
            throw new OSNotSupportedException();
        }
    }

    public File getVoice() {
        return voiceName;
    }
}
