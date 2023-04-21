package edu.ucsd.ncmir.jinx.xml;

import edu.sdsc.grid.io.GeneralFile;
import edu.ucsd.ncmir.jinx.events.JxLogEvent;
import edu.ucsd.ncmir.spl.minixml.Element;

/**
 *
 * @author spl
 */
public abstract class JxXMLLoader {

    protected GeneralFile _file;

    protected Element _root;

    public JxXMLLoader(GeneralFile file, Element root) {
        this._file = file;
        this._root = root;
        String[] cname = this.getClass().getName().split("\\.");
        String[] version = cname[cname.length - 1].split("[A-Za-z]+");
        new JxLogEvent().send("Loading " + file.toString() + ": Jinx XML version " + version[1] + "." + version[2]);
    }

    public abstract boolean load();
}
