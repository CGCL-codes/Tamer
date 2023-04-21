package generate.java.verbs;

import generate.CBaseLanguageExporter;
import semantic.Verbs.CEntityOpenFile;
import utils.CObjectCatalog;

public class CJavaOpenFile extends CEntityOpenFile {

    public CJavaOpenFile(int line, CObjectCatalog cat, CBaseLanguageExporter out) {
        super(line, cat, out);
    }

    @Override
    protected void DoExport() {
        String cs;
        cs = m_eFileDescriptor.ExportReference(getLine());
        switch(m_eMode) {
            case APPEND:
                cs = "openExtend(" + cs + ") ;";
                break;
            case INPUT:
                cs = "openInput(" + cs + ") ;";
                break;
            case INPUT_OUTPUT:
                cs = "openInputOutput()(" + cs + ") ;";
                break;
            case OUTPUT:
                cs = "openOutput(" + cs + ") ;";
                break;
            default:
                cs = "open(" + cs + ") ;";
                break;
        }
        WriteLine(cs);
    }
}
