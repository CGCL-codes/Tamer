package net.sf.josceleton.prototype.midi.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Properties;
import javax.swing.JOptionPane;
import net.pulseproject.commons.util.StringUtil;
import net.sf.josceleton.core.api.entity.joint.Joint;
import net.sf.josceleton.core.api.entity.joint.Joints;
import net.sf.josceleton.prototype.console.util.CloseableUtil;
import net.sf.josceleton.prototype.midi.logic.MidiMapping;

public class SomeUtil {

    public static Properties loadPropertiesFromClassPath(final ClassLoader loader, final String fileName) {
        final Properties properties = new Properties();
        InputStream inputStream = null;
        try {
            inputStream = loader.getResourceAsStream(fileName);
            if (inputStream == null) {
                throw new RuntimeException("Could not get resource [" + fileName + "] as stream!");
            }
            properties.load(inputStream);
            return properties;
        } catch (IOException e) {
            throw new RuntimeException("Could not load properties file [" + fileName + "]!", e);
        } finally {
            CloseableUtil.close(inputStream);
        }
    }

    public static void handleException(Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, e.getMessage(), "Josceleton Midi Route Error", JOptionPane.ERROR_MESSAGE);
        LogUtil.log("Stack trace:\n" + StringUtil.exceptionToString(e));
        LogUtil.log("Error: " + e.getMessage());
    }

    public static String fillString(String in, int len) {
        if (in.length() >= len) {
            return in;
        }
        return in + "        ".substring(0, (len - in.length()));
    }

    public static Joint jointByOsceletonId(String rawJointName) {
        for (Joint joint : Joints.values()) {
            if (joint.getOsceletonId().equals(rawJointName)) {
                return joint;
            }
        }
        throw new RuntimeException("unkown body part: " + rawJointName);
    }

    public static String toString(final Collection<MidiMapping> maps) {
        StringBuilder sb = new StringBuilder();
        sb.append("JointMidiMappings [\n");
        int i = 1;
        for (final MidiMapping map : maps) {
            if (i != 1) sb.append("\n");
            sb.append("  ").append(i).append(". ").append(map);
            i++;
        }
        sb.append("\n]");
        return sb.toString();
    }
}
