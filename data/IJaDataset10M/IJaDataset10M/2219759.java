package ru.korusconsulting.connector.funambol;

import java.io.ByteArrayInputStream;
import java.util.TimeZone;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import com.funambol.common.pim.converter.ConverterException;
import com.funambol.common.pim.converter.NoteToSIFN;
import com.funambol.common.pim.note.Note;
import com.funambol.common.pim.sif.SIFNParser;

public class NoteUtils {

    public static Element asElement(Note n, DocumentFactory documentFactory, String namespace, boolean b) {
        Element note = documentFactory.createElement("note", namespace);
        return note;
    }

    public static Note asNote(Element noteEl) {
        return null;
    }

    public static byte[] convertTo(String type, String version, Note n, TimeZone timezone, String charset) throws ConverterException {
        byte[] content = null;
        if (PhoneDependedConverter.SIFN_TYPE.equals(type)) {
            NoteToSIFN conv = new NoteToSIFN(timezone, charset);
            content = conv.convert(n).getBytes();
        } else if (PhoneDependedConverter.VNOTE_TYPE.equals(type)) {
            throw new RuntimeException("Don't support at this time");
        } else {
            throw new ConverterException("Unsupported type:" + type);
        }
        return content;
    }

    public static Note convertFrom(String type, String version, byte[] content, TimeZone timezone, String charset) throws ConverterException {
        Note n = null;
        if (PhoneDependedConverter.SIFN_TYPE.equals(type)) {
            SIFNParser sifnParser;
            try {
                sifnParser = new SIFNParser(new ByteArrayInputStream(content));
                n = sifnParser.parse();
            } catch (Throwable e) {
                throw new ConverterException(e);
            }
        } else {
            throw new ConverterException("Unsupported type:" + type);
        }
        return n;
    }
}
