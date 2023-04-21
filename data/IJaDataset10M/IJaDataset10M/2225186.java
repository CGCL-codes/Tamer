package multimanipulators.converters;

import java.io.OutputStreamWriter;
import multimanipulators.interfaces.Converters;

public class Converter_replaceString implements Converters {

    private String replacewith;

    public Converter_replaceString(String replacewith) {
        this.replacewith = replacewith;
    }

    public String getname() {
        return Converter_replaceString.class.getName();
    }

    public void manipulate(char[] buffer, OutputStreamWriter doutstream) throws Exception {
        doutstream.write(replacewith);
    }
}
