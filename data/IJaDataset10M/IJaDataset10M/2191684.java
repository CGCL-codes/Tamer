package tts;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author John Nabil
 */
public class MBROLA {

    private String m_speaker = null;

    public MBROLA(String speaker) {
        m_speaker = speaker;
    }

    public void generate() {
        Runtime rt = Runtime.getRuntime();
        try {
            String cmd = "";
            if (m_speaker.equals("us2")) {
                cmd = "mb0.bat";
            }
            if (m_speaker.equals("us1")) {
                cmd = "mb1.bat";
            }
            Process pr = rt.exec(cmd);
        } catch (IOException ex) {
            Logger.getLogger(MBROLA.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
