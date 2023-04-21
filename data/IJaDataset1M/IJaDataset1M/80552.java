package uk.co.pointofcare.echobase.objectstream.loaders;

import java.lang.annotation.Annotation;
import org.apache.log4j.Logger;

/**
 * @author RCHALLEN
 *
 */
public class ConfigBuilder {

    static Logger log = Logger.getLogger(ConfigBuilder.class);

    public void m() {
        ExcelSheetLoader loader = new ExcelSheetLoader();
        loader.load(annotBuild("this File"));
    }

    ExcelSourceConfig annotBuild(final String filename) {
        ExcelSourceConfig out = new ExcelSourceConfig() {

            @Override
            public String filename() {
                return filename;
            }

            @Override
            public boolean header() {
                return false;
            }

            @Override
            public String sheetname() {
                return null;
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return ExcelSourceConfig.class;
            }
        };
        return out;
    }
}
