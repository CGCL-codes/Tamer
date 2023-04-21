package jdc.lib;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import org.apache.log4j.Logger;

/**
 * <p>Title: Java DirectConnect client and lib</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2001</p>
 * <p> </p>
 * @author unascribed
 * @version 1.0
 */
public class ConfigurationFile {

    /** get the logger for this package */
    protected static Logger libLogger = LoggerContainer.getLogger(ConfigurationFile.class);

    File _config_file;

    long _current_pos = 0;

    FileReader _reader;

    FileWriter _writer;

    public ConfigurationFile(String filename) {
        _config_file = new File(filename);
    }

    public boolean exists() {
        return (_config_file.canRead() && _config_file.exists());
    }

    public String read(String default_value) {
        if (_reader == null) {
            try {
                _reader = new FileReader(_config_file);
                _current_pos = 0;
            } catch (java.io.FileNotFoundException e) {
                libLogger.error("Exception: Config file create reader", e);
                return default_value;
            }
        }
        String ret_val = new String();
        int read = 0;
        while (_current_pos < _config_file.length() && read != '\n') {
            try {
                read = _reader.read();
                _current_pos++;
            } catch (java.io.IOException e) {
                libLogger.error("Exception: Config file reader", e);
                return default_value;
            }
            if (read != '\n') ret_val += (char) read;
        }
        if (ret_val.length() == 0) ret_val = default_value;
        return ret_val;
    }

    public short readShort(short default_value) {
        return Short.parseShort(read(Short.toString(default_value)));
    }

    public int readInt(int default_value) {
        return Integer.parseInt(read(Integer.toString(default_value)));
    }

    public byte readByte(byte default_value) {
        return Byte.parseByte(read(Byte.toString(default_value)));
    }

    public boolean readBoolean(boolean default_value) {
        String str = read((default_value ? "true" : "false"));
        return (str.compareTo("true") == 0);
    }

    public void write(String value) {
        if (_writer == null) {
            try {
                _writer = new FileWriter(_config_file);
            } catch (java.io.IOException e) {
                libLogger.error("Exception: Config file create writer", e);
            }
        }
        try {
            _writer.write(value + "\n");
            _writer.flush();
        } catch (java.io.IOException e) {
            libLogger.error("Exception: Config file writer", e);
        }
    }

    public void writeShort(short value) {
        write(Short.toString(value));
    }

    public void writeInt(int value) {
        write(Integer.toString(value));
    }

    public void writeByte(byte value) {
        write(Byte.toString(value));
    }

    public void writeBoolean(boolean value) {
        write((value ? "true" : "false"));
    }
}
