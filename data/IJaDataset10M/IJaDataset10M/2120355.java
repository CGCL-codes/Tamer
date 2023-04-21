package net.sf.opentranquera.integration.mf.format.transformers;

import java.util.Map;
import org.apache.commons.lang.StringUtils;

/**
 * Genera un String para filler usando los argumentos char y size para armarlo. El char por default es " "
 * @author Guillermo Meyer
 * @date 18/05/2005
 */
public class FillerTransformer implements FieldTransformer {

    public Object transform(Object src, Map args, Map valueHolder) {
        String chr = (String) args.get("char");
        if (chr == null || "".equals(chr)) {
            chr = " ";
        }
        String size = (String) args.get("size");
        return StringUtils.repeat(chr, Integer.parseInt(size));
    }
}
