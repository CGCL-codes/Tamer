package net.sf.fileexchange.api.snapshot;

import java.io.File;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public class XmlFileAdapter extends XmlAdapter<String, File> {

    public XmlFileAdapter() {
    }

    @Override
    public String marshal(File file) throws Exception {
        return file == null ? null : file.getPath();
    }

    ;

    @Override
    public File unmarshal(String v) throws Exception {
        return v == null ? null : new File(v);
    }

    ;
}
