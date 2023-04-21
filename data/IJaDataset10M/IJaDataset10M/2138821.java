package net.sourceforge.freejava.type.parser;

import java.io.File;
import java.util.zip.ZipFile;
import net.sourceforge.freejava.type.traits.AbstractParser;
import net.sourceforge.freejava.util.exception.ParseException;

public class ZipFileParser extends AbstractParser<ZipFile> {

    @Override
    public ZipFile parse(String path) throws ParseException {
        File cwd = SystemContextFactory.getSystemContext().getWorkingDirectoryContext().getWorkingDirectory();
        File file = new File(cwd, path);
        try {
            return new ZipFile(file);
        } catch (Exception e) {
            throw new ParseException(e);
        }
    }
}
