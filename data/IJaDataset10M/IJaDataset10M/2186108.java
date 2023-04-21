package br.com.maniasim.extractor.utility;

import br.com.maniasim.extractor.dbpf2.NamedTgi;
import br.com.maniasim.extractor.dbpf2.NamedTgi64;
import com.lepidllama.packageeditor.core.ConfigManager;
import com.lepidllama.packageeditor.dbpf.Header;
import com.lepidllama.packageeditor.dbpf.Tgi;

public class StringUtils extends com.lepidllama.packageeditor.utility.StringUtils {

    /**
	 * Versão modificada para incluir o nome do arquivo dado em {@link NamedTgi64}.
	 * @param tgi TGI do arquivo.
	 * @return Nome completo do arquivo.
	 */
    public static String getFilenamefromTgi(Tgi tgi) {
        return getFilenamefromTgi(tgi, "S3");
    }

    public static String getFilenamefromTgi(Tgi tgi, String preffix) {
        StringBuilder filename = new StringBuilder(preffix + "_" + printHex8(tgi.getType()) + "_" + printHex8(tgi.getGroup()) + "_" + printHex8(tgi.getInstanceHigh()) + printHex8(tgi.getInstance()));
        if (tgi instanceof NamedTgi) {
            String name = ((NamedTgi) tgi).getName();
            if (name != null && name.length() > 0) {
                filename.append("_" + name);
            }
        }
        filename.append("." + ConfigManager.getInstance().getShortName(tgi.getType()));
        return filename.toString();
    }

    public static String getFilenamefromTgi(Tgi tgi, Header header) {
        return getFilenamefromTgi(tgi, header.getVersionMajor() < 2 ? "S2" : "S3");
    }
}
